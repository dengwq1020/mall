//时间控件
function timePlugIn() {
	$.datepicker.regional['zh-CN'] = {
		changeMonth: true,
		changeYear: true,
		clearText: '清除',
		clearStatus: '清除已选日期',
		closeText: '关闭',
		closeStatus: '不改变当前选择',
		prevText: '<上月',
		prevStatus: '显示上月',
		prevBigText: '<<',
		prevBigStatus: '显示上一年',
		nextText: '下月>',
		nextStatus: '显示下月',
		nextBigText: '>>',
		nextBigStatus: '显示下一年',
		currentText: '今天',
		currentStatus: '显示本月',
		monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
		monthNamesShort: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
		monthStatus: '选择月份',
		yearStatus: '选择年份',
		weekHeader: '周',
		weekStatus: '年内周次',
		dayNames: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
		dayNamesShort: ['周日', '周一', '周二', '周三', '周四', '周五', '周六'],
		dayNamesMin: ['日', '一', '二', '三', '四', '五', '六'],
		dayStatus: '设置 DD 为一周起始',
		dateStatus: '选择 m月 d日, DD',
		dateFormat: 'yy-mm-dd',
		firstDay: 1,
		initStatus: '请选择日期',
		isRTL: false
	};

	$.datepicker.setDefaults($.datepicker.regional['zh-CN']);
	$('#start-date').prop("readonly", true).datetimepicker({
		timeText: '时间',
		hourText: '小时',
		minuteText: '分钟',
		secondText: '秒',
		currentText: '现在',
		closeText: '完成',
		showSecond: true, //显示秒  
		timeFormat: 'HH:mm:ss' //格式化时间  
	});

	$('#end-date').prop("readonly", true).datetimepicker({
		timeText: '时间',
		hourText: '小时',
		minuteText: '分钟',
		secondText: '秒',
		currentText: '现在',
		closeText: '完成',
		showSecond: true, //显示秒  
		timeFormat: 'HH:mm:ss' //格式化时间  
	});

}

//获取时间
function abc() {
	var a = $("#start-date").val();
	console.log(a);
}

//获取所有玩家名单
function getNameList() {
	//拼接
	var joint = "";
	var nameList = "";
	$.ajax({
		type: "post",
		//获取所有玩家名单的接口
		url: url+"/user/getPlayerName.do",
		async: true,
		dataType: "json",
		success: function(json) {
			nameList = json.data;

			$.each(nameList, function(i, item) {
				//拼接数组里不为空的数据
				if(item != null && item != "") {
					joint += '<li onclick="people(this)">';
					joint += item.name;
					joint += '</li>';
				}
			});
			//添加给ul
			$('.query_people ul').html(joint);

			$("#bbg").css({
				display: "block",
				height: $(document).height()
			});
			var $box = $('.query_people_mask');
			$box.css({
				display: "block",
			});
		}
	});
}
//选择楼层的时候相应的li变样式
function people(s) {
	$(s).css("background-color", "goldenrod").siblings().css("background-color", "#FFFFFF");
	$(s).addClass('bh').siblings().removeClass('bh');
	name = $(s).text();
}

//选名单关闭事件
$(".query_people_mask .close").click(function() {
	//隐藏弹窗
	$("#bbg,.query_people_mask").css("display", "none");
})

//选择名字确认点击事件
$(".query_people_mask .button").click(function() {
	//隐藏弹窗
	$("#bbg,.query_people_mask").css("display", "none");
	//显示在input里
	$(".queryByName .query-name").val(name);
})

//开始时间
var startTime = "";
//结束时间
var endTime = "";

//接收验证结果
var check = "";
//名字
var queryName = "";
//接受传过来的数据
var queryData = "";
//表格
var table = $(".query-info .part1 .table");

//验证时间是非为空
function checkTime() {
	startTime = $("#start-date").val();
	endTime = $("#end-date").val();
	if(startTime != null && endTime != null && startTime != "" && endTime != "") {
		return true;
	} else {
		return false;
	}
}

