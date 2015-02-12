package com.volley.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.LocalStack;
import com.android.volley.toolbox.RequestHelper;
import com.android.volley.toolbox.StrategyRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class ExampleLocalJsonRequest extends ActionBarActivity {

    private TextView mTvResult;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_example_local_json_request);

        mTvResult = (TextView) findViewById(R.id.tv_result);

        Button btnJsonRequest = (Button) findViewById(R.id.btn_json_request);
        btnJsonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queue = Volley.newRequestQueue(ExampleLocalJsonRequest.this,new LocalStack(ExampleLocalJsonRequest.this.getApplicationContext(),LocalStack.FileType.RAW));
                ExampleLocalJsonRequest.this.getResources().openRawResource(R.raw.data);
                JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.GET,
                        String.valueOf(R.raw.data),
                        null,
                        createMyReqSuccessListener(),
                        createMyReqErrorListener());
                myReq.setRequestType(StrategyRequest.RequestType.NETWORK);
                RequestHelper.getInstance().doRequest(queue,myReq);
                //queue.add(myReq);

            }
        });
    }


    private Response.Listener<JSONObject> createMyReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    mTvResult.setText(response.getString("message"));
                } catch (JSONException e) {
                    mTvResult.setText("Parse error");
                }
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
                Toast.makeText(ExampleLocalJsonRequest.this, "netWorkErrorReadCache", Toast.LENGTH_LONG).show();
                request.setRequestType(StrategyRequest.RequestType.CACHE);
                RequestHelper.getInstance().doRequest(queue,((StrategyRequest)request));
            }
        };
    }


}
