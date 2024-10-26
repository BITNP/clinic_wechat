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
    display: false,
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
    this.record.campus = '良乡校区-图书馆'
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
