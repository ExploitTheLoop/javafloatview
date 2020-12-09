package com.tencent.hz.service;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import com.tencent.hz.R;
import com.tencent.hz.activity.MainActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import com.tencent.hz.godrawing.Start;

public class DrawService extends Service {

    private boolean 绘制状态;//线程是否继续
    private boolean viewAdded = false;
    private View view;
    private SurfaceView sv;//绘制悬浮
    private WindowManager wm;//悬浮窗

    private WindowManager.LayoutParams params;



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onCreate() {
        view = View.inflate(getApplicationContext(), R.layout.draw, null);
        sv = view.findViewById(R.id.surface_view);
        sv.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        sv.getHolder().addCallback(new SurfaceHolder.Callback() {
                private DrawThread dt;

                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    绘制状态 = true;
                    dt = new DrawThread(surfaceHolder);
                    dt.Flag = true;
                    dt.start();

                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

                    dt.Flag = false;


                }
            });
        wm = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            // android 8.0及以后使用
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            params.flags = computeFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        } else {
            // android 8.0以前使用
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        }

        // params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;               // 设置window type

        // params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_SECURE;  // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）

        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.format = PixelFormat.RGBA_8888;

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (viewAdded) {
            wm.updateViewLayout(view, params);
        } else {
            wm.addView(view, params);
            viewAdded = true;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    //销毁服务
    @Override
    public void onDestroy() {

        if (viewAdded && wm != null) {
            wm.removeView(view);
        }
        super.onDestroy();
    }


    //适配低版本悬浮窗
    private int computeFlags(int curFlags) {
        boolean mTouchable = false;
        if (!mTouchable) {
            curFlags |= WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        } else {
            curFlags &= ~WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        }

        boolean mFocusable = false;
        if (!mFocusable) {
            curFlags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        } else {
            curFlags &= ~WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }

        boolean mTouchModal = true;
        if (!mTouchModal) {
            curFlags |= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        } else {
            curFlags &= ~WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        }

        boolean mOutsideTouchable = false;
        if (mOutsideTouchable) {
            curFlags |= WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        } else {
            curFlags &= ~WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        }

        return curFlags;
    }


    class DrawThread extends Thread {
        private boolean Flag;//用于标注线程是否继续

        private SurfaceHolder surfaceHolder;

        //定义画笔
        Paint 绘制文本=new Paint();
        Paint 准心圆圈 = new Paint();       
        Paint 人机射线 = new Paint();
        Paint 人机方框 = new Paint();
        Paint 玩家射线 = new Paint();
        Paint 玩家方框 = new Paint();
        Paint paint5 = new Paint();
        Paint paint6 = new Paint();
        Paint paint7 = new Paint();
        
        Canvas canvas = null;
        float x = 0;//x轴
        float y = 0;//y轴
        float w = 0;//宽
        float h = 0;//高
        float M = 0;//距离
        float ai = 0;//人机识别
        float hp = 0;//血量
        float x1;//偏移

        
        private  String getFileContent(File file) {
            String content = "";
            if (!file.isDirectory()) {
                try {
                    InputStream instream = new FileInputStream(file);
                    if (instream != null) {
                        InputStreamReader inputreader
                            = new InputStreamReader(instream, "UTF-8");
                        BufferedReader buffreader = new BufferedReader(inputreader);
                        String line = "";
                        while ((line = buffreader.readLine()) != null) {
                            content += line;
                        }
                        instream.close();//关闭输入流
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return content;
        }

        public DrawThread(SurfaceHolder h) {
            init();
            surfaceHolder = h;
            Flag = true;
        }

        private void init() {
            //绘制文本
            绘制文本.setColor(0xFF00FFAC);
            绘制文本.setAntiAlias(true);
            绘制文本.setDither(true);
            绘制文本.setTextSize(30);


            //准心圆圈
            准心圆圈.setColor(0xFF00FFAC);
            准心圆圈.setStyle(Paint.Style.STROKE);
            准心圆圈.setStrokeWidth(3.0f);

            //玩家射线
            玩家射线.setColor(Color.rgb(248, 0, 0));
            玩家射线.setStyle(Paint.Style.FILL);
            玩家射线.setStrokeWidth(1.8f);
            //玩家方框
            玩家方框.setColor(Color.rgb(248, 0, 0));
            玩家方框.setStyle(Paint.Style.STROKE);
            玩家方框.setStrokeWidth(2f);

            //人机射线
            人机射线.setColor(Color.rgb(248, 248, 255));
            人机射线.setStyle(Paint.Style.FILL);
            人机射线.setStrokeWidth(1.8f);
            //人机方框
            人机方框.setColor(Color.rgb(248, 248, 255));
            人机方框.setStyle(Paint.Style.STROKE);
            人机方框.setStrokeWidth(2f);

            //血条边框
            paint5.setColor(Color.rgb(0, 0, 0));
            paint5.setStyle(Paint.Style.STROKE);  //边框
            paint5.setStrokeWidth(2f);

            //血条实心
            paint6.setColor(Color.rgb(248, 248, 255));
            paint6.setStyle(Paint.Style.FILL);
            paint6.setStrokeWidth(10f);
            paint6.setAlpha(200);

            //方框实心
            paint7.setColor(Color.rgb(255, 255, 255));
            paint7.setStyle(Paint.Style.FILL);
            paint7.setAlpha(0);



        }



        private void Draw() {
            SimpleDateFormat simpleDateFormat = null;// HH:mm:ss
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            }
            Date 时间 = new Date(System.currentTimeMillis());
            if (绘制状态) {
                int x宽= getResources().getDisplayMetrics().widthPixels;//实际宽
                int y高= getResources().getDisplayMetrics().heightPixels;//实际高

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    canvas.drawText("Time：" + simpleDateFormat.format(时间) + "   " + "Resolusi : " + x宽 + "x" + y高 + " Offset : " + x1, x宽 / 1, 10, 绘制文本);
                }

                //hero = 0;//默认人数
                x1 = 0;//偏移
                File file = new File("/sdcard/hz.log");//坐标输出必须和cpp的路径一致

                /*if (file == null)
                 return;
                 */
                String bb = getFileContent(file);

                String[] Concent = bb.split(";");
                //String[] Concent = string.split(";");
                for (int i = 0; i < Concent.length; i++) {
                    String[] zb = Concent[i].split(",");
                    try {
                        x = Float.parseFloat(zb[0]);//y轴
                        y = Float.parseFloat(zb[1]);//x轴
                        w = Float.parseFloat(zb[2]);//宽
                        h = Float.parseFloat(zb[3]);//高
                        M = Float.parseFloat(zb[4]);//距离
                        hp = Float.parseFloat(zb[5]);//血量
                        ai = Float.parseFloat(zb[6]);//人机

              
                    } catch (Exception v) {
                        v.printStackTrace();
                    }

                    int m = (int) M;//距离
            
                    canvas.drawCircle(x宽 / 2 + x1, y高 / 2, 350, 准心圆圈);
                    
                    if (x <= x宽 && x >= 0 && y >= 0 && w >= 0 && h >= 0 && hp > 0 && m < 400 && hp < 121) {

                        if (Start.射线().equals("true")) {
                            if (ai == 0) {
                                canvas.drawLine(y高 / 2, 10, x + x1, y - h / 2, 人机射线);//射线人机
                            } else {
                                canvas.drawLine(y高 / 2, 10, x + x1, y, 玩家射线);//射线  //真人
                            }

                        }

                        if (Start.方框().equals("false")) {//方框
                            if (ai == 0) {

                                canvas.drawRect(x + h / 3 + x1, y + h / 2, x - h / 3 + x1, y - h + h / 2, 人机方框);
                            } else {
                                canvas.drawRect(x + h / 3 + x1, y + h / 2, x - h / 3 + x1, y - h + h / 2, 玩家方框);
                            }

                        }



                        //血量
                        if (Start.血条().equals("false")) {//血量

                            canvas.drawRect(x - 120 + 15 + x1, y - h / 2 - 40, x + 120 - 15 + x1, y - h / 2 - 5, paint5);
                            canvas.drawLine(x - 104 + x1, y - h / 2 - 22, x - 100 + hp / 100 * 205 + x1, y - h / 2 - 22 , paint6);

                        }

                    }


                }


                surfaceHolder.unlockCanvasAndPost(canvas);

            }
        }





        @Override
        public void run() {
            Thread syncTask = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (Flag) {
                            try {
                                canvas = surfaceHolder.lockCanvas();
                                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                            } catch (Exception e) {
                                break;
                            }
                            Draw();
                        }
                    }
                });
            syncTask.start();
        }
    }


