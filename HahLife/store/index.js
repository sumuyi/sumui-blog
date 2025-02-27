import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
	state: () => ({
		userId: "",
		userInfo: {},
		familyList: []
	}),
	
	getters: {
		getFamilyList: (state) => {
			console.log('getFamilyList state:', state)
			return state.familyList.length > 0 
				? state.familyList 
				: (state.userInfo.familyUsersList || [])
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
		}
	}
})

export default useUserStore