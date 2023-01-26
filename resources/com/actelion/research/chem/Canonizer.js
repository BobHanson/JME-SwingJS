(function(){var P$=Clazz.newPackage("com.actelion.research.chem"),p$1={},I$=[[0,'java.util.Arrays','com.actelion.research.chem.CanonizerMesoHelper','java.util.ArrayList','com.actelion.research.chem.CanonizerBond','com.actelion.research.chem.CanonizerBaseValue','com.actelion.research.chem.SortedStringList','com.actelion.research.chem.CanonizerFragment','com.actelion.research.chem.EZHalfParity','com.actelion.research.chem.StereoMolecule','com.actelion.research.chem.Molecule','StringBuilder','com.actelion.research.chem.Canonizer$1RankObject','com.actelion.research.chem.Canonizer$2RankObject']],I$0=I$[0],$I$=function(i,n,m){return m?$I$(i)[n].apply(null,m):((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "Canonizer", function(){
Clazz.newInstance(this, arguments,0,C$);
});
C$.$classes$=[['ESRGroup',0]];

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['Z',['mIsMeso','mStereoCentersFound','mIsOddParityRound','mZCoordinatesAvailable','mCIPParityNoDistinctionProblem','mEncodeAvoid127','mGraphGenerated'],'I',['mMode','mNoOfRanks','mNoOfPseudoGroups','mGraphRings','mFeatureBlock','mEncodingBitsAvail','mEncodingTempData','mAtomBits','mMaxConnAtoms'],'S',['mIDCode','mEncodedCoords','mMapping'],'O',['mMol','com.actelion.research.chem.StereoMolecule','mCanRank','int[]','+mCanRankBeforeTieBreaking','+mPseudoTHGroup','+mPseudoEZGroup','mTHParity','byte[]','+mEZParity','+mTHConfiguration','+mEZConfiguration','+mTHCIPParity','+mEZCIPParity','+mTHESRType','+mTHESRGroup','+mEZESRType','+mEZESRGroup','+mAbnormalValence','mCanBase','com.actelion.research.chem.CanonizerBaseValue[]','mMesoHelper','com.actelion.research.chem.CanonizerMesoHelper','mIsStereoCenter','boolean[]','+mTHParityIsMesoInverted','+mTHParityNeedsNormalization','+mTHESRTypeNeedsNormalization','+mTHParityRoundIsOdd','+mEZParityRoundIsOdd','+mTHParityIsPseudo','+mEZParityIsPseudo','+mProTHAtomsInSameFragment','+mProEZAtomsInSameFragment','+mNitrogenQualifiesForParity','mFragmentList','java.util.ArrayList','+mTHParityNormalizationGroupList','mGraphAtom','int[]','+mGraphIndex','+mGraphBond','+mGraphFrom','+mGraphClosure','mEncodingBuffer','StringBuilder']]]

Clazz.newMeth(C$, 'c$$com_actelion_research_chem_StereoMolecule',  function (mol) {
C$.c$$com_actelion_research_chem_StereoMolecule$I.apply(this, [mol, 0]);
}, 1);

Clazz.newMeth(C$, 'c$$com_actelion_research_chem_StereoMolecule$I',  function (mol, mode) {
;C$.$init$.apply(this);
this.mMol=mol;
this.mMode=mode;
this.mMol.ensureHelperArrays$I(7);
this.mAtomBits=C$.getNeededBits$I(this.mMol.getAtoms$());
if ((this.mMode & 2048) == 0) p$1.canFindNitrogenQualifyingForParity.apply(this, []);
this.mZCoordinatesAvailable=((mode & 64) != 0) || this.mMol.is3D$() ;
if ((this.mMode & 2048) == 0) {
this.mTHParity=Clazz.array(Byte.TYPE, [this.mMol.getAtoms$()]);
this.mTHParityIsPseudo=Clazz.array(Boolean.TYPE, [this.mMol.getAtoms$()]);
this.mTHParityRoundIsOdd=Clazz.array(Boolean.TYPE, [this.mMol.getAtoms$()]);
this.mEZParity=Clazz.array(Byte.TYPE, [this.mMol.getBonds$()]);
this.mEZParityRoundIsOdd=Clazz.array(Boolean.TYPE, [this.mMol.getBonds$()]);
this.mEZParityIsPseudo=Clazz.array(Boolean.TYPE, [this.mMol.getBonds$()]);
}this.mCIPParityNoDistinctionProblem=false;
p$1.canInitializeRanking.apply(this, []);
if ((this.mMode & 2048) == 0) p$1.canRankStereo.apply(this, []);
p$1.canRankFinal.apply(this, []);
}, 1);

Clazz.newMeth(C$, 'hasCIPParityDistinctionProblem$',  function () {
return this.mCIPParityNoDistinctionProblem;
});

Clazz.newMeth(C$, 'canFindNitrogenQualifyingForParity',  function () {
this.mNitrogenQualifiesForParity=Clazz.array(Boolean.TYPE, [this.mMol.getAtoms$()]);
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mMol.getAtomicNo$I(atom) == 7) {
if (this.mMol.getConnAtoms$I(atom) == 4) {
this.mNitrogenQualifiesForParity[atom]=true;
continue;
}if (this.mMol.getConnAtoms$I(atom) == 3) {
if (this.mMol.getAtomRingSize$I(atom) == 3) {
this.mNitrogenQualifiesForParity[atom]=true;
continue;
}if (this.mMol.getAtomCharge$I(atom) == 1) {
this.mNitrogenQualifiesForParity[atom]=true;
continue;
}if (this.mMol.isFlatNitrogen$I(atom)) continue;
if ((this.mMode & 32) != 0) {
this.mNitrogenQualifiesForParity[atom]=true;
continue;
}if (this.mMol.getAtomRingBondCount$I(atom) != 3) continue;
var smallRingSize=this.mMol.getAtomRingSize$I(atom);
if (smallRingSize > 7) continue;
var ringSet=this.mMol.getRingSet$();
var smallRingNo=0;
while (smallRingNo < ringSet.getSize$()){
if (ringSet.getRingSize$I(smallRingNo) == smallRingSize && ringSet.isAtomMember$I$I(smallRingNo, atom) ) break;
++smallRingNo;
}
if (smallRingNo >= 1024 && smallRingNo == ringSet.getSize$() ) continue;
var firstBridgeAtom=-1;
var firstBridgeBond=-1;
for (var i=0; i < 3; i++) {
var connBond=this.mMol.getConnBond$I$I(atom, i);
if (!ringSet.isBondMember$I$I(smallRingNo, connBond)) {
firstBridgeAtom=this.mMol.getConnAtom$I$I(atom, i);
firstBridgeBond=connBond;
break;
}}
var neglectBond=Clazz.array(Boolean.TYPE, [this.mMol.getBonds$()]);
neglectBond[firstBridgeBond]=true;
var pathAtom=Clazz.array(Integer.TYPE, [11]);
var pathLength=this.mMol.getPath$IA$I$I$I$ZA(pathAtom, firstBridgeAtom, atom, 10, neglectBond);
if (pathLength == -1) continue;
var bridgeAtomCount=1;
while (!ringSet.isAtomMember$I$I(smallRingNo, pathAtom[bridgeAtomCount]))++bridgeAtomCount;

var bondCountToBridgeHead=pathLength - bridgeAtomCount;
var bridgeHead=pathAtom[bridgeAtomCount];
if (smallRingSize == 6 && bondCountToBridgeHead == 2  && bridgeAtomCount == 3 ) {
if (this.mMol.getAtomRingBondCount$I(pathAtom[1]) >= 3) {
var isAdamantane=false;
var ringAtom=ringSet.getRingAtoms$I(smallRingNo);
for (var i=0; i < 6; i++) {
if (atom == ringAtom[i]) {
var potentialOtherBridgeHeadIndex=ringSet.validateMemberIndex$I$I(smallRingNo, (bridgeHead == ringAtom[ringSet.validateMemberIndex$I$I(smallRingNo, i + 2)]) ? i - 2 : i + 2);
var potentialOtherBridgeHead=ringAtom[potentialOtherBridgeHeadIndex];
if (this.mMol.getAtomRingBondCount$I(potentialOtherBridgeHead) >= 3 && this.mMol.getPathLength$I$I$I$ZA(pathAtom[1], potentialOtherBridgeHead, 2, null) == 2 ) isAdamantane=true;
break;
}}
if (isAdamantane) {
this.mNitrogenQualifiesForParity[atom]=true;
continue;
}}}var bridgeHeadIsFlat=(this.mMol.getAtomPi$I(bridgeHead) == 1 || this.mMol.isAromaticAtom$I(bridgeHead)  || this.mMol.isFlatNitrogen$I(bridgeHead) );
var bridgeHeadMayInvert=!bridgeHeadIsFlat && this.mMol.getAtomicNo$I(bridgeHead) == 7  && this.mMol.getAtomCharge$I(bridgeHead) != 1 ;
if (bondCountToBridgeHead == 1) {
if (!bridgeHeadIsFlat && !bridgeHeadMayInvert && smallRingSize <= 4   && bridgeAtomCount <= 3 ) this.mNitrogenQualifiesForParity[atom]=true;
continue;
}switch (smallRingSize) {
case 4:
if (!bridgeHeadIsFlat && !bridgeHeadMayInvert ) {
if (bridgeAtomCount <= 4) this.mNitrogenQualifiesForParity[atom]=true;
}break;
case 5:
if (bridgeHeadMayInvert) {
if (bridgeAtomCount <= 3) this.mNitrogenQualifiesForParity[atom]=true;
} else if (!bridgeHeadIsFlat) {
if (bridgeAtomCount <= 4) this.mNitrogenQualifiesForParity[atom]=true;
}break;
case 6:
if (bondCountToBridgeHead == 2) {
if (bridgeHeadIsFlat) {
if (bridgeAtomCount <= 4) this.mNitrogenQualifiesForParity[atom]=true;
} else if (!bridgeHeadMayInvert) {
if (bridgeAtomCount <= 3) this.mNitrogenQualifiesForParity[atom]=true;
}} else if (bondCountToBridgeHead == 3) {
if (bridgeHeadIsFlat) {
if (bridgeAtomCount <= 6) this.mNitrogenQualifiesForParity[atom]=true;
} else {
if (bridgeAtomCount <= 4) this.mNitrogenQualifiesForParity[atom]=true;
}}break;
case 7:
if (bondCountToBridgeHead == 3) {
if (bridgeAtomCount <= 3) this.mNitrogenQualifiesForParity[atom]=true;
}break;
}
}}}
}, p$1);

Clazz.newMeth(C$, 'canCalcImplicitAbnormalValence$I',  function (atom) {
var explicitAbnormalValence=this.mMol.getAtomAbnormalValence$I(atom);
var implicitHigherValence=this.mMol.getImplicitHigherValence$I$Z(atom, false);
var newImplicitHigherValence=this.mMol.getImplicitHigherValence$I$Z(atom, true);
var valence=-1;
if (implicitHigherValence != newImplicitHigherValence) {
if (explicitAbnormalValence != -1 && explicitAbnormalValence > implicitHigherValence ) valence=($b$[0] = explicitAbnormalValence, $b$[0]);
 else valence=($b$[0] = implicitHigherValence, $b$[0]);
} else if (explicitAbnormalValence != -1) {
if (explicitAbnormalValence > newImplicitHigherValence || (explicitAbnormalValence < newImplicitHigherValence && explicitAbnormalValence >= this.mMol.getOccupiedValence$I(atom) ) ) valence=($b$[0] = explicitAbnormalValence, $b$[0]);
} else if (!this.mMol.supportsImplicitHydrogen$I(atom) && this.mMol.getExplicitHydrogens$I(atom) != 0 ) {
valence=this.mMol.getOccupiedValence$I(atom);
valence-=this.mMol.getElectronValenceCorrection$I$I(atom, valence);
}p$1.canSetAbnormalValence$I$I.apply(this, [atom, valence]);
return valence;
}, p$1);

Clazz.newMeth(C$, 'canSetAbnormalValence$I$I',  function (atom, valence) {
if (this.mAbnormalValence == null ) {
this.mAbnormalValence=Clazz.array(Byte.TYPE, [this.mMol.getAtoms$()]);
$I$(1).fill$BA$B(this.mAbnormalValence, -1);
}this.mAbnormalValence[atom]=(valence|0);
}, p$1);

Clazz.newMeth(C$, 'canRankStereo',  function () {
var noOfRanksWithoutStereo=this.mNoOfRanks;
var canRankWithoutStereo=$I$(1,"copyOf$IA$I",[this.mCanRank, this.mMol.getAtoms$()]);
if (!this.mMol.isFragment$()) {
p$1.canRecursivelyFindCIPParities.apply(this, []);
p$1.initializeParities$I$IA.apply(this, [noOfRanksWithoutStereo, canRankWithoutStereo]);
}this.mTHESRType=Clazz.array(Byte.TYPE, [this.mMol.getAtoms$()]);
this.mTHESRGroup=Clazz.array(Byte.TYPE, [this.mMol.getAtoms$()]);
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
this.mTHESRType[atom]=(this.mMol.getAtomESRType$I(atom)|0);
this.mTHESRGroup[atom]=(this.mMol.getAtomESRGroup$I(atom)|0);
}
this.mEZESRType=Clazz.array(Byte.TYPE, [this.mMol.getBonds$()]);
this.mEZESRGroup=Clazz.array(Byte.TYPE, [this.mMol.getBonds$()]);
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
this.mEZESRType[bond]=(this.mMol.getBondESRType$I(bond)|0);
this.mEZESRGroup[bond]=(this.mMol.getBondESRGroup$I(bond)|0);
}
p$1.canRecursivelyFindAllParities.apply(this, []);
this.mStereoCentersFound=false;
this.mIsStereoCenter=Clazz.array(Boolean.TYPE, [this.mMol.getAtoms$()]);
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mTHParity[atom] != 0) {
this.mIsStereoCenter[atom]=true;
this.mStereoCentersFound=true;
}}
p$1.canRemoveOverspecifiedESRGroups.apply(this, []);
this.mMesoHelper=null;
this.mTHESRTypeNeedsNormalization=Clazz.array(Boolean.TYPE, [this.mMol.getAtoms$()]);
if (this.mStereoCentersFound) {
this.mMesoHelper=Clazz.new_($I$(2,1).c$$com_actelion_research_chem_ExtendedMolecule$IA$ZA$BA$BA$BA$BA$BA$BA$ZA$ZA$ZA,[this.mMol, canRankWithoutStereo, this.mIsStereoCenter, this.mTHParity, this.mEZParity, this.mTHESRType, this.mTHESRGroup, this.mEZESRType, this.mEZESRGroup, this.mTHParityRoundIsOdd, this.mEZParityRoundIsOdd, this.mTHESRTypeNeedsNormalization]);
this.mMesoHelper.normalizeESRGroups$();
}this.mTHParityIsMesoInverted=Clazz.array(Boolean.TYPE, [this.mMol.getAtoms$()]);
this.mTHParityNeedsNormalization=Clazz.array(Boolean.TYPE, [this.mMol.getAtoms$()]);
this.mTHParityNormalizationGroupList=Clazz.new_($I$(3,1));
if (this.mMesoHelper != null ) this.mMesoHelper.normalizeESRGroupSwappingAndRemoval$IA(this.mCanRank);
p$1.canMarkESRGroupsForParityNormalization.apply(this, []);
p$1.initializeParities$I$IA.apply(this, [noOfRanksWithoutStereo, canRankWithoutStereo]);
p$1.canRecursivelyFindCanonizedParities.apply(this, []);
if (this.mMesoHelper != null ) this.mIsMeso=this.mMesoHelper.isMeso$();
p$1.determineChirality$IA.apply(this, [canRankWithoutStereo]);
}, p$1);

Clazz.newMeth(C$, 'canRankFinal',  function () {
if ((this.mMode & 1) != 0 && (this.mMode & 2) == 0 ) {
this.mCanRankBeforeTieBreaking=$I$(1,"copyOf$IA$I",[this.mCanRank, this.mMol.getAtoms$()]);
}if ((this.mMode & 2048) == 0) {
this.mProTHAtomsInSameFragment=Clazz.array(Boolean.TYPE, [this.mMol.getAtoms$()]);
this.mProEZAtomsInSameFragment=Clazz.array(Boolean.TYPE, [this.mMol.getBonds$()]);
if (this.mNoOfRanks < this.mMol.getAtoms$()) {
p$1.canBreakTiesByHeteroTopicity.apply(this, []);
if ((this.mMode & 2048) == 0) {
p$1.canNormalizeGroupParities.apply(this, []);
if (this.mMesoHelper != null ) this.mMesoHelper.normalizeESRGroupSwappingAndRemoval$IA(this.mCanRank);
}}}if (this.mCanRankBeforeTieBreaking == null  && (this.mMode & 1) != 0  && (this.mMode & 2) != 0 ) this.mCanRankBeforeTieBreaking=$I$(1,"copyOf$IA$I",[this.mCanRank, this.mMol.getAtoms$()]);
while (this.mNoOfRanks < this.mMol.getAtoms$()){
p$1.canBreakTiesRandomly.apply(this, []);
if ((this.mMode & 2048) == 0) {
p$1.canNormalizeGroupParities.apply(this, []);
if (this.mMesoHelper != null ) this.mMesoHelper.normalizeESRGroupSwappingAndRemoval$IA(this.mCanRank);
}}
if ((this.mMode & 2048) == 0) {
p$1.canNormalizeGroupParities.apply(this, []);
p$1.canFindPseudoParities.apply(this, []);
p$1.flagStereoProblems.apply(this, []);
}}, p$1);

Clazz.newMeth(C$, 'canBreakTiesByHeteroTopicity',  function () {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
this.mCanBase[atom].init$I(atom);
this.mCanBase[atom].add$I$J((2 * this.mAtomBits + 4), Long.$sl(this.mCanRank[atom],(this.mAtomBits + 4)));
}
var found=false;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) found=!!(found|(p$1.canCalcTHParity$I$I.apply(this, [atom, 3])));

for (var bond=0; bond < this.mMol.getBonds$(); bond++) found=!!(found|(p$1.canCalcEZParity$I$I.apply(this, [bond, 3])));

if (!found) return false;
while (this.mNoOfRanks < this.mMol.getAtoms$()){
found=p$1.canInnerBreakTiesByHeteroTopicity.apply(this, []);
if (!found) break;
p$1.canNormalizeGroupParities.apply(this, []);
if (this.mMesoHelper != null ) this.mMesoHelper.normalizeESRGroupSwappingAndRemoval$IA(this.mCanRank);
}
return true;
}, p$1);

Clazz.newMeth(C$, 'canInnerBreakTiesByHeteroTopicity',  function () {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
this.mCanBase[atom].init$I(atom);
this.mCanBase[atom].add$I$J((2 * this.mAtomBits + 4), Long.$sl(this.mCanRank[atom],(this.mAtomBits + 4)));
}
for (var rank=1; rank <= this.mNoOfRanks; rank++) {
var found=false;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) if (this.mCanRank[atom] == rank) found=!!(found|(p$1.canCalcTHParity$I$I.apply(this, [atom, 2])));

if (found) {
var oldRanks=this.mNoOfRanks;
this.mNoOfRanks=p$1.canPerformRanking.apply(this, []);
if (this.mNoOfRanks != oldRanks) return true;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
this.mCanBase[atom].init$I(atom);
this.mCanBase[atom].add$I$J((2 * this.mAtomBits + 4), Long.$sl(this.mCanRank[atom],(this.mAtomBits + 4)));
}
}}
var rankedBond=Clazz.array($I$(4), [this.mMol.getBonds$()]);
for (var i=0; i < rankedBond.length; i++) rankedBond[i]=Clazz.new_([this.mCanRank[this.mMol.getBondAtom$I$I(0, i)], this.mCanRank[this.mMol.getBondAtom$I$I(1, i)], i],$I$(4,1).c$$I$I$I);

$I$(1).sort$OA(rankedBond);
for (var i=0; i < rankedBond.length; i++) {
if (p$1.canCalcEZParity$I$I.apply(this, [rankedBond[i].bond, 2])) {
while (i + 1 < rankedBond.length && rankedBond[i].compareTo$com_actelion_research_chem_CanonizerBond(rankedBond[i + 1]) == 0 )p$1.canCalcEZParity$I$I.apply(this, [rankedBond[++i].bond, 2]);

var oldRanks=this.mNoOfRanks;
this.mNoOfRanks=p$1.canPerformRanking.apply(this, []);
if (this.mNoOfRanks != oldRanks) return true;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
this.mCanBase[atom].init$I(atom);
this.mCanBase[atom].add$I$J((2 * this.mAtomBits + 4), Long.$sl(this.mCanRank[atom],(this.mAtomBits + 4)));
}
}}
return false;
}, p$1);

