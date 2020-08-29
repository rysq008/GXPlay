package com.ikats.shop.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.ikats.shop.App;
import com.ikats.shop.model.GoodsBean;
import com.ikats.shop.model.PrintBean;
import com.lvrenyang.io.Pos;
import com.lvrenyang.io.base.IOCallBack;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;

import cn.droidlover.xdroidmvp.kit.Kits;

import static com.ikats.shop.fragments.HomeFragment.mcom;
import static com.ikats.shop.fragments.HomeFragment.mpos;

public class Prints {

    public static void PostPrint(Context context, PrintBean pstr, TextView order_tv) {
        try {
            pbody = pstr;
            Executors.newSingleThreadExecutor().execute(() -> {
                if (Kits.Empty.check(pbody)) return;
                final int bPrintResult = Prints.PrintTicket(App.getApp(), mpos, 576, true,
                        false, true, 1, 1, 0);
                final boolean bIsOpened = mpos.GetIO().IsOpened();
                if (bPrintResult == 0) pbody = null;
                order_tv.post(() -> {
                    // TODO Auto-generated method stub
                    Toast.makeText(
                            App.getApp(),
                            (bPrintResult >= 0) ? " printsuccess" + Prints.ResultCodeToString(bPrintResult) : ("printfailed")
                                    + " "
                                    + Prints.ResultCodeToString(bPrintResult),
                            Toast.LENGTH_SHORT).show();
                    order_tv.setEnabled(bIsOpened);
                });

            });
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    static PrintBean pbody;

    public static void OpenPosCallBack(View order_tv) {
        mcom.SetCallBack(new IOCallBack() {
            @Override
            public void OnOpen() {
                order_tv.post(() -> {
                    ToastUtils.showLong("Connected success !");
                    order_tv.setEnabled(false);
                });
            }

            @Override
            public void OnOpenFailed() {
                order_tv.post(() -> {
                    ToastUtils.showLong("Connected failed !");
                    order_tv.setEnabled(true);
                });
            }

            @Override
            public void OnClose() {
                order_tv.post(() -> {
                    ToastUtils.showLong("Connected close !");
                    order_tv.setEnabled(true);
                });
            }
        });
        Executors.newSingleThreadExecutor().execute(() -> {
            mcom.Open("/dev/ttyS4", 9600, 0);
        });

    }

    public static int PrintTicket(Context ctx, Pos pos, int nPrintWidth, boolean bCutter, boolean bDrawer, boolean bBeeper, int nCount, int nPrintContent, int nCompressMethod) {
        int bPrintResult = -8;
        int price_offset = 248, count_offset = 371, amount_offset = 464, line_offset = 10;

        byte[] status = new byte[1];
        if (pos.POS_RTQueryStatus(status, 3, 1000, 2)) {

            if ((status[0] & 0x08) == 0x08)   //判断切刀是否异常
                return bPrintResult = -2;

            if ((status[0] & 0x40) == 0x40)   //判断打印头是否在正常值范围内
                return bPrintResult = -3;

            if (pos.POS_RTQueryStatus(status, 2, 1000, 2)) {

                if ((status[0] & 0x04) == 0x04)    //判断合盖是否正常
                    return bPrintResult = -6;
                if ((status[0] & 0x20) == 0x20)    //判断是否缺纸
                    return bPrintResult = -5;
                else {
                    Bitmap bm1 = getTestImage1(nPrintWidth, nPrintWidth);
                    Bitmap bm2 = getTestImage2(nPrintWidth, nPrintWidth);
                    Bitmap bmBlackWhite = getImageFromAssetsFile(ctx, "blackwhite.png");
                    Bitmap bmIu = getImageFromAssetsFile(ctx, "iu.jpeg");
                    Bitmap bmYellowmen = getImageFromAssetsFile(ctx, "yellowmen.png");
                    for (int i = 0; i < nCount; i++) {
                        if (!pos.GetIO().IsOpened())
                            break;

                        if (nPrintContent >= 1) {
                            pos.POS_Reset();
                            pos.POS_FeedLine();
                            pos.POS_TextOut("序号:" + App.getSettingBean().shop_code + "\r\n", 0, line_offset, 1, 1, 0, 0);
                            pos.POS_TextOut("\r\n", 3, 24, 0, 0, 0, 0);
                            pos.POS_TextOut(App.getSettingBean().shop_name + "\r\n", 0, line_offset, 1, 1, 0, 0);
//                            pos.POS_TextOut("\r\n", 3, 24, 0, 0, 0, 0);
                            pos.POS_TextOut(String.format("%-47s\r\n", ""), 0, line_offset, 0, 0, 0, 0x80);
                            pos.POS_TextOut("\r\n", 3, 60, 1, 1, 0, 0);
                            pos.POS_TextOut("销售单号:" + pbody.sell_code + "\r\n", 0, line_offset, 0, 0, 0, 0);
//                            pos.POS_TextOut("\r\n", 0, 0, 0, 0, 0, 0);
                            pos.POS_TextOut("收银员:" + App.getSettingBean().shop_cashier + "\r\n", 0, line_offset, 0, 0, 0, 0);
//                            pos.POS_TextOut("\r\n", 0, 0, 0, 0, 0, 0);
                            pos.POS_TextOut(String.format("%-47s\r\n", ""), 0, line_offset, 0, 0, 0, 0x80);
                            pos.POS_TextOut("\r\n", 0, 0, 0, 0, 0, 0);

                            pos.POS_TextOut("品名", 0, 0 + line_offset, 0, 0, 0, 0x08);
                            pos.POS_TextOut("单价", 0, price_offset + line_offset, 0, 0, 0, 0x08);
                            pos.POS_TextOut("数量", 0, count_offset + line_offset, 0, 0, 0, 0x08);
                            pos.POS_TextOut("金额\r\n", 0, amount_offset + line_offset, 0, 0, 0, 0x08);

                            for (GoodsBean bean : pbody.list) {
                                pos.POS_TextOut(bean.name, 0, 0 + line_offset, 0, 0, 0, 0);
                                pos.POS_TextOut(String.valueOf(bean.sell_price), 0, price_offset + line_offset, 0, 0, 0, 0);
                                pos.POS_TextOut(String.valueOf(bean.count), 0, count_offset + line_offset, 0, 0, 0, 0);
                                pos.POS_TextOut(bean.amount + "\r\n", 0, amount_offset + line_offset, 0, 0, 0, 0);
                                pos.POS_TextOut("\r\n", 0, 0 + line_offset, 0, 0, 0, 0);
                            }

                            pos.POS_TextOut("合计:", 0, 0 + line_offset, 0, 0, 0, 0);
                            pos.POS_TextOut(pbody.total + "\r\n", 0, amount_offset + line_offset, 0, 0, 0, 0);
                            pos.POS_TextOut("\r\n", 0, 0 + line_offset, 0, 0, 0, 0);
                            pos.POS_TextOut("优惠:", 0, 0 + line_offset, 0, 0, 0, 0);
                            pos.POS_TextOut(pbody.discounts + "\r\n", 0, amount_offset + line_offset, 0, 0, 0, 0);
                            pos.POS_TextOut("\r\n", 0, 0 + line_offset, 0, 0, 0, 0);
                            pos.POS_TextOut("应付:", 0, 0 + line_offset, 0, 0, 0, 0);
                            pos.POS_TextOut(pbody.cope + "\r\n", 0, amount_offset + line_offset, 0, 0, 0, 0);
                            pos.POS_TextOut("\r\n", 0, 0 + line_offset, 0, 0, 0, 0);
                            pos.POS_TextOut("实付:", 0, 0 + line_offset, 0, 0, 0, 0);
                            pos.POS_TextOut(pbody.paid + "\r\n", 0, amount_offset + line_offset, 0, 0, 0, 0);

                            pos.POS_FeedLine();
                            pos.POS_S_SetQRcode(App.getSettingBean().shop_url, 2, 5, 4);
                            pos.POS_TextOut("更多惊喜关注线上商城\r\n\n\n", 0, 198, 0, 0, 0, 0);
                            pos.POS_FeedLine();
                            pos.POS_FeedLine();


                            if (nPrintContent == 1 && nCount > 1) {
                                pos.POS_HalfCutPaper();
                                try {
                                    Thread.currentThread();
                                    Thread.sleep(4000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        if (nPrintContent >= 2) {

                            if (bm1 != null) {
                                pos.POS_PrintPicture(bm1, nPrintWidth, 1, nCompressMethod);
                            }
                            if (bm2 != null) {
                                pos.POS_PrintPicture(bm2, nPrintWidth, 1, nCompressMethod);
                            }

                            if (nPrintContent == 2 && nCount > 1) {
                                pos.POS_HalfCutPaper();
                                try {
                                    Thread.currentThread();
                                    Thread.sleep(4500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (nPrintContent == 2 && nCount == 1) {
                                if (bBeeper)
                                    pos.POS_Beep(1, 5);
                                if (bCutter)
                                    pos.POS_FullCutPaper();
                                if (bDrawer)
                                    pos.POS_KickDrawer(0, 100);

                                if (pos.POS_RTQueryStatus(status, 1, 1000, 2)) {
                                    if ((status[0] & 0x80) == 0x80) {

                                        try {
                                            Thread.currentThread();
                                            Thread.sleep(3000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        if (pos.POS_RTQueryStatus(status, 2, 1000, 2)) {
                                            if ((status[0] & 0x20) == 0x20)    //打印过程缺纸
                                                return bPrintResult = -9;
                                            if ((status[0] & 0x04) == 0x04)    //打印过程打开纸仓盖
                                                return bPrintResult = -10;
                                        } else {
                                            return bPrintResult = -11;         //查询失败
                                        }


                                        if (pos.POS_RTQueryStatus(status, 4, 1000, 2)) {
                                            if ((status[0] & 0x08) == 0x08) {
                                                if (pos.POS_RTQueryStatus(status, 1, 1000, 2)) {
                                                    if ((status[0] & 0x80) == 0x80)     //纸将尽且未取纸
                                                        return bPrintResult = 2;
                                                    else
                                                        return bPrintResult = 1;
                                                }
                                            } else {
                                                if (pos.POS_RTQueryStatus(status, 1, 1000, 2)) {
                                                    if ((status[0] & 0x80) == 0x80) {
                                                        return bPrintResult = 3;
                                                    } else
                                                        return bPrintResult = 0;
                                                } else
                                                    return bPrintResult = -11;
                                            }
                                        } else {
                                            return bPrintResult = -11;         //查询失败
                                        }


                                    } else {
                                        try {
                                            Thread.currentThread();
                                            Thread.sleep(3000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        if (pos.POS_RTQueryStatus(status, 2, 1000, 2)) {
                                            if ((status[0] & 0x20) == 0x20) {
                                                return bPrintResult = -9;                //打印过程缺纸
                                            }

                                            if ((status[0] & 0x04) == 0x04) {
                                                return bPrintResult = -10;                //打印过程打开纸仓盖
                                            } else {
                                                return bPrintResult = -1;
                                            }


                                        } else {
                                            return bPrintResult = -11;         //查询失败
                                        }
                                    }

                                } else {
                                    return bPrintResult = -11;
                                }
                            }
                        }

                        if (nPrintContent >= 3) {
                            if (bmBlackWhite != null) {
                                pos.POS_PrintPicture(bmBlackWhite, nPrintWidth, 1, nCompressMethod);
                            }
                            if (bmIu != null) {
                                pos.POS_PrintPicture(bmIu, nPrintWidth, 0, nCompressMethod);
                            }
                            if (bmYellowmen != null) {
                                pos.POS_PrintPicture(bmYellowmen, nPrintWidth, 0, nCompressMethod);
                            }

                            if (nPrintContent == 3 && nCount > 1) {
                                pos.POS_HalfCutPaper();
                                try {
                                    Thread.currentThread();
                                    Thread.sleep(6000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (nPrintContent == 3 && nCount == 1) {
                                if (bBeeper)
                                    pos.POS_Beep(1, 5);
                                if (bCutter)
                                    pos.POS_FullCutPaper();
                                if (bDrawer)
                                    pos.POS_KickDrawer(0, 100);
                                if (pos.POS_RTQueryStatus(status, 1, 1000, 2)) {
                                    if ((status[0] & 0x80) == 0x80) {

                                        try {
                                            Thread.currentThread();
                                            Thread.sleep(3000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        if (pos.POS_RTQueryStatus(status, 2, 1000, 2)) {
                                            if ((status[0] & 0x20) == 0x20)    //打印过程缺纸
                                                return bPrintResult = -9;
                                            if ((status[0] & 0x04) == 0x04)    //打印过程打开纸仓盖
                                                return bPrintResult = -10;
                                        } else {
                                            return bPrintResult = -11;         //查询失败
                                        }


                                        if (pos.POS_RTQueryStatus(status, 4, 1000, 2)) {
                                            if ((status[0] & 0x08) == 0x08) {
                                                if (pos.POS_RTQueryStatus(status, 1, 1000, 2)) {
                                                    if ((status[0] & 0x80) == 0x80)     //纸将尽且未取纸
                                                        return bPrintResult = 2;
                                                    else
                                                        return bPrintResult = 1;
                                                }
                                            } else {
                                                if (pos.POS_RTQueryStatus(status, 1, 1000, 2)) {
                                                    if ((status[0] & 0x80) == 0x80) {
                                                        return bPrintResult = 3;
                                                    } else
                                                        return bPrintResult = 0;
                                                } else
                                                    return bPrintResult = -11;
                                            }
                                        } else {
                                            return bPrintResult = -11;         //查询失败
                                        }


                                    } else {
                                        try {
                                            Thread.currentThread();
                                            Thread.sleep(3000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        if (pos.POS_RTQueryStatus(status, 2, 1000, 2)) {
                                            if ((status[0] & 0x20) == 0x20) {
                                                return bPrintResult = -9;                //打印过程缺纸
                                            }

                                            if ((status[0] & 0x04) == 0x04) {
                                                return bPrintResult = -10;                //打印过程打开纸仓盖
                                            } else {
                                                return bPrintResult = -1;
                                            }


                                        } else {
                                            return bPrintResult = -11;         //查询失败
                                        }
                                    }

                                } else {
                                    return bPrintResult = -11;
                                }
                            }
                        }
                    }

                    if (bBeeper)
                        pos.POS_Beep(1, 5);
                    if (bCutter && nCount == 1)
                        pos.POS_FullCutPaper();
                    if (bDrawer)
                        pos.POS_KickDrawer(0, 100);


                    if (nCount == 1) {
                        try {
                            Thread.currentThread();
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (pos.POS_RTQueryStatus(status, 1, 1000, 2)) {
                            if ((status[0] & 0x80) == 0x80) {

                                try {
                                    Thread.currentThread();
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (pos.POS_RTQueryStatus(status, 2, 1000, 2)) {
                                    if ((status[0] & 0x20) == 0x20)    //打印过程缺纸
                                        return bPrintResult = -9;
                                    if ((status[0] & 0x04) == 0x04)    //打印过程打开纸仓盖
                                        return bPrintResult = -10;
                                } else {
                                    return bPrintResult = -11;         //查询失败
                                }


                                if (pos.POS_RTQueryStatus(status, 4, 1000, 2)) {
                                    if ((status[0] & 0x08) == 0x08) {
                                        if (pos.POS_RTQueryStatus(status, 1, 1000, 2)) {
                                            if ((status[0] & 0x80) == 0x80)     //纸将尽且未取纸
                                                return bPrintResult = 2;
                                            else
                                                return bPrintResult = 1;
                                        }
                                    } else {
                                        if (pos.POS_RTQueryStatus(status, 1, 1000, 2)) {
                                            if ((status[0] & 0x80) == 0x80) {
                                                return bPrintResult = 3;
                                            } else
                                                return bPrintResult = 0;
                                        } else
                                            return bPrintResult = -11;
                                    }
                                } else {
                                    return bPrintResult = -11;         //查询失败
                                }


                            } else {
                                try {
                                    Thread.currentThread();
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (pos.POS_RTQueryStatus(status, 2, 1000, 2)) {
                                    if ((status[0] & 0x20) == 0x20) {
                                        return bPrintResult = -9;                //打印过程缺纸
                                    }

                                    if ((status[0] & 0x04) == 0x04) {
                                        return bPrintResult = -10;                //打印过程打开纸仓盖
                                    } else {
                                        return bPrintResult = -1;
                                    }


                                } else {
                                    return bPrintResult = -11;         //查询失败
                                }
                            }

                        } else {
                            return bPrintResult = -11;
                        }
                    } else {
                        if (pos.POS_RTQueryStatus(status, 1, 1000, 2)) {
                            if ((status[0] & 0x80) == 0x80) {

                                try {
                                    Thread.currentThread();
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (pos.POS_RTQueryStatus(status, 2, 1000, 2)) {
                                    if ((status[0] & 0x20) == 0x20)    //打印过程缺纸
                                        return bPrintResult = -9;
                                    if ((status[0] & 0x04) == 0x04)    //打印过程打开纸仓盖
                                        return bPrintResult = -10;
                                } else {
                                    return bPrintResult = -11;         //查询失败
                                }


                                if (pos.POS_RTQueryStatus(status, 4, 1000, 2)) {
                                    if ((status[0] & 0x08) == 0x08) {
                                        if (pos.POS_RTQueryStatus(status, 1, 1000, 2)) {
                                            if ((status[0] & 0x80) == 0x80)     //纸将尽且未取纸
                                                return bPrintResult = 2;
                                            else
                                                return bPrintResult = 1;
                                        }
                                    } else {
                                        if (pos.POS_RTQueryStatus(status, 1, 1000, 2)) {
                                            if ((status[0] & 0x80) == 0x80) {
                                                return bPrintResult = 3;
                                            } else
                                                return bPrintResult = 0;
                                        } else
                                            return bPrintResult = -11;
                                    }
                                } else {
                                    return bPrintResult = -11;         //查询失败
                                }


                            } else {
                                try {
                                    Thread.currentThread();
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (pos.POS_RTQueryStatus(status, 2, 1000, 2)) {
                                    if ((status[0] & 0x20) == 0x20) {
                                        return bPrintResult = -9;                //打印过程缺纸
                                    }

                                    if ((status[0] & 0x04) == 0x04) {
                                        return bPrintResult = -10;                //打印过程打开纸仓盖
                                    } else {
                                        return bPrintResult = -1;
                                    }


                                } else {
                                    return bPrintResult = -11;         //查询失败
                                }
                            }

                        } else {
                            return bPrintResult = -11;
                        }
                    }


//					try {
//						Thread.currentThread();
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}

//					if(pos.POS_RTQueryStatus(status, 1, 1000, 2)){
//						if((status[0] & 0x80) == 0x80){
//
//							try {
//                                Thread.currentThread();
//                                Thread.sleep(3000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//
//							if (pos.POS_RTQueryStatus(status, 2, 1000, 2)) {
//								if ((status[0] & 0x20) == 0x20)    //打印过程缺纸
//									return bPrintResult = -9;
//								if ((status[0] & 0x04) == 0x04)    //打印过程打开纸仓盖
//									return bPrintResult = -10;
//							} else {
//								return bPrintResult = -11;         //查询失败
//							}
//
//
//							if (pos.POS_RTQueryStatus(status, 4, 1000, 2)){
//								if ((status[0] & 0x08) == 0x08){
//									if (pos.POS_RTQueryStatus(status, 1, 1000, 2)){
//										if((status[0] & 0x80) == 0x80)     //纸将尽且未取纸
//											return bPrintResult = 2;
//										else
//											return bPrintResult = 1;
//									}
//								}else{
//									if(pos.POS_RTQueryStatus(status, 1, 1000, 2)){
//										if((status[0] & 0x80) == 0x80){
//											return bPrintResult = 3;
//										}else
//											return bPrintResult = 0;
//									}else
//										return bPrintResult = -11;
//								}
//							}else{
//								return bPrintResult = -11;         //查询失败
//							}
//
//
//						}else{
//							try {
//								Thread.currentThread();
//								Thread.sleep(3000);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//
//							if (pos.POS_RTQueryStatus(status, 2, 1000, 2)) {
//                                if ((status[0] & 0x20) == 0x20) {
//									return bPrintResult = -9;				//打印过程缺纸
//								}
//
//                                if ((status[0] & 0x04) == 0x04){
//									return bPrintResult = -10;				//打印过程打开纸仓盖
//								}else{
//									return bPrintResult = -1;
//								}
//
//
//                            } else {
//                                return bPrintResult = -11;         //查询失败
//                            }
//						}
//
//					}else{
//						return bPrintResult = -11;
//					}

                    //无取纸侦测的机型执行以下代码

//					//延时5S等待打印机打印完票据，可根据打印内容自行调整
//					try {
//						Thread.currentThread();
//						Thread.sleep(3000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//
//					//查询是否在打印过程中打开纸仓盖或缺纸
//					if (pos.POS_RTQueryStatus(status, 2, 1000, 2)) {
//						if ((status[0] & 0x20) == 0x20)    //打印过程缺纸
//							return bPrintResult = -9;
//						if ((status[0] & 0x04) == 0x04)    //打印过程打开纸仓盖
//							return bPrintResult = -10;
//					} else {
//						return bPrintResult = -11;         //查询失败
//					}
//
//
//					//查询是否纸将尽
//					if (pos.POS_RTQueryStatus(status, 4, 1000, 2)) {
//						if ((status[0] & 0x08) == 0x08){
//							if (pos.POS_RTQueryStatus(status, 1, 1000, 2)){
//								if((status[0] & 0x80) == 0x80)     //纸将尽且未取纸
//									return bPrintResult = 2;
//								else
//									return bPrintResult = 1;       //纸将尽
//							}else{
//								return bPrintResult = -11;         //查询失败
//							}
//						}
//					} else {
//						return bPrintResult = -11;
//					}

                }

            } else {
                return bPrintResult = -8;           //查询失败
            }

        } else {
            return bPrintResult = -8;          //查询失败
        }

        return bPrintResult;
    }

    public static String ResultCodeToString(int code) {
        switch (code) {
            case 3:
                return "出纸口有未取小票，请注意及时取走小票";
            case 2:
                return "紙将尽 且 出纸口有未取小票，请注意更换纸卷 和 及时取走小票";
            case 1:
                return "紙将尽，请注意更换纸卷";
            case 0:
                return " ";
            case -1:
                return "未打印小票，请检查是否卡纸";
            case -2:
                return "切刀异常，请手动排除";
            case -3:
                return "打印头过热，请等待打印机冷却";
            case -4:
                return "打印机脱机";
            case -5:
                return "打印机缺纸";
            case -6:
                return "上盖打开";
            case -7:
                return "实时状态查询失败";
            case -8:
                return "查询状态失败，请检查通讯端口是否连接正常";
            case -9:
                return "打印过程中缺纸，请检查单据完整性";
            case -10:
                return "打印过程中上盖开启，请重新打印";
            case -11:
                return "连接中断，请确认打印机是否连线";
            case -12:
                return "请取走打印完的票据后，再进行打印！";
            case -13:
            default:
                return "未知错误";
        }
    }

    /**
     * 从Assets中读取图片
     */
    public static Bitmap getImageFromAssetsFile(Context ctx, String fileName) {
        Bitmap image = null;
        AssetManager am = ctx.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        // load the origial Bitmap
        Bitmap BitmapOrg = bitmap;

        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        // calculate the scale
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);

        // make a Drawable from Bitmap to allow to set the Bitmap
        // to the ImageView, ImageButton or what ever
        return resizedBitmap;
    }

    public static Bitmap getTestImage1(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, width, height, paint);

        paint.setColor(Color.BLACK);
        for (int i = 0; i < 8; ++i) {
            for (int x = i; x < width; x += 8) {
                for (int y = i; y < height; y += 8) {
                    canvas.drawPoint(x, y, paint);
                }
            }
        }
        return bitmap;
    }

    public static Bitmap getTestImage2(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, width, height, paint);

        paint.setColor(Color.BLACK);
        for (int y = 0; y < height; y += 4) {
            for (int x = y % 32; x < width; x += 32) {
                canvas.drawRect(x, y, x + 4, y + 4, paint);
            }
        }
        return bitmap;
    }

    public static void close() {
        mcom.Close();
    }
}
