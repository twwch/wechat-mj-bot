import express from 'express';
import {FileBox} from 'file-box'
import multer from 'multer';
const upload = multer(); // 使用内存存储引擎


const PORT = 3000;
const app = express();
app.use(express.json());
app.use(express.urlencoded({ extended: true }));


app.get('/state', (req, res) => {
    res.json({message: `Server running on port ${PORT}`, code: 0, data: 'ok'});
})

function start(bot: any | undefined) {

    app.post("/sendmsg", upload.single('file'), async (req, res) => {
        const {room_topic, open_name, image} = req.body;
        console.log(req.body);
        console.log(req.file);
        if (bot) {
            let msg = req.body.content;
            if (req.file) {
                msg = FileBox.fromBuffer(req.file.buffer, req.file.originalname);
            }
            if (room_topic) {
                const room = await bot.Room.find({topic: room_topic});
                await room.say(msg);
            } else if (open_name) {
                console.log("发给谁：open_name")
                const contact = await bot.Contact.find({name: open_name});
                console.log("contact: ", contact);
                await contact.say(msg);
            } else {
                console.log("不知道发给谁：no room_id or open_id")
            }
        }
        res.json({message: `ok`, code: 0, data: req.body});
    })

    app.listen(PORT, () => {
        console.log(`Server running on port ${PORT}`);
    });
}

export {start};