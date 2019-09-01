package com.myava.springboot.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Spring task demo
 *
 * @author biao
 */
@Component
public class DemoSpringTask {

    private static final Logger LOG = LoggerFactory.getLogger(DemoSpringTask.class);

    @Value("${demo.spring.task.cron:0 0/2 * * * ?}")
    private  String cron;

    @Scheduled(cron = "${demo.spring.task.cron:0 0/2 * * * ?}")
    public void execute() {
        LOG.info("This is spring task schedule, cron: {}", cron);
    }

}
