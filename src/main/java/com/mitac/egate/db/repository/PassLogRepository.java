package com.mitac.egate.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mitac.egate.db.model.PassLog;
import com.mitac.egate.db.model.PassLogKey;

public interface PassLogRepository extends JpaRepository<PassLog, PassLogKey> {
	
}
