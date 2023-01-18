(function(){var P$=Clazz.newPackage("com.actelion.research.chem"),I$=[];
/*c*/var C$=Clazz.newClass(P$, "DepictorTransformation");

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['D',['mOffsetX','mOffsetY','mScaling']]]

Clazz.newMeth(C$, 'c$',  function () {
;C$.$init$.apply(this);
this.clear$();
}, 1);

Clazz.newMeth(C$, 'c$$com_actelion_research_chem_DepictorTransformation',  function (t) {
;C$.$init$.apply(this);
this.mScaling=t.mScaling;
this.mOffsetX=t.mOffsetX;
this.mOffsetY=t.mOffsetY;
}, 1);

Clazz.newMeth(C$, 'c$$D$D$D',  function (scaling, offsetX, offsetY) {
;C$.$init$.apply(this);
this.mScaling=scaling;
this.mOffsetX=offsetX;
this.mOffsetY=offsetY;
}, 1);

Clazz.newMeth(C$, 'c$$com_actelion_research_gui_generic_GenericRectangle$com_actelion_research_gui_generic_GenericRectangle$D$I',  function (bounds, view, averageBondLength, mode) {
;C$.$init$.apply(this);
this.clear$();
if (view != null ) {
if ((mode & 196608) == 0) {
if (!view.contains$com_actelion_research_gui_generic_GenericRectangle(bounds)) {
if ((bounds.width > view.width ) || (bounds.height > view.height ) ) {
var hScaling=view.width / bounds.width;
var vScaling=view.height / bounds.height;
this.mScaling=Math.min(hScaling, vScaling);
}if (bounds.x * this.mScaling < view.x ) this.mOffsetX=view.x - bounds.x * this.mScaling;
 else if ((bounds.x + bounds.width) * this.mScaling > view.x + view.width ) this.mOffsetX=view.x + view.width - (bounds.x + bounds.width) * this.mScaling;
if (bounds.y * this.mScaling < view.y ) this.mOffsetY=view.y - bounds.y * this.mScaling;
 else if ((bounds.y + bounds.height) * this.mScaling > view.y + view.height ) this.mOffsetY=view.y + view.height - (bounds.y + bounds.height) * this.mScaling;
}} else {
var hScaling=view.width / bounds.width;
var vScaling=view.height / bounds.height;
var maxAVBL=mode & 65535;
if (maxAVBL == 0 ) maxAVBL=24;
 else if ((mode & 131072) != 0) maxAVBL/=256;
var bScaling=maxAVBL / averageBondLength;
this.mScaling=Math.min(bScaling, Math.min(hScaling, vScaling));
this.mOffsetX=view.x + view.width / 2.0 - this.mScaling * (bounds.x + bounds.width / 2.0);
this.mOffsetY=view.y + view.height / 2.0 - this.mScaling * (bounds.y + bounds.height / 2.0);
}} else if ((mode & 65536) != 0) {
var maxAVBL=((mode & 65535) != 0) ? mode & 65535 : 24;
this.mScaling=maxAVBL / averageBondLength;
}}, 1);

Clazz.newMeth(C$, 'clear$',  function () {
this.mOffsetX=0.0;
this.mOffsetY=0.0;
this.mScaling=1.0;
});

Clazz.newMeth(C$, 'transformX$D',  function (x) {
return x * this.mScaling + this.mOffsetX;
});

Clazz.newMeth(C$, 'transformY$D',  function (y) {
return y * this.mScaling + this.mOffsetY;
});

Clazz.newMeth(C$, 'getScaling$',  function () {
return this.mScaling;
});

Clazz.newMeth(C$, 'getOffsetX$',  function () {
return this.mOffsetX;
});

Clazz.newMeth(C$, 'getOffsetY$',  function () {
return this.mOffsetY;
});

Clazz.newMeth(C$, 'move$D$D',  function (dx, dy) {
this.mOffsetX+=dx;
this.mOffsetY+=dy;
});

Clazz.newMeth(C$, 'setScaling$D',  function (scale) {
this.mScaling=scale;
});

Clazz.newMeth(C$, 'isVoidTransformation$',  function () {
return (this.mScaling == 1.0  && this.mOffsetX == 0.0   && this.mOffsetY == 0.0  );
});

Clazz.newMeth(C$, 'applyTo$com_actelion_research_chem_DepictorTransformation',  function (t) {
t.mScaling*=this.mScaling;
t.mOffsetX=t.mOffsetX * this.mScaling + this.mOffsetX;
t.mOffsetY=t.mOffsetY * this.mScaling + this.mOffsetY;
});

Clazz.newMeth(C$, 'applyTo$com_actelion_research_gui_generic_GenericPoint',  function (p) {
p.x=p.x * this.mScaling + this.mOffsetX;
p.y=p.y * this.mScaling + this.mOffsetY;
});

Clazz.newMeth(C$, 'applyTo$com_actelion_research_gui_generic_GenericRectangle',  function (r) {
r.x=r.x * this.mScaling + this.mOffsetX;
r.y=r.y * this.mScaling + this.mOffsetY;
r.width*=this.mScaling;
r.height*=this.mScaling;
});

Clazz.newMeth(C$, 'applyTo$com_actelion_research_chem_Molecule',  function (m) {
m.scaleCoords$D(this.mScaling);
m.translateCoords$D$D(this.mOffsetX, this.mOffsetY);
});

Clazz.newMeth(C$, 'applyTo$com_actelion_research_chem_AbstractDrawingObject',  function (o) {
o.scale$D(this.mScaling);
o.move$D$D(this.mOffsetX, this.mOffsetY);
});

Clazz.newMeth(C$, 'getInverseTransformation$',  function () {
var t=Clazz.new_(C$);
t.mScaling=1.0 / this.mScaling;
t.mOffsetX=-this.mOffsetX / this.mScaling;
t.mOffsetY=-this.mOffsetY / this.mScaling;
return t;
});

Clazz.newMeth(C$, 'toString',  function () {
return "DepictorTransformation Offset: " + new Double(this.mOffsetX).toString() + "," + new Double(this.mOffsetY).toString() + " Scaling: " + new Double(this.mScaling).toString() ;
});
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-18 09:54:14 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
