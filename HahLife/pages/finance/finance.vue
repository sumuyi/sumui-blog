<template>
	<view class="page-container">
		<!-- 顶部统计栏 -->
    <view class="flex-between">
      <view class="statistic-card pd-tb20">
        <uv-text type="info" text="总支出"></uv-text>
        <uv-count-to :startVal="0" :endVal="statistics.totalExpense" :decimals="2" decimal="." color="#cf1322" bold></uv-count-to>
      </view>
      <view class="statistic-card pd-tb20">
        <uv-text type="info" text="总收入"></uv-text>
        <uv-count-to :startVal="0" :endVal="statistics.totalIncome" :decimals="2" decimal="." color="#3f8600" bold></uv-count-to>
      </view>
      <view class="statistic-card pd-tb20">
        <uv-text type="info" text="结余"></uv-text>
        <uv-count-to :startVal="0" :endVal="statistics.balance" :decimals="2" decimal="." bold></uv-count-to>
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
          <span>出 {{ group.totalExpense.toFixed(2) }}</span>
          <span>入 {{ group.totalIncome.toFixed(2) }}</span>
        </view>
      </view>
      <view class="su-card-body">
        <view class="su-card-normal mb-10" v-for="(item, index) in group.items" :key="index">
          <view class="flex-between font-14">
            <view class="bill-category">{{ item.category }}</view>
            <view class="bill-amount" :class="{ 'expense': item.type === 1 }">
              ￥{{ item.type === 1 ? '-' : '+' }}{{ item.amount.toFixed(2) }}
            </view>
          </view>
          <view class="bill-time font-12">{{ item.userName || '-' }} | {{ item.remark || ' 无备注' }}</view>
        </view>
      </view>
    </view>
		
	</view>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { billApi } from '@/api/bills'
import { timeFormat } from '@/uni_modules/uv-ui-tools/libs/function/index.js';

const billList = ref([])
// 添加新的响应式变量
const statistics = ref({
  totalExpense: 0,
  totalIncome: 0,
  balance: 0
})
const currentMonth = ref(timeFormat(new Date().getTime(), "yyyy-mm"))

// 分类数据
const categories = [
  { label: '三餐', value: 1, icon: 'su-icon-sancanshuiping' },
  { label: '水果蔬菜', value: 2, icon: 'su-icon-shuiguoshucai' },
  { label: '咖啡饮品', value: 3, icon: 'su-icon-kafeiguan' },
  { label: '话费', value: 4, icon: 'su-icon-huafei' },
  { label: '购物', value: 5, icon: 'su-icon-gouwu' },
  { label: '火锅', value: 6, icon: 'su-icon-huoguo' }
  // ... 添加更多分类
]

// 添加新的响应式变量

// 获取账单数据
const getFinance = async () => {
	try {
		let response = await billApi.getList(1, '2025-02')
		
    // 添加空值检查
    if (!response) {
      bills.value = []
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
          category: categories.find(c => c.value === bill.categoryId)?.label || '其他',
          icon: categories.find(c => c.value === bill.categoryId)?.icon || 'su-icon-qita'
        }))
      }
    }
    billList.value = Object.values(groupedBills)
	} catch (err) {
		console.error(err)
	}
}

// 新增获取统计数据的独立函数
const fetchStatistics = async () => {
  try {
    const statsResponse = await billApi.getStatistics(1, currentMonth.value)
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

onMounted(() => {
	getFinance()
  fetchStatistics()
})
</script>

<style lang="scss" scoped>
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

  span {
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

</style>
