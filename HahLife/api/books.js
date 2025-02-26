import request from '@/utils/request'
import config from '@/config'

const baseURL = config[process.env.NODE_ENV].baseURL
  
export const billApi = {
  // 获取用户账簿列表
  getList: () => request.get(`${baseURL}/books/user/list`)
}