Clazz.newMeth(C$, 'canBreakTiesRandomly',  function () {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
this.mCanBase[atom].init$I(atom);
this.mCanBase[atom].add$I$J(this.mAtomBits + 1, 2 * this.mCanRank[atom]);
}
var rankCount=Clazz.array(Integer.TYPE, [this.mNoOfRanks + 1]);
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) ++rankCount[this.mCanRank[atom]];

var rank=1;
while (rankCount[rank] == 1)++rank;

for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mCanRank[atom] == rank) {
this.mCanBase[atom].add$J(1);
break;
}}
this.mNoOfRanks=p$1.canPerformRanking.apply(this, []);
}, p$1);

Clazz.newMeth(C$, 'initializeParities$I$IA',  function (noOfRanksWithoutStereo, canRankWithoutStereo) {
this.mNoOfRanks=noOfRanksWithoutStereo;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
this.mCanRank[atom]=canRankWithoutStereo[atom];
this.mTHParity[atom]=(0|0);
this.mTHParityRoundIsOdd[atom]=false;
}
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
this.mEZParity[bond]=(0|0);
this.mEZParityRoundIsOdd[bond]=false;
}
}, p$1);

Clazz.newMeth(C$, 'canInitializeRanking',  function () {
var bondQueryFeaturesPresent=false;
if (this.mMol.isFragment$()) {
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mMol.getBondQueryFeatures$I(bond) != 0) {
bondQueryFeaturesPresent=true;
break;
}}
}this.mMaxConnAtoms=2;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) this.mMaxConnAtoms=Math.max(this.mMaxConnAtoms, this.mMol.getConnAtoms$I(atom) + this.mMol.getMetalBondedConnAtoms$I(atom));

var baseValueSize=Math.max(2, bondQueryFeaturesPresent ? ((62 + this.mAtomBits + this.mMaxConnAtoms * (this.mAtomBits + 23) )/63|0) : ((62 + this.mAtomBits + this.mMaxConnAtoms * (this.mAtomBits + 5) )/63|0));
this.mCanRank=Clazz.array(Integer.TYPE, [this.mMol.getAllAtoms$()]);
this.mCanBase=Clazz.array($I$(5), [this.mMol.getAtoms$()]);
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) this.mCanBase[atom]=Clazz.new_($I$(5,1).c$$I,[baseValueSize]);

var atomListFound=false;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
this.mCanBase[atom].init$I(atom);
if ((Long.$ne((Long.$and(this.mMol.getAtomQueryFeatures$I(atom),1)),0 )) || this.mMol.getAtomList$I(atom) != null  ) this.mCanBase[atom].add$I$J(8, 6);
 else this.mCanBase[atom].add$I$J(8, this.mMol.getAtomicNo$I(atom));
this.mCanBase[atom].add$I$J(8, this.mMol.getAtomMass$I(atom));
this.mCanBase[atom].add$I$J(2, this.mMol.getAtomPi$I(atom));
this.mCanBase[atom].add$I$J(4, this.mMol.getConnAtoms$I(atom) + this.mMol.getMetalBondedConnAtoms$I(atom));
if (Long.$ne((Long.$and(this.mMol.getAtomQueryFeatures$I(atom),1)),0 )) this.mCanBase[atom].add$I$J(4, 8);
 else this.mCanBase[atom].add$I$J(4, 8 + this.mMol.getAtomCharge$I(atom));
this.mCanBase[atom].add$I$J(5, Math.min(31, this.mMol.getAtomRingSize$I(atom)));
this.mCanBase[atom].add$I$J(4, p$1.canCalcImplicitAbnormalValence$I.apply(this, [atom]) + 1);
this.mCanBase[atom].add$I$J(2, this.mMol.getAtomRadical$I(atom) >> 4);
if (this.mMol.isFragment$()) {
this.mCanBase[atom].add$I$J(46, this.mMol.getAtomQueryFeatures$I(atom));
if (this.mMol.getAtomList$I(atom) != null ) atomListFound=true;
}}
this.mNoOfRanks=p$1.canPerformRanking.apply(this, []);
if (this.mNoOfRanks < this.mMol.getAtoms$()) {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
this.mCanBase[atom].init$I(atom);
this.mCanBase[atom].add$I$J(this.mAtomBits, this.mCanRank[atom]);
var bondRingSize=Clazz.array(Integer.TYPE, [this.mMol.getConnAtoms$I(atom)]);
for (var i=0; i < this.mMol.getConnAtoms$I(atom); i++) {
bondRingSize[i]=this.mCanRank[this.mMol.getConnAtom$I$I(atom, i)] << 5;
bondRingSize[i]|=Math.min(31, this.mMol.getBondRingSize$I(this.mMol.getConnBond$I$I(atom, i)));
}
$I$(1).sort$IA(bondRingSize);
for (var i=this.mMaxConnAtoms; i > bondRingSize.length; i--) this.mCanBase[atom].add$I$J(this.mAtomBits + 5, 0);

for (var i=bondRingSize.length - 1; i >= 0; i--) this.mCanBase[atom].add$I$J(this.mAtomBits + 5, bondRingSize[i]);

}
this.mNoOfRanks=p$1.canPerformRanking.apply(this, []);
}if (atomListFound && this.mNoOfRanks < this.mMol.getAtoms$() ) {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
this.mCanBase[atom].init$I(atom);
this.mCanBase[atom].add$I$J(this.mAtomBits, this.mCanRank[atom]);
var atomList=this.mMol.getAtomList$I(atom);
var listLength=(atomList == null ) ? 0 : Math.min(12, atomList.length);
for (var i=12; i > listLength; i--) this.mCanBase[atom].add$I$J(8, 0);

for (var i=listLength - 1; i >= 0; i--) this.mCanBase[atom].add$I$J(8, atomList[i]);

}
this.mNoOfRanks=p$1.canPerformRanking.apply(this, []);
}if (bondQueryFeaturesPresent && this.mNoOfRanks < this.mMol.getAtoms$() ) {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
this.mCanBase[atom].init$I(atom);
this.mCanBase[atom].add$I$J(this.mAtomBits, this.mCanRank[atom]);
var bondQFList=Clazz.array(Long.TYPE, [this.mMol.getConnAtoms$I(atom) + this.mMol.getMetalBondedConnAtoms$I(atom)]);
var index=0;
for (var i=0; i < this.mMol.getAllConnAtomsPlusMetalBonds$I(atom); i++) {
if (i < this.mMol.getConnAtoms$I(atom) || i >= this.mMol.getAllConnAtoms$I(atom) ) {
bondQFList[index]=this.mCanRank[this.mMol.getConnAtom$I$I(atom, i)];
(bondQFList[$k$=index]=Long.$sl(bondQFList[$k$],(23)));
(bondQFList[$k$=index]=Long.$or(bondQFList[$k$],(this.mMol.getBondQueryFeatures$I(this.mMol.getConnBond$I$I(atom, i)))));
++index;
}}
$I$(1).sort$JA(bondQFList);
for (var i=this.mMaxConnAtoms; i > bondQFList.length; i--) this.mCanBase[atom].add$I$J(this.mAtomBits + 23, 0);

for (var i=bondQFList.length - 1; i >= 0; i--) this.mCanBase[atom].add$I$J(this.mAtomBits + 23, bondQFList[i]);

}
this.mNoOfRanks=p$1.canPerformRanking.apply(this, []);
}if ((this.mMode & 8) != 0 && this.mNoOfRanks < this.mMol.getAtoms$() ) {
var list=Clazz.new_($I$(6,1));
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) if (this.mMol.getAtomCustomLabel$I(atom) != null ) list.addString$S(this.mMol.getAtomCustomLabel$I(atom));

for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
var rank=(this.mMol.getAtomCustomLabel$I(atom) == null ) ? 0 : 1 + list.getListIndex$S(this.mMol.getAtomCustomLabel$I(atom));
this.mCanBase[atom].init$I(atom);
this.mCanBase[atom].add$I$J(this.mAtomBits, this.mCanRank[atom]);
this.mCanBase[atom].add$I$J(this.mAtomBits, rank);
}
this.mNoOfRanks=p$1.canPerformRanking.apply(this, []);
}if ((this.mMode & 16) != 0 && this.mNoOfRanks < this.mMol.getAtoms$() ) {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
this.mCanBase[atom].init$I(atom);
this.mCanBase[atom].add$I$J(this.mAtomBits, this.mCanRank[atom]);
this.mCanBase[atom].add$I$J(1, this.mMol.isSelectedAtom$I(atom) ? 1 : 0);
}
this.mNoOfRanks=p$1.canPerformRanking.apply(this, []);
}if ((this.mMode & 512) != 0 && this.mMol.isFragment$() ) p$1.canBreakFreeValenceAtomTies.apply(this, []);
}, p$1);

Clazz.newMeth(C$, 'canBreakFreeValenceAtomTies',  function () {
while (true){
var isFreeValenceRank=Clazz.array(Boolean.TYPE, [this.mNoOfRanks + 1]);
var highestSharedFreeValenceRank=-1;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mMol.getLowestFreeValence$I(atom) != 0) {
if (isFreeValenceRank[this.mCanRank[atom]] && highestSharedFreeValenceRank < this.mCanRank[atom] ) highestSharedFreeValenceRank=this.mCanRank[atom];
isFreeValenceRank[this.mCanRank[atom]]=true;
}}
if (highestSharedFreeValenceRank == -1) break;
var increment=0;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
var value=0;
if (this.mCanRank[atom] == highestSharedFreeValenceRank) value=++increment;
this.mCanBase[atom].init$I(atom);
this.mCanBase[atom].add$I$J(this.mAtomBits, this.mCanRank[atom]);
this.mCanBase[atom].add$I$J(8, value);
}
this.mNoOfRanks=p$1.canPerformRanking.apply(this, []);
}
}, p$1);

Clazz.newMeth(C$, 'canRemoveOverspecifiedESRGroups',  function () {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) if (!this.mIsStereoCenter[atom] || this.mTHParity[atom] == 3 ) this.mTHESRType[atom]=(0|0);

for (var bond=0; bond < this.mMol.getBonds$(); bond++) if (this.mMol.getBondType$I(bond) != 1 || this.mEZParity[bond] == 0  || this.mEZParity[bond] == 3 ) this.mEZESRType[bond]=(0|0);

}, p$1);

Clazz.newMeth(C$, 'canRecursivelyFindAllParities',  function () {
this.mIsOddParityRound=true;
var paritiesFound=p$1.canFindParities$Z.apply(this, [false]);
var parityInfoBits=9;
while ((this.mNoOfRanks < this.mMol.getAtoms$()) && paritiesFound ){
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
this.mCanBase[atom].init$I(atom);
this.mCanBase[atom].add$I$J(this.mAtomBits, this.mCanRank[atom]);
var thParityInfo=this.mTHParity[atom] << (7);
if ((this.mTHParity[atom] == 1 || this.mTHParity[atom] == 2 ) && this.mTHESRType[atom] != 0 ) {
thParityInfo|=(this.mTHESRType[atom] << 5);
thParityInfo|=this.mTHESRGroup[atom];
}this.mCanBase[atom].add$I$J(18, thParityInfo << 9);
}
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
var ezParityInfo=this.mEZParity[bond] << (7);
if ((this.mEZParity[bond] == 1 || this.mEZParity[bond] == 2 ) && this.mMol.getBondType$I(bond) == 1  && this.mEZESRType[bond] != 0 ) {
ezParityInfo|=(this.mEZESRType[bond] << 5);
ezParityInfo|=this.mEZESRGroup[bond];
}this.mCanBase[this.mMol.getBondAtom$I$I(0, bond)].add$J(ezParityInfo);
this.mCanBase[this.mMol.getBondAtom$I$I(1, bond)].add$J(ezParityInfo);
}
var newNoOfRanks=p$1.canPerformRanking.apply(this, []);
if (this.mNoOfRanks == newNoOfRanks) break;
this.mNoOfRanks=newNoOfRanks;
paritiesFound=p$1.canFindParities$Z.apply(this, [false]);
}
}, p$1);

Clazz.newMeth(C$, 'canRecursivelyFindCIPParities',  function () {
this.mIsOddParityRound=true;
this.mTHCIPParity=Clazz.array(Byte.TYPE, [this.mMol.getAtoms$()]);
this.mEZCIPParity=Clazz.array(Byte.TYPE, [this.mMol.getBonds$()]);
var paritiesFound=p$1.canFindParities$Z.apply(this, [true]);
while ((this.mNoOfRanks < this.mMol.getAtoms$()) && paritiesFound ){
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
this.mCanBase[atom].init$I(atom);
this.mCanBase[atom].add$I$J(this.mAtomBits + 4, (this.mCanRank[atom] << 4) | (this.mTHParity[atom] << 2));
}
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
this.mCanBase[this.mMol.getBondAtom$I$I(0, bond)].add$J(this.mEZParity[bond]);
this.mCanBase[this.mMol.getBondAtom$I$I(1, bond)].add$J(this.mEZParity[bond]);
}
var newNoOfRanks=p$1.canPerformRanking.apply(this, []);
if (this.mNoOfRanks == newNoOfRanks) break;
this.mNoOfRanks=newNoOfRanks;
paritiesFound=p$1.canFindParities$Z.apply(this, [true]);
}
}, p$1);

Clazz.newMeth(C$, 'canRecursivelyFindCanonizedParities',  function () {
this.mIsOddParityRound=true;
var esrGroupMember=p$1.compileESRGroupMembers.apply(this, []);
if (this.mMesoHelper != null  && this.mMesoHelper.normalizeESRGroupSwappingAndRemoval$IA(this.mCanRank) ) esrGroupMember=p$1.compileESRGroupMembers.apply(this, []);
if (p$1.canFindParities$Z.apply(this, [false])) p$1.canNormalizeGroupParities.apply(this, []);
var newStereoInfoAvailable=true;
while ((this.mNoOfRanks < this.mMol.getAtoms$()) && newStereoInfoAvailable ){
var groupRank=p$1.canGetESRGroupRank$IAAA.apply(this, [esrGroupMember]);
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
this.mCanBase[atom].init$I(atom);
this.mCanBase[atom].add$I$J(this.mAtomBits, this.mCanRank[atom]);
this.mCanBase[atom].add$I$J(20, 0);
if (!this.mTHESRTypeNeedsNormalization[atom] && this.mTHESRType[atom] != 0 ) this.mCanBase[atom].add$J((this.mTHESRType[atom] << 18) + (groupRank[(this.mTHESRType[atom] == 1) ? 0 : 1][this.mTHESRGroup[atom]] << 8));
var parity=this.mTHParity[atom];
if (this.mTHParityIsMesoInverted[atom]) {
if (parity == 1) parity=2;
 else if (parity == 2) parity=1;
}this.mCanBase[atom].add$J(parity << 4);
}
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
this.mCanBase[this.mMol.getBondAtom$I$I(0, bond)].add$J(this.mEZParity[bond]);
this.mCanBase[this.mMol.getBondAtom$I$I(1, bond)].add$J(this.mEZParity[bond]);
}
var newNoOfRanks=p$1.canPerformRanking.apply(this, []);
if (this.mNoOfRanks == newNoOfRanks) break;
this.mNoOfRanks=newNoOfRanks;
newStereoInfoAvailable=false;
if (this.mMesoHelper != null  && this.mMesoHelper.normalizeESRGroupSwappingAndRemoval$IA(this.mCanRank) ) {
newStereoInfoAvailable=true;
esrGroupMember=p$1.compileESRGroupMembers.apply(this, []);
}if (p$1.canFindParities$Z.apply(this, [false])) {
newStereoInfoAvailable=true;
p$1.canNormalizeGroupParities.apply(this, []);
}}
}, p$1);

Clazz.newMeth(C$, 'compileESRGroupMembers',  function () {
var esrGroupMember=Clazz.array(Integer.TYPE, [2, 32, null]);
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mIsStereoCenter[atom]) {
if (this.mTHESRType[atom] == 1) esrGroupMember[0][this.mTHESRGroup[atom]]=$I$(2).addToIntArray$IA$I(esrGroupMember[0][this.mTHESRGroup[atom]], atom);
 else if (this.mTHESRType[atom] == 2) esrGroupMember[1][this.mTHESRGroup[atom]]=$I$(2).addToIntArray$IA$I(esrGroupMember[0][this.mTHESRGroup[atom]], atom);
}}
return esrGroupMember;
}, p$1);

Clazz.newMeth(C$, 'canNormalizeGroupParities',  function () {
var groupNormalized=false;
for (var i=0; i < this.mTHParityNormalizationGroupList.size$(); i++) {
var groupAtom=this.mTHParityNormalizationGroupList.get$I(i);
var allParitiesDetermined=true;
var maxRank=-1;
var invertParities=false;
for (var j=0; j < groupAtom.length; j++) {
var atom=groupAtom[j];
if (this.mTHParity[atom] == 0) {
allParitiesDetermined=false;
break;
}if (this.mTHParity[atom] != 3) {
var isUniqueRank=true;
for (var k=0; k < groupAtom.length; k++) {
if (k != j && this.mCanRank[atom] == this.mCanRank[groupAtom[k]] ) {
isUniqueRank=false;
break;
}}
if (isUniqueRank && maxRank < this.mCanRank[atom] ) {
maxRank=this.mCanRank[atom];
invertParities=(this.mTHParity[atom] == 1);
}}}
if (allParitiesDetermined && maxRank != -1 ) {
for (var atom, $atom = 0, $$atom = groupAtom; $atom<$$atom.length&&((atom=($$atom[$atom])),1);$atom++) {
if ((this.mTHParity[atom] == 1 || this.mTHParity[atom] == 2 )) this.mTHParityIsMesoInverted[atom]=invertParities;
this.mTHParityNeedsNormalization[atom]=false;
}
this.mTHParityNormalizationGroupList.remove$O(groupAtom);
groupNormalized=true;
--i;
}}
return groupNormalized;
}, p$1);

Clazz.newMeth(C$, 'canMarkESRGroupsForParityNormalization',  function () {
var count=0;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) if (this.mTHESRType[atom] != 0 && (this.mTHESRType[atom] != 2 || (this.mMode & 256) == 0 ) ) ++count;

if (count == 0) return;
var parity=Clazz.array(Integer.TYPE, [count]);
count=0;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mTHESRType[atom] != 0 && (this.mTHESRType[atom] != 2 || (this.mMode & 256) == 0 ) ) {
parity[count]=(this.mTHESRType[atom] << 29) | (this.mTHESRGroup[atom] << 24) | (this.mCanRank[atom] << 12) | atom ;
++count;
}}
$I$(1).sort$IA(parity);
var groupBase=0;
var nextGroupBase=0;
var groupID=parity[0] & -16777216;
while (true){
++nextGroupBase;
if (nextGroupBase == parity.length || groupID != (parity[nextGroupBase] & -16777216) ) {
var atomList=Clazz.array(Integer.TYPE, [nextGroupBase - groupBase]);
for (var i=groupBase; i < nextGroupBase; i++) {
var atom=parity[i] & 4095;
atomList[i - groupBase]=atom;
this.mTHParityNeedsNormalization[atom]=true;
}
this.mTHParityNormalizationGroupList.add$O(atomList);
if (nextGroupBase == parity.length) break;
groupID=(parity[nextGroupBase] & -16777216);
groupBase=nextGroupBase;
}}
}, p$1);

Clazz.newMeth(C$, 'canGetESRGroupRank$IAAA',  function (groupMember) {
var groupRank=Clazz.array(Integer.TYPE, [2, 32]);
for (var groupTypeIndex=0; groupTypeIndex < 2; groupTypeIndex++) {
var atomRank=Clazz.array(Integer.TYPE, [32, null]);
var rankCount=0;
for (var group=0; group < 32; group++) {
if (groupMember[groupTypeIndex][group] != null ) {
var memberCount=groupMember[groupTypeIndex][group].length;
atomRank[group]=Clazz.array(Integer.TYPE, [memberCount]);
for (var i=0; i < memberCount; i++) atomRank[group][i]=this.mCanRank[groupMember[groupTypeIndex][group][i]];

$I$(1).sort$IA(atomRank[group]);
++rankCount;
}}
for (var rank=rankCount; rank > 0; rank--) {
var maxGroup=0;
var maxAtomRank=null;
for (var group=0; group < 32; group++) {
if (atomRank[group] != null ) {
if (maxAtomRank == null  || maxAtomRank.length < atomRank[group].length ) {
maxAtomRank=atomRank[group];
maxGroup=group;
} else if (maxAtomRank.length == atomRank[group].length) {
for (var i=maxAtomRank.length - 1; i >= 0; i--) {
if (maxAtomRank[i] < atomRank[group][i]) {
maxAtomRank=atomRank[group];
maxGroup=group;
break;
}}
}}}
groupRank[groupTypeIndex][maxGroup]=rank;
atomRank[maxGroup]=null;
}
}
return groupRank;
}, p$1);

