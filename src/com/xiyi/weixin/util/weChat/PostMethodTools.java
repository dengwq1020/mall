package com.xiyi.weixin.util.weChat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostMethodTools {
	private final static Logger logger = LoggerFactory.getLogger(PostMethodTools.class);
	public static String postMethod(String url,String json){	
		//String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		try {
			String returnData = "";
			PostMethod post = new PostMethod(url);
			HttpClient http = new HttpClient();
			//设置请求头
			post.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8"); 
			post.setRequestBody(json);
//			if ( map != null )   
//            {   
//                  HttpMethodParams p = new HttpMethodParams();   
//                  for ( Map.Entry<String, Object> entry : map.entrySet() )   
//                  {   
//                        p.setParameter(entry.getKey(), entry.getValue());   
//                  }   
//                  post.setParams(p);   
//            }
//			NameValuePair[] data = {
//				    new NameValuePair("ID", "11"),
//				    new NameValuePair("mtg", "0"),
//				    new NameValuePair("haveCookie", "0"),
//				    new NameValuePair("backID", "30"),
//				    new NameValuePair("psw", "password")
//				  };
			post.releaseConnection();
			//发送post请求给微信
			http.executeMethod(post);
			//得到返回的数据
			returnData= post.getResponseBodyAsString();	
			if(returnData.contains("%")){
				returnData = URLDecoder.decode(returnData);
			}	
			// 编码格式
			//returnData = new String(returnData.getBytes("ISO-8859-1"), "utf-8");
			return returnData;
		}catch(Exception e){
			logger.error("发送请求失败",e);
			return "no";
		}
	}
	 public static String sendGet(String url, String param) {
	        String result = "";
	        BufferedReader in = null;
	        try {
	            String urlNameString = url + "?" + param;
	            URL realUrl = new URL(urlNameString);
	            // 打开和URL之间的连接
	            URLConnection connection = realUrl.openConnection();
	            // 设置通用的请求属性
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("connection", "Keep-Alive");
	            connection.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            // 建立实际的连接
	            connection.connect();
	            // 获取所有响应头字段
	            Map<String, List<String>> map = connection.getHeaderFields();
	            // 遍历所有的响应头字段
	            for (String key : map.keySet()) {
	                System.out.println(key + "--->" + map.get(key));
	            }
	            // 定义 BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(new InputStreamReader(
	                    connection.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	            result = new String(result.getBytes(),"UTF-8");
	        } catch (Exception e) {
	            System.out.println("发送GET请求出现异常！" + e);
	            e.printStackTrace();
	        }
	        // 使用finally块来关闭输入流
	        finally {
	            try {
	                if (in != null) {
	                    in.close();
	                }
	            } catch (Exception e2) {
	                e2.printStackTrace();
	            }
	        }
	        return result;
	    } 
}
