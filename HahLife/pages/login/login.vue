<template>
	<view class="login-container">
		<!-- 中间内容区 -->
		<view class="content">
			<view class="logo-container">
				<image class="logo" src="@/static/logo.png" mode="aspectFit"></image>
			</view>
			
			<view class="app-name">
				<text class="separator">✨</text>
				<text class="slogan">生活需要点柴米油盐</text>
			</view>
			
			<!-- 微信登录按钮 -->
			<view class="login-btn-container">
				<button class="wechat-login-btn" @click="wechatLogin">
					<view class="wechat-icon align-center">
						<image src="@/static/wechat.png" mode="aspectFit" style="width: 24px; height: 24px;"></image>
					</view>
					<view class="wechat-text align-center">通过微信登录</view>
				</button>
			</view>
		</view>
	</view>
</template>

<script setup>
import { useUserStore } from '@/store/index.js'

const userStore = useUserStore()

const wechatLogin = () => {
	uni.showLoading({
		title: '正在登录...',
		mask: true
    })
	// 这里可以调用uni.login等API实现微信登录
	uni.login({
		provider: 'weixin',
		success: res => {
			if (res.errMsg === "login:ok") {
				// 获取用户信息
				uni.getUserInfo({
					provider: 'weixin',
					success: (infoRes) => {
						let param = {
							code: res.code,
							encryptedData: infoRes.encryptedData,
							iv: infoRes.iv,
							signature: infoRes.signature,
							rawData: infoRes.rawData
						}
						// 请求后端登录接口
						doLogin(param)
					}
				});
			}
		}
	})
}

const doLogin = (param) => {
	console.log(param)
	uni.request({
		// url: 'https://back.071020.xyz/auth/api/wx-login',
		url: 'http://localhost:9090/auth/api/wx-login',
		method: 'POST',
		data: param,
		success: (res) => {
			let { code, result } = res.data;
			if (code === 200) {
				// 存储用户信息到本地
				uni.setStorageSync('userId', result.userId);
				uni.setStorageSync('tokenName', "Sa-token");
				uni.setStorageSync('tokenValue', result.saToken);
				uni.setStorageSync('userInfo', result);

				// 更新 store
				userStore.setUserId(result.userId);
				userStore.setUserInfo(result);
				
				// 如果有家庭成员列表，直接设置
				if (result.familyUsersList) {
					userStore.setFamilyList(result.familyUsersList);
				}
				uni.hideLoading()

				uni.switchTab({
					url: "/pages/index/index"
				})
			} else {
				uni.showToast({
					icon: 'none',
					title: '登录失败',
					duration: 2000
				})
			}
		},
		fail: (err) => {
			console.error('登录请求失败：', err);
			uni.showToast({
				icon: 'none',
				title: '网络请求失败',
				duration: 2000
			})
		}
	})
}
</script>

<style>
.login-container {
	display: flex;
	flex-direction: column;
	min-height: 100vh;
	background-color: #f5f5f5;
}

/* 导航栏样式 */
.nav-bar {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 20rpx 30rpx;
	background-color: #b8dbc8;
	height: 100rpx;
}

.home-icon image {
	width: 48rpx;
	height: 48rpx;
}

.title {
	font-size: 32rpx;
	font-weight: bold;
}

.right-icons {
	display: flex;
	align-items: center;
}

.dots, .line, .circle {
	margin-left: 20rpx;
	font-size: 36rpx;
}

/* 内容区样式 */
.content {
	flex: 1;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 40rpx;
}

.logo-container {
	margin-bottom: 40rpx;
}

.logo {
	width: 160rpx;
	height: 160rpx;
	background-color: #6a9a76;
	border-radius: 40rpx;
}

.app-name {
	display: flex;
	align-items: center;
	justify-content: center;
	margin-bottom: 400rpx;
}

.name, .slogan {
	font-size: 36rpx;
	color: #6a9a76;
}

.separator {
	margin: 0 10rpx;
	color: #f0c14b;
}

/* 登录按钮样式 */
.login-btn-container {
	width: 100%;
	padding: 0 40rpx;
}

.wechat-login-btn {
	display: flex;
	align-items: center;
	justify-content: center;
	background-color: #94edbf;
	border-radius: 60rpx;
	padding: 24rpx 0;
}

.wechat-icon {
	width: 48rpx;
	margin-right: 16rpx;
}

.wechat-text {
	font-size: 40rpx;
	font-weight: bold;
	color: #000000;
	line-height: 80rpx;
}

.align-center {
	display: flex;
	align-items: center;
	justify-content: center;
}
</style>
