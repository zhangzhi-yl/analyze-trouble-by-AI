package org.yy.util;

import org.yy.entity.PageData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    /*
     *yyyy-MM-dd格式转换为时间戳 /
     */
    public static String TimeToStamp(String date) throws Exception {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final Date datetime = sdf.parse(date);//将你的日期转换为时间戳
        final String time =String.valueOf(datetime.getTime());
        return time;
    }

    /**
     * 根据year年的第week周，查询本周的起止时间
     * @param year 年份
     * @param week 周数
     */
    public static PageData weekToDayFormate(int year, int week){

        PageData res = new PageData();

        Calendar calendar = Calendar.getInstance();
        // ①.设置该年份的开始日期：第一个月的第一天
        calendar.set(year,0,1);
        // ②.计算出第一周还剩几天：+1是因为1号是1天
        int dayOfWeek = 7 - calendar.get(Calendar.DAY_OF_WEEK) + 1;
        // ③.周数减去第一周再减去要得到的周
        week = week - 2;
        // ④.计算起止日期
        calendar.add(Calendar.DAY_OF_YEAR,week * 7 + dayOfWeek);
        res.put("StartTime",new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
//        System.out.println("开始日期：" + new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_YEAR, 6);
        res.put("EndTime",new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
//        System.out.println("结束日期：" + new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));

        return res;
    }


    /**
     * 根据季度数获取季度的开始时间结束时间
     * @param quarter 季度
     * @return
     */
    public static PageData getQuarterStartEndTime(int year,int quarter) {
        PageData result = new PageData();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        try {
            if (quarter == 1 ) {
                start.set(Calendar.YEAR,year);
                start.set(Calendar.MONTH, 0);
                start.set(Calendar.DATE,1);
                end.set(Calendar.YEAR,year);
                end.set(Calendar.MONTH, 2);
                end.set(Calendar.DATE, 31);
            } else if (quarter == 2) {
                start.set(Calendar.YEAR,year);
                start.set(Calendar.MONTH, 3);
                start.set(Calendar.DATE,1);
                end.set(Calendar.YEAR,year);
                end.set(Calendar.MONTH, 5);
                end.set(Calendar.DATE, 30);
            } else if (quarter == 3) {
                start.set(Calendar.YEAR,year);
                start.set(Calendar.MONTH, 6);
                start.set(Calendar.DATE,1);
                end.set(Calendar.YEAR,year);
                end.set(Calendar.MONTH, 8);
                end.set(Calendar.DATE, 30);
            } else if (quarter == 4) {
                start.set(Calendar.YEAR,year);
                start.set(Calendar.MONTH, 9);
                start.set(Calendar.DATE,1);
                end.set(Calendar.YEAR,year);
                end.set(Calendar.MONTH, 11);
                end.set(Calendar.DATE, 31);
            }
            end.add(Calendar.DATE,1);
            result.put("StartTime", new SimpleDateFormat("yyyy-MM-dd").format(start.getTime()));
            result.put("EndTime",new SimpleDateFormat("yyyy-MM-dd").format(end.getTime()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    /**
     * 日期加一天
     * @param date yyyy-MM-dd
     * @return
     */
    public static String DateAddOneDay(String date){

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            Date dd = df.parse(date);
            calendar.setTime(dd);
            calendar.add(Calendar.DAY_OF_MONTH, 1);//加一天
            //System.out.println("增加一天之后：" + df.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return df.format(calendar.getTime());
    }

    /**
     * 日期加一月
     * @param date yyyy-MM-dd
     * @return
     */
    public static String DateAddOneMonth(String date){

        DateFormat df = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        try {
            Date dd = df.parse(date);
            calendar.setTime(dd);
            calendar.add(Calendar.MONTH, 1);//加一月
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return df.format(calendar.getTime());
    }

    /**
     * 日期减一月
     * @param date yyyy-MM-dd
     * @return
     */
    public static String MonthResuceOne(String date){

        DateFormat df = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        try {
            Date dd = df.parse(date);
            calendar.setTime(dd);
            calendar.add(Calendar.MONTH, -1);//加一月
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return df.format(calendar.getTime());
    }

    /**
     * 日期减一天
     * @param d yyyy-MM-dd
     * @returnd
     */
    public static String DateResuceOne(String d){

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            long dif = df.parse(d).getTime() - 86400 * 1000;//减一天
            date.setTime(dif);
            //System.out.println("减少一天之后：" + df.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return df.format(date);
    }

    /**
     * 日期减一年
     * @param date yyyy-MM-dd
     * @return
     */
    public static String DateDiffOneYear(String date){

        DateFormat df = new SimpleDateFormat("yyyy");
        Calendar calendar = Calendar.getInstance();
        try {
            Date dd = df.parse(date);
            calendar.setTime(dd);
            calendar.add(Calendar.YEAR, -1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return df.format(calendar.getTime());
    }

    /**
     * 日期减一月
     * @param date yyyy-MM-dd
     * @return
     */
    public static String DiffMonth(String date,int i){

        DateFormat df = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        try {
            Date dd = df.parse(date);
            calendar.setTime(dd);
            calendar.add(Calendar.MONTH, i);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return df.format(calendar.getTime());
    }

    public static void main(String[] args){
        String pd = MonthResuceOne("2021-10-25");
        pd = "";
    }
}
