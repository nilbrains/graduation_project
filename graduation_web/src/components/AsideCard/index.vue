<template>
  <div>
    <el-card class="statement-card nil-card">
      <div class="header clearfix" slot="header">
        <span>社区声明</span>
      </div>
      <div class="body">
        <ul>
          <li v-for="(item,index) in statement" :key="index"><i class="iconfont nil-tag"></i> {{item}}</li>
        </ul>
      </div>
    </el-card>
    <el-card class="join-card nil-card">
      <div class="header clearfix" slot="header">
        <span>加入我们</span>
      </div>
      <div class="body">
        <el-image class="img" :src="wxPng" fit="fill" :lazy="true"></el-image>
      </div>
    </el-card>
  </div>
</template>

<script>
import wxPng from '@/assets/wx.png';
import { getSettingStatement } from '@/api/setting';
export default {
  data() {
    return {
      wxPng,
      statement: []
    };
  },
  mounted() {
    this.initStstment()
  },
  methods: {
    initStstment() {
      getSettingStatement().then(res => {
        this.statement = JSON.parse(res.data.value)
      })
    }
  }
};
</script>

<style lang="scss" scope>
.nil-card {
  margin-bottom: 10px;
  .header span {
    padding-bottom: 15px;
    border-bottom: 3px solid #7579e7;
  }
}
.statement-card {
  ul {
    padding: 0;
  }
  ul > li {
    list-style: none;
    line-height: 25px;
  }
}
.join-card {
  .img {
    padding: 20px;
  }
}
</style>
