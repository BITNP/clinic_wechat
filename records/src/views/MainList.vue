<template>
  <div>
    <tab>
      <tab-item @on-item-click="tabNum = 0" selected>正在处理</tab-item>
      <tab-item @on-item-click="tabNum = 1">已处理</tab-item>
    </tab>
    <template v-for="(d,index) in filtered_records">
      <!-- {{d}} -->
      <FormPreview
        header-label="工单状态"
        :key="index"
        :header-value="STATUS_MAP[d.status]"
        :body-items="dict2list(d)"
        :footer-buttons="d.status<=5?dict2button(d):[]"
        :name="[d,index]"
      />
      <br :key="index + 'd'" />
    </template>

    <infinite-loading @infinite="loadMore" spinner="circles">
      <template slot="no-results">
        <divider>没有更多数据咧</divider>
      </template>
      <template slot="no-more">
        <divider>已经到底啦 :)</divider>
      </template>
    </infinite-loading>

    <br />
    <Box gap="10px 10px" v-if="tabNum == 0">
      <x-button type="primary" action-type="button" link="/new">新建工单</x-button>
    </Box>
    <br />

    <br />
    <div v-transfer-dom>
      <popup v-model="display">
        <Popupcontent v-model="record" :create="false" />
      </popup>
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
  Box
} from 'vux'
import InfiniteLoading from 'vue-infinite-loading'
import Popupcontent from '@/components/PopupContent'

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
    Box
  },
  data: () => ({
    tabNum: 0,
    display_new_form: false,
    scroll: false,
    display: false,
    next: '',
    record: {
      campus: 'LX',
      appointment_time: new Date().toISOString().substr(0, 10)
    },
    record_index: -1,
    all_records: [],
    STATUS_MAP: {
      0: '上单问题未解决',
      1: '预约待确认',
      2: '预约已确认',
      3: '预约已驳回',
      4: '登记待受理',
      5: '正在处理',
      6: '已解决问题',
      7: '建议返厂',
      8: '交给明天解决',
      _: 'error'
    },
    KEY_TRANSLATION: {
      appointment_time: '预约时间',
      campus: '校区',
      description: '问题描述',
      model: '电脑型号',
      phone_num: '手机号',
      realname: '真实姓名',
      reject_reason: '拒绝原因',
      status: '工单状态',
      url: 'URL',
      worker_description: '工作人员对问题描述'
    },
    CAMPUS_MAP: {
      LX: '良乡',
      ZGC: '中关村'
    }
  }),
  computed: {
    filtered_records () {
      if (this.tabNum === 0) {
        return this.all_records.filter(r => r.status <= 5)
      } else {
        return this.all_records.filter(r => r.status > 5)
      }
    }
  },
  methods: {
    dict2list (data) {
      let keys = [
        'appointment_time',
        'description',
        'model',
        'phone_num',
        'realname',
        'workder_description'
      ]
      let ret = []
      ret.push({ label: '工单号', value: this.getId(data.url) })
      ret.push({ label: '校区', value: this.CAMPUS_MAP[data.campus] })
      for (let key of keys) {
        ret.push({ label: this.KEY_TRANSLATION[key], value: data[key] })
      }

      return ret
    },
    dict2button (data) {
      return [{ style: 'primary', text: '编辑', onButtonClick: this.popup }]
    },
    popup (name) {
      console.log(name)
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
    getId (url) {
      let res = url.match(/\/\d+\//)
      res = res[res.length - 1]
      res = res.slice(1, res.length - 1)
      return res.padStart(8, '0')
    },
    initRecords () {
      this.next = this.Const + 'wechat/'
    }
  },
  mounted () {
    this.initRecords()
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
</style>
