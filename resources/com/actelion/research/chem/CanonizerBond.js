(function(){var P$=Clazz.newPackage("com.actelion.research.chem"),I$=[];
/*c*/var C$=Clazz.newClass(P$, "CanonizerBond", null, null, 'Comparable');

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['I',['maxAtomRank','minAtomRank','bond']]]

Clazz.newMeth(C$, 'c$$I$I$I',  function (atomRank1, atomRank2, bond) {
;C$.$init$.apply(this);
this.maxAtomRank=Math.max(atomRank1, atomRank2);
this.minAtomRank=Math.min(atomRank1, atomRank2);
this.bond=bond;
}, 1);

Clazz.newMeth(C$, ['compareTo$com_actelion_research_chem_CanonizerBond','compareTo$O'],  function (cb) {
if (this.maxAtomRank != cb.maxAtomRank) return this.maxAtomRank > cb.maxAtomRank ? -1 : 1;
if (this.minAtomRank != cb.minAtomRank) return this.minAtomRank > cb.minAtomRank ? -1 : 1;
return 0;
});

Clazz.newMeth(C$);
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-18 09:54:14 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
