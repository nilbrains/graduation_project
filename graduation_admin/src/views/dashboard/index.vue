<template>
  <div class="dashboard-container">
    <avue-data-box :option="option" />
    <div class="dashboard-text">欢迎管理员 {{ name }}</div>
  </div>
</template>

<script>
import { getSettingWebInfo } from '@/api/setting'
import { mapGetters } from 'vuex'

export default {
  name: 'Dashboard',
  data() {
    return {
      option: {
        span: 8,
        data: [
          {
            title: '用户数量',
            count: 0,
            icon: 'el-icon-user',
            color: 'rgb(49, 180, 141)'
          },
          {
            title: '文章数量',
            count: 0,
            icon: 'el-icon-document',
            color: 'rgb(56, 161, 242)'
          },
          {
            title: '提问数量',
            count: 0,
            icon: 'el-icon-umbrella',
            color: 'rgb(117, 56, 199)'
          }
        ]
      }
    }
  },
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  created() {
    this.getinfo()
  },
  methods: {
    getinfo() {
      getSettingWebInfo().then(res => {
        this.option.data[0].count = res.data.user_count
        this.option.data[1].count = res.data.article_count
        this.option.data[2].count = res.data.answer_count
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
