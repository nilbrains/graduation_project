<template>
  <div class="app-wapper">
    <div class="navbar">
      <div class="left-menu">
          <router-link to="/">
            <el-image class="logo" :src="logo" fit="fill"></el-image>
          </router-link>
      </div>
      <div class="right-menu">
        <el-dropdown class="avatar-container" trigger="click">
          <div class="avatar-wrapper">
            <img
              :src="avatar + '?imageView2/1/w/80/h/80'"
              class="user-avatar"
            />
            <i class="el-icon-caret-bottom" />
          </div>
          <el-dropdown-menu slot="dropdown" class="user-dropdown">
            <router-link to="/">
              <el-dropdown-item>
                主页
              </el-dropdown-item>
            </router-link>
            <el-dropdown-item divided @click.native="logout">
              <span style="display:block;">退出</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
    <el-container class="app-main">
      <el-aside width="200px">
        <el-menu
          mode="vertical"
          router
          :default-active="key"
          style="height: 100%"
        >
          <el-submenu index="2">
            <template slot="title">
              <i class="el-icon-location"></i>
              <span>发布内容</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/center/publish/qaPost">
                <i class="el-icon-guide"></i>
                提问</el-menu-item
              >
              <el-menu-item index="/center/publish/articlePost">
                <i class="el-icon-chicken"></i>
                写文章</el-menu-item
              >
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="3">
            <template slot="title">
              <i class="el-icon-location"></i>
              <span>内容管理</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/center/content/qaContent">
                <i class="el-icon-potato-strips"></i>
                问题列表</el-menu-item
              >
              <el-menu-item index="/center/content/articleContent">
                <i class="el-icon-stopwatch"></i> 文章管理</el-menu-item
              >
            </el-menu-item-group>
          </el-submenu>
          <el-menu-item index="/center/setting/accountInfo">
          <i class="el-icon-user"></i>
            个人信息
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container direction="vertical">
        <el-main height="">
          <section>
            <transition name="fade-transform" mode="out-in">
              <router-view :key="key" />
            </transition>
          </section>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import logo from '@/assets/logo.png'
export default {
  name: 'AppMain',
  computed: {
    key() {
      return this.$route.path
    },
    ...mapGetters(['avatar']),
  },
  data() {
    return {
      logo: logo,
    }
  },
  methods: {
    async logout() {
      await this.$store.dispatch('user/logout')
      this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    },
  },
}
</script>
<style scoped>
.app-main {
  /*50 = navbar  */
  min-height: calc(100vh - 50px);
  width: 100%;
  position: relative;
  overflow: hidden;
}
.fixed-header + .app-main {
  padding-top: 50px;
}
</style>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  .left-menu {
    float: left;
    height: 100%;
    min-height: 50px;
    .logo {
      height: 40px;
      width: 40px;
      border-radius: 50%;
      margin-top: 5px;
      margin-left: 30px;
    }
  }
  .nav {
    display: inline-block;
    font-size: 16px;

    line-height: 50px;
    margin-left: 8px;
  }
  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
