package com.example.fjm0313_takeout_self.agent.Tool;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ToolResult {

    private  Boolean success;

    private String message;

    private Map<String,Object> data;

    public static ToolResult success(String message,Map<String,Object> data){
        ToolResult result =  new ToolResult();
        result.setSuccess(true);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static ToolResult success(String message){
        return success(message,null);
    }

    public static ToolResult fail(String message){
        ToolResult result = new ToolResult();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }

}
