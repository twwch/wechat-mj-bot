package com.chtw.midjourney.storage.mj.document;

import com.chtw.midjourney.dto.RequestTrigger;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "imagine_logs")
@Data
@Getter
@Setter
public class ImagineLogs {
    @Id
    private String id;

    private String msg_id;

    private String room_id;

    private String room_name;

    private String room_topic;

    private String open_id;

    private String open_name;

    private RequestTrigger mj_request;
}
