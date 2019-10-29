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
    <TOS v-model="toc" @toggle-toc="toc = false" />
  </div>
</template>

<script>
import RecordForm from '@/components/RecordForm'
import TOS from '@/components/TOS'
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
    TOS
  },
  data: () => ({
    agree: false,
    toc: false,
    dates: [],
    record: {
      campus: ''
    }
  }),
  methods: {
    submit () {
      if (
        !(
          this.record.campus &&
          this.record.appointment_time &&
          this.record.description
        )
      ) {
        this.$store.commit('popError', '有选项仍未填完')
        return
      }
      if (this.agree === false) {
        this.$store.commit('popError', '请在提交前先阅读并同意我们的免责声明')
        return
      }
      this.axios
        .post(this.Const + 'wechat/', {
          ...this.record
        })
        .then(({ data }) => {
          this.$router.push('/success')
        })
        .catch(({ response }) => {
          if (
            response &&
            response.data && typeof (response.data) === 'object'
          ) {
            this.$store.commit('popError', response.data[Object.keys(response.data)[0]])
          } else {
            this.$store.commit('popError', 'Oops! 我们遇到了一些技术问题')
          }
        })
    }
  },
  computed: {
    campus () {
      return this.$store.state.campus
    }
  },
  mounted () {
    if (this.campus && this.campus.length > 0) {
      this.record.campus = this.$store.state.campus[0].name
    }
  }
}
</script>

<style>
</style>