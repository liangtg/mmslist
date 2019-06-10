package com.aitek.app.mms;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    /**
     * 时间转化为聊天界面显示字符串
     *
     * @param timeStamp 单位为秒
     */
    public static String getChatTimeStr(long timeStamp) {
        if (timeStamp == 0) return "";
        Calendar inputTime = Calendar.getInstance();
        String timeStr = timeStamp + "";
        if (timeStr.length() == 10) {
            timeStamp = timeStamp * 1000;
        }
        inputTime.setTimeInMillis(timeStamp);
        Date currenTimeZone = inputTime.getTime();
        Calendar calendar = Calendar.getInstance();
//        if (calendar.before(inputTime)){
//            //当前时间在输入时间之前
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + "年"+"MM"+"月"+"dd"+"日");
//            return sdf.format(currenTimeZone);
//        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
            return timeFormatStr(inputTime, sdf.format(currenTimeZone));
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
            return "昨天" + " " + timeFormatStr(inputTime, sdf.format(currenTimeZone));
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            if (calendar.before(inputTime)) {
                SimpleDateFormat sdf = new SimpleDateFormat("M" + "月" + "d" + "日");
                String temp1 = sdf.format(currenTimeZone);
                SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm");
                String temp2 = timeFormatStr(inputTime, sdf1.format(currenTimeZone));
                return temp1 + temp2;
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + "年" + "M" + "月" + "d" + "日");
                String temp1 = sdf.format(currenTimeZone);
                SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm");
                String temp2 = timeFormatStr(inputTime, sdf1.format(currenTimeZone));
                return temp1 + temp2;
            }

        }

    }

    /**
     * 24小时制转化成12小时制
     *
     * @param strDay
     */
    public static String timeFormatStr(Calendar calendar, String strDay) {
        String tempStr = "";
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour > 11) {
            tempStr = "下午" + " " + strDay;
        } else {
            tempStr = "上午" + " " + strDay;
        }
        return tempStr;
    }

}
