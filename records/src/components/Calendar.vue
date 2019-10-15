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
            <td colspan="2">{{getAddress(date.campus)}}</td>
            <td colspan="2">
              {{date.startTime.slice(0,5)}} ~ {{
              date.endTime.slice(0,5)}}
            </td>
          </tr>
        </template>
      </tbody>
    </x-table>
    <br />
    <template v-for="(c, idx) in campus">
      <flexbox v-if="dates.filter(v=>v.campus===c.name).length===0" :key="idx">
        <flexbox-item>
          <div class="flex-demo">{{c.name}} 未来没有可预约时间</div>
        </flexbox-item>
      </flexbox>
    </template>
    <br />

    <Divider>网络开拓者协会 - 电脑诊所</Divider>
  </div>
</template>

<script>
import { InlineCalendar, XTable, Divider, Flexbox, FlexboxItem } from 'vux';
export default {
  components: { InlineCalendar, XTable, Divider, Flexbox, FlexboxItem },
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
    campus () {
      return this.$store.state.campus
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
    validDate (date) {
      // return false if valid
      return !this.dates.map(date => date.date).includes(date.formatedDate)
    },
    getAddress (campusName) {
      return this.campus.filter(v => v.name === campusName)[0]['address']
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
.flex-demo {
  text-align: center;
  border-radius: 4px;
  background-clip: padding-box;
}
</style>