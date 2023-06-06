package com.chtw.midjourney;

import com.alibaba.fastjson.JSONObject;
import com.chtw.midjourney.untils.WeChatSDK;
import com.chtw.midjourney.untils.model.MenuMsgModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
