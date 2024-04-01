<template>
    <div>
        <div @click="handleClick('/test1-1')">test1-2-1</div>
        <div @click="handleClick('/test1-2')">test1-2-2</div>
    </div>
</template>

<script>
export default {
    name: 'test1-2',
    data() {
        return {

        }
    },
    methods: {
        handleClick(path) {
            this.$router.push({ path: path })
        },
    }
}
</script>

<style></style>