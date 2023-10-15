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
import * as dd from 'dingtalk-jsapi'
import axios from 'axios'
import store from './store'

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
  async created () {
    let USER_URL = process.env.USER_URL
    await (() => {
      return new Promise(async (resolve, reject) => {
        if (!store.state.loggedin) {
          const response = await axios.get(USER_URL)
          if (response.data.status === 200) {
            store.state.loggedin = true
            resolve()
          } else if (response.data.status === 403) {
            dd.ready(() => {
              dd.runtime.permission.requestAuthCode({
                corpId: findGetParameter('corpId'),
                onSuccess: async (info) => {
                  localStorage.setItem('user-token', info.code)
                  const response2 = await axios.post(USER_URL)
                  if (response2.data.status !== 200) {
                    reject(Error('login failed'))
                  } else {
                    resolve()
                  }
                }
              })
            })
          }
        } else {
          resolve()
        }
      })
    })()
    this.$store.commit('initAnnouncements')
    this.$store.commit('getCampus')
    this.$store.commit('getDates')
    this.$store.commit('getWorkingRecord')
  }
}

function findGetParameter (parameterName) {
  let result = null
  let tmp = []
  console.log(location.search)
  location.search.substr(1).split('&').forEach(function (item) {
    tmp = item.split('=')
    if (tmp[0] === parameterName) {
      result = decodeURIComponent(tmp[1])
    }
  })
  return result
}
</script>

<style lang="less">
@import "~vux/src/styles/reset.less";

body {
  background-color: #fbf9fe;
}
</style>
