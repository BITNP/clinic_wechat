<template>
  <div>
    <RecordPreview
      v-for="(d,index) in records"
      :key="index"
      :record="d"
      @edit-current-record="popup([d,index])"
    ></RecordPreview>
    <infinite-loading @infinite="loadMore" spinner="circles">
      <template slot="no-results">
        <div></div>
      </template>
      <template slot="no-more">
        <div></div>
      </template>
    </infinite-loading>
  </div>
</template>

<script>
import RecordPreview from '@/components/RecordPreview'
import InfiniteLoading from 'vue-infinite-loading'
export default {
  components: {
    RecordPreview,
    InfiniteLoading
  },
  data: () => ({
    next: undefined,
    records: [],
    WORKING_STATUS: [0, 1, 2, 4, 5],
    FINISHED_STATUS: [3, 6, 7, 8]
  }),
  computed: {
  },

  methods: {
    initRecords () {
      if (this.next === undefined) { this.next = this.Const + 'wechat/finish/' }
    },

    loadMore ($state) {
      if ($state && !this.next) {
        $state.complete()
        return
      }
      this.$http
        .get(this.next)
        .then(({ data }) => {
          if (typeof data === 'string') {
            // 如果返回了string，表示服务端可能出现错误
            this.$store.commit('popError', 'Oops! 我们的服务器出现了一些问题')
            return
          }
          this.records = this.records.concat(data.results)
          this.next = data.next
          if ($state && this.next) {
            $state.loaded()
          } else if ($state) {
            $state.complete()
          }
        })
    }
  },
  mounted () {
    this.initRecords()
  }
}
</script>

<style>
</style>