<template>
  <div id="comment_container">
    <el-alert
      v-if="postInfo.type === '1'"
      class="reply_header_hint"
      title="感谢您的回答"
      type="success"
      description="您的每一个用心回答，都会让这个世界变得更美好一些！"
    >
    </el-alert>
    <div class="reply_header" ref="commentContent">
      <span v-if="commentsList">{{ page.total }} 条评论</span>
      <span v-else>暂时没有评论，快来抢沙发吧 </span>
    </div>
    <div v-if="isReply" class="reply-is">
      正在回复 => @{{ replyCommment.user.name }}
      <span class="cancel" @click="cancelReply"> 取消</span>
    </div>
    <div class="reply_box">
      <div class="reply_content">
        <el-input
          v-if="!editTop"
          v-model="commentContent"
          placeholder="撰写评论..."
          size="normal"
          clearable
          ref="myEditor"
          type="textarea"
          :rows="4"
          resize="none"
        ></el-input>
        <quill-editor
          v-if="editTop"
          class="myQuillEditor"
          v-model="commentContent"
          ref="myQuillEditor"
          :options="editorOption"
        >
        </quill-editor>
      </div>
      <div class="reply_option">
        <el-button
          type="info"
          size="default"
          @click="changeEditer"
          icon="el-icon-sort"
          >切换</el-button
        >
        <el-button
          class="reply_option__submit"
          type="primary"
          size="default"
          @click="submitCommmment"
          icon="iconfont nil-submit"
        >
          {{ postInfo.type === '0' ? '提交评论' : '提交回答' }}</el-button
        >
      </div>
    </div>
    <div class="reply_lists">
      <div v-for="item in commentsList" :key="item.id" class="reply_list_item">
        <div class="comment_reply">
          <div class="aside">
            <el-avatar
              icon="el-icon-user-solid"
              shape="circle"
              fit="fill"
              :src="item.user.avatar"
            ></el-avatar>
          </div>
          <div class="body">
            <div class="heading">
              <div class="meta">
                <el-dropdown class="right" v-if="islogin">
                  <span class="el-dropdown-link ">
                    操作<i class="el-icon-arrow-down el-icon--right"></i>
                  </span>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item class="option">
                      <span
                        v-if="postInfo.type === '1' && uid === author.uid"
                        @click="replyTrue(item)"
                        >设置精选</span
                      >
                    </el-dropdown-item>
                    <el-dropdown-item class="option">
                      <span
                        v-if="uid === author.uid || uid === item.user.uid"
                        @click="replyDelete(item)"
                        >删除</span
                      >
                    </el-dropdown-item>
                    <el-dropdown-item class="option">
                      <span @click="$message.info('抱歉此为无效功能')"
                        >举报</span
                      >
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
                <span class="reply" @click="replyComment(item)">回复</span>
                <span class="time">{{ item.publishTime | formatTime }}</span>
                <span class="name"
                  >{{ item.user.name }}
                  <span><span :lvl="item.user.rate" class="m-level"></span></span>
                  <el-tag
                    v-if="author.uid === item.user.uid"
                    type="success"
                    size="mini"
                    effect="dark"
                    >作者</el-tag
                  >
                  <el-tag
                    v-if="'1' === item.isTrue"
                    type="warning"
                    size="mini"
                    effect="dark"
                    >精选</el-tag
                  >
                </span>
              </div>
            </div>
            <div class="rich_content" v-html="item.content"></div>
            <div class="children" v-if="item.children">
              <div
                v-for="it in item.children"
                :key="it.id"
                class="reply_list_item"
              >
                <div class="comment_reply">
                  <div class="aside">
                    <el-avatar
                      icon="el-icon-user-solid"
                      shape="circle"
                      fit="fill"
                      :src="it.user.avatar"
                    ></el-avatar>
                  </div>
                  <div class="body">
                    <div class="heading">
                      <div class="meta">
                        <el-dropdown class="right" v-if="islogin">
                          <span class="el-dropdown-link ">
                            操作<i
                              class="el-icon-arrow-down el-icon--right"
                            ></i>
                          </span>
                          <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item class="option">
                              <span
                                v-if="
                                  postInfo.type === '1' && uid === author.uid
                                "
                                @click="replyTrue(it)"
                                >设置精选</span
                              >
                            </el-dropdown-item>
                            <el-dropdown-item class="option">
                              <span
                                v-if="uid === author.uid || uid === it.user.uid"
                                @click="replyDelete(it)"
                                >删除</span
                              >
                            </el-dropdown-item>
                            <el-dropdown-item class="option">
                              <span @click="$message.info('抱歉此为无效功能')"
                                >举报</span
                              >
                            </el-dropdown-item>
                          </el-dropdown-menu>
                        </el-dropdown>
                        <span class="time">{{
                          it.publishTime | formatTime
                        }}</span>
                        <span class="name"
                          >{{ it.user.name }}
                          <span><span :lvl="it.user.rate" class="m-level"></span></span>
                          <el-tag
                            v-if="author.uid === it.user.uid"
                            type="success"
                            size="mini"
                            effect="dark"
                            >作者</el-tag
                          >
                          <el-tag
                            v-if="'1' === it.isTrue"
                            type="warning"
                            size="mini"
                            effect="dark"
                            >精选</el-tag
                          >
                        </span>
                      </div>
                    </div>
                    <div class="rich_content" v-html="it.content"></div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <el-pagination
        style="text-align:center;"
        layout="prev, pager, next"
        :hide-on-single-page="true"
        :current-page="page.currentPage"
        @current-change="pageChange"
        :total="page.total"
        :page-size="page.pageSize"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import {
  addPostComment,
  commentSetTrue,
  delPostComment,
  getPostComment,
} from '@/api/post'
import { formatTime } from '@/utils'
import { mapGetters } from 'vuex'
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'

