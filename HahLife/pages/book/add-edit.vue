<template>
	<view class="add-edit-book">
		<!-- 表单内容 -->
		<view class="form-content">
			<!-- 封面图片选择 -->
			<view class="form-item">
				<text class="item-label">账本封面</text>
				<view class="item-content" @click="chooseCover">
					<image class="cover-image" :src="bookForm.coverImage" mode="aspectFill"></image>
					<uv-icon name="arrow-right" color="#ccc" size="16"></uv-icon>
				</view>
			</view>

			<!-- 账本名称 -->
			<view class="form-item">
				<text class="item-label">账本名称</text>
				<view class="item-content">
					<input class="input-field" type="text" v-model="bookForm.name" placeholder="请输入账本名称" maxlength="20" placeholder-style="color: #ccc;" />
				</view>
			</view>

			<!-- 账本类型 -->
			<view class="form-item">
				<text class="item-label">账本类型</text>
				<view class="item-content" @click="openBookTypePopup">
					<text class="type-text">{{ bookTypeMap[bookForm.type] }}</text>
					<uv-icon name="arrow-right" color="#ccc" size="16"></uv-icon>
				</view>
			</view>

			<!-- 预算设置 -->
			<view class="form-item">
				<text class="item-label">预算金额</text>
				<view class="item-content">
					<input class="input-field" type="digit" v-model="bookForm.budgetAmount" placeholder="请输入预算金额（可选）"
						placeholder-style="color: #ccc;" />
				</view>
			</view>

			<!-- 账本描述 -->
			<view class="form-item">
				<text class="item-label">账本描述</text>
				<view class="item-content">
					<input class="input-field" type="text" v-model="bookForm.description" placeholder="请输入账本描述（可选）"
						maxlength="100" placeholder-style="color: #ccc;" />
				</view>
			</view>

			<!-- 设为默认账本 -->
			<view class="form-item">
				<text class="item-label">设为默认账本</text>
				<view class="item-content">
					<switch :checked="bookForm.isDefault" @change="onSwitchChange" color="#5cbb89"
						style="transform: scale(0.8);" />
				</view>
			</view>
		</view>

		<!-- 保存按钮 -->
		<!-- 删除按钮和保存按钮（编辑模式） -->
		<view class="action-section" :class="{ 'edit-mode': isEdit }">
		  <template v-if="isEdit">
		    <button class="action-btn delete-btn" @click="deleteBook">删除账本</button>
		    <button class="action-btn save-btn" @click="saveBook">保存</button>
		  </template>
		  <template v-else>
		    <button class="action-btn save-btn full-width" @click="saveBook">保存</button>
		  </template>
		</view>

		<!-- 账本封面选择器 -->
		<uv-popup ref="popupBookImageRef" mode="bottom" round="16">
			<view class="images-selector">
				<view class="selector-header">
					<text class="header-title">选择账本封面背景</text>
				</view>
				<view class="selector-content">
					<view class="image-grid">
						<view v-for="imgUrl in bookImagesList" :key="imgUrl" class="image-item"
							:class="{ active: bookForm.coverImage === imgUrl }" @click="selectBookImage(imgUrl)">
							<image class="cover-image" :src="'../..' + imgUrl" mode="aspectFill"></image>
							<view v-if="bookForm.coverImage === imgUrl" class="check-icon">
								<image class="cover-image" src="../../static/icon/common/selected.png" mode="aspectFill"></image>
							</view>
						</view>
					</view>
				</view>
			</view>
			<view class="upload-btn" @click="uploadImage">上传图片</view>
		</uv-popup>

		<!-- 账本类型选择器 -->
		<uv-popup ref="popupBookTypeRef" :close-on-click-overlay="false" mode="bottom" round="16">
			<view class="type-selector">
				<view class="selector-header">
					<text class="header-title">选择账本类型</text>
				</view>
				<view class="selector-content">
					<view v-for="(name, type) in bookTypeMap" :key="type" class="type-item"
						:class="{ active: bookTypeSelector === type }" @click="selectBookType(type)">
						<text>{{ name }}</text>
						<uv-icon v-if="bookTypeSelector === type" name="checkmark" color="#5cbb89" size="16"></uv-icon>
					</view>
				</view>
			</view>
			<view class="type-selector-footer">
				<view class="footer-btn" @click="cancelBookTypeSelection">取消</view>
				<view class="footer-btn confirm-btn" @click="confirmBookTypeSelection">确定</view>
			</view>
		</uv-popup>
	</view>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue';
