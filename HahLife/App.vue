<template>
	<view>
		<view class="container">
			<router-view></router-view>
		</view>
	</view>
</template>

<script>
export default {
	onLaunch: function() {
		console.log('App Launch')
		// 检查本地存储中是否存在用户信息
		const userInfo = uni.getStorageSync('userInfo');
		if (!userInfo) {
			// 如果没有存储用户信息，则跳转到登录页
			uni.reLaunch({
				url: '/pages/login/login'
			});
		}
	},
	onShow: function() {
		console.log('App Show')
	},
	onHide: function() {
		console.log('App Hide')
	}
}
</script>

<style lang="scss">
	/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
	@import "./static/iconfont.css";
	page{
		width: 100vw;
		background-color: #e4f2fd;
	}
	/* 卡片样式 */
	.su-card-normal {
		background: #fff;
		border-radius: 16rpx;
		padding: 20rpx;
	}
	.flex {
		display: flex;
		align-items: center;
	}
	.flex-between {
		display: flex;
		justify-content: space-between;
		align-items: center;
	}
	.align-center {
		display: flex;
		align-items: center;
	}
	.mb-10 {
		margin-bottom: 20rpx;
	}
	.mr-10 {
		margin-right: 20rpx;
	}
	.pd-20 {
		padding: 20rpx;
	}
	.pd-lr20 {
		padding: 0 20rpx;
	}
	.pd-tb20 {
		padding: 20rpx 0;
	}
	.border-b{
		border-bottom: 2rpx solid #f8f8f8 !important;
	}
	/* 字体样式 */
	.font-12 {
		font-size: 20rpx;
	}
	.font-14 {
		font-size: 28rpx;
	}
</style>

