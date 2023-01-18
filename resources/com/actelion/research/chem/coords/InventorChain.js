(function(){var P$=Clazz.newPackage("com.actelion.research.chem.coords"),I$=[];
/*c*/var C$=Clazz.newClass(P$, "InventorChain");

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['O',['mAtom','int[]','+mBond']]]

Clazz.newMeth(C$, 'c$$I',  function (chainLength) {
;C$.$init$.apply(this);
this.mAtom=Clazz.array(Integer.TYPE, [chainLength]);
this.mBond=Clazz.array(Integer.TYPE, [chainLength]);
}, 1);

Clazz.newMeth(C$, 'getChainLength$',  function () {
return this.mAtom.length;
});

Clazz.newMeth(C$, 'getRingAtoms$',  function () {
return this.mAtom;
});

Clazz.newMeth(C$, 'getRingBonds$',  function () {
return this.mBond;
});

Clazz.newMeth(C$);
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-18 09:54:18 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
