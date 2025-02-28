<template>
	<view class="add-bill-container">
    <!-- 顶部类型切换 -->
    <view class="flex-between pd-lr20">
      <view class="current-book-name flex-between" @click="showPopupBookList">
        {{ currentBookName }}
        <uv-icon name="arrow-down-fill" color="black"></uv-icon>
      </view>
      <view class="type-box flex-between">
        <view 
          v-for="(type, index) in typeList" 
          :key="index"
          class="type-item"
          :style="{ backgroundColor: currentType === type.value ? type.bgColor : '' }"
          @tap="handleTypeClick(type.value)"
        >
          {{ type.label }}
        </view>
      </view>
    </view>
    <!-- 中间 图标分类 选择 -->
    <view class="category-grid">
      <view class="category-container">
        <view 
          v-for="(category, index) in categories" 
          :key="index"
          class="category-item"
          :class="{ active: formState.categoryId === category.value }"
          @tap="selectCategory(category)"
        >
          <view class="icon-box">
            <uv-icon :custom-prefix="'su-icon ' + category.icon" size="20"></uv-icon>
          </view>
          <text style="font-size: 12px;" :style="{ color: formState.categoryId === category.value ? '#1890ff' : '#666' }">
            {{ category.label }}
          </text>
        </view>
      </view>
    </view>

    <!-- 备注 -->
    <view>
      <uv-textarea v-model="formState.remark" border="bottom" placeholder="请输入备注内容"></uv-textarea>
    </view>

    <!-- 金额和键盘 -->
    <view>
      <!-- 底部计算器操作 -->
      <view class="operate-box flex-between">
        <view class="flex">
          <view class="flex" @click="showPickerUser">
            <uv-icon name="server-man"></uv-icon>
            <text class="user-name mr-10">{{ formState.userName }}</text>
          </view>
          <view class="flex" @click="showPickerBillDate">
            <uv-icon name="server-man"></uv-icon>
            <text class="user-name">{{ formState.billDate }}</text>
          </view>
        </view>

        <!-- 金额显示 -->
        <view class="amount-display">
          <text class="currency">¥</text>
          <text class="amount">{{ formState.amount || '0.00' }}</text>
        </view>
      </view>
      

      <!-- 数字键盘 -->
      <view class="number-keyboard">
        <view v-for="(row, rowIndex) in keyboardLayout" :key="rowIndex" class="keyboard-row" >
          <view v-for="(key, keyIndex) in row" :key="keyIndex" class="num-btn" :class="{ 'function-key': isNaN(key) }" 
            :style="{ backgroundColor: setColor(key) }"
            @click="handleKeyPress(key)"
          >
            {{ key }}
          </view>
        </view>
      </view>
    </view>

    <!-- 账本弹框 -->
    <uv-picker ref="pickerBookRef" :columns="bookList" keyName="name" @confirm="confirmBook"></uv-picker>
    

    <uv-picker 
      ref="pickerUserRef" 
      :columns="familyList" 
      keyName="label" 
      @confirm="confirmUser"
    ></uv-picker>
    <uv-toast ref="toast"></uv-toast>
    <l-calendar v-model:value="showCalendar" @change="confirmBillDate"></l-calendar>
  </view>
</template>

<script setup>
import { reactive, ref, nextTick, computed } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import { timeFormat } from '@/uni_modules/uv-ui-tools/libs/function/index.js';
import { billApi } from '@/api/bills'
import { bookApi } from '@/api/books'

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
}

// 修改为二维数组结构
const familyList = ref([[]])

const formState = reactive({
  type: 1,
  userId: 1,
  userName: '苏木易',
  amount: null,
  categoryId: 1,
  account: undefined,
  billDate: timeFormat(new Date().getTime(), "yyyy-mm-dd"),
  remark: undefined
})

// 类型切换
const currentType = ref(1)
const typeList = ref([
	{ label: '支出', value: 1, bgColor: '#fd3b2f' },
	{ label: '收入', value: 2, bgColor: '#3bc25c' },
	{ label: '转账', value: 3, bgColor: 'orange' }
])
const handleTypeClick = (typeValue) => {
    currentType.value = typeValue;
}

