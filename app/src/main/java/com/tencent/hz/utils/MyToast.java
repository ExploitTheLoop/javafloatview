package com.tencent.hz.utils;



import android.content.Context;
import android.widget.Toast;

/**
 * Author：  KuenCheung
 * Email：   zhang_quan_888@163.com
 * Time：    2018/6/2
 * Description：吐司工具类
 */

/*
 *leakcary已检测，内存泄露了，toast定义的静态成员变量，
 *在activity和fragment的ondestory()方法走完之后toast没有得到及时销毁，
 *解决方法：在ondestory()方法结束之前，判断toast是否为null,不为null,则z置为null.

 @Override
 protected void onDestroy() {
 super.onDestroy();
 if (MyToast.toast != null) MyToast.toast = null;
 }
 */
public final class MyToast {
    private MyToast() { }

    public static boolean isShow = true;
    public static Toast toast;

    //短时间显示Toast
    public static void showShort(Context context, CharSequence message) {
        if (isShow) {
            if (toast == null) {
                toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            } else {
                toast.cancel();//关闭吐司显示
                toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            }
            toast.show();
        }
    }

    //短时间显示Toast
    public static void showShort(Context context, int message) {
        if (isShow) {
            if (toast == null) {
                toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            } else {
                toast.cancel();//关闭吐司显示
                toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            }
            toast.show();
        }
    }

    //长时间显示Toast
    public static void showLong(Context context, CharSequence message) {
        if (isShow) {
            if (toast == null) {
                toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            } else {
                toast.cancel();//关闭吐司显示
                toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            }
            toast.show();
        }
    }

    //长时间显示Toast
    public static void showLong(Context context, int message) {
        if (isShow) {
            if (toast == null) {
                toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            } else {
                toast.cancel();//关闭吐司显示
                toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            }
            toast.show();
        }
    }

    //自定义显示Toast时间
    public static void show(Context context, CharSequence message, int duration) {
        if (isShow) {
            Toast.makeText(context, message, duration).show();
        }
    }

    //自定义显示Toast时间
    public static void show(Context context, int message, int duration) {
        if (isShow) {
            Toast.makeText(context, message, duration).show();
        }
    }
}
