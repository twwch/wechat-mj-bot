
import { Configuration, OpenAIApi, ChatCompletionRequestMessageRoleEnum } from "openai"
import axios from "axios";

const configuration = new Configuration({
  apiKey: process.env.OPENAI_API_KEY,
  baseOptions: {
    proxy: {
        host: '127.0.0.1',
        port: 7890
    },
    timeout: 10000
  }
});

axios.defaults.timeout = 10000;

async function ChatCompletion(msg: string) {
    console.log("ChatCompletion: "+ msg);
    const openai = new OpenAIApi(configuration);
    const response = await openai.createChatCompletion({
      messages: [{
        role: ChatCompletionRequestMessageRoleEnum.User,
        content: msg,
      }],
      model: "gpt-3.5-turbo",
      max_tokens: 2048,
      temperature: 0.1,
    });
    const text = response.data.choices[0];
    // 打印 API 返回的结果
    console.log(text);
    return text.message?.content;
}

export default ChatCompletion;
