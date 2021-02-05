import request from '@/utils/request'

export function uploadImge(formData) {
  return request.post(
    '/image',
    formData,
    {
      headers: {
        'Content-Type': 'multipart/form-data'
      },
    }
  )
}
