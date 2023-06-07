import axios from "axios";

const instance = axios.create(
    {
        baseURL: 'http://127.0.0.1:80',
        timeout: 10000
    }
)

const request = async (url: string, method: string, data: any) => {
    return await instance.request({
        url,
        method,
        data
    })
}

const post = async (url: string, data: any) => {
    return await request(url, 'post', data)
}

const get = async (url: string, data: any) => {
    return await request(url, 'get', data)
}

export {
    request,
    post,
    get
}
