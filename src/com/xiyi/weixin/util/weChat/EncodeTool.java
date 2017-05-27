package com.xiyi.weixin.util.weChat;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class EncodeTool
{
  public static String get2Urlencode(String url, String charset)
    throws UnsupportedEncodingException
  {
    return URLEncoder.encode(url, (charset == null) || ("".equals(charset)) ? "UTF-8" : charset);
  }

  public static String getStringEncode(String s, String charset1, String charset2)
    throws UnsupportedEncodingException
  {
    String ct1 = (charset1 == null) || ("".equals(charset1)) ? "utf-8" : charset1;
    String ct2 = (charset2 == null) || ("".equals(charset2)) ? "UTF-8" : charset2;
    return new String(s.getBytes(ct1), ct2);
  }
}