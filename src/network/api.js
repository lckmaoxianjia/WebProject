import { httpGet, httpPost, httpRequest } from './http'

export function getBookList(params){
  //交给页面层处理promise
 return httpGet({url:'getBookList',params})

}

export function addBook(data){
  //交给页面层处理promise
 return httpRequest({url:'addBook',data,headers:{'Content-Type':'application/json'}})

}

export function deletBookById(id){
  //交给页面层处理promise
 return httpRequest({url:'delete',params:{id},method:'delete'})

}

export function getBookById(id){
  //交给页面层处理promise
 return httpGet({url:`book/${id}`})

}
//这里必须要返回
export function login(data){
  //交给页面层处理promise
 return httpPost({url:'login',data})

}