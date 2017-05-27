package com.xiyi.pojo;

public class CommonJpush {
	//推送消息id
	private String id;
	//推送状态
	private String status;
	//发送类型
	private String sender_type;
	//发送者
	private String sender;
	//推送类型
	private String push_type;
	//推送标题
	private String title;
	//推送内容
	private String content;
	//JSON 格式的可选参数
	private String extras;
	private String receiver;
	private String push_rtn;
	//是否被读取
	private String is_read;
	//创建时间
	private String create_time;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSender_type() {
		return sender_type;
	}
	public void setSender_type(String sender_type) {
		this.sender_type = sender_type;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public String getPush_type() {
		return push_type;
	}
	public void setPush_type(String push_type) {
		this.push_type = push_type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getExtras() {
		return extras;
	}
	public void setExtras(String extras) {
		this.extras = extras;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getPush_rtn() {
		return push_rtn;
	}
	public void setPush_rtn(String push_rtn) {
		this.push_rtn = push_rtn;
	}
	public String getIs_read() {
		return is_read;
	}
	public void setIs_read(String is_read) {
		this.is_read = is_read;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
}
