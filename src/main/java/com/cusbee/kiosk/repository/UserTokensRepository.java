package com.cusbee.kiosk.repository;

import com.cusbee.kiosk.entity.UserTokens;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokensRepository extends JpaRepository<UserTokens, Long> {
	UserTokens findByPasscode(String arg0);
}