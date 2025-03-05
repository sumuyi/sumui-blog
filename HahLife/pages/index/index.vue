<template>
	<view class="container">
		<!-- 顶部状态栏 -->
		<view class="header">
			<view class="greeting">
				<text class="greeting-text">{{ greeting }}，{{ userName }}</text>
			</view>
			<view class="header-right">
				<uv-icon name="setting" size="20" @click="showToast"></uv-icon>
			</view>
		</view>

		<view class="weather-card">
			<view class="weather-info">
				<view class="weather-left">
				<text class="temperature">{{ weather.temperature }}°</text>
				<text class="weather-desc">{{ weather.description }}</text>
				</view>
				<view class="weather-right">
				<image :src="getWeatherImage(weather.description)" class="weather-image"></image>
				<text class="location">{{ weather.location }}</text>
				</view>
			</view>
			<view class="date-info">
				<text class="date-text">{{ today }}</text>
				<text class="calendar-info">{{ lunarDate }}</text>
			</view>
		</view>
	</view>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue';

// 用户名
const userName = ref(uni.getStorageSync('userNickName') || '用户');

// 问候语
const greeting = computed(() => {
	const hour = new Date().getHours();
	if (hour < 6) return '凌晨好';
	if (hour < 9) return '早上好';
	if (hour < 12) return '上午好';
	if (hour < 14) return '中午好';
	if (hour < 17) return '下午好';
	if (hour < 19) return '傍晚好';
	return '晚上好';
});

// 今日日期
const today = computed(() => {
	const date = new Date();
	const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
	return `${date.getMonth() + 1}月${date.getDate()}日 ${weekdays[date.getDay()]}`;
});

// 农历日期（模拟数据）
const lunarDate = ref('农历三月初五');
// 天气信息
const weather = ref({
	temperature: '--',
	description: '未知',
	location: '定位中...',
	icon: 'sun'
});
let weatherTimer = null;
const getWeatherInfo = async () => {
	try {
		// 获取位置信息
		const location = await new Promise((resolve, reject) => {
			uni.getLocation({
				type: 'gcj02',
				success: res => resolve(res),
				fail: err => {
					console.error('获取位置失败：', err);
					// 位置获取失败时使用默认城市
					resolve({ longitude: 116.397428, latitude: 39.90923 }); // 北京市默认坐标
				}
			});
		});
		
		// 请求天气API
		const weatherRes = await uni.request({
			url: 'https://restapi.amap.com/v3/weather/weatherInfo',
			data: {
				key: 'cde01e7626a0f3fce56dccab37d734da', // 使用新的key
				city: '320500',
				extensions: 'base', // 获取实况天气
				output: 'JSON'
			}
		});
		console.log('天气API返回数据：', weatherRes.data);
		if (weatherRes.data.status === '1' && weatherRes.data.lives?.length > 0) {
			const weatherData = weatherRes.data.lives[0];

			// 更新天气信息
			weather.value = {
				temperature: weatherData.temperature,
				description: weatherData.weather,
				location: weatherData.city,
				icon: getWeatherIcon(weatherData.weather) // 根据天气状况设置对应图标
			};
		}
	} catch (err) {
		console.error('获取天气信息失败：', err);
		uni.showToast({
			title: '获取天气信息失败',
			icon: 'none'
		});
	}
};


// 根据天气状况返回对应的图片路径
const getWeatherImage = (weather) => {
  const weatherMap = {
    '晴': '../../static/weather/sunny.png',
    '多云': '../../static/weather/cloud.png',
    '阴': '../../static/weather/cloud.png',
    '小雨': '../../static/weather/rain.png',
    '中雨': '../../static/weather/rain.png',
    '大雨': '../../static/weather/rain.png',
    '雷阵雨': '../../static/weather/rain.png',
    '雪': '../../static/weather/snow.png',
    '雾': '../../static/weather/fog.png',
    '霾': '../../static/weather/haze.png'
  };
  return weatherMap[weather] || '/static/weather/sunny.png';
};

// 根据天气状况返回对应的图标名称
const getWeatherIcon = (weather) => {
	const weatherMap = {
		'晴': 'sun',
		'多云': 'cloud',
		'阴': 'cloud',
		'小雨': 'rain',
		'中雨': 'rain',
		'大雨': 'rain',
		'雷阵雨': 'lightning',
		'雪': 'snow',
		'小雪': 'snow',
		'中雪': 'snow',
		'大雪': 'snow',
		'雾': 'mist',
		'霾': 'mist'
	};
	return weatherMap[weather] || 'sun';
};
const showToast = () => {
	uni.showToast({
		title: '功能开发中',
		icon: 'none'
	});
};


// 在页面加载时获取天气信息
onMounted(() => {
  getWeatherInfo();
  // 每30分钟更新一次天气
  weatherTimer = setInterval(getWeatherInfo, 30 * 60 * 1000);
});

// 在页面卸载时清除定时器
onUnmounted(() => {
  if (weatherTimer) {
    clearInterval(weatherTimer);
    weatherTimer = null;
  }
});
</script>