//按名字查询按钮
function nameBtnClick(s) {
	//获取时间验证结果
	check = checkTime();
	//拼接
	var joint = "";
	//获取名字
	queryName = $(".query-name").val();

	//验证是否为空
	if(check) {
		//如果开始时间早于结束时间
		if(startTime < endTime) {
			//如果名字不为空
			if(queryName != null && queryName != "") {
				$.ajax({
					type: "post",
					url: url+"/user/nameBy.do",
					async: true,
					data: {
						//开始时间
						"startTime": startTime,
						//结束时间
						"endTime": endTime,
						//名字
						"name": queryName
					},
					dataType: "json",
					success: function(json) {
						table.html("");
						queryData = json.data;
						joint += '<caption>';
						joint += queryName;
						joint += '&nbsp;&nbsp;<span class="time">';
						joint += startTime;
						joint += '</span>&nbsp;&#8764;&nbsp;<span class="time2">';
						joint += endTime;
						joint += '</span></caption>';
						joint += '<thead><tr><th>发包次数</th>';
						joint += '<th>发包金额</th><th>盈利金额</th></tr></thead><tbody>';
						
							joint += '<tr><td>';
							//发包次数
							joint += queryData.hb_num;
							joint += '</td><td>';
							//红包金额
							joint += queryData.hb_total;
							joint += '</td><td>';
							//盈利金额
							joint += queryData.hb_profit;
							joint += '</td></tr>';
					
						joint += '</tbody>';
						table.html(joint);
					}
				});
			} else {
				reset();
				alertify.alert("名字不能为空！");
				return false;
			}
		} else {
			reset();
			alertify.alert("开始时间不能晚于结束时间！");
			return false;
		}
	} else {
		reset();
		alertify.alert("请选择开始时间和结束时间！");
		return false;
	}

}

//查询全部玩家数据按钮
function allBtnClick(s) {
	//获取时间验证结果
	check = checkTime();
	//拼接
	var joint = "";
	//验证是否为空
	if(check) {
		if(startTime < endTime) {
			$.ajax({
				type: "post",
				url: url+"/user/allGameData.do",
				async: true,
				data: {
					//开始时间
					"startTime": startTime,
					//结束时间
					"endTime": endTime
				},
				dataType: "json",
				success: function(json) {
					table.html("");
					queryData = json.data;
					joint += '<caption>全部玩家数据&nbsp;&nbsp;<span class="time">';
					joint += startTime;
					joint += '</span>&nbsp;&#8764;&nbsp;<span class="time2">';
					joint += endTime;
					joint += '</span></caption>';
					joint += '<thead><tr><th>微信号</th><th>姓名</th><th>发包次数</th>';
					joint += '<th>发包金额</th><th>盈利金额</th></tr></thead><tbody>';
					$.each(queryData, function(index, item) {
						joint += '<tr><td>#';
						/*//微信号
						joint += item.wenxinID;*/
						joint += '</td><td>';
						//发包玩家
						joint += item.name;
						joint += '</td><td>';
						//发包次数
						joint += item.hb_num;
						joint += '</td><td>';
						//红包金额
						joint += item.hb_total;
						joint += '</td><td>';
						//盈利金额
						joint += item.hb_profit;
						joint += '</td></tr>';
					});
					joint += '</tbody>';
					table.html(joint);
				}
			});
		} else {
			reset();
			alertify.alert("开始时间不能晚于结束时间！");
			return false;
		}
	} else {
		reset();
		alertify.alert("请选择开始时间和结束时间！");
		return false;
	}

}

