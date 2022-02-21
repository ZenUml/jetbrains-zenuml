import Vue from 'vue'
import VueGtag from 'vue-gtag';
import App from './App.vue'
import router from './router'
import store from './store'
import 'vue-sequence/dist/vue-sequence.css'

Vue.use(VueGtag, {
  config: {
    id: 'UA-114996686-1'
  }
})

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