Clazz.newMeth(C$, 'canFindParities$Z',  function (doCIP) {
var ezFound=false;
for (var bond=0; bond < this.mMol.getBonds$(); bond++) if (p$1.canCalcEZParity$I$I.apply(this, [bond, 1])) {
this.mEZParityRoundIsOdd[bond]=this.mIsOddParityRound;
if (doCIP) p$1.cipCalcEZParity$I.apply(this, [bond]);
ezFound=true;
}
var thFound=false;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) if (p$1.canCalcTHParity$I$I.apply(this, [atom, 1])) {
this.mTHParityRoundIsOdd[atom]=this.mIsOddParityRound;
if (doCIP) p$1.cipCalcTHParity$I.apply(this, [atom]);
thFound=true;
}
if (thFound) this.mIsOddParityRound=!this.mIsOddParityRound;
return ezFound || thFound ;
}, p$1);

Clazz.newMeth(C$, 'determineChirality$IA',  function (canRankWithoutStereo) {
var stereoCenters=0;
var stereoCentersUnknown=0;
var stereoCentersTypeAbs=0;
var stereoCentersTypeAbsInMesoFragment=0;
var stereoCentersTypeAndGroup0=0;
var stereoCentersTypeOrGroup0=0;
var typeAndGroups=0;
var typeAndInMesoFragmentFound=false;
var andGroupUsed=Clazz.array(Boolean.TYPE, [32]);
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mTHParity[atom] != 0) {
++stereoCenters;
if (this.mTHParity[atom] == 3) {
++stereoCentersUnknown;
} else {
if (this.mTHESRType[atom] == 0) {
++stereoCentersTypeAbs;
if (this.mMesoHelper != null  && this.mMesoHelper.isInMesoFragment$I(atom) ) ++stereoCentersTypeAbsInMesoFragment;
} else if (this.mTHESRType[atom] == 2) {
if (this.mTHESRGroup[atom] == 0) ++stereoCentersTypeOrGroup0;
} else if (this.mTHESRType[atom] == 1) {
var group=this.mTHESRGroup[atom];
if (!andGroupUsed[group]) {
++typeAndGroups;
andGroupUsed[group]=true;
}if (this.mTHESRGroup[atom] == 0) ++stereoCentersTypeAndGroup0;
if (this.mMesoHelper != null  && this.mMesoHelper.isInMesoFragment$I(atom) ) typeAndInMesoFragmentFound=true;
}}}}
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mEZParity[bond] != 0 && this.mMol.getBondType$I(bond) == 1 ) {
++stereoCenters;
if (this.mEZParity[bond] == 3) {
++stereoCentersUnknown;
} else {
if (this.mEZESRType[bond] == 0) {
++stereoCentersTypeAbs;
if (this.mMesoHelper != null  && this.mMesoHelper.isInMesoFragment$I(this.mMol.getBondAtom$I$I(0, bond))  && this.mMesoHelper.isInMesoFragment$I(this.mMol.getBondAtom$I$I(1, bond)) ) ++stereoCentersTypeAbsInMesoFragment;
} else if (this.mEZESRType[bond] == 2) {
if (this.mEZESRGroup[bond] == 0) ++stereoCentersTypeOrGroup0;
} else if (this.mEZESRType[bond] == 1) {
var group=this.mEZESRGroup[bond];
if (!andGroupUsed[group]) {
++typeAndGroups;
andGroupUsed[group]=true;
}if (this.mEZESRGroup[bond] == 0) ++stereoCentersTypeAndGroup0;
if (this.mMesoHelper != null  && this.mMesoHelper.isInMesoFragment$I(this.mMol.getBondAtom$I$I(0, bond))  && this.mMesoHelper.isInMesoFragment$I(this.mMol.getBondAtom$I$I(1, bond)) ) typeAndInMesoFragmentFound=true;
}}}}
if (stereoCenters == 0) {
this.mMol.setChirality$I(65536);
return;
}if (stereoCentersUnknown != 0) {
this.mMol.setChirality$I(0);
return;
}if (this.mIsMeso) {
this.mMol.setChirality$I(131072 + (1 << typeAndGroups));
return;
}if (stereoCentersTypeAndGroup0 + stereoCentersTypeAbsInMesoFragment == stereoCenters && !typeAndInMesoFragmentFound ) {
this.mMol.setChirality$I(196608);
} else if (stereoCentersTypeAbs == stereoCenters) {
this.mMol.setChirality$I(262144);
} else if (stereoCentersTypeOrGroup0 == stereoCenters) {
this.mMol.setChirality$I(327680);
} else if (stereoCentersTypeAbs == stereoCenters - 1 && stereoCentersTypeAndGroup0 == 1 ) {
this.mMol.setChirality$I(393216);
} else {
this.mMol.setChirality$I(458752 + (1 << typeAndGroups));
}}, p$1);

Clazz.newMeth(C$, 'canFindPseudoParities',  function () {
var isFreshPseudoParityAtom=Clazz.array(Boolean.TYPE, [this.mMol.getAtoms$()]);
var isFreshPseudoParityBond=Clazz.array(Boolean.TYPE, [this.mMol.getBonds$()]);
var anyPseudoParityCount=0;
var pseudoParity1Or2Found=false;
if ((this.mMode & 128) != 0) {
this.mPseudoTHGroup=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
this.mPseudoEZGroup=Clazz.array(Integer.TYPE, [this.mMol.getBonds$()]);
}for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mProTHAtomsInSameFragment[atom]) {
if (!this.mTHParityIsPseudo[atom]) {
if (p$1.canCalcTHParity$I$I.apply(this, [atom, 1])) {
this.mTHParityIsPseudo[atom]=true;
isFreshPseudoParityAtom[atom]=true;
++anyPseudoParityCount;
}}}}
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mProEZAtomsInSameFragment[bond]) {
if (!this.mEZParityIsPseudo[bond]) {
if (p$1.canCalcEZParity$I$I.apply(this, [bond, 1])) {
this.mEZParityIsPseudo[bond]=true;
isFreshPseudoParityBond[bond]=true;
++anyPseudoParityCount;
}}}}
if (anyPseudoParityCount == 1) {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (isFreshPseudoParityAtom[atom]) {
this.mTHParity[atom]=(0|0);
break;
}}
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (isFreshPseudoParityBond[bond]) {
this.mEZParity[bond]=(0|0);
break;
}}
} else if (anyPseudoParityCount > 1) {
p$1.canEnsureFragments.apply(this, []);
this.mNoOfPseudoGroups=0;
for (var f, $f = this.mFragmentList.iterator$(); $f.hasNext$()&&((f=($f.next$())),1);) {
var pseudoParitiesInGroup=0;
var pseudoParity1Or2InGroup=0;
var highRankingTHAtom=0;
var highRankingEZBond=0;
var highTHAtomRank=-1;
var highEZBondRank=-1;
for (var i=0; i < f.atom.length; i++) {
if (isFreshPseudoParityAtom[f.atom[i]]) {
++pseudoParitiesInGroup;
if (this.mTHParity[f.atom[i]] == 1 || this.mTHParity[f.atom[i]] == 2 ) {
++pseudoParity1Or2InGroup;
pseudoParity1Or2Found=true;
if (highTHAtomRank < this.mCanRank[f.atom[i]]) {
highTHAtomRank=this.mCanRank[f.atom[i]];
highRankingTHAtom=f.atom[i];
}}}}
for (var i=0; i < f.bond.length; i++) {
if (isFreshPseudoParityBond[f.bond[i]]) {
++pseudoParitiesInGroup;
var rank1=this.mCanRank[this.mMol.getBondAtom$I$I(0, f.bond[i])];
var rank2=this.mCanRank[this.mMol.getBondAtom$I$I(1, f.bond[i])];
var higherRank=(rank1 > rank2) ? (rank1 << 16) + rank2 : (rank2 << 16) + rank1;
if (this.mEZParity[f.bond[i]] == 1 || this.mEZParity[f.bond[i]] == 2 ) {
++pseudoParity1Or2InGroup;
pseudoParity1Or2Found=true;
if (highEZBondRank < higherRank) {
highEZBondRank=higherRank;
highRankingEZBond=f.bond[i];
}}}}
if (pseudoParitiesInGroup == 0) continue;
if (pseudoParitiesInGroup == 1) {
for (var i=0; i < f.atom.length; i++) if (isFreshPseudoParityAtom[f.atom[i]]) this.mTHParity[f.atom[i]]=(0|0);

for (var i=0; i < f.bond.length; i++) if (isFreshPseudoParityBond[f.bond[i]]) this.mEZParity[f.bond[i]]=(0|0);

} else {
if (pseudoParity1Or2InGroup == 1) {
for (var i=0; i < f.atom.length; i++) if (isFreshPseudoParityAtom[f.atom[i]]) this.mTHParity[f.atom[i]]=(3|0);

for (var i=0; i < f.bond.length; i++) if (isFreshPseudoParityBond[f.bond[i]]) this.mEZParity[f.bond[i]]=(3|0);

} else {
if ((this.mMode & 128) != 0) {
++this.mNoOfPseudoGroups;
for (var i=0; i < f.atom.length; i++) if (isFreshPseudoParityAtom[f.atom[i]]) this.mPseudoTHGroup[f.atom[i]]=this.mNoOfPseudoGroups;

for (var i=0; i < f.bond.length; i++) if (isFreshPseudoParityBond[f.bond[i]]) this.mPseudoEZGroup[f.bond[i]]=this.mNoOfPseudoGroups;

}var invertFragmentsStereoFeatures=false;
if (highTHAtomRank != -1) {
if (this.mTHParity[highRankingTHAtom] == 2) invertFragmentsStereoFeatures=true;
} else {
if (this.mEZParity[highRankingEZBond] == 2) invertFragmentsStereoFeatures=true;
}if (invertFragmentsStereoFeatures) {
for (var i=0; i < f.atom.length; i++) {
if (isFreshPseudoParityAtom[f.atom[i]]) {
switch (this.mTHParity[f.atom[i]]) {
case 1:
this.mTHParity[f.atom[i]]=(2|0);
break;
case 2:
this.mTHParity[f.atom[i]]=(1|0);
break;
}
}}
for (var i=0; i < f.bond.length; i++) {
if (isFreshPseudoParityBond[f.bond[i]]) {
switch (this.mEZParity[f.bond[i]]) {
case 1:
this.mEZParity[f.bond[i]]=(2|0);
break;
case 2:
this.mEZParity[f.bond[i]]=(1|0);
break;
}
}}
}}}}
}return pseudoParity1Or2Found;
}, p$1);

Clazz.newMeth(C$, 'canEnsureFragments',  function () {
if (this.mFragmentList != null ) return;
this.mFragmentList=Clazz.new_($I$(3,1));
var fragmentCount=0;
var fragmentNo=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
var fragmentAtom=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
var fragmentBond=Clazz.array(Integer.TYPE, [this.mMol.getBonds$()]);
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (fragmentNo[atom] == 0 && (this.mMol.isRingAtom$I(atom) || this.mMol.getAtomPi$I(atom) == 1 ) ) {
fragmentAtom[0]=atom;
var fragmentAtoms=1;
var fragmentBonds=0;
fragmentNo[atom]=++fragmentCount;
var bondHandled=Clazz.array(Boolean.TYPE, [this.mMol.getBonds$()]);
for (var current=0; current < fragmentAtoms; current++) {
for (var i=0; i < this.mMol.getConnAtoms$I(fragmentAtom[current]); i++) {
var connBond=this.mMol.getConnBond$I$I(fragmentAtom[current], i);
if (this.mMol.isRingBond$I(connBond) || this.mMol.getBondOrder$I(connBond) == 2  || this.mMol.isBINAPChiralityBond$I(connBond) ) {
var connAtom=this.mMol.getConnAtom$I$I(fragmentAtom[current], i);
if (!bondHandled[connBond]) {
fragmentBond[fragmentBonds++]=connBond;
bondHandled[connBond]=true;
}if (fragmentNo[connAtom] == 0) {
fragmentAtom[fragmentAtoms++]=connAtom;
fragmentNo[connAtom]=fragmentCount;
}}}
}
this.mFragmentList.add$O(Clazz.new_($I$(7,1).c$$IA$I$IA$I,[fragmentAtom, fragmentAtoms, fragmentBond, fragmentBonds]));
}}
}, p$1);

Clazz.newMeth(C$, 'canPerformRanking',  function () {
var oldNoOfRanks;
var newNoOfRanks;
newNoOfRanks=p$1.canConsolidate.apply(this, []);
do {
oldNoOfRanks=newNoOfRanks;
p$1.canCalcNextBaseValues.apply(this, []);
newNoOfRanks=p$1.canConsolidate.apply(this, []);
} while (oldNoOfRanks != newNoOfRanks);
return newNoOfRanks;
}, p$1);

Clazz.newMeth(C$, 'canCalcNextBaseValues',  function () {
var connRank=Clazz.array(Integer.TYPE, [this.mMaxConnAtoms]);
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
var neighbours=this.mMol.getConnAtoms$I(atom) + this.mMol.getMetalBondedConnAtoms$I(atom);
var neighbour=0;
for (var i=0; i < this.mMol.getAllConnAtomsPlusMetalBonds$I(atom); i++) {
if (i < this.mMol.getConnAtoms$I(atom) || i >= this.mMol.getAllConnAtoms$I(atom) ) {
var rank=2 * this.mCanRank[this.mMol.getConnAtom$I$I(atom, i)];
var connBond=this.mMol.getConnBond$I$I(atom, i);
if (this.mMol.getBondOrder$I(connBond) == 2) if (!this.mMol.isAromaticBond$I(connBond)) ++rank;
var j;
for (j=0; j < neighbour; j++) if (rank < connRank[j]) break;

for (var k=neighbour; k > j; k--) connRank[k]=connRank[k - 1];

connRank[j]=rank;
++neighbour;
}}
this.mCanBase[atom].init$I(atom);
this.mCanBase[atom].add$I$J(this.mAtomBits, this.mCanRank[atom]);
for (var i=neighbours; i < this.mMaxConnAtoms; i++) this.mCanBase[atom].add$I$J(this.mAtomBits + 1, 0);

for (var i=0; i < neighbours; i++) this.mCanBase[atom].add$I$J(this.mAtomBits + 1, connRank[i]);

}
}, p$1);

Clazz.newMeth(C$, 'canConsolidate',  function () {
var canRank=0;
$I$(1).sort$OA(this.mCanBase);
for (var i=0; i < this.mCanBase.length; i++) {
if (i == 0 || this.mCanBase[i].compareTo$com_actelion_research_chem_CanonizerBaseValue(this.mCanBase[i - 1]) != 0 ) ++canRank;
this.mCanRank[this.mCanBase[i].getAtom$()]=canRank;
}
return canRank;
}, p$1);

Clazz.newMeth(C$, 'canCalcTHParity$I$I',  function (atom, mode) {
if (this.mTHParity[atom] != 0) return false;
if (this.mMol.getAtomicNo$I(atom) != 5 && this.mMol.getAtomicNo$I(atom) != 6  && this.mMol.getAtomicNo$I(atom) != 7  && this.mMol.getAtomicNo$I(atom) != 14  && this.mMol.getAtomicNo$I(atom) != 15  && this.mMol.getAtomicNo$I(atom) != 16 ) return false;
if (this.mMol.getAtomPi$I(atom) != 0) {
if (this.mMol.isCentralAlleneAtom$I(atom)) return p$1.canCalcAlleneParity$I$I.apply(this, [atom, mode]);
if (this.mMol.getAtomicNo$I(atom) != 15 && this.mMol.getAtomicNo$I(atom) != 16 ) return false;
}if (this.mMol.getConnAtoms$I(atom) < 3 || this.mMol.getAllConnAtoms$I(atom) > 4 ) return false;
if (this.mMol.getAtomCharge$I(atom) > 0 && this.mMol.getAtomicNo$I(atom) == 6 ) return false;
if (this.mMol.getAtomicNo$I(atom) == 5 && this.mMol.getAllConnAtoms$I(atom) != 4 ) return false;
if (this.mMol.getAtomicNo$I(atom) == 7 && !this.mNitrogenQualifiesForParity[atom] ) return false;
var remappedConn=Clazz.array(Integer.TYPE, [4]);
var remappedRank=Clazz.array(Integer.TYPE, [4]);
var neighbourUsed=Clazz.array(Boolean.TYPE, [4]);
for (var i=0; i < this.mMol.getAllConnAtoms$I(atom); i++) {
var highestRank=-1;
var highestConn=0;
for (var j=0; j < this.mMol.getAllConnAtoms$I(atom); j++) {
if (!neighbourUsed[j]) {
if (highestRank < this.mCanRank[this.mMol.getConnAtom$I$I(atom, j)]) {
highestRank=this.mCanRank[this.mMol.getConnAtom$I$I(atom, j)];
highestConn=j;
}}}
remappedConn[i]=highestConn;
remappedRank[i]=highestRank;
neighbourUsed[highestConn]=true;
}
if (this.mMol.getAllConnAtoms$I(atom) == 4 && remappedRank[0] == remappedRank[1]  && remappedRank[2] == remappedRank[3] ) return false;
if ((this.mMol.getAllConnAtoms$I(atom) == 4) && (remappedRank[0] == remappedRank[2] || remappedRank[1] == remappedRank[3] ) ) return false;
if (this.mMol.getAllConnAtoms$I(atom) == 3 && remappedRank[0] == remappedRank[2] ) return false;
var proTHAtom1=0;
var proTHAtom2=0;
var proTHAtomsFound=false;
for (var i=1; i < this.mMol.getAllConnAtoms$I(atom); i++) {
if (remappedRank[i - 1] == remappedRank[i]) {
if (mode == 1 || remappedRank[i] == 0 ) return false;
proTHAtom1=this.mMol.getConnAtom$I$I(atom, remappedConn[i - 1]);
proTHAtom2=this.mMol.getConnAtom$I$I(atom, remappedConn[i]);
if (mode == 3 && this.mMol.isRingBond$I(this.mMol.getConnBond$I$I(atom, remappedConn[i])) ) this.mProTHAtomsInSameFragment[atom]=true;
proTHAtomsFound=true;
}}
if (mode != 1 && !proTHAtomsFound ) return false;
var atomTHParity=(this.mZCoordinatesAvailable) ? p$1.canCalcTHParity3D$I$IA.apply(this, [atom, remappedConn]) : p$1.canCalcTHParity2D$I$IA.apply(this, [atom, remappedConn]);
if (mode == 1) {
this.mTHParity[atom]=atomTHParity;
} else if (mode == 2) {
if (atomTHParity == 1) {
this.mCanBase[proTHAtom1].add$J(this.mCanRank[atom]);
} else if (atomTHParity == 2) {
this.mCanBase[proTHAtom2].add$J(this.mCanRank[atom]);
}}return true;
}, p$1);

