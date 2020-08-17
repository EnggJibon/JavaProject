/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.util.FileUtil;
import static com.kmcj.karte.util.FileUtil.SEPARATOR;
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
 * @author f.kitaoji
 */
@Singleton
@Startup
public class ApplicationStartup {

    @Resource
    public TimerService timerService;
    @Inject
    private KartePropertyService kartePropertyService;
    

    @PostConstruct
    public void init() { //@Observes @Initialized(ApplicationScoped.class) Object event) {
        ScheduleExpression scheduleExpression = new ScheduleExpression();
        scheduleExpression.second("*/30");
        scheduleExpression.minute("*");
        scheduleExpression.hour("*");
        scheduleExpression.dayOfMonth("*");
        scheduleExpression.month("*");
        scheduleExpression.year("*");
        scheduleExpression.dayOfWeek("*");

        TimerConfig timerConfig = new TimerConfig("batchJobTimer", false);

        timerService.createCalendarTimer(scheduleExpression, timerConfig);
    }

    @Timeout
    public void executeTimer(Timer timer) {
        //30秒ごとに実行確認し、すでに実行中であれば実行しない。
        JobOperator job = BatchRuntime.getJobOperator();
        
        //ModuleフォルダにBatch停止指令ファイル(batchstop)があれば起動しない（デプロイ時にバッチを停止するため）
        boolean batchStop = false;
        StringBuilder batchStopFilePath = new StringBuilder(kartePropertyService.getDocumentDirectory());
        batchStopFilePath.append(FileUtil.SEPARATOR);
        batchStopFilePath.append(CommonConstants.MODULE);
        batchStopFilePath.append(FileUtil.SEPARATOR);
        batchStopFilePath.append("batchstop");
        File file = new File(batchStopFilePath.toString());
        if (file.exists()) {
            batchStop = true;
        }
        StringBuilder batchStopMsg = new StringBuilder("Batch process is ordered to stop.");
        
        List<Long> karteBatchlets = null;
        try {
            karteBatchlets = job.getRunningExecutions("karteBatchlet");
        }
        catch (NoSuchJobException ne) {
            //do nothing
        }
        if (karteBatchlets != null && karteBatchlets.size() > 0) {
            batchStopMsg.append("KarteBatchlet is now running.");
        } else {
            if (!batchStop) {
                long jobId = job.start("karteBatchlet", null);
                System.out.println("KarteBatchlet has started. Job ID is " + jobId);
            }
            batchStopMsg.append("No batch process is running.");
        }
        
        if (batchStop) {
            System.out.println(batchStopMsg.toString());
        }
        
        /*
        //金型と設備外部データ取得
        List<Long> externalDataGetBatchlets = null;
        try {
            externalDataGetBatchlets = job.getRunningExecutions("externalDataGetBatchlet");
        }
        catch (NoSuchJobException ne) {
            //do nothing
        }
        if (externalDataGetBatchlets != null && externalDataGetBatchlets.size() > 0) {
            //System.out.println("externalDataGetBatchlet is now running. ID = " + externalDataGetBatchlets.get(0));
        } else {
            job.start("externalDataGetBatchlet", null);
           // System.out.println("externalDataGetBatchlet has started. ID = " + sampleId);
        }
        //生産計画更新
        List<Long> runningProductionPlanUpdateIds = null;
        try {
            runningProductionPlanUpdateIds = job.getRunningExecutions("productionPlanUpdateBatchlet");
        }
        catch (NoSuchJobException ne) {
            //do nothing
        }
        if (runningProductionPlanUpdateIds != null && runningProductionPlanUpdateIds.size() > 0) {
            //do nothing
        } else {
            job.start("productionPlanUpdateBatchlet", null);
        }
*/

    }
}
