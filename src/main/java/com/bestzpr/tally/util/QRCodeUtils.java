package com.bestzpr.tally.util;

/**
 * @className QRCodeUtils
 * @Desc TODO (这里用一句话描述这个类的作用)
 * @Author 张埔枘
 * @Date 2023/12/8 00:35
 * @Version 1.0
 */

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * QRCode 生成工具类
 */
public class QRCodeUtils {

    /**
     * 生成二维码
     *
     * @param content 二维码内容
     * @param width  二维码宽度
     * @param height 二维码高度
     * @param logo   二维码中间LOGO
     * @return 二维码图片
     */
    public static BufferedImage createQRCode(String content, int width, int height, File logo) throws WriterException, IOException {
        // 设置二维码参数
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 0);

        // 创建二维码
        BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

        // 生成二维码图片
        BufferedImage image = new

                BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        // 添加LOGO
        if (logo != null) {
            // 获取LOGO的宽度和高度
            int logoWidth = ImageIO.read(logo).getWidth();
            int logoHeight = ImageIO.read(logo).getHeight();

            // 计算LOGO在二维码中的位置
            int x = (width - logoWidth) / 2;
            int y = (height - logoHeight) / 2;

            // 将LOGO添加到二维码中
            image.getGraphics().drawImage(ImageIO.read(logo), x, y, logoWidth, logoHeight, null);
        }

        return image;
    }
}