Clazz.newMeth(C$, 'canCalcTHParity2D$I$IA',  function (atom, remappedConn) {
var up_down=Clazz.array(Integer.TYPE, -2, [Clazz.array(Integer.TYPE, -1, [2, 1, 2, 1]), Clazz.array(Integer.TYPE, -1, [1, 2, 2, 1]), Clazz.array(Integer.TYPE, -1, [1, 1, 2, 2]), Clazz.array(Integer.TYPE, -1, [2, 1, 1, 2]), Clazz.array(Integer.TYPE, -1, [2, 2, 1, 1]), Clazz.array(Integer.TYPE, -1, [1, 2, 1, 2])]);
var angle=Clazz.array(Double.TYPE, [this.mMol.getAllConnAtoms$I(atom)]);
for (var i=0; i < this.mMol.getAllConnAtoms$I(atom); i++) angle[i]=this.mMol.getBondAngle$I$I(this.mMol.getConnAtom$I$I(atom, remappedConn[i]), atom);

var parity=($b$[0] = this.mMol.getFisherProjectionParity$I$IA$DA$IA(atom, remappedConn, angle, null), $b$[0]);
if (parity != 3) return parity;
var stereoBond=0;
var stereoType=0;
for (var i=0; i < this.mMol.getAllConnAtoms$I(atom); i++) {
var bnd=this.mMol.getConnBond$I$I(atom, remappedConn[i]);
if (this.mMol.getBondAtom$I$I(0, bnd) == atom) {
if (this.mMol.getBondType$I(bnd) == 129) {
if (stereoType != 0) this.mMol.setStereoProblem$I(atom);
stereoBond=i;
stereoType=1;
}if (this.mMol.getBondType$I(bnd) == 257) {
if (stereoType != 0) this.mMol.setStereoProblem$I(atom);
stereoBond=i;
stereoType=2;
}}}
if (stereoType == 0) return $b$[0] = 3, $b$[0];
for (var i=1; i < this.mMol.getAllConnAtoms$I(atom); i++) if (angle[i] < angle[0] ) angle[i]+=6.283185307179586;

if (this.mMol.getAllConnAtoms$I(atom) == 3) {
switch (stereoBond) {
case 0:
if (((angle[1] < angle[2] ) && (angle[2] - angle[1] < 3.141592653589793 ) ) || ((angle[1] > angle[2] ) && (angle[1] - angle[2] > 3.141592653589793 ) ) ) stereoType=3 - stereoType;
break;
case 1:
if (angle[2] - angle[0] > 3.141592653589793 ) stereoType=3 - stereoType;
break;
case 2:
if (angle[1] - angle[0] < 3.141592653589793 ) stereoType=3 - stereoType;
break;
}
return (stereoType == 1) ? 2 : ($b$[0] = 1, $b$[0]);
}var order=0;
if (angle[1] <= angle[2]  && angle[2] <= angle[3]  ) order=0;
 else if (angle[1] <= angle[3]  && angle[3] <= angle[2]  ) order=1;
 else if (angle[2] <= angle[1]  && angle[1] <= angle[3]  ) order=2;
 else if (angle[2] <= angle[3]  && angle[3] <= angle[1]  ) order=3;
 else if (angle[3] <= angle[1]  && angle[1] <= angle[2]  ) order=4;
 else if (angle[3] <= angle[2]  && angle[2] <= angle[1]  ) order=5;
return (up_down[order][stereoBond] == stereoType) ? 2 : ($b$[0] = 1, $b$[0]);
}, p$1);

Clazz.newMeth(C$, 'canCalcTHParity3D$I$IA',  function (atom, remappedConn) {
var atomList=Clazz.array(Integer.TYPE, [4]);
for (var i=0; i < this.mMol.getAllConnAtoms$I(atom); i++) atomList[i]=this.mMol.getConnAtom$I$I(atom, remappedConn[i]);

if (this.mMol.getAllConnAtoms$I(atom) == 3) atomList[3]=atom;
var coords=Clazz.array(Double.TYPE, [3, 3]);
for (var i=0; i < 3; i++) {
coords[i][0]=this.mMol.getAtomX$I(atomList[i + 1]) - this.mMol.getAtomX$I(atomList[0]);
coords[i][1]=this.mMol.getAtomY$I(atomList[i + 1]) - this.mMol.getAtomY$I(atomList[0]);
coords[i][2]=this.mMol.getAtomZ$I(atomList[i + 1]) - this.mMol.getAtomZ$I(atomList[0]);
}
var n=Clazz.array(Double.TYPE, [3]);
n[0]=coords[0][1] * coords[1][2] - coords[0][2] * coords[1][1];
n[1]=coords[0][2] * coords[1][0] - coords[0][0] * coords[1][2];
n[2]=coords[0][0] * coords[1][1] - coords[0][1] * coords[1][0];
var cosa=(coords[2][0] * n[0] + coords[2][1] * n[1] + coords[2][2] * n[2]) / (Math.sqrt(coords[2][0] * coords[2][0] + coords[2][1] * coords[2][1] + coords[2][2] * coords[2][2]) * Math.sqrt(n[0] * n[0] + n[1] * n[1] + n[2] * n[2]));
return (cosa > 0.0 ) ? 1 : ($b$[0] = 2, $b$[0]);
}, p$1);

Clazz.newMeth(C$, 'canCalcAlleneParity$I$I',  function (atom, mode) {
if (this.mMol.getAtomicNo$I(atom) != 6 && this.mMol.getAtomicNo$I(atom) != 7 ) return false;
var atom1=this.mMol.getConnAtom$I$I(atom, 0);
var atom2=this.mMol.getConnAtom$I$I(atom, 1);
if (this.mMol.getAtomPi$I(atom1) != 1 || this.mMol.getAtomPi$I(atom2) != 1 ) return false;
if (this.mMol.getConnAtoms$I(atom1) == 1 || this.mMol.getConnAtoms$I(atom2) == 1 ) return false;
if ((this.mMol.getAllConnAtoms$I(atom1) > 3) || (this.mMol.getAllConnAtoms$I(atom2) > 3) ) return false;
var halfParity1=Clazz.new_($I$(8,1).c$$com_actelion_research_chem_ExtendedMolecule$IA$I$I,[this.mMol, this.mCanRank, atom, atom1]);
if (halfParity1.mRanksEqual && mode == 1 ) return false;
var halfParity2=Clazz.new_($I$(8,1).c$$com_actelion_research_chem_ExtendedMolecule$IA$I$I,[this.mMol, this.mCanRank, atom, atom2]);
if (halfParity2.mRanksEqual && mode == 1 ) return false;
if (halfParity1.mRanksEqual && halfParity2.mRanksEqual ) return false;
if (mode == 3) {
if (halfParity1.mRanksEqual && halfParity1.mInSameFragment ) this.mProTHAtomsInSameFragment[atom]=true;
if (halfParity2.mRanksEqual && halfParity2.mInSameFragment ) this.mProTHAtomsInSameFragment[atom]=true;
}var alleneParity=this.mZCoordinatesAvailable ? p$1.canCalcAlleneParity3D$com_actelion_research_chem_EZHalfParity$com_actelion_research_chem_EZHalfParity.apply(this, [halfParity1, halfParity2]) : p$1.canCalcAlleneParity2D$com_actelion_research_chem_EZHalfParity$com_actelion_research_chem_EZHalfParity.apply(this, [halfParity1, halfParity2]);
if (mode == 1) {
this.mTHParity[atom]=alleneParity;
} else if (mode == 2) {
if (halfParity1.mRanksEqual) {
if (alleneParity == 1) {
this.mCanBase[halfParity1.mHighConn].add$J(this.mCanRank[atom1]);
} else {
this.mCanBase[halfParity1.mLowConn].add$J(this.mCanRank[atom1]);
}}if (halfParity2.mRanksEqual) {
if (alleneParity == 2) {
this.mCanBase[halfParity2.mHighConn].add$J(this.mCanRank[atom2]);
} else {
this.mCanBase[halfParity2.mLowConn].add$J(this.mCanRank[atom2]);
}}}return true;
}, p$1);

Clazz.newMeth(C$, 'canCalcAlleneParity2D$com_actelion_research_chem_EZHalfParity$com_actelion_research_chem_EZHalfParity',  function (halfParity1, halfParity2) {
var hp1=halfParity1.getValue$();
var hp2=halfParity2.getValue$();
if (hp1 == -1 || hp2 == -1  || ((hp1 + hp2) & 1) == 0 ) return $b$[0] = 3, $b$[0];
var alleneParity=($b$[0] = 0, $b$[0]);
switch (hp1 + hp2) {
case 3:
case 7:
alleneParity=($b$[0] = 2, $b$[0]);
break;
case 5:
alleneParity=($b$[0] = 1, $b$[0]);
break;
}
return alleneParity;
}, p$1);

Clazz.newMeth(C$, 'canCalcAlleneParity3D$com_actelion_research_chem_EZHalfParity$com_actelion_research_chem_EZHalfParity',  function (halfParity1, halfParity2) {
var atom=Clazz.array(Integer.TYPE, [4]);
atom[0]=halfParity1.mHighConn;
atom[1]=halfParity1.mCentralAxialAtom;
atom[2]=halfParity2.mCentralAxialAtom;
atom[3]=halfParity2.mHighConn;
var torsion=this.mMol.calculateTorsion$IA(atom);
if (Math.abs(torsion) < 0.3  || Math.abs(torsion) > 2.8415926535897933  ) return $b$[0] = 3, $b$[0];
if (torsion < 0 ) return $b$[0] = 2, $b$[0];
 else return $b$[0] = 1, $b$[0];
}, p$1);

Clazz.newMeth(C$, 'canCalcBINAPParity$I$I',  function (bond, mode) {
if (!this.mMol.isBINAPChiralityBond$I(bond)) return false;
var atom1=this.mMol.getBondAtom$I$I(0, bond);
var atom2=this.mMol.getBondAtom$I$I(1, bond);
var halfParity1=Clazz.new_($I$(8,1).c$$com_actelion_research_chem_ExtendedMolecule$IA$I$I,[this.mMol, this.mCanRank, atom1, atom2]);
if (halfParity1.mRanksEqual && mode == 1 ) return false;
var halfParity2=Clazz.new_($I$(8,1).c$$com_actelion_research_chem_ExtendedMolecule$IA$I$I,[this.mMol, this.mCanRank, atom2, atom1]);
if (halfParity2.mRanksEqual && mode == 1 ) return false;
if (halfParity1.mRanksEqual && halfParity2.mRanksEqual ) return false;
if (mode == 3) {
if (halfParity1.mRanksEqual) this.mProEZAtomsInSameFragment[bond]=p$1.hasSecondBINAPBond$I.apply(this, [atom2]);
if (halfParity2.mRanksEqual) this.mProEZAtomsInSameFragment[bond]=p$1.hasSecondBINAPBond$I.apply(this, [atom1]);
}var axialParity=this.mZCoordinatesAvailable ? p$1.canCalcBINAPParity3D$com_actelion_research_chem_EZHalfParity$com_actelion_research_chem_EZHalfParity.apply(this, [halfParity1, halfParity2]) : p$1.canCalcBINAPParity2D$com_actelion_research_chem_EZHalfParity$com_actelion_research_chem_EZHalfParity.apply(this, [halfParity1, halfParity2]);
if (mode == 1) {
this.mEZParity[bond]=axialParity;
} else if (mode == 2) {
if (halfParity1.mRanksEqual) {
if (axialParity == 2) {
this.mCanBase[halfParity1.mHighConn].add$J(this.mCanRank[atom2]);
} else {
this.mCanBase[halfParity1.mLowConn].add$J(this.mCanRank[atom2]);
}}if (halfParity2.mRanksEqual) {
if (axialParity == 2) {
this.mCanBase[halfParity2.mHighConn].add$J(this.mCanRank[atom1]);
} else {
this.mCanBase[halfParity2.mLowConn].add$J(this.mCanRank[atom1]);
}}}return true;
}, p$1);

Clazz.newMeth(C$, 'canCalcBINAPParity2D$com_actelion_research_chem_EZHalfParity$com_actelion_research_chem_EZHalfParity',  function (halfParity1, halfParity2) {
var hp1=halfParity1.getValue$();
var hp2=halfParity2.getValue$();
if (hp1 == -1 || hp2 == -1  || ((hp1 + hp2) & 1) == 0 ) return $b$[0] = 3, $b$[0];
var axialParity=($b$[0] = 0, $b$[0]);
switch (hp1 + hp2) {
case 3:
case 7:
axialParity=($b$[0] = 1, $b$[0]);
break;
case 5:
axialParity=($b$[0] = 2, $b$[0]);
break;
}
return axialParity;
}, p$1);

Clazz.newMeth(C$, 'canCalcBINAPParity3D$com_actelion_research_chem_EZHalfParity$com_actelion_research_chem_EZHalfParity',  function (halfParity1, halfParity2) {
var atom=Clazz.array(Integer.TYPE, [4]);
atom[0]=halfParity1.mHighConn;
atom[1]=halfParity1.mCentralAxialAtom;
atom[2]=halfParity2.mCentralAxialAtom;
atom[3]=halfParity2.mHighConn;
var torsion=this.mMol.calculateTorsion$IA(atom);
if (Math.abs(torsion) < 0.3  || Math.abs(torsion) > 2.8415926535897933  ) return $b$[0] = 3, $b$[0];
if (torsion < 0 ) return $b$[0] = 1, $b$[0];
 else return $b$[0] = 2, $b$[0];
}, p$1);

Clazz.newMeth(C$, 'hasSecondBINAPBond$I',  function (atom) {
var ringSet=this.mMol.getRingSet$();
for (var i=0; i < ringSet.getSize$(); i++) {
if (ringSet.isAromatic$I(i) && ringSet.isAtomMember$I$I(i, atom) ) {
for (var j, $j = 0, $$j = ringSet.getRingAtoms$I(i); $j<$$j.length&&((j=($$j[$j])),1);$j++) if (j != atom) for (var k=0; k < this.mMol.getConnAtoms$I(j); k++) if (this.mMol.isBINAPChiralityBond$I(this.mMol.getConnBond$I$I(j, k))) return true;


return false;
}}
return false;
}, p$1);

Clazz.newMeth(C$, 'canCalcEZParity$I$I',  function (bond, mode) {
if (this.mEZParity[bond] != 0) return false;
if (this.mMol.getBondOrder$I(bond) == 1) return p$1.canCalcBINAPParity$I$I.apply(this, [bond, mode]);
if (this.mMol.getBondOrder$I(bond) != 2) return false;
if (this.mMol.isAromaticBond$I(bond)) return false;
var dbAtom1=this.mMol.getBondAtom$I$I(0, bond);
var dbAtom2=this.mMol.getBondAtom$I$I(1, bond);
if (this.mMol.getConnAtoms$I(dbAtom1) == 1 || this.mMol.getConnAtoms$I(dbAtom2) == 1 ) return false;
if ((this.mMol.getConnAtoms$I(dbAtom1) > 3) || (this.mMol.getConnAtoms$I(dbAtom2) > 3) ) return false;
if (this.mMol.getAtomPi$I(dbAtom1) == 2 || this.mMol.getAtomPi$I(dbAtom2) == 2 ) return false;
var halfParity1=Clazz.new_($I$(8,1).c$$com_actelion_research_chem_ExtendedMolecule$IA$I$I,[this.mMol, this.mCanRank, dbAtom2, dbAtom1]);
if (halfParity1.mRanksEqual && mode == 1 ) return false;
var halfParity2=Clazz.new_($I$(8,1).c$$com_actelion_research_chem_ExtendedMolecule$IA$I$I,[this.mMol, this.mCanRank, dbAtom1, dbAtom2]);
if (halfParity2.mRanksEqual && mode == 1 ) return false;
if (halfParity1.mRanksEqual && halfParity2.mRanksEqual ) return false;
if (mode == 3) {
if (halfParity1.mRanksEqual && halfParity1.mInSameFragment ) this.mProEZAtomsInSameFragment[bond]=true;
if (halfParity2.mRanksEqual && halfParity2.mInSameFragment ) this.mProEZAtomsInSameFragment[bond]=true;
}var bondDBParity=this.mMol.isBondParityUnknownOrNone$I(bond) ? ($b$[0] = 3, $b$[0]) : (this.mZCoordinatesAvailable) ? p$1.canCalcEZParity3D$com_actelion_research_chem_EZHalfParity$com_actelion_research_chem_EZHalfParity.apply(this, [halfParity1, halfParity2]) : p$1.canCalcEZParity2D$com_actelion_research_chem_EZHalfParity$com_actelion_research_chem_EZHalfParity.apply(this, [halfParity1, halfParity2]);
if (mode == 1) {
this.mEZParity[bond]=bondDBParity;
} else if (mode == 2) {
if (halfParity1.mRanksEqual) {
if (bondDBParity == 1) {
this.mCanBase[halfParity1.mHighConn].add$J(this.mCanRank[dbAtom1]);
} else if (bondDBParity == 2) {
this.mCanBase[halfParity1.mLowConn].add$J(this.mCanRank[dbAtom1]);
}}if (halfParity2.mRanksEqual) {
if (bondDBParity == 1) {
this.mCanBase[halfParity2.mHighConn].add$J(this.mCanRank[dbAtom2]);
} else if (bondDBParity == 2) {
this.mCanBase[halfParity2.mLowConn].add$J(this.mCanRank[dbAtom2]);
}}}return true;
}, p$1);

Clazz.newMeth(C$, 'canCalcEZParity2D$com_actelion_research_chem_EZHalfParity$com_actelion_research_chem_EZHalfParity',  function (halfParity1, halfParity2) {
if (halfParity1.getValue$() == -1 || halfParity2.getValue$() == -1 ) return $b$[0] = 3, $b$[0];
if (((halfParity1.getValue$() | halfParity2.getValue$()) & 1) != 0) return $b$[0] = 3, $b$[0];
return (halfParity1.getValue$() == halfParity2.getValue$()) ? 1 : ($b$[0] = 2, $b$[0]);
}, p$1);

Clazz.newMeth(C$, 'canCalcEZParity3D$com_actelion_research_chem_EZHalfParity$com_actelion_research_chem_EZHalfParity',  function (halfParity1, halfParity2) {
var db=Clazz.array(Double.TYPE, [3]);
db[0]=this.mMol.getAtomX$I(halfParity2.mCentralAxialAtom) - this.mMol.getAtomX$I(halfParity1.mCentralAxialAtom);
db[1]=this.mMol.getAtomY$I(halfParity2.mCentralAxialAtom) - this.mMol.getAtomY$I(halfParity1.mCentralAxialAtom);
db[2]=this.mMol.getAtomZ$I(halfParity2.mCentralAxialAtom) - this.mMol.getAtomZ$I(halfParity1.mCentralAxialAtom);
var s1=Clazz.array(Double.TYPE, [3]);
s1[0]=this.mMol.getAtomX$I(halfParity1.mHighConn) - this.mMol.getAtomX$I(halfParity1.mCentralAxialAtom);
s1[1]=this.mMol.getAtomY$I(halfParity1.mHighConn) - this.mMol.getAtomY$I(halfParity1.mCentralAxialAtom);
s1[2]=this.mMol.getAtomZ$I(halfParity1.mHighConn) - this.mMol.getAtomZ$I(halfParity1.mCentralAxialAtom);
var s2=Clazz.array(Double.TYPE, [3]);
s2[0]=this.mMol.getAtomX$I(halfParity2.mHighConn) - this.mMol.getAtomX$I(halfParity2.mCentralAxialAtom);
s2[1]=this.mMol.getAtomY$I(halfParity2.mHighConn) - this.mMol.getAtomY$I(halfParity2.mCentralAxialAtom);
s2[2]=this.mMol.getAtomZ$I(halfParity2.mHighConn) - this.mMol.getAtomZ$I(halfParity2.mCentralAxialAtom);
var n1=Clazz.array(Double.TYPE, [3]);
n1[0]=db[1] * s1[2] - db[2] * s1[1];
n1[1]=db[2] * s1[0] - db[0] * s1[2];
n1[2]=db[0] * s1[1] - db[1] * s1[0];
var n2=Clazz.array(Double.TYPE, [3]);
n2[0]=db[1] * n1[2] - db[2] * n1[1];
n2[1]=db[2] * n1[0] - db[0] * n1[2];
n2[2]=db[0] * n1[1] - db[1] * n1[0];
var cosa=(s1[0] * n2[0] + s1[1] * n2[1] + s1[2] * n2[2]) / (Math.sqrt(s1[0] * s1[0] + s1[1] * s1[1] + s1[2] * s1[2]) * Math.sqrt(n2[0] * n2[0] + n2[1] * n2[1] + n2[2] * n2[2]));
var cosb=(s2[0] * n2[0] + s2[1] * n2[1] + s2[2] * n2[2]) / (Math.sqrt(s2[0] * s2[0] + s2[1] * s2[1] + s2[2] * s2[2]) * Math.sqrt(n2[0] * n2[0] + n2[1] * n2[1] + n2[2] * n2[2]));
return (!!((cosa < 0.0 ) ^ (cosb < 0.0 ))) ? 1 : ($b$[0] = 2, $b$[0]);
}, p$1);

Clazz.newMeth(C$, 'flagStereoProblems',  function () {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mTHParity[atom] == 3 && !this.mMol.isAtomConfigurationUnknown$I(atom) ) this.mMol.setStereoProblem$I(atom);
if ((this.mMol.getAtomESRType$I(atom) == 1 || this.mMol.getAtomESRType$I(atom) == 2 ) && (this.mTHParity[atom] == 3) ) this.mMol.setStereoProblem$I(atom);
if (this.mMol.isAtomConfigurationUnknown$I(atom) && this.mTHParity[atom] != 3  && !p$1.isUnknownBINAPBondAtom$I.apply(this, [atom]) ) this.mMol.setStereoProblem$I(atom);
}
for (var bond=0; bond < this.mMol.getAllBonds$(); bond++) if (this.mMol.isStereoBond$I(bond) && !p$1.isJustifiedStereoBond$I.apply(this, [bond]) ) this.mMol.setStereoProblem$I(this.mMol.getBondAtom$I$I(0, bond));

