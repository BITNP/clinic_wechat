<template>
  <div class="page">
    <div class="page-header">
      <tab>
        <tab-item
          v-for="(tabTitle, index) in tabTitles"
          :key="index"
          @on-item-click="switchTab"
          :selected="index == tabNum"
        >{{tabTitle.title}}</tab-item>
      </tab>
    </div>
    <div style=";width:100%;">
      <tab></tab>
    </div>
    <div class="page-content">
      <TopAnnouncements></TopAnnouncements>
      <router-view></router-view>
      <!-- <template v-if="[2,3].includes(tabNum)">
        <template v-for="(d,index) in filtered_records">
          <RecordPreview :key="index" :record="d" @edit-current-record="popup([d,index])"></RecordPreview>
        </template>

        <infinite-loading @infinite="loadMore" spinner="circles">
          <template slot="no-results">
            <Footer />
          </template>
          <template slot="no-more">
            <Footer />
          </template>
        </infinite-loading>

        <br />
        <div v-transfer-dom>
          <popup v-model="display">
            <Popupcontent v-model="record" :create="false" />
          </popup>
        </div>
      </template>
      <template v-else-if="tabNum == 0">
        <Announcements></Announcements>
        <Footer></Footer>
      </template>
      <template v-else>
        <Calendar />
        <Footer />
      </template>-->
      <Footer />
    </div>

    <div v-if="!hasOne">
      <Box gap="10px 10px">
        <x-button></x-button>
      </Box>
    </div>
    <div class="page-footer" v-if="!hasOne">
      <Box gap="10px 10px">
        <x-button type="primary" action-type="button" link="/new">新建工单</x-button>
      </Box>
    </div>
  </div>
</template>

<script>
import {
  Tab,
  TabItem,
  Card,
  XButton,
  Box
} from 'vux'
// import Calendar from '@/components/Calendar'
// import RecordPreview from '@/components/RecordPreview'
// import Announcements from '@/components/Announcements'
import TopAnnouncements from '@/components/TopAnnouncements'
import Footer from '@/components/Footer'

export default {
  components: {
    Tab,
    TabItem,
    Card,
    XButton,
    Box,
    TopAnnouncements,
    Footer
  },
  data: () => ({
    tabTitles: [
      { title: '公告', to: {name: 'announcements'} },
      { title: '诊所服务日历', to: {name: 'calendar'} },
      { title: '正在处理', to: {name: 'working'} },
      { title: '已处理', to: {name: 'finish'} }
    ],
    tabNum: 0,
    // display_new_form: false,
    // scroll: false,
    display: false,
    // next: '',
    // touch: {
    //   x: -1
    // },
    record: {
      campus: '',
      appointment_time: new Date().toISOString().substr(0, 10)
    }
    // record_index: -1
  }),
  computed: {
    hasOne () {
      return this.working_record === null
    }
  },
  methods: {
    // popup (name) {
    //   this.display = !this.display
    //   this.record = name[0]
    //   this.record_index = name[1]
    // },
    // loadMore ($state) {
    //   if ($state && !this.next) {
    //     $state.complete()
    //     return
    //   }
    //   this.$http
    //     .get(this.next)
    //     .then(({ data }) => {
    //       if (typeof data === 'string') {
    //         // 如果返回了string，表示服务端可能出现错误
    //         this.$store.commit('popError', 'Oops! 我们的服务器出现了一些问题')
    //         return
    //       }
    //       this.all_records = this.all_records.concat(data.results)
    //       this.next = data.next
    //       if ($state && this.next) {
    //         $state.loaded()
    //       } else if ($state) {
    //         $state.complete()
    //       }
    //     })
    //     .catch(e => {
    //       console.log(e)
    //       this.$store.commit('popError', '无法获取数据')
    //     })
    // },
    // initRecords () {
    //   this.next = this.Const + 'wechat/'
    // },
    switchTab (index) {
      if (this.tabNum === index) {
        // 当前页面
        return
      }
      this.tabNum = index
      this.$router.push(this.tabTitles[index].to, () => {})
    }
  },
  created () {
    let name = this.$route.name
    this.tabNum = this.tabTitles.map(v => v.to.name).indexOf(name)
  },
  mounted () {
    this.record.campus = '良乡校区'
  }
}
</script>

<style>
html,
body,
.page {
  height: 100%;
}

body {
  overflow: wrap;
}

.page {
  display: flex;
  flex-direction: column;
}
.page-header {
  position: fixed;
  left: 0px;
  top: 0px;
  width: 100%;
  z-index: 10;
}
.page-content {
  margin-bottom: 42px;
}
.page-footer {
  position: fixed;
  left: 0px;
  bottom: 0px;
  width: 100%;
  background-color: #fff;
  z-index: 10;
}
</style>
