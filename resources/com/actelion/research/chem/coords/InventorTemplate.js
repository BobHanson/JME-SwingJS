(function(){var P$=Clazz.newPackage("com.actelion.research.chem.coords"),I$=[];
/*c*/var C$=Clazz.newClass(P$, "InventorTemplate");

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['Z',['mKeepAbsoluteOrientation'],'D',['mAVBL'],'O',['mFragment','com.actelion.research.chem.StereoMolecule','mFFP','long[]']]]

Clazz.newMeth(C$, 'c$$com_actelion_research_chem_StereoMolecule$JA$Z',  function (fragment, ffp, keepAbsoluteOrientation) {
;C$.$init$.apply(this);
this.mFragment=fragment;
this.mFFP=ffp;
this.mKeepAbsoluteOrientation=keepAbsoluteOrientation;
}, 1);

Clazz.newMeth(C$, 'keepAbsoluteOrientation$',  function () {
return this.mKeepAbsoluteOrientation;
});

Clazz.newMeth(C$, 'normalizeCoordinates$',  function () {
this.mAVBL=this.mFragment.getAverageBondLength$();
});

Clazz.newMeth(C$, 'getNormalizedAtomX$I',  function (atom) {
return this.mFragment.getAtomX$I(atom) / this.mAVBL;
});

Clazz.newMeth(C$, 'getNormalizedAtomY$I',  function (atom) {
return this.mFragment.getAtomY$I(atom) / this.mAVBL;
});

Clazz.newMeth(C$, 'getFragment$',  function () {
return this.mFragment;
});

Clazz.newMeth(C$, 'getFFP$',  function () {
return this.mFFP;
});

Clazz.newMeth(C$);
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-18 09:54:18 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