import { quillEditor } from 'vue-quill-editor'
export default {
  filters: {
    formatTime,
  },
  components: {
    quillEditor,
  },
  props: {
    pid: {
      type: String,
      required: true,
    },
    author: {
      type: Object,
      required: true,
    },
    postInfo: {
      type: Object,
      required: true,
    },
  },
  computed: {
    ...mapGetters(['islogin', 'uid']),
  },
  data() {
    return {
      editTop: false,
      commentContent: '',
      commentsList: [],
      page: {
        pageSize: 10,
        total: 0,
        currentPage: 1,
      },
      editorOption: {
        modules: {
          toolbar: [
            ['bold', 'italic', 'underline', 'strike'], // toggled buttons
            ['blockquote', 'code-block'],
          ],
        },
      },
      replyCommment: {},
      isReply: false,
    }
  },
  mounted() {
    this.getComments()
  },
  methods: {
    replyTrue(item) {
      commentSetTrue(item.id).then((res) => {
        if (res.success) {
          this.$message.success('设置成功')
          this.getComments()
        }
      })
    },
    replyDelete(item) {
      console.log('del item ---- > ', item)
      delPostComment(item.id).then((res) => {
        if (res.success) {
          this.$message.success('删除成功')
          this.getComments()
        }
      })
    },
    cancelReply() {
      this.isReply = false
      this.replyCommment = {}
    },
    replyComment(item) {
      this.isReply = true
      this.replyCommment = item
      this.$refs.commentContent.scrollIntoView({
        behavior: 'smooth', // 平滑过渡
        block: 'start', // 上边框与视窗顶部平齐。默认值
      })

      if (this.editTop) {
        this.$refs.myQuillEditor.quill.focus()
      } else {
        this.$refs.myEditor.focus()
      }
      // 回复 item
    },
    changeEditer() {
      this.commentContent = ''
      this.editTop = !this.editTop
    },
    pageChange(page) {
      this.page.currentPage = page
      this.getComments()
    },
    submitCommmment() {
      const data = {
        content: this.commentContent,
      }
      if (!this.islogin) {
        this.$message.info('请先登录')
        return
      }
      if (!data.content) {
        this.$message.info('评论内容不能为空')
        return
      }
      if (this.isReply) {
        data.tid = this.replyCommment.id
      }
      addPostComment(this.pid, data).then((res) => {
        if (res.success) {
          this.$message.success(res.message)
          this.commentContent = ''
          this.getComments()
          this.cancelReply()
        }
      })
    },
    getComments() {
      getPostComment(this.pid, this.page.currentPage, this.page.pageSize).then(
        (res) => {
          // console.log('评论 ==》 ', res.data);
          this.commentsList = res.data.pageData
          this.page.pageSize = res.data.pageSize
          this.page.total = res.data.totalCount
          this.page.currentPage = res.data.currentPage
        }
      )
    },
  },
}
</script>

<style>
.myQuillEditor .ql-container {
  height: 200px;
}
</style>

<style lang="scss" scoped>
#comment_container {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 5px;
  padding: 10px;
  .reply_header_hint {
    margin-bottom: 10px;
  }
  .reply_header {
    font-size: 20px;
    margin-bottom: 10px;
  }
  .reply-is {
    cursor: pointer;
    padding: 5px 0;
  }
  .reply_box {
    margin-bottom: 10px;
    padding-bottom: 15px;
    .reply_content {
      margin-bottom: 10px;
      .el-textarea {
        font-size: 18px;
      }
    }
    .reply_option {
      display: flex;
      justify-content: space-between;
    }
  }
  .reply_lists {
    padding: 10px;
    margin-bottom: 10px;
    .reply_list_item {
      padding-bottom: 10px;
      padding-top: 10px;
      .comment_reply {
        display: flex;
        .aside {
          flex: 0 0 50px;
        }
        .body {
          padding: 0 10px;
          flex: 1;
          width: 630px;
          overflow-wrap: break-word;
          overflow-y: hidden;
          .heading {
            margin-bottom: 15px;
            .meta {
              height: 20px;
              line-height: 20px;
              vertical-align: middle !important;
              margin-bottom: 5px;
              .time {
                float: right;
                font-size: 12px;
              }
              .right,
              .reply {
                float: right;
                cursor: pointer;
                margin-left: 10px;
                font-size: 12px;
              }
              .delete {
                color: #f40;
              }
              .name {
                margin-left: 5px;
              }
            }
          }
          .rich_content {
            white-space: pre-line;
          }
        }
      }
      .children {
        padding-top: 10px;
        .body {
          padding-right: 0;
        }
      }
    }
  }
}
</style>
