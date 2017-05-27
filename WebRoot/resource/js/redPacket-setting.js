//设置全局变量保存对象
var dom = "";
//设置全局变量保存免死楼层
var storeyNum = "";
//设置全局变量保存免死名单
var nameShow = "";
//楼层
var storey = "";
//免死名字
var fdName = "";
//抢单手名单数组
var peopleArr = [];
//日(爆)模式金额对象
var priceArr = {
	price: [],
	payPrice: []
};
//保存免死楼层的隐藏input
var msycInput="";

//设置全局数组
var ratioArr = [];
//初始化数组根据i生成用于存储数值的数组
for(var i = 4; i <= 10; i++) {
	ratioArr[i] = {
		ratioName: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
		ratioStorey: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
		ratioMine: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
	}
}

//用于保存按名字玩法所显示的名单
var nameList_4 = [];
var nameList_5 = [];
var nameList_6 = [];
var nameList_7 = [];
var nameList_8 = [];
var nameList_9 = [];
var nameList_10 = [];

//var ratioArr_4 = {
//	ratioName: [],
//	ratioStorey: [],
//	ratioMine: []
//}
//var ratioArr_5 = {
//	ratioName: [],
//	ratioStorey: [],
//	ratioMine: []
//}




//倍数的输入限制，只能输入数字和小数点
function checkNum(obj) {
	obj.value = obj.value.replace(/[^\d.]/g, ""); //清除“数字”和“.”以外的字符  
	obj.value = obj.value.replace(/\.{2,}/g, "."); //只保留第一个. 清除多余的  
	obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
	obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d).*$/, '$1$2.$3'); //只能输入两个小数  
	if(obj.value > 100) {
		obj.value = 0;
	}
	//不能为空格
	if($.trim(obj.value) == "") {
		obj.value = 0;
	}
	if(obj.value.indexOf(".") < 0 && obj.value != "") { //以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额 
		obj.value = parseFloat(obj.value);
	}
}

/*
 * 单雷部分
 */
//单雷方式按楼层绑定事件
function mineOn(s) {
	//给点击格子所在表格里所有的.single绑定事件
	$(s).parents("table").find(".single").on("click", function() {
		//把点击按钮里的span保存为全局变量
		dom = $(this).find("span");
		//					console.log(dom);
		//拼接
		var joint = "";
		if(peopleArr == "") {
			//消息提示
			showMsg('请先设置抢包手名单,并保存', 'center');
		} else {
			//循环遍历保存枪单手名单的数组
			$.each(peopleArr, function(i, item) {
				//拼接数组里不为空的数据
				if(item != null && item != "") {
					joint += '<li onclick="peopleLI(this)">';
					joint += item;
					joint += '</li>';
				}
			});
			//添加给ul
			$('.payment_time_mask ul').html(joint);

			$("#bg").css({ display: "block", height: $(document).height() });
			var $box = $('.payment_time_mask');
			$box.css({
				display: "block",
			});
		}

	})
	//获取点击格子所在表格里所有的.single
	var tdlen = $(s).parent().parent().parent().parent().find(".single");
	//判断长度
	switch(tdlen.length) {
		case 4:
			//符合长度的判断相应的数组，赋值给对应格子
			$.each(nameList_4, function(index, item) {
				tdlen.eq(index).find("span").html(item);
			});
			break;
		case 5:
			$.each(nameList_5, function(index, item) {
				tdlen.eq(index).find("span").html(item);
			});
			break;
		case 6:
			$.each(nameList_6, function(index, item) {
				tdlen.eq(index).find("span").html(item);
			});
			break;
		case 7:
			$.each(nameList_7, function(index, item) {
				tdlen.eq(index).find("span").html(item);
			});
			break;
		case 8:
			$.each(nameList_8, function(index, item) {
				tdlen.eq(index).find("span").html(item);
			});
			break;
		case 9:
			$.each(nameList_9, function(index, item) {
				tdlen.eq(index).find("span").html(item);
			});
			break;
		case 10:
			$.each(nameList_10, function(index, item) {
				tdlen.eq(index).find("span").html(item);
			});
			break;

	}
}

//单雷解除绑定事件
function mineOff(s) {
	$(s).parent().parent().parent().parent().find(".single").off("click");
	//获取点击格子所在表格里类名.single格子里的span
	var tds = $(s).parent().parent().parent().parent().find(".single span");
	$.each(tds, function(index, item) {
		//给span赋值
		$(item).html(index + 1);
	});

}

//选择楼层的时候相应的li变样式
function peopleLI(s) {
	$(s).css("background-color", "goldenrod").siblings().css("background-color", "#FFFFFF");
	$(s).addClass('bh').siblings().removeClass('bh');
	name = $(s).html();
}

