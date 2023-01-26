(function(){var P$=Clazz.newPackage("com.actelion.research.chem"),p$1={},I$=[[0,'com.actelion.research.chem.RingCollection']],I$0=I$[0],$I$=function(i,n){return((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "AromaticityResolver");

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['Z',['mAllHydrogensAreExplicit'],'I',['mAromaticAtoms','mAromaticBonds','mPiElectronsAdded'],'O',['mMol','com.actelion.research.chem.ExtendedMolecule','mIsDelocalizedAtom','boolean[]','+mIsDelocalizedBond']]]

Clazz.newMeth(C$, 'c$$com_actelion_research_chem_ExtendedMolecule',  function (mol) {
;C$.$init$.apply(this);
this.mMol=mol;
}, 1);

Clazz.newMeth(C$, 'locateDelocalizedDoubleBonds$ZA',  function (isAromaticBond) {
return this.locateDelocalizedDoubleBonds$ZA$Z$Z(isAromaticBond, false, false);
});

Clazz.newMeth(C$, 'locateDelocalizedDoubleBonds$ZA$Z$Z',  function (isAromaticBond, mayChangeAtomCharges, allHydrogensAreExplicit) {
this.mMol.ensureHelperArrays$I(1);
if (isAromaticBond != null ) {
this.mIsDelocalizedBond=isAromaticBond;
} else {
this.mIsDelocalizedBond=Clazz.array(Boolean.TYPE, [this.mMol.getBonds$()]);
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mMol.getBondType$I(bond) == 64) {
this.mIsDelocalizedBond[bond]=true;
this.mMol.setBondType$I$I(bond, 1);
}}
}this.mPiElectronsAdded=0;
this.mIsDelocalizedAtom=Clazz.array(Boolean.TYPE, [this.mMol.getAtoms$()]);
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mIsDelocalizedBond[bond]) {
++this.mAromaticBonds;
for (var i=0; i < 2; i++) {
if (!this.mIsDelocalizedAtom[this.mMol.getBondAtom$I$I(i, bond)]) {
this.mIsDelocalizedAtom[this.mMol.getBondAtom$I$I(i, bond)]=true;
++this.mAromaticAtoms;
}}
}}
if (this.mAromaticBonds == 0) return true;
this.mAllHydrogensAreExplicit=allHydrogensAreExplicit;
p$1.protectFullValenceAtoms$Z.apply(this, [mayChangeAtomCharges]);
if (this.mMol.isFragment$()) p$1.promoteDelocalizedChains.apply(this, []);
var ringSet=Clazz.new_($I$(1,1).c$$com_actelion_research_chem_ExtendedMolecule$I,[this.mMol, 1]);
if (mayChangeAtomCharges) p$1.addObviousAtomCharges$com_actelion_research_chem_RingCollection.apply(this, [ringSet]);
p$1.protectObviousDelocalizationLeaks$com_actelion_research_chem_RingCollection.apply(this, [ringSet]);
p$1.protectAmideBonds$com_actelion_research_chem_RingCollection.apply(this, [ringSet]);
p$1.protectDoubleBondAtoms.apply(this, []);
p$1.promoteObviousBonds.apply(this, []);
while (p$1.promoteOuterShellDelocalizedRingSystems$com_actelion_research_chem_RingCollection.apply(this, [ringSet]))p$1.promoteObviousBonds.apply(this, []);

while (this.mAromaticBonds != 0){
var bondsPromoted=false;
if (!bondsPromoted) {
for (var ring=0; ring < ringSet.getSize$(); ring++) {
if (ringSet.getRingSize$I(ring) == 6) {
var isAromaticRing=true;
var ringBond=ringSet.getRingBonds$I(ring);
for (var i=0; i < 6; i++) {
if (!this.mIsDelocalizedBond[ringBond[i]]) {
isAromaticRing=false;
break;
}}
if (isAromaticRing) {
for (var i=0; i < 6; i+=2) p$1.promoteBond$I.apply(this, [ringBond[i]]);

bondsPromoted=true;
break;
}}}
}if (!bondsPromoted) {
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mIsDelocalizedBond[bond]) {
p$1.promoteBond$I.apply(this, [bond]);
p$1.promoteObviousBonds.apply(this, []);
bondsPromoted=true;
break;
}}
}}
return (this.mAromaticAtoms == this.mPiElectronsAdded);
});

