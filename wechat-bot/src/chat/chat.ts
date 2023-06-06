import axios from "axios";

const instance = axios.create({
    baseURL: 'https://api.openai.com/',
    // proxy: {
    //   host: '127.0.0.1',
    //   port: 7890
    // },
    headers: {
        "Content-Type": "application/json",
        "Authorization": process.env.OPENAI_API_KEY
    },
});

instance.defaults.timeout = 10000

async function ChatCompletionV2(msg: string) {
    console.log("ChatCompletion: "+ msg);
    let res = await instance.post('https://api.openai.com/v1/chat/completions', {
        "messages": [
            {"role": "system", "content": "You are a helpful assistant."},
            {"role": "user", "content": msg},
        ],
        "model": "gpt-3.5-turbo",
        "max_tokens": 2048,
        "temperature": 0.1,
    })
    let content = res.data.choices[0].message.content;
    console.log(content);   
    return content;
}

async function ChatCompletionV3(msg: string) {
    console.log("ChatCompletion: "+ msg);
    let res = await axios.post('http://127.0.0.1:81/chat/completions', {
        "messages": [
            {"role": "system", "content": "You are a helpful assistant."},
            {"role": "user", "content": msg},
        ]
    })
    
    let content = res.data.data.choices[0].message.content;
    console.log(content);   
    return content;
}

export {ChatCompletionV2, ChatCompletionV3}
