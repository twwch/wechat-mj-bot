package com.chtw.midjourney.controller;

import com.alibaba.fastjson.JSONObject;
import com.chtw.midjourney.service.WeChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wechat")
@Slf4j
public class WeChatController {

    @Autowired
    private WeChatService weChatService;

    @GetMapping("/callback")
    public String GetWx(String signature, String timestamp, String nonce, String echostr) {

        log.info("/wechat/callback " + signature + timestamp + nonce + echostr);
        try {
            if (weChatService.checkSignature(signature, timestamp, nonce)) {
                return echostr;
            }
        } catch (Exception e) {
            log.error("checkSignature error", e);
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/callback")
    public JSONObject jsonObject (@RequestBody JSONObject jsonObject){
        log.info("/wechat/callback "+jsonObject.toString());
        return weChatService.callback(jsonObject);
    }
}
