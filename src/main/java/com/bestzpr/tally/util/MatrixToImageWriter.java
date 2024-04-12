package com.bestzpr.tally.util;

import com.google.zxing.common.BitMatrix;

import java.awt.image.BufferedImage;

/**
 * @className MatrixToImageWriter
 * @Desc TODO (这里用一句话描述这个类的作用)
 * @Author 张埔枘
 * @Date 2023/12/14 23:12
 * @Version 1.0
 */
public class MatrixToImageWriter {

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    private MatrixToImageWriter() {
    }

    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }
}