package com.example.userservice.service;

import java.util.Set;

public interface BlackListService {

    void addToBlackList(Long userId,String reason);

    void removeFromBlacklist(Long userId);

    boolean isBlacklisted(Long userId);

    Set<String> getAllBlacklist();

}
