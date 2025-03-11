import request from '@/utils/request'
import config from '@/config'

const baseURL = config[process.env.NODE_ENV].baseURL
  
export const bookApi = {
  // 获取用户账簿列表
  getList: () => request.get(`${baseURL}/books/user/list`),

  // 添加账本
  addBook: (params) => request.post(`${baseURL}/books/create`, params),
}