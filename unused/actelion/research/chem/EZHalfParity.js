(function(){var P$=Clazz.newPackage("com.actelion.research.chem"),I$=[];
/*c*/var C$=Clazz.newClass(P$, "EZHalfParity");

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['Z',['mStereoBondFound','mRanksEqual','mInSameFragment'],'I',['mCentralAxialAtom','mRemoteAxialAtom','mHighConn','mLowConn','mValue'],'O',['mMol','com.actelion.research.chem.ExtendedMolecule']]]

Clazz.newMeth(C$, 'c$$com_actelion_research_chem_ExtendedMolecule$IA$I$I',  function (mol, rank, atom1, atom2) {
;C$.$init$.apply(this);
this.mMol=mol;
this.mRemoteAxialAtom=atom1;
this.mCentralAxialAtom=atom2;
var highRank=-1;
for (var i=0; i < this.mMol.getAllConnAtoms$I(this.mCentralAxialAtom); i++) {
var connAtom=this.mMol.getConnAtom$I$I(this.mCentralAxialAtom, i);
var connBond=this.mMol.getConnBond$I$I(this.mCentralAxialAtom, i);
if (connAtom == this.mRemoteAxialAtom) {
if (this.mMol.getBondType$I(connBond) == 386) this.mValue=-1;
continue;
}if (this.mMol.isStereoBond$I$I(connBond, this.mCentralAxialAtom)) {
if (this.mStereoBondFound) mol.setStereoProblem$I(atom2);
this.mStereoBondFound=true;
}if (highRank == rank[connAtom]) {
this.mLowConn=connAtom;
this.mRanksEqual=true;
this.mInSameFragment=this.mMol.isRingBond$I(connBond);
} else if (highRank < rank[connAtom]) {
highRank=rank[connAtom];
this.mLowConn=this.mHighConn;
this.mHighConn=connAtom;
} else {
this.mLowConn=connAtom;
}}
}, 1);

Clazz.newMeth(C$, 'getValue$',  function () {
if (this.mValue != 0) return this.mValue;
if (this.mStereoBondFound && this.mMol.getAtomicNo$I(this.mCentralAxialAtom) != 15  && this.mMol.getAtomicNo$I(this.mCentralAxialAtom) != 16 ) {
for (var i=0; i < this.mMol.getAllConnAtoms$I(this.mCentralAxialAtom); i++) {
var connBond=this.mMol.getConnBond$I$I(this.mCentralAxialAtom, i);
if (this.mMol.isStereoBond$I$I(connBond, this.mCentralAxialAtom)) {
if (this.mMol.getConnAtom$I$I(this.mCentralAxialAtom, i) == this.mHighConn) this.mValue=(this.mMol.getBondType$I(connBond) == 257) ? 3 : 1;
 else this.mValue=(this.mMol.getBondType$I(connBond) == 257) ? 1 : 3;
return this.mValue;
}}
}var angleDB=this.mMol.getBondAngle$I$I(this.mCentralAxialAtom, this.mRemoteAxialAtom);
var angleHigh=this.mMol.getBondAngle$I$I(this.mCentralAxialAtom, this.mHighConn);
if (angleHigh < angleDB ) angleHigh+=6.283185307179586;
if (this.mMol.getAllConnAtoms$I(this.mCentralAxialAtom) == 2) {
var angleDif=angleHigh - angleDB;
if ((angleDif > 3.0915926535897933 ) && (angleDif < 3.191592653589793 ) ) {
this.mValue=-1;
return this.mValue;
}this.mValue=(angleDif < 3.141592653589793 ) ? 4 : 2;
return this.mValue;
} else {
var angleOther=this.mMol.getBondAngle$I$I(this.mCentralAxialAtom, this.mLowConn);
if (angleOther < angleDB ) angleOther+=6.283185307179586;
this.mValue=(angleOther < angleHigh ) ? 2 : 4;
return this.mValue;
}});

Clazz.newMeth(C$);
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-18 09:54:14 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
