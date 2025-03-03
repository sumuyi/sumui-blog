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

const userInfo = ref({
  avatarUrl: '',
  nickName: '今天周末呀'
});

// 选择头像回调
const onChooseAvatar = (e) => {
  // 获取临时文件路径
  const tempFilePath = e.detail.avatarUrl;
  
  // 使用uni.uploadFile上传到服务器或保存到本地
  // 这里先简单地将临时文件保存到本地
  uni.saveFile({
    tempFilePath: tempFilePath,
    success: function(res) {
      const savedFilePath = res.savedFilePath;
      console.log('保存成功，文件路径：', savedFilePath);
      
      // 更新头像URL
      userInfo.value.avatarUrl = savedFilePath;
      
      // 保存到本地存储
      uni.setStorageSync('userAvatarUrl', savedFilePath);
    },
    fail: function(err) {
      console.error('保存文件失败：', err);
      
      // 如果保存失败，可以尝试直接使用临时路径
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