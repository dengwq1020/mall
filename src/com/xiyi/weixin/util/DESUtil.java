package com.xiyi.weixin.util;

import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;


public class DESUtil {
	public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
	
	public final static String KEY = "qjqj=(!@#$%^&*)=yyqj";
		//加密
		public static byte[] desEncrypt(byte[] plainText, String KEY) throws Exception {
			SecureRandom sr = new SecureRandom();
			sr.setSeed(KEY.getBytes());
			DESKeySpec dks = new DESKeySpec(KEY.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key, sr);
			byte data[] = plainText;
			byte encryptedData[] = cipher.doFinal(data);
			return encryptedData;
		}

		/**
	     * DES算法，加密
	     *
	     * @param data 待加密字符串
	     * @param key  加密私钥，长度不能够小于8位
	     * @return 加密后的字节数组，一般结合Base64编码使用
	     * @throws InvalidAlgorithmParameterException 
	     * @throws Exception 
	     */
	    public static String encode(String key,String data) {
	    	if(data == null)
	    		return null;
	    	try{
		    	DESKeySpec dks = new DESKeySpec(key.getBytes());	    	
		    	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		        //key的长度不能够小于8位字节
		        Key secretKey = keyFactory.generateSecret(dks);
		        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
		        IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
		        AlgorithmParameterSpec paramSpec = iv;
		        cipher.init(Cipher.ENCRYPT_MODE, secretKey,paramSpec);           
		        byte[] bytes = cipher.doFinal(data.getBytes());            
		        return byte2hex(bytes);
	    	}catch(Exception e){
	    		e.printStackTrace();
	    		return data;
	    	}
	    }
	    
	    /**
		 * 二行制转字符串
		 * @param b
		 * @return
		 */
	    private static String byte2hex(byte[] b) {
			StringBuilder hs = new StringBuilder();
			String stmp;
			for (int n = 0; b!=null && n < b.length; n++) {
				stmp = Integer.toHexString(b[n] & 0XFF);
				if (stmp.length() == 1)
					hs.append('0');
				hs.append(stmp);
			}
			return hs.toString().toUpperCase();
		}
	    
		//解密
		public static byte[] desDecrypt(byte[] encryptText, String KEY) throws Exception {
			SecureRandom sr = new SecureRandom();
			DESKeySpec dks = new DESKeySpec(KEY.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key, sr);
			byte encryptedData[] = encryptText;
			byte decryptedData[] = cipher.doFinal(encryptedData);
			return decryptedData;
		}

		//加密
		public static String encrypt(String input, String KEY) throws Exception {
			byte[] encryptedData = desEncrypt(input.getBytes("utf-8"), KEY);
			String a = byteArr2HexStr(encryptedData);
			return a;
		}

		//解密
		public static String decrypt(String input, String KEY) throws Exception {
			byte[] b = hexStr2ByteArr(input);
			return new String(desDecrypt(b,KEY),"utf-8");
		}

		

		/**
		 * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[] hexStr2ByteArr(String strIn) 互为可逆的转换过程
		 * 
		 * @param arrB
		 *            需要转换的byte数组
		 * @return 转换后的字符串
		 * @throws Exception
		 *             本方法不处理任何异常，所有异常全部抛出
		 */
		public static String byteArr2HexStr(byte[] arrB) throws Exception {
			int iLen = arrB.length;
			// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
			StringBuffer sb = new StringBuffer(iLen * 2);
			for (int i = 0; i < iLen; i++) {
				int intTmp = arrB[i];
				// 把负数转换为正数
				while (intTmp < 0) {
					intTmp = intTmp + 256;
				}
				// 小于0F的数需要在前面补0
				if (intTmp < 16) {
					sb.append("0");
				}
				sb.append(Integer.toString(intTmp, 16));
			}
			return sb.toString();
		}

		/**
		 * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB) 互为可逆的转换过程
		 * 
		 * @param strIn
		 *            需要转换的字符串
		 * @return 转换后的byte数组
		 * @throws Exception
		 *             本方法不处理任何异常，所有异常全部抛出
		 */
		public static byte[] hexStr2ByteArr(String strIn) throws Exception {
			byte[] arrB = strIn.getBytes();
			int iLen = arrB.length;

			// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
			byte[] arrOut = new byte[iLen / 2];
			for (int i = 0; i < iLen; i = i + 2) {
				String strTmp = new String(arrB, i, 2);
				arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
			}
			return arrOut;
		}
		
		 private static byte[] hex2byte(byte[] b) {
		        if((b.length%2)!=0)
		            throw new IllegalArgumentException();
				byte[] b2 = new byte[b.length/2];
				for (int n = 0; n < b.length; n+=2) {
				    String item = new String(b,n,2);
				    b2[n/2] = (byte)Integer.parseInt(item,16);
				}
		        return b2;
		    }
		 /**
	     * DES算法，解密
	     *
	     * @param data 待解密字符串
	     * @param key  解密私钥，长度不能够小于8位
	     * @return 解密后的字节数组
	     * @throws Exception 异常
	     */
	    public static String decode(String key,String data) {
	    	if(data == null)
	    		return null;
	        try {
		    	DESKeySpec dks = new DESKeySpec(key.getBytes());
		    	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	            //key的长度不能够小于8位字节
	            Key secretKey = keyFactory.generateSecret(dks);
	            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
	            IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
	            AlgorithmParameterSpec paramSpec = iv;
	            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
	            return new String(cipher.doFinal(hex2byte(data.getBytes())));
	        } catch (Exception e){
	    		e.printStackTrace();
	    		return data;
	        }
	    }
		
		public static void main(String args[]) {
			//密钥(约定)
			String str = "A393570EDC36F6622901E128A8B74092";
			try {
				//加密
				String str2 = DESUtil.encrypt("15050555155",str);
				System.out.println(str2);
				
				/**
				 *  str2就是加密后的手机号，用字符串加上密钥解密
				 */
				
				//解密
				try{
				String str1 = DESUtil.decrypt(str2, str);
				System.out.println(str1);
				}catch(Exception e){
					System.err.println("解密失败");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
