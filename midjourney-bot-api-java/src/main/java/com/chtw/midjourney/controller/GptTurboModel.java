
package com.chtw.midjourney.controller;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class GptTurboModel {


    /**
     * The Model.
     */
    private String model = "gpt-3.5-turbo";

    /**
     * The Top p.
     */
    private Double top_p = 0.9;

    /**
     * The Stream.
     */
    private boolean stream = false;

    /**
     * The Messages.
     */
    private List<Messages> messages;

    /**
     * The Max tokens.
     */
    private Integer max_tokens = 2048;

    @Data
    public static class Messages {

        /**
         * The Role.
         */
        private String role;

        /**
         * The Content.
         */
        private String content;
    }

}
