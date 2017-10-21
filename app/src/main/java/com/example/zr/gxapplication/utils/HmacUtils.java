package com.example.zr.gxapplication.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author yaoyi@shandianshua.com (Yi Yao)
 */
public class HmacUtils {

  public enum Algorithm {
    HmacMD5, HmacSHA1, HmacSHA256
  }

  public static String hmacDigest(String msg, String keyString, Algorithm algorithm) {
    return hmacDigest(msg, keyString, algorithm.toString());
  }

  private static String hmacDigest(String msg, String keyString, String algo) {
    String digest = null;
    try {
      SecretKeySpec key = new SecretKeySpec((keyString).getBytes("utf-8"), algo);
      Mac mac = Mac.getInstance(algo);
      mac.init(key);

      byte[] bytes = mac.doFinal(msg.getBytes("utf-8"));

      StringBuilder hash = new StringBuilder();
      for (byte aByte : bytes) {
        String hex = Integer.toHexString(0xFF & aByte);
        if (hex.length() == 1) {
          hash.append('0');
        }
        hash.append(hex);
      }
      digest = hash.toString();
    } catch (UnsupportedEncodingException e) {
      if (LogUtils.isLogEnabled()) {
        e.printStackTrace();
      }
    } catch (InvalidKeyException e) {
      if (LogUtils.isLogEnabled()) {
        e.printStackTrace();
      }
    } catch (NoSuchAlgorithmException e) {
      if (LogUtils.isLogEnabled()) {
        e.printStackTrace();
      }
    }
    return digest;
  }
}
