package com.muses;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class TestGetUser {

	@Test
	public void test() throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://localhost:8080/api/user/all");
		
		//httpGet.setEntity(new UrlEncodedFormEntity(nvps,"UTF-8"));
		CloseableHttpResponse response = httpclient.execute(httpGet);
		String responseString = EntityUtils.toString(response.getEntity());
		System.out.println(new String(responseString.getBytes("ISO-8859-1"), "UTF-8"));
	}

}
