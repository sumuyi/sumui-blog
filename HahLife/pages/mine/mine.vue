<template>
	<view class="mine-container">
		<!-- 顶部用户信息 -->
		<view class="user-info flex-between">
			<image class="avatar" src="../../static/wechat.png" mode="aspectFill" @tap="goToEditInfo"></image>
			<view>
				<view class="username" @tap="goToEditInfo">太阳是我吗</view>
				<view class="user-description">请写个人信息更容易被别人关注哦~</view>
			</view>
		</view>

		<!-- 功能列表 -->
		<view class="function-list">
			<view class="function-item">
				<text class="iconfont icon-fabiao"></text>
				<text>我的发布</text>
				<text class="iconfont icon-arrow-right"></text>
			</view>
			<view class="function-item">
				<text class="iconfont icon-huida"></text>
				<text>我的回答</text>
				<text class="iconfont icon-arrow-right"></text>
			</view>
			<view class="function-item">
				<text class="iconfont icon-huati"></text>
				<text>我的话题</text>
				<text class="iconfont icon-arrow-right"></text>
			</view>
			<view class="function-item">
				<text class="iconfont icon-jifen"></text>
				<text>积分</text>
				<text>2014</text>
				<text class="iconfont icon-arrow-right"></text>
			</view>
			<view class="function-item">
				<text class="iconfont icon-tongzhi"></text>
				<text>通知</text>
				<text class="notification-count">40</text>
				<text class="iconfont icon-arrow-right"></text>
			</view>
			<view class="function-item">
				<text class="iconfont icon-shoucang"></text>
				<text>我的收藏</text>
				<text>50</text>
				<text class="iconfont icon-arrow-right"></text>
			</view>
			<view class="function-item">
				<text class="iconfont icon-shezhi"></text>
				<text>设置</text>
				<text class="iconfont icon-arrow-right"></text>
			</view>
			<view class="function-item">
				<text class="iconfont icon-fankui"></text>
				<text>帮助与反馈</text>
				<text class="iconfont icon-arrow-right"></text>
			</view>
		</view>
	</view>
</template>

<script setup>
const goToEditInfo = () => {
	uni.navigateTo({
		url: '/pages/mine/edit'
	})
}
</script>

<style>
.mine-container {
	padding: 20rpx;
}

.user-info {
	background-color: #fff;
	margin-bottom: 20px;
	border-radius: 16rpx;
	padding: 20rpx;
}

.info-item {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 15px 20px;
	border-bottom: 1px solid #f0f0f0;
}

.item-label {
	font-size: 16px;
	color: #333;
}

.item-content {
	display: flex;
	align-items: center;
}

.avatar {
	width: 60px;
	height: 60px;
	border-radius: 50%;
	margin-right: 10px;
}

.icon-arrow-right {
	color: #999;
	font-size: 16px;
}

.username {
	font-size: 18px;
	font-weight: bold;
	margin-bottom: 5px;
}

.user-description {
	font-size: 14px;
	color: #999;
}

.user-stats {
	display: flex;
	justify-content: space-around;
	margin-bottom: 20px;
}

.stat-item {
	display: flex;
	flex-direction: column;
	align-items: center;
}

.stat-value {
	font-size: 18px;
	font-weight: bold;
}

.stat-label {
	font-size: 12px;
	color: #999;
}

.vip-button {
	background-color: #ff69b4;
	color: white;
	border: none;
	border-radius: 20px;
	padding: 10px;
	margin-bottom: 20px;
}

.function-list {
	background-color: #fff;
	border-radius: 10px;
}

.function-item {
	display: flex;
	align-items: center;
	padding: 15px 10px;
	border-bottom: 1px solid #f0f0f0;
}

.function-item:last-child {
	border-bottom: none;
}

.iconfont {
	margin-right: 10px;
}

.icon-arrow-right {
	margin-left: auto;
}

.notification-count {
	background-color: #ff0000;
	color: white;
	border-radius: 50%;
	padding: 2px 6px;
	font-size: 12px;
	margin-left: auto;
	margin-right: 10px;
}
</style>
