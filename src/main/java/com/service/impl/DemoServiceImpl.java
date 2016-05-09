package com.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dao.DemoDao;
import com.entity.Demo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mapper.xml.DemoMapper;
import com.service.DemoService;
import com.vo.PageBean;
@Service
public class DemoServiceImpl implements DemoService{
	@Resource
	private DemoDao demoDao;
	@Resource
	private DemoMapper demoMapper;
	
	public void save(Demo t) throws Exception {
		demoDao.save(t);
	} 

	public void update(Demo t) throws Exception {
		demoDao.save(t);
	}

	public void del(Demo t) throws Exception {
		demoDao.delete(t);
	}

	public void delById(Integer s) throws Exception {
		demoDao.delete(s);
	}

	public Demo findById(Integer s) {
		return demoDao.findOne(s);
	}

	public List<Demo> findByAll() {
		return demoDao.findAll();
	}

	public PageBean<Demo> findByPageBean(Integer page, Integer pageSize, Object... params) {
		
		return null;
	}
	
}
