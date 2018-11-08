var map = "#map";

$(tableDiv).ready(function(){
	//Création de l'heure actelle -10min
	for(var i=0; i<listStopArea.length;i++){
		var stopArea = listStopArea[i];
		//Création du header du tableau
		var tab = '<div class="stop row">' +
		'<div class="head row">' +
		'<div class="col-xs-offset-0 col-xs-3">'+stopArea.name+'</div>' +
		'<div class="col-xs-3">prev</div>' +
		'<div class="col-xs-3">next</div>' +
		'<div class="col-xs-3">next</div>' +
		'</div>';
		for(var j=0; j<stopArea.listBus.length; j++){
			var bus = stopArea.listBus[j];
			if(bus.faster===1){
				tab += '<div class="horaires row faster1">';
			} else if (bus.faster===2){
				tab += '<div class="horaires row faster2">';
			} else {
				tab += '<div class="horaires row">';
			}
				
			tab += '<div class="numero col-xs-3">'+bus.number+'</div>' +
			'<div class="previous center col-xs-3">'+bus.prev+'</div>' +
			'<div class="horaire col-xs-3">'+bus.next1+'</div>' +
			'<div class="horaire col-xs-3">'+bus.next2+'</div>' +
			'</div>';
		}
		tab += '</div>';
		
		$(tableDiv).append(tab);
		
	}
})

