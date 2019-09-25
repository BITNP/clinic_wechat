<template>
 <vue-event-calendar :events="events" :title="'容量详情'" >
        <template scope="props">
          <div v-for="(event, index) in props.showEvents" class="event-item" :key="index">
            <!-- In here do whatever you want, make you owner event template -->
            {{event}}
          </div>
        </template>
      </vue-event-calendar>
</template>

<script>
export default {
  data: () => ({
    demoEvents: [
      {
        date: '2019/09/25',
        title: 'eat',
        desc: 'longlonglong description'
      },
      {
        date: '2019/09/26',
        title: 'this is a title'
      }
    ]
  }),
  computed: {
    events () {
      return this.dates.map(v => ({title: v.name, date: v.start.replace('-', '/').replace('-', '/'), capacity: v.capacity, count: v.count, finish: v.finish}))
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
  props: ['dates']
}
</script>

<style>

</style>