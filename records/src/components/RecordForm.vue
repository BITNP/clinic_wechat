<template>
  <div>
    
    <group title="必填项">
      <calendar title="预约时间" required v-model="record.appointment_time" disable-weekend disable-past></calendar>
      <x-input title="问题描述" placeholder="必填项" required text-align="right" v-model="record.description"></x-input>
      <selector title="校区" required v-model="record.campus" :options="CAMPUS_LIST" direction="rtl"></selector>
    </group>
    <group title="选填项，这将有助于我们提供更好的服务">
      <x-input title="电脑型号" text-align="right" v-model="record.model"></x-input>
      <x-input title="手机号" is-type="china-mobile" text-align="right" v-model="record.phone_num"></x-input>
      <x-input title="真实姓名" text-align="right" v-model="record.realname"></x-input>
    </group>
    <!-- <group>
    </group> -->
  </div>
</template>

<script>
import { Group, Calendar, XInput, Selector } from 'vux'

export default {
  components: {
    Group,
    Calendar,
    XInput,
    Selector
  },
  props: ['record'],
  data: () => ({
    CAMPUS_LIST: [{ key: 'LX', value: '良乡' }, { key: 'ZGC', value: '中关村' }]
  })
}
</script>

<style>
</style>