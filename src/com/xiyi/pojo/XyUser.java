package com.xiyi.pojo;

import java.io.Serializable;
/**
 * xy_user用户表
 * @author xyp
 *
 */
public class XyUser implements Serializable {
	
	/* 用户id */
	private String uid;
	
	/* 用户名 */
	private String username;
	
	/* 手机号码 */
	private String phone;

	/* 密码 */
	private String password;
	
	/* 用户头像 */
	private String img;

	/* 邀请码 */
	private String yaoqing;
	
	/* 用户注册时间 */
	private String register_time;
	
	/* 用户登入时间 */
	private String login_time;
	
	/* 微信id */
	private String openid;
	
	/* 登陆状态 */
	private int login_status;
	
	/* 用户类型 */
	private int user_type;
	
	
	public int getUser_type() {
		return user_type;
	}

	public void setUser_type(int user_type) {
		this.user_type = user_type;
	}

	public int getLogin_status() {
		return login_status;
	}

	public void setLogin_status(int login_status) {
		this.login_status = login_status;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getYaoqing() {
		return yaoqing;
	}

	public void setYaoqing(String yaoqing) {
		this.yaoqing = yaoqing;
	}

	public String getRegister_time() {
		return register_time;
	}

	public void setRegister_time(String register_time) {
		this.register_time = register_time;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLogin_time() {
		return login_time;
	}

	public void setLogin_time(String login_time) {
		this.login_time = login_time;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	
}
