(function(){var P$=Clazz.newPackage("com.actelion.research.chem"),p$1={},I$=[[0,'com.actelion.research.chem.AromaticityResolver','java.util.Arrays','com.actelion.research.chem.Molecule','com.actelion.research.util.Angle','com.actelion.research.chem.RingCollection','com.actelion.research.chem.Coordinates']],I$0=I$[0],$I$=function(i,n,m){return m?$I$(i)[n].apply(null,m):((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "ExtendedMolecule", null, 'com.actelion.research.chem.Molecule', 'java.io.Serializable');

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['I',['mAtoms','mBonds'],'O',['mRingSet','com.actelion.research.chem.RingCollection','mPi','int[]','+mConnAtoms','+mAllConnAtoms','mConnAtom','int[][]','+mConnBond','+mConnBondOrder']]]

Clazz.newMeth(C$, 'c$',  function () {
Clazz.super_(C$, this);
}, 1);

Clazz.newMeth(C$, 'c$$I$I',  function (maxAtoms, maxBonds) {
;C$.superclazz.c$$I$I.apply(this,[maxAtoms, maxBonds]);C$.$init$.apply(this);
}, 1);

Clazz.newMeth(C$, 'c$$com_actelion_research_chem_Molecule',  function (mol) {
;C$.superclazz.c$$I$I.apply(this,[mol == null  ? 256 : mol.getMaxAtoms$(), mol == null  ? 256 : mol.getMaxBonds$()]);C$.$init$.apply(this);
if (mol != null ) mol.copyMolecule$com_actelion_research_chem_Molecule(this);
}, 1);

Clazz.newMeth(C$, 'copyMoleculeByAtoms$com_actelion_research_chem_ExtendedMolecule$ZA$Z$IA',  function (destMol, includeAtom, recognizeDelocalizedBonds, atomMap) {
if (recognizeDelocalizedBonds) this.ensureHelperArrays$I(7);
destMol.mAtomList=null;
if (this.mIsFragment) destMol.setFragment$Z(true);
var atomCount=includeAtom.length;
if (atomMap == null ) atomMap=Clazz.array(Integer.TYPE, [atomCount]);
destMol.mAllAtoms=0;
for (var atom=0; atom < atomCount; atom++) atomMap[atom]=includeAtom[atom] ? this.copyAtom$com_actelion_research_chem_Molecule$I$I$I(destMol, atom, 0, 0) : -1;

destMol.mAllBonds=0;
for (var bnd=0; bnd < this.mAllBonds; bnd++) {
var atom1=this.mBondAtom[0][bnd];
var atom2=this.mBondAtom[1][bnd];
if (atom1 < atomCount && atom2 < atomCount ) {
if (includeAtom[atom1] && includeAtom[atom2] ) this.copyBond$com_actelion_research_chem_Molecule$I$I$I$IA$Z(destMol, bnd, 0, 0, atomMap, recognizeDelocalizedBonds);
 else if (this.mAtomCharge[atom1] != 0 && this.mAtomCharge[atom2] != 0  && (!!(this.mAtomCharge[atom1] < 0 ^ this.mAtomCharge[atom2] < 0)) ) {
if (includeAtom[atom1]) destMol.mAtomCharge[atomMap[atom1]]+=(this.mAtomCharge[atom1] < 0) ? 1 : -1;
if (includeAtom[atom2]) destMol.mAtomCharge[atomMap[atom2]]+=(this.mAtomCharge[atom2] < 0) ? 1 : -1;
}}}
this.copyMoleculeProperties$com_actelion_research_chem_Molecule(destMol);
destMol.mValidHelperArrays=0;
destMol.renumberESRGroups$I(1);
destMol.renumberESRGroups$I(2);
if (destMol.mAllAtoms != atomCount) destMol.setFragment$Z(true);
if (recognizeDelocalizedBonds) Clazz.new_($I$(1,1).c$$com_actelion_research_chem_ExtendedMolecule,[destMol]).locateDelocalizedDoubleBonds$ZA(null);
});

Clazz.newMeth(C$, 'copyMoleculeByBonds$com_actelion_research_chem_ExtendedMolecule$ZA$Z$IA',  function (destMol, includeBond, recognizeDelocalizedBonds, atomMap) {
if (recognizeDelocalizedBonds) this.ensureHelperArrays$I(7);
destMol.mAtomList=null;
if (this.mIsFragment) destMol.setFragment$Z(true);
if (atomMap == null ) atomMap=Clazz.array(Integer.TYPE, [this.mAllAtoms]);
destMol.mAllAtoms=0;
for (var atom=0; atom < this.mAllAtoms; atom++) {
atomMap[atom]=-1;
for (var i=0; i < this.mConnAtoms[atom]; i++) {
if (includeBond[this.mConnBond[atom][i]]) {
atomMap[atom]=this.copyAtom$com_actelion_research_chem_Molecule$I$I$I(destMol, atom, 0, 0);
break;
}}
}
destMol.mAllBonds=0;
for (var bnd=0; bnd < this.mAllBonds; bnd++) if (includeBond[bnd]) {
this.copyBond$com_actelion_research_chem_Molecule$I$I$I$IA$Z(destMol, bnd, 0, 0, atomMap, recognizeDelocalizedBonds);
} else {
var atom1=this.mBondAtom[0][bnd];
var atom2=this.mBondAtom[1][bnd];
if (!!(atomMap[atom1] == -1 ^ atomMap[atom2] == -1)) {
if (this.mAtomCharge[atom1] != 0 && this.mAtomCharge[atom2] != 0  && (!!((this.mAtomCharge[atom1] < 0) ^ (this.mAtomCharge[atom2] < 0))) ) {
if (atomMap[atom1] != -1) destMol.mAtomCharge[atomMap[atom1]]+=(this.mAtomCharge[atom1] < 0) ? 1 : -1;
if (atomMap[atom2] != -1) destMol.mAtomCharge[atomMap[atom2]]+=(this.mAtomCharge[atom2] < 0) ? 1 : -1;
}}}
this.copyMoleculeProperties$com_actelion_research_chem_Molecule(destMol);
destMol.mValidHelperArrays=0;
destMol.renumberESRGroups$I(1);
destMol.renumberESRGroups$I(2);
if (destMol.mAllAtoms != this.mAllAtoms) destMol.setFragment$Z(true);
if (recognizeDelocalizedBonds) Clazz.new_($I$(1,1).c$$com_actelion_research_chem_ExtendedMolecule,[destMol]).locateDelocalizedDoubleBonds$ZA(null);
return atomMap;
});

Clazz.newMeth(C$, 'getAllConnAtoms$I',  function (atom) {
return this.mAllConnAtoms[atom];
});

Clazz.newMeth(C$, 'getPlainHydrogens$I',  function (atom) {
return this.getExplicitHydrogens$I(atom) + this.getImplicitHydrogens$I(atom);
});

Clazz.newMeth(C$, 'getAllHydrogens$I',  function (atom) {
return this.mAllConnAtoms[atom] - this.getNonHydrogenNeighbourCount$I(atom) + this.getImplicitHydrogens$I(atom);
});

Clazz.newMeth(C$, 'getAtoms$',  function () {
return this.mAtoms;
});

Clazz.newMeth(C$, 'getMetalBondedConnAtoms$I',  function (atom) {
return this.mConnAtom[atom].length - this.mAllConnAtoms[atom];
});

Clazz.newMeth(C$, 'getAtomPi$I',  function (atom) {
return this.mPi[atom];
});

Clazz.newMeth(C$, 'getAtomElectronegativeNeighbours$I',  function (atom) {
var e=0;
for (var i=0; i < this.mConnAtoms[atom]; i++) if (this.isElectronegative$I(this.mConnAtom[atom][i]) && !this.isBondBridge$I(this.mConnBond[atom][i]) ) ++e;

return e;
});

Clazz.newMeth(C$, 'getAtomZValue$I',  function (atom) {
var z=0;
var arom=0;
for (var i=0; i < this.mConnAtoms[atom]; i++) {
if (this.isElectronegative$I(this.mConnAtom[atom][i])) {
if (this.isDelocalizedBond$I(this.mConnBond[atom][i])) {
++z;
++arom;
} else {
z+=this.mConnBondOrder[atom][i];
}}}
return z + (arom/2|0);
});

Clazz.newMeth(C$, 'getAtomRingSize$I',  function (atom) {
return (this.mRingSet != null  && atom < this.mAtoms ) ? this.mRingSet.getAtomRingSize$I(atom) : 0;
});

Clazz.newMeth(C$, 'getBondRingSize$I',  function (bond) {
return (this.mRingSet != null  && bond < this.mBonds ) ? this.mRingSet.getBondRingSize$I(bond) : 0;
});

Clazz.newMeth(C$, 'getBonds$',  function () {
return this.mBonds;
});

Clazz.newMeth(C$, 'getBond$I$I',  function (atom1, atom2) {
for (var i=0; i < this.getAllConnAtomsPlusMetalBonds$I(atom1); i++) if (this.mConnAtom[atom1][i] == atom2) return this.mConnBond[atom1][i];

return -1;
});

Clazz.newMeth(C$, 'getCompactCopy$',  function () {
var theCopy=Clazz.new_(C$.c$$I$I,[this.mAllAtoms, this.mAllBonds]);
this.copyMolecule$com_actelion_research_chem_Molecule(theCopy);
return theCopy;
});

Clazz.newMeth(C$, 'getConnAtom$I$I',  function (atom, i) {
return this.mConnAtom[atom][i];
});

Clazz.newMeth(C$, 'getConnAtoms$I',  function (atom) {
return this.mConnAtoms[atom];
});

Clazz.newMeth(C$, 'getAllConnAtomsPlusMetalBonds$I',  function (atom) {
return this.mConnAtom[atom].length;
});

Clazz.newMeth(C$, 'getConnBond$I$I',  function (atom, i) {
return this.mConnBond[atom][i];
});

Clazz.newMeth(C$, 'getConnBondOrder$I$I',  function (atom, i) {
return this.mConnBondOrder[atom][i];
});

Clazz.newMeth(C$, 'getNonHydrogenNeighbourCount$I',  function (atom) {
var count=this.mConnAtoms[atom];
for (var i=0; i < this.mConnAtoms[atom]; i++) if (this.mAtomicNo[this.mConnAtom[atom][i]] == 1) --count;

return count;
});

Clazz.newMeth(C$, 'getExcludedNeighbourCount$I',  function (atom) {
var count=0;
for (var i=0; i < this.mConnAtoms[atom]; i++) if (Long.$ne((Long.$and(this.mAtomQueryFeatures[i],536870912)),0 )) ++count;

return count;
});

Clazz.newMeth(C$, 'getAverageBondLength$Z',  function (nonHydrogenBondsOnly) {
if (nonHydrogenBondsOnly) {
this.ensureHelperArrays$I(1);
return this.getAverageBondLength$I$I(this.mAtoms, this.mBonds);
} else {
return this.getAverageBondLength$I$I(this.mAllAtoms, this.mAllBonds);
}});

Clazz.newMeth(C$, 'getSortedConnMap$I',  function (atom) {
var connAtoms=this.mAllConnAtoms[atom];
var indexMap=Clazz.array(Integer.TYPE, [connAtoms]);
for (var i=0; i < connAtoms; i++) indexMap[i]=(this.mConnAtom[atom][i] << 16) + i;

$I$(2).sort$IA(indexMap);
for (var i=0; i < connAtoms; i++) indexMap[i]&=65535;

return indexMap;
}, p$1);

Clazz.newMeth(C$, 'getOccupiedValence$I',  function (atom) {
this.ensureHelperArrays$I(1);
var piElectronsFound=false;
var delocalizedBondFound=false;
var valence=0;
for (var i=0; i < this.mAllConnAtoms[atom]; i++) {
if (!this.mIsFragment || Long.$eq((Long.$and(this.mAtomQueryFeatures[this.mConnAtom[atom][i]],536870912)),0 ) ) {
var order=this.mConnBondOrder[atom][i];
valence+=order;
if (order > 1) piElectronsFound=true;
var bond=this.mConnBond[atom][i];
if (this.mBondType[bond] == 64) delocalizedBondFound=true;
}}
if (delocalizedBondFound && !piElectronsFound ) ++valence;
return valence;
});

Clazz.newMeth(C$, 'getFreeValence$I',  function (atom) {
return this.getMaxValence$I(atom) - this.getOccupiedValence$I(atom);
});

Clazz.newMeth(C$, 'getLowestFreeValence$I',  function (atom) {
var occupiedValence=this.getOccupiedValence$I(atom);
var correction=this.getElectronValenceCorrection$I$I(atom, occupiedValence);
var valence=this.getAtomAbnormalValence$I(atom);
if (valence == -1) {
var valenceList=$I$(3).getAllowedValences$I(this.mAtomicNo[atom]);
var i=0;
while ((occupiedValence > valenceList[i] + correction) && (i < valenceList.length - 1) )++i;

valence=valenceList[i];
}return valence + correction - occupiedValence;
});

Clazz.newMeth(C$, 'getImplicitHigherValence$I$Z',  function (atom, neglectExplicitHydrogen) {
var occupiedValence=this.getOccupiedValence$I(atom);
occupiedValence-=this.getElectronValenceCorrection$I$I(atom, occupiedValence);
if (neglectExplicitHydrogen) occupiedValence-=this.mAllConnAtoms[atom] - this.mConnAtoms[atom];
var valences=$I$(3).getAllowedValences$I(this.mAtomicNo[atom]);
if (occupiedValence <= valences[0]) return -1;
for (var i=1; i < valences.length; i++) if (valences[i] >= occupiedValence) return valences[i];

return occupiedValence;
});

Clazz.newMeth(C$, 'getAverageTopologicalAtomDistance$',  function () {
this.ensureHelperArrays$I(1);
var meanDistance=Clazz.array(Float.TYPE, [this.mAtoms]);
var graphAtom=Clazz.array(Integer.TYPE, [this.mAtoms]);
for (var startAtom=0; startAtom < this.mAtoms; startAtom++) {
graphAtom[0]=startAtom;
var graphLevel=Clazz.array(Integer.TYPE, [this.mAtoms]);
graphLevel[startAtom]=1;
var current=0;
var highest=0;
while (current <= highest){
for (var i=0; i < this.mConnAtoms[graphAtom[current]]; i++) {
var candidate=this.mConnAtom[graphAtom[current]][i];
if (graphLevel[candidate] == 0) {
graphLevel[candidate]=graphLevel[graphAtom[current]] + 1;
graphAtom[++highest]=candidate;
meanDistance[startAtom]+=(graphLevel[candidate] - 1);
}}
++current;
}
meanDistance[startAtom]/=highest;
}
return meanDistance;
});

Clazz.newMeth(C$, 'getPathLength$I$I',  function (atom1, atom2) {
if (atom1 == atom2) return 0;
this.ensureHelperArrays$I(1);
var graphLevel=Clazz.array(Integer.TYPE, [this.mAllAtoms]);
var graphAtom=Clazz.array(Integer.TYPE, [this.mAllAtoms]);
graphAtom[0]=atom1;
graphLevel[atom1]=1;
var current=0;
var highest=0;
while (current <= highest){
for (var i=0; i < this.mAllConnAtoms[graphAtom[current]]; i++) {
var candidate=this.mConnAtom[graphAtom[current]][i];
if (candidate == atom2) return graphLevel[graphAtom[current]];
if (graphLevel[candidate] == 0) {
graphAtom[++highest]=candidate;
graphLevel[candidate]=graphLevel[graphAtom[current]] + 1;
}}
++current;
}
return -1;
});

Clazz.newMeth(C$, 'getPathLength$I$I$I$ZA',  function (atom1, atom2, maxLength, neglectAtom) {
if (atom1 == atom2) return 0;
this.ensureHelperArrays$I(1);
var graphLevel=Clazz.array(Integer.TYPE, [this.mAllAtoms]);
var graphAtom=Clazz.array(Integer.TYPE, [this.mAllAtoms]);
graphAtom[0]=atom1;
graphLevel[atom1]=1;
var current=0;
var highest=0;
while (current <= highest && graphLevel[graphAtom[current]] <= maxLength ){
for (var i=0; i < this.mAllConnAtoms[graphAtom[current]]; i++) {
var candidate=this.mConnAtom[graphAtom[current]][i];
if (candidate == atom2) return graphLevel[graphAtom[current]];
if (graphLevel[candidate] == 0 && (neglectAtom == null  || neglectAtom.length <= candidate  || !neglectAtom[candidate] ) ) {
graphAtom[++highest]=candidate;
graphLevel[candidate]=graphLevel[graphAtom[current]] + 1;
}}
++current;
}
return -1;
});

Clazz.newMeth(C$, 'getPath$IA$I$I$I$ZA',  function (pathAtom, atom1, atom2, maxLength, neglectBond) {
return this.getPath$IA$I$I$I$ZA$ZA(pathAtom, atom1, atom2, maxLength, null, neglectBond);
});

Clazz.newMeth(C$, 'getPath$IA$I$I$I$ZA$ZA',  function (pathAtom, atom1, atom2, maxLength, neglectAtom, neglectBond) {
if (atom1 == atom2) {
pathAtom[0]=atom1;
return 0;
}this.ensureHelperArrays$I(1);
var graphLevel=Clazz.array(Integer.TYPE, [this.mAllAtoms]);
var graphAtom=Clazz.array(Integer.TYPE, [this.mAllAtoms]);
var parentAtom=Clazz.array(Integer.TYPE, [this.mAllAtoms]);
graphAtom[0]=atom1;
graphLevel[atom1]=1;
var current=0;
var highest=0;
while (current <= highest && graphLevel[graphAtom[current]] <= maxLength ){
var parent=graphAtom[current];
for (var i=0; i < this.mAllConnAtoms[parent]; i++) {
if (neglectBond == null  || neglectBond.length <= this.mConnBond[parent][i]  || !neglectBond[this.mConnBond[parent][i]] ) {
var candidate=this.mConnAtom[parent][i];
if (candidate == atom2) {
var index=graphLevel[parent];
pathAtom[index]=candidate;
pathAtom[--index]=parent;
while (index > 0){
pathAtom[index - 1]=parentAtom[pathAtom[index]];
--index;
}
return graphLevel[parent];
}if (graphLevel[candidate] == 0 && (neglectAtom == null  || neglectAtom.length <= candidate  || !neglectAtom[candidate] ) ) {
graphAtom[++highest]=candidate;
graphLevel[candidate]=graphLevel[parent] + 1;
parentAtom[candidate]=parent;
}}}
++current;
}
return -1;
});

Clazz.newMeth(C$, 'getPathBonds$IA$IA$I',  function (pathAtom, pathBond, pathLength) {
this.ensureHelperArrays$I(1);
for (var i=0; i < pathLength; i++) {
for (var j=0; j < this.mAllConnAtoms[pathAtom[i]]; j++) {
if (this.mConnAtom[pathAtom[i]][j] == pathAtom[i + 1]) {
pathBond[i]=this.mConnBond[pathAtom[i]][j];
break;
}}
}
});

Clazz.newMeth(C$, 'shareSameFragment$I$I',  function (atom1, atom2) {
return (this.getPathLength$I$I(atom1, atom2) != -1);
});

Clazz.newMeth(C$, 'addFragment$com_actelion_research_chem_ExtendedMolecule$I$IA',  function (sourceMol, rootAtom, atomMap) {
sourceMol.ensureHelperArrays$I(1);
if (atomMap == null ) atomMap=Clazz.array(Integer.TYPE, [sourceMol.mAllAtoms]);
var esrGroupCountAND=this.renumberESRGroups$I(1);
var esrGroupCountOR=this.renumberESRGroups$I(2);
var isFragmentMember=Clazz.array(Boolean.TYPE, [sourceMol.mAllAtoms]);
var graphAtom=Clazz.array(Integer.TYPE, [sourceMol.mAllAtoms]);
graphAtom[0]=rootAtom;
isFragmentMember[rootAtom]=true;
atomMap[rootAtom]=sourceMol.copyAtom$com_actelion_research_chem_Molecule$I$I$I(this, rootAtom, esrGroupCountAND, esrGroupCountOR);
var current=0;
var highest=0;
while (current <= highest){
for (var i=0; i < sourceMol.getAllConnAtoms$I(graphAtom[current]); i++) {
var candidate=sourceMol.mConnAtom[graphAtom[current]][i];
if (!isFragmentMember[candidate]) {
graphAtom[++highest]=candidate;
isFragmentMember[candidate]=true;
atomMap[candidate]=sourceMol.copyAtom$com_actelion_research_chem_Molecule$I$I$I(this, candidate, esrGroupCountAND, esrGroupCountOR);
}}
++current;
}
for (var bond=0; bond < sourceMol.mAllBonds; bond++) if (isFragmentMember[sourceMol.mBondAtom[0][bond]]) sourceMol.copyBond$com_actelion_research_chem_Molecule$I$I$I$IA$Z(this, bond, esrGroupCountAND, esrGroupCountOR, atomMap, false);

this.renumberESRGroups$I(1);
this.renumberESRGroups$I(2);
this.mValidHelperArrays=0;
});

Clazz.newMeth(C$, 'getFragmentAtoms$I',  function (rootAtom) {
return this.getFragmentAtoms$I$Z(rootAtom, false);
});

Clazz.newMeth(C$, 'getFragmentAtoms$I$Z',  function (rootAtom, considerMetalBonds) {
this.ensureHelperArrays$I(1);
var isFragmentMember=Clazz.array(Boolean.TYPE, [this.mAllAtoms]);
var graphAtom=Clazz.array(Integer.TYPE, [this.mAllAtoms]);
graphAtom[0]=rootAtom;
isFragmentMember[rootAtom]=true;
var current=0;
var highest=0;
var fragmentMembers=1;
while (current <= highest){
var connAtoms=considerMetalBonds ? this.getAllConnAtomsPlusMetalBonds$I(graphAtom[current]) : this.mAllConnAtoms[graphAtom[current]];
for (var i=0; i < connAtoms; i++) {
var candidate=this.mConnAtom[graphAtom[current]][i];
if (!isFragmentMember[candidate]) {
graphAtom[++highest]=candidate;
isFragmentMember[candidate]=true;
++fragmentMembers;
}}
++current;
}
var fragmentMember=Clazz.array(Integer.TYPE, [fragmentMembers]);
fragmentMembers=0;
for (var atom=0; atom < this.mAllAtoms; atom++) if (isFragmentMember[atom]) fragmentMember[fragmentMembers++]=atom;

return fragmentMember;
});

Clazz.newMeth(C$, 'getFragmentNumbers$IA$ZA$Z',  function (fragmentNo, neglectBond, considerMetalBonds) {
this.ensureHelperArrays$I(1);
for (var atom=0; atom < this.mAllAtoms; atom++) fragmentNo[atom]=-1;

var fragments=0;
for (var atom=0; atom < this.mAllAtoms; atom++) {
if (fragmentNo[atom] == -1) {
fragmentNo[atom]=fragments;
var graphAtom=Clazz.array(Integer.TYPE, [this.mAllAtoms]);
graphAtom[0]=atom;
var current=0;
var highest=0;
while (current <= highest){
var connAtoms=considerMetalBonds ? this.getAllConnAtomsPlusMetalBonds$I(graphAtom[current]) : this.mAllConnAtoms[graphAtom[current]];
for (var i=0; i < connAtoms; i++) {
var candidate=this.mConnAtom[graphAtom[current]][i];
if (fragmentNo[candidate] == -1 && !neglectBond[this.mConnBond[graphAtom[current]][i]] ) {
graphAtom[++highest]=candidate;
fragmentNo[candidate]=fragments;
}}
++current;
}
++fragments;
}}
return fragments;
});

Clazz.newMeth(C$, 'getFragmentNumbers$IA$Z$Z',  function (fragmentNo, markedAtomsOnly, considerMetalBonds) {
this.ensureHelperArrays$I(1);
for (var atom=0; atom < this.mAllAtoms; atom++) fragmentNo[atom]=-1;

var fragments=0;
for (var atom=0; atom < this.mAllAtoms; atom++) {
if (fragmentNo[atom] == -1 && (!markedAtomsOnly || this.isMarkedAtom$I(atom) ) ) {
fragmentNo[atom]=fragments;
var graphAtom=Clazz.array(Integer.TYPE, [this.mAllAtoms]);
graphAtom[0]=atom;
var current=0;
var highest=0;
while (current <= highest){
var connAtoms=considerMetalBonds ? this.getAllConnAtomsPlusMetalBonds$I(graphAtom[current]) : this.mAllConnAtoms[graphAtom[current]];
for (var i=0; i < connAtoms; i++) {
var candidate=this.mConnAtom[graphAtom[current]][i];
if (fragmentNo[candidate] == -1 && (!markedAtomsOnly || this.isMarkedAtom$I(candidate) ) ) {
graphAtom[++highest]=candidate;
fragmentNo[candidate]=fragments;
}}
++current;
}
++fragments;
}}
return fragments;
});

Clazz.newMeth(C$, 'stripSmallFragments$',  function () {
return this.stripSmallFragments$Z(false);
});

Clazz.newMeth(C$, 'stripSmallFragments$Z',  function (considerMetalBonds) {
var fragmentNo=Clazz.array(Integer.TYPE, [this.mAllAtoms]);
var fragmentCount=this.getFragmentNumbers$IA$Z$Z(fragmentNo, false, considerMetalBonds);
if (fragmentCount <= 1) return null;
var fragmentSize=Clazz.array(Integer.TYPE, [fragmentCount]);
for (var atom=0; atom < this.mAtoms; atom++) ++fragmentSize[fragmentNo[atom]];

var largestFragment=0;
var largestSize=fragmentSize[0];
for (var i=1; i < fragmentCount; i++) {
if (largestSize < fragmentSize[i]) {
largestSize=fragmentSize[i];
largestFragment=i;
}}
for (var atom=0; atom < this.mAllAtoms; atom++) if (fragmentNo[atom] != largestFragment) this.mAtomicNo[atom]=-1;

for (var bond=0; bond < this.mAllBonds; bond++) if ((!considerMetalBonds && this.mBondType[bond] == 32 ) || fragmentNo[this.mBondAtom[0][bond]] != largestFragment ) this.mBondType[bond]=512;

var atomMap=this.compressMolTable$();
this.mValidHelperArrays=0;
try {
this.canonizeCharge$Z$Z(true, true);
} catch (e) {
if (Clazz.exceptionOf(e,"Exception")){
} else {
throw e;
}
}
return atomMap;
});

Clazz.newMeth(C$, 'findRingSystem$I$Z$ZA$ZA',  function (startAtom, aromaticOnly, isMemberAtom, isMemberBond) {
this.ensureHelperArrays$I(7);
if (!this.isRingAtom$I(startAtom) || (aromaticOnly && !this.isAromaticAtom$I(startAtom) ) ) return;
var graphAtom=Clazz.array(Integer.TYPE, [this.mAtoms]);
graphAtom[0]=startAtom;
isMemberAtom[startAtom]=true;
var current=0;
var highest=0;
while (current <= highest){
for (var i=0; i < this.mConnAtoms[graphAtom[current]]; i++) {
var candidateBond=this.mConnBond[graphAtom[current]][i];
if (!isMemberBond[candidateBond] && this.isRingBond$I(candidateBond) && (!aromaticOnly || this.isAromaticBond$I(candidateBond) )  ) {
isMemberBond[candidateBond]=true;
var candidateAtom=this.mConnAtom[graphAtom[current]][i];
if (!isMemberAtom[candidateAtom]) {
isMemberAtom[candidateAtom]=true;
graphAtom[++highest]=candidateAtom;
}}}
++current;
}
});

Clazz.newMeth(C$, 'getSubstituent$I$I$ZA$com_actelion_research_chem_ExtendedMolecule$IA',  function (coreAtom, firstAtom, isMemberAtom, substituent, atomMap) {
this.ensureHelperArrays$I(1);
if (substituent != null ) {
substituent.clear$();
substituent.mIsFragment=false;
}var graphAtom=Clazz.array(Integer.TYPE, [this.mAllAtoms]);
if (isMemberAtom == null ) isMemberAtom=Clazz.array(Boolean.TYPE, [this.mAllAtoms]);
 else $I$(2).fill$ZA$Z(isMemberAtom, false);
graphAtom[0]=coreAtom;
graphAtom[1]=firstAtom;
isMemberAtom[coreAtom]=true;
isMemberAtom[firstAtom]=true;
var current=1;
var highest=1;
while (current <= highest){
for (var i=0; i < this.mAllConnAtoms[graphAtom[current]]; i++) {
var candidate=this.mConnAtom[graphAtom[current]][i];
if (candidate == coreAtom) {
if (current != 1) return -1;
}if (!isMemberAtom[candidate]) {
isMemberAtom[candidate]=true;
graphAtom[++highest]=candidate;
}}
++current;
}
if (substituent != null ) {
if (atomMap == null ) atomMap=Clazz.array(Integer.TYPE, [isMemberAtom.length]);
this.copyMoleculeByAtoms$com_actelion_research_chem_ExtendedMolecule$ZA$Z$IA(substituent, isMemberAtom, false, atomMap);
substituent.changeAtom$I$I$I$I$I(atomMap[coreAtom], 0, 0, -1, 0);
}isMemberAtom[coreAtom]=false;
return highest;
});

Clazz.newMeth(C$, 'getSubstituentSize$I$I',  function (coreAtom, firstAtom) {
this.ensureHelperArrays$I(1);
var graphAtom=Clazz.array(Integer.TYPE, [this.mAtoms]);
var isMember=Clazz.array(Boolean.TYPE, [this.mAtoms]);
graphAtom[0]=coreAtom;
graphAtom[1]=firstAtom;
isMember[coreAtom]=true;
isMember[firstAtom]=true;
var current=1;
var highest=1;
while (current <= highest){
for (var i=0; i < this.mConnAtoms[graphAtom[current]]; i++) {
var candidate=this.mConnAtom[graphAtom[current]][i];
if (candidate == coreAtom) {
if (current != 1) return -1;
}if (!isMember[candidate]) {
isMember[candidate]=true;
graphAtom[++highest]=candidate;
}}
++current;
}
return highest;
});

Clazz.newMeth(C$, 'supportsImplicitHydrogen$I',  function (atom) {
if ((this.mAtomFlags[atom] & 2013265920) != 0) return true;
if (this.mAtomicNo[atom] == 1) return false;
return this.isOrganicAtom$I(atom) || this.mAtomicNo[atom] == 13  || this.mAtomicNo[atom] >= 171 ;
});

Clazz.newMeth(C$, 'getImplicitHydrogens$I',  function (atom) {
if (this.mIsFragment && Long.$eq((Long.$and(this.mAtomQueryFeatures[atom],2048)),0 ) ) return 0;
if (!this.supportsImplicitHydrogen$I(atom)) return 0;
this.ensureHelperArrays$I(1);
var occupiedValence=0;
for (var i=0; i < this.mAllConnAtoms[atom]; i++) occupiedValence+=this.mConnBondOrder[atom][i];

if (this.mIsFragment) {
var delocalizedBonds=1;
for (var i=0; i < this.mConnAtoms[atom]; i++) if (this.mBondType[this.mConnBond[atom][i]] == 64) ++delocalizedBonds;

occupiedValence+=delocalizedBonds >> 1;
}occupiedValence-=this.getElectronValenceCorrection$I$I(atom, occupiedValence);
var maxValence=this.getAtomAbnormalValence$I(atom);
if (maxValence == -1) {
var valenceList=$I$(3).getAllowedValences$I(this.mAtomicNo[atom]);
maxValence=valenceList[0];
for (var i=1; (maxValence < occupiedValence) && (i < valenceList.length) ; i++) maxValence=valenceList[i];

}return Math.max(0, maxValence - occupiedValence);
});

Clazz.newMeth(C$, 'getExplicitHydrogens$I',  function (atom) {
return this.mAllConnAtoms[atom] - this.mConnAtoms[atom];
});

Clazz.newMeth(C$, 'getMolweight$',  function () {
this.ensureHelperArrays$I(1);
var molweight=0;
for (var atom=0; atom < this.mAllAtoms; atom++) {
var mass=this.mAtomMass[atom] != 0 ? this.mAtomMass[atom] : $I$(3).cRoundedMass[this.mAtomicNo[atom]];
molweight+=mass + this.getImplicitHydrogens$I(atom) * $I$(3).cRoundedMass[1];
if (this.mAtomicNo[atom] >= 171 && this.mAtomicNo[atom] <= 190 ) {
var connAtoms=this.mAllConnAtoms[atom];
if (connAtoms > 2) molweight-=(connAtoms - 2) * $I$(3).cRoundedMass[1];
}}
return molweight;
});

Clazz.newMeth(C$, 'getRotatableBondCount$',  function () {
var rCount=0;
this.ensureHelperArrays$I(7);
for (var bond=0; bond < this.mBonds; bond++) {
if (this.getBondOrder$I(bond) == 1 && !this.isRingBond$I(bond) ) {
var isRotatable=true;
for (var i=0; i < 2; i++) {
var atom1=this.mBondAtom[i][bond];
if (this.mConnAtoms[atom1] == 1) {
isRotatable=false;
break;
}if (this.mAtomicNo[atom1] == 7 && !this.isAromaticAtom$I(atom1) ) {
var atom2=this.mBondAtom[1 - i][bond];
for (var j=0; j < this.mConnAtoms[atom2]; j++) {
var connAtom=this.mConnAtom[atom2][j];
var connBond=this.mConnBond[atom2][j];
if (connBond != bond && this.getBondOrder$I(connBond) > 1  && !this.isAromaticAtom$I(connAtom)  && this.isElectronegative$I(connAtom) ) {
isRotatable=false;
break;
}}
}}
if (isRotatable && !this.isPseudoRotatableBond$I(bond) ) ++rCount;
}}
return rCount;
});

Clazz.newMeth(C$, 'isPseudoRotatableBond$I',  function (bond) {
if (this.getBondOrder$I(bond) != 1) return false;
for (var i=0; i < 2; i++) {
var atom=this.mBondAtom[i][bond];
var rearAtom=this.mBondAtom[1 - i][bond];
while (this.mPi[atom] == 2 && this.mConnAtoms[atom] == 2  && this.mAtomicNo[atom] < 10 ){
for (var j=0; j < 2; j++) {
var connAtom=this.mConnAtom[atom][j];
if (connAtom != rearAtom) {
if (this.mConnAtoms[connAtom] == 1) return true;
var connBond=this.mConnBond[atom][j];
if (this.getBondOrder$I(connBond) == 1 && connBond < bond ) return true;
rearAtom=atom;
atom=connAtom;
break;
}}
}
if (this.mConnAtoms[atom] == 1) return true;
}
return false;
});

Clazz.newMeth(C$, 'getAromaticRingCount$',  function () {
this.ensureHelperArrays$I(7);
var count=0;
for (var i=0; i < this.mRingSet.getSize$(); i++) if (this.mRingSet.isAromatic$I(i)) ++count;

return count;
});

Clazz.newMeth(C$, 'getAtomRingCount$I$I',  function (atom, maxRingSize) {
this.ensureHelperArrays$I(7);
var bondTouched=Clazz.array(Boolean.TYPE, [this.mBonds]);
var neglectBond=Clazz.array(Boolean.TYPE, [this.mBonds]);
var ringAtom=Clazz.array(Integer.TYPE, [this.mAtoms]);
var count=0;
for (var i=1; i < this.mConnAtoms[atom]; i++) {
var bond1=this.mConnBond[atom][i];
if (this.isRingBond$I(bond1)) {
for (var j=0; j < i; j++) {
var bond2=this.mConnBond[atom][j];
if (this.isRingBond$I(bond2)) {
neglectBond[bond1]=true;
neglectBond[bond2]=true;
var pathLength=this.getPath$IA$I$I$I$ZA(ringAtom, this.mConnAtom[atom][i], this.mConnAtom[atom][j], maxRingSize - 2, neglectBond);
neglectBond[bond1]=false;
neglectBond[bond2]=false;
if (pathLength != -1) {
var isIndependentRing=false;
var pathBond=Clazz.array(Integer.TYPE, [pathLength]);
this.getPathBonds$IA$IA$I(ringAtom, pathBond, pathLength);
for (var k=0; k < pathLength; k++) {
if (!bondTouched[pathBond[k]]) {
bondTouched[pathBond[k]]=true;
isIndependentRing=true;
}}
if (isIndependentRing) ++count;
}}}
}}
return count;
});

Clazz.newMeth(C$, 'getRingSet$',  function () {
this.ensureHelperArrays$I(7);
return this.mRingSet;
});

Clazz.newMeth(C$, 'getRingSetSimple$',  function () {
this.ensureHelperArrays$I(3);
return this.mRingSet;
});

Clazz.newMeth(C$, 'getAtomPreferredStereoBond$I',  function (atom) {
this.ensureHelperArrays$I(7);
if (this.mPi[atom] == 2 && this.mConnAtoms[atom] == 2 ) return p$1.preferredAlleneStereoBond$I$Z.apply(this, [atom, false]);
 else return p$1.preferredTHStereoBond$I$Z.apply(this, [atom, false]);
});

Clazz.newMeth(C$, 'getBondPreferredStereoBond$I',  function (bond) {
return p$1.preferredBinapStereoBond$I.apply(this, [bond]);
});

Clazz.newMeth(C$, 'getStereoBondScore$I$I',  function (bond, atom) {
if (this.getBondOrder$I(bond) != 1) return 0;
return 16 - this.mAllConnAtoms[atom] + (((this.mBondType[bond] & 384) == 0 || this.mBondAtom[0][bond] != atom ) ? 32768 : 0) + ((this.mAtomicNo[atom] == 1) ? 4096 : 0) + ((this.mAllConnAtoms[atom] == 1) ? 2048 : 0) + ((this.getAtomParity$I(atom) == 0) ? 1024 : 0) + ((!this.isRingBond$I(bond)) ? 512 : 0) + ((this.mAtomicNo[atom] != 6) ? 256 : 0);
}, p$1);

Clazz.newMeth(C$, 'isAllylicAtom$I',  function (atom) {
return (this.mAtomFlags[atom] & 4096) != 0;
});

Clazz.newMeth(C$, 'isAromaticAtom$I',  function (atom) {
return (atom < this.mAtoms) ? this.mRingSet.isAromaticAtom$I(atom) : false;
});

Clazz.newMeth(C$, 'isHeteroAromaticAtom$I',  function (atom) {
return (atom < this.mAtoms) ? this.mRingSet.isHeteroAromaticAtom$I(atom) : false;
});

Clazz.newMeth(C$, 'isDelocalizedAtom$I',  function (atom) {
return (atom < this.mAtoms) ? this.mRingSet.isDelocalizedAtom$I(atom) : false;
});

Clazz.newMeth(C$, 'isAromaticBond$I',  function (bond) {
return (bond < this.mBonds) ? this.mRingSet.isAromaticBond$I(bond) : false;
});

Clazz.newMeth(C$, 'isHeteroAromaticBond$I',  function (bond) {
return (bond < this.mBonds) ? this.mRingSet.isHeteroAromaticBond$I(bond) : false;
});

Clazz.newMeth(C$, 'isDelocalizedBond$I',  function (bond) {
return (bond < this.mBonds) ? this.mRingSet.isDelocalizedBond$I(bond) || this.mBondType[bond] == 64  : false;
});

Clazz.newMeth(C$, 'isRingAtom$I',  function (atom) {
return (this.mAtomFlags[atom] & 3072) != 0;
});

Clazz.newMeth(C$, 'isRingBond$I',  function (bnd) {
return (this.mBondFlags[bnd] & 64) != 0;
});

Clazz.newMeth(C$, 'isSmallRingAtom$I',  function (atom) {
return (this.mAtomFlags[atom] & 8) != 0;
});

Clazz.newMeth(C$, 'isSmallRingBond$I',  function (bond) {
return (this.mBondFlags[bond] & 128) != 0;
});

Clazz.newMeth(C$, 'isStabilizedAtom$I',  function (atom) {
return (this.mAtomFlags[atom] & 8192) != 0;
});

Clazz.newMeth(C$, 'getAtomRingBondCount$I',  function (atom) {
var flags=(this.mAtomFlags[atom] & 3072);
return (flags == 0) ? 0 : (flags == 1024) ? 2 : (flags == 2048) ? 3 : 4;
});

Clazz.newMeth(C$, 'getChiralText$',  function () {
return null;
});

Clazz.newMeth(C$, 'getStereoBond$I',  function (atom) {
this.ensureHelperArrays$I(1);
if (this.mConnAtoms[atom] == 2 && this.mConnBondOrder[atom][0] == 2  && this.mConnBondOrder[atom][1] == 2 ) {
for (var i=0; i < 2; i++) for (var j=0; j < this.mAllConnAtoms[this.mConnAtom[atom][i]]; j++) if (this.isStereoBond$I$I(this.mConnBond[this.mConnAtom[atom][i]][j], this.mConnAtom[atom][i])) return this.mConnBond[this.mConnAtom[atom][i]][j];


} else {
for (var i=0; i < this.mAllConnAtoms[atom]; i++) if (this.isStereoBond$I$I(this.mConnBond[atom][i], atom)) return this.mConnBond[atom][i];

}return -1;
});

Clazz.newMeth(C$, 'setParitiesValid$I',  function (helperStereoBits) {
this.mValidHelperArrays|=(248 & (8 | helperStereoBits));
});

Clazz.newMeth(C$, 'setStereoBondsFromParity$',  function () {
Clazz.assert(C$, this, function(){return ((this.mValidHelperArrays & 8) != 0)});
this.ensureHelperArrays$I(7);
for (var bond=0; bond < this.mBonds; bond++) if (this.isStereoBond$I(bond)) this.mBondType[bond]=1;

for (var atom=0; atom < this.mAtoms; atom++) this.setStereoBondFromAtomParity$I(atom);

for (var bond=0; bond < this.mBonds; bond++) this.setStereoBondFromBondParity$I(bond);

for (var bond=0; bond < this.mBonds; bond++) if (this.mBondType[bond] == 2 && this.getBondParity$I(bond) == 3 ) this.mBondType[bond]=386;

Clazz.assert(C$, this, function(){return ((this.mValidHelperArrays & 8) != 0)});
});

Clazz.newMeth(C$, 'convertStereoBondsToSingleBonds$I',  function (atom) {
if (this.mPi[atom] == 2 && this.mConnAtoms[atom] == 2  && this.mConnBondOrder[atom][0] == 2 ) {
for (var i=0; i < 2; i++) {
var alleneEnd=this.findAlleneEndAtom$I$I(atom, this.mConnAtom[atom][i]);
if (alleneEnd != -1) {
for (var j=0; j < this.mConnAtoms[alleneEnd]; j++) {
var connBond=this.mConnBond[alleneEnd][j];
if (this.isStereoBond$I(connBond) && this.mBondAtom[0][connBond] == alleneEnd ) this.mBondType[this.mConnBond[alleneEnd][j]]=1;
}
}}
return;
}if (this.mPi[atom] == 0 || this.mAtomicNo[atom] >= 15 ) {
for (var i=0; i < this.mAllConnAtoms[atom]; i++) {
var connBond=this.mConnBond[atom][i];
if (this.isStereoBond$I$I(connBond, atom) && this.mBondAtom[0][connBond] == atom ) this.mBondType[connBond]=1;
}
}});

Clazz.newMeth(C$, 'setStereoBondFromAtomParity$I',  function (atom) {
this.convertStereoBondsToSingleBonds$I(atom);
if (this.getAtomParity$I(atom) == 0 || this.getAtomParity$I(atom) == 3 ) return;
if (this.mPi[atom] == 2 && this.mConnAtoms[atom] == 2 ) {
p$1.setAlleneStereoBondFromParity$I.apply(this, [atom]);
return;
}if (this.mConnAtoms[atom] < 3 || this.mConnAtoms[atom] > 4 ) {
this.setAtomParity$I$I$Z(atom, 0, false);
return;
}var allConnAtoms=this.mAllConnAtoms[atom];
var singleBondFound=false;
for (var i=0; i < allConnAtoms; i++) {
if (this.getBondOrder$I(this.mConnBond[atom][i]) == 1) {
singleBondFound=true;
break;
}}
if (!singleBondFound) return;
var sortedConnMap=p$1.getSortedConnMap$I.apply(this, [atom]);
var angle=Clazz.array(Double.TYPE, [allConnAtoms]);
for (var i=0; i < allConnAtoms; i++) angle[i]=this.getBondAngle$I$I(this.mConnAtom[atom][sortedConnMap[i]], atom);

for (var i=0; i < allConnAtoms; i++) if (this.mBondAtom[0][this.mConnBond[atom][i]] == atom && this.getBondOrder$I(this.mConnBond[atom][i]) == 1 ) this.mBondType[this.mConnBond[atom][i]]=1;

if (this.getAtomRingSize$I(atom) <= 24.0  && p$1.setFisherProjectionStereoBondsFromParity$I$IA$DA.apply(this, [atom, sortedConnMap, angle]) ) return;
var preferredBond=p$1.preferredTHStereoBond$I$Z.apply(this, [atom, true]);
if (this.mBondAtom[0][preferredBond] != atom) {
this.mBondAtom[1][preferredBond]=this.mBondAtom[0][preferredBond];
this.mBondAtom[0][preferredBond]=atom;
}var preferredBondIndex=-1;
for (var i=0; i < allConnAtoms; i++) {
if (preferredBond == this.mConnBond[atom][sortedConnMap[i]]) {
preferredBondIndex=i;
break;
}}
var up_down=Clazz.array(Integer.TYPE, -2, [Clazz.array(Integer.TYPE, -1, [2, 1, 2, 1]), Clazz.array(Integer.TYPE, -1, [1, 2, 2, 1]), Clazz.array(Integer.TYPE, -1, [1, 1, 2, 2]), Clazz.array(Integer.TYPE, -1, [2, 1, 1, 2]), Clazz.array(Integer.TYPE, -1, [2, 2, 1, 1]), Clazz.array(Integer.TYPE, -1, [1, 2, 1, 2])]);
for (var i=1; i < allConnAtoms; i++) if (angle[i] < angle[0] ) angle[i]+=6.283185307179586;

var bondType;
if (allConnAtoms == 3) {
var inverted=false;
switch (preferredBondIndex) {
case 0:
inverted=(((angle[1] < angle[2] ) && (angle[2] - angle[1] < 3.141592653589793 ) ) || ((angle[1] > angle[2] ) && (angle[1] - angle[2] > 3.141592653589793 ) ) );
break;
case 1:
inverted=(angle[2] - angle[0] > 3.141592653589793 );
break;
case 2:
inverted=(angle[1] - angle[0] < 3.141592653589793 );
break;
}
bondType=(!!((this.getAtomParity$I(atom) == 1) ^ inverted)) ? 257 : 129;
} else {
var order=0;
if (angle[1] <= angle[2]  && angle[2] <= angle[3]  ) order=0;
 else if (angle[1] <= angle[3]  && angle[3] <= angle[2]  ) order=1;
 else if (angle[2] <= angle[1]  && angle[1] <= angle[3]  ) order=2;
 else if (angle[2] <= angle[3]  && angle[3] <= angle[1]  ) order=3;
 else if (angle[3] <= angle[1]  && angle[1] <= angle[2]  ) order=4;
 else if (angle[3] <= angle[2]  && angle[2] <= angle[1]  ) order=5;
bondType=(!!((this.getAtomParity$I(atom) == 1) ^ (up_down[order][preferredBondIndex] == 1))) ? 129 : 257;
}this.mBondType[preferredBond]=bondType;
});

Clazz.newMeth(C$, 'setFisherProjectionStereoBondsFromParity$I$IA$DA',  function (atom, sortedConnMap, angle) {
var allConnAtoms=this.mAllConnAtoms[atom];
var direction=Clazz.array(Integer.TYPE, [allConnAtoms]);
var parity=this.getFisherProjectionParity$I$IA$DA$IA(atom, sortedConnMap, angle, direction);
if (parity == 3) return false;
var bondType=(this.getAtomParity$I(atom) == parity) ? 257 : 129;
for (var i=0; i < allConnAtoms; i++) {
if ((direction[i] & 1) == 1) {
var bond=this.mConnBond[atom][sortedConnMap[i]];
this.mBondType[bond]=bondType;
if (this.mBondAtom[0][bond] != atom) {
this.mBondAtom[1][bond]=this.mBondAtom[0][bond];
this.mBondAtom[0][bond]=atom;
}}}
return true;
}, p$1);

Clazz.newMeth(C$, 'getFisherProjectionParity$I$IA$DA$IA',  function (atom, sortedConnMap, angle, direction) {
if (this.getAtomRingSize$I(atom) > 24.0 ) return 3;
var allConnAtoms=this.mAllConnAtoms[atom];
if (direction == null ) direction=Clazz.array(Integer.TYPE, [allConnAtoms]);
if (!p$1.getFisherProjectionBondDirections$I$IA$DA$IA.apply(this, [atom, sortedConnMap, angle, direction])) return 3;
var horizontalBondType=-1;
for (var i=0; i < allConnAtoms; i++) {
if ((direction[i] & 1) == 1) {
var bondType=this.mBondType[this.mConnBond[atom][sortedConnMap[i]]];
if (horizontalBondType != -1 && horizontalBondType != bondType ) return 3;
horizontalBondType=bondType;
}}
var index=(Math.abs(direction[0] - direction[1]) == 2) ? 1 : 0;
var dif=direction[index] - direction[index + 1];
var isClockwise=!!((Math.abs(dif) == 3) ^ (direction[index] < direction[index + 1]));
var is4thConnHorizontal=(allConnAtoms == 3 || ((direction[3] & 1) == 1) );
return !!((!!(isClockwise ^ is4thConnHorizontal)) ^ (horizontalBondType == 129)) ? 1 : 2;
});

Clazz.newMeth(C$, 'getFisherProjectionBondDirections$I$IA$DA$IA',  function (atom, sortedConnMap, angle, direction) {
var allConnAtoms=this.mAllConnAtoms[atom];
if (this.mPi[atom] != 0 || this.isAromaticAtom$I(atom)  || this.mConnAtoms[atom] < 3  || allConnAtoms > 4 ) return false;
var isUsed=Clazz.array(Boolean.TYPE, [4]);
for (var i=0; i < allConnAtoms; i++) {
var a=3.9269908169872414 - angle[i];
if (Math.abs(0.7853981633974483 - (a % (1.5707963267948966))) > 0.08726647 ) return false;
direction[i]=3 & ((a / (1.5707963267948966))|0);
if (isUsed[direction[i]]) return false;
isUsed[direction[i]]=true;
if ((direction[i] & 1) == 0) {
if (this.mBondType[this.mConnBond[atom][sortedConnMap[i]]] != 1) return false;
} else {
if (!this.isStereoBond$I$I(this.mConnBond[atom][sortedConnMap[i]], atom)) return false;
}}
return isUsed[0] && isUsed[2] ;
}, p$1);

Clazz.newMeth(C$, 'setAlleneStereoBondFromParity$I',  function (atom) {
if (this.mConnAtoms[atom] != 2 || this.mConnBondOrder[atom][0] != 2  || this.mConnBondOrder[atom][1] != 2  || this.mConnAtoms[this.mConnAtom[atom][0]] < 2  || this.mConnAtoms[this.mConnAtom[atom][1]] < 2  || this.mPi[this.mConnAtom[atom][0]] != 1  || this.mPi[this.mConnAtom[atom][1]] != 1 ) {
this.setAtomParity$I$I$Z(atom, 0, false);
return;
}var preferredBond=-1;
var preferredAtom=-1;
var preferredAlleneAtom=-1;
var oppositeAlleneAtom=-1;
var bestScore=0;
for (var i=0; i < 2; i++) {
var alleneAtom=this.mConnAtom[atom][i];
for (var j=0; j < this.mAllConnAtoms[alleneAtom]; j++) {
var connAtom=this.mConnAtom[alleneAtom][j];
if (connAtom != atom) {
var connBond=this.mConnBond[alleneAtom][j];
var score=p$1.getStereoBondScore$I$I.apply(this, [connBond, connAtom]);
if (bestScore < score) {
bestScore=score;
preferredAtom=connAtom;
preferredBond=connBond;
preferredAlleneAtom=alleneAtom;
oppositeAlleneAtom=this.mConnAtom[atom][1 - i];
}}}
}
if (preferredAtom == -1) return;
for (var i=0; i < 2; i++) {
var alleneAtom=this.mConnAtom[atom][i];
for (var j=0; j < this.mAllConnAtoms[alleneAtom]; j++) {
var connAtom=this.mConnAtom[alleneAtom][j];
var connBond=this.mConnBond[alleneAtom][j];
if (connAtom != atom && this.mBondAtom[0][connBond] == alleneAtom ) this.mBondType[connBond]=1;
}
}
if (this.mBondAtom[1][preferredBond] != preferredAtom) {
this.mBondAtom[0][preferredBond]=this.mBondAtom[1][preferredBond];
this.mBondAtom[1][preferredBond]=preferredAtom;
}var highPriorityAtom=2147483647;
for (var i=0; i < this.mConnAtoms[preferredAlleneAtom]; i++) {
var connAtom=this.mConnAtom[preferredAlleneAtom][i];
if ((connAtom != atom) && (highPriorityAtom > connAtom) ) highPriorityAtom=connAtom;
}
var oppositeAtom=Clazz.array(Integer.TYPE, [2]);
var oppositeAtoms=0;
for (var i=0; i < this.mConnAtoms[oppositeAlleneAtom]; i++) {
var connAtom=this.mConnAtom[oppositeAlleneAtom][i];
if (connAtom != atom) oppositeAtom[oppositeAtoms++]=connAtom;
}
var alleneAngle=this.getBondAngle$I$I(atom, oppositeAlleneAtom);
var angleDif=0.0;
if (oppositeAtoms == 2) {
if (oppositeAtom[0] > oppositeAtom[1]) {
var temp=oppositeAtom[0];
oppositeAtom[0]=oppositeAtom[1];
oppositeAtom[1]=temp;
}var hpAngleDif=$I$(3,"getAngleDif$D$D",[alleneAngle, this.getBondAngle$I$I(oppositeAlleneAtom, oppositeAtom[0])]);
var lpAngleDif=$I$(3,"getAngleDif$D$D",[alleneAngle, this.getBondAngle$I$I(oppositeAlleneAtom, oppositeAtom[1])]);
angleDif=hpAngleDif - lpAngleDif;
} else {
angleDif=$I$(3,"getAngleDif$D$D",[alleneAngle, this.getBondAngle$I$I(oppositeAlleneAtom, oppositeAtom[0])]);
}if (!!((angleDif < 0.0 ) ^ (this.getAtomParity$I(atom) == 1) ^((highPriorityAtom == preferredAtom)))) this.mBondType[preferredBond]=257;
 else this.mBondType[preferredBond]=129;
}, p$1);

Clazz.newMeth(C$, 'setStereoBondFromBondParity$I',  function (bond) {
if (this.getBondParity$I(bond) == 0 || this.getBondParity$I(bond) == 3  || !this.isBINAPChiralityBond$I(bond) ) return;
var preferredBond=-1;
var preferredAtom=-1;
var preferredBINAPAtom=-1;
var oppositeBINAPAtom=-1;
var bestScore=0;
for (var i=0; i < 2; i++) {
var atom=this.mBondAtom[i][bond];
for (var j=0; j < this.mAllConnAtoms[atom]; j++) {
var connBond=this.mConnBond[atom][j];
if (connBond != bond && this.getBondOrder$I(connBond) == 1 ) {
var connAtom=this.mConnAtom[atom][j];
var score=p$1.getStereoBondScore$I$I.apply(this, [connBond, connAtom]);
if (bestScore < score) {
bestScore=score;
preferredAtom=connAtom;
preferredBond=connBond;
preferredBINAPAtom=atom;
oppositeBINAPAtom=this.mBondAtom[1 - i][bond];
}}}
}
if (preferredAtom == -1) return;
for (var i=0; i < 2; i++) {
for (var j=0; j < this.mAllConnAtoms[this.mBondAtom[i][bond]]; j++) {
var connBond=this.mConnBond[this.mBondAtom[i][bond]][j];
if (connBond != bond && this.getBondOrder$I(connBond) == 1 ) this.mBondType[connBond]=1;
}
}
if (this.mBondAtom[1][preferredBond] != preferredAtom) {
this.mBondAtom[0][preferredBond]=this.mBondAtom[1][preferredBond];
this.mBondAtom[1][preferredBond]=preferredAtom;
}var highPriorityAtom=2147483647;
for (var i=0; i < this.mConnAtoms[preferredBINAPAtom]; i++) {
var connAtom=this.mConnAtom[preferredBINAPAtom][i];
if ((this.mConnBond[preferredBINAPAtom][i] != bond) && (highPriorityAtom > connAtom) ) highPriorityAtom=connAtom;
}
var oppositeAtom=Clazz.array(Integer.TYPE, [2]);
var oppositeAtoms=0;
for (var i=0; i < this.mConnAtoms[oppositeBINAPAtom]; i++) if (this.mConnBond[oppositeBINAPAtom][i] != bond) oppositeAtom[oppositeAtoms++]=this.mConnAtom[oppositeBINAPAtom][i];

var binapAngle=this.getBondAngle$I$I(preferredBINAPAtom, oppositeBINAPAtom);
var angleDif=0.0;
if (oppositeAtoms == 2) {
if (oppositeAtom[0] > oppositeAtom[1]) {
var temp=oppositeAtom[0];
oppositeAtom[0]=oppositeAtom[1];
oppositeAtom[1]=temp;
}var hpAngleDif=$I$(3,"getAngleDif$D$D",[binapAngle, this.getBondAngle$I$I(oppositeBINAPAtom, oppositeAtom[0])]);
var lpAngleDif=$I$(3,"getAngleDif$D$D",[binapAngle, this.getBondAngle$I$I(oppositeBINAPAtom, oppositeAtom[1])]);
angleDif=hpAngleDif - lpAngleDif;
} else {
angleDif=$I$(3,"getAngleDif$D$D",[binapAngle, this.getBondAngle$I$I(oppositeBINAPAtom, oppositeAtom[0])]);
}if (!!((angleDif < 0.0 ) ^ (this.getBondParity$I(bond) == 2) ^((highPriorityAtom == preferredAtom)))) this.mBondType[preferredBond]=257;
 else this.mBondType[preferredBond]=129;
});

Clazz.newMeth(C$, 'bondsAreParallel$D$D',  function (angle1, angle2) {
var angleDif=Math.abs($I$(3).getAngleDif$D$D(angle1, angle2));
return (angleDif < 0.08  || angleDif > 3.061592653589793  );
});

Clazz.newMeth(C$, 'preferredTHStereoBond$I$Z',  function (atom, excludeStereoBonds) {
var allConnAtoms=this.mAllConnAtoms[atom];
var angle=Clazz.array(Double.TYPE, [allConnAtoms]);
for (var i=0; i < allConnAtoms; i++) angle[i]=this.getBondAngle$I$I(atom, this.mConnAtom[atom][i]);

for (var i=1; i < allConnAtoms; i++) {
for (var j=0; j < i; j++) {
if (this.bondsAreParallel$D$D(angle[i], angle[j])) {
var angleDistanceSum1=0;
var angleDistanceSum2=0;
for (var k=0; k < allConnAtoms; k++) {
if (k != i && k != j ) {
angleDistanceSum1+=Math.abs($I$(4).difference$D$D(angle[i], angle[k]));
angleDistanceSum2+=Math.abs($I$(4).difference$D$D(angle[j], angle[k]));
}}
var bond=(angleDistanceSum1 < angleDistanceSum2 ) ? this.mConnBond[atom][i] : this.mConnBond[atom][j];
if (this.getBondOrder$I(bond) == 1 && (!excludeStereoBonds || !this.isStereoBond$I(bond) ) ) return bond;
}}
}
var isPreferred=Clazz.array(Boolean.TYPE, [allConnAtoms]);
for (var i=0; i < allConnAtoms; i++) {
var closestLeftDif=-10.0;
var closestRightDif=10.0;
for (var j=0; j < allConnAtoms; j++) {
if (j != i) {
var dif=$I$(4).difference$D$D(angle[i], angle[j]);
if (dif < 0 ) {
if (closestLeftDif < dif ) closestLeftDif=dif;
} else {
if (closestRightDif > dif ) closestRightDif=dif;
}}isPreferred[i]=(closestRightDif - closestLeftDif < 3.141592653589793 );
}
}
var preferredBond=-1;
var bestScore=0;
for (var i=0; i < allConnAtoms; i++) {
var connAtom=this.mConnAtom[atom][i];
var connBond=this.mConnBond[atom][i];
var score=p$1.getStereoBondScore$I$I.apply(this, [connBond, connAtom]);
if (isPreferred[i]) score+=16384;
if (bestScore < score && (!excludeStereoBonds || !this.isStereoBond$I(connBond) ) ) {
bestScore=score;
preferredBond=connBond;
}}
return preferredBond;
}, p$1);

Clazz.newMeth(C$, 'preferredAlleneStereoBond$I$Z',  function (atom, excludeStereoBonds) {
var preferredBond=-1;
var bestScore=0;
for (var i=0; i < 2; i++) {
var alleneAtom=this.mConnAtom[atom][i];
for (var j=0; j < this.mAllConnAtoms[alleneAtom]; j++) {
var connAtom=this.mConnAtom[alleneAtom][j];
if (connAtom != atom) {
var connBond=this.mConnBond[alleneAtom][j];
var score=p$1.getStereoBondScore$I$I.apply(this, [connBond, connAtom]);
if (bestScore < score && (!excludeStereoBonds || !this.isStereoBond$I(connBond) ) ) {
bestScore=score;
preferredBond=connBond;
}}}
}
return preferredBond;
}, p$1);

Clazz.newMeth(C$, 'preferredBinapStereoBond$I',  function (bond) {
var preferredBond=-1;
var bestScore=0;
for (var i=0; i < 2; i++) {
var atom=this.mBondAtom[i][bond];
for (var j=0; j < this.mAllConnAtoms[atom]; j++) {
var connAtom=this.mConnAtom[atom][j];
if (connAtom != this.mBondAtom[1 - i][bond]) {
var connBond=this.mConnBond[atom][j];
var score=p$1.getStereoBondScore$I$I.apply(this, [connBond, connAtom]);
if (bestScore < score) {
bestScore=score;
preferredBond=connBond;
}}}
}
return preferredBond;
}, p$1);

Clazz.newMeth(C$, 'findAlleneCenterAtom$I',  function (atom) {
var center=-1;
if (this.mPi[atom] == 1) {
for (var i=0; i < this.mConnAtoms[atom]; i++) {
if (this.mConnBondOrder[atom][i] == 2) {
var connAtom=this.mConnAtom[atom][i];
if (this.mConnAtoms[connAtom] == 2 && this.mPi[connAtom] == 2 ) {
for (var j=0; j < 2; j++) {
var endAtom=this.mConnAtom[connAtom][j];
if (endAtom != atom && this.mPi[endAtom] == 1 ) {
center=connAtom;
break;
}}
}break;
}}
}return center;
});

Clazz.newMeth(C$, 'findAlleneEndAtom$I$I',  function (atom1, atom2) {
var startAtom=atom1;
while (this.mConnAtoms[atom2] == 2 && this.mPi[atom2] == 2  && atom2 != startAtom ){
var temp=atom2;
atom2=(this.mConnAtom[atom2][0] == atom1) ? this.mConnAtom[atom2][1] : this.mConnAtom[atom2][0];
atom1=temp;
}
return (atom2 == startAtom) ? -1 : atom2;
});

Clazz.newMeth(C$, 'findBINAPOppositeAtom$I',  function (atom) {
if (this.mConnAtoms[atom] == 3 && this.isAromaticAtom$I(atom)  && this.getAtomRingSize$I(atom) >= 6 ) for (var i=0; i < this.mConnAtoms[atom]; i++) if (this.isBINAPChiralityBond$I(this.mConnBond[atom][i])) return this.mConnAtom[atom][i];

return -1;
}, p$1);

Clazz.newMeth(C$, 'findBINAPChiralityBond$I',  function (atom) {
if (this.mConnAtoms[atom] == 3 && this.isAromaticAtom$I(atom)  && this.getAtomRingSize$I(atom) >= 5 ) for (var i=0; i < this.mConnAtoms[atom]; i++) if (this.isBINAPChiralityBond$I(this.mConnBond[atom][i])) return this.mConnBond[atom][i];

return -1;
});

Clazz.newMeth(C$, 'isAmideTypeBond$I',  function (bond) {
this.ensureHelperArrays$I(1);
for (var i=0; i < 2; i++) {
var atom1=this.mBondAtom[i][bond];
if (this.mAtomicNo[atom1] == 7) {
var atom2=this.mBondAtom[1 - i][bond];
for (var j=0; j < this.mConnAtoms[atom2]; j++) {
var connAtom=this.mConnAtom[atom2][j];
var connBond=this.mConnBond[atom2][j];
if ((this.mAtomicNo[connAtom] == 7 || this.mAtomicNo[connAtom] == 8  || this.mAtomicNo[connAtom] == 16 ) && this.getBondOrder$I(connBond) >= 2 ) return true;
}
}}
return false;
});

Clazz.newMeth(C$, 'isCentralAlleneAtom$I',  function (atom) {
return this.mPi[atom] == 2 && this.mConnAtoms[atom] == 2  && this.mConnBondOrder[atom][0] == 2  && this.mConnBondOrder[atom][1] == 2  && this.mAtomicNo[atom] <= 7 ;
});

Clazz.newMeth(C$, 'isFlatNitrogen$I',  function (atom) {
if (this.mAtomicNo[atom] != 7) return false;
if (this.isAromaticAtom$I(atom) || this.mPi[atom] != 0  || Long.$ne((Long.$and(this.mAtomQueryFeatures[atom],268435456)),0 ) ) return true;
if (this.mAtomCharge[atom] == 1) return false;
var heteroCount=0;
for (var i=0; i < this.mConnAtoms[atom]; i++) {
if (this.mConnBondOrder[atom][i] == 1) {
var atomicNo=this.mAtomicNo[this.mConnAtom[atom][i]];
if (atomicNo == 8 || atomicNo == 9  || atomicNo == 17 ) ++heteroCount;
}}
if (heteroCount == 0) {
for (var i=0; i < this.mConnAtoms[atom]; i++) {
var connAtom=this.mConnAtom[atom][i];
if (this.mPi[connAtom] != 0) {
if (this.isAromaticAtom$I(connAtom)) {
if (this.getAtomRingSize$I(connAtom) >= 5) {
var orthoSubstituentCount=0;
for (var j=0; j < this.mConnAtoms[connAtom]; j++) {
var ortho=this.mConnAtom[connAtom][j];
if (ortho != atom && this.getNonHydrogenNeighbourCount$I(ortho) >= 3 ) ++orthoSubstituentCount;
}
var nitrogenNeighbourCount=this.getNonHydrogenNeighbourCount$I(atom);
if ((orthoSubstituentCount == 2 && nitrogenNeighbourCount >= 2 ) || (orthoSubstituentCount == 1 && nitrogenNeighbourCount == 3 ) ) continue;
}return true;
}for (var j=0; j < this.mConnAtoms[connAtom]; j++) {
if ((this.mConnBondOrder[connAtom][j] == 2 || this.isAromaticBond$I(this.mConnBond[connAtom][j]) )) return true;
}
}}
}if (heteroCount < 2) {
for (var i=0; i < this.mConnAtoms[atom]; i++) {
var connAtom=this.mConnAtom[atom][i];
var isStabilized=false;
var hasCompetitor=false;
for (var j=0; j < this.mConnAtoms[connAtom]; j++) {
if (this.mConnAtom[connAtom][j] != atom) {
if (this.mConnBondOrder[connAtom][j] != 1 && (this.mAtomicNo[this.mConnAtom[connAtom][j]] == 7 || this.mAtomicNo[this.mConnAtom[connAtom][j]] == 8  || this.mAtomicNo[this.mConnAtom[connAtom][j]] == 16 ) ) isStabilized=true;
if (this.mConnBondOrder[connAtom][j] == 1 && this.mAtomicNo[this.mConnAtom[connAtom][j]] == 7 ) hasCompetitor=true;
}}
if (isStabilized && (!hasCompetitor || heteroCount == 0 ) ) return true;
}
}return false;
});

Clazz.newMeth(C$, 'isBINAPChiralityBond$I',  function (bond) {
if (this.mBondType[bond] != 1 || this.isAromaticBond$I(bond)  || (this.isRingBond$I(bond) && this.getBondRingSize$I(bond) < 7 ) ) return false;
var atom1=this.mBondAtom[0][bond];
if (!this.isAromaticAtom$I(atom1) || this.getAtomRingSize$I(atom1) < 5 ) return false;
var atom2=this.mBondAtom[1][bond];
if (!this.isAromaticAtom$I(atom2) || this.getAtomRingSize$I(atom2) < 5 ) return false;
var orthoSubstituentCount1=p$1.getOrthoSubstituentCount$I$I.apply(this, [atom1, atom2]);
var orthoSubstituentCount2=p$1.getOrthoSubstituentCount$I$I.apply(this, [atom2, atom1]);
if (this.getAtomRingSize$I(atom1) > 5 && this.getAtomRingSize$I(atom2) > 5 ) return orthoSubstituentCount1 + orthoSubstituentCount2 > 2;
var secondOrderOrthoSubstituentCount1=p$1.getSecondOrderOrthoSubstituentCount$I$I.apply(this, [atom1, atom2]);
var secondOrderOrthoSubstituentCount2=p$1.getSecondOrderOrthoSubstituentCount$I$I.apply(this, [atom2, atom1]);
if (orthoSubstituentCount1 == 2 && secondOrderOrthoSubstituentCount2 >= 1 ) return true;
if (orthoSubstituentCount2 == 2 && secondOrderOrthoSubstituentCount1 >= 1 ) return true;
if (secondOrderOrthoSubstituentCount1 == 2 && (orthoSubstituentCount2 >= 1 || secondOrderOrthoSubstituentCount2 >= 1 ) ) return true;
if (secondOrderOrthoSubstituentCount2 == 2 && (orthoSubstituentCount1 >= 1 || secondOrderOrthoSubstituentCount1 >= 1 ) ) return true;
return false;
});

Clazz.newMeth(C$, 'getOrthoSubstituentCount$I$I',  function (atom, otherBondAtom) {
var count=0;
for (var i=0; i < this.mConnAtoms[atom]; i++) {
var connAtom=this.mConnAtom[atom][i];
if (connAtom != otherBondAtom && this.mConnAtoms[connAtom] > 2 ) ++count;
}
return count;
}, p$1);

Clazz.newMeth(C$, 'getSecondOrderOrthoSubstituentCount$I$I',  function (atom, otherBondAtom) {
var count=0;
for (var i=0; i < this.mConnAtoms[atom]; i++) {
var connAtom=this.mConnAtom[atom][i];
if (connAtom != otherBondAtom) {
var innerCount=0;
for (var j=0; j < this.mConnAtoms[connAtom]; j++) {
var nextConnAtom=this.mConnAtom[connAtom][j];
if (nextConnAtom != atom && this.isAromaticBond$I(this.mConnBond[connAtom][j])  && this.mConnAtoms[nextConnAtom] > 2 ) ++innerCount;
}
if (innerCount == 2) ++count;
}}
return count;
}, p$1);

Clazz.newMeth(C$, 'validateBondType$I$I',  function (bond, type) {
var ok=C$.superclazz.prototype.validateBondType$I$I.apply(this, [bond, type]);
if (ok && type == 386 ) {
this.ensureHelperArrays$I(7);
ok=!!(ok&(!this.isSmallRingBond$I(bond)));
}return ok;
});

Clazz.newMeth(C$, 'validate$',  function () {
var avbl=this.getAverageBondLength$();
var minDistanceSquare=avbl * avbl / 16.0;
for (var atom1=1; atom1 < this.mAllAtoms; atom1++) {
for (var atom2=0; atom2 < atom1; atom2++) {
var xdif=this.mCoordinates[atom2].x - this.mCoordinates[atom1].x;
var ydif=this.mCoordinates[atom2].y - this.mCoordinates[atom1].y;
var zdif=this.mCoordinates[atom2].z - this.mCoordinates[atom1].z;
if ((xdif * xdif + ydif * ydif + zdif * zdif) < minDistanceSquare ) throw Clazz.new_(Clazz.load('Exception').c$$S,["The distance between two atoms is too close."]);
}
}
this.ensureHelperArrays$I(1);
var allCharge=0;
for (var atom=0; atom < this.mAtoms; atom++) {
if (this.getOccupiedValence$I(atom) > this.getMaxValence$I(atom)) throw Clazz.new_(Clazz.load('Exception').c$$S,["atom valence exceeded"]);
allCharge+=this.mAtomCharge[atom];
}
if (allCharge != 0) throw Clazz.new_(Clazz.load('Exception').c$$S,["unbalanced atom charge"]);
});

Clazz.newMeth(C$, 'normalizeAmbiguousBonds$',  function () {
this.ensureHelperArrays$I(1);
p$1.normalizeExplicitlyDelocalizedBonds.apply(this, []);
var found=false;
for (var atom=0; atom < this.mAtoms; atom++) {
if (this.mAtomicNo[atom] == 7 && this.mAtomCharge[atom] == 0 ) {
var valence=this.getOccupiedValence$I(atom);
if (valence == 4) {
for (var i=0; i < this.mConnAtoms[atom]; i++) {
var connAtom=this.mConnAtom[atom][i];
if (this.mConnBondOrder[atom][i] == 1 && this.mAtomicNo[connAtom] == 8  && this.mConnAtoms[connAtom] == 1  && this.mAtomCharge[connAtom] == 0 ) {
found=true;
++this.mAtomCharge[atom];
--this.mAtomCharge[connAtom];
break;
}}
} else if (valence == 5) {
for (var i=0; i < this.mConnAtoms[atom]; i++) {
var connAtom=this.mConnAtom[atom][i];
var connBond=this.mConnBond[atom][i];
if (this.mConnBondOrder[atom][i] == 2 && this.mAtomicNo[connAtom] == 8 ) {
found=true;
++this.mAtomCharge[atom];
--this.mAtomCharge[connAtom];
this.mBondType[connBond]=1;
break;
}if (this.mConnBondOrder[atom][i] == 3 && this.mAtomicNo[connAtom] == 7 ) {
found=true;
++this.mAtomCharge[atom];
--this.mAtomCharge[connAtom];
this.mBondType[connBond]=2;
break;
}}
}}}
var bondDeleted=false;
for (var bond=0; bond < this.mBonds; bond++) {
for (var i=0; i < 2; i++) {
if (this.isElectronegative$I(this.mBondAtom[i][bond])) {
var atom=this.mBondAtom[1 - i][bond];
if (this.isAlkaliMetal$I(atom) || this.isEarthAlkaliMetal$I(atom) ) {
if (this.getBondOrder$I(bond) == 1) {
++this.mAtomCharge[atom];
--this.mAtomCharge[this.mBondAtom[i][bond]];
this.mBondType[bond]=512;
bondDeleted=true;
} else if (this.mBondType[bond] == 32) {
this.mBondType[bond]=512;
bondDeleted=true;
}}break;
}}
}
if (bondDeleted) {
this.compressMolTable$();
found=true;
}if (found) this.mValidHelperArrays=0;
return found;
});

Clazz.newMeth(C$, 'normalizeExplicitlyDelocalizedBonds',  function () {
for (var bond=0; bond < this.mBonds; bond++) if (this.mBondType[bond] == 64) return Clazz.new_($I$(1,1).c$$com_actelion_research_chem_ExtendedMolecule,[this]).locateDelocalizedDoubleBonds$ZA(null);

return false;
}, p$1);

Clazz.newMeth(C$, 'isAlkaliMetal$I',  function (atom) {
var atomicNo=this.mAtomicNo[atom];
return atomicNo == 3 || atomicNo == 11  || atomicNo == 19  || atomicNo == 37  || atomicNo == 55 ;
});

Clazz.newMeth(C$, 'isEarthAlkaliMetal$I',  function (atom) {
var atomicNo=this.mAtomicNo[atom];
return atomicNo == 12 || atomicNo == 20  || atomicNo == 38  || atomicNo == 56 ;
});

Clazz.newMeth(C$, 'isNitrogenFamily$I',  function (atom) {
var atomicNo=this.mAtomicNo[atom];
return atomicNo == 7 || atomicNo == 15  || atomicNo == 33 ;
});

Clazz.newMeth(C$, 'isChalcogene$I',  function (atom) {
var atomicNo=this.mAtomicNo[atom];
return atomicNo == 8 || atomicNo == 16  || atomicNo == 34  || atomicNo == 52 ;
});

Clazz.newMeth(C$, 'isHalogene$I',  function (atom) {
var atomicNo=this.mAtomicNo[atom];
return atomicNo == 9 || atomicNo == 17  || atomicNo == 35  || atomicNo == 53 ;
});

Clazz.newMeth(C$, 'canonizeCharge$Z',  function (allowUnbalancedCharge) {
return this.canonizeCharge$Z$Z(allowUnbalancedCharge, false);
});

Clazz.newMeth(C$, 'canonizeCharge$Z$Z',  function (allowUnbalancedCharge, doNeutralize) {
this.ensureHelperArrays$I(1);
if (doNeutralize) allowUnbalancedCharge=true;
for (var bond=0; bond < this.mAllBonds; bond++) {
var bondOrder=this.getBondOrder$I(bond);
if (bondOrder == 1 || bondOrder == 2 ) {
var atom1;
var atom2;
if (this.mAtomCharge[this.mBondAtom[0][bond]] > 0 && this.mAtomCharge[this.mBondAtom[1][bond]] < 0 ) {
atom1=this.mBondAtom[0][bond];
atom2=this.mBondAtom[1][bond];
} else if (this.mAtomCharge[this.mBondAtom[0][bond]] < 0 && this.mAtomCharge[this.mBondAtom[1][bond]] > 0 ) {
atom1=this.mBondAtom[1][bond];
atom2=this.mBondAtom[0][bond];
} else continue;
if (this.isMetalAtom$I(atom1) || this.isMetalAtom$I(atom2) ) continue;
if ((this.mAtomicNo[atom1] < 9 && this.getOccupiedValence$I(atom1) > 3 ) || (this.mAtomicNo[atom2] < 9 && this.getOccupiedValence$I(atom2) > 3 ) ) continue;
var hasImplicitHydrogen=(this.getImplicitHydrogens$I(atom1) != 0);
this.mAtomCharge[atom1]-=1;
this.mAtomCharge[atom2]+=1;
if (!hasImplicitHydrogen) {
var oldBondType=this.mBondType[bond];
if (bondOrder == 1) this.mBondType[bond]=2;
 else this.mBondType[bond]=4;
if (oldBondType == 129 || oldBondType == 257 ) {
var stereoCenter=this.mBondAtom[0][bond];
var newStereoBond=p$1.preferredTHStereoBond$I$Z.apply(this, [stereoCenter, false]);
if (this.mBondAtom[0][newStereoBond] != stereoCenter) {
this.mBondAtom[1][newStereoBond]=this.mBondAtom[0][newStereoBond];
this.mBondAtom[1][newStereoBond]=stereoCenter;
}}}this.mValidHelperArrays=0;
}}
var overallCharge=0;
var negativeAtomCount=0;
var negativeAdjustableCharge=0;
for (var atom=0; atom < this.mAllAtoms; atom++) {
overallCharge+=this.mAtomCharge[atom];
if (this.mAtomCharge[atom] < 0 && !p$1.hasPositiveNeighbour$I.apply(this, [atom]) ) {
++negativeAtomCount;
if (this.isElectronegative$I(atom)) negativeAdjustableCharge-=this.mAtomCharge[atom];
}}
if (!allowUnbalancedCharge && overallCharge != 0 ) throw Clazz.new_(Clazz.load('Exception').c$$S,["molecule\'s overall charges are not balanced"]);
this.ensureHelperArrays$I(1);
var overallChargeChange=0;
var positiveChargeForRemoval=doNeutralize ? overallCharge + negativeAdjustableCharge : negativeAdjustableCharge;
for (var atom=0; atom < this.mAllAtoms; atom++) {
if (this.mAtomCharge[atom] > 0) {
if (!p$1.hasNegativeNeighbour$I.apply(this, [atom]) && this.isElectronegative$I(atom) ) {
var chargeReduction=Math.min(this.getImplicitHydrogens$I(atom), this.mAtomCharge[atom]);
if (chargeReduction != 0 && positiveChargeForRemoval >= chargeReduction ) {
overallCharge-=chargeReduction;
overallChargeChange-=chargeReduction;
positiveChargeForRemoval-=chargeReduction;
this.mAtomCharge[atom]-=chargeReduction;
this.mValidHelperArrays&=1;
}}}}
var negativeChargeForRemoval=doNeutralize ? overallCharge : overallChargeChange;
if (negativeChargeForRemoval < 0) {
var negativeAtom=Clazz.array(Integer.TYPE, [negativeAtomCount]);
negativeAtomCount=0;
for (var atom=0; atom < this.mAllAtoms; atom++) {
if (this.mAtomCharge[atom] < 0 && !p$1.hasPositiveNeighbour$I.apply(this, [atom]) ) {
negativeAtom[negativeAtomCount++]=(this.mAtomicNo[atom] << 22) + atom;
}}
$I$(2).sort$IA(negativeAtom);
for (var i=negativeAtom.length - 1; (negativeChargeForRemoval < 0) && (i >= negativeAtom.length - negativeAtomCount) ; i--) {
var atom=negativeAtom[i] & 4194303;
if (this.isElectronegative$I(atom)) {
var chargeReduction=Math.min(-negativeChargeForRemoval, -this.mAtomCharge[atom]);
overallCharge+=chargeReduction;
negativeChargeForRemoval+=chargeReduction;
this.mAtomCharge[atom]+=chargeReduction;
this.mValidHelperArrays&=1;
}}
}return overallCharge;
});

Clazz.newMeth(C$, 'hasNegativeNeighbour$I',  function (atom) {
for (var i=0; i < this.mConnAtoms[atom]; i++) if (this.mAtomCharge[this.mConnAtom[atom][i]] < 0) return true;

return false;
}, p$1);

Clazz.newMeth(C$, 'hasPositiveNeighbour$I',  function (atom) {
for (var i=0; i < this.mConnAtoms[atom]; i++) if (this.mAtomCharge[this.mConnAtom[atom][i]] > 0) return true;

return false;
}, p$1);

Clazz.newMeth(C$, 'getZNeighbour$I$I',  function (connAtom, bond) {
if (this.getBondOrder$I(bond) != 2 && !this.isAromaticBond$I(bond) ) return -1;
var parity=this.getBondParity$I(bond);
if (parity != 1 && parity != 2 ) return -1;
for (var i=0; i < 2; i++) {
var atom1=this.mBondAtom[i][bond];
var atom2=this.mBondAtom[1 - i][bond];
var other1=-1;
var found=false;
for (var j=0; j < this.mConnAtoms[atom1]; j++) {
var conn=this.mConnAtom[atom1][j];
if (conn != atom2) {
if (conn == connAtom) found=true;
 else other1=conn;
}}
if (found) {
var lowConn=-1;
var highConn=-1;
for (var j=0; j < this.mConnAtoms[atom2]; j++) {
var conn=this.mConnAtom[atom2][j];
if (conn != atom1) {
if (lowConn == -1) lowConn=conn;
 else if (conn > lowConn) highConn=conn;
 else {
highConn=lowConn;
lowConn=conn;
}}}
if (this.mConnAtoms[atom1] == 2) {
if (this.mConnAtoms[atom2] == 2) return parity == 2 ? lowConn : -1;
return (parity == 2) ? lowConn : highConn;
} else {
if (this.mConnAtoms[atom2] == 2) return !!((parity == 2) ^ (connAtom < other1)) ? -1 : lowConn;
return !!((parity == 2) ^ (connAtom < other1)) ? highConn : lowConn;
}}}
return -1;
});

Clazz.newMeth(C$, 'getHelperArrayStatus$',  function () {
return this.mValidHelperArrays;
});

Clazz.newMeth(C$, 'ensureHelperArrays$I',  function (required) {
if ((required & ~this.mValidHelperArrays) == 0) return;
if ((this.mValidHelperArrays & 1) == 0) {
p$1.handleHydrogens.apply(this, []);
p$1.calculateNeighbours.apply(this, []);
this.mValidHelperArrays|=1;
if (p$1.convertHydrogenToQueryFeatures.apply(this, [])) {
p$1.handleHydrogens.apply(this, []);
p$1.calculateNeighbours.apply(this, []);
}}if ((required & ~this.mValidHelperArrays) == 0) return;
if ((this.mValidHelperArrays & ~(6)) != 0) {
for (var atom=0; atom < this.mAtoms; atom++) this.mAtomFlags[atom]&=~15368;

for (var bond=0; bond < this.mBonds; bond++) this.mBondFlags[bond]&=~704;

if ((required & 4) == 0) {
p$1.findRings$I.apply(this, [1]);
this.mValidHelperArrays|=2;
return;
}p$1.findRings$I.apply(this, [7]);
for (var atom=0; atom < this.mAtoms; atom++) {
for (var i=0; i < this.mConnAtoms[atom]; i++) {
var connBond=this.mConnBond[atom][i];
if (this.isAromaticBond$I(connBond)) continue;
var connAtom=this.mConnAtom[atom][i];
for (var j=0; j < this.mConnAtoms[connAtom]; j++) {
if (this.mConnBond[connAtom][j] == connBond) continue;
if (this.mConnBondOrder[connAtom][j] > 1) {
if (this.mAtomicNo[this.mConnAtom[connAtom][j]] == 6) this.mAtomFlags[atom]|=4096;
 else {
if (!this.isAromaticBond$I(this.mConnBond[connAtom][j]) && this.isElectronegative$I(this.mConnAtom[connAtom][j]) ) this.mAtomFlags[atom]|=8192;
}}}
}
}
while (true){
var found=false;
for (var atom=0; atom < this.mAtoms; atom++) {
if (this.mPi[atom] > 0 && (this.mAtomFlags[atom] & 8192) != 0  && !this.mRingSet.isAromaticAtom$I(atom) ) {
for (var i=0; i < this.mConnAtoms[atom]; i++) {
if (this.mConnBondOrder[atom][i] > 1) {
var connAtom=this.mConnAtom[atom][i];
var connBond=this.mConnBond[atom][i];
for (var j=0; j < this.mConnAtoms[connAtom]; j++) {
if (this.mConnBond[connAtom][j] != connBond) {
var candidate=this.mConnAtom[connAtom][j];
if ((this.mAtomFlags[candidate] & 8192) == 0) {
this.mAtomFlags[candidate]|=8192;
found=true;
}}}
}}
}}
if (!found) break;
}
}this.mValidHelperArrays|=(6);
});

Clazz.newMeth(C$, 'handleHydrogens',  function () {
var isHydrogen=p$1.findSimpleHydrogens.apply(this, []);
var lastNonHAtom=this.mAllAtoms;
do --lastNonHAtom;
 while ((lastNonHAtom >= 0) && isHydrogen[lastNonHAtom] );
for (var atom=0; atom < lastNonHAtom; atom++) {
if (isHydrogen[atom]) {
this.swapAtoms$I$I(atom, lastNonHAtom);
var temp=isHydrogen[atom];
isHydrogen[atom]=isHydrogen[lastNonHAtom];
isHydrogen[lastNonHAtom]=temp;
do --lastNonHAtom;
 while (isHydrogen[lastNonHAtom]);
}}
this.mAtoms=lastNonHAtom + 1;
if (this.mAllAtoms == this.mAtoms) {
this.mBonds=this.mAllBonds;
return;
}var isHydrogenBond=Clazz.array(Boolean.TYPE, [this.mAllBonds]);
for (var bond=0; bond < this.mAllBonds; bond++) {
var atom1=this.mBondAtom[0][bond];
var atom2=this.mBondAtom[1][bond];
if (isHydrogen[atom1] || isHydrogen[atom2] ) isHydrogenBond[bond]=true;
}
var lastNonHBond=this.mAllBonds;
do --lastNonHBond;
 while ((lastNonHBond >= 0) && isHydrogenBond[lastNonHBond] );
for (var bond=0; bond < lastNonHBond; bond++) {
if (isHydrogenBond[bond]) {
this.swapBonds$I$I(bond, lastNonHBond);
isHydrogenBond[bond]=false;
do --lastNonHBond;
 while (isHydrogenBond[lastNonHBond]);
}}
this.mBonds=lastNonHBond + 1;
}, p$1);

Clazz.newMeth(C$, 'getHandleHydrogenMap$',  function () {
return this.getHandleHydrogenAtomMap$ZA(p$1.findSimpleHydrogens.apply(this, []));
});

Clazz.newMeth(C$, 'getHandleHydrogenAtomMap$ZA',  function (isSimpleHydrogen) {
var map=Clazz.array(Integer.TYPE, [this.mAllAtoms]);
for (var i=0; i < this.mAllAtoms; i++) map[i]=i;

var lastNonHAtom=this.mAllAtoms;
do --lastNonHAtom;
 while ((lastNonHAtom >= 0) && isSimpleHydrogen[lastNonHAtom] );
for (var i=0; i < lastNonHAtom; i++) {
if (isSimpleHydrogen[map[i]]) {
var tempIndex=map[i];
map[i]=map[lastNonHAtom];
map[lastNonHAtom]=tempIndex;
var temp=isSimpleHydrogen[i];
isSimpleHydrogen[i]=isSimpleHydrogen[lastNonHAtom];
isSimpleHydrogen[lastNonHAtom]=temp;
do --lastNonHAtom;
 while (isSimpleHydrogen[lastNonHAtom]);
}}
return map;
});

Clazz.newMeth(C$, 'getHandleHydrogenBondMap$',  function () {
var isSimpleHydrogen=p$1.findSimpleHydrogens.apply(this, []);
var map=Clazz.array(Integer.TYPE, [this.mAllBonds]);
for (var i=0; i < this.mAllBonds; i++) map[i]=i;

var isHydrogenBond=Clazz.array(Boolean.TYPE, [this.mAllBonds]);
for (var bond=0; bond < this.mAllBonds; bond++) isHydrogenBond[bond]=isSimpleHydrogen[this.mBondAtom[0][bond]] || isSimpleHydrogen[this.mBondAtom[1][bond]] ;

var lastNonHBond=this.mAllBonds;
do --lastNonHBond;
 while ((lastNonHBond >= 0) && isHydrogenBond[lastNonHBond] );
for (var i=0; i < lastNonHBond; i++) {
if (isHydrogenBond[map[i]]) {
var tempIndex=map[i];
map[i]=map[lastNonHBond];
map[lastNonHBond]=tempIndex;
do --lastNonHBond;
 while (isHydrogenBond[map[lastNonHBond]]);
}}
return map;
});

Clazz.newMeth(C$, 'findSimpleHydrogens',  function () {
var isSimpleHydrogen=Clazz.array(Boolean.TYPE, [this.mAllAtoms]);
for (var atom=0; atom < this.mAllAtoms; atom++) isSimpleHydrogen[atom]=this.isSimpleHydrogen$I(atom);

var oneBondFound=Clazz.array(Boolean.TYPE, [this.mAllAtoms]);
for (var bond=0; bond < this.mAllBonds; bond++) {
var atom1=this.mBondAtom[0][bond];
var atom2=this.mBondAtom[1][bond];
if (this.getBondOrder$I(bond) != 1) {
isSimpleHydrogen[atom1]=false;
isSimpleHydrogen[atom2]=false;
continue;
}if (oneBondFound[atom1]) isSimpleHydrogen[atom1]=false;
if (oneBondFound[atom2]) isSimpleHydrogen[atom2]=false;
if (isSimpleHydrogen[atom1] && this.isMetalAtom$I(atom2) && this.mAtomicNo[atom2] != 13  ) isSimpleHydrogen[atom1]=false;
if (isSimpleHydrogen[atom2] && this.isMetalAtom$I(atom1) && this.mAtomicNo[atom1] != 13  ) isSimpleHydrogen[atom2]=false;
oneBondFound[atom1]=true;
oneBondFound[atom2]=true;
}
for (var bond=0; bond < this.mAllBonds; bond++) {
if (isSimpleHydrogen[this.mBondAtom[0][bond]] && isSimpleHydrogen[this.mBondAtom[1][bond]] ) {
isSimpleHydrogen[this.mBondAtom[0][bond]]=false;
isSimpleHydrogen[this.mBondAtom[1][bond]]=false;
}}
for (var atom=0; atom < this.mAllAtoms; atom++) if (!oneBondFound[atom]) isSimpleHydrogen[atom]=false;

return isSimpleHydrogen;
}, p$1);

Clazz.newMeth(C$, 'isSimpleHydrogen$I',  function (atom) {
return this.mAtomicNo[atom] == 1 && this.mAtomMass[atom] == 0  && this.mAtomCharge[atom] == 0  && (this.mAtomCustomLabel == null  || this.mAtomCustomLabel[atom] == null  ) ;
});

Clazz.newMeth(C$, 'removeExplicitHydrogens$',  function () {
this.removeExplicitHydrogens$Z$Z(true, false);
});

Clazz.newMeth(C$, 'removeExplicitHydrogens$Z$Z',  function (hasValid2DCoords, hasValid3DCoords) {
this.ensureHelperArrays$I(hasValid2DCoords ? 15 : 1);
this.mAllAtoms=this.mAtoms;
this.mAllBonds=this.mBonds;
for (var atom=0; atom < this.mAtoms; atom++) {
if (this.mAllConnAtoms[atom] != this.mConnAtoms[atom]) {
var abnormalValence=this.getImplicitHigherValence$I$Z(atom, false);
this.mAllConnAtoms[atom]=this.mConnAtoms[atom];
if (abnormalValence != -1) {
var newAbnormalValence=this.getImplicitHigherValence$I$Z(atom, true);
if (abnormalValence != newAbnormalValence) {
var explicitAbnormalValence=this.getAtomAbnormalValence$I(atom);
if (explicitAbnormalValence == -1 || explicitAbnormalValence < abnormalValence ) this.setAtomAbnormalValence$I$I(atom, abnormalValence);
}}}}
if (hasValid2DCoords) this.setStereoBondsFromParity$();
this.mValidHelperArrays=0;
});

Clazz.newMeth(C$, 'calculateNeighbours',  function () {
this.mConnAtoms=Clazz.array(Integer.TYPE, [this.mAllAtoms]);
this.mAllConnAtoms=Clazz.array(Integer.TYPE, [this.mAllAtoms]);
this.mConnAtom=Clazz.array(Integer.TYPE, [this.mAllAtoms, null]);
this.mConnBond=Clazz.array(Integer.TYPE, [this.mAllAtoms, null]);
this.mConnBondOrder=Clazz.array(Integer.TYPE, [this.mAllAtoms, null]);
this.mPi=Clazz.array(Integer.TYPE, [this.mAtoms]);
var connCount=Clazz.array(Integer.TYPE, [this.mAllAtoms]);
for (var bnd=0; bnd < this.mAllBonds; bnd++) {
++connCount[this.mBondAtom[0][bnd]];
++connCount[this.mBondAtom[1][bnd]];
}
for (var atom=0; atom < this.mAllAtoms; atom++) {
this.mConnAtom[atom]=Clazz.array(Integer.TYPE, [connCount[atom]]);
this.mConnBond[atom]=Clazz.array(Integer.TYPE, [connCount[atom]]);
this.mConnBondOrder[atom]=Clazz.array(Integer.TYPE, [connCount[atom]]);
}
var metalBondFound=false;
for (var bnd=0; bnd < this.mBonds; bnd++) {
var order=this.getBondOrder$I(bnd);
if (order == 0) {
metalBondFound=true;
continue;
}for (var i=0; i < 2; i++) {
var atom=this.mBondAtom[i][bnd];
var allConnAtoms=this.mAllConnAtoms[atom];
this.mConnBondOrder[atom][allConnAtoms]=order;
this.mConnAtom[atom][allConnAtoms]=this.mBondAtom[1 - i][bnd];
this.mConnBond[atom][allConnAtoms]=bnd;
++this.mAllConnAtoms[atom];
++this.mConnAtoms[atom];
if (atom < this.mAtoms) {
if (order > 1) this.mPi[atom]+=order - 1;
 else if (this.mBondType[bnd] == 64) this.mPi[atom]=1;
}}
}
for (var bnd=this.mBonds; bnd < this.mAllBonds; bnd++) {
var order=this.getBondOrder$I(bnd);
if (order == 0) {
metalBondFound=true;
continue;
}for (var i=0; i < 2; i++) {
var atom=this.mBondAtom[i][bnd];
var allConnAtoms=this.mAllConnAtoms[atom];
this.mConnBondOrder[atom][allConnAtoms]=order;
this.mConnAtom[atom][allConnAtoms]=this.mBondAtom[1 - i][bnd];
this.mConnBond[atom][allConnAtoms]=bnd;
++this.mAllConnAtoms[atom];
if (this.mBondAtom[1 - i][bnd] < this.mAtoms) ++this.mConnAtoms[atom];
}
}
if (metalBondFound) {
var allConnAtoms=Clazz.array(Integer.TYPE, [this.mAllAtoms]);
for (var atom=0; atom < this.mAllAtoms; atom++) allConnAtoms[atom]=this.mAllConnAtoms[atom];

for (var bnd=0; bnd < this.mAllBonds; bnd++) {
var order=this.getBondOrder$I(bnd);
if (order == 0) {
for (var i=0; i < 2; i++) {
var atom=this.mBondAtom[i][bnd];
this.mConnBondOrder[atom][allConnAtoms[atom]]=order;
this.mConnAtom[atom][allConnAtoms[atom]]=this.mBondAtom[1 - i][bnd];
this.mConnBond[atom][allConnAtoms[atom]]=bnd;
++allConnAtoms[atom];
}
}}
}}, p$1);

Clazz.newMeth(C$, 'findRings$I',  function (mode) {
this.mRingSet=Clazz.new_($I$(5,1).c$$com_actelion_research_chem_ExtendedMolecule$I,[this, mode]);
var atomRingBondCount=Clazz.array(Integer.TYPE, [this.mAtoms]);
for (var bond=0; bond < this.mBonds; bond++) {
if (this.mRingSet.getBondRingSize$I(bond) != 0) {
this.mBondFlags[bond]|=64;
++atomRingBondCount[this.mBondAtom[0][bond]];
++atomRingBondCount[this.mBondAtom[1][bond]];
}}
for (var atom=0; atom < this.mAtoms; atom++) {
if (atomRingBondCount[atom] == 2) this.mAtomFlags[atom]|=1024;
 else if (atomRingBondCount[atom] == 3) this.mAtomFlags[atom]|=2048;
 else if (atomRingBondCount[atom] > 3) this.mAtomFlags[atom]|=3072;
}
for (var ringNo=0; ringNo < this.mRingSet.getSize$(); ringNo++) {
var ringAtom=this.mRingSet.getRingAtoms$I(ringNo);
var ringBond=this.mRingSet.getRingBonds$I(ringNo);
var ringAtoms=ringAtom.length;
for (var i=0; i < ringAtoms; i++) {
this.mAtomFlags[ringAtom[i]]|=8;
this.mBondFlags[ringBond[i]]|=128;
if (this.mBondType[ringBond[i]] == 386) this.mBondType[ringBond[i]]=2;
}
}
}, p$1);

Clazz.newMeth(C$, 'convertHydrogenToQueryFeatures',  function () {
if (!this.mIsFragment) return false;
for (var atom=0; atom < this.mAllAtoms; atom++) {
if (this.getFreeValence$I(atom) <= 0 && !(this.mAtomCharge[atom] == 0 && (this.mAtomicNo[atom] == 5 || this.isNitrogenFamily$I(atom)  || this.isChalcogene$I(atom) ) ) ) (this.mAtomQueryFeatures[$k$=atom]=Long.$and(this.mAtomQueryFeatures[$k$],((Long.$not((6144))))));
}
var deleteHydrogens=false;
for (var atom=0; atom < this.mAtoms; atom++) {
var explicitHydrogens=this.getExplicitHydrogens$I(atom);
if (!this.mProtectHydrogen && explicitHydrogens > 0 ) {
if (Long.$eq((Long.$and(this.mAtomQueryFeatures[atom],2048)),0 )) {
var queryFeatureHydrogens=Long.$eq((Long.$and(this.mAtomQueryFeatures[atom],1920)),(896) ) ? 3 : Long.$eq((Long.$and(this.mAtomQueryFeatures[atom],1920)),(384) ) ? 2 : Long.$eq((Long.$and(this.mAtomQueryFeatures[atom],128)),128 ) ? 1 : 0;
var freeValence=this.getFreeValence$I(atom);
if (this.mAtomCharge[atom] == 0 && Long.$eq((Long.$and(this.mAtomQueryFeatures[atom],234881024)),0 )  && this.mAtomicNo[atom] != 6 ) ++freeValence;
var queryFeatureShift=explicitHydrogens;
if (queryFeatureShift > 3 - queryFeatureHydrogens) queryFeatureShift=3 - queryFeatureHydrogens;
if (queryFeatureShift > freeValence + explicitHydrogens - queryFeatureHydrogens) queryFeatureShift=freeValence + explicitHydrogens - queryFeatureHydrogens;
if (queryFeatureShift > 0) {
var queryFeatures=(queryFeatureHydrogens == 0) ? 0 : Long.$sl((Long.$and(this.mAtomQueryFeatures[atom],1920)),queryFeatureShift);
(queryFeatures=Long.$or(queryFeatures,((queryFeatureShift == 3 ? 7 : explicitHydrogens == 2 ? 3 : 1) << 7)));
(this.mAtomQueryFeatures[$k$=atom]=Long.$and(this.mAtomQueryFeatures[$k$],((Long.$not(1920)))));
(this.mAtomQueryFeatures[$k$=atom]=Long.$or(this.mAtomQueryFeatures[$k$],((Long.$and(1920,queryFeatures)))));
}}for (var i=this.mConnAtoms[atom]; i < this.mAllConnAtoms[atom]; i++) {
var connBond=this.mConnBond[atom][i];
if (this.mBondType[connBond] == 1) {
this.mAtomicNo[this.mConnAtom[atom][i]]=-1;
this.mBondType[connBond]=512;
deleteHydrogens=true;
}}
}}
if (deleteHydrogens) this.compressMolTable$();
return deleteHydrogens;
}, p$1);

Clazz.newMeth(C$, 'validateAtomQueryFeatures$',  function () {
if (!this.mIsFragment) return;
this.ensureHelperArrays$I(7);
for (var atom=0; atom < this.mAtoms; atom++) {
if (this.isRingAtom$I(atom)) (this.mAtomQueryFeatures[$k$=atom]=Long.$and(this.mAtomQueryFeatures[$k$],((Long.$not((4294967304))))));
if (this.isAromaticAtom$I(atom)) (this.mAtomQueryFeatures[$k$=atom]=Long.$and(this.mAtomQueryFeatures[$k$],((Long.$not(70368744177670)))));
 else if (Long.$ne((Long.$and(this.mAtomQueryFeatures[atom],2)),0 )) (this.mAtomQueryFeatures[$k$=atom]=Long.$and(this.mAtomQueryFeatures[$k$],((Long.$not(4)))));
if (Long.$ne((Long.$and(this.mAtomQueryFeatures[atom],29360128)),0 )) (this.mAtomQueryFeatures[$k$=atom]=Long.$and(this.mAtomQueryFeatures[$k$],((Long.$not(8)))));
if (this.mAtomCharge[atom] != 0) this.mAtomFlags[$k$=atom]=Long.$ival(Long.$and(this.mAtomFlags[$k$],((Long.$not(234881024)))));
}
});

Clazz.newMeth(C$, 'validateBondQueryFeatures$',  function () {
if (!this.mIsFragment) return;
this.ensureHelperArrays$I(7);
for (var bond=0; bond < this.mBonds; bond++) {
if (this.isDelocalizedBond$I(bond)) this.mBondQueryFeatures[bond]&=~8;
var bondType=this.mBondType[bond] & 127;
if (bondType == 1) this.mBondQueryFeatures[bond]&=~1;
 else if (bondType == 2) this.mBondQueryFeatures[bond]&=~2;
 else if (bondType == 4) this.mBondQueryFeatures[bond]&=~4;
 else if (bondType == 8) this.mBondQueryFeatures[bond]&=~32;
 else if (bondType == 16) this.mBondQueryFeatures[bond]&=~64;
 else if (bondType == 32) this.mBondQueryFeatures[bond]&=~16;
 else if (bondType == 64) this.mBondQueryFeatures[bond]&=~8;
}
});

Clazz.newMeth(C$, 'writeObject$java_io_ObjectOutputStream',  function (stream) {
}, p$1);

Clazz.newMeth(C$, 'readObject$java_io_ObjectInputStream',  function (stream) {
}, p$1);

Clazz.newMeth(C$, 'getCenterGravity$com_actelion_research_chem_ExtendedMolecule',  function (mol) {
var n=mol.getAllAtoms$();
var indices=Clazz.array(Integer.TYPE, [n]);
for (var i=0; i < indices.length; i++) {
indices[i]=i;
}
return C$.getCenterGravity$com_actelion_research_chem_ExtendedMolecule$IA(mol, indices);
}, 1);

Clazz.newMeth(C$, 'getCenterGravity$com_actelion_research_chem_ExtendedMolecule$IA',  function (mol, indices) {
var c=Clazz.new_($I$(6,1));
for (var i=0; i < indices.length; i++) {
c.x+=mol.getAtomX$I(indices[i]);
c.y+=mol.getAtomY$I(indices[i]);
c.z+=mol.getAtomZ$I(indices[i]);
}
c.x/=indices.length;
c.y/=indices.length;
c.z/=indices.length;
return c;
}, 1);

C$.$static$=function(){C$.$static$=0;
C$.$_ASSERT_ENABLED_ = ClassLoader.getClassAssertionStatus$(C$);
};
var $k$;
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-18 09:54:14 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
