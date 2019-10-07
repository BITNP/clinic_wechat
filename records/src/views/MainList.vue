<template>
  <div @touchstart="touchStart" @touchend="touchEnd">
    <div>
      <tab>
        <tab-item
          v-for="(tabTitle, index) in tabTitles"
          :key="index"
          @on-item-click="tabNum = index"
          :selected="index == tabNum"
        >{{tabTitle}}</tab-item>
      </tab>
    </div>
    <!-- [more]:{{ debug_more }} -->
    <!-- <br /> -->
    <!-- [announcements]:{{ debug_announcements }} -->
    <!-- <br /> -->
    <!-- [request]:{{ debug_request }} -->
    <!-- <br /> -->
    <!-- [request_data]:{{ debug_request_data}} -->
    <template v-if="[1,2].includes(tabNum)">
      <!-- {{filtered_records}} -->
      <template v-for="(d,index) in filtered_records">
        <RecordPreview :key="index" :record="d" @edit-current-record="popup([d,index])"></RecordPreview>
        <!-- <Flow :key="index + 'c'">
          <flow-state state="1" title="创建工单" is-done></flow-state>
          <flow-line is-done></flow-line>

          <flow-state state="2" title="审核通过" is-done>
          </flow-state>
          <flow-line tip="正在完成"></flow-line>

          <flow-state state="3" title="已到诊所" is-done></flow-state>
          <flow-line></flow-line>

          <flow-state state="4" title="完成服务"></flow-state>
        </Flow>
        <FormPreview
          header-label="工单状态"
          :key="index+'fp'"
          :header-value="STATUS_MAP[d.status]"
          :body-items="dict2list(d)"
          :footer-buttons="WORKING_STATUS.includes(d.status)?dict2button(d):[]"
          :name="[d,index]"
        />
        <br :key="index + 'd'" /> -->
      </template>

      <infinite-loading @infinite="loadMore" spinner="circles">
        <template slot="no-results">
          <divider>网络开拓者协会 - 电脑诊所</divider>
        </template>
        <template slot="no-more">
          <divider>网络开拓者协会 - 电脑诊所</divider>
        </template>
      </infinite-loading>

      <br />
      <Box
        gap="10px 10px"
        v-if="tabNum == 1 && filtered_records.filter(v=>WORKING_STATUS.includes(v.status)).length == 0"
      >
        <x-button type="primary" action-type="button" link="/new">新建工单</x-button>
      </Box>
      <br />

      <br />
      <div v-transfer-dom>
        <popup v-model="display">
          <Popupcontent v-model="record" :create="false" />
        </popup>
      </div>
    </template>
    <template v-else-if="tabNum == 3">
      <group>
        <popup-radio
          title="排序方式"
          :options="ANNOUNCEMENTS_ORDER_OPTIONS"
          v-model="announcements_order"
        ></popup-radio>
      </group>
      <template v-for="(ann,idx) in ordered_announcements">
        <Card :key="idx+'c'">
          <div slot="header">
            <group>
              <Cell title="标题" :value="ann.title"></Cell>
              <Cell title="公告类别" :value="ann.title"></Cell>
            </group>
          </div>
          <template slot="content">
            <group>
              <!-- 可以修改为可折叠的？ -->
              <Cell title="内容">
                <!-- {{ann.show}}
                <x-icon type="ios-arrow-down" size="30" v-if="ann.show"></x-icon>-->
                <!-- <x-icon type="ios-arrow-up" size="30"></x-icon> -->
              </Cell>
            </group>
            <p slot="content" class="card-padding">
              <vue-markdown :show="ann.content.length>0" :source="ann.content"></vue-markdown>
            </p>
          </template>

          <div slot="footer">
            <group>
              <Cell title="最后更新时间" :value="new Date(ann.lastEditedTime).toLocaleDateString()"></Cell>
              <Cell title="过期时间" :value="new Date(ann.expireDate).toLocaleDateString()"></Cell>
            </group>
          </div>
        </Card>
        <br :key="idx+'b'" />
      </template>
      <divider>网络开拓者协会 - 电脑诊所</divider>
    </template>
    <template v-else>
      <Calendar :dates="dates" />
    </template>
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
import VueMarkdown from 'vue-markdown'
import Calendar from '@/components/Calendar'
import RecordPreview from '@/components/RecordPreview'

