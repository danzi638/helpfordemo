package com.example.jiayin.helpfordemo.utils;

import android.util.Log;

import com.example.jiayin.helpfordemo.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.hyphenate.chat.EMGCMListenerService.TAG;

/**
 * Created by jiayin on 2017/9/6.
 */

public class OftenUtils {
    //时间差
    public static long timeValue(String endTime) {
        Log.e(TAG, "timeValue: " + endTime );
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date startDateTime = df.parse(getLocalTime());
            Date endtDateTime = df.parse(endTime);
            Log.e(TAG, "timeValue: " + endtDateTime.getTime());
            Log.e(TAG, "timeValue: " + startDateTime.getTime());
            return endtDateTime.getTime() - startDateTime.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
    //系统当前时间
    public static String getLocalTime() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return year + "-" + month + "-" + day + " " + hour + ":" + minute;
    }
    //转换状态文字
    public static String setStatuText(int status) {
        switch (status) {
            case 0:
                return "未支付";
            case 1:
                return "已支付";
            case 2:
                return "已接单";
            case 3:
                return "已完成";
            case 4:
                return "已过期";
            case 5:
                return "无人接单";
            case 6:
                return "已违约";
            case 7:
                return "待确认";
            default:
                return null;
        }
    }
    //转换状态图片
    public static int setStatuImage(int status) {
        switch (status) {
            case 0:
                return R.drawable.status_0;
            case 1:
                return R.drawable.status_1;
            case 2:
                return R.drawable.status_2;
            case 3:
                return R.drawable.status_3;
            case 4:
                return R.drawable.status_4_5;
            case 5:
                return R.drawable.status_5;
            case 6:
                return R.drawable.status_6;
            case 7:
                return R.drawable.status_7;
            default:
                return 0;
        }
    }
    //格式化时间
    public static String formatTime(Date time) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return df.format(time);
    }
    //格式化时间
    public static String formatTimeYYYY_MM_DD(Date time) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(time);
    }

    //图片转换为字节
    public static byte[] getBytes(String filePath){
        File file = new File(filePath);
        ByteArrayOutputStream out = null;
        try {
            FileInputStream in = new FileInputStream(file);
            out = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int i = 0;
            while ((i = in.read(b)) != -1) {

                out.write(b, 0, b.length);
            }
            out.close();
            in.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] s = out.toByteArray();
        return s;

    }
}