import { useUserStore } from '@/store';
import { bookApi } from '@/api/books'
import config from '@/config'

// 使用 Pinia store
const bookStore = useUserStore();

// 账本类型映射
const bookTypeMap = {
	personal: '个人账本',
	family: '家庭账本',
	travel: '旅行账本',
	etc: '其他账本'
};

// 类型选择器显示状态
const bookTypeSelector = ref(false);
const popupBookTypeRef = ref(false);
const openBookTypePopup = async () => {
	await nextTick();
	if (popupBookTypeRef.value) {
		bookTypeSelector.value = bookForm.value.type;
		popupBookTypeRef.value.open();
	}
};

// 选择账本类型
const selectBookType = (type) => {
	bookTypeSelector.value = type;
};
// 确认选择账本类型
const confirmBookTypeSelection = () => {
	bookForm.value.type = bookTypeSelector.value;
	cancelBookTypeSelection();
};
// 取消选择账本类型
const cancelBookTypeSelection = () => {
	popupBookTypeRef.value.close();
};

// 获取路由参数
const bookId = ref(null);
onMounted(() => {
	const pages = getCurrentPages();
	const currentPage = pages[pages.length - 1];
	const options = currentPage.options;

	if (options && options.id) {
		bookId.value = options.id;
		// 如果是编辑模式，获取账本信息
		const bookInfo = bookStore.bookList.find(book => book.id === bookId.value);
		if (bookInfo) {
			bookForm.value = {
				id: bookInfo.id,
				name: bookInfo.name,
				type: bookInfo.type || 'personal',
				coverImage: bookInfo.coverImage,
				budgetAmount: bookInfo.budgetAmount || '',
				description: bookInfo.description || '',
				isDefault: bookInfo.id === bookStore.currentBook.id
			};
		}
	}
});

// 判断是否为编辑模式
const isEdit = computed(() => !!bookId.value);

// 表单数据
const bookForm = ref({
	name: '',
	type: 'personal',
	coverImage: '',
	budgetAmount: '',
	description: '',
	isDefault: false
});

// 返回上一页
const goBack = () => {
	uni.navigateBack();
};

// 选择封面图片
const bookImagesList = [
	"/static/books/book1.png",
	"/static/books/book2.png",
	"/static/books/book3.png",
	"/static/books/book4.png",
	"/static/books/book5.png",
	"/static/books/book6.png"
];
const popupBookImageRef = ref(false);
const selectedImage = ref('');

const chooseCover = async () => {
	await nextTick();
	if (popupBookImageRef.value) {
		selectedImage.value = bookForm.value.coverImage;
		popupBookImageRef.value.open();
	}
};
const selectBookImage = (imgUrl) => {
	bookForm.value.coverImage = imgUrl;
	popupBookImageRef.value.close();
};
const uploadImage = () => {
	uni.showLoading({
	    title: '上传中',
		mask: true
	})
	uni.chooseImage({
		count: 1,
		sizeType: ['compressed'],
		sourceType: ['album', 'camera'],
		success: (res) => {
			// 获取选择的图片临时路径
			const tempFilePath = res.tempFilePaths[0];
			
			// 上传图片到服务器
			uni.uploadFile({
				url: config[process.env.NODE_ENV].baseURL + '/api/files/upload/image',
				filePath: tempFilePath,
				name: 'file',
				header: {
					'content-type': 'application/json',
					'Sa-token': uni.getStorageSync('tokenValue')
				},
				formData: {
					type: 'book'
				},
				success: (uploadRes) => {
					const data = JSON.parse(uploadRes.data);
					if (data.code === 200 && data.result) {
						// 设置服务器返回的图片URL
						bookForm.value.coverImage = data.result;
						uni.showToast({
							title: '上传成功',
							icon: 'success'
						});
					} else {
						uni.showToast({
							title: data.message || '上传失败',
							icon: 'none'
						});
					}
				},
				fail: (err) => {
					console.error('上传失败:', err);
					uni.showToast({
						title: '上传失败',
						icon: 'none'
					});
				},
				complete: () => {
					popupBookImageRef.value.close();
					uni.hideLoading();
				}
			});
		},
		fail:(fail)=>{
			uni.hideLoading();
			uni.showToast({
				title: '选择图片失败',
				icon: 'none'
			});
		},
	});
}

