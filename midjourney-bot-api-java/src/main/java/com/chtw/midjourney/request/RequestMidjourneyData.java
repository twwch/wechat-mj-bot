package com.chtw.midjourney.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class RequestMidjourneyData {
    @Builder.Default
    private String version = "1077969938624553050";
    @Builder.Default
    private String id = "938956540159881230";
    @Builder.Default
    private String name = "imagine";
    @Builder.Default
    private int type = 1;
    private List<RequestMidjourneyOptions> options;
    private RequestMidjourneyApplicationCommand application_command;
    private List<String> attachments;




}