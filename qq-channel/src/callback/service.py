import json
import os

import requests
from PIL import Image

from bot.mdb import QQ_MESSAGE_TABLE
from bot.sdk import QQChannelRobotSDK

qq_sdk = QQChannelRobotSDK()


class CallbackService:

    @staticmethod
    def save_image_and_send(channel_id, qq_msg_id, data, log):
        discord = data.get("discord") or {}
        image_url = discord.get("url")
        response = requests.get(image_url)
        image_name = image_url.split("/")[-1]

        # 将图片保存到本地文件
        with open(image_name, 'wb') as f:
            f.write(response.content)
        mew_image_name = f"{qq_msg_id}.jpg"
        image = Image.open(image_name)
        new_image = image.convert("RGB")
        # 保存图片，压缩其质量
        new_image.save(mew_image_name, quality=70)
        author = log.get("qq_message").get("author").replace("'", '"').replace("True", "true").replace("False", "false")
        author_id = json.loads(author).get("id")
        content = f"<@!{author_id}> {qq_msg_id}::{discord.get('messageId')}"
        res = qq_sdk.send_image_message(channel_id, content, mew_image_name)
        print("save_image_and_send", res)
        os.remove(image_name)
        os.remove(mew_image_name)
        return 0, "success", res

    def msg_call(self, data):
        discord = data.get("discord") or {}
        q_type = data.get("type")
        content = data.get("content").replace("**", "", -1)
        qq_msg_id = content.split(" ")[0]
        log = QQ_MESSAGE_TABLE.find_one({"qq_id": qq_msg_id})
        if not log:
            return 1, f"qq_msg_id {qq_msg_id} not found", None
        if discord:
            QQ_MESSAGE_TABLE.update_one({"qq_id": qq_msg_id, "mj_id": discord.get("messageId")}, {"$set": {"mj_message": data.get("discord")}}, upsert=True)
        channel_id = log.get("qq_message").get("channel_id")
        if q_type == 'FIRST_TRIGGER':
            content = content.replace(qq_msg_id, "")
            qq_sdk.send_text_message(channel_id, content, message_reference={"message_id": qq_msg_id})
        if q_type == 'GENERATE_END':
            return self.save_image_and_send(channel_id, qq_msg_id, data, log)
        return 0, "success", None
