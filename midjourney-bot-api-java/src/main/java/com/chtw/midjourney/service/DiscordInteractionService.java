package com.chtw.midjourney.service;

public interface DiscordInteractionService {

    public void generateImage(String prompt);

    public void upscale(int index, String messageId, String messageHash);

    public void variate(int index, String messageId, String messageHash);

    public void maxUpscale(String messageId, String messageHash);


    public void reSet(String messageId, String messageHash);
}