for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mMol.getBondOrder$I(bond) == 2) {
if (this.mMol.isBondParityUnknownOrNone$I(bond) && (this.mEZParity[bond] == 1 || this.mEZParity[bond] == 2 ) ) {
this.mEZParity[bond]=(3|0);
this.mMol.setBondType$I$I(bond, 386);
}if (this.mEZParity[bond] == 3 && !this.mEZParityIsPseudo[bond] ) {
if (this.mMol.getBondType$I(bond) != 386) {
this.mMol.setStereoProblem$I(this.mMol.getBondAtom$I$I(0, bond));
this.mMol.setStereoProblem$I(this.mMol.getBondAtom$I$I(1, bond));
}}}if (this.mMol.getBondType$I(bond) == 1 && this.mEZParity[bond] == 3  && !this.mMol.isAtomConfigurationUnknown$I(this.mMol.getBondAtom$I$I(0, bond))  && !this.mMol.isAtomConfigurationUnknown$I(this.mMol.getBondAtom$I$I(1, bond)) ) {
this.mMol.setStereoProblem$I(this.mMol.getBondAtom$I$I(0, bond));
this.mMol.setStereoProblem$I(this.mMol.getBondAtom$I$I(1, bond));
}if ((this.mMol.getBondESRType$I(bond) == 1 || this.mMol.getBondESRType$I(bond) == 2 ) && (this.mMol.getBondType$I(bond) != 1 || (this.mEZParity[bond] != 1 && this.mEZParity[bond] != 2 ) ) ) {
this.mMol.setStereoProblem$I(this.mMol.getBondAtom$I$I(0, bond));
this.mMol.setStereoProblem$I(this.mMol.getBondAtom$I$I(1, bond));
}}
}, p$1);

Clazz.newMeth(C$, 'isUnknownBINAPBondAtom$I',  function (atom) {
for (var i=0; i < this.mMol.getConnAtoms$I(atom); i++) if (this.mEZParity[this.mMol.getConnBond$I$I(atom, i)] == 3 && this.mMol.getConnBondOrder$I$I(atom, i) == 1 ) return true;

return false;
}, p$1);

Clazz.newMeth(C$, 'isJustifiedStereoBond$I',  function (bond) {
var atom=this.mMol.getBondAtom$I$I(0, bond);
if (atom >= this.mMol.getAtoms$()) return false;
if (this.mTHParity[atom] == 1 || this.mTHParity[atom] == 2 ) return true;
if (this.mTHParity[atom] == 3) return false;
var binapBond=this.mMol.findBINAPChiralityBond$I(atom);
if (binapBond != -1) return this.mEZParity[binapBond] == 1 || this.mEZParity[binapBond] == 2 ;
for (var i=0; i < this.mMol.getConnAtoms$I(atom); i++) {
if (this.mMol.getConnBondOrder$I$I(atom, i) == 2) {
if (this.mTHParity[this.mMol.getConnAtom$I$I(atom, i)] == 1 || this.mTHParity[this.mMol.getConnAtom$I$I(atom, i)] == 2 ) return true;
}}
return false;
}, p$1);

Clazz.newMeth(C$, 'generateGraph',  function () {
if (this.mMol.getAtoms$() == 0) return;
if (this.mGraphGenerated) return;
this.mGraphRings=0;
var startAtom=0;
for (var atom=1; atom < this.mMol.getAtoms$(); atom++) if (this.mCanRank[atom] > this.mCanRank[startAtom]) startAtom=atom;

var atomHandled=Clazz.array(Boolean.TYPE, [this.mMol.getAtoms$()]);
var bondHandled=Clazz.array(Boolean.TYPE, [this.mMol.getBonds$()]);
this.mGraphIndex=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
this.mGraphAtom=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
this.mGraphFrom=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
this.mGraphBond=Clazz.array(Integer.TYPE, [this.mMol.getBonds$()]);
this.mGraphAtom[0]=startAtom;
this.mGraphIndex[startAtom]=0;
atomHandled[startAtom]=true;
var atomsWithoutParents=1;
var firstUnhandled=0;
var firstUnused=1;
var graphBonds=0;
while (firstUnhandled < this.mMol.getAtoms$()){
if (firstUnhandled < firstUnused) {
while (true){
var highestRankingConnAtom=0;
var highestRankingConnBond=0;
var highestRank=-1;
var atom=this.mGraphAtom[firstUnhandled];
for (var i=0; i < this.mMol.getAllConnAtomsPlusMetalBonds$I(atom); i++) {
if (i < this.mMol.getConnAtoms$I(atom) || i >= this.mMol.getAllConnAtoms$I(atom) ) {
var connAtom=this.mMol.getConnAtom$I$I(atom, i);
if (!atomHandled[connAtom] && this.mCanRank[connAtom] > highestRank ) {
highestRankingConnAtom=connAtom;
highestRankingConnBond=this.mMol.getConnBond$I$I(atom, i);
highestRank=this.mCanRank[connAtom];
}}}
if (highestRank == -1) break;
this.mGraphIndex[highestRankingConnAtom]=firstUnused;
this.mGraphFrom[firstUnused]=firstUnhandled;
this.mGraphAtom[firstUnused++]=highestRankingConnAtom;
this.mGraphBond[graphBonds++]=highestRankingConnBond;
atomHandled[highestRankingConnAtom]=true;
bondHandled[highestRankingConnBond]=true;
}
++firstUnhandled;
} else {
var highestRankingAtom=0;
var highestRank=-1;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (!atomHandled[atom] && this.mCanRank[atom] > highestRank ) {
highestRankingAtom=atom;
highestRank=this.mCanRank[atom];
}}
++atomsWithoutParents;
this.mGraphIndex[highestRankingAtom]=firstUnused;
this.mGraphFrom[firstUnused]=-1;
this.mGraphAtom[firstUnused++]=highestRankingAtom;
atomHandled[highestRankingAtom]=true;
}}
this.mGraphClosure=Clazz.array(Integer.TYPE, [2 * (this.mMol.getBonds$() - graphBonds)]);
while (true){
var lowAtomNo1=this.mMol.getMaxAtoms$();
var lowAtomNo2=this.mMol.getMaxAtoms$();
var lowBond=-1;
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
var loAtom;
var hiAtom;
if (!bondHandled[bond]) {
if (this.mGraphIndex[this.mMol.getBondAtom$I$I(0, bond)] < this.mGraphIndex[this.mMol.getBondAtom$I$I(1, bond)]) {
loAtom=this.mGraphIndex[this.mMol.getBondAtom$I$I(0, bond)];
hiAtom=this.mGraphIndex[this.mMol.getBondAtom$I$I(1, bond)];
} else {
loAtom=this.mGraphIndex[this.mMol.getBondAtom$I$I(1, bond)];
hiAtom=this.mGraphIndex[this.mMol.getBondAtom$I$I(0, bond)];
}if (loAtom < lowAtomNo1 || (loAtom == lowAtomNo1 && hiAtom < lowAtomNo2 ) ) {
lowAtomNo1=loAtom;
lowAtomNo2=hiAtom;
lowBond=bond;
}}}
if (lowBond == -1) break;
bondHandled[lowBond]=true;
this.mGraphBond[graphBonds++]=lowBond;
this.mGraphClosure[2 * this.mGraphRings]=lowAtomNo1;
this.mGraphClosure[2 * this.mGraphRings + 1]=lowAtomNo2;
++this.mGraphRings;
}
this.mGraphGenerated=true;
}, p$1);

Clazz.newMeth(C$, 'getCanMolecule$',  function () {
return this.getCanMolecule$Z(false);
});

Clazz.newMeth(C$, 'getCanMolecule$Z',  function (includeExplicitHydrogen) {
p$1.generateGraph.apply(this, []);
var mol=Clazz.new_([this.mMol.getAtoms$(), this.mMol.getBonds$()],$I$(9,1).c$$I$I);
mol.setFragment$Z(this.mMol.isFragment$());
for (var i=0; i < this.mMol.getAtoms$(); i++) {
this.mMol.copyAtom$com_actelion_research_chem_Molecule$I$I$I(mol, this.mGraphAtom[i], 0, 0);
mol.setAtomESR$I$I$I(i, this.mTHESRType[this.mGraphAtom[i]], this.mTHESRGroup[this.mGraphAtom[i]]);
}
for (var i=0; i < this.mMol.getBonds$(); i++) {
this.mMol.copyBond$com_actelion_research_chem_Molecule$I$I$I$IA$Z(mol, this.mGraphBond[i], 0, 0, this.mGraphIndex, false);
if (!mol.isStereoBond$I(i) && mol.getBondAtom$I$I(0, i) > mol.getBondAtom$I$I(1, i) ) {
var temp=mol.getBondAtom$I$I(0, i);
mol.setBondAtom$I$I$I(0, i, mol.getBondAtom$I$I(1, i));
mol.setBondAtom$I$I$I(1, i, temp);
}mol.setBondESR$I$I$I(i, this.mEZESRType[this.mGraphBond[i]], this.mEZESRGroup[this.mGraphBond[i]]);
}
if (includeExplicitHydrogen) {
for (var i=0; i < this.mMol.getAtoms$(); i++) {
var atom=this.mGraphAtom[i];
for (var j=this.mMol.getConnAtoms$I(atom); j < this.mMol.getAllConnAtoms$I(atom); j++) {
var hydrogen=this.mMol.copyAtom$com_actelion_research_chem_Molecule$I$I$I(mol, this.mMol.getConnAtom$I$I(atom, j), 0, 0);
this.mMol.copyBond$com_actelion_research_chem_Molecule$I$I$I$I$I$Z(mol, this.mMol.getConnBond$I$I(atom, j), 0, 0, this.mGraphIndex[atom], hydrogen, false);
}
}
}for (var bond=0; bond < mol.getAllBonds$(); bond++) {
var atom=mol.getBondAtom$I$I(0, bond);
if (this.mTHParityIsMesoInverted[this.mGraphAtom[atom]]) {
if (mol.getBondType$I(bond) == 257) mol.setBondType$I$I(bond, 129);
 else if (mol.getBondType$I(bond) == 129) mol.setBondType$I$I(bond, 257);
}}
this.mMol.copyMoleculeProperties$com_actelion_research_chem_Molecule(mol);
this.mMol.invalidateHelperArrays$I(8);
return mol;
});

Clazz.newMeth(C$, 'setUnknownParitiesToExplicitlyUnknown$',  function () {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) if (!this.mMol.isAtomConfigurationUnknown$I(atom) && this.mTHParity[atom] == 3 ) this.mMol.setAtomConfigurationUnknown$I$Z(atom, true);

for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mEZParity[bond] == 3) {
var order=this.mMol.getBondOrder$I(bond);
if (order == 1) {
this.mMol.setAtomConfigurationUnknown$I$Z(this.mMol.getBondAtom$I$I(0, bond), true);
} else if (order == 2) {
this.mMol.setBondType$I$I(bond, 386);
}}}
});

Clazz.newMeth(C$, 'setSingleUnknownAsRacemicParity$',  function () {
var unknownTHParities=0;
var knownTHParities=0;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mTHParity[atom] != 0 && !this.mTHParityIsPseudo[atom] ) {
if (this.mTHParity[atom] == 3) ++unknownTHParities;
 else ++knownTHParities;
}}
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mMol.getBondType$I(bond) == 1 && this.mEZParity[bond] != 0  && !this.mEZParityIsPseudo[bond] ) {
if (this.mEZParity[bond] == 3) ++unknownTHParities;
 else ++knownTHParities;
}}
if (knownTHParities == 0 && unknownTHParities == 1 ) {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mTHParity[atom] == 3 && !this.mTHParityIsPseudo[atom] ) {
if (this.mMol.getAtomPi$I(atom) == 2 && this.mMol.getConnAtoms$I(atom) == 2 ) {
for (var i=0; i < 2; i++) {
var connAtom=this.mMol.getConnAtom$I$I(atom, i);
for (var j=0; j < this.mMol.getConnAtoms$I(connAtom); j++) if (this.mMol.isStereoBond$I(this.mMol.getConnBond$I$I(connAtom, j))) return false;

}
} else {
for (var i=0; i < this.mMol.getConnAtoms$I(atom); i++) if (this.mMol.isStereoBond$I(this.mMol.getConnBond$I$I(atom, i))) return false;

}this.mTHParity[atom]=(2|0);
this.mTHESRType[atom]=(1|0);
this.mTHESRGroup[atom]=(0|0);
this.mMol.setAtomParity$I$I$Z(atom, 2, false);
this.mMol.setAtomESR$I$I$I(atom, 1, 0);
var stereoBond=this.mMol.getAtomPreferredStereoBond$I(atom);
this.mMol.setBondType$I$I(stereoBond, 257);
if (this.mMol.getBondAtom$I$I(1, stereoBond) == atom) {
var connAtom=this.mMol.getBondAtom$I$I(0, stereoBond);
this.mMol.setBondAtom$I$I$I(0, stereoBond, atom);
this.mMol.setBondAtom$I$I$I(1, stereoBond, connAtom);
}return true;
}}
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mMol.getBondType$I(bond) == 1 && this.mEZParity[bond] != 0  && !this.mEZParityIsPseudo[bond] ) {
for (var i=0; i < 2; i++) {
var atom=this.mMol.getBondAtom$I$I(i, bond);
for (var j=0; j < this.mMol.getConnAtoms$I(atom); j++) if (this.mMol.isStereoBond$I(this.mMol.getConnBond$I$I(atom, j))) return false;

}
this.mEZParity[bond]=(2|0);
this.mEZESRType[bond]=(1|0);
this.mEZESRGroup[bond]=(0|0);
this.mMol.setBondParity$I$I$Z(bond, 2, false);
this.mMol.setAtomESR$I$I$I(bond, 1, 0);
var stereoBond=this.mMol.getBondPreferredStereoBond$I(bond);
this.mMol.setBondType$I$I(stereoBond, 257);
if (this.mMol.getBondAtom$I$I(1, stereoBond) == this.mMol.getBondAtom$I$I(0, bond) || this.mMol.getBondAtom$I$I(1, stereoBond) == this.mMol.getBondAtom$I$I(1, bond) ) {
var connAtom=this.mMol.getBondAtom$I$I(0, stereoBond);
this.mMol.setBondAtom$I$I$I(0, stereoBond, this.mMol.getBondAtom$I$I(1, stereoBond));
this.mMol.setBondAtom$I$I$I(1, stereoBond, connAtom);
}return true;
}}
}return false;
});

Clazz.newMeth(C$, 'getIDCode$',  function () {
if (this.mIDCode == null ) {
p$1.generateGraph.apply(this, []);
if ((this.mMode & 2048) == 0) {
p$1.idGenerateConfigurations.apply(this, []);
p$1.idNormalizeESRGroupNumbers.apply(this, []);
}p$1.idCodeCreate.apply(this, []);
}return this.mIDCode;
});

Clazz.newMeth(C$, 'getFinalRank$',  function () {
return this.mCanRank;
});

Clazz.newMeth(C$, 'getSymmetryRank$I',  function (atom) {
return (this.mCanRankBeforeTieBreaking == null ) ? -1 : this.mCanRankBeforeTieBreaking[atom];
});

Clazz.newMeth(C$, 'getSymmetryRanks$',  function () {
return this.mCanRankBeforeTieBreaking;
});

Clazz.newMeth(C$, 'idCodeCreate',  function () {
p$1.encodeBitsStart$Z.apply(this, [false]);
p$1.encodeBits$J$I.apply(this, [9, 4]);
var nbits=Math.max(C$.getNeededBits$I(this.mMol.getAtoms$()), C$.getNeededBits$I(this.mMol.getBonds$()));
p$1.encodeBits$J$I.apply(this, [nbits, 4]);
if (nbits == 0) {
p$1.encodeBits$J$I.apply(this, [this.mMol.isFragment$() ? 1 : 0, 1]);
p$1.encodeBits$J$I.apply(this, [0, 1]);
this.mIDCode=p$1.encodeBitsEnd.apply(this, []);
return;
}var nitrogens;
var oxygens;
var otherAtoms;
var chargedAtoms;
nitrogens=oxygens=otherAtoms=chargedAtoms=0;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (Long.$eq((Long.$and(this.mMol.getAtomQueryFeatures$I(atom),1)),0 )) {
switch (this.mMol.getAtomicNo$I(atom)) {
case 6:
break;
case 7:
++nitrogens;
break;
case 8:
++oxygens;
break;
default:
++otherAtoms;
break;
}
if (this.mMol.getAtomCharge$I(atom) != 0) ++chargedAtoms;
}}
p$1.encodeBits$J$I.apply(this, [this.mMol.getAtoms$(), nbits]);
p$1.encodeBits$J$I.apply(this, [this.mMol.getBonds$(), nbits]);
p$1.encodeBits$J$I.apply(this, [nitrogens, nbits]);
p$1.encodeBits$J$I.apply(this, [oxygens, nbits]);
p$1.encodeBits$J$I.apply(this, [otherAtoms, nbits]);
p$1.encodeBits$J$I.apply(this, [chargedAtoms, nbits]);
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) if (this.mMol.getAtomicNo$I(this.mGraphAtom[atom]) == 7 && Long.$eq((Long.$and(this.mMol.getAtomQueryFeatures$I(this.mGraphAtom[atom]),1)),0 ) ) p$1.encodeBits$J$I.apply(this, [atom, nbits]);

for (var atom=0; atom < this.mMol.getAtoms$(); atom++) if (this.mMol.getAtomicNo$I(this.mGraphAtom[atom]) == 8 && Long.$eq((Long.$and(this.mMol.getAtomQueryFeatures$I(this.mGraphAtom[atom]),1)),0 ) ) p$1.encodeBits$J$I.apply(this, [atom, nbits]);

for (var atom=0; atom < this.mMol.getAtoms$(); atom++) if (this.mMol.getAtomicNo$I(this.mGraphAtom[atom]) != 6 && this.mMol.getAtomicNo$I(this.mGraphAtom[atom]) != 7  && this.mMol.getAtomicNo$I(this.mGraphAtom[atom]) != 8  && Long.$eq((Long.$and(this.mMol.getAtomQueryFeatures$I(this.mGraphAtom[atom]),1)),0 ) ) {
p$1.encodeBits$J$I.apply(this, [atom, nbits]);
p$1.encodeBits$J$I.apply(this, [this.mMol.getAtomicNo$I(this.mGraphAtom[atom]), 8]);
}
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) if (this.mMol.getAtomCharge$I(this.mGraphAtom[atom]) != 0 && Long.$eq((Long.$and(this.mMol.getAtomQueryFeatures$I(this.mGraphAtom[atom]),1)),0 ) ) {
p$1.encodeBits$J$I.apply(this, [atom, nbits]);
p$1.encodeBits$J$I.apply(this, [8 + this.mMol.getAtomCharge$I(this.mGraphAtom[atom]), 4]);
}
var maxdif=0;
var base=0;
for (var atom=1; atom < this.mMol.getAtoms$(); atom++) {
var dif;
if (this.mGraphFrom[atom] == -1) {
dif=0;
} else {
dif=1 + this.mGraphFrom[atom] - base;
base=this.mGraphFrom[atom];
}if (maxdif < dif) maxdif=dif;
}
var dbits=C$.getNeededBits$I(maxdif);
p$1.encodeBits$J$I.apply(this, [dbits, 4]);
base=0;
for (var atom=1; atom < this.mMol.getAtoms$(); atom++) {
var dif;
if (this.mGraphFrom[atom] == -1) {
dif=0;
} else {
dif=1 + this.mGraphFrom[atom] - base;
base=this.mGraphFrom[atom];
}p$1.encodeBits$J$I.apply(this, [dif, dbits]);
}
for (var i=0; i < 2 * this.mGraphRings; i++) p$1.encodeBits$J$I.apply(this, [this.mGraphClosure[i], nbits]);

