<template>
  <div>
    <Panel header="请点击以查看具体内容" :list="ordered_announcements" type="2" @on-click-item="clickItem"></Panel>
    <br />
    <div v-transfer-dom>
      <popup v-model="showpopup" position="right" width="100%">
        <box gap="10px 10px">
          <group >
            <cell class="cell-grey" title="标题" :value="selectedItem.title"></cell >
            <cell class="cell-grey" title="公告类型" :value="selectedItem.tag"></cell >
            <cell class="cell-grey" title="最后修改时间" :value="selectedItem.lastEditedTime"></cell >
          </group>
          <div class="card-padding">
            <vue-markdown :show="selectedItem.content.length>0" :source="selectedItem.content"></vue-markdown>
          </div>

          <div class="position-horizontal-demo">
            <x-button plain @click.native="showpopup = false" type="primary">确认</x-button>
          </div>
        </box>
      </popup>
    </div>
    <divider>网络开拓者协会 - 电脑诊所</divider>
  </div>
</template>

<script>
import { Card, XButton, Divider, Cell, Group, Panel, Popup, Box } from 'vux'
import VueMarkdown from 'vue-markdown'

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
    Card,
    XButton,
    Divider,
    Cell,
    Group,
    VueMarkdown,
    Panel,
    Popup,
    Box
  },
  data: () => ({
    selectedItem: {
      content: ''
    },
    showpopup: false,
    announcements_order: '优先级',
    ANNOUNCEMENTS_MAP: {
      TOC: '免责声明',
      AN: '普通公告'
    },
    ANNOUNCEMENTS_ORDER_OPTIONS: [
      '优先级',
      '最新创建',
      '最早创建',
      '最新修改',
      '最早修改',
      '最晚过期',
      '最早过期'
    ]
  }),
  computed: {
    ordered_announcements () {
      let sortby
      if (this.announcements_order === '优先级') {
        sortby = cmpHelper('priority', true)
      } else if (this.announcements_order === '最新创建') {
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
      let announcements = this.$store.state.announcements
      announcements = announcements.map(v => ({
        title: v.title,
        desc: `[${this.ANNOUNCEMENTS_MAP[v.tag]}]` + ' ' + v.brief,
        content: v.content,
        lastEditedTime: new Date(v.lastEditedTime).toLocaleString(),
        tag: this.ANNOUNCEMENTS_MAP[v.tag]
      }))
      return announcements.sort(sortby)
    }
  },
  methods: {
    clickItem (item) {
      this.showpopup = true
      this.selectedItem = item
    }
  }
}
</script>

<style  lang="less" scoped>

.cell-grey{
  background-color: #eee;
}

.card-padding {
  padding: 15px;
  margin: 30px;
}
</style>