<template>
  <div>
    <inline-calendar ref="calendar" v-model="value" :disable-date-function="validDate"></inline-calendar>
    <br />

    <template v-for="(date, idx) in datesSelected">
      <DatePreview :date="date" :key="idx"></DatePreview>
    </template>
    <br />
    <template v-for="(c, idx) in campus">
      <flexbox v-if="dates.filter(v=>v.campus===c.name).length===0" :key="idx">
        <flexbox-item>
          <div class="flex-demo">{{c.name}}未来没有可预约时间</div>
        </flexbox-item>
      </flexbox>
    </template>
  </div>
</template>

<script>
import { InlineCalendar, XTable, Divider, Flexbox, FlexboxItem } from 'vux'
import DatePreview from '@/components/DatePreview'
export default {
  components: {
    InlineCalendar,
    XTable,
    Divider,
    Flexbox,
    FlexboxItem,
    DatePreview
  },
  data: () => ({ value: null }),
  computed: {
    events () {
      return this.dates.map(v => ({
        title: v.title,
        date: v.date,
        capacity: v.capacity,
        count: v.count,
        finish: v.finish,
        campus: v.campus,
        address: this.getAddress(v.campus),
        startTime: v.startTime,
        endTime: v.endTime,
        working: v.working
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
        return this.events.filter(v => v.date === this.value)
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
      this.value = this.dates[0].date
    }
  }
}
</script>

<style lang="less">
.flex-demo {
  text-align: center;
  border-radius: 4px;
  background-clip: padding-box;
}
</style>