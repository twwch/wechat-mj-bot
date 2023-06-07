package com.chtw.midjourney.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.chtw.midjourney.service.WeChatService;
import com.chtw.midjourney.storage.mj.document.ImagineLogs;
import com.chtw.midjourney.storage.mj.repository.ImagineRepository;
import com.chtw.midjourney.untils.FileCompress;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;


@Service
@Slf4j
public class WeChatServiceImpl implements WeChatService {
    @Value("${wechat.token}")
    private String WX_TOKEN;

    @Autowired
    private ImagineRepository imagineRepository;

    @Value("${wechat.send_msg_url}")
    private String WECHAT_SEND_MSG_URL;


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


    private JSONObject string2Json(String str) {
        JSONObject json = JSONObject.parseObject(str);
        assert json != null;
        return json;
    }

    private JSONObject sendRequest(MultipartEntityBuilder builder) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {

            builder.setContentType(ContentType.MULTIPART_FORM_DATA);
            builder.setCharset(StandardCharsets.UTF_8);

            HttpPost httpPost = new HttpPost(WECHAT_SEND_MSG_URL);
            org.apache.http.HttpEntity build = builder.build();
            httpPost.setEntity(build);

            CloseableHttpResponse response = httpClient.execute(httpPost);
            org.apache.http.HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                return string2Json(result);
            }
        } catch (IOException e) {
            log.error("sendRequest error: {}", e.getMessage());
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                log.error("sendRequest error: {}", e.getMessage());
            }
        }
        return null;
    }

    private JSONObject trigger(ImagineLogs logs, String content) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        return getJsonObject(logs, content, builder);
    }

    private JSONObject replay(ImagineLogs logs, String content, File image) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("file", image, ContentType.MULTIPART_FORM_DATA, image.getName());
        return getJsonObject(logs, content, builder);
    }

    @Nullable
    private JSONObject getJsonObject(ImagineLogs logs, String content, MultipartEntityBuilder builder) {
        ContentType contentType = ContentType.create("text/plain", "UTF-8");
        builder.addTextBody("open_name", logs.getOpen_name(), contentType);
        if (logs.getRoom_name() != null) {
            builder.addTextBody("room_name", logs.getRoom_name(), contentType);
        }
        if (logs.getRoom_topic() != null) {
            builder.addTextBody("room_topic", logs.getRoom_topic(), contentType);
        }
        builder.addTextBody("content", content, contentType);
        return sendRequest(builder);
    }

    @Override
    public JSONObject sendTextMessage(JSONObject data) {
        if (data == null) {
            return null;
        }
        String content = data.getString("content").replace("**", "");
        String[] infos = content.split(" ");
        String msgId = infos[0];
        ImagineLogs logs = imagineRepository.findOne(msgId);
        // log 为空，直接返回
        if (logs == null) {
            log.info("log is null");
            return null;
        }

        content = content.replace(msgId, "").trim();

        String type = data.getString("type");
        if ("FIRST_TRIGGER".equals(type)) {
            return trigger(logs, content);
        }
        if ("GENERATE_END".equals(type)) {
            JSONObject discordData = data.getJSONObject("discord");
            String url = discordData.getString("url");
            String destinationFile = discordData.getString("fileName");
            File file = null;
            File outfile = null;
            try (InputStream in = new URL(url).openStream()) {
                Path path = Paths.get(destinationFile);
                Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
                file = new File(destinationFile);
                outfile = FileCompress.compress(file);
                return replay(logs, content, outfile);
            } catch (IOException e) {
                log.error("sendTextMessage error: {}", e.getMessage());
            } finally {
                if (file != null) {
                    boolean delete = file.delete();
                    System.out.println("delete file = " + delete);
                }
                if (outfile != null) {
                    boolean delete = outfile.delete();
                    System.out.println("delete outfile = " + delete);
                }
            }
        }
        return null;
    }

    @Override
    public JSONObject callback(JSONObject jsonObject) {
        return null;
    }
}
