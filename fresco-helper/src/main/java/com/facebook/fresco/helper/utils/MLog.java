package com.facebook.fresco.helper.utils;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * 打印LOG，用于调试，可直接定位到行数
 * <p>
 * Created by android_ls on 2020/4/9 1:25 PM.
 *
 * @author android_ls
 * @version 1.0
 */
public class MLog {

    private static final String DEFAULT_MESSAGE = "execute";
    private static final String PARAM = "Param";
    private static final String NULL = "null";
    private static final String TAG_DEFAULT = "MLog";
    private static final String SUFFIX = ".java";

    private static final int MIN_STACK_OFFSET = 5;

    private static String mGlobalTag;
    private static boolean IS_SHOW_LOG = true;

    public static void init(String tag, boolean isShowLog) {
        IS_SHOW_LOG = isShowLog;
        mGlobalTag = tag;
    }

    public static void v() {
        printLog(Log.VERBOSE, null, DEFAULT_MESSAGE);
    }

    public static void v(Object msg) {
        printLog(Log.VERBOSE, null, msg);
    }

    public static void v(String tag, Object... objects) {
        printLog(Log.VERBOSE, tag, objects);
    }

    public static void d() {
        printLog(Log.DEBUG, null, DEFAULT_MESSAGE);
    }

    public static void d(Object msg) {
        printLog(Log.DEBUG, null, msg);
    }

    public static void d(String tag, Object... objects) {
        printLog(Log.DEBUG, tag, objects);
    }

    public static void i() {
        printLog(Log.INFO, null, DEFAULT_MESSAGE);
    }

    public static void i(Object msg) {
        printLog(Log.INFO, null, msg);
    }

    public static void i(String tag, Object... objects) {
        printLog(Log.INFO, tag, objects);
    }

    public static void w() {
        printLog(Log.WARN, null, DEFAULT_MESSAGE);
    }

    public static void w(Object msg) {
        printLog(Log.WARN, null, msg);
    }

    public static void w(String tag, Object... objects) {
        printLog(Log.WARN, tag, objects);
    }

    public static void e() {
        printLog(Log.ERROR, null, DEFAULT_MESSAGE);
    }

    public static void e(Object msg) {
        printLog(Log.ERROR, null, msg);
    }

    public static void e(String tag, Object... objects) {
        printLog(Log.ERROR, tag, objects);
    }

    private static void printLog(int type, String tagStr, Object... objects) {
        if (!IS_SHOW_LOG) {
            return;
        }

        String[] contents = wrapperContent(tagStr, objects);
        String tag = contents[0];
        String msg = contents[1];
        String headString = contents[2];
        printDefault(type, tag, headString + "\n" + msg);
    }

    private static String[] wrapperContent(String tagStr, Object... objects) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        checkNotNull(stackTrace);

        int stackOffset = MIN_STACK_OFFSET;
        for (int i = MIN_STACK_OFFSET; i < stackTrace.length; i++) {
            StackTraceElement e = stackTrace[i];
            String name = e.getClassName();
            if (!name.equals(MLog.class.getName())) {
                stackOffset = i;
                break;
            }
        }

        StackTraceElement targetElement = stackTrace[stackOffset];
        String className = targetElement.getClassName();
        String[] classNameInfo = className.split("\\.");
        if (classNameInfo.length > 0) {
            className = classNameInfo[classNameInfo.length - 1] + SUFFIX;
        }

        if (className.contains("$")) {
            className = className.split("\\$")[0] + SUFFIX;
        }

        String methodName = targetElement.getMethodName();
        int lineNumber = targetElement.getLineNumber();

        if (lineNumber < 0) {
            lineNumber = 0;
        }

        String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);

        /**
         * 支持全局TAG和局部TAG混搭
         *
         * 示例：
         * KLog.i("使用全局的TAG");
         KLog.i("Test", "使用局部的TAG");
         执行结果：
         I/BoloLog: [ (Method.java:0)#Invoke ] 使用全局的TAG
         I/Test: [ (Method.java:0)#Invoke ] 使用局部的TAG
         **/
        String tag = tagStr;
        if (TextUtils.isEmpty(tag)) {
            if (!TextUtils.isEmpty(mGlobalTag)) {
                tag = mGlobalTag;
            } else {
                tag = TAG_DEFAULT;
            }
        }

        String msg = getObjectsString(objects);
        String headString = "[ (" + className + ":" + lineNumber + ")-->" + methodNameShort + " ] ";

        return new String[]{tag, msg, headString};
    }

    private static String getObjectsString(Object... objects) {
        if (objects.length > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n");
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                if (object == null) {
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ").append(NULL).append("\n");
                } else {
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ").append(object.toString()).append("\n");
                }
            }
            return stringBuilder.toString();
        } else {
            Object object = objects[0];
            return object == null ? NULL : object.toString();
        }
    }

    private static void printDefault(int type, String tag, String msg) {
        int index = 0;
        int maxLength = 4000;
        int countOfSub = msg.length() / maxLength;

        if (countOfSub > 0) {
            for (int i = 0; i < countOfSub; i++) {
                String sub = msg.substring(index, index + maxLength);
                printSub(type, tag, sub);
                index += maxLength;
            }
            printSub(type, tag, msg.substring(index, msg.length()));
        } else {
            printSub(type, tag, msg);
        }
    }

    private static void printSub(int type, String tag, String sub) {
        switch (type) {
            case Log.VERBOSE:
                Log.v(tag, sub);
                break;
            case Log.DEBUG:
                Log.d(tag, sub);
                break;
            case Log.INFO:
                Log.i(tag, sub);
                break;
            case Log.WARN:
                Log.w(tag, sub);
                break;
            case Log.ERROR:
                Log.e(tag, sub);
                break;
        }
    }

    private static <T> T checkNotNull(@Nullable final T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

}