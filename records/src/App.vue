<template>
  <div id="app">
    <router-view></router-view>
    <toast
      v-model="toast.show"
      type="text"
      :time="2000"
      is-show-mask
      :text="toast.text"
      :position="toast.position"
    ></toast>
    <loading :show="loading" text="正在努力加载"></loading>
  </div>
</template>

<script>
import { Toast, Loading } from 'vux'

export default {
  name: 'app',
  components: {
    Toast, Loading
  },
  computed: {
    toast () {
      return this.$store.state.toast
    },
    loading () {
      return this.$store.state.loading
    }
  },
  created () {
    this.$store.commit('getCampus')
    this.$store.commit('getDates')
    this.$store.commit('getWorkingRecord')
  }
}
</script>

<style lang="less">
@import "~vux/src/styles/reset.less";

body {
  background-color: #fbf9fe;
}
</style>
