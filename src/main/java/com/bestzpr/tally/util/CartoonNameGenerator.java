package com.bestzpr.tally.util;

import java.util.Random;

/**
 * @className CartoonNameGenerator
 * @Desc 随机名称生成器
 * @Author 张埔枘
 * @Date 2023/12/21 11:26
 * @Version 1.0
 */
public class CartoonNameGenerator {
    private static String[] adjectives = {
            "萌萌的", "搞怪", "呆萌", "独特", "活泼", "无邪", "机灵", "快乐", "调皮", "顽皮"
    };

    private static String[] nouns = {
            "小喵", "小汪", "小兔", "小鸟", "小熊", "小鱼", "小鸭", "小猴", "小猪"
    };

    public static String generateCartoonName() {
        Random random = new Random();
        String adjective = adjectives[random.nextInt(adjectives.length)];
        String noun = nouns[random.nextInt(nouns.length)];
        return adjective + noun;
    }

    public static void main(String[] args) {
        String cartoonName = generateCartoonName();
        System.out.println("随机生成的卡通名称是：" + cartoonName);
    }
}