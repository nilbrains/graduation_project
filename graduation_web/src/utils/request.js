import axios from 'axios'
import { MessageBox, Message ,Loading} from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'
import router from '@/router'

// create an axios instance
const service = axios.create({
  baseURL: 'http://localhost:8080', // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 5000 // request timeout
})



let loading = null // 定义loading变量
// 开始 加载loading
const startLoading = () => {
  loading = Loading.service({
    lock: true,
    customClass: 'create-isLoading',
    spinner: 'el-icon-loading',
    text: '拼命加载中',
    background: 'rgba(0, 0, 0, 0)'
  })
}
// 结束 取消loading加载
const endLoading = () => {
  loading.close()
}

// showFullScreenLoading() 与 tryHideFullScreenLoading() 目的是合并同一页面多个请求触发loading

let needLoadingRequestCount = 0 // 声明一个变量

const showFullScreenLoading = () => {
  if (needLoadingRequestCount === 0) { // 当等于0时证明第一次请求 这时开启loading
    startLoading()
  }
  needLoadingRequestCount++ // 全局变量值++
}

const tryHideFullScreenLoading = () => {
  if (needLoadingRequestCount <= 0) return // 小于等于0 证明没有开启loading 此时return
  needLoadingRequestCount-- // 正常响应后 全局变量 --
  if (needLoadingRequestCount === 0) { // 等于0 时证明全部加载完毕 此时结束loading 加载
    endLoading()
  }
}


// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent
    if (store.getters.token) {
      config.headers['auth'] = getToken()
    }
    showFullScreenLoading()
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    tryHideFullScreenLoading()
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(

  response => {
    const res = response.data
    tryHideFullScreenLoading()
    // if the custom code is not 20000, it is judged as an error.
    if (res.code !== 20000) {
 

      if (res.code === 404) {
        router.push('/404')
      }
      
      Message({
        message: res.message || 'Error',
        type: 'error',
        duration: 2 * 1000
      })
      // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
      if (res.code === 403 || res.code === '403') {
        // to re-login
        MessageBox.confirm('You have been logged out, you can cancel to stay on this page, or log in again', 'Confirm logout', {
          confirmButtonText: 'Re-Login',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload()
          })
        })
      }
      return res
    } else {
      return res
    }
  },
  error => {
    console.log('err' + error) // for debug
    tryHideFullScreenLoading()
    Message({
      message: error.message,
      type: 'error',
      duration: 2 * 1000
    })
    return Promise.reject(error)
  }
)
window.axios = service
export default service
