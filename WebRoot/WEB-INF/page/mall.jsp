<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, user-scalable=no, maximum-scale=1.0">
		<meta content="yes" name="apple-mobile-web-app-capable">
		<meta content="black" name="apple-mobile-web-app-status-bar-style">
		<meta content="telephone=no" name="format-detection">
		<title>心艺商城</title>
		<link rel="stylesheet" href="${pageContext.request.contextPath }/css/common.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/swiper.css" />
		<script src="${pageContext.request.contextPath }/js/jquery190.js"></script>
		<script src="${pageContext.request.contextPath }/js/common.js"></script>
		<script src="${pageContext.request.contextPath }/js/swiper.js" type="text/javascript" charset="utf-8"></script>
		<style>
			.goods-list {
				background-color: #f3f5f4;
				padding-top: 8px;
			}

			.goods-list li {
				margin: 5px;
				background-color: #FFFFFF;
			}

			.goods-list .goods {
				float: left;
				width: 49%;
				background-color: #f3f5f4;
				box-shadow: 0 0 1px #333;
				position: relative;
			}

			.goods-list li .goodsImg-wrap {
				height: 180px;
				/*border: 1px solid red;*/
			}

			.goods-list li .goodsImg-wrap img {
				display: block;
				width: 100%;
				height: 100%;
			}

			.goods-list li b {
				display: block;
				position: absolute;
				right: 4%;
				bottom: 10px;
				width: 53px;
				height: 35px;
				/*border: 1px solid red;*/
				background: rgba(0, 0, 0, 0) url(${pageContext.request.contextPath }/images/icon.png) no-repeat scroll 0px -290px;
				background-size: 150px;

			}

			.goods-list li h3,
			.goods-list li span {
				font: bold 12px "宋体";
				padding-left: 8px;
				padding-top: 10px;
				display: block;
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
				color: #993350;

			}

			.goods-list li p {
				font: bold 20px "宋体";
				color: red;
				padding: 10px;
			}
			/*2017-03-07商城*/
			/*头部*/

			#head {
				height: 50px;
				width: 100%;
				background-color: #2cac89;
			}

			.flexsearch {
				width: 238px;
				position: relative;
				margin: 0 auto;
			}

			.flexsearch--input {
				margin: 9px auto;
				box-sizing: content-box;
				height: 30px;
				padding: 0px 46px 0px 10px;
				border: 1px solid #FFFFFF;
				border-radius: 35px;
				color: #333;
				font-family: 'Helvetica', sans-serif;
				font-size: 16px;
				/*设置提示字体居中*/
				line-height: normal;
			}

			.flexsearch--submit {
				position: absolute;
				top: 9px;
				right: 0px;
				width: 40px;
				height: 33px;
				border: none;
				color: #888;
				font-family: 'Helvetica', sans-serif;	
				font-size: 20px;
				line-height: 30px;
				background: url(${pageContext.request.contextPath }/images/search.png) no-repeat scroll 6px 2.5px;
				background-size: 25px;
			}
			/*设置搜索框默认提示字体样式*/

			::-webkit-input-placeholder {
				color: #dfdfdf;
				font: bold 14px "微软雅黑";
			}

			input:-moz-placeholder {
				color: #dfdfdf;
				text-align: center;
			}
			/*中间按钮部分*/

			.blank {
				width: 100%;
				/*border: 1px solid red;*/
				height: 40px;
			}

			.blank ul li {
				font: bold 16px "微软雅黑";
				float: left;
				line-height: 40px;
				text-align: center;
				color: #c5c5c5;
			}

			.blank #one {
				width: 33%;
			}

			.blank #two {
				width: 33%;
			}

			.blank #three {
				width: 30%;
				text-align: right;
				/*border: 1px solid red;*/
				font: bold 16px "微软雅黑";
				float: left;
				line-height: 40px;
				color: #c5c5c5;
			}
			/*轮播部分*/

			.swiper-container {
				height: 300px;
			}

			.swiper-container img {
				width: 100%;
				height: 100%;
			}
		</style>
	</head>

	<body topmargin="0" leftmargin="0" scroll=no>
		<header id="head">
			<div class="flexsearch">
				<form action="${path}/mobile/user/goodListPage" method="POST">
					<input name="goodName" class="flexsearch--input"  placeholder="输入商品的名称">
					<input class="flexsearch--submit" type="submit" value="" />
				</form>
			</div>
		</header>
		<!--轮播图部分-->
		<div class="common-container">
			<!--轮播图部分-->
			<div class="swiper-container">
				<!--swiper容器[可以随意更改该容器的样式-->
				<div class="swiper-wrapper"></div>
				<div class="swiper-pagination"></div>
				<!--分页器-->、

			</div>

			<div class="blank">
				<ul>
					<li id="one" value="9">新品</li>
					<li id="two" value="8">配件</li>
					<a id="three" href="${path}/mobile/user/goodType">更多>></a>
				</ul>
			</div>
			<!--新品部分-->
			<ul id="part" class="goods-list clearfix">
			</ul>
		</div>
		<script>
			//addHeader("心艺商城");
			//添加头部尾部
			addMallFooter();

			var typeId = $("#one").val();
			var str1 = ""; //用于拼接
			var pageNum = 1; //页数
			var isCtn=0;//是否继续下拉加载
			//显示数据,下拉加载
			$(document).ready(function() {
				swipea(); //轮播图
				loadMore(typeId, pageNum);

			});

			//滚动
			$(window).scroll(function() {
				// 当滚动到最底部以上100像素时， 加载新内容
				if($(document).height() - $(this).scrollTop() - $(this).height() < 100 && isCtn == 0) {
					isCtn=1;
					pageNum++;
					loadMore(typeId, pageNum);
				}
			});

			//下拉瀑布式加载内容
			function loadMore(typeId, pageNum) {
				$.ajax({
					type: "post",
					url: '${path }/mobile/mall/goodList',
					data: {
						"typeId": typeId,
						"pageNum": pageNum
					},
					success: function(json) {
						var data=json.data;
						var oProduct, $row, iHeight, iTempHeight;
						str1 = ""; //清空拼接，避免数据重复
						for(var i = 0 ; i < data.length ; i++) {
							// 找出当前高度最小的列, 新内容添加到该列
							iHeight = -1;
							$('#part').each(function() {
								iTempHeight = Number($(this).height());
								if(iHeight == -1 || iHeight > iTempHeight) {
									iHeight = iTempHeight;
									$row = $(this);
								}
							});
							str1 += '<div class="goods"><li><input type="hidden" value="';
							str1 += data[i].id;
							str1 += '"></input><a href="${path}/mobile/user/goodDetails?id=';
							str1 += data[i].id;
							str1 += '"><div class="goodsImg-wrap">';
							str1 += '<img src="${path}/';
							str1 += data[i].thumb;
							str1 += '"></div></a><h3>';
							str1 += data[i].name;
							str1 += '</h3><span>';
							str1 += data[i].description;
							str1 += '</span><p>&yen;';
							str1 += data[i].price;
							str1 += '</p><b onclick="shoppingClick(this)"></b></li></div>';
						}
						if (data!=null && data!="") {
							$row.append(str1);
						}
						isCtn=0;
					}
				});
			}

			$(".blank li").click(function() {
				$("#part").html("");
				typeId = $(this).val();
				pageNum = 1;
				loadMore(typeId, pageNum);
			});

			//加入购物车事件
			function shoppingClick(s){
				var pid=$(s).parent().find('input').val();
				$.ajax({
					type: "post",
					url: "${path}/mobile/shopCart/addGoods/"+pid,
					success:function(result){
						if(result.success){
							showPrompt(result.msg);
						}
						
					}
				});
			}

			//加载轮播图
			function swipea() {
				$.ajax({
					type: "post",
					url: "${path}/mobile/mall/mallAdver",
					error: function() {
						showPrompt("加载轮播图失败，请刷新");
					},
					success: function(result) {
						var data=result.data
						var shuffling = ""; //轮播拼接
						for(var i = 0; i < data.length; i++) {
							shuffling += '<div class="swiper-slide"><a href="${path}/';
							shuffling += data[i].link;
							shuffling += '"><img src="${path}/';
							shuffling += data[i].img;
							shuffling += '"></a></div>';
						}
						$(".swiper-wrapper").append(shuffling);
						loadingimg();  //重新加载轮播

					}
				});
			}

			//轮播方法
			function loadingimg() {

				var mySwiper = new Swiper(".swiper-container", {
					direction: "horizontal",
					/*横向滑动*/
					loop: true,
					/*形成环路（即：可以从最后一张图跳转到第一张图*/
					pagination: ".swiper-pagination",
					/*分页器*/
					autoplay: 3000,
					/*每隔3秒自动播放*/
					paginationClickable: true,
					/*此参数设置为true时，点击分页器的指示点分页器会控制Swiper切换。*/
					initialSlide: 0,
					observer: true, //修改swiper自己或子元素时，自动初始化swiper
					observeParents: true //修改swiper的父元素时，自动初始化swiper
				});
			}

			//轮播窗口高度
			$(".swiper-container").css('height', parseInt($(document).height() * 0.4) + 'px');
		</script>
	</body>

</html>