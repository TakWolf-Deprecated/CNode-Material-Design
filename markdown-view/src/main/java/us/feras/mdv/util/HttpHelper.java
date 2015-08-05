package us.feras.mdv.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

/**
 * @author Feras Alnatsheh
 */

public class HttpHelper {

	private static final String CHARSET_UTF8 = "UTF-8";
	public static final String CONTENT_TYPE_JSON = "json";
	public static final String CONTENT_TYPE_XML = "xml";

	// Timeout when reading from Input stream when a connection is established
	// to a resource
	private static final int DEFULT_READ_TIMEOUT = 5000;
	// Timeout for establishing a connection.
	private static final int DEFULT_CONNECT_TIMEOUT = 5000;

	static public Response get(String url, String query)
			throws MalformedURLException, IOException {
		return get(url, query, DEFULT_CONNECT_TIMEOUT, DEFULT_READ_TIMEOUT);
	}

	static public Response get(String url) throws MalformedURLException,
			IOException {
		return get(url, null, DEFULT_CONNECT_TIMEOUT, DEFULT_READ_TIMEOUT);
	}

	static public Response get(String url, String query, int connectTimeout,
			int readTimeout) throws MalformedURLException, IOException {
		String fullUrl = url;
		if (query != null && !query.equals("")) {
			fullUrl += "?" + query;
		}
		URLConnection connection = new URL(fullUrl).openConnection();
		connection.setReadTimeout(readTimeout);
		connection.setConnectTimeout(connectTimeout);
		connection.setRequestProperty("Accept-Charset", CHARSET_UTF8);
		return getResponse((HttpURLConnection) connection);
	}

	static public Response post(String url, String query, String contentType)
			throws MalformedURLException, IOException {
		return post(url, query, contentType, DEFULT_CONNECT_TIMEOUT,
				DEFULT_READ_TIMEOUT);
	}

	static public Response post(String url, String query, String contentType,
			int connectTimeout, int readTimeout) throws MalformedURLException,
			IOException {
		URLConnection connection = new URL(url).openConnection();
		connection.setReadTimeout(readTimeout);
		connection.setConnectTimeout(connectTimeout);
		connection.setDoOutput(true); // Triggers POST.
		connection.setRequestProperty("Accept-Charset", CHARSET_UTF8);
		connection.setRequestProperty("Content-Type", "application/"
				+ contentType);
		OutputStream output = null;
		try {
			output = connection.getOutputStream();
			output.write(query.getBytes(CHARSET_UTF8));
		} finally {
			closeSilently(output);
		}
		return getResponse((HttpURLConnection) connection);
	}

	/*
	 * Open the input stream to get responses from the server.
	 */
	private static Response getResponse(HttpURLConnection connection)
			throws IOException {
		InputStream inputStream = connection.getInputStream();
		Response response = new Response();
		response.setHttpResponseCode(connection.getResponseCode());
		response.setHttpResponseHeader(connection.getHeaderFields().entrySet());
		response.setResponseMessage(getResponseMessage(inputStream, connection));
		response.setHttpResponseMessage(connection.getResponseMessage());
		return response;
	}

	/*
	 * Get the HTTP response message from the server.
	 */
	private static String getResponseMessage(InputStream inputStream,
			HttpURLConnection connection) throws UnsupportedEncodingException,
			IOException {
		String responseMessage = null;
		StringBuffer sb = new StringBuffer();
		InputStream dis = connection.getInputStream();
		int chr;
		while ((chr = dis.read()) != -1) {
			sb.append((char) chr);
		}
		if (sb != null) {
			responseMessage = sb.toString();
		}
		return responseMessage;
	}

	/*
	 * Close the connection, if the connection could not be closed (probably
	 * because its already closed) ignore the error.
	 */
	private static void closeSilently(OutputStream output) {
		if (output != null) {
			try {
				output.close();
			} catch (IOException e) {
			}
		}
	}

	public static class Response {

		private Set<Entry<String, List<String>>> httpResponseHeader;
		private int httpResponseCode;
		private String httpResponseMessage;
		private String serverResponseMessage;

		Response() {
		}

		Response(Set<Entry<String, List<String>>> httpResponseHeader,
				int httpResponseCode, String httpResponseMessage,
				String responseMessage) {
			setHttpResponseHeader(httpResponseHeader);
			setHttpResponseCode(httpResponseCode);
			setHttpResponseMessage(httpResponseMessage);
			setResponseMessage(responseMessage);
		}

		public String getHttpResponseMessage() {
			return httpResponseMessage;
		}

		public void setHttpResponseMessage(String httpResponseMessage) {
			this.httpResponseMessage = httpResponseMessage;
		}

		public Set<Entry<String, List<String>>> getHttpResponseHeader() {
			return httpResponseHeader;
		}

		public void setHttpResponseHeader(
				Set<Entry<String, List<String>>> httpResponseHeader) {
			this.httpResponseHeader = httpResponseHeader;
		}

		public int getHttpResponseCode() {
			return httpResponseCode;
		}

		public void setHttpResponseCode(int httpResponseCode) {
			this.httpResponseCode = httpResponseCode;
		}

		public String getResponseMessage() {
			return serverResponseMessage;
		}

		public void setResponseMessage(String responseMessage) {
			this.serverResponseMessage = responseMessage;
		}

		public String toString() {
			return "httpResponseCode = " + httpResponseCode + " , "
					+ "httpResponseMessage = " + httpResponseMessage + " , "
					+ "serverResponseMessage = " + serverResponseMessage;
		}

	}

}