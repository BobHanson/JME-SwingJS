(function(){var P$=Clazz.newPackage("com.actelion.research.chem"),p$1={},I$=[[0,'java.util.ArrayList','com.actelion.research.chem.Molecule']],I$0=I$[0],$I$=function(i,n,m){return m?$I$(i)[n].apply(null,m):((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "RingCollection");

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['I',['mMaxSmallRingSize'],'O',['mMol','com.actelion.research.chem.ExtendedMolecule','mRingAtomSet','java.util.ArrayList','+mRingBondSet','mAtomRingFeatures','int[]','+mBondRingFeatures','+mHeteroPosition','mIsAromatic','boolean[]','+mIsDelocalized']]]

Clazz.newMeth(C$, 'c$$com_actelion_research_chem_ExtendedMolecule$I',  function (mol, mode) {
C$.c$$com_actelion_research_chem_ExtendedMolecule$I$I.apply(this, [mol, mode, 7]);
}, 1);

Clazz.newMeth(C$, 'c$$com_actelion_research_chem_ExtendedMolecule$I$I',  function (mol, mode, maxSmallRingSize) {
;C$.$init$.apply(this);
this.mMol=mol;
this.mMaxSmallRingSize=maxSmallRingSize;
this.mRingAtomSet=Clazz.new_($I$(1,1));
this.mRingBondSet=Clazz.new_($I$(1,1));
this.mAtomRingFeatures=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
this.mBondRingFeatures=Clazz.array(Integer.TYPE, [this.mMol.getBonds$()]);
this.mMol.ensureHelperArrays$I(1);
var isConfirmedChainAtom=Clazz.array(Boolean.TYPE, [this.mMol.getAtoms$()]);
var isConfirmedChainBond=Clazz.array(Boolean.TYPE, [this.mMol.getBonds$()]);
var found;
do {
found=false;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (!isConfirmedChainAtom[atom]) {
var potentialRingNeighbours=0;
for (var i=0; i < this.mMol.getConnAtoms$I(atom); i++) if (!isConfirmedChainAtom[this.mMol.getConnAtom$I$I(atom, i)]) ++potentialRingNeighbours;

if (potentialRingNeighbours < 2) {
isConfirmedChainAtom[atom]=true;
for (var i=0; i < this.mMol.getConnAtoms$I(atom); i++) isConfirmedChainBond[this.mMol.getConnBond$I$I(atom, i)]=true;

found=true;
}}}
} while (found);
var startAtom=0;
while ((startAtom < this.mMol.getAtoms$()) && isConfirmedChainAtom[startAtom] )++startAtom;

if (startAtom == this.mMol.getAtoms$()) return;
var graphAtom=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
graphAtom[0]=startAtom;
var parent=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
parent[0]=-1;
var fragmentNo=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
fragmentNo[startAtom]=1;
var current=0;
var highest=0;
var noOfFragments=1;
while (current <= highest){
for (var i=0; i < this.mMol.getConnAtoms$I(graphAtom[current]); i++) {
var candidate=this.mMol.getConnAtom$I$I(graphAtom[current], i);
if (candidate == parent[graphAtom[current]]) continue;
if (fragmentNo[candidate] != 0) {
p$1.addSmallRingsToSet$I$ZA.apply(this, [this.mMol.getConnBond$I$I(graphAtom[current], i), isConfirmedChainAtom]);
continue;
}if (!isConfirmedChainAtom[candidate]) {
fragmentNo[candidate]=noOfFragments;
parent[candidate]=graphAtom[current];
graphAtom[++highest]=candidate;
}}
++current;
if (current > highest) {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (fragmentNo[atom] == 0 && !isConfirmedChainAtom[atom] ) {
fragmentNo[atom]=++noOfFragments;
graphAtom[++highest]=atom;
parent[atom]=-1;
break;
}}
}}
if ((mode & 4) != 0) {
this.mIsAromatic=Clazz.array(Boolean.TYPE, [this.mRingAtomSet.size$()]);
this.mIsDelocalized=Clazz.array(Boolean.TYPE, [this.mRingAtomSet.size$()]);
this.mHeteroPosition=Clazz.array(Integer.TYPE, [this.mRingAtomSet.size$()]);
this.determineAromaticity$ZA$ZA$IA$Z(this.mIsAromatic, this.mIsDelocalized, this.mHeteroPosition, (mode & 8) != 0);
p$1.updateAromaticity.apply(this, []);
}if ((mode & 2) != 0) {
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (!isConfirmedChainBond[bond] && this.mMol.getBondOrder$I(bond) != 0 ) {
var ringAtom=p$1.findSmallestRing$I$ZA.apply(this, [bond, isConfirmedChainAtom]);
if (ringAtom != null ) p$1.updateRingSize$IA$IA.apply(this, [ringAtom, p$1.getRingBonds$IA.apply(this, [ringAtom])]);
}}
}}, 1);

