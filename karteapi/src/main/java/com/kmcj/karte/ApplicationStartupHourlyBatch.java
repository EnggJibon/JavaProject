/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte;

import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.util.DateFormat;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;

/**
 *
 * @author f.kitaoji
 */
@Singleton
@Startup
public class ApplicationStartupHourlyBatch {
    @Resource
    public TimerService timerService;
    //@Inject
    //private KartePropertyService kartePropertyService;
    @Inject 
    private CnfSystemService cnfSystemService;
    

    @PostConstruct
    public void init() { //@Observes @Initialized(ApplicationScoped.class) Object event) {
        //毎時0分に起動する
        ScheduleExpression scheduleExpression = new ScheduleExpression();
        scheduleExpression.minute("0");
        scheduleExpression.hour("*/1");
        scheduleExpression.dayOfMonth("*");
        scheduleExpression.month("*");
        scheduleExpression.year("*");
        scheduleExpression.dayOfWeek("*");

        TimerConfig timerConfig = new TimerConfig("hourlyBatchJobTimer", false);

        timerService.createCalendarTimer(scheduleExpression, timerConfig);
    }

    @Timeout
    public void executeTimer(Timer timer) {
        System.out.println("Hourly scheduler for batchlet is alive. [" + DateFormat.getCurrentDateTime() + "]");
        //毎時0分に起動
        JobOperator job = BatchRuntime.getJobOperator();
        //アラートメールジョブは一日一回実行。システム設定にて設定した時刻
        String sendTime = null; //24H:MM
        CnfSystem cnfSystem = cnfSystemService.findByKey("system", "daily_alert_mail_send_time");
        if (cnfSystem != null) {
            sendTime = cnfSystem.getConfigValue();
        }
        boolean executeMailAlertJob = false;
        if (sendTime != null) {
            int sendHour;
            int colonIndex = sendTime.indexOf(":"); //コロンが含まれるか検査
            if (colonIndex >= 0) {
                sendHour = Integer.valueOf(sendTime.substring(0, colonIndex)); //コロンが含まれているときはコロンの前まで切り取る
            }
            else {
                sendHour = Integer.valueOf(sendTime); //コロンがなければそのままの値を利用
            }
            String currentHourStr = DateFormat.dateToStr(new java.util.Date(), "HH");
            int currentHour = Integer.valueOf(currentHourStr);
            executeMailAlertJob = sendHour == currentHour;
        }
        if (executeMailAlertJob) {
            job.start("dailyMailAlertBatchlet", null);
        }
        sendDailyInspectMail(job);
        job.start("hourlyBatchlet", null);
    }
    
    private void sendDailyInspectMail(JobOperator job) {
        CnfSystem cnfSystem = cnfSystemService.findByKey("system", "component_inspection_notice_time");
        if(cnfSystem == null) {
            return;
        }
        String sendTime = cnfSystem.getConfigValue();
        Set<Integer> times = Arrays.asList(sendTime.split(",")).stream().map(str->toInt(str)).collect(Collectors.toSet());
        String currentHourStr = DateFormat.dateToStr(new java.util.Date(), "HH");
        if(times.contains(Integer.valueOf(currentHourStr))) {
            long jobId = job.start("sendMailInsectionResultBatchlet", null);
            System.out.println("sendMailInsectionResultBatchlet has started. Job ID is " + jobId);
        }
    }
    
    private Integer toInt(String str) {
        try {
            return Integer.valueOf(str.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
