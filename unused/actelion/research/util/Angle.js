(function(){var P$=Clazz.newPackage("com.actelion.research.util"),I$=[];
/*c*/var C$=Clazz.newClass(P$, "Angle");

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
this.TWO_PI=6.283185307179586;
},1);

C$.$fields$=[['D',['TWO_PI','mValue']]]

Clazz.newMeth(C$, 'c$',  function () {
;C$.$init$.apply(this);
}, 1);

Clazz.newMeth(C$, 'c$$com_actelion_research_util_Angle',  function (a) {
C$.c$$D.apply(this, [a.mValue]);
}, 1);

Clazz.newMeth(C$, 'c$$D',  function (value) {
;C$.$init$.apply(this);
this.mValue=value;
this.normalize$();
}, 1);

Clazz.newMeth(C$, 'normalize$',  function () {
while (this.mValue < -3.141592653589793 )this.mValue+=6.283185307179586;

while (this.mValue >= 3.141592653589793 )this.mValue-=6.283185307179586;

});

Clazz.newMeth(C$, 'toDegrees$',  function () {
return 180.0 * this.mValue / 3.141592653589793;
});

Clazz.newMeth(C$, 'getValue$',  function () {
return this.mValue;
});

Clazz.newMeth(C$, 'setValue$D',  function (value) {
this.mValue=value;
this.normalize$();
});

Clazz.newMeth(C$, 'cos$',  function () {
return Math.cos(this.mValue);
});

Clazz.newMeth(C$, 'sin$',  function () {
return Math.sin(this.mValue);
});

Clazz.newMeth(C$, 'tan$',  function () {
return Math.tan(this.mValue);
});

Clazz.newMeth(C$, 'arcsin$D',  function (x) {
return Clazz.new_(C$.c$$D,[Math.asin(x)]);
}, 1);

Clazz.newMeth(C$, 'arccos$D',  function (x) {
return Clazz.new_(C$.c$$D,[Math.acos(x)]);
}, 1);

Clazz.newMeth(C$, 'arctan$D$D',  function (y, x) {
return Clazz.new_(C$.c$$D,[Math.atan2(y, x)]);
}, 1);

Clazz.newMeth(C$, 'mean$com_actelion_research_util_Angle$com_actelion_research_util_Angle',  function (a1, a2) {
var mean=(a1.mValue + a2.mValue) / 2.0;
var dif=a2.mValue - a1.mValue;
if (Math.abs(dif) > 3.141592653589793 ) {
if (mean < 0.0 ) mean+=3.141592653589793;
 else mean-=3.141592653589793;
}return mean;
}, 1);

Clazz.newMeth(C$, 'difference$D$D',  function (a2, a1) {
var a=a2 - a1;
if (a >= 3.141592653589793 ) a-=6.283185307179586;
 else if (a < -3.141592653589793 ) a+=6.283185307179586;
return a;
}, 1);

Clazz.newMeth(C$, 'difference$com_actelion_research_util_Angle$com_actelion_research_util_Angle',  function (a2, a1) {
return C$.difference$D$D(a2.mValue, a1.mValue);
}, 1);

Clazz.newMeth(C$, 'add$D',  function (value) {
this.mValue+=value;
this.normalize$();
});

Clazz.newMeth(C$, 'add$com_actelion_research_util_Angle',  function (a) {
this.add$D(a.mValue);
});

Clazz.newMeth(C$, 'subtract$D',  function (value) {
this.mValue-=value;
this.normalize$();
});

Clazz.newMeth(C$, 'subtract$com_actelion_research_util_Angle',  function (a) {
this.subtract$D(a.mValue);
});

Clazz.newMeth(C$, 'isSmallerThan$com_actelion_research_util_Angle',  function (a) {
var dif=a.mValue - this.mValue;
return ((dif > 0.0  && dif < 3.141592653589793  ) || (dif < 0.0  && dif > 3.141592653589793  ) );
});

Clazz.newMeth(C$, 'isGreaterThan$com_actelion_research_util_Angle',  function (a) {
var dif=this.mValue - a.mValue;
return ((dif > 0.0  && dif < 3.141592653589793  ) || (dif < 0.0  && dif > 3.141592653589793  ) );
});

Clazz.newMeth(C$, 'toString',  function () {
return new Double(this.toDegrees$()).toString() + " degrees";
});
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-25 13:08:05 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
