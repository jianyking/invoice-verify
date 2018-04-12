package com.flamemaster.platform.infra.microservice.wrapper;

import com.flamemaster.platform.infra.microservice.common.infrastruct.utils.ImageUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class CaptchaWrapper {

    private BufferedImage bufferedImage;

    public static final String SAVE_FORMAT_BMP = "bmp";

    public static final String SAVE_FORMAT_PNG = "png";

    public static final String SAVE_FORMAT_JPG = "jpg";

    public CaptchaWrapper(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public CaptchaWrapper(String captchaPath) throws IOException {
        this.bufferedImage = ImageUtils.loadImage(captchaPath);
    }

    public CaptchaWrapper(URL url) throws IOException {
        this.bufferedImage = ImageUtils.loadImage(url);
    }

    public CaptchaWrapper(File file) throws IOException {
        this.bufferedImage = ImageUtils.loadImage(file);
    }

    public BufferedImage bi() {
        return this.bufferedImage;
    }

    public CaptchaWrapper save(String savePath) throws IOException {
        ImageUtils.saveImage(savePath, this.bufferedImage, SAVE_FORMAT_BMP);
        return this;
    }

    public CaptchaWrapper save(String savePath, String format) throws IOException {
        ImageUtils.saveImage(savePath, this.bufferedImage, format);
        return this;
    }

    //二值化
    public CaptchaWrapper binarization(int mode) {
        this.bufferedImage = ImageUtils.binarization(this.bufferedImage, mode);
        return this;
    }

    //切边，csx表示切除左右连边的宽度，csy示切除上下连边的厚度
    public CaptchaWrapper cleanSide(int csx, int csy) {
        this.bufferedImage = ImageUtils.cleanSide(this.bufferedImage, csx, csy);
        return this;
    }

    //清除孤岛，threshold是岛屿的最小保留值（像素数）
    public CaptchaWrapper cleanIsland(int threshold) {
        this.bufferedImage = ImageUtils.cleanIsland(this.bufferedImage, threshold);
        return this;
    }

    //八邻域除噪法，threshold是包括中心点在内的黑色像素的最小保留值， hcp:最长水平连续像素保护，防止单像素字母边被清除，vcp: 竖直保护（设置为小于0则为关闭保护）
    public CaptchaWrapper cleanByEightBitDomain(int threshold, int hcp, int vcp) {
        this.bufferedImage = ImageUtils.cleanByEightBitDomain(this.bufferedImage, threshold, hcp, vcp);
        return this;
    }

    //对比度调节
    public CaptchaWrapper adjustContrast(double contrast) {
        this.bufferedImage = ImageUtils.adjustContrast(this.bufferedImage, contrast);
        return this;
    }
}
