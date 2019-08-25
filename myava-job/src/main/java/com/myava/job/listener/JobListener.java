package com.myava.job.listener;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * elastic job监听器
 *
 * @author biao
 */
public class JobListener implements ElasticJobListener {

    private static final Logger LOG = LoggerFactory.getLogger(JobListener.class);

    private static final ThreadLocal<Long> TL_START_TIME = new ThreadLocal<>();

    @Override
    public void beforeJobExecuted(ShardingContexts shardingContexts) {
        TL_START_TIME.set(System.currentTimeMillis());
        LOG.info("[{}] start.", shardingContexts.getJobName());
    }

    @Override
    public void afterJobExecuted(ShardingContexts shardingContexts) {
        LOG.info("[{}] finish, spend(ms): {}", shardingContexts.getJobName(), System.currentTimeMillis() - TL_START_TIME.get());
    }
}
