import request from '@/utils/request'
import config from '@/config'

const baseURL = config[process.env.NODE_ENV].baseURL

export const userApi = {
    // 修改用户名
    updateAllName: (data) => request.post(`${baseURL}/sumui/user/update/nickname`, data)
} 