<template>
  <div class="app-wapper">
    <div class="navbar">
      <div class="container">
        <div class="left-menu">
          <el-image class="logo" :src="logo" fit="fill"></el-image>
        </div>
        <nav class="nav">
          <el-link icon="el-icon-home">
            <router-link to="/home">
              Hello-World
            </router-link>
          </el-link>
          <el-link icon="el-icon-home">
            <router-link to="/qa">
              问答
            </router-link>
          </el-link>
          <el-input
            class="search-box"
            v-model="searchKey"
            placeholder="搜索一下(回车)"
            prefix-icon="el-icon-search"
            @keyup.enter.native="search"
          ></el-input>
        </nav>
        <div class="right-menu">
          <el-dropdown v-if="islogin" class="avatar-container" trigger="click">
            <div class="avatar-wrapper">
              <img :src="avatar" class="user-avatar" />
              <i class="el-icon-caret-bottom" />
            </div>
            <el-dropdown-menu slot="dropdown" class="user-dropdown">
              <router-link :to="`/u/${uid}`">
                <el-dropdown-item>
                  我的主页
                </el-dropdown-item>
              </router-link>
              <router-link to="/center">
                <el-dropdown-item>
                  个人中心
                </el-dropdown-item>
              </router-link>
              <el-dropdown-item divided @click.native="logout">
                <span style="display:block;">退出</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <span v-else>
            <router-link type="text" :to="`/login?redirect=${this.$route.path}`">登录</router-link> /
            <router-link type="text" to="/reg">注册</router-link>
          </span>
        </div>
      </div>
    </div>
    <section class="app-main">
      <transition name="fade-transform" mode="out-in">
        <router-view :key="key" />
      </transition>
    </section>
     <el-backtop :bottom="100">
      <div
        style="{
          height: 100%;
          width: 100%;
          background-color: #FFFFFF;
          box-shadow: rgba(0, 0, 0, 0.12) 0px 0px 9px 3px;
          text-align: center;
          line-height: 40px;
          color: #1989fa;
          border-radius: 10px;
        }"
      >
        UP
      </div>
    </el-backtop>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import logo from '@/assets/logo.png'
export default {
  name: 'AppMain',
  computed: {
    ...mapGetters(['avatar', 'islogin', 'name', 'uid']),
    key() {
      return this.$route.path
    },
  },
  data() {
    return {
      searchKey: '',
      logo: logo,
    }
  },
  methods: {
    async logout() {
      await this.$store.dispatch('user/logout')
      this.$router.push(`/redirect${this.$route.fullPath}`)
    },
    search() {
      this.$nextTick(() => {
        this.$router.replace({
          path: '/redirect' + `/s?word=${this.searchKey}`,
        })
      })
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
  padding-bottom: 10px;
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
      margin-left: 5px;
    }
  }
  .nav {
    display: inline-block;
    font-size: 16px;
    font-weight: 800;
    line-height: 50px;
    margin-left: 8px;
    .el-link {
      margin: 0 15px;
    }
    .search-box {
      margin-left: 15px;
      width: 250px;
    }
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
