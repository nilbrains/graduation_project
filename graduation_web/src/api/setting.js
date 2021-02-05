import request from '@/utils/request'

export function getSettingStatement() {
  return request({
    url: '/setting/statement',
    method: 'get'
  })
}

export function getSettingWebInfo() {
  return request({
    url: '/setting/web-info',
    method: 'get'
  })
}