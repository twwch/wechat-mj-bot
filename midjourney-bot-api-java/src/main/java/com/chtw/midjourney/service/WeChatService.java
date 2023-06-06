package com.chtw.midjourney.service;

import com.alibaba.fastjson.JSONObject;

import java.security.NoSuchAlgorithmException;

public interface WeChatService {

    boolean checkSignature(String signature, String timestamp, String nonce) throws NoSuchAlgorithmException;

    JSONObject sendTextMessage(JSONObject data);

    JSONObject callback(JSONObject jsonObject);
}