// 切换默认账本
const onSwitchChange = (e) => {
	bookForm.value.isDefault = e.detail.value;
};

// 保存账本
const saveBook = async () => {
	// 表单验证
	if (!bookForm.value.name.trim()) {
		uni.showToast({
			title: '请输入账本名称',
			icon: 'none'
		});
		return;
	}

	// 构建保存的数据
	const saveData = {
		id: bookId.value,
		name: bookForm.value.name,
		type: bookForm.value.type,
		coverImage: bookForm.value.coverImage,
		budgetAmount: bookForm.value.budgetAmount ? Number(bookForm.value.budgetAmount) : null,
		description: bookForm.value.description
	};

	// 保存逻辑（这里简单模拟，实际应调用API）
	uni.showLoading({
		title: isEdit.value ? '更新中' : '添加中',
		mask: true
	});
	try {
		let res = await bookApi.addBook(saveData);
		console.log('res:', res);

		// 添加新账本
		if (!saveData.id) {
			saveData.id = res
			bookStore.bookList.push(saveData);
		} else {
			// 更新账本 - 直接修改对应的账本信息，而不是重新添加
			const index = bookStore.bookList.findIndex(book => book.id === saveData.id);
			if (index !== -1) {
				bookStore.bookList.splice(index, 1, saveData);
			}
		}
		
		// 如果设为默认账本
		if (bookForm.value.isDefault) {
			bookStore.currentBook = saveData;
		}

		uni.showToast({
			title: isEdit.value ? '账本已更新' : '账本已添加',
			icon: 'success',
			success: () => {
			setTimeout(() => {
				uni.navigateBack();
			}, 1500);
			}
		});
	} catch (error) {
		console.log('error:', error);
		uni.showToast({
			title: '保存失败',
			icon: 'none'
		});
	} finally {
		uni.hideLoading();
	}
};

// 删除账本
const deleteBook = () => {
	uni.showModal({
		title: '确认删除',
		content: '删除后无法恢复，确定要删除该账本吗？',
		confirmColor: '#ff6b6b',
		success: (res) => {
			if (res.confirm) {
				// 删除逻辑
				const index = bookStore.bookList.findIndex(book => book.id === bookId.value);
				if (index !== -1) {
					bookStore.bookList.splice(index, 1);

					// 如果删除的是当前选中的账本，则选择第一个账本作为当前账本
					if (bookId.value === bookStore.currentBook.id && bookStore.bookList.length > 0) {
						bookStore.currentBook = bookStore.bookList[0];
					}

					uni.showToast({
						title: '账本已删除',
						icon: 'success',
						success: () => {
							setTimeout(() => {
								uni.navigateBack();
							}, 1500);
						}
					});
				}
			}
		}
	});
};
</script>

<style lang="scss" scoped>
.add-edit-book {
	min-height: 100vh;
	background-color: #f8f8f8;
	padding-bottom: 40rpx;
}

