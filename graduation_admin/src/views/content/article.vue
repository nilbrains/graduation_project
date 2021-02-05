<template>
  <div>
    <avue-crud
      :option="option"
      :data="data"
      :table-loading="tableLoding"
      :page.sync="page"
      @on-load="onLoad"
    >
      <template slot="status" slot-scope="{row}">
        <el-tag :type="elTagFilter[row.status]">{{ statusFilter[row.status] }}</el-tag>
      </template>
      <template slot="type" slot-scope="{row}">
        <el-tag :type="elTagFilter[row.type]">{{ typeFilter[row.type] }}</el-tag>
      </template>
      <template slot="menu" slot-scope="{type,size,row}">
        <el-button :size="size" :type="type" @click="on18Post(row)"> 改变状态 </el-button>
      </template>
    </avue-crud>
  </div>
</template>

<script>
import { delPost, getPostList } from '@/api/post'

export default {
  data() {
    return {
      tableLoding: false,
      elTagFilter: {
        '0': 'success',
        '1': 'info'
      },
      elTagTypeFilter: {
        '0': 'success',
        '1': 'warning'
      },
      statusFilter: {
        '0': '启用',
        '1': '禁用'
      },
      typeFilter: {
        '0': '文章',
        '1': '提问'
      },
      page: {
        layout: 'total,prev,pager, next',
        pageSize: 10
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
        addBtn: false,
        editBtn: false,
        column: [
          {
            label: 'ID',
            prop: 'pid',
            addDisplay: false
          }, {
            label: '标题',
            prop: 'title'
          }, {
            label: '类型',
            prop: 'type',
            slot: true
          }, {
            label: '分类',
            prop: 'cid',
            bind: 'category.title'
          }, {
            label: '作者',
            prop: 'uid',
            bind: 'user.name'
          }, {
            label: '发布时间',
            prop: 'publishTime',
            type: 'datetime',
            format: 'yyyy-MM-dd hh:mm:ss'
          },
          {
            prop: 'status',
            label: '状态',
            slot: true,
            editDisplay: false
          }
        ]
      },
      data: []
    }
  },
  methods: {
    on18Post({ pid }) {
      delPost(pid).then(res => {
        this.$message.success(res.message)
        this.onLoad(this.page)
      })
    },
    onLoad(page) {
      console.log(page)
      this.getCategoryList({}, page.currentPage, page.pageSize)
    },
    getCategoryList(title, currentPage, pageSize) {
      this.tableLoding = true
      getPostList({}, currentPage, pageSize).then(res => {
        console.log(res)
        this.data = res.data.pageData
        this.page.pageSize = res.data.pageSize
        this.page.total = res.data.totalCount
        this.page.currentPage = res.data.currentPage
        this.tableLoding = false
      })
    }
  }
}
</script>

<style>

</style>
