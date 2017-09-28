package com.example.zr.gxapplication.net.interceptor;

import com.shandianshua.base.utils.RSAEncrypt;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;

/**
 * author: zhoulei date: 15/11/23.
 */
public class RSAResponseInterceptor implements Interceptor {
  public RSAPublicKey publicKey;

  public RSAResponseInterceptor(RSAPublicKey publicKey) {
    this.publicKey = publicKey;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    final Response origin = chain.proceed(chain.request());
    if (origin.isSuccessful()) {
      byte[] srcBody;
      try {
        srcBody = origin.body().bytes();
        if (srcBody.length <= 0) {
          return origin;
        }
        byte[] decryptedBody = RSAEncrypt.decryptPubKey(srcBody, publicKey);
        if (decryptedBody == null || decryptedBody.length <= 0) {
          return origin;
        }
        return origin.newBuilder()
            .body(ResponseBody.create(origin.body().contentType(), decryptedBody)).build();
      } catch (Exception e) {
        e.printStackTrace();
        throw new IOException(e);
      }
    } else {
      return origin;
    }
  }
}
