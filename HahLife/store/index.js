import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
	state: () => ({
		userId: "",
		userInfo: {},
		familyList: [],
		bookList: [],
		currentBook: null
	}),
	
	getters: {
		getFamilyList: (state) => {
			console.log('getFamilyList state:', state)
			return state.familyList.length > 0 
				? state.familyList 
				: (state.userInfo.familyUsersList || [])
		},
		// 获取账本列表
		getBookList: (state) => {
			return state.bookList
		},
		// 获取当前选中的账本
		getCurrentBook: (state) => {
			return state.currentBook || (state.bookList.length > 0 ? state.bookList[0] : null)
		}
	},
	
	actions: {
		setUserId(userId) {
			this.userId = userId
		},
		setUserInfo(userInfo) {
			this.userInfo = userInfo
			if (userInfo.familyUsersList) {
				this.familyList = userInfo.familyUsersList
			}
		},
		setFamilyList(list) {
			console.log('Setting family list:', list)
			this.familyList = Array.isArray(list) ? list : []
		},
		// 设置账本列表
		setBookList(bookList) {
			console.log('Setting book list:', bookList)
			this.bookList = Array.isArray(bookList) ? bookList : []
			
			// 如果有账本且当前未选择账本，则默认选择第一个
			if (this.bookList.length > 0 && !this.currentBook) {
				this.setCurrentBook(this.bookList[0])
			}
		},
		
		// 设置当前选中的账本
		setCurrentBook(book) {
			console.log('Setting current book:', book)
			this.currentBook = book
		},
		
		// 添加账本
		addBook(book) {
			this.bookList.push(book)
		},
		
		// 更新账本
		updateBook(updatedBook) {
			const index = this.bookList.findIndex(book => book.id === updatedBook.id)
			if (index !== -1) {
				this.bookList[index] = updatedBook
				
				// 如果更新的是当前选中的账本，也更新当前账本
				if (this.currentBook && this.currentBook.id === updatedBook.id) {
					this.currentBook = updatedBook
				}
			}
		},
		
		// 删除账本
		deleteBook(bookId) {
			this.bookList = this.bookList.filter(book => book.id !== bookId)
			
			// 如果删除的是当前选中的账本，重置当前账本
			if (this.currentBook && this.currentBook.id === bookId) {
				this.currentBook = this.bookList.length > 0 ? this.bookList[0] : null
			}
		}
	},
	// 添加持久化配置
	persist: {
		key: 'hahlife-user-store',
		storage: {
			// 在 uniapp 中使用 uni 的存储 API
			getItem: (key) => {
				return uni.getStorageSync(key)
			},
			setItem: (key, value) => {
				uni.setStorageSync(key, value)
			}
		},
		// 指定需要持久化的状态
		paths: ['userId', 'userInfo', 'familyList', 'bookList', 'currentBook']
	}
})

export default useUserStore