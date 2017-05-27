package com.xiyi.weixin.util;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;
/**
 * XML工具类
 * @author Administrator
 *
 */
public class XStreamUtils {
	private static final Logger logger = LoggerFactory.getLogger(XStreamUtils.class);
	private XStream  xstream = null;
    private ObjectOutputStream  out = null;
    private ObjectInputStream in = null;
 
   
	public String ObjectToXml(Object object){
		try {
			xstream = new XStream();
		} catch (Exception e) {
			  e.printStackTrace();
		}
		String xml = "";
		try {
		xml = xstream.toXML(object);
		} catch (Exception e) {
			System.out.print("转换失败");
			  e.printStackTrace();	  
		}
		return xml;	
	}
	/**
	 * inputStream 转对象
	 * @param inputStream
	 * @param cls
	 * @return
	 */
	public  Object WeixinObject(InputStream inputStream, Class<?> cls)
	 {
		logger.info("inpustream to object");
		try
		    {
		      
		      ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
		      ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

		      String tmp = "";
		      Integer len = 0;
		      while ((len = Integer.valueOf(readableByteChannel.read(byteBuffer))).intValue() != -1)
		      {
		        byteBuffer.flip();
		        byte[] bytes = null;
		        bytes = byteBuffer.array();
		        tmp = tmp + new String(bytes, 0, len.intValue());
		        byteBuffer.clear();
		      }
		      if (tmp.indexOf("VIEW") != -1)
		       return "";
		      return getWeixinXml(tmp, cls);
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		    return null;
		  }
	/**
	 * xml转obj
	 * @param xml
	 * @param xmlObj
	 * @return
	 */
	  public  Object getWeixinXml(String xml, Class<?> xmlObj)
	  {
		 logger.info("生成的xml:",xml);
		 xstream = null;
		 xstream = new XStream();
		 xml = xml.replaceAll("xml", xmlObj.getName());
	    return xstream.fromXML(xml);
	  }
	public Object xmlToJson(String xml){
		XMLSerializer xmlSerializer = new XMLSerializer();
		JSON json = xmlSerializer.read(xml);
		return json;
	}


	  public  String getXMLForObject(Object obj, Class<?> clazz)
	  {
		xstream = null;
		xstream = new XStream();
	    return xstream.toXML(obj).replaceAll("__", "_").replaceAll(clazz.getName(), "xml");
	  }

}

   

