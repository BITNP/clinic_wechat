import Vue from 'vue'
import Vuex from 'vuex'
import Axios from 'axios'

Vue.use(Vuex)

// utils function
function newToast (text, position = 'bottom') {
  return {
    show: true,
    text: text,
    position: position
  }
}

export default new Vuex.Store({
  state: {
    toast: {
      show: false,
      text: '',
      position: ''
    },
    campus: [],
    dates: [],
    announcements: [],
    workingRecord: undefined
  },
  mutations: {
    popSuccess (state, text = '操作成功') {
      state.toast = newToast(text)
    },
    popError (state, text = '操作失败') {
      state.toast = newToast(text)
    },
    getCampus (state) {
      Axios.get(`${Vue.prototype.Const}campus/`)
        .then(({ data }) => {
          state.campus = data
        })
    },
    getDates (state) {
      Axios.get(Vue.prototype.Const + 'date/').then(({ data }) => {
        if (!data || (typeof (data) === 'string')) {
          // No content
          data = []
        }
        state.dates = data
      })
    },
    initAnnouncements (state) {
      // get announcements from api
      Axios
        .get(`${Vue.prototype.Const}announcement/`)
        .then(({ data }) => {
          data.map(v => {
            v.show = false
          })
          state.announcements = data
        })
        .catch(e => {
          state.toast = newToast('无法获取公告信息')
        })
    },
    getWorkingRecord (state) {
      Axios.get(`${Vue.prototype.Const}wechat/working/`)
      .then(({ data }) => {
        if (data && data.count === 1) {
          // 有
          state.workingRecord = data.data
        } else {
          // 没有
          state.workingRecord = null
        }
      })
    }
  },
  actions: {},
  getters: {}
})
