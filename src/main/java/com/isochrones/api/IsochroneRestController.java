package com.isochrones.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isochrones.business.IsochronesBusiness;
import com.isochrones.exception.BadParametersException;
import com.isochrones.model.Isochrones;

@RestController
public class IsochroneRestController {

	@Autowired
	private IsochronesBusiness business;

	@CrossOrigin(origins = "https://isochrones-angular.herokuapp.com")
	@GetMapping(value ="/api/isochrones")
	public ResponseEntity<Isochrones> getIsochrones(
			@RequestParam(value="from", required = true) Optional<String> from,
			@RequestParam(value="to", required = true) Optional<String> to,
			@RequestParam(value="durations", required = true) List<Integer> durations,
			@RequestParam(value="beginDate", required = true) String beginDate,
			@RequestParam(value="delay", required = true) Integer delay,
			@RequestParam(value="nb", required = true) Integer nb)
			throws Exception {
		long responseTime = System.currentTimeMillis();

		String fromOrTo;
		String fromOrToValue;
		if(from.isPresent()) {
			fromOrTo = "from";
			fromOrToValue = from.get();
		} else if(to.isPresent()) {
			fromOrTo = "to";
			fromOrToValue = to.get();
		} else {
			throw new BadParametersException("Parameters [from] and [to] are absent");
		}
		Isochrones isochrones = business.getIsochrones(fromOrTo, fromOrToValue, durations, beginDate, delay, nb);
		
		responseTime = System.currentTimeMillis()-responseTime;
		System.out.println("total "+responseTime+"ms");
		
		return ResponseEntity.status(HttpStatus.OK).body(isochrones);
	}
	
}