Clazz.newMeth(C$, 'protectObviousDelocalizationLeaks$com_actelion_research_chem_RingCollection',  function (ringSet) {
for (var r=0; r < ringSet.getSize$(); r++) {
var ringSize=ringSet.getRingSize$I(r);
if (ringSize == 3 || ringSize == 5  || ringSize == 7 ) {
var ringAtom=ringSet.getRingAtoms$I(r);
for (var i=0; i < ringSize; i++) {
var atom=ringAtom[i];
if (p$1.isAromaticAtom$I.apply(this, [atom])) {
if (ringSize == 5) {
if ((this.mMol.getAtomicNo$I(atom) == 6 && this.mMol.getAtomCharge$I(atom) == -1  && this.mMol.getAllConnAtoms$I(atom) == 3 ) || (this.mMol.getAtomicNo$I(atom) == 7 && this.mMol.getAtomCharge$I(atom) == 0  && this.mMol.getAllConnAtoms$I(atom) == 3 ) || (this.mMol.getAtomicNo$I(atom) == 8 && this.mMol.getAtomCharge$I(atom) == 0  && this.mMol.getConnAtoms$I(atom) == 2 ) || (this.mMol.getAtomicNo$I(atom) == 16 && this.mMol.getAtomCharge$I(atom) == 0  && this.mMol.getConnAtoms$I(atom) == 2 ) || (this.mMol.getAtomicNo$I(atom) == 34 && this.mMol.getAtomCharge$I(atom) == 0  && this.mMol.getConnAtoms$I(atom) == 2 )  ) p$1.protectAtom$I.apply(this, [atom]);
} else {
if ((this.mMol.getAtomicNo$I(atom) == 5 && this.mMol.getAtomCharge$I(atom) == 0  && this.mMol.getAllConnAtoms$I(atom) == 3 ) || (this.mMol.getAtomicNo$I(atom) == 6 && this.mMol.getAtomCharge$I(atom) == 1 ) ) p$1.protectAtom$I.apply(this, [atom]);
}}}
}}
for (var r=0; r < ringSet.getSize$(); r++) {
if (ringSet.getRingSize$I(r) == 5) {
var ringBond=ringSet.getRingBonds$I(r);
var isDelocalized=true;
for (var i=0; i < ringBond.length; i++) {
if (!this.mIsDelocalizedBond[ringBond[i]]) {
isDelocalized=false;
break;
}}
if (isDelocalized) {
var ringAtom=ringSet.getRingAtoms$I(r);
var negativeCarbonPriority=0;
var negativeCarbon=-1;
for (var i=0; i < ringBond.length; i++) {
if (this.mMol.getAtomCharge$I(ringAtom[i]) == -1 && this.mMol.getAtomicNo$I(ringAtom[i]) == 6 ) {
var priority=this.mMol.getAllConnAtoms$I(ringAtom[i]) == 3 ? 3 : this.mMol.getAllConnAtomsPlusMetalBonds$I(ringAtom[i]) == 3 ? 2 : 1;
if (negativeCarbonPriority < priority) {
negativeCarbonPriority=priority;
negativeCarbon=ringAtom[i];
}}}
if (negativeCarbon != -1) p$1.protectAtom$I.apply(this, [negativeCarbon]);
}}}
}, p$1);