// 支出分类数据
const expenseCategories = [
  { label: '三餐', value: 1, icon: 'su-icon-food' },
  { label: '蔬菜水果', value: 2, icon: 'su-icon-shucai' },
  { label: '奶茶', value: 3, icon: 'su-icon-bubble-tea' },
  { label: '购物', value: 5, icon: 'su-icon-gouwu-2' },
  { label: '健身', value: 22, icon: 'su-icon-jianshen' },
  { label: '美食', value: 6, icon: 'su-icon-canyin' },
  { label: '交通', value: 7, icon: 'su-icon-jiaotong' },
  { label: '数码', value: 8, icon: 'su-icon-xiangji' },
  { label: '美容', value: 9, icon: 'su-icon-meirong' },
  { label: '娱乐', value: 10, icon: 'su-icon-yule' },
  { label: '通讯', value: 11, icon: 'su-icon-shouji-iphone12-zuhe' },
  { label: '教育', value: 12, icon: 'su-icon-jiaoyu' },
  { label: '住房', value: 13, icon: 'su-icon-a-fangziwuzijianzhu' },
  { label: '服饰', value: 14, icon: 'su-icon-Suit-flat' },
  { label: '医疗', value: 15, icon: 'su-icon-a-yiyuanzhensuoloufangjianzhu' },
  { label: '旅行', value: 21, icon: 'su-icon-feiji' },
  { label: '其他', value: 16, icon: 'su-icon-qita' }
]

// 收入分类数据
const incomeCategories = [
  { label: '工资', value: 17, icon: 'su-icon-wodegongzi' },
  { label: '生活费', value: 23, icon: 'su-icon-shenghuofei' },
  { label: '收益', value: 18, icon: 'su-icon-shouyi' },
  { label: '退款', value: 19, icon: 'su-icon-tuikuan' },
  { label: '红包', value: 20, icon: 'su-icon-hongbao' },
]

// 根据当前类型显示对应的分类列表
const categories = computed(() => {
  return currentType.value === 1 ? expenseCategories : incomeCategories
})

const confirmUser = (e) => {
  console.log('confirm', e)
  if (e.value && e.value[0]) {
    formState.userId = e.value[0].value
    formState.userName = e.value[0].label
  }
}
const pickerUserRef = ref(null)
const showPickerUser = async () => {
  console.log('showPickerUser');
  await nextTick()
  if (pickerUserRef.value) {
    pickerUserRef.value.open()
  }
}

const showCalendar = ref(false)

const showPickerBillDate = () => {
  showCalendar.value = true
}

const confirmBillDate = (e) => {
  console.log('选择的日期', e)
  formState.billDate = e.result
  showCalendar.value = false
}


// 数字键盘布局
const keyboardLayout = [
  ['1', '2', '3', 'Del'],
  ['4', '5', '6', '+'],
  ['7', '8', '9', '-'],
  ['00', '0', '.', 'Save']
]

// 抽离处理函数
const handleDelete = () => {
  if (formState.amount) {
    formState.amount = formState.amount.slice(0, -1)
  }
}

const toast = ref(null)
const handleSave = async () => {
  if (!formState.amount) {
    toast.value.show({
      message: '请输入金额',
      type: 'error',
      duration: 1000
    })
    return
  }
  if (!formState.categoryId) {
    toast.value.show({
      message: '请选择分类',
      type: 'error',
      duration: 1000
    })
    return
  }

  const billData = {
    type: currentType.value,
    userId: formState.userId,
    amount: parseFloat(formState.amount),
    categoryId: formState.categoryId,
    account: formState.account || 1,
    remark: formState.remark,
    billDate: formState.billDate,
    bookId: currentBookId.value
  }

  try {
    uni.showLoading({
      title: '保存中...',
      mask: true
    })
    let result = await billApi.add(billData)
    uni.hideLoading()
    console.log('保存结果', result);
    
    toast.value.show({
      message: '保存成功',
      type: 'success',
      duration: 1000
    })
    
    uni.$emit('refreshBillList')
    goBack()
  } catch (error) {
    console.error('保存失败:', error)
  }
}

const handleAgain = () => {
  // 再记一笔的逻辑
  console.log('再记一笔')
}

const handleSubway = () => {
  // 地铁快捷键逻辑
  console.log('地铁快捷键')
}

const handleQuick = () => {
  // 秒记逻辑
  console.log('秒记')
}

const handleNumberInput = (key) => {
  if (!formState.amount) {
    formState.amount = key
  } else {
    if (formState.amount.includes('.')) {
      if (key === '00') {
      	return
      }
      const [integer, decimal = ''] = formState.amount.split('.')
      // 只有当小数部分长度小于2时才允许继续输入
      if (decimal.length < 2) {
        formState.amount = `${integer}.${decimal}${key}`
      }
    } else {
      formState.amount += key
    }
  }
}

