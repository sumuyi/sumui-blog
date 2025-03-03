<template>
	<view class="page-container">
		<!-- 头部 -->
    <view class="flex-between">
      <view class="current-book-name flex-between" @click="showPopupBookList">
        {{ currentBookName }}
        <uv-icon name="arrow-down-fill" color="black"></uv-icon>
      </view>
      <view class="current-month-str flex-between" @click="showPopupDate">
        {{ currentMonthStr }}
        <uv-icon name="arrow-down-fill" color="black"></uv-icon>
      </view>
    </view>
		<!-- 顶部统计栏 -->
    <view class="flex-between">
      <view class="statistic-card pd-tb20">
        <uv-text type="info" text="总支出" size="14"></uv-text>
        <uv-text :text="statistics.totalExpense || '0.00'" size="24"></uv-text>
        <!-- <uv-count-to :startVal="0" :endVal="statistics.totalExpense" :decimals="2" decimal="." color="#cf1322" bold></uv-count-to> -->
      </view>
      <view class="statistic-card pd-tb20">
        <uv-text type="info" text="总收入" size="14"></uv-text>
        <uv-text :text="statistics.totalIncome || '0.00'" size="24"></uv-text>
        <!-- <uv-count-to :startVal="0" :endVal="statistics.totalIncome" :decimals="2" decimal="." color="#3f8600" bold></uv-count-to> -->
      </view>
      <view class="statistic-card pd-tb20">
        <uv-text type="info" text="结余" size="14"></uv-text>
        <uv-text :text="statistics.balance || '0.00'" size="24"></uv-text>
        <!-- <uv-count-to :startVal="0" :endVal="statistics.balance" :decimals="2" decimal="." bold></uv-count-to> -->
      </view>
    </view>
		<!-- 账单列表 -->
    <uv-alert type="info" title="账单列表"></uv-alert>
    <view class="su-card mb-10" v-for="group in billList" :key="group.date">
      <view class="su-card-header flex-between">
        <view class="date-info">
          <span class="date">{{ formatDate(group.date) }}</span>
          <span class="day-label">{{ getDayLabel(group.date) }}</span>
        </view>
        <view class="daily-total">
          <text>出 ￥{{ group.totalExpense.toFixed(2) }}</text>
          <text>入 ￥{{ group.totalIncome.toFixed(2) }}</text>
        </view>
      </view>
      <view class="su-card-body">
        <view class="su-card-normal mb-10" 
          v-for="(item, index) in group.items" 
          :key="index"
          @click="showBillDetail(item)"
        >
          <view class="flex-between font-14">
            <view class="bill-category">{{ item.category }}</view>
            <view class="bill-amount" :style="{ color: setColor(item.type) }">
              ￥{{ item.type == 1 ? '-' : '+' }}{{ item.amount.toFixed(2) }}
            </view>
          </view>
          <view class="bill-time font-12">{{ item.userName || '-' }} | {{ item.remark || ' 无备注' }}</view>
        </view>
      </view>
    </view>

    <!-- 月份弹框 -->
    <uv-popup ref="popupDate" mode="bottom" :close-on-click-overlay="false" round="16">
			<view class="month-select-container">
        <!-- 顶部标题 -->
        <uv-text text="请选择日期" bold align="center"></uv-text>
				<!-- 月份选择器 -->
        <view class="month-selector">
            <view v-for="(monthName, monthIndex) in monthMap" :key="monthIndex" class="month-item" :class="{ 'active': currentMonthIndex === monthIndex }" @click="selectMonth(monthIndex)">
                {{ monthName }}
            </view>
        </view>
        <!-- 底部操作按钮 取消 确定 -->
        <view class="flex-between btn-box">
            <view class="btn cancel-btn" @click="cancel">取消</view>
            <view class="btn confirm-btn" @click="confirm">确定</view>
        </view>
			</view>
		</uv-popup>

    <!-- 账本弹框 -->
    <uv-picker ref="pickerBookRef" :columns="bookList" keyName="name" @confirm="confirmBook"></uv-picker>
		
    <view class="fixed-bottom-right-button">
      <uv-button icon="plus" @click="showPopupAddBill" shape="circle" color="#3c9cff" iconColor="#fff"></uv-button>
    </view>

    <!-- 添加账单详情弹框 -->
    <uv-popup ref="popupBillDetailRef" :close-on-click-overlay="false" mode="bottom" round="16">
      <view class="bill-detail">
        <!-- 标题和关闭按钮 -->
        <view class="detail-header">
          <text class="title">账单详情</text>
          <uv-icon name="close" @click="cancelDetail" size="20"></uv-icon>
        </view>
        
        <!-- 操作按钮组 -->
        <view class="action-group">
          <view class="action-item" @click="handleEdit">
            <uv-icon name="clock" size="24"></uv-icon>
            <text>统计</text>
          </view>
          <view class="action-item" @click="handleEdit">
            <uv-icon name="reload" color="#ff9900" size="24"></uv-icon>
            <text style="color: #ff9900;">退款</text>
          </view>
          <view class="action-item" @click="handleDelete">
            <uv-icon name="trash" color="#ff0000" size="24"></uv-icon>
            <text style="color: #ff0000;">删除</text>
          </view>
          <view class="action-item" @click="handleEdit">
            <uv-icon name="edit-pen" color="#2979ff" size="24"></uv-icon>
            <text style="color: #2979ff;">编辑</text>
          </view>
        </view>

        <!-- 详情内容 -->
        <view class="detail-content">
          <view class="detail-item">
            <text class="label">金额</text>
            <text class="value amount" :style="{ color: setColor(currentBill?.type) }">
              {{ currentBill?.type === 1 ? '-' : '+' }}{{ currentBill?.amount?.toFixed(2) }}
            </text>
          </view>
          <view class="detail-item">
            <text class="label">分类</text>
            <text class="value">{{ currentBill?.category }}</text>
          </view>
          <view class="detail-item">
            <text class="label">账本</text>
            <text class="value">{{ currentBill?.bookName }}</text>
          </view>
          <view class="detail-item">
            <text class="label">账户</text>
            <text class="value">现金钱包</text>
          </view>
          <view class="detail-item">
            <text class="label">标签</text>
            <text class="value">-</text>
          </view>
          <view class="detail-item">
            <text class="label">日期</text>
            <text class="value">{{ currentBill?.billDate }}</text>
          </view>
          <view class="detail-item">
            <text class="label">备注</text>
            <text class="value">{{ currentBill?.remark || '-' }}</text>
          </view>
        </view>
      </view>
    </uv-popup>

	</view>
