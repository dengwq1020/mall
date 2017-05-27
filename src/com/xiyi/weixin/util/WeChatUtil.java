package com.xiyi.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiyi.weixin.util.alipay.AlipayCore;
import com.xiyi.weixin.util.config.WeChatConfig;
import com.xiyi.weixin.util.weChat.MD5Util;

public class WeChatUtil {
	private static final Logger logger = LoggerFactory.getLogger(WeChatUtil.class);
	/**
	 * 验证签名
	 * 
	 * @param Params
	 * @param sign
	 * @param key
	 * @return
	 */
	public static boolean getSignVeryfy(Map<String, String> params, String key) {
		// 获取签名
		String sign = params.get("sign");
		// 过滤空值、sign与sign_type参数
		Map<String, String> sParaNew = AlipayCore.paraFilter(params);
		// 获取待签名字符串
		String preSignStr = AlipayCore.createLinkString(sParaNew);
		// 拼接key
		String signStr = preSignStr + "&key=" + key;
		MD5Util md5 = new MD5Util();
		// 将签名转换为大写，和原来的签名比较
		return (md5.GetMD5Code(signStr).toUpperCase()).equals(sign);
	}

	public static String getSign(Map<String, String> params, String key) {
		// 获取签名
		String sign = params.get("sign");
		// 过滤空值、sign与sign_type参数
		Map<String, String> sParaNew = AlipayCore.paraFilter(params);
		// 获取待签名字符串
		String preSignStr = AlipayCore.createLinkString(sParaNew);
		// 拼接key
		String signStr = preSignStr + "&key=" + key;
		MD5Util md5 = new MD5Util();
		// 将签名转换为大写，和原来的签名比较
		return md5.GetMD5Code(signStr).toUpperCase();
	}

	public static Object receiveParams(InputStream inputStream, Class<?> cls)
			throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		try {
			logger.info(" welcome xml analysis ");
			ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
			byte[] bytes;
			String tmp = "";
			Integer len;
			while ((len = readableByteChannel.read(byteBuffer)) != -1) {
				byteBuffer.flip();
				bytes = null;
				bytes = byteBuffer.array();
				tmp += new String(bytes, 0, len);
				byteBuffer.clear();
			}
			logger.debug(tmp);
			if (tmp.indexOf("VIEW") != -1)
				return "";
			XStreamUtils xStream = new XStreamUtils();
			return xStream.getWeixinXml(tmp, cls);

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
		//{"appid":"wxaf0b8b0c78efd5db","bank_type":"CFT","cash_fee":1,"device_info":"WEB","fee_type":"CNY","is_subscribe":"N",
		//"mch_id":"1325192901","nonce_str":"97fe44095bdd4de092796c89322fb6b2","openid":"oVk__s3tCuhv8zsLkFpjR2Hn37sE",
		//"out_trade_no":"33ec73833c5e4628903cb51460219941","result_code":"SUCCESS","return_code":"SUCCESS",
		//"sign":"1B37DDFD096B940FB07AB8D5012EF201","time_end":"20160410003926","total_fee":1,
		//"trade_type":"APP","transaction_id":"4006602001201604104696941106"}
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("appid", "wxaf0b8b0c78efd5db");
		map.put("bank_type", "CFT");
		map.put("cash_fee", "1");
		map.put("device_info", "WEB");
		map.put("fee_type", "CNY");
		map.put("is_subscribe", "N");
		map.put("mch_id", "1325192901");
		map.put("nonce_str", "97fe44095bdd4de092796c89322fb6b2");
		map.put("openid", "oVk__s3tCuhv8zsLkFpjR2Hn37sE");
		map.put("out_trade_no", "33ec73833c5e4628903cb51460219941");
		map.put("result_code", "SUCCESS");
		map.put("return_code", "SUCCESS");
		map.put("sign", "1B37DDFD096B940FB07AB8D5012EF201");
		map.put("time_end", "20160410003926");
		map.put("total_fee", "1");
		map.put("trade_type", "APP");
		map.put("transaction_id", "4006602001201604104696941106");
		//System.out.println(getSignVeryfy(map, WeChatConfig.key));
	}
}
