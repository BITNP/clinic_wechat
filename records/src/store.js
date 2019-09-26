import Vue from 'vue'
import Vuex from 'vuex'

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
    }
  },
  mutations: {
    popSuccess (state, text = '操作成功') {
      state.toast = newToast(text)
    },
    popError (state, text = '操作失败') {
      state.toast = newToast(text)
    }
  },
  actions: {

  }
})
