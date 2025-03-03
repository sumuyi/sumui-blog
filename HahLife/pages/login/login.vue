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
				<button class="wechat-login-btn" @click="handleLogin">
					<!-- <view class="wechat-icon align-center">
						<image src="@/static/wechat.png" mode="aspectFit" style="width: 40rpx; height: 40rpx;"></image>
					</view> -->
					<view class="wechat-text align-center">微信一键登录</view>
				</button>
			</view>
			
			<!-- 用户协议和隐私政策 -->
			<view class="agreement-container">
				<checkbox :checked="isAgreed" @tap="toggleAgreement" style="transform:scale(0.7)" />
				<text class="agreement-text">我已阅读并同意</text>
				<text class="agreement-link" @tap="showUserAgreement">《用户协议》</text>
				<text class="agreement-text">和</text>
				<text class="agreement-link" @tap="showPrivacyPolicy">《隐私政策》</text>
			</view>
		</view>
		
		<!-- 用户协议弹窗 -->
		<uv-popup ref="showAgreementPopup" :close-on-click-overlay="false" mode="center" round="16" closeable>
			<view class="popup-content">
				<view class="popup-title">用户协议</view>
				<scroll-view scroll-y class="popup-scroll">
					<view class="popup-text">
						<!-- 用户协议内容 -->
						<p>欢迎使用“哈哈日常”小程序！在您使用本小程序之前，请仔细阅读并同意以下用户协议。如果您不同意本协议的任何条款，请立即停止使用本小程序。</p>
						<p>1. 服务内容</p>
						<p>“哈哈日常”是一款为情侣提供记账、生活点滴记录等功能的小程序。用户可以通过本小程序记录日常开销、分享生活点滴，并与其他用户互动。</p>

						<p>2. 用户注册与账号安全</p>
						<p>2.1 您需要注册一个账号以使用本小程序的全部功能。注册时，您需要提供真实、准确、完整的个人信息。</p>
						<p>2.2 您有责任妥善保管您的账号信息及密码，并对通过您的账号进行的所有活动负责。</p>
						<p>2.3 如发现任何未经授权的账号使用行为，请立即通知我们。</p>

						<p>3. 用户行为规范</p>
						<p>3.1 您承诺在使用本小程序时遵守所有适用的法律法规，并尊重他人的合法权益。</p>
						<p>3.2 您不得利用本小程序进行任何违法、违规或侵犯他人权益的行为。</p>
						<p>3.3 您不得发布任何虚假、诽谤、侮辱、淫秽、暴力或其他不当内容。</p>

						<p>4. 服务变更与终止</p>
						<p>4.1 我们有权根据实际情况随时变更、中断或终止部分或全部服务。</p>
						<p>4.2 如您违反本协议的任何条款，我们有权立即终止您的账号使用权。</p>

						<p>5. 免责声明</p>
						<p>5.1 本小程序提供的服务按“现状”提供，我们不保证服务的及时性、安全性、准确性或完整性。</p>
						<p>5.2 对于因使用或无法使用本小程序而导致的任何损失，我们不承担任何责任。</p>

						<p>6. 协议修改</p>
						<p>6.1 我们有权随时修改本协议的任何条款。修改后的协议将在小程序内公布，并立即生效。</p>
						<p>6.2 如您继续使用本小程序，即表示您接受修改后的协议。</p>
					</view>
				</scroll-view>
				<button class="popup-btn" @tap="closeAgreementPopup">我知道了</button>
			</view>
		</uv-popup>
		
		<!-- 隐私政策弹窗 -->
		<uv-popup ref="showPrivacyPopup" :close-on-click-overlay="false" mode="center" round="16" closeable>
			<view class="popup-content">
				<view class="popup-title">隐私政策</view>
				<scroll-view scroll-y class="popup-scroll">
					<view class="popup-text">
						<!-- 隐私政策内容 -->
						<p>我们非常重视您的隐私保护，并致力于保护您的个人信息。本隐私政策解释了我们在您使用“哈哈日常”小程序时如何收集、使用和保护您的个人信息。</p>

						<p>1. 信息收集</p>
						<p>1.1 我们可能会收集您的以下信息：</p>

						<p>注册信息：如用户名、密码、电子邮件地址等。</p>

						<p>使用信息：如您在小程序内的操作记录、互动内容等。</p>

						<p>设备信息：如设备型号、操作系统版本、IP地址等。</p>

						<p>2. 信息使用</p>
						<p>2.1 我们收集的信息将用于以下目的：</p>

						<p>提供、维护和改进我们的服务。</p>

						<p>处理您的请求和反馈。</p>

						<p>发送重要通知，如服务变更、安全更新等。</p>

						<p>进行数据分析，以优化用户体验。</p>

						<p>3. 信息共享</p>
						<p>3.1 我们不会将您的个人信息出售、交易或转让给第三方，除非获得您的明确同意或法律法规要求。</p>
						<p>3.2 我们可能会与第三方服务提供商共享必要的信息，以协助我们提供服务。</p>

						<p>4. 信息安全</p>
						<p>4.1 我们采取合理的技术和管理措施，以保护您的个人信息免受未经授权的访问、使用或泄露。</p>
						<p>4.2 尽管我们尽力保护您的个人信息，但无法保证绝对的安全性。</p>

						<p>5. 您的权利</p>
						<p>5.1 您可以随时访问、更正或删除您的个人信息。</p>
						<p>5.2 您可以要求我们停止使用或删除您的个人信息，但可能会影响您使用部分或全部服务。</p>

						<p>6. 隐私政策修改</p>
						<p>6.1 我们有权随时修改本隐私政策。修改后的隐私政策将在小程序内公布，并立即生效。</p>
						<p>6.2 如您继续使用本小程序，即表示您接受修改后的隐私政策。</p>
					</view>
				</scroll-view>
				<button class="popup-btn" @tap="closePrivacyPopup">我知道了</button>
			</view>
		</uv-popup>
	</view>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { useUserStore } from '@/store/index.js'
