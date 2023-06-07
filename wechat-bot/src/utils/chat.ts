import {post} from "./request";

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

export {ChatCompletion}
