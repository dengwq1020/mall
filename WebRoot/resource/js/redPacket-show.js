/*
 * 首页红包信息
 */
//所有数据
var hbList=[];
var path="";
//红包踩雷信息显示
function minesRedShow(url) {
	path=url;
	//拼接
	var joint = "";
	//保存传过来的数据
	var redData = "";
	//有踩雷的红包
	var mineRedBog = "";
	$.ajax({
		type: "post",
		url: url+"/app/hb/allHb.do",
		dataType: "json",
		async: false,
		success: function(json) {
			redData = json.data;
			//数据存到全局变量
			hbList=redData;
			$.each(redData, function(index, item) {
				joint += '<div class="info"><a href="javascript:;" class="examine button button-glow button-rounded button-royal" onclick="redBagLiClick(this)">查看</a>';
				joint += '<li class="clearfix" onclick="redBagLiClick(this)"><input type="hidden" class="redId" value="';
				//红包id(使用创建时间为id)
				joint += item.hb_id;
				joint += '" /><h5 class="list-group-item">';
				//转换后的创建时间
				joint += getLocalTime(item.create_time);
				joint += '</h5><ol class="breadcrumb"><li>微信号</li><li>昵称</li><li>金额</li></ol>';
				joint += '<ul class="list-group">';
				//把存有红包具体信息的对象存到变量里
				mineRedBog = item.information;
				//循环遍历判断是否有踩雷的
				$.each(mineRedBog, function(i, value) {
					//判断状态是否踩到雷,1的话为踩到雷
					if(value.man_in == 1) {
						joint += '<li class="list-group-item"><span class="weChatId">';
						//显示微信号
						joint += value.name_num;
						joint += '</span><span class="weChatName">';
						//显示微信名称
						joint += value.rod_name;
						joint += '</span><span class="price">';
						//显示红包金额
						joint += value.grab_price;
						joint += '</span></li>';
					}
				});
				joint += '</ul><div class="earnings "><span>';
				//判断是否为收益
				if(item.payable < 0) {
					joint += '赔付：';
				} else {
					joint += '收益：';
				}
				joint += '</span><span>';
				joint += item.payable;
				joint += '</span></div></li>';
				//判断状态
				if(item.status == 1) {
					joint += '<a class="state button button-raised button-caution" href="javascript:;" onclick="buttonState(this)">未结清(待审核)';
				}
				if(item.status == 2) {
					joint += '<a class="state button button-raised button-primary" href="javascript:;" onclick="buttonState(this)">未结清(已审核)';
				}
				if(item.status == 3) {
					joint += '<a class="state button button-raised button-inverse" href="javascript:;" onclick="buttonState(this)">记账';
				}
				if(item.status == 4) {
					joint += '<a class="state button button-raised button-action" href="javascript:;" onclick="buttonState(this)">已结清';
				}
				joint += '</a></div>';
			});

			$(".row .red-left .list-unstyled").html(joint);
			addBGurl();
		},
		error: function() {
			console.log(2);
		}
	});
}

//给踩雷的红包加上相应背景
function addBGurl() {
	var countLi = $(".row .list-unstyled .info .list-group");
	$.each(countLi, function(i, b) {
		if($(b).find("li").length > 0) {
			$(b).parents(".info").addClass("step");
		}
	});
}

//按钮点击事件，修改红包的状态状态有(未结清(待审核)，已审核，已结清，记账)
function buttonState(s) {
	var btn = $(s);
	//获取当前按钮的文字
	var btnText = $(s).html();
	//点击红包id
	var id = $(s).parent(".info").find(".redId").val();
	//要改变的红包状态
	var newState = 0;
	//结算时间
	var clearingTime = "";
	//判断按钮 如果为未结清(待审核)
	if(btnText == "未结清(待审核)") {
		newState = 2;
		clearingTime = "";
		console.log(clearingTime);
		messageShow("确认", "取消", "是否已审核完毕", id, newState, btn, clearingTime);
	}

	//判断按钮 如果为未结清(已审核)
	if(btnText == "未结清(已审核)") {
		reset();
		alertify.set({ labels: { ok: "去结算", cancel: "本包记账" } });
		alertify.confirm("请选择对此红包的操作", function(e) {
			if(e) {
				clearingTime = new Date();
				newState = 4;
				console.log(clearingTime);
				messageShow("确认", "取消", "是否进行结算", id, newState, btn, clearingTime);
			} else {
				clearingTime = new Date();
				newState = 3;
				console.log(clearingTime);
				messageShow("确认", "取消", "是否进行记账", id, newState, btn, clearingTime);
				
			}
		});

	}
	//判断按钮 如果为已结清(已审核)
	if(btnText == "已结清") {
		reset();
		alertify.alert("此红包已结清");
		return false;
	}

	if(btnText == "记账") {
		newState = 4;
		clearingTime = new Date();
		console.log(clearingTime);
		messageShow("确认", "取消", "是否进行结算", id, newState, btn, clearingTime);
	}

}

