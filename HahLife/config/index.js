const config = {
  // 开发环境
  development: {
    baseURL: 'http://localhost:9090',
    minioURL: 'https://minio-s3.071020.xyz/sumui-finance'
  },
  // 生产环境
  production: {
    baseURL: 'https://back.071020.xyz',
    minioURL: 'https://minio-s3.071020.xyz/sumui-finance'
  }
}

// 直接导出完整的 config 对象，而不是只导出 baseURL
export default config 