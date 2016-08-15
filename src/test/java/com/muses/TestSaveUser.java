package com.muses;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class TestSaveUser {

	@Test
	public void test() throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("http://localhost:8080/api/user");
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("openId", "22234456778886543335678997654"));
		nvps.add(new BasicNameValuePair("headpic", "http://baidu.com/test4.jpg"));
		
		httpPost.setEntity(new UrlEncodedFormEntity(nvps,"UTF-8"));
		CloseableHttpResponse response = httpclient.execute(httpPost);
		String responseString = EntityUtils.toString(response.getEntity());
		System.out.println(new String(responseString.getBytes("ISO-8859-1"), "UTF-8"));
		
	}

}
