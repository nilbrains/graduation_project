<template>
  <div class="container" id="user-container">
    <el-row :gutter="20">
      <el-col :span="18" :offset="0">
        <div class="person">
          <div class="main-top">
            <el-avatar
              icon="el-icon-user-solid"
              size="large"
              shape="circle"
              :src="user.user.avatar"
              fit="fill"
              class="avatar"
            ></el-avatar>
            <div class="title">
              <span class="name">{{ user.user.name }}</span>
              <span class="rate"
                ><span
                  :lvl="user.userinfo.rate | lavelFilter"
                  class="m-level"
                ></span
              ></span>
            </div>
            <div class="info">
              <ul>
                <li>
                  <div class="meta-block">
                    <p>{{ user.userinfo.pyb }}</p>
                    <div>PY币</div>
                  </div>
                </li>
                <li>
                  <div class="meta-block">
                    <p>{{ user.userinfo.integral }}</p>
                    <div>积分</div>
                  </div>
                </li>
              </ul>
            </div>
          </div>
          <div class="type-part">
            <div class="type">
              <span
                class="type-item"
                :class="pType === 'article' ? 'active' : ''"
                @click="changeType('article')"
              >
                文章
              </span>
              <span
                class="type-item"
                :class="pType === 'answer' ? 'active' : ''"
                @click="changeType('answer')"
              >
                提问
              </span>
            </div>
          </div>
          <div id="list-container">
            <ul class="note-list" v-if="postList" infinite-scroll-url="/">
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
                    {{ item.title }}
                  </router-link>
                  <p class="abstract">
                    {{ item.desc || '描述好像失踪了~~' }}
                  </p>
                  <div class="meta">
                    <router-link class="cate" :to="`/c/${item.category.cid}`">
                      <i class="iconfont nil-wenjianjia"></i>
                      {{ item.category.title }}
                    </router-link>
                    <span
                      ><i class="iconfont nil-fangke"></i>
                      {{ item.views }}</span
                    >
                    <span
                      ><i class="iconfont nil-time"></i>
                      {{ item.publishTime | formatTime }}</span
                    >
                  </div>
                </div>
              </li>
            </ul>
               <empty v-else />
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
      </el-col>
      <el-col :span="6" :offset="0">
        <el-card shadow="always" class="user-sign">
          <div>{{ user.user.sign }}</div>
        </el-card>
        <el-card shadow="always" class="user-sign-in" v-if="uidparams === uid">
          <el-button type="primary" size="default" class="signInBtn" @click="toSignIn" v-if="!isToday"> {{ '签到' }} </el-button>
          <el-button type="primary" size="default" class="signInBtn" v-if="isToday"> {{ '已签到'+ (user.userinfo.days + 1)+'天' }} </el-button>
        </el-card>
        <aside-card />
        <site-info />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import AsideCard from '@/components/AsideCard'
import SiteInfo from '@/components/SiteInfo'
import { getUserByUidPostList } from '@/api/post'
import { formatTime ,isTodayDay} from '@/utils'
import { getInfoByUid, userSignIn } from '@/api/user'
import Empty from '@/components/Empty';
import { mapGetters } from 'vuex'
export default {
  components: {
    AsideCard,
    SiteInfo,
    Empty
  },
  filters: {
    formatTime,
    lavelFilter(target) {
      return +target
    },
  },
  computed: {
    ...mapGetters(['uid'])
  },
  data() {
    return {
      pType: 'article',
      user: {
        user: {},
        userinfo: {},
      },
      uidparams: this.$route.params.uid,
      page: {
        pageSize: 10,
        total: 0,
        currentPage: 1,
      },
      postList: [],
      typefilter: {
        article: '0',
        answer: '1',
      },
      isToday: false
    }
  },
  created() {
    this.fetchPosts()
    this.getUser()
  },
  methods: {
    isTodayDay,
    toSignIn(){
      userSignIn().then(res => {
        console.log("res data ==== > ",res);
        if (res.success) {
          this.isToday = true
          this.$message.success(res.message)
        }
      })
    },
    pageChange(page) {
      this.page.currentPage = page
      this.fetchPosts()
    },
    changeType(type) {
      this.pType = type
      this.fetchPosts()
    },
    getUser() {
      getInfoByUid(this.uidparams).then((res) => {
        this.user = res.data

        this.isToday = this.isTodayDay(this.user.userinfo.signDay)
      })
    },
    fetchPosts() {
      getUserByUidPostList(
        this.uidparams,
        this.typefilter[this.pType],
        '',
        this.page.currentPage,
        this.page.pageSize
      ).then((res) => {
        this.postList = res.data.pageData
        this.page.pageSize = res.data.pageSize
        this.page.total = res.data.totalCount
        this.page.currentPage = res.data.currentPage
      })
    },
  },
}
</script>

<style lang="scss" scoped>
@import '../index/noteList.css';
#list-container {
  padding: 10px;
}
.person {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 10px;
  margin-top: 10px;

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

<style>
.signInBtn{
  display: block;
  width: 100%;
}
.user-sign {
  margin-top: 10px;
  margin-bottom: 10px;
}
.user-sign-in{
    margin-bottom: 10px;
}
.person .main-top {
  margin-bottom: 20px;
  margin-top: 20px;
}

.person .main-top .avatar {
  float: left;
  width: 80px;
  height: 80px;
  margin-left: -2px;
}

.person .main-top .title {
  padding: 5px 0 0 100px;
}

.person .main-top .title .name {
  display: inline;
  font-size: 21px;
  font-weight: 700;
  vertical-align: middle;
}

.person .main-top .title .rate {
  margin-left: 10px;
}


.person .main-top .title .name:hover {
  color: #2f2f2f;
}

.person .main-top .title span {
  vertical-align: middle;
  display: inline-block;
}

.person .main-top .info {
  margin-top: 5px;
  padding-left: 100px;
  font-size: 14px;
}

.person .main-top .info ul {
  padding-left: 0;
  font-size: 0;
}

.person .main-top .info ul li {
  display: inline-block;
}

.person .main-top .info ul li:last-child .meta-block {
  margin: 0;
  padding: 0;
  border: none;
}

.person .main-top .info ul .meta-block {
  font-size: 12px;
  margin: 0 7px 6px 0;
  padding: 0 7px 0 0;
  border-right: 1px solid #f0f0f0;
}
</style>
