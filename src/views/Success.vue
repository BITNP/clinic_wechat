<template>
  <div>
    <br />
    <br />
    <br />
    <msg :title="title || '已成功提交工单'" description="网络开拓者协会 - 电脑诊所">
      <template slot="buttons">
        <x-button type="primary" @click.native="backHome">返回首页</x-button>
      </template>
    </msg>
  </div>
</template>

<script>
import { Msg, XButton } from 'vux'
export default {
  components: {
    Msg, XButton
  },
  methods: {
    backHome () {
      this.data = true
      this.$router.push('/')
      this.$store.commit('getWorkingRecord') // reload working record
    }
  },
  computed: {
    title () {
      return this.$route.params.title
    }
  },
  data: () => ({
    data: false
  })
}
</script>

<style>
</style>