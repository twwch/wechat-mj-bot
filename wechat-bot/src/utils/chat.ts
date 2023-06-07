import {post} from "./request";
import axios from "axios";

async function ChatCompletion(msg: string) {
    console.log("ChatCompletion: " + msg);
    let res = await post('/test/chat/completions', {
        "messages": [
            {"role": "system", "content": "You are a helpful assistant."},
            {"role": "user", "content": msg},
        ]
    })
    return res.data.data.choices[0].message.content;
}

async function ChatCompletionV2(msg: string) {
    console.log("ChatCompletion: " + msg);
    let res = await axios.post('http://127.0.0.1:82/chat/completions', {
        "messages": [
            {"role": "system", "content": "You are a helpful assistant."},
            {"role": "user", "content": msg},
        ]
    })
    return res.data.data.choices[0].message.content;
}

export {ChatCompletion, ChatCompletionV2}
