<template>
  <div>
    <popup-header title="编辑当前工单"></popup-header>
    <card>
      <div slot="content">
        <RecordFrom v-model="record" :create="create" />
        <br />
      </div>

      <div slot="footer">
        <box gap="10px 10px">
          <x-button type="primary" @click.native="update">更新</x-button>

          <x-button type="warn" @click.native="toggleDelete" v-if="delete_confirm == false">删除</x-button>
          <template v-else>
            <br />

            <flexbox>
              <flexbox-item>
                <x-button type="default" @click.native="toggleDelete">取消</x-button>
              </flexbox-item>
              <flexbox-item>
                <x-button type="warn" @click.native="onDelete">确认删除</x-button>
              </flexbox-item>
            </flexbox>
          </template>
        </box>

        <br />
      </div>
    </card>
  </div>
</template>

<script>
import {
  Card,
  XButton,
  // XDialog,
  Flexbox,
  FlexboxItem,
  PopupHeader,
  Box
} from 'vux'

import RecordFrom from './RecordForm'

export default {
  components: {
    Card,
    XButton,
    // XDialog,
    Flexbox,
    FlexboxItem,
    PopupHeader,
    RecordFrom,
    Box
  },
  data: () => ({
    delete_confirm: false
  }),
  methods: {
    toggleDelete (event) {
      this.delete_confirm = !this.delete_confirm
      return false
    },
    onDelete (event) {
      this.axios
        .delete(this.fixUrl(this.$props.record.url))
        .then(({data, statusCode}) => {
          this.$router.push({
            name: 'success',
            params: {
              title: '已成功删除工单'
            }
          })
          this.$store.commit('getWorkingRecord')
        })
    },
    update () {
      let record = { ...this.$props.record }
      let _record = {}
      for (const key in record) {
        if (record[key]) {
          _record[key] = record[key]
        }
      }
      this.axios
        .put(this.fixUrl(this.$props.record.url), { ..._record })
        .then(() => {
          this.$store.commit('popSuccess', '更新成功')
        })
        .catch((e) => {
          this.$store.commit('popError', 'Oops! 我们遇到了一些技术问题')
        })
    },
    fixUrl (url) {
      let res = url.match(/\/\d+\//)
      res = res[res.length - 1]
      res = res.slice(1, res.length - 1)
      let pk = parseInt(res)
      return `${this.Const}wechat/${pk}/`
    }
  },
  props: ['record', 'create'],
  model: {
    prop: 'record', // 绑定的值，通过父组件传递
    event: 'update' // 自定义事件名
  }
}
</script>

<style>
</style>