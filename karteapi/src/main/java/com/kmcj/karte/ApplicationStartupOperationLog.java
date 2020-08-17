/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.util.FileUtil;
import java.io.File;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.batch.operations.JobOperator;
import javax.batch.operations.NoSuchJobException;
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
 * @author shinpei.a
 */
@Singleton
@Startup
public class ApplicationStartupOperationLog {

    @Resource
    public TimerService timerService;
    @Inject
    private KartePropertyService kartePropertyService;
    

    @PostConstruct
    public void init() { //@Observes @Initialized(ApplicationScoped.class) Object event) {
        //毎時5分に起動する
        ScheduleExpression scheduleExpression = new ScheduleExpression();
        // バッチの実行サイクル設定
        // テスト用
//        scheduleExpression.second("*/30");
//        scheduleExpression.minute("*");
//        scheduleExpression.hour("*");
        // 本番用
        scheduleExpression.minute("5");
        scheduleExpression.hour("*/1");
        // 共通部分
        scheduleExpression.dayOfMonth("*");
        scheduleExpression.month("*");
        scheduleExpression.year("*");
        scheduleExpression.dayOfWeek("*");

        TimerConfig timerConfig = new TimerConfig("opelogBatchJobTimer", false);

        timerService.createCalendarTimer(scheduleExpression, timerConfig);
    }

    @Timeout
    public void executeTimer(Timer timer) {
        //毎時5分に起動
        JobOperator job = BatchRuntime.getJobOperator();

        List<Long> opelogBatchlets = null;
        try {
            opelogBatchlets = job.getRunningExecutions("operationLogBatchlet");
        }
        catch (NoSuchJobException ne) {
            //do nothing
        }
        if (opelogBatchlets != null && opelogBatchlets.size() > 0) {
            System.out.println("OperationLogBatchlet is now running.");
        } else {
            long jobId = job.start("operationLogBatchlet", null);
            System.out.println("OperationLogBatchlet has started. Job ID is " + jobId);
        }
    }
}
