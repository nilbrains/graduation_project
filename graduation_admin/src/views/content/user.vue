<template>
  <div>
    <avue-crud
      :option="option"
      :data="data"
      :table-loading="tableLoding"
      :page.sync="page"
      :upload-after="avatarUploadAfter"
      @on-load="onLoad"
      @row-update="userUpdate"
    >
      <template slot="status" slot-scope="{row}">
        <el-tag :type="elTagFilter[row.status]">{{ statusFilter[row.status] }}</el-tag>
      </template>
      <template slot="menu" slot-scope="{type,size,row}">
        <el-button :size="size" :type="type" @click="on18User(row)"> 改变状态 </el-button>
      </template>
    </avue-crud>
  </div>
</template>

<script>
import { changeStatusUser, editUser, getUserList } from '@/api/user'

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
        delBtn: false,
        column: [
          {
            labelWidth: 20,
            label: '头像',
            prop: 'avatar',
            type: 'upload',
            listType: 'picture-img',
            action: '/image',
            span: 24,
            tip: '只能上传jpg/png用户头像，且不超过500kb'
          },
          {
            label: 'ID',
            prop: 'uid',
            addDisabled: true,
            editDisabled: true,
            addDisplay: false
          }, {
            label: '账号',
            prop: 'account'
          }, {
            label: '用户名',
            prop: 'name'
          }, {
            label: '签名',
            prop: 'sign'
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
    on18User({ uid }) {
      changeStatusUser(uid).then(res => {
        this.$message.success(res.message)
        this.onLoad(this.page)
      })
    },
    userUpdate(row, index, done, loading) {
      // console.log('user update --- >', row)
      loading()
      editUser(row).then(res => {
        this.$message.success('修改成功')
        done()
        this.onLoad(this.page)
      })
    },
    onLoad(page) {
      console.log(page)
      this.getUserList('', page.currentPage, page.pageSize)
    },
    getUserList(title, currentPage, pageSize) {
      this.tableLoding = true
      getUserList('', currentPage, pageSize).then(res => {
        console.log(res)
        this.data = res.data.pageData
        this.page.pageSize = res.data.pageSize
        this.page.total = res.data.totalCount
        this.page.currentPage = res.data.currentPage
        this.tableLoding = false
      })
    },
    avatarUploadAfter(res, done, loading, column) {
      done()
      this.$message.success('上传成功')
    }
  }
}
</script>

<style>

</style>
