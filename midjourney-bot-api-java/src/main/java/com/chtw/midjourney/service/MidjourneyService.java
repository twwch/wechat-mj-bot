package com.chtw.midjourney.service;

import com.chtw.midjourney.dto.RequestTrigger;
import com.chtw.midjourney.storage.mj.document.ImagineLogs;

public interface MidjourneyService {
    void executeAction(RequestTrigger requestTrigger) throws Exception;

    void saveAndExecuteAction(ImagineLogs data) throws Exception;
}
