import request from '@/utils/request'

export function uploadImge(param) {
  return request({
    url: '/image',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: {
      param
    }
  })
}
