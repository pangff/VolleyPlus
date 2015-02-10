package com.android.volley.toolbox;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.error.VolleyError;

/**
 * Created by pangff on 15-2-4.
 */
public abstract class StrategyRequest<T> extends Request<T> {

    public RequestType requestType;
    CustomCacheErrorListener mErrorListener;

    public interface CustomCacheErrorListener extends Response.ErrorListener {
        public void netWorkErrorReadCache(StrategyRequest<?> request);

    }
    /**
     * @param method
     * @param url
     * @param listener
     */
    public StrategyRequest(int method, String url, CustomCacheErrorListener listener) {
        super(method,url,listener);
        this.mErrorListener = listener;
    }

    /**
     * Creates a new request with the given method (one of the values from {@link Method}),
     * URL, priority, error listener and retry policy.  Note that the normal response listener is not provided here as
     * delivery of responses is provided by subclasses, who have a better idea of how to deliver
     * an already-parsed response.
     */
    public StrategyRequest(int method, String url, Priority priority,CustomCacheErrorListener listener, RetryPolicy retryPolicy) {
        super(method,url,priority,listener,retryPolicy);
        this.mErrorListener = listener;
    }


    public enum RequestType {
        SERVER_CONTROL,
        CACHE,
        CACHE_BEFORE_NETWORK,
        NETWORK
    }

    public void setRequestType(RequestType requestType){
        this.requestType = requestType;
    }

    public RequestType getRequestType(){
        return requestType;
    }


    @Override
    public void deliverError(VolleyError error) {
        if(this.getRequestType()==RequestType.NETWORK){
            this.setRequestType(RequestType.CACHE);
            mErrorListener.netWorkErrorReadCache(this);
        }
        super.deliverError(error);
    }
}