//选名单关闭事件
$(".payment_time_mask .close").click(function() {
	//隐藏弹窗
	$("#bg,.payment_time_mask").css("display", "none");
})

//选择名字确认点击事件
$(".payment_time_mask .button").click(function() {
	//隐藏弹窗
	$("#bg,.payment_time_mask").css("display", "none");
	//把选择的值赋给开启弹窗的按钮对象
	dom.html(name);
	//遍历开启弹窗按钮对象所在表格里的类名累.single的格子的长度
	var len = dom.parent().parent().parent().find(".single");
	var spanList = "";
	//隐藏的input
	var hinput="";
	//判断长度
	switch(len.length) {
		case 4:
			//清空相应数组
			nameList_4 = [];
			spanList = len.find("span");
			hinput =len.find(".louceng");
			$.each(spanList, function(index, item) {
				//保存选择的名单
				nameList_4.push($(item).html());
				hinput.eq(index).val($(item).html());
			});
			break;

		case 5:
			nameList_5 = [];
			spanList = len.find("span");
			$.each(spanList, function(index, item) {
				nameList_5.push($(item).html());
			});
			break;
		case 6:
			nameList_6 = [];
			spanList = len.find("span");
			$.each(spanList, function(index, item) {
				nameList_6.push($(item).html());
			});
			break;
		case 7:
			nameList_7 = [];
			spanList = len.find("span");
			$.each(spanList, function(index, item) {
				nameList_7.push($(item).html());
			});
			break;
		case 8:
			nameList_8 = [];
			spanList = len.find("span");
			$.each(spanList, function(index, item) {
				nameList_8.push($(item).html());
			});
			break;
		case 9:
			nameList_9 = [];
			spanList = len.find("span");
			$.each(spanList, function(index, item) {
				nameList_9.push($(item).html());
			});
			break;
		case 10:
			nameList_10 = [];
			spanList = len.find("span");
			$.each(spanList, function(index, item) {
				nameList_10.push($(item).html());
			});
			break;
	}
	//设置对象为空
	dom = "";
	//消息提示
	showMsg('操作成功', 'center');
})

/**
 * 免死部分选楼部分
 */

//免死方式选择楼层
function select_Storey(s) {
	
	//获取长度
	var tds = $(s).parent().parent().parent().parent().find(".single").length;
	//拼接
	var pj = "";
	//根据长度循环遍历生成li数量
	for(var i = 1; i <= tds; i++) {
		pj += '<li onclick="storeyLI(this)">';
		pj += i;
		pj += '</li>';
	}
	//显示数量到ul
	$(".exemptDeath_storey_mask ul").html(pj);
	//把点击按钮里的span保存为全局变量
	storeyNum = $(s).find("span");
	msycInput =$(s).find("input");
	//弹窗
	$("#bg").css({ display: "block", height: $(document).height() });
	var $box = $('.exemptDeath_storey_mask');
	$box.css({
		display: "block",
	});

}

//选择楼层的时候相应的li变样式
function storeyLI(s) {
	$(s).css("background-color", "goldenrod").siblings().css("background-color", "#FFFFFF");
	$(s).addClass('bh').siblings().removeClass('bh');
	storey = $(s).html();
}

//选楼层关闭事件
$(".exemptDeath_storey_mask .close").click(function() {
	//隐藏弹窗
	$("#bg,.exemptDeath_storey_mask").css("display", "none");
})

//选择楼层确认点击事件
$(".exemptDeath_storey_mask .button").click(function() {
	//隐藏弹窗
	$("#bg,.exemptDeath_storey_mask").css("display", "none");
	//把选择的值赋给开启弹窗的按钮对象
	storeyNum.html(storey);
	//把选择的值赋给开启弹窗的按钮隐藏input
	msycInput.val(storey);
	//设置对象为空
	storeyNum = "";
	
	//操作成功消息提示
	showMsg('操作成功', 'center');
})

function select_name(s) {
	//获取长度
	var tds = $(s).parent().parent().parent().parent().find(".single").length;
	//拼接
	var pj = "";
	//根据长度循环遍历生成li数量
	for(var i = 1; i <= tds; i++) {
		pj += '<li onclick="storeyLI(this)">';
		pj += i;
		pj += '</li>';
	}
	//显示数量到ul
	$(".exemptDeath_storey_mask ul").html(pj);
	//把点击按钮里的span保存为全局变量
	storeyNum = $(s).find("span");
	//弹窗
	$("#bg").css({ display: "block", height: $(document).height() });
	var $box = $('.exemptDeath_storey_mask');
	$box.css({
		display: "block",
	});
}

