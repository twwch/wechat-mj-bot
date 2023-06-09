import {
    WechatyBuilder,
    ScanStatus,
    Message,
    Contact,
} from 'wechaty'

import qrTerm from 'qrcode-terminal'
import {ChatCompletionV2, Midjourney} from './utils'
import {start} from "./server";

const options = {
    name: 'chat-bot',
}

const bot = WechatyBuilder.build(options)

bot
    .on('logout', onLogout)
    .on('login', onLogin)
    .on('scan', onScan)
    .on('error', onError)
    .on('message', onMessage)
    .start()
    .catch(async e => {
        console.error('Bot start() fail:', e)
        await bot.stop()
        process.exit(-1)
    })


function onScan(qrcode: string, status: ScanStatus) {
    if (status === ScanStatus.Waiting || status === ScanStatus.Timeout) {
        qrTerm.generate(qrcode)

        const qrcodeImageUrl = [
            'https://wechaty.js.org/qrcode/',
            encodeURIComponent(qrcode),
        ].join('')

        console.info('123 onScan: %s(%s) - %s', ScanStatus[status], status, qrcodeImageUrl)
    } else {
        console.info('onScan: %s(%s)', ScanStatus[status], status)
    }
}

function onLogin(user: Contact) {
    console.info(`${user.name()} login`)
}

function onLogout(user: Contact) {
    console.info(`${user.name()} logged out`)
}

function onError(e: Error) {
    console.error('Bot error:', e)
}


async function onMessage(msg: Message) {
    if (msg.self()) {
        console.info('Message discarded because its outgoing')
        return
    }
    const receiver = msg.to()
    let text = msg.text()
    const room = msg.room()
    const msg_id = msg.id
    if (!(receiver || await msg.mentionSelf())) {
        return
    }
    console.info(msg.toString())

    if (await msg.mentionSelf()) {
        console.log(`我被@了, 在群聊${room}中, 由${msg.talker().name()}发送的消息: ${msg.text()}`);
        text = await msg.mentionText()
    }

    text = text.trim().replace("\n", "")

    if (text === 'ding') {
        await msg.say('dong')
        return
    }

    const type_for_chat = text.split(' ')[0]

    if (type_for_chat === '/help') {
        await msg.say('目前支持的命令有: \n/chat: 聊天\n/imagine: 绘图（只支持英文）\n/help: 帮助')
        return
    }

    if (type_for_chat === '/chat') {
        text = text.replace('/chat', '')
        const res = await ChatCompletionV2(text)
        console.info(res)
        const resp = `${text}\n=================\n\n${res || '我不知道你在说什么'}`
        await msg.say(resp)
        return
    }


    if (type_for_chat === '/imagine') {
        text = text.replace('/imagine', '')
        await Midjourney.createImages(msg, text)
        await msg.say(`任务 ${msg_id} 正在排队中...`)
        return
    }

    // return
    //
    // const fileBox = FileBox.fromUrl('https://wechaty.github.io/wechaty/images/bot-qr-code.png')
    //
    // const sender = msg.talker()
    // await msg.say(fileBox)
    // console.info('REPLY: %s', fileBox.toString())
    // await msg.say(sender?.id || 'unknown')

}

start(bot)