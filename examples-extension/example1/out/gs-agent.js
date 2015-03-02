/**
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