//保存设置的倍数
function minebtnClcik(s) {
	//获取点击的input
	var ipt = $(s).parent().find(".singleMine input:radio:checked");
	//获取点击的按钮所在的包
	var len = $(s).parent().find(".single").length;
	//获取包里填写倍数的input
	var ratioLen = $(s).parent().find(".mine");

	if(ipt.val() == "3") {
		//清空对应包对象里的数组
		ratioArr[len].ratioName = [];
		//赋值
		$.each(ratioLen, function(index, item) {
			ratioArr[len].ratioName.push($(item).val());
		});
	};
	if(ipt.val() == "2") {
		//清空
		ratioArr[len].ratioStorey = [];
		$.each(ratioLen, function(index, item) {
			ratioArr[len].ratioStorey.push($(item).val());
		});
	};
	if(ipt.val() == "1") {
		//清空
		ratioArr[len].ratioMine = [];
		$.each(ratioLen, function(index, item) {
			console.log($(item).val());
			ratioArr[len].ratioMine.push($(item).val());
		});
	}
	

	//保存成功消息提示
	showMsg('保存成功', 'center');
}
//显示绑定的倍数
function ratioShow(s) {
	var span=$(s).parents("table").find(".single span");
	//获取点击的按钮所在的包
	var len = $(s).parents("table").find(".single").length;
	var ipt = $(s).val();
	//获取包里填写倍数的input
	var ratioLen = $(s).parents("table").find(".mine");
	//获取楼层隐藏的input
	var hiddenInput=$(s).parents("table").find(".louceng");
	if(ipt == "3") {
		$.each(span, function(i,b) {
			hiddenInput.eq(i).val($(b).html());
		});
		//赋值
		$.each(ratioArr[len].ratioName, function(index, item) {
			ratioLen.eq(index).val(item);
		});
		
	};
	
	
	if(ipt == "2") {
		$.each(ratioArr[len].ratioStorey, function(index, item) {
			ratioLen.eq(index).val(item);
		});
		$.each(hiddenInput, function(i,val) {
			$(val).val(i+1);
		});
	};
	if(ipt == "1") {
		$.each(ratioArr[len].ratioMine, function(index, item) {
			ratioLen.eq(index).val(item);
		});
		$.each(hiddenInput, function(i,val) {
			$(val).val(i+1);
			console.log(val);
		});

	}

}



//免死方式名单选择
function fromDeathNameSelect(s) {
	//拼接
	var joint = "";
	if(peopleArr == "") {
		//消息提示
		showMsg('请先设置抢包手名单,并保存', 'center');
	} else {
		//循环遍历保存枪单手名单的数组
		$.each(peopleArr, function(i, item) {

			//拼接数组里不为空的数据
			if(item != null && item != "") {
				joint += '<li onclick="nameLI(this)">';
				joint += item;
				joint += '</li>';
			}
		});

		//显示数量到ul
		$(".exemptDeath_people_mask ul").html(joint);
		//根据点击的按钮找到所要显示值的元素，保存到全局变量
		nameShow = $(s).parent().find("input");
		//弹窗
		$("#bg").css({ display: "block", height: $(document).height() });
		var $box = $('.exemptDeath_people_mask');
		$box.css({
			display: "block",
		});
	}

}

//免死名单关闭事件
$(".exemptDeath_people_mask .close").click(function() {
	//隐藏弹窗
	$("#bg,.exemptDeath_people_mask").css("display", "none");
})

//选择免死名单的时候相应的li变样式
function nameLI(s) {
	$(s).css("background-color", "goldenrod").siblings().css("background-color", "#FFFFFF");
	$(s).addClass('bh').siblings().removeClass('bh');
	fdName = $(s).html();
}

//免死名单列表确认按钮
$(".exemptDeath_people_mask .button").click(function() {
	//隐藏弹窗
	$("#bg,.exemptDeath_people_mask").css("display", "none");
	//把选取的数值赋值给input
	nameShow.val(fdName);
	//消息提示
	showMsg('操作成功', 'center');
})

/*
 * 抢包手名单部分
 */
//循环生成抢包手HTML方法事件
function jointPeopleList(num){
	var joint="";
	
	for (var i=1;i<=num;i++) {
		if(i<10){
			joint+='<li><span>抢包手0';
			joint+=i;
			joint+='&nbsp;:&nbsp;</span><input type="text" name="grabBagPeople_';
			joint+=i;
			joint+='" size="20"></li>';
		}
		if(i>=10){
			joint+='<li><span>抢包手';
			joint+=i;
			joint+='&nbsp;:&nbsp;</span><input type="text" name="grabBagPeople_';
			joint+=i;
			joint+='" size="20"></li>';
		}
	}
	$(".people_bonus .people_setting ul").html(joint);
}