import config from '@/config'

const baseURL = config[process.env.NODE_ENV].baseURL
const userStore = useUserStore()

// 协议相关状态
const isAgreed = ref(false)
const showAgreementPopup = ref(false)
const showPrivacyPopup = ref(false)

// 切换协议勾选状态
const toggleAgreement = () => {
	isAgreed.value = !isAgreed.value
}

// 显示用户协议
const showUserAgreement = async() => {
	await nextTick()
	if (showAgreementPopup.value) {
		showAgreementPopup.value.open()
	}
}

// 关闭用户协议
const closeAgreementPopup = () => {
	showAgreementPopup.value.close()
}

// 显示隐私政策
const showPrivacyPolicy = async() => {
	await nextTick()
	if (showPrivacyPopup.value) {
		showPrivacyPopup.value.open()
	}
}

// 关闭隐私政策
const closePrivacyPopup = () => {
	showPrivacyPopup.value.close()
}

// 处理登录
const handleLogin = () => {
	if (!isAgreed.value) {
		uni.showToast({
			title: '请先同意用户协议和隐私政策',
			icon: 'none'
		})
		return
	}
	wechatLogin()
}

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
		url: baseURL + '/auth/api/wx-login',
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
	background-color: #07c160; /* 微信绿色 */
	border-radius: 60rpx;
	padding: 24rpx 0;
	border: none;
	box-shadow: 0 4rpx 8rpx rgba(0, 0, 0, 0.1);
}

.wechat-icon {
	margin-right: 20rpx;
	display: flex;
	align-items: center;
	justify-content: center;
}

.wechat-text {
	font-size: 36rpx;
	font-weight: 500;
	color: #ffffff; /* 白色文字 */
	line-height: 80rpx;
}

.align-center {
	display: flex;
	align-items: center;
	justify-content: center;
}

.wechat-login-btn[disabled] {
	opacity: 0.6;
}

/* 协议相关样式 */
.agreement-container {
	display: flex;
	align-items: center;
	justify-content: center;
	margin-top: 30rpx;
	flex-wrap: wrap;
}

.agreement-text {
	font-size: 28rpx;
	color: #666;
}

.agreement-link {
	font-size: 28rpx;
	color: #6a9a76;
}

/* 弹窗样式 */
.popup-content {
	padding: 30rpx;
	display: flex;
	flex-direction: column;
	width: 600rpx; /* 设置固定宽度 */
	height: 100%;
}

.popup-title {
	font-size: 36rpx;
	font-weight: bold;
	text-align: center;
	margin-bottom: 20rpx;
}

.popup-scroll {
	flex: 1;
	margin-bottom: 20rpx;
	padding: 0 20rpx; /* 增加左右内边距 */
}

.popup-text {
	font-size: 28rpx;
	line-height: 1.6;
	color: #333;
	padding: 10rpx 0; /* 增加上下内边距 */
	height: 800rpx; /* 设置固定高度 */
}

.popup-btn {
	background-color: #94edbf;
	color: #000000;
	border-radius: 60rpx;
	font-size: 32rpx;
	margin-top: 20rpx;
	margin-bottom: 40rpx; /* 增加底部外边距 */
	width: 80%; /* 按钮宽度 */
	margin-left: auto;
	margin-right: auto;
}
</style>
