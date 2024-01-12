import Vue from 'vue'
import App from './App.vue'
import router from './router/index'
import store from './store/index'
import axios from 'axios'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import VueParticles from 'vue-particles';

Vue.use(ElementUI)
Vue.use(VueParticles)
Vue.config.productionTip = false

//加入到vue的属性中，可以使用this.$axios
// Vue.prototype.$axios = axios

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