//时间戳转换时间的方法
function getLocalTime(nS) {
	return new Date(parseInt(nS) * 1000).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
}

//提示框默认样式
function reset() {
	$("#toggleCSS").attr("href", "../css/alertify.default.css");
	alertify.set({
		labels: {
			ok: "OK",
			cancel: "Cancel"
		},
		delay: 5000,
		buttonReverse: false,
		buttonFocus: "ok"
	});
}

//消息提示框显示方法，参数分别为确认按钮显示文本，取消按钮显示文本，提示文本，红包id，红包状态，按钮本身，结算时间
function messageShow(sure, cancel, text, val1, val2, btn, Time) {
	reset();
	alertify.set({ labels: { ok: sure, cancel: cancel } });
	alertify.confirm(text, function(e) {
		if(e) {
			$.ajax({
				type: "post",
				url: path+"/app/hb/editStatus.do",
				data: {
					//传红包id
					"hb_id": val1,
					//传红包状态
					"status": val2
				},
				success: function(result) {
					if(result.success){
						//如果传回来的状态为2
						if(val2 == 2) {
							//返回true
								//按钮修改的文本
								btn.html("未结清(已审核)");
								//删除样式
								btn.removeClass("button-caution");
								//添加样式
								btn.addClass("button-primary");
								/*//删除样式
								btn.removeClass("button-caution");
								//添加样式
								btn.addClass("button-action");*/
							
						}
						if(val2 == 4) {
								btn.html("已结清");
								if(btn.hasClass("button-inverse")) {
									btn.removeClass("button-inverse");
									btn.addClass("button-action");
								} else {
									btn.removeClass("button-primary");
									btn.addClass("button-action");
								}
								/*if(btn.hasClass("button-inverse")) {
									btn.removeClass("button-inverse");
									btn.addClass("button-primary");
								} else {
									btn.removeClass("button-action");
									btn.addClass("button-primary");
								}*/
							
						}
						if(val2 == 3) {
							btn.html("记账");
							btn.removeClass("button-primary");
							btn.addClass("button-inverse");
								/*btn.html("记账");
								btn.removeClass("button-action");
								btn.addClass("button-inverse");*/
						}
						alertify.success("操作成功");
					}else{
						alertify.success("操作失败");
					}
					
				},
				error: function() {
					console.log("错误");
				}
			});
		} else {
			alertify.error("取消操作");
		}
	});
	return false;
}