let cmpHelper = function (attrName, less = false) {
  if (less) {
    return (a, b) => {
      if (a[attrName] > b[attrName]) {
        return 1
      } else if (a[attrName] < b[attrName]) {
        return -1
      } else return 0
    }
  } else {
    return (a, b) => {
      if (a[attrName] > b[attrName]) {
        return -1
      } else if (a[attrName] < b[attrName]) {
        return 1
      } else return 0
    }
  }
}

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
    VueMarkdown,
    Cell,
    Group,
    Calendar,
    PopupRadio,
    Flow,
    FlowState,
    FlowLine,
    RecordPreview
  },
  data: () => ({
    tabTitles: ['诊所服务日历', '正在处理', '已处理', '公告'],
    debug_more: '',
    debug_announcements: '',
    debug_request: '',
    debug_request_data: '',
    tabNum: 0,
    display_new_form: false,
    announcements_order: '最新创建',
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
    announcements: [],
    WORKING_STATUS: [0, 1, 2, 4, 5],
    FINISHED_STATUS: [3, 6, 7, 8],
    // STATUS_MAP: {
    //   0: '上单问题未解决',
    //   1: '预约待确认',
    //   2: '预约已确认',
    //   3: '预约已驳回',
    //   4: '登记待受理',
    //   5: '正在处理',
    //   6: '已解决问题',
    //   7: '建议返厂',
    //   8: '交给明天解决',
    //   _: 'error'
    // },
    // KEY_TRANSLATION: {
    //   appointment_time: '预约时间',
    //   campus: '校区',
    //   description: '问题描述',
    //   model: '电脑型号',
    //   phone_num: '手机号',
    //   realname: '真实姓名',
    //   reject_reason: '工单申请驳回原因',
    //   status: '工单状态',
    //   url: 'URL',
    //   worker_description: '工作人员对问题描述',
    //   password: '电脑开机密码'
    // },
    CAMPUS_MAP: {
      LX: '良乡',
      ZGC: '中关村'
    },
    ANNOUNCEMENTS_MAP: {
      TOC: '免责声明',
      CM: '普通公告'
    },
    ANNOUNCEMENTS_ORDER_OPTIONS: [
      '最新创建',
      '最早创建',
      '最新修改',
      '最早修改',
      '最晚过期',
      '最早过期'
    ]
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
    },
    ordered_announcements () {
      let sortby
      if (this.announcements_order === '最新创建') {
        sortby = cmpHelper('createdTime')
      } else if (this.announcements_order === '最早创建') {
        sortby = cmpHelper('createdTime', true)
      } else if (this.announcements_order === '最新修改') {
        sortby = cmpHelper('lastEditedTime')
      } else if (this.announcements_order === '最早修改') {
        sortby = cmpHelper('lastEditedTime', true)
      } else if (this.announcements_order === '最晚过期') {
        sortby = cmpHelper('expireTime')
      } else {
        // 最早过期
        sortby = cmpHelper('expireTime', true)
      }
      return this.announcements.sort(sortby)
    }
  },
  methods: {
    touchStart (e) {
      this.touch.x = e.touches[0].clientX
    },
    touchEnd (e) {
      const threshold = 150
      let endX = e.changedTouches[0].clientX
      if (endX - this.touch.x > threshold) {
        this.tabNum -= 1
      } else if (endX - this.touch.x < -threshold) {
        this.tabNum += 1
      }
      this.tabNum =
        (this.tabNum + this.tabTitles.length) % this.tabTitles.length
    },
    // dict2list (data) {
    //   let keys = [
    //     'appointment_time',
    //     'description',
    //     'model',
    //     'password',
    //     'campus',
    //     'phone_num',
    //     'realname',
    //     'workder_description',
    //     'reject_reason'
    //   ]
    //   let ret = []
    //   ret.push({ label: '工单号', value: this.getId(data.url) })
    //   for (let key of keys) {
    //     if (data[key]) {
    //       ret.push({ label: this.KEY_TRANSLATION[key], value: data[key] })
    //     }
    //   }

    //   return ret
    // },
    // dict2button (data) {
    //   return [{ style: 'primary', text: '编辑', onButtonClick: this.popup }]
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
      this.$http.get(this.next).then(({ data }) => {
        console.log(data)

        this.debug_more = data
        this.all_records = this.all_records.concat(data.results)
        this.next = data.next
        if (this.next) {
          $state.loaded()
        } else {
          // $state.loaded()
          $state.complete()
        }
      })
    },
    // getId (url) {
    //   let res = url.match(/\/\d+\//)
    //   res = res[res.length - 1]
    //   res = res.slice(1, res.length - 1)
    //   return res.padStart(8, '0')
    // },
    initRecords () {
      this.next = this.Const + 'wechat/'
    },
    initAnnouncements () {
      console.log('init announce')
      this.$http
        .get(`${this.Const}announcement/`)
        .then(({ data }) => {
          this.debug_announcements = data
          data.map(v => {
            v.show = false
          })
          this.announcements = data
          console.log(this.announcements)
        })
        .catch(e => {
          if (e.request && e.request.data) {
            this.debug_request = e.request
            this.debug_request_data = e.request.data
          } else if (e && e.request) {
            this.debug_request = e
            this.debug_request_data = e.request
          } else {
            this.debug_request = e
          }
        })
    }
  },
  created () {
    // 放到Store里，还可以进行性能优化
    this.initRecords()
    this.initAnnouncements()
  },
  mounted () {
    this.record.campus = '良乡校区'
  }
}
</script>

<style>
.vux-demo {
  text-align: center;
}
.logo {
  width: 100px;
  height: 100px;
}
.card-padding {
  padding: 15px;
  margin: 40px;
}
</style>
