<template>
    <div>
        <div @click="handleClick('/test1-1')">test1-1-1</div>
        <div @click="handleClick('/test1-2')">test1-1-2</div>
    </div>
</template>

<script>
export default {
    name: 'test1-1',
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