Clazz.newMeth(C$, 'promoteOuterShellDelocalizedRingSystems$com_actelion_research_chem_RingCollection',  function (ringSet) {
var sharedDelocalizedRingCount=Clazz.array(Integer.TYPE, [this.mMol.getBonds$()]);
for (var r=0; r < ringSet.getSize$(); r++) {
var ringBond=ringSet.getRingBonds$I(r);
var isDelocalized=true;
for (var i=0; i < ringBond.length; i++) {
if (!this.mIsDelocalizedBond[ringBond[i]]) {
isDelocalized=false;
break;
}}
if (isDelocalized) for (var i=0; i < ringBond.length; i++) ++sharedDelocalizedRingCount[ringBond[i]];

}
var delocalizedBonds=this.mAromaticBonds;
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (sharedDelocalizedRingCount[bond] == 1) {
for (var i=0; i < 2 && this.mIsDelocalizedBond[bond] ; i++) {
var atom1=this.mMol.getBondAtom$I$I(i, bond);
var atom2=this.mMol.getBondAtom$I$I(1 - i, bond);
if (p$1.hasSharedDelocalizedBond$I$IA.apply(this, [atom1, sharedDelocalizedRingCount]) && !p$1.hasSharedDelocalizedBond$I$IA.apply(this, [atom2, sharedDelocalizedRingCount]) ) {
var connIndex;
while (-1 != (connIndex=p$1.getNextOuterDelocalizedConnIndex$I$I$IA.apply(this, [atom2, atom1, sharedDelocalizedRingCount]))){
var atom3=this.mMol.getConnAtom$I$I(atom2, connIndex);
var bond2to3=this.mMol.getConnBond$I$I(atom2, connIndex);
if (!this.mIsDelocalizedBond[bond2to3]) break;
p$1.promoteBond$I.apply(this, [bond2to3]);
connIndex=p$1.getNextOuterDelocalizedConnIndex$I$I$IA.apply(this, [atom3, atom2, sharedDelocalizedRingCount]);
if (connIndex == -1) break;
atom1=atom3;
atom2=this.mMol.getConnAtom$I$I(atom3, connIndex);
}
}}
}}
return delocalizedBonds != this.mAromaticBonds;
}, p$1);

Clazz.newMeth(C$, 'hasSharedDelocalizedBond$I$IA',  function (atom, sharedDelocalizedRingCount) {
for (var i=0; i < this.mMol.getConnAtoms$I(atom); i++) if (sharedDelocalizedRingCount[this.mMol.getConnBond$I$I(atom, i)] > 1) return true;

return false;
}, p$1);

Clazz.newMeth(C$, 'getNextOuterDelocalizedConnIndex$I$I$IA',  function (atom, previousAtom, sharedDelocalizedRingCount) {
for (var i=0; i < this.mMol.getConnAtoms$I(atom); i++) if (sharedDelocalizedRingCount[this.mMol.getConnBond$I$I(atom, i)] == 1 && this.mMol.getConnAtom$I$I(atom, i) != previousAtom ) return i;

return -1;
}, p$1);

Clazz.newMeth(C$, 'isAromaticAtom$I',  function (atom) {
for (var i=0; i < this.mMol.getConnAtoms$I(atom); i++) if (this.mIsDelocalizedBond[this.mMol.getConnBond$I$I(atom, i)]) return true;

return false;
}, p$1);

Clazz.newMeth(C$, 'protectFullValenceAtoms$Z',  function (mayChangeAtomCharges) {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) if (this.mIsDelocalizedAtom[atom] && this.mMol.getLowestFreeValence$I(atom) == 0  && (!mayChangeAtomCharges || (this.mMol.getAtomicNo$I(atom) == 5 && this.mMol.getAtomCharge$I(atom) < 0 ) || (this.mMol.getAtomicNo$I(atom) == 6 || this.mMol.getAtomicNo$I(atom) == 14 ) || (this.mMol.isElectronegative$I(atom) && this.mMol.getAtomCharge$I(atom) > 0 )  ) ) p$1.protectAtom$I.apply(this, [atom]);

}, p$1);

Clazz.newMeth(C$, 'protectAtom$I',  function (atom) {
if (this.mIsDelocalizedAtom[atom]) {
this.mIsDelocalizedAtom[atom]=false;
--this.mAromaticAtoms;
}for (var i=0; i < this.mMol.getConnAtoms$I(atom); i++) {
var connBond=this.mMol.getConnBond$I$I(atom, i);
if (this.mIsDelocalizedBond[connBond]) {
this.mIsDelocalizedBond[connBond]=false;
--this.mAromaticBonds;
}}
}, p$1);

Clazz.newMeth(C$, 'promoteBond$I',  function (bond) {
if (this.mMol.getBondType$I(bond) == 1) {
this.mMol.setBondType$I$I(bond, 2);
this.mPiElectronsAdded+=2;
}for (var i=0; i < 2; i++) {
var bondAtom=this.mMol.getBondAtom$I$I(i, bond);
this.mIsDelocalizedAtom[bondAtom]=false;
for (var j=0; j < this.mMol.getConnAtoms$I(bondAtom); j++) {
var connBond=this.mMol.getConnBond$I$I(bondAtom, j);
if (this.mIsDelocalizedBond[connBond]) {
this.mIsDelocalizedBond[connBond]=false;
--this.mAromaticBonds;
}}
}
}, p$1);

