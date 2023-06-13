import logging

from flask import Flask, request
from flask_cors import CORS
from callback.service import CallbackService

app = Flask(__name__)
cors = CORS(app, resources={r"/*": {"origins": "*"}})

logger = logging.getLogger(__name__)

svc = CallbackService()


@app.after_request
def after_request(response):
    response.headers["Access-Control-Allow-Origin"] = "*"
    response.headers["Access-Control-Allow-Methods"] = "GET,POST,PUT,DELETE,OPTIONS"
    response.headers["Access-Control-Allow-Headers"] = "Content-Type,Authorization"
    return response


@app.route('/state', methods=['GET'])
def create_test():
    return {
        "code": 0,
        "data": "server is running in 9875",
        "msg": "success"
    }


@app.route('/callback', methods=['POST'])
def callback_handler():
    data = request.get_json(force=True)
    print(f"callback data: {data}")
    code, msg, data = svc.msg_call(data)
    return {
        "code": code,
        "msg": msg,
        "data": data
    }


def run():
    app.run(host='0.0.0.0', port=9875)
