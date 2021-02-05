<template>
  <div id="setting_page">
    <div class="title"><span>页面配置</span></div>
    <div class="el-form el-form--label-left">
      <div class="el-form-item">
        <label class="el-form-item__label" style="">社区声明</label>
        <div class="el-form-item__content">
          <el-tag
            v-for="tag in statements"
            :key="tag"
            :closable="iseditstatements"
            :disable-transitions="false"
            @close="handleClose(tag)"
          >
            {{ tag }}
          </el-tag>
          <el-input
            v-if="iseditstatements"
            ref="saveTagInput"
            v-model="inputValue"
            placeholder="按回车 添加"
            class="input-new-tag"
            size="small"
            @keyup.enter.native="handleInputConfirm"
            @blur="handleInputConfirm"
          />
          <el-button v-if="!iseditstatements" class="optionBtn" type="primary" size="small" @click="iseditstatements = true">编辑</el-button>
          <el-button v-if="iseditstatements" class="optionBtn" type="primary" size="small" @click="saveStatement">保存</el-button>
          <el-button v-if="iseditstatements" class="optionBtn" type="info" size="small" @click="cancel">取消</el-button>
        </div>
      </div>
      <div class="el-form-item">
        <label class="el-form-item__label" style="">建站时间</label>
        <div class="el-form-item__content">
          <el-tag
            v-if="!iseditsiteUpTime"
            :disable-transitions="false"
          >
            {{ siteUpTime || '空' }}
          </el-tag>
          <el-date-picker
            v-if="iseditsiteUpTime"
            v-model="siteUpTime"
            size="small"
            type="date"
            placeholder="选择日期"
            format="yyyy 年 MM 月 dd 日"
            value-format="yyyy-MM-dd"
          />
          <el-button v-if="!iseditsiteUpTime" class="optionBtn" type="primary" size="small" @click="iseditsiteUpTime = true">编辑</el-button>
          <el-button v-if="iseditsiteUpTime" class="optionBtn" type="primary" size="small" @click="saveSiteUpTime">保存</el-button>
          <el-button v-if="iseditsiteUpTime" class="optionBtn" type="info" size="small" @click="cancel">取消</el-button>
        </div>
      </div>
      <div class="el-form-item">
        <label class="el-form-item__label" style="">备案号</label>
        <div class="el-form-item__content">
          <el-tag
            v-if="!iseditsiteRecord"
            :disable-transitions="false"
          >
            {{ siteRecord || '空' }}
          </el-tag>
          <el-input
            v-if="iseditsiteRecord"
            v-model="siteRecord"
            class="input-new-tag"
            size="small"
            placeholder="输入备案号"
          />
          <el-button v-if="!iseditsiteRecord" class="optionBtn" type="primary" size="small" @click="iseditsiteRecord = true">编辑</el-button>
          <el-button v-if="iseditsiteRecord" class="optionBtn" type="primary" size="small" @click="saveSiteRecord">保存</el-button>
          <el-button v-if="iseditsiteRecord" class="optionBtn" type="info" size="small" @click="cancel">取消</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getSettingAll, setSettingSiteRecord, setSettingSiteUpTime, setSettingStatement } from '@/api/setting'
export default {
  data() {
    return {
      statements: [],
      iseditstatements: false,
      //
      siteUpTime: '',
      iseditsiteUpTime: false,
      //
      siteRecord: '',
      iseditsiteRecord: false,
      //
      inputVisible: false,
      inputValue: '',
      settings: []
    }
  },
  created() {
    this.initData()
  },
  methods: {
    initData() {
      getSettingAll().then(res => {
        const temp = {}
        res.data.forEach(item => {
          temp[item.key] = item.value
        })
        this.settings = temp
        this.cancel()
      })
    },
    cancel() {
      this.iseditstatements = false
      this.iseditsiteUpTime = false
      this.iseditsiteRecord = false
      if (this.settings['web_statement'] === '') {
        this.statements = []
      } else {
        this.statements = JSON.parse(this.settings['web_statement'])
      }
      this.siteUpTime = this.settings['web_site_up_time']
      this.siteRecord = this.settings['web_site_record']
    },
    saveSiteRecord() {
      setSettingSiteRecord({
        value: this.siteRecord
      }).then(res => {
        this.$message.success('保存成功')
        this.iseditstatements = false
        this.initData()
      })
    },
    saveSiteUpTime() {
      setSettingSiteUpTime({
        value: this.siteUpTime
      }).then(res => {
        this.$message.success('保存成功')
        this.iseditstatements = false
        this.initData()
      })
    },
    saveStatement() {
      setSettingStatement({
        value: JSON.stringify(this.statements)
      }).then(res => {
        this.$message.success('保存成功')
        this.iseditstatements = false
        this.initData()
      })
    },

    handleClose(tag) {
      this.statements.splice(this.statements.indexOf(tag), 1)
    },

    showInput() {
      this.inputVisible = true
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus()
      })
    },

    handleInputConfirm() {
      const inputValue = this.inputValue
      if (inputValue) {
        this.statements.push(inputValue)
      }
      this.inputVisible = false
      this.inputValue = ''
    }
  }
}
</script>

<style>
.el-tag + .el-tag {
  margin-left: 10px;
}
.input-new-tag {
  width: 300px;
  margin-left: 10px;
  vertical-align: bottom;
}
</style>

<style lang="scss" scope>
#setting_page {
  .optionBtn{
    margin-left: 5px;
  }
  .title {
    margin-bottom: 2rem;
    span {
      font-weight: 400;
      font-size: 20px;
      line-height: 30px;
      text-align: center;
    }
  }
}
</style>
