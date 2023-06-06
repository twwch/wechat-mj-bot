package com.chtw.midjourney.untils;

import com.alibaba.fastjson.JSONObject;
import com.chtw.midjourney.untils.model.MenuMsgModel;
import io.netty.util.internal.StringUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;

@Component
public class WeChatSDK {

    @Value("${wechat.app_id}")
    private String appId;

    @Value("${wechat.app_secret}")
    private String appSecret;

    private final WebClient webClient;

    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    private final String baseUrl = "https://api.weixin.qq.com";

    // 构造方法
    public WeChatSDK() {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    private JSONObject string2Json(String str) {
        JSONObject json = JSONObject.parseObject(str);
        assert json != null;
        return json;
    }

    // 获取 access_token
    private String getAccessToken() {
        // todo save access_token to redis
        String res = webClient.get().uri("/cgi-bin/token?grant_type=client_credential&appid={appid}&secret={secret}", appId, appSecret)
                .retrieve().bodyToMono(String.class).block();
        return string2Json(res).getString("access_token");
    }

    // send message
    public JSONObject sendMessage(String openId, String content) {
        if (StringUtil.isNullOrEmpty(openId) || StringUtil.isNullOrEmpty(content)) {
            throw new IllegalArgumentException("参数错误");
        }
        JSONObject json = new JSONObject();
        json.put("touser", openId);
        json.put("msgtype", "text");
        JSONObject text = new JSONObject();
        text.put("content", content);
        json.put("text", text);
        String url = String.format("/cgi-bin/message/custom/send?access_token=%s", getAccessToken());
        String res = webClient.post().uri(url)
                .bodyValue(json.toJSONString()).retrieve().bodyToMono(String.class).block();
        return string2Json(res);
    }

    // send image message
    public JSONObject sendImageMessage(String openId, String mediaId) {
        if (StringUtil.isNullOrEmpty(openId) || StringUtil.isNullOrEmpty(mediaId)) {
            throw new IllegalArgumentException("参数错误");
        }
        JSONObject json = new JSONObject();
        json.put("touser", openId);
        json.put("msgtype", "image");
        JSONObject image = new JSONObject();
        image.put("media_id", mediaId);
        json.put("image", image);
        String res = webClient.post().uri("/cgi-bin/message/custom/send?access_token={access_token}", getAccessToken())
                .bodyValue(json.toJSONString()).retrieve().bodyToMono(String.class).block();
        return string2Json(res);
    }

    // send menu message
    // 测试号有问题，无法进行测试
    public JSONObject SendMenuMessage(String openId, String head_content, List<MenuMsgModel> menu_list) {
        if (StringUtil.isNullOrEmpty(openId) || StringUtil.isNullOrEmpty(head_content) || menu_list.size() == 0) {
            throw new IllegalArgumentException("参数错误");
        }
        JSONObject json = new JSONObject();
        json.put("touser", openId);
        json.put("msgtype", "msgmenu");
        JSONObject msgmenu = new JSONObject();
        msgmenu.put("head_content", head_content);
        msgmenu.put("list", menu_list);
        json.put("msgmenu", msgmenu);
        String res = webClient.post().uri("/cgi-bin/message/custom/send?access_token={access_token}", getAccessToken())
                .bodyValue(json.toJSONString()).retrieve().bodyToMono(String.class).block();
        return string2Json(res);
    }

    // 素材上传
    public String uploadMedia(String type, String filePath) {
        if (StringUtil.isNullOrEmpty(type) || StringUtil.isNullOrEmpty(filePath)) {
            return null;
        }
        try {
            java.io.File file = new java.io.File(filePath);
            String url = String.format("%s/cgi-bin/media/upload?access_token=%s&type=%s", baseUrl, getAccessToken(), type);

            HttpPost httpPost = new HttpPost(url);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("media", file, ContentType.MULTIPART_FORM_DATA, file.getName());

            org.apache.http.HttpEntity build = builder.build();
            httpPost.setEntity(build);

            CloseableHttpResponse response = httpClient.execute(httpPost);
            org.apache.http.HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                return string2Json(result).getString("media_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
