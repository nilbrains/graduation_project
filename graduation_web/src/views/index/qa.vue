<template>
  <div class="container">
    <div class="qa-list-container">
      <el-row :gutter="20">
        <el-col :span="18" :offset="0">
          <div class="type-part">
            <div class="type">
              <span
                class="type-item"
                :class="qaType === 'new' ? 'active' : ''"
                @click="changeType('new')"
              >
                最新问答
              </span>
              <span
                class="type-item"
                :class="qaType === 'un' ? 'active' : ''"
                @click="changeType('un')"
              >
                等待回答
              </span>
              <span
                class="type-item"
                :class="qaType === 'ans' ? 'active' : ''"
                @click="changeType('ans')"
              >
                已解决
              </span>
            </div>
          </div>
          <div class="qa-content-list" v-if="qaList.length > 0">
            <div class="qa-list-item" v-for="item in qaList" :key="item.pid">
              <div class="qa-rank">
                <div class="answers">
                  <span>{{ item.answer === '1' ? '✔' : '✘' }}</span>
                  <small>回答</small>
                </div>
                <div class="views">
                  <span>{{ item.views }}</span
                  ><small>浏览</small>
                </div>
              </div>
              <div class="summary">
                <ul class="author list-inline">
                  <li>
                    <span>{{ item.user.name }}</span>
                    <span class="split"></span>
                    <span>{{ item.publishTime | formatTime }} 提问</span>
                  </li>
                </ul>
                <h2 class="title">
                  <router-link :to="`/p/${item.pid}`">{{
                    item.title
                  }}</router-link>
                </h2>
              </div>
            </div>
            <el-pagination
              style="text-align:center;margin-top:10px"
              layout="prev, pager, next"
              :hide-on-single-page="true"
              :current-page="page.currentPage"
              @current-change="pageChange"
              :total="page.total"
              :page-size="page.pageSize"
            >
            </el-pagination>
          </div>
          <empty v-else />
        </el-col>
        <el-col :span="6" :offset="0">
          <el-card
            shadow="always"
            :body-style="{ padding: '20px' }"
            style="margin-bottom:10px"
          >
            <router-link to="/center/publish/qaPost" class="pubBtn">
              我要提问
            </router-link>
          </el-card>

          <aside-card />
          <site-info />
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getPostList } from '@/api/post'
import { formatTime } from '@/utils'
import Empty from '@/components/Empty'
import AsideCard from '@/components/AsideCard'
import SiteInfo from '@/components/SiteInfo'
export default {
  components: {
    Empty,
    AsideCard,
    SiteInfo,
  },
  filters: {
    formatTime,
  },
  data() {
    return {
      qaType: 'new',
      qaList: [],
      page: {
        pageSize: 10,
        total: 0,
        currentPage: 1,
      },
      answerFilter: {
        new: '',
        un: '0',
        ans: '1',
      },
    }
  },
  created() {
    this.getQas()
  },
  methods: {
    pageChange(page) {
      this.page.currentPage = page
      this.getQas()
    },
    changeType(type) {
      this.page.currentPage = 1
      this.qaType = type
      this.getQas()
    },
    getQas() {
      const query = {
        type: '1',
        answer: this.answerFilter[this.qaType] || '',
      }
      getPostList(query, this.page.currentPage, this.page.pageSize).then(
        (res) => {
          this.qaList = res.data?.pageData || []
          this.page.pageSize = res.data?.pageSize || this.page.pageSize
          this.page.total = res.data?.totalCount || 0
          this.page.currentPage = res.data?.currentPage || 1
        }
      )
    },
  },
}
</script>

<style lang="scss" scope>
.qa-list-container {
  margin-top: 10px;
  .qa-content-list {
    background: #fff;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    margin-top: 20px;
    padding: 10px;
    .qa-list-item {
      margin: 0;
      border-bottom: 1px solid #eee;
      padding: 12px 0;
      overflow: hidden;
      .split:before {
        content: '·';
        color: #ddd;
      }
      .summary {
        overflow: auto;
        overflow-x: hidden;
        .author {
          padding: 0;
          margin: 0;
          margin-bottom: 5px;
          color: #999;
          font-size: 13px;
          list-style: none;
          & > li {
            display: inline-block;
            padding-left: 5px;
            padding-right: 5px;
          }
        }
        .title {
          display: inline-block;
          margin: 0 5px 0 0;
          font-size: 16px;
          height: 22px;
          font-weight: normal;
          line-height: 22px;
        }
      }
      .qa-rank {
        float: left;
        margin-right: 10px;
        font-size: 16px;
        text-align: center;
        line-height: 1.2;
        color: #666;
        .views,
        .answers {
          //
          padding-top: 3px;
          width: 45px;
          height: 42px;
          color: #757575;
          display: inline-block;
          -moz-border-radius: 3px;
          -webkit-border-radius: 3px;
          border-radius: 3px;
          small {
            display: block;
            font-size: 12px;
          }
        }
        .answers {
          color: #017e66;
          background: rgba(1, 126, 102, 0.08);
          border: 1px solid rgba(1, 126, 102, 0.16);
          margin: 0 6px;
        }
      }
    }
  }
  .type-part {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 5px;
  }
  .type {
    overflow: hidden;
  }
  .type-part .active {
    color: #0084ff;
    border-bottom: 2px solid #0084ff;
  }

  .type-item {
    float: left;
    margin-left: 20px;
    margin-right: 20px;
    color: #07111b;
    line-height: 40px;
    cursor: pointer;
    font-size: 16px;
    font-weight: 700;
  }
}
</style>
