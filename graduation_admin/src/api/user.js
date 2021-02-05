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

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
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
    url: '/user',
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