.form-content {
	background-color: #fff;
	margin-top: 20rpx;

	.form-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 30rpx;
		border-bottom: 1rpx solid #f0f0f0;

		.item-label {
			font-size: 28rpx;
			color: #333;
			flex-shrink: 0;
		}

		.item-content {
			flex: 1;
			display: flex;
			justify-content: flex-end;
			align-items: center;

			.input-field {
				text-align: right;
				font-size: 28rpx;
				color: #333;
				width: 100%;
			}

			.cover-image {
				width: 80rpx;
				height: 80rpx;
				border-radius: 8rpx;
				margin-right: 10rpx;
			}

			.type-text {
				font-size: 28rpx;
				color: #333;
				margin-right: 10rpx;
			}
		}
	}
}

// 类型选择器样式
.type-selector {
	background-color: #fff;
	border-radius: 20rpx 20rpx 0 0;
	overflow: hidden;

	.selector-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 40rpx;
		border-bottom: 1rpx solid #f0f0f0;

		.header-title {
			font-size: 32rpx;
			font-weight: bold;
			color: #333;
		}

		.close-btn {
			padding: 10rpx;
		}
	}

	.selector-content {
		padding: 20rpx 0;
		max-height: 600rpx;

		.type-item {
			display: flex;
			justify-content: space-between;
			align-items: center;
			padding: 40rpx;
			font-size: 30rpx;
			color: #333;

			&.active {
				color: #1890ff;
			}
		}
	}
}

.action-section {
  padding: 30rpx;
  margin-top: 30rpx;
  
  &.edit-mode {
    display: flex;
    justify-content: space-between;
    
    .action-btn {
      flex: 0 0 calc(50% - 15rpx);
    }
  }

  .action-btn {
    height: 90rpx;
    line-height: 90rpx;
    border-radius: 45rpx;
    font-size: 32rpx;
    
    &.full-width {
      width: 100%;
    }
  }

  .delete-btn {
    background-color: #fff;
    color: #ff6b6b;
    border: 1rpx solid #ff6b6b;
  }

  .save-btn {
    background-color: #1890ff;
    color: #fff;
    border: none;
  }
}

.type-selector-footer {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 40rpx 20rpx;

	.footer-btn {
		flex: 0 0 calc(50% - 20rpx);
		height: 90rpx;
		line-height: 90rpx;
		text-align: center;
		font-size: 32rpx;
		background-color: #e9ebed;
		
		border-radius: 40rpx;
		
		&.confirm-btn {
			background-color: #1890ff;
			color: #fff;
		}
	}
}

// 图片选择器样式
.images-selector {
	background-color: #fff;
	border-radius: 20rpx 20rpx 0 0;
	overflow: hidden;
	padding-bottom: 20rpx;

	.selector-header {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 40rpx;
		border-bottom: 1rpx solid #f0f0f0;

		.header-title {
			font-size: 32rpx;
			font-weight: bold;
			color: #333;
		}
	}

	.selector-content {
		padding: 20rpx;
		
		.image-grid {
			display: flex;
			flex-wrap: wrap;
			justify-content: space-between;
			
			.image-item {
				width: calc((100% - 40rpx) / 3); // 每行3张，减去间距
				margin-bottom: 20rpx;
				position: relative;
				border-radius: 12rpx;
				overflow: hidden;
				
				&::before {
					content: '';
					display: block;
					padding-top: 133.33%; // 3:4比例
				}
				
				.cover-image {
					position: absolute;
					top: 0;
					left: 0;
					width: 100%;
					height: 100%;
					object-fit: cover;
				}
				
				&.active {
				
					.check-icon {
						position: absolute;
						top: 50%;
						left: 50%;
						transform: translate(-50%, -50%);
						width: 60rpx;
						height: 60rpx;
						z-index: 2;
						
						image {
							width: 100%;
							height: 100%;
						}
					}
				}
			}
		}
	}
}
.upload-btn {
	margin: 0 40rpx;
	padding: 20rpx;
	line-height: 40rpx;
	border-radius: 16rpx;
	background-color: #1890ff;
	color: #fff;
	text-align: center;
}
</style>
