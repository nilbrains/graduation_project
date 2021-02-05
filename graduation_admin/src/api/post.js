import request from '@/utils/request'

export function getPostList(query, current_page, page_size) {
  return request({
    url: '/post/list/admin',
    method: 'get',
    params: {
      title: query.title || '',
      cid: query.cid || '',
      type: query.type || '',
      current_page,
      page_size
    }
  })
}

export function delPost(id) {
  return request({
    url: `/post/${id}`,
    method: 'delete'
  })
}
