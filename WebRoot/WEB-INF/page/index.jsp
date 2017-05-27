<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">

	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->

		<title>红包系统</title>

		<!-- Bootstrap -->
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/font-awesome.min.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/bootstrap.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/redPacket.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/redPaclet-bottom.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/buttons.css" />
		<link href="http://cdn.bootcss.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/payment.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/alertify.core.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/alertify.default.css" />
		
		<!--查询弹窗样式-->
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/querymodal.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/jquery-ui.min.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/jquery-ui-timepicker-addon.min.css" />

		<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
		<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
		<!--[if lt IE 9]>
      <script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

		<style type="text/css">
			.show_msg {
				width: 100%;
				height: 35px;
				text-align: center;
				position: fixed;
				left: 0;
				z-index: 10001;
			}
			
			.show_span {
				display: inline-block;
				height: 35px;
				padding: 0 15px;
				line-height: 35px;
				background: rgba(0, 0, 0, 0.8);
				border-radius: 50px;
				color: #fff;
				font-size: 1em;
			}
		</style>
	</head>

	<body>
		<div class="container">
			<header class="red-top">
				<figcaption>红包系统</figcaption>
			</header>
			<div class="row clearfix">
				<div class="red-left">
					<ul class="list-unstyled">
						

					</ul>
				</div>
				<!--右边红包详情上部分-->
				<div class="red-right">
					<div class="red-right-top" style="display: block;">
						
					</div>
					<!--右边红包详情下部分-->
					<div class="red-right-bottom">
						<!--所有名单-->
						<div class="customers">
							<a class="refresh button button-raised button-caution" onclick="completeInfo()"><i class="fa fa-refresh"></i></a>
							<table class="table-striped">

								<caption>完整名单信息</caption>
								<thead>
									<tr>
										<th>序号</th>
										<th>微信号</th>
										<th>昵称</th>
										<th>总金额</th>
										<th>发红包次数</th>
										<th>未审核金额</th>
										<th>已审核金额</th>
										<th>记账金额</th >
										<th>状态</th>
										<th>结算时间</th>
										<th>记账金额结算</th>
									</tr>
								</thead>
								<tbody>
									
								</tbody>
							</table>
						</div>
					</div>
				</div>

			</div>
			<!--开始结束操作和设置脚本显示区域-->
			<footer class="bottom">
				<div class="bottom-left">
					<!--开始按钮-->
					<div class="start">
						<a href="javascript:;" class="button button-primary button-box button-giant button-longshadow-right">开始&nbsp;&nbsp;<i class="fa fa-play"></i></a>
					</div>
					<!--结束按钮-->
					<div class="end">
						<a href="javascript:;" class="button button-caution button-box button-raised button-giant button-longshadow">結束&nbsp;&nbsp;<i class="fa fa-stop"></i></a>
					</div>
					<!--设置按钮-->
					<div class="configuration">
						<a href="javascript" data-toggle="modal" data-target="#ratioModal" class="button button-highlight button-box button-giant button-longshadow-right button-longshadow-expand" onclick="modalShow()">设置&nbsp;&nbsp;<i class="fa fa-gear"></i></a>
					</div>
					
					<!--刷新按钮-->
					<div class="refreshbtn">
						<a href="javascript:;" class="button button-action button-box button-giant button-longshadow-right button-longshadow-expand">刷新&nbsp;&nbsp;<i class="fa fa-refresh"></i></a>
					</div>
					
					<!--查询按钮-->
					<div class="querybtn">
						<a href="javascript:;" data-toggle="modal" data-target="#queryModal" class="button button-royal button-box button-giant button-longshadow-right button-longshadow-expand" onclick="">查询&nbsp;&nbsp;<i class="fa fa-search"></i></a>
					</div>
					
					<!--操作时间-->
					<div class="operationTime">
						<p>开始时间:</p>
						<p id="startTime"></p>
						<p>结束时间:</p>
						<p id="endTime"></p>
					</div>
				</div>
				<div class="bottom-right">

				</div>
			</footer>

			<!--弹窗部分-->
			<div id="ratioModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="ratioModalLabel" aria-hidden="false">
				<div class="modal-dialog">
					<form role="form" action="allRule.do" class="md-content" id="redform">
						<!--弹窗头部-->
						<div class="modal-header">
							<!--关闭按钮-->
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
							<h4 class="modal-title" id="myModalLabel">规则详细设置&nbsp;&nbsp;&nbsp;&nbsp;方案名称&nbsp;:&nbsp;<input type="text" name="packageName" class="packageName" placeholder="请输入方案的名字"/></h4>
						</div>
						<!--弹窗主窗体-->
						<div class="modal-body">
							

						</div>
						<!--抢包手和日爆奖励部分-->
						<div class="people_bonus">
							<div class="people_setting">
								<header class="title">抢包手名单</header>
								<ul>

								</ul>
								<a href="javascript:;" class="button button-raised button-pill button-inverse" onclick="savePeopleList()">保存</a>
							</div>
							<!--日爆金额设置部分-->
							<div class="bonus_setting">
								<header class="title">日(爆)模式规则(规则1金额为奖励减半,规则2金额为正常奖励)</header>
								<!--日爆规则和包数设置-->
								<div class="dayModel">
									<ul>
										<li>
											<span class="s1">规则1:</span>
											<span class="dayPrice">金额&nbsp;&nbsp;:<input type="text" name="dayPrice_1" class="dayPrice_1" value="0.00" onkeyup="priceCheck(this)"></span>
											<span class="dayBog">包数&nbsp;&nbsp;:<input type="text" name="dayBog_1" class="dayBog_1" value="0" onkeyup="payPriceC(this)"></span>
										</li>
										<li>
											<span class="s1">规则2:</span>
											<span class="dayPrice">金额&nbsp;&nbsp;:<input type="text" name="dayPrice_2" class="dayPrice_2" value="0.00" onkeyup="priceCheck(this)"></span>
											<span class="dayBog">包数&nbsp;&nbsp;:<input type="text" name="dayBog_2" class="dayBog_2" value="0" onkeyup="payPriceC(this)"></span>
										</li>
									</ul>
								</div>
								<header class="title-price">金额(最大金额999.99)</header>
								<header class="title-price">赔付金额(最大金额99999)</header>
								<ul class="priceUl">
									<li><input type="text" class="price" name="price_1" value="0.00" onkeyup="priceCheck(this)"></li>
									<li><input type="text" class="payPrice" name="payPrice_1" value="0" onkeyup="payPriceCheck(this)"></li>
								</ul>
								<a href="javascript:;" class="button button-raised button-pill button-inverse" onclick="savePrice()">保存</a>
							</div>
						</div>
						<!--弹窗底部-->
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
							<!-- <input type="submit" class="btn btn-primary sure" value="提交" /> -->
							<a href="javascript:;" class="btn btn-primary sure" value="提交" onclick="fromSubmit()">提交</ a>
						</div>
					</form>
				</div>
				<!--名单弹窗部分-->
				<div class="payment_time">
					<div id="bg"></div>
					<div class="payment_time_mask">
						<a class="close" href="javascript:;">&times;</a>
						<h4>名单</h4>
						<ul></ul>
						<button class="button button-primary button-small button-inverse">确定</button>
					</div>
				</div>

				<!--楼层弹窗部分-->
				<div class="exemptDeath_storey">
					<div id="bg"></div>
					<div class="exemptDeath_storey_mask">
						<a class="close" href="javascript:;">&times;</a>
						<h4>楼层</h4>
						<ul></ul>
						<button class="button button-primary button-small button-inverse">确定</button>
					</div>
				</div>

				<!--免死名单弹窗部分-->
				<div class="exemptDeath_people">
					<div id="bg"></div>
					<div class="exemptDeath_people_mask">
						<a class="close" href="javascript:;">&times;</a>
						<h4>楼层</h4>
						<ul></ul>
						<button class="button button-primary button-small button-inverse">确定</button>
					</div>
				</div>

			</div>
		</div>
		<!-- 2017-05-11 -->
		<!--查询的弹窗-->
			<div id="queryModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="queryModalLabel" aria-hidden="false">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<!--关闭按钮-->
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
							<h4 class="modal-title" id="myModalLabel">查询</h4>
						</div>
						<div class="modal-body">
							<!--功能栏-->
							<div class="query-head">
								<!--时间部分-->
								<div class="query-time">
									<div class="start-time">
										<span>开始时间&nbsp;:&nbsp;</span>
										<!--时间控件-->
										<input id="start-date" value="" placeholder="请选择开始时间" />
									</div>
									<div class="end-time">
										<!--时间控件-->
										<span>结束时间&nbsp;:&nbsp;</span>
										<input id="end-date" value="" placeholder="请选择结束时间" />
									</div>
								</div>
								<!--按钮部分-->
								<div class="query-button">
									<!--按名字-->
									<div class="queryByName">
										<input class="query-name" type="text" placeholder="请输入要查询的玩家昵称" value="" size="25" />
										<a href="javascript:;" onclick="getNameList()"><i class="fa fa-book"></i></a>
									</div>
									<!--按名字查询按钮-->
									<a href="javascript:;" class="nameBtn button button-raised button-box button-pill" onclick="nameBtnClick(this)">按名字查询</a>
									<!--查询全部玩家数据按钮-->
									<a href="javascript:;" class="allBtn button button-raised button-box button-pill" onclick="allBtnClick(this)">查询全部玩家数据</a>
									<!--查询免死详情按钮-->
									<a href="javascript:;" class="fromDeathBtn button button-raised button-box button-pill" onclick="fromDeathBtnClick(this)">查询免死详情</a>
									<!--查询发包金额详情按钮-->
									<a href="javascript:;" class="priceBtn button button-raised button-box button-pill" onclick="priceBtnClick(this)">发包金额详情</a>
									<!--查询总流水按钮-->
									<a href="javascript:;" class="totalBtn button button-raised button-box button-pill" onclick="totalBtnClick(this)">查询群总流水</a>
								</div>
							</div>
							<!--显示部分-->
							<div class="query-info">
								<!--按名字查询与查询全部玩家数据部分-->
								<div class="part1" style="display: block;">
									<table class="table table-striped table-bordered">
										<caption>按名字查询&nbsp;&nbsp;<span class="time">2017-01-01 17:20</span>&nbsp;&#8764;&nbsp;<span class="time2">2017-05-10 18:20</span></caption>
										<thead>
											<tr>
												<th>微信号</th>
												<th>姓名</th>
												<th>发包次数</th>
												<th>发包金额</th>
												<th>盈利金额</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td>2</td>
												<td>3</td>
												<td>3</td>
												<td>3</td>
												<td>3</td>
											</tr>
											<tr>
												<td>2</td>
												<td>2</td>
												<td>2</td>
												<td>3</td>
												<td>3</td>
											</tr>
											<tr>
												<td>3</td>
												<td>2</td>
												<td>2</td>
												<td>3</td>
												<td>3</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>

						</div>
					</div>
					<!-- /.modal-content -->
				</div>
				<!-- /.modal -->
				<!--查询名单弹窗部分-->
				<div class="query_people">
					<div id="bbg"></div>
					<div class="query_people_mask">
						<a class="close" href="javascript:;">&times;</a>
						<h4>名单</h4>
						<ul></ul>
						<button class="button button-primary button-small button-inverse">确定</button>
					</div>
				</div>
			</div>
		</div>

		<!-- <script src="${pageContext.request.contextPath }/js/jquery190.js" type="text/javascript" charset="utf-8"></script>  -->
		<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
		<script src="${pageContext.request.contextPath }/js/jquery.min.js"></script>
		
		<script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
		<!-- Include all compiled plugins (below), or include individual files as needed -->
		<script src="${pageContext.request.contextPath }/js/redPacket-show.js" type="text/javascript" charset="utf-8"></script>
		<script src="${pageContext.request.contextPath }/js/table-info.js"></script>
		<script src="${pageContext.request.contextPath }/js/bootstrap.min.js"></script>
		<script src="${pageContext.request.contextPath }/js/alert.js" type="text/javascript" charset="utf-8"></script>
		<script src="${pageContext.request.contextPath }/js/redPacket-setting.js" type="text/javascript" charset="utf-8"></script>
		<script src="${pageContext.request.contextPath }/js/alertify.min.js" type="text/javascript" charset="utf-8"></script>
		<!--2017-05-10时间控件-->
		<script src="${pageContext.request.contextPath }/js/jquery-ui.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${pageContext.request.contextPath }/js/jquery-ui-timepicker-addon.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${pageContext.request.contextPath }/js/jquery-ui-timepicker-zh-CN.js" type="text/javascript" charset="utf-8"></script>
		<script src="${pageContext.request.contextPath }/js/redPacket-query.js" type="text/javascript" charset="utf-8"></script>

		<script type="text/javascript">
		
		var url="${path}";
			//设置窗体高度
			$(".container").css("height", document.height + "px");
			//页面加载的时候执行的
			$(function() {
				//运行时间控件
				timePlugIn();
				//踩雷红包部分显示方法事件
				minesRedShow(url);
				completeInfo();
				//刷新按钮绑定方法
				$(".refreshbtn .button").click(function(){
					minesRedShow(url);
				});
				//开始按钮
				$(".start .button").click(function(){
					$.ajax({
						type: "post",
						url: "${path}/rule/updateStatus.do",
						data: {
							//状态
							"status":"1",
							//方案名称
							"plan_name": "haha"
						},
						success: function(result) {
							if(result.success){
								minesRedShow(url);
								$("#startTime").html(result.data);
								alertify.success("开始");
							}else{
								alertify.success("方案开始失败");
							}
						},
						error: function() {
							console.log("错误");
						}
					});
				});

				//结束
				$(".end .button").click(function(){
					//拼接
					var joint = "";
					//保存传来数据
					var liushui = "";
					//开始时间
					var startTime = $("#startTime").html();
					if(startTime==null||startTime==""){
						reset();
							alertify.alert("开始时间不能为空！");
							return false;
					}
					$.ajax({
						type: "post",
						url: "${path}/rule/updateStatus.do",
						data: {
							//状态
							"status":"0",
							//方案名称
							"plan_name": "haha",
							//开始时间
							"startTime":startTime
						},
						success: function(result) {
							if(result.success){
								minesRedShow(url);
								$("#endTime").html(result.data);
								alertify.success("结束");
								liushui = result.data;
								joint += '<h4>本次流水&nbsp;:&nbsp;<span>';
								joint+=startTime
								joint += '</span>&nbsp;&#8764;&nbsp;<span>';
								joint+=endTime;
								joint += '</span></h4><div><span>累计发包次数 &nbsp;:&nbsp;';
								//计发包次数
								joint+=liushui.cumulative_hb_number;
								joint += '</span><span>累计发包金额&nbsp;:&nbsp;';
								//计发包金额
								joint+=liushui.cumulative_hb_price;
								joint += '</span></div><div><span>累计踩雷奖励 &nbsp;:&nbsp;';
								//计踩雷奖励
								joint+=liushui.cumulative_mine_reward;
								joint += '< /span><span>累计盈亏统计&nbsp;:&nbsp;';
								//计盈亏统计
								joint+=liushui.cumulative_total_price;
								joint += '</span></div>';
								$(".bottom-right").html(joint);
							}else{
								alertify.success("方案结束失败");
							}
						},
						error: function() {
							console.log("错误");
						}
					});
				});
				
			});
			
			//表单提交
			function fromSubmit() {
				//var redFrom = $('#redFrom').serialize();
				//redFrom = decodeURIComponent(redFrom, true);
			var packageName = $(".packageName").val();
			if(packageName == null || packageName == "") {
				reset();
				alertify.alert("方案名称不能为空,请输入方案名称");
				return false;
			}
		
				$.ajax({
					type: "post",
					url: "${path}/rule/allRule.do",
					async: true,
					data: $('#redform').serialize(),
					success: function(result) {
						if(result.data==1){
							alertify.success("方案名称重名");
						}else if(result.success) {
							$("#ratioModal").modal('hide');
							alertify.success("方案提交成功");
							
						}else{
							alertify.success("方案提交失败");
						}
					}
				});
			}

	
			//点击设置按钮弹窗的时候加载的事件
			function modalShow() {
				console.log(2);
				//js生成表格html
				tableShow();
				//js生成抢包手名单HTML
				jointPeopleList(20);
				//js生成日爆金额html
				jointPrice(30);
				//点击设置的时候默认执行一次绑定
				var ipt = $(".modal-body").find(".singleMine input:radio:checked");
				if(ipt.val() == "3") {
					ipt.click();
				}
			}
	
		</script>
	</body>

</html>