<template>
	<view class="book-manage">
	  <!-- 账本列表 -->
	  <view class="book-list">
		<view v-for="(book, index) in bookList" :key="book.id" class="book-card">
		  <!-- 常用标签 -->
		  <view v-if="index === 0" class="tag">常用</view>
		  
		  <!-- 账本信息 -->
		  <view class="book-content">
			<image class="book-image" :src="book.image || '../../static/components/image.png'" mode="aspectFill"></image>
			<view class="book-info">
			  <text class="book-name">{{ book.name }}</text>
			  <text class="book-stats">累计:{{ book.count }}笔</text>
			  <text class="book-balance">结余:{{ book.balance }}</text>
			</view>
		  </view>
		  
		  <!-- 编辑按钮 -->
		  <view class="edit-icon" @click="editBook(book)">
			<uv-icon name="edit-pen" color="#999" size="20"></uv-icon>
		  </view>
		</view>
		
		<!-- 添加新账本 -->
		<view class="add-book-card" @click="addNewBook">
		  <view class="add-icon">
			<uv-icon name="plus" color="#ddd" size="40"></uv-icon>
		  </view>
		  <text class="add-text">新增账本</text>
		</view>
	  </view>
	</view>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import { useUserStore } from '@/store';
  
   // 使用 Pinia store
   const bookStore = useUserStore();
  // 模拟账本数据
  const bookList = ref([]);
  
  // 返回上一页
  const goBack = () => {
	uni.navigateBack();
  };
  
  // 编辑账本
  const editBook = (book) => {
	console.log('编辑账本', book);
	uni.navigateTo({
	  url: `/pages/finance/book/edit?id=${book.id}`
	});
  };
  
  // 添加新账本
  const addNewBook = () => {
	console.log('添加新账本');
	uni.navigateTo({
	  url: '/pages/finance/book/add'
	});
  };
  
  // 页面加载时获取账本列表
  onMounted(() => {
	// 这里可以添加获取账本列表的API调用
	bookList.value = bookStore.bookList;
	console.log('获取账本列表:', bookList.value);
  });
  </script>
  
  <style lang="scss" scoped>
  .book-manage {
	min-height: 100vh;
	background-color: #f8f8f8;
	padding-bottom: 30rpx;
  }
  
  .nav-bar {
	position: relative;
	display: flex;
	align-items: center;
	justify-content: center;
	height: 90rpx;
	background-color: #fff;
	padding: 0 30rpx;
	
	.back-icon {
	  position: absolute;
	  left: 30rpx;
	}
	
	.page-title {
	  font-size: 34rpx;
	  font-weight: bold;
	  color: #333;
	}
  }
  
  .book-list {
	padding: 30rpx;
	
	.book-card {
	  position: relative;
	  display: flex;
	  align-items: center;
	  background-color: #fff;
	  border-radius: 16rpx;
	  padding: 30rpx;
	  margin-bottom: 30rpx;
	  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
	  
	  .tag {
		position: absolute;
		top: 0;
		left: 0;
		background-color: #ff6b6b;
		color: #fff;
		font-size: 22rpx;
		padding: 4rpx 12rpx;
		border-radius: 12rpx 0 12rpx 0;
	  }
	  
	  .book-content {
		display: flex;
		flex: 1;
		
		.book-image {
		  width: 120rpx;
		  height: 150rpx;
		  border-radius: 12rpx;
		  margin-right: 20rpx;
		}
		
		.book-info {
		  display: flex;
		  flex-direction: column;
		  justify-content: space-between;
		  
		  .book-name {
			font-size: 32rpx;
			font-weight: bold;
			color: #333;
			margin-bottom: 10rpx;
		  }
		  
		  .book-stats, .book-balance {
			font-size: 26rpx;
			color: #999;
		  }
		}
	  }
	  
	  .edit-icon {
		padding: 10rpx;
	  }
	}
	
	.add-book-card {
	  display: flex;
	  flex-direction: column;
	  align-items: center;
	  justify-content: center;
	  background-color: #fff;
	  border-radius: 16rpx;
	  padding: 40rpx;
	  border: 2rpx dashed #ddd;
	  
	  .add-icon {
		margin-bottom: 20rpx;
	  }
	  
	  .add-text {
		font-size: 28rpx;
		color: #999;
	  }
	}
  }
  </style>