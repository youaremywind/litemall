import request from '@/utils/request'

export function listContracts(query) {
  return request({
    url: '/contracts/list',
    method: 'get',
    params: query
  })
}

export function deleteContracts(data) {
  return request({
    url: '/contracts/delete',
    method: 'post',
    data
  })
}

export function publishContracts(data) {
  return request({
    url: '/contracts/create',
    method: 'post',
    data
  })
}

export function detailContracts(id) {
  return request({
    url: '/contracts/detail',
    method: 'get',
    params: { id }
  })
}

export function editContracts(data) {
  return request({
    url: '/contracts/update',
    method: 'post',
    data
  })
}

export function listCatAndBrand() {
  return request({
    url: '/contracts/catAndBrand',
    method: 'get'
  })
}