Clazz.newMeth(C$, 'promoteObviousBonds',  function () {
var terminalAromaticBondFound;
do {
terminalAromaticBondFound=false;
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mIsDelocalizedBond[bond]) {
var isTerminalAromaticBond=false;
for (var i=0; i < 2; i++) {
var bondAtom=this.mMol.getBondAtom$I$I(i, bond);
var aromaticNeighbourFound=false;
for (var j=0; j < this.mMol.getConnAtoms$I(bondAtom); j++) {
if (bond != this.mMol.getConnBond$I$I(bondAtom, j) && this.mIsDelocalizedBond[this.mMol.getConnBond$I$I(bondAtom, j)] ) {
aromaticNeighbourFound=true;
break;
}}
if (!aromaticNeighbourFound) {
isTerminalAromaticBond=true;
break;
}}
if (isTerminalAromaticBond) {
terminalAromaticBondFound=true;
p$1.promoteBond$I.apply(this, [bond]);
}}}
} while (terminalAromaticBondFound);
}, p$1);

Clazz.newMeth(C$, 'promoteDelocalizedChains',  function () {
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mIsDelocalizedBond[bond]) {
for (var i=0; i < 2; i++) {
var terminalAtom=this.mMol.getBondAtom$I$I(i, bond);
var aromaticNeighbourFound=false;
for (var j=0; j < this.mMol.getConnAtoms$I(terminalAtom); j++) {
if (bond != this.mMol.getConnBond$I$I(terminalAtom, j) && this.mIsDelocalizedBond[this.mMol.getConnBond$I$I(terminalAtom, j)] ) {
aromaticNeighbourFound=true;
break;
}}
if (!aromaticNeighbourFound) {
var terminalBond=bond;
var bridgeAtom=this.mMol.getBondAtom$I$I(1 - i, bond);
while (terminalBond != -1){
this.mIsDelocalizedBond[terminalBond]=false;
--this.mAromaticBonds;
this.mMol.setBondType$I$I(terminalBond, 64);
terminalBond=-1;
terminalAtom=bridgeAtom;
for (var j=0; j < this.mMol.getConnAtoms$I(terminalAtom); j++) {
if (this.mIsDelocalizedBond[this.mMol.getConnBond$I$I(terminalAtom, j)]) {
if (terminalBond == -1) {
terminalBond=this.mMol.getConnBond$I$I(terminalAtom, j);
bridgeAtom=this.mMol.getConnAtom$I$I(terminalAtom, j);
} else {
terminalAtom=-1;
terminalBond=-1;
break;
}}}
}
break;
}}
}}
}, p$1);

Clazz.newMeth(C$, 'protectAmideBonds$com_actelion_research_chem_RingCollection',  function (ringSet) {
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mIsDelocalizedBond[bond] && ringSet.qualifiesAsAmideTypeBond$I(bond) ) {
p$1.protectAtom$I.apply(this, [this.mMol.getBondAtom$I$I(0, bond)]);
p$1.protectAtom$I.apply(this, [this.mMol.getBondAtom$I$I(1, bond)]);
}}
}, p$1);

Clazz.newMeth(C$, 'protectDoubleBondAtoms',  function () {
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mMol.getBondOrder$I(bond) == 2) {
for (var i=0; i < 2; i++) {
var atom=this.mMol.getBondAtom$I$I(i, bond);
if (this.mMol.getAtomicNo$I(atom) <= 8) {
for (var j=0; j < this.mMol.getConnAtoms$I(atom); j++) {
var connBond=this.mMol.getConnBond$I$I(atom, j);
if (this.mIsDelocalizedBond[connBond]) {
p$1.protectAtom$I.apply(this, [atom]);
break;
}}
}}
}}
}, p$1);

