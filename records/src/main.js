// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import FastClick from 'fastclick'
import VueRouter from 'vue-router'
import App from './App'
import Home from '@/views/MainList'
// import Record from '@/views/NewRecord'
// import Success from '@/views/Success'
import { TransferDom } from 'vux'
import axios from 'axios'
import VueAxios from 'vue-axios'
import store from './store'

Vue.use(VueAxios, axios)
let BASE_URL = process.env.BASE_URL
let PROXY_URL = process.env.PROXY_URL
let USE_PROXY = process.env.USE_PROXY

// Add a request interceptor
axios.interceptors.request.use(
  function (config) {
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
  },
  function (error) {
    // Do something with request error
    return Promise.reject(error)
  }
)

console.info(
  `
  如果你能看到这段文字，那就说明你跟我们一样，是代码与情怀的追求者，
  那就不妨加入我们，一起去探索更广阔的技术世界❤！
  join@bitnp.net
  `
)

// Add a response interceptor
axios.interceptors.response.use(
  function (response) {
    // Do something with response data
    return response
  },
  function (error) {
    // Do something with response error
    store.commit('popError', '无法获取数据')
    if (error.response && error.response.data) {
      console.log(error.response.data)
    } else if (error.response) {
      console.log(error.response)
    } else {
      console.log(error)
    }
    return Promise.reject(error)
  }
)

Vue.directive('transfer-dom', TransferDom)
// import VueI18n from 'vue-i18n'

// Vue.use(VueI18n)

// ============================================================
// ======================= Vue Router =========================
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: Home,
    children: [
      {
        path: 'calendar',
        name: 'calendar',
        component: () =>
          import(/* webpackChunkName: "calendar" */ './views/Calendar.vue')
      },
      {
        path: '',
        name: 'announcements',
        component: () =>
          import(/* webpackChunkName: "announcements" */ './views/Announcements.vue')
      },
      {
        path: 'working',
        name: 'working',
        component: () =>
          import(/* webpackChunkName: "working" */ './views/WorkingRecord.vue')
      },
      {
        path: 'finish',
        name: 'finish',
        component: () =>
          import(/* webpackChunkName: "finish" */ './views/FinishedRecords.vue')
      }
    ]
  },
  {
    path: '/new/',
    name: 'record',
    component: () =>
      import(/* webpackChunkName: "record" */ './views/NewRecord.vue')
  },
  {
    path: '/success/',
    name: 'success',
    component: () =>
      import(/* webpackChunkName: "success" */ './views/Success.vue')
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
