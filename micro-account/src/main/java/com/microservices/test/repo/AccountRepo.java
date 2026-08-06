package com.microservices.test.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservices.test.entity.AccountEntity;

@Repository
public interface AccountRepo extends JpaRepository<AccountEntity, String>{

	Optional<AccountEntity> findByAccNo(String accNo);
	void deleteByEmployeeId(String id);
}
