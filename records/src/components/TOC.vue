<template>
  <div v-transfer-dom>
    <popup v-model="show" height="100%">
      <div class="popup">
        <div id="toc">
          <vue-markdown :show="toc.length>0" :source="toc"></vue-markdown>
        </div>
        <br />
        <x-button @click.native="toggle">返回</x-button>
      </div>
    </popup>
  </div>
</template>

<script>
import VueMarkdown from 'vue-markdown'
import { Popup, XButton } from 'vux'
export default {
  components: {
    Popup,
    VueMarkdown,
    XButton
  },
  data: () => ({
    toc: '无'
  }),
  props: ['show'],
  model: {
    prop: 'show',
    event: 'click'
  },
  methods: {
    toggle () {
      this.$emit('toggle-toc')
    },
    getTOC () {
      this.axios.get(`${this.Const}announcement/toc/`).then(({ data }) => {
        this.toc = data.content
      }).catch(e => {
        console.error(e)
        this.$store.commit('popError', '无法获取免责声明')
      })
    }
  },
  mounted () {
    this.getTOC()
  },
  created () {
    this.getTOC()
  }
}
</script>

<style>
#toc{
    margin: 40px;
}
</style>
