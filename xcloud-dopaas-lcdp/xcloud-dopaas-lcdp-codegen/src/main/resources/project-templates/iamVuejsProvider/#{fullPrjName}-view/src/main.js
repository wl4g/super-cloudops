// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import 'register/'
import store from 'store/'
import dictutil from './common/dictutil'
import permitutil from './common/permitutil'
import globalVariable from './common/global_variable.js'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import VueClipboard from 'vue-clipboard2'
import OrganizationSelector from './components/organization-selector';
import SvgIcon from './layout/left-menu/svgIcon'
import i18n from '@/i18n/i18n'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import '@/utils/security/sysmodules'
import splitPane from 'vue-splitpane'



Vue.prototype.dictutil = dictutil;
Vue.prototype.permitutil = permitutil;
Vue.prototype.GLOBAL = globalVariable;
Vue.use(ElementUI, { size: 'mini' });
Vue.use(mavonEditor);
Vue.use(VueClipboard);
Vue.component('organization-selector', OrganizationSelector);
Vue.component('svg-icon', SvgIcon);
Vue.component('split-pane', splitPane);

Vue.config.productionTip = false;
Vue.config.devtools = true;

new Promise(resolve => {
    var iamModuleConfig = { "pluginName": "IamPlugin", "version": "v2.0.0", "modules": [{ "modName": "IamAllModule", "stable": "IAM.all.min.js", "grey": "IAM.all.js", "css_stable": "IAM.all.min.css", "css_grey": "IAM.all.css", "ratio": 100 }], "dependencies": [{ "features": ["IamAll"], "depends": ["IamAllModule"], "sync": true }] };
    new LoaderJS(iamModuleConfig).use("IamAll", function () {
        console.log("******* IAM JSSDK loaded completed! *******");
        resolve()
    })
}).then(() => {
    /* eslint-disable no-new */
    new Vue({
        el: '#app',
        router,
        store,
        i18n,
        template: '<App/>',
        components: { App },
        beforeCreate() {
            console.debug('根组件：beforeCreate')
        },
        created() {
            console.debug('根组件：created')
        },
        beforeMount() {
            console.debug('根组件：beforeMount')
        },
        mounted() {
            console.debug('根组件：mounted')
        }
    });
})