for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
var bondOrder=((this.mMol.getBondQueryFeatures$I(this.mGraphBond[bond]) & 130560) != 0 || this.mMol.getBondType$I(this.mGraphBond[bond]) == 32 ) ? 1 : (this.mMol.isDelocalizedBond$I(this.mGraphBond[bond])) ? 0 : Math.min(3, this.mMol.getBondOrder$I(this.mGraphBond[bond]));
p$1.encodeBits$J$I.apply(this, [bondOrder, 2]);
}
var THCount=0;
if ((this.mMode & 2048) == 0) {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) if (this.mTHConfiguration[this.mGraphAtom[atom]] != 0 && this.mTHConfiguration[this.mGraphAtom[atom]] != 3 ) ++THCount;

}p$1.encodeBits$J$I.apply(this, [THCount, nbits]);
if ((this.mMode & 2048) == 0) {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mTHConfiguration[this.mGraphAtom[atom]] != 0 && this.mTHConfiguration[this.mGraphAtom[atom]] != 3 ) {
p$1.encodeBits$J$I.apply(this, [atom, nbits]);
if (this.mTHESRType[this.mGraphAtom[atom]] == 0) {
p$1.encodeBits$J$I.apply(this, [this.mTHConfiguration[this.mGraphAtom[atom]], 3]);
} else {
var parity=(this.mTHConfiguration[this.mGraphAtom[atom]] == 1) ? ((this.mTHESRType[this.mGraphAtom[atom]] == 1) ? 4 : 6) : ((this.mTHESRType[this.mGraphAtom[atom]] == 1) ? 5 : 7);
p$1.encodeBits$J$I.apply(this, [parity, 3]);
p$1.encodeBits$J$I.apply(this, [this.mTHESRGroup[this.mGraphAtom[atom]], 3]);
}}}
}var EZCount=0;
if ((this.mMode & 2048) == 0) {
for (var bond=0; bond < this.mMol.getBonds$(); bond++) if (this.mEZConfiguration[this.mGraphBond[bond]] != 0 && this.mEZConfiguration[this.mGraphBond[bond]] != 3  && (!this.mMol.isSmallRingBond$I(this.mGraphBond[bond]) || this.mMol.getBondType$I(this.mGraphBond[bond]) == 1 ) ) ++EZCount;

}p$1.encodeBits$J$I.apply(this, [EZCount, nbits]);
if ((this.mMode & 2048) == 0) {
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mEZConfiguration[this.mGraphBond[bond]] != 0 && this.mEZConfiguration[this.mGraphBond[bond]] != 3  && (!this.mMol.isSmallRingBond$I(this.mGraphBond[bond]) || this.mMol.getBondType$I(this.mGraphBond[bond]) == 1 ) ) {
p$1.encodeBits$J$I.apply(this, [bond, nbits]);
if (this.mMol.getBondType$I(this.mGraphBond[bond]) == 1) {
if (this.mEZESRType[this.mGraphBond[bond]] == 0) {
p$1.encodeBits$J$I.apply(this, [this.mEZConfiguration[this.mGraphBond[bond]], 3]);
} else {
var parity=(this.mEZConfiguration[this.mGraphBond[bond]] == 1) ? ((this.mEZESRType[this.mGraphBond[bond]] == 1) ? 4 : 6) : ((this.mEZESRType[this.mGraphBond[bond]] == 1) ? 5 : 7);
p$1.encodeBits$J$I.apply(this, [parity, 3]);
p$1.encodeBits$J$I.apply(this, [this.mEZESRGroup[this.mGraphBond[bond]], 3]);
}} else {
p$1.encodeBits$J$I.apply(this, [this.mEZConfiguration[this.mGraphBond[bond]], 2]);
}}}
}p$1.encodeBits$J$I.apply(this, [this.mMol.isFragment$() ? 1 : 0, 1]);
var count=0;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) if (this.mMol.getAtomMass$I(this.mGraphAtom[atom]) != 0) ++count;

if (count != 0) {
p$1.encodeFeatureNo$I.apply(this, [1]);
p$1.encodeBits$J$I.apply(this, [count, nbits]);
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mMol.getAtomMass$I(this.mGraphAtom[atom]) != 0) {
p$1.encodeBits$J$I.apply(this, [atom, nbits]);
p$1.encodeBits$J$I.apply(this, [this.mMol.getAtomMass$I(this.mGraphAtom[atom]), 8]);
}}
}this.mFeatureBlock=0;
if (this.mMol.isFragment$()) {
p$1.addAtomQueryFeatures$I$I$J$I$I.apply(this, [0, nbits, 2048, 1, -1]);
p$1.addAtomQueryFeatures$I$I$J$I$I.apply(this, [3, nbits, 4096, 1, -1]);
p$1.addAtomQueryFeatures$I$I$J$I$I.apply(this, [4, nbits, 120, 4, 3]);
p$1.addAtomQueryFeatures$I$I$J$I$I.apply(this, [5, nbits, 70368744177670, 2, 1]);
p$1.addAtomQueryFeatures$I$I$J$I$I.apply(this, [6, nbits, 1, 1, -1]);
p$1.addAtomQueryFeatures$I$I$J$I$I.apply(this, [7, nbits, 1920, 4, 7]);
count=0;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) if (this.mMol.getAtomList$I(this.mGraphAtom[atom]) != null ) ++count;

if (count > 0) {
p$1.encodeFeatureNo$I.apply(this, [8]);
p$1.encodeBits$J$I.apply(this, [count, nbits]);
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
var atomList=this.mMol.getAtomList$I(this.mGraphAtom[atom]);
if (atomList != null ) {
p$1.encodeBits$J$I.apply(this, [atom, nbits]);
p$1.encodeBits$J$I.apply(this, [atomList.length, 4]);
for (var a, $a = 0, $$a = atomList; $a<$$a.length&&((a=($$a[$a])),1);$a++) p$1.encodeBits$J$I.apply(this, [a, 8]);

}}
}p$1.addBondQueryFeatures$I$I$I$I$I.apply(this, [9, nbits, 384, 2, 7]);
p$1.addBondQueryFeatures$I$I$I$I$I.apply(this, [10, nbits, 31, 5, 0]);
p$1.addAtomQueryFeatures$I$I$J$I$I.apply(this, [11, nbits, 8192, 1, -1]);
p$1.addBondQueryFeatures$I$I$I$I$I.apply(this, [12, nbits, 130560, 8, 9]);
p$1.addAtomQueryFeatures$I$I$J$I$I.apply(this, [13, nbits, 114688, 3, 14]);
p$1.addAtomQueryFeatures$I$I$J$I$I.apply(this, [14, nbits, 4063232, 5, 17]);
p$1.addAtomQueryFeatures$I$I$J$I$I.apply(this, [16, nbits, 29360128, 3, 22]);
}count=0;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) if (this.mAbnormalValence != null  && this.mAbnormalValence[this.mGraphAtom[atom]] != -1 ) ++count;

if (count != 0) {
p$1.encodeFeatureNo$I.apply(this, [17]);
p$1.encodeBits$J$I.apply(this, [count, nbits]);
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mAbnormalValence != null  && this.mAbnormalValence[this.mGraphAtom[atom]] != -1 ) {
p$1.encodeBits$J$I.apply(this, [atom, nbits]);
p$1.encodeBits$J$I.apply(this, [this.mAbnormalValence[this.mGraphAtom[atom]], 4]);
}}
}if ((this.mMode & 8) != 0 || (this.mMode & 1024) != 0 ) {
count=0;
var maxLength=0;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
var label=this.mMol.getAtomCustomLabel$I(this.mGraphAtom[atom]);
if (label != null ) {
++count;
maxLength=Math.max(maxLength, label.length$());
}}
if (count != 0) {
var lbits=C$.getNeededBits$I(maxLength);
p$1.encodeFeatureNo$I.apply(this, [18]);
p$1.encodeBits$J$I.apply(this, [count, nbits]);
p$1.encodeBits$J$I.apply(this, [lbits, 4]);
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
var customLabel=this.mMol.getAtomCustomLabel$I(this.mGraphAtom[atom]);
if (customLabel != null ) {
p$1.encodeBits$J$I.apply(this, [atom, nbits]);
p$1.encodeBits$J$I.apply(this, [customLabel.length$(), lbits]);
for (var i=0; i < customLabel.length$(); i++) p$1.encodeBits$J$I.apply(this, [customLabel.charAt$I(i), 7]);

}}
}}if (this.mMol.isFragment$()) {
p$1.addAtomQueryFeatures$I$I$J$I$I.apply(this, [19, nbits, 234881024, 3, 25]);
p$1.addBondQueryFeatures$I$I$I$I$I.apply(this, [20, nbits, 917504, 3, 17]);
}count=0;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) if (this.mMol.getAtomRadical$I(this.mGraphAtom[atom]) != 0) ++count;

if (count != 0) {
p$1.encodeFeatureNo$I.apply(this, [21]);
p$1.encodeBits$J$I.apply(this, [count, nbits]);
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mMol.getAtomRadical$I(this.mGraphAtom[atom]) != 0) {
p$1.encodeBits$J$I.apply(this, [atom, nbits]);
p$1.encodeBits$J$I.apply(this, [this.mMol.getAtomRadical$I(this.mGraphAtom[atom]) >> 4, 2]);
}}
}if (this.mMol.isFragment$()) {
p$1.addAtomQueryFeatures$I$I$J$I$I.apply(this, [22, nbits, 268435456, 1, -1]);
p$1.addBondQueryFeatures$I$I$I$I$I.apply(this, [23, nbits, 1048576, 1, -1]);
p$1.addBondQueryFeatures$I$I$I$I$I.apply(this, [24, nbits, 6291456, 2, 21]);
}if ((this.mMode & 16) != 0) {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mMol.isSelectedAtom$I(this.mGraphAtom[atom])) {
p$1.encodeFeatureNo$I.apply(this, [25]);
for (var a=0; a < this.mMol.getAtoms$(); a++) p$1.encodeBits$J$I.apply(this, [this.mMol.isSelectedAtom$I(this.mGraphAtom[a]) ? 1 : 0, 1]);

break;
}}
}var isAromaticSPBond=p$1.getAromaticSPBonds.apply(this, []);
if (isAromaticSPBond != null ) {
count=0;
for (var bond=0; bond < this.mMol.getBonds$(); bond++) if (isAromaticSPBond[this.mGraphBond[bond]]) ++count;

p$1.encodeFeatureNo$I.apply(this, [26]);
p$1.encodeBits$J$I.apply(this, [count, nbits]);
for (var bond=0; bond < this.mMol.getBonds$(); bond++) if (isAromaticSPBond[this.mGraphBond[bond]]) p$1.encodeBits$J$I.apply(this, [bond, nbits]);

}if (this.mMol.isFragment$()) p$1.addAtomQueryFeatures$I$I$J$I$I.apply(this, [27, nbits, 536870912, 1, -1]);
count=0;
for (var bond=0; bond < this.mMol.getBonds$(); bond++) if (this.mMol.getBondType$I(this.mGraphBond[bond]) == 32) ++count;

if (count != 0) {
p$1.encodeFeatureNo$I.apply(this, [28]);
p$1.encodeBits$J$I.apply(this, [count, nbits]);
for (var bond=0; bond < this.mMol.getBonds$(); bond++) if (this.mMol.getBondType$I(this.mGraphBond[bond]) == 32) p$1.encodeBits$J$I.apply(this, [bond, nbits]);

}if (this.mMol.isFragment$()) {
p$1.addAtomQueryFeatures$I$I$J$I$I.apply(this, [29, nbits, 3221225472, 2, 30]);
p$1.addAtomQueryFeatures$I$I$J$I$I.apply(this, [30, nbits, 545460846592, 7, 32]);
p$1.addAtomQueryFeatures$I$I$J$I$I.apply(this, [32, nbits, 52776558133248, 2, 44]);
p$1.addAtomQueryFeatures$I$I$J$I$I.apply(this, [33, nbits, 17042430230528, 5, 39]);
p$1.addAtomQueryFeatures$I$I$J$I$I.apply(this, [34, nbits, 70368744177664, 1, -1]);
p$1.addBondQueryFeatures$I$I$I$I$I.apply(this, [35, nbits, 8388608, 1, -1]);
p$1.addBondQueryFeatures$I$I$I$I$I.apply(this, [36, nbits, 96, 2, 5]);
}count=0;
for (var bond=0; bond < this.mMol.getBonds$(); bond++) if (this.mMol.getBondType$I(this.mGraphBond[bond]) == 8 || this.mMol.getBondType$I(this.mGraphBond[bond]) == 16 ) ++count;

if (count != 0) {
p$1.encodeFeatureNo$I.apply(this, [37]);
p$1.encodeBits$J$I.apply(this, [count, nbits]);
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mMol.getBondType$I(this.mGraphBond[bond]) == 8 || this.mMol.getBondType$I(this.mGraphBond[bond]) == 16 ) {
p$1.encodeBits$J$I.apply(this, [bond, nbits]);
p$1.encodeBits$J$I.apply(this, [this.mMol.getBondType$I(this.mGraphBond[bond]) == 8 ? 0 : 1, 1]);
}}
}p$1.encodeBits$J$I.apply(this, [0, 1]);
this.mIDCode=p$1.encodeBitsEnd.apply(this, []);
}, p$1);

Clazz.newMeth(C$, 'addAtomQueryFeatures$I$I$J$I$I',  function (codeNo, nbits, qfMask, qfBits, qfShift) {
var count=0;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) if (Long.$ne((Long.$and(this.mMol.getAtomQueryFeatures$I(this.mGraphAtom[atom]),qfMask)),0 )) ++count;

if (count == 0) return;
p$1.encodeFeatureNo$I.apply(this, [codeNo]);
p$1.encodeBits$J$I.apply(this, [count, nbits]);
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
var feature=Long.$and(this.mMol.getAtomQueryFeatures$I(this.mGraphAtom[atom]),qfMask);
if (Long.$ne(feature,0 )) {
p$1.encodeBits$J$I.apply(this, [atom, nbits]);
if (qfBits != 1) p$1.encodeBits$J$I.apply(this, [Long.$sr(feature,qfShift), qfBits]);
}}
}, p$1);

Clazz.newMeth(C$, 'addBondQueryFeatures$I$I$I$I$I',  function (codeNo, nbits, qfMask, qfBits, qfShift) {
var count=0;
for (var bond=0; bond < this.mMol.getBonds$(); bond++) if ((this.mMol.getBondQueryFeatures$I(this.mGraphBond[bond]) & qfMask) != 0) ++count;

if (count == 0) return;
p$1.encodeFeatureNo$I.apply(this, [codeNo]);
p$1.encodeBits$J$I.apply(this, [count, nbits]);
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
var feature=this.mMol.getBondQueryFeatures$I(this.mGraphBond[bond]) & qfMask;
if (feature != 0) {
p$1.encodeBits$J$I.apply(this, [bond, nbits]);
if (qfBits != 1) p$1.encodeBits$J$I.apply(this, [feature >> qfShift, qfBits]);
}}
}, p$1);

Clazz.newMeth(C$, 'getAromaticSPBonds',  function () {
var isAromaticSPBond=null;
var ringSet=this.mMol.getRingSet$();
for (var r=0; r < ringSet.getSize$(); r++) {
if (ringSet.isDelocalized$I(r)) {
var count=0;
var ringAtom=ringSet.getRingAtoms$I(r);
for (var atom, $atom = 0, $$atom = ringAtom; $atom<$$atom.length&&((atom=($$atom[$atom])),1);$atom++) if (p$1.hasTwoAromaticPiElectrons$I.apply(this, [atom])) ++count;

if (count != 0) {
var ringBond=ringSet.getRingBonds$I(r);
if (isAromaticSPBond == null ) isAromaticSPBond=Clazz.array(Boolean.TYPE, [this.mMol.getBonds$()]);
if (count == ringAtom.length) {
var minIndex=-1;
var minValue=2147483647;
for (var i=0; i < ringAtom.length; i++) {
if (minValue > this.mGraphAtom[ringBond[i]]) {
minValue=this.mGraphAtom[ringBond[i]];
minIndex=i;
}}
while (count > 0){
isAromaticSPBond[ringBond[minIndex]]=true;
minIndex=p$1.validateCyclicIndex$I$I.apply(this, [minIndex + 2, ringAtom.length]);
count-=2;
}
} else {
var index=0;
while (p$1.hasTwoAromaticPiElectrons$I.apply(this, [ringAtom[index]]))++index;

while (!p$1.hasTwoAromaticPiElectrons$I.apply(this, [ringAtom[index]]))index=p$1.validateCyclicIndex$I$I.apply(this, [index + 1, ringAtom.length]);

while (count > 0){
isAromaticSPBond[ringBond[index]]=true;
index=p$1.validateCyclicIndex$I$I.apply(this, [index + 2, ringAtom.length]);
count-=2;
while (!p$1.hasTwoAromaticPiElectrons$I.apply(this, [ringAtom[index]]))index=p$1.validateCyclicIndex$I$I.apply(this, [index + 1, ringAtom.length]);

}
}}}}
return isAromaticSPBond;
}, p$1);

Clazz.newMeth(C$, 'hasTwoAromaticPiElectrons$I',  function (atom) {
if (this.mMol.getAtomPi$I(atom) < 2) return false;
if (this.mMol.getConnAtoms$I(atom) == 2) return true;
var aromaticPi=0;
for (var i=0; i < this.mMol.getConnAtoms$I(atom); i++) {
var connBond=this.mMol.getConnBond$I$I(atom, i);
if (this.mMol.isAromaticBond$I(connBond)) aromaticPi+=this.mMol.getBondOrder$I(connBond) - 1;
}
return aromaticPi > 1;
}, p$1);

Clazz.newMeth(C$, 'validateCyclicIndex$I$I',  function (index, limit) {
return (index < limit) ? index : index - limit;
}, p$1);

Clazz.newMeth(C$, 'invalidateCoordinates$',  function () {
this.mEncodedCoords=null;
});

Clazz.newMeth(C$, 'getEncodedCoordinates$',  function () {
return this.getEncodedCoordinates$Z(this.mZCoordinatesAvailable);
});

Clazz.newMeth(C$, 'getEncodedCoordinates$Z',  function (keepPositionAndScale) {
if (this.mEncodedCoords == null ) {
p$1.generateGraph.apply(this, []);
p$1.encodeCoordinates$Z$com_actelion_research_chem_CoordinatesA.apply(this, [keepPositionAndScale, this.mMol.getAtomCoordinates$()]);
}return this.mEncodedCoords;
});

Clazz.newMeth(C$, 'getEncodedCoordinates$Z$com_actelion_research_chem_CoordinatesA',  function (keepPositionAndScale, atomCoordinates) {
if (this.mEncodedCoords == null ) {
p$1.generateGraph.apply(this, []);
p$1.encodeCoordinates$Z$com_actelion_research_chem_CoordinatesA.apply(this, [keepPositionAndScale, atomCoordinates]);
}return this.mEncodedCoords;
});

Clazz.newMeth(C$, 'encodeCoordinates$Z$com_actelion_research_chem_CoordinatesA',  function (keepPositionAndScale, coords) {
if (this.mMol.getAtoms$() == 0) {
this.mEncodedCoords="";
return;
}var includeHydrogenCoordinates=false;
if (this.mZCoordinatesAvailable && this.mMol.getAllAtoms$() > this.mMol.getAtoms$()  && !this.mMol.isFragment$() ) {
includeHydrogenCoordinates=true;
for (var i=0; i < this.mMol.getAtoms$(); i++) {
if (this.mMol.getImplicitHydrogens$I(i) != 0) {
includeHydrogenCoordinates=false;
break;
}}
}var resolutionBits=this.mZCoordinatesAvailable ? 16 : 8;
p$1.encodeBitsStart$Z.apply(this, [true]);
this.mEncodingBuffer.append$C(includeHydrogenCoordinates ? "#" : "!");
p$1.encodeBits$J$I.apply(this, [this.mZCoordinatesAvailable ? 1 : 0, 1]);
p$1.encodeBits$J$I.apply(this, [keepPositionAndScale ? 1 : 0, 1]);
p$1.encodeBits$J$I.apply(this, [(resolutionBits/2|0), 4]);
var maxDelta=0.0;
for (var i=1; i < this.mMol.getAtoms$(); i++) maxDelta=p$1.getMaxDelta$I$I$D$com_actelion_research_chem_CoordinatesA.apply(this, [this.mGraphAtom[i], (this.mGraphFrom[i] == -1) ? -1 : this.mGraphAtom[this.mGraphFrom[i]], maxDelta, coords]);

if (includeHydrogenCoordinates) {
for (var i=0; i < this.mMol.getAtoms$(); i++) {
var atom=this.mGraphAtom[i];
for (var j=this.mMol.getConnAtoms$I(atom); j < this.mMol.getAllConnAtoms$I(atom); j++) maxDelta=p$1.getMaxDelta$I$I$D$com_actelion_research_chem_CoordinatesA.apply(this, [this.mMol.getConnAtom$I$I(atom, j), atom, maxDelta, coords]);

}
}if (this.mMol.getAtoms$() > 1 && maxDelta == 0.0  ) {
this.mEncodedCoords="";
return;
}var binCount=(1 << resolutionBits);
var increment=maxDelta / (binCount / 2.0 - 1);
var maxDeltaPlusHalfIncrement=maxDelta + increment / 2.0;
for (var i=1; i < this.mMol.getAtoms$(); i++) p$1.encodeCoords$I$I$D$D$I$com_actelion_research_chem_CoordinatesA.apply(this, [this.mGraphAtom[i], (this.mGraphFrom[i] == -1) ? -1 : this.mGraphAtom[this.mGraphFrom[i]], maxDeltaPlusHalfIncrement, increment, resolutionBits, coords]);

if (includeHydrogenCoordinates) {
for (var i=0; i < this.mMol.getAtoms$(); i++) {
var atom=this.mGraphAtom[i];
for (var j=this.mMol.getConnAtoms$I(atom); j < this.mMol.getAllConnAtoms$I(atom); j++) p$1.encodeCoords$I$I$D$D$I$com_actelion_research_chem_CoordinatesA.apply(this, [this.mMol.getConnAtom$I$I(atom, j), atom, maxDeltaPlusHalfIncrement, increment, resolutionBits, coords]);

}
}if (keepPositionAndScale) {
var avblDefault=this.mZCoordinatesAvailable ? 1.5 : $I$(10).getDefaultAverageBondLength$();
var avbl=this.mMol.getAverageBondLength$I$I$D$com_actelion_research_chem_CoordinatesA(includeHydrogenCoordinates ? this.mMol.getAllAtoms$() : this.mMol.getAtoms$(), includeHydrogenCoordinates ? this.mMol.getAllBonds$() : this.mMol.getBonds$(), avblDefault, coords);
p$1.encodeBits$J$I.apply(this, [p$1.encodeABVL$D$I.apply(this, [avbl, binCount]), resolutionBits]);
p$1.encodeBits$J$I.apply(this, [p$1.encodeShift$D$I.apply(this, [coords[this.mGraphAtom[0]].x / avbl, binCount]), resolutionBits]);
p$1.encodeBits$J$I.apply(this, [p$1.encodeShift$D$I.apply(this, [coords[this.mGraphAtom[0]].y / avbl, binCount]), resolutionBits]);
if (this.mZCoordinatesAvailable) p$1.encodeBits$J$I.apply(this, [p$1.encodeShift$D$I.apply(this, [coords[this.mGraphAtom[0]].z / avbl, binCount]), resolutionBits]);
}this.mEncodedCoords=p$1.encodeBitsEnd.apply(this, []);
}, p$1);

