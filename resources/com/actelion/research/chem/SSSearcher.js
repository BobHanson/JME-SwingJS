(function(){var P$=Clazz.newPackage("com.actelion.research.chem"),p$1={},I$=[[0,'java.util.TreeSet','com.actelion.research.util.IntArrayComparator','java.util.ArrayList','java.util.Arrays','com.actelion.research.chem.StereoMolecule',['com.actelion.research.chem.SSSearcher','.BridgeBond']]],I$0=I$[0],$I$=function(i,n){return((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "SSSearcher", function(){
Clazz.newInstance(this, arguments,0,C$);
});
C$.$classes$=[['BridgeBond',2]];

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['Z',['mMoleculeFeaturesValid','mFragmentFeaturesValid','mStop'],'I',['mDefaultMatchMode','mFragmentExcludeAtoms','mFragmentExcludeBonds','mFragmentGraphSize','mFragmentGraphSizeWithExcludeGroups','mRequiredHelperLevel','mExcludeGroupCount'],'O',['mMolecule','com.actelion.research.chem.StereoMolecule','+mFragment','mMoleculeAtomType','int[]','+mFragmentAtomType','mMoleculeAtomFeatures','long[]','+mFragmentAtomFeatures','+mMoleculeRingFeatures','+mFragmentRingFeatures','mMoleculeBondFeatures','int[]','+mFragmentBondFeatures','+mFragmentGraphAtom','+mFragmentGraphParentAtom','+mFragmentGraphParentBond','mFragmentGraphIsRingClosure','boolean[]','+mIsExcludeAtom','+mIsBridgeBondAtom','mFragmentConnAtoms','int[]','+mMatchTable','+mExcludeGroupNo','+mExcludeGroupGraphIndex','+mFragmentAtomContextRank','mSortedMatchSet','java.util.TreeSet','mMatchList','java.util.ArrayList','+mBridgeBondList','+mBridgeBondAtomList']]]

Clazz.newMeth(C$, 'c$',  function () {
;C$.$init$.apply(this);
this.mDefaultMatchMode=8;
this.mSortedMatchSet=Clazz.new_([Clazz.new_($I$(2,1))],$I$(1,1).c$$java_util_Comparator);
}, 1);

Clazz.newMeth(C$, 'c$$I',  function (matchMode) {
;C$.$init$.apply(this);
this.mDefaultMatchMode=matchMode;
this.mSortedMatchSet=Clazz.new_([Clazz.new_($I$(2,1))],$I$(1,1).c$$java_util_Comparator);
}, 1);

Clazz.newMeth(C$, 'setMol$com_actelion_research_chem_StereoMolecule$com_actelion_research_chem_StereoMolecule',  function (fragment, molecule) {
this.setMolecule$com_actelion_research_chem_StereoMolecule(molecule);
this.setFragment$com_actelion_research_chem_StereoMolecule(fragment);
});

Clazz.newMeth(C$, 'setMolecule$com_actelion_research_chem_StereoMolecule',  function (molecule) {
if (molecule == null  || molecule.getAllAtoms$() == 0 ) {
this.mMolecule=null;
return;
}this.mMolecule=molecule;
this.mMoleculeFeaturesValid=false;
this.mMolecule.ensureHelperArrays$I(1);
});

Clazz.newMeth(C$, 'stop$',  function () {
this.mStop=true;
});

Clazz.newMeth(C$, 'setFragment$com_actelion_research_chem_StereoMolecule',  function (fragment) {
if (fragment == null  || fragment.getAllAtoms$() == 0  || !fragment.isFragment$() ) {
this.mFragment=null;
return;
}this.mFragment=fragment;
this.mFragmentFeaturesValid=false;
this.mFragment.ensureHelperArrays$I(1);
this.mRequiredHelperLevel=7;
for (var atom=0; atom < this.mFragment.getAtoms$(); atom++) if (Long.$ne((Long.$and(this.mFragment.getAtomQueryFeatures$I(atom),(52776558141440))),0 )) this.mRequiredHelperLevel=15;

for (var bond=0; bond < this.mFragment.getBonds$(); bond++) if ((this.mFragment.getBondQueryFeatures$I(bond) & 1048576) != 0) this.mRequiredHelperLevel=15;

if (this.mMoleculeFeaturesValid && this.mRequiredHelperLevel != 7 ) this.mMolecule.ensureHelperArrays$I(this.mRequiredHelperLevel);
this.mFragmentExcludeAtoms=0;
this.mFragmentExcludeBonds=0;
this.mIsExcludeAtom=Clazz.array(Boolean.TYPE, [this.mFragment.getAtoms$()]);
for (var atom=0; atom < this.mFragment.getAtoms$(); atom++) {
this.mIsExcludeAtom[atom]=(Long.$ne((Long.$and(this.mFragment.getAtomQueryFeatures$I(atom),536870912)),0 ));
if (this.mIsExcludeAtom[atom]) ++this.mFragmentExcludeAtoms;
}
this.mExcludeGroupCount=0;
this.mExcludeGroupNo=null;
this.mFragmentAtomContextRank=null;
if (this.mFragmentExcludeAtoms != 0) {
if (this.mFragmentExcludeAtoms != 0) for (var bond=0; bond < this.mFragment.getBonds$(); bond++) if (this.mIsExcludeAtom[this.mFragment.getBondAtom$I$I(0, bond)] || this.mIsExcludeAtom[this.mFragment.getBondAtom$I$I(1, bond)] ) ++this.mFragmentExcludeBonds;

for (var atom=0; atom < this.mFragment.getAllAtoms$(); atom++) this.mFragment.setAtomMarker$I$Z(atom, this.mIsExcludeAtom[atom]);

this.mExcludeGroupNo=Clazz.array(Integer.TYPE, [this.mFragment.getAllAtoms$()]);
this.mExcludeGroupCount=this.mFragment.getFragmentNumbers$IA$Z$Z(this.mExcludeGroupNo, true, false);
}});

Clazz.newMeth(C$, 'setFragmentSymmetryConstraints$IA',  function (fragmentContextRank) {
this.mFragmentAtomContextRank=fragmentContextRank;
});

Clazz.newMeth(C$, 'buildFragmentGraph',  function () {
this.mFragment.ensureHelperArrays$I(this.mRequiredHelperLevel);
var graphAllocation=Math.max(this.mFragment.getAtoms$(), this.mFragment.getBonds$()) + 16;
this.mFragmentGraphAtom=Clazz.array(Integer.TYPE, [graphAllocation]);
this.mFragmentGraphParentAtom=Clazz.array(Integer.TYPE, [graphAllocation]);
this.mFragmentGraphParentBond=Clazz.array(Integer.TYPE, [graphAllocation]);
this.mFragmentGraphIsRingClosure=Clazz.array(Boolean.TYPE, [graphAllocation + 1]);
var fragmentAtomUsed=Clazz.array(Boolean.TYPE, [this.mFragment.getAtoms$()]);
var fragmentBondUsed=Clazz.array(Boolean.TYPE, [this.mFragment.getBonds$()]);
var current=0;
for (var atom=0; atom < this.mFragment.getAtoms$(); atom++) {
if (!this.mIsExcludeAtom[atom] && !fragmentAtomUsed[atom] ) {
this.mFragmentGraphAtom[current]=atom;
this.mFragmentGraphParentBond[current]=-1;
this.mFragmentGraphParentAtom[current]=-1;
var highest=current;
while (current <= highest){
for (var i=0; i < this.mFragment.getAllConnAtomsPlusMetalBonds$I(this.mFragmentGraphAtom[current]); i++) highest=p$1.tryAddCandidate$I$I$I$ZA$ZA$I.apply(this, [current, highest, i, fragmentAtomUsed, fragmentBondUsed, -1]);

while (this.mFragmentGraphIsRingClosure[++current]);
}
}}
this.mFragmentGraphSize=current;
if (this.mFragmentExcludeAtoms != 0) {
var highest=this.mFragmentGraphSize - 1;
for (var excludeGroupNo=0; excludeGroupNo < this.mExcludeGroupCount; excludeGroupNo++) {
current=0;
while (current <= highest){
for (var i=0; i < this.mFragment.getAllConnAtomsPlusMetalBonds$I(this.mFragmentGraphAtom[current]); i++) highest=p$1.tryAddCandidate$I$I$I$ZA$ZA$I.apply(this, [current, highest, i, fragmentAtomUsed, fragmentBondUsed, excludeGroupNo]);

while (this.mFragmentGraphIsRingClosure[++current]);
}
}
for (var atom=0; atom < this.mFragment.getAtoms$(); atom++) {
if (this.mIsExcludeAtom[atom] && !fragmentAtomUsed[atom] ) {
this.mFragmentGraphAtom[current]=atom;
this.mFragmentGraphParentBond[current]=-1;
this.mFragmentGraphParentAtom[current]=-1;
highest=current;
while (current <= highest){
for (var i=0; i < this.mFragment.getAllConnAtomsPlusMetalBonds$I(this.mFragmentGraphAtom[current]); i++) if (this.mFragment.getConnAtom$I$I(this.mFragmentGraphAtom[current], i) < this.mFragment.getAtoms$()) highest=p$1.tryAddCandidate$I$I$I$ZA$ZA$I.apply(this, [current, highest, i, fragmentAtomUsed, fragmentBondUsed, this.mExcludeGroupNo[atom]]);

while (this.mFragmentGraphIsRingClosure[++current]);
}
}}
this.mExcludeGroupGraphIndex=Clazz.array(Integer.TYPE, [this.mExcludeGroupCount]);
for (var i=0; i < this.mExcludeGroupCount; i++) this.mExcludeGroupGraphIndex[i]=-1;

for (var i=this.mFragmentGraphSize; i < current; i++) {
var excludeGroupNo=this.mExcludeGroupNo[this.mFragmentGraphAtom[i]];
if (this.mExcludeGroupGraphIndex[excludeGroupNo] == -1) this.mExcludeGroupGraphIndex[excludeGroupNo]=i;
}
}this.mFragmentGraphSizeWithExcludeGroups=current;
}, p$1);

Clazz.newMeth(C$, 'tryAddCandidate$I$I$I$ZA$ZA$I',  function (current, highest, i, fragmentAtomUsed, fragmentBondUsed, excludeGroupNo) {
var candidate=this.mFragment.getConnAtom$I$I(this.mFragmentGraphAtom[current], i);
if ((!this.mIsExcludeAtom[candidate] || this.mExcludeGroupNo[candidate] == excludeGroupNo ) && candidate != this.mFragmentGraphParentAtom[current] ) {
var candidateBond=this.mFragment.getConnBond$I$I(this.mFragmentGraphAtom[current], i);
if (!fragmentBondUsed[candidateBond] && !this.mFragment.isBondBridge$I(candidateBond) ) {
this.mFragmentGraphAtom[++highest]=candidate;
this.mFragmentGraphParentAtom[highest]=this.mFragmentGraphAtom[current];
this.mFragmentGraphParentBond[highest]=candidateBond;
fragmentBondUsed[candidateBond]=true;
if (fragmentAtomUsed[candidate]) this.mFragmentGraphIsRingClosure[highest]=true;
 else fragmentAtomUsed[candidate]=true;
}}return highest;
}, p$1);

Clazz.newMeth(C$, 'getMatchList$',  function () {
return this.mMatchList;
});

Clazz.newMeth(C$, 'getMatchingBridgeBondAtoms$I',  function (matchNo) {
return this.mBridgeBondAtomList.size$() <= matchNo ? null : this.mBridgeBondAtomList.get$I(matchNo);
});

Clazz.newMeth(C$, 'isFragmentInMolecule$',  function () {
return (this.findFragmentInMolecule$I$I(1, this.mDefaultMatchMode) > 0);
});

Clazz.newMeth(C$, 'isFragmentInMolecule$I',  function (matchMode) {
return (this.findFragmentInMolecule$I$I(1, matchMode) > 0);
});

Clazz.newMeth(C$, 'findFragmentInMolecule$',  function () {
return this.findFragmentInMolecule$I$I(4, this.mDefaultMatchMode);
});

Clazz.newMeth(C$, 'findFragmentInMolecule$I$I',  function (countMode, matchMode) {
return this.findFragmentInMolecule$I$I$ZA(countMode, matchMode, null);
});

Clazz.newMeth(C$, 'findFragmentInMolecule$I$I$ZA',  function (countMode, matchMode, atomExcluded) {
this.mStop=false;
this.mMatchList=Clazz.new_($I$(3,1));
this.mBridgeBondAtomList=Clazz.new_($I$(3,1));
this.mSortedMatchSet.clear$();
if (this.mMolecule == null  || this.mFragment == null  ) return 0;
if (this.mFragment.getAtoms$() - this.mFragmentExcludeAtoms > this.mMolecule.getAtoms$() || this.mFragment.getBonds$() - this.mFragmentExcludeBonds > this.mMolecule.getBonds$() ) return 0;
if (this.mFragment.getAtoms$() == 0) return 0;
if (countMode == 6) this.mRequiredHelperLevel=63;
this.setupAtomAndBondFeatures$I(matchMode);
var atomUsed=Clazz.array(Boolean.TYPE, [this.mMolecule.getAtoms$()]);
if (atomExcluded != null ) for (var atom=0; atom < this.mMolecule.getAtoms$(); atom++) atomUsed[atom]=atomExcluded[atom];

this.mMatchTable=Clazz.array(Integer.TYPE, [this.mFragment.getAtoms$()]);
$I$(4).fill$IA$I(this.mMatchTable, -1);
var index=Clazz.array(Integer.TYPE, [this.mFragmentGraphSizeWithExcludeGroups]);
$I$(4).fill$IA$I(index, -1);
var current=0;
while (!this.mStop){
if (this.mFragmentGraphSize != 0) {
var maxIndex=(this.mFragmentGraphParentAtom[current] == -1) ? this.mMolecule.getAtoms$() : this.mMolecule.getAllConnAtomsPlusMetalBonds$I(this.mMatchTable[this.mFragmentGraphParentAtom[current]]);
++index[current];
if (index[current] == maxIndex) {
index[current]=-1;
if (current == 0) break;
--current;
if (!this.mFragmentGraphIsRingClosure[current]) atomUsed[this.mMatchTable[this.mFragmentGraphAtom[current]]]=false;
continue;
}if (this.mFragmentGraphParentAtom[current] == -1) {
if (!atomUsed[index[current]]) {
if (this.areAtomsSimilar$I$I(index[current], this.mFragmentGraphAtom[current])) {
this.mMatchTable[this.mFragmentGraphAtom[current]]=index[current];
atomUsed[index[current]]=true;
++current;
}}} else {
if (this.mMolecule.getConnAtom$I$I(this.mMatchTable[this.mFragmentGraphParentAtom[current]], index[current]) >= this.mMolecule.getAtoms$()) continue;
var candidate=this.mMolecule.getConnAtom$I$I(this.mMatchTable[this.mFragmentGraphParentAtom[current]], index[current]);
if (!this.mFragmentGraphIsRingClosure[current]) {
if (!atomUsed[candidate]) {
if (this.areAtomsSimilar$I$I(candidate, this.mFragmentGraphAtom[current]) && this.areBondsSimilar$I$I(this.mMolecule.getConnBond$I$I(this.mMatchTable[this.mFragmentGraphParentAtom[current]], index[current]), this.mFragmentGraphParentBond[current]) ) {
atomUsed[candidate]=true;
this.mMatchTable[this.mFragmentGraphAtom[current]]=candidate;
++current;
}}} else {
if (candidate == this.mMatchTable[this.mFragmentGraphAtom[current]] && this.areBondsSimilar$I$I(this.mMolecule.getConnBond$I$I(this.mMatchTable[this.mFragmentGraphParentAtom[current]], index[current]), this.mFragmentGraphParentBond[current]) ) {
++current;
}}}}if (current == this.mFragmentGraphSize) {
if (p$1.doTHParitiesMatch$I.apply(this, [-1]) && p$1.doEZParitiesMatch$I.apply(this, [-1]) && p$1.doBridgeBondsMatch$ZA$I.apply(this, [atomUsed, -1])  ) {
var isExcludedMatch=false;
for (var excludeGroup=0; excludeGroup < this.mExcludeGroupCount; excludeGroup++) {
if (p$1.isExcludeGroupMatch$ZA$IA$I.apply(this, [atomUsed, index, excludeGroup])) {
isExcludedMatch=true;
break;
}}
if (countMode == 1 && !isExcludedMatch ) return 1;
if (!isExcludedMatch) {
p$1.addMatchIfQualifies$I.apply(this, [countMode]);
if (countMode == 2) return 1;
}}if (current == 0) break;
--current;
if (!this.mFragmentGraphIsRingClosure[current]) atomUsed[this.mMatchTable[this.mFragmentGraphAtom[current]]]=false;
}}
return this.mMatchList.size$();
});

Clazz.newMeth(C$, 'addMatchIfQualifies$I',  function (countMode) {
if (countMode == 2 || countMode == 5 ) {
p$1.addMatchAtoms.apply(this, []);
} else if (countMode == 4) {
var sortedMatch=p$1.getSortedMatch$IA.apply(this, [C$.copyOf$IA$I(this.mMatchTable, this.mMatchTable.length)]);
if (!this.mSortedMatchSet.contains$O(sortedMatch)) {
this.mSortedMatchSet.add$O(sortedMatch);
p$1.addMatchAtoms.apply(this, []);
}} else if (countMode == 3) {
var sortedMatch=p$1.getSortedMatch$IA.apply(this, [C$.copyOf$IA$I(this.mMatchTable, this.mMatchTable.length)]);
if (!this.mSortedMatchSet.contains$O(sortedMatch)) {
var found=false;
for (var existing, $existing = this.mSortedMatchSet.iterator$(); $existing.hasNext$()&&((existing=($existing.next$())),1);) {
var existingIndex=0;
for (var atom, $atom = 0, $$atom = sortedMatch; $atom<$$atom.length&&((atom=($$atom[$atom])),1);$atom++) {
while (existingIndex < existing.length && existing[existingIndex] < atom )++existingIndex;

if (existingIndex < existing.length) {
if (atom == existing[existingIndex]) {
found=true;
break;
}}}
if (found) break;
}
if (!found) {
this.mSortedMatchSet.add$O(sortedMatch);
p$1.addMatchAtoms.apply(this, []);
}}} else if (countMode == 6) {
var sortedMatch=p$1.getSortedSymmetryMatch$IA.apply(this, [C$.copyOf$IA$I(this.mMatchTable, this.mMatchTable.length)]);
if (!this.mSortedMatchSet.contains$O(sortedMatch)) {
this.mSortedMatchSet.add$O(sortedMatch);
p$1.addMatchAtoms.apply(this, []);
}}}, p$1);

Clazz.newMeth(C$, 'addMatchAtoms',  function () {
this.mMatchList.add$O(C$.copyOf$IA$I(this.mMatchTable, this.mMatchTable.length));
if (this.mBridgeBondList != null ) this.mBridgeBondAtomList.add$O(C$.copyOf$ZA$I(this.mIsBridgeBondAtom, this.mIsBridgeBondAtom.length));
}, p$1);

Clazz.newMeth(C$, 'getSortedMatch$IA',  function (match) {
var count=0;
for (var atom, $atom = 0, $$atom = match; $atom<$$atom.length&&((atom=($$atom[$atom])),1);$atom++) if (atom == -1) ++count;

if (count != 0) {
var oldMatch=match;
match=Clazz.array(Integer.TYPE, [oldMatch.length - count]);
var index=0;
for (var atom, $atom = 0, $$atom = oldMatch; $atom<$$atom.length&&((atom=($$atom[$atom])),1);$atom++) if (atom != -1) match[index++]=atom;

}$I$(4).sort$IA(match);
return match;
}, p$1);

Clazz.newMeth(C$, 'getSortedSymmetryMatch$IA',  function (match) {
var count=0;
for (var atom, $atom = 0, $$atom = match; $atom<$$atom.length&&((atom=($$atom[$atom])),1);$atom++) if (atom == -1) ++count;

var symmetryMatch=Clazz.array(Integer.TYPE, [match.length - count]);
var index=0;
for (var i=0; i < match.length; i++) {
if (match[i] != -1) {
symmetryMatch[index]=(this.mFragment.getSymmetryRank$I(i) << 16) | this.mMolecule.getSymmetryRank$I(match[i]);
if (this.mFragmentAtomContextRank != null ) symmetryMatch[index]|=this.mFragmentAtomContextRank[i] << 24;
++index;
}}
$I$(4).sort$IA(symmetryMatch);
return symmetryMatch;
}, p$1);

Clazz.newMeth(C$, 'areAtomsSimilar$I$I',  function (moleculeAtom, fragmentAtom) {
var moleculeConnAtoms=this.mMolecule.getConnAtoms$I(moleculeAtom);
var fragmentConnAtoms=this.mFragmentConnAtoms[fragmentAtom];
if (fragmentConnAtoms > moleculeConnAtoms) return false;
var moleculeQF=this.mMolecule.getAtomQueryFeatures$I(moleculeAtom);
var fragmentQF=this.mFragment.getAtomQueryFeatures$I(fragmentAtom);
var fragmentList=this.mFragment.getAtomList$I(fragmentAtom);
var moleculeList=this.mMolecule.getAtomList$I(moleculeAtom);
if (Long.$ne((Long.$and(fragmentQF,1)),0 )) {
if (fragmentList != null ) {
if (Long.$ne((Long.$and(moleculeQF,1)),0 )) {
if (moleculeList == null ) return false;
if (!p$1.isSubListOf$IA$IA.apply(this, [fragmentList, moleculeList])) return false;
} else {
if (moleculeList != null ) {
if (p$1.listsOverlap$IA$IA.apply(this, [moleculeList, fragmentList])) return false;
} else {
if (p$1.isListMember$I$IA.apply(this, [this.mMolecule.getAtomicNo$I(moleculeAtom), fragmentList])) return false;
}}}} else {
if (Long.$ne((Long.$and(moleculeQF,1)),0 )) return false;
if (fragmentList != null ) {
if (moleculeList != null ) {
if (!p$1.isSubListOf$IA$IA.apply(this, [moleculeList, fragmentList])) return false;
} else {
if (!p$1.isListMember$I$IA.apply(this, [this.mMolecule.getAtomicNo$I(moleculeAtom), fragmentList])) return false;
}} else {
if (moleculeList != null ) return false;
if (this.mMoleculeAtomType[moleculeAtom] != this.mFragmentAtomType[fragmentAtom]) return false;
}}if (Long.$ne((Long.$or(moleculeQF,fragmentQF)),0 )) {
if (Long.$ne((Long.$and(fragmentQF,2048)),0 )) {
if (this.mMolecule.isFragment$() && Long.$eq((Long.$and(moleculeQF,2048)),0 ) ) return false;
 else if (fragmentConnAtoms != moleculeConnAtoms) return false;
}if (Long.$ne((Long.$and(fragmentQF,4096)),0 )) {
if ((fragmentConnAtoms >= moleculeConnAtoms) && Long.$eq((Long.$and(moleculeQF,4096)),0 ) ) return false;
}}if (Long.$ne((Long.$and(this.mMoleculeAtomFeatures[moleculeAtom],(Long.$not(this.mFragmentAtomFeatures[fragmentAtom])))),0 )) return false;
if (Long.$ne((Long.$and(this.mFragmentRingFeatures[fragmentAtom],(Long.$not(this.mMoleculeRingFeatures[moleculeAtom])))),0 )) return false;
var fragmentRingQF=Long.$and(fragmentQF,545460846592);
if (this.mMolecule.isFragment$()) {
var moleculeRingQF=Long.$and(fragmentQF,545460846592);
if (Long.$ne(moleculeRingQF,0 ) && (Long.$eq(fragmentRingQF,0 ) || Long.$ne((Long.$and(fragmentRingQF,(Long.$not(moleculeRingQF)))),0 ) ) ) return false;
} else {
if (Long.$ne(fragmentRingQF,0 ) && Long.$eq((Long.$and(fragmentRingQF,this.mMoleculeRingFeatures[moleculeAtom])),0 ) ) return false;
}if (this.mFragment.getAtomCharge$I(fragmentAtom) != 0 && this.mFragment.getAtomCharge$I(fragmentAtom) != this.mMolecule.getAtomCharge$I(moleculeAtom) ) return false;
if (this.mFragment.getAtomMass$I(fragmentAtom) != 0 && this.mFragment.getAtomMass$I(fragmentAtom) != this.mMolecule.getAtomMass$I(moleculeAtom) ) return false;
if (this.mFragment.getAtomRadical$I(fragmentAtom) != 0 && this.mFragment.getAtomRadical$I(fragmentAtom) != this.mMolecule.getAtomRadical$I(moleculeAtom) ) return false;
var smallestRingSize=Long.$ival((Long.$sr((Long.$and(this.mFragment.getAtomQueryFeatures$I(fragmentAtom),29360128)),22)));
if (smallestRingSize != 0) {
if (!this.mMolecule.isFragment$()) {
if (this.mMolecule.getAtomRingSize$I(moleculeAtom) != smallestRingSize) return false;
} else {
var targetRingSize=Long.$ival((Long.$sr((Long.$and(this.mMolecule.getAtomQueryFeatures$I(moleculeAtom),29360128)),22)));
if (smallestRingSize != targetRingSize) return false;
}}return true;
});

Clazz.newMeth(C$, 'doTHParitiesMatch$I',  function (excludeGroupNo) {
var esrGroupAtomCount=0;
for (var fragmentAtom=0; fragmentAtom < this.mFragment.getAtoms$(); fragmentAtom++) {
if ((this.mExcludeGroupNo == null  || this.mExcludeGroupNo[fragmentAtom] == excludeGroupNo ) && Long.$ne((Long.$and(this.mFragment.getAtomQueryFeatures$I(fragmentAtom),8192)),0 ) ) {
var moleculeAtom=this.mMatchTable[fragmentAtom];
var fragmentParity=this.mFragment.getAtomParity$I(fragmentAtom);
var moleculeParity=this.mMolecule.getAtomParity$I(moleculeAtom);
if (fragmentParity == 0) continue;
if (fragmentParity == 3) continue;
if (moleculeParity == 0 || moleculeParity == 3 ) return false;
if (this.mFragment.getAtomESRType$I(fragmentAtom) == 1) {
++esrGroupAtomCount;
continue;
}if (this.mMolecule.getAtomESRType$I(moleculeAtom) == 1) return false;
if (this.mFragment.getAtomESRType$I(fragmentAtom) == 2) {
++esrGroupAtomCount;
continue;
}if (this.mMolecule.getAtomESRType$I(moleculeAtom) == 2) return false;
if (p$1.isTHParityInversion$I.apply(this, [fragmentAtom]) == (fragmentParity == moleculeParity) ) return false;
}}
if (esrGroupAtomCount != 0) {
var esrAtom=Clazz.array(Integer.TYPE, [esrGroupAtomCount]);
var esrAtomIndex=0;
for (var fragmentAtom=0; fragmentAtom < this.mFragment.getAtoms$(); fragmentAtom++) {
if ((this.mExcludeGroupNo == null  || this.mExcludeGroupNo[fragmentAtom] == excludeGroupNo ) && Long.$ne((Long.$and(this.mFragment.getAtomQueryFeatures$I(fragmentAtom),8192)),0 ) ) {
var fragmentParity=this.mFragment.getAtomParity$I(fragmentAtom);
if (fragmentParity != 0 && fragmentParity != 3 ) {
esrAtom[esrAtomIndex++]=(this.mFragment.getAtomESRGroup$I(fragmentAtom) << 24) | (this.mFragment.getAtomESRType$I(fragmentAtom) << 22) | fragmentAtom ;
}}}
$I$(4).sort$IA(esrAtom);
esrAtomIndex=0;
while (esrAtomIndex < esrAtom.length){
var fragmentBaseAtom=esrAtom[esrAtomIndex] & 4194303;
var moleculeBaseAtom=this.mMatchTable[fragmentBaseAtom];
var baseGroupAndType=esrAtom[esrAtomIndex] & -4194304;
var baseParityComparison=!!(p$1.isTHParityInversion$I.apply(this, [fragmentBaseAtom]) ^ (this.mFragment.getAtomParity$I(fragmentBaseAtom) == this.mMolecule.getAtomParity$I(moleculeBaseAtom)));
for (esrAtomIndex++; esrAtomIndex < esrAtom.length && (esrAtom[esrAtomIndex] & -4194304) == baseGroupAndType ; esrAtomIndex++) {
var fragmentAtom=esrAtom[esrAtomIndex] & 4194303;
var moleculeAtom=this.mMatchTable[fragmentAtom];
if (this.mMolecule.getAtomESRType$I(moleculeAtom) != this.mMolecule.getAtomESRType$I(moleculeBaseAtom) || this.mMolecule.getAtomESRGroup$I(moleculeAtom) != this.mMolecule.getAtomESRGroup$I(moleculeBaseAtom) ) return false;
var parityComparison=!!(p$1.isTHParityInversion$I.apply(this, [fragmentAtom]) ^ (this.mFragment.getAtomParity$I(fragmentAtom) == this.mMolecule.getAtomParity$I(moleculeAtom)));
if (parityComparison != baseParityComparison ) return false;
}
}
}return true;
}, p$1);

Clazz.newMeth(C$, 'isTHParityInversion$I',  function (fragmentAtom) {
var inversion=false;
if (this.mFragment.getAtomPi$I(fragmentAtom) == 0) {
for (var i=1; i < this.mFragment.getConnAtoms$I(fragmentAtom); i++) {
for (var j=0; j < i; j++) {
var connAtom1=this.mFragment.getConnAtom$I$I(fragmentAtom, i);
var connAtom2=this.mFragment.getConnAtom$I$I(fragmentAtom, j);
if (!!((this.mMatchTable[connAtom1] > this.mMatchTable[connAtom2]) ^ (connAtom1 > connAtom2))) inversion=!inversion;
}
}
} else {
for (var i=0; i < this.mFragment.getConnAtoms$I(fragmentAtom); i++) {
var connAtom=this.mFragment.getConnAtom$I$I(fragmentAtom, i);
var neighbours=0;
var neighbour=Clazz.array(Integer.TYPE, [3]);
for (var j=0; j < this.mFragment.getConnAtoms$I(connAtom); j++) {
neighbour[neighbours]=this.mFragment.getConnAtom$I$I(connAtom, j);
if (neighbour[neighbours] != fragmentAtom) ++neighbours;
}
if (neighbours == 2 && (!!((this.mMatchTable[neighbour[0]] > this.mMatchTable[neighbour[1]]) ^ (neighbour[0] > neighbour[1]))) ) inversion=!inversion;
}
}return inversion;
}, p$1);

Clazz.newMeth(C$, 'doEZParitiesMatch$I',  function (excludeGroupNo) {
for (var fragmentBond=0; fragmentBond < this.mFragment.getBonds$(); fragmentBond++) {
if ((this.mFragment.getBondQueryFeatures$I(fragmentBond) & 1048576) != 0) {
var fragmentParity=this.mFragment.getBondParity$I(fragmentBond);
if (fragmentParity == 0) continue;
var fragmentAtom1=this.mFragment.getBondAtom$I$I(0, fragmentBond);
var fragmentAtom2=this.mFragment.getBondAtom$I$I(1, fragmentBond);
if (this.mExcludeGroupNo == null  || (excludeGroupNo == -1 && this.mExcludeGroupNo[fragmentAtom1] == -1  && this.mExcludeGroupNo[fragmentAtom2] == -1 )  || (excludeGroupNo != -1 && (this.mExcludeGroupNo[fragmentAtom1] == excludeGroupNo || this.mExcludeGroupNo[fragmentAtom2] == excludeGroupNo ) ) ) {
var moleculeAtom1=this.mMatchTable[fragmentAtom1];
var moleculeAtom2=this.mMatchTable[fragmentAtom2];
var moleculeBond=this.mMolecule.getBond$I$I(moleculeAtom1, moleculeAtom2);
var moleculeParity=this.mMolecule.getBondParity$I(moleculeBond);
if (moleculeParity == 0) {
if (this.mMolecule.isSmallRingBond$I(moleculeBond)) moleculeParity=p$1.calculateImplicitSmallRingBondParity$I.apply(this, [moleculeBond]);
if (moleculeParity == 0) continue;
}if (fragmentParity == 3) continue;
if (moleculeParity == 3) continue;
if (p$1.isEZParityInversion$I$I.apply(this, [fragmentBond, moleculeBond]) == (fragmentParity == moleculeParity) ) return false;
}}}
return true;
}, p$1);

Clazz.newMeth(C$, 'calculateImplicitSmallRingBondParity$I',  function (moleculeBond) {
var ringSet=this.mMolecule.getRingSet$();
for (var r=0; r < ringSet.getSize$(); r++) {
if (ringSet.isBondMember$I$I(r, moleculeBond)) {
var relevantAtom=Clazz.array(Integer.TYPE, [2]);
for (var i=0; i < 2; i++) {
relevantAtom[i]=2147483647;
var bondAtom=this.mMolecule.getBondAtom$I$I(i, moleculeBond);
for (var j=0; j < this.mMolecule.getConnAtoms$I(bondAtom); j++) {
var atom=this.mMolecule.getConnAtom$I$I(bondAtom, j);
if (atom != this.mMolecule.getBondAtom$I$I(1 - i, moleculeBond) && relevantAtom[i] > atom ) relevantAtom[i]=atom;
}
}
var memberCount=0;
if (ringSet.isAtomMember$I$I(r, relevantAtom[0])) ++memberCount;
if (ringSet.isAtomMember$I$I(r, relevantAtom[1])) ++memberCount;
if (memberCount == 2) return 2;
if (memberCount == 1) return 1;
return 2;
}}
return 0;
}, p$1);

Clazz.newMeth(C$, 'isEZParityInversion$I$I',  function (fragmentBond, moleculeBond) {
var inversion=false;
for (var i=0; i < 2; i++) {
var fragmentAtom=this.mFragment.getBondAtom$I$I(i, fragmentBond);
var moleculeAtom=this.mMatchTable[fragmentAtom];
if (this.mMolecule.getConnAtoms$I(moleculeAtom) > 2) {
var otherFragmentAtom=this.mFragment.getBondAtom$I$I(1 - i, fragmentBond);
var lowFragmentNeighbour=2147483647;
for (var j=0; j < this.mFragment.getConnAtoms$I(fragmentAtom); j++) {
var fragmentNeighbour=this.mFragment.getConnAtom$I$I(fragmentAtom, j);
if (fragmentNeighbour != otherFragmentAtom && lowFragmentNeighbour > fragmentNeighbour ) lowFragmentNeighbour=fragmentNeighbour;
}
var otherMoleculeAtom=this.mMatchTable[otherFragmentAtom];
var lowMoleculeNeighbour=2147483647;
for (var j=0; j < this.mMolecule.getConnAtoms$I(moleculeAtom); j++) {
var moleculeNeighbour=this.mMolecule.getConnAtom$I$I(moleculeAtom, j);
if (moleculeNeighbour != otherMoleculeAtom && lowMoleculeNeighbour > moleculeNeighbour ) lowMoleculeNeighbour=moleculeNeighbour;
}
if (this.mMatchTable[lowFragmentNeighbour] != lowMoleculeNeighbour) inversion=!inversion;
}}
return inversion;
}, p$1);

Clazz.newMeth(C$, 'isExcludeGroupMatch$ZA$IA$I',  function (atomUsed, index, excludeGroupNo) {
var excludeGroupGraphBase=this.mExcludeGroupGraphIndex[excludeGroupNo];
var excludeGroupGraphMax=excludeGroupGraphBase + 1;
while (excludeGroupGraphMax < this.mFragmentGraphSizeWithExcludeGroups && this.mExcludeGroupNo[this.mFragmentGraphAtom[excludeGroupGraphMax]] == excludeGroupNo )++excludeGroupGraphMax;

for (var i=excludeGroupGraphBase; i < excludeGroupGraphMax; i++) index[i]=-1;

var current=excludeGroupGraphBase;
while (true){
var maxIndex=(this.mFragmentGraphParentAtom[current] == -1) ? this.mMolecule.getAtoms$() : this.mMolecule.getAllConnAtomsPlusMetalBonds$I(this.mMatchTable[this.mFragmentGraphParentAtom[current]]);
++index[current];
if (index[current] == maxIndex) {
index[current]=-1;
if (current == excludeGroupGraphBase) break;
--current;
if (!this.mFragmentGraphIsRingClosure[current]) {
atomUsed[this.mMatchTable[this.mFragmentGraphAtom[current]]]=false;
this.mMatchTable[this.mFragmentGraphAtom[current]]=-1;
}continue;
}if (this.mFragmentGraphParentAtom[current] == -1) {
if (!atomUsed[index[current]]) {
if (this.areAtomsSimilar$I$I(index[current], this.mFragmentGraphAtom[current])) {
this.mMatchTable[this.mFragmentGraphAtom[current]]=index[current];
atomUsed[index[current]]=true;
++current;
}}} else {
if (this.mMolecule.getConnAtom$I$I(this.mMatchTable[this.mFragmentGraphParentAtom[current]], index[current]) >= this.mMolecule.getAtoms$()) {
++index[current];
continue;
}var candidate=this.mMolecule.getConnAtom$I$I(this.mMatchTable[this.mFragmentGraphParentAtom[current]], index[current]);
if (!this.mFragmentGraphIsRingClosure[current]) {
if (!atomUsed[candidate]) {
if (this.areAtomsSimilar$I$I(candidate, this.mFragmentGraphAtom[current]) && this.areBondsSimilar$I$I(this.mMolecule.getConnBond$I$I(this.mMatchTable[this.mFragmentGraphParentAtom[current]], index[current]), this.mFragmentGraphParentBond[current]) ) {
atomUsed[candidate]=true;
this.mMatchTable[this.mFragmentGraphAtom[current]]=candidate;
++current;
}}} else {
if (candidate == this.mMatchTable[this.mFragmentGraphAtom[current]] && this.areBondsSimilar$I$I(this.mMolecule.getConnBond$I$I(this.mMatchTable[this.mFragmentGraphParentAtom[current]], index[current]), this.mFragmentGraphParentBond[current]) ) {
++current;
}}}if (current == excludeGroupGraphMax) {
if (p$1.doTHParitiesMatch$I.apply(this, [excludeGroupNo]) && p$1.doEZParitiesMatch$I.apply(this, [excludeGroupNo]) && p$1.doBridgeBondsMatch$ZA$I.apply(this, [atomUsed, excludeGroupNo])  ) {
for (var i=excludeGroupGraphBase; i < excludeGroupGraphMax; i++) {
if (!this.mFragmentGraphIsRingClosure[i]) {
var atom=this.mFragmentGraphAtom[i];
atomUsed[this.mMatchTable[atom]]=false;
this.mMatchTable[atom]=-1;
}}
return true;
}--current;
if (!this.mFragmentGraphIsRingClosure[current]) {
atomUsed[this.mMatchTable[this.mFragmentGraphAtom[current]]]=false;
this.mMatchTable[this.mFragmentGraphAtom[current]]=-1;
}}}
return false;
}, p$1);

Clazz.newMeth(C$, 'doBridgeBondsMatch$ZA$I',  function (moleculeAtomUsed, excludeGroupNo) {
if (this.mBridgeBondList != null ) {
this.mIsBridgeBondAtom=Clazz.array(Boolean.TYPE, [moleculeAtomUsed.length]);
for (var bb, $bb = this.mBridgeBondList.iterator$(); $bb.hasNext$()&&((bb=($bb.next$())),1);) {
if (this.mExcludeGroupNo == null  || (excludeGroupNo == -1 && this.mExcludeGroupNo[bb.atom1] == -1  && this.mExcludeGroupNo[bb.atom2] == -1 )  || (excludeGroupNo != -1 && (this.mExcludeGroupNo[bb.atom1] == excludeGroupNo || this.mExcludeGroupNo[bb.atom2] == excludeGroupNo ) ) ) {
var pathAtom=Clazz.array(Integer.TYPE, [bb.maxBridgeSize + 2]);
var bridgeSize=this.mMolecule.getPath$IA$I$I$I$ZA$ZA(pathAtom, this.mMatchTable[bb.atom1], this.mMatchTable[bb.atom2], bb.maxBridgeSize + 1, moleculeAtomUsed, null) - 1;
if (bridgeSize < bb.minBridgeSize || bridgeSize > bb.maxBridgeSize ) return false;
for (var i=1; i <= bridgeSize; i++) this.mIsBridgeBondAtom[pathAtom[i]]=true;

}}
}return true;
}, p$1);

Clazz.newMeth(C$, 'areBondsSimilar$I$I',  function (moleculeBond, fragmentBond) {
var molDefaults=this.mMoleculeBondFeatures[moleculeBond];
var frgDefaults=this.mFragmentBondFeatures[fragmentBond];
if ((this.mFragment.getBondQueryFeatures$I(fragmentBond) & 8388608) != 0) {
var molBondType=this.mMolecule.getBondTypeSimple$I(moleculeBond);
var frgBondType=this.mFragment.getBondTypeSimple$I(fragmentBond);
var frgBondTypes=this.mFragment.getBondQueryFeatures$I(fragmentBond) & 31;
if (molBondType != frgBondType && !(molBondType == 1 && (frgBondTypes & 1) != 0 )  && !(molBondType == 2 && (frgBondTypes & 2) != 0 )  && !(molBondType == 4 && (frgBondTypes & 4) != 0 )  && !(molBondType == 8 && (frgBondTypes & 32) != 0 )  && !(molBondType == 16 && (frgBondTypes & 64) != 0 )  && !(molBondType == 32 && (frgBondTypes & 16) != 0 )  && !(molBondType == 64 && (frgBondTypes & 8) != 0 ) ) return false;
molDefaults&=~31;
frgDefaults&=~31;
}if ((molDefaults & ~frgDefaults) != 0) return false;
var ringSize=(this.mFragment.getBondQueryFeatures$I(fragmentBond) & 917504) >> 17;
if (ringSize != 0) {
if (this.mMolecule.isFragment$() && ringSize == (this.mMolecule.getBondQueryFeatures$I(fragmentBond) & 917504) >> 17 ) return true;
var found=false;
var ringSet=this.mMolecule.getRingSet$();
for (var i=0; i < ringSet.getSize$(); i++) {
if (ringSet.getRingSize$I(i) == ringSize) {
if (ringSet.isBondMember$I$I(i, moleculeBond)) {
found=true;
break;
}}}
if (!found) return false;
}return true;
});

Clazz.newMeth(C$, 'isSubListOf$IA$IA',  function (list1, list2) {
var i2=0;
for (var i1=0; i1 < list1.length; i1++) {
var atomicNo1=list1[i1];
while (list2[i2] < atomicNo1){
++i2;
if (i2 == list2.length) return false;
}
if (list2[i2] > atomicNo1) return false;
}
return true;
}, p$1);

Clazz.newMeth(C$, 'listsOverlap$IA$IA',  function (list1, list2) {
var i1=0;
var i2=0;
while (i1 < list1.length && i2 < list2.length ){
var atomicNo1=list1[i1];
var atomicNo2=list2[i2];
if (atomicNo1 == atomicNo2) return true;
if (atomicNo1 < atomicNo2) ++i1;
 else ++i2;
}
return false;
}, p$1);

Clazz.newMeth(C$, 'isListMember$I$IA',  function (atomicNo, list) {
for (var i=0; i < list.length; i++) if (list[i] == atomicNo) return true;

return false;
}, p$1);

Clazz.newMeth(C$, 'setupAtomAndBondFeatures$I',  function (matchMode) {
if (!this.mMoleculeFeaturesValid) {
p$1.setupMoleculeFeatures$I.apply(this, [matchMode]);
this.mMoleculeFeaturesValid=true;
}if (!this.mFragmentFeaturesValid) {
p$1.setupFragmentFeatures$I.apply(this, [matchMode]);
p$1.buildFragmentGraph.apply(this, []);
p$1.buildBridgeBondList.apply(this, []);
this.mFragmentFeaturesValid=true;
}});

Clazz.newMeth(C$, 'setupMoleculeFeatures$I',  function (matchMode) {
this.mMolecule.ensureHelperArrays$I(this.mRequiredHelperLevel);
var nTotalMoleculeAtoms=this.mMolecule.getAtoms$();
this.mMoleculeAtomType=Clazz.array(Integer.TYPE, [nTotalMoleculeAtoms]);
this.mMoleculeAtomFeatures=Clazz.array(Long.TYPE, [nTotalMoleculeAtoms]);
for (var atom=0; atom < nTotalMoleculeAtoms; atom++) {
this.mMoleculeAtomFeatures[atom]=Long.$xor((Long.$and((Long.$or(p$1.getAtomQueryDefaults$com_actelion_research_chem_StereoMolecule$I.apply(this, [this.mMolecule, atom]),this.mMolecule.getAtomQueryFeatures$I(atom))),140187971602430)),140733461823486);
this.mMoleculeAtomType[atom]=this.mMolecule.getAtomicNo$I(atom);
if ((matchMode & 1) != 0) this.mMoleculeAtomType[atom]+=(this.mMolecule.getAtomCharge$I(atom) + 16) << 8;
if ((matchMode & 2) != 0) this.mMoleculeAtomType[atom]+=this.mMolecule.getAtomMass$I(atom) << 16;
}
this.mMoleculeRingFeatures=Clazz.array(Long.TYPE, [nTotalMoleculeAtoms]);
var ringSet=this.mMolecule.getRingSet$();
for (var i=0; i < ringSet.getSize$(); i++) {
var ringSize=ringSet.getRingSize$I(i);
for (var atom, $atom = 0, $$atom = ringSet.getRingAtoms$I(i); $atom<$$atom.length&&((atom=($$atom[$atom])),1);$atom++) {
if (ringSize == 3) (this.mMoleculeRingFeatures[$k$=atom]=Long.$or(this.mMoleculeRingFeatures[$k$],(8589934592)));
 else if (ringSize == 4) (this.mMoleculeRingFeatures[$k$=atom]=Long.$or(this.mMoleculeRingFeatures[$k$],(17179869184)));
 else if (ringSize == 5) (this.mMoleculeRingFeatures[$k$=atom]=Long.$or(this.mMoleculeRingFeatures[$k$],(34359738368)));
 else if (ringSize == 6) (this.mMoleculeRingFeatures[$k$=atom]=Long.$or(this.mMoleculeRingFeatures[$k$],(68719476736)));
 else if (ringSize == 7) (this.mMoleculeRingFeatures[$k$=atom]=Long.$or(this.mMoleculeRingFeatures[$k$],(137438953472)));
}
}
for (var atom=0; atom < nTotalMoleculeAtoms; atom++) {
var ringSize=this.mMolecule.getAtomRingSize$I(atom);
if (ringSize == 0) (this.mMoleculeRingFeatures[$k$=atom]=Long.$or(this.mMoleculeRingFeatures[$k$],(4294967296)));
 else if (ringSize > 7) (this.mMoleculeRingFeatures[$k$=atom]=Long.$or(this.mMoleculeRingFeatures[$k$],(274877906944)));
}
var nTotalMoleculeBonds=this.mMolecule.getBonds$();
this.mMoleculeBondFeatures=Clazz.array(Integer.TYPE, [nTotalMoleculeBonds]);
for (var bond=0; bond < nTotalMoleculeBonds; bond++) this.mMoleculeBondFeatures[bond]=(p$1.getBondQueryDefaults$com_actelion_research_chem_StereoMolecule$I.apply(this, [this.mMolecule, bond]) | this.mMolecule.getBondQueryFeatures$I(bond)) & (6422527) ^ 6291840;

}, p$1);

Clazz.newMeth(C$, 'setupFragmentFeatures$I',  function (matchMode) {
var atomFeaturesWithoutExcludeAtoms=null;
var bondFeaturesWithoutExcludeAtoms=null;
var atomTypeWithoutExcludeAtoms=null;
this.mFragment.ensureHelperArrays$I(this.mRequiredHelperLevel);
this.mFragmentConnAtoms=Clazz.array(Integer.TYPE, [this.mFragment.getAtoms$()]);
for (var atom=0; atom < this.mFragment.getAtoms$(); atom++) this.mFragmentConnAtoms[atom]=this.mFragment.getConnAtoms$I(atom);

if (this.mFragmentExcludeAtoms != 0) {
var fragmentWithoutExcludeGroups=Clazz.new_([this.mFragment.getAllAtoms$(), this.mFragment.getAllBonds$()],$I$(5,1).c$$I$I);
var isNonExcludeAtom=Clazz.array(Boolean.TYPE, [this.mFragment.getAllAtoms$()]);
for (var atom=0; atom < this.mFragment.getAllAtoms$(); atom++) isNonExcludeAtom[atom]=!this.mIsExcludeAtom[atom];

this.mFragment.copyMoleculeByAtoms$com_actelion_research_chem_ExtendedMolecule$ZA$Z$IA(fragmentWithoutExcludeGroups, isNonExcludeAtom, true, null);
fragmentWithoutExcludeGroups.ensureHelperArrays$I(this.mRequiredHelperLevel);
p$1.setupFragmentFeatures$com_actelion_research_chem_StereoMolecule$I.apply(this, [fragmentWithoutExcludeGroups, matchMode]);
atomFeaturesWithoutExcludeAtoms=this.mFragmentAtomFeatures;
bondFeaturesWithoutExcludeAtoms=this.mFragmentBondFeatures;
atomTypeWithoutExcludeAtoms=this.mFragmentAtomType;
var index=0;
for (var atom=0; atom < this.mFragment.getAtoms$(); atom++) if (!this.mIsExcludeAtom[atom]) this.mFragmentConnAtoms[atom]=fragmentWithoutExcludeGroups.getConnAtoms$I(index++);

}p$1.setupFragmentFeatures$com_actelion_research_chem_StereoMolecule$I.apply(this, [this.mFragment, matchMode]);
if (this.mFragmentExcludeAtoms != 0) {
var index=0;
for (var atom=0; atom < this.mFragment.getAllAtoms$(); atom++) {
if (!this.mIsExcludeAtom[atom]) {
this.mFragmentAtomFeatures[atom]=atomFeaturesWithoutExcludeAtoms[index];
this.mFragmentAtomType[atom]=atomTypeWithoutExcludeAtoms[index++];
}}
index=0;
for (var bond=0; bond < this.mFragment.getAllBonds$(); bond++) {
if (!this.mIsExcludeAtom[this.mFragment.getBondAtom$I$I(0, bond)] && !this.mIsExcludeAtom[this.mFragment.getBondAtom$I$I(1, bond)] ) {
this.mFragmentBondFeatures[bond]=bondFeaturesWithoutExcludeAtoms[index++];
}}
}}, p$1);

Clazz.newMeth(C$, 'setupFragmentFeatures$com_actelion_research_chem_StereoMolecule$I',  function (fragment, matchMode) {
var nTotalFragmentAtoms=fragment.getAtoms$();
this.mFragmentAtomFeatures=Clazz.array(Long.TYPE, [fragment.getAtoms$()]);
this.mFragmentAtomType=Clazz.array(Integer.TYPE, [fragment.getAtoms$()]);
for (var atom=0; atom < nTotalFragmentAtoms; atom++) {
this.mFragmentAtomFeatures[atom]=Long.$xor((Long.$and((Long.$or(p$1.getAtomQueryDefaults$com_actelion_research_chem_StereoMolecule$I.apply(this, [fragment, atom]),fragment.getAtomQueryFeatures$I(atom))),140187971602430)),140733461823486);
this.mFragmentAtomType[atom]=fragment.getAtomicNo$I(atom);
if ((matchMode & 1) != 0) this.mFragmentAtomType[atom]+=(fragment.getAtomCharge$I(atom) + 16) << 8;
if ((matchMode & 2) != 0) this.mFragmentAtomType[atom]+=fragment.getAtomMass$I(atom) << 16;
}
this.mFragmentRingFeatures=Clazz.array(Long.TYPE, [fragment.getAtoms$()]);
var ringSet=fragment.getRingSet$();
for (var i=0; i < ringSet.getSize$(); i++) {
var containsBridgeBond=false;
for (var bond, $bond = 0, $$bond = ringSet.getRingBonds$I(i); $bond<$$bond.length&&((bond=($$bond[$bond])),1);$bond++) {
if (fragment.isBondBridge$I(bond)) {
containsBridgeBond=true;
break;
}}
if (!containsBridgeBond) {
var ringSize=ringSet.getRingSize$I(i);
for (var atom, $atom = 0, $$atom = ringSet.getRingAtoms$I(i); $atom<$$atom.length&&((atom=($$atom[$atom])),1);$atom++) {
if (ringSize == 3) (this.mFragmentRingFeatures[$k$=atom]=Long.$or(this.mFragmentRingFeatures[$k$],(8589934592)));
 else if (ringSize == 4) (this.mFragmentRingFeatures[$k$=atom]=Long.$or(this.mFragmentRingFeatures[$k$],(17179869184)));
 else if (ringSize == 5) (this.mFragmentRingFeatures[$k$=atom]=Long.$or(this.mFragmentRingFeatures[$k$],(34359738368)));
 else if (ringSize == 6) (this.mFragmentRingFeatures[$k$=atom]=Long.$or(this.mFragmentRingFeatures[$k$],(68719476736)));
 else if (ringSize == 7) (this.mFragmentRingFeatures[$k$=atom]=Long.$or(this.mFragmentRingFeatures[$k$],(137438953472)));
}
}}
var nTotalFragmentBonds=fragment.getBonds$();
this.mFragmentBondFeatures=Clazz.array(Integer.TYPE, [fragment.getBonds$()]);
for (var bond=0; bond < nTotalFragmentBonds; bond++) {
this.mFragmentBondFeatures[bond]=(p$1.getBondQueryDefaults$com_actelion_research_chem_StereoMolecule$I.apply(this, [fragment, bond]) | fragment.getBondQueryFeatures$I(bond)) & 6291967 ^ 6291840;
if ((matchMode & 4) != 0) {
if ((this.mFragmentBondFeatures[bond] & 2) != 0) this.mFragmentBondFeatures[bond]|=8;
} else if ((matchMode & 8) != 0) {
if ((this.mFragmentBondFeatures[bond] & 2) != 0 && fragment.isAromaticBond$I(bond) ) this.mFragmentBondFeatures[bond]|=8;
}}
}, p$1);

Clazz.newMeth(C$, 'getAtomQueryDefaults$com_actelion_research_chem_StereoMolecule$I',  function (mol, atom) {
var queryDefaults=0;
if (!mol.isFragment$()) {
if (mol.isHeteroAromaticAtom$I(atom)) (queryDefaults=Long.$or(queryDefaults,((70368744177666))));
 else if (mol.isAromaticAtom$I(atom)) (queryDefaults=Long.$or(queryDefaults,(2)));
 else (queryDefaults=Long.$or(queryDefaults,(4)));
if (mol.isAtomStereoCenter$I(atom)) (queryDefaults=Long.$or(queryDefaults,(17592186044416)));
 else (queryDefaults=Long.$or(queryDefaults,(35184372088832)));
var ringBonds=mol.getAtomRingBondCount$I(atom);
if (ringBonds == 0) (queryDefaults=Long.$or(queryDefaults,((112))));
 else if (ringBonds == 2) (queryDefaults=Long.$or(queryDefaults,((104))));
 else if (ringBonds == 3) (queryDefaults=Long.$or(queryDefaults,((88))));
 else (queryDefaults=Long.$or(queryDefaults,((56))));
var charge=mol.getAtomCharge$I(atom);
if (charge == 0) (queryDefaults=Long.$or(queryDefaults,((167772160))));
 else if (charge < 0) (queryDefaults=Long.$or(queryDefaults,((201326592))));
 else if (charge > 0) (queryDefaults=Long.$or(queryDefaults,((100663296))));
var hydrogens=mol.getAllHydrogens$I(atom);
switch (hydrogens) {
case 0:
(queryDefaults=Long.$or(queryDefaults,((1792))));
break;
case 1:
(queryDefaults=Long.$or(queryDefaults,((1664))));
break;
case 2:
(queryDefaults=Long.$or(queryDefaults,((1408))));
break;
default:
(queryDefaults=Long.$or(queryDefaults,((896))));
break;
}
var neighbours=mol.getConnAtoms$I(atom);
switch (neighbours) {
case 0:
(queryDefaults=Long.$or(queryDefaults,((3932160))));
break;
case 1:
(queryDefaults=Long.$or(queryDefaults,((3801088))));
break;
case 2:
(queryDefaults=Long.$or(queryDefaults,((3538944))));
break;
case 3:
(queryDefaults=Long.$or(queryDefaults,((3014656))));
break;
default:
(queryDefaults=Long.$or(queryDefaults,((1966080))));
break;
}
var eValue=mol.getAtomElectronegativeNeighbours$I(atom);
switch (eValue) {
case 0:
(queryDefaults=Long.$or(queryDefaults,((16492674416640))));
break;
case 1:
(queryDefaults=Long.$or(queryDefaults,((15942918602752))));
break;
case 2:
(queryDefaults=Long.$or(queryDefaults,((14843406974976))));
break;
case 3:
(queryDefaults=Long.$or(queryDefaults,((12644383719424))));
break;
default:
(queryDefaults=Long.$or(queryDefaults,((8246337208320))));
break;
}
var piElectrons=mol.getAtomPi$I(atom);
switch (piElectrons) {
case 0:
(queryDefaults=Long.$or(queryDefaults,((98304))));
break;
case 1:
(queryDefaults=Long.$or(queryDefaults,((81920))));
break;
default:
(queryDefaults=Long.$or(queryDefaults,((49152))));
break;
}
} else {
if (mol.isHeteroAromaticAtom$I(atom)) (queryDefaults=Long.$or(queryDefaults,((70368744177666))));
 else if (mol.isAromaticAtom$I(atom)) (queryDefaults=Long.$or(queryDefaults,(2)));
var ringBonds=mol.getAtomRingBondCount$I(atom);
if (ringBonds != 0) {
(queryDefaults=Long.$or(queryDefaults,(8)));
if (ringBonds > 2) (queryDefaults=Long.$or(queryDefaults,(16)));
if (ringBonds > 3) (queryDefaults=Long.$or(queryDefaults,(32)));
}var charge=mol.getAtomCharge$I(atom);
if (charge < 0) (queryDefaults=Long.$or(queryDefaults,((201326592))));
 else if (charge > 0) (queryDefaults=Long.$or(queryDefaults,((100663296))));
var neighbours=mol.getConnAtoms$I(atom);
switch (neighbours) {
case 0:
break;
case 1:
(queryDefaults=Long.$or(queryDefaults,((131072))));
break;
case 2:
(queryDefaults=Long.$or(queryDefaults,((393216))));
break;
case 3:
(queryDefaults=Long.$or(queryDefaults,((917504))));
break;
default:
(queryDefaults=Long.$or(queryDefaults,((1966080))));
break;
}
var eValue=mol.getAtomElectronegativeNeighbours$I(atom);
switch (eValue) {
case 0:
break;
case 1:
(queryDefaults=Long.$or(queryDefaults,((549755813888))));
break;
case 2:
(queryDefaults=Long.$or(queryDefaults,((1649267441664))));
break;
case 3:
(queryDefaults=Long.$or(queryDefaults,((3848290697216))));
break;
default:
(queryDefaults=Long.$or(queryDefaults,((8246337208320))));
break;
}
var piElectrons=mol.getAtomPi$I(atom);
if (piElectrons > 0) (queryDefaults=Long.$or(queryDefaults,(16384)));
if (piElectrons > 1) (queryDefaults=Long.$or(queryDefaults,(32768)));
}return queryDefaults;
}, p$1);

Clazz.newMeth(C$, 'getBondQueryDefaults$com_actelion_research_chem_StereoMolecule$I',  function (mol, bond) {
var queryDefaults=0;
if (mol.isDelocalizedBond$I(bond) || mol.getBondType$I(bond) == 64 ) queryDefaults|=8;
 else switch (mol.getBondOrder$I(bond)) {
case 0:
queryDefaults|=32;
break;
case 1:
queryDefaults|=1;
break;
case 2:
queryDefaults|=2;
break;
case 3:
queryDefaults|=4;
break;
case 4:
queryDefaults|=32;
break;
case 5:
queryDefaults|=64;
break;
}
if (mol.isRingBond$I(bond)) queryDefaults|=256;
 else if (!mol.isFragment$()) queryDefaults|=128;
if (mol.isAromaticBond$I(bond)) queryDefaults|=2097152;
 else if (!mol.isFragment$()) queryDefaults|=4194304;
return queryDefaults;
}, p$1);

Clazz.newMeth(C$, 'buildBridgeBondList',  function () {
this.mBridgeBondList=null;
for (var bond=0; bond < this.mFragment.getBonds$(); bond++) {
if (this.mFragment.isBondBridge$I(bond)) {
if (this.mBridgeBondList == null ) this.mBridgeBondList=Clazz.new_($I$(3,1));
var bridgeBond=Clazz.new_($I$(6,1),[this, null]);
bridgeBond.atom1=this.mFragment.getBondAtom$I$I(0, bond);
bridgeBond.atom2=this.mFragment.getBondAtom$I$I(1, bond);
bridgeBond.minBridgeSize=this.mFragment.getBondBridgeMinSize$I(bond);
bridgeBond.maxBridgeSize=this.mFragment.getBondBridgeMaxSize$I(bond);
this.mBridgeBondList.add$O(bridgeBond);
}}
}, p$1);

Clazz.newMeth(C$, 'copyOf$IA$I',  function (original, newLength) {
var copy=Clazz.array(Integer.TYPE, [newLength]);
System.arraycopy$O$I$O$I$I(original, 0, copy, 0, Math.min(original.length, newLength));
return copy;
}, 1);

Clazz.newMeth(C$, 'copyOf$ZA$I',  function (original, newLength) {
var copy=Clazz.array(Boolean.TYPE, [newLength]);
System.arraycopy$O$I$O$I$I(original, 0, copy, 0, Math.min(original.length, newLength));
return copy;
}, 1);
var $k$;
;
(function(){/*c*/var C$=Clazz.newClass(P$.SSSearcher, "BridgeBond", function(){
Clazz.newInstance(this, arguments[0],true,C$);
});

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['I',['atom1','atom2','minBridgeSize','maxBridgeSize']]]

Clazz.newMeth(C$);
})()
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-18 09:54:15 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
