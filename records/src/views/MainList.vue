<template>
  <!-- <div @touchstart="touchStart" @touchend="touchEnd"> -->
  <div class="page">
    <div class="page-header">
      <tab>
        <tab-item
          v-for="(tabTitle, index) in tabTitles"
          :key="index"
          @on-item-click="tabNum = index; display = false"
          :selected="index == tabNum"
        >{{tabTitle}}</tab-item>
      </tab>
    </div>
    <div style=";width:100%;">
      <tab></tab>
    </div>

    <div class="page-content">
      <TopAnnouncements></TopAnnouncements>

      <template v-if="[1,2].includes(tabNum)">
        <template v-for="(d,index) in filtered_records">
          <RecordPreview :key="index" :record="d" @edit-current-record="popup([d,index])"></RecordPreview>
        </template>

        <br />

        <br />

        <infinite-loading @infinite="loadMore" spinner="circles">
          <template slot="no-results">
            <divider>网络开拓者协会 - 电脑诊所</divider>
          </template>
          <template slot="no-more">
            <divider>网络开拓者协会 - 电脑诊所</divider>
          </template>
        </infinite-loading>

        <br />
        <div v-transfer-dom>
          <popup v-model="display">
            <Popupcontent v-model="record" :create="false" />
          </popup>
        </div>
      </template>
      <template v-else-if="tabNum == 3">
        <Announcements></Announcements>
      </template>
      <template v-else>
        <Calendar :dates="dates" />
      </template>
    </div>


    <div class="page-footer">
      <Box
        gap="10px 10px"
        v-if="all_records.filter(v=>WORKING_STATUS.includes(v.status)).length == 0"
      >
        <x-button style=";" type="primary" action-type="button" link="/new">新建工单</x-button>
      </Box>
    </div>
  </div>
</template>

<script>
import {
  Popup,
  FormPreview,
  Tab,
  TabItem,
  XHeader,
  Card,
  XButton,
  Divider,
  Box,
  Cell,
  Group,
  PopupRadio,
  Flow,
  FlowState,
  FlowLine
} from 'vux'
import InfiniteLoading from 'vue-infinite-loading'
import Popupcontent from '@/components/PopupContent'
import Calendar from '@/components/Calendar'
import RecordPreview from '@/components/RecordPreview'
import Announcements from '@/components/Announcements'
import TopAnnouncements from '@/components/TopAnnouncements'

export default {
  components: {
    FormPreview,
    Tab,
    TabItem,
    XHeader,
    Card,
    Popup,
    Popupcontent,
    XButton,
    Divider,
    InfiniteLoading,
    Box,
    Cell,
    Group,
    Calendar,
    PopupRadio,
    Flow,
    FlowState,
    FlowLine,
    RecordPreview,
    Announcements,
    TopAnnouncements
  },
  data: () => ({
    tabTitles: ['诊所服务日历', '正在处理', '已处理', '公告'],
    tabNum: 0,
    display_new_form: false,
    scroll: false,
    display: false,
    dates: [],
    next: '',
    touch: {
      x: -1
    },
    record: {
      campus: '',
      appointment_time: new Date().toISOString().substr(0, 10)
    },
    record_index: -1,
    all_records: [],
    WORKING_STATUS: [0, 1, 2, 4, 5],
    FINISHED_STATUS: [3, 6, 7, 8],
    CAMPUS_MAP: {
      LX: '良乡',
      ZGC: '中关村'
    }
  }),
  computed: {
    filtered_records () {
      if (this.tabNum === 1) {
        return this.all_records.filter(r =>
          this.WORKING_STATUS.includes(r.status)
        )
      } else {
        return this.all_records.filter(
          r => !this.WORKING_STATUS.includes(r.status)
        )
      }
    }
  },
  methods: {
    // touchStart (e) {
    //   this.touch.x = e.touches[0].clientX
    // },
    // touchEnd (e) {
    //   const threshold = 75
    //   let endX = e.changedTouches[0].clientX
    //   if (endX - this.touch.x > threshold) {
    //     this.tabNum -= 1
    //   } else if (endX - this.touch.x < -threshold) {
    //     this.tabNum += 1
    //   }
    //   this.tabNum =
    //     (this.tabNum + this.tabTitles.length) % this.tabTitles.length
    // },
    popup (name) {
      this.display = !this.display
      this.record = name[0]
      this.record_index = name[1]
    },
    loadMore ($state) {
      if (!this.next) {
        $state.complete()
        return
      }
      this.$http
        .get(this.next)
        .then(({ data }) => {
          if (typeof data === 'string') {
            // 如果返回了string，表示服务端可能出现错误
            console.error(data)
            this.$store.commit('popError', 'Oops! 我们的服务器出现了一些问题')
            return
          }
          // this.debug_more = data
          this.all_records = this.all_records.concat(data.results)
          this.next = data.next
          if (this.next) {
            $state.loaded()
          } else {
            // $state.loaded()
            $state.complete()
          }
        })
        .catch(e => {
          console.error(e)
          this.$store.commit('popError', '无法获取数据')
        })
    },
    initRecords () {
      this.next = this.Const + 'wechat/'
    }
  },
  created () {
    // 放到Store里，还可以进行性能优化
    this.initRecords()
    this.$store.commit('initAnnouncements')
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
  z-index: 2;
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
  z-index: 999;
}
</style>
