package com.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.Demo;

public interface DemoDao extends JpaRepository<Demo, Integer>{
	
}
