var a=a||{},e;
e={a:null,j:function(b){var c;b?(b="local_js/jquery-2.1.0.min.js",c="bootstrap-3.1.1-dist/js/bootstrap.min.js"):(b="//ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js",c="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js");a.t.H(["js/log.js","js/ListenerList.js","js/heyoffline.js","js/holder.js",b,c],e.o,e.n,!0)},o:function(){Log.f("Javascript loaded");$("#elementModal").b("show");e.a=new Google("map_canvas",e.i)},n:function(){Log.f("Javascript error");$("#loadingErrorModal").b("show")},i:function(){Log.f("GOOGLE initialized");
e.a.c("glyphicon glyphicon-screenshot",e.a.h.e(e.a));e.a.c("glyphicon glyphicon-zoom-in",e.a.R.e(e.a));e.a.c("glyphicon glyphicon-zoom-out",e.a.S.e(e.a));e.a.c("glyphicon glyphicon-phone",e.a.P.e(e.a));e.a.c("glyphicon glyphicon-cloud-download",e.p);jQuery("#centerView").A(function(){google.h()});$(window).m("offline",function(){$("#onLineModal").b("hide");$("#offLineModal").b("show")});$(window).m("online",function(){$("#offLineModal").b("hide");$("#onLineModal").b("show")});e.a.w("mousemove",function(b){void 0!=
b.d&&jQuery("#latLong").N("Lat,Lng: [ "+Number(b.d.k()).q(7)+" , "+Number(b.d.l()).q(7)+" ] - "+e.a.G())})},p:function(){$.x({Q:"data/me.json",C:"json",z:!0,M:function(b){for(var c=0;c<b.I;c++){var d=b[c],f=new google.g.u(d.d.k,d.d.l),d=new google.g.v({L:f,J:e.a.F(),B:!0,y:google.g.r.s,O:d.K});Log.D("Locaded marker:"+d)}},E:function(){alert("Network error has occurred please try again!")}})}};e.j(!0);