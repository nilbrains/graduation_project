import request from '@/utils/request'

export function getCategoryList(status) {
  return request({
    url: '/cate/list',
    method: 'get',
    params:{
      status
    }
  })
}
