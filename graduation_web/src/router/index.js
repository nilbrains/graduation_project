import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

import Layout from '@/layout';
import Admin from '@/layout/admin';

export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    meta: { needLogin: false }
  },

  {
    path: '/reg',
    component: () => import('@/views/login/reg'),
    meta: { needLogin: false }
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    meta: { needLogin: false }
  },
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index'),
        meta: { needLogin: false }
      }
    ],
    meta: { needLogin: false }
  },
  
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    meta: { needLogin: false },
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/views/index/index'),
        meta: { title: '首页', icon: 'dashboard',needLogin: false }
      },
      {
        path: 'p/:pid',
        name: 'P',
        component: () => import('@/views/post/pid'),
        meta: { title: '文章详情', icon: 'dashboard',needLogin: false }
      },
      {
        path: 's',
        name: 'Search',
        component: () => import('@/views/search/index'),
        meta: { title: '搜索', icon: 'dashboard' ,needLogin: false}
      },
      {
        path: 'c/:cid',
        name: 'Cate',
        component: () => import('@/views/search/cate'),
        meta: { title: '分类', icon: 'dashboard' ,needLogin: false}
      },
      {
        path: 'u/:uid',
        name: 'Person',
        component: () => import('@/views/user/uid'),
        meta: { title: '用户', icon: 'dashboard' ,needLogin: false}
      },
      {
        path: 'qa',
        name: 'Qa',
        component: () => import('@/views/index/qa'),
        meta: { title: '问答', icon: 'dashboard' ,needLogin: false}
      }
    ]
  },

  {
    path: '/center',
    component: Admin,
    redirect: '/center/setting/accountInfo',
    meta: { needLogin: true },
    children: [
      {
        path: 'home',
        name: 'CenterHome',
        component: () => import('@/views/index/index'),
        meta: { title: '首页', icon: 'dashboard' ,needLogin: true}
      },
      {
        path: 'publish/qaPost',
        name: 'QaPost',
        component: () => import('@/views/publish/qaPost'),
        meta: { title: '提问', icon: 'dashboard' ,needLogin: true}
      },
      {
        path: 'publish/articlePost',
        name: 'ArticlePost',
        component: () => import('@/views/publish/articlePost'),
        meta: { title: '文章', icon: 'dashboard' ,needLogin: true}
      },
      {
        path:'content/qaContent',
        name: 'QaContent',
        component: () => import('@/views/content/qaContent'),
        meta: { title: '问题列表', icon: 'dashboard' ,needLogin: true}
      },
      {
        path:'content/articleContent',
        name: 'ArticleContent',
        component: () => import('@/views/content/articleContent'),
        meta: { title: '文章管理', icon: 'dashboard' ,needLogin: true}
      },
      {
        path:'setting/accountInfo',
        name: 'AccountInfo',
        component: () => import('@/views/setting/accountInfo'),
        meta: { title: '个人信息', icon: 'dashboard' ,needLogin: true}
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
