import json

import requests

from config.env import APP_ID, APP_TOKEN


class QQChannelRobotSDK(object):

    def __init__(self):
        self.host = "https://api.sgroup.qq.com"
        # self.host = "https://sandbox.api.sgroup.qq.com"

    @staticmethod
    def __headers():
        return {
            "Authorization": f"Bot {APP_ID}.{APP_TOKEN}"
        }

    def __request(self, method, url, data=None, **kwargs):
        retry = 3
        while retry > 0:
            try:
                return requests.request(method, url, json=data, headers=self.__headers(), **kwargs).json()
            except Exception as e:
                retry -= 1
                print(e)
        return {}

    def __form_request(self, method, url, data=None, **kwargs):
        retry = 3
        while retry > 0:
            try:
                return requests.request(method, url, data=data, headers=self.__headers(), **kwargs).json()
            except Exception as e:
                retry -= 1
                print(e)
        return {}

    def send_message(self, channel_id, content, **kwargs):
        path = f"/channels/{channel_id}/messages"
        data = {
            "content": content,
            **kwargs
        }
        return self.__request("POST", self.host + path, data=data)

    def send_text_message(self, channel_id, content, **kwargs):
        return self.send_message(channel_id, content, **kwargs)

    def send_image_message(self, channel_id, content, file_path):
        path = f"/channels/{channel_id}/messages"
        data = {
            "content": content,
        }
        print(data)
        files = {
            "file_image": (file_path, open(file_path, 'rb'), 'image/jpeg')
        }
        return self.__form_request("POST", self.host + path, data=data, files=files)
