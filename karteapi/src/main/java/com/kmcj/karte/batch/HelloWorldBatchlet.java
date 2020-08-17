/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch;

/**
 *
 * @author f.kitaoji
 */
import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@Dependent
public class HelloWorldBatchlet extends AbstractBatchlet {
// ジョブ名称を取得するためにJobContextをインジェクション
    @Inject
    JobContext jobContext;

    @Override
    public String process() throws Exception {
        System.out.println("Hello World. " + (new java.util.Date()) + "**********" + jobContext.getJobName());
        String sNull = null;
        if (sNull.equals("Hello")) {
            System.out.println("This never happenes.");
        }
        Thread.sleep(15000);
        return "SUCCESS";
    }    
}
