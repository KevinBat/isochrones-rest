package com.isochrones.model;

import java.util.List;

public class Isochrones {

	List<Geojson> geojsons;

	public Isochrones(List<Geojson> geojsons) {
		this.geojsons = geojsons;
	}

	public List<Geojson> getGeojsons() {
		return geojsons;
	}

	public void setGeojsons(List<Geojson> geojsons) {
		this.geojsons = geojsons;
	}
	
}
