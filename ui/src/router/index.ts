import Vue from 'vue';
import VueRouter, { RouteConfig } from 'vue-router';
import MainFrameView from "@/views/index.vue";
import IndexView from "@/views/user/index.vue";
import RecordView from "@/views/user/record.vue";
Vue.use(VueRouter);

const routes: Array<RouteConfig> = [
    {
        path: '/',
        redirect: '/index',
    },
    {
        path: '/index',
        component: MainFrameView,
        children: [
            {
                path: '',
                component: IndexView,
            },
            {
                path: 'record',
                component: RecordView,
            }
        ]
    },

];

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes,
});

export default router;