Clazz.newMeth(C$, 'addObviousAtomCharges$com_actelion_research_chem_RingCollection',  function (ringSet) {
var isDelocalized=Clazz.array(Boolean.TYPE, [ringSet.getSize$()]);
var delocalizedRingCount=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
for (var r=0; r < ringSet.getSize$(); r++) {
isDelocalized[r]=true;
for (var bond, $bond = 0, $$bond = ringSet.getRingBonds$I(r); $bond<$$bond.length&&((bond=($$bond[$bond])),1);$bond++) {
if (!this.mIsDelocalizedBond[bond]) {
isDelocalized[r]=false;
break;
}}
if (isDelocalized[r]) for (var atom, $atom = 0, $$atom = ringSet.getRingAtoms$I(r); $atom<$$atom.length&&((atom=($$atom[$atom])),1);$atom++) ++delocalizedRingCount[atom];

}
var isAromaticRingAtom=Clazz.array(Boolean.TYPE, [this.mMol.getAtoms$()]);
for (var ring=0; ring < ringSet.getSize$(); ring++) {
var ringSize=ringSet.getRingSize$I(ring);
if (ringSize == 3 || ringSize == 5  || ringSize == 6  || ringSize == 7 ) {
if (isDelocalized[ring]) {
for (var atom, $atom = 0, $$atom = ringSet.getRingAtoms$I(ring); $atom<$$atom.length&&((atom=($$atom[$atom])),1);$atom++) isAromaticRingAtom[atom]=true;

var possible=true;
var leakAtom=-1;
var leakPriority=0;
for (var atom, $atom = 0, $$atom = ringSet.getRingAtoms$I(ring); $atom<$$atom.length&&((atom=($$atom[$atom])),1);$atom++) {
if (ringSize == 6 || delocalizedRingCount[atom] > 1 ) {
if (!p$1.checkAtomTypePi1$I$Z.apply(this, [atom, false])) {
possible=false;
break;
}} else {
var priority=(ringSize == 5) ? p$1.checkAtomTypeLeak5$I$Z.apply(this, [atom, false]) : p$1.checkAtomTypeLeak7$I$Z.apply(this, [atom, false]);
if (!p$1.checkAtomTypePi1$I$Z.apply(this, [atom, false])) {
if (leakPriority == 10) {
possible=false;
break;
}leakAtom=atom;
leakPriority=20;
} else if (leakPriority < priority) {
leakPriority=priority;
leakAtom=atom;
}}}
if (possible) {
for (var atom, $atom = 0, $$atom = ringSet.getRingAtoms$I(ring); $atom<$$atom.length&&((atom=($$atom[$atom])),1);$atom++) {
if (atom == leakAtom) {
if (ringSize == 5) p$1.checkAtomTypeLeak5$I$Z.apply(this, [atom, true]);
 else p$1.checkAtomTypeLeak7$I$Z.apply(this, [atom, true]);
p$1.protectAtom$I.apply(this, [atom]);
} else {
p$1.checkAtomTypePi1$I$Z.apply(this, [atom, true]);
}}
}}}}
var delocalizedNeighbourCount=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
var hasMetalLigandBond=Clazz.array(Boolean.TYPE, [this.mMol.getAtoms$()]);
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
var atom1=this.mMol.getBondAtom$I$I(0, bond);
var atom2=this.mMol.getBondAtom$I$I(1, bond);
if (!isAromaticRingAtom[atom1] && !isAromaticRingAtom[atom2] ) {
if (this.mIsDelocalizedBond[bond]) {
++delocalizedNeighbourCount[atom1];
++delocalizedNeighbourCount[atom2];
}if (this.mMol.getBondType$I(bond) == 32) {
hasMetalLigandBond[atom1]=true;
hasMetalLigandBond[atom2]=true;
}}}
var priority=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
var graphAtom=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
for (var seedAtom=0; seedAtom < this.mMol.getAtoms$(); seedAtom++) {
if (delocalizedNeighbourCount[seedAtom] == 1) {
graphAtom[0]=seedAtom;
var current=0;
var highest=0;
while (current <= highest){
for (var i=0; i < this.mMol.getConnAtoms$I(graphAtom[current]); i++) {
if (this.mIsDelocalizedBond[this.mMol.getConnBond$I$I(graphAtom[current], i)]) {
var candidate=this.mMol.getConnAtom$I$I(graphAtom[current], i);
if ((current == 0 || candidate != graphAtom[current - 1] ) && delocalizedNeighbourCount[candidate] != 0 ) {
graphAtom[++highest]=candidate;
if ((delocalizedNeighbourCount[candidate] & 1) != 0) {
for (var j=1; j < highest; j+=2) priority[graphAtom[j]]=-1;

highest=0;
}break;
}}}
++current;
}
}}
var atomHandled=Clazz.array(Boolean.TYPE, [this.mMol.getAtoms$()]);
for (var seedAtom=0; seedAtom < this.mMol.getAtoms$(); seedAtom++) {
if (!atomHandled[seedAtom] && delocalizedNeighbourCount[seedAtom] != 0 ) {
graphAtom[0]=seedAtom;
atomHandled[seedAtom]=true;
var current=0;
var highest=0;
while (current <= highest){
for (var i=0; i < this.mMol.getConnAtoms$I(graphAtom[current]); i++) {
if (this.mIsDelocalizedBond[this.mMol.getConnBond$I$I(graphAtom[current], i)]) {
var candidate=this.mMol.getConnAtom$I$I(graphAtom[current], i);
if (!atomHandled[candidate]) {
graphAtom[++highest]=candidate;
atomHandled[candidate]=true;
}}}
++current;
}
if ((highest & 1) == 0) {
for (var i=0; i <= highest; i++) if (priority[graphAtom[i]] == 0) priority[graphAtom[i]]=p$1.checkAtomTypeLeakNonRing$I$Z.apply(this, [graphAtom[i], false]);

var isPossible=true;
for (var i=0; i <= highest; i++) {
if (priority[graphAtom[i]] <= 0) {
if (!p$1.checkAtomTypePi1$I$Z.apply(this, [graphAtom[i], false])) {
isPossible=false;
break;
}}}
if (isPossible) {
var maxPriority=0;
var maxAtom=-1;
for (var i=0; i <= highest; i++) {
if (maxPriority < priority[graphAtom[i]]) {
maxPriority=priority[graphAtom[i]];
maxAtom=graphAtom[i];
}}
if (maxPriority > 0) {
p$1.checkAtomTypeLeakNonRing$I$Z.apply(this, [maxAtom, true]);
p$1.protectAtom$I.apply(this, [maxAtom]);
}}}}}
}, p$1);

