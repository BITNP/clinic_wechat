<template>
  <div>
    <Flow>
      <flow-state state="1" title="创建工单" is-done></flow-state>
      <flow-line :is-done="[0,2,3,4,5,6,7,8].includes(record.status)" tip="正在审核"></flow-line>

      <flow-state state="2" title="审核通过" :is-done="[0,2,4,5,6,7,8].includes(record.status)"></flow-state>
      <flow-line
        :is-done="[3,5,6,7,8].includes(record.status)"
        :tip="[0, 2, 4].includes(record.status)?'可以前往诊所' : ''"
      ></flow-line>

      <flow-state state="3" title="已到诊所" :is-done="[5,6,7,8].includes(record.status)"></flow-state>
      <flow-line
        :is-done="[3,6,7,8].includes(record.status)"
        :tip="[5].includes(record.status)?'正在服务':''"
      ></flow-line>

      <flow-state state="4" title="完成服务" :is-done="FINISHED_STATUS.includes(record.status)"></flow-state>
    </Flow>
    <FormPreview
      header-label="工单状态"
      :header-value="STATUS_MAP[record.status]"
      :body-items="dict2list(record)"
      :footer-buttons="WORKING_STATUS.includes(record.status)?dict2button(record):[]"
    />
    <br />
  </div>
</template>

<script>
import { Flow, FlowState, FlowLine, FormPreview } from 'vux'
export default {
  components: { Flow, FlowState, FlowLine, FormPreview },
  data: () => ({
    WORKING_STATUS: [0, 1, 2, 4, 5],
    FINISHED_STATUS: [3, 6, 7, 8],
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
      reject_reason: '工单申请驳回原因',
      status: '工单状态',
      url: 'URL',
      worker_description: '工作人员对问题描述',
      password: '电脑开机密码'
    }
  }),
  methods: {
    dict2list (data) {
      let keys = [
        'appointment_time',
        'description',
        'model',
        'password',
        'campus',
        'phone_num',
        'realname',
        'workder_description',
        'reject_reason'
      ]
      let ret = []
      ret.push({ label: '工单号', value: this.getId(data.url) })
      for (let key of keys) {
        if (data[key]) {
          ret.push({ label: this.KEY_TRANSLATION[key], value: data[key] })
        }
      }

      return ret
    },
    dict2button (data) {
      return [
        {
          style: 'primary',
          text: '编辑',
          onButtonClick: () => this.$emit('edit-current-record')
        }
      ]
    },
    getId (url) {
      let res = url.match(/\/\d+\//)
      res = res[res.length - 1]
      res = res.slice(1, res.length - 1)
      return res.padStart(8, '0')
    }
  },
  props: ['record']
}
</script>

<style>
</style>