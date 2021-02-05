<template>
  <div id="search-page" class="container">
    <div class="search-top-part">
      <div class="search-page-input-box">
        <el-row :gutter="20">
          <el-col :span="4" :offset="0">
            <el-select
              v-model="searchCate"
              clearable
              filterable
              placeholder="分类"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="20" :offset="0">
            <el-input
              placeholder="请输入内容"
              v-model="searchKey"
              class="input-with-select"
            >
              <el-button slot="append" @click="search">搜索一下</el-button>
            </el-input>
          </el-col>
        </el-row>
      </div>
    </div>
    <div class="search-bottom-part">
      <el-row :gutter="20">
        <el-col :span="18" :offset="0">
          <div class="search-type-part">
            <div class="search-type">
              <span
                class="search-type-item"
                :class="searchType === 'all' ? 'active' : ''"
                @click="changeType('all')"
              >
                全部
              </span>
              <span
                class="search-type-item"
                :class="searchType === 'article' ? 'active' : ''"
                @click="changeType('article')"
              >
                文章
              </span>
              <span
                class="search-type-item"
                :class="searchType === 'qa' ? 'active' : ''"
                @click="changeType('qa')"
              >
                提问
              </span>
            </div>
          </div>
          <div class="search-content-list" v-if="postList.length > 0">
            <div
              class="search-content-item"
              v-for="item in postList"
              :key="item.pid"
            >
              <div class="type-article-share-wenda">
                <router-link :to="`/p/${item.pid}`"
                  ><span class="search-page-result-title">{{
                    item.title
                  }}</span></router-link
                >
                <div class="search-content-description">
                  {{ item.desc || '描述好像失踪了~~' }}
                </div>
              </div>
            </div>
            <el-pagination
              style="text-align:center;margin-top:10px"
              layout="prev, pager, next"
              :current-page="page.currentPage"
              :hide-on-single-page="true"
              @current-change="pageChange"
              :total="page.total"
              :page-size="page.pageSize"
            >
            </el-pagination>
          </div>
          <empty v-else />
        </el-col>
        <el-col :span="6" :offset="0">
          <aside-card />
          <site-info />
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getCategoryList } from '@/api/category'
import { getPostList } from '@/api/post'
import Empty from '@/components/Empty'
import AsideCard from '@/components/AsideCard'
import SiteInfo from '@/components/SiteInfo'
export default {
  components: {
    Empty,
    AsideCard,
    SiteInfo,
  },
  data() {
    return {
      searchKey: this.$route.query.word || '',
      searchCate: this.$route.query.cate || '',
      searchType: this.$route.query.type || 'all',
      options: [],
      filter: {},
      typeFilter: {
        all: '',
        article: '0',
        qa: '1',
      },
      page: {
        pageSize: 10,
        total: 0,
        currentPage: 1,
      },
      postList: [],
    }
  },
  created() {
    this.getCate()
    this.tosearch()
  },
  methods: {
    pageChange(page) {
      this.page.currentPage = page
      this.tosearch()
    },
    tosearch() {
      console.log(this.filter)
      const query = {
        title: this.searchKey || '',
        cid: this.searchCate || '',
        type: this.typeFilter[this.searchType] || '',
      }
      getPostList(query, this.page.currentPage, this.page.pageSize).then(
        (res) => {
          this.postList = res.data?.pageData || []
          this.page.pageSize = res.data?.pageSize || this.page.pageSize
          this.page.total = res.data?.totalCount || 0
          this.page.currentPage = res.data?.currentPage || 1
        }
      )
    },
    changeType(type) {
      this.page.currentPage = 1
      this.searchType = type
      this.tosearch()
    },
    search() {
      this.$nextTick(() => {
        this.$router.replace({
          path:
            '/redirect' +
            `/s?word=${this.searchKey}&cate=${this.searchCate || ''}`,
        })
      })
    },
    getCate() {
      getCategoryList().then((res) => {
        // console.log(res.data);
        const arr = []
        const obj = {}
        res.data.forEach((item) => {
          if (item.status === '0') {
            arr.push({
              value: item.cid,
              label: item.title,
            })
            obj[item.cid] = item.title
          }
        })
        this.filter = obj
        this.options = arr
      })
    },
  },
}
</script>

<style lang="scss" scope>
#search-page {
  margin-top: 20px;
  .search-content-list {
    background: #fff;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    margin-top: 20px;
    padding: 10px;
    .search-content-item {
      padding: 20px 10px;
      margin-top: 10px;
      border-bottom: 1px solid #f3f3f3;
      .type-article-share-wenda > a {
        color: #0084ff;
      }
      .search-content-description {
        line-height: 1.4;
        margin-top: 10px;
        color: #545454;
        word-break: break-word;
        font-size: 14px;
        text-overflow: -o-ellipsis-lastline;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
      }
    }
  }
  .search-type-part {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 5px;
  }
  .search-type {
    overflow: hidden;
  }
  .search-type-part .active {
    color: #0084ff;
    border-bottom: 2px solid #0084ff;
  }

  .search-type-item {
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
.search-top-part {
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  .search-page-input-box {
    text-align: center;
    padding: 30px;
    background: #fff;
  }
}
.input-with-select .el-input-group__prepend {
  background-color: #fff;
}
</style>