//单个红包点击事件
function redBagLiClick(s) {
		
	//获取点击的id
	var id = $(s).parent(".info").find(".redId").val();
	console.log(+id);
	//拼接字符串
	var joint = "";
	//通过全局变量获取数据
			var hbData = hbList;
			$.each(hbData,function(i,val){
				console.log(val.hb_id==id);
				if(val.hb_id==id){
					joint += '<h5 class="list-group-item"><span>';
					//红包事件
					joint += getLocalTime(val.create_time);
					joint += '</span>';
					joint += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>Id:';
					//红包id
					joint += val.hb_id;
					joint += '</span></h5>';
					joint += '<div class="info"><ol class="breadcrumb">';
					joint += '<li>状态</li><li>微信号</li><li>昵称</li><li>金额</li></ol>';
					joint += '<ul class="list-group one">';
					//遍历具体抢包情况
					$.each(val.information, function(index, item) {
						if(item.man_in == 0) {
							joint += '<li class="list-group-item success">';
							joint += '<span class="people">抢包人员</span>';
							joint += '<span class="weChatId">';
							//抢包人微信id
							joint += item.weChatId;
							joint += '</span><span class="weChatName">';
							if(item.rod_type==1){
								//抢包人昵称
								joint += item.rod_name;
								joint += '(庄家)</span><span class="price">';
							}else if(item.rod_type==2){
								//抢包人昵称
								joint += item.rod_name;
								joint += '(玩家)</span><span class="price">';
							}else{
								joint += item.rod_name;
								joint += '</span><span class="price">';
							}
							//抢包人抢到的金额
							joint += item.grab_price;
							joint += '</span><input type="hidden" class="redstate" value="';
							//抢包人员状态
							joint += item.man_in;
							joint += '"/></li>';
						}
						if(item.man_in == 1) {
							joint += '<li class="list-group-item danger">';
							joint += '<span class="people">踩雷人员</span>';
							joint += '<span class="weChatId">';
							//抢包人微信id
							joint += item.weChatId;
							joint += '</span><span class="weChatName">';
							if(item.rod_type==1){
								//抢包人昵称
								joint += item.rod_name;
								joint += '(庄家)</span><span class="price">';
							}else if(item.rod_type==2){
								//抢包人昵称
								joint += item.rod_name;
								joint += '(玩家)</span><span class="price">';
							}else{
								joint += item.rod_name;
								joint += '</span><span class="price">';
							}
							//抢包人抢到的金额
							joint += item.grab_price;
							joint += '</span><input type="hidden" class="redstate" value="';
							//抢包人员状态
							joint += item.man_in;
							joint += '"/></li>';
						}
						if(item.man_in == 2) {
							joint += '<li class="list-group-item immune">';
							joint += '<span class="people">免死人员</span>';
							joint += '<span class="weChatId">';
							//抢包人微信id
							joint += item.weChatId;
							joint += '</span><span class="weChatName">';
							if(item.rod_type==1){
								//抢包人昵称
								joint += item.rod_name;
								joint += '(庄家)</span><span class="price">';
							}else if(item.rod_type==2){
								//抢包人昵称
								joint += item.rod_name;
								joint += '(玩家)</span><span class="price">';
							}else{
								joint += item.rod_name;
								joint += '</span><span class="price">';
							}
							//抢包人抢到的金额
							joint += item.grab_price;
							joint += '</span><input type="hidden" class="redstate" value="';
							//抢包人员状态
							joint += item.man_in;
							joint += '"/></li>';
						}

					});
					joint += '</ul><div class="other"><ul class="list-group">';
					joint += '<li class="list-group-item"><div class="banker">';
					joint += '<span>埋雷玩家：</span><span>';
					//发包人名称
					if(val.information[0].send_type==1){
						joint += val.information[0].send_name;
						joint += '(庄家)<b>&#40;';
					}else if(val.information[0].send_type==2){
						joint += val.information[0].send_name;
						joint += '(玩家)<b>&#40;';
					}else{
						joint += val.information[0].send_name;
						joint += '<b>&#40;';
					}
					
					//发包人微信号
					joint += val.send_weChatId;
					joint += '&#41;</b></span></div></li><li class="list-group-item">';
					joint += '<div class="banker"><span>红包统计：</span><span><b>';
					//发包金额
					joint += val.information[0].hb_price;
					joint += '</b>&#47;<b>';
					//发的包数
					joint += val.information[0].hb_num;
					joint += '</b>包<b>&#40;';
					//备注
					joint += val.information[0].remarks;
					joint += '&#41;</b></span></div></li>';
					joint += '<li class="list-group-item"><div class="banker">';
					joint += '<span>免死详情：</span><span>';
					//判断是否有免死
					//循环获取免死楼层抢到的金额
					
					var num=0;
					$.each(val.information, function(i, val) {
						if(val.man_in == 2) {
							//免死楼层
							joint += val.floor;
							joint += '楼&#47;';
							//金额
							joint += val.grab_price;
							num++;
						}
						
					
					});
					if(num==0){
						joint += "无免死";
					}
					joint += '</span></div></li><li class="list-group-item ">';
					joint += '<div class="rayInfo"><span>踩雷详情：</span></div></li>';
					joint += '<li class="rayList">';
					//循环遍历踩雷信息
					$.each(val.information, function(i, data) {
						//判断是否踩雷
						if(data.man_in == 1) {
							joint += '<p><span>';
							if(data.rod_type==1){
								//踩雷的名字
								joint += data.rod_name;
								joint += '(庄家)</span><span>';
							}else if(data.rod_type==2){
								//踩雷的名字
								joint += data.rod_name;
								joint += '(玩家)</span><span>';
							}else{
								//踩雷的名字
								joint += data.rod_name;
								joint += '</span><span>';
							}
							
							//踩雷的金额
							joint += data.grab_price;
							joint += '</span></p>';
						}
					});
					joint += '</li><li class="list-group-item "><div class="banker">';
					joint += '<span>埋雷收益：</span><span>';
					//收益
					joint += val.payable;
					joint += '</span></div></li></ul></div></div>';
				}
			})
			
			console.log(joint);
			//添加
			$(".red-right .red-right-top").html(joint);

}

//方案名称非空判断
function myCheck() {
	var packageName = $(".packageName").val();
	if(packageName == null || packageName == "") {
		reset();
		alertify.alert("方案名称不能为空,请输入方案名称");
		return false;
	}
	return true;
}

