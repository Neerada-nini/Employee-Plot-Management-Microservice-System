package com.microservices.test.service;

import java.util.List;

import com.microservices.test.ApiResponse;
import com.microservices.test.dto.AccountDTO;
import com.microservices.test.entity.AccountEntity;

public interface AccountService {

	ApiResponse<AccountEntity> saveAccount(AccountDTO accDTO);
	
	ApiResponse<List<AccountEntity>> getAllAccounts();
	

	ApiResponse<AccountEntity> getSingleAccount(String id);

	ApiResponse<Object> deleteByEmployeeId(String string);
}