</template>

<script setup>
import { onMounted, ref, nextTick } from 'vue'
import { onShow, onPullDownRefresh } from '@dcloudio/uni-app'  // 添加 onLoad 导入
import { billApi } from '@/api/bills'
import { bookApi } from '@/api/books'
import { timeFormat } from '@/uni_modules/uv-ui-tools/libs/function/index.js';

const setColor = type => {
	if(type == 1){
		return '#ff4d4f'
	} else if(type == 2){
		return '#52c41a'
	}else{
		return 'black'
	}
}
// 获取用户账本列表
const currentBookName = ref('默认账本')
const currentBookId = ref(null)
const bookList = ref([])
const getBookList = async () => {
  try {
    let response = await bookApi.getList()
    console.log('userBooks', response)
    bookList.value = [response]
    if (response && response.length > 0) {
      currentBookId.value = response[0].id
      currentBookName.value = response[0].name
      
      refreshList()
    }
  } catch (error) {
    console.error('获取账本列表失败', error)
  }
}
// 打开账本列表
const pickerBookRef = ref(null)
const showPopupBookList = async () => {
  await nextTick()
  if (pickerBookRef.value) {
    console.log('当前账本', currentBookName.value);
    pickerBookRef.value.open()
  }
}
// 确认选择账本
const confirmBook = (e) => {
  console.log('confirm', e);
  currentBookId.value = e.value[0].id
  currentBookName.value = e.value[0].name
  refreshList()
}

