package com.ikats.shop.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.blankj.utilcode.util.ToastUtils;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.INT_PTR;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_FILECOND;
import com.hikvision.netsdk.NET_DVR_FINDDATA_V30;
import com.hikvision.netsdk.NET_DVR_IPPARACFG_V40;
import com.hikvision.netsdk.NET_DVR_PLAYBACK_INFO;
import com.hikvision.netsdk.NET_DVR_PREVIEWINFO;
import com.hikvision.netsdk.NET_DVR_STREAM_INFO;
import com.hikvision.netsdk.NET_DVR_TIME;
import com.hikvision.netsdk.NET_DVR_VOD_PARA;
import com.hikvision.netsdk.PlaybackCallBack;
import com.hikvision.netsdk.PlaybackControlCommand;
import com.hikvision.netsdk.RealPlayCallBack;
import com.ikats.shop.App;
import com.ikats.shop.BuildConfig;

import org.MediaPlayer.PlayM4.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/2/28.
 * 封装的海康播放库
 */
public class PlayerHikvision {
    private static final String TAG = "Hikvision";
    public static final int HIK_MAIN_STREAM_CODE = 0;      //主码流
    public static final int HIK_SUB_STREAM_CODE = 1;      //子码流
    public int mLogId = -1;
    public int mPort = -1;
    public int mPlayId = -1;
    public int mPlaybackId = -1;
    public int mGetFileId = -1;
    private SurfaceView mSurfaceView;
    public boolean mStopPlayback = true;
    private Handler handler;
    private int chanNo;
    private boolean mRecord;

    public boolean isLive() {
        return isLive;
    }

    private boolean isLive;//是否预处于览状态

    public boolean isplayback() {
        return isplayback;
    }

    private boolean isplayback;//是否处于回放状态

    SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            Log.i(TAG, "surface is created" + mPort);
            if (-1 == mPlayId) {
                return;
            }
            Surface surface = holder.getSurface();
            if (surface.isValid()) {
                if (!Player.getInstance().setVideoWindow(mPort, 0, holder)) {
                    Log.e(TAG, "播放器设置显示区域失败!");
                } else {
                    if (BuildConfig.DEBUG)
                        ToastUtils.showLong("播放器设置显示区域成功!");
                }
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.e(TAG, "Player setVideoWindow release!" + mPort);
            if (-1 == mPort) {
                return;
            }
            if (holder.getSurface().isValid()) {
                if (!Player.getInstance().setVideoWindow(mPort, 0, null)) {
                    Log.e(TAG, "播放器销毁显示区域失败!");
                } else {
                    if (BuildConfig.DEBUG)
                        ToastUtils.showLong("播放器销毁显示区域成功!");
                }
            }
        }
    };


    public PlayerHikvision(SurfaceView surfaceView, Handler handler) {
        this.mSurfaceView = surfaceView;
        this.handler = handler;
        mSurfaceView.getHolder().addCallback(callback);
    }

    /**
     * 登录设备
     *
     * @param ip
     * @param port
     * @param userName
     * @param password
     * @param channelNo
     */
    private void login(String ip, int port, String userName, String password, int channelNo, Runnable runnable) {
        Executors.newSingleThreadExecutor().execute(() -> {

            HCNetSDK.getInstance().NET_DVR_Init();  //初始化海康的Device Network SDK
            NET_DVR_DEVICEINFO_V30 oNetDvrDeviceInfoV30 = new NET_DVR_DEVICEINFO_V30();  //设备参数
            mLogId = HCNetSDK.getInstance().NET_DVR_Login_V30(ip, port, userName, password, oNetDvrDeviceInfoV30);  //调用设备登录接口
            Log.i(TAG, "logId:" + mLogId);
            if (mLogId == -1) {  //登录失败
                int errorCode = HCNetSDK.getInstance().NET_DVR_GetLastError();  //得到错误码，并根据错误码得到具体原因
                switch (errorCode) {
                    case 1:
                        messageFeedback("连接设备失败：设备的用户名或者密码错误");  //设备的用户名或者密码错误
                        break;
                    case 7:
                        messageFeedback("连接设备失败：设备离线或者连接超时");  //连接设备失败：设备离线或者连接超时
                        break;
                    default:
                        messageFeedback("连接设备失败：其他错误");  //连接设备失败：其他错误
                        break;
                }
            } else {  //成功登录设备，动态换算通道号
                NET_DVR_IPPARACFG_V40 net_dvr_ipparacfg_v40 = new NET_DVR_IPPARACFG_V40();  //IP设备资源和IP通道资源配置
                HCNetSDK.getInstance().NET_DVR_GetDVRConfig(mLogId, HCNetSDK.NET_DVR_GET_IPPARACFG_V40, 0, net_dvr_ipparacfg_v40);  //获取设备设置信息
                Log.i(TAG, "模拟通道数：" + net_dvr_ipparacfg_v40.dwAChanNum);
                Log.i(TAG, "IP通道数：" + net_dvr_ipparacfg_v40.dwDChanNum);
                if (net_dvr_ipparacfg_v40.dwAChanNum == 0 && net_dvr_ipparacfg_v40.dwDChanNum == 0) {
                    chanNo = channelNo;
                } else {
                    if (channelNo > net_dvr_ipparacfg_v40.dwAChanNum) {
                        chanNo = (channelNo - net_dvr_ipparacfg_v40.dwAChanNum) + 32;
                    } else {
                        chanNo = channelNo;
                    }
                }
//            if (oNetDvrDeviceInfoV30.byChanNum > 0) {
//                chanNo = oNetDvrDeviceInfoV30.byStartChan;
//            } else if (oNetDvrDeviceInfoV30.byIPChanNum > 0) {
//                chanNo = oNetDvrDeviceInfoV30.byStartDChan;
//            }
                Log.i(TAG, "网络设备登录成功!");
                if (runnable != null)
                    handler.post(runnable);
            }
        });
    }

    /**
     * 实时预览
     *
     * @param ip
     * @param port
     * @param userName
     * @param password
     * @param streamType
     * @param channelNo
     */
    public void live(final String ip, final int port, final String userName, final String password, final int streamType, final int channelNo) {
        Runnable run = () -> {
            if (mLogId != -1) {  //已经登录设备，下一步：实时预览
                RealPlayCallBack fRealDataCallBack = getRealPlayerCbf();  //得到实时预览回调
                if (fRealDataCallBack == null) {
                    Log.e(TAG, "实时预览回调对象创建失败");
                    messageFeedback("实时预览失败");
                    return;
                }

                NET_DVR_PREVIEWINFO previewInfo = new NET_DVR_PREVIEWINFO();  //实时预览设置
                previewInfo.lChannel = chanNo;  //通道号
                previewInfo.dwStreamType = streamType;  //码流类型0：主码，1：子码
                previewInfo.bBlocked = 1;  //阻塞流获取
                mPlayId = HCNetSDK.getInstance().NET_DVR_RealPlay_V40(mLogId, previewInfo, fRealDataCallBack);  //调用实时预览接口
//                mRecord = HCNetSDK.getInstance().NET_DVR_StartDVRRecord(mLogId, chanNo,0 );  //调用实时录制接口
                if (mPlayId == -1) {//实时预览失败
                    if (streamType == 1) {
                        previewInfo.dwStreamType = 0;  //尝试换一种码流播放
                        mPlayId = HCNetSDK.getInstance().NET_DVR_RealPlay_V40(mLogId, previewInfo, fRealDataCallBack);
                        if (mPlayId == -1) {
                            Log.e(TAG, "实时预览失败，错误码：" + HCNetSDK.getInstance().NET_DVR_GetLastError());
                            messageFeedback("实时预览失败");
                            return;
                        } else {
                            //录制状态
                            isLive = true;
                        }
                    } else {
                        Log.e(TAG, "实时预览失败，错误码：" + HCNetSDK.getInstance().NET_DVR_GetLastError());
                        messageFeedback("实时预览失败");
                        return;
                    }
                } else {
                    //录制
                    isLive = true;
                }
            }
        };
        if (mLogId == -1) {//未登录设备，先登录设备
            login(ip, port, userName, password, channelNo, run);
        } else {
            handler.post(run);
        }
    }

    /**
     * 远程回放
     *
     * @param ip
     * @param port
     * @param userName
     * @param password
     * @param channelNo
     * @param beginYear
     * @param beginMonth
     * @param beginDay
     * @param beginHour
     * @param beginMinute
     * @param beginSecond
     * @param endYear
     * @param endMonth
     * @param endDay
     * @param endHour
     * @param endMinute
     * @param endSecond
     */
    public void playback(String ip, int port, String userName, String password, int channelNo,
                         int beginYear, int beginMonth, int beginDay, int beginHour, int beginMinute, int beginSecond,
                         int endYear, int endMonth, int endDay, int endHour, int endMinute, int endSecond) {
        final Runnable[] runs = new Runnable[1];
        Runnable run = () -> {
            if (mLogId != -1) {  //已经登录设备，下一步：远程回放
                PlaybackCallBack fPlaybackCallBack = getPlaybackPlayerCbf();  //得到远程回放回调
                if (fPlaybackCallBack == null) {
                    Log.e(TAG, "远程回放回调对象创建失败");
                    messageFeedback("远程回放失败");
                    return;
                }
                NET_DVR_TIME beginTime = new NET_DVR_TIME();
                NET_DVR_TIME endTime = new NET_DVR_TIME();
                beginTime.dwYear = beginYear;
                beginTime.dwMonth = beginMonth;
                beginTime.dwDay = beginDay;
                beginTime.dwHour = beginHour;
                beginTime.dwMinute = beginMinute;
                beginTime.dwSecond = beginSecond;
                endTime.dwYear = endYear;
                endTime.dwMonth = endMonth;
                endTime.dwDay = endDay;
                endTime.dwHour = endHour;
                endTime.dwMinute = endMinute;
                endTime.dwSecond = endSecond;
                NET_DVR_VOD_PARA net_dvr_vod_para = new NET_DVR_VOD_PARA();
                net_dvr_vod_para.struBeginTime = beginTime;
                net_dvr_vod_para.struEndTime = endTime;
                NET_DVR_STREAM_INFO info = new NET_DVR_STREAM_INFO();
                info.dwChannel = chanNo;
                net_dvr_vod_para.struIDInfo = info;
                mPlaybackId = HCNetSDK.getInstance().NET_DVR_PlayBackByTime_V40(mLogId, net_dvr_vod_para);  //调用远程回放接口
                Log.e(TAG, "playback: ---->" + beginMinute + "," + beginSecond + "," + endMinute + "," + endSecond);
                if (mPlaybackId != -1) {
                    if (!HCNetSDK.getInstance().NET_DVR_SetPlayDataCallBack(mPlaybackId, fPlaybackCallBack)) {
                        Log.e(TAG, "设置远程回放回调失败，错误码：" + HCNetSDK.getInstance().NET_DVR_GetLastError());
                        messageFeedback("远程回放失败");
                        handler.postDelayed(runs[0], 1000);
                        return;
                    }
                    NET_DVR_PLAYBACK_INFO net_dvr_playback_info = null;
                    if (!HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(mPlaybackId, PlaybackControlCommand.NET_DVR_PLAYSTART, null, 0, net_dvr_playback_info)) {
                        Log.e(TAG, "远程回放开始失败，错误码：" + HCNetSDK.getInstance().NET_DVR_GetLastError());
                        messageFeedback("远程回放失败");
                        handler.postDelayed(runs[0], 1000);
                        return;
                    }
                    mStopPlayback = false;
                    isplayback = true;
                } else {
                    Log.e(TAG, "远程回放失败，错误码：" + HCNetSDK.getInstance().NET_DVR_GetLastError());
                    messageFeedback("远程回放失败");
                }
            }
        };
        runs[0] = run;
        if (mLogId == -1) {  //未登录设备，先登录设备
            login(ip, port, userName, password, channelNo, run);
        } else {
            handler.post(run);
        }

    }

    /**
     * 下载回放
     *
     * @param ip
     * @param port
     * @param userName
     * @param password
     * @param channelNo
     * @param beginYear
     * @param beginMonth
     * @param beginDay
     * @param beginHour
     * @param beginMinute
     * @param beginSecond
     * @param endYear
     * @param endMonth
     * @param endDay
     * @param endHour
     * @param endMinute
     * @param endSecond
     */
    public void downloadback(String ip, int port, String userName, String password, int channelNo,
                             int beginYear, int beginMonth, int beginDay, int beginHour, int beginMinute, int beginSecond,
                             int endYear, int endMonth, int endDay, int endHour, int endMinute, int endSecond) {
        final Runnable[] runs = new Runnable[1];
        Runnable run = () -> {
            if (mLogId != -1) {  //已经登录设备，下一步：远程回放
                NET_DVR_TIME beginTime = new NET_DVR_TIME();
                NET_DVR_TIME endTime = new NET_DVR_TIME();
                beginTime.dwYear = beginYear;
                beginTime.dwMonth = beginMonth;
                beginTime.dwDay = beginDay;
                beginTime.dwHour = beginHour;
                beginTime.dwMinute = beginMinute;
                beginTime.dwSecond = beginSecond;
                endTime.dwYear = endYear;
                endTime.dwMonth = endMonth;
                endTime.dwDay = endDay;
                endTime.dwHour = endHour;
                endTime.dwMinute = endMinute;
                endTime.dwSecond = endSecond;

//                Executors.newSingleThreadScheduledExecutor().execute(()->{});
//                NET_DVR_FILECOND net_dvr_filecond = new NET_DVR_FILECOND();  //被搜索录像文件信息
//                net_dvr_filecond.lChannel = chanNo;  //通道号
//                net_dvr_filecond.dwFileType = 0xff;  //录像文件类型 0xff代表所有
//                net_dvr_filecond.dwIsLocked = 0xff;  //是否锁定 0xff代表所有
//                net_dvr_filecond.dwUseCardNo = 0;  //是否使用卡号搜索
//                net_dvr_filecond.struStartTime = beginTime;
//                net_dvr_filecond.struStopTime = endTime;
//                int findFileId = HCNetSDK.getInstance().NET_DVR_FindFile_V30(mLogId, net_dvr_filecond);  //调用搜索录像文件接口
//                if (findFileId != -1) {  //查询录像文件成功
//                    for (int i = 0; i < 4000; i++) {  //最多4000份录像段文件
//                        NET_DVR_FINDDATA_V30 net_dvr_finddata_v30 = new NET_DVR_FINDDATA_V30();
//                        int iRet = HCNetSDK.getInstance().NET_DVR_FindNextFile_V30(findFileId, net_dvr_finddata_v30);
//                        if (-1 == iRet) {  //调用失败
//                            break;
//                        } else {
//                            /**
//                             NET_DVR_FILE_SUCCESS 1000 获取文件信息成功
//                             NET_DVR_FILE_NOFIND 1001 未查找到文件
//                             NET_DVR_ISFINDING 1002 正在查找请等待
//                             NET_DVR_NOMOREFILE 1003 没有更多的文件，查找结束
//                             NET_DVR_FILE_EXCEPTION 1004 查找文件时异常
//                             */
//                            if (iRet == 1000) {  //文件信息获取成功
//                                recordList.add(net_dvr_finddata_v30);
//                            } else if (iRet == 1003) {  //无更多文件
//                                break;
//                            }
//                            //还有一些其他状态，这里不可任何处理，既不加上该录像段文件，也不跳出循环
//                        }
//                    }
//                } else {
//                    Log.e(TAG, "查询录像失败，错误码：" + HCNetSDK.getInstance().NET_DVR_GetLastError());
//                }



                mGetFileId = HCNetSDK.getInstance().NET_DVR_GetFileByTime(mLogId, chanNo, beginTime, endTime, new File(App.getApp().getExternalCacheDir(), "123.mp4").getAbsolutePath());
                Log.e(TAG, "downloadback: ---->" + beginMinute + "," + beginSecond + "," + endMinute + "," + endSecond);
                if (mGetFileId != -1) {
                    boolean downloadback = HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(mGetFileId, HCNetSDK.NET_DVR_PLAYSTART, null, 0, null);
                    if (downloadback) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int nPos = HCNetSDK.getInstance().NET_DVR_GetDownloadPos(mGetFileId);
                                if (nPos > 100) {
                                    HCNetSDK.getInstance().NET_DVR_StopGetFile(mGetFileId);
                                    mGetFileId = (-1);
                                    Log.e(TAG, "run: 由于网络原因或DVR忙,下载异常终止!");
                                    return;
                                }
                                if (nPos == 100) {
                                    boolean stop = HCNetSDK.getInstance().NET_DVR_StopGetFile(mGetFileId);
                                    if (stop) {
                                        mGetFileId = (-1);
                                        Log.e(TAG, "run: 按时间下载结束！");
                                        messageFeedback("远程下载完成");
                                    } else {
                                        INT_PTR intPtr = new INT_PTR();
                                        intPtr.iValue = HCNetSDK.getInstance().NET_DVR_GetLastError();
                                        Log.e(TAG, "结束远程下载失败，错误码：" + intPtr.iValue);
                                        messageFeedback(HCNetSDK.getInstance().NET_DVR_GetErrorMsg(intPtr));
                                    }
                                    return;
                                } else {
                                    handler.postDelayed(this::run, 5000);
                                }
                            }
                        }, 5000);
                    } else {
                        cleanup();
                        handler.postDelayed(runs[0], 1000);
                    }
                } else {
                    Log.e(TAG, "远程下载失败，错误码：" + HCNetSDK.getInstance().NET_DVR_GetLastError());
                    messageFeedback("远程下载失败！");
                }
            } else {
                login(ip, port, userName, password, channelNo, runs[0]);
            }
        };
        runs[0] = run;
        if (mLogId == -1) {  //未登录设备，先登录设备
            login(ip, port, userName, password, channelNo, run);
        } else {
            handler.post(run);
        }

    }


    /**
     * 获取录像段文件
     *
     * @param ip
     * @param port
     * @param userName
     * @param password
     * @param channelNo
     * @param beginYear
     * @param beginMonth
     * @param beginDay
     * @param beginHour
     * @param beginMinute
     * @param beginSecond
     * @param endYear
     * @param endMonth
     * @param endDay
     * @param endHour
     * @param endMinute
     * @param endSecond
     * @return
     */
    public List<NET_DVR_FINDDATA_V30> getRecordFile(String ip, int port, String userName, String password, int channelNo,
                                                    int beginYear, int beginMonth, int beginDay, int beginHour, int beginMinute, int beginSecond,
                                                    int endYear, int endMonth, int endDay, int endHour, int endMinute, int endSecond) {
        List<NET_DVR_FINDDATA_V30> recordList = new ArrayList<NET_DVR_FINDDATA_V30>();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                if (mLogId != -1) {
                    NET_DVR_FILECOND net_dvr_filecond = new NET_DVR_FILECOND();  //被搜索录像文件信息
                    net_dvr_filecond.lChannel = chanNo;  //通道号
                    net_dvr_filecond.dwFileType = 0xff;  //录像文件类型 0xff代表所有
                    net_dvr_filecond.dwIsLocked = 0xff;  //是否锁定 0xff代表所有
                    net_dvr_filecond.dwUseCardNo = 0;  //是否使用卡号搜索
                    NET_DVR_TIME beginTime = new NET_DVR_TIME();
                    NET_DVR_TIME endTime = new NET_DVR_TIME();
                    beginTime.dwYear = beginYear;
                    beginTime.dwMonth = beginMonth;
                    beginTime.dwDay = beginDay;
                    beginTime.dwHour = beginHour;
                    beginTime.dwMinute = beginMinute;
                    beginTime.dwSecond = beginSecond;
                    endTime.dwYear = endYear;
                    endTime.dwMonth = endMonth;
                    endTime.dwDay = endDay;
                    endTime.dwHour = endHour;
                    endTime.dwMinute = endMinute;
                    endTime.dwSecond = endSecond;
                    net_dvr_filecond.struStartTime = beginTime;
                    net_dvr_filecond.struStopTime = endTime;
                    int findFileId = HCNetSDK.getInstance().NET_DVR_FindFile_V30(mLogId, net_dvr_filecond);  //调用搜索录像文件接口
                    if (findFileId != -1) {  //查询录像文件成功
                        for (int i = 0; i < 4000; i++) {  //最多4000份录像段文件
                            NET_DVR_FINDDATA_V30 net_dvr_finddata_v30 = new NET_DVR_FINDDATA_V30();
                            int iRet = HCNetSDK.getInstance().NET_DVR_FindNextFile_V30(findFileId, net_dvr_finddata_v30);
                            if (-1 == iRet) {  //调用失败
                                break;
                            } else {
                                if (iRet == 1000) {  //文件信息获取成功
                                    recordList.add(net_dvr_finddata_v30);
                                } else if (iRet == 1003) {  //无更多文件
                                    break;
                                }
                                //还有一些其他状态，这里不可任何处理，既不加上该录像段文件，也不跳出循环
                            }
                        }
                    } else {
                        Log.e(TAG, "查询录像失败，错误码：" + HCNetSDK.getInstance().NET_DVR_GetLastError());
                    }
                }
            }
        };
        if (mLogId == -1) {  //未登录设备，先登录设备
            login(ip, port, userName, password, channelNo, run);
        } else {
            handler.post(run);
        }
        return recordList;
    }

    /**
     * 停止实时预览
     *
     * @param playId
     * @param port
     */
    public void stopLive(int playId, int port) {
        capturePicture();
        if (playId != -1) {
            if (HCNetSDK.getInstance().NET_DVR_StopRealPlay(playId)) {
                Player.getInstance().stopSound();
                if (Player.getInstance().stop(port)) {
                    if (Player.getInstance().closeStream(port)) {
                        if (Player.getInstance().freePort(port)) {
                            Log.i(TAG, "停止实时预览成功");
                        } else {
                            Log.e(TAG, "停止实时预览，调用freePort接口失败，错误码：" + Player.getInstance().getLastError(port));
                        }
                    } else {
                        Log.e(TAG, "停止实时预览，关闭流失败，错误码：" + Player.getInstance().getLastError(port));
                    }
                } else {
                    Log.e(TAG, "停止实时预览，调用stop接口失败，错误码：" + Player.getInstance().getLastError(port));
                }
            } else {
                Log.e(TAG, "调用停止实时预览接口失败，错误码：" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            }
        }
//        if(mRecord){
//            HCNetSDK.getInstance().NET_DVR_StopDVRRecord(mLogId,chanNo);
//            mRecord = false;
//        }
        isLive = false;
    }

    /**
     * 停止远程回放
     *
     * @param playbackId
     * @param port
     */
    public void stopPlayback(int playbackId, int port) {
        if (playbackId != -1) {
            if (HCNetSDK.getInstance().NET_DVR_StopPlayBack(playbackId)) {
                Player.getInstance().stopSound();
                if (Player.getInstance().stop(port)) {
                    if (Player.getInstance().closeStream(port)) {
                        if (Player.getInstance().freePort(port)) {
                            mStopPlayback = true;
                            Log.i(TAG, "停止远程回放成功");
                        } else {
                            Log.e(TAG, "停止远程回放，调用freePort接口失败，错误码：" + Player.getInstance().getLastError(port));
                        }
                    } else {
                        Log.e(TAG, "停止远程回放，关闭流失败，错误码：" + Player.getInstance().getLastError(port));
                    }
                } else {
                    Log.e(TAG, "停止远程回放，调用stop接口失败，错误码：" + Player.getInstance().getLastError(port));
                }
                isplayback = false;
            } else {
                Log.e(TAG, "调用停止远程回放接口失败，错误码：" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            }
        }
    }

    /**
     * 登出设备
     *
     * @param logId
     */
    public void logout(int logId) {
        if (logId != -1) {
            if (HCNetSDK.getInstance().NET_DVR_Logout_V30(logId)) {
                Log.i(TAG, "登出设备成功");
            } else {
                Log.e(TAG, "登出设备失败，错误码：" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            }
        }
    }

    private RealPlayCallBack getRealPlayerCbf() {
        RealPlayCallBack cbf = new RealPlayCallBack() {
            public void fRealDataCallBack(int iRealHandle, int iDataType, byte[] pDataBuffer, int iDataSize) {
                processRealData(1, iDataType, pDataBuffer, iDataSize, Player.STREAM_REALTIME);
            }
        };
        return cbf;
    }

    private PlaybackCallBack getPlaybackPlayerCbf() {
        PlaybackCallBack cbf = new PlaybackCallBack() {
            @Override
            public void fPlayDataCallBack(int iPlaybackHandle, int iDataType, byte[] pDataBuffer, int iDataSize) {
                processRealData(1, iDataType, pDataBuffer, iDataSize, Player.STREAM_FILE);
            }
        };
        return cbf;
    }

    int pbc = 0;

    private void processRealData(int iPlayViewNo, int iDataType, byte[] pDataBuffer, int iDataSize, int iStreamMode) {
        if (HCNetSDK.NET_DVR_SYSHEAD == iDataType) {
            mPort = Player.getInstance().getPort();
            if (mPort == -1) {
                Log.e(TAG, "获取端口失败，错误码：" + Player.getInstance().getLastError(mPort));
                return;
            }
            Log.i(TAG, "获取端口成功，端口：" + mPort);
//            Player.getInstance().renderPrivateData(mPort, 0x00000002, 0);  //取消显示移动侦测
            if (iDataSize > 0) {
                if (!Player.getInstance().setStreamOpenMode(mPort, iStreamMode)) {
                    Log.e(TAG, "设置流打开类型失败");
                    return;
                }
                if (!Player.getInstance().openStream(mPort, pDataBuffer, iDataSize, 2 * 1024 * 1024)) {
                    Log.e(TAG, "打开流失败");
                    return;
                }
                if (!Player.getInstance().play(mPort, mSurfaceView.getHolder())) {
                    Log.e(TAG, "播放失败");
                    return;
                }
                if (!Player.getInstance().playSound(mPort)) {
                    Log.e(TAG, "播放声音失败");
                    return;
                }
                messageFeedback("初始化播放成功");
            }
        } else {
            if (isplayback()) pbc++;
            if (!Player.getInstance().inputData(mPort, pDataBuffer, iDataSize)) {
                for (int i = 0; i < 4000 && mPlaybackId >= 0 && !mStopPlayback; i++) {
                    if (Player.getInstance().inputData(mPort, pDataBuffer, iDataSize)) {
                        break;
                    }
                    if (i % 100 == 0) {
                        Log.e(TAG, "输入数据失败，错误码：" + Player.getInstance().getLastError(mPort) + ", i:" + i);
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 抓图
     *
     * @return
     */
    public Bitmap capturePicture() {
        Bitmap bitmap = null;
        if (mPort >= 0) {
            Player.MPInteger stWidth = new Player.MPInteger();
            Player.MPInteger stHeight = new Player.MPInteger();
            if (Player.getInstance().getPictureSize(mPort, stWidth, stHeight)) {
                int nSize = 5 * stWidth.value * stHeight.value;
                byte[] picBuf = new byte[nSize];
                Player.MPInteger stSize = new Player.MPInteger();
                if (Player.getInstance().getBMP(mPort, picBuf, nSize, stSize)) {
                    bitmap = BitmapFactory.decodeByteArray(picBuf, 0, stSize.value);
                    try {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream("/mnt/sdcard/hikvison.jpg"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return bitmap;
    }

    /**
     * 暂停/播放
     *
     * @param status 1---暂停；0---播放
     */
    public void pauseOrPlay(int status) {
        Player.getInstance().pause(mPort, status);
        isLive = (status == 0);
    }

    /**
     * @return NULL
     * @fn Cleanup
     * @author zhuzhenlei
     * @brief cleanup
     */
    public void cleanup() {
        // release player resource
        refresh();
        logout(mLogId);
        // release net SDK resource
        HCNetSDK.getInstance().NET_DVR_Cleanup();
        mPort = -1;
        mPlaybackId = -1;
        mPlayId = -1;
        mLogId = -1;
        mGetFileId = -1;
        isLive = false;
        isplayback = false;
        mStopPlayback = true;
//        mSurfaceView.getHolder().removeCallback(callback);
    }

    public void refresh() {
        Player.getInstance().freePort(mPort);
        mSurfaceView.getHolder().getSurface().release();
    }

    private void messageFeedback(String msg) {
        Message message = Message.obtain();
        message.what = 0;
        message.obj = msg;
        handler.sendMessage(message);
    }
}
