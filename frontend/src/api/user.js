import request from '../utils/request'

export const loginUser = (data) => request.post('/user/login', data)

export const registerUser = (data) => request.post('/user/register', data)

export const deleteUser = (data) => request.post('/user/delete', data)

export const updateUser = (data) => request.post('/user/update', data)

export const identifyUser = (data) => request.post('/user/identify', data)

export const getUserInfo = (params) => request.get('/user/info', { params })

export const getUserList = () => request.get('/user/get')
