package com.tencent.hz.utils;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 轻量级吐司，支持在上下中间显示
 * @author LayMan(俗人)
 * @call 875367608@qq.com
 */
 
 
 //LayManToast.show(MainActivity.this,"  我是轻量级吐司  ");
   
public class LayManToast {

    /**
     * 不给new
     */
    private LayManToast() {
        throw new UnsupportedOperationException();
    }

    /**
     * 显示默认吐司
     */
    public static void show(Activity activity, String content) {
        handle(activity, content, Config.duration, Config.gravity, Config.textColor, Config.backgroundColor, Config.radius, Config.elevation);
    }

    /**
     * 显示自定义吐司
     */
    public static void show(Activity activity, String content, Config config) {
        handle(activity, content, config.duration, config.gravity, config.textColor, config.backgroundColor, config.radius, config.elevation);
    }

    /**
     * 显示默认吐司
     */
    public static void show(Fragment fragment, String content) {
        handle(fragment.getActivity(), content, Config.duration, Config.gravity, Config.textColor, Config.backgroundColor, Config.radius, Config.elevation);
    }
    /**
     * 显示自定义吐司
     */
    public static void show(Fragment fragment, String content, Config config) {
        handle(fragment.getActivity(), content, config.duration, config.gravity, config.textColor, config.backgroundColor, config.radius, config.elevation);
    }

    /**
     * 吐司处理
     */
    private static void handle(Activity activity, String content, long duration, int gravity, int textColor, int backgroundColor, int radius, int elevation) {
        // Activity空不处理
        if (activity == null)
            return ;

        // 没内容设置空内容
        if (TextUtils.isEmpty(content)) {
            content = "Content is null!";
        }

        // 获取顶级FrameLayout
        ViewGroup parent = (ViewGroup) activity.getWindow().getDecorView();
        // 创建吐司布局
        LinearLayout layout = createToastLayout(activity, content, gravity, textColor, backgroundColor, radius, elevation);

        // 添加到容器
        parent.addView(layout);

        // 创建动画
        createAnimator(parent, layout, gravity, duration);
    }

    /**
     * 创建动画
     */
    private static void createAnimator(final ViewGroup parent, final LinearLayout layout, int gravity, long duration) {
        // 中间显示
        if (gravity == Gravity.CENTER) {
            // 设置透明度0完全透明
            layout.setAlpha(0);

            // 创建显示透明度动画
            ObjectAnimator start = ObjectAnimator.ofFloat(layout, "alpha", 0f, 1f);
            start.setDuration(500);
            start.start();

            // 创建隐藏透明度动画
            final ObjectAnimator end = ObjectAnimator.ofFloat(layout, "alpha", 1f, 0f);
            end.setDuration(500);

            // 延迟隐藏吐司
            layout.postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        // 开始隐藏动画
                        end.start();
                        // 延迟删除吐司
                        layout.postDelayed(new Runnable(){
                                @Override
                                public void run() {
                                    // 从容器删除
                                    parent.removeView(layout);
                                }
                            }, 600);
                    }
                }, duration);
        }

        // 上面显示或者下面显示
        if (gravity == Gravity.TOP || gravity == Gravity.BOTTOM) {
            // 移动的轴，这里为下面的y轴终点
            float y = - 290f;
            // 是上面的话
            if (gravity == Gravity.TOP) {
                // 改为上面的y轴终点
                y = 290f;
            }

            // 创建y轴显示移动动画
            ObjectAnimator start = ObjectAnimator.ofFloat(layout, "translationY", 0f, y);
            start.setDuration(500);
            start.start();

            // 创建y轴隐藏移动动画
            final ObjectAnimator end = ObjectAnimator.ofFloat(layout, "translationY", y, 0f);
            end.setDuration(500);

            // 延迟隐藏吐司
            layout.postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        // 开始隐藏动画
                        end.start();
                        // 延迟删除吐司
                        layout.postDelayed(new Runnable(){
                                @Override
                                public void run() {
                                    // 从容器删除
                                    parent.removeView(layout);
                                }
                            }, 600);
                    }
                }, duration);
        }
    }

    /**
     * 创建吐司布局
     */
    private static LinearLayout createToastLayout(Context context, String content, int gravity, int textColor, int backgroundColor, int radius, int elevation) {
        // 布局参数
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        // 吐司显示位置
        params.gravity = gravity | Gravity.CENTER;

        // 左上右下外边距，这里默认设置为下外边距
        int leftMargin = dip2px(context, 16);
        int topMargin = 0;
        int rightMargin = dip2px(context, 16);
        int bottomMargin = - dip2px(context, 50);

        // 如果是上面的话
        if (gravity == Gravity.TOP) {
            // 上外边距为 -50， 这样布局就不可见，配合动画显示
            topMargin = - dip2px(context, 50);
            // 下外边距为 0
            bottomMargin = 0;
        }
        // 如果是中间的话
        if (gravity == Gravity.CENTER) {
            // 上外边距为 0
            topMargin = 0;
            // 下外边距为 0
            bottomMargin = 0;
        }
        // 设置外边距
        params.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

        // 创建布局
        LinearLayout layout = new LinearLayout(context);
        // 设置对齐方式
        layout.setGravity(Gravity.CENTER);
        // 设置布局参数
        layout.setLayoutParams(params);
        //设置z轴阴影
        layout.setElevation(dip2px(context, elevation));
        // 设置最小高度
        layout.setMinimumHeight(dip2px(context, 50));

        // 创建背景
        GradientDrawable gd = new GradientDrawable();
        // 设置颜色
        gd.setColor(backgroundColor);
        // 设置圆角
        gd.setCornerRadius(dip2px(context, radius));

        // 为布局设置背景
        layout.setBackground(gd);

        // 创建文本
        TextView tv = new TextView(context);
        // 设置内容
        tv.setText(content);
        // 设置文本颜色
        tv.setTextColor(textColor);
        // 设置文本粗体
        tv.getPaint().setFakeBoldText(true);
        // 设置内边距
        tv.setPadding(dip2px(context, 16), 0, dip2px(context, 16), 0);

        // 添加到容器
        layout.addView(tv);

        // 返回容器
        return layout;
    }

    /**
     * 吐司默认配置
     */
    static class Config {
        // 显示时间
        static long duration = 2000;
        // 显示位置
        static int gravity = Gravity.BOTTOM;
        // 文本颜色
        static int textColor = Color.WHITE;
        // 背景颜色
        static int backgroundColor = Color.BLACK;
        // 圆角
        static int radius = 5;
        // z轴阴影
        static int elevation = 2;
    }

    /**
     * dp转px
     */
    private static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (scale * dpValue + 0.5f);
    }
}
