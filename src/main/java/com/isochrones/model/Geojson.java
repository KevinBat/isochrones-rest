
package com.isochrones.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Geojson {

	/**
	 * MultiPolygon
	 */
    private String type;
    /**
     * Polygon<PolygonSansTrou<Point<List<Double>>>>
     * 
     * MultiPolygon = List<Polygon> - > List<List<List<List<Double>>>>
     * Polygon = List<PolygonSansTrou> -> List<List<List<Double>>>
     * PolygonSansTrou = List<Point> -> List<List<Double>>
     * Point = List<Double> {X, Y}
     * 
     */
    private List<List<List<List<Double>>>> coordinates = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<List<List<List<Double>>>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<List<List<Double>>>> coordinates) {
        this.coordinates = coordinates;
    }

	@Override
	public String toString() {
		return "Geojson [type=" + type + ", coordinates=" + coordinates.toString() + "]";
	}
}