Clazz.newMeth(C$, 'checkAtomTypePi1$I$Z',  function (atom, correctCharge) {
var atomicNo=this.mMol.getAtomicNo$I(atom);
if ((atomicNo >= 5 && atomicNo <= 8 ) || atomicNo == 15  || atomicNo == 16  || atomicNo == 33  || atomicNo == 34  || atomicNo == 52 ) {
var freeValence=this.mMol.getLowestFreeValence$I(atom);
if (freeValence != 0) return true;
var charge=this.mMol.getAtomCharge$I(atom);
if (atomicNo == 5 && charge >= 0 ) {
if (correctCharge) this.mMol.setAtomCharge$I$I(atom, charge - 1);
return true;
}if (atomicNo != 5 && charge <= 0 ) {
if (correctCharge) this.mMol.setAtomCharge$I$I(atom, charge + 1);
return true;
}}return false;
}, p$1);

Clazz.newMeth(C$, 'checkAtomTypeLeak5$I$Z',  function (atom, correctCharge) {
if (this.mMol.getAtomicNo$I(atom) == 7) {
if (this.mMol.getAllConnAtoms$I(atom) == 3) return 6;
 else if (this.mMol.getConnAtoms$I(atom) == 2) return 4;
} else if (this.mMol.getAtomicNo$I(atom) == 8) {
return 10;
} else if (this.mMol.getAtomicNo$I(atom) == 15 || this.mMol.getAtomicNo$I(atom) == 33 ) {
if (this.mMol.getConnAtoms$I(atom) == 3) return 8;
} else if (this.mMol.getAtomicNo$I(atom) == 16 || this.mMol.getAtomicNo$I(atom) == 34  || this.mMol.getAtomicNo$I(atom) == 52 ) {
if (this.mMol.getConnAtoms$I(atom) == 2) return 12;
} else if (this.mMol.getAtomicNo$I(atom) == 6) {
if (correctCharge) this.mMol.setAtomCharge$I$I(atom, -1);
return (this.mMol.getAllConnAtoms$I(atom) != this.mMol.getAllConnAtomsPlusMetalBonds$I(atom)) ? 2 : 3;
}return 0;
}, p$1);

