import request from '@/utils/request'
import config from '@/config'

const baseURL = config[process.env.NODE_ENV].baseURL
  
export const billApi = {
  // 添加账单
  add: (data) => request.post(`${baseURL}/bill/add`, data),
  
  // 获取账单列表
  getList: (bookId, month) => request.get(`${baseURL}/bill/list/${bookId}/${month}`),
  
  // 获取账单详情
  getDetail: (id) => request.get(`${baseURL}/bill/detail/${id}`),
  
  // 删除账单
  delete: (id) => request.delete(`${baseURL}/bill/delete/${id}`),

  // 获取统计数据
  getStatistics: (bookId, month) => request.get(`${baseURL}/bill/statistics/${bookId}/${month}`)
} 