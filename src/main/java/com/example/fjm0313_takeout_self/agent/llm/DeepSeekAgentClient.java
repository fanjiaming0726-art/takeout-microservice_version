package com.example.fjm0313_takeout_self.agent.llm;

import com.example.fjm0313_takeout_self.agent.config.DeepSeekAgentProperties;
import com.example.fjm0313_takeout_self.agent.dto.AgentDecision;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class DeepSeekAgentClient {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DeepSeekAgentProperties properties;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public AgentDecision decide(ArrayNode messages){

        AgentDecision decision = new AgentDecision();

        if(!StringUtils.hasText(properties.getApikey())){
            decision.setType("final");
            decision.setReply("AI 配置缺少DeepSeek API KEY");
            return decision;
        }

        try {
            String requestBody = buildRequestBody(messages);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(properties.getBaseUrl()))
                    .header("Content-Type","application/json")
                    .header("Authorization","Bearer " + properties.getApikey())
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
            );

            if(response.statusCode() < 200 || response.statusCode()  >= 300){
                System.out.println("DeepSeek Agent调用失败，状态码：" + response.statusCode());
                System.out.println("响应内容：" + response.body());

                decision.setType("final");
                decision.setReply("AI 服务暂时不可用，请稍后再试");
                return decision;
            }
            return parseResponse(response.body());

        } catch (Exception e) {
            System.out.println("DeepSeek Agent 调用异常：" + e.getMessage());
            decision.setType("final");
            decision.setReply("AI 服务调用异常：" + e.getMessage());
            return decision;
        }
    }



    private String buildRequestBody(ArrayNode messages) throws JsonProcessingException {
            JsonNode root = objectMapper.createObjectNode()
                    .put("model",properties.getModel())
                    .put("temperature",0.2)
                    .put("max_tokens",500)
                    .set("messages",messages);

            return objectMapper.writeValueAsString(root);

    }



    private AgentDecision parseResponse(String responseBody) throws Exception {
        JsonNode root = objectMapper.readTree(responseBody);

        String content = root.path("choices").path(0).path("message").path("content").asText();

        if(!StringUtils.hasText(content)){
            AgentDecision agentDecision = new AgentDecision();
            agentDecision.setType("final");
            agentDecision.setReply("AI 没有返回有效内容");
            return agentDecision;
        }

        String jsonText = cleanJson(content);
        AgentDecision agentDecision = objectMapper.readValue(jsonText,AgentDecision.class);
        agentDecision.setRawContent(jsonText);

        if(!StringUtils.hasText(agentDecision.getType())){
            agentDecision.setType("final");
            agentDecision.setReply("我暂时没有理解你的意思");
        }

        return agentDecision;

    }


    private String cleanJson(String content){
        String text = content.trim();
        if(text.startsWith("```json")){
            text = text.substring(7);
        }
        if(text.startsWith("```")){
            text = text.substring(3);
        }
        if(text.endsWith("```")){
            text = text.substring(0,text.length() - 3);
        }

        return text.trim();

    }
}
