(function(){var P$=Clazz.newPackage("com.actelion.research.chem"),p$1={},I$=[[0,'java.util.ArrayList','StringBuilder','java.awt.Font','java.awt.image.BufferedImage']],I$0=I$[0],$I$=function(i,n){return((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "SVGDepictor", null, 'com.actelion.research.chem.AbstractDepictor');

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
this.lineWidth=1;
this.textSize=10;
this.width=400;
this.height=400;
this.legacyMode=true;
this.currentColor="black";
this.bonds=Clazz.new_($I$(1,1));
this.atoms=Clazz.new_($I$(1,1));
this.buffer=Clazz.new_($I$(2,1));
this.currentFont=Clazz.new_($I$(3,1).c$$S$I$I,["Helvetica", 0, 12]);
},1);

C$.$fields$=[['Z',['legacyMode'],'D',['lineWidth'],'I',['textSize','width','height'],'S',['currentColor','id'],'O',['bonds','java.util.List','+atoms','buffer','StringBuilder','currentFont','java.awt.Font','graphics','java.awt.Graphics2D']]
,['I',['instanceCnt']]]

Clazz.newMeth(C$, 'c$$com_actelion_research_chem_StereoMolecule$S',  function (mol, id) {
C$.c$$com_actelion_research_chem_StereoMolecule$I$S.apply(this, [mol, 0, id]);
}, 1);

Clazz.newMeth(C$, 'c$$com_actelion_research_chem_StereoMolecule$I$S',  function (mol, displayMode, id) {
;C$.superclazz.c$$com_actelion_research_chem_StereoMolecule$I.apply(this,[mol, displayMode]);C$.$init$.apply(this);
this.id=id;
++C$.instanceCnt;
}, 1);

Clazz.newMeth(C$, 'setLegacyMode$Z',  function (b) {
this.legacyMode=b;
});

Clazz.newMeth(C$, 'makeColor$I$I$I',  function (r, g, b) {
return "rgb(" + r + "," + g + "," + b + ")" ;
}, 1);

Clazz.newMeth(C$, 'getId$',  function () {
return this.id != null  ? this.id : ("mol" + C$.instanceCnt);
});

Clazz.newMeth(C$, 'write$S',  function (s) {
this.buffer.append$S("\t");
this.buffer.append$S(s);
this.buffer.append$S("\n");
}, p$1);

Clazz.newMeth(C$, 'drawBlackLine$com_actelion_research_chem_AbstractDepictor_DepictorLine',  function (theLine) {
var x1=(theLine.x1|0);
var x2=(theLine.x2|0);
var y1=(theLine.y1|0);
var y2=(theLine.y2|0);
var s="<line x1=\"" + x1 + "\" " + "y1=\"" + y1 + "\" " + "x2=\"" + x2 + "\" " + "y2=\"" + y2 + "\" " + "style=\"stroke:" + this.currentColor + "; stroke-width:" + new Double(this.lineWidth).toString() + "\"/>" ;
p$1.write$S.apply(this, [s]);
});

Clazz.newMeth(C$, 'drawDottedLine$com_actelion_research_chem_AbstractDepictor_DepictorLine',  function (theLine) {
var x1=(theLine.x1|0);
var x2=(theLine.x2|0);
var y1=(theLine.y1|0);
var y2=(theLine.y2|0);
var d=((3 * this.lineWidth)|0);
var s="<line stroke-dasharray=\"" + d + "," + d + "\" " + "x1=\"" + x1 + "\" " + "y1=\"" + y1 + "\" " + "x2=\"" + x2 + "\" " + "y2=\"" + y2 + "\" " + "style=\"stroke:" + this.currentColor + "; stroke-width:" + new Double(this.lineWidth).toString() + "\"/>" ;
p$1.write$S.apply(this, [s]);
});

Clazz.newMeth(C$, 'drawPolygon$com_actelion_research_gui_generic_GenericPolygon',  function (p) {
var s=Clazz.new_(["<polygon points=\""],$I$(2,1).c$$S);
for (var i=0; i < p.getSize$(); i++) {
s.append$J(Math.round$D(p.getX$I(i)));
s.append$S(",");
s.append$J(Math.round$D(p.getY$I(i)));
s.append$S(" ");
}
s.append$S("\" style=\"fill:" + this.currentColor + "; stroke:" + this.currentColor + "; stroke-width:" + new Double(this.lineWidth).toString() + "\"/>" );
p$1.write$S.apply(this, [s.toString()]);
});

Clazz.newMeth(C$, 'drawString$S$D$D',  function (theString, x, y) {
var strWidth=this.getStringWidth$S(theString);
var s="<text x=\"" + ((x - strWidth / 2.0)|0) + "\" " + "y=\"" + ((y + (this.textSize/3|0))|0) + "\" " + "stroke=\"none\" " + "font-size=\"" + this.currentFont.getSize$() + "\" " + "fill=\"" + this.currentColor + "\">" + theString + "</text>" ;
p$1.write$S.apply(this, [s]);
});

Clazz.newMeth(C$, 'fillCircle$D$D$D',  function (x, y, d) {
var s="<circle cx=\"" + ((x + d / 2)|0) + "\" " + "cy=\"" + ((y + d / 2)|0) + "\" " + "r=\"" + ((d / 2)|0) + "\" " + "fill=\"" + this.currentColor + "\" />" ;
p$1.write$S.apply(this, [s]);
});

Clazz.newMeth(C$, 'getLineWidth$',  function () {
return this.lineWidth;
});

Clazz.newMeth(C$, 'getStringWidth$S',  function (theString) {
var ret=this.currentFont.getStringBounds$S$java_awt_font_FontRenderContext(theString, this.graphics.getFontRenderContext$()).getWidth$();
return ret;
});

Clazz.newMeth(C$, 'getTextSize$',  function () {
return this.textSize;
});

Clazz.newMeth(C$, 'setTextSize$I',  function (theSize) {
if (this.textSize != theSize) {
this.textSize=theSize;
this.currentFont=Clazz.new_($I$(3,1).c$$S$I$I,["Helvetica", 0, theSize]);
}});

Clazz.newMeth(C$, 'setLineWidth$D',  function (width) {
this.lineWidth=Long.$dval(Long.$div(Math.round$D(100 * Math.max(width, 1.0)),100));
});

Clazz.newMeth(C$, 'setRGB$I',  function (rgb) {
this.currentColor=C$.makeColor$I$I$I((rgb & 16711680) >> 16, (rgb & 65280) >> 8, rgb & 255);
});

Clazz.newMeth(C$, 'onDrawBond$I$D$D$D$D',  function (bond, x1, y1, x2, y2) {
var s="<line id=\"" + this.getId$() + ":Bond:" + bond + "\" " + "class=\"event\" " + "x1=\"" + ((x1)|0) + "\" " + "y1=\"" + ((y1)|0) + "\" " + "x2=\"" + ((x2)|0) + "\" " + "y2=\"" + ((y2)|0) + "\" " + "stroke-width=\"" + 8 + "\" " + "stroke-opacity=\"0\"" + "/>" ;
this.bonds.add$O(s);
});

Clazz.newMeth(C$, 'onDrawAtom$I$S$D$D',  function (atom, symbol, x, y) {
var r=8;
var s="<circle id=\"" + this.getId$() + ":Atom:" + atom + "\" " + "class=\"event\" " + "cx=\"" + ((x)|0) + "\" " + "cy=\"" + ((y)|0) + "\" " + "r=\"" + r + "\" " + "fill-opacity=\"0\"/>" ;
this.atoms.add$O(s);
});

Clazz.newMeth(C$, 'toString',  function () {
var header="<svg id=\"" + this.getId$() + "\" " + "xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" " + "width=\"" + this.width + "px\" " + "height=\"" + this.height + "px\" " + "viewBox=\"0 0 " + this.width + " " + this.height + "\">\n" ;
var footer="</svg>";
var style=this.legacyMode ? "<style> #" + this.getId$() + " {pointer-events:none; } " + " #" + this.getId$() + " .event " + " { pointer-events:all;} " + " </style>\n"  : "<g style=\"font-size:" + this.getTextSize$() + "px; fill-opacity:1; stroke-opacity:1; fill:black; stroke:black;" + " font-weight:normal; text-rendering:optimizeLegibility; font-family:sans-serif;" + " stroke-linejoin:round; stroke-linecap:round; stroke-dashoffset:0;\">" ;
header+="\t";
header+=style;
if (this.legacyMode) {
for (var b, $b = this.bonds.iterator$(); $b.hasNext$()&&((b=($b.next$())),1);) p$1.write$S.apply(this, [b]);

for (var a, $a = this.atoms.iterator$(); $a.hasNext$()&&((a=($a.next$())),1);) p$1.write$S.apply(this, [a]);

}if (!this.legacyMode) p$1.write$S.apply(this, ["</g>"]);
return header + this.buffer.toString() + footer ;
});

Clazz.newMeth(C$, 'simpleValidateView$com_actelion_research_gui_generic_GenericRectangle$I',  function (viewRect, mode) {
this.width=(viewRect.getWidth$()|0);
this.height=(viewRect.getHeight$()|0);
var img=Clazz.new_($I$(4,1).c$$I$I$I,[this.width, this.height, 2]);
this.graphics=img.createGraphics$();
return C$.superclazz.prototype.simpleValidateView$com_actelion_research_gui_generic_GenericRectangle$I.apply(this, [viewRect, mode]);
});

C$.$static$=function(){C$.$static$=0;
C$.instanceCnt=0;
};

Clazz.newMeth(C$);
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-18 09:54:15 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
