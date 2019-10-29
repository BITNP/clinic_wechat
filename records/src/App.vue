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
  </div>
</template>

<script>
import { Toast } from 'vux'

export default {
  name: 'app',
  components: {
    Toast
  },
  computed: {
    toast () {
      return this.$store.state.toast
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
