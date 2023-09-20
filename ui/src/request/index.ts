/**
 * This module will:
 *
 * 1. Create an initialized axios instance
 *
 * 2. Add request interceptor to log request info
 *
 * 3. Add response interceptor to handle some global response codes
 */
import axios from 'axios';
import {PageLocation} from '@/ts/dynamicLocation';
import {Notification} from 'element-ui'

// create an axios instance
const service = axios.create({
    baseURL: new PageLocation().baseURL, // url = base url + request url
    // withCredentials: true, // send cookies when cross-domain requests
    timeout: 5000, // request timeout
});

// request interceptor
service.interceptors.request.use(
    (config) => {
        let logtag = `Network Request: =====> ${config.url}\n`;
        console.log(logtag, config);
        return config;
    },
    (error) => {
        // do something with request error
        console.log(error); // for debug
        return Promise.reject(error);
    },
);

// response interceptor
service.interceptors.response.use(
    /**
     * If you want to get http information such as headers or status
     * Please return  response => response
     */

    /**
     * Determine the request status by custom code
     * Here is just an example
     * You can also judge the status by HTTP Status Code
     */
    (response) => {
        // log only
        let relativeUrl = response.config.url!.substring(
            response.config.baseURL!.length,
        );
        let logtag = `Network Response: <==== ${relativeUrl}\n`;
        console.log(logtag, response);

        return Promise.resolve(response);
    },
    (error) => {
        let response = error.response;
        if (response == null) {
            // if the target is not reachable, the response will be null
            // this is comprehensible, the request is not reaching the server, thus the response should
            // be not present.
            Notification.error('Server unreachable');
            return Promise.reject();
        }

        // when code comes here, means the status is not 200
        let status = response.status;
        let responseBody = response.data;
        Notification.error(`${status}: ${responseBody}`);
        return Promise.reject(response);
    },
);

export default service;
