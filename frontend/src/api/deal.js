import request from '../utils/request'

// 分页查询订单
export const getDealList = (data) => request.post('/deal/info', data)

// 新增订单
export const insertDeal = (data) => request.post('/deal/insert', data)

// 删除订单
export const deleteDeal = (data) => request.post('/deal/deleteByID', data)

// 更新订单
export const updateDeal = (data) => request.post('/deal/update', data)

// 创建订单（含支付）
export const createDeal = (data) => request.post('/deal/create', data)
