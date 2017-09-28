package com.example.zr.gxapplication.net;

import com.shandianshua.totoro.data.net.model.Alimama;

import rx.Observable;

/**
 * Created by huchen on 2017/3/7.
 */
public class CustomDataService {

  private static final String PUB_ALIMAMA = "http://pub.alimama.com/items/";

  /**
   * 获取阿里妈妈优惠信息
   * 
   * @param goodUrl
   * @param time
   * @return
   */
  public static Observable<Alimama> getAlimamaData(String goodUrl, long time) {
    return RetrofitNetHelper.getCustomApi(PUB_ALIMAMA).getAlimamaData(goodUrl,
        time);
  }
}
