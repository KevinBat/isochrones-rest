package com.isochrones.business;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.locationtech.jts.io.geojson.GeoJsonWriter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isochrones.model.Geojson;
import com.isochrones.model.Isochrones;

@Component
public class IsochronesBusiness {

	public Isochrones getIsochrones(String from, List<Integer> durations, String beginDateString, Integer delay, Integer nb) throws IOException, URISyntaxException, ParseException, InterruptedException, ExecutionException {
		
		// Define the different dates
		LocalDateTime beginDate = LocalDateTime.parse(beginDateString);
		List<LocalDateTime> dates = new ArrayList<>();
		for (int i = 0; i < nb; i++) {
			dates.add(beginDate.plusMinutes(delay*i));
		}
		durations.sort(Comparator.naturalOrder());

		// Init HashMap <duration, List<geoJson>>
		HashMap<Integer, List<String>> geoJsonListMap = new HashMap<>();
		durations.forEach(x -> geoJsonListMap.put(x, new ArrayList<>()));
		

		ExecutorService executor = Executors.newFixedThreadPool(10);
		List<Future<HashMap<Integer, String>>> futureList = new ArrayList<>();
		// Put all geoJson in map by duration
		for (LocalDateTime date : dates) {
			Callable<HashMap<Integer, String>> task = new NavitiaCallTask(durations, date, from);
			futureList.add(executor.submit(task));
		}
				
		// Put all geoJson in map by duration
		for (Future<HashMap<Integer, String>> future : futureList) {
			HashMap<Integer, String> geoJsonMap = future.get();
			durations.forEach(x -> geoJsonListMap.get(x).add(geoJsonMap.get(x)));
		}

		// Merge all geojson multipolygon by duration
		List<Geojson> geojsons = new ArrayList<>(); 
	    ObjectMapper objectMapper = new ObjectMapper();
		for (Integer duration : durations) {
			String unionGeojsonString = jtsUnion(geoJsonListMap.get(duration));
			Geojson unionGeojson = objectMapper.readValue(unionGeojsonString, Geojson.class);
			geojsons.add(unionGeojson);
			System.out.println("geojsons.add(unionGeojson);"+unionGeojson.toString());
		}
		
		return new Isochrones(geojsons);
	}

	private String jtsUnion(List<String> geoJsonList) throws ParseException {
		
		GeoJsonReader geoJsonReader = new GeoJsonReader();
		GeoJsonWriter geoJsonWriter = new GeoJsonWriter();
		GeometryFactory geometryFactory = new GeometryFactory();
		
		String result= "";

		List<Geometry> geometries = new ArrayList<>();
		for (String geoJson : geoJsonList) {
			geometries.add(geoJsonReader.read(geoJson));
		}

		GeometryCollection collection = geometryFactory.createGeometryCollection(geometries.toArray(new Geometry[0]));

		Geometry geometryResult = collection.buffer(0);

		if(geometryResult instanceof Polygon) {
			geometryResult = geometryFactory.createMultiPolygon(new Polygon[] {(Polygon)geometryResult});
		}

		result = geoJsonWriter.write(geometryResult);

		return result;
	}
}
