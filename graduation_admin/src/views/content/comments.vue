<template>
  <div>
    <avue-crud
      :option="option"
      :data="data"
      :table-loading="tableLoding"
      :page.sync="page"
      @on-load="onLoad"
      @row-del="commentDel"
    >
      <template slot="isTrue" slot-scope="{row}">
        <el-tag :type="elTagFilter[row.isTrue]">{{ statusFilter[row.isTrue] }}</el-tag>
      </template>
    </avue-crud>
  </div>
</template>

<script>
import { delPostComment, getComments } from '@/api/comments'

export default {
  data() {
    return {
      tableLoding: false,
      elTagFilter: {
        '0': 'info',
        '1': 'success'
      },
      statusFilter: {
        '0': '默认',
        '1': '精选'
      },
      page: {
        layout: 'total,prev,pager, next',
        pageSize: 10
      },
      option: {
        border: true,
        index: true,
        addBtn: false,
        refreshBtn: false,
        columnBtn: false,
        headerAlign: 'center',
        align: 'center',
        labelWidth: 100,
        editBtn: false,
        column: [
          {
            label: 'ID',
            prop: 'id'
          }, {
            label: '评论内容',
            prop: 'content'
          },
          {
            prop: 'isTrue',
            label: '状态',
            slot: true
          },
          {
            prop: 'username',
            label: '用户',
            bind: 'user.name'
          },
          {
            prop: 'tid',
            label: '父评论id'
          }
        ]
      },
      data: []
    }
  },
  methods: {
    commentDel(form) {
      const { id } = form
      this.$confirm('此操作将永久删除该评论以及她的子评论, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delPostComment(id).then(res => {
          if (res.success) {
            this.$message.success('删除成功')
            this.onLoad()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    onLoad() {
      this.getCommentList(this.page.currentPage, this.page.pageSize)
    },
    getCommentList(currentPage, pageSize) {
      this.tableLoding = true
      getComments(currentPage, pageSize).then(res => {
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
