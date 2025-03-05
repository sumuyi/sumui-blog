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
          const { code, message, result, status } = res.data
          if (code === 200) {
            const data = {code, message}
            resolve(result ? result : data)
          } else {
            // 未授权访问
            if (status.code === 401) {
              // 清除本地缓存，并跳转到登录页
              uni.clearStorageSync();
              uni.redirectTo({
                url: '/pages/login/login'
              })
              return
            }
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