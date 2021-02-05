<template>
  <div class="container index" id="index-container">
    <el-row :gutter="20">
      <el-col :span="18" :offset="0">
        <div id="list-container">
          <ul class="note-list" infinite-scroll-url="/">
            <li
              v-for="(item, index) in postList"
              :key="index"
              :class="item.cover ? 'have-img' : ''"
            >
              <router-link
                v-if="item.cover"
                class="wrap-img"
                :to="`/p/${item.pid}`"
                target="_blank"
              >
                <img class="img-blur-done" :src="item.cover" alt="120" />
              </router-link>
              <div class="content">
                <router-link class="title" :to="`/p/${item.pid}`">
                  <el-tag :type="elTagFilter[item.type]" size="mini">{{
                    typeFilter[item.type]
                  }}</el-tag>
                  <span v-if="item.type === '1' && item.answer === '1'" style="color: #1fad4e;">【已解决】</span>
                  {{ item.title }}
                </router-link>
                <p class="abstract">
                  {{ item.desc || '描述好像失踪了~~' }}
                </p>
                <div class="meta">
                  <router-link class="nickname" :to="`/u/${item.user.uid}`">
                    <i class="iconfont nil-Serviceusers"></i>
                    {{ item.user.name }}
                  </router-link>
                  <router-link class="cate" :to="`/c/${item.category.cid}`">
                    <i class="iconfont nil-wenjianjia"></i>
                    {{ item.category.title }}
                  </router-link>
                  <span
                    ><i class="iconfont nil-fangke"></i> {{ item.views }}</span
                  >
                  <span
                    ><i class="iconfont nil-time"></i>
                    {{ item.publishTime | formatTime }}</span
                  >
                </div>
              </div>
            </li>
          </ul>
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
        <el-card shadow="always" class="top-part">
          <span class="title">全部分类 ：</span>
          <router-link
            class="cate-item el-tag"
            :style="{
              background:
                arr_color[Math.floor(Math.random() * arr_color.length)],
            }"
            :to="`/c/${item.cid}`"
            v-for="item in cateList"
            :key="item.cid"
          >
            {{ item.title }}
          </router-link>
        </el-card>
      </el-col>
      <el-col :span="6" :offset="0">
        <el-card
          shadow="always"
          :body-style="{ padding: '20px' }"
          style="margin-bottom:10px"
        >
          <router-link to="/center/publish/articlePost" class="pubBtn">
            发表文章
          </router-link>
        </el-card>
        <aside-card />
        <site-info />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getPostList } from '@/api/post'
import AsideCard from '@/components/AsideCard'
import SiteInfo from '@/components/SiteInfo'
import { formatTime, arr_color } from '@/utils'
import { getCategoryList } from '@/api/category'
export default {
  components: {
    AsideCard,
    SiteInfo,
  },
  filters: {
    formatTime,
  },
  data() {
    return {
      arr_color,
      elTagFilter: {
        '0': 'success',
        '1': 'warning',
      },
      typeFilter: {
        '0': '文章',
        '1': '提问',
      },
      postList: [],
      page: {
        pageSize: 10,
        total: 0,
        currentPage: 1,
      },
      cateList: [],
    }
  },
  created() {
    this.getPostList()
  },
  methods: {
    pageChange(page) {
      this.page.currentPage = page
      this.getPostList()
    },
    getPostList() {
      getPostList({}, this.page.currentPage, this.page.pageSize).then((res) => {
        this.postList = res.data.pageData
        this.page.pageSize = res.data.pageSize
        this.page.total = res.data.totalCount
        this.page.currentPage = res.data.currentPage
      })
      getCategoryList().then((res) => {
        this.cateList = res.data
      })
    },
  },
}
</script>

<style lang="scss" scoped>
@import './noteList.css';
#index-container {
  margin-top: 10px;
  .top-part {
    margin-top: 10px;
    .cate-item {
      color: #fff;
      font-weight: 800;
      border: none;
    }
  }
  #list-container {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 10px;
  }
}
</style>
