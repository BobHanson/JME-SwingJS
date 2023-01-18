(function(){var P$=Clazz.newPackage("com.actelion.research.chem"),I$=[[0,'java.util.Arrays']],I$0=I$[0],$I$=function(i,n){return((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "CanonizerBaseValue", null, null, 'Comparable');

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['I',['mAtom','mIndex','mAvailableBits'],'O',['mValue','long[]']]]

Clazz.newMeth(C$, 'c$$I',  function (size) {
;C$.$init$.apply(this);
this.mValue=Clazz.array(Long.TYPE, [size]);
}, 1);

Clazz.newMeth(C$, 'init$I',  function (atom) {
this.mAtom=atom;
this.mIndex=0;
this.mAvailableBits=63;
$I$(1).fill$JA$J(this.mValue, 0);
});

Clazz.newMeth(C$, 'add$J',  function (data) {
(this.mValue[$k$=this.mIndex]=Long.$add(this.mValue[$k$],(data)));
});

Clazz.newMeth(C$, 'add$I$J',  function (bits, data) {
if (this.mAvailableBits == 0) {
++this.mIndex;
this.mAvailableBits=63;
}if (this.mAvailableBits == 63) {
(this.mValue[$k$=this.mIndex]=Long.$or(this.mValue[$k$],(data)));
this.mAvailableBits-=bits;
} else {
if (this.mAvailableBits >= bits) {
(this.mValue[$k$=this.mIndex]=Long.$sl(this.mValue[$k$],(bits)));
(this.mValue[$k$=this.mIndex]=Long.$or(this.mValue[$k$],(data)));
this.mAvailableBits-=bits;
} else {
(this.mValue[$k$=this.mIndex]=Long.$sl(this.mValue[$k$],(this.mAvailableBits)));
(this.mValue[$k$=this.mIndex]=Long.$or(this.mValue[$k$],((Long.$sr(data,(bits - this.mAvailableBits))))));
bits-=this.mAvailableBits;
++this.mIndex;
this.mAvailableBits=63 - bits;
(this.mValue[$k$=this.mIndex]=Long.$or(this.mValue[$k$],((Long.$and(data,((1 << bits) - 1))))));
}}});

Clazz.newMeth(C$, 'getAtom$',  function () {
return this.mAtom;
});

Clazz.newMeth(C$, ['compareTo$com_actelion_research_chem_CanonizerBaseValue','compareTo$O'],  function (b) {
Clazz.assert(C$, this, function(){return (this.mIndex == b.mIndex)});
for (var i=0; i < this.mIndex; i++) if (Long.$ne(this.mValue[i],b.mValue[i] )) return (Long.$lt(this.mValue[i],b.mValue[i] )) ? -1 : 1;

return (Long.$eq(this.mValue[this.mIndex],b.mValue[this.mIndex] )) ? 0 : (Long.$lt(this.mValue[this.mIndex],b.mValue[this.mIndex] )) ? -1 : 1;
});

C$.$static$=function(){C$.$static$=0;
C$.$_ASSERT_ENABLED_ = ClassLoader.getClassAssertionStatus$(C$);
};
var $k$;

Clazz.newMeth(C$);
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-18 09:54:14 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
