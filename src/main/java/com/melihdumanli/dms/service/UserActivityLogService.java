package com.melihdumanli.dms.service;

import com.melihdumanli.dms.model.UserActivityLog;
import com.melihdumanli.dms.repository.UserActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserActivityLogService {
    private final UserActivityLogRepository logRepository;

    public void saveUserActivity(UserActivityLog userActivityLog) {
        logRepository.save(userActivityLog);
    }
}
