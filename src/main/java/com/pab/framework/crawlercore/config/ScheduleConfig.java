//package com.pab.framework.crawlercore.config;
//
//import com.pab.framework.crawlerengine.task.TaskHandler;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.scheduling.Trigger;
//import org.springframework.scheduling.TriggerContext;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
//import org.springframework.scheduling.support.CronTrigger;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.concurrent.ScheduledFuture;
//
//@Component
//public class ScheduleConfig {
//    @Autowired
//    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
//    @Autowired
//    private TaskHandler taskHandler;
//
//    @Bean
//    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
//        return new ThreadPoolTaskScheduler();
//    }
//    private ScheduledFuture<?> future;
//
//    private String cron;
//
//
//    public String getCron() {
//        return cron;
//    }
//
//    public void stopCron() {
//        if (future != null) {
//            future.cancel(true);
//        }
//    }
//    public void setCron(String cron) {
//        this.cron = cron;
//        stopCron();
//        future = threadPoolTaskScheduler.schedule(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    taskHandler.taskRun();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Trigger() {
//            @Override
//            public Date nextExecutionTime(TriggerContext triggerContext) {
//                if ("".equals(cron) || cron == null) {
//                    return null;
//                }
//                CronTrigger trigger = new CronTrigger(cron);
//                Date nextExecDate = trigger.nextExecutionTime(triggerContext);
//                return nextExecDate;
//            }
//        });
//    }
//}
