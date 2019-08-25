package com.myava.job.config;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.myava.job.annotation.JobScheduled;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * elastic-job配置
 */
@Component
@ConditionalOnProperty("regCenter.serverList")
public class ElasticJobConfig {

    @Value("${regCenter.serverList}")
    private String serverList;

    @Value("${regCenter.namespace}")
    private String namespace;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        ZookeeperRegistryCenter regCenter = new ZookeeperRegistryCenter(new ZookeeperConfiguration(serverList, namespace));
        regCenter.init();
        Map<String, SimpleJob> jobMap = applicationContext.getBeansOfType(SimpleJob.class);
        jobMap.forEach((key, simpleJob) -> {
            JobScheduled elasticSimpleJobAnnotation = simpleJob.getClass().getAnnotation(JobScheduled.class);
            String cron = StringUtils.defaultIfBlank(elasticSimpleJobAnnotation.cron(), elasticSimpleJobAnnotation.value());
            SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(JobCoreConfiguration.newBuilder(simpleJob.getClass().getName(), cron, elasticSimpleJobAnnotation.shardingTotalCount()).shardingItemParameters(elasticSimpleJobAnnotation.shardingItemParameters()).build(), simpleJob.getClass().getCanonicalName());
            LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(simpleJobConfiguration).overwrite(true).build();
            SpringJobScheduler jobScheduler = new SpringJobScheduler(simpleJob, regCenter, liteJobConfiguration);
            jobScheduler.init();
        });
    }
}
