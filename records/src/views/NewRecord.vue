<template>
  <div>
    <x-header>新建工单</x-header>
    <RecordForm v-model="record" :create="true" />
    <br />
    <check-icon :value.sync="agree">
      我已阅读并同意
      <a @click="toc = true" style="color: blue;">免责声明</a> 中的内容
    </check-icon>

    <br />
    <box gap="10px 10px">
      <x-button type="primary" @click.native="submit">提交</x-button>
    </box>
    <toast v-model="show_toast" type="text" :time="800" :text="toast_text"></toast>
    <TOC v-model="toc"/>
  </div>
</template>

<script>
import RecordForm from '@/components/RecordForm'
import TOC from '@/components/TOC'
import { XHeader, XButton, Toast, Box, CheckIcon } from 'vux'

export default {
  props: ['display'],
  model: {
    prop: 'display',
    event: 'change'
  },
  components: {
    RecordForm,
    XHeader,
    XButton,
    Toast,
    Box,
    CheckIcon,
    TOC
  },
  data: () => ({
    agree: false,
    toc: false,
    dates: [],
    record: {
      campus: ''
    },
    show_toast: false,
    toast_text: ''
  }),
  methods: {
    toast (text) {
      this.show_toast = true
      this.toast_text = text
    },
    submit () {
      if (
        !(
          this.record.campus &&
          this.record.appointment_time &&
          this.record.description
        )
      ) {
        this.toast('有选项仍未填完')
        return
      }
      if (this.agree === false) {
        this.toast('请在提交前先阅读并同意我们的免责声明')
        return
      }
      this.axios
        .post(this.Const + 'wechat/', {
          ...this.record
        })
        .then(({ data }) => {
          this.$router.push('/success')
        })
        .catch(() => {
          this.toast('Oops! 我们遇到了一些技术问题')
        })
    }
  },
  mounted () {
    this.record.campus = this.$store.state.campus[0].name
  }
}
</script>

<style>
</style>