package com.android.volley.request;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.error.ParseError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StrategyRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by mac on 15-2-12.
 */
public class WebServiceRequest<T> extends StrategyRequest<T> {

    WebServiceBean bean;
    private final Response.Listener<T> mListener;

    public WebServiceRequest(int method, String url, WebServiceBean bean,Response.Listener<T> listener, CustomCacheErrorListener errorListener) {
        super(method, url,errorListener);
        this.mListener = listener;
        this.bean = bean;
    }



    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return (Response<T>) Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseIgnoreCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }


    @Override
    public WebServiceBean getWebServiceParam() {
        return bean;
    }

    @Override
    protected void deliverResponse(T response) {
        if(mListener!=null){
            mListener.onResponse(response);
        }
    }

}
