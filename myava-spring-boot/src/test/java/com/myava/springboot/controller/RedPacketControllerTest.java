package com.myava.springboot.controller;

import com.google.common.collect.Lists;
import com.myava.springboot.domain.RedPacketReceive;
import com.myava.springboot.domain.RemainRedPacket;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RedPacketControllerTest {

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private RedisTemplate redisTemplate;

    private DefaultRedisScript<RedPacketReceive> redPacketScript;
    private static final String KEY_PREFIX = "market:{redPacket}:";

    private ExecutorService executorService = new ThreadPoolExecutor(
            10,
            10,
            0L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10000));

    @Before
    public void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        // 初始化红包lua脚本
        redPacketScript = new DefaultRedisScript<>();
        redPacketScript.setResultType(RedPacketReceive.class);
        redPacketScript.setLocation(new ClassPathResource("script/red-packet.lua"));
    }

    @Test
    public void initRedPacket() {
    }

    @Test
    public void redPacket() throws Exception {
        String uri = "/redPacket/dis";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("redPacketId", "1")
                .param("userId", "1");
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                /*.andDo(MockMvcResultHandlers.print())*/
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void redPacketDis() {
        final int redPacketId = 1;
        long start = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(2000);
        for (int i = 1; i <= 2000; i++) {
            final int userId = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    RedPacketReceive result = (RedPacketReceive) redisTemplate.execute(redPacketScript, Lists.newArrayList(KEY_PREFIX), redPacketId, userId);
                    if (result == null) {
                        System.out.println("This user is consuemd.");
                    } else {
                        System.out.println("result: " + result);
                    }
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Execute finish, spend(ms): " + (System.currentTimeMillis() - start));
    }

    @Test
    public void remainRedPacket() {
        RemainRedPacket remainRedPacket = (RemainRedPacket) redisTemplate.opsForValue().get(KEY_PREFIX + 1);
        System.out.println("remainRedPacket: " + remainRedPacket);
    }

}