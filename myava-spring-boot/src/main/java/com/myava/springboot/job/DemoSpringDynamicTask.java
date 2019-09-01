package com.myava.springboot.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * Spring dynamic task demo
 *
 * @author biao
 */
@Component
public class DemoSpringDynamicTask implements SchedulingConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(DemoSpringDynamicTask.class);

    @Value("${demo.spring.dynamic.task.cron:0 0/2 * * * ?}")
    private String cron;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        Runnable runnable = () -> {
            LOG.info("This is spring dynamic task schedule, cron: {}", cron);
        };
        Trigger trigger = triggerContext -> {
            CronTrigger cronTrigger = new CronTrigger(cron);
            return cronTrigger.nextExecutionTime(triggerContext);
        };
        scheduledTaskRegistrar.addTriggerTask(runnable, trigger);
    }
}