Clazz.newMeth(C$, 'getMaxDelta$I$I$D$com_actelion_research_chem_CoordinatesA',  function (atom, from, maxDelta, coords) {
var deltaX=(from == -1) ? Math.abs(coords[atom].x - coords[this.mGraphAtom[0]].x) / 8.0 : Math.abs(coords[atom].x - coords[from].x);
if (maxDelta < deltaX ) maxDelta=deltaX;
var deltaY=(from == -1) ? Math.abs(coords[atom].y - coords[this.mGraphAtom[0]].y) / 8.0 : Math.abs(coords[atom].y - coords[from].y);
if (maxDelta < deltaY ) maxDelta=deltaY;
if (this.mZCoordinatesAvailable) {
var deltaZ=(from == -1) ? Math.abs(coords[atom].z - coords[this.mGraphAtom[0]].z) / 8.0 : Math.abs(coords[atom].z - coords[from].z);
if (maxDelta < deltaZ ) maxDelta=deltaZ;
}return maxDelta;
}, p$1);

Clazz.newMeth(C$, 'encodeCoords$I$I$D$D$I$com_actelion_research_chem_CoordinatesA',  function (atom, from, maxDeltaPlusHalfIncrement, increment, resolutionBits, coords) {
var deltaX=(from == -1) ? (coords[atom].x - coords[this.mGraphAtom[0]].x) / 8.0 : coords[atom].x - coords[from].x;
var deltaY=(from == -1) ? (coords[atom].y - coords[this.mGraphAtom[0]].y) / 8.0 : coords[atom].y - coords[from].y;
p$1.encodeBits$J$I.apply(this, [(((maxDeltaPlusHalfIncrement + deltaX) / increment)|0), resolutionBits]);
p$1.encodeBits$J$I.apply(this, [(((maxDeltaPlusHalfIncrement + deltaY) / increment)|0), resolutionBits]);
if (this.mZCoordinatesAvailable) {
var deltaZ=(from == -1) ? (coords[atom].z - coords[this.mGraphAtom[0]].z) / 8.0 : coords[atom].z - coords[from].z;
p$1.encodeBits$J$I.apply(this, [(((maxDeltaPlusHalfIncrement + deltaZ) / increment)|0), resolutionBits]);
}}, p$1);

Clazz.newMeth(C$, 'encodeABVL$D$I',  function (value, binCount) {
return Math.min(binCount - 1, Math.max(0, ((0.5 + Math.log10(value / 0.1) / Math.log10(2000.0) * (binCount - 1))|0)));
}, p$1);

Clazz.newMeth(C$, 'encodeShift$D$I',  function (value, binCount) {
var halfBinCount=(binCount/2|0);
var isNegative=(value < 0 );
value=Math.abs(value);
var steepness=(binCount/32|0);
var intValue=Math.min(halfBinCount - 1, Long.$ival(Math.round$D(value * halfBinCount / (value + steepness))));
return isNegative ? halfBinCount + intValue : intValue;
}, p$1);

Clazz.newMeth(C$, 'getEncodedMapping$',  function () {
if (this.mMapping == null ) {
p$1.generateGraph.apply(this, []);
p$1.encodeMapping.apply(this, []);
}return this.mMapping;
});

Clazz.newMeth(C$, 'encodeMapping',  function () {
if (this.mMol.getAtoms$() == 0) {
this.mMapping="";
return;
}var maxMapNo=0;
var autoMappingFound=false;
var manualMappingFound=false;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (maxMapNo < this.mMol.getAtomMapNo$I(atom)) maxMapNo=this.mMol.getAtomMapNo$I(atom);
if (this.mMol.isAutoMappedAtom$I(atom)) autoMappingFound=true;
 else manualMappingFound=true;
}
if (maxMapNo == 0) {
this.mMapping="";
return;
}var nbits=C$.getNeededBits$I(maxMapNo);
p$1.encodeBitsStart$Z.apply(this, [true]);
p$1.encodeBits$J$I.apply(this, [nbits, 4]);
p$1.encodeBits$J$I.apply(this, [autoMappingFound ? 1 : 0, 1]);
p$1.encodeBits$J$I.apply(this, [manualMappingFound ? 1 : 0, 1]);
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
p$1.encodeBits$J$I.apply(this, [this.mMol.getAtomMapNo$I(this.mGraphAtom[atom]), nbits]);
if (autoMappingFound && manualMappingFound ) p$1.encodeBits$J$I.apply(this, [this.mMol.isAutoMappedAtom$I(this.mGraphAtom[atom]) ? 1 : 0, 1]);
}
this.mMapping=p$1.encodeBitsEnd.apply(this, []);
}, p$1);

Clazz.newMeth(C$, 'idGenerateConfigurations',  function () {
this.mTHConfiguration=Clazz.array(Byte.TYPE, [this.mMol.getAtoms$()]);
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mTHParity[atom] == 1 || this.mTHParity[atom] == 2 ) {
var inversion=this.mTHParityIsMesoInverted[atom];
if (this.mMol.isCentralAlleneAtom$I(atom)) {
for (var i=0; i < this.mMol.getConnAtoms$I(atom); i++) {
var connAtom=this.mMol.getConnAtom$I$I(atom, i);
var neighbours=0;
var neighbour=Clazz.array(Integer.TYPE, [3]);
for (var j=0; j < this.mMol.getConnAtoms$I(connAtom); j++) {
neighbour[neighbours]=this.mMol.getConnAtom$I$I(connAtom, j);
if (neighbour[neighbours] != atom) ++neighbours;
}
if (neighbours == 2 && (!!((this.mCanRank[neighbour[0]] > this.mCanRank[neighbour[1]]) ^ (this.mGraphIndex[neighbour[0]] < this.mGraphIndex[neighbour[1]]))) ) inversion=!inversion;
}
} else {
for (var i=1; i < this.mMol.getConnAtoms$I(atom); i++) {
for (var j=0; j < i; j++) {
var connAtom1=this.mMol.getConnAtom$I$I(atom, i);
var connAtom2=this.mMol.getConnAtom$I$I(atom, j);
if (this.mCanRank[connAtom1] > this.mCanRank[connAtom2]) inversion=!inversion;
if (this.mGraphIndex[connAtom1] < this.mGraphIndex[connAtom2]) inversion=!inversion;
}
}
}this.mTHConfiguration[atom]=(!!((this.mTHParity[atom] == 1) ^ inversion)) ? 1 : (2|0);
} else {
this.mTHConfiguration[atom]=this.mTHParity[atom];
}}
this.mEZConfiguration=Clazz.array(Byte.TYPE, [this.mMol.getBonds$()]);
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mEZParity[bond] == 1 || this.mEZParity[bond] == 2 ) {
var inversion=false;
for (var i=0; i < 2; i++) {
var bondAtom=this.mMol.getBondAtom$I$I(i, bond);
if (this.mMol.getConnAtoms$I(bondAtom) == 3) {
var neighbour=Clazz.array(Integer.TYPE, [2]);
var neighbours=0;
for (var j=0; j < 3; j++) if (this.mMol.getConnAtom$I$I(bondAtom, j) != this.mMol.getBondAtom$I$I(1 - i, bond)) neighbour[neighbours++]=this.mMol.getConnAtom$I$I(bondAtom, j);

if (this.mCanRank[neighbour[0]] > this.mCanRank[neighbour[1]]) inversion=!inversion;
if (this.mGraphIndex[neighbour[0]] < this.mGraphIndex[neighbour[1]]) inversion=!inversion;
}}
this.mEZConfiguration[bond]=(!!((this.mEZParity[bond] == 1) ^ inversion)) ? 1 : (2|0);
} else {
this.mEZConfiguration[bond]=this.mEZParity[bond];
}}
}, p$1);

Clazz.newMeth(C$, 'idNormalizeESRGroupNumbers',  function () {
p$1.idNormalizeESRGroupNumbers$I.apply(this, [1]);
p$1.idNormalizeESRGroupNumbers$I.apply(this, [2]);
}, p$1);

Clazz.newMeth(C$, 'idNormalizeESRGroupNumbers$I',  function (type) {
var groupRank=Clazz.array(Integer.TYPE, [32]);
var groups=0;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if ((this.mTHConfiguration[atom] == 1 || this.mTHConfiguration[atom] == 2 ) && this.mTHESRType[atom] == type ) {
var group=this.mTHESRGroup[atom];
if (groupRank[group] < this.mCanRank[atom]) {
if (groupRank[group] == 0) ++groups;
groupRank[group]=this.mCanRank[atom];
}}}
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if ((this.mEZConfiguration[bond] == 1 || this.mEZConfiguration[bond] == 2 ) && this.mEZESRType[bond] == type  && this.mMol.getBondType$I(bond) == 1 ) {
var group=this.mEZESRGroup[bond];
var rank=Math.max(this.mCanRank[this.mMol.getBondAtom$I$I(0, bond)], this.mCanRank[this.mMol.getBondAtom$I$I(1, bond)]);
if (groupRank[group] < rank) {
if (groupRank[group] == 0) ++groups;
groupRank[group]=rank;
}}}
var canGroup=Clazz.array(Byte.TYPE, [32]);
for (var i=0; i < groups; i++) {
var maxGroup=-1;
var maxRank=0;
for (var j=0; j < 32; j++) {
if (maxRank < groupRank[j]) {
maxRank=groupRank[j];
maxGroup=j;
}}
groupRank[maxGroup]=0;
canGroup[maxGroup]=(i|0);
}
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) if ((this.mTHConfiguration[atom] == 1 || this.mTHConfiguration[atom] == 2 ) && this.mTHESRType[atom] == type ) this.mTHESRGroup[atom]=canGroup[this.mTHESRGroup[atom]];

for (var bond=0; bond < this.mMol.getBonds$(); bond++) if ((this.mEZConfiguration[bond] == 1 || this.mEZConfiguration[bond] == 2 ) && this.mEZESRType[bond] == type  && this.mMol.getBondType$I(bond) == 1 ) this.mEZESRGroup[bond]=canGroup[this.mEZESRGroup[bond]];

}, p$1);

Clazz.newMeth(C$, 'encodeBitsStart$Z',  function (avoid127) {
this.mEncodingBuffer=Clazz.new_($I$(11,1));
this.mEncodingBitsAvail=6;
this.mEncodingTempData=0;
this.mEncodeAvoid127=avoid127;
}, p$1);

Clazz.newMeth(C$, 'encodeFeatureNo$I',  function (codeNo) {
for (var i=0; i < this.mFeatureBlock; i++) codeNo-=16;

if (codeNo < 0) System.out.println$S("ERROR in Canonizer: Code unexpectedly low.");
while (codeNo > 15){
p$1.encodeBits$J$I.apply(this, [1, 1]);
p$1.encodeBits$J$I.apply(this, [15, 4]);
codeNo-=16;
++this.mFeatureBlock;
}
p$1.encodeBits$J$I.apply(this, [1, 1]);
p$1.encodeBits$J$I.apply(this, [codeNo, 4]);
}, p$1);

Clazz.newMeth(C$, 'encodeBits$J$I',  function (data, bits) {
while (bits != 0){
if (this.mEncodingBitsAvail == 0) {
if (!this.mEncodeAvoid127 || this.mEncodingTempData != 63 ) this.mEncodingTempData+=64;
this.mEncodingBuffer.append$C(String.fromCharCode(this.mEncodingTempData));
this.mEncodingBitsAvail=6;
this.mEncodingTempData=0;
}this.mEncodingTempData<<=1;
this.mEncodingTempData=Long.$ival(Long.$or(this.mEncodingTempData,((Long.$and(data,1)))));
(data=Long.$sr(data,(1)));
--bits;
--this.mEncodingBitsAvail;
}
}, p$1);

Clazz.newMeth(C$, 'encodeBitsEnd',  function () {
this.mEncodingTempData<<=this.mEncodingBitsAvail;
if (!this.mEncodeAvoid127 || this.mEncodingTempData != 63 ) this.mEncodingTempData+=64;
this.mEncodingBuffer.append$C(String.fromCharCode(this.mEncodingTempData));
return this.mEncodingBuffer.toString();
}, p$1);

Clazz.newMeth(C$, 'getNeededBits$I',  function (maxNo) {
var bits=0;
while (maxNo > 0){
maxNo>>=1;
++bits;
}
return bits;
}, 1);

Clazz.newMeth(C$, 'getTHParity$I',  function (atom) {
return this.mTHParity[atom];
});

Clazz.newMeth(C$, 'getEZParity$I',  function (bond) {
return this.mEZParity[bond];
});

Clazz.newMeth(C$, 'getPseudoStereoGroupCount$',  function () {
return this.mNoOfPseudoGroups;
});

Clazz.newMeth(C$, 'getPseudoEZGroup$I',  function (bond) {
return this.mPseudoEZGroup[bond];
});

Clazz.newMeth(C$, 'getPseudoTHGroup$I',  function (atom) {
return this.mPseudoTHGroup[atom];
});

Clazz.newMeth(C$, 'normalizeEnantiomer$',  function () {
var parityCount=Clazz.array(Integer.TYPE, [this.mNoOfRanks + 1]);
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mMol.getAtomESRType$I(atom) == 0) {
if (this.mTHParity[atom] == 1) ++parityCount[this.mCanRank[atom]];
 else if (this.mTHParity[atom] == 2) --parityCount[this.mCanRank[atom]];
}}
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mMol.getBondOrder$I(bond) == 1 && this.mMol.getBondESRType$I(bond) == 0 ) {
if (this.mEZParity[bond] == 1) {
++parityCount[this.mCanRank[this.mMol.getBondAtom$I$I(0, bond)]];
++parityCount[this.mCanRank[this.mMol.getBondAtom$I$I(1, bond)]];
} else if (this.mEZParity[bond] == 2) {
--parityCount[this.mCanRank[this.mMol.getBondAtom$I$I(0, bond)]];
--parityCount[this.mCanRank[this.mMol.getBondAtom$I$I(1, bond)]];
}}}
for (var rank=1; rank <= this.mNoOfRanks; rank++) {
if (parityCount[rank] != 0) {
var invert=(parityCount[rank] < 0);
if (invert) {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mMol.getAtomESRType$I(atom) == 0) {
if (this.mTHParity[atom] == 1) this.mTHParity[atom]=(2|0);
 else if (this.mTHParity[atom] == 2) this.mTHParity[atom]=(1|0);
}}
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mMol.getBondOrder$I(bond) == 1 && this.mMol.getBondESRType$I(bond) == 0 ) {
if (this.mEZParity[bond] == 1) this.mEZParity[bond]=(2|0);
 else if (this.mEZParity[bond] == 2) this.mEZParity[bond]=(1|0);
}}
}return invert;
}}
return false;
});

Clazz.newMeth(C$, 'setParities$',  function () {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mTHParity[atom] == 1 || this.mTHParity[atom] == 2 ) {
var inversion=false;
if (this.mMol.isCentralAlleneAtom$I(atom)) {
for (var i=0; i < this.mMol.getConnAtoms$I(atom); i++) {
var connAtom=this.mMol.getConnAtom$I$I(atom, i);
var neighbours=0;
var neighbour=Clazz.array(Integer.TYPE, [3]);
for (var j=0; j < this.mMol.getConnAtoms$I(connAtom); j++) {
neighbour[neighbours]=this.mMol.getConnAtom$I$I(connAtom, j);
if (neighbour[neighbours] != atom) ++neighbours;
}
if (neighbours == 2 && (!!((this.mCanRank[neighbour[0]] > this.mCanRank[neighbour[1]]) ^ (neighbour[0] < neighbour[1]))) ) inversion=!inversion;
}
} else {
for (var i=1; i < this.mMol.getConnAtoms$I(atom); i++) {
for (var j=0; j < i; j++) {
var connAtom1=this.mMol.getConnAtom$I$I(atom, i);
var connAtom2=this.mMol.getConnAtom$I$I(atom, j);
if (this.mCanRank[connAtom1] > this.mCanRank[connAtom2]) inversion=!inversion;
if (connAtom1 < connAtom2) inversion=!inversion;
}
}
}this.mMol.setAtomParity$I$I$Z(atom, (!!((this.mTHParity[atom] == 1) ^ inversion)) ? 1 : 2, this.mTHParityIsPseudo[atom]);
} else {
this.mMol.setAtomParity$I$I$Z(atom, this.mTHParity[atom], this.mTHParityIsPseudo[atom]);
}}
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mEZParity[bond] == 1 || this.mEZParity[bond] == 2 ) {
var inversion=false;
for (var i=0; i < 2; i++) {
var bondAtom=this.mMol.getBondAtom$I$I(i, bond);
if (this.mMol.getConnAtoms$I(bondAtom) == 3) {
var neighbour=Clazz.array(Integer.TYPE, [2]);
var neighbours=0;
for (var j=0; j < 3; j++) if (this.mMol.getConnAtom$I$I(bondAtom, j) != this.mMol.getBondAtom$I$I(1 - i, bond)) neighbour[neighbours++]=this.mMol.getConnAtom$I$I(bondAtom, j);

if (this.mCanRank[neighbour[0]] > this.mCanRank[neighbour[1]]) inversion=!inversion;
if (neighbour[0] < neighbour[1]) inversion=!inversion;
}}
this.mMol.setBondParity$I$I$Z(bond, (!!((this.mEZParity[bond] == 1) ^ inversion)) ? 1 : 2, this.mEZParityIsPseudo[bond]);
} else {
this.mMol.setBondParity$I$I$Z(bond, this.mEZParity[bond], this.mEZParityIsPseudo[bond]);
}}
});

Clazz.newMeth(C$, 'setStereoCenters$',  function () {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
this.mMol.setAtomStereoCenter$I$Z(atom, this.mIsStereoCenter[atom]);
}
});

Clazz.newMeth(C$, 'setCIPParities$',  function () {
if (this.mTHCIPParity != null ) for (var atom=0; atom < this.mMol.getAtoms$(); atom++) this.mMol.setAtomCIPParity$I$I(atom, this.mTHCIPParity[atom]);

if (this.mEZCIPParity != null ) for (var bond=0; bond < this.mMol.getBonds$(); bond++) this.mMol.setBondCIPParity$I$I(bond, this.mEZCIPParity[bond]);

});

