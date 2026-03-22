import request from '../utils/request'

export const getItemList = (data) => request.post('/good/info', data)

export const searchItemByLabel = (data) => request.post('/good/search', data)