public class StopDrawService extends Service {

    private boolean 绘制状态;//线程是否继续
    private boolean viewAdded = false;
    private View view;
    private SurfaceView sv;//绘制悬浮
    private WindowManager wm;//悬浮窗

    private WindowManager.LayoutParams params;



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onCreate() {
        view = View.inflate(getApplicationContext(), R.layout.draw, null);
        sv = view.findViewById(R.id.surface_view);
        sv.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        sv.getHolder().addCallback(new SurfaceHolder.Callback() {
                private DrawThread dt;

                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    绘制状态 = true;
                    dt = new DrawThread(surfaceHolder);
                    dt.Flag = true;
                    dt.start();

                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

                    dt.Flag = false;


                }
            });
        wm = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            // android 8.0及以后使用
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            params.flags = computeFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        } else {
            // android 8.0以前使用
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        }

        // params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;               // 设置window type

        // params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_SECURE;  // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）

        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.format = PixelFormat.RGBA_8888;

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (viewAdded) {
            wm.updateViewLayout(view, params);
        } else {
            wm.addView(view, params);
            viewAdded = true;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    //销毁服务
    @Override
    public void onDestroy() {

        if (viewAdded && wm != null) {
            wm.removeView(view);
        }
        super.onDestroy();
    }


    //适配低版本悬浮窗
    private int computeFlags(int curFlags) {
        boolean mTouchable = false;
        if (!mTouchable) {
            curFlags |= WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        } else {
            curFlags &= ~WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        }

        boolean mFocusable = false;
        if (!mFocusable) {
            curFlags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        } else {
            curFlags &= ~WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }

        boolean mTouchModal = true;
        if (!mTouchModal) {
            curFlags |= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        } else {
            curFlags &= ~WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        }

        boolean mOutsideTouchable = false;
        if (mOutsideTouchable) {
            curFlags |= WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        } else {
            curFlags &= ~WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        }

        return curFlags;
    }


    class DrawThread extends Thread {
        private boolean Flag;//用于标注线程是否继续

        private SurfaceHolder surfaceHolder;

        //定义画笔
        Paint 绘制文本=new Paint();
        Paint 准心圆圈 = new Paint();       
        Paint 人机射线 = new Paint();
        Paint 人机方框 = new Paint();
        Paint 玩家射线 = new Paint();
        Paint 玩家方框 = new Paint();
        Paint paint5 = new Paint();
        Paint paint6 = new Paint();
        Paint paint7 = new Paint();

        Canvas canvas = null;
        float x = 0;//x轴
        float y = 0;//y轴
        float w = 0;//宽
        float h = 0;//高
        float M = 0;//距离
        float ai = 0;//人机识别
        float hp = 0;//血量
        float x1;//偏移


        private  String getFileContent(File file) {
            String content = "";
            if (!file.isDirectory()) {
                try {
                    InputStream instream = new FileInputStream(file);
                    if (instream != null) {
                        InputStreamReader inputreader
                            = new InputStreamReader(instream, "UTF-8");
                        BufferedReader buffreader = new BufferedReader(inputreader);
                        String line = "";
                        while ((line = buffreader.readLine()) != null) {
                            content += line;
                        }
                        instream.close();//关闭输入流
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return content;
        }

        public DrawThread(SurfaceHolder h) {
            init();
            surfaceHolder = h;
            Flag = true;
        }

        private void init() {
            //绘制文本
            绘制文本.setColor(0xFFFF0000);
            绘制文本.setAntiAlias(true);
            绘制文本.setDither(true);
            绘制文本.setTextSize(30);


            //准心圆圈
            准心圆圈.setColor(0xFFFF0000);
            准心圆圈.setStyle(Paint.Style.STROKE);
            准心圆圈.setStrokeWidth(3.0f);

            //玩家射线
            玩家射线.setColor(Color.rgb(248, 0, 0));
            玩家射线.setStyle(Paint.Style.FILL);
            玩家射线.setStrokeWidth(1.8f);
            //玩家方框
            玩家方框.setColor(Color.rgb(248, 0, 0));
            玩家方框.setStyle(Paint.Style.STROKE);
            玩家方框.setStrokeWidth(2f);

            //人机射线
            人机射线.setColor(Color.rgb(248, 248, 255));
            人机射线.setStyle(Paint.Style.FILL);
            人机射线.setStrokeWidth(1.8f);
            //人机方框
            人机方框.setColor(Color.rgb(248, 248, 255));
            人机方框.setStyle(Paint.Style.STROKE);
            人机方框.setStrokeWidth(2f);

            //血条边框
            paint5.setColor(Color.rgb(0, 0, 0));
            paint5.setStyle(Paint.Style.STROKE);  //边框
            paint5.setStrokeWidth(2f);

            //血条实心
            paint6.setColor(Color.rgb(248, 248, 255));
            paint6.setStyle(Paint.Style.FILL);
            paint6.setStrokeWidth(10f);
            paint6.setAlpha(200);

            //方框实心
            paint7.setColor(Color.rgb(255, 255, 255));
            paint7.setStyle(Paint.Style.FILL);
            paint7.setAlpha(0);



        }



        private void Draw() {
            SimpleDateFormat simpleDateFormat = null;// HH:mm:ss
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            }
            Date 时间 = new Date(System.currentTimeMillis());
            if (绘制状态) {
                int x宽= getResources().getDisplayMetrics().widthPixels;//实际宽
                int y高= getResources().getDisplayMetrics().heightPixels;//实际高

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    canvas.drawText("Time：" + simpleDateFormat.format(时间) + "   " + "Resolusi : " + x宽 + "x" + y高 + " Offset : " + x1, x宽 / 1, 10, 绘制文本);
                }

                //hero = 0;//默认人数
                x1 = 0;//偏移
                File file = new File("/sdcard/hz.log");//坐标输出必须和cpp的路径一致

                /*if (file == null)
                 return;
                 */
                String bb = getFileContent(file);

                String[] Concent = bb.split(";");
                //String[] Concent = string.split(";");
                for (int i = 0; i < Concent.length; i++) {
                    String[] zb = Concent[i].split(",");
                    try {
                        x = Float.parseFloat(zb[0]);//y轴
                        y = Float.parseFloat(zb[1]);//x轴
                        w = Float.parseFloat(zb[2]);//宽
                        h = Float.parseFloat(zb[3]);//高
                        M = Float.parseFloat(zb[4]);//距离
                        hp = Float.parseFloat(zb[5]);//血量
                        ai = Float.parseFloat(zb[6]);//人机


                    } catch (Exception v) {
                        v.printStackTrace();
                    }

                    int m = (int) M;//距离

                    canvas.drawCircle(x宽 / 2 + x1, y高 / 2, 350, 准心圆圈);

                    if (x <= x宽 && x >= 0 && y >= 0 && w >= 0 && h >= 0 && hp > 0 && m < 400 && hp < 121) {

                        if (Start.射线().equals("true")) {
                            if (ai == 0) {
                                canvas.drawLine(y高 / 2, 10, x + x1, y - h / 2, 人机射线);//射线人机
                            } else {
                                canvas.drawLine(y高 / 2, 10, x + x1, y, 玩家射线);//射线  //真人
                            }

                        }

                        if (Start.方框().equals("false")) {//方框
                            if (ai == 0) {

                                canvas.drawRect(x + h / 3 + x1, y + h / 2, x - h / 3 + x1, y - h + h / 2, 人机方框);
                            } else {
                                canvas.drawRect(x + h / 3 + x1, y + h / 2, x - h / 3 + x1, y - h + h / 2, 玩家方框);
                            }

                        }



                        //血量
                        if (Start.血条().equals("false")) {//血量

                            canvas.drawRect(x - 120 + 15 + x1, y - h / 2 - 40, x + 120 - 15 + x1, y - h / 2 - 5, paint5);
                            canvas.drawLine(x - 104 + x1, y - h / 2 - 22, x - 100 + hp / 100 * 205 + x1, y - h / 2 - 22 , paint6);

                        }

                    }


                }


                surfaceHolder.unlockCanvasAndPost(canvas);

            }
        }





        @Override
        public void run() {
            Thread syncTask = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (Flag) {
                            try {
                                canvas = surfaceHolder.lockCanvas();
                                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                            } catch (Exception e) {
                                break;
                            }
                            Draw();
                        }
                    }
                });
            syncTask.start();
        }
    }
}
}
