import request from '@/utils/request'

export function getPostList(query, current_page, page_size) {
  return request({
    url: '/post/list',
    method: 'get',
    params: {
      title: query.title || '',
      cid: query.cid || '',
      type: query.type || '',
      answer: query.answer || '',
      current_page,
      page_size
    }
  })
}

export function getUserPostList(type, answer, current_page, page_size) {
  return request({
    url: '/post/user/list',
    method: 'get',
    params: {
      type: type || '',
      answer: answer || '',
      current_page,
      page_size
    }
  })
}

export function getUserByUidPostList(uid,type, answer, current_page, page_size) {
  return request({
    url: `/post/user/${uid}/list`,
    method: 'get',
    params: {
      type: type || '',
      answer: answer || '',
      current_page,
      page_size
    }
  })
}

export function updatePost(data){
  return request({
    url: `/post`,
    method: 'put',
    data
  })
}


export function addPost(type,data){
  return request({
    url: `/post`,
    method: 'post',
    params: {
      type
    },
    data
  })
}

export function getPostFullInfo(pid){
  return request({
    url: `/post/${pid}/full`,
    method: 'get',
  })
}

export function getPostInfo(pid){
  return request({
    url: `/post/${pid}`,
    method: 'get',
  })
}

export function delPost(id) {
  return request({
    url: `/post/${id}`,
    method: 'delete'
  })
}

export function changePostAnswer(id) {
  return request({
    url: `/post/${id}/answer`,
    method: 'post'
  })
}

export function getPostComment(pid,current_page,page_size) {
  return request({
    url: `/comment/${pid}`,
    method: 'get',
    params: {
      current_page,
      page_size
    }
  })
}

export function addPostComment(pid,data) {
  return request({
    url: `/comment/${pid}`,
    method: 'post',
    data
  })
}

export function delPostComment(id) {
  return request({
    url: `/comment/${id}`,
    method: 'delete'
  })
}

export function listRecommendPost(pid,size) {
  return request({
    url: `/post/recommend`,
    method: 'get',
    params: {
      pid,
      size
    }
  })
}

export function commentSetTrue(id){
  return request({
    url: `/comment/${id}/true`,
    method: 'post'
  })
}
