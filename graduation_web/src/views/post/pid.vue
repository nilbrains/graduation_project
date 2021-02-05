<template>
  <div class="main">
    <div class="content">
      <section class="post" v-loading="postLoading">
        <h1 class="post-title">
          <span v-if="postInfo.type === '1' && postInfo.answer === '1'" style="color: #1fad4e;">【已解决】</span>
          {{ postInfo.title }}
          </h1>
        <div class="post__info">
          <span>
            <i class="iconfont nil-date"></i>
            {{ postInfo.publishTime | formatTime }}</span
          >
          <span
            ><router-link :to="`/c/${postInfo.category.cid}`">{{
              postInfo.category.title
            }}</router-link></span
          >
          <span><i class="iconfont nil-fangke"></i> {{ postInfo.views }}</span>
        </div>
        <div class="post__content">
          <mavon-editor
            v-model="postInfo.content"
            :subfield="false"
            :editable="false"
            defaultOpen="preview"
            :toolbarsFlag="false"
            boxShadowStyle="none"
            previewBackground="transparent"
            :toolbars="markdownOption"
            :ishljs="true"
          />
        </div>
        <div v-if="postInfo.tags" class="post__tags">
          <el-tag
            v-for="(item, index) in formatTags(postInfo.tags)"
            :key="index"
            type="success"
            style="border:none;color:#fff;font-weight: 800;cursor: normal;"
            :style="`background:${tagColors[Math.floor((Math.random()*tagColors.length))]}`"
            size="normal"
            effect="light"
            >{{ item }}</el-tag
          >
        </div>
      </section>
      <div class="post__comment" id="comment">
        <p-comment :pid="pid" :postInfo="postInfo" :author="postInfo.user" />
      </div>
    </div>
    <aside class="aside">
      <section class="aside__top">
        <div class="user_info">
          <el-avatar
            icon="el-icon-user-solid"
            size="large"
            shape="circle"
            :src="postInfo.user.avatar"
            fit="fill"
          ></el-avatar>
          <div class="info">
            <div class="username">{{ postInfo.user.name }}
               <span><span :lvl="postInfo.user.rate | lavelFilter" class="m-level"></span></span>
            </div>
            <div class="sign">{{ postInfo.user.sign }}</div>
          </div>
        </div>
      </section>
      <section class="aside__recommend">
        <h3 class="title">
          <span>推荐</span>
        </h3>
        <div v-loading="recommendPostLoading" class="listitem" v-for="(item,index) in recommendPost" :key="index">
          <div
            class="list__title"
          >
            <router-link
              class="list__title_link"
              :to="`/p/${item.pid}`"
              rel="noopener noreferrer"
              >{{item.title}}</router-link
            >
          </div>
          <div class="list__mate">阅读 {{item.views}}</div>
        </div>
      </section>
    </aside>
  </div>
</template>

<script>
import { getPostFullInfo, listRecommendPost } from '@/api/post';
import { formatTime, formatTags } from '@/utils';
import PComment from '@/components/PComment';
export default {
  components: {
    PComment,
  },
  filters: {
    formatTime,
    lavelFilter(target){
      return +target
    }
  },
  data() {
    return {
      postLoading: false,
      recommendPostLoading: false,
      pid: this.$route.params.pid || '',
      postInfo: {
        category: {
          title: '',
        },
        user: {
          avatar: '',
        },
      },
      recommendPost: [],
      markdownOption: {},
      tagColors: [
        '#a3d2ca',
        '#158467',
        '#FF0066',
        '#FF00CC',
        '#9900FF',
        '#CC66FF',
        '#2196F3',
        '#42A5F5',
        '#00BCD4',
        '#80DEEA',
        '#4CAF50',
        '#81C784',
      ],
    };
  },
  created() {
    this.getPostInfo(this.pid);
    this.getRecommendPost()
  },
  methods: {
    formatTags,
    getPostInfo(pid) {
      this.postLoading = true;
      getPostFullInfo(pid).then((res) => {
        this.postInfo = res.data;
        this.postLoading = false;
      });
    },
    getRecommendPost(){
      this.recommendPostLoading = true
      listRecommendPost(this.pid,5).then(res => {
        this.recommendPost = res.data
              this.recommendPostLoading = false
      })
    }
  },
};
</script>

<style lang="scss" scope>
.main {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding-top: 10px;
  font-size: 16px;
  .content {
    flex-shrink: 0;
    margin-bottom: 24px;
    margin-right: 10px;
    width: 730px;
    .post {
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      border-radius: 5px;
      padding: 10px;
      margin-bottom: 10px;
      .post__info {
        margin-bottom: 32px;
        font-size: 13px;
        span:after {
          content: ' / ';
        }
        span:last-child:after {
          content: '';
        }
      }
      .post__content {
        .v-note-wrapper .v-note-panel .v-note-show .v-show-content,
        .v-note-wrapper .v-note-panel .v-note-show .v-show-content-html {
          padding: 0;
        }
      }
    }
  }
  .aside {
    flex-shrink: 0;
    width: 260px;
    .aside__top {
      padding: 16px;
      .user_info {
        display: flex;
        align-items: center;
        .el-avatar{
          overflow: visible;
          img{
            border-radius: 50%;
          }
        }
        .info {
          flex-direction: column;
          flex-grow: 1;
          overflow: hidden;
          min-height: 45px;
          margin-left: 8px;

          display: flex;
          justify-content: space-between;
          .username {
            margin-bottom: 4px;
          }
          .sign {
            color: #969696;
            font-size: 12px;
          }
        }
      }
    }
    .aside__recommend {
      padding: 16px;
      .title {
        font-size: 16px;
        padding-left: 6px;
        height: 18px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 16px;
        padding-left: 12px;
        border-left: 4px solid #767ae8;
        font-size: 18px;
        font-weight: 500;
        height: 20px;
        line-height: 20px;
      }
      .listitem {
        margin-bottom: 12px;
        .list__title {
          font-size: 14px;
          line-height: 22px;
          margin-bottom: 4px;
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
        }
        .list__title_link {
          color: inherit;
        }
        .list__mate {
          font-size: 12px;
          color: #969696;
        }
      }
    }
  }
}
</style>
