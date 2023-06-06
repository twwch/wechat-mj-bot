
import {
    WechatyBuilder,
    ScanStatus,
    Message,
    Contact,
  }  from 'wechaty' 
  
  import qrTerm from 'qrcode-terminal'
  import { FileBox } from 'file-box'
  // import http from 'http'
  import {ChatCompletionV3} from './chat/chat'

  const options = {
    name : 'chat-bot',
  }
  
  const bot = WechatyBuilder.build(options)
  
  bot
    .on('logout', onLogout)
    .on('login',  onLogin)
    .on('scan',   onScan)
    .on('error',  onError)
    .on('message', onMessage)
    .start()
    .catch(async e => {
      console.error('Bot start() fail:', e)
      await bot.stop()
      process.exit(-1)
    })
  
  
  function onScan (qrcode: string, status: ScanStatus) {
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
  
  function onLogin (user: Contact) {
    console.info(`${user.name()} login`)
  }
  
  function onLogout (user: Contact) {
    console.info(`${user.name()} logged out`)
  }
  
  function onError (e: Error) {
    console.error('Bot error:', e)
  }
  
  
  async function onMessage (msg: Message) {
   
  
    if (msg.self()) {
      console.info('Message discarded because its outgoing')
      return
    }
    const sender   = msg.talker()
    const receiver = msg.to()
    let text     = msg.text()
    const room     = msg.room()
    
    if (!(receiver || await msg.mentionSelf())) {
      return
    }
    console.info(msg.toString())
    
    if (await msg.mentionSelf()) {
      console.log(`我被@了, 在群聊${room}中, 由${msg.talker().name()}发送的消息: ${msg.text()}`);
      // await msg.say(`我被@了, 在群聊${room}中, 由${msg.talker().name()}发送的消息: ${msg.text()}`)
      // return
      text = await msg.mentionText()
    }

    if (text === 'ding') {
      await msg.say('dong')
      return 
    }
    
    const type_for_chat = text.split(' ')[0]

    if (type_for_chat === '/chat') {
      text = text.replace('/chat', '')
      const res = await ChatCompletionV3(text)
      console.info(res)
      msg.say(res || '我不知道你在说什么')
      return
    }

    if (type_for_chat === '/help') {
      await  msg.say('目前支持的命令有: \n/chat: 聊天\n/help: 帮助')
      return
    }

    // await  msg.say('目前支持的命令有: \n/chat: 聊天\n/help: 帮助')
    return
    
    if (msg.age() > 2 * 60) {
      console.info('Message discarded because its TOO OLD(than 2 minutes)')
      return
    }
  
    if (msg.type() !== bot.Message.Type.Text
      || !/^(ding|ping|bing|code)$/i.test(msg.text())
    ) {
      console.info('Message discarded because it does not match ding/ping/bing/code')
      return
    }
  
    /**
     * 1. reply 'dong'
     */
    await msg.say('dong')
    console.info('REPLY: dong')
  
    /**
     * 2. reply image(qrcode image)
     */
    const fileBox = FileBox.fromUrl('https://wechaty.github.io/wechaty/images/bot-qr-code.png')
  
    await msg.say(fileBox)
    console.info('REPLY: %s', fileBox.toString())
    await msg.say(sender?.id || 'unknown')
    
  }


async function xx () {
  let contact1 = await bot.Contact.find({ name: '吾梦回' }) // 查找联系人
  
  const url = 'https://cdn.discordapp.com/attachments/1100968347086102530/1112651481645060116/WK_ebfebe0c8542_a_technology_poster__show_intelligence__smarthi_4d46852f-9a83-4baa-a0f8-8a63d91686f6.png'
  const fileBox = FileBox.fromUrl(url)
  // @ts-ignore
  await contact1.say(fileBox) // 发送消息
}

// const PORT = process.env.PORT || 3000;

// const server =  http.createServer((req, res) => {
//   res.statusCode = 200;
//   res.setHeader('Content-Type', 'text/plain');
//   // let contact =  bot.Contact.find({ name: '栀子花' }) // 查找联系人
//   // // @ts-ignore
//   // contact.say('hello')
//   xx()
//   res.end('Hello World\n');
// });

// server.listen(PORT, () => {
//   console.log(`Server running on port ${PORT}`);
// });
