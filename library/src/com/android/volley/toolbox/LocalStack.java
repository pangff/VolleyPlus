package com.android.volley.toolbox;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.error.AuthFailureError;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by mac on 15-2-12.
 */
public class LocalStack implements HttpStack {

    FileType fileType;
    Context context;

    public enum FileType{
        RAW,
        ASSERT,
        FILE
    }

    public LocalStack(Context context,FileType fileType){
        this.fileType = fileType;
        this.context = context;
    }


    @Override
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError, XmlPullParserException {

        BasicHttpResponse response;
        try {
            // 调用WebService
            ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
            int responseCode = 200;
            StatusLine responseStatus = new BasicStatusLine(protocolVersion,responseCode, "");
            response = new BasicHttpResponse(responseStatus);
            BasicHttpEntity entity = new BasicHttpEntity();
            InputStream stream;
            if(fileType==FileType.RAW){
                stream =  context.getResources().openRawResource(Integer.parseInt(request.getUrl()));
            }else if(fileType==FileType.ASSERT){
                stream =  context.getResources().getAssets().open(request.getUrl());
            }else{
                stream = readFile(request.getUrl());
            }
            entity.setContent(stream);
            response.setEntity(entity);
        } catch(IOException e) {
            throw e;
        }
        return response;
    }


    //读文件
    public InputStream readFile(String fileName) throws IOException {

        File file = new File(fileName);

        FileInputStream fis = new FileInputStream(file);

        return fis;
    }
}
