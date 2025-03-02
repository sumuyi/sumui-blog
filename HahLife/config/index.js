const config = {
  // 开发环境
  development: {
    baseURL: 'http://localhost:9090'  // 本地开发环境地址
  },
  // 生产环境
  production: {
    baseURL: 'https://back.071020.xyz'  // 实际生产环境地址
  }
}

// 直接导出完整的 config 对象，而不是只导出 baseURL
export default config 