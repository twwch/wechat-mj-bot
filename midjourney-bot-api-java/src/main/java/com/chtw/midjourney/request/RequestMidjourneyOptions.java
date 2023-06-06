package com.chtw.midjourney.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class RequestMidjourneyOptions {
    @Builder.Default
    private int type = 3;
    @Builder.Default
    private String name = "prompt";
    private String value ;

}