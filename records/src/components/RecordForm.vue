<template>
  <div>
    <group title="必填项">
      <selector
        title="校区"
        required
        v-model="record.campus"
        :options="campus"
        direction="rtl"
        @change.native="delete record.appointment_time"
      ></selector>
      <calendar
        :readonly="!create"
        title="预约时间"
        required
        v-model="record.appointment_time"
        :disable-date-function="validDate"
        placeholder="请选择一个日期，可选表示正常营业"
      ></calendar>
      <cell v-if="selectedDate" title="服务时间" inline-desc="请仔细查看诊所日历获取相应信息" :value="selectedDate?`${selectedDate.startTime} - ${selectedDate.endTime}`:''"></cell>
      <x-textarea
        title="问题描述"
        placeholder="请尽可能详细地描述您所遇到的问题"
        v-model="record.description"
        required
        :max="600"
      ></x-textarea>
    </group>
    <group title="选填项，这将有助于我们提供更好的服务">
      <x-input title="电脑型号" text-align="right" v-model="record.model"
      :max="200"></x-input>
      <x-input title="手机号" is-type="china-mobile" text-align="right" v-model="record.phone_num"></x-input>
      <x-input title="真实姓名" text-align="right" v-model="record.realname" :max="50"></x-input>
      <x-input
        title="电脑开机密码"
        placeholder="帮助我们唤醒休眠的电脑"
        text-align="right"
        :max="256"
        v-model="record.password"
      ></x-input>
    </group>
  </div>
</template>

<script>
import { Group, Calendar, XInput, Selector, XTextarea, Cell } from 'vux'

export default {
  components: {
    Group,
    Calendar,
    XInput,
    Selector,
    XTextarea,
    Cell
  },
  props: ['record', 'create'],
  data: () => ({}),
  model: {
    prop: 'record', // 绑定的值，通过父组件传递
    event: 'update' // 自定义事件名
  },
  methods: {
    validDate (date) {
      // return false if valid
      return !this.dates.includes(date.formatedDate)
    }
  },
  watch: {},
  computed: {
    campus () {
      let campus = this.$store.state.campus
      return campus.map(v => ({ key: v.name, value: v.name }))
    },
    dates () {
      return this.$store.state.dates
        .filter(v => v.campus === this.record.campus)
        .filter(v => v.capacity > v.count)
        .map(date => date.date)
    },
    selectedDate () {
      let dates = this.$store.state.dates
      return dates.filter(v => v.campus === this.record.campus && v.date === this.record.appointment_time)[0]
    }
  }
}
</script>

<style>
</style>