Clazz.newMeth(C$, 'findSmallestRing$I$ZA',  function (bond, isConfirmedChainAtom) {
var atom1=this.mMol.getBondAtom$I$I(0, bond);
var atom2=this.mMol.getBondAtom$I$I(1, bond);
var graphAtom=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
var graphLevel=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
var graphParent=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
graphAtom[0]=atom1;
graphAtom[1]=atom2;
graphLevel[atom1]=1;
graphLevel[atom2]=2;
graphParent[atom1]=-1;
graphParent[atom2]=atom1;
var current=1;
var highest=1;
while (current <= highest){
for (var i=0; i < this.mMol.getConnAtoms$I(graphAtom[current]); i++) {
var candidate=this.mMol.getConnAtom$I$I(graphAtom[current], i);
if ((current > 1) && candidate == atom1 ) {
var ringAtom=Clazz.array(Integer.TYPE, [graphLevel[graphAtom[current]]]);
var atom=graphAtom[current];
for (var j=0; j < ringAtom.length; j++) {
ringAtom[j]=atom;
atom=graphParent[atom];
}
return ringAtom;
}if (graphLevel[candidate] == 0 && !isConfirmedChainAtom[candidate] ) {
graphAtom[++highest]=candidate;
graphLevel[candidate]=graphLevel[graphAtom[current]] + 1;
graphParent[candidate]=graphAtom[current];
}}
++current;
}
return null;
}, p$1);

Clazz.newMeth(C$, 'getAtomRingSize$I',  function (atom) {
return this.mAtomRingFeatures[atom] & 65535;
});

Clazz.newMeth(C$, 'getBondRingSize$I',  function (bond) {
return this.mBondRingFeatures[bond] & 65535;
});

Clazz.newMeth(C$, 'addSmallRingsToSet$I$ZA',  function (closureBond, isConfirmedChainAtom) {
var graphAtom=Clazz.array(Integer.TYPE, [this.mMaxSmallRingSize]);
var connIndex=Clazz.array(Integer.TYPE, [this.mMaxSmallRingSize]);
var isUsed=Clazz.array(Boolean.TYPE, [this.mMol.getAtoms$()]);
var atom1=this.mMol.getBondAtom$I$I(0, closureBond);
var atom2=this.mMol.getBondAtom$I$I(1, closureBond);
graphAtom[0]=atom1;
graphAtom[1]=atom2;
connIndex[1]=-1;
isUsed[atom2]=true;
var current=1;
while (current >= 1){
++connIndex[current];
if (connIndex[current] == this.mMol.getConnAtoms$I(graphAtom[current])) {
isUsed[graphAtom[current]]=false;
--current;
continue;
}var candidate=this.mMol.getConnAtom$I$I(graphAtom[current], connIndex[current]);
if (isUsed[candidate] || isConfirmedChainAtom[candidate] ) continue;
if (candidate == atom1 && current > 1 ) {
p$1.addRingIfNew$IA$I.apply(this, [graphAtom, current + 1]);
if (this.mRingAtomSet.size$() >= 1024) return;
continue;
}if (current + 1 < this.mMaxSmallRingSize) {
++current;
graphAtom[current]=candidate;
isUsed[candidate]=true;
connIndex[current]=-1;
}}
}, p$1);

