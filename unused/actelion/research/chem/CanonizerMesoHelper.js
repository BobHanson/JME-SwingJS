(function(){var P$=Clazz.newPackage("com.actelion.research.chem"),p$1={},p$2={},p$3={},I$=[[0,'com.actelion.research.chem.CanonizerMesoHelper','java.util.TreeSet','com.actelion.research.chem.MesoFragmentBranch','com.actelion.research.chem.MesoFragmentMembers',['com.actelion.research.chem.CanonizerMesoHelper','.ESRGroupFragmentMatrix'],'java.util.ArrayList','com.actelion.research.chem.ESRGroupNormalizationInfo','com.actelion.research.chem.CanonizerRankListComparator','java.util.Arrays']],I$0=I$[0],$I$=function(i,n,m){return m?$I$(i)[n].apply(null,m):((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "CanonizerMesoHelper", function(){
Clazz.newInstance(this, arguments,0,C$);
});
C$.$classes$=[['ESRGroupFragmentMatrix',2]];

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['O',['mMol','com.actelion.research.chem.ExtendedMolecule','mCanRankWithoutStereo','int[]','mTHParity','byte[]','+mEZParity','+mTHESRType','+mTHESRGroup','mMesoFragmentAtom','int[][]','mIsStereoCenter','boolean[]','+mIsMesoFragmentMember','+mTHParityRoundIsOdd','+mEZParityRoundIsOdd','+mTHESRTypeNeedsNormalization','mESRGroupNormalizationInfoList','java.util.ArrayList']]]

Clazz.newMeth(C$, 'c$$com_actelion_research_chem_ExtendedMolecule$IA$ZA$BA$BA$BA$BA$BA$BA$ZA$ZA$ZA',  function (mol, canRankWithoutStereo, isStereoCenter, thParity, ezParity, thESRType, thESRGroup, ezESRType, ezESRGroup, thParityRoundIsOdd, ezParityRoundIsOdd, esrTypeNeedsNormalization) {
;C$.$init$.apply(this);
this.mMol=mol;
this.mCanRankWithoutStereo=canRankWithoutStereo;
this.mIsStereoCenter=isStereoCenter;
this.mTHParity=thParity;
this.mEZParity=ezParity;
this.mTHESRType=thESRType;
this.mTHESRGroup=thESRGroup;
this.mTHParityRoundIsOdd=thParityRoundIsOdd;
this.mEZParityRoundIsOdd=ezParityRoundIsOdd;
this.mTHESRTypeNeedsNormalization=esrTypeNeedsNormalization;
p$1.findMesoFragments.apply(this, []);
}, 1);

Clazz.newMeth(C$, 'isMeso$',  function () {
var meso=true;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mTHParity[atom] != 0 && !this.mIsMesoFragmentMember[atom] ) {
meso=false;
break;
}}
return meso;
});

Clazz.newMeth(C$, 'isInMesoFragment$I',  function (atom) {
return this.mIsMesoFragmentMember[atom];
});

Clazz.newMeth(C$, 'mayBeMirrorAtoms$I$I',  function (atom1, atom2) {
if (atom1 == atom2) return false;
if (this.mCanRankWithoutStereo[atom1] != this.mCanRankWithoutStereo[atom2]) return false;
if (this.mTHParity[atom1] != 0) {
if (this.mTHParity[atom1] == 3 || this.mTHParity[atom2] == 3 ) return false;
if (!!(this.mTHParityRoundIsOdd[atom1] ^ (this.mTHParity[atom1] != this.mTHParity[atom2]))) return false;
if (this.mTHESRType[atom1] != this.mTHESRType[atom2] || this.mTHESRGroup[atom1] != this.mTHESRGroup[atom2] ) return false;
}var bond=this.mMol.getBond$I$I(atom1, atom2);
if (bond != -1) {
if (this.mMol.getBondOrder$I(bond) == 1 && this.mEZParity[bond] != 0 ) return false;
if (this.mMol.getBondOrder$I(bond) == 2 && this.mEZParity[bond] == 1 ) return false;
}if (this.mMol.getAtomPi$I(atom1) == 1 && !this.mMol.isAromaticAtom$I(atom1) ) {
var bond1=-1;
for (var i=0; i < this.mMol.getConnAtoms$I(atom1); i++) {
if (this.mMol.getConnAtom$I$I(atom1, i) != atom2 && this.mMol.getConnBondOrder$I$I(atom1, i) == 2 ) {
bond1=this.mMol.getConnBond$I$I(atom1, i);
break;
}}
var bond2=-1;
for (var i=0; i < this.mMol.getConnAtoms$I(atom2); i++) {
if (this.mMol.getConnAtom$I$I(atom2, i) != atom1 && this.mMol.getConnBondOrder$I$I(atom2, i) == 2 ) {
bond2=this.mMol.getConnBond$I$I(atom2, i);
break;
}}
if (bond1 != -1 && this.mEZParity[bond1] != 0  && (!!(this.mEZParityRoundIsOdd[bond1] ^ (this.mEZParity[bond1] == this.mEZParity[bond2]))) ) return false;
}return true;
}, p$1);

Clazz.newMeth(C$, 'findMesoFragments',  function () {
var mesoFragmentList=Clazz.new_($I$(2,1));
for (var seedAtom=0; seedAtom < this.mMol.getAtoms$(); seedAtom++) {
if (this.mMol.getAtomPi$I(seedAtom) < 2 || this.mMol.getConnAtoms$I(seedAtom) > 2 ) {
for (var i=1; i < this.mMol.getConnAtoms$I(seedAtom); i++) {
var atom1=this.mMol.getConnAtom$I$I(seedAtom, i);
for (var j=0; j < i; j++) {
var atom2=this.mMol.getConnAtom$I$I(seedAtom, j);
if (p$1.mayBeMirrorAtoms$I$I.apply(this, [atom1, atom2])) p$1.tryAddNewMesoFragment$I$I$java_util_TreeSet.apply(this, [atom1, atom2, mesoFragmentList]);
}
}
}}
for (var seedBond=0; seedBond < this.mMol.getBonds$(); seedBond++) {
if (this.mEZParity[seedBond] != 0) {
if (this.mMol.getBondOrder$I(seedBond) != 2 || this.mEZParity[seedBond] != 2 ) continue;
}var atom1=this.mMol.getBondAtom$I$I(0, seedBond);
var atom2=this.mMol.getBondAtom$I$I(1, seedBond);
if (p$1.mayBeMirrorAtoms$I$I.apply(this, [atom1, atom2])) p$1.tryAddNewMesoFragment$I$I$java_util_TreeSet.apply(this, [atom1, atom2, mesoFragmentList]);
}
this.mMesoFragmentAtom=Clazz.array(Integer.TYPE, [mesoFragmentList.size$(), null]);
this.mIsMesoFragmentMember=Clazz.array(Boolean.TYPE, [this.mMol.getAtoms$()]);
var fragmentNo=0;
for (var members, $members = mesoFragmentList.iterator$(); $members.hasNext$()&&((members=($members.next$())),1);) {
this.mMesoFragmentAtom[fragmentNo++]=members.memberAtom;
for (var i=0; i < members.memberAtom.length; i++) this.mIsMesoFragmentMember[members.memberAtom[i]]=true;

}
}, p$1);

Clazz.newMeth(C$, 'tryAddNewMesoFragment$I$I$java_util_TreeSet',  function (atom1, atom2, mesoFragmentList) {
var members=p$1.tryFindMesoFragment$I$I.apply(this, [atom1, atom2]);
if (members != null  && members.hasStereoCenters$ZA(this.mIsStereoCenter) ) mesoFragmentList.add$O(members);
}, p$1);

Clazz.newMeth(C$, 'tryFindMesoFragment$I$I',  function (atom1, atom2) {
var graphAtom=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
var matchAtom=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
var isOrthogonal=Clazz.array(Boolean.TYPE, [this.mMol.getAtoms$()]);
var hasOrthogonality=Clazz.array(Boolean.TYPE, [this.mMol.getAtoms$()]);
var branch=Clazz.array($I$(3), [this.mMol.getAtoms$()]);
var members=Clazz.new_([this.mMol.getAtoms$()],$I$(4,1).c$$I);
graphAtom[0]=atom1;
matchAtom[atom1]=atom2;
matchAtom[atom2]=-2;
members.add$I(atom1);
members.add$I(atom2);
var current=0;
var highest=0;
while (current <= highest){
var currentAtom=graphAtom[current];
if (matchAtom[currentAtom] == currentAtom) {
for (var i=0; i < this.mMol.getConnAtoms$I(currentAtom); i++) {
var candidate=this.mMol.getConnAtom$I$I(currentAtom, i);
if (!members.isMember[candidate]) {
if (this.mMol.getConnBondOrder$I$I(currentAtom, i) == 2 && this.mMol.getAtomicNo$I(candidate) < 10 ) {
graphAtom[++highest]=candidate;
matchAtom[candidate]=candidate;
hasOrthogonality[candidate]=hasOrthogonality[currentAtom] || (this.mMol.getAtomPi$I(candidate) == 2) ;
isOrthogonal[candidate]=hasOrthogonality[currentAtom] ? !isOrthogonal[currentAtom] : false;
members.add$I(candidate);
} else if (hasOrthogonality[currentAtom] && isOrthogonal[currentAtom] ) {
var opponent=p$1.findMirrorAtom$I$I$ZA.apply(this, [candidate, matchAtom[currentAtom], members.isMember]);
if (opponent == -1) return null;
graphAtom[++highest]=candidate;
matchAtom[candidate]=opponent;
matchAtom[opponent]=-2;
hasOrthogonality[candidate]=false;
members.add$I(candidate);
members.add$I(opponent);
} else if (this.mMol.isRingBond$I(this.mMol.getConnBond$I$I(currentAtom, i))) {
graphAtom[++highest]=candidate;
matchAtom[candidate]=candidate;
hasOrthogonality[candidate]=false;
members.add$I(candidate);
if (p$1.isTetrahedral$I.apply(this, [candidate]) && this.mMol.getConnAtoms$I(candidate) > 2 ) {
var found=false;
for (var j=1; j < this.mMol.getConnAtoms$I(candidate); j++) {
var symAtom1=this.mMol.getConnAtom$I$I(candidate, j);
if (!members.isMember[symAtom1]) {
for (var k=0; k < j; k++) {
var symAtom2=this.mMol.getConnAtom$I$I(candidate, k);
if (!members.isMember[symAtom2]) {
if (p$1.mayBeMirrorAtoms$I$I.apply(this, [symAtom1, symAtom2])) {
graphAtom[++highest]=symAtom1;
matchAtom[symAtom1]=symAtom2;
matchAtom[symAtom2]=-2;
hasOrthogonality[symAtom1]=false;
members.add$I(symAtom1);
members.add$I(symAtom2);
found=true;
}}}
}}
if (!found) return null;
}}}}
} else {
var connIsOnMirrorPlane=Clazz.array(Boolean.TYPE, [this.mMol.getConnAtoms$I(currentAtom)]);
for (var i=0; i < this.mMol.getConnAtoms$I(currentAtom); i++) {
var candidate=this.mMol.getConnAtom$I$I(currentAtom, i);
if (members.isMember[candidate]) {
connIsOnMirrorPlane[i]=(matchAtom[candidate] == candidate);
} else {
for (var j=0; j < this.mMol.getConnAtoms$I(candidate); j++) {
if (this.mMol.getConnAtom$I$I(candidate, j) == matchAtom[currentAtom]) {
connIsOnMirrorPlane[i]=true;
break;
}}
}}
for (var i=0; i < this.mMol.getConnAtoms$I(currentAtom); i++) {
if (connIsOnMirrorPlane[i]) {
var candidate=this.mMol.getConnAtom$I$I(currentAtom, i);
if (members.isMember[candidate]) {
if (this.mMol.getBond$I$I(candidate, matchAtom[currentAtom]) == -1) return null;
} else {
graphAtom[++highest]=candidate;
matchAtom[candidate]=candidate;
isOrthogonal[candidate]=false;
hasOrthogonality[candidate]=true;
members.add$I(candidate);
}}}
var b=branch[currentAtom];
for (var i=(b == null ) ? 0 : b.neighbourIndex; i < this.mMol.getConnAtoms$I(currentAtom); i++) {
if (!connIsOnMirrorPlane[i]) {
var candidate=this.mMol.getConnAtom$I$I(currentAtom, i);
if (!members.isMember[candidate]) {
var opponent=p$1.findMirrorAtom$I$I$ZA.apply(this, [candidate, matchAtom[currentAtom], members.isMember]);
if (opponent == -1) return null;
graphAtom[++highest]=candidate;
matchAtom[candidate]=opponent;
matchAtom[opponent]=-2;
hasOrthogonality[candidate]=false;
members.add$I(candidate);
members.add$I(opponent);
}}}
}++current;
}
return members;
}, p$1);

Clazz.newMeth(C$, 'isTetrahedral$I',  function (atom) {
return (this.mMol.getAtomicNo$I(atom) == 6 && this.mMol.getAtomPi$I(atom) == 0 ) || (this.mMol.getAtomicNo$I(atom) == 7 && this.mMol.getAtomCharge$I(atom) == 1 ) || (this.mMol.getAtomicNo$I(atom) == 14) || (this.mMol.getAtomicNo$I(atom) == 15 && this.mMol.getConnAtoms$I(atom) > 2 ) || (this.mMol.getAtomicNo$I(atom) == 16 && this.mMol.getConnAtoms$I(atom) > 2 )  ;
}, p$1);

Clazz.newMeth(C$, 'findMirrorAtom$I$I$ZA',  function (atom, parentOfMirrorAtom, isFragmentMember) {
var candidate=Clazz.array(Integer.TYPE, [this.mMol.getConnAtoms$I(parentOfMirrorAtom)]);
var index=0;
for (var i=0; i < this.mMol.getConnAtoms$I(parentOfMirrorAtom); i++) {
candidate[index]=this.mMol.getConnAtom$I$I(parentOfMirrorAtom, i);
if (!isFragmentMember[candidate[index]] && p$1.mayBeMirrorAtoms$I$I.apply(this, [atom, candidate[index]]) ) ++index;
}
if (index == 0) return -1;
if (index == 1) return candidate[0];
var lowCandidate=-1;
var lowPathLength=2147483647;
for (var i=0; i < index; i++) {
var pathLength=this.mMol.getPathLength$I$I$I$ZA(atom, candidate[i], 2147483647, isFragmentMember);
if (pathLength < lowPathLength) {
lowPathLength=pathLength;
lowCandidate=candidate[i];
}}
return lowCandidate;
}, p$1);

Clazz.newMeth(C$, 'normalizeESRGroups$',  function () {
if (this.mMesoFragmentAtom != null ) {
var matrix=Clazz.new_($I$(5,1),[this, null]);
this.mESRGroupNormalizationInfoList=Clazz.new_($I$(6,1));
for (var fragment=0; fragment < this.mMesoFragmentAtom.length; fragment++) {
var dependentGroupCount=p$2.getDependentGroupCount$I.apply(matrix, [fragment]);
if (dependentGroupCount == 0) {
p$2.cutTiesOfIndependentGroups$I.apply(matrix, [fragment]);
var orCount=p$1.countESRGroups$I$I.apply(this, [fragment, 2]);
var andCount=p$1.countESRGroups$I$I.apply(this, [fragment, 1]);
var containsABS=p$1.containsTypeABSParity1Or2$I.apply(this, [fragment]);
if (orCount == 1 && andCount == 1  && !containsABS ) {
p$1.putORAtomsIntoANDGroup$I$I.apply(this, [fragment, p$2.newESRGroup$I.apply(matrix, [1])]);
this.mESRGroupNormalizationInfoList.add$O(Clazz.new_($I$(7,1).c$$I$I$I$I,[fragment, 1, -1, -1]));
}if (orCount > 0) {
if (containsABS) {
p$1.putABSAtomsIntoESRGroup$I$I$I.apply(this, [fragment, p$2.newESRGroup$I.apply(matrix, [2]), 2]);
++orCount;
}this.mESRGroupNormalizationInfoList.add$O(Clazz.new_($I$(7,1).c$$I$I$I$I,[fragment, 1, -1, -1]));
} else if (andCount > 0) {
if (containsABS) p$1.putABSAtomsIntoESRGroup$I$I$I.apply(this, [fragment, p$2.newESRGroup$I.apply(matrix, [1]), 1]);
this.mESRGroupNormalizationInfoList.add$O(Clazz.new_($I$(7,1).c$$I$I$I$I,[fragment, 1, -1, -1]));
} else if (containsABS) {
p$1.putABSAtomsIntoESRGroup$I$I$I.apply(this, [fragment, p$2.newESRGroup$I.apply(matrix, [1]), 1]);
this.mESRGroupNormalizationInfoList.add$O(Clazz.new_($I$(7,1).c$$I$I$I$I,[fragment, 1, -1, -1]));
}} else if (dependentGroupCount == 1) {
if (p$1.containsTypeABSParity1Or2$I.apply(this, [fragment])) {
var group=p$2.getDependentGroup$I.apply(matrix, [fragment]);
var type=p$2.getDependentType$I.apply(matrix, [fragment]);
this.mESRGroupNormalizationInfoList.add$O(Clazz.new_($I$(7,1).c$$I$I$I$I,[fragment, 2, group, type]));
} else {
p$2.cutTiesOfIndependentGroups$I.apply(matrix, [fragment]);
this.mESRGroupNormalizationInfoList.add$O(Clazz.new_($I$(7,1).c$$I$I$I$I,[fragment, 1, -1, -1]));
}}}
}});

Clazz.newMeth(C$, 'containsTypeABSParity1Or2$I',  function (fragment) {
for (var i=0; i < this.mMesoFragmentAtom[fragment].length; i++) {
var atom=this.mMesoFragmentAtom[fragment][i];
if (p$1.hasParity1or2$I.apply(this, [atom]) && this.mTHESRType[atom] == 0 ) return true;
}
return false;
}, p$1);

Clazz.newMeth(C$, 'countESRGroups$I$I',  function (fragment, esrType) {
var count=0;
var groupBits=0;
for (var i=0; i < this.mMesoFragmentAtom[fragment].length; i++) {
var atom=this.mMesoFragmentAtom[fragment][i];
if (this.mTHESRType[atom] == esrType) {
var groupBit=(1 << this.mTHESRGroup[atom]);
if ((groupBits & groupBit) == 0) {
groupBits|=groupBit;
++count;
}}}
return count;
}, p$1);

Clazz.newMeth(C$, 'hasParity1or2$I',  function (atom) {
return this.mIsStereoCenter[atom] && (this.mTHParity[atom] == 1 || this.mTHParity[atom] == 2 ) ;
}, p$1);

Clazz.newMeth(C$, 'putABSAtomsIntoESRGroup$I$I$I',  function (fragment, esrGroup, esrType) {
for (var j=0; j < this.mMesoFragmentAtom[fragment].length; j++) {
var atom=this.mMesoFragmentAtom[fragment][j];
if (p$1.hasParity1or2$I.apply(this, [atom]) && this.mTHESRType[atom] == 0 ) {
this.mTHESRType[atom]=(esrType|0);
this.mTHESRGroup[atom]=(esrGroup|0);
}}
}, p$1);

Clazz.newMeth(C$, 'putORAtomsIntoANDGroup$I$I',  function (fragment, esrGroup) {
for (var j=0; j < this.mMesoFragmentAtom[fragment].length; j++) {
var atom=this.mMesoFragmentAtom[fragment][j];
if (this.mTHESRType[atom] == 2) {
this.mTHESRType[atom]=(1|0);
this.mTHESRGroup[atom]=(esrGroup|0);
}}
}, p$1);

Clazz.newMeth(C$, 'normalizeESRGroupSwappingAndRemoval$IA',  function (canRank) {
if (this.mESRGroupNormalizationInfoList == null ) return false;
var doneAny=false;
for (var i=this.mESRGroupNormalizationInfoList.size$() - 1; i >= 0; i--) {
var done=false;
var info=this.mESRGroupNormalizationInfoList.get$I(i);
if (info.action == 2) {
done=p$1.normalizeESRGroupSwapping$I$I$I$IA.apply(this, [info.fragment, info.group, info.type, canRank]);
} else if (info.action == 1) {
done=p$1.removeESRGroupFromFragment$I$IA.apply(this, [info.fragment, canRank]);
}if (done) {
this.mESRGroupNormalizationInfoList.remove$O(info);
for (var j=0; j < this.mMesoFragmentAtom[info.fragment].length; j++) {
var atom=this.mMesoFragmentAtom[info.fragment][j];
this.mTHESRTypeNeedsNormalization[atom]=false;
}
doneAny=true;
}}
return doneAny;
});

Clazz.newMeth(C$, 'normalizeESRGroupSwapping$I$I$I$IA',  function (fragment, group, type, canRank) {
var groupAtom=null;
var absAtom=null;
for (var i=0; i < this.mMesoFragmentAtom[fragment].length; i++) {
var atom=this.mMesoFragmentAtom[fragment][i];
if (p$1.hasParity1or2$I.apply(this, [atom])) {
if (this.mTHESRType[atom] == 0) absAtom=C$.addToIntArray$IA$I(absAtom, (canRank[atom] << 16) + atom);
 else if (this.mTHESRType[atom] == type && this.mTHESRGroup[atom] == group ) groupAtom=C$.addToIntArray$IA$I(groupAtom, (canRank[atom] << 16) + atom);
}}
var comparison=Clazz.new_($I$(8,1)).compare$IA$IA(groupAtom, absAtom);
if (comparison == 0) return false;
if (comparison < 0) {
for (var i=0; i < this.mMesoFragmentAtom[fragment].length; i++) {
var atom=this.mMesoFragmentAtom[fragment][i];
if (p$1.hasParity1or2$I.apply(this, [atom])) {
if (this.mTHESRType[atom] == 0) {
this.mTHESRType[atom]=(type|0);
this.mTHESRGroup[atom]=(group|0);
} else if (this.mTHESRType[atom] == type && this.mTHESRGroup[atom] == group ) {
this.mTHESRType[atom]=(0|0);
this.mTHESRGroup[atom]=(-1|0);
}}}
}return true;
}, p$1);

Clazz.newMeth(C$, 'removeESRGroupFromFragment$I$IA',  function (fragment, canRank) {
var fragmentAtom=this.mMesoFragmentAtom[fragment];
var esrType=1;
for (var i=0; i < fragmentAtom.length; i++) {
var atom=fragmentAtom[i];
if (this.mIsStereoCenter[atom] && this.mTHESRType[atom] == 2 ) {
esrType=2;
break;
}}
var groupMember=Clazz.array(Integer.TYPE, [32, null]);
for (var i=0; i < fragmentAtom.length; i++) {
var atom=fragmentAtom[i];
if (this.mIsStereoCenter[atom] && this.mTHESRType[atom] == esrType ) groupMember[this.mTHESRGroup[atom]]=C$.addToIntArray$IA$I(groupMember[this.mTHESRGroup[atom]], (canRank[atom] << 16) + atom);
}
for (var i=0; i < 32; i++) if (groupMember[i] != null ) $I$(9).sort$IA(groupMember[i]);

$I$(9,"sort$OA$java_util_Comparator",[groupMember, Clazz.new_($I$(8,1))]);
if (Clazz.new_($I$(8,1)).compare$IA$IA(groupMember[0], groupMember[1]) == 0) return false;
for (var i=0; i < groupMember[0].length; i++) {
var atom=groupMember[0][i] & 65535;
this.mTHESRType[atom]=(0|0);
this.mTHESRGroup[atom]=(-1|0);
}
return true;
}, p$1);

Clazz.newMeth(C$, 'addToIntArray$IA$I',  function (intArray, intValue) {
var newArray=Clazz.array(Integer.TYPE, [(intArray == null ) ? 1 : intArray.length + 1]);
for (var i=0; i < newArray.length - 1; i++) newArray[i]=intArray[i];

newArray[newArray.length - 1]=intValue;
return newArray;
}, 1);
;
(function(){/*c*/var C$=Clazz.newClass(P$.CanonizerMesoHelper, "ESRGroupFragmentMatrix", function(){
Clazz.newInstance(this, arguments[0],true,C$);
});

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['I',['mAndGroupCount','mOrGroupCount','mGroupCount','mNewAndGroupCount','mNewOrGroupCount'],'O',['mMatrix','boolean[][]','mGroupDependence','int[]','mGroupNeighbour','int[][]']]]

Clazz.newMeth(C$, 'c$',  function () {
;C$.$init$.apply(this);
for (var atom=0; atom < this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mMol.getAtoms$(); atom++) {
if (p$1.hasParity1or2$I.apply(this.b$['com.actelion.research.chem.CanonizerMesoHelper'], [atom])) {
if (this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mTHESRType[atom] == 1) {
if (this.mAndGroupCount <= this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mTHESRGroup[atom]) this.mAndGroupCount=1 + this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mTHESRGroup[atom];
} else if (this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mTHESRType[atom] == 2) {
if (this.mOrGroupCount <= this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mTHESRGroup[atom]) this.mOrGroupCount=1 + this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mTHESRGroup[atom];
}}}
this.mGroupCount=this.mAndGroupCount + this.mOrGroupCount;
this.mMatrix=Clazz.array(Boolean.TYPE, [this.mGroupCount + 1, this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mMesoFragmentAtom.length + 1]);
for (var atom=0; atom < this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mMol.getAtoms$(); atom++) if (p$1.hasParity1or2$I.apply(this.b$['com.actelion.research.chem.CanonizerMesoHelper'], [atom]) && !this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mIsMesoFragmentMember[atom] ) this.mMatrix[p$2.groupIndex$I.apply(this, [atom])][this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mMesoFragmentAtom.length]=true;

for (var fragment=0; fragment < this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mMesoFragmentAtom.length; fragment++) {
for (var j=0; j < this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mMesoFragmentAtom[fragment].length; j++) {
var atom=this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mMesoFragmentAtom[fragment][j];
if (p$1.hasParity1or2$I.apply(this.b$['com.actelion.research.chem.CanonizerMesoHelper'], [atom])) this.mMatrix[p$2.groupIndex$I.apply(this, [atom])][fragment]=true;
}
}
this.mGroupNeighbour=Clazz.array(Integer.TYPE, [this.mGroupCount, null]);
for (var fragment=0; fragment < this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mMesoFragmentAtom.length; fragment++) {
for (var group1=1; group1 < this.mGroupCount; group1++) {
if (this.mMatrix[group1][fragment]) {
for (var group2=0; group2 < group1; group2++) {
if (this.mMatrix[group2][fragment]) {
this.mGroupNeighbour[group1]=$I$(1).addToIntArray$IA$I(this.mGroupNeighbour[group1], group2);
this.mGroupNeighbour[group2]=$I$(1).addToIntArray$IA$I(this.mGroupNeighbour[group2], group1);
}}
}}
}
this.mGroupDependence=Clazz.array(Integer.TYPE, [this.mGroupCount + 1]);
for (var group=0; group < this.mGroupCount; group++) {
if (this.mMatrix[group][this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mMesoFragmentAtom.length]) this.mGroupDependence[group]=-1;
 else this.mGroupDependence[group]=-2;
}
for (var fragment=0; fragment < this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mMesoFragmentAtom.length; fragment++) {
if (this.mMatrix[this.mGroupCount][fragment]) {
for (var group=0; group < this.mGroupCount; group++) {
if (this.mMatrix[group][fragment] && this.mGroupDependence[group] != fragment ) {
if (this.mGroupDependence[group] == -2) this.mGroupDependence[group]=fragment;
 else this.mGroupDependence[group]=-3;
}}
}}
for (var anchorGroup=0; anchorGroup < this.mGroupCount; anchorGroup++) {
if (this.mGroupDependence[anchorGroup] >= -1) {
var chainMemberLevel=Clazz.array(Integer.TYPE, [this.mGroupCount]);
if (p$2.extendAnchorChain$IA$I.apply(this, [chainMemberLevel, anchorGroup])) {
for (var group=0; group < this.mGroupCount; group++) {
if (chainMemberLevel[group] != 0) this.mGroupDependence[group]=-3;
}
}}}
for (var fragment=0; fragment < this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mMesoFragmentAtom.length - 1; fragment++) {
for (var group1=1; group1 < this.mGroupCount; group1++) {
if (this.mMatrix[group1][fragment] && this.mGroupDependence[group1] != -3 ) {
for (var group2=0; group2 < group1; group2++) {
if (this.mMatrix[group2][fragment] && this.mGroupDependence[group2] != -3 ) {
var cycle=p$2.getDependencyCycle$I$I$I.apply(this, [group1, group2, fragment]);
if (cycle != null ) {
for (var i=0; i < cycle.length; i++) this.mGroupDependence[cycle[i]]=-3;

p$2.removeOneGroupFromCycle$IA.apply(this, [cycle]);
break;
}}}
}}
}
}, 1);

Clazz.newMeth(C$, 'extendAnchorChain$IA$I',  function (chainMemberLevel, anchorGroup) {
var secondAnchorFound=false;
var level=1;
chainMemberLevel[anchorGroup]=level;
var chainExtentionFound=true;
while (chainExtentionFound){
chainExtentionFound=false;
for (var group1=0; group1 < this.mGroupCount; group1++) {
if (chainMemberLevel[group1] == level) {
for (var group2=0; group2 < this.mGroupCount; group2++) {
if (chainMemberLevel[group2] == 0 && p$2.groupsShareFragment$I$I.apply(this, [group1, group2]) ) {
if (this.mGroupDependence[group2] == -2) {
chainMemberLevel[group2]=level + 1;
chainExtentionFound=true;
} else if (this.mGroupDependence[group2] != this.mGroupDependence[anchorGroup]) {
chainMemberLevel[group2]=level + 1;
secondAnchorFound=true;
}}}
}}
++level;
}
return secondAnchorFound;
}, p$2);

Clazz.newMeth(C$, 'getDependencyCycle$I$I$I',  function (group1, group2, startFragment) {
for (var fragment=startFragment + 1; fragment < this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mMesoFragmentAtom.length; fragment++) {
if (fragment != startFragment && this.mMatrix[group1][fragment]  && this.mMatrix[group2][fragment] ) {
var cycle=Clazz.array(Integer.TYPE, [2]);
cycle[0]=group2;
cycle[1]=group1;
return cycle;
}}
var parentGroup=Clazz.array(Integer.TYPE, [this.mGroupCount]);
var graphLevel=Clazz.array(Integer.TYPE, [this.mGroupCount]);
var graphGroup=Clazz.array(Integer.TYPE, [this.mGroupCount]);
var current=0;
var highest=0;
graphGroup[0]=group1;
graphLevel[group1]=1;
while (current <= highest){
for (var i=0; i < this.mGroupNeighbour[graphGroup[current]].length; i++) {
var candidate=this.mGroupNeighbour[graphGroup[current]][i];
if (candidate == group2) {
if (current == 0) continue;
var cycleLength=graphLevel[graphGroup[current]] + 1;
var cycle=Clazz.array(Integer.TYPE, [cycleLength]);
cycle[0]=candidate;
cycle[1]=graphGroup[current];
for (var j=2; j < cycleLength; j++) cycle[j]=parentGroup[cycle[j - 1]];

return cycle;
}if (graphLevel[candidate] == 0 && this.mGroupDependence[candidate] != -3 ) {
graphLevel[candidate]=graphLevel[graphGroup[current]] + 1;
graphGroup[++highest]=candidate;
parentGroup[candidate]=graphGroup[current];
}}
++current;
}
return null;
}, p$2);

Clazz.newMeth(C$, 'removeOneGroupFromCycle$IA',  function (cycle) {
var minRank=2147483647;
var minGroup=-1;
var minType=-1;
var minGroupIndex=-1;
for (var atom=0; atom < this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mMol.getAtoms$(); atom++) {
if (p$1.hasParity1or2$I.apply(this.b$['com.actelion.research.chem.CanonizerMesoHelper'], [atom]) && this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mTHESRType[atom] != 0 ) {
for (var i=0; i < cycle.length; i++) {
var esrGroup=p$2.getESRGroup$I.apply(this, [cycle[i]]);
var esrType=p$2.getESRType$I.apply(this, [cycle[i]]);
if (this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mTHESRType[atom] == esrType && this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mTHESRGroup[atom] == esrGroup ) {
if (minRank > this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mCanRankWithoutStereo[atom] + ((esrType == 1) ? 65536 : 0)) {
minRank=this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mCanRankWithoutStereo[atom] + ((esrType == 1) ? 65536 : 0);
minGroup=esrGroup;
minType=esrType;
minGroupIndex=cycle[i];
}}}
}}
for (var atom=0; atom < this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mMol.getAtoms$(); atom++) {
if (p$1.hasParity1or2$I.apply(this.b$['com.actelion.research.chem.CanonizerMesoHelper'], [atom]) && this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mTHESRType[atom] == minType  && this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mTHESRGroup[atom] == minGroup ) {
this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mTHESRType[atom]=(0|0);
this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mTHESRGroup[atom]=(-1|0);
}}
for (var fragment=0; fragment < this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mMesoFragmentAtom.length; fragment++) this.mMatrix[minGroupIndex][fragment]=false;

}, p$2);

Clazz.newMeth(C$, 'groupsShareFragment$I$I',  function (group1, group2) {
for (var fragment=0; fragment < this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mMesoFragmentAtom.length; fragment++) if (this.mMatrix[group1][fragment] && this.mMatrix[group2][fragment] ) return true;

return false;
}, p$2);

Clazz.newMeth(C$, 'getDependentGroupCount$I',  function (fragment) {
var count=0;
for (var group=0; group < this.mGroupCount; group++) if (this.mMatrix[group][fragment] && this.mGroupDependence[group] == -3 ) ++count;

return count;
}, p$2);

Clazz.newMeth(C$, 'getDependentType$I',  function (fragment) {
for (var group=0; group < this.mGroupCount; group++) if (this.mMatrix[group][fragment] && this.mGroupDependence[group] == -3 ) return p$2.getESRType$I.apply(this, [group]);

return -1;
}, p$2);

Clazz.newMeth(C$, 'getDependentGroup$I',  function (fragment) {
for (var group=0; group < this.mGroupCount; group++) if (this.mMatrix[group][fragment] && this.mGroupDependence[group] == -3 ) return p$2.getESRGroup$I.apply(this, [group]);

return -1;
}, p$2);

Clazz.newMeth(C$, 'getESRType$I',  function (group) {
return (group < this.mAndGroupCount) ? 1 : (group < this.mGroupCount) ? 2 : 0;
}, p$2);

Clazz.newMeth(C$, 'getESRGroup$I',  function (group) {
return (group < this.mAndGroupCount) ? group : (group < this.mGroupCount) ? group - this.mAndGroupCount : -1;
}, p$2);

Clazz.newMeth(C$, 'groupIndex$I',  function (atom) {
var type=this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mTHESRType[atom];
var group=this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mTHESRGroup[atom];
return (type == 0) ? this.mGroupCount : (type == 1) ? group : this.mAndGroupCount + group;
}, p$2);

Clazz.newMeth(C$, 'cutTiesOfIndependentGroups$I',  function (fragment) {
for (var group=0; group < this.mGroupCount; group++) {
if (this.mMatrix[group][fragment] && this.mGroupDependence[group] != -3 ) {
for (var f=0; f <= this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mMesoFragmentAtom.length; f++) {
if (f != fragment && this.mMatrix[group][f] ) {
this.mMatrix[group][fragment]=false;
var oldESRGroup=p$2.getESRGroup$I.apply(this, [group]);
var newESRGroup=p$2.newESRGroup$I.apply(this, [p$2.getESRType$I.apply(this, [group])]);
for (var i=0; i < this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mMesoFragmentAtom[fragment].length; i++) {
var atom=this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mMesoFragmentAtom[fragment][i];
if (p$1.hasParity1or2$I.apply(this.b$['com.actelion.research.chem.CanonizerMesoHelper'], [atom]) && this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mTHESRGroup[atom] == oldESRGroup ) this.b$['com.actelion.research.chem.CanonizerMesoHelper'].mTHESRGroup[atom]=(newESRGroup|0);
}
}}
}}
}, p$2);

Clazz.newMeth(C$, 'newESRGroup$I',  function (esrType) {
return (esrType == 1) ? this.mAndGroupCount + this.mNewAndGroupCount++ : this.mOrGroupCount + this.mNewOrGroupCount++;
}, p$2);
})()

Clazz.newMeth(C$);
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-25 13:07:44 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
