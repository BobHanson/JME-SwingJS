(function(){var P$=Clazz.newPackage("com.actelion.research.chem.coords"),I$=[];
/*c*/var C$=Clazz.newClass(P$, "InventorAngle");

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['D',['mAngle','mLength']]]

Clazz.newMeth(C$, 'getAngle$D$D$D$D',  function (x1, y1, x2, y2) {
var angle;
var xdif=x2 - x1;
var ydif=y2 - y1;
if (ydif != 0 ) {
angle=Math.atan(xdif / ydif);
if (ydif < 0 ) {
if (xdif < 0 ) angle-=3.141592653589793;
 else angle+=3.141592653589793;
}} else angle=(xdif > 0 ) ? 1.5707963267948966 : -1.5707963267948966;
return angle;
}, 1);

Clazz.newMeth(C$, 'c$$D$D',  function (angle, length) {
;C$.$init$.apply(this);
this.mAngle=angle;
this.mLength=length;
}, 1);

Clazz.newMeth(C$, 'c$$D$D$D$D',  function (x1, y1, x2, y2) {
;C$.$init$.apply(this);
this.mAngle=C$.getAngle$D$D$D$D(x1, y1, x2, y2);
var xdif=x2 - x1;
var ydif=y2 - y1;
this.mLength=Math.sqrt(xdif * xdif + ydif * ydif);
}, 1);

Clazz.newMeth(C$);
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-18 09:54:18 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
