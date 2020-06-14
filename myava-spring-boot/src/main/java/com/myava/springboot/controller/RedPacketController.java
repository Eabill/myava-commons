package com.myava.springboot.controller;

import com.google.common.collect.Lists;
import com.myava.base.util.DateUtil;
import com.myava.redis.RedisLock;
import com.myava.springboot.domain.RedPacketInfo;
import com.myava.springboot.domain.RedPacketReceive;
import com.myava.springboot.domain.RemainRedPacket;
import com.myava.springboot.util.RedPacketUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 红包控制器，基于redis lua或redis锁实现
 *
 * @author biao
 */
@Controller
public class RedPacketController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private DefaultRedisScript<String> redPacketScript;
    private static final String KEY_PREFIX = "market:{redPacket}:";

    @PostConstruct
    public void init() {
        redPacketScript = new DefaultRedisScript<>();
        redPacketScript.setResultType(String.class);
        redPacketScript.setLocation(new ClassPathResource("script/red-packet.lua"));
    }

    @RequestMapping(value = "/redPacket/init", method = RequestMethod.GET)
    @ResponseBody
    public String initRedPacket(String totalAmt, int totalNum) {
        Date now = new Date();
        RedPacketInfo info = RedPacketInfo.builder()
                .id(1L)
                .redPacketName("双11红包")
                .redPacketNo("20191111001")
                .totalAmount(new BigDecimal(totalAmt))
                .totalNumber(totalNum)
                .startDate(now)
                .endDate(DateUtil.addDays(now, 1))
                .build();
        RemainRedPacket remainRedPacket = new RemainRedPacket();
        remainRedPacket.setRemainNum(info.getTotalNumber());
        remainRedPacket.setRemainAmt(info.getTotalAmount().multiply(new BigDecimal("100")).intValue());

        int avg = remainRedPacket.getRemainAmt() / remainRedPacket.getRemainNum();
        int min = avg / 10 * 5;
        int max = avg / 10 * 15;
        List<Integer> amountList = RedPacketUtil.splitRedPacket(remainRedPacket.getRemainAmt(), remainRedPacket.getRemainNum(), min, max);
        List<RedPacketReceive> list = Lists.newArrayList();
        for (Integer amount : amountList) {
            RedPacketReceive receive = RedPacketReceive.builder()
                    .redPacketId(info.getId())
                    .amount(amount)
                    .expireDate(info.getEndDate())
                    .build();
            list.add(receive);
        }
        String key = KEY_PREFIX + info.getId();
        String listKey = key + ":list";
        String recKey = key + ":received";
        redisTemplate.delete(key);
        redisTemplate.delete(listKey);
        redisTemplate.delete(recKey);

        redisTemplate.opsForValue().set(key, remainRedPacket);
        redisTemplate.expireAt(key, info.getEndDate());
        redisTemplate.opsForSet().add(recKey, 0);
        redisTemplate.expireAt(recKey, info.getEndDate());
        redisTemplate.opsForList().rightPushAll(listKey, new ArrayList<>(list));
        redisTemplate.expireAt(listKey, info.getEndDate());

        System.out.println("size: " + redisTemplate.opsForList().size(listKey));
        return "init redPacket success";
    }

    @RequestMapping(value = "/redPacket/dis", method = RequestMethod.GET)
    @ResponseBody
    public void redPacket(long redPacketId, long userId) {

        String result = redisTemplate.execute(redPacketScript,
                new StringRedisSerializer(), new StringRedisSerializer(),
                Lists.newArrayList(KEY_PREFIX), String.valueOf(redPacketId), String.valueOf(userId));
        if (result == "RECEIVED") {
            System.out.println("This user has received.");
            return;
        }
        if (result == "NOT_HAVE_RED_PACKET") {
            System.out.println("Not have red packet.");
            return;
        }
        System.out.println("result: " + result);
        RemainRedPacket remainRedPacket = (RemainRedPacket) redisTemplate.opsForValue().get(KEY_PREFIX + redPacketId);
        System.out.println("remainRedPacket: " + remainRedPacket);
    }

    @RequestMapping(value = "/redPacket/dis/lock", method = RequestMethod.GET)
    @ResponseBody
    public void redPacketLock(long redPacketId, long userId) {
        RedisLock redisLock = new RedisLock("market:redPacket:" + redPacketId);
        if (!redisLock.lock()) {
            System.out.println("Get lock error.");
        }
        try {
            String key = KEY_PREFIX + redPacketId;
            String recKey = key + ":received";
            String listKey = key + ":list";
            Boolean member = redisTemplate.opsForSet().isMember(recKey, userId);
            if (member) {
                System.out.println("This user has received.");
                return;
            }
            RedPacketReceive redPacketReceive = (RedPacketReceive) redisTemplate.opsForList().rightPop(listKey);
            if (redPacketReceive == null) {
                System.out.println("Not have red packet.");
                return;
            }
            redPacketReceive.setUserId(userId);
            System.out.println("result: " + redPacketReceive);
            redisTemplate.opsForSet().add(recKey, userId);
            RemainRedPacket remainRedPacket = (RemainRedPacket) redisTemplate.opsForValue().get(key);
            remainRedPacket.setRemainNum(remainRedPacket.getRemainNum() - 1);
            remainRedPacket.setRemainAmt(remainRedPacket.getRemainAmt() - redPacketReceive.getAmount());
            redisTemplate.opsForValue().set(key, remainRedPacket);
            System.out.println("remainRedPacket: " + remainRedPacket);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            redisLock.unlock();
        }
    }

}