//群聊所有发包人发包信息显示
function completeInfo() {
	//拼接
	var joint = ""
	//完整信息
	var completeData = "";

	$.ajax({
		type: "post",
		url: path+"/user/playerDetails.do",
		dataType: "json",
		async: true,
		error: function() {
			console.log("出错了");
		},
		success: function(json) {
			completeData = json.data;
			$.each(completeData, function(index, item) {
				joint += '<tr><th>';
				//编号
				joint += index+1;
				joint += '</th><th>';
				//微信号
				joint += '#';
				joint += '</th><th>';
				//发红包的人
				joint += item.name;
				joint += '</th><th>';
				//总金额
				joint += item.total_price;
				joint += '</th><th>';
				//红包个数
				joint += item.hb_num;
				joint += '</th><th>';
				//未审核金额
				joint += item.debt_price;
				joint += '</th><th>';
				//已审核金额
				joint += item.audited_price;
				joint += '</th><th class="tally_price">';
				//记账金额
				joint += item.tally_price;
				joint += '</th><th class="priceState">';
				//状态
				//状态
				if(item.debt_price !=0) {
					joint += '<span style="color:red">未结清 </span>';
				}else if(item.audited_price != 0) {
					joint += '<span style="color:blue">已审核</span>';
				}else if(item.tally_price!=0) {
					joint += '<span style="color:violet">记账</span>';
				}else{
					joint += '<span style="color:green">已结清</span>';
				}
				joint += '</th><th>';
				//结算时间
				if(item.tally_price==0){
					joint += item.complete_time;
				}
				joint += '</th><th><a href="javascript:;" class="button button-raised button-caution" onclick="fullListOperation(this)"><i class="fa fa-gear"></i></a></th>';
				joint += '<input type="hidden" class="userId" value="';
				joint += item.id;
				joint += '" /></tr>';

			});
			//添加
			$(".red-right-bottom .customers .table-striped tbody").html(joint);
		}
	});
}

//编号
var bianhao = 0;
//完整信息显示的操作按钮事件
function fullListOperation(s) {
	//获取点击的编号
	bianhao = $(s).parent().parent().find("th:first-child").html();
	//获取id
	var id = $(s).parent().parent().find(".userId").val();
	//状态
	var priceState = $(s).parent().parent().find(".priceState").html();
	//记账金额
	var tally_price = $(s).parent().parent().find(".tally_price").html();
	//获取点击的按钮
	var btn = $(s);
	//状态id
	var stateId = 0;
	//结算时间
	var settleTime = "";
	/*//判断按钮 如果为未结清
	if(priceState == "未结清") {
		settleTime = "";
		stateId = 2;
		operationPopup("确认", "取消", "是否已审核完毕", id, stateId, btn, settleTime);
	}*/

	//判断按钮 如果为已审核
	/*if(item.tally_price!=0) {
		reset();
		alertify.set({ labels: { ok: "去结算", cancel: "记账" } });
		alertify.confirm("请选择对此玩家的操作", function(e) {
			if(e) {
				settleTime = new Date();
				stateId = 4;
				operationPopup("确认", "取消", "是否进行结算", id, stateId, btn, settleTime);
			} else {
				settleTime = "";
				stateId = 3;
				operationPopup("确认", "取消", "是否进行记账", id, stateId, btn, settleTime);
			}
		});

	}*/
	//判断按钮 如果为已结清
	if(priceState == "已结清") {
		reset();
		alertify.alert("此玩家已结清");
		return false;
	}

	//判断状态文本为记账
	if(tally_price!=0) {
		stateId = 4;
		settleTime = new Date();
		operationPopup("确认", "取消", "是否进行结算", id, stateId, btn, settleTime);
	}else{
		reset();
		alertify.alert("只能对记账金额操作");
		return false;
	}

}

//完整名单操作弹窗事件
function operationPopup(sure, cancel, text, val1, val2, btn, time) {
	//保存返回的
	var item = "";
	//拼接
	var joint = "";
	reset();
	alertify.set({ labels: { ok: sure, cancel: cancel } });
	alertify.confirm(text, function(e) {
		//点击确认事件
		if(e) {
			$.ajax({
				type: "post",
				url: path+"/app/hb/editStatus.do",
				data: {
					//id
					"id": val1,
					//状态
					"status": val2
				},
				dataType: "json",
				async: true,
				//ajax请求
				success: function(json) {
					completeInfo();
					alertify.success("操作成功");
				}
			});
			completeInfo();
			alertify.success("提交成功");

		} else {
			//点击取消
			alertify.error("取消操作");
		}
	});
	return false;
}

//
function onSettle(s) {
	var userId = $(s).parent().parent().find(".userId").val();
	$.ajax({
		type: "post",
		url: path+"/app/hb/editStatus.do",
		data: {
			//状态
			"status":"4",
			//方案名称
			"userId": userId,
			//结算
			"settle":3
		},
		success: function(result) {
			if(result.success){
				alertify.success("已结清");
			}else{
				alertify.success("操作失败");
			}
		},
		error: function() {
			console.log("错误");
		}

	});
	}