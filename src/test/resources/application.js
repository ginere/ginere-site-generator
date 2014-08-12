
/**
 * Application
 */
var avem = avem || {};

// Use strict header
(function() {
	"use strict";
	
	avem.app ={
		google:null,
		init: function (isLocal) {
			// Loading Java Script Files
			var local=isLocal;
			var jqueySrc;
			var bootstrap;
			
			// verificar que son las mismas
			if (local) {
				jqueySrc="local_js/jquery-2.1.0.min.js";
				//		jqueySrc="local_js/jquery-1.10.2.min.js";

				bootstrap="bootstrap-3.1.1-dist/js/bootstrap.min.js";
				
			} else {
				jqueySrc="//ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js";
				bootstrap="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js";
			}

			avem.Init.includeArray(["js/log.js",
									"js/ListenerList.js",
									"js/heyoffline.js",
									"js/holder.js",
									jqueySrc,
									bootstrap
									//, "js/google.js"
								   ],
								   avem.app.onLoaded,
								   avem.app.onError,
								   true
								  );
			
		},
		onLoaded: function () {
			Log.info("Javascript loaded");
			
			$('#elementModal').modal('show');			

			avem.app.google=new Google("map_canvas",avem.app.googleInited);


//			avem.app.googleInited();
			
		},
		onError: function () {
			Log.info("Javascript error");
			
			$('#loadingErrorModal').modal('show');			
		},
		googleInited: function () {
			Log.info("GOOGLE initialized");

			avem.app.google.addControl("glyphicon glyphicon-screenshot",avem.app.google.centerView.bind(avem.app.google));
			avem.app.google.addControl("glyphicon glyphicon-zoom-in",avem.app.google.zoomIn.bind(avem.app.google));
			avem.app.google.addControl("glyphicon glyphicon-zoom-out",avem.app.google.zoomOut.bind(avem.app.google));
			avem.app.google.addControl("glyphicon glyphicon-phone",avem.app.google.tracking.bind(avem.app.google));
			avem.app.google.addControl("glyphicon glyphicon-cloud-download",avem.app.readMapElements);

			jQuery("#centerView").click(function(){
				google.centerView();
			});

//				$('#offLineModal').modal('show');
//				$('#onLineModal').modal('show');
//			$("#MyAlert").alert('close');
			$(window).on('offline', function(){
				$('#onLineModal').modal('hide');
				$('#offLineModal').modal('show');
			});
			
			$(window).on('online', function(){
				$('#offLineModal').modal('hide');
				$('#onLineModal').modal('show');
			});

			avem.app.google.addListener("mousemove", function(e){
				if (e.latLng != undefined){
					jQuery("#latLong").text('Lat,Lng: [ '+
											Number(e.latLng.lat()).toFixed(7)+' , '+
											Number(e.latLng.lng()).toFixed(7)+
											' ] - '+avem.app.google.getZoom()
										   );
//					Log.debug("latlong:"+e.latLng.toString());
				}
			});
		},
		readMapElements: function () {
			/*
			debugger;
			$.getJSON( "data/me.json", function( data ) {
				Log.error(data);
			});
			*/

			$.ajax({url: "data/me.json",
					dataType: "json",
					async: true,
					success: function (result) {
//						Log.error(result);
						
						for (var i=0; i<result.length;i++){
							var data=result[i];
							var position=new google.maps.LatLng(data.latLng.lat,
																data.latLng.lng);

							// https://developers.google.com/maps/documentation/javascript/3.exp/reference?hl=es#MarkerShape
							var marker = new google.maps.Marker({
								position: position,
								map: avem.app.google.getMap(),
								clickable: true,
								animation: google.maps.Animation.DROP,
//								http://blog.mridey.com/2010/03/using-markerimage-in-maps-javascript.html
								title: data.name
							});

							Log.debug("Locaded marker:"+marker);
						}
					},
					error: function (request,error) {
						alert('Network error has occurred please try again!');
					}
				   });         
		}
	};
	// Use strict footer
})();

avem.app.init(true);
