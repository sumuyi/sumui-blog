<template>
	<div id="home">
		<el-container class="su-container">
			<el-main class="su-main">
				<router-view />
			</el-main>
			<el-aside width="400px" style="background-color: rgb(238, 241, 246)"></el-aside>
		</el-container>

	</div>

</template>

<script>

export default {
	name: 'Home',
	data() {
		return {

		}
	},
	beforeRouteEnter(to, from, next) {
		next(vm => {
			vm.activeIndex = to.path
		})
	},
	beforeRouteUpdate(to, from, next) {
		if (to.path == '/') {
			this.footerShow = true
		} else {
			this.footerShow = false
		}
		this.activeIndex = to.path
		next()
	}
}
</script>

<style>
.su-container {
	width: 100vw;
	width: 100dvw;
	max-width: 1400px;
	height: 100vh;
	height: 100dvh;
	margin: 0 auto;
	padding: 1rem 2rem;
	animation: show .5s forwards;
	animation-duration: .5s;
}

.su-main {
	background: #f7f9fe;
}
</style>
