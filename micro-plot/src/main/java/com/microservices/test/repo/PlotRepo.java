package com.microservices.test.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservices.test.entity.PlotEntity;

@Repository
public interface PlotRepo extends JpaRepository<PlotEntity, String>{

	void deleteByEmployeeId(String id);
}
