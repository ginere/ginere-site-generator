/**
 * http://blog.barthe.ph/2013/05/21/js-oo-programming/
 */
var jj=$("pepe").html();

alert(jj);


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
})();gs.core.init('SERVICES','ROOT','WHITE','JSON','EXTERN');/**
 * http://codepb.github.io/jquery-template/
 */

var gs = gs || {};

// Use strict header
(function() {
    "use strict";

    gs.Agent=function () {
		var userAgent=navigator.userAgent.toLowerCase();
		gs.Agent.userAgent=userAgent;

		if (/iphone/.test(userAgent)){
			gs.Agent.agentname="iphone";
			gs.Agent.agentid=gs.Agent.IPHONE;
		} else if (/ipad/.test(userAgent)){
			gs.Agent.agentname="ipad";
			gs.Agent.agentid=gs.Agent.IPAD;
		} else if (/android/.test(userAgent)){
			gs.Agent.agentname="android";
			gs.Agent.agentid=gs.Agent.ANDROID;			
		} else if (/iemobile/.test(userAgent)){
			gs.Agent.agentname="iemobile";
			gs.Agent.agentid=gs.Agent.IEMOBILE;			
		} else if (/msie/.test(userAgent)){
			gs.Agent.agentname="iexplorer";
			gs.Agent.agentid=gs.Agent.IEXPLORER;			
		} else if (/chrome/.test(userAgent)){
			gs.Agent.agentname="chrome";
			gs.Agent.agentid=gs.Agent.CHROME;
		} else if (/firefox/.test(userAgent)){
			gs.Agent.agentname="firefox";
			gs.Agent.agentid=gs.Agent.FIREFOX;
		} else if (/safari/.test(userAgent)){
			gs.Agent.agentname="safari";
			gs.Agent.agentid=gs.Agent.SAFARI;
		} else if (/tv/.test(userAgent)){
			gs.Agent.agentname="tv";
			gs.Agent.agentid=gs.Agent.TV;
		} else {
			gs.Agent.agentname="unkwon";
			gs.Agent.agentid=gs.Agent.UNKWON;
		}

		if ( gs.Agent.agentid== gs.Agent.CHROME || 
             gs.Agent.agentid== gs.Agent.SAFARI || 
             gs.Agent.agentid== gs.Agent.IEXPLORER || 
             gs.Agent.agentid== gs.Agent.FIREFOX){
			gs.Agent.autoPlay=true;
		} else {
			gs.Agent.autoPlay=false;
        }

		if ( gs.Agent.agentid== gs.Agent.TV){
            gs.Agent.isTv=true;
        }

		//		gs.Agent.autoPlay=false;

		/*
		var param=gs.core.getParameterByName("agent");

		if (param!=null){
			gs.Agent.autoPlay=param;
		}
		*/
        log.debug("Agent:"+gs.Agent.agentname);
	};

    var i=0;
	gs.Agent.UNKWON=i++;
	gs.Agent.CHROME=i++;
	gs.Agent.FIREFOX=i++;
	gs.Agent.SAFARI=i++;

	gs.Agent.IPHONE=i++;
	gs.Agent.IPAD=i++;
	gs.Agent.ANDROID=i++;
	gs.Agent.IEMOBILE=i++;
	gs.Agent.IEXPLORER=i++;
	gs.Agent.TV=i++;

	// The long string
	gs.Agent.userAgent="";
	// The short agent name
	gs.Agent.agentname="";
	// The video can be autoplayed
	gs.Agent.autoPlay=false;
	gs.Agent.isTv=false;
	
})();

new gs.Agent();
