<template>
  <card>
    <div slot="header" class="container">
      <div class="container-left">{{date.campus}}</div>
      <div class="container-center"></div>
      <div class="container-right">{{date.address}}</div>
    </div>
    <div slot="content" class="card-flex card-content01">
      <div class="vux-1px-r">
        <span>{{date.capacity}}</span>
        <br />容量
      </div>
      <div class="vux-1px-r">
        <span>{{date.count}}</span>
        <br />已预约
      </div>
      <div class="vux-1px-r">
        <span>{{date.working}}</span>
        <br />正在服务
      </div>
      <div class="vux-1px-r">
        <span>{{date.title}}</span>
        <br />状态
      </div>
    </div>
    <div slot="footer" class="container">
            <div class="container-left">营业时间</div>
      <div class="container-center"></div>
      <div class="container-right">{{date.startTime}} - {{ date.endTime}}</div>
      <div style="text-align:center;"></div>
    </div>
  </card>
</template>

<script>
import { Card, XButton } from 'vux'

export default {
  components: {
    Card,
    XButton
  },
  props: ['date']
}
</script>

<style scoped lang="less">
@import "~vux/src/styles/1px.less";

.card-flex {
  display: flex;
}
.card-content01 {
  padding: 10px 0;
}
.card-padding {
  padding: 15px;
}
.card-flex > div {
  flex: 1;
  text-align: center;
  font-size: 15px;
}
.card-flex span {
  color: #f74c31;
}

.container {
  width: auto;
  display: flex;
  padding:0 20px;
  margin-top: 5px;
}
.container-left,
.container-right {
  width: auto;
  // background-color: red;
  // min-height: 500px;
}
.container-center {
  flex: 1;
  // min-height: 500px;
  // margin: 0 10px;
  // background-color: yellow;
}
</style>