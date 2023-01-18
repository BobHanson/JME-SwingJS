(function(){var P$=Clazz.newPackage("com.actelion.research.gui.generic"),I$=[];
/*c*/var C$=Clazz.newClass(P$, "GenericRectangle", null, null, 'com.actelion.research.gui.generic.GenericShape');

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['D',['x','y','width','height']]]

Clazz.newMeth(C$, 'c$',  function () {
;C$.$init$.apply(this);
}, 1);

Clazz.newMeth(C$, 'c$$D$D$D$D',  function (x, y, w, h) {
;C$.$init$.apply(this);
this.x=x;
this.y=y;
this.width=w;
this.height=h;
}, 1);

Clazz.newMeth(C$, 'contains$D$D',  function (x, y) {
return x >= this.x  && x <= this.x + this.width   && y >= this.y   && y <= this.y + this.height  ;
});

Clazz.newMeth(C$, 'contains$com_actelion_research_gui_generic_GenericRectangle',  function (r) {
return this.contains$D$D(r.x, r.y) && this.contains$D$D(r.x + r.width, r.y + r.height) ;
});

Clazz.newMeth(C$, 'set$D$D$D$D',  function (x, y, w, h) {
this.x=x;
this.y=y;
this.width=w;
this.height=h;
});

Clazz.newMeth(C$, 'union$com_actelion_research_gui_generic_GenericRectangle',  function (r) {
var x=Math.min(this.x, r.x);
var y=Math.min(this.y, r.y);
var w=Math.max(this.x + this.width, r.x + r.width) - x;
var h=Math.max(this.y + this.height, r.y + r.height) - y;
return Clazz.new_(C$.c$$D$D$D$D,[x, y, w, h]);
});

Clazz.newMeth(C$, 'intersects$com_actelion_research_gui_generic_GenericRectangle',  function (r) {
return (this.x < r.x + r.width ) && (this.y < r.y + r.height ) && (r.x < this.x + this.width ) && (r.y < this.y + this.height )  ;
});

Clazz.newMeth(C$, 'intersection$com_actelion_research_gui_generic_GenericRectangle',  function (r) {
if (!this.intersects$com_actelion_research_gui_generic_GenericRectangle(r)) return null;
var x=Math.max(this.x, r.x);
var y=Math.max(this.y, r.y);
var w=Math.min(this.x + this.width, r.x + r.width) - x;
var h=Math.min(this.y + this.height, r.y + r.height) - y;
return Clazz.new_(C$.c$$D$D$D$D,[x, y, w, h]);
});

Clazz.newMeth(C$, 'getX$',  function () {
return this.x;
});

Clazz.newMeth(C$, 'getY$',  function () {
return this.y;
});

Clazz.newMeth(C$, 'getWidth$',  function () {
return this.width;
});

Clazz.newMeth(C$, 'getHeight$',  function () {
return this.height;
});

Clazz.newMeth(C$, 'getCenterX$',  function () {
return this.x + this.width / 2.0;
});

Clazz.newMeth(C$, 'getCenterY$',  function () {
return this.y + this.height / 2.0;
});

Clazz.newMeth(C$, 'toString',  function () {
return "x:" + new Double(this.x).toString() + " y:" + new Double(this.y).toString() + " w:" + new Double(this.width).toString() + " h:" + new Double(this.height).toString() ;
});
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-18 09:54:33 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
