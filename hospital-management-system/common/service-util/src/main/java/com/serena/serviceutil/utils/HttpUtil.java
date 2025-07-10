package com.serena.serviceutil.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 */
@Slf4j
public final class HttpUtil {

	static final String POST = "POST";
	static final String GET = "GET";
	static final int CONN_TIMEOUT = 30000;// ms
	static final int READ_TIMEOUT = 30000;// ms

	public static byte[] doPost(String strUrl, byte[] reqData) {
		return send(strUrl, POST, reqData);
	}

	public static byte[] doGet(String strUrl) {
		return send(strUrl, GET, null);
	}

	public static byte[] send(String strUrl, String reqmethod, byte[] reqData) {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
			httpcon.setDoOutput(true);
			httpcon.setDoInput(true);
			httpcon.setUseCaches(false);
			httpcon.setInstanceFollowRedirects(true);
			httpcon.setConnectTimeout(CONN_TIMEOUT);
			httpcon.setReadTimeout(READ_TIMEOUT);
			httpcon.setRequestMethod(reqmethod);
			httpcon.connect();
			if (reqmethod.equalsIgnoreCase(POST)) {
				OutputStream os = httpcon.getOutputStream();
				os.write(reqData);
				os.flush();
				os.close();
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(httpcon.getInputStream(),"utf-8"));
			String inputLine;
			StringBuilder bankXmlBuffer = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {  
			    bankXmlBuffer.append(inputLine);  
			}  
			in.close();  
			httpcon.disconnect();
			return bankXmlBuffer.toString().getBytes();
		} catch (Exception ex) {
			log.error(ex.toString(), ex);
			return null;
		}
	}

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;
	}
}
