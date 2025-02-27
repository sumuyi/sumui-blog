// uni-app 环境下使用 uni.request
const service = {
  request(options) {
    return new Promise((resolve, reject) => {
      var tokenName = uni.getStorageSync('tokenName');    // 从本地缓存读取tokenName值
      var tokenValue = uni.getStorageSync('tokenValue');    // 从本地缓存读取tokenValue值
      var header = {
          "content-type": "application/json"
      };
      if (tokenName != undefined && tokenName != '') {
          header[tokenName] = tokenValue;
      }

      uni.request({
        ...options,
        header: header,
        success: (res) => {
          const { code, message, result } = res.data
          if (code === 200) {
            resolve(result)
          } else {
            uni.showToast({
              title: message || '请求失败',
              icon: 'none'
            })
            reject(new Error(message || '请求失败'))
          }
        },
        fail: (err) => {
          uni.showToast({
            title: '请求失败',
            icon: 'none'
          })
          reject(err)
        }
      })
    })
  }
}

// 封装请求方法
const request = {
  get(url, data) {
    return service.request({
      url,
      method: 'GET',
      data
    })
  },
  post(url, data) {
    return service.request({
      url,
      method: 'POST',
      data
    })
  },
  put(url, data) {
    return service.request({
      url,
      method: 'PUT',
      data
    })
  },
  delete(url, data) {
    return service.request({
      url,
      method: 'DELETE',
      data
    })
  }
}

export default request  // uni-app 中使用 ES6 导出 