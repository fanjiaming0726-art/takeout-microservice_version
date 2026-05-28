package com.example.commonservice.context;

public class UserContext {

    private static final ThreadLocal<Long> currentUserId = new ThreadLocal<>();
    private static final ThreadLocal<Long> currentEmployeeId = new ThreadLocal<>();

    public static void setUserId(Long userId){
        currentUserId.set(userId);
    }

    public static Long getUserId(){
        return currentUserId.get();
    }

    public static void setEmployeeId(Long employeeId){
        currentEmployeeId.set(employeeId);
    }

    public static Long getEmployeeId(){
        return currentEmployeeId.get();
    }

    public static void clear(){
        currentUserId.remove();
        currentEmployeeId.remove();
    }
}
