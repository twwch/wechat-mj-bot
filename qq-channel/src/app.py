import threading

from bot.qq import bot_start
from callback.app import run

if __name__ == '__main__':
    threading.Thread(target=run).start()
    # 启动bot
    bot_start()
