// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import FastClick from 'fastclick'
import VueRouter from 'vue-router'
import App from './App'
import Home from '@/views/MainList'
import Record from '@/views/NewRecord'
import Success from '@/views/Success'
import {
  TransferDom
} from 'vux'
import axios from 'axios'
import VueAxios from 'vue-axios'

Vue.use(VueAxios, axios)

let BASE_URL = 'http://10.195.191.61:8000/api/wechat/'

// Add a request interceptor
axios.interceptors.request.use(function (config) {
  // Do something before request is sent
  config.headers = {
    apikey: 'oh-my-tlb'
  }
  config.params = {
    username: 'FKY'
  }

  if (config.data) {
    config.data.user = 'FKY'
  } else {
    config.data = {
      user: 'FKY'
    }
  }
  return config
}, function (error) {
  // Do something with request error
  return Promise.reject(error)
})

// Add a response interceptor
axios.interceptors.response.use(function (response) {
  // Do something with response data
  return response
}, function (error) {
  // Do something with response error
  console.log(error.response.data)
  return Promise.reject(error)
})

Vue.directive('transfer-dom', TransferDom)
// import VueI18n from 'vue-i18n'

// Vue.use(VueI18n)

// ============================================================
// ======================= Vue Router =========================
Vue.use(VueRouter)

const routes = [{
  path: '/',
  name: 'home',
  component: Home
}, {
  path: '/new',
  name: 'record',
  component: Record
},
{
  path: '/success',
  name: 'success',
  component: Success
}
]

const router = new VueRouter({
  routes
})

FastClick.attach(document.body)

Vue.config.productionTip = false
Vue.prototype.Const = BASE_URL

/* eslint-disable no-new */
new Vue({
  router,
  render: h => h(App)
}).$mount('#app-box')
