package com.example.zr.gxapplication.net.interceptor;

import com.shandianshua.base.utils.RSAEncrypt;
import com.shandianshua.totoro.utils.net.RequestBodyUtil;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;

/**
 * author: zhoulei date: 15/11/23.
 */
public class RSARequestInterceptor implements Interceptor {
  private final RSAPublicKey publicKey;

  public RSARequestInterceptor(RSAPublicKey publicKey) {
    this.publicKey = publicKey;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    final Request origin = chain.request();
    if (RequestBodyUtil.hasRequestBody(origin)) {
      try {
        byte[] originBody = RequestBodyUtil.readBody(origin);

        byte[] encryptedBody = RSAEncrypt.encryptPubKey(originBody, publicKey);
        Request encrypted = origin.newBuilder().method(origin.method(),
            RequestBody.create(MediaType.parse("application/octet-stream; charset=UTF-8"),
                encryptedBody))
            .build();
        return chain.proceed(encrypted);
      } catch (Exception e) {
        e.printStackTrace();
        throw new IOException(e);
      }
    } else {
      return chain.proceed(origin);
    }
  }
}
