package com.xiyi.weixin.util.weChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

public class WebTool
{
  private static PrintWriter out;

  public static Object viaApplication(HttpServletRequest request, String attr_name)
  {
    return request.getSession().getServletContext().getAttribute(attr_name);
  }

  public static Object viaSession(HttpSession session, String attr_name)
  {
    return session.getAttribute(attr_name);
  }

  public static void viaSetSession(HttpSession session, String attr_name, Object attr_value)
  {
    session.setAttribute(attr_name, attr_value);
  }

  public static Object viaRequestAttribute(HttpServletRequest request, String attr_name)
  {
    return request.getAttribute(attr_name);
  }

  public static String viaRequestParameter(HttpServletRequest request, String attr_name)
  {
    return request.getParameter(attr_name);
  }

  public static void viaSetRequest(HttpServletRequest request, String attr_name, Object attr_value)
  {
    request.setAttribute(attr_name, attr_value);
  }

  public static String httpConnection(String toUrl, String param)
  {
    if ((param == null) || ("".equals(param)))
      return "";
    JSONObject jsonO = httpConnection(toUrl);

    return jsonO.getString(param);
  }

  public static JSONObject httpConnection(String toUrl)
  {
    try
    {
      URL url = new URL(toUrl);
      HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
      httpConn.setRequestMethod("GET");
      httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

      httpConn.setDoInput(true);
      httpConn.setDoOutput(true);

      httpConn.connect();

      InputStream inputStream = httpConn.getInputStream();
      Integer len = Integer.valueOf(inputStream.available());
      byte[] bytes = new byte[len.intValue()];
      inputStream.read(bytes);

      String returnV = new String(bytes, "UTF-8");

      return JSONObject.fromObject(returnV);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String sendPost(String url, String param)
  {
    PrintWriter out = null;
    BufferedReader in = null;
    String result = "";
    try {
      URL realUrl = new URL(url);

      URLConnection conn = realUrl.openConnection();

      conn.setRequestProperty("accept", "*/*");
      conn.setRequestProperty("connection", "Keep-Alive");
      conn.setRequestProperty("user-agent", 
        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

      conn.setDoOutput(true);
      conn.setDoInput(true);

      out = new PrintWriter(conn.getOutputStream());

      out.print(param);

      out.flush();

      in = new BufferedReader(
        new InputStreamReader(conn.getInputStream()));
      String line;
      while ((line = in.readLine()) != null)
      {
        //String line;
        result = result + line;
      }
    } catch (Exception e) {
      System.out.println("发送 POST 请求出现异常！" + e);
      e.printStackTrace();
      try
      {
        if (out != null) {
          out.close();
        }
        if (in != null)
          in.close();
      }
      catch (IOException ex)
      {
        ex.printStackTrace();
      }
    }
    finally
    {
      try
      {
        if (out != null) {
          out.close();
        }
        if (in != null)
          in.close();
      }
      catch (IOException ex)
      {
        ex.printStackTrace();
      }
    }
    return result;
  }

  public static PrintWriter getPrintWriter(HttpServletResponse response)
  {
    try
    {
      out = response.getWriter();
      return out;
    } catch (IOException e) {
      e.printStackTrace();
    }return null;
  }

  public static void getPrintWriter(HttpServletResponse response, String content)
  {
    try
    {
      out = getPrintWriter(response);
      out.write(content);
    } finally {
      closePrintWriter(out);
    }
  }

  public static void closePrintWriter(PrintWriter out)
  {
    if (out != null)
    {
      out.flush();
      out.close();
    }
  }
}
