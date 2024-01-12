// http.js
//from https://github.com/huangnuoen/learn-vue/blob/master/vue%20axios%E5%B0%81%E8%A3%85.md
import axios from 'axios'

// 环境的切换
// if (process.env.NODE_ENV === 'development') {
//   axios.defaults.baseURL = '/proxyApi'
// } else if (process.env.NODE_ENV === 'production') {
//   axios.defaults.baseURL = 'http://localhost:8080/api/'
// }else{
//   axios.defaults.baseURL = 'http://localhost:8080/api/'
// }

axios.defaults.baseURL = 'http://localhost:8081/api'
//解决后端存储不了session的问题
axios.defaults.withCredentials = true
axios.defaults.responseType = 'json'

// 请求拦截器
// 每次请求是否携带token
// axios.interceptors.request.use(
//   config => {
//     /*
//     This line checks if a token variable is defined. If it is,
//      it sets the Authorization header in the request's config object to the value of the token
//     */
//     token && (config.headers.Authorization = token)
//     return config
//   },
//   error => {
//     return Promise.error(error)
//   })

axios.defaults.timeout = 10000

axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8'

// 响应拦截器
//需要更据具体api进行修改
/**
 *   SUCCESS("200","请求成功"),
    FAIL("400","操作失败"),
    DATABASE_ERROR("301","数据库查询错误"),
    USER_ERROR("302","用户名或密码错误"),
    SERVER_ERROR("500","内部服务器错误");
 */
axios.interceptors.response.use(
  response => {
    console.log('axios response ', response);
    if (response.status === 200) {
      const res = response.data
      console.log('axios rese');
      //返回来的是字符串，不能用 === 否则会判断不了，严格相等
      if (res.statusCode == 200) {
        console.log('Request was successful:', res.content);
        return Promise.resolve(res.content)
      } else {
        console.log('Request failed with statusCode:', res.statusMessage);
        // 失败,如果是在登录中，最好输出错误信息，
        return Promise.reject(res.statusMessage)
      } 
      
    } else {
      console.error("Request failed with status:", response);
      return Promise.reject(response)
    }
  },
  error => {
    console.error("axios errr", error);
    // Handle error cases
    return Promise.reject(error);
  })

// get 请求
export function httpGet({
  url,
  params = {}
}) {
  return new Promise((resolve, reject) => {
    axios.get(url, {
      params
    }).then((res) => {
      resolve(res)
    }).catch(err => {
      reject(err)
    })
  })
}

// post请求
export function httpPost({
  url,
  data = {},
  params = {}
}) {
  console.log('excute httpRequest');
  return new Promise((resolve, reject) => {
    axios({
      url,
      method: 'post',
      /**
       * The transformRequest function you've provided is used to serialize the data object 
       * into a URL-encoded string. This is commonly done when you 
       * want to send data in the format of application/x-www-form-urlencoded in a POST request.
       */
      // transformRequest: [function (data) {
      //   let ret = ''
      //   for (let it in data) {
      //     ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      //   }
      //   return ret
      // }],
      // 发送的数据
      data,
      // �url参数
      params

    }).then(res => {
      //这里的res是由响应拦截器返回的
      console.log(("http res:"+JSON.stringify(res)));
      resolve(res)
    }).catch(error => {
      console.log("http error "+error);
      reject(error); // Ensure to reject the Promise on Axios error
    });
  })
}

//other request
// post请求
export function httpRequest({
  url,
  data = {},
  params = {},
  //默认get
  method = 'post',
  // 默认的 headers 对象
  headers = {},
}) {
  console.log("http request");
  return new Promise((resolve, reject) => {
    if (!headers['Content-Type']) {
      headers['Content-Type'] = 'application/json';
    }
    axios({
      url,
      method,
      // 发送的数据
      data,
      // �url参数
      params,
     

    }).then(res => {
      resolve(res)
    }).catch(err=>{
        console.log('other request err ',err)
        reject(err)
      })
  })
}