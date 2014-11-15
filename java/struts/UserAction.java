package com.skcc.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.ExceptionMappings;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Results;

import com.opensymphony.xwork2.ModelDriven;
import com.skcc.model.entity.RmsMenuGroup;
import com.skcc.model.entity.RmsUser;
import com.skcc.model.page.Grid;
import com.skcc.model.page.Json;
import com.skcc.service.UserService;
import com.skcc.util.LogUtil;

/**
 * Struts框架Action类写法示例-UserAction
 * 
 * 功能-简单的增删改查示例
 * 功能-接受前台传值的示例
 * 
 * 返回值-使用super.toJsonString 来响应前台ajax请求
 * 返回值-视图层提示信息使用json.message来传递,有利于多语言模块
 * 返回值-始终要设置json.success来标识操作结果
 * 
 * @author NiChunping
 *
 */
@Action("userAction")	//令spring自动注入控制器组件
public class UserAction extends BaseAction implements ModelDriven<RmsUser> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UserService userService;		//业务逻辑组件
	
	private RmsUser user = new RmsUser();	//此处必须手动创建一个new user,否则struts报错	
	
	/**
	 * 根据过滤条件查询用户列表
	 */
	public void getUserList(){

		Json json = new Json();

		json.setObj(userService.getUserList(user));
		json.setSuccess(true);

		super.toJsonString(json);
	}
	
	/**
	 * 根据过滤条件查询用户列表,用作datatable.js插件的远程数据源
	 * 
	 * 返回值使用专用的Grid类,数据表插件才能识别
	 */
	public void getUserGrid(){

		Grid grid = new Grid();

		grid.setData(userService.getUserList(user));

		super.toJsonString(grid);
	}
	
	
	/**
	 * 登记新用户
	 * 需要新用户除用户号和登记修改人,等级修改时间之外的所有信息,可为空
	 * 用json.success来表达是否成功
	 */
	public void addUser(){

		Json json = new Json();
		user.setFrstInptId(super.getSessionUser().getUserId());
		user.setLstUpdtId(super.getSessionUser().getUserId());

		if(userService.addUser(user)){

			json.setSuccess(true);
			json.setMsg("保存成功");
		}else{

			json.setMsg("保存失败");
			json.setSuccess(false);
		}
		
		super.toJsonString(json);
	}
	
	/**
	 * 修改用户
	 * 用户编号是修改依据
	 * 用json.success来表达是否成功
	 */
	public void modifyUser(){

		Json json = new Json();
		user.setLstUpdtId(super.getSessionUser().getUserId());

		if(userService.modifyUserById(user)){

			json.setSuccess(true);
			json.setMsg("保存成功");
		}else{

			json.setMsg("保存失败");
			json.setSuccess(false);
		}

		super.toJsonString(json);
	}
	
	/**
	 * 删除用户
	 * 需要待删除用户的编号
	 * 用json.success来表达是否成功
	 */
	public void dropUser(){

		Json json = new Json();

		if(userService.dropUserById(user)){

			json.setSuccess(true);
			json.setMsg("删除成功");
		}else{

			json.setMsg("删除失败");
			json.setSuccess(false);
		}

		super.toJsonString(json);
	}

	////////////////////////////////////////getters and setters/////////////////////////////////
	
	public UserService getUserService() {
		return userService;
	}

	/**
	 * spring自动注入业务逻辑组件,不必手动调用 
	 * 注意数据成员的名字和其getter方法名必须和对应service实现类的注解名相对应
	 * 注解Autowired必须加上
	 * @param userService spring自动创建的业务逻辑部件对象
	 */
	@Autowired	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 实现ModelDriven接口
	 * 前台表单的name不必写为user.userNm的形式,写为userNm即可
	 * 适用于传参较少的情况
	 */
	@Override
	public RmsUser getModel() {
		return this.user;
	}
	
	public void setUser(RmsUser user) {
		this.user = user;
	}
}
