package com.cusbee.kiosk.repository;

import com.cusbee.kiosk.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
	Users findByUsername(String arg0);

	Users findByPhoneNumber(String arg0);

	Users findById(Long arg0);

	Page<Users> findAll(Pageable arg0);
}