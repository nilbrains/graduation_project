<template>
  <div>
    <avue-crud
      :option="option"
      :data="data"
      :table-loading="tableLoding"
      @on-load="onLoad"
      @row-save="categorySave"
      @row-update="categoryUpdate"
    >
      <template slot="status" slot-scope="{row}">
        <el-tag :type="elTagFilter[row.status]">{{ statusFilter[row.status] }}</el-tag>
      </template>
      <template slot="menu" slot-scope="{type,size,row}">
        <el-button :size="size" :type="type" @click="on18Category(row)"> 改变状态 </el-button>
      </template>
    </avue-crud>
  </div>
</template>

<script>

import { addCategory, changeStatusCategory, editCategory, getCategoryList } from '@/api/category'

export default {
  data() {
    return {
      tableLoding: false,
      elTagFilter: {
        '0': 'success',
        '1': 'info'
      },
      statusFilter: {
        '0': '启用',
        '1': '禁用'
      },
      option: {
        border: true,
        index: true,
        refreshBtn: false,
        columnBtn: false,
        headerAlign: 'center',
        align: 'center',
        labelWidth: 100,
        delBtn: false,
        column: [
          {
            label: 'ID',
            prop: 'cid',
            addDisabled: true,
            editDisabled: true,
            addDisplay: false
          }, {
            label: '名称',
            prop: 'title'
          },
          {
            prop: 'status',
            label: '状态',
            slot: true,
            addDisplay: false,
            addDisabled: true,
            editDisplay: false
          }
        ]
      },
      data: []
    }
  },
  methods: {
    onLoad(page) {
      console.log(page)
      this.getCategoryList()
    },
    getCategoryList() {
      this.tableLoding = true
      getCategoryList().then(res => {
        console.log(res)
        this.data = res.data
        this.tableLoding = false
      })
    },
    categorySave(row, done, loading) {
      loading()
      addCategory(row).then(res => {
        this.$message.success('添加成功')
        done()
        this.onLoad(this.page)
      })
    },
    categoryUpdate(row, index, done, loading) {
      loading()
      editCategory(row).then(res => {
        this.$message.success('修改成功')
        done()
        this.onLoad(this.page)
      })
    },
    on18Category({ cid }) {
      changeStatusCategory(cid).then(res => {
        this.$message.success(res.message)
        this.onLoad(this.page)
      })
    }
  }
}
</script>

<style>

</style>
