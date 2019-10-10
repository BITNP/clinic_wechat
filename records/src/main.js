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
import store from './store'

Vue.use(VueAxios, axios)
console.log(process.env)
let BASE_URL = process.env.BASE_URL
let PROXY_URL = process.env.PROXY_URL
let USE_PROXY = process.env.USE_PROXY

// Add a request interceptor
axios.interceptors.request.use(function (config) {
  if (USE_PROXY) {
    // save real url in headers[url]
    if (config.headers) {
      config.headers['url'] = config.url
    } else {
      config.headers = {
        url: config.url
      }
    }
    // substitude url with groovy proxy url
    config.url = PROXY_URL
    config.params = {
      script: 'requestProxy.groovy'
    }

    // set script name
    if (config && config.data) {
      config.data.script = 'requestProxy.groovy'
    } else {
      config.data = {
        script: 'requestProxy.groovy'
      }
    }
  } else {
    // 本机开发，不使用groovy proxy
    // inject api and user name
    config.headers = {
      apikey: 'oh-my-tlb'
    }
    config.params = {
      username: 'FKY'
    }

    if (config && config.data) {
      config.data.user = 'FKY'
    } else {
      config.data = {
        user: 'FKY'
      }
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
  if (error.response && error.response.data) {
    console.log(error.response.data)
  } else if (error.response) {
    console.log(error.response)
  } else {
    console.log(error)
  }
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
  store,
  render: h => h(App)
}).$mount('#app-box')
