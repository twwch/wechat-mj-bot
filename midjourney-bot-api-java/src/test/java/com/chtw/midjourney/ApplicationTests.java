package com.chtw.midjourney;

import com.alibaba.fastjson.JSONObject;
import com.chtw.midjourney.untils.WeChatSDK;
import com.chtw.midjourney.untils.model.MenuMsgModel;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ApplicationTests {

    @Autowired
    private WeChatSDK sdk;

    private static final String OPEN_USER_ID = "oyQ4N6yQAfsBxghYbZYYSoQc7lDw";

    // Test wechatSDk
    @Test
    public void testWechatSDk() {
        System.out.println(ContentType.DEFAULT_TEXT);
        System.out.println(ContentType.create("text/plain", "UTF-8"));
    }

    @Test
    public void testSendMessage() {
        JSONObject accessToken = sdk.sendMessage(OPEN_USER_ID, "Hello World!");
        System.out.println(accessToken);
    }

    @Test
    public void testUploadMedia() {
        String id = sdk.uploadMedia("image", "/Users/codes/javaCode/midjourney-bot-api/midjourney-bot-api-java/src/test/java/com/chtw/midjourney/TEST.jpg");
        System.out.println(id);
    }

    @Test
    public void uploadMedia() {
        String id = sdk.uploadMedia("image", "/Users/codes/javaCode/midjourney-bot-api/midjourney-bot-api-java/src/test/java/com/chtw/midjourney/TEST.jpg");
        System.out.println(id);
    }

    @Test
    public void sendImageMessage() {
        String media_id = "9Ys--Y3BobdZ7uGcPmyXm1GiRl-sn0FtqHJFlHpVe_bEuudM2epjk_FuSVUns7Lr";
        JSONObject res = sdk.sendImageMessage(OPEN_USER_ID, media_id);
        System.out.println(res);
    }

    @Test
    public void sendMenuMessage() {
        List<MenuMsgModel> options = new ArrayList<>();
        options.add(new MenuMsgModel("测试1", "http://www.baidu.com"));
        options.add(new MenuMsgModel("测试2", "http://www.baidu.com"));
        JSONObject res = sdk.SendMenuMessage(OPEN_USER_ID,"测试", options);
        System.out.println(res);
    }

    @Test
    public void compressImage() throws IOException {

        File input = new File("/Users/codes/javaCode/midjourney-bot-api/midjourney-bot-api-java/src/test/java/com/chtw/midjourney/TEST.png");
        BufferedImage sourceImage = ImageIO.read(input);

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
        File output = new File("/Users/codes/javaCode/midjourney-bot-api/midjourney-bot-api-java/src/test/java/com/chtw/midjourney/TEST-2.png");
        ImageIO.write(outputImage, "png", output);
    }
}
