package com.example.fjm0313_takeout_self.common.MQ.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;


import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SellerOrderWebSocketHandler implements WebSocketHandler {

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();


    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    //这句代码是在前端给后端发连接请求，连接完成后自动执行这步动作
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("商家端 WebSocket 连接成功，当前在线商家数量：" + sessions.size());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println("收到商家端 WebSocket 消息：" + message.getPayload());

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        sessions.remove(session);
        System.out.println("商家端 WebSocket 连接异常：" + exception.getMessage());

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessions.remove(session);
        System.out.println("商家端 WebSocket 连接关闭，当前在线商家数量：" + sessions.size());
    }

    @Override
    // 不支持分片消息，前端给后端发的消息一次完整发完
    public boolean supportsPartialMessages() {
        return false;
    }

    public void sendToALlSellers(Object data){
        try{
            // 将data对象转换成json字符串对象
            String json = objectMapper.writeValueAsString(data);

            // 这份通知消息就是以TextMessage发出去的
            TextMessage textMessage = new TextMessage(json);

            for(WebSocketSession session : sessions){
                if(session.isOpen()){
                    session.sendMessage(textMessage);
                }
            }
            System.out.println("已向商家端推送新订单消息：" + json);

        }catch (Exception e){
            System.out.println("推送商家端 WebSocket 消息失败：" + e.getMessage());
        }
    }
}
