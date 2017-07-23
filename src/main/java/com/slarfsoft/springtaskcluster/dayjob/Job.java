package com.slarfsoft.springtaskcluster.dayjob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Auther Zhaoyu Chen @ Fuzhou
 * @Date 2017/7/21 0021.
 */
@Component
//@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Job {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Scheduled(cron = "0 0 2 * * *") // 2:00 AM every day.
    public void dayEndCal(){
        LocalDate today = LocalDate.now();
        ((Job)AopContext.currentProxy()).dayEndCal(Date.valueOf(today));
    }

    public void dayEndCal(Date calDate){
        log.info("Cal job is begin running at {}......",calDate.toString());
        try {
            //Mock long TIme cal
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Cal job is ending running at {}......",calDate.toString());
    }

    @Scheduled(cron = "0 * * * * *") // every 60 seconds,just for test.
    public void noAspectMethod(){
        log.info("no Aspect Method is running ......");
    }
}
