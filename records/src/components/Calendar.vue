<template>
  <vue-event-calendar :events="events" :title="'容量详情'">
    <template slot-scope="props">
      <div v-for="(event, index) in props.showEvents" class="event-item" :key="index">
        <!-- In here do whatever you want, make you owner event template -->
        {{event.campus}} - {{event.title}}
        <br />
        总容量：{{event.capacity}}，已预约：{{event.count}}，已完成：{{event.finish}}
      </div>
    </template>
  </vue-event-calendar>
</template>

<script>
export default {
  data: () => ({}),
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
    }
  },
  methods: {
    monthChange (month) {
      console.log(month)
    },
    dayChange (day) {
      console.log(day)
    }
  },
  mounted () {
    this.$EventCalendar.toDate(new Date().toLocaleDateString())
  }
}
</script>

<style>
</style>