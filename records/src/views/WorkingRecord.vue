<template>
  <div v-if="record">
    <RecordPreview :record="record" @edit-current-record="popup"></RecordPreview>
    <div v-transfer-dom>
      <popup v-model="display">
        <Popupcontent v-model="record" :create="false" />
      </popup>
    </div>
  </div>
</template>
<script>
import {Popup} from 'vux'
import RecordPreview from '@/components/RecordPreview'
import Popupcontent from '@/components/PopupContent'
export default {
  components: {
    Popup,
    RecordPreview,
    Popupcontent
  },
  data: () => ({
    display: false
  }),
  computed: {
    record () {
      return this.$store.state.workingRecord
    }
  },
  methods: {
    popup () {
      this.display = !this.display
    }
  }
}
</script>

<style>
</style>