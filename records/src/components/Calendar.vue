<template>
  <div>
    <inline-calendar ref="calendar" v-model="value" :disable-date-function="validDate"></inline-calendar>
    <br />
    <x-table
      :cell-bordered="false"
      :content-bordered="false"
      style="background-color:#fff;"
      v-if="value"
    >
      <thead>
        <tr style="background-color: #F7F7F7">
          <th>校区名称</th>
          <th>容量</th>
          <th>已预约</th>
          <th>已完成</th>
        </tr>
      </thead>
      <tbody>
        <template v-for="(date, idx) in datesSelected">
          <tr :key="idx">
            <td>{{date.campus}}</td>
            <td>{{date.capacity}}</td>
            <td>{{date.count}}</td>
            <td>{{date.finish}}</td>
          </tr>
          <tr :key="idx+'1'">
            <td colspan="4">
              {{new Date('0001-01-01 '+date.startTime).toLocaleTimeString()}} ~ {{
              new Date('0001-01-01 '+ date.endTime).toLocaleTimeString()}}
            </td>
          </tr>
        </template>
      </tbody>
    </x-table>
    <br />
    <Divider>网络开拓者协会 - 电脑诊所</Divider>
    <!-- {{value}}
    {{datesSelected}}-->
  </div>
  <!-- <vue-event-calendar :events="events" :title="'容量详情'">
    <template slot-scope="props">
      <div v-for="(event, index) in props.showEvents" class="event-item" :key="index">
        In here do whatever you want, make you owner event template
        {{event.campus}} - {{event.title}}
        <br />
        总容量：{{event.capacity}}，已预约：{{event.count}}，已完成：{{event.finish}}
      </div>
    </template>
  </vue-event-calendar>-->
</template>

<script>
import { InlineCalendar, XTable, Divider } from 'vux'
export default {
  components: { InlineCalendar, XTable, Divider },
  data: () => ({ value: null }),
  computed: {
    events () {
      return this.dates.map(v => ({
        title: v.title,
        date: v.date.replace('-', '/').replace('-', '/'),
        capacity: v.capacity,
        count: v.count,
        finish: v.finish,
        campus: v.campus
      }))
    },
    dates () {
      return this.$store.state.dates
    },
    datesSelected () {
      if (this.value) {
        return this.dates.filter(v => v.date === this.value)
      } else return []
    }
  },
  watch: {
    dates: function (val) {
      if (this.dates.length > 0) {
        this.value = this.dates[0].date
      }
    }
  },
  methods: {
    // monthChange (month) {
    //   console.log(month)
    // },
    // dayChange (day) {
    //   console.log(day)
    // },
    validDate (date) {
      // return false if valid
      return !this.dates.map(date => date.date).includes(date.formatedDate)
    }
  },
  mounted () {
    if (this.dates.length > 0) {
      this.value = this.dates[this.dates.length - 1].date
    }
  }
}
</script>

<style>
</style>