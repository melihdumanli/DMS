package com.melihdumanli.dms.repository;

import com.melihdumanli.dms.model.UserActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Long> {
}
