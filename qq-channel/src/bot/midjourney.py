import requests

from config.env import MIDJOURNEY_CONF


class MidjourneyAPI(object):

    def __init__(self):
        self.headers = {
            'authorization': MIDJOURNEY_CONF["user_token"]
        }

    def create_imagine(self, prompt, message_id):
        payload = {
            'type': 2,
            "application_id": "936929561302675456",
            "guild_id": MIDJOURNEY_CONF["guild_id"],
            "channel_id": MIDJOURNEY_CONF["channel_id"],
            "session_id": MIDJOURNEY_CONF["session_id"],
            'data': {
                'version': '1077969938624553050',
                'id': '938956540159881230',
                'name': 'imagine',
                'type': 1,
                'options': [{'type': 3, 'name': 'prompt', 'value': message_id + " " + str(prompt)}],
                'attachments': []
            }
        }
        retry = 3
        while retry > 0:
            retry -= 1
            r = requests.post(
                'https://discord.com/api/v9/interactions', json=payload, headers=self.headers)
            print(r.text)
            if r.status_code == 204:
                return True
        return False

    def up_imagine(self, mj_message_id, custom_id):
        payload = {
            'type': 3,
            "application_id": "936929561302675456",
            "guild_id": MIDJOURNEY_CONF["guild_id"],
            "channel_id": MIDJOURNEY_CONF["channel_id"],
            "session_id": MIDJOURNEY_CONF["session_id"],
            "message_id": mj_message_id,
            'data': {
                'component_type': 2,
                'custom_id': custom_id
            }
        }
        retry = 3
        while retry > 0:
            retry -= 1
            r = requests.post(
                'https://discord.com/api/v9/interactions', json=payload, headers=self.headers)
            print(r.text, payload)
            if r.status_code == 204:
                return True
        return False
