package com.android.volley.toolbox;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;


/**
 * Created by pangff on 15-2-4.
 * 请求heler 自定义缓存策略
 */
public class RequestHelper {

    private static RequestHelper requestHelper;

    private  RequestHelper(){

    }

    public static RequestHelper getInstance(){
        if(requestHelper==null){
            requestHelper = new RequestHelper();
        }
        return requestHelper;
    }



    /**
     * 发送请求
     * @param queue
     * @param request
     */
    public void doRequest(RequestQueue queue, StrategyRequest<?> request) {

        /** 如果是查询缓存后查询网络－两次返回 **/
        if (request.getRequestType() == StrategyRequest.RequestType.CACHE_BEFORE_NETWORK) {
            long ttl = System.currentTimeMillis() + 60 * 1000;
            long softTtl = -1;
            setCacheControl(queue, request, ttl, softTtl);
        }

        /** 如果要求只查询网络 **/
        if (request.getRequestType() == StrategyRequest.RequestType.NETWORK) {
            long time = -1;
            setCacheControl(queue, request, time, time);
        }

        /** 如果要求只读缓存 **/
        if (request.getRequestType() == StrategyRequest.RequestType.CACHE) {
            long time = System.currentTimeMillis() + 60 * 1000;
            setCacheControl(queue, request, time, time);
        }

        queue.add(request);
    }


    /**
     * @param queue
     * @param request
     * @param ttl
     * @param softTtl
     */
    private void setCacheControl(RequestQueue queue, Request<?> request, long ttl, long softTtl) {
        Cache.Entry entry = queue.getCache().get(request.getCacheKey());
        if (entry != null) {
            entry.ttl = ttl;
            entry.softTtl = softTtl;
        }
        queue.getCache().put(request.getCacheKey(),entry);
    }


}
