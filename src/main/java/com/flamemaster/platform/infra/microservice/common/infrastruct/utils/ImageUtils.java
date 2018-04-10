package com.flamemaster.platform.infra.microservice.common.infrastruct.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImageUtils {


    public static void saveImage(String path, BufferedImage bufferedImage, String formatName) throws IOException {
        ImageIO.write(bufferedImage, formatName, new File(path));
    }

    public static BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(new File(path));
    }

    public static BufferedImage loadImage(URL url) throws IOException {
        return ImageIO.read(url);
    }

    //清理验证码图片
    public static BufferedImage cleanCaptcha(BufferedImage bufferedImage) {

        int h = bufferedImage.getHeight();
        int w = bufferedImage.getWidth();

        // 灰度化
        int[][] gray = new int[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int argb = bufferedImage.getRGB(x, y);
                // 图像加亮（调整亮度识别率非常高）
                int r = (int) (((argb >> 16) & 0xFF) * 1.1 + 30);
                int g = (int) (((argb >> 8) & 0xFF) * 1.1 + 30);
                int b = (int) ((argb & 0xFF) * 1.1 + 30);
                if (r >= 255) {
                    r = 255;
                }
                if (g >= 255) {
                    g = 255;
                }
                if (b >= 255) {
                    b = 255;
                }
                gray[x][y] = (int) Math
                        .pow((Math.pow(r, 2.2) * 0.2973 + Math.pow(g, 2.2)
                                * 0.6274 + Math.pow(b, 2.2) * 0.0753), 1 / 2.2);
            }
        }
        // 二值化
        int threshold = ostu(gray, w, h);
        BufferedImage binaryBufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
        BufferedImage masterBinaryBufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (gray[x][y] > threshold) {
                    gray[x][y] |= 0x00FFFF;
                } else {
                    gray[x][y] &= 0xFF0000;
                }
                binaryBufferedImage.setRGB(x, y, gray[x][y]);
                masterBinaryBufferedImage.setRGB(x, y, gray[x][y]);
            }
        }

        //去除小孤岛
        List<Set<String>> isLand = findIsland(binaryBufferedImage);
        for (Set<String> set : isLand) {
            if (set.size() < 5) {
                for (String xy : set) {
                    masterBinaryBufferedImage.setRGB(Integer.parseInt(xy.split("-")[0]), Integer.parseInt(xy.split("-")[1]), -1);
                }
            }
        }
        //去除干扰线条
        for (int y = 1; y < h - 1; y++) {
            for (int x = 1; x < w - 1; x++) {
                if (is8Direction(binaryBufferedImage, x, y)) {
                    masterBinaryBufferedImage.setRGB(x, y, -1);
                }
            }
        }
        return masterBinaryBufferedImage;
    }


    private static boolean is8Direction(BufferedImage bufferedImage, int x, int y) {
        int pointCount = 9;
        if (isBlack(bufferedImage.getRGB(x, y))) {
            if (isWhite(bufferedImage.getRGB(x + 1, y + 1))) pointCount--;
            if (isWhite(bufferedImage.getRGB(x - 1, y - 1))) pointCount--;
            if (isWhite(bufferedImage.getRGB(x, y + 1))) pointCount--;
            if (isWhite(bufferedImage.getRGB(x, y - 1))) pointCount--;
            if (isWhite(bufferedImage.getRGB(x + 1, y))) pointCount--;
            if (isWhite(bufferedImage.getRGB(x - 1, y))) pointCount--;
            if (isWhite(bufferedImage.getRGB(x - 1, y + 1))) pointCount--;
            if (isWhite(bufferedImage.getRGB(x + 1, y - 1))) pointCount--;
            return pointCount < 4;
        }
        return false;
    }

    private static List<Set<String>> findIsland(BufferedImage bufferedImage) {
        List<Set<String>> isLandList = new ArrayList<>();
        int h = bufferedImage.getHeight();
        int w = bufferedImage.getWidth();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (!isBlack(bufferedImage.getRGB(x, y))) {
                    continue;
                }
                boolean isFinded = false;
                for (int i = x - 1; i <= x + 1 && !isFinded; i++) {
                    for (int j = y + 1; j >= y - 1 && !isFinded; j--) {
                        if (i < 0 || j < 0 || i >= w || j >= h) {
                            continue;
                        }
                        if (isBlack(bufferedImage.getRGB(i, j))) {
                            for (Set<String> set : isLandList) {
                                if (set.contains(i + "-" + j)) {
                                    set.add(x + "-" + y);
                                    isFinded = true;
                                }
                            }
                        }
                    }
                }
                if (!isFinded) {
                    Set<String> set = new HashSet<>();
                    set.add(x + "-" + y);
                    isLandList.add(set);
                }
            }
        }
        return isLandList;
    }

    private static boolean isBlack(int colorInt) {
        Color color = new Color(colorInt);
        return color.getRed() + color.getGreen() + color.getBlue() <= 300;
    }

    private static boolean isWhite(int colorInt) {
        Color color = new Color(colorInt);
        return color.getRed() + color.getGreen() + color.getBlue() > 300;
    }


    private static int ostu(int[][] gray, int w, int h) {
        int[] histData = new int[w * h];
        // Calculate histogram
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int red = 0xFF & gray[x][y];
                histData[red]++;
            }
        }

        // Total number of pixels
        int total = w * h;

        float sum = 0;
        for (int t = 0; t < 256; t++)
            sum += t * histData[t];

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int t = 0; t < 256; t++) {
            wB += histData[t]; // Weight Background
            if (wB == 0)
                continue;

            wF = total - wB; // Weight Foreground
            if (wF == 0)
                break;

            sumB += (float) (t * histData[t]);

            float mB = sumB / wB; // Mean Background
            float mF = (sum - sumB) / wF; // Mean Foreground

            // Calculate Between Class Variance
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            // Check if new maximum found
            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = t;
            }
        }

        return threshold;
    }
}
