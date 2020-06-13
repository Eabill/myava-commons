package com.myava.springboot.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 红包算法
 *
 * @author biao
 */
public class RedPacketAlgorithm {

    /**
     * 二倍均值法：剩余红包金额为M，剩余人数为N，那么有如下公式：每次抢到的金额 = 随机区间 （0， M / N X 2）
     * 每次随机金额的平均值相等，不会因为抢红包的先后顺序而造成不公平
     * 发红包算法，金额参数以分为单位
     * @param totalAmount
     * @param totalNumber
     * @return
     */
    public static List<Integer> divideRedPackage(Integer totalAmount, Integer totalNumber) {

        List<Integer> amountList = new ArrayList<>();
        Integer remainAmt = totalAmount;
        Integer remainNum = totalNumber;
        Random random = new Random();
        for (int i = 0; i < totalNumber - 1; i++) {
            // 随机范围：[1，剩余人均金额的两倍)，左闭右开
            int amount = random.nextInt(remainAmt / remainNum * 2 - 1) + 1;
            remainAmt -= amount;
            remainNum--;
            amountList.add(amount);
        }
        amountList.add(remainAmt);

        return amountList;
    }

    /**
     * 线段切割法：当N个人一起抢总金额为M的红包时，我们需要做N-1次随机运算，以此确定N-1个切割点
     * 随机自由度高，且不会因为抢红包的先后顺序而造成不公平
     * @param totalAmount
     * @param totalNumber
     */
    public static List<Integer> line_cut(int totalAmount, int totalNumber) {
        List<Integer> splitList = new ArrayList<>();
        List<Integer> amountList = new ArrayList<>();
        Random random = new Random();
        int money = totalAmount - 1;
        while (splitList.size() < totalNumber - 1) {
            int split = random.nextInt(money) + 1;
            if (!splitList.contains(split)) {
                splitList.add(split);
            }
        }
        Collections.sort(splitList);
        for (int i = 0; i < splitList.size(); i++) {
            if (i == 0) {
                amountList.add(splitList.get(i));
                continue;
            }
            amountList.add(splitList.get(i) - splitList.get(i - 1));
            if (i == splitList.size() - 1) {
                amountList.add(totalAmount - splitList.get(i));
            }
        }
        return amountList;
    }

    public static void main(String[] args) {
        System.out.println(line_cut(10000, 10));
        System.out.println(divideRedPackage(10000, 10));
    }
}
