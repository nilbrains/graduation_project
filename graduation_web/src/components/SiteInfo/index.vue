<template>
  <div>
    <el-card class="statement-card nil-card">
      <div class="header clearfix" slot="header">
        <span>本站信息</span>
      </div>
      <div class="body">
        <div class="two_table">
          <div class="table-item">
            <div class="key"><i class="iconfont nil-prople"></i> 注册用户</div>
            <div class="value">{{ webinfo.user_count }}</div>
          </div>
          <div class="table-item">
            <div class="key"><i class="iconfont nil-zhibiao"></i> 文章数量</div>
            <div class="value">{{ webinfo.article_count }}</div>
          </div>
          <div class="table-item">
            <div class="key"><i class="iconfont nil-zhibiao"></i> 提问数量</div>
            <div class="value">{{ webinfo.answer_count }}</div>
          </div>
          <div class="table-item">
            <div class="key"><i class="iconfont nil-date"></i> 建站时间</div>
            <div class="value">{{ webinfo.web_site_up_time }}</div>
          </div>
          <div class="table-item">
            <div class="key"><i class="iconfont nil-beian"></i></div>
            <div class="value">{{ webinfo.web_site_record }}</div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getSettingWebInfo } from '@/api/setting'
export default {
  data() {
    return {
      webinfo: {
        user_count: 0,
        article_count: 0,
        answer_count: 0,
        web_site_up_time: '',
        web_site_record: '',
      },
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      getSettingWebInfo().then((res) => {
        this.webinfo = res.data
      })
    },
  },
}
</script>

<style lang="scss" scope>
.nil-card {
  margin-bottom: 10px;
  .header span {
    padding-bottom: 15px;
    border-bottom: 3px solid #7579e7;
  }
}

.two_table {
  padding: 0.2rem 1rem;
  .table-item {
    display: flex;
    padding: 4px 0 0;
    flex-direction: row;
    justify-content: space-between;
    .key {
      display: inline-block;
    }
    .value {
      display: inline-block;
    }
  }
}
</style>
