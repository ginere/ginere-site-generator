/**
 * http://blog.barthe.ph/2013/05/21/js-oo-programming/
 */

// Namespace
var gs = gs || {};
//avem.Init = avem.init || {};

// Use strict header
(function() {
	"use strict";
	// use strict

	gs.core={
		servicesURL:"./",
		ROOT:"./",
		white:"./",
		json:"./",
		externUrl:"./",
		init: function (servicesURL,ROOT,WHITE,JSON,EXTERN_URL) {
 			this.servicesURL=servicesURL;
			this.ROOT=ROOT;
			this.white=WHITE;
			this.json=JSON;
			this.externUrl=EXTERN_URL;
		},
		getServicesUrl: function (url) {
			return this.servicesURL+url;
		},
		getUrl: function (url) {
			return this.ROOT+url;
		},
		getWhiteUrl: function (url) {
			return this.white+url;
		},
		getJsonUrl: function (url) {
			return this.json+url;
		},
		getExternUrl: function (url) {
			return this.externUrl+url;
		},
		redirect: function (url) {
//			window.location.replace(url);
//			location.href=  url;
//            document.location.href = url;
			window.location.assign(url);
		},
		preloadImage:function(url) {
            log.debug("Preloading image:"+url+"....");

		    var image=(new Image()).src = url;

            //$('<img />').attr('src',url).appendTo('body').css('display','none');

            log.debug("Preloading image:"+url+". DONE.");
            
            return image;
        },
		getParameterByName:function(name) {
			name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
			var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
			results = regex.exec(location.search);
			return results === null ? null : decodeURIComponent(results[1].replace(/\+/g, " "));
		},

		getParameterFromScriptUrl:function(name) {
            var scriptParameter=gs.core.getParametersFromScriptUrl();
            if (scriptParameter!=null){
                return scriptParameter[name];
            } else {
                return null;
            }
        },

		getParametersFromScriptUrl:function() {
            var scripts = document.getElementsByTagName('script');
            var myScript = scripts[ scripts.length - 1 ];
            
            var queryString = myScript.src.replace(/^[^\?]+\??/,'');
            
            var params = parseQuery( queryString );
            
            function parseQuery ( query ) {
                var Params = new Object ();
                if ( ! query ) return Params; // return empty object
                var Pairs = query.split(/[;&]/);
                for ( var i = 0; i < Pairs.length; i++ ) {
                    var KeyVal = Pairs[i].split('=');
                    if ( ! KeyVal || KeyVal.length != 2 ) continue;
                    var key = unescape( KeyVal[0] );
                    var val = unescape( KeyVal[1] );
                    val = val.replace(/\+/g, ' ');
                    Params[key] = val;
                }
                return Params;
            }
            return params;
        },
        
 		parseInt:function(_val,defaultvalue){
			if (_val == null ) {
				return defaultvalue;
			}
			var _ret=parseInt(_val);

			if (isNaN(_ret)){
				return defaultvalue;
			} else {
				return _ret;
			}			
		},
		switchClass:function(elemen,onClass,offClass1,offClass2){
			var el=$(elemen);
			el.addClass(onClass);

            if (offClass1 !== undefined ) {
			    el.removeClass(offClass1);
            }

            if (offClass2 !== undefined ) {
			    el.removeClass(offClass2);
            }

		}

	};

	// Use strict footer
})();