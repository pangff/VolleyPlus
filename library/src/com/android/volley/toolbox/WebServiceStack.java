package com.android.volley.toolbox;

import com.android.volley.Request;
import com.android.volley.error.AuthFailureError;
import com.android.volley.request.WebServiceBean;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.Map;

/**
 * Created by mac on 15-2-12.
 */
public class WebServiceStack implements HttpStack {


    @Override
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError, XmlPullParserException {

        WebServiceBean webServiceBean = request.getWebServiceParam();

        // 指定WebService的命名空间和调用的方法名
        SoapObject rpc = webServiceBean.getSoapObject();
        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
        envelope.bodyOut = rpc;
        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = true;
        // 等价于envelope.bodyOut = rpc;
        envelope.setOutputSoapObject(rpc);

        HttpTransportSE transport = new HttpTransportSE(webServiceBean.getEndPoint(),request.getTimeoutMs());
        BasicHttpResponse response;
        try {
            // 调用WebService
            transport.call(webServiceBean.getSoapAction(), envelope);
        } catch(SocketTimeoutException e){
            throw e;
        } catch(Exception e) {
            throw e;
        }
        // 获取返回的数据
        if(envelope.bodyIn instanceof SoapObject){
            SoapObject object = (SoapObject) envelope.bodyIn;

            // 获取返回的结果
            String resultS = object.getProperty(0).toString();

            ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
            int responseCode = 200;
            StatusLine responseStatus = new BasicStatusLine(protocolVersion,responseCode, "");
            response = new BasicHttpResponse(responseStatus);
            BasicHttpEntity entity = new BasicHttpEntity();
            InputStream stream = new ByteArrayInputStream(resultS.getBytes("UTF-8"));
            entity.setContent(stream);
            response.setEntity(entity);
        }else if(envelope.bodyIn instanceof SoapFault){
            SoapFault object = (SoapFault) envelope.bodyIn;
            throw new VerifyError(object.faultstring);
        }else{
            throw new VerifyError("服务器异常");
        }

        return response;
    }
}