const handleDecimalPoint = () => {
  // 如果已经有小数点，则不再添加
  if (!formState.amount || !formState.amount.includes('.')) {
    formState.amount = formState.amount ? `${formState.amount}.` : '0.'
  }
}

const setColor = (key) => {
  if (key === 'Del') {
    return '#fae6e5'
  } else if (key === 'Save') {
    return '#007aff'
  } else if (key === '+' || key === '-' || key === '.') {
    return '#f3f3f3'
  } else {
    return '#ffffff'
  }
}

// 修改后的 handleKeyPress 函数
const handleKeyPress = async (key) => {
  if (key === '.') {
    handleDecimalPoint()
  } else if (isNaN(key)) {
    // 处理功能键
    switch (key) {
      case 'Del':
        handleDelete()
        break
      case 'Save':
        await handleSave()
        break
      case '+':
        handleAgain()
        break
      case '-':
        handleSubway()
        break
      case 'quick':
        handleQuick()
        break
    }
  } else {
    // 处理数字输入
    handleNumberInput(key)
  }
}

// 返回上一页
const goBack = () => {
  uni.navigateBack({
    delta: 1
  });
}

// 选择分类
const selectCategory = (category) => {
  console.log('选择分类', category);
  
  formState.categoryId = category.value
}

onLoad(operation => {
  console.log('页面加载', operation)
  getBookList()
  
  // 获取本地存储的用户信息
  const userInfo = uni.getStorageSync('userInfo')
  console.log('本地存储的用户信息：', userInfo)
  
  if (userInfo) {
    formState.userId = userInfo.userId
    formState.userName = userInfo.nickname || userInfo.userName
    
    // 初始化家庭成员列表
    const members = []
    // 添加家庭成员
    if (userInfo.familyUsersList) {
      userInfo.familyUsersList.forEach(item => {
        members.push({
          label: item.nickname || item.username,
          value: item.userId
        })
      })
    }
    
    // 设置为二维数组
    familyList.value = [members]
    console.log('家庭成员列表', familyList.value)
  }
})
</script>

<style lang="scss" scoped>
.current-book-name {
  font-size: 28rpx;
  color: black;
  font-weight: 600;
}
::v-deep .category-item .uv-icon {
  border-radius: 50%;
  width: 70rpx;
  height: 70rpx;
  .su-icon {
    margin: 0 auto !important;
  }
}

.amount-display {
  text-align: right;
  font-size: 64rpx;
  font-weight: bold;
  color: #333;
}

.amount-display .currency {
  font-size: 24px;
  margin-right: 4px;
}
.amount-display .amount {
  color: #fe3730;
}

.number-keyboard {
  padding: 8px;
  display: grid;
  grid-template-rows: repeat(4, 1fr);
  gap: 8px;
}

.keyboard-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 8px;
  height: 54px;
  /* 固定每行高度 */
}

.keyboard-row .num-btn {
  flex: 0 0 calc(20% - 10rpx);
  height: 100rpx;
  text-align: center;
  color: #020202;
  border-radius: 16rpx;
  font-size: 30rpx;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2rpx 6rpx rgba(0, 0, 0, 0.1);
}

.operate-box {
  padding: 20rpx;
  .user-name {
    font-size: 24rpx;
    color: #535454;
  }
}
.category-grid {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  height: 530rpx;
}

.category-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10rpx; /* 月份项之间的间距 */
}

.category-item {
  flex: 0 0 calc(20% - 30rpx); /* 一行显示5个 */
  text-align: center;
  padding: 10rpx 10rpx;

  .icon-box {
    width: 70rpx;
    height: 70rpx;
    background: #f6eeee;
    border-radius: 50%;
    margin: 0 auto;
  }
  
  text {
    font-size: 24rpx;
    color: #666;
  }
  
  &.active {
    .icon-box { background: #c6d7f6; }
    text { color: #1890ff; }
  }
}

.type-box {
	width: 380rpx;
	background-color: #ededed;
  border-radius: 40rpx;
  .type-item {
		width: 120rpx;
    padding: 6rpx 0;
    text-align: center;
    border-radius: 40rpx;
    font-size: 24rpx;
	}
}
.add-bill-container {
  padding: 40rpx 0;
  background-color: #f9f9f9;
}
</style>