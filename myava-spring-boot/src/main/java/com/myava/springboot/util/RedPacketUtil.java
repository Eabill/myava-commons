package com.myava.springboot.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RedPacketUtil {

    private static Random random = new Random();

    /**
     * 校验红包合法性
     * @param money
     * @param count
     * @param min
     * @param max
     * @return
     */
    private static boolean isRight(int money, int count, int min, int max) {
        double avg = money / count;
        return (avg < min) ? false : !(avg > max);
    }

    /**
     * 随机分配一个红包
     * @param money
     * @param min
     * @param max
     * @param count
     * @return
     */
    public static int randomRedPacket(int money, int min, int max, int count) {
        // 若只有一个，直接返回
        if (count == 1) {
            return money;
        }
        // 若最大金额和最小金额相等，直接返回
        if (min == max) {
            return min;
        }
        // 分配红包正确情况下允许红包最小值
        int _min = money - (count - 1) * max;
        // 分配红包正确情况下允许红包最大值
        int _max = money - (count - 1) * min;
        // 随机产品红包最小值
        min = min > _min ? min : _min;
        max = max > money ? money : max;
        // 随机产生红包最大值
        max = max > _max ? _max : max;
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * 拆分红包
     * @param money
     * @param count
     * @param min
     * @param max
     * @return
     */
    public static List<Integer> splitRedPacket(int money, int count, int min, int max) {
        if (!isRight(money, count, min, max)) {
            throw new RuntimeException("Invalid red packet");
        }
        List<Integer> amountList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            int amount = randomRedPacket(money, min, max, count - i);
            money -= amount;
            amountList.add(amount);
        }
        return amountList;
    }

    public static void main(String[] args) {
        int totalAmount = 10000;
        int totalNumber = 10;
        int min = totalAmount / totalNumber / 10 * 5;
        int max = totalAmount / totalNumber / 10 * 15;
        System.out.println(min + "|" + max);
        List<Integer> amountList = splitRedPacket(totalAmount, totalNumber, min, max);
        System.out.println(amountList);
        int total = 0;
        for (Integer amount : amountList) {
            total += amount;
        }
        System.out.println("total: " + total);
    }
}
