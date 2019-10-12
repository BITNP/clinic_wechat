<template>
  <div>
    <transition name="switch" mode="out-in">
      <template v-for="(ta,idx) in topAnnouncements">
        <div
          :key="idx"
          v-if="idx === index"
          style="margin: 3px 10px; text-align:center; height:40px;"
        > {{topAnnouncements[idx].content}}</div>
      </template>
    </transition>
  </div>
</template>

<script>
export default {
  data: () => ({
    index: 0
    // topAnnouncements: ['123123', '213132']
  }),
  computed: {
    topAnnouncements () {
      return this.$store.state.announcements.filter(v => v.tag === 'TA')
    }
  },
  mounted () {
    setInterval(() => {
      this.index = (this.index + 1) % this.topAnnouncements.length
    }, 5000)
  }
}
</script>

<style>
.switch-enter-active {
  transition: all 0.8s ease;
}

.switch-leave-active {
  transition: all 0.8s cubic-bezier(1, 0.5, 0.8, 1);
}

.switch-enter {
  transform: translateX(15px);
  opacity: 0;
}

.switch-leave-to {
  transform: translateX(-15px);
  opacity: 0;
}
</style>