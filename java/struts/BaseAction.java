package com.skcc.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionSupport;
import com.skcc.model.entity.RmsUser;

/**
 * 所有Action的基类
 * 所有方法的权限都是protected的,只应该在他的子类,即各个Action类中调用这些方法
 * 
 * 提供-将数据以json格式返回前台的方法
 * 提供-从Struts上下文中获取request,response对象的方法
 * 提供-从spring-security上下文中获取session信息的方法
 * 
 * @author NiChunping
 *
 */
public abstract class BaseAction extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4191146336091082281L;

	/**
	 * 将任意对象序列化为json格式,并将其返回ajax请求发起的地方
	 * 使用这个方法可以免于配置action,result
	 * @param obj 希望序列化并返回视图层的对象
	 */
	protected void toJsonString(Object obj){
		
		try{
			//使用fastjson的静态方法将java对象转化为json字符串
			String json = JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd HH:mm:ss");
			
			ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().write(json);
			ServletActionContext.getResponse().getWriter().flush();
			ServletActionContext.getResponse().getWriter().close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 从spring-security上下文中获取session中的用户信息
	 * 
	 * @return 当前session中的用户对象
	 */
	protected RmsUser getSessionUser(){
		
		return (RmsUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	/**
	 * 获取Servlet API提供的request对象
	 * 
	 * @return request对象
	 */
	protected HttpServletRequest getRequest(){
		return ServletActionContext.getRequest();
	}
	
	/**
	 * 获取Servlet API提供的response对象
	 * 
	 * @return response对象
	 */
	protected HttpServletResponse getResponse(){
		return ServletActionContext.getResponse();
	}
	
	/**
	 * 获取Servlet API提供的session对象
	 * 
	 * @return session对象
	 */
	protected HttpSession getSession(){
		return getRequest().getSession();
	}
}
