package com.chtw.midjourney.service;

import com.chtw.midjourney.dto.RequestTrigger;

public interface MidjourneyService {
    public void executeAction(RequestTrigger requestTrigger) throws Exception;
}
