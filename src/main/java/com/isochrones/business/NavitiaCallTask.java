package com.isochrones.business;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NavitiaCallTask implements Callable<HashMap<Integer, String>> {

	private List<Integer> durations;
	private LocalDateTime date;
	private String from;
	
	public NavitiaCallTask(List<Integer> durations, LocalDateTime date, String from) {
		this.durations = durations;
		this.date = date;
		this.from = from;
	}

	@Override
	public HashMap<Integer, String> call() throws Exception {
		long responseTime = System.currentTimeMillis();
		
		CloseableHttpClient httpclient;
		CloseableHttpResponse response = null;

		HashMap<Integer, String> geojsonByDuration = new HashMap<>();
		
		try {

			URIBuilder uriBuilder = new URIBuilder("https://api.navitia.io/v1/coverage/fr-idf/isochrones?")
					.addParameter("from", from)
					.addParameter("datetime", date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
			durations.forEach(x -> uriBuilder.addParameter("boundary_duration[]", x.toString()));
			URI uri = uriBuilder.build();
			
			HttpGet httpGet = new HttpGet(uri);

		    System.out.println(httpGet);
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(
					new AuthScope(uri.getHost(), uri.getPort()),
					new UsernamePasswordCredentials("a2635513-5107-4d03-9644-afbc5cdf090f", ""));

			httpclient = HttpClients.custom()
					.setDefaultCredentialsProvider(credsProvider)
					.build();
			
			response = httpclient.execute(httpGet);
		    System.out.println(response.getStatusLine());
		    
		    ObjectMapper objectMapper = new ObjectMapper();
		    JsonNode jsonNode = objectMapper.readTree(response.getEntity().getContent());

		    JsonNode isochrones = jsonNode.get("isochrones");
		    
		    isochrones.forEach(x -> geojsonByDuration.put(x.get("max_duration").asInt(), x.get("geojson").toString()));
		    
		    return geojsonByDuration;
		    
		} finally {
		    try {
				if (response != null) {
					response.close();
				}
			} catch (IOException ignore) {
			}
			responseTime = System.currentTimeMillis()-responseTime;
			System.out.println("Navitia response-time : "+responseTime+"ms");
		}
	}
}