(function(){var P$=Clazz.newPackage("com.actelion.research.chem"),I$=[[0,'java.text.DecimalFormat','java.text.NumberFormat','java.util.Random']],I$0=I$[0],$I$=function(i,n){return((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "Coordinates", null, null, ['java.io.Serializable', 'Comparable']);

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['D',['x','y','z']]]

Clazz.newMeth(C$, 'c$',  function () {
;C$.$init$.apply(this);
}, 1);

Clazz.newMeth(C$, 'c$$com_actelion_research_chem_Coordinates',  function (c) {
C$.c$$D$D$D.apply(this, [c.x, c.y, c.z]);
}, 1);

Clazz.newMeth(C$, 'c$$D$D$D',  function (x, y, z) {
;C$.$init$.apply(this);
this.x=x;
this.y=y;
this.z=z;
}, 1);

Clazz.newMeth(C$, 'set$com_actelion_research_chem_Coordinates',  function (c) {
this.set$D$D$D(c.x, c.y, c.z);
return this;
});

Clazz.newMeth(C$, 'set$D$D$D',  function (x, y, z) {
this.x=x;
this.y=y;
this.z=z;
});

Clazz.newMeth(C$, 'getLength$',  function () {
return this.dist$();
});

Clazz.newMeth(C$, 'dist$',  function () {
return Math.sqrt(this.distSq$());
});

Clazz.newMeth(C$, 'distSq$',  function () {
return this.x * this.x + this.y * this.y + this.z * this.z;
});

Clazz.newMeth(C$, 'distanceSquared$com_actelion_research_chem_Coordinates',  function (c) {
return (c.x - this.x) * (c.x - this.x) + (c.y - this.y) * (c.y - this.y) + (c.z - this.z) * (c.z - this.z);
});

Clazz.newMeth(C$, 'distSquareTo$com_actelion_research_chem_Coordinates',  function (c) {
return this.distanceSquared$com_actelion_research_chem_Coordinates(c);
});

Clazz.newMeth(C$, 'distance$com_actelion_research_chem_Coordinates',  function (c) {
return Math.sqrt(this.distanceSquared$com_actelion_research_chem_Coordinates(c));
});

Clazz.newMeth(C$, 'dot$com_actelion_research_chem_Coordinates',  function (c) {
return this.x * c.x + this.y * c.y + this.z * c.z;
});

Clazz.newMeth(C$, 'cross$com_actelion_research_chem_Coordinates',  function (c) {
return Clazz.new_(C$.c$$D$D$D,[this.y * c.z - this.z * c.y, -(this.x * c.z - this.z * c.x), this.x * c.y - this.y * c.x]);
});

Clazz.newMeth(C$, 'getAngle$com_actelion_research_chem_Coordinates',  function (c) {
var d1=this.distSq$();
var d2=c.distSq$();
if (d1 == 0  || d2 == 0  ) return 0;
var d=this.dot$com_actelion_research_chem_Coordinates(c) / Math.sqrt(d1 * d2);
if (d >= 1 ) return 0;
if (d <= -1 ) return 3.141592653589793;
return Math.acos(d);
});

Clazz.newMeth(C$, 'getAngleXY$com_actelion_research_chem_Coordinates',  function (c) {
var dx=c.x - this.x;
var dy=c.y - this.y;
if (dy == 0.0 ) return (dx > 0.0 ) ? 1.5707963267948966 : -1.5707963267948966;
var angle=Math.atan(dx / dy);
if (dy < 0.0 ) return (dx < 0.0 ) ? angle - 3.141592653589793 : angle + 3.141592653589793;
return angle;
});

Clazz.newMeth(C$, 'getDihedral$com_actelion_research_chem_Coordinates$com_actelion_research_chem_Coordinates$com_actelion_research_chem_Coordinates',  function (c2, c3, c4) {
return C$.getDihedral$com_actelion_research_chem_Coordinates$com_actelion_research_chem_Coordinates$com_actelion_research_chem_Coordinates$com_actelion_research_chem_Coordinates(this, c2, c3, c4);
});

Clazz.newMeth(C$, 'subC$com_actelion_research_chem_Coordinates',  function (c) {
return Clazz.new_(C$.c$$D$D$D,[this.x - c.x, this.y - c.y, this.z - c.z]);
});

Clazz.newMeth(C$, 'addC$com_actelion_research_chem_Coordinates',  function (c) {
return Clazz.new_(C$.c$$D$D$D,[this.x + c.x, this.y + c.y, this.z + c.z]);
});

Clazz.newMeth(C$, 'scaleC$D',  function (scale) {
return Clazz.new_(C$.c$$D$D$D,[this.x * scale, this.y * scale, this.z * scale]);
});

Clazz.newMeth(C$, 'sub$com_actelion_research_chem_Coordinates',  function (c) {
this.x-=c.x;
this.y-=c.y;
this.z-=c.z;
return this;
});

Clazz.newMeth(C$, 'add$com_actelion_research_chem_Coordinates',  function (c) {
this.x+=c.x;
this.y+=c.y;
this.z+=c.z;
return this;
});

Clazz.newMeth(C$, 'add$D$D$D',  function (dx, dy, dz) {
this.x+=dx;
this.y+=dy;
this.z+=dz;
});

Clazz.newMeth(C$, 'scale$D',  function (scale) {
this.x*=scale;
this.y*=scale;
this.z*=scale;
return this;
});

Clazz.newMeth(C$, 'negate$',  function () {
this.x=-this.x;
this.y=-this.y;
this.z=-this.z;
});

Clazz.newMeth(C$, 'rotate$DAA',  function (m) {
var x0=this.x;
var y0=this.y;
var z0=this.z;
this.x=x0 * m[0][0] + y0 * m[1][0] + z0 * m[2][0];
this.y=x0 * m[0][1] + y0 * m[1][1] + z0 * m[2][1];
this.z=x0 * m[0][2] + y0 * m[1][2] + z0 * m[2][2];
return this;
});

Clazz.newMeth(C$, 'rotateC$DAA',  function (m) {
return Clazz.new_(C$.c$$D$D$D,[this.x * m[0][0] + this.y * m[1][0] + this.z * m[2][0], this.x * m[0][1] + this.y * m[1][1] + this.z * m[2][1], this.x * m[0][2] + this.y * m[1][2] + this.z * m[2][2]]);
});

Clazz.newMeth(C$, 'rotate$com_actelion_research_chem_Coordinates$D',  function (normal, theta) {
if (Math.abs(normal.x * normal.x + normal.y * normal.y + normal.z * normal.z - 1) > 1.0E-6 ) throw Clazz.new_(Clazz.load('IllegalArgumentException').c$$S,["normal needs to a unit vector: " + normal]);
var x=normal.x;
var y=normal.y;
var z=normal.z;
var c=Math.cos(theta);
var s=Math.sin(theta);
var t=1 - c;
var opp=Clazz.new_(C$.c$$D$D$D,[(t * x * x  + c) * this.x + (t * x * y  + s * z) * this.y + (t * x * z  - s * y) * this.z, (t * x * y  - s * z) * this.x + (t * y * y  + c) * this.y + (t * y * z  + s * x) * this.z, (t * x * z  + s * y) * this.x + (t * z * y  - s * x) * this.y + (t * z * z  + c) * this.z]);
return opp;
});

Clazz.newMeth(C$, 'unitC$',  function () {
var d=this.dist$();
if (d == 0 ) {
System.err.println$S("Cannot call unitC() on a null vector");
return Clazz.new_(C$.c$$D$D$D,[1, 0, 0]);
}return Clazz.new_(C$.c$$D$D$D,[this.x / d, this.y / d, this.z / d]);
});

Clazz.newMeth(C$, 'unit$',  function () {
var d=this.dist$();
if (d == 0 ) {
System.err.println$S("Cannot call unit() on a null vector. Returned (1,0,0)");
this.x=1;
this.y=0;
this.z=0;
return this;
}this.x/=d;
this.y/=d;
this.z/=d;
return this;
});

Clazz.newMeth(C$, 'center$com_actelion_research_chem_Coordinates',  function (c) {
this.x=(this.x + c.x) / 2.0;
this.y=(this.y + c.y) / 2.0;
this.z=(this.z + c.z) / 2.0;
return this;
});

Clazz.newMeth(C$, 'center$com_actelion_research_chem_Coordinates$com_actelion_research_chem_Coordinates',  function (c1, c2) {
this.x=(c1.x + c2.x) / 2.0;
this.y=(c1.y + c2.y) / 2.0;
this.z=(c1.z + c2.z) / 2.0;
});

Clazz.newMeth(C$, 'between$com_actelion_research_chem_Coordinates$com_actelion_research_chem_Coordinates$D',  function (c1, c2, f) {
this.x=c1.x + f * (c2.x - c1.x);
this.y=c1.y + f * (c2.y - c1.y);
this.z=c1.z + f * (c2.z - c1.z);
return this;
});

Clazz.newMeth(C$, 'insideBounds$com_actelion_research_chem_CoordinatesA',  function (bounds) {
return bounds != null  && bounds[0].x <= this.x   && this.x <= bounds[1].x   && bounds[0].y <= this.y   && this.y <= bounds[1].y   && bounds[0].z <= this.z   && this.z <= bounds[1].z  ;
});

Clazz.newMeth(C$, 'toString',  function () {
var df=Clazz.new_($I$(1,1).c$$S,["0.00"]);
return "[" + df.format$D(this.x) + ", " + df.format$D(this.y) + ", " + df.format$D(this.z) + "]" ;
});

Clazz.newMeth(C$, 'toStringSpaceDelimited$',  function () {
var df=Clazz.new_($I$(1,1).c$$S,["0.00"]);
return df.format$D(this.x) + " " + df.format$D(this.y) + " " + df.format$D(this.z) ;
});

Clazz.newMeth(C$, 'toStringSpaceDelimited$java_util_Locale',  function (locale) {
var nf=$I$(2).getNumberInstance$java_util_Locale(locale);
var df=nf;
df.applyPattern$S("0.00");
return df.format$D(this.x) + " " + df.format$D(this.y) + " " + df.format$D(this.z) ;
});

Clazz.newMeth(C$, 'equals$O',  function (o) {
if (o == null  || !(Clazz.instanceOf(o, "com.actelion.research.chem.Coordinates")) ) return false;
var c=o;
return Math.abs(c.x - this.x) + Math.abs(c.y - this.y) + Math.abs(c.z - this.z)  < 1.0E-6 ;
});

Clazz.newMeth(C$, 'isNaN$',  function () {
return Double.isNaN$D(this.x) || Double.isNaN$D(this.y) || Double.isNaN$D(this.z)  ;
});

Clazz.newMeth(C$, 'min$com_actelion_research_chem_Coordinates',  function (c) {
return Clazz.new_(C$.c$$D$D$D,[Math.min(this.x, c.x), Math.min(this.y, c.y), Math.min(this.z, c.z)]);
});

Clazz.newMeth(C$, 'max$com_actelion_research_chem_Coordinates',  function (c) {
return Clazz.new_(C$.c$$D$D$D,[Math.max(this.x, c.x), Math.max(this.y, c.y), Math.max(this.z, c.z)]);
});

Clazz.newMeth(C$, 'cosAngle$com_actelion_research_chem_Coordinates',  function (c) {
var d=this.dist$() * c.dist$();
if (d <= 0 ) return 0;
return this.dot$com_actelion_research_chem_Coordinates(c) / d;
});

Clazz.newMeth(C$, 'min$com_actelion_research_chem_CoordinatesA',  function (c) {
var min=Clazz.new_(C$.c$$com_actelion_research_chem_Coordinates,[c[0]]);
for (var i=1; i < c.length; i++) {
min.x=Math.min(c[i].x, min.x);
min.y=Math.min(c[i].y, min.y);
min.z=Math.min(c[i].z, min.z);
}
return min;
}, 1);

Clazz.newMeth(C$, 'max$com_actelion_research_chem_CoordinatesA',  function (c) {
var max=Clazz.new_(C$.c$$com_actelion_research_chem_Coordinates,[c[0]]);
for (var i=1; i < c.length; i++) {
max.x=Math.max(c[i].x, max.x);
max.y=Math.max(c[i].y, max.y);
max.z=Math.max(c[i].z, max.z);
}
return max;
}, 1);

Clazz.newMeth(C$, 'createBarycenter$com_actelion_research_chem_CoordinatesA',  function (coords) {
if (coords == null ) throw Clazz.new_(Clazz.load('IllegalArgumentException').c$$S,["The coordinates are null"]);
var res=Clazz.new_(C$);
for (var i=0; i < coords.length; i++) {
res.x+=coords[i].x;
res.y+=coords[i].y;
res.z+=coords[i].z;
}
res.x/=coords.length;
res.y/=coords.length;
res.z/=coords.length;
return res;
}, 1);

Clazz.newMeth(C$, 'getMirror$com_actelion_research_chem_Coordinates$com_actelion_research_chem_Coordinates$com_actelion_research_chem_Coordinates$com_actelion_research_chem_Coordinates',  function (p, c1, c2, c3) {
var r31=Clazz.new_(C$.c$$com_actelion_research_chem_Coordinates,[c3]);
r31.sub$com_actelion_research_chem_Coordinates(c1);
var r21=Clazz.new_(C$.c$$com_actelion_research_chem_Coordinates,[c2]);
r21.sub$com_actelion_research_chem_Coordinates(c1);
var c=r31.cross$com_actelion_research_chem_Coordinates(r21);
if (c.distSq$() < 0.05 ) return Clazz.new_(C$.c$$com_actelion_research_chem_Coordinates,[p]);
var n=c.unitC$();
var pc1=Clazz.new_(C$.c$$com_actelion_research_chem_Coordinates,[c1]);
pc1.sub$com_actelion_research_chem_Coordinates(p);
var l=pc1.dot$com_actelion_research_chem_Coordinates(n);
n.scale$D(2 * l);
var pp=Clazz.new_(C$.c$$com_actelion_research_chem_Coordinates,[p]);
pp.add$com_actelion_research_chem_Coordinates(n);
return pp;
}, 1);

Clazz.newMeth(C$, 'getDihedral$com_actelion_research_chem_Coordinates$com_actelion_research_chem_Coordinates$com_actelion_research_chem_Coordinates$com_actelion_research_chem_Coordinates',  function (c1, c2, c3, c4) {
var v1=c2.subC$com_actelion_research_chem_Coordinates(c1);
var v2=c3.subC$com_actelion_research_chem_Coordinates(c2);
var v3=c4.subC$com_actelion_research_chem_Coordinates(c3);
var n1=v1.cross$com_actelion_research_chem_Coordinates(v2);
var n2=v2.cross$com_actelion_research_chem_Coordinates(v3);
return -Math.atan2(v2.getLength$() * v1.dot$com_actelion_research_chem_Coordinates(n2), n1.dot$com_actelion_research_chem_Coordinates(n2));
}, 1);

Clazz.newMeth(C$, ['compareTo$com_actelion_research_chem_Coordinates','compareTo$O'],  function (o) {
if (this.x != o.x ) return this.x < o.x  ? -1 : 1;
if (this.y != o.y ) return this.y < o.y  ? -1 : 1;
if (this.z != o.z ) return this.z < o.z  ? -1 : 1;
return 0;
});

Clazz.newMeth(C$, 'random$',  function () {
var random=Clazz.new_($I$(3,1));
return Clazz.new_(C$.c$$D$D$D,[random.nextDouble$() * 2 - 1, random.nextDouble$() * 2 - 1, random.nextDouble$() * 2 - 1]);
}, 1);

Clazz.newMeth(C$, 'getRmsd$com_actelion_research_chem_CoordinatesA$com_actelion_research_chem_CoordinatesA',  function (c1, c2) {
return C$.getRmsd$com_actelion_research_chem_CoordinatesA$com_actelion_research_chem_CoordinatesA$I(c1, c2, Math.min(c1.length, c2.length));
}, 1);

Clazz.newMeth(C$, 'getRmsd$com_actelion_research_chem_CoordinatesA$com_actelion_research_chem_CoordinatesA$I',  function (c1, c2, l) {
var sum=0;
for (var i=0; i < l; i++) {
sum+=c1[i].distanceSquared$com_actelion_research_chem_Coordinates(c2[i]);
}
return l > 0 ? Math.sqrt(sum / l) : 0;
}, 1);
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-25 13:07:44 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
