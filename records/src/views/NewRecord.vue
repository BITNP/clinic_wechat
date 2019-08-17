<template>
  <div>
    <x-header>新建工单</x-header>
    <RecordForm v-model="record" :create="true"  />
    <br />
    <box gap="10px 10px">
      <x-button type="primary" @click.native="submit">提交</x-button>
    </box>
    <toast v-model="show_toast" type="text" :time="800" :text="toast_text"></toast>
  </div>
</template>

<script>
import RecordForm from '@/components/RecordForm'
import { XHeader, XButton, Toast, Box } from 'vux'

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
    Box
  },
  data: () => ({
    dates: [],
    record: {
      campus: 'LX',
      appointment_time: ''
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
      this.axios
        .post(this.Const + 'wechat/', {
          ...this.record
        })
        .then(({ data }) => {
          console.log(data)
          this.$router.push('/success')
        })
        .catch(() => {
          this.toast('Oops! 我们遇到了一些技术问题')
        })
    }
  }
}
</script>

<style>
</style>