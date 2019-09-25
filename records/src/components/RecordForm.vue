<template>
  <div>
    {{ record }}
    <group title="必填项">
      <calendar
        :readonly="!create"
        title="预约时间"
        required
        v-model="record.appointment_time"
        :disable-date-function="validDate"
        placeholder="请选择一个工作中的日期"
      ></calendar>
      <x-textarea
        title="问题描述"
        placeholder="请描述您所遇到的问题"
        required
        :max="300"
        v-model="record.description"
      ></x-textarea>
      <selector title="校区" required v-model="record.campus" :options="CAMPUS_LIST" direction="rtl"></selector>
    </group>
    <group title="选填项，这将有助于我们提供更好的服务">
      <x-input title="电脑型号" text-align="right" v-model="record.model"></x-input>
      <x-input title="手机号" is-type="china-mobile" text-align="right" v-model="record.phone_num"></x-input>
      <x-input title="真实姓名" text-align="right" v-model="record.realname"></x-input>
      <x-input title="电脑开机密码" placeholder="如果您在维修电脑的过程中会离开诊所，请填写以帮助我们唤醒休眠的电脑" text-align="right" v-model="record.password"></x-input>
    </group>
  </div>
</template>

<script>
import { Group, Calendar, XInput, Selector, XTextarea } from 'vux'

export default {
  components: {
    Group,
    Calendar,
    XInput,
    Selector,
    XTextarea
  },
  props: ['record', 'create'],
  data: () => ({
    dates: [],
    CAMPUS_LIST: [{ key: 'LX', value: '良乡' }, { key: 'ZGC', value: '中关村' }]
  }),
  model: {
    prop: 'record', // 绑定的值，通过父组件传递
    event: 'update' // 自定义事件名
  },
  methods: {
    getDates () {
      this.axios.get(this.Const + 'date/').then(({ data }) => {
        this.dates = data.map(date => date.start)
      })
    },
    validDate (date) {
      // return false if valid
      return !this.dates.includes(date.formatedDate)
    }
  },

  mounted () {
    if (this.create) {
      this.getDates()
    }
  }
}
</script>

<style>
</style>