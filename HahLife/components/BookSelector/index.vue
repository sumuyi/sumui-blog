<template>
	<view class="book-selector">
		<!-- 触发器部分 -->
		<view class="book-trigger" @click="showPopupBookList">
			<text class="book-name">{{ currentBook.name || '请选择账本' }}</text>
			<uv-icon name="arrow-down" size="14"></uv-icon>
		</view>

		<!-- 弹出层部分 -->
		<uv-popup ref="pickerBookRef" :close-on-click-overlay="false" mode="bottom" round="16">
			<view class="popup-content">
				<view class="popup-header">
					<text class="popup-title">选择账本</text>
				</view>

				<!-- 管理账本按钮 -->
				<view class="manage-link">
					<text @click="manageBooks">管理账本</text>
				</view>

				<!-- 账本列表 -->
				<view class="book-grid">
					<view v-for="(book) in bookList" :key="book.id" class="book-card"
						:class="{ 'active': currentBook.id === book.id }" @click="selectBook(book)">
						<image class="book-image" src="../../static/components/image.png" mode="aspectFill"></image>
						<text class="book-card-name">{{ book.name }}</text>
					</view>
				</view>

				<!-- 底部按钮 -->
				<view class="bottom-button">
					<button class="view-all-btn" @click="viewAllBooks">查看所有账本汇总数据</button>
				</view>
			</view>
		</uv-popup>
	</view>
</template>

<script setup>
import { ref, defineProps, defineEmits, nextTick, onMounted } from 'vue';

const props = defineProps({
	// 当前选中的账本
	value: {
		type: Object,
		default: () => ({})
	},
	// 账本列表
	books: {
		type: Array,
		default: () => []
	}
});

const emit = defineEmits(['update:value', 'change']);

// 当前选中的账本
const currentBook = ref(props.value || {});
// 账本列表
const bookList = ref(props.books || []);
// 是否显示账本列表弹窗
const showBookList = ref(false);

// 打开账本列表
const pickerBookRef = ref(null)
const showPopupBookList = async () => {
	await nextTick()
	if (pickerBookRef.value) {
		console.log('当前账本', currentBook.value);
		pickerBookRef.value.open()
	}
}

// 获取随机图片
const randomUrl = ref('');
const randomImage = () => {
	return new Promise((resolve, reject) => {
		uni.request({
			url: 'https://www.dmoe.cc/random.php?return=json',
			method: 'GET',
			success: (res) => {
				console.log('获取随机图片', res);
				if (res.data.code === 200) {
					resolve(res.data.imgurl);
				} else {
					resolve();
				}
			},
			fail: () => {
				resolve();
			}
		});
	});
}

// 关闭弹窗
const closePopup = () => {
	if (pickerBookRef.value) {
		pickerBookRef.value.close()
	}
};

// 选择账本
const selectBook = (book) => {
	currentBook.value = book;
	emit('update:value', book);
	emit('change', book);
	closePopup();
};

// 查看所有账本汇总数据
const viewAllBooks = () => {
	console.log('查看所有账本汇总数据');
	uni.showToast({
		title: '查看所有账本汇总数据',
		icon: 'none'
	});
	// 这里可以添加跳转到汇总页面的逻辑
};

// 管理账本
const manageBooks = () => {
	console.log('管理账本');
	uni.navigateTo({
		url: '/pages/book/book'
	});
};

// 初始化时也处理一次
onMounted(() => {
	//   randomUrl.value = randomImage();
});
</script>

<style lang="scss" scoped>
.book-selector {
	display: flex;
	align-items: center;
}

.book-trigger {
	display: flex;
	align-items: center;
	padding: 8rpx 16rpx;
	background-color: #f5f5f5;
	border-radius: 8rpx;

	.book-name {
		margin-right: 8rpx;
		font-size: 28rpx;
		color: #333;
	}
}

.popup-content {
	padding: 30rpx;
	background-color: #ffffff;

	.popup-header {
		margin-bottom: 30rpx;
		text-align: center;

		.popup-title {
			font-size: 32rpx;
			font-weight: bold;
			color: #333;
		}
	}

	.book-grid {
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
		padding: 10rpx 0;

		.book-card {
			width: 30%;
			margin-bottom: 20rpx;
			display: flex;
			flex-direction: column;
			align-items: center;

			&.active {
				.book-image {
					border: 4rpx solid #1890ff;
				}

				.book-card-name {
					color: #1890ff;
				}
			}

			.book-image {
				width: 146rpx;
				height: 180rpx;
				border-radius: 16rpx;
				margin-bottom: 10rpx;
				border: 4rpx solid transparent;
				box-sizing: border-box;
			}

			.book-card-name {
				font-size: 28rpx;
				color: #333;
				text-align: center;
				width: 100%;
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
			}
		}
	}

	.bottom-button {
		margin-top: 30rpx;

		.view-all-btn {
			width: 100%;
			height: 80rpx;
			line-height: 80rpx;
			text-align: center;
			background-color: #1890ff;
			color: #fff;
			border-radius: 40rpx;
			font-size: 28rpx;
			border: none;
		}
	}

	.manage-link {
		text-align: center;
		position: absolute;
		right: 30rpx;
		top: 25rpx;

		text {
			font-size: 26rpx;
			color: #1890ff;
			text-decoration: none;
		}
	}
}
</style>