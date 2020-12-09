package com.tencent.hz.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.tencent.hz.R;
import com.tencent.hz.utils.Miscellaneous;
import com.tencent.hz.utils.MyToast;

import java.io.IOException;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;


public class MainActivity extends AppCompatActivity implements OnClickListener
{
    private boolean isDisplay = false;
    private boolean isMenuDis = false;
    private WindowManager manager_ball;
    private WindowManager.LayoutParams params_ball;
    private boolean isopen = false;
    private boolean isShow = false;
    private View ball;
    private Switch tolol,tolol2,tolol3,tolol4;


    Button click;
    Context mcontex;    


    public static final int EXTERNAL_STORAGE_REQ_CODE = 10;
    public static String string = "300,600,200,100,50,60,700,800,900,100,100;100,200,100,100,50,60,700,800,900,100,100";
    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim)
    {
        super.overridePendingTransition(enterAnim, exitAnim);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        初始化Stars();
    
        MyToast.showLong(MainActivity.this, "Welcome to BENKKSTUDIO");
        Miscellaneous.RunShell(("chmod 777 " + getFilesDir() + "/assets/plexusviix"));
        System.out.println("Done");

    }

    public void StopCheat(View v)
    {
        if (isDisplay == true || isMenuDis == true)
        {
            manager_ball.removeView(ball);
            isDisplay = false; isMenuDis = false; isShow = false; isopen = false;
        }
        else
        {
            System.exit(0);
        }
    }

    public void closeFloat(View v)
    {
        if (isDisplay == true || isMenuDis == true)
        {
            manager_ball.removeView(ball);
            isDisplay = false; isMenuDis = false; isShow = false; isopen = false;
        }
        else
        {}
    }

    public void onClick(View v)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                MyToast.showLong(MainActivity.this, "[ Your Name ]");
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
            }
        }
        if (isDisplay == false || isMenuDis == false)
        {
            showFloats();
            isDisplay = true; isMenuDis = true; isopen = true;
        }
        else
        {}

        tolol = ball.findViewById(R.id.test1);
        tolol.setOnCheckedChangeListener(new onc());
        tolol2 = ball.findViewById(R.id.test2);
        tolol2.setOnCheckedChangeListener(new onc());
        tolol3 = ball.findViewById(R.id.test3);
        tolol3.setOnCheckedChangeListener(new onc());
        tolol4 = ball.findViewById(R.id.test4);
        tolol4.setOnCheckedChangeListener(new onc());

    }



    class onc implements OnCheckedChangeListener
    {
        @Override
        public void onCheckedChanged(CompoundButton Njay, boolean isChecked)
        {
            switch (Njay.getId())
            {

                case R.id.test1:
                    if (isChecked)
                    {
                        Miscellaneous.RunShell(("chmod 777 " + getFilesDir() + "/assets/plexusviix"));
                        Miscellaneous.RunShell("su -c " + getFilesDir() + "/assets/plexusviix 13");
                        MyToast.showLong(MainActivity.this, "Aimlock On");               
                    }
                    else
                    {
                        Miscellaneous.RunShell(("chmod 777 " + getFilesDir() + "/assets/plexusviix"));
                        Miscellaneous.RunShell("su -c " + getFilesDir() + "/assets/plexusviix 14");
                        MyToast.showLong(MainActivity.this, "Aimlock Off");             
                    }
                    break;
                case R.id.test2:
                    if (isChecked)
                    {
                        Miscellaneous.RunShell(("chmod 777 " + getFilesDir() + "/assets/plexusviix"));
                        Miscellaneous.RunShell("su -c " + getFilesDir() + "/assets/plexusviix 9");
                        MyToast.showLong(MainActivity.this, "Less Recoil On");               
                    }
                    else
                    {
                        Miscellaneous.RunShell(("chmod 777 " + getFilesDir() + "/assets/plexusviix"));
                        Miscellaneous.RunShell("su -c " + getFilesDir() + "/assets/plexusviix 10");
                        MyToast.showLong(MainActivity.this, "Less Recoil Off");             
                    }
                    break;
                case R.id.test3:
                    if (isChecked)
                    {
                        Miscellaneous.RunShell(("chmod 777 " + getFilesDir() + "/assets/plexusviix"));
                        Miscellaneous.RunShell("su -c " + getFilesDir() + "/assets/plexusviix 3");
                        MyToast.showLong(MainActivity.this, "Wide View On");               
                    }
                    else
                    {
                        Miscellaneous.RunShell(("chmod 777 " + getFilesDir() + "/assets/plexusviix"));
                        Miscellaneous.RunShell("su -c " + getFilesDir() + "/assets/plexusviix 4");
                        MyToast.showLong(MainActivity.this, "Wide View Off");             
                    }
                    break;
                case R.id.test4:
                    if (isChecked)
                    {
                        Miscellaneous.RunShell(("chmod 777 " + getFilesDir() + "/assets/plexusviix"));
                        Miscellaneous.RunShell("su -c " + getFilesDir() + "/assets/plexusviix 7");
                        MyToast.showLong(MainActivity.this, "Night Mode On");               
                    }
                    else
                    {
                        Miscellaneous.RunShell(("chmod 777 " + getFilesDir() + "/assets/plexusviix"));
                        Miscellaneous.RunShell("su -c " + getFilesDir() + "/assets/plexusviix 8");
                        MyToast.showLong(MainActivity.this, "Night Mode Off");             
                    }
                    break;
            }

        }
    }

    
    

    @SuppressLint({"RtlHardcoded", "ClickableViewAccessibility"})
    private void showFloats()
    {
        if (isShow)
        {
            MyToast.showLong(MainActivity.this, "×_•");
            return;
        }

        manager_ball = (WindowManager) getSystemService(WINDOW_SERVICE);
        params_ball = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            params_ball.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        else
        {
            params_ball.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        params_ball.format = PixelFormat.RGBA_8888;
        params_ball.gravity = Gravity.LEFT | Gravity.TOP;
        params_ball.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
            WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
            WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params_ball.width = -2;
        params_ball.height = -2;
        params_ball.x = 50;
        params_ball.y = 200;
        params_ball.dimAmount = 0;
        ball = View.inflate(getApplicationContext(), R.layout.xfc, null);
        ImageView img = ball.findViewById(R.id.ball_img);
        img.setOnTouchListener(moveLis(ball, manager_ball, params_ball));
        img.setImageResource(R.drawable.icon);
        manager_ball.addView(ball, params_ball);
        isShow = true;
    }
    public View.OnTouchListener moveLis(final View v, final WindowManager manager, final WindowManager.LayoutParams params)
    {
        return new View.OnTouchListener() {
            int x;
            int y;
            int _x;
            int _y;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View p1, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        x = (int) event.getRawX();
                        y = (int) event.getRawY();
                        _x = params.x;
                        _y = params.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int nowX = (int) event.getRawX();
                        int nowY = (int) event.getRawY();
                        int movedX = nowX - x;
                        int movedY = nowY - y;
                        x = nowX;
                        y = nowY;
                        params.x = params.x + movedX;
                        params.y = params.y + movedY;
                        manager.updateViewLayout(v, params);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (Math.abs(params.x - _x) < 5.0 && Math.abs(params.y - _y) < 5.0)
                        {
                            LinearLayout lin = v.findViewById(R.id.ball_lin);
                            if (isopen)
                            {
                                lin.setVisibility(View.GONE);
                            }
                            else
                            {
                                lin.setVisibility(View.VISIBLE);
                            }
                            isopen = !isopen;
                        }
                        break;
                }
                return true;
            }
        };
    }


    private void 初始化Stars()
    {

        Miscellaneous.StatusNavigationColor(MainActivity.this, R.color.atropurpureus);

        // EXTRACT ASSETS COMMAND 

        Miscellaneous.写出assets资源文件(this, getFilesDir() + "/assets", "plexusviix");
        Miscellaneous.写出assets资源文件(this, getFilesDir() + "/assets", "a.sh");
        Miscellaneous.写出assets资源文件(this, getFilesDir() + "/assets", "hz.zip");

        //动态申请储存权限
        int permission = ActivityCompat.checkSelfPermission(this,
                                                            Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED)
        {
            // 请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                              EXTERNAL_STORAGE_REQ_CODE);
        }

    }


}
    


