import request from '@/utils/request'

export function getSettingAll() {
  return request({
    url: '/setting/all',
    method: 'get'
  })
}

export function getSettingWebInfo() {
  return request({
    url: '/setting/web-info',
    method: 'get'
  })
}

export function setSettingStatement(data) {
  return request({
    url: '/setting/statement',
    method: 'post',
    data
  })
}

export function setSettingSiteUpTime(data) {
  return request({
    url: '/setting/site-up-time',
    method: 'post',
    data
  })
}

export function setSettingSiteRecord(data) {
  return request({
    url: '/setting/site-record',
    method: 'post',
    data
  })
}

