import App from './App'
import { createSSRApp } from 'vue'
import { createPinia } from 'pinia'
import uView from "uview-plus"
import CommonJS from './utils/common.js'

uni.CommonJS = CommonJS

export function createApp() {
	const app = createSSRApp(App)
	const pinia = createPinia()
	
	app.use(pinia)
	app.use(uView)
	
	return {
		app,
		pinia
	}
}