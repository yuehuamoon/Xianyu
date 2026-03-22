import request from '../utils/request'

// 获取商品列表
export const getGoodList = (data) => request.post('/good/info', data)

// 搜索商品
export const searchGood = (data) => request.post('/good/search', data)

// 新增商品
export const insertGood = (data) => request.post('/good/insert', data)

// 删除商品
export const deleteGood = (data) => request.post('/good/deleteByID', data)

// 更新商品
export const updateGood = (data) => request.post('/good/update', data)

// 商品审核 (Admin)
export const reviewGood = (data) => request.post('/good/review/confirm', data)

// 获取待审核商品列表 (Admin)
export const getReviewList = (data) => request.post('/good/review/info', data)
