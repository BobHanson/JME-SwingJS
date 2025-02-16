(function(){var P$=Clazz.newPackage("com.actelion.research.gui.generic"),I$=[];
/*c*/var C$=Clazz.newClass(P$, "GenericPoint");

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['D',['x','y']]]

Clazz.newMeth(C$, 'c$',  function () {
;C$.$init$.apply(this);
}, 1);

Clazz.newMeth(C$, 'c$$D$D',  function (x, y) {
;C$.$init$.apply(this);
this.x=x;
this.y=y;
}, 1);

Clazz.newMeth(C$, 'getX$',  function () {
return this.x;
});

Clazz.newMeth(C$, 'getY$',  function () {
return this.y;
});

Clazz.newMeth(C$, 'distance$com_actelion_research_gui_generic_GenericPoint',  function (p) {
var dx=this.x - p.x;
var dy=this.y - p.y;
return Math.sqrt(dx * dx + dy * dy);
});

Clazz.newMeth(C$, 'toString',  function () {
return "x:" + new Double(this.x).toString() + " y:" + new Double(this.y).toString() ;
});
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-25 13:08:03 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
