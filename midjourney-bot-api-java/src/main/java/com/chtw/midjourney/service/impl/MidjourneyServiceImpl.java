package com.chtw.midjourney.service.impl;

import com.chtw.midjourney.dto.RequestTrigger;
import com.chtw.midjourney.service.DiscordInteractionService;
import com.chtw.midjourney.service.MidjourneyService;
import com.chtw.midjourney.storage.mj.document.ImagineLogs;
import com.chtw.midjourney.storage.mj.repository.ImagineRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MidjourneyServiceImpl implements MidjourneyService {

    @Autowired
    private ImagineRepository imagineRepository;

    @Autowired
    private DiscordInteractionService discordInteractionService;
    public void executeAction(RequestTrigger requestTrigger) throws Exception {
        String type = requestTrigger.getType();
        switch (type) {
            case "generate" ->
                // Call GenerateImage service
                    discordInteractionService.generateImage(requestTrigger.getPrompt());
            case "upscale" ->
                // Call ImageUpscale service
                    discordInteractionService.upscale(requestTrigger.getIndex(),
                            requestTrigger.getDiscordMsgId(),
                            requestTrigger.getMsgHash());
            case "variation" ->
                // Call ImageVariation service
                    discordInteractionService.variate(requestTrigger.getIndex(),
                            requestTrigger.getDiscordMsgId(),
                            requestTrigger.getMsgHash());
            case "maxUpscale" ->
                // Call ImageMaxUpscale service
                    discordInteractionService.maxUpscale(requestTrigger.getDiscordMsgId(),
                            requestTrigger.getMsgHash());
            case "reset" ->
                // Call ImageReset service
                    discordInteractionService.reSet(requestTrigger.getDiscordMsgId(),
                            requestTrigger.getMsgHash());
            default -> throw new Exception("Invalid type");
        }
    }

    @Override
    public void saveAndExecuteAction(ImagineLogs data) throws Exception {
        //  sava to mongodb
        data.setId(data.getMsg_id());
        ImagineLogs imagineLogs = imagineRepository.insertOne(data);
        log.info("Saved to mongodb: {}", imagineLogs);
        executeAction(data.getMj_request());
    }
}