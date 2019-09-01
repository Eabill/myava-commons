package com.myava.springboot.job;

import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.myava.job.annotation.JobScheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * elastic job demo
 *
 * @author biao
 */
@Component
@JobScheduled(cron = "0 0/1 * * * ?", disabled = true)
public class DemoElasticJob implements SimpleJob {

    private static final Logger LOG = LoggerFactory.getLogger(DemoElasticJob.class);

    @Override
    public void execute(ShardingContext shardingContext) {
        LOG.info(JSON.toJSONString(shardingContext));
    }
}
