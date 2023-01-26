(function(){var P$=Clazz.newPackage("com.actelion.research.chem"),I$=[[0,'com.actelion.research.chem.coords.CoordinateInventor']],I$0=I$[0],$I$=function(i,n){return((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "IDCodeParser", null, 'com.actelion.research.chem.IDCodeParserWithoutCoordinateInvention');

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
this.mCoordinateMode=2;
},1);

C$.$fields$=[['Z',['mEnsure2DCoordinates'],'I',['mCoordinateMode']]]

Clazz.newMeth(C$, 'c$',  function () {
C$.c$$Z.apply(this, [true]);
}, 1);

Clazz.newMeth(C$, 'c$$I',  function (coordinateMode) {
C$.c$$Z.apply(this, [true]);
this.mCoordinateMode=coordinateMode;
}, 1);

Clazz.newMeth(C$, 'c$$Z',  function (ensure2DCoordinates) {
;C$.superclazz.c$.apply(this,[]);C$.$init$.apply(this);
this.mEnsure2DCoordinates=ensure2DCoordinates;
}, 1);

Clazz.newMeth(C$, 'ensure2DCoordinates$',  function () {
return this.mEnsure2DCoordinates;
});

Clazz.newMeth(C$, 'inventCoordinates$com_actelion_research_chem_StereoMolecule',  function (mol) {
var inventor=Clazz.new_($I$(1,1).c$$I,[this.mCoordinateMode]);
inventor.setRandomSeed$J(78187493520);
inventor.invent$com_actelion_research_chem_StereoMolecule(mol);
});
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-25 13:07:44 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
