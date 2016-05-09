package com.vo;
/**
 * 用于业务逻辑的分页模型
 * @author Administrator
 *
 * @param <T>
 */
public class PageBean<T> implements java.io.Serializable{
    	//查询记录集合
	private java.util.List<T> rows;
	//查询当前页号
	private int page = 1;
	//每页显示记录数量
	private int pageSize = 20;
	//查询结果总记录数
	private int total;
	//查询结果总页数
	private int totlaPage;
	public java.util.List<T> getRows() {
		return rows;
	}
	public void setRows(java.util.List<T> rows) {
		this.rows = rows;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
	    	this.totlaPage = (this.total-1)/this.pageSize+1;
		this.total = total;
	}
	public int getTotlaPage() {
		return totlaPage;
	}
	public void setTotlaPage(int totlaPage) {
		this.totlaPage = totlaPage;
	}
		
}