Clazz.newMeth(C$, 'addRingIfNew$IA$I',  function (ringAtom, ringSize) {
var lowAtom=this.mMol.getMaxAtoms$();
var lowIndex=0;
for (var i=0; i < ringSize; i++) {
if (lowAtom > ringAtom[i]) {
lowAtom=ringAtom[i];
lowIndex=i;
}}
var sortedRing=Clazz.array(Integer.TYPE, [ringSize]);
var leftIndex=(lowIndex > 0) ? lowIndex - 1 : ringSize - 1;
var rightIndex=(lowIndex < ringSize - 1) ? lowIndex + 1 : 0;
var inverse=(ringAtom[leftIndex] < ringAtom[rightIndex]);
for (var i=0; i < ringSize; i++) {
sortedRing[i]=ringAtom[lowIndex];
if (inverse) {
if (--lowIndex < 0) lowIndex=ringSize - 1;
} else {
if (++lowIndex == ringSize) lowIndex=0;
}}
for (var i=0; i < this.mRingAtomSet.size$(); i++) {
var ringOfSet=this.mRingAtomSet.get$I(i);
if (ringOfSet.length != ringSize) continue;
var equal=true;
for (var j=0; j < ringSize; j++) {
if (ringOfSet[j] != sortedRing[j]) {
equal=false;
break;
}}
if (equal) return;
}
this.mRingAtomSet.add$O(sortedRing);
var ringBond=p$1.getRingBonds$IA.apply(this, [sortedRing]);
this.mRingBondSet.add$O(ringBond);
p$1.updateRingSize$IA$IA.apply(this, [sortedRing, ringBond]);
}, p$1);

Clazz.newMeth(C$, 'getSize$',  function () {
return this.mRingAtomSet.size$();
});

Clazz.newMeth(C$, 'getRingAtoms$I',  function (ringNo) {
return this.mRingAtomSet.get$I(ringNo);
});

Clazz.newMeth(C$, 'getRingBonds$I',  function (ringNo) {
return this.mRingBondSet.get$I(ringNo);
});

Clazz.newMeth(C$, 'getRingSize$I',  function (ringNo) {
return this.mRingBondSet.get$I(ringNo).length;
});

Clazz.newMeth(C$, 'isAromatic$I',  function (ringNo) {
return this.mIsAromatic[ringNo];
});

Clazz.newMeth(C$, 'isAromaticAtom$I',  function (atom) {
return (this.mAtomRingFeatures[atom] & 65536) != 0;
});

Clazz.newMeth(C$, 'isDelocalizedAtom$I',  function (atom) {
return (this.mAtomRingFeatures[atom] & 131072) != 0;
});

Clazz.newMeth(C$, 'isHeteroAromaticAtom$I',  function (atom) {
return (this.mAtomRingFeatures[atom] & 262144) != 0;
});

Clazz.newMeth(C$, 'isAromaticBond$I',  function (bond) {
return (this.mBondRingFeatures[bond] & 65536) != 0;
});

Clazz.newMeth(C$, 'isDelocalizedBond$I',  function (bond) {
return (this.mBondRingFeatures[bond] & 131072) != 0;
});

Clazz.newMeth(C$, 'isHeteroAromaticBond$I',  function (bond) {
return (this.mBondRingFeatures[bond] & 262144) != 0;
});

Clazz.newMeth(C$, 'isDelocalized$I',  function (ringNo) {
return this.mIsDelocalized[ringNo];
});

Clazz.newMeth(C$, 'getAtomIndex$I$I',  function (ringNo, atom) {
var ringAtom=this.mRingAtomSet.get$I(ringNo);
for (var i=0; i < ringAtom.length; i++) if (atom == ringAtom[i]) return i;

return -1;
});

