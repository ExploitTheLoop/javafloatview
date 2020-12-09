package com.tencent.hz.utils;

import android.content.Context;
import android.os.Build;

import java.util.Locale;

/**
 * 获取设备信息工具类
 * by 辛木 2020/6/12
 */
public class SystemUtil {

    /**
     * 获取屏幕宽度(px)
     */
    public static int 获取屏幕宽度(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }


    /**
     * 获取屏幕高度(px)
     */
    public static int 获取屏幕高度(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }


    /**
     * 获取当前手机DPI
     */
    public static int 获取手机DPI(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    /**
     * 获取当前手机密度
     */
    public static float 获取手机密度(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String 当前系统语言() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String 系统版本号() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String 手机型号() {
        return Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String 手机厂商() {
        return Build.BRAND;
    }

    /**
     * 获取机型英文代号
     *
     * @return 英文代号
     */
    public static String 机型英文代号() {
        return Build.PRODUCT;
    }


    /**
     * 获取SDK,APL版本号
     *
     * @return SDK
     */
    public static int SDK版本号() {

        return Build.VERSION.SDK_INT;

    }

    /**
     * 获取设备指令集名称（CPU的类型）
     *
     * @return CPU类型
     */
    public static String CPU类型() {

        return Build.CPU_ABI;

    }

    /**
     * 获取主机地址
     *
     * @return 主机地址
     */
    public static String 主机地址() {
        return Build.HOST;
    }

    /**
     * 开发代号
     *
     * @return 开发代号
     */
    public static String 开发代号() {
        return Build.VERSION.CODENAME;
    }

    /**
     * 硬件类型
     *
     * @return 硬件类型
     */
    public static String 硬件类型() {
        return Build.HARDWARE;
    }

    /**
     * 获取版本显示
     *
     * @return 版本显示
     */
    public static String 版本显示() {
        return Build.DISPLAY;
    }

    /**
     * 获取生产id
     *
     * @return 生产id
     */
    public static String 生产id() {
        return Build.ID;
    }







}