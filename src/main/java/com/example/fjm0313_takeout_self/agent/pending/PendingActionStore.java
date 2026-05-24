package com.example.fjm0313_takeout_self.agent.pending;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PendingActionStore {


    private final Map<Long, PendingAction> userLatestActionMap = new ConcurrentHashMap<>();

    public void save (PendingAction action){
        userLatestActionMap.put(action.getUserId(),action);
    }

    public PendingAction getLatestPendingAction(Long userId){
        return userLatestActionMap.get(userId);
    }

    public void removeLatestByUserId(Long userId){
        userLatestActionMap.remove(userId);
    }

}