Clazz.newMeth(C$, 'getBondIndex$I$I',  function (ringNo, bond) {
var ringBond=this.mRingBondSet.get$I(ringNo);
for (var i=0; i < ringBond.length; i++) if (bond == ringBond[i]) return i;

return -1;
});

Clazz.newMeth(C$, 'validateMemberIndex$I$I',  function (ringNo, index) {
var ringSize=this.mRingBondSet.get$I(ringNo).length;
while (index >= ringSize)index-=ringSize;

while (index < 0)index+=ringSize;

return index;
});

Clazz.newMeth(C$, 'getHeteroPosition$I',  function (ringNo) {
return this.mHeteroPosition[ringNo];
});

Clazz.newMeth(C$, 'isAtomMember$I$I',  function (ringNo, atom) {
var ringAtom=this.mRingAtomSet.get$I(ringNo);
for (var i=0; i < ringAtom.length; i++) if (atom == ringAtom[i]) return true;

return false;
});

Clazz.newMeth(C$, 'isBondMember$I$I',  function (ringNo, bond) {
var ringBond=this.mRingBondSet.get$I(ringNo);
for (var i=0; i < ringBond.length; i++) if (bond == ringBond[i]) return true;

return false;
});

Clazz.newMeth(C$, 'getSharedRing$I$I',  function (bond1, bond2) {
for (var i=0; i < this.mRingBondSet.size$(); i++) if (this.isBondMember$I$I(i, bond1) && this.isBondMember$I$I(i, bond2) ) return i;

return -1;
});

Clazz.newMeth(C$, 'updateRingSize$IA$IA',  function (ringAtom, ringBond) {
var ringSize=ringAtom.length;
for (var i=0; i < ringSize; i++) {
var currentSize=this.mAtomRingFeatures[ringAtom[i]] & 65535;
if (currentSize == 0 || currentSize > ringSize ) {
this.mAtomRingFeatures[ringAtom[i]]&=~65535;
this.mAtomRingFeatures[ringAtom[i]]|=ringSize;
}}
for (var i=0; i < ringSize; i++) {
var currentSize=this.mBondRingFeatures[ringBond[i]] & 65535;
if (currentSize == 0 || currentSize > ringSize ) {
this.mBondRingFeatures[ringBond[i]]&=~65535;
this.mBondRingFeatures[ringBond[i]]|=ringSize;
}}
}, p$1);

Clazz.newMeth(C$, 'updateAromaticity',  function () {
for (var ring=0; ring < this.mIsAromatic.length; ring++) {
if (this.mIsAromatic[ring]) {
var isHeteroAromatic=false;
for (var atom, $atom = 0, $$atom = this.mRingAtomSet.get$I(ring); $atom<$$atom.length&&((atom=($$atom[$atom])),1);$atom++) {
this.mAtomRingFeatures[atom]|=65536;
if (p$1.qualifiesAsHeteroAtom$I.apply(this, [atom])) isHeteroAromatic=true;
}
for (var bond, $bond = 0, $$bond = this.mRingBondSet.get$I(ring); $bond<$$bond.length&&((bond=($$bond[$bond])),1);$bond++) this.mBondRingFeatures[bond]|=65536;

if (this.mIsDelocalized[ring]) {
for (var atom, $atom = 0, $$atom = this.mRingAtomSet.get$I(ring); $atom<$$atom.length&&((atom=($$atom[$atom])),1);$atom++) this.mAtomRingFeatures[atom]|=131072;

for (var bond, $bond = 0, $$bond = this.mRingBondSet.get$I(ring); $bond<$$bond.length&&((bond=($$bond[$bond])),1);$bond++) this.mBondRingFeatures[bond]|=131072;

}if (isHeteroAromatic) {
for (var atom, $atom = 0, $$atom = this.mRingAtomSet.get$I(ring); $atom<$$atom.length&&((atom=($$atom[$atom])),1);$atom++) this.mAtomRingFeatures[atom]|=262144;

for (var bond, $bond = 0, $$bond = this.mRingBondSet.get$I(ring); $bond<$$bond.length&&((bond=($$bond[$bond])),1);$bond++) this.mBondRingFeatures[bond]|=262144;

}}}
}, p$1);

