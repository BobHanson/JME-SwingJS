(function(){var P$=Clazz.newPackage("com.actelion.research.chem"),I$=[];
/*c*/var C$=Clazz.newClass(P$, "MesoFragmentBranch");

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['I',['mirrorAtomIndex','neighbourIndex','current'],'O',['mirrorAtom','int[]']]]

Clazz.newMeth(C$, 'c$$IA$I$I',  function (mirrorAtom, neighbourIndex, current) {
;C$.$init$.apply(this);
this.mirrorAtom=mirrorAtom;
this.neighbourIndex=neighbourIndex;
this.current=current;
this.mirrorAtomIndex=1;
}, 1);

Clazz.newMeth(C$, 'getNextMirrorAtom$',  function () {
return this.mirrorAtomIndex < this.mirrorAtom.length ? this.mirrorAtom[this.mirrorAtomIndex++] : -1;
});

Clazz.newMeth(C$, 'hasNextMirrorAtom$',  function () {
return this.mirrorAtomIndex < this.mirrorAtom.length;
});

Clazz.newMeth(C$);
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-25 13:07:44 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
