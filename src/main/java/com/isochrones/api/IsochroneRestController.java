package com.isochrones.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isochrones.business.IsochronesBusiness;
import com.isochrones.model.Isochrones;

@RestController
public class IsochroneRestController {

	@Autowired
	private IsochronesBusiness business;

	//from = "2.23940;48.83504"
	// durations 5400, 4800, 4200
	@GetMapping(value ="/api/isochrones", params = { "from", "durations", "beginDate", "delay", "nb"})
	public ResponseEntity<Isochrones> getIsochrones(@RequestParam(required = true) String from,
			@RequestParam(required = true) List<Integer> durations, @RequestParam(required = true) String beginDate,
			@RequestParam(required = true) Integer delay, @RequestParam(required = true) Integer nb)
			throws Exception {
		long responseTime = System.currentTimeMillis();

		Isochrones isochrones = business.getIsochrones(from, durations, beginDate, delay, nb);
		
		responseTime = System.currentTimeMillis()-responseTime;
		System.out.println("total "+responseTime+"ms");
		
		return ResponseEntity.status(HttpStatus.OK).header("Access-Control-Allow-Origin", "http://localhost:4200").body(isochrones);
	}
	
}
