<template>
  <div id="catePage-page" class="container">
    <div class="catePage-bottom-part">
      <el-row :gutter="20">
        <el-col :span="18" :offset="0">
          <div class="catePage-header-part">
            <div class="catePage-header">
              当前分类:
              <span 
              v-loading="catepageLoading && !filter"
              v-html="filter[catePageCate] || '分类失踪了....'"
              ></span>
            </div>
          </div>
          <div class="catePage-type-part">
            <div class="catePage-type">
              <span
                class="catePage-type-item"
                :class="catePageType === 'all' ? 'active' : ''"
                @click="changeType('all')"
              >
                全部
              </span>
              <span
                class="catePage-type-item"
                :class="catePageType === 'article' ? 'active' : ''"
                @click="changeType('article')"
              >
                文章
              </span>
              <span
                class="catePage-type-item"
                :class="catePageType === 'qa' ? 'active' : ''"
                @click="changeType('qa')"
              >
                提问
              </span>
            </div>
          </div>
          <div
            class="catePage-content-list"
            v-if="postList.length > 0"
            v-loading="catepageLoading"
          >
            <div
              class="catePage-content-item"
              v-for="item in postList"
              :key="item.pid"
            >
              <div class="type-article-share-wenda">
                <router-link :to="`/p/${item.pid}`"
                  ><span class="catePage-page-result-title">{{
                    item.title
                  }}</span></router-link
                >
                <div class="catePage-content-description">
                  {{ item.desc || '描述好像失踪了~~' }}
                </div>
              </div>
            </div>
            <el-pagination
              style="text-align:center;margin-top:10px"
              layout="prev, pager, next"
              :current-page="page.currentPage"
              @current-change="pageChange"
              :hide-on-single-page="true"
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
import { getPostList } from '@/api/post'
import Empty from '@/components/Empty'
import AsideCard from '@/components/AsideCard'
import SiteInfo from '@/components/SiteInfo'
import { getCategoryList } from '@/api/category'
export default {
  components: {
    Empty,
    AsideCard,
    SiteInfo,
  },
  data() {
    return {
      catepageLoading: false,
      catePageCate: this.$route.params.cid,
      catePageType: this.$route.query.type || 'all',
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
    this.tocatePage()
  },
  methods: {
    getCate() {
      getCategoryList("1").then((res) => {
        // console.log(res.data);
        // const arr = [];
        const obj = {}
        res.data.forEach((item) => {
            // arr.push({
            //   value: item.cid,
            //   label: item.title,
            // });
            obj[item.cid] = item.title
            if (item.status === '1') {
              obj[item.cid] = item.title + "<span style='color:#f40;'>【已删除】</span>"
            }
        })
        this.filter = obj
        // this.options = arr;
      })
    },
    pageChange(page) {
      this.page.currentPage = page
      this.tocatePage()
    },
    tocatePage() {
      this.catepageLoading = true
      console.log(this.filter)
      const query = {
        cid: this.catePageCate || '',
        type: this.typeFilter[this.catePageType] || '',
      }
      getPostList(query, this.page.currentPage, this.page.pageSize).then(
        (res) => {
          this.postList = res.data?.pageData || []
          this.page.pageSize = res.data?.pageSize || this.page.pageSize
          this.page.total = res.data?.totalCount || 0
          this.page.currentPage = res.data?.currentPage || 1
          this.catepageLoading = false
        }
      )
    },
    changeType(type) {
      this.page.currentPage = 1
      this.catePageType = type
      this.tocatePage()
    },
  },
}
</script>

<style lang="scss" scope>
#catePage-page {
  margin-top: 20px;
  .catePage-header-part {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 20px;
    margin-bottom: 10px;
    .catePage-header {
      font-size: 18px;
      font-weight: 800;
    }
  }
  .catePage-content-list {
    background: #fff;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    margin-top: 20px;
    padding: 10px;
    .catePage-content-item {
      padding: 20px 10px;
      margin-top: 10px;
      border-bottom: 1px solid #f3f3f3;
      .type-article-share-wenda > a {
        color: #0084ff;
      }
      .catePage-content-description {
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
  .catePage-type-part {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 5px;
  }
  .catePage-type {
    overflow: hidden;
  }
  .catePage-type-part .active {
    color: #0084ff;
    border-bottom: 2px solid #0084ff;
  }

  .catePage-type-item {
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
.catePage-top-part {
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  .catePage-page-input-box {
    text-align: center;
    padding: 30px;
    background: #fff;
  }
}
.input-with-select .el-input-group__prepend {
  background-color: #fff;
}
</style>
