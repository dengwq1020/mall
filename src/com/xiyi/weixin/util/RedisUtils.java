package com.xiyi.weixin.util;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Tuple;




public class RedisUtils {
	private static Logger logger = LoggerFactory.getLogger(RedisUtils.class);

	private static ShardedJedisPool pool;
	
    static {
      
    }
    
    private synchronized static void init(){
    	if(pool==null){
    		pool=SpringConfigTool.getBean(ShardedJedisPool.class);
    	}
    }
    
    private static ShardedJedis getJedis(){
    	if(pool==null){
    		init();
    	}    	 
    	return pool.getResource();
    }
    

    public static String get(String key)
    {
    	ShardedJedis clientJedis=null;
    	try {
    		clientJedis=getJedis();
    		return clientJedis.get(key);
		} catch (Exception e) {
			logger.error("get error:", e);
		}
    	finally{
    		if(clientJedis!=null){
    			clientJedis.close();
    		}
    	}
    	return null;
    }
    
    public static boolean set(String key, String value)
    {
    	return set(key,value,-1L);
    }

    public static boolean set(String key, String value, Long sec)
    {
    	//sec = sec*1000;
    	ShardedJedis clientJedis=null;
    	try {
    		clientJedis=getJedis();
    	
	    	if(sec == null || sec <= 0){
	    		clientJedis.set(key, value);
				return true;
	    	}
			else{
				clientJedis.set(key, value);
				clientJedis.expire(key, sec.intValue());
				return true;
			}
    	} catch (Exception e) {
			logger.error("get error:", e);
		}
    	finally{
    		if(clientJedis!=null){
    			clientJedis.close();
    		}
    	}
    	return false;
    }
    

    public static boolean delete(String key)
    {
    	ShardedJedis clientJedis=null;
    	try {
    		clientJedis=getJedis();
    		return clientJedis.del(key)>0;
		} catch (Exception e) {
			logger.error("get error:", e);
		}
    	finally{
    		if(clientJedis!=null){
    			clientJedis.close();
    		}
    	}
    	return false;    	
    }
    

    public static void setBean(String key, Object obj) throws Exception{
    	setBean(key,obj,-1L);
	}
    
	public static void setBean(String key, Object obj, Long sec) throws Exception{
		String value = JsonUtils.objectToJson(obj);
		logger.debug("setBean key:{} value:{}",key,value);
		set(key, value ,sec);
	}
	
	public static <T> T getBean(String key, Class<T> t)  throws Exception{
		String value = get(key);
		logger.debug("getBean key:{} value:{}",key,value);
		if (null == value){
			return null;
		}		
		return JsonUtils.jsonToObject(value,t); 
	}
	
	public static long llen(String key)  throws Exception{
    	ShardedJedis clientJedis=null;
    	try {
    		clientJedis=getJedis();
    		return clientJedis.llen(key);
		} catch (Exception e) {
			logger.error("get error:", e);
		}
    	finally{
    		if(clientJedis!=null){
    			clientJedis.close();
    		}
    	}
    	return -1; 
	}
	 public static String lPop(String key)
	 {
	    	ShardedJedis clientJedis=null;
	    	try {
	    		clientJedis=getJedis();
	    		return clientJedis.lpop(key);
			} catch (Exception e) {
				logger.error("get error:", e);
			}
	    	finally{
	    		if(clientJedis!=null){
	    			clientJedis.close();
	    		}
	    	}
	    	return "-1";    	
	  }
	 public static String RPop(String key)
	 {
	    	ShardedJedis clientJedis=null;
	    	try {
	    		clientJedis=getJedis();
	    		return clientJedis.rpop(key);
			} catch (Exception e) {
				logger.error("get error:", e);
			}
	    	finally{
	    		if(clientJedis!=null){
	    			clientJedis.close();
	    		}
	    	}
	    	return "-1";    	
	  }
	 public static long lPush(String key,String value)
	 {
	    	ShardedJedis clientJedis=null;
	    	try {
	    		clientJedis=getJedis();
	    		return clientJedis.lpush(key, value);
			} catch (Exception e) {
				logger.error("get error:", e);
			}
	    	finally{
	    		if(clientJedis!=null){
	    			clientJedis.close();
	    		}
	    	}
	    	return -1;    	
	  }
	 public static void del(String key)
	 {
		 ShardedJedis clientJedis=null;
	    	try {
	    		clientJedis=getJedis();
	    		clientJedis.del(key);
			} catch (Exception e) {
				logger.error("get error:", e);
			}
	    	finally{
	    		if(clientJedis!=null){
	    			clientJedis.close();
	    		}
	    	}
	 
	 }
	 //======================================================
	 public static long zcount(String key,String min,String max) throws Exception{
		 ShardedJedis clientJedis=null;
	    	try {
	    		clientJedis=getJedis();
	    		return clientJedis.zcount(key, min, max);
			} catch (Exception e) {
				logger.error("get error:", e);
				throw e;
			}
	    	finally{
	    		if(clientJedis!=null){
	    			clientJedis.close();
	    		}
	    	}
	 }
	 
	 public static Set<Tuple> zrangeWithScores(String key,long start,long end) throws Exception{
		 ShardedJedis clientJedis=null;
	    	try {
	    		clientJedis=getJedis();
	    		return clientJedis.zrangeWithScores(key, start, end);
			} catch (Exception e) {
				logger.error("get error:", e);
				throw e;
			}
	    	finally{
	    		if(clientJedis!=null){
	    			clientJedis.close();
	    		}
	    	}
	 }
	 
	 public static Set<Tuple> zrevrangeWithScores(String key,long start,long end) throws Exception{
		 ShardedJedis clientJedis=null;
	    	try {
	    		clientJedis=getJedis();
	    		return clientJedis.zrevrangeWithScores(key, start, end);
			} catch (Exception e) {
				logger.error("get error:", e);
				throw e;
			}
	    	finally{
	    		if(clientJedis!=null){
	    			clientJedis.close();
	    		}
	    	}
	 }
	 
	 public static Double zincrby(String key,long score,String member) throws Exception{
		 ShardedJedis clientJedis=null;
	    	try {
	    		clientJedis=getJedis();
	    		return clientJedis.zincrby(key, score, member);
			} catch (Exception e) {
				logger.error("get error:", e);
				throw e;
			}
	    	finally{
	    		if(clientJedis!=null){
	    			clientJedis.close();
	    		}
	    	}
	 }
	 //======================================================
	 
}
