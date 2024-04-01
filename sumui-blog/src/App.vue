<template>
  <div id="app">
    <router-view />
    <go-top></go-top>
  </div>
</template>

<script>
import GoTop from '@/components/gotop/GoTop'
export default {
  name: 'App',
  components: { GoTop }
}
</script>

<style>
#app {
  width: 100vw;
  height: 100vh;
  height: 100dvh;
  transition: filter .3s cubic-bezier(.25, .46, .45, .94);
}

* {
  margin: 0;
  padding: 0;
}

body {
  background: linear-gradient(117deg, #f3efff, #e7f1ff);
  font-weight: 400;
  font-family: "JetBrains Mono";
  line-height: 1.5;
}

a {
  cursor: pointer;
  text-decoration: none;
  transition: none 86ms ease-out;
}

a:hover {
  color: #5FB878;
}

.me-area {
  background-color: #fff;
}

body {
  position: relative;
}

.me-pull-right {
  float: right;
}
</style>