//查询免死详情按钮
function fromDeathBtnClick(s) {
	//获取时间验证结果
	check = checkTime();
	//拼接
	var joint = "";
	//验证是否为空
	if(check) {
		if(startTime < endTime) {
			$.ajax({
				type: "post",
				url: url+"/user/freeDetails.do",
				async: true,
				data: {
					//开始时间
					"startTime": startTime,
					//结束时间
					"endTime": endTime
				},
				dataType: "json",
				success: function(json) {
					table.html("");
					queryData = json.data;
					joint += '<caption>免死详情&nbsp;&nbsp;<span class="time">';
					joint += startTime;
					joint += '</span>&nbsp;&#8764;&nbsp;<span class="time2">';
					joint += endTime;
					joint += '</span></caption>';
					joint += '<thead><tr><th>免死抢包次数</th><th>免死抢包金额</th></tr></thead><tbody>';
					
						joint += '<tr><td>';
						//免死抢包次数
						joint += queryData.hb_num;
						joint += '</td><td>';
						//免死抢包金额
						joint += queryData.hb_total;
						joint += '</td></tr>';
					
					joint += '</tbody>';
					table.html(joint);
				}
			});
		} else {
			reset();
			alertify.alert("开始时间不能晚于结束时间！");
			return false;
		}
	} else {
		reset();
		alertify.alert("请选择开始时间和结束时间！");
		return false;
	}

}

//查询发包金额详情按钮
function priceBtnClick(s) {
	//获取时间验证结果
	check = checkTime();
	//拼接
	var joint = "";
	//验证是否为空
	if(check) {
		if(startTime < endTime) {
			$.ajax({
				type: "post",
				url: "",
				async: true,
				data: {
					//开始时间
					"startTime": startTime,
					//结束时间
					"endTime": endTime
				},
				dataType: "json",
				success: function(json) {
					table.html("");
					queryData = json.data;
					joint += '<caption>发包金额详情&nbsp;&nbsp;<span class="time">';
					joint += startTime;
					joint += '</span>&nbsp;&#8764;&nbsp;<span class="time2">';
					joint += endTime;
					joint += '</span></caption>';
					joint += '<thead><tr><th>发包金额</th>';
					joint += '<th>发包次数</th><th>总金额</th></tr></thead><tbody>';
					$.each(queryData, function(index, item) {
						joint += '<tr><td>';
						//发包金额
						joint += item.hb_price;
						joint += '</td><td>';
						//发包次数
						joint += item.hb_count;
						joint += '</td><td>';
						//总金额
						joint += item.total_price;
						joint += '</td></tr>';
					});
					joint += '</tbody>';
					table.html(joint);
				}
			});
		} else {
			reset();
			alertify.alert("开始时间不能晚于结束时间！");
			return false;
		}
	} else {
		reset();
		alertify.alert("请选择开始时间和结束时间！");
		return false;
	}

}

//查询总流水按钮
function totalBtnClick(s) {
	//获取时间验证结果
	check = checkTime();
	//拼接
	var joint = "";
	//验证是否为空
	if(check) {
		if(startTime < endTime) {
			$.ajax({
				type: "post",
				url: url+"/user/totalProfit.do",
				async: true,
				data: {
					//开始时间
					"startTime": startTime,
					//结束时间
					"endTime": endTime
				},
				dataType: "json",
				success: function(json) {
					table.html("");
					queryData = json.data;
					joint += '<caption>总流水数据&nbsp;&nbsp;<span class="time">';
					joint += startTime;
					joint += '</span>&nbsp;&#8764;&nbsp;<span class="time2">';
					joint += endTime;
					joint += '</span></caption>';
					joint += '<thead><tr><th>累计发包次数</th><th>累计发包金额</th><th>累计踩雷奖励</th>';
					joint += '<th>累计盈亏统计</th></tr></thead><tbody>';
					
						joint += '<tr><td>';
						//累计发包次数
						joint += queryData.hb_num;
						joint += '</td><td>';
						//累计发包金额
						joint += queryData.hb_total;
						joint += '</td><td>';
						//累计踩雷奖励
						joint += queryData.hb_mine;
						joint += '</td><td>';
						//累计盈亏统计
						joint += queryData.hb_profit;
						joint += '</td></tr>';
					
					joint += '</tbody>';
					table.html(joint);
				}
			});
		} else {
			reset();
			alertify.alert("开始时间不能晚于结束时间！");
			return false;
		}
	} else {
		reset();
		alertify.alert("请选择开始时间和结束时间！");
		return false;
	}

}