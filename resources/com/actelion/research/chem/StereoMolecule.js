(function(){var P$=Clazz.newPackage("com.actelion.research.chem"),p$1={},I$=[[0,'com.actelion.research.chem.Canonizer']],I$0=I$[0],$I$=function(i,n){return((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "StereoMolecule", null, 'com.actelion.research.chem.ExtendedMolecule');

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['Z',['mAssignParitiesToNitrogen'],'O',['mCanonizer','com.actelion.research.chem.Canonizer']]
,['O',['VALIDATION_ERRORS_STEREO','String[]']]]

Clazz.newMeth(C$, 'c$',  function () {
Clazz.super_(C$, this);
}, 1);

Clazz.newMeth(C$, 'c$$I$I',  function (maxAtoms, maxBonds) {
;C$.superclazz.c$$I$I.apply(this,[maxAtoms, maxBonds]);C$.$init$.apply(this);
}, 1);

Clazz.newMeth(C$, 'c$$com_actelion_research_chem_Molecule',  function (mol) {
;C$.superclazz.c$$com_actelion_research_chem_Molecule.apply(this,[mol]);C$.$init$.apply(this);
}, 1);

Clazz.newMeth(C$, 'createMolecule$I$I',  function (atoms, bonds) {
return Clazz.new_(C$.c$$I$I,[atoms, bonds]);
});

Clazz.newMeth(C$, 'getCompactCopy$',  function () {
var theCopy=Clazz.new_(C$.c$$I$I,[this.mAllAtoms, this.mAllBonds]);
this.copyMolecule$com_actelion_research_chem_Molecule(theCopy);
return theCopy;
});

Clazz.newMeth(C$, 'copyMoleculeProperties$com_actelion_research_chem_Molecule',  function (destMol) {
C$.superclazz.prototype.copyMoleculeProperties$com_actelion_research_chem_Molecule.apply(this, [destMol]);
if (this.mCanonizer != null ) destMol.mValidHelperArrays=0;
});

Clazz.newMeth(C$, 'getFragments$',  function () {
var fragmentNo=Clazz.array(Integer.TYPE, [this.mAllAtoms]);
var fragments=this.getFragmentNumbers$IA$Z$Z(fragmentNo, false, false);
return this.getFragments$IA$I(fragmentNo, fragments);
});

Clazz.newMeth(C$, 'getFragments$IA$I',  function (fragmentNo, fragmentCount) {
var fragment=Clazz.array(C$, [fragmentCount]);
var atoms=Clazz.array(Integer.TYPE, [fragmentCount]);
var bonds=Clazz.array(Integer.TYPE, [fragmentCount]);
var atomMap=Clazz.array(Integer.TYPE, [this.mAllAtoms]);
for (var atom=0; atom < this.mAllAtoms; atom++) if (fragmentNo[atom] != -1) atomMap[atom]=atoms[fragmentNo[atom]]++;

for (var bond=0; bond < this.mAllBonds; bond++) {
var f1=fragmentNo[this.mBondAtom[0][bond]];
var f2=fragmentNo[this.mBondAtom[1][bond]];
if (f1 == f2 && f1 != -1 ) ++bonds[f1];
}
for (var i=0; i < fragmentCount; i++) {
fragment[i]=this.createMolecule$I$I(atoms[i], bonds[i]);
this.copyMoleculeProperties$com_actelion_research_chem_Molecule(fragment[i]);
}
for (var atom=0; atom < this.mAllAtoms; atom++) if (fragmentNo[atom] != -1) this.copyAtom$com_actelion_research_chem_Molecule$I$I$I(fragment[fragmentNo[atom]], atom, 0, 0);

for (var bond=0; bond < this.mAllBonds; bond++) {
var f1=fragmentNo[this.mBondAtom[0][bond]];
var f2=fragmentNo[this.mBondAtom[1][bond]];
if (f1 == f2 && f1 != -1 ) this.copyBond$com_actelion_research_chem_Molecule$I$I$I$IA$Z(fragment[f1], bond, 0, 0, atomMap, false);
}
for (var f, $f = 0, $$f = fragment; $f<$$f.length&&((f=($$f[$f])),1);$f++) {
f.renumberESRGroups$I(1);
f.renumberESRGroups$I(2);
}
return fragment;
});

Clazz.newMeth(C$, 'ensureHelperArrays$I',  function (required) {
C$.superclazz.prototype.ensureHelperArrays$I.apply(this, [required]);
if ((required & ~this.mValidHelperArrays) == 0) return;
if (this.mAssignParitiesToNitrogen) required|=128;
for (var atom=0; atom < this.getAllAtoms$(); atom++) this.mAtomFlags[atom]&=~67223559;

for (var bond=0; bond < this.getBonds$(); bond++) this.mBondFlags[bond]&=~63;

var rankBits=0;
var rankMode=0;
if ((required & 32) != 0) {
rankBits=32;
rankMode=1;
} else if ((required & 64) != 0) {
rankBits=64;
rankMode=3;
}if ((required & 128) != 0) {
rankBits|=128;
rankMode|=32;
}this.mCanonizer=Clazz.new_($I$(1,1).c$$com_actelion_research_chem_StereoMolecule$I,[this, rankMode]);
this.mCanonizer.setParities$();
this.mCanonizer.setStereoCenters$();
this.mCanonizer.setCIPParities$();
if (p$1.validateESR.apply(this, [])) this.mCanonizer=Clazz.new_($I$(1,1).c$$com_actelion_research_chem_StereoMolecule$I,[this, rankMode]);
this.mValidHelperArrays|=(8 | 16 | rankBits );
});

Clazz.newMeth(C$, 'validateESR',  function () {
var paritiesUpdated=false;
for (var atom=0; atom < this.getAtoms$(); atom++) if (!this.isAtomStereoCenter$I(atom) || this.getAtomParity$I(atom) == 3 ) this.mAtomFlags[atom]&=~33292288;

for (var bond=0; bond < this.getBonds$(); bond++) if (this.getBondOrder$I(bond) != 1 || this.getBondParity$I(bond) == 0  || this.getBondParity$I(bond) == 3 ) this.mBondFlags[bond]&=~32512;

if (this.mIsRacemate) {
if ((this.mChirality & ~65535) != 131072) {
var isIndependentRacemicAtom=Clazz.array(Boolean.TYPE, [this.getAtoms$()]);
for (var atom=0; atom < this.getAtoms$(); atom++) if (this.isAtomStereoCenter$I(atom) && this.getAtomParity$I(atom) != 3  && this.getAtomESRType$I(atom) == 1 ) isIndependentRacemicAtom[atom]=true;

for (var atom=0; atom < this.getAtoms$(); atom++) {
if (this.isAtomStereoCenter$I(atom) && this.getAtomParity$I(atom) != 3 ) {
this.setAtomESR$I$I$I(atom, 1, 0);
paritiesUpdated=true;
}}
for (var atom=0; atom < this.getAtoms$(); atom++) {
if (isIndependentRacemicAtom[atom]) {
this.setAtomParity$I$I$Z(atom, 1, false);
this.setAtomESR$I$I$I(atom, 1, -1);
paritiesUpdated=true;
}}
}this.mIsRacemate=false;
}this.renumberESRGroups$I(1);
this.renumberESRGroups$I(2);
return paritiesUpdated;
}, p$1);

Clazz.newMeth(C$, 'stripStereoInformation$',  function () {
this.ensureHelperArrays$I(15);
this.mIsRacemate=false;
for (var atom=0; atom < this.mAllAtoms; atom++) {
this.mAtomFlags[atom]&=~33292288;
if ((this.mAtomFlags[atom] & 3) != 0) this.mAtomFlags[atom]|=33554432;
 else this.mAtomFlags[atom]&=~33554432;
}
for (var bond=0; bond < this.mAllBonds; bond++) if ((this.mBondFlags[bond] & 3) != 0 && this.getBondOrder$I(bond) == 2 ) this.mBondType[bond]=386;
 else this.mBondType[bond]&=~384;

this.mValidHelperArrays&=~248;
});

Clazz.newMeth(C$, 'getAbsoluteAtomParity$I',  function (atom) {
return this.mCanonizer.getTHParity$I(atom);
});

Clazz.newMeth(C$, 'getAbsoluteBondParity$I',  function (bond) {
return this.mCanonizer.getEZParity$I(bond);
});

Clazz.newMeth(C$, 'getSymmetryRank$I',  function (atom) {
return this.mCanonizer.getSymmetryRank$I(atom);
});

Clazz.newMeth(C$, 'getIDCode$',  function () {
this.ensureHelperArrays$I(15);
return this.mCanonizer == null  ? null : this.mCanonizer.getIDCode$();
});

Clazz.newMeth(C$, 'getIDCoordinates$',  function () {
this.ensureHelperArrays$I(15);
return this.mCanonizer == null  ? null : this.mCanonizer.getEncodedCoordinates$();
});

Clazz.newMeth(C$, 'getCanonizer$',  function () {
this.ensureHelperArrays$I(15);
return this.mCanonizer;
});

Clazz.newMeth(C$, 'getStereoCenterCount$',  function () {
this.ensureHelperArrays$I(31);
var scCount=0;
for (var atom=0; atom < this.getAtoms$(); atom++) if (this.getAtomParity$I(atom) != 0 && !this.isAtomParityPseudo$I(atom) ) ++scCount;

return scCount;
});

Clazz.newMeth(C$, 'getESRGroupMemberCounts$',  function () {
this.ensureHelperArrays$I(15);
var maxESRGroup=Clazz.array(Integer.TYPE, [3]);
for (var atom=0; atom < this.getAtoms$(); atom++) {
if (this.isAtomStereoCenter$I(atom)) {
var type=this.getAtomESRType$I(atom);
if (type != 0) maxESRGroup[type]=Math.max(maxESRGroup[type], this.getAtomESRGroup$I(atom));
}}
for (var bond=0; bond < this.getBonds$(); bond++) {
if ((this.getBondParity$I(bond) == 1 || this.getBondParity$I(bond) == 2 ) && this.getBondType$I(bond) == 1 ) {
var type=this.getBondESRType$I(bond);
if (type != 0) maxESRGroup[type]=Math.max(maxESRGroup[type], this.getBondESRGroup$I(bond));
}}
var esrGroupMembers=Clazz.array(Integer.TYPE, [3, null]);
esrGroupMembers[1]=Clazz.array(Integer.TYPE, [1 + maxESRGroup[1]]);
esrGroupMembers[2]=Clazz.array(Integer.TYPE, [1 + maxESRGroup[2]]);
for (var atom=0; atom < this.getAtoms$(); atom++) {
if (this.isAtomStereoCenter$I(atom)) {
var type=this.getAtomESRType$I(atom);
if (type != 0) ++esrGroupMembers[type][this.getAtomESRGroup$I(atom)];
}}
for (var bond=0; bond < this.getBonds$(); bond++) {
if ((this.getBondParity$I(bond) == 1 || this.getBondParity$I(bond) == 2 ) && this.getBondType$I(bond) == 1 ) {
var type=this.getBondESRType$I(bond);
if (type != 0) ++esrGroupMembers[type][this.getBondESRGroup$I(bond)];
}}
return esrGroupMembers;
});

Clazz.newMeth(C$, 'setUnknownParitiesToExplicitlyUnknown$',  function () {
this.ensureHelperArrays$I(31);
if (this.mCanonizer != null ) this.mCanonizer.setUnknownParitiesToExplicitlyUnknown$();
});

Clazz.newMeth(C$, 'setAssignParitiesToNitrogen$Z',  function (b) {
this.mAssignParitiesToNitrogen=b;
this.mValidHelperArrays&=~(143);
});

Clazz.newMeth(C$, 'translateTHParity$I$IA',  function (atom, targetAtomIndex) {
var parity=this.getAtomParity$I(atom);
if (parity == 1 || parity == 2 ) {
var inversion=false;
if (this.isCentralAlleneAtom$I(atom)) {
for (var i=0; i < this.getConnAtoms$I(atom); i++) {
var connAtom=this.getConnAtom$I$I(atom, i);
var neighbours=0;
var neighbour=Clazz.array(Integer.TYPE, [3]);
for (var j=0; j < this.getConnAtoms$I(connAtom); j++) {
neighbour[neighbours]=this.getConnAtom$I$I(connAtom, j);
if (neighbour[neighbours] != atom) ++neighbours;
}
if (!!(neighbours == 2 && !!(((neighbour[0] < neighbour[1])) ^ (targetAtomIndex[neighbour[0]] < targetAtomIndex[neighbour[1]])))) inversion=!inversion;
}
} else {
for (var i=1; i < this.getConnAtoms$I(atom); i++) {
for (var j=0; j < i; j++) {
var connAtom1=this.getConnAtom$I$I(atom, i);
var connAtom2=this.getConnAtom$I$I(atom, j);
if (!!((connAtom1 < connAtom2) ^ (targetAtomIndex[connAtom1] < targetAtomIndex[connAtom2]))) inversion=!inversion;
}
}
}if (inversion) parity=(parity == 1) ? 2 : 1;
}return parity;
});

Clazz.newMeth(C$, 'validate$',  function () {
C$.superclazz.prototype.validate$.apply(this, []);
this.ensureHelperArrays$I(31);
for (var atom=0; atom < this.getAtoms$(); atom++) {
if ((this.getAtomESRType$I(atom) == 1 || this.getAtomESRType$I(atom) == 2 ) && (!this.isAtomStereoCenter$I(atom) || this.getAtomParity$I(atom) == 3 ) ) throw Clazz.new_(Clazz.load('Exception').c$$S,["Members of ESR groups must only be stereo centers with known configuration."]);
if ((this.mAtomFlags[atom] & 65536) != 0) throw Clazz.new_(Clazz.load('Exception').c$$S,["Over- or under-specified stereo feature or more than one racemic type bond"]);
if ((this.getAtomParity$I(atom) == 1 || this.getAtomParity$I(atom) == 2 ) && this.getAtomPi$I(atom) == 0 ) {
var angle=Clazz.array(Double.TYPE, [this.getConnAtoms$I(atom)]);
for (var i=0; i < this.getConnAtoms$I(atom); i++) angle[i]=this.getBondAngle$I$I(atom, this.getConnAtom$I$I(atom, i));

for (var i=1; i < this.getConnAtoms$I(atom); i++) if (!this.isStereoBond$I$I(this.getConnBond$I$I(atom, i), atom)) for (var j=0; j < i; j++) if (!this.isStereoBond$I$I(this.getConnBond$I$I(atom, j), atom)) if (this.bondsAreParallel$D$D(angle[i], angle[j])) throw Clazz.new_(Clazz.load('Exception').c$$S,["Ambiguous configuration at stereo center because of 2 parallel bonds"]);


}}
});

Clazz.newMeth(C$, 'getChiralText$',  function () {
this.ensureHelperArrays$I(31);
var count=this.mChirality & 65535;
switch (this.mChirality & ~65535) {
case 65536:
return null;
case 131072:
return (count == 1) ? "meso" : "" + count + " meso diastereomers" ;
case 0:
return "unknown chirality";
case 196608:
return "both enantiomers";
case 262144:
return "this enantiomer";
case 327680:
return "this or other enantiomer";
case 393216:
return "two epimers";
default:
return (count == 1) ? "one stereo isomer" : "" + count + " stereo isomers" ;
}
});

Clazz.newMeth(C$, 'writeObject$java_io_ObjectOutputStream',  function (stream) {
}, p$1);

Clazz.newMeth(C$, 'readObject$java_io_ObjectInputStream',  function (stream) {
}, p$1);

C$.$static$=function(){C$.$static$=0;
C$.VALIDATION_ERRORS_STEREO=Clazz.array(String, -1, ["Members of ESR groups must only be stereo centers with known configuration.", "Over- or under-specified stereo feature or more than one racemic type bond", "Ambiguous configuration at stereo center because of 2 parallel bonds"]);
};
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-25 13:07:46 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