//保存抢包手名单
function savePeopleList() {
	peopleArr = [];
	var list = $(".people_bonus .people_setting ul li").find("input");
	$.each(list, function(index, item) {
		peopleArr.push($(item).val());
	});

	//保存成功消息提示
	showMsg('保存成功', 'bottom');
}

/*
 * 日爆模式
 */

//循环生成金额赔付金额HTML方法事件
function jointPrice(num) {
	var joint = "";

	for(var i = 0; i < num; i++) {
		joint += '<li><input type="text" class="price" name="price_';
		joint += i + 1;
		joint += '" value="';
		//判断数组里是否有值，如果超过数组的长度并且没有值则赋给一个默认的值
		if(priceArr.price[i] != undefined) {
			joint += priceArr.price[i];
		} else {
			joint += "0.00";
		}
		joint += '" onkeyup="priceCheck(this)"></li>';
		joint += '<li><input type="text" class="payPrice" name="payPrice_';
		joint += i + 1;
		joint += '" value="';
		if(priceArr.payPrice[i] != undefined) {
			joint += priceArr.payPrice[i];
		} else {
			joint += "0";
		}

		joint += '" onkeyup="payPriceCheck(this)"></li>';
	}
	$(".bonus_setting .priceUl").html(joint);

}

//保存日爆金额
function savePrice() {
	//金额数组清空
	priceArr.price = [];
	//金额数组清空
	priceArr.payPrice = [];
	//变量保存金额li
	var priceList = $(".people_bonus .bonus_setting .priceUl li").find(".price");
	//变量保存赔付金额li
	var payPriceList = $(".people_bonus .bonus_setting .priceUl li").find(".payPrice");
	//遍历金额li保存值到金额数组
	$.each(priceList, function(index, item) {
		priceArr.price.push($(item).val());
	});

	//遍历赔付金额li保存值到赔付金额数组
	$.each(payPriceList, function(index, item) {
		priceArr.payPrice.push($(item).val());
	});
	
	$.each(priceArr.price, function(i,b) {
		console.log(b);
	});
	//保存成功消息提示
	showMsg('保存成功', 'bottom');

}

//倍数的输入限制，只能输入数字和小数点
function priceCheck(obj) {
	obj.value = obj.value.replace(/[^\d.]/g, ""); //清除“数字”和“.”以外的字符  
	obj.value = obj.value.replace(/^\./g, ""); //验证第一个字符是数字而不是.  
	obj.value = obj.value.replace(/\.{2,}/g, "."); //只保留第一个. 清除多余的  
	obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
	obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); //只能输入两个小数  
	if(obj.value > 1000) {
		obj.value = 0;
	}
	//不能为空格
	if($.trim(obj.value) == "") {
		obj.value = 0;
	}
	if(obj.value.indexOf(".") < 0 && obj.value != "") { //以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额 
		obj.value = parseFloat(obj.value);
	}
}

//倍数的输入限制，只能输入数字不能输小数点
function payPriceCheck(obj) {
	obj.value = obj.value.replace(/[^\d.]/g, ""); //清除“数字”和“.”以外的字符  
	obj.value = obj.value.replace(/^\./g, ""); //验证第一个字符是数字而不是.  
	obj.value = obj.value.replace(/\.{2,}/g, "."); //只保留第一个. 清除多余的  
	obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
	obj.value = obj.value.replace(/^(\-)*(\d+)\.*$/, '$1$2'); //只能输入两个小数  
	if(obj.value > 100000) {
		obj.value = 0;
	}
	//不能为空格
	if($.trim(obj.value) == "") {
		obj.value = 0;
	}
	if(obj.value.indexOf(".") < 0 && obj.value != "") { //以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额 
		obj.value = parseFloat(obj.value);
	}
}

//连环雷无免死点击修改value
function checkboxVal(s){
	if ($(s).is(':checked')) {
   		$(s).parent().find(".lhwms").val(0);
	}else{
		$(s).parent().find(".lhwms").val(1);
	}
}


//倍数的输入限制，只能输入数字不能输小数点
function payPriceC(obj) {
	obj.value = obj.value.replace(/[^\d]/g, ""); //清除“数字”和“.”以外的字符  
	obj.value = obj.value.replace(/^\./g, ""); //验证第一个字符是数字而不是.  
	obj.value = obj.value.replace(/\.{2,}/g, "."); //只保留第一个. 清除多余的  
	obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
	obj.value = obj.value.replace(/^(\-)*(\d+)\.*$/, '$1$2'); //只能输入两个小数  
	if(obj.value >100) {
		obj.value = 0;
	}

	if(obj.value.indexOf(".") < 0 && obj.value != "") { //以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额 
		obj.value = parseFloat(obj.value);
	}
}