// 打开添加账单弹框
const showPopupAddBill = () => {
  console.log('点击了新增账单按钮');
  uni.navigateTo({
    url: '/pages/finance/addBill?id=1&name=uniapp'
  });
}

const refreshList = () => {
  getFinance()
  fetchStatistics()
}

const billList = ref([])
// 添加新的响应式变量
const statistics = ref({
  totalExpense: 0,
  totalIncome: 0,
  balance: 0
})
const currentMonth = ref(timeFormat(new Date().getTime(), "yyyy-mm"))
const currentMonthStr = ref(timeFormat(new Date().getTime(), "yyyy年mm月"))
// 月份 map
const monthMap = {
    1: '一月',
    2: '二月',
    3: '三月',
    4: '四月',
    5: '五月',
    6: '六月',
    7: '七月',
    8: '八月',
    9: '九月',
    10: '十月',
    11: '十一月',
    12: '十二月'
}
// 当前选中的月份索引
const currentMonthIndex = ref(new Date().getMonth() + 1)

// 添加新的响应式变量
const cates = uni.CommonJS.categories
// 获取账单数据
const getFinance = async () => {
	try {
    uni.showLoading({
      title: '加载中...',
      mask: true
    })
		let response = await billApi.getList(currentBookId.value, currentMonth.value)
		
    // 添加空值检查
    if (!response || response.length === 0) {
      billList.value = {}
      return
    }

    // 按日期分组处理数据
    const groupedBills = {}
    for (const [date, dayBills] of Object.entries(response)) {
      // 确保 dayBills 是数组
      if (!Array.isArray(dayBills)) continue

      const formattedDate = date.split('T')[0]
      groupedBills[formattedDate] = {
        date: formattedDate,
        totalExpense: dayBills.filter(b => b.type === 1).reduce((sum, b) => sum + (b.amount || 0), 0),
        totalIncome: dayBills.filter(b => b.type === 2).reduce((sum, b) => sum + (b.amount || 0), 0),
        items: dayBills.map(bill => ({
          id: bill.id,
          userId: bill.userId,
          userName: bill.userName,
          amount: bill.amount || 0,
          type: bill.type,
          categoryId: bill.categoryId,
          remark: bill.remark,
          category: cates.find(c => c.value == bill.categoryId)?.label || '其他',
          icon: cates.find(c => c.value == bill.categoryId)?.icon || 'su-icon-qita'
        }))
      }
    }
    billList.value = Object.values(groupedBills)
    uni.hideLoading()
	} catch (err) {
		console.error(err)
	}
}

// 新增获取统计数据的独立函数
const fetchStatistics = async () => {
  try {
    const statsResponse = await billApi.getStatistics(currentBookId.value, currentMonth.value)
    if (statsResponse) {
      statistics.value = {
        totalExpense: statsResponse.totalExpense || 0,
        totalIncome: statsResponse.totalIncome || 0,
        balance: (statsResponse.totalIncome || 0) - (statsResponse.totalExpense || 0)
      }
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
    statistics.value = { totalExpense: 0, totalIncome: 0, balance: 0 }
  }
}

// 格式化日期，转换为 "2月21日" 格式
const formatDate = (dateStr) => {
  const date = new Date(dateStr)
  return `${date.getMonth() + 1}月${date.getDate()}日`
}

// 获取日期标签（今天、昨天等）
const getDayLabel = (dateStr) => {
  const today = new Date()
  const targetDate = new Date(dateStr)

  // 重置时间部分，只比较日期
  today.setHours(0, 0, 0, 0)
  targetDate.setHours(0, 0, 0, 0)

  const diffDays = Math.floor((today - targetDate) / (1000 * 60 * 60 * 24))

  if (diffDays === 0) return '今天'
  if (diffDays === 1) return '昨天'
  if (diffDays === 2) return '前天'
  return ''
}

// 显示日期选择器
const popupDate = ref(null)
const showPopupDate = async () => {
  await nextTick()
  if (popupDate.value) {
    console.log('当前月份', currentMonthIndex.value);
    popupDate.value.open()
  }
}
// 日期选择器回调函数
const cancel = () => {
  console.log('cancel');
  popupDate.value.close()
}
const confirm = () => {
  console.log('confirm');
  currentMonth.value = new Date().getFullYear() + '-' + (currentMonthIndex.value < 10 ? '0' + currentMonthIndex.value : currentMonthIndex.value)
  currentMonthStr.value = new Date().getFullYear() + '年' + (currentMonthIndex.value < 10? '0' + currentMonthIndex.value : currentMonthIndex.value) + '月'
  refreshList()
  popupDate.value.close()
}

// 选择月份
const selectMonth = (index) => {
  console.log('选中了', index);
  
  currentMonthIndex.value = index
  // 可以在这里添加更新数据的逻辑
}

onPullDownRefresh(() => {
  console.log('refresh');
  setTimeout(function () {
    refreshList()
    uni.stopPullDownRefresh();
  }, 1000);
}) 

onShow(() => {
  console.log('------onShow');
  uni.$once('refreshBillList',function(){
		console.log('监听到事件来自 refreshBillList');
    refreshList()
	})
})

onMounted(() => {
  getBookList()
})

const popupBillDetailRef = ref(null)
const currentBill = ref(null)

// 显示账单详情
const showBillDetail = async (item) => {
  await nextTick()
  if (popupBillDetailRef.value) {
    currentBill.value = item
    popupBillDetailRef.value.open()
  }
}

// 处理退款
const handleRefund = () => {
  // 实现退款逻辑
  uni.showToast({ title: '暂未开发', icon: 'warning' })
}

const cancelDetail = () => {
  popupBillDetailRef.value.close()
}

// 处理删除
const handleDelete = () => {
  uni.showModal({
    title: '提示',
    content: '确定要删除这条记录吗？',
    success: async (res) => {
      if (res.confirm) {
        try {
          uni.showLoading({ title: '删除中...' })
          let res = await billApi.delete(currentBill.value.id)
          uni.hideLoading()
          if (res.code != 200) {
            uni.showToast({ title: '删除失败', icon: 'error' })
            return
          }
          uni.showToast({ title: '删除成功', icon: 'success' })
          setTimeout(() => {
            popupBillDetailRef.value.close()
            refreshList()
          }, 1000)
        } catch (error) {
          uni.hideLoading()
          uni.showToast({ title: '删除失败', icon: 'error' })
          console.error('删除失败:', error)
        }
      }
    }
  })
}

// 处理编辑
const handleEdit = () => {
  // 实现编辑逻辑
  uni.showToast({ title: '暂未开发', icon: 'warning' })
}
</script>

<style lang="scss" scoped>
.current-book-name {
  font-size: 28rpx;
  color: black;
  font-weight: 600;
}
.current-month-str {
  color: black;
  font-size: 28rpx;
  font-weight: bold;
  background: #d4ebf9;
  padding: 10rpx 20rpx;
  border-radius: 30rpx;
}
/* 新增月份选择器样式 */
.month-selector {
  display: flex;
  flex-wrap: wrap;
  gap: 10rpx; /* 月份项之间的间距 */
  margin: 40rpx 0;
}

.month-item {
  flex: 0 0 calc(25% - 10rpx); /* 一行显示4个，减去间距 */
  text-align: center;
  padding: 10rpx;
  background-color: #e6f7ff; /* 淡蓝色背景 */
  
  border-radius: 8rpx;
  box-sizing: border-box;
}

.month-item.active {
  background-color: #1890ff; /* 选中时的蓝色背景 */
  color: #fff; /* 选中时的白色字体 */
}

.btn-box {
	.btn {
    flex: 1;
    text-align: center;
    padding: 20rpx;
    margin: 0 10rpx;
    border-radius: 40rpx;
    border: 1rpx solid #ccc;
  }
  .confirm-btn {
    background-color: #1890ff;
    color: #fff;
  }
}
::v-deep.uv-popup {
  z-index: 1000 !important;
}
.month-select-container {
  min-height: 300rpx;
  padding: 40rpx;
  background-color: #fff;
}
.su-card {
	border-radius: 16rpx;
}

.su-card-header {
  padding: 20rpx;
}

.su-card-body {
	padding: 0 20rpx;
}

/* 统计区域 */
::v-deep .statistic-card {
  flex: 1;
  text-align: center;
  margin: 20rpx;
  background: #fff;
  border-radius: 16rpx;
  .uv-text {
    margin: 0 auto !important;
    justify-content: center !important;
  }
}

::v-deep .uv-alert .uv-alert__content .uv-alert__content__title {
  color: black !important;
  font-size: 28rpx !important;
}
.date-info {
  display: flex;
  align-items: center;
  gap: 8px;

  .date {
    font-size: 16px;
    font-weight: bold;
  }

  .day-label {
    font-size: 14px;
    color: rgba(0, 0, 0, 0.45);
  }
}

.daily-total {
  font-size: 14px;

  text {
    margin-left: 16px;

    &:first-child {
      color: #ff4d4f;
    }

    &:last-child {
      color: #52c41a;
    }
  }
}
.bill-amount {
	font-weight: 600;
	&.expense {
		color: #ff4d4f;
	}
}
.bill-time {
	color: rgba(0, 0, 0, 0.45);
}
.title {
  font-size: 36rpx;
  font-weight: bold;
  margin-bottom: 20rpx;
}

.filter-bar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20rpx;
}

