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
    dates: []
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
          console.log(Vue.prototype.Const)
          console.log(data)
          state.campus = data
        })
        .catch(e => (state.toast = newToast('操作失败')))
    },
    getDates (state) {
      Axios.get(Vue.prototype.Const + 'date/').then(({ data }) => {
        console.log(data)
        state.dates = data
      })
    }
  },
  actions: {}
})
