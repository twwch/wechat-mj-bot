package com.chtw.midjourney.untils;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Slf4j
public class FileCompress {

    public static File compress(File file) throws IOException {
        File pngFile = null;
        String fileName = file.getName();
        BufferedImage sourceImage = ImageIO.read(file);
        if (file.getName().endsWith(".webp")) {
            BufferedImage webpImage = ImageIO.read(file);
            fileName = fileName.replace(".webp", ".png");
            pngFile = new File(fileName);
            // 写入PNG图片
            ImageIO.write(webpImage, "PNG", pngFile);
            sourceImage = ImageIO.read(pngFile);
        }
        int newWidth = sourceImage.getWidth() / 4;
        int newHeight = sourceImage.getHeight() / 4;

        // 创建一个新的 BufferedImage 对象用于保存改变分辨率后的图片
        BufferedImage outputImage = new BufferedImage(newWidth, newHeight, sourceImage.getType());

        // 使用 Graphics2D 对象重新绘制图片
        Graphics2D graphics2D = outputImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(sourceImage, 0, 0, newWidth, newHeight, null);
        graphics2D.dispose();

        // 保存到新的文件
        File output = new File("./compressed-" + fileName);
        ImageIO.write(outputImage, "png", output);
        if (pngFile != null) {
            boolean delete = pngFile.delete();
            log.info("删除webp文件: {}", delete);
        }
        return output;
    }
}
