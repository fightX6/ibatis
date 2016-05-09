package com.service;

public interface CURDService<T,S> {
	public void save(T t) throws Exception;
	public void update(T t) throws Exception;
	public void del(T t) throws Exception;
	public void delById(S s) throws Exception;
	public T findById(S s);
	public java.util.List<T> findByAll();
	public com.vo.PageBean<T> findByPageBean(Integer page,Integer pageSize,Object... params);
}
