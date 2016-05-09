package com.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LogInterceptors implements HandlerInterceptor  {

	private static final Logger LOG = LoggerFactory.getLogger(LogInterceptors.class);
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		LOG.debug("请求完成以后执行。。。。{}",handler.getClass().getName());
		if(ex != null){
			throw ex;
		}
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if(modelAndView == null){
			LOG.debug("无视图。。。。");
			return;
		}
		LOG.debug("渲染视图之前执行。。。。[视图：{}]",modelAndView.getViewName());
	}

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		LOG.debug("将要执行的方法。。。。[方法：{}]",handler.getClass().getName());
		return true;
	}

}
