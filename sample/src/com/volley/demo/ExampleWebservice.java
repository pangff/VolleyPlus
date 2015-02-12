package com.volley.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.WebServiceBean;
import com.android.volley.request.WebServiceRequest;
import com.android.volley.toolbox.RequestHelper;
import com.android.volley.toolbox.StrategyRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.WebServiceStack;

import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;


public class ExampleWebservice extends ActionBarActivity {
    private TextView mTvResult;
    public static final String  login_nameSpace = "http://com/icss/qsp/webServices/services";
    public static final String  login_method = "loginIn";
    public static final String  login_action = "http://user_ip:user_port/qsp_sc/services/loginIn";
    public static final String  login_endPoint = "http://user_ip:user_port/qsp_sc/services/DCS_JnLoginAction";
    WebServiceBean bean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_request);

        mTvResult = (TextView) findViewById(R.id.tv_result);
        bean = new WebServiceBean() {
            @Override
            public String getEndPoint() {
                return login_endPoint;
            }

            @Override
            public String getSoapAction() {
                return login_action;
            }

            @Override
            public SoapObject getSoapObject() {
                SoapObject  soapObject = new SoapObject(login_nameSpace, login_method);
                String params = "{\"params\":[{\"imei\":\"" + 123456 + "\",\"pwd\":\"" + 123456 + "\"}]}";
                soapObject.addProperty("params", params);
                return soapObject;
            }
        };
        Button btnJsonRequest = (Button) findViewById(R.id.btn_json_request);
        btnJsonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(ExampleWebservice.this,new WebServiceStack());
                WebServiceRequest myReq = new WebServiceRequest(Request.Method.POST,
                        "my_web_service_url",
                        bean,
                        createMyReqSuccessListener(),
                        createMyReqErrorListener());

                myReq.setRequestType(StrategyRequest.RequestType.CACHE_BEFORE_NETWORK);
                RequestHelper.getInstance().doRequest(queue,myReq);
            }
        });
    }

    private Response.Listener<JSONObject> createMyReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mTvResult.setText(response.toString());
            }
        };
    }


    private StrategyRequest.CustomCacheErrorListener createMyReqErrorListener() {
        return new StrategyRequest.CustomCacheErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTvResult.setText(error.getMessage());
            }

            @Override
            public void netWorkErrorReadCache(StrategyRequest<?> request) {

            }
        };
    }
}
