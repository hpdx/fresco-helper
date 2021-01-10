package com.facebook.fresco.helper.statusbar;

import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class StatusBarFontColorUtils {

    /**
     * 已知系统类型时，设置状态栏黑色字体图标。
     * 适配4.4以上版本MIUI、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @return 1:MIUI 2:Flyme 3:android6.0
     */
    public static void statusBarLightMode(Activity activity, boolean black) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (isMeizu()) {
                    FlymeSetStatusBarLightMode(activity.getWindow(), black);
                } else if (isXiaomi()) {
                    MIUISetStatusBarLightMode(activity.getWindow(), black);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (black) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static void FlymeSetStatusBarLightMode(Window window, boolean dark) {
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
            } catch (Exception e) {

            }
        }
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static void MIUISetStatusBarLightMode(Window window, boolean dark) {
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
            } catch (Exception e) {

            }
        }
    }

    /**
     * 检测是否Xiaomi手机
     */
    public static boolean isXiaomi() {
        String manufacturer = Build.MANUFACTURER;
        return TextUtils.equals("xiaomi", manufacturer.toLowerCase());
    }

    /**
     * 检测是否Meizu手机
     */
    public static boolean isMeizu() {
        String manufacturer = Build.MANUFACTURER;
        return TextUtils.equals("meizu", manufacturer.toLowerCase());
    }

    /**
     * 检测是否Meitu手机
     */
    public static boolean isMeitu() {
        String manufacturer = Build.MANUFACTURER;
        return TextUtils.equals("meitu", manufacturer.toLowerCase());
    }

    /**
     * 检测是否Oppo手机
     */
    public static boolean isOppo() {
        String manufacturer = Build.MANUFACTURER;
        return TextUtils.equals("oppo", manufacturer.toLowerCase());
    }

    /**
     * 检测是否HUAWEI手机
     */
    public static boolean isHuawei() {
        String manufacturer = Build.MANUFACTURER;
        return TextUtils.equals("huawei", manufacturer.toLowerCase());
    }

}
