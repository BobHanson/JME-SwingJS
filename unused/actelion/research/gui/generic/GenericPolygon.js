(function(){var P$=Clazz.newPackage("com.actelion.research.gui.generic"),I$=[[0,'java.util.Arrays']],I$0=I$[0],$I$=function(i,n){return((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "GenericPolygon", null, null, 'com.actelion.research.gui.generic.GenericShape');

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['I',['mIndex'],'O',['mX','double[]','+mY']]]

Clazz.newMeth(C$, 'c$$I',  function (size) {
;C$.$init$.apply(this);
this.mX=Clazz.array(Double.TYPE, [size]);
this.mY=Clazz.array(Double.TYPE, [size]);
this.mIndex=0;
}, 1);

Clazz.newMeth(C$, 'c$',  function () {
;C$.$init$.apply(this);
this.mX=Clazz.array(Double.TYPE, [64]);
this.mY=Clazz.array(Double.TYPE, [64]);
this.mIndex=0;
}, 1);

Clazz.newMeth(C$, 'addPoint$D$D',  function (x, y) {
if (this.mIndex == this.mX.length) {
this.mX=$I$(1).copyOf$DA$I(this.mX, 2 * this.mIndex);
this.mY=$I$(1).copyOf$DA$I(this.mY, 2 * this.mIndex);
}this.mX[this.mIndex]=x;
this.mY[this.mIndex]=y;
++this.mIndex;
});

Clazz.newMeth(C$, 'removeLastPoint$',  function () {
if (this.mIndex > 0) --this.mIndex;
});

Clazz.newMeth(C$, 'clear$',  function () {
this.mIndex=0;
});

Clazz.newMeth(C$, 'contains$D$D',  function (x, y) {
var result=false;
var j=this.mIndex - 1;
for (var i=0; i < this.mIndex; i++) {
if (((this.mY[i] > y ) != (this.mY[j] > y ) ) && (x < (this.mX[j] - this.mX[i]) * (y - this.mY[i]) / (this.mY[j] - this.mY[i]) + this.mX[i] ) ) result=!result;
j=i;
}
return result;
});

Clazz.newMeth(C$, 'getSize$',  function () {
return this.mIndex;
});

Clazz.newMeth(C$, 'getX$I',  function (i) {
return this.mX[i];
});

Clazz.newMeth(C$, 'getY$I',  function (i) {
return this.mY[i];
});

Clazz.newMeth(C$, 'getX$',  function () {
return this.mX;
});

Clazz.newMeth(C$, 'getY$',  function () {
return this.mY;
});
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-18 09:54:33 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
