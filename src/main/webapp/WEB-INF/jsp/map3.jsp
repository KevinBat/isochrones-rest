<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link type="text/css" rel="stylesheet" href="css/bootstrap.min.css" />
<link type="text/css" rel="stylesheet" href="css/style.css" />
<link rel="stylesheet"
	href="https://unpkg.com/leaflet@1.3.1/dist/leaflet.css"
	integrity="sha512-Rksm5RenBEKSKFjgI3a41vrjkw4EVPlJ3+OiI65vTjIdo9brlAacEuKOiQ5OFh7cOI1bkDwLqdLw3Zg0cRJAAQ=="
	crossorigin="" />
<style>
html {
	width: 100%;
	height: 100%;
}

/* Decalage pour la navbar */
body {
	width: 100%;
	margin-top: 50px;
	height: calc(100% - 50px);
	/* Pour eviter la fusion des marges */
	padding: 1px 0;
}

#map {
	width: 100%;
	height: 100%;
}

</style>


<title>Les Bus De Mon Quartier</title>
</head>
<body onload='initMap();'>
  <div id="map"></div>
</body>

<script src="js/lib/jquery-3.2.1.min.js" type="application/javascript"></script>
<script src="js/lib/bootstrap.min.js" type="application/javascript"></script>
<script src="https://unpkg.com/leaflet@1.3.1/dist/leaflet.js"
	integrity="sha512-/Nsx9X4HebavoBvEBuyp3I7od5tA0UzAxs+j83KgC8PU0kgB4XiK4Lfe4y4cgBtaRJQEIFCW+oC506aPT2L1zw=="
	crossorigin=""></script>
<script src='https://npmcdn.com/@turf/turf/turf.min.js'></script>

<script type="text/javascript">


	function initMap() {

		map = L.map('map');

		// Utilisation d'un title(cartographie) mapbox avec ma cle session 
		// Zoom de niveau 19 maximum autorisÃ©
		//WpzgjRXIDbZlxVLLhuxgcDPcHaPpEK9Mu19hiRq9dlxE0TwBKjx2okFvDM9HdPaB
		L.tileLayer(
						'https://tile.jawg.io/jawg-streets/{z}/{x}/{y}.png?access-token=WpzgjRXIDbZlxVLLhuxgcDPcHaPpEK9Mu19hiRq9dlxE0TwBKjx2okFvDM9HdPaB',
						{
							maxZoom : 19,
							minZoom : 5
						}).addTo(map);

		//Avertit l'utilisateur si la geolocalisation est desactivÃ©e
		map.setView([ 48.66670468318454, 2.3675537109375 ], 13);

		var beginDate = (new Date()).getTime();
		//5400, 4800, 4200
		putIsochrones();
		
		var endDate = (new Date()).getTime();
		var responseTime = endDate - beginDate
		console.log('response-time : ' + responseTime);

	}

	function putIsochrones() {
		$.ajax({
            type: 'GET',
            url: 'http://localhost:8080/isochrones/api/isochrones?from=2.23940;48.83504&durations=5400&durations=4800&durations=4200&beginDate=2018-10-29T09:00&delay=5&nb=5',
            map: map,
            async: false,
            dataType: 'json',
            // Callback success
            success: function(data){
            	 //var json = JSON.parse('${json}');
                 var colorArrays = [ 'rgb(0,255,0)', 'rgb(255,255,0)', 'rgb(255,0,0)' ];

                 for (var i = 0; i < data.geojsons.length; i++) {
                     L.geoJSON(data.geojsons[i], {
                         weight : 1,
                         color : colorArrays[i],
                         opacity : 0.20
                     }).addTo(map);
                 }
            },
            error: function(xhr, textStatus, errorThrown) {
                console.log('Error: ' + textStatus + ' ' + errorThrown);
            }
          });
       
	}
</script>
</html>