Clazz.newMeth(C$, 'cipCalcTHParity$I',  function (atom) {
if ((this.mTHParity[atom] == 1 || this.mTHParity[atom] == 2 )) {
var invertedOrder=false;
if (this.mMol.getAtomPi$I(atom) == 2) {
try {
for (var i=0; i < 2; i++) {
var alleneAtom=this.mMol.getConnAtom$I$I(atom, i);
if (this.mMol.getConnAtoms$I(alleneAtom) == 3) {
var connAtom=Clazz.array(Integer.TYPE, [2]);
var count=0;
for (var j=0; j < this.mMol.getConnAtoms$I(alleneAtom); j++) if (this.mMol.getConnBondOrder$I$I(alleneAtom, j) == 1) connAtom[count++]=this.mMol.getConnAtom$I$I(alleneAtom, j);

if (!!((this.mCanRank[connAtom[0]] > this.mCanRank[connAtom[1]]) ^ p$1.cipComparePriority$I$I$I.apply(this, [alleneAtom, connAtom[0], connAtom[1]]))) invertedOrder=!invertedOrder;
}}
} catch (e) {
if (Clazz.exceptionOf(e,"Exception")){
this.mTHCIPParity[atom]=(3|0);
return;
} else {
throw e;
}
}
} else {
var cipConnAtom;
try {
cipConnAtom=p$1.cipGetOrderedConns$I.apply(this, [atom]);
} catch (e) {
if (Clazz.exceptionOf(e,"Exception")){
this.mTHCIPParity[atom]=(3|0);
return;
} else {
throw e;
}
}
for (var i=1; i < cipConnAtom.length; i++) for (var j=0; j < i; j++) if (this.mCanRank[cipConnAtom[i]] < this.mCanRank[cipConnAtom[j]]) invertedOrder=!invertedOrder;


}if (!!((this.mTHParity[atom] == 1) ^ invertedOrder)) this.mTHCIPParity[atom]=(1|0);
 else this.mTHCIPParity[atom]=(2|0);
}}, p$1);

Clazz.newMeth(C$, 'cipCalcEZParity$I',  function (bond) {
if ((this.mEZParity[bond] == 1 || this.mEZParity[bond] == 2 ) && !this.mMol.isSmallRingBond$I(bond) ) {
var invertedOrder=false;
try {
for (var i=0; i < 2; i++) {
var bondAtom=this.mMol.getBondAtom$I$I(i, bond);
if (this.mMol.getConnAtoms$I(bondAtom) == 3) {
var connAtom=Clazz.array(Integer.TYPE, [2]);
var count=0;
for (var j=0; j < this.mMol.getConnAtoms$I(bondAtom); j++) if (this.mMol.getConnBond$I$I(bondAtom, j) != bond) connAtom[count++]=this.mMol.getConnAtom$I$I(bondAtom, j);

if (!!((this.mCanRank[connAtom[0]] > this.mCanRank[connAtom[1]]) ^ p$1.cipComparePriority$I$I$I.apply(this, [bondAtom, connAtom[0], connAtom[1]]))) invertedOrder=!invertedOrder;
}}
} catch (e) {
if (Clazz.exceptionOf(e,"Exception")){
this.mEZCIPParity[bond]=(3|0);
return;
} else {
throw e;
}
}
if (!!((this.mEZParity[bond] == 1) ^ invertedOrder)) this.mEZCIPParity[bond]=(1|0);
 else this.mEZCIPParity[bond]=(2|0);
}}, p$1);

Clazz.newMeth(C$, 'cipGetOrderedConns$I',  function (atom) {
var noOfConns=this.mMol.getAllConnAtoms$I(atom);
var orderedConn=Clazz.array(Integer.TYPE, [noOfConns]);
for (var i=0; i < noOfConns; i++) orderedConn[i]=this.mMol.getConnAtom$I$I(atom, i);

for (var i=noOfConns; i > 1; i--) {
var found=false;
for (var j=1; j < i; j++) {
if (p$1.cipComparePriority$I$I$I.apply(this, [atom, orderedConn[j - 1], orderedConn[j]])) {
found=true;
var temp=orderedConn[j - 1];
orderedConn[j - 1]=orderedConn[j];
orderedConn[j]=temp;
}}
if (!found) break;
}
return orderedConn;
}, p$1);

Clazz.newMeth(C$, 'cipComparePriority$I$I$I',  function (rootAtom, atom1, atom2) {
if (this.mMol.getAtomicNo$I(atom1) != this.mMol.getAtomicNo$I(atom2)) return (this.mMol.getAtomicNo$I(atom1) > this.mMol.getAtomicNo$I(atom2));
if (this.mMol.getAtomMass$I(atom1) != this.mMol.getAtomMass$I(atom2)) {
var mass1=this.mMol.isNaturalAbundance$I(atom1) ? $I$(10).cRoundedMass[this.mMol.getAtomicNo$I(atom1)] : this.mMol.getAtomMass$I(atom1);
var mass2=this.mMol.isNaturalAbundance$I(atom2) ? $I$(10).cRoundedMass[this.mMol.getAtomicNo$I(atom2)] : this.mMol.getAtomMass$I(atom2);
return (mass1 > mass2);
}var graphSize=this.mMol.getAtoms$();
var graphAtom=Clazz.array(Integer.TYPE, [graphSize]);
var graphParent=Clazz.array(Integer.TYPE, [graphSize]);
var graphRank=Clazz.array(Integer.TYPE, [graphSize]);
var graphIsPseudo=Clazz.array(Boolean.TYPE, [graphSize]);
var atomUsed=Clazz.array(Boolean.TYPE, [this.mMol.getAllAtoms$()]);
graphAtom[0]=rootAtom;
graphAtom[1]=atom1;
graphAtom[2]=atom2;
graphParent[0]=-1;
graphParent[1]=0;
graphParent[2]=0;
atomUsed[rootAtom]=true;
atomUsed[atom1]=true;
atomUsed[atom2]=true;
var current=1;
var highest=2;
var levelStart=Clazz.array(Integer.TYPE, [64]);
levelStart[1]=1;
levelStart[2]=3;
var currentLevel=2;
while (current <= highest){
while (current < levelStart[currentLevel]){
var currentAtom=graphAtom[current];
if (!graphIsPseudo[current]) {
var delocalizedBondCount=0;
var delocalizedMeanAtomicNo=0;
for (var i=0; i < this.mMol.getConnAtoms$I(currentAtom); i++) {
var candidate=this.mMol.getConnAtom$I$I(currentAtom, i);
if (highest + this.mMol.getConnBondOrder$I$I(currentAtom, i) + 1  >= graphSize) {
graphSize+=this.mMol.getAtoms$();
graphAtom=p$1.resize$IA$I.apply(this, [graphAtom, graphSize]);
graphParent=p$1.resize$IA$I.apply(this, [graphParent, graphSize]);
graphRank=p$1.resize$IA$I.apply(this, [graphRank, graphSize]);
graphIsPseudo=p$1.resize$ZA$I.apply(this, [graphIsPseudo, graphSize]);
}if (this.mMol.isDelocalizedBond$I(this.mMol.getConnBond$I$I(currentAtom, i))) {
++delocalizedBondCount;
delocalizedMeanAtomicNo+=this.mMol.getAtomicNo$I(candidate);
} else {
for (var j=1; j < this.mMol.getConnBondOrder$I$I(currentAtom, i); j++) {
++highest;
graphAtom[highest]=candidate;
graphParent[highest]=current;
graphIsPseudo[highest]=true;
}
}var parentGraphIndex=graphParent[current];
if (candidate == graphAtom[parentGraphIndex]) continue;
var atomInParentChain=false;
if (atomUsed[candidate]) {
var parent=graphParent[parentGraphIndex];
while (parent != -1){
if (candidate == graphAtom[parent]) {
atomInParentChain=true;
break;
}parent=graphParent[parent];
}
}if (atomInParentChain) {
++highest;
graphAtom[highest]=candidate;
graphParent[highest]=current;
graphIsPseudo[highest]=true;
} else {
++highest;
graphAtom[highest]=candidate;
graphParent[highest]=current;
atomUsed[candidate]=true;
}}
if (delocalizedBondCount != 0) {
++highest;
graphRank[highest]=((delocalizedMeanAtomicNo << 2)/delocalizedBondCount|0);
graphParent[highest]=current;
graphIsPseudo[highest]=true;
}}++current;
if (current == 10000) {
throw Clazz.new_(Clazz.load('Exception').c$$S,["Emergency break in while loop."]);
}}
if (levelStart.length == currentLevel + 1) levelStart=p$1.resize$IA$I.apply(this, [levelStart, levelStart.length + 64]);
levelStart[currentLevel + 1]=highest + 1;
for (var i=levelStart[currentLevel]; i < levelStart[currentLevel + 1]; i++) {
if (graphRank[i] == 0) graphRank[i]=(this.mMol.getAtomicNo$I(graphAtom[i]) == 151 ? 1 : this.mMol.getAtomicNo$I(graphAtom[i]) == 152 ? 1 : this.mMol.getAtomicNo$I(graphAtom[i])) << 2;
graphRank[i]+=(graphRank[graphParent[i]] << 16);
}
p$1.cipUpdateParentRanking$ZA$IA$IA$IA$IA$I.apply(this, [graphIsPseudo, graphRank, graphParent, graphAtom, levelStart, currentLevel]);
if (graphRank[1] != graphRank[2]) return (graphRank[1] > graphRank[2]);
if (currentLevel > 1) p$1.cipCompileRelativeRanks$IA$IA$IA$I.apply(this, [graphRank, graphParent, levelStart, currentLevel]);
++currentLevel;
}
var cipRank=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
var isotopDataFound=false;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (atomUsed[atom] && !this.mMol.isNaturalAbundance$I(atom) ) {
isotopDataFound=true;
break;
}}
if (isotopDataFound) {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) cipRank[atom]=this.mMol.isNaturalAbundance$I(atom) ? $I$(10).cRoundedMass[this.mMol.getAtomicNo$I(atom)] : this.mMol.getAtomMass$I(atom);

if (p$1.cipTryDistinguishBranches$ZA$IA$IA$IA$IA$IA$I.apply(this, [graphIsPseudo, graphRank, graphParent, graphAtom, cipRank, levelStart, currentLevel])) return (graphRank[1] > graphRank[2]);
}$I$(1).fill$IA$I(cipRank, 0);
var ezDataFound=false;
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (atomUsed[this.mMol.getBondAtom$I$I(0, bond)] || atomUsed[this.mMol.getBondAtom$I$I(1, bond)] ) {
if (this.mEZCIPParity[bond] == 1) {
cipRank[this.mMol.getBondAtom$I$I(0, bond)]=1;
cipRank[this.mMol.getBondAtom$I$I(1, bond)]=1;
ezDataFound=true;
} else if (this.mEZCIPParity[bond] == 2) {
cipRank[this.mMol.getBondAtom$I$I(0, bond)]=2;
cipRank[this.mMol.getBondAtom$I$I(1, bond)]=2;
ezDataFound=true;
}}}
if (ezDataFound && p$1.cipTryDistinguishBranches$ZA$IA$IA$IA$IA$IA$I.apply(this, [graphIsPseudo, graphRank, graphParent, graphAtom, cipRank, levelStart, currentLevel]) ) return (graphRank[1] > graphRank[2]);
$I$(1).fill$IA$I(cipRank, 0);
var rsDataFound=false;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (atomUsed[atom]) {
if (this.mTHCIPParity[atom] == 2) {
cipRank[atom]=1;
rsDataFound=true;
} else if (this.mTHCIPParity[atom] == 1) {
cipRank[atom]=2;
rsDataFound=true;
}}}
if (rsDataFound && p$1.cipTryDistinguishBranches$ZA$IA$IA$IA$IA$IA$I.apply(this, [graphIsPseudo, graphRank, graphParent, graphAtom, cipRank, levelStart, currentLevel]) ) return (graphRank[1] > graphRank[2]);
this.mCIPParityNoDistinctionProblem=true;
throw Clazz.new_(Clazz.load('Exception').c$$S,["no distinction applying CIP rules"]);
}, p$1);

Clazz.newMeth(C$, 'cipTryDistinguishBranches$ZA$IA$IA$IA$IA$IA$I',  function (graphIsPseudo, graphRank, graphParent, graphAtom, cipRank, levelStart, currentLevel) {
for (var level=1; level < currentLevel; level++) {
for (var i=levelStart[level]; i < levelStart[level + 1]; i++) graphRank[i]=cipRank[graphAtom[i]] + (graphRank[graphParent[i]] << 8);

p$1.cipUpdateParentRanking$ZA$IA$IA$IA$IA$I.apply(this, [graphIsPseudo, graphRank, graphParent, graphAtom, levelStart, level]);
if (graphRank[1] != graphRank[2]) return true;
if (level > 1) p$1.cipCompileRelativeRanks$IA$IA$IA$I.apply(this, [graphRank, graphParent, levelStart, level]);
}
return false;
}, p$1);

Clazz.newMeth(C$, 'resize$IA$I',  function (array, newSize) {
var copy=Clazz.array(Integer.TYPE, [newSize]);
System.arraycopy$O$I$O$I$I(array, 0, copy, 0, array.length);
return copy;
}, p$1);

Clazz.newMeth(C$, 'resize$ZA$I',  function (array, newSize) {
var copy=Clazz.array(Boolean.TYPE, [newSize]);
System.arraycopy$O$I$O$I$I(array, 0, copy, 0, array.length);
return copy;
}, p$1);

Clazz.newMeth(C$, 'cipUpdateParentRanking$ZA$IA$IA$IA$IA$I',  function (graphIsPseudo, graphRank, graphParent, graphAtom, levelStart, currentLevel) {
for (var level=currentLevel; level > 1; level--) {
var parentCount=levelStart[level] - levelStart[level - 1];
var rankObject=Clazz.array($I$(12), [parentCount]);
var baseIndex=levelStart[level];
for (var parent=0; parent < parentCount; parent++) {
var parentIndex=levelStart[level - 1] + parent;
var nextBaseIndex=baseIndex;
while (nextBaseIndex < levelStart[level + 1] && graphParent[nextBaseIndex] == parentIndex )++nextBaseIndex;

rankObject[parent]=Clazz.new_(P$.Canonizer$1RankObject.$init$,[this, null]);
rankObject[parent].parentIndex=parentIndex;
rankObject[parent].parentRank=graphRank[parentIndex];
rankObject[parent].parentHCount=graphIsPseudo[parentIndex] ? 0 : this.mMol.getPlainHydrogens$I(graphAtom[parentIndex]);
rankObject[parent].childRank=Clazz.array(Integer.TYPE, [nextBaseIndex - baseIndex]);
for (var i=baseIndex; i < nextBaseIndex; i++) rankObject[parent].childRank[i - baseIndex]=graphRank[i];

$I$(1).sort$IA(rankObject[parent].childRank);
baseIndex=nextBaseIndex;
}
var comparator=((P$.Canonizer$1||
(function(){/*a*/var C$=Clazz.newClass(P$, "Canonizer$1", function(){Clazz.newInstance(this, arguments[0],1,C$);}, null, 'java.util.Comparator', 1);

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

Clazz.newMeth(C$, ['compare$com_actelion_research_chem_Canonizer$1RankObject$com_actelion_research_chem_Canonizer$1RankObject','compare$O$O'],  function (r1, r2) {
if (r1.parentRank != r2.parentRank) return (r1.parentRank > r2.parentRank) ? 1 : -1;
var i1=r1.childRank.length;
var i2=r2.childRank.length;
var count=Math.min(i1, i2);
for (var i=0; i < count; i++) {
--i1;
--i2;
if (r1.childRank[i1] != r2.childRank[i2]) return (r1.childRank[i1] > r2.childRank[i2]) ? 1 : -1;
}
if (i1 != i2) return (i1 > i2) ? 1 : -1;
if (r1.parentHCount != r2.parentHCount) return (r1.parentHCount > r2.parentHCount) ? 1 : -1;
return 0;
});
})()
), Clazz.new_(P$.Canonizer$1.$init$,[this, null]));
$I$(1).sort$OA$java_util_Comparator(rankObject, comparator);
var consolidatedRank=1;
for (var parent=0; parent < parentCount; parent++) {
graphRank[rankObject[parent].parentIndex]=consolidatedRank;
if (parent != parentCount - 1 && comparator.compare$O$O(rankObject[parent], rankObject[parent + 1]) != 0 ) ++consolidatedRank;
}
}
}, p$1);

Clazz.newMeth(C$, 'cipCompileRelativeRanks$IA$IA$IA$I',  function (graphRank, graphParent, levelStart, currentLevel) {
var levelOffset=levelStart[currentLevel];
var count=levelStart[currentLevel + 1] - levelOffset;
var rankObject=Clazz.array($I$(13), [count]);
for (var i=0; i < count; i++) {
rankObject[i]=Clazz.new_(P$.Canonizer$2RankObject.$init$,[this, null]);
rankObject[i].rank=graphRank[i + levelOffset];
rankObject[i].parent=graphParent[i + levelOffset];
rankObject[i].index=i + levelOffset;
}
var comparator=((P$.Canonizer$2||
(function(){/*a*/var C$=Clazz.newClass(P$, "Canonizer$2", function(){Clazz.newInstance(this, arguments[0],1,C$);}, null, 'java.util.Comparator', 1);

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

Clazz.newMeth(C$, ['compare$com_actelion_research_chem_Canonizer$2RankObject$com_actelion_research_chem_Canonizer$2RankObject','compare$O$O'],  function (r1, r2) {
if (r1.rank != r2.rank) return (r1.rank > r2.rank) ? 1 : -1;
return 0;
});
})()
), Clazz.new_(P$.Canonizer$2.$init$,[this, null]));
for (var level=currentLevel; level > 1; level--) {
for (var i=0; i < count; i++) {
rankObject[i].rank+=(graphRank[rankObject[i].parent] << 16);
rankObject[i].parent=graphParent[rankObject[i].parent];
}
$I$(1).sort$OA$java_util_Comparator(rankObject, comparator);
var consolidatedRank=1;
for (var i=0; i < count; i++) {
graphRank[rankObject[i].index]=consolidatedRank;
if (i != count - 1 && comparator.compare$O$O(rankObject[i], rankObject[i + 1]) != 0 ) ++consolidatedRank;
}
}
}, p$1);

Clazz.newMeth(C$, 'getGraphAtoms$',  function () {
p$1.generateGraph.apply(this, []);
return this.mGraphAtom;
});

Clazz.newMeth(C$, 'getGraphIndexes$',  function () {
p$1.generateGraph.apply(this, []);
return this.mGraphIndex;
});
var $b$ = new Int8Array(1);
var $k$;
;
(function(){/*l*/var C$=Clazz.newClass(P$, "Canonizer$1RankObject", function(){
Clazz.newInstance(this, arguments[0],true,C$);
}, null, null, 2);

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['I',['parentIndex','parentRank','parentHCount'],'O',['childRank','int[]']]]

Clazz.newMeth(C$);
})()
;
(function(){/*l*/var C$=Clazz.newClass(P$, "Canonizer$2RankObject", function(){
Clazz.newInstance(this, arguments[0],true,C$);
}, null, null, 2);

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['I',['rank','parent','index']]]

Clazz.newMeth(C$);
})()
;
(function(){/*c*/var C$=Clazz.newClass(P$.Canonizer, "ESRGroup", function(){
Clazz.newInstance(this, arguments[0],true,C$);
}, null, 'Comparable');

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['O',['atomList','int[]','+rankList']]]

Clazz.newMeth(C$, 'c$$I$I',  function (type, group) {
;C$.$init$.apply(this);
var count=0;
for (var atom=0; atom < this.b$['com.actelion.research.chem.Canonizer'].mMol.getAtoms$(); atom++) if (this.b$['com.actelion.research.chem.Canonizer'].mTHESRType[atom] == type && this.b$['com.actelion.research.chem.Canonizer'].mTHESRGroup[atom] == group ) ++count;

this.atomList=Clazz.array(Integer.TYPE, [count]);
this.rankList=Clazz.array(Integer.TYPE, [count]);
count=0;
for (var atom=0; atom < this.b$['com.actelion.research.chem.Canonizer'].mMol.getAtoms$(); atom++) {
if (this.b$['com.actelion.research.chem.Canonizer'].mTHESRType[atom] == type && this.b$['com.actelion.research.chem.Canonizer'].mTHESRGroup[atom] == group ) {
this.atomList[count]=atom;
this.rankList[count++]=this.b$['com.actelion.research.chem.Canonizer'].mCanRank[atom];
}}
$I$(1).sort$IA(this.rankList);
}, 1);

Clazz.newMeth(C$, ['compareTo$com_actelion_research_chem_Canonizer_ESRGroup','compareTo$O'],  function (g) {
if (this.rankList.length != g.rankList.length) return this.rankList.length < g.rankList.length ? -1 : 1;
for (var i=0; i < this.rankList.length; i++) if (this.rankList[i] != g.rankList[i]) return this.rankList[i] < g.rankList[i] ? -1 : 1;

return 0;
});

Clazz.newMeth(C$);
})()

Clazz.newMeth(C$);
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-25 13:07:44 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
