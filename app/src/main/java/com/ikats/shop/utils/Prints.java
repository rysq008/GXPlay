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

import com.lvrenyang.io.Pos;

import java.io.IOException;
import java.io.InputStream;

public class Prints {

	public static int PrintTicket(Context ctx, Pos pos, int nPrintWidth, boolean bCutter, boolean bDrawer, boolean bBeeper, int nCount, int nPrintContent, int nCompressMethod) {
		int bPrintResult = -8;

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
							pos.POS_TextOut("序号:		02\r\n", 3, 24, 1, 1, 0, 0);
							pos.POS_TextOut("青岛XXXX门店\r\n", 3, 24, 1, 1, 0, 0);
							pos.POS_TextOut("\r\n", 3, 24, 1, 1, 0, 0);
//							Bitmap line = Bitmap.createBitmap(384,1,Config.ARGB_8888);
//							Canvas canvas = new com.lvrenyang.io.Canvas(pos);
							pos.POS_TextOut("\r\n", 3, 24, 0, 0, 0, 0);
							pos.POS_TextOut("105年05-06月\r\n", 3, 48, 1, 1, 0, 0);
							pos.POS_TextOut("\r\n", 3, 24, 0, 0, 0, 0);
							pos.POS_TextOut("TW-56321497\r\n", 3, 60, 1, 1, 0, 0);
							pos.POS_TextOut("2016-05-26 09:43:29\r\n", 3, 0, 0, 0, 0, 0);
							pos.POS_TextOut("隨機碼：2887   總計：418\r\n", 3, 0, 0, 0, 0, 0);

							pos.POS_TextOut("賣方：12345678                   \r\n", 3, 0, 0, 0, 0, 0);
							pos.POS_FeedLine();
							pos.POS_S_SetBarcode("46661366725", 0, 69, 2, 50, 0, 0);
							pos.POS_FeedLine();
							pos.POS_DoubleQRCode("乾電池:1:105", 20, 4, 5, "口罩:1:210:牛奶:1:25", 230, 4, 5, 4);
							pos.POS_FeedLine();
							pos.POS_FeedLine();

							pos.POS_TextOut("REC" + String.format("%03d", i+1) + "\r\nPrinter\r\n简体中文测试\r\n\r\n", 0, 1, 1, 0, 0, 0);
							pos.POS_FeedLine();
							pos.POS_FeedLine();

							if (nPrintContent == 1 && nCount > 1){
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

							if (nPrintContent == 2 && nCount > 1){
								pos.POS_HalfCutPaper();
								try {
									Thread.currentThread();
									Thread.sleep(4500);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}

							if (nPrintContent == 2 && nCount == 1){
								if (bBeeper)
									pos.POS_Beep(1, 5);
								if (bCutter)
									pos.POS_FullCutPaper();
								if (bDrawer)
									pos.POS_KickDrawer(0, 100);

								if(pos.POS_RTQueryStatus(status, 1, 1000, 2)){
									if((status[0] & 0x80) == 0x80){

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


										if (pos.POS_RTQueryStatus(status, 4, 1000, 2)){
											if ((status[0] & 0x08) == 0x08){
												if (pos.POS_RTQueryStatus(status, 1, 1000, 2)){
													if((status[0] & 0x80) == 0x80)     //纸将尽且未取纸
														return bPrintResult = 2;
													else
														return bPrintResult = 1;
												}
											}else{
												if(pos.POS_RTQueryStatus(status, 1, 1000, 2)){
													if((status[0] & 0x80) == 0x80){
														return bPrintResult = 3;
													}else
														return bPrintResult = 0;
												}else
													return bPrintResult = -11;
											}
										}else{
											return bPrintResult = -11;         //查询失败
										}


									}else{
										try {
											Thread.currentThread();
											Thread.sleep(3000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}

										if (pos.POS_RTQueryStatus(status, 2, 1000, 2)) {
											if ((status[0] & 0x20) == 0x20) {
												return bPrintResult = -9;				//打印过程缺纸
											}

											if ((status[0] & 0x04) == 0x04){
												return bPrintResult = -10;				//打印过程打开纸仓盖
											}else{
												return bPrintResult = -1;
											}


										} else {
											return bPrintResult = -11;         //查询失败
										}
									}

								}else{
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

							if (nPrintContent == 3 && nCount > 1){
								pos.POS_HalfCutPaper();
								try {
									Thread.currentThread();
									Thread.sleep(6000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}

							if (nPrintContent == 3 && nCount == 1){
								if (bBeeper)
									pos.POS_Beep(1, 5);
								if (bCutter)
									pos.POS_FullCutPaper();
								if (bDrawer)
									pos.POS_KickDrawer(0, 100);
								if(pos.POS_RTQueryStatus(status, 1, 1000, 2)){
									if((status[0] & 0x80) == 0x80){

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


										if (pos.POS_RTQueryStatus(status, 4, 1000, 2)){
											if ((status[0] & 0x08) == 0x08){
												if (pos.POS_RTQueryStatus(status, 1, 1000, 2)){
													if((status[0] & 0x80) == 0x80)     //纸将尽且未取纸
														return bPrintResult = 2;
													else
														return bPrintResult = 1;
												}
											}else{
												if(pos.POS_RTQueryStatus(status, 1, 1000, 2)){
													if((status[0] & 0x80) == 0x80){
														return bPrintResult = 3;
													}else
														return bPrintResult = 0;
												}else
													return bPrintResult = -11;
											}
										}else{
											return bPrintResult = -11;         //查询失败
										}


									}else{
										try {
											Thread.currentThread();
											Thread.sleep(3000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}

										if (pos.POS_RTQueryStatus(status, 2, 1000, 2)) {
											if ((status[0] & 0x20) == 0x20) {
												return bPrintResult = -9;				//打印过程缺纸
											}

											if ((status[0] & 0x04) == 0x04){
												return bPrintResult = -10;				//打印过程打开纸仓盖
											}else{
												return bPrintResult = -1;
											}


										} else {
											return bPrintResult = -11;         //查询失败
										}
									}

								}else{
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


					if (nCount == 1){
						try {
							Thread.currentThread();
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						if(pos.POS_RTQueryStatus(status, 1, 1000, 2)){
							if((status[0] & 0x80) == 0x80){

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


								if (pos.POS_RTQueryStatus(status, 4, 1000, 2)){
									if ((status[0] & 0x08) == 0x08){
										if (pos.POS_RTQueryStatus(status, 1, 1000, 2)){
											if((status[0] & 0x80) == 0x80)     //纸将尽且未取纸
												return bPrintResult = 2;
											else
												return bPrintResult = 1;
										}
									}else{
										if(pos.POS_RTQueryStatus(status, 1, 1000, 2)){
											if((status[0] & 0x80) == 0x80){
												return bPrintResult = 3;
											}else
												return bPrintResult = 0;
										}else
											return bPrintResult = -11;
									}
								}else{
									return bPrintResult = -11;         //查询失败
								}


							}else{
								try {
									Thread.currentThread();
									Thread.sleep(3000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}

								if (pos.POS_RTQueryStatus(status, 2, 1000, 2)) {
									if ((status[0] & 0x20) == 0x20) {
										return bPrintResult = -9;				//打印过程缺纸
									}

									if ((status[0] & 0x04) == 0x04){
										return bPrintResult = -10;				//打印过程打开纸仓盖
									}else{
										return bPrintResult = -1;
									}


								} else {
									return bPrintResult = -11;         //查询失败
								}
							}

						}else{
							return bPrintResult = -11;
						}
					}else{
						if(pos.POS_RTQueryStatus(status, 1, 1000, 2)){
							if((status[0] & 0x80) == 0x80){

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


								if (pos.POS_RTQueryStatus(status, 4, 1000, 2)){
									if ((status[0] & 0x08) == 0x08){
										if (pos.POS_RTQueryStatus(status, 1, 1000, 2)){
											if((status[0] & 0x80) == 0x80)     //纸将尽且未取纸
												return bPrintResult = 2;
											else
												return bPrintResult = 1;
										}
									}else{
										if(pos.POS_RTQueryStatus(status, 1, 1000, 2)){
											if((status[0] & 0x80) == 0x80){
												return bPrintResult = 3;
											}else
												return bPrintResult = 0;
										}else
											return bPrintResult = -11;
									}
								}else{
									return bPrintResult = -11;         //查询失败
								}


							}else{
								try {
									Thread.currentThread();
									Thread.sleep(3000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}

								if (pos.POS_RTQueryStatus(status, 2, 1000, 2)) {
									if ((status[0] & 0x20) == 0x20) {
										return bPrintResult = -9;				//打印过程缺纸
									}

									if ((status[0] & 0x04) == 0x04){
										return bPrintResult = -10;				//打印过程打开纸仓盖
									}else{
										return bPrintResult = -1;
									}


								} else {
									return bPrintResult = -11;         //查询失败
								}
							}

						}else{
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
				return  "请取走打印完的票据后，再进行打印！";
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
	
	public static Bitmap getTestImage1(int width, int height)
	{
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();

		paint.setColor(Color.WHITE);
		canvas.drawRect(0, 0, width, height, paint);
		
		paint.setColor(Color.BLACK);
		for(int i = 0; i < 8; ++i)
		{
			for(int x = i; x < width; x += 8)
			{
				for(int y = i; y < height; y += 8)
				{
					canvas.drawPoint(x, y, paint);
				}
			}
		}
		return bitmap;
	}

	public static Bitmap getTestImage2(int width, int height)
	{
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();

		paint.setColor(Color.WHITE);
		canvas.drawRect(0, 0, width, height, paint);
		
		paint.setColor(Color.BLACK);
		for(int y = 0; y < height; y += 4)
		{
			for(int x = y%32; x < width; x += 32)
			{
				canvas.drawRect(x, y, x+4, y+4, paint);
			}
		}
		return bitmap;
	}
}
