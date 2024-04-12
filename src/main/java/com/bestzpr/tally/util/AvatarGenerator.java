package com.bestzpr.tally.util;


import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @className AvatarGenerator
 * @Desc 头像随机生成器
 * @Author 张埔枘
 * @Date 2023/12/21 11:30
 * @Version 1.0
 */
public class AvatarGenerator {

    private static List<String> avatars = Arrays.asList(
            "WX20231221-191957%402x.png",
            "WX20231221-192019%402x.png",
            "WX20231221-192033%402x.png",
            "WX20231221-192045%402x.png",
            "WX20231221-192056%402x.png",
            "WX20231221-192106%402x.png",
            "WX20231221-192124%402x.png",
            "WX20231221-192135%402x.png",
            "WX20231221-192148%402x.png",
            "WX20231221-192222%402x.png",
            "WX20231221-192234%402x.png",
            "WX20231221-192247%402x.png",
            "WX20231221-192253%402x.png",
            "WX20231221-192300%402x.png",
            "WX20231221-192307%402x.png",
            "WX20231221-192317%402x.png",
            "WX20231221-192325%402x.png",
            "WX20231221-192331%402x.png",
            "WX20231221-192341%402x.png",
            "WX20231221-192347%402x.png",
            "WX20231221-192358%402x.png",
            "WX20231221-192431%402x.png",
            "WX20231221-192438%402x.png",
            "WX20231221-192446%402x.png",
            "WX20231221-192453%402x.png",
            "WX20231221-192502%402x.png",
            "WX20231221-192508%402x.png",
            "WX20231221-192518%402x.png",
            "WX20231221-192531%402x.png",
            "WX20231221-192540%402x.png",
            "WX20231221-192550%402x.png",
            "WX20231221-192558%402x.png",
            "WX20231221-192604%402x.png",
            "WX20231221-192622%402x.png",
            "WX20231221-192632%402x.png"
    );

    public static String getAvatarUrl() {
        Random rand = new Random();
        int randomNum = rand.nextInt(avatars.size() - 0) + 0;
        return "https://doraemon-website.oss-cn-shanghai.aliyuncs.com/ramdomAvatar/" + avatars.get(randomNum);
    }

}