<style lang="scss" scoped>
.container {
	padding: 30rpx;
	background-color: #f8f8f8;
	min-height: 100vh;
}

.header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 30rpx;
}

.greeting-text {
	font-size: 36rpx;
	font-weight: bold;
}

.header-right {
	padding: 10rpx;
}

.weather-card {
  background-color: #ffffff;
  border-radius: 16rpx;
  padding: 40rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.weather-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
}

.weather-left {
  display: flex;
  flex-direction: column;
}

.temperature {
  font-size: 72rpx;
  font-weight: bold;
  color: #333333;
  line-height: 1;
  margin-bottom: 10rpx;
}

.weather-desc {
  font-size: 32rpx;
  color: #666666;
}

.weather-right {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.weather-image {
  width: 120rpx;
  height: 120rpx;
  margin-bottom: 10rpx;
}

.location {
  font-size: 28rpx;
  color: #666666;
}

.date-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top: 1rpx solid #f0f0f0;
  padding-top: 30rpx;
  margin-top: 10rpx;
}

.date-text {
  font-size: 32rpx;
  color: #333333;
  font-weight: 500;
}

.calendar-info {
  font-size: 32rpx;
  color: #666666;
}

.feature-grid {
	display: flex;
	flex-wrap: wrap;
	justify-content: space-between;
	margin-bottom: 30rpx;
}

.feature-item {
	width: 22%;
	display: flex;
	flex-direction: column;
	align-items: center;
	margin-bottom: 30rpx;
}

.feature-icon {
	width: 100rpx;
	height: 100rpx;
	border-radius: 20rpx;
	display: flex;
	justify-content: center;
	align-items: center;
	margin-bottom: 10rpx;
}

.feature-name {
	font-size: 24rpx;
	color: #333333;
}

.todo-card,
.health-card {
	background-color: #ffffff;
	border-radius: 16rpx;
	padding: 30rpx;
	margin-bottom: 30rpx;
	box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.section-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 20rpx;
}

.section-title {
	font-size: 32rpx;
	font-weight: bold;
}

.section-more {
	font-size: 26rpx;
	color: #8f8f94;
}

.todo-list {
	margin-top: 20rpx;
}

.todo-item {
	display: flex;
	align-items: center;
	padding: 20rpx 0;
	border-bottom: 1rpx solid #f0f0f0;
}

.todo-item:last-child {
	border-bottom: none;
}

.todo-checkbox {
	width: 40rpx;
	height: 40rpx;
	border-radius: 50%;
	border: 2rpx solid #d9d9d9;
	display: flex;
	justify-content: center;
	align-items: center;
	margin-right: 20rpx;
}

.todo-checkbox.checked {
	background-color: #007AFF;
	border-color: #007AFF;
}

.todo-content {
	flex: 1;
}

.todo-title {
	font-size: 30rpx;
	color: #333333;
	margin-bottom: 6rpx;
}

.todo-title.completed {
	text-decoration: line-through;
	color: #8f8f94;
}

.todo-time {
	font-size: 24rpx;
	color: #8f8f94;
}

.todo-priority {
	width: 10rpx;
	height: 40rpx;
	border-radius: 5rpx;
}

.priority-high {
	background-color: #FF2D55;
}

.priority-medium {
	background-color: #FF9500;
}

.priority-low {
	background-color: #34C759;
}

.life-record {
	margin-bottom: 30rpx;
}

.record-scroll {
	white-space: nowrap;
	margin-top: 20rpx;
}

.record-item {
	display: inline-block;
	width: 220rpx;
	margin-right: 20rpx;
}

.record-image {
	width: 220rpx;
	height: 220rpx;
	border-radius: 16rpx;
	margin-bottom: 10rpx;
}

.record-title {
	font-size: 28rpx;
	color: #333333;
	display: block;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.record-date {
	font-size: 24rpx;
	color: #8f8f94;
}

.health-grid {
	display: flex;
	flex-wrap: wrap;
	justify-content: space-between;
}

.health-item {
	width: 48%;
	display: flex;
	align-items: center;
	background-color: #f8f8f8;
	border-radius: 16rpx;
	padding: 20rpx;
	margin-bottom: 20rpx;
}

.health-icon {
	width: 60rpx;
	height: 60rpx;
	border-radius: 16rpx;
	display: flex;
	justify-content: center;
	align-items: center;
	margin-right: 20rpx;
}

.health-info {
	flex: 1;
}

.health-value {
	font-size: 32rpx;
	font-weight: bold;
	color: #333333;
	margin-bottom: 4rpx;
}

.health-label {
	font-size: 24rpx;
	color: #8f8f94;
}

.add-record-btn {
	position: fixed;
	bottom: 60rpx;
	right: 40rpx;
	width: 200rpx;
	height: 80rpx;
	background: linear-gradient(to right, #007AFF, #5856D6);
	border-radius: 40rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	box-shadow: 0 4rpx 16rpx rgba(0, 122, 255, 0.3);
}

.add-record-text {
	color: #ffffff;
	font-size: 28rpx;
	margin-left: 10rpx;
}
</style>