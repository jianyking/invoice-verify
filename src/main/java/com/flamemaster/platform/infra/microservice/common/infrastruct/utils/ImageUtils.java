package com.flamemaster.platform.infra.microservice.common.infrastruct.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class ImageUtils {


    public static void saveImage(String path, BufferedImage bufferedImage, String formatName) throws IOException {
        ImageIO.write(bufferedImage, formatName, new File(path));
    }

    public static BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(new File(path));
    }

    public static BufferedImage loadImage(File file) throws IOException {
        return ImageIO.read(file);
    }

    public static BufferedImage loadImage(URL url) throws IOException {
        return ImageIO.read(url);
    }

    public static BufferedImage binarization(BufferedImage bufferedImage, int mode) {

        int h = bufferedImage.getHeight();
        int w = bufferedImage.getWidth();

        //灰度化
        int[][] gray = grayscale(bufferedImage);
        // 二值化
        int histoGram[] = calcHistogram(gray, w, h);
        int threshold = 150;
        switch (mode) {
            case 0:
                threshold = getOSTUThreshold(histoGram, w * h);
                break;
            case 1:
                threshold = getMeanThreshold(histoGram);
                break;
            case 2:
                threshold = getHuangFuzzyThreshold(histoGram);
                break;
            default:
                threshold = mode;
        }
        BufferedImage binaryBufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (gray[x][y] > threshold) {
                    gray[x][y] |= 0x00FFFF;
                } else {
                    gray[x][y] &= 0xFF0000;
                }
                binaryBufferedImage.setRGB(x, y, gray[x][y]);
            }
        }
        return binaryBufferedImage;
    }

    public static BufferedImage cleanIsland(BufferedImage bufferedImage, int threshold) {
        List<Set<PixPoint>> isLand = findIsland(bufferedImage);
        for (Set<PixPoint> set : isLand) {
            if (set.size() < threshold) {
                for (PixPoint xy : set) {
                    bufferedImage.setRGB(xy.x, xy.y, -1);
                }
            }
        }
        return bufferedImage;
    }

    public static BufferedImage cleanByEightBitDomain(BufferedImage bi, int threshold, int hcp, int vcp) {
        BufferedImage newBi = deepCopy(bi);
        for (int y = 1; y < bi.getHeight() - 1; y++) {
            for (int x = 1; x < bi.getWidth() - 1; x++) {
                if (get8DirectionBlackPixNumber(bi, x, y) < threshold) {
                    if ((hcp <= 0 || horizontallyContinuous(bi, x, y) < hcp) &&
                            vcp <= 0 || verticalLongestContinuous(bi, x, y) < vcp) {
                        newBi.setRGB(x, y, -1);
                    }
                }
            }
        }
        return newBi;
    }

    public static BufferedImage adjustContrast(BufferedImage bi, double contrast) {
        for (int i = 0; i < bi.getHeight(); i++) {
            for (int j = 0; j < bi.getWidth(); j++) {
                Color color = new Color(bi.getRGB(j, i));
                int red = clamp((int) (color.getRed() * contrast));
                int green = clamp((int) (color.getGreen() * contrast));
                int blue = clamp((int) (color.getBlue() * contrast));
                bi.setRGB(j, i, new Color(red, green, blue).getRGB());
            }
        }
        return bi;
    }

    private static int[][] grayscale(BufferedImage bi) {

        int h = bi.getHeight();
        int w = bi.getWidth();

        // 灰度化
        int[][] gray = new int[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int argb = bi.getRGB(x, y);
                // 图像加亮（调整亮度识别率非常高）
                int r = (int) (((argb >> 16) & 0xFF) * 1.1 + 30);
                int g = (int) (((argb >> 8) & 0xFF) * 1.1 + 30);
                int b = (int) ((argb & 0xFF) * 1.1 + 30);
                r = (r >= 255 ? 255 : r);
                g = (g >= 255 ? 255 : g);
                b = (b >= 255 ? 255 : b);
                gray[x][y] = (int) Math
                        .pow((Math.pow(r, 2.2) * 0.2973 + Math.pow(g, 2.2)
                                * 0.6274 + Math.pow(b, 2.2) * 0.0753), 1 / 2.2);
            }
        }
        return gray;
    }

    private static int horizontallyContinuous(BufferedImage bi, int x, int y) {
        int continuousNum = 0;
        for (int i = x; i >= 0; i--) {
            if (!isBlack(bi.getRGB(i, y))) {
                break;
            }
            continuousNum++;
        }
        for (int i = x + 1; i < bi.getWidth(); i++) {
            if (!isBlack(bi.getRGB(i, y))) {
                break;
            }
            continuousNum++;
        }
        return continuousNum;
    }

    private static int verticalLongestContinuous(BufferedImage bi, int x, int y) {
        int continuousNum = 0;
        for (int i = y; i >= 0; i--) {
            if (!isBlack(bi.getRGB(x, i))) {
                break;
            }
            continuousNum++;
        }
        for (int i = y + 1; i < bi.getHeight(); i++) {
            if (!isBlack(bi.getRGB(x, i))) {
                break;
            }
            continuousNum++;
        }
        return continuousNum;
    }

    public static BufferedImage cleanSide(BufferedImage bufferedImage, int xThreshold, int yThreshold) {
        if (bufferedImage.getWidth() <= xThreshold || bufferedImage.getHeight() <= yThreshold) {
            return bufferedImage;
        }
        for (int y = 0; y < yThreshold; y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                bufferedImage.setRGB(x, y, -1);
                bufferedImage.setRGB(x, bufferedImage.getHeight() - y - 1, -1);
            }
        }

        for (int x = 0; x < xThreshold; x++) {
            for (int y = yThreshold; y < bufferedImage.getHeight() - yThreshold; y++) {
                bufferedImage.setRGB(x, y, -1);
                bufferedImage.setRGB(bufferedImage.getWidth() - x - 1, y, -1);
            }
        }
        return bufferedImage;
    }

    private static int get8DirectionBlackPixNumber(BufferedImage bufferedImage, int x, int y) {
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
        }
        return pointCount;
    }

    private static List<Set<PixPoint>> findIsland(BufferedImage bufferedImage) {
        List<Set<PixPoint>> isLands = new ArrayList<>();
        Map<String, Boolean> accessedMap = new HashMap<>();
        int w = bufferedImage.getWidth();
        int h = bufferedImage.getHeight();

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (!isBlack(bufferedImage.getRGB(i, j)) || accessedMap.containsKey(i + "." + j)) {
                    continue;
                }
                LinkedList<PixPoint> queue = new LinkedList<>();
                Set<PixPoint> pixPoints = new HashSet<>();
                PixPoint startPP = new PixPoint(i, j, 0);
                queue.offer(startPP);
                pixPoints.add(startPP);
                accessedMap.put(i + "." + j, true);
                while (queue.size() != 0) {
                    PixPoint tmp = queue.poll();
                    int startX = (tmp.x - 1 < 0) ? 0 : tmp.x - 1;
                    int startY = (tmp.y - 1 < 0) ? 0 : tmp.y - 1;
                    int endX = (tmp.x + 1 > w - 1) ? w - 1 : tmp.x + 1;
                    int endY = (tmp.y + 1 > h - 1) ? h - 1 : tmp.y + 1;
                    for (int tx = startX; tx <= endX; tx++) {
                        for (int ty = startY; ty <= endY; ty++) {
                            if (tx == tmp.x && ty == tmp.y) {
                                continue;
                            }
                            if (isBlack(bufferedImage.getRGB(tx, ty)) && !accessedMap.containsKey(tx + "." + ty)) {
                                PixPoint eightPP = new PixPoint(tx, ty, 0);
                                queue.offer(eightPP);
                                pixPoints.add(eightPP);
                                accessedMap.put(tx + "." + ty, true);
                            }
                        }
                    }
                }
                isLands.add(pixPoints);
            }
        }
        return isLands;
    }

    private static boolean isBlack(int colorInt) {
        Color color = new Color(colorInt);
        return color.getRed() + color.getGreen() + color.getBlue() <= 300;
    }

    private static boolean isWhite(int colorInt) {
        Color color = new Color(colorInt);
        return color.getRed() + color.getGreen() + color.getBlue() > 300;
    }

    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    private static int clamp(int value) {
        return value > 255 ? 255 : (value < 0 ? 0 : value);
    }

    public static int getMeanThreshold(int[] histoGram) {
        int sum = 0, amount = 0;
        for (int Y = 0; Y < 256; Y++) {
            amount += histoGram[Y];
            sum += Y * histoGram[Y];
        }
        return sum / amount;
    }

    private static int getOSTUThreshold(int[] histoGram, int total) {

        float sum = 0;
        for (int t = 0; t < 256; t++)
            sum += t * histoGram[t];

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int t = 0; t < 256; t++) {
            wB += histoGram[t]; // Weight Background
            if (wB == 0)
                continue;

            wF = total - wB; // Weight Foreground
            if (wF == 0)
                break;

            sumB += (float) (t * histoGram[t]);

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

    public static int getHuangFuzzyThreshold(int[] histGram) {
        int X, Y;
        int First, Last;
        int Threshold = -1;
        double BestEntropy = Double.MAX_VALUE, Entropy;
        //   找到第一个和最后一个非0的色阶值
        for (First = 0; First < histGram.length && histGram[First] == 0; First++) ;
        for (Last = histGram.length - 1; Last > First && histGram[Last] == 0; Last--) ;
        if (First == Last) return First;                // 图像中只有一个颜色
        if (First + 1 == Last) return First;            // 图像中只有二个颜色

        // 计算累计直方图以及对应的带权重的累计直方图
        int[] S = new int[Last + 1];
        int[] W = new int[Last + 1];            // 对于特大图，此数组的保存数据可能会超出int的表示范围，可以考虑用long类型来代替
        S[0] = histGram[0];
        for (Y = First > 1 ? First : 1; Y <= Last; Y++) {
            S[Y] = S[Y - 1] + histGram[Y];
            W[Y] = W[Y - 1] + Y * histGram[Y];
        }

        // 建立公式（4）及（6）所用的查找表
        double[] Smu = new double[Last + 1 - First];
        for (Y = 1; Y < Smu.length; Y++) {
            double mu = 1 / (1 + (double) Y / (Last - First));               // 公式（4）
            Smu[Y] = -mu * Math.log(mu) - (1 - mu) * Math.log(1 - mu);      // 公式（6）
        }

        // 迭代计算最佳阈值
        for (Y = First; Y <= Last; Y++) {
            Entropy = 0;
            int mu = (int) Math.round((double) W[Y] / S[Y]);             // 公式17
            for (X = First; X <= Y; X++)
                Entropy += Smu[Math.abs(X - mu)] * histGram[X];
            mu = (int) Math.round((double) (W[Last] - W[Y]) / (S[Last] - S[Y]));  // 公式18
            for (X = Y + 1; X <= Last; X++)
                Entropy += Smu[Math.abs(X - mu)] * histGram[X];       // 公式8
            if (BestEntropy > Entropy) {
                BestEntropy = Entropy;      // 取最小熵处为最佳阈值
                Threshold = Y;
            }
        }
        return Threshold;
    }

    private static int[] calcHistogram(int[][] gray, int w, int h) {
        int[] histoGram = new int[256];
        // Calculate histogram
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int red = 0xFF & gray[x][y];
                histoGram[red]++;
            }
        }
        return histoGram;
    }

    static class PixPoint {
        int x;
        int y;
        int rgb;

        PixPoint(int x, int y, int rgb) {
            this.x = x;
            this.y = y;
            this.rgb = rgb;
        }
    }
}
