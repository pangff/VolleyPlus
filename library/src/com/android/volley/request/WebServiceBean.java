package com.android.volley.request;



import org.ksoap2.serialization.SoapObject;

/**
 * add buy pangff
 * 2015-02-12
 */
public abstract class WebServiceBean{

	public abstract String getEndPoint();

	public abstract String getSoapAction();

	public abstract SoapObject getSoapObject();
}