Clazz.newMeth(C$, 'checkAtomTypeLeak7$I$Z',  function (atom, correctCharge) {
if (this.mAllHydrogensAreExplicit) {
if (this.mMol.getAllConnAtoms$I(atom) != 3) return 0;
} else {
if (this.mMol.getAllConnAtoms$I(atom) > 3) return 0;
}if (this.mMol.getAtomicNo$I(atom) == 6) {
if (correctCharge) this.mMol.setAtomCharge$I$I(atom, 1);
return 2;
}if (this.mMol.getAtomicNo$I(atom) == 5) {
return 4;
}return 0;
}, p$1);

Clazz.newMeth(C$, 'checkAtomTypeLeakNonRing$I$Z',  function (atom, correctCharge) {
if (this.mMol.getAtomCharge$I(atom) != 0) return 0;
if (this.mAllHydrogensAreExplicit) {
if (this.mMol.getAtomicNo$I(atom) == 5) {
if (this.mMol.getOccupiedValence$I(atom) != 2) return 0;
if (correctCharge) this.mMol.setAtomCharge$I$I(atom, 1);
return 1;
}if (this.mMol.getAtomicNo$I(atom) == 7) {
if (this.mMol.getOccupiedValence$I(atom) != 2) return 0;
if (correctCharge) this.mMol.setAtomCharge$I$I(atom, -1);
return p$1.hasMetalNeighbour$I.apply(this, [atom]) ? 6 : 3;
}if (this.mMol.getAtomicNo$I(atom) == 8) {
if (this.mMol.getOccupiedValence$I(atom) != 1) return 0;
if (correctCharge) this.mMol.setAtomCharge$I$I(atom, -1);
return p$1.hasMetalNeighbour$I.apply(this, [atom]) ? 7 : 4;
}if (this.mMol.getAtomicNo$I(atom) == 16) {
if (this.mMol.getOccupiedValence$I(atom) != 1) return 0;
if (correctCharge) this.mMol.setAtomCharge$I$I(atom, -1);
return p$1.hasMetalNeighbour$I.apply(this, [atom]) ? 5 : 2;
}if (this.mMol.getAtomicNo$I(atom) == 34) {
if (this.mMol.getOccupiedValence$I(atom) != 1) return 0;
if (correctCharge) this.mMol.setAtomCharge$I$I(atom, -1);
return p$1.hasMetalNeighbour$I.apply(this, [atom]) ? 4 : 1;
}} else {
if (this.mMol.getAtomicNo$I(atom) == 5) {
if (this.mMol.getOccupiedValence$I(atom) > 2) return 0;
if (correctCharge) this.mMol.setAtomCharge$I$I(atom, 1);
return 1;
}if (this.mMol.getAtomicNo$I(atom) == 7) {
if (this.mMol.getOccupiedValence$I(atom) > 2) return 0;
if (correctCharge) this.mMol.setAtomCharge$I$I(atom, -1);
return p$1.hasMetalNeighbour$I.apply(this, [atom]) ? 5 : 3;
}if (this.mMol.getAtomicNo$I(atom) == 8) {
if (this.mMol.getOccupiedValence$I(atom) > 1) return 0;
if (correctCharge) this.mMol.setAtomCharge$I$I(atom, -1);
return p$1.hasMetalNeighbour$I.apply(this, [atom]) ? 7 : 4;
}if (this.mMol.getAtomicNo$I(atom) == 16) {
if (this.mMol.getOccupiedValence$I(atom) > 1) return 0;
if (correctCharge) this.mMol.setAtomCharge$I$I(atom, -1);
return p$1.hasMetalNeighbour$I.apply(this, [atom]) ? 5 : 2;
}}return 0;
}, p$1);

Clazz.newMeth(C$, 'hasMetalNeighbour$I',  function (atom) {
for (var i=0; i < this.mMol.getConnAtoms$I(atom); i++) if (this.mMol.isMetalAtom$I(this.mMol.getConnAtom$I$I(atom, i))) return true;

return false;
}, p$1);

Clazz.newMeth(C$);
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-25 13:07:43 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
