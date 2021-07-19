package com.peixueshi.crm.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Util {
    /**
     * Determine encrypt algorithm MD5
     */
    private static final String ALGORITHM_MD5 = "MD5";
    /**
     * UTF-8 Encoding
     */
    private static final String UTF_8 = "UTF-8";


    /**
     * 32位MD5值
     *
     * @param s
     * @return
     */
    public static String md5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            String encodeData = Base64.encode(md, 0, md.length - 1);
            return encodeData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static final InputStream byte2Input(byte[] buf) {
        return new ByteArrayInputStream(buf);
    }

    public static final byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

	private static final int RADIX_16 = 16;//16进制
	public static String HASH_ALGORITHM = "MD5";

	public static String generate(String str) {
		byte[] md5 = getMD5(str.getBytes());
		BigInteger abs = new BigInteger(md5).abs();
		return abs.toString(Character.MAX_RADIX);
	}

	/**
	 * 方式一：<br>
	 * byte[] or byte转换16进制字符串<br>
	 * 使用Integer.toHexString(int)
	 * @param str
	 * @return
	 */
	public static String hexString(String str) {
		StringBuilder sb = new StringBuilder();
		byte[] md5 = getMD5(str.getBytes());
		for (int i = 0; i < md5.length; i++) {
			int temp = md5[i] & 0Xff;
			String hexString = Integer.toHexString(temp);
			if (hexString.length() < 2) {
				sb.append("0").append(hexString);
			} else {
				sb.append(hexString);
			}
		}
		return sb.toString();
	}

	private static byte[] getMD5(byte[] bytes) {
		byte[] tmp = null;
		try {
			MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
			digest.update(bytes);
			tmp = digest.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return tmp;
	}



	/**
	 * 32位MD5加密
	 * @param content -- 待加密内容
	 * @return
	 */
	public static String md5Decode32(String content) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("NoSuchAlgorithmException",e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UnsupportedEncodingException", e);
		}
		//对生成的16字节数组进行补零操作
		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10){
				hex.append("0");
			}
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

	/**
	 * 2字节的二进制数转换为int16整数
	 * @param bytes 2字节的二进制数
	 * @return int16整数
	 */
	public static int byteToint16(byte[] bytes){
		int res = ((bytes[0]<<8)|((bytes[1]<<24)>>>24));
		return res;
	}


    /**
     * 将byte[]转为各种进制的字符串
     *
     * @param bytes byte[]
     * @param radix 基数可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String binary(byte[] bytes, int radix) {
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
    }

    /**
     * 16位MD5
     *
     * @param readyEncryptStr
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static final String getMD5_16(String readyEncryptStr) throws NoSuchAlgorithmException {
        if (readyEncryptStr != null) {
            return md5(readyEncryptStr).substring(8, 24);
        } else {
            return null;
        }
    }

}
