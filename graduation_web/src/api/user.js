import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export function getInfo() {
  return request({
    url: '/user/info',
    method: 'get'
  })
}

export function getInfoByUid(uid) {
  return request({
    url: `/user/${uid}`,
    method: 'get'
  })
}


export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}

export function createUser(data){
  return request({
    url: '/user',
    method: 'post',
    data
  })
}

export function getUserList(name, current_page, page_size) {
  return request({
    url: '/user/list',
    method: 'get',
    params: {
      name,
      current_page,
      page_size
    }
  })
}

export function editUser(data) {
  return request({
    url: '/user?t='+new Date().getTime(),
    method: 'put',
    data
  })
}

export function changeStatusUser(id) {
  return request({
    url: `/user/${id}/status`,
    method: 'post'
  })
}

export function getUserInfo(){
  return request({
    url: `/user/userinfo`,
    method: 'get'
  })
}

export function userSignIn(){
  return request({
    url: `/user/signIn`,
    method: 'post'
  })
}

export function changePass(data) {
  return request({
    url: '/user/pass',
    method: 'put',
    data
  })
}