import {Message} from "wechaty";
import {post} from "./request";


const GENERATE_TYPE = {
    GENERATE: "generate"
}

class Midjourney {

    static async createImages(msg: Message, text: string) {
        const room = msg.room();
        const talker = msg.talker();
        const topic = await room?.topic();
        return post("/midjourney/imagine/save", {
            "msg_id": msg.id,
            "room_id": room?.id,
            "room_topic": topic,
            "room_name": topic,
            "open_id": talker.id,
            "open_name": talker.name(),
            "mj_request": {
                "prompt": `/imagine ${msg.id} ${text}`,
                "type": GENERATE_TYPE.GENERATE
            }
        })
    }
}

export {Midjourney}