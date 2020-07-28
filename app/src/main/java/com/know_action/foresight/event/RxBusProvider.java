package com.know_action.foresight.event;


/**
 * Created by wanglei on 2016/12/22.
 */

public class RxBusProvider {

    private static RxBusImpl bus;

    public static RxBusImpl getBus() {
        if (bus == null) {
            synchronized (RxBusProvider.class) {
                if (bus == null) {
                    bus = RxBusImpl.get();
                }
            }
        }
        return bus;
    }

}
