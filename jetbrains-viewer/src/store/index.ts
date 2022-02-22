import Vue from 'vue'
import Vuex from 'vuex'
import { VueSequence } from 'vue-sequence';


Vue.use(Vuex)

export default new Vuex.Store(VueSequence.Store())
