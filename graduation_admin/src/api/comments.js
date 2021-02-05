import request from '@/utils/request'

export function getComments(current_page, page_size) {
  return request({
    url: '/comments',
    method: 'get',
    params: {
      current_page,
      page_size
    }
  })
}

export function delPostComment(id) {
  return request({
    url: `/comment/${id}`,
    method: 'delete'
  })
}