Clazz.newMeth(C$, 'qualifiesAsHeteroAtom$I',  function (atom) {
if (this.mMol.isFragment$()) {
if (Long.$ne((Long.$and(this.mMol.getAtomQueryFeatures$I(atom),1)),0 )) return false;
var atomList=this.mMol.getAtomList$I(atom);
if (atomList != null ) {
for (var atomicNo, $atomicNo = 0, $$atomicNo = atomList; $atomicNo<$$atomicNo.length&&((atomicNo=($$atomicNo[$atomicNo])),1);$atomicNo++) if (!$I$(2).isAtomicNoElectronegative$I(atomicNo)) return false;

return true;
}}return $I$(2,"isAtomicNoElectronegative$I",[this.mMol.getAtomicNo$I(atom)]);
}, p$1);

Clazz.newMeth(C$, 'getRingBonds$IA',  function (ringAtom) {
var ringAtoms=ringAtom.length;
var ringBond=Clazz.array(Integer.TYPE, [ringAtoms]);
for (var i=0; i < ringAtoms; i++) {
var atom=(i == ringAtoms - 1) ? ringAtom[0] : ringAtom[i + 1];
for (var j=0; j < this.mMol.getConnAtoms$I(ringAtom[i]); j++) {
if (this.mMol.getConnAtom$I$I(ringAtom[i], j) == atom) {
ringBond[i]=this.mMol.getConnBond$I$I(ringAtom[i], j);
break;
}}
}
return ringBond;
}, p$1);

Clazz.newMeth(C$, 'determineAromaticity$ZA$ZA$IA$Z',  function (isAromatic, isDelocalized, heteroPosition, includeTautomericBonds) {
var annelatedRing=Clazz.array(Integer.TYPE, [this.mRingAtomSet.size$(), null]);
for (var i=0; i < this.mRingAtomSet.size$(); i++) {
annelatedRing[i]=Clazz.array(Integer.TYPE, [this.mRingAtomSet.get$I(i).length]);
for (var j=0; j < this.mRingAtomSet.get$I(i).length; j++) annelatedRing[i][j]=-1;

}
var ringMembership=Clazz.array(Integer.TYPE, [this.mMol.getBonds$()]);
for (var ring=0; ring < this.mRingBondSet.size$(); ring++) {
var ringBond=this.mRingBondSet.get$I(ring);
if (ringBond.length == 3 || (ringBond.length >= 5 && ringBond.length <= 7 ) ) {
for (var i=0; i < ringBond.length; i++) {
var bond=ringBond[i];
if (this.mMol.getConnAtoms$I(this.mMol.getBondAtom$I$I(0, bond)) == 3 && this.mMol.getConnAtoms$I(this.mMol.getBondAtom$I$I(1, bond)) == 3 ) {
if (ringMembership[bond] > 0) {
annelatedRing[ringMembership[bond] >>> 16][ringMembership[bond] & 32767]=ring;
annelatedRing[ring][i]=(ringMembership[bond] >>> 16);
} else {
ringMembership[bond]=(ring << 16) + 32768 + i ;
}}}
}}
var aromaticityHandled=Clazz.array(Boolean.TYPE, [this.mRingAtomSet.size$()]);
var ringsHandled=0;
var lastRingsHandled=-1;
while (ringsHandled > lastRingsHandled){
lastRingsHandled=ringsHandled;
for (var ring=0; ring < this.mRingAtomSet.size$(); ring++) {
if (!aromaticityHandled[ring]) {
if (p$1.determineAromaticity$I$IAA$ZA$ZA$ZA$IA$Z.apply(this, [ring, annelatedRing, aromaticityHandled, isAromatic, isDelocalized, heteroPosition, includeTautomericBonds])) {
aromaticityHandled[ring]=true;
++ringsHandled;
}}}
}
});

Clazz.newMeth(C$, 'determineAromaticity$I$IAA$ZA$ZA$ZA$IA$Z',  function (ringNo, annelatedRing, aromaticityHandled, isAromatic, isDelocalized, heteroPosition, includeTautomericBonds) {
var ringAtom=this.mRingAtomSet.get$I(ringNo);
for (var atom, $atom = 0, $$atom = ringAtom; $atom<$$atom.length&&((atom=($$atom[$atom])),1);$atom++) if (!p$1.qualifiesAsAromaticAtom$I.apply(this, [atom])) return true;

var ringBond=this.mRingBondSet.get$I(ringNo);
var ringBonds=ringBond.length;
var bondSequence=0;
var aromaticButNotDelocalizedSequence=0;
var unhandledAnnelatedRingFound=false;
for (var i=0; i < ringBonds; i++) {
bondSequence<<=1;
aromaticButNotDelocalizedSequence<<=1;
if (p$1.qualifiesAsPiBond$I.apply(this, [ringBond[i]])) {
bondSequence|=1;
} else if (includeTautomericBonds && this.qualifiesAsAmideTypeBond$I(ringBond[i]) ) {
bondSequence|=1;
aromaticButNotDelocalizedSequence|=1;
} else {
var annelated=annelatedRing[ringNo][i];
if (annelated != -1) {
if (aromaticityHandled[annelated]) {
if (isAromatic[annelated]) {
bondSequence|=1;
if (!isDelocalized[annelated]) aromaticButNotDelocalizedSequence|=1;
}} else {
unhandledAnnelatedRingFound=true;
}}}}
var hasDelocalizationLeak=false;
switch (ringBonds) {
case 3:
var cSequence3Ring=Clazz.array(Integer.TYPE, -1, [2, 1, 4]);
hasDelocalizationLeak=true;
for (var carbeniumPosition=0; carbeniumPosition < 3; carbeniumPosition++) {
if ((bondSequence & cSequence3Ring[carbeniumPosition]) == cSequence3Ring[carbeniumPosition]) {
if ((this.mMol.getAtomicNo$I(ringAtom[carbeniumPosition]) == 6 && this.mMol.getAtomCharge$I(ringAtom[carbeniumPosition]) == 1 ) || (this.mMol.getAtomicNo$I(ringAtom[carbeniumPosition]) == 5 && this.mMol.getAtomCharge$I(ringAtom[carbeniumPosition]) == 0 ) ) {
isAromatic[ringNo]=true;
heteroPosition[ringNo]=carbeniumPosition;
if ((aromaticButNotDelocalizedSequence & cSequence3Ring[carbeniumPosition]) == 0) hasDelocalizationLeak=false;
}}}
break;
case 5:
var cSequence5Ring=Clazz.array(Integer.TYPE, -1, [10, 5, 18, 9, 20]);
hasDelocalizationLeak=true;
for (var position=0; position < 5; position++) {
if ((bondSequence & cSequence5Ring[position]) == cSequence5Ring[position]) {
switch (this.mMol.getAtomicNo$I(ringAtom[position])) {
case 6:
if (this.mMol.getAtomCharge$I(ringAtom[position]) == -1) {
isAromatic[ringNo]=true;
heteroPosition[ringNo]=position;
if ((aromaticButNotDelocalizedSequence & cSequence5Ring[position]) == 0) hasDelocalizationLeak=false;
}break;
case 7:
if (this.mMol.getAtomCharge$I(ringAtom[position]) <= 0) {
isAromatic[ringNo]=true;
heteroPosition[ringNo]=position;
}break;
case 8:
isAromatic[ringNo]=true;
heteroPosition[ringNo]=position;
break;
case 16:
case 34:
case 52:
if (this.mMol.getConnAtoms$I(ringAtom[position]) == 2) {
isAromatic[ringNo]=true;
heteroPosition[ringNo]=position;
}break;
}
}}
break;
case 6:
hasDelocalizationLeak=true;
if ((bondSequence & 21) == 21) {
isAromatic[ringNo]=true;
if ((aromaticButNotDelocalizedSequence & 21) == 0) hasDelocalizationLeak=false;
}if ((bondSequence & 42) == 42) {
isAromatic[ringNo]=true;
if ((aromaticButNotDelocalizedSequence & 42) == 0) hasDelocalizationLeak=false;
}break;
case 7:
var cSequence7Ring=Clazz.array(Integer.TYPE, -1, [42, 21, 74, 37, 82, 41, 84]);
hasDelocalizationLeak=true;
for (var carbeniumPosition=0; carbeniumPosition < 7; carbeniumPosition++) {
if ((bondSequence & cSequence7Ring[carbeniumPosition]) == cSequence7Ring[carbeniumPosition]) {
if ((this.mMol.getAtomicNo$I(ringAtom[carbeniumPosition]) == 6 && this.mMol.getAtomCharge$I(ringAtom[carbeniumPosition]) == 1 ) || (this.mMol.getAtomicNo$I(ringAtom[carbeniumPosition]) == 5 && this.mMol.getAtomCharge$I(ringAtom[carbeniumPosition]) == 0 ) ) {
isAromatic[ringNo]=true;
heteroPosition[ringNo]=carbeniumPosition;
if ((aromaticButNotDelocalizedSequence & cSequence7Ring[carbeniumPosition]) == 0) hasDelocalizationLeak=false;
}}}
break;
}
if (isAromatic[ringNo] && !hasDelocalizationLeak ) isDelocalized[ringNo]=true;
if (isAromatic[ringNo]) return true;
return !unhandledAnnelatedRingFound;
}, p$1);

Clazz.newMeth(C$, 'qualifiesAsPiBond$I',  function (bond) {
return (this.mMol.getBondOrder$I(bond) > 1 || this.mMol.getBondType$I(bond) == 64 );
}, p$1);

Clazz.newMeth(C$, 'qualifiesAsAmideTypeBond$I',  function (bond) {
for (var i=0; i < 2; i++) {
var atom1=this.mMol.getBondAtom$I$I(i, bond);
if ((this.mMol.getAtomicNo$I(atom1) == 7) && this.mMol.getConnAtoms$I(atom1) == 2 ) {
var atom2=this.mMol.getBondAtom$I$I(1 - i, bond);
if (this.mMol.getAtomicNo$I(atom2) == 6) {
for (var j=0; j < this.mMol.getConnAtoms$I(atom2); j++) {
var connAtom=this.mMol.getConnAtom$I$I(atom2, j);
var connBond=this.mMol.getConnBond$I$I(atom2, j);
if ((this.mMol.getAtomicNo$I(connAtom) == 8 || this.mMol.getAtomicNo$I(connAtom) == 16 ) && this.mMol.getBondOrder$I(connBond) == 2  && this.mMol.getConnAtoms$I(connAtom) == 1 ) return true;
}
}}}
return false;
});

Clazz.newMeth(C$, 'qualifiesAsAromaticAtom$I',  function (atom) {
if (this.mMol.isFragment$()) {
if (Long.$ne((Long.$and(this.mMol.getAtomQueryFeatures$I(atom),1)),0 )) {
return true;
} else {
var list=this.mMol.getAtomList$I(atom);
if (list != null ) {
for (var atomicNo, $atomicNo = 0, $$atomicNo = list; $atomicNo<$$atomicNo.length&&((atomicNo=($$atomicNo[$atomicNo])),1);$atomicNo++) if (C$.qualifiesAsAromaticAtomicNo$I(atomicNo)) return true;

return false;
}}}return C$.qualifiesAsAromaticAtomicNo$I(this.mMol.getAtomicNo$I(atom));
}, p$1);

Clazz.newMeth(C$, 'qualifiesAsAromaticAtomicNo$I',  function (atomicNo) {
return atomicNo == 5 || atomicNo == 6  || atomicNo == 7  || atomicNo == 8  || atomicNo == 15  || atomicNo == 16  || atomicNo == 33  || atomicNo == 34 ;
}, 1);

Clazz.newMeth(C$);
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-25 13:07:45 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
