package com.ikats.shop.services;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ikats.shop.App;
import com.ikats.shop.event.RxBusProvider;
import com.ikats.shop.event.RxMsgEvent;
import com.ikats.shop.model.HeartBeatBean;
import com.ikats.shop.utils.JWebSocketClient;
import com.ikats.shop.views.GlobalStateView;
import com.tamsiree.rxkit.RxDeviceTool;

import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import cn.droidlover.xdroidmvp.kit.Kits;

import static com.ikats.shop.views.GlobalStateView.SOCKET_CODE;

/**
 *
 */
public class JWebSocketClientService extends Service {
    private URI uri;
    public JWebSocketClient client;
    private JWebSocketClientBinder mBinder = new JWebSocketClientBinder();

    public static boolean isServiceRegister() {
        return isServiceRegister;
    }

    private static boolean isServiceRegister;

    //用于Activity和service通讯
    public class JWebSocketClientBinder extends Binder {
        public JWebSocketClientService getService() {
            return JWebSocketClientService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        reference.set(heartBeatRunnable);
        //初始化websocket
        initSocketClient();
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测
        isServiceRegister = true;
        Log.e("JWebSocketClientService", "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isServiceRegister = true;
        Log.e("JWebSocketClientService", "onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceRegister = false;
        Log.e("JWebSocketClientService", "onDestroy()");
    }

    private static final String mAddress = "wss://shop.%1$s.com/websocket/order/%2$s";//wss://sit.shop.chigoose.com/websocket/order/{name};

    /**
     * 初始化websocket连接
     */
    private void initSocketClient() {
        String mAddress = App.getSettingBean().shop_url.replace("https", "wss").concat("websocket/order/").concat(RxDeviceTool.getMacAddress()).replace(" ", "");
        URI uri = URI.create(mAddress);//测试使用
        client = new JWebSocketClient(uri) {
            @Override
            public void onMessage(String message) {
                receiveTime = System.currentTimeMillis();
                isConnect = true;
                RxMsgEvent msgEvent = new RxMsgEvent(SOCKET_CODE, GlobalStateView.TAG, isConnect);
                RxBusProvider.getBus().postEvent(msgEvent);
                Log.e("JWebSocketClientService", uri.toString() + "收到的消息：" + message);
                if (message.contains("heartbreak") && message.contains("serialNo")) {
                    HeartBeatBean beatBean = new Gson().fromJson(message, HeartBeatBean.class);
                    Log.e(TAG, "onMessage: --->" + message);
                } else if (message.contains("websocket") && message.contains("心跳")) {/*{"websocket":"心跳"}*/} else {
                    Intent intent = new Intent();//广播接收到的消息,在Activity   接收
                    intent.setAction("com.xxx.servicecallback.content");
                    intent.putExtra("message", message);
                    sendBroadcast(intent);
                }
            }

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                super.onOpen(handshakedata);
                super.onOpen(handshakedata);
                Log.e("JWebSocketClientService", "websocket连接成功");
            }
        };
        connect();
    }

    /**
     * 连接websocket
     */
    private void connect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    //connectBlocking多出一个等待操作，会先连接再发送，否则未连接发送会报错
                    client.connectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    //    -------------------------------------websocket心跳检测------------------------------------------------
    private static long HEART_BEAT_RATE = 10 * 1000;//每隔10秒进行一次对长连接的心跳检测
    private static Handler mHandler = new Handler();
    private static AtomicReference<Runnable> reference = new AtomicReference<>();
    private static long sendTime, receiveTime;
    private boolean isConnect;
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            Log.e("JWebSocketClientService", "心跳包检测websocket连接状态");
            if (client != null) {
                if (client.isClosed()) {
                    isConnect = false;
                    sendTime = receiveTime = 0;
                    reconnectWs();
                } else {
                    //业务逻辑 这里如果服务端需要心跳包为了防止断开 需要不断发送消息给服务端
                    if (Kits.Empty.check(orderSn)) {
//                        JsonObject jso = new JsonObject();
//                        jso.addProperty("messageType", "heartbreak");
//                        JsonObject sjso = new JsonObject();
//                        sjso.addProperty("serialNo", UUID.randomUUID().toString());
//                        sjso.addProperty("posCode", RxDeviceTool.getMacAddress());
//                        jso.add("data", sjso);
                        HeartBeatBean beatBean = HeartBeatBean.build(UUID.randomUUID().toString());
                        String json = new Gson().toJson(beatBean);
                        if (client.isOpen())
                            client.send(json);
                        Log.e(TAG, "send heartbeat to keep live: -->" + json);
                    } else {
                        JsonObject jso = new JsonObject();
                        jso.addProperty("messageType", "order.paymentStatus");
                        JsonObject sjso = new JsonObject();
                        sjso.addProperty("orderSn", orderSn);
                        sjso.addProperty("posCode", RxDeviceTool.getMacAddress());
                        jso.add("data", sjso);
                        if (client.isOpen())
                            client.send(jso.toString());
                    }

                    if (sendTime == 0 && receiveTime == 0) {
                        sendTime = receiveTime = System.currentTimeMillis();
                        isConnect = false;
                    } else {
/*                        if (receiveTime == 0)
                            sendTime = receiveTime;
                        else */
                        if ((receiveTime > sendTime)) {
                            isConnect = true;
                        } else {
                            isConnect = false;
                        }
                        sendTime = System.currentTimeMillis();
                    }
                }
            } else {
                //如果client已为空，重新初始化连接
                isConnect = false;
                sendTime = receiveTime = 0;
                initSocketClient();
            }

            //每隔一定的时间，对长连接进行一次心跳检测
            mHandler.postDelayed(this, HEART_BEAT_RATE);
            RxMsgEvent msgEvent = new RxMsgEvent(SOCKET_CODE, GlobalStateView.TAG, isConnect);
            RxBusProvider.getBus().postEvent(msgEvent);
        }
    };

    /**
     * 开启重连
     */
    private void reconnectWs() {
        mHandler.removeCallbacks(heartBeatRunnable);
        new Thread() {
            @Override
            public void run() {
                try {
                    Log.e("JWebSocketClientService", "开启重连");
                    client.reconnectBlocking();
                    mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private static String TAG = JWebSocketClientService.class.getCanonicalName();
    private static ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //服务与活动成功绑定
            Log.e(TAG, "服务与活动成功绑定");
            JWebSocketClientBinder binder = (JWebSocketClientBinder) iBinder;
            JWebSocketClientService jWebSClientService = binder.getService();
            JWebSocketClient sclient = jWebSClientService.client;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //服务与活动断开
            Log.e(TAG, "服务与活动成功断开");
        }
    };

    /**
     * 绑定服务
     */
    public static void bindService(Context context) {
        if (isServiceRegister) {
            unbindService(context);
        }
        Intent bindIntent = new Intent(context, JWebSocketClientService.class);
        context.bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    /**
     * 解绑服务
     */
    public static void unbindService(Context context) {
        context.unbindService(serviceConnection);
        mHandler.removeCallbacksAndMessages(null);
    }

    public static void setOrderSn(String orderSn) {
        JWebSocketClientService.orderSn = orderSn;
        mHandler.removeCallbacksAndMessages(null);
        if (Kits.Empty.check(orderSn)) {
            HEART_BEAT_RATE = 10 * 1000;
        } else {
            HEART_BEAT_RATE = 1 * 1000;
        }
        mHandler.postDelayed(reference.get(), HEART_BEAT_RATE);
    }

    private static String orderSn;
}
