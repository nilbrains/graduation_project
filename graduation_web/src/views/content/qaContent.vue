<template>
  <div id="article-manager">
    <div class="title"><span>问答管理</span></div>
    <el-table  v-loading="listLoading" :data="activeList" border style="width: 100%">
      <el-table-column prop="title" label="标题">
        <template slot-scope="{ row }">
          <router-link  target="_blank" :to="`/p/${row.pid}`"> {{ row.title }}</router-link>
        </template>
      </el-table-column>
      <el-table-column prop="category.title" label="分类"> </el-table-column>
      <el-table-column prop="publishTime" label="发布时间">
        <template slot-scope="{ row }">
          <span>{{ row.publishTime | formatTime }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="answer" label="解决状态">
        <template slot-scope="{ row }">
          <el-tag :type="elTagAnswerFilter[row.answer]">{{
            answerFilter[row.answer]
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态">
        <template slot-scope="{ row }">
          <el-tag :type="elTagFilter[row.status]">{{
            statusFilter[row.status]
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button size="mini" :disabled="scope.row.status === '1'" @click="handleEdit(scope.row)"
            >{{ scope.row.answer === '0'? '解决': '取消'}}</el-button
          >
          <el-button
            size="mini"
            type="primary"
            :disabled="scope.row.status === '1'"
            @click="handlechange(scope.row)"
            >编辑</el-button
          >
          <el-button
            size="mini"
            type="danger"
            v-if="scope.row.status === '0'"
            @click="handleDelete(scope.row)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-pagination layout="prev, pager, next" :current-page="page.currentPage" @current-change="pageChange" :total="page.total" :page-size="page.pageSize">
    </el-pagination>
  </div>
</template>

<script>
import { changePostAnswer, delPost, getUserPostList } from '@/api/post';
import { formatTime } from '@/utils';
export default {
  filters: {
    formatTime,
  },
  data() {
    return {
      elTagFilter: {
        '0': 'success',
        '1': 'info',
      },
      elTagAnswerFilter: {
        '0': 'info',
        '1': 'success',
      },
      statusFilter: {
        '0': '启用',
        '1': '禁用',
      },
      answerFilter: {
        '0': '未解决',
        '1': '已解决',
      },
      postType: 1,
      activeName: 'all',
      activeList: [],
      page: {
        pageSize: 10,
        total: 0,
        currentPage: 1,
      },
      activeFilter: {
        all: '',
        answer: '1',
        unanswer: '0',
      },
      listLoading: false
    };
  },
  created() {
    this.pageChange(1)
  },
  methods: {
    handlechange(row) {
      console.log("update row ....." ,row);
      this.$router.push(`/center/publish/qaPost?isedit=1&pid=${row.pid}`)
    },
    pageChange(page){
      this.getUserPosts(this.activeFilter[this.activeName],page,this.page.pageSize);
    },
    handleEdit(row) {
      changePostAnswer(row.pid).then(res => {
        if (res.success) {
          this.$message.success(res.message)
          this.pageChange(this.page.currentPage)
        }
      })
    },
    handleDelete(row) {
      this.$confirm('此操作将删除该内容, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          delPost(row.pid).then((res) => {
            if (res.success) {
              this.$message.success(res.message)
              this.pageChange(this.page.currentPage)
            }
          })
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除',
          })
        })
    },
    getUserPosts(answer, cur, size) {
      this.listLoading = true
      getUserPostList(this.postType, answer, cur, size).then((res) => {
        this.activeList = res.data.pageData;
        this.page.pageSize = res.data.pageSize;
        this.page.total = res.data.totalCount;
        this.page.currentPage = res.data.currentPage;
        this.listLoading = false
      });
    },
  },
};
</script>

<style lang="scss" scope>
#article-manager 
{
  .el-pagination{
    text-align: center;
    padding-top: 2rem;
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
