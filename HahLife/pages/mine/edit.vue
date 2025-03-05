<template>
	<view class="edit-info">
		<view class="user-info">
			<view class="info-item">
				<text class="item-label">头像</text>
				<view class="item-content">
					<button class="avatar-button" open-type="chooseAvatar" @chooseavatar="onChooseAvatar">
						<view class="flex">
							<image class="avatar" :src="userInfo.avatarUrl || '../../static/wechat.png'" mode="aspectFill"></image>
							<uv-icon name="arrow-right"></uv-icon>
						</view>
					</button>
				</view>
			</view>
			<view class="info-item">
				<text class="item-label">昵称</text>
				<view class="item-content">
					<input type="nickname" class="nickname-input" placeholder="请输入昵称" :value="userInfo.nickName" @change="onInputNickname" />
					<uv-icon name="arrow-right"></uv-icon>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup>
import { ref } from 'vue';
import config from '@/config'

const baseURL = config[process.env.NODE_ENV].baseURL

const userInfo = ref({
  avatarUrl: '',
  nickName: '今天周末呀'
});

// 选择头像回调
const onChooseAvatar = (e) => {
  // 获取临时文件路径
  const tempFilePath = e.detail.avatarUrl;

	// 显示上传中提示
	uni.showLoading({
		title: '上传中...'
	});
  
	// 上传文件到服务器
	uni.uploadFile({
		url: baseURL + '/api/files/upload/avatar', // 替换为你的服务器上传接口
		filePath: tempFilePath,
		name: 'file',// 服务器接收文件的字段名
		formData: {
			'userId': uni.getStorageSync('userId') || '', // 可以添加用户ID等额外信息
			'type': 'avatar'
		},
		success: (uploadRes) => {
			uni.hideLoading();
			console.log('uploadRes', uploadRes);
			

			// 解析服务器返回的数据
			let data;
			try {
				data = JSON.parse(uploadRes.data);
			} catch (e) {
				console.error('解析响应数据失败:', e);
				data = uploadRes.data;
			}

			// 判断上传是否成功
			if (data.code === 200) { // 根据你的服务器返回格式调整
				// 获取服务器返回的图片URL
				const avatarUrl = data.result;

				// 更新头像URL
				userInfo.value.avatarUrl = avatarUrl;

				// 保存到本地存储
				uni.setStorageSync('userAvatarUrl', avatarUrl);

				uni.showToast({
					title: '头像上传成功',
					icon: 'success'
				});
			} else {
				// 未授权访问
				if (data.status.code === 401) {
					// 清除本地缓存，并跳转到登录页
					uni.clearStorageSync();
					uni.redirectTo({
						url: '/pages/login/login'
					})
					return
				}
				uni.showToast({
					title: '上传失败：' + (data.msg || '未知错误'),
					icon: 'none'
				});
			}
		},
		fail: (err) => {
			uni.hideLoading();
			console.error('上传文件失败：', err);

			uni.showToast({
				title: '上传失败，请重试',
				icon: 'none'
			});

			// 如果上传失败，可以尝试直接使用临时路径
			userInfo.value.avatarUrl = tempFilePath;
			uni.setStorageSync('userAvatarUrl', tempFilePath);
		}
	});
};

// 输入昵称回调
const onInputNickname = (e) => {
  const nickName = e.detail.value;
  userInfo.value.nickName = nickName;
  
  // 可以在这里将昵称保存到本地或上传到服务器
  uni.setStorageSync('userNickName', nickName);
};

// 页面加载时获取本地存储的用户信息
uni.getStorage({
  key: 'userAvatarUrl',
  success: (res) => {
    userInfo.value.avatarUrl = res.data;
  }
});

uni.getStorage({
  key: 'userNickName',
  success: (res) => {
    userInfo.value.nickName = res.data;
  }
});
</script>

<style>
.user-info {
	background-color: #fff;
	margin-bottom: 40rpx;
}

.info-item {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 20rpx 40rpx;
	border-bottom: 2rpx solid #f0f0f0;
}

.item-label {
	font-size: 32rpx;
	color: #333;
}

.item-content {
	display: flex;
	align-items: center;
}

.avatar {
	width: 50rpx;
	height: 50rpx;
	border-radius: 16rpx;
	margin-right: 20rpx;
}

.icon-arrow-right {
	color: #999;
	font-size: 32rpx;
}

/* 新增样式 */
.avatar-button {
	background: none;
	border: none;
	padding: 0;
	margin: 0;
	line-height: normal;
}

.avatar-button::after {
	border: none;
}

.nickname-input {
	text-align: right;
	margin-right: 10rpx;
}
</style>