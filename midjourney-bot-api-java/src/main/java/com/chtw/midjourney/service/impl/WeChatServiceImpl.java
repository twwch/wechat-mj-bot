package com.chtw.midjourney.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.chtw.midjourney.service.WeChatService;
import com.chtw.midjourney.untils.WeChatSDK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Service
public class WeChatServiceImpl implements WeChatService {
    @Value("${wechat.token}")
    private String WX_TOKEN;

    @Autowired
    private WeChatSDK weChatSDK;

    private static final String TEST_OPEN_USER_ID = "oyQ4N6yQAfsBxghYbZYYSoQc7lDw";

    @Override
    public boolean checkSignature(String signature, String timestamp, String nonce) throws NoSuchAlgorithmException {
        String[] arr = new String[]{WX_TOKEN, timestamp, nonce};
        Arrays.sort(arr);  // 字典排序

        StringBuilder sb = new StringBuilder();
        for (String str : arr) {
            sb.append(str);
        }
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(sb.toString().getBytes());
        byte[] output = md.digest();

        StringBuilder sb1 = new StringBuilder();
        for (byte b : output) {
            sb1.append(String.format("%02x", b));
        }
        return sb1.toString().equals(signature);
    }

    @Override
    public JSONObject sendTextMessage(JSONObject data) {
        if (data == null) {
            return null;
        }
        String content = data.getString("content");
        String[] infos = content.split(" ");
        String userId = infos[0];
        if (userId == null) {
            userId = TEST_OPEN_USER_ID;
        }
        String type = data.getString("type");
        if ("FIRST_TRIGGER".equals(type)) {
            return weChatSDK.sendMessage(userId, data.getString("content"));
        }
        if ("GENERATE_END".equals(type)) {
            JSONObject discordData = data.getJSONObject("discord");
            String url = discordData.getString("url");
            String destinationFile = discordData.getString("fileName");
            try (InputStream in = new URL(url).openStream()) {
                Files.copy(in, Paths.get(destinationFile), StandardCopyOption.REPLACE_EXISTING);
                String mediaId = weChatSDK.uploadMedia("image", destinationFile);
                if (mediaId == null) {
                    System.out.println("mediaId is null");
                    return null;
                }
                return weChatSDK.sendImageMessage(userId, mediaId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public JSONObject callback(JSONObject jsonObject) {
        return null;
    }
}