.summary {
  display: flex;
  gap: 30rpx;
  font-size: 28rpx;
}

.bill-list {
  padding: 20rpx;
}

.date-header {
  display: flex;
  justify-content: space-between;
  padding: 20rpx 0;
  font-size: 28rpx;
  color: #666;
}

.date-right {
  display: flex;
  gap: 20rpx;
}

.bill-items {
  background: #fff;
  border-radius: 12rpx;
  margin-bottom: 20rpx;
}

.bill-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #f5f5f5;
}

.item-left {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.icon-wrapper {
  width: 80rpx;
  height: 80rpx;
  background: #f0f0f0;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.bill-info {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.category {
  font-size: 30rpx;
}

.time {
  font-size: 24rpx;
  color: #999;
}

.amount {
  font-size: 32rpx;
  color: #333;
}

.amount.income {
  color: #45B680;
}

.tab-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 100rpx;
  background: #fff;
  display: flex;
  justify-content: space-around;
  align-items: center;
  border-top: 1rpx solid #eee;
}

.tab-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  font-size: 24rpx;
  color: #666;
}

.tab-item .iconfont {
  font-size: 40rpx;
  margin-bottom: 6rpx;
}
.page-container {
	padding: 20rpx;
}

::v-deep .uv-list-item {
	padding: 16rpx 0;
}

.fixed-bottom-right-button {
    position: fixed;
    bottom: 200rpx;
    right: 20rpx;
    width: 80rpx;
    height: 80rpx;
    border-radius: 50%;
}

.bill-detail {
  padding: 30rpx;
  background-color: #fff;
  
  .detail-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-bottom: 20rpx;
    
    .title {
      font-size: 32rpx;
      font-weight: 500;
      color: #333;
    }
  }
  
  .action-group {
    display: flex;
    justify-content: space-around;
    padding: 30rpx 0;
    border-bottom: 1px solid #eee;
    
    .action-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 8rpx;
      
      text {
        font-size: 24rpx;
        color: #666;
      }
    }
  }
  
  .detail-content {
    padding-top: 20rpx;
    
    .detail-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 24rpx 0;
      font-size: 28rpx;
      border-bottom: 1px solid #f5f5f5;
      
      &:last-child {
        border-bottom: none;
      }
      
      .label {
        color: #999;
      }
      
      .value {
        color: #333;
        
        &.amount {
          font-size: 32rpx;
          font-weight: 500;
        }
      }
    }
  }
}

.su-card-normal {
  &:active {
    background-color: #f5f5f5;
  }
}
</style>
