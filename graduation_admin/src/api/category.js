import request from '@/utils/request'

export function getCategoryList() {
  return request({
    url: '/cate/list/admin',
    method: 'get'
  })
}

export function addCategory(data) {
  return request({
    url: '/cate',
    method: 'post',
    data
  })
}

export function editCategory(data) {
  return request({
    url: '/cate',
    method: 'put',
    data
  })
}

export function changeStatusCategory(id) {
  return request({
    url: `/cate/${id}/status`,
    method: 'post'
  })
}
