(function(){var P$=Clazz.newPackage("com.actelion.research.chem"),p$1={},I$=[[0,'com.actelion.research.chem.Coordinates','java.util.Arrays','com.actelion.research.gui.generic.GenericRectangle']],I$0=I$[0],$I$=function(i,n){return((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "Molecule", null, null, 'java.io.Serializable');

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['Z',['mIsFragment','mIsRacemate','mProtectHydrogen'],'D',['mZoomRotationX','mZoomRotationY'],'I',['mMaxAtoms','mMaxBonds','mValidHelperArrays','mAllAtoms','mAllBonds','mChirality','mMoleculeColor'],'S',['mName'],'O',['mAtomicNo','int[]','+mAtomCharge','+mAtomMapNo','+mAtomMass','+mAtomFlags','mAtomQueryFeatures','long[]','mBondAtom','int[][]','mBondType','int[]','+mBondFlags','+mBondQueryFeatures','mCoordinates','com.actelion.research.chem.Coordinates[]','mAtomList','int[][]','mAtomCustomLabel','byte[][]','mOriginalAngle','double[]','+mOriginalDistance','mUserData','java.lang.Object']]
,['D',['sDefaultAVBL'],'O',['cAtomLabel','String[]','cRoundedMass','short[]','cDefaultAtomValences','byte[]','+cAminoAcidValences','cAtomValence','byte[][]','+cCommonOxidationState']]]

Clazz.newMeth(C$, 'getAtomicNoFromLabel$S',  function (atomLabel) {
return C$.getAtomicNoFromLabel$S$I(atomLabel, 321);
}, 1);

Clazz.newMeth(C$, 'getAtomicNoFromLabel$S$I',  function (atomLabel, allowedPseudoAtomGroups) {
if (((allowedPseudoAtomGroups & 256) != 0) && atomLabel.equals$O("?") ) return 0;
for (var i=1; i <= 128; i++) if (!atomLabel.equals$O("??") && atomLabel.equalsIgnoreCase$S(C$.cAtomLabel[i]) ) return i;

if ((allowedPseudoAtomGroups & 2) != 0) for (var i=129; i <= 144; i++) if (atomLabel.equalsIgnoreCase$S(C$.cAtomLabel[i])) return i;

if ((allowedPseudoAtomGroups & 4) != 0) for (var i=146; i <= 148; i++) if (atomLabel.equalsIgnoreCase$S(C$.cAtomLabel[i])) return i;

if ((allowedPseudoAtomGroups & 1) != 0) for (var i=151; i <= 152; i++) if (atomLabel.equalsIgnoreCase$S(C$.cAtomLabel[i])) return i;

if ((allowedPseudoAtomGroups & 32) != 0) if (atomLabel.equalsIgnoreCase$S(C$.cAtomLabel[153])) return 153;
if ((allowedPseudoAtomGroups & 8) != 0) if (atomLabel.equalsIgnoreCase$S(C$.cAtomLabel[154])) return 154;
if ((allowedPseudoAtomGroups & 16) != 0) if (atomLabel.equalsIgnoreCase$S(C$.cAtomLabel[145])) return 145;
if ((allowedPseudoAtomGroups & 128) != 0) if (atomLabel.equalsIgnoreCase$S(C$.cAtomLabel[159])) return 159;
if ((allowedPseudoAtomGroups & 64) != 0) for (var i=171; i <= 190; i++) if (atomLabel.equalsIgnoreCase$S(C$.cAtomLabel[i])) return i;

return 0;
}, 1);

Clazz.newMeth(C$, 'getAllowedValences$I',  function (atomicNo) {
return (atomicNo >= 0) && (atomicNo < C$.cAtomValence.length) && (C$.cAtomValence[atomicNo] != null )   ? C$.cAtomValence[atomicNo] : (atomicNo >= 171 && atomicNo <= 190 ) ? C$.cAminoAcidValences : C$.cDefaultAtomValences;
}, 1);

Clazz.newMeth(C$, 'getAngle$D$D$D$D',  function (x1, y1, x2, y2) {
var angle;
var xdiff=x2 - x1;
var ydiff=y2 - y1;
if (ydiff != 0 ) {
angle=Math.atan(xdiff / ydiff);
if (ydiff < 0 ) {
if (xdiff < 0 ) angle-=3.141592653589793;
 else angle+=3.141592653589793;
}} else angle=(xdiff > 0.0 ) ? 1.5707963267948966 : -1.5707963267948966;
return angle;
}, 1);

Clazz.newMeth(C$, 'getAngleDif$D$D',  function (angle1, angle2) {
var angleDif=angle1 - angle2;
while (angleDif < -3.141592653589793 )angleDif+=6.283185307179586;

while (angleDif > 3.141592653589793 )angleDif-=6.283185307179586;

return angleDif;
}, 1);

Clazz.newMeth(C$, 'bondTypeToOrder$I',  function (bondType) {
var simpleType=bondType & 127;
return (simpleType == 1 || simpleType == 64 ) ? 1 : simpleType == 2 ? 2 : simpleType == 4 ? 3 : simpleType == 8 ? 4 : simpleType == 16 ? 5 : 0;
}, 1);

Clazz.newMeth(C$, 'bondOrderToType$I$Z',  function (bondOrder, useCrossBond) {
return bondOrder == 0 ? 32 : bondOrder == 1 ? 1 : bondOrder == 2 ? (useCrossBond ? 386 : 2) : bondOrder == 3 ? 4 : bondOrder == 4 ? 8 : 16;
}, 1);

Clazz.newMeth(C$, 'c$',  function () {
;C$.$init$.apply(this);
this.mMaxAtoms=this.mMaxBonds=256;
p$1.init.apply(this, []);
}, 1);

Clazz.newMeth(C$, 'c$$I$I',  function (maxAtoms, maxBonds) {
;C$.$init$.apply(this);
this.mMaxAtoms=Math.max(1, maxAtoms);
this.mMaxBonds=Math.max(1, maxBonds);
p$1.init.apply(this, []);
}, 1);

Clazz.newMeth(C$, 'init',  function () {
this.mValidHelperArrays=0;
this.mAtomicNo=Clazz.array(Integer.TYPE, [this.mMaxAtoms]);
this.mAtomCharge=Clazz.array(Integer.TYPE, [this.mMaxAtoms]);
this.mAtomMapNo=Clazz.array(Integer.TYPE, [this.mMaxAtoms]);
this.mCoordinates=Clazz.array($I$(1), [this.mMaxAtoms]);
for (var i=0; i < this.mMaxAtoms; i++) this.mCoordinates[i]=Clazz.new_($I$(1,1));

this.mAtomMass=Clazz.array(Integer.TYPE, [this.mMaxAtoms]);
this.mAtomFlags=Clazz.array(Integer.TYPE, [this.mMaxAtoms]);
this.mAtomQueryFeatures=Clazz.array(Long.TYPE, [this.mMaxAtoms]);
this.mAtomList=null;
this.mAtomCustomLabel=null;
this.mBondAtom=Clazz.array(Integer.TYPE, [2, this.mMaxBonds]);
this.mBondType=Clazz.array(Integer.TYPE, [this.mMaxBonds]);
this.mBondFlags=Clazz.array(Integer.TYPE, [this.mMaxBonds]);
this.mBondQueryFeatures=Clazz.array(Integer.TYPE, [this.mMaxBonds]);
}, p$1);

Clazz.newMeth(C$, 'addAtom$D$D',  function (x, y) {
return this.addAtom$D$D$D(x, y, 0);
});

Clazz.newMeth(C$, 'addAtom$D$D$D',  function (x, y, z) {
var atom=this.addAtom$I(6);
this.mCoordinates[atom].set$D$D$D(x, y, z);
return atom;
});

Clazz.newMeth(C$, 'addAtom$S',  function (atomLabel) {
var atomicNo=C$.getAtomicNoFromLabel$S(atomLabel);
return (atomicNo == 0) ? -1 : this.addAtom$I(atomicNo);
});

Clazz.newMeth(C$, 'addAtom$I',  function (atomicNo) {
if (this.mAllAtoms >= this.mMaxAtoms) this.setMaxAtoms$I(this.mMaxAtoms * 2);
this.mAtomicNo[this.mAllAtoms]=0;
this.setAtomicNo$I$I(this.mAllAtoms, atomicNo);
this.mAtomCharge[this.mAllAtoms]=0;
this.mAtomFlags[this.mAllAtoms]=0;
this.mAtomQueryFeatures[this.mAllAtoms]=0;
this.mAtomMapNo[this.mAllAtoms]=0;
this.mCoordinates[this.mAllAtoms].set$D$D$D(0, 0, 0);
if (this.mAtomList != null ) this.mAtomList[this.mAllAtoms]=null;
if (this.mAtomCustomLabel != null ) this.mAtomCustomLabel[this.mAllAtoms]=null;
this.mValidHelperArrays=0;
return this.mAllAtoms++;
});

Clazz.newMeth(C$, 'suggestBondType$I$I',  function (atom1, atom2) {
return this.isMetalAtom$I(atom1) || this.isMetalAtom$I(atom2)  ? 32 : 1;
});

Clazz.newMeth(C$, 'addBond$I$I',  function (atom1, atom2) {
return this.addBond$I$I$I(atom1, atom2, this.suggestBondType$I$I(atom1, atom2));
});

Clazz.newMeth(C$, 'addBond$I$I$I',  function (atom1, atom2, type) {
if (atom1 == atom2) return -1;
for (var bnd=0; bnd < this.mAllBonds; bnd++) {
if (this.mBondAtom[0][bnd] == atom1 && this.mBondAtom[1][bnd] == atom2  || this.mBondAtom[0][bnd] == atom2 && this.mBondAtom[1][bnd] == atom1  ) {
if (this.mBondType[bnd] < type) this.mBondType[bnd]=type;
return bnd;
}}
if (this.mAllBonds >= this.mMaxBonds) this.setMaxBonds$I(this.mMaxBonds * 2);
this.mBondAtom[0][this.mAllBonds]=atom1;
this.mBondAtom[1][this.mAllBonds]=atom2;
this.mBondType[this.mAllBonds]=type;
this.mBondFlags[this.mAllBonds]=0;
this.mBondQueryFeatures[this.mAllBonds]=0;
this.mValidHelperArrays=0;
return this.mAllBonds++;
});

Clazz.newMeth(C$, 'addOrChangeAtom$D$D$I$I$I$I$S',  function (x, y, atomicNo, mass, abnormalValence, radical, customLabel) {
var atom=this.findAtom$D$D(x, y);
if (atom == -1) {
if (this.mAllAtoms >= this.mMaxAtoms) this.setMaxAtoms$I(this.mMaxAtoms * 2);
atom=this.addAtom$I(atomicNo);
this.mCoordinates[atom].set$D$D$D(x, y, 0);
this.mAtomMass[atom]=mass;
this.setAtomAbnormalValence$I$I(atom, abnormalValence);
this.setAtomRadical$I$I(atom, radical);
this.setAtomCustomLabel$I$S(atom, customLabel);
return true;
}var changed=this.changeAtom$I$I$I$I$I(atom, atomicNo, mass, abnormalValence, radical);
this.setAtomCustomLabel$I$S(atom, customLabel);
return changed;
});

Clazz.newMeth(C$, 'addOrChangeBond$I$I$I',  function (atm1, atm2, type) {
for (var bnd=0; bnd < this.mAllBonds; bnd++) {
if (this.mBondAtom[0][bnd] == atm1 && this.mBondAtom[1][bnd] == atm2  || this.mBondAtom[0][bnd] == atm2 && this.mBondAtom[1][bnd] == atm1  ) {
this.changeBond$I$I(bnd, type);
this.mValidHelperArrays=0;
return bnd;
}}
if (this.mAllBonds >= this.mMaxBonds) this.setMaxBonds$I(this.mMaxBonds * 2);
this.mBondAtom[0][this.mAllBonds]=atm1;
this.mBondAtom[1][this.mAllBonds]=atm2;
this.mBondType[this.mAllBonds]=type;
this.mBondFlags[this.mAllBonds]=0;
this.mBondQueryFeatures[this.mAllBonds]=0;
this.mValidHelperArrays=0;
return this.mAllBonds++;
});

Clazz.newMeth(C$, 'addRing$D$D$I$Z$D',  function (x, y, ringSize, aromatic, bondLength) {
while (this.mAllAtoms + ringSize > this.mMaxAtoms)this.setMaxAtoms$I(this.mMaxAtoms * 2);

while (this.mAllBonds + ringSize > this.mMaxBonds)this.setMaxBonds$I(this.mMaxBonds * 2);

var atom=this.findAtom$D$D(x, y);
if (atom != -1) return this.addRingToAtom$I$I$Z$D(atom, ringSize, aromatic, bondLength);
var bond=this.findBond$D$D(x, y);
if (bond != -1) return this.addRingToBond$I$I$Z$D(bond, ringSize, aromatic, bondLength);
atom=this.addAtom$D$D(x, y);
var cornerAngle=3.141592653589793 * (ringSize - 2) / ringSize;
p$1.polygon$I$I$I$Z$D$D$D.apply(this, [atom, ringSize, atom, aromatic, 0, 3.141592653589793 - cornerAngle, bondLength]);
this.mValidHelperArrays=0;
return true;
});

Clazz.newMeth(C$, 'addRingToAtom$I$I$Z$D',  function (atom, ringSize, aromatic, bondLength) {
if ((aromatic && this.getOccupiedValence$I(atom) > 1 ) || (!aromatic && this.getOccupiedValence$I(atom) > 2 ) ) return false;
var angles=0;
var angle=Clazz.array(Double.TYPE, [4]);
for (var i=0; i < this.mAllBonds; i++) {
for (var j=0; j < 2; j++) {
if (this.mBondAtom[j][i] == atom) {
if (angles == 2) {
angles=3;
break;
}angle[angles++]=this.getBondAngle$I$I(atom, this.mBondAtom[1 - j][i]);
}}
if (angles == 3) break;
}
if (angles == 3) return false;
var newAngle=(angles == 1) ? angle[0] + 3.141592653589793 : (Math.abs(angle[0] - angle[1]) > 3.141592653589793 ) ? (angle[0] + angle[1]) / 2 : (angle[0] + angle[1]) / 2 + 3.141592653589793;
var cornerAngle=(3.141592653589793 * (ringSize - 2)) / ringSize;
p$1.polygon$I$I$I$Z$D$D$D.apply(this, [atom, ringSize, atom, aromatic, newAngle - cornerAngle / 2, 3.141592653589793 - cornerAngle, bondLength]);
this.mValidHelperArrays=0;
return true;
});

Clazz.newMeth(C$, 'addRingToBond$I$I$Z$D',  function (bond, ringSize, aromatic, bondLength) {
var bondAtom=Clazz.array(Integer.TYPE, [2]);
var bondAngle=Clazz.array(Double.TYPE, [2]);
bondAtom[0]=this.mBondAtom[0][bond];
bondAtom[1]=this.mBondAtom[1][bond];
if (this.getOccupiedValence$I(bondAtom[0]) > 3) return false;
if (this.getOccupiedValence$I(bondAtom[1]) > 3) return false;
var angles=0;
var angle=Clazz.array(Double.TYPE, [4]);
for (var i=0; i < this.mAllBonds; i++) {
if (i == bond) continue;
for (var j=0; j < 2; j++) {
for (var k=0; k < 2; k++) {
if (this.mBondAtom[j][i] == bondAtom[k]) {
if (angles == 4) {
angles=5;
break;
}angle[angles++]=this.getBondAngle$I$I(bondAtom[k], this.mBondAtom[1 - j][i]);
}}
if (angles == 5) break;
}
if (angles == 5) break;
}
if (angles == 5) return false;
bondAngle[0]=this.getBondAngle$I$I(bondAtom[0], bondAtom[1]);
var atomNo;
if (bondAngle[0] < 0 ) {
bondAngle[1]=bondAngle[0] + 3.141592653589793;
atomNo=0;
} else {
bondAngle[1]=bondAngle[0];
bondAngle[0]=bondAngle[1] - 3.141592653589793;
atomNo=1;
}var side=0;
for (var i=0; i < angles; i++) {
if ((angle[i] > bondAngle[0] ) && (angle[i] < bondAngle[1] ) ) --side;
 else ++side;
}
atomNo=(side > 0) ? 1 - atomNo : atomNo;
var cornerAngle=(3.141592653589793 * (ringSize - 2)) / ringSize;
p$1.polygon$I$I$I$Z$D$D$D.apply(this, [bondAtom[atomNo], ringSize - 1, bondAtom[1 - atomNo], aromatic, bondAngle[(side > 0) ? 0 : 1] + 3.141592653589793 - cornerAngle, 3.141592653589793 - cornerAngle, bondLength]);
this.mValidHelperArrays=0;
return true;
});

Clazz.newMeth(C$, 'changeAtom$I$I$I$I$I',  function (atom, atomicNo, mass, abnormalValence, radical) {
if ((atomicNo == 1 || atomicNo == 151  || atomicNo == 152 ) && this.getOccupiedValence$I(atom) > 1 ) return false;
(this.mAtomQueryFeatures[$k$=atom]=Long.$and(this.mAtomQueryFeatures[$k$],((Long.$not(1)))));
if (this.mAtomList != null ) this.mAtomList[atom]=null;
if (this.mAtomCustomLabel != null ) this.mAtomCustomLabel[atom]=null;
if (atomicNo == this.mAtomicNo[atom] && mass == this.mAtomMass[atom]  && abnormalValence == this.getAtomAbnormalValence$I(atom)  && radical == this.getAtomRadical$I(atom) ) return false;
if (atomicNo == 151 || atomicNo == 152 ) {
mass=atomicNo - 149;
atomicNo=1;
}this.mAtomFlags[atom]&=(960);
this.mAtomicNo[atom]=atomicNo;
this.mAtomMass[atom]=mass;
this.mAtomCharge[atom]=0;
this.mAtomQueryFeatures[atom]=0;
this.setAtomAbnormalValence$I$I(atom, abnormalValence);
this.setAtomRadical$I$I(atom, radical);
this.removeMappingNo$I(this.mAtomMapNo[atom]);
this.mValidHelperArrays=0;
return true;
});

Clazz.newMeth(C$, 'changeAtomCharge$D$D$Z',  function (x, y, positive) {
var atom=this.findAtom$D$D(x, y);
return atom != -1 && this.changeAtomCharge$I$Z(atom, positive) ;
});

Clazz.newMeth(C$, 'changeAtomCharge$I$Z',  function (atom, positive) {
if (positive) {
if (this.mAtomCharge[atom] > 8) return false;
++this.mAtomCharge[atom];
} else {
if (this.mAtomCharge[atom] < -8) return false;
--this.mAtomCharge[atom];
}this.mValidHelperArrays=0;
return true;
});

Clazz.newMeth(C$, 'changeBond$I$I',  function (bnd, type) {
var bondWasChanged=false;
var oldType=this.mBondType[bnd];
if (type == 511) {
bondWasChanged=p$1.incrementBondOrder$I.apply(this, [bnd]);
} else if (this.validateBondType$I$I(bnd, type)) {
if (type == 257 || type == 129 ) {
var bondAtAtom1Qualifies=p$1.qualifiesAsStereoBond$I$I.apply(this, [bnd, this.mBondAtom[0][bnd]]);
var bondAtAtom2Qualifies=p$1.qualifiesAsStereoBond$I$I.apply(this, [bnd, this.mBondAtom[1][bnd]]);
if (type == oldType) {
if (bondAtAtom1Qualifies == bondAtAtom2Qualifies  || bondAtAtom2Qualifies ) {
var temp=this.mBondAtom[0][bnd];
this.mBondAtom[0][bnd]=this.mBondAtom[1][bnd];
this.mBondAtom[1][bnd]=temp;
bondWasChanged=true;
}} else {
if (!bondAtAtom1Qualifies && bondAtAtom2Qualifies ) {
var temp=this.mBondAtom[0][bnd];
this.mBondAtom[0][bnd]=this.mBondAtom[1][bnd];
this.mBondAtom[1][bnd]=temp;
}this.mBondType[bnd]=type;
bondWasChanged=true;
}} else {
this.mBondType[bnd]=type;
bondWasChanged=true;
}}if (bondWasChanged) {
this.mValidHelperArrays=(oldType & 127) == (type & 127) ? this.mValidHelperArrays & 7 : 0;
this.mBondQueryFeatures[bnd]=0;
}return bondWasChanged;
});

Clazz.newMeth(C$, 'qualifiesAsStereoBond$I$I',  function (bond, atom) {
if (this.getBondOrder$I(bond) != 1) return false;
if ((this.mAtomFlags[atom] & 3) != 0) return true;
for (var i=0; i < this.mAllBonds; i++) if (i != bond && this.mBondType[i] == 2  && ((this.mBondAtom[0][i] == atom && (this.mAtomFlags[this.mBondAtom[1][i]] & 3) != 0 ) || (this.mBondAtom[1][i] == atom && (this.mAtomFlags[this.mBondAtom[0][i]] & 3) != 0 ) ) ) return true;

for (var i=0; i < this.mAllBonds; i++) if (i != bond && this.mBondType[i] == 1  && (this.mBondAtom[0][i] == atom || this.mBondAtom[1][i] == atom )  && (this.mBondFlags[i] & 3) != 0 ) return true;

return false;
}, p$1);

Clazz.newMeth(C$, 'addMolecule$com_actelion_research_chem_Molecule',  function (mol) {
return this.addMolecule$com_actelion_research_chem_Molecule$I$I(mol, mol.mAllAtoms, mol.mAllBonds);
});

Clazz.newMeth(C$, 'addMolecule$com_actelion_research_chem_Molecule$I$I',  function (mol, atoms, bonds) {
this.mIsFragment=!!(this.mIsFragment|(mol.mIsFragment));
var atomMap=Clazz.array(Integer.TYPE, [mol.mAllAtoms]);
var esrGroupCountAND=this.renumberESRGroups$I(1);
var esrGroupCountOR=this.renumberESRGroups$I(2);
for (var atom=0; atom < atoms; atom++) {
atomMap[atom]=mol.copyAtom$com_actelion_research_chem_Molecule$I$I$I(this, atom, esrGroupCountAND, esrGroupCountOR);
}
for (var bond=0; bond < bonds; bond++) {
mol.copyBond$com_actelion_research_chem_Molecule$I$I$I$IA$Z(this, bond, esrGroupCountAND, esrGroupCountOR, atomMap, false);
}
this.mIsRacemate=(this.mIsRacemate && mol.mIsRacemate );
this.mChirality=0;
this.mValidHelperArrays=0;
return atomMap;
});

Clazz.newMeth(C$, 'addSubstituent$com_actelion_research_chem_Molecule$I',  function (substituent, connectionAtom) {
return this.addSubstituent$com_actelion_research_chem_Molecule$I$Z(substituent, connectionAtom, false);
});

Clazz.newMeth(C$, 'addSubstituent$com_actelion_research_chem_Molecule$I$Z',  function (substituent, connectionAtom, encodeRingClosuresInMapNo) {
var atomMap=Clazz.array(Integer.TYPE, [substituent.mAllAtoms]);
var esrGroupCountAND=this.renumberESRGroups$I(1);
var esrGroupCountOR=this.renumberESRGroups$I(2);
for (var atom=0; atom < substituent.mAllAtoms; atom++) {
if (substituent.getAtomicNo$I(atom) != 0) atomMap[atom]=substituent.copyAtom$com_actelion_research_chem_Molecule$I$I$I(this, atom, esrGroupCountAND, esrGroupCountOR);
 else if (encodeRingClosuresInMapNo && substituent.getAtomMapNo$I(atom) != 0 ) atomMap[atom]=substituent.getAtomMapNo$I(atom) - 1;
 else atomMap[atom]=connectionAtom;
}
for (var bond=0; bond < substituent.mAllBonds; bond++) {
substituent.copyBond$com_actelion_research_chem_Molecule$I$I$I$IA$Z(this, bond, esrGroupCountAND, esrGroupCountOR, atomMap, false);
}
this.mIsRacemate=(this.mIsRacemate && substituent.mIsRacemate );
this.mChirality=0;
this.mValidHelperArrays=0;
return atomMap;
});

Clazz.newMeth(C$, 'copyMolecule$com_actelion_research_chem_Molecule',  function (destMol) {
destMol.mAtomList=null;
destMol.mAtomCustomLabel=null;
destMol.mIsFragment=this.mIsFragment;
destMol.mAllAtoms=0;
for (var atom=0; atom < this.mAllAtoms; atom++) this.copyAtom$com_actelion_research_chem_Molecule$I$I$I(destMol, atom, 0, 0);

destMol.mAllBonds=0;
for (var bnd=0; bnd < this.mAllBonds; bnd++) this.copyBond$com_actelion_research_chem_Molecule$I$I$I$IA$Z(destMol, bnd, 0, 0, null, false);

this.copyMoleculeProperties$com_actelion_research_chem_Molecule(destMol);
});

Clazz.newMeth(C$, 'copyAtom$com_actelion_research_chem_Molecule$I$I$I',  function (destMol, sourceAtom, esrGroupOffsetAND, esrGroupOffsetOR) {
var destAtom=destMol.mAllAtoms;
if (destAtom >= destMol.mMaxAtoms) destMol.setMaxAtoms$I(destMol.mMaxAtoms * 2);
var esrType=this.getAtomESRType$I(sourceAtom);
var esrGroup=-1;
if (esrType == 1) {
if (esrGroupOffsetAND == -1) esrGroup=destMol.renumberESRGroups$I(esrType);
 else esrGroup=Math.min(31, esrGroupOffsetAND + this.getAtomESRGroup$I(sourceAtom));
} else if (esrType == 2) {
if (esrGroupOffsetOR == -1) esrGroup=destMol.renumberESRGroups$I(esrType);
 else esrGroup=Math.min(31, esrGroupOffsetOR + this.getAtomESRGroup$I(sourceAtom));
}destMol.mAtomicNo[destAtom]=this.mAtomicNo[sourceAtom];
destMol.mAtomCharge[destAtom]=this.mAtomCharge[sourceAtom];
destMol.mAtomMass[destAtom]=this.mAtomMass[sourceAtom];
destMol.mAtomFlags[destAtom]=this.mAtomFlags[sourceAtom];
destMol.mAtomQueryFeatures[destAtom]=destMol.mIsFragment ? this.mAtomQueryFeatures[sourceAtom] : 0;
destMol.mCoordinates[destAtom].set$com_actelion_research_chem_Coordinates(this.mCoordinates[sourceAtom]);
destMol.mAtomMapNo[destAtom]=this.mAtomMapNo[sourceAtom];
if (destMol.mAtomList != null ) destMol.mAtomList[destAtom]=null;
if (this.mAtomList != null  && this.mAtomList[sourceAtom] != null   && destMol.mIsFragment ) {
if (destMol.mAtomList == null ) destMol.mAtomList=Clazz.array(Integer.TYPE, [destMol.mAtomicNo.length, null]);
destMol.mAtomList[destAtom]=$I$(2).copyOf$IA$I(this.mAtomList[sourceAtom], this.mAtomList[sourceAtom].length);
}if (destMol.mAtomCustomLabel != null ) destMol.mAtomCustomLabel[destAtom]=null;
if (this.mAtomCustomLabel != null  && this.mAtomCustomLabel[sourceAtom] != null  ) {
if (destMol.mAtomCustomLabel == null ) destMol.mAtomCustomLabel=Clazz.array(Byte.TYPE, [destMol.mAtomicNo.length, null]);
destMol.mAtomCustomLabel[destAtom]=$I$(2).copyOf$BA$I(this.mAtomCustomLabel[sourceAtom], this.mAtomCustomLabel[sourceAtom].length);
}if (esrGroup != -1) {
destMol.mAtomFlags[destAtom]&=~32505856;
destMol.mAtomFlags[destAtom]|=(esrGroup << 20);
}++destMol.mAllAtoms;
destMol.mValidHelperArrays=0;
return destAtom;
});

Clazz.newMeth(C$, 'copyBond$com_actelion_research_chem_Molecule$I$I$I$IA$Z',  function (destMol, sourceBond, esrGroupOffsetAND, esrGroupOffsetOR, atomMap, useBondTypeDelocalized) {
return this.copyBond$com_actelion_research_chem_Molecule$I$I$I$I$I$Z(destMol, sourceBond, esrGroupOffsetAND, esrGroupOffsetOR, (atomMap == null ) ? this.mBondAtom[0][sourceBond] : atomMap[this.mBondAtom[0][sourceBond]], (atomMap == null ) ? this.mBondAtom[1][sourceBond] : atomMap[this.mBondAtom[1][sourceBond]], useBondTypeDelocalized);
});

Clazz.newMeth(C$, 'copyBond$com_actelion_research_chem_Molecule$I$I$I$I$I$Z',  function (destMol, sourceBond, esrGroupOffsetAND, esrGroupOffsetOR, destAtom1, destAtom2, useBondTypeDelocalized) {
var destBond=destMol.mAllBonds;
if (destBond >= destMol.mMaxBonds) destMol.setMaxBonds$I(destMol.mMaxBonds * 2);
var esrType=this.getBondESRType$I(sourceBond);
var esrGroup=-1;
if (esrType == 1) {
if (esrGroupOffsetAND == -1) esrGroup=destMol.renumberESRGroups$I(esrType);
 else esrGroup=Math.min(32, esrGroupOffsetAND + this.getBondESRGroup$I(sourceBond));
}if (esrType == 2) {
if (esrGroupOffsetOR == -1) esrGroup=destMol.renumberESRGroups$I(esrType);
 else esrGroup=Math.min(32, esrGroupOffsetOR + this.getBondESRGroup$I(sourceBond));
}destMol.mBondAtom[0][destBond]=destAtom1;
destMol.mBondAtom[1][destBond]=destAtom2;
var bondType=(useBondTypeDelocalized && this.isDelocalizedBond$I(sourceBond) ) ? 64 : this.mBondType[sourceBond];
destMol.mBondType[destBond]=bondType;
destMol.mBondFlags[destBond]=this.mBondFlags[sourceBond];
destMol.mBondQueryFeatures[destBond]=destMol.mIsFragment ? this.mBondQueryFeatures[sourceBond] : 0;
if (esrGroup != -1) {
destMol.mBondFlags[destBond]&=~31744;
destMol.mBondFlags[destBond]|=(esrGroup << 10);
}++destMol.mAllBonds;
destMol.mValidHelperArrays=0;
return destBond;
});

Clazz.newMeth(C$, 'copyMoleculeProperties$com_actelion_research_chem_Molecule',  function (destMol) {
destMol.mIsFragment=this.mIsFragment;
destMol.mIsRacemate=this.mIsRacemate;
destMol.mProtectHydrogen=this.mProtectHydrogen;
destMol.mChirality=this.mChirality;
destMol.mName=this.mName;
destMol.mValidHelperArrays=(this.mValidHelperArrays & (24));
});

Clazz.newMeth(C$, 'invalidateHelperArrays$I',  function (helperBits) {
this.mValidHelperArrays&=~helperBits;
});

Clazz.newMeth(C$, 'renumberESRGroups$I',  function (type) {
if (type == 0) return 0;
var groupUsed=null;
for (var atom=0; atom < this.mAllAtoms; atom++) {
if (this.getAtomESRType$I(atom) == type) {
if (groupUsed == null ) groupUsed=Clazz.array(Boolean.TYPE, [32]);
groupUsed[this.getAtomESRGroup$I(atom)]=true;
}}
for (var bond=0; bond < this.mAllBonds; bond++) {
if (this.getBondESRType$I(bond) == type) {
if (groupUsed == null ) groupUsed=Clazz.array(Boolean.TYPE, [32]);
groupUsed[this.getBondESRGroup$I(bond)]=true;
}}
var newIndex=0;
if (groupUsed != null ) {
var newGroup=Clazz.array(Integer.TYPE, [32]);
for (var i=0; i < 32; i++) if (groupUsed[i]) newGroup[i]=newIndex++;

for (var atom=0; atom < this.mAllAtoms; atom++) {
if (this.getAtomESRType$I(atom) == type) {
var group=newGroup[this.getAtomESRGroup$I(atom)];
this.mAtomFlags[atom]&=~32505856;
this.mAtomFlags[atom]|=(group << 20);
}}
for (var bond=0; bond < this.mAllBonds; bond++) {
if (this.getBondESRType$I(bond) == type) {
var group=newGroup[this.getBondESRGroup$I(bond)];
this.mBondFlags[bond]&=~31744;
this.mBondFlags[bond]|=(group << 10);
}}
}return newIndex;
});

Clazz.newMeth(C$, 'swapAtoms$I$I',  function (atom1, atom2) {
var tempInt=this.mAtomicNo[atom1];
this.mAtomicNo[atom1]=this.mAtomicNo[atom2];
this.mAtomicNo[atom2]=tempInt;
tempInt=this.mAtomCharge[atom1];
this.mAtomCharge[atom1]=this.mAtomCharge[atom2];
this.mAtomCharge[atom2]=tempInt;
tempInt=this.mAtomMass[atom1];
this.mAtomMass[atom1]=this.mAtomMass[atom2];
this.mAtomMass[atom2]=tempInt;
tempInt=this.mAtomFlags[atom1];
this.mAtomFlags[atom1]=this.mAtomFlags[atom2];
this.mAtomFlags[atom2]=tempInt;
var tempLong=this.mAtomQueryFeatures[atom1];
this.mAtomQueryFeatures[atom1]=this.mAtomQueryFeatures[atom2];
this.mAtomQueryFeatures[atom2]=tempLong;
tempInt=this.mAtomMapNo[atom1];
this.mAtomMapNo[atom1]=this.mAtomMapNo[atom2];
this.mAtomMapNo[atom2]=tempInt;
var tempCoords=this.mCoordinates[atom1];
this.mCoordinates[atom1]=this.mCoordinates[atom2];
this.mCoordinates[atom2]=tempCoords;
if (this.mAtomList != null ) {
var tempList=this.mAtomList[atom1];
this.mAtomList[atom1]=this.mAtomList[atom2];
this.mAtomList[atom2]=tempList;
}if (this.mAtomCustomLabel != null ) {
var tempList=this.mAtomCustomLabel[atom1];
this.mAtomCustomLabel[atom1]=this.mAtomCustomLabel[atom2];
this.mAtomCustomLabel[atom2]=tempList;
}for (var bond=0; bond < this.mAllBonds; bond++) {
for (var i=0; i < 2; i++) {
if (this.mBondAtom[i][bond] == atom1) this.mBondAtom[i][bond]=atom2;
 else if (this.mBondAtom[i][bond] == atom2) this.mBondAtom[i][bond]=atom1;
}
}
this.mValidHelperArrays=0;
});

Clazz.newMeth(C$, 'swapBonds$I$I',  function (bond1, bond2) {
var temp=this.mBondAtom[0][bond1];
this.mBondAtom[0][bond1]=this.mBondAtom[0][bond2];
this.mBondAtom[0][bond2]=temp;
temp=this.mBondAtom[1][bond1];
this.mBondAtom[1][bond1]=this.mBondAtom[1][bond2];
this.mBondAtom[1][bond2]=temp;
temp=this.mBondType[bond1];
this.mBondType[bond1]=this.mBondType[bond2];
this.mBondType[bond2]=temp;
temp=this.mBondFlags[bond1];
this.mBondFlags[bond1]=this.mBondFlags[bond2];
this.mBondFlags[bond2]=temp;
temp=this.mBondQueryFeatures[bond1];
this.mBondQueryFeatures[bond1]=this.mBondQueryFeatures[bond2];
this.mBondQueryFeatures[bond2]=temp;
this.mValidHelperArrays=0;
});

Clazz.newMeth(C$, 'deleteAtom$I',  function (atom) {
for (var bnd=0; bnd < this.mAllBonds; bnd++) {
for (var i=0; i < 2; i++) {
if (this.mBondAtom[i][bnd] == atom) {
this.mBondType[bnd]=512;
var bonds=0;
for (var j=0; j < this.mAllBonds; j++) {
if (j == bnd) continue;
if ((this.mBondAtom[0][j] == this.mBondAtom[1 - i][bnd]) || (this.mBondAtom[1][j] == this.mBondAtom[1 - i][bnd]) ) ++bonds;
}
if (bonds == 0) {
this.removeMappingNo$I(this.mAtomMapNo[this.mBondAtom[1 - i][bnd]]);
this.mAtomicNo[this.mBondAtom[1 - i][bnd]]=-1;
}}}
}
this.removeMappingNo$I(this.mAtomMapNo[atom]);
this.mAtomicNo[atom]=-1;
if (this.mAtomList != null ) this.mAtomList[atom]=null;
if (this.mAtomCustomLabel != null ) this.mAtomCustomLabel[atom]=null;
this.compressMolTable$();
this.mValidHelperArrays=0;
});

Clazz.newMeth(C$, 'deleteAtomOrBond$D$D',  function (x, y) {
var atom=this.findAtom$D$D(x, y);
if (atom != -1) {
if ((this.mAtomFlags[atom] & 512) != 0) this.deleteSelectedAtoms$();
 else this.deleteAtom$I(atom);
this.mValidHelperArrays=0;
return true;
}var bnd=this.findBond$D$D(x, y);
if (bnd != -1) {
if (((this.mAtomFlags[this.mBondAtom[0][bnd]] & this.mAtomFlags[this.mBondAtom[1][bnd]]) & 512) != 0) this.deleteSelectedAtoms$();
 else this.deleteBondAndSurrounding$I(bnd);
this.mValidHelperArrays=0;
return true;
}return false;
});

Clazz.newMeth(C$, 'deleteBond$I',  function (bond) {
this.mBondType[bond]=512;
this.compressMolTable$();
this.mValidHelperArrays=0;
});

Clazz.newMeth(C$, 'deleteBondAndSurrounding$I',  function (bond) {
for (var i=0; i < 2; i++) {
var bonds=0;
for (var j=0; j < this.mAllBonds; j++) {
if (j == bond) continue;
if ((this.mBondAtom[0][j] == this.mBondAtom[i][bond]) || (this.mBondAtom[1][j] == this.mBondAtom[i][bond]) ) ++bonds;
}
if (bonds == 0) {
this.removeMappingNo$I(this.mAtomMapNo[this.mBondAtom[i][bond]]);
this.mAtomicNo[this.mBondAtom[i][bond]]=-1;
}}
this.mBondType[bond]=512;
this.compressMolTable$();
this.mValidHelperArrays=0;
});

Clazz.newMeth(C$, 'deleteAtoms$ZA',  function (deleteAtom) {
var found=false;
for (var atom=0; atom < this.mAllAtoms; atom++) {
if (deleteAtom[atom]) {
this.markAtomForDeletion$I(atom);
found=true;
}}
if (!found) return null;
return this.deleteMarkedAtomsAndBonds$();
});

Clazz.newMeth(C$, 'deleteAtoms$IA',  function (atomList) {
if (atomList.length == 0) return null;
for (var atom, $atom = 0, $$atom = atomList; $atom<$$atom.length&&((atom=($$atom[$atom])),1);$atom++) this.markAtomForDeletion$I(atom);

return this.deleteMarkedAtomsAndBonds$();
});

Clazz.newMeth(C$, 'getDeleteAtomsBondMap$ZA',  function (deleteAtom) {
var deleteBond=Clazz.array(Boolean.TYPE, [this.mAllBonds]);
for (var bond=0; bond < this.mAllBonds; bond++) if (deleteAtom[this.mBondAtom[0][bond]] || deleteAtom[this.mBondAtom[1][bond]] ) deleteBond[bond]=true;

var bondDest=0;
var bondMap=Clazz.array(Integer.TYPE, [this.mAllBonds]);
for (var bnd=0; bnd < this.mAllBonds; bnd++) bondMap[bnd]=deleteBond[bnd] ? -1 : bondDest++;

return bondMap;
});

Clazz.newMeth(C$, 'getDeleteAtomsBondMap$IA',  function (atomList) {
var deleteAtom=Clazz.array(Boolean.TYPE, [this.mAllAtoms]);
for (var atom, $atom = 0, $$atom = atomList; $atom<$$atom.length&&((atom=($$atom[$atom])),1);$atom++) deleteAtom[atom]=true;

return this.getDeleteAtomsBondMap$ZA(deleteAtom);
});

Clazz.newMeth(C$, 'deleteSelectedAtoms$',  function () {
var found=false;
for (var atom=0; atom < this.mAllAtoms; atom++) {
if ((this.mAtomFlags[atom] & 512) != 0) {
this.markAtomForDeletion$I(atom);
found=true;
}}
return found && this.deleteMarkedAtomsAndBonds$() != null  ;
});

Clazz.newMeth(C$, 'markAtomForDeletion$I',  function (atom) {
this.mAtomicNo[atom]=-1;
});

Clazz.newMeth(C$, 'markBondForDeletion$I',  function (bond) {
this.mBondType[bond]=512;
});

Clazz.newMeth(C$, 'isAtomMarkedForDeletion$I',  function (atom) {
return (this.mAtomicNo[atom] == -1);
});

Clazz.newMeth(C$, 'isBondMarkedForDeletion$I',  function (bond) {
return (this.mBondType[bond] == 512);
});

Clazz.newMeth(C$, 'deleteMarkedAtomsAndBonds$',  function () {
var found=false;
for (var atom=0; atom < this.mAllAtoms; atom++) {
if (this.mAtomicNo[atom] == -1) {
found=true;
this.removeMappingNo$I(this.mAtomMapNo[atom]);
}}
for (var bond=0; bond < this.mAllBonds; bond++) {
if (this.mBondType[bond] == 512) {
found=true;
} else if (this.mAtomicNo[this.mBondAtom[0][bond]] == -1 || this.mAtomicNo[this.mBondAtom[1][bond]] == -1 ) {
this.mBondType[bond]=512;
found=true;
}}
if (found) {
this.mValidHelperArrays=0;
return this.compressMolTable$();
}return null;
});

Clazz.newMeth(C$, 'deleteMolecule$',  function () {
this.clear$();
});

Clazz.newMeth(C$, 'clear$',  function () {
this.mAllAtoms=0;
this.mAllBonds=0;
this.mIsFragment=false;
this.mIsRacemate=false;
this.mChirality=0;
this.mAtomList=null;
this.mAtomCustomLabel=null;
this.mName=null;
this.mValidHelperArrays=0;
});

Clazz.newMeth(C$, 'removeAtomSelection$',  function () {
for (var i=0; i < this.mAllAtoms; i++) this.mAtomFlags[i]&=~512;

});

Clazz.newMeth(C$, 'removeAtomColors$',  function () {
for (var i=0; i < this.mAllAtoms; i++) this.mAtomFlags[i]&=~448;

});

Clazz.newMeth(C$, 'removeAtomCustomLabels$',  function () {
this.mAtomCustomLabel=null;
});

Clazz.newMeth(C$, 'removeAtomMarkers$',  function () {
for (var i=0; i < this.mAllAtoms; i++) this.mAtomFlags[i]&=~131072;

});

Clazz.newMeth(C$, 'removeBondHiliting$',  function () {
for (var i=0; i < this.mAllBonds; i++) this.mBondFlags[i]&=~(98304);

});

Clazz.newMeth(C$, 'findAtom$D$D',  function (pickx, picky) {
var foundAtom=-1;
var avbl=this.getAverageBondLength$();
var foundDistanceSquare=1.7976931348623157E308;
var maxDistanceSquare=avbl * avbl / 12.0;
for (var atom=0; atom < this.mAllAtoms; atom++) {
var x=this.mCoordinates[atom].x;
var y=this.mCoordinates[atom].y;
var distanceSquare=(pickx - x) * (pickx - x) + (picky - y) * (picky - y);
if (distanceSquare < maxDistanceSquare  && distanceSquare < foundDistanceSquare  ) {
foundDistanceSquare=distanceSquare;
foundAtom=atom;
}}
return foundAtom;
});

Clazz.newMeth(C$, 'findBond$D$D',  function (pickx, picky) {
var foundBond=-1;
var maxDistance=this.getAverageBondLength$();
var foundDistance=1.7976931348623157E308;
for (var bond=0; bond < this.mAllBonds; bond++) {
var x1=this.mCoordinates[this.mBondAtom[0][bond]].x;
var y1=this.mCoordinates[this.mBondAtom[0][bond]].y;
var x2=this.mCoordinates[this.mBondAtom[1][bond]].x;
var y2=this.mCoordinates[this.mBondAtom[1][bond]].y;
var dx=x2 - x1;
var dy=y2 - y1;
var bondLength=Math.sqrt((dx * dx + dy * dy));
var centralX=(x1 + x2) / 2.0;
var centralY=(y1 + y2) / 2.0;
dx=pickx - centralX;
dy=picky - centralY;
if (Math.sqrt(dx * dx + dy * dy) > bondLength / 2.0 ) continue;
var distance;
if (x2 == x1 ) distance=Math.abs(x1 - pickx);
 else {
var constA=(y2 - y1) / (x1 - x2);
var constC=-constA * x1 - y1;
distance=Math.abs((constA * pickx + picky + constC) / Math.sqrt(constA * constA + 1));
}if (distance < maxDistance  && distance < foundDistance  ) {
foundDistance=distance;
foundBond=bond;
}}
return foundBond;
});

Clazz.newMeth(C$, 'getAllAtoms$',  function () {
return this.mAllAtoms;
});

Clazz.newMeth(C$, 'getAllBonds$',  function () {
return this.mAllBonds;
});

Clazz.newMeth(C$, 'getAtomAbnormalValence$I',  function (atom) {
return ((this.mAtomFlags[atom] & 2013265920) >>> 27) - 1;
});

Clazz.newMeth(C$, 'getAtomCharge$I',  function (atom) {
return this.mAtomCharge[atom];
});

Clazz.newMeth(C$, 'getAtomCIPParity$I',  function (atom) {
return (this.mAtomFlags[atom] & 49152) >> 14;
});

Clazz.newMeth(C$, 'getAtomColor$I',  function (atom) {
return this.mAtomFlags[atom] & 448;
});

Clazz.newMeth(C$, 'getAtomCoordinates$',  function () {
return this.mCoordinates;
});

Clazz.newMeth(C$, 'getAtomESRGroup$I',  function (atom) {
if (this.getAtomESRType$I(atom) != 1 && this.getAtomESRType$I(atom) != 2 ) return -1;
 else return (this.mAtomFlags[atom] & 32505856) >> 20;
});

Clazz.newMeth(C$, 'getAtomESRType$I',  function (atom) {
return (this.mAtomFlags[atom] & 786432) >> 18;
});

Clazz.newMeth(C$, 'getAtomicNo$I',  function (atom) {
return this.mAtomicNo[atom];
});

Clazz.newMeth(C$, 'getAtomCustomLabel$I',  function (atom) {
return (this.mAtomCustomLabel == null ) ? null : (this.mAtomCustomLabel[atom] == null ) ? null :  String.instantialize(this.mAtomCustomLabel[atom]);
});

Clazz.newMeth(C$, 'getAtomCustomLabelBytes$I',  function (atom) {
return (this.mAtomCustomLabel == null ) ? null : this.mAtomCustomLabel[atom];
});

Clazz.newMeth(C$, 'getAtomLabel$I',  function (atom) {
return C$.cAtomLabel[this.mAtomicNo[atom]];
});

Clazz.newMeth(C$, 'getAtomList$I',  function (atom) {
return (this.mAtomList == null ) ? null : this.mAtomList[atom];
});

Clazz.newMeth(C$, 'getAtomListString$I',  function (atom) {
if (this.mAtomList == null  || this.mAtomList[atom] == null  ) return (Long.$ne((Long.$and(this.mAtomQueryFeatures[atom],1)),0 )) ? "" : C$.cAtomLabel[this.mAtomicNo[atom]];
var listString="";
for (var i=0; i < this.mAtomList[atom].length; i++) {
if (i > 0) listString=listString.concat$S(",");
var atomicNo=this.mAtomList[atom][i];
listString=listString.concat$S(C$.cAtomLabel[atomicNo]);
}
return listString;
});

Clazz.newMeth(C$, 'getAtomMapNo$I',  function (atom) {
return Math.abs(this.mAtomMapNo[atom]);
});

Clazz.newMeth(C$, 'getAtomMass$I',  function (atom) {
return this.mAtomMass[atom];
});

Clazz.newMeth(C$, 'getAtomParity$I',  function (atom) {
return this.mAtomFlags[atom] & 3;
});

Clazz.newMeth(C$, 'getAtomQueryFeatures$I',  function (atom) {
return this.mAtomQueryFeatures[atom];
});

Clazz.newMeth(C$, 'getAtomRadical$I',  function (atom) {
return this.mAtomFlags[atom] & 48;
});

Clazz.newMeth(C$, 'getCoordinates$I',  function (atom) {
return this.mCoordinates[atom];
});

Clazz.newMeth(C$, 'getAtomX$I',  function (atom) {
return this.mCoordinates[atom].x;
});

Clazz.newMeth(C$, 'getAtomY$I',  function (atom) {
return this.mCoordinates[atom].y;
});

Clazz.newMeth(C$, 'getAtomZ$I',  function (atom) {
return this.mCoordinates[atom].z;
});

Clazz.newMeth(C$, 'getBounds$com_actelion_research_gui_generic_GenericRectangle',  function (r) {
if (this.mAllAtoms == 0) return null;
var x1=this.mCoordinates[0].x;
var y1=this.mCoordinates[0].y;
var x2=this.mCoordinates[0].x;
var y2=this.mCoordinates[0].y;
for (var atom=1; atom < this.mAllAtoms; atom++) {
if (x1 > this.mCoordinates[atom].x ) x1=this.mCoordinates[atom].x;
 else if (x2 < this.mCoordinates[atom].x ) x2=this.mCoordinates[atom].x;
if (y1 > this.mCoordinates[atom].y ) y1=this.mCoordinates[atom].y;
 else if (y2 < this.mCoordinates[atom].y ) y2=this.mCoordinates[atom].y;
}
if (r == null ) {
r=Clazz.new_($I$(3,1).c$$D$D$D$D,[x1, y1, x2 - x1, y2 - y1]);
} else {
r.x=x1;
r.y=y1;
r.width=x2 - x1;
r.height=y2 - y1;
}return r;
});

Clazz.newMeth(C$, 'getDefaultAverageBondLength$',  function () {
return C$.sDefaultAVBL;
}, 1);

Clazz.newMeth(C$, 'setDefaultAverageBondLength$D',  function (defaultAVBL) {
C$.sDefaultAVBL=defaultAVBL;
}, 1);

Clazz.newMeth(C$, 'getAverageBondLength$',  function () {
return this.getAverageBondLength$I$I$D(this.mAllAtoms, this.mAllBonds, C$.sDefaultAVBL);
});

Clazz.newMeth(C$, 'getAverageBondLength$D',  function (defaultBondLength) {
return this.getAverageBondLength$I$I$D(this.mAllAtoms, this.mAllBonds, defaultBondLength);
});

Clazz.newMeth(C$, 'getAverageBondLength$I$I',  function (atoms, bonds) {
return this.getAverageBondLength$I$I$D(atoms, bonds, C$.sDefaultAVBL);
});

Clazz.newMeth(C$, 'getAverageBondLength$I$I$D',  function (atoms, bonds, defaultBondLength) {
return this.getAverageBondLength$I$I$D$com_actelion_research_chem_CoordinatesA(atoms, bonds, defaultBondLength, this.mCoordinates);
});

Clazz.newMeth(C$, 'getAverageBondLength$I$I$D$com_actelion_research_chem_CoordinatesA',  function (atoms, bonds, defaultBondLength, coords) {
var considerMetalBonds=false;
var consideredBonds=0;
for (var bond=0; bond < bonds; bond++) if (this.mBondType[bond] != 32 && (this.mBondQueryFeatures[bond] & 130560) == 0 ) ++consideredBonds;

if (consideredBonds == 0) {
for (var bond=0; bond < bonds; bond++) if ((this.mBondQueryFeatures[bond] & 130560) == 0) ++consideredBonds;

considerMetalBonds=true;
}if (consideredBonds == 0) {
if (atoms < 2) return defaultBondLength;
var lowDistance=1.7976931348623157E308;
for (var atom1=1; atom1 < atoms; atom1++) {
for (var atom2=0; atom2 < atom1; atom2++) {
var distance=coords[atom1].distance$com_actelion_research_chem_Coordinates(coords[atom2]);
if (distance > 0  && distance < lowDistance  ) lowDistance=distance;
}
}
return (lowDistance != 1.7976931348623157E308 ) ? 0.6 * lowDistance : defaultBondLength;
}var avblSum=0.0;
for (var bond=0; bond < bonds; bond++) {
if ((considerMetalBonds || this.mBondType[bond] != 32 ) && (this.mBondQueryFeatures[bond] & 130560) == 0 ) avblSum+=coords[this.mBondAtom[1][bond]].distance$com_actelion_research_chem_Coordinates(coords[this.mBondAtom[0][bond]]);
}
return avblSum / consideredBonds;
});

Clazz.newMeth(C$, 'getBondAngle$I$I',  function (atom1, atom2) {
return C$.getAngle$D$D$D$D(this.mCoordinates[atom1].x, this.mCoordinates[atom1].y, this.mCoordinates[atom2].x, this.mCoordinates[atom2].y);
});

Clazz.newMeth(C$, 'calculateTorsion$IA',  function (atom) {
var c1=this.mCoordinates[atom[0]];
var c2=this.mCoordinates[atom[1]];
var c3=this.mCoordinates[atom[2]];
var c4=this.mCoordinates[atom[3]];
var v1=c2.subC$com_actelion_research_chem_Coordinates(c1);
var v2=c3.subC$com_actelion_research_chem_Coordinates(c2);
var v3=c4.subC$com_actelion_research_chem_Coordinates(c3);
var n1=v1.cross$com_actelion_research_chem_Coordinates(v2);
var n2=v2.cross$com_actelion_research_chem_Coordinates(v3);
return -Math.atan2(v2.getLength$() * v1.dot$com_actelion_research_chem_Coordinates(n2), n1.dot$com_actelion_research_chem_Coordinates(n2));
});

Clazz.newMeth(C$, 'center$',  function () {
var cog=Clazz.new_($I$(1,1));
for (var atom=0; atom < this.mAllAtoms; atom++) cog.add$com_actelion_research_chem_Coordinates(this.mCoordinates[atom]);

cog.scale$D(1.0 / this.mAllAtoms);
for (var atom=0; atom < this.mAllAtoms; atom++) this.mCoordinates[atom].sub$com_actelion_research_chem_Coordinates(cog);

});

Clazz.newMeth(C$, 'translate$D$D$D',  function (dx, dy, dz) {
for (var atom=0; atom < this.mAllAtoms; atom++) this.mCoordinates[atom].add$D$D$D(dx, dy, dz);

});

Clazz.newMeth(C$, 'getBondAtom$I$I',  function (no, bond) {
return this.mBondAtom[no][bond];
});

Clazz.newMeth(C$, 'getBondCIPParity$I',  function (bond) {
return (this.mBondFlags[bond] & 48) >> 4;
});

Clazz.newMeth(C$, 'getBondESRGroup$I',  function (bond) {
if (this.getBondESRType$I(bond) != 1 && this.getBondESRType$I(bond) != 2 ) return -1;
 else return (this.mBondFlags[bond] & 31744) >> 10;
});

Clazz.newMeth(C$, 'getBondESRType$I',  function (bond) {
return (this.mBondFlags[bond] & 768) >> 8;
});

Clazz.newMeth(C$, 'getBondLength$I',  function (bond) {
var atom1=this.mBondAtom[0][bond];
var atom2=this.mBondAtom[1][bond];
var xdif=this.mCoordinates[atom2].x - this.mCoordinates[atom1].x;
var ydif=this.mCoordinates[atom2].y - this.mCoordinates[atom1].y;
return Math.sqrt(xdif * xdif + ydif * ydif);
});

Clazz.newMeth(C$, 'getBondOrder$I',  function (bond) {
switch (this.mBondType[bond] & 127) {
case 1:
case 64:
return 1;
case 2:
return 2;
case 4:
return 3;
case 8:
return 4;
case 16:
return 5;
default:
return 0;
}
});

Clazz.newMeth(C$, 'getBondParity$I',  function (bnd) {
return this.mBondFlags[bnd] & 3;
});

Clazz.newMeth(C$, 'getBondQueryFeatures$I',  function (bnd) {
return this.mBondQueryFeatures[bnd];
});

Clazz.newMeth(C$, 'isBondBridge$I',  function (bond) {
return (this.mBondQueryFeatures[bond] & 130560) != 0;
});

Clazz.newMeth(C$, 'getBondBridgeMinSize$I',  function (bond) {
return (this.mBondQueryFeatures[bond] & 7680) >> 9;
});

Clazz.newMeth(C$, 'getBondBridgeMaxSize$I',  function (bond) {
return ((this.mBondQueryFeatures[bond] & 7680) >> 9) + ((this.mBondQueryFeatures[bond] & 122880) >> 13);
});

Clazz.newMeth(C$, 'getBondType$I',  function (bond) {
return this.mBondType[bond];
});

Clazz.newMeth(C$, 'getBondTypeSimple$I',  function (bond) {
return this.mBondType[bond] & 127;
});

Clazz.newMeth(C$, 'getChirality$',  function () {
return this.mChirality;
});

Clazz.newMeth(C$, 'getMaxAtoms$',  function () {
return this.mMaxAtoms;
});

Clazz.newMeth(C$, 'copyOf$com_actelion_research_chem_CoordinatesA$I',  function (original, newLength) {
var copy=Clazz.array($I$(1), [newLength]);
for (var i=0; i < original.length; i++) if (original[i] != null ) copy[i]=Clazz.new_($I$(1,1).c$$com_actelion_research_chem_Coordinates,[original[i]]);

return copy;
}, 1);

Clazz.newMeth(C$, 'copyOf$IA$I',  function (original, newLength) {
var copy=Clazz.array(Integer.TYPE, [newLength]);
System.arraycopy$O$I$O$I$I(original, 0, copy, 0, Math.min(original.length, newLength));
return copy;
}, 1);

Clazz.newMeth(C$, 'copyOf$JA$I',  function (original, newLength) {
var copy=Clazz.array(Long.TYPE, [newLength]);
System.arraycopy$O$I$O$I$I(original, 0, copy, 0, Math.min(original.length, newLength));
return copy;
}, 1);

Clazz.newMeth(C$, 'copyOf$IAA$I',  function (original, newLength) {
var copy=Clazz.array(Integer.TYPE, [newLength, null]);
for (var i=0; i < original.length; i++) {
if (original[i] != null ) {
copy[i]=Clazz.array(Integer.TYPE, [original[i].length]);
System.arraycopy$O$I$O$I$I(original[i], 0, copy[i], 0, original[i].length);
}}
return copy;
}, 1);

Clazz.newMeth(C$, 'copyOf$BAA$I',  function (original, newLength) {
var copy=Clazz.array(Byte.TYPE, [newLength, null]);
for (var i=0; i < original.length; i++) {
if (original[i] != null ) {
copy[i]=Clazz.array(Byte.TYPE, [original[i].length]);
System.arraycopy$O$I$O$I$I(original[i], 0, copy[i], 0, original[i].length);
}}
return copy;
}, 1);

Clazz.newMeth(C$, 'setMaxAtoms$I',  function (v) {
this.mAtomicNo=C$.copyOf$IA$I(this.mAtomicNo, v);
this.mAtomCharge=C$.copyOf$IA$I(this.mAtomCharge, v);
this.mAtomMapNo=C$.copyOf$IA$I(this.mAtomMapNo, v);
var orig=this.mCoordinates.length;
this.mCoordinates=C$.copyOf$com_actelion_research_chem_CoordinatesA$I(this.mCoordinates, v);
for (var i=orig; i < v; i++) this.mCoordinates[i]=Clazz.new_($I$(1,1));

this.mAtomMass=C$.copyOf$IA$I(this.mAtomMass, v);
this.mAtomFlags=C$.copyOf$IA$I(this.mAtomFlags, v);
this.mAtomQueryFeatures=C$.copyOf$JA$I(this.mAtomQueryFeatures, v);
if (this.mAtomList != null ) this.mAtomList=C$.copyOf$IAA$I(this.mAtomList, v);
if (this.mAtomCustomLabel != null ) this.mAtomCustomLabel=C$.copyOf$BAA$I(this.mAtomCustomLabel, v);
this.mMaxAtoms=v;
});

Clazz.newMeth(C$, 'getMaxBonds$',  function () {
return this.mMaxBonds;
});

Clazz.newMeth(C$, 'setMaxBonds$I',  function (v) {
this.mBondAtom[0]=C$.copyOf$IA$I(this.mBondAtom[0], v);
this.mBondAtom[1]=C$.copyOf$IA$I(this.mBondAtom[1], v);
this.mBondType=C$.copyOf$IA$I(this.mBondType, v);
this.mBondFlags=C$.copyOf$IA$I(this.mBondFlags, v);
this.mBondQueryFeatures=C$.copyOf$IA$I(this.mBondQueryFeatures, v);
this.mMaxBonds=v;
});

Clazz.newMeth(C$, 'getMoleculeColor$',  function () {
return this.mMoleculeColor;
});

Clazz.newMeth(C$, 'setMoleculeColor$I',  function (color) {
this.mMoleculeColor=color;
});

Clazz.newMeth(C$, 'getName$',  function () {
return this.mName;
});

Clazz.newMeth(C$, 'getStereoProblem$I',  function (atom) {
return ((this.mAtomFlags[atom] & 65536) != 0);
});

Clazz.newMeth(C$, 'isAtomConfigurationUnknown$I',  function (atom) {
return ((this.mAtomFlags[atom] & 33554432) != 0);
});

Clazz.newMeth(C$, 'isAtomParityPseudo$I',  function (atom) {
return ((this.mAtomFlags[atom] & 4) != 0);
});

Clazz.newMeth(C$, 'isAtomStereoCenter$I',  function (atom) {
return ((this.mAtomFlags[atom] & 67108864) != 0);
});

Clazz.newMeth(C$, 'isBondParityPseudo$I',  function (bond) {
return ((this.mBondFlags[bond] & 4) != 0);
});

Clazz.newMeth(C$, 'isBondParityUnknownOrNone$I',  function (bond) {
return ((this.mBondFlags[bond] & 131072) != 0);
});

Clazz.newMeth(C$, 'isFragment$',  function () {
return this.mIsFragment;
});

Clazz.newMeth(C$, 'isDelocalizedBond$I',  function (bond) {
return this.mBondType[bond] == 64;
});

Clazz.newMeth(C$, 'is3D$',  function () {
for (var atom=0; atom < this.mAllAtoms; atom++) if (this.mCoordinates[atom].z != 0.0 ) return true;

return false;
});

Clazz.newMeth(C$, 'isNaturalAbundance$I',  function (atom) {
return (this.mAtomMass[atom] == 0);
});

Clazz.newMeth(C$, 'isPurelyOrganic$',  function () {
for (var atom=0; atom < this.mAllAtoms; atom++) {
switch (this.mAtomicNo[atom]) {
case 1:
case 5:
case 6:
case 7:
case 8:
case 9:
case 14:
case 15:
case 16:
case 17:
case 33:
case 34:
case 35:
case 52:
case 53:
continue;
default:
return false;
}
}
return true;
});

Clazz.newMeth(C$, 'isSelectedAtom$I',  function (atom) {
return (this.mAtomFlags[atom] & 512) != 0;
});

Clazz.newMeth(C$, 'isMarkedAtom$I',  function (atom) {
return (this.mAtomFlags[atom] & 131072) != 0;
});

Clazz.newMeth(C$, 'isBondBackgroundHilited$I',  function (bond) {
return (this.mBondFlags[bond] & 32768) != 0;
});

Clazz.newMeth(C$, 'isBondForegroundHilited$I',  function (bond) {
return (this.mBondFlags[bond] & 65536) != 0;
});

Clazz.newMeth(C$, 'isSelectedBond$I',  function (bond) {
return (this.mAtomFlags[this.mBondAtom[0][bond]] & this.mAtomFlags[this.mBondAtom[1][bond]] & 512 ) != 0;
});

Clazz.newMeth(C$, 'isAutoMappedAtom$I',  function (atom) {
return (this.mAtomMapNo[atom] < 0);
});

Clazz.newMeth(C$, 'isStereoBond$I',  function (bond) {
return this.mBondType[bond] == 257 || this.mBondType[bond] == 129 ;
});

Clazz.newMeth(C$, 'isStereoBond$I$I',  function (bond, atom) {
return (this.mBondType[bond] == 257 || this.mBondType[bond] == 129 ) && this.mBondAtom[0][bond] == atom ;
});

Clazz.newMeth(C$, 'setAllAtoms$I',  function (no) {
this.mAllAtoms=no;
this.mValidHelperArrays=0;
});

Clazz.newMeth(C$, 'setAllBonds$I',  function (no) {
this.mAllBonds=no;
this.mValidHelperArrays=0;
});

Clazz.newMeth(C$, 'setAtomAbnormalValence$I$I',  function (atom, valence) {
if (valence >= -1 && valence <= 14 ) {
this.mAtomFlags[atom]&=~2013265920;
this.mAtomFlags[atom]|=((1 + valence) << 27);
if (this.mAtomicNo[atom] == 6) {
if (valence == -1 || valence == 0  || valence == 2  || valence == 4 ) {
this.mAtomFlags[atom]&=~48;
if (valence == 2) this.mAtomFlags[atom]|=16;
}}}});

Clazz.newMeth(C$, 'setAtomCharge$I$I',  function (atom, charge) {
this.mAtomCharge[atom]=charge;
this.mValidHelperArrays=0;
});

Clazz.newMeth(C$, 'setAtomColor$I$I',  function (atom, color) {
this.mAtomFlags[atom]&=~448;
this.mAtomFlags[atom]|=color;
});

Clazz.newMeth(C$, 'setAtomConfigurationUnknown$I$Z',  function (atom, u) {
if (u) this.mAtomFlags[atom]|=33554432;
 else this.mAtomFlags[atom]&=~33554432;
this.mValidHelperArrays&=7;
});

Clazz.newMeth(C$, 'setAtomSelection$I$Z',  function (atom, s) {
if (s) this.mAtomFlags[atom]|=512;
 else this.mAtomFlags[atom]&=~512;
});

Clazz.newMeth(C$, 'setAtomMarker$I$Z',  function (atom, s) {
if (s) this.mAtomFlags[atom]|=131072;
 else this.mAtomFlags[atom]&=~131072;
});

Clazz.newMeth(C$, 'setAtomicNo$I$I',  function (atom, no) {
if ((no >= 0) && (no <= 190) ) {
if (no == 151 || no == 152 ) {
this.mAtomicNo[atom]=1;
this.mAtomMass[atom]=no - 149;
} else {
this.mAtomicNo[atom]=no;
this.mAtomMass[atom]=0;
}this.mAtomFlags[atom]&=~2013265920;
this.mValidHelperArrays=0;
}});

Clazz.newMeth(C$, 'setAtomList$I$IA',  function (atom, list) {
if (this.mAtomList == null ) this.mAtomList=Clazz.array(Integer.TYPE, [this.mMaxAtoms, null]);
if (list != null ) $I$(2).sort$IA(list);
this.mAtomList[atom]=list;
this.mValidHelperArrays=0;
this.mIsFragment=true;
});

Clazz.newMeth(C$, 'setAtomList$I$IA$Z',  function (atom, list, isExcludeList) {
if (list == null ) {
if (this.mAtomList != null ) this.mAtomList[atom]=null;
return;
}if (list.length == 1 && !isExcludeList ) {
var atomicNo=list[0];
if (this.mAtomicNo[atom] != atomicNo) this.changeAtom$I$I$I$I$I(atom, atomicNo, 0, -1, 0);
if (this.mAtomList != null ) this.mAtomList[atom]=null;
return;
}if (this.mAtomList == null ) this.mAtomList=Clazz.array(Integer.TYPE, [this.mMaxAtoms, null]);
this.mAtomList[atom]=list;
if (isExcludeList) (this.mAtomQueryFeatures[$k$=atom]=Long.$or(this.mAtomQueryFeatures[$k$],(1)));
this.mValidHelperArrays=0;
this.mIsFragment=true;
});

Clazz.newMeth(C$, 'setAtomMapNo$I$I$Z',  function (atom, mapNo, autoMapped) {
this.mAtomMapNo[atom]=(autoMapped) ? -mapNo : mapNo;
});

Clazz.newMeth(C$, 'setAtomMass$I$I',  function (atom, mass) {
this.mAtomMass[atom]=mass;
this.mValidHelperArrays&=7;
});

Clazz.newMeth(C$, 'setAtomParity$I$I$Z',  function (atom, parity, isPseudo) {
this.mAtomFlags[atom]&=~(33554439);
this.mAtomFlags[atom]|=parity;
if (isPseudo) this.mAtomFlags[atom]|=4;
});

Clazz.newMeth(C$, 'setAtomStereoCenter$I$Z',  function (atom, isStereoCenter) {
this.mAtomFlags[atom]&=~67108864;
if (isStereoCenter) this.mAtomFlags[atom]|=67108864;
});

Clazz.newMeth(C$, 'setAtomQueryFeature$I$J$Z',  function (atom, feature, value) {
if (value) (this.mAtomQueryFeatures[$k$=atom]=Long.$or(this.mAtomQueryFeatures[$k$],(feature)));
 else (this.mAtomQueryFeatures[$k$=atom]=Long.$and(this.mAtomQueryFeatures[$k$],((Long.$not(feature)))));
this.mValidHelperArrays=0;
this.mIsFragment=true;
});

Clazz.newMeth(C$, 'setAtomRadical$I$I',  function (atom, radical) {
this.mAtomFlags[atom]&=~48;
this.mAtomFlags[atom]|=radical;
this.mValidHelperArrays&=7;
});

Clazz.newMeth(C$, 'setAtomCIPParity$I$I',  function (atom, parity) {
this.mAtomFlags[atom]&=~49152;
this.mAtomFlags[atom]|=(parity << 14);
});

Clazz.newMeth(C$, 'setAtomX$I$D',  function (atom, x) {
this.mCoordinates[atom].x=x;
this.mValidHelperArrays&=7;
});

Clazz.newMeth(C$, 'setAtomY$I$D',  function (atom, y) {
this.mCoordinates[atom].y=y;
this.mValidHelperArrays&=7;
});

Clazz.newMeth(C$, 'setAtomZ$I$D',  function (atom, z) {
this.mCoordinates[atom].z=z;
this.mValidHelperArrays&=7;
});

Clazz.newMeth(C$, 'setBondAtom$I$I$I',  function (no, bond, atom) {
this.mBondAtom[no][bond]=atom;
this.mValidHelperArrays=0;
});

Clazz.newMeth(C$, 'setBondCIPParity$I$I',  function (bond, parity) {
this.mBondFlags[bond]&=~48;
this.mBondFlags[bond]|=(parity << 4);
});

Clazz.newMeth(C$, 'setBondBackgroundHiliting$I$Z',  function (bond, s) {
if (s) this.mBondFlags[bond]|=32768;
 else this.mBondFlags[bond]&=~32768;
});

Clazz.newMeth(C$, 'setBondForegroundHiliting$I$Z',  function (bond, s) {
if (s) this.mBondFlags[bond]|=65536;
 else this.mBondFlags[bond]&=~65536;
});

Clazz.newMeth(C$, 'setBondParity$I$I$Z',  function (bond, parity, isPseudo) {
this.mBondFlags[bond]&=~(131079);
this.mBondFlags[bond]|=parity;
if (isPseudo) this.mBondFlags[bond]|=4;
});

Clazz.newMeth(C$, 'setBondParityUnknownOrNone$I',  function (bond) {
this.mBondFlags[bond]|=131072;
});

Clazz.newMeth(C$, 'setBondQueryFeature$I$I$Z',  function (bond, feature, value) {
if (value) this.mBondQueryFeatures[bond]|=feature;
 else this.mBondQueryFeatures[bond]&=~feature;
this.mValidHelperArrays=0;
this.mIsFragment=true;
});

Clazz.newMeth(C$, 'setBondOrder$I$I',  function (bond, order) {
this.mBondType[bond]=(order == 1) ? 1 : (order == 2) ? 2 : (order == 3) ? 4 : 32;
this.mValidHelperArrays=0;
});

Clazz.newMeth(C$, 'setBondType$I$I',  function (bond, type) {
this.mBondType[bond]=type;
this.mValidHelperArrays=0;
});

Clazz.newMeth(C$, 'setChirality$I',  function (c) {
this.mChirality=c;
});

Clazz.newMeth(C$, 'setHydrogenProtection$Z',  function (protectHydrogen) {
this.mProtectHydrogen=protectHydrogen;
});

Clazz.newMeth(C$, 'setHelperValidity$I',  function (helperValidity) {
this.mValidHelperArrays=helperValidity;
});

Clazz.newMeth(C$, 'setToRacemate$',  function () {
this.mIsRacemate=true;
});

Clazz.newMeth(C$, 'setAtomCustomLabel$I$BA',  function (atom, label) {
if (label != null  && label.length == 0 ) label=null;
if (label == null ) {
if (this.mAtomCustomLabel != null ) this.mAtomCustomLabel[atom]=null;
} else {
if (this.mAtomCustomLabel == null ) this.mAtomCustomLabel=Clazz.array(Byte.TYPE, [this.mMaxAtoms, null]);
this.mAtomCustomLabel[atom]=label;
}});

Clazz.newMeth(C$, 'setAtomCustomLabel$I$S',  function (atom, label) {
if (label != null ) {
if (label.length$() == 0) label=null;
 else {
var atomicNo=C$.getAtomicNoFromLabel$S(label);
if ((atomicNo != 0 && label.equals$O(C$.cAtomLabel[atomicNo]) ) || label.equals$O("?") ) {
this.setAtomicNo$I$I(atom, atomicNo);
label=null;
}}}if (label == null ) {
if (this.mAtomCustomLabel != null ) this.mAtomCustomLabel[atom]=null;
} else {
if (this.mAtomCustomLabel == null ) this.mAtomCustomLabel=Clazz.array(Byte.TYPE, [this.mMaxAtoms, null]);
this.mAtomCustomLabel[atom]=label.getBytes$();
}});

Clazz.newMeth(C$, 'setAtomESR$I$I$I',  function (atom, type, group) {
if (type == 0) {
this.mAtomFlags[atom]&=~33292288;
this.mAtomFlags[atom]|=(type << 18);
} else {
if (group >= 32) return;
if (group == -1) {
var maxGroup=-1;
for (var i=0; i < this.mAllAtoms; i++) if (i != atom && type == this.getAtomESRType$I(i)  && maxGroup < this.getAtomESRGroup$I(i) ) maxGroup=this.getAtomESRGroup$I(i);

for (var i=0; i < this.mAllBonds; i++) if (type == this.getBondESRType$I(i) && maxGroup < this.getBondESRGroup$I(i) ) maxGroup=this.getBondESRGroup$I(i);

group=maxGroup + 1;
if (group >= 32) return;
}this.mAtomFlags[atom]&=~33292288;
this.mAtomFlags[atom]|=((type << 18) | (group << 20));
}this.mValidHelperArrays&=7;
});

Clazz.newMeth(C$, 'setBondESR$I$I$I',  function (bond, type, group) {
if (type == 0) {
this.mBondFlags[bond]&=~32512;
this.mBondFlags[bond]|=(type << 8);
} else {
if (group >= 32) return;
if (group == -1) {
var maxGroup=-1;
for (var i=0; i < this.mAllAtoms; i++) if (type == this.getAtomESRType$I(i) && maxGroup < this.getAtomESRGroup$I(i) ) maxGroup=this.getAtomESRGroup$I(i);

for (var i=0; i < this.mAllBonds; i++) if (i != bond && type == this.getBondESRType$I(i)  && maxGroup < this.getBondESRGroup$I(i) ) maxGroup=this.getBondESRGroup$I(i);

group=maxGroup + 1;
if (group >= 32) return;
}this.mBondFlags[bond]&=~32512;
this.mBondFlags[bond]|=((type << 8) | (group << 10));
}this.mValidHelperArrays&=7;
});

Clazz.newMeth(C$, 'setFragment$Z',  function (isFragment) {
if (this.mIsFragment != isFragment ) {
this.mIsFragment=isFragment;
if (!isFragment) this.removeQueryFeatures$();
this.mValidHelperArrays=0;
}});

Clazz.newMeth(C$, 'setName$S',  function (name) {
this.mName=name;
});

Clazz.newMeth(C$, 'getUserData$',  function () {
return this.mUserData;
});

Clazz.newMeth(C$, 'setUserData$O',  function (userData) {
this.mUserData=userData;
});

Clazz.newMeth(C$, 'removeQueryFeatures$',  function () {
var isChanged=false;
for (var atom=0; atom < this.mAllAtoms; atom++) {
if (Long.$ne((Long.$and(this.mAtomQueryFeatures[atom],536870912)),0 )) {
this.markAtomForDeletion$I(atom);
isChanged=true;
}}
if (isChanged) this.deleteMarkedAtomsAndBonds$();
if (this.mAtomList != null ) {
this.mAtomList=null;
isChanged=true;
}for (var atom=0; atom < this.mAllAtoms; atom++) {
if (Long.$ne(this.mAtomQueryFeatures[atom],0 )) {
this.mAtomQueryFeatures[atom]=0;
isChanged=true;
}}
for (var bond=0; bond < this.mAllBonds; bond++) {
if (this.mBondQueryFeatures[bond] != 0) {
this.mBondQueryFeatures[bond]=0;
isChanged=true;
}if (this.mBondType[bond] == 64) {
this.mBondType[bond]=1;
isChanged=true;
}}
if (isChanged) this.mValidHelperArrays=0;
return isChanged;
});

Clazz.newMeth(C$, 'setStereoProblem$I',  function (atom) {
this.mAtomFlags[atom]|=65536;
});

Clazz.newMeth(C$, 'stripIsotopInfo$',  function () {
var found=false;
var hydrogenIsotopFound=false;
for (var atom=0; atom < this.mAllAtoms; atom++) {
if (this.mAtomMass[atom] != 0) {
this.mAtomMass[atom]=0;
found=true;
if (this.mAtomicNo[atom] == 1) hydrogenIsotopFound=true;
}}
if (hydrogenIsotopFound) this.mValidHelperArrays=0;
return found;
});

Clazz.newMeth(C$, 'translateCoords$D$D',  function (dx, dy) {
for (var i=0; i < this.mAllAtoms; i++) {
this.mCoordinates[i].x+=dx;
this.mCoordinates[i].y+=dy;
}
this.mZoomRotationX+=dx;
this.mZoomRotationY+=dy;
});

Clazz.newMeth(C$, 'scaleCoords$D',  function (f) {
for (var i=0; i < this.mAllAtoms; i++) {
this.mCoordinates[i].x*=f;
this.mCoordinates[i].y*=f;
}
});

Clazz.newMeth(C$, 'zoomAndRotateInit$D$D',  function (x, y) {
this.mZoomRotationX=x;
this.mZoomRotationY=y;
this.mOriginalAngle=Clazz.array(Double.TYPE, [this.mAllAtoms]);
this.mOriginalDistance=Clazz.array(Double.TYPE, [this.mAllAtoms]);
for (var atom=0; atom < this.mAllAtoms; atom++) {
var dx=x - this.mCoordinates[atom].x;
var dy=y - this.mCoordinates[atom].y;
this.mOriginalDistance[atom]=Math.sqrt(dx * dx + dy * dy);
this.mOriginalAngle[atom]=C$.getAngle$D$D$D$D(x, y, this.mCoordinates[atom].x, this.mCoordinates[atom].y);
}
});

Clazz.newMeth(C$, 'zoomAndRotate$D$D$Z',  function (zoom, angle, selected) {
for (var atom=0; atom < this.mAllAtoms; atom++) {
if (!selected || this.isSelectedAtom$I(atom) ) {
var newDistance=this.mOriginalDistance[atom] * zoom;
var newAngle=this.mOriginalAngle[atom] - angle;
this.mCoordinates[atom].x=this.mZoomRotationX + newDistance * Math.sin(newAngle);
this.mCoordinates[atom].y=this.mZoomRotationY + newDistance * Math.cos(newAngle);
}}
if (selected) this.mValidHelperArrays&=7;
});

Clazz.newMeth(C$, 'getMaximumBondOrder$I',  function (bond) {
var maxBondOrder=(this.isTransitionMetalAtom$I(this.mBondAtom[0][bond]) || this.isTransitionMetalAtom$I(this.mBondAtom[1][bond]) ) ? 5 : 3;
for (var i=0; i < 2; i++) {
var atom=this.mBondAtom[i][bond];
var max=this.getBondOrder$I(bond) + this.getMaxValence$I(atom) - this.getOccupiedValence$I(atom);
if (maxBondOrder > max) maxBondOrder=max;
}
return maxBondOrder;
}, p$1);

Clazz.newMeth(C$, 'incrementBondOrder$I',  function (bond) {
var maxBondOrder=p$1.getMaximumBondOrder$I.apply(this, [bond]);
var hasMetal=this.isMetalAtom$I(this.mBondAtom[0][bond]) || this.isMetalAtom$I(this.mBondAtom[1][bond]) ;
var startBond=hasMetal ? 32 : 1;
if (this.mBondType[bond] == 16) {
this.mBondType[bond]=startBond;
this.mValidHelperArrays=0;
return true;
}if (this.mBondType[bond] == 8) {
this.mBondType[bond]=(maxBondOrder > 4) ? 16 : startBond;
this.mValidHelperArrays=0;
return true;
}if (this.mBondType[bond] == 4) {
this.mBondType[bond]=(maxBondOrder > 3) ? 8 : startBond;
this.mValidHelperArrays=0;
return true;
}if (this.mBondType[bond] == 2) {
this.mBondType[bond]=386;
this.mValidHelperArrays&=7;
if ((this.mBondFlags[bond] & 128) == 0) return true;
}if (this.mBondType[bond] == 386) {
if (maxBondOrder > 2) this.mBondType[bond]=4;
 else this.mBondType[bond]=startBond;
this.mValidHelperArrays=0;
return true;
}if ((384 & this.mBondType[bond]) != 0) {
this.mBondType[bond]=1;
this.mValidHelperArrays&=7;
return true;
}if (!hasMetal && maxBondOrder < 2 ) return false;
if (this.mBondType[bond] == 1) {
this.mBondType[bond]=2;
this.mValidHelperArrays=0;
return true;
}if (maxBondOrder < 1) return false;
if (this.mBondType[bond] == 32) {
this.mBondType[bond]=1;
this.mValidHelperArrays=0;
return true;
}return false;
}, p$1);

Clazz.newMeth(C$, 'validateBondType$I$I',  function (bond, type) {
var simpleType=type & 127;
var maxBondOrder=p$1.getMaximumBondOrder$I.apply(this, [bond]);
switch (simpleType) {
case 1:
case 64:
return maxBondOrder >= 1;
case 2:
return maxBondOrder >= 2;
case 4:
return maxBondOrder >= 3;
case 8:
return maxBondOrder >= 4;
case 16:
return maxBondOrder >= 5;
case 32:
return true;
default:
return false;
}
});

Clazz.newMeth(C$, 'getOccupiedValence$I',  function (atom) {
return p$1.simpleGetValence$I.apply(this, [atom]);
});

Clazz.newMeth(C$, 'simpleGetValence$I',  function (atom) {
var val=0;
for (var bnd=0; bnd < this.mAllBonds; bnd++) if (this.mBondAtom[0][bnd] == atom || this.mBondAtom[1][bnd] == atom ) val+=this.getBondOrder$I(bnd);

return val;
}, p$1);

Clazz.newMeth(C$, 'getMaxValenceUncharged$I',  function (atom) {
var valence=this.getAtomAbnormalValence$I(atom);
if (valence == -1) valence=this.getDefaultMaxValenceUncharged$I(atom);
return valence;
});

Clazz.newMeth(C$, 'getDefaultMaxValenceUncharged$I',  function (atom) {
var valenceList=(this.mAtomicNo[atom] < C$.cAtomValence.length) ? C$.cAtomValence[this.mAtomicNo[atom]] : null;
return (valenceList == null ) ? ($b$[0] = 6, $b$[0]) : valenceList[valenceList.length - 1];
});

Clazz.newMeth(C$, 'getMaxValence$I',  function (atom) {
var valence=this.getMaxValenceUncharged$I(atom);
return valence + this.getElectronValenceCorrection$I$I(atom, valence);
});

Clazz.newMeth(C$, 'getElectronValenceCorrection$I$I',  function (atom, occupiedValence) {
if (this.mAtomicNo[atom] >= 171 && this.mAtomicNo[atom] <= 190 ) return 0;
var correction=0;
if ((this.mAtomFlags[atom] & 48) == 32) correction-=1;
if ((this.mAtomFlags[atom] & 48) == 16 || (this.mAtomFlags[atom] & 48) == 48 ) correction-=2;
var charge=this.mAtomCharge[atom];
if (charge == 0 && this.mIsFragment ) {
if (Long.$eq((Long.$and(this.mAtomQueryFeatures[atom],234881024)),201326592 )) charge=-1;
if (Long.$eq((Long.$and(this.mAtomQueryFeatures[atom],234881024)),100663296 )) charge=1;
}if (this.mAtomicNo[atom] == 7 || this.mAtomicNo[atom] == 8  || this.mAtomicNo[atom] == 9 ) correction+=charge;
 else if (this.mAtomicNo[atom] == 6 || this.mAtomicNo[atom] == 14  || this.mAtomicNo[atom] == 32 ) correction-=Math.abs(charge);
 else if (this.mAtomicNo[atom] == 15 || this.mAtomicNo[atom] == 33 ) {
if (occupiedValence - correction - charge  <= 3) correction+=charge;
 else correction-=charge;
} else if (this.mAtomicNo[atom] == 16 || this.mAtomicNo[atom] == 34  || this.mAtomicNo[atom] == 52 ) {
if (occupiedValence - correction - charge  <= 4) correction+=charge;
 else correction-=Math.abs(charge);
} else if (this.mAtomicNo[atom] == 17 || this.mAtomicNo[atom] == 35  || this.mAtomicNo[atom] == 53 ) {
if (occupiedValence - correction - charge  <= 5) correction+=charge;
 else correction-=Math.abs(charge);
} else {
correction-=charge;
}return correction;
});

Clazz.newMeth(C$, 'isAtomicNoElectronegative$I',  function (atomicNo) {
switch (atomicNo) {
case 7:
case 8:
case 9:
case 15:
case 16:
case 17:
case 33:
case 34:
case 35:
case 52:
case 53:
return true;
}
return false;
}, 1);

Clazz.newMeth(C$, 'isElectronegative$I',  function (atom) {
if (this.mIsFragment) {
if (Long.$ne((Long.$and(this.mAtomQueryFeatures[atom],1)),0 )) return false;
if (this.mAtomList != null  && this.mAtomList[atom] != null  ) for (var atomicNo, $atomicNo = 0, $$atomicNo = this.mAtomList[atom]; $atomicNo<$$atomicNo.length&&((atomicNo=($$atomicNo[$atomicNo])),1);$atomicNo++) if (!C$.isAtomicNoElectronegative$I(atomicNo)) return false;

}return C$.isAtomicNoElectronegative$I(this.mAtomicNo[atom]);
});

Clazz.newMeth(C$, 'isAtomicNoElectropositive$I',  function (atomicNo) {
if (atomicNo == 1 || atomicNo == 6 ) return false;
if (C$.isAtomicNoElectronegative$I(atomicNo)) return false;
if (atomicNo == 2 || atomicNo == 10  || atomicNo == 18  || atomicNo == 36  || atomicNo == 54 ) return false;
if (atomicNo > 103) return false;
return true;
}, 1);

Clazz.newMeth(C$, 'isElectropositive$I',  function (atom) {
if (this.mIsFragment) {
if (Long.$ne((Long.$and(this.mAtomQueryFeatures[atom],1)),0 )) return false;
if (this.mAtomList != null  && this.mAtomList[atom] != null  ) for (var atomicNo, $atomicNo = 0, $$atomicNo = this.mAtomList[atom]; $atomicNo<$$atomicNo.length&&((atomicNo=($$atomicNo[$atomicNo])),1);$atomicNo++) if (!C$.isAtomicNoElectropositive$I(atomicNo)) return false;

}return C$.isAtomicNoElectropositive$I(this.mAtomicNo[atom]);
});

Clazz.newMeth(C$, 'isMetalAtom$I',  function (atom) {
if (this.mIsFragment) {
if (Long.$ne((Long.$and(this.mAtomQueryFeatures[atom],1)),0 )) return false;
if (this.mAtomList != null  && this.mAtomList[atom] != null  ) for (var atomicNo, $atomicNo = 0, $$atomicNo = this.mAtomList[atom]; $atomicNo<$$atomicNo.length&&((atomicNo=($$atomicNo[$atomicNo])),1);$atomicNo++) if (!C$.isAtomicNoMetal$I(atomicNo)) return false;

}return C$.isAtomicNoMetal$I(this.mAtomicNo[atom]);
});

Clazz.newMeth(C$, 'isTransitionMetalAtom$I',  function (atom) {
if (this.mIsFragment) {
if (Long.$ne((Long.$and(this.mAtomQueryFeatures[atom],1)),0 )) return false;
if (this.mAtomList != null  && this.mAtomList[atom] != null  ) for (var atomicNo, $atomicNo = 0, $$atomicNo = this.mAtomList[atom]; $atomicNo<$$atomicNo.length&&((atomicNo=($$atomicNo[$atomicNo])),1);$atomicNo++) if (!C$.isAtomicNoMetal$I(atomicNo)) return false;

}return C$.isAtomicNoTransitionMetal$I(this.mAtomicNo[atom]);
});

Clazz.newMeth(C$, 'isAtomicNoMetal$I',  function (atomicNo) {
return (atomicNo >= 3 && atomicNo <= 4 ) || (atomicNo >= 11 && atomicNo <= 13 ) || (atomicNo >= 19 && atomicNo <= 31 ) || (atomicNo >= 37 && atomicNo <= 51 ) || (atomicNo >= 55 && atomicNo <= 84 ) || (atomicNo >= 87 && atomicNo <= 103 )  ;
}, 1);

Clazz.newMeth(C$, 'isAtomicNoTransitionMetal$I',  function (atomicNo) {
return (atomicNo >= 21 && atomicNo <= 30 ) || (atomicNo >= 39 && atomicNo <= 48 ) || atomicNo == 57   || (atomicNo >= 72 && atomicNo <= 80 )  || atomicNo == 89  || (atomicNo >= 104 && atomicNo <= 112 ) ;
}, 1);

Clazz.newMeth(C$, 'isOrganicAtom$I',  function (atom) {
if (this.mIsFragment) {
if (Long.$ne((Long.$and(this.mAtomQueryFeatures[atom],1)),0 )) return false;
if (this.mAtomList != null  && this.mAtomList[atom] != null  ) for (var atomicNo, $atomicNo = 0, $$atomicNo = this.mAtomList[atom]; $atomicNo<$$atomicNo.length&&((atomicNo=($$atomicNo[$atomicNo])),1);$atomicNo++) if (!C$.isAtomicNoOrganic$I(atomicNo)) return false;

}return C$.isAtomicNoOrganic$I(this.mAtomicNo[atom]);
});

Clazz.newMeth(C$, 'isAtomicNoOrganic$I',  function (atomicNo) {
return atomicNo == 1 || (atomicNo >= 5 && atomicNo <= 9 )  || (atomicNo >= 14 && atomicNo <= 17 )  || (atomicNo >= 32 && atomicNo <= 35 )  || (atomicNo >= 52 && atomicNo <= 53 ) ;
}, 1);

Clazz.newMeth(C$, 'removeAtomMapping$Z',  function (keepManualMapping) {
for (var atom=0; atom < this.mAllAtoms; atom++) if (!keepManualMapping || this.mAtomMapNo[atom] < 0 ) this.mAtomMapNo[atom]=0;

});

Clazz.newMeth(C$, 'removeMappingNo$I',  function (mapNo) {
for (var atom=0; atom < this.mAllAtoms; atom++) if (Math.abs(this.mAtomMapNo[atom]) == Math.abs(mapNo)) this.mAtomMapNo[atom]=0;

});

Clazz.newMeth(C$, 'compressMolTable$',  function () {
for (var bnd=0; bnd < this.mAllBonds; bnd++) {
if (this.mBondType[bnd] == 512) {
var atom1=this.mBondAtom[0][bnd];
var atom2=this.mBondAtom[1][bnd];
if (!!(this.mAtomicNo[atom1] == -1 ^ this.mAtomicNo[atom2] == -1)) {
if (this.mAtomCharge[atom1] != 0 && this.mAtomCharge[atom2] != 0 ) {
if (!!(this.mAtomCharge[atom1] < 0 ^ this.mAtomCharge[atom2] < 0)) {
if (this.mAtomCharge[atom1] < 0) {
++this.mAtomCharge[atom1];
--this.mAtomCharge[atom2];
} else {
--this.mAtomCharge[atom1];
++this.mAtomCharge[atom2];
}}}}}}
var newAtmNo=Clazz.array(Integer.TYPE, [this.mAllAtoms]);
var atomDest=0;
for (var atom=0; atom < this.mAllAtoms; atom++) {
if (this.mAtomicNo[atom] == -1) {
newAtmNo[atom]=-1;
continue;
}if (atomDest < atom) {
this.mAtomicNo[atomDest]=this.mAtomicNo[atom];
this.mAtomCharge[atomDest]=this.mAtomCharge[atom];
this.mAtomMass[atomDest]=this.mAtomMass[atom];
this.mAtomFlags[atomDest]=this.mAtomFlags[atom];
this.mAtomQueryFeatures[atomDest]=this.mAtomQueryFeatures[atom];
this.mAtomMapNo[atomDest]=this.mAtomMapNo[atom];
this.mCoordinates[atomDest].set$com_actelion_research_chem_Coordinates(this.mCoordinates[atom]);
if (this.mAtomList != null ) this.mAtomList[atomDest]=this.mAtomList[atom];
if (this.mAtomCustomLabel != null ) this.mAtomCustomLabel[atomDest]=this.mAtomCustomLabel[atom];
}newAtmNo[atom]=atomDest;
++atomDest;
}
this.mAllAtoms=atomDest;
var bondDest=0;
for (var bnd=0; bnd < this.mAllBonds; bnd++) {
if (this.mBondType[bnd] == 512) continue;
this.mBondType[bondDest]=this.mBondType[bnd];
this.mBondFlags[bondDest]=this.mBondFlags[bnd];
this.mBondQueryFeatures[bondDest]=this.mBondQueryFeatures[bnd];
this.mBondAtom[0][bondDest]=newAtmNo[this.mBondAtom[0][bnd]];
this.mBondAtom[1][bondDest]=newAtmNo[this.mBondAtom[1][bnd]];
++bondDest;
}
this.mAllBonds=bondDest;
return newAtmNo;
});

Clazz.newMeth(C$, 'polygon$I$I$I$Z$D$D$D',  function (atom, bonds, endAtm, aromatic, actlAngle, angleChange, bondLength) {
var dblBnd;
var actlAtm;
var remoteAtm;
var bnd;
var xdiff;
var ydiff;
var newx;
var newy;
if (atom != endAtm) {
xdiff=this.mCoordinates[atom].x - this.mCoordinates[endAtm].x;
ydiff=this.mCoordinates[atom].y - this.mCoordinates[endAtm].y;
bondLength=Math.sqrt(xdiff * xdiff + ydiff * ydiff);
}actlAtm=atom;
dblBnd=!(p$1.simpleGetValence$I.apply(this, [atom]) == 3);
for (var step=1; step < bonds; step++) {
newx=this.mCoordinates[actlAtm].x + bondLength * Math.sin(actlAngle);
newy=this.mCoordinates[actlAtm].y + bondLength * Math.cos(actlAngle);
remoteAtm=-1;
for (var i=0; i < this.mAllAtoms; i++) {
if ((Math.abs(newx - this.mCoordinates[i].x) < 4 ) && (Math.abs(newy - this.mCoordinates[i].y) < 4 ) ) {
remoteAtm=i;
break;
}}
if (remoteAtm == -1) {
remoteAtm=this.addAtom$D$D(newx, newy);
this.mCoordinates[remoteAtm].x=newx;
this.mCoordinates[remoteAtm].y=newy;
this.mCoordinates[remoteAtm].z=0;
}bnd=p$1.getBondNo$I$I.apply(this, [actlAtm, remoteAtm]);
if (bnd == -1) {
bnd=this.addBond$I$I$I(actlAtm, remoteAtm, this.suggestBondType$I$I(actlAtm, remoteAtm));
if (aromatic) {
if (dblBnd) {
if ((p$1.simpleGetValence$I.apply(this, [this.mBondAtom[0][bnd]]) < 4) && (p$1.simpleGetValence$I.apply(this, [this.mBondAtom[1][bnd]]) < 3) ) this.mBondType[bnd]=2;
}dblBnd=!dblBnd;
}}actlAtm=remoteAtm;
actlAngle+=angleChange;
}
bnd=p$1.getBondNo$I$I.apply(this, [actlAtm, endAtm]);
if (bnd == -1) bnd=this.addBond$I$I$I(actlAtm, endAtm, this.suggestBondType$I$I(actlAtm, endAtm));
if (aromatic) if (dblBnd) if ((p$1.simpleGetValence$I.apply(this, [this.mBondAtom[0][bnd]]) < 4) && (p$1.simpleGetValence$I.apply(this, [this.mBondAtom[1][bnd]]) < 4) ) this.mBondType[bnd]=2;
}, p$1);

Clazz.newMeth(C$, 'getBondNo$I$I',  function (atm1, atm2) {
for (var bnd=0; bnd < this.mAllBonds; bnd++) if (((this.mBondAtom[0][bnd] == atm1) && (this.mBondAtom[1][bnd] == atm2) ) || ((this.mBondAtom[0][bnd] == atm2) && (this.mBondAtom[1][bnd] == atm1) ) ) if (this.mBondType[bnd] != 512) return bnd;

return -1;
}, p$1);

Clazz.newMeth(C$, 'writeObject$java_io_ObjectOutputStream',  function (stream) {
stream.writeInt$I(this.mAllAtoms);
stream.writeInt$I(this.mAllBonds);
stream.writeBoolean$Z(this.mIsFragment);
for (var atom=0; atom < this.mAllAtoms; atom++) {
stream.writeInt$I(this.mAtomicNo[atom]);
stream.writeInt$I(this.mAtomCharge[atom]);
stream.writeInt$I(this.mAtomMass[atom]);
stream.writeInt$I(this.mAtomFlags[atom] & ~67238927);
stream.writeLong$J(this.mAtomQueryFeatures[atom]);
stream.writeDouble$D(this.mCoordinates[atom].x);
stream.writeDouble$D(this.mCoordinates[atom].y);
stream.writeDouble$D(this.mCoordinates[atom].z);
stream.writeInt$I(this.mAtomMapNo[atom]);
if (this.mAtomList != null  && this.mAtomList[atom] != null  ) {
stream.writeInt$I(this.mAtomList[atom].length);
for (var i=0; i < this.mAtomList[atom].length; i++) stream.writeInt$I(this.mAtomList[atom][i]);

} else stream.writeInt$I(0);
if (this.mAtomCustomLabel != null  && this.mAtomCustomLabel[atom] != null  ) {
stream.writeInt$I(this.mAtomCustomLabel[atom].length);
for (var i=0; i < this.mAtomCustomLabel[atom].length; i++) stream.writeByte$I(this.mAtomCustomLabel[atom][i]);

} else stream.writeInt$I(0);
}
for (var bond=0; bond < this.mAllBonds; bond++) {
stream.writeInt$I(this.mBondAtom[0][bond]);
stream.writeInt$I(this.mBondAtom[1][bond]);
stream.writeInt$I(this.mBondType[bond]);
stream.writeInt$I(this.mBondFlags[bond]);
stream.writeInt$I(this.mBondQueryFeatures[bond]);
}
stream.writeObject$O(this.mName);
}, p$1);

Clazz.newMeth(C$, 'readObject$java_io_ObjectInputStream',  function (stream) {
this.mAllAtoms=stream.readInt$();
this.mAllBonds=stream.readInt$();
this.mMaxAtoms=this.mAllAtoms;
this.mMaxBonds=this.mAllBonds;
p$1.init.apply(this, []);
this.mIsFragment=stream.readBoolean$();
for (var atom=0; atom < this.mAllAtoms; atom++) {
this.mAtomicNo[atom]=stream.readInt$();
this.mAtomCharge[atom]=stream.readInt$();
this.mAtomMass[atom]=stream.readInt$();
this.mAtomFlags[atom]=stream.readInt$();
this.mAtomQueryFeatures[atom]=stream.readLong$();
this.mCoordinates[atom].set$D$D$D(stream.readDouble$(), stream.readDouble$(), stream.readDouble$());
this.mAtomMapNo[atom]=stream.readInt$();
var count=stream.readInt$();
if (count != 0) {
if (this.mAtomList == null ) this.mAtomList=Clazz.array(Integer.TYPE, [this.mMaxAtoms, null]);
this.mAtomList[atom]=Clazz.array(Integer.TYPE, [count]);
for (var i=0; i < count; i++) this.mAtomList[atom][i]=stream.readInt$();

}count=stream.readInt$();
if (count != 0) {
if (this.mAtomCustomLabel == null ) this.mAtomCustomLabel=Clazz.array(Byte.TYPE, [this.mMaxAtoms, null]);
this.mAtomCustomLabel[atom]=Clazz.array(Byte.TYPE, [count]);
for (var i=0; i < count; i++) this.mAtomCustomLabel[atom][i]=stream.readByte$();

}}
for (var bond=0; bond < this.mAllBonds; bond++) {
this.mBondAtom[0][bond]=stream.readInt$();
this.mBondAtom[1][bond]=stream.readInt$();
this.mBondType[bond]=stream.readInt$();
this.mBondFlags[bond]=stream.readInt$();
this.mBondQueryFeatures[bond]=stream.readInt$();
}
try {
this.mName=stream.readObject$();
} catch (e) {
if (Clazz.exceptionOf(e,"Exception")){
} else {
throw e;
}
}
this.mValidHelperArrays=0;
}, p$1);

C$.$static$=function(){C$.$static$=0;
C$.sDefaultAVBL=24.0;
C$.cAtomLabel=Clazz.array(String, -1, ["?", "H", "He", "Li", "Be", "B", "C", "N", "O", "F", "Ne", "Na", "Mg", "Al", "Si", "P", "S", "Cl", "Ar", "K", "Ca", "Sc", "Ti", "V", "Cr", "Mn", "Fe", "Co", "Ni", "Cu", "Zn", "Ga", "Ge", "As", "Se", "Br", "Kr", "Rb", "Sr", "Y", "Zr", "Nb", "Mo", "Tc", "Ru", "Rh", "Pd", "Ag", "Cd", "In", "Sn", "Sb", "Te", "I", "Xe", "Cs", "Ba", "La", "Ce", "Pr", "Nd", "Pm", "Sm", "Eu", "Gd", "Tb", "Dy", "Ho", "Er", "Tm", "Yb", "Lu", "Hf", "Ta", "W", "Re", "Os", "Ir", "Pt", "Au", "Hg", "Tl", "Pb", "Bi", "Po", "At", "Rn", "Fr", "Ra", "Ac", "Th", "Pa", "U", "Np", "Pu", "Am", "Cm", "Bk", "Cf", "Es", "Fm", "Md", "No", "Lr", "Rf", "Db", "Sg", "Bh", "Hs", "Mt", "Ds", "Rg", "Cn", "Nh", "Fl", "Mc", "Lv", "Ts", "Og", "??", "??", "??", "??", "??", "??", "??", "??", "??", "??", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", "R16", "R1", "R2", "R3", "A", "A1", "A2", "A3", "??", "??", "D", "T", "X", "R", "H2", "H+", "Nnn", "HYD", "Pol", "??", "??", "??", "??", "??", "??", "??", "??", "??", "??", "??", "Ala", "Arg", "Asn", "Asp", "Cys", "Gln", "Glu", "Gly", "His", "Ile", "Leu", "Lys", "Met", "Phe", "Pro", "Ser", "Thr", "Trp", "Tyr", "Val"]);
C$.cRoundedMass=Clazz.array(Short.TYPE, -1, [0, 1, 4, 7, 9, 11, 12, 14, 16, 19, 20, 23, 24, 27, 28, 31, 32, 35, 40, 39, 40, 45, 48, 51, 52, 55, 56, 59, 58, 63, 64, 69, 74, 75, 80, 79, 84, 85, 88, 89, 90, 93, 98, 0, 102, 103, 106, 107, 114, 115, 120, 121, 130, 127, 132, 133, 138, 139, 140, 141, 142, 0, 152, 153, 158, 159, 164, 165, 166, 169, 174, 175, 180, 181, 184, 187, 192, 193, 195, 197, 202, 205, 208, 209, 209, 210, 222, 223, 226, 227, 232, 231, 238, 237, 244, 243, 247, 247, 251, 252, 257, 258, 259, 262, 267, 268, 271, 270, 277, 276, 281, 281, 283, 285, 289, 289, 293, 294, 294, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 71, 156, 114, 115, 103, 128, 129, 57, 137, 113, 113, 128, 131, 147, 97, 87, 101, 186, 163, 99]);
C$.cDefaultAtomValences=Clazz.array(Byte.TYPE, -1, [6]);
C$.cAminoAcidValences=Clazz.array(Byte.TYPE, -1, [2]);
C$.cAtomValence=Clazz.array(Byte.TYPE, -2, [null, Clazz.array(Byte.TYPE, -1, [1]), Clazz.array(Byte.TYPE, -1, [0]), Clazz.array(Byte.TYPE, -1, [1]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [4]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [1]), Clazz.array(Byte.TYPE, -1, [0]), Clazz.array(Byte.TYPE, -1, [1]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [4]), Clazz.array(Byte.TYPE, -1, [3, 5]), Clazz.array(Byte.TYPE, -1, [2, 4, 6]), Clazz.array(Byte.TYPE, -1, [1, 3, 5, 7]), Clazz.array(Byte.TYPE, -1, [0]), Clazz.array(Byte.TYPE, -1, [1]), Clazz.array(Byte.TYPE, -1, [2]), null, null, null, null, null, null, null, null, null, null, Clazz.array(Byte.TYPE, -1, [2, 3]), Clazz.array(Byte.TYPE, -1, [2, 4]), Clazz.array(Byte.TYPE, -1, [3, 5]), Clazz.array(Byte.TYPE, -1, [2, 4, 6]), Clazz.array(Byte.TYPE, -1, [1, 3, 5, 7]), Clazz.array(Byte.TYPE, -1, [0, 2]), Clazz.array(Byte.TYPE, -1, [1]), Clazz.array(Byte.TYPE, -1, [2]), null, null, null, null, null, null, null, null, null, null, Clazz.array(Byte.TYPE, -1, [1, 2, 3]), Clazz.array(Byte.TYPE, -1, [2, 4]), Clazz.array(Byte.TYPE, -1, [3, 5]), Clazz.array(Byte.TYPE, -1, [2, 4, 6]), Clazz.array(Byte.TYPE, -1, [1, 3, 5, 7]), Clazz.array(Byte.TYPE, -1, [0, 2, 4, 6]), Clazz.array(Byte.TYPE, -1, [1]), Clazz.array(Byte.TYPE, -1, [2]), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [2])]);
C$.cCommonOxidationState=Clazz.array(Byte.TYPE, -2, [null, Clazz.array(Byte.TYPE, -1, [1]), null, Clazz.array(Byte.TYPE, -1, [1]), Clazz.array(Byte.TYPE, -1, [2]), null, null, Clazz.array(Byte.TYPE, -1, [-3]), Clazz.array(Byte.TYPE, -1, [-2]), Clazz.array(Byte.TYPE, -1, [-1]), null, Clazz.array(Byte.TYPE, -1, [1]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [3]), null, Clazz.array(Byte.TYPE, -1, [-3]), Clazz.array(Byte.TYPE, -1, [-2]), Clazz.array(Byte.TYPE, -1, [-1]), null, Clazz.array(Byte.TYPE, -1, [1]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [2, 3, 4]), Clazz.array(Byte.TYPE, -1, [2, 3, 4, 5]), Clazz.array(Byte.TYPE, -1, [2, 3, 6]), Clazz.array(Byte.TYPE, -1, [2, 3, 4, 7]), Clazz.array(Byte.TYPE, -1, [2, 3]), Clazz.array(Byte.TYPE, -1, [2, 3]), Clazz.array(Byte.TYPE, -1, [2, 3]), Clazz.array(Byte.TYPE, -1, [1, 2]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [2, 4]), Clazz.array(Byte.TYPE, -1, [-3, 3, 5]), Clazz.array(Byte.TYPE, -1, [-2]), Clazz.array(Byte.TYPE, -1, [-1]), null, Clazz.array(Byte.TYPE, -1, [1]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [4]), Clazz.array(Byte.TYPE, -1, [3, 5]), Clazz.array(Byte.TYPE, -1, [6]), Clazz.array(Byte.TYPE, -1, [4, 6, 7]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [2, 4]), Clazz.array(Byte.TYPE, -1, [1]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [2, 4]), Clazz.array(Byte.TYPE, -1, [-3, 3, 5]), Clazz.array(Byte.TYPE, -1, [-2, 4, 6]), Clazz.array(Byte.TYPE, -1, [-1]), null, Clazz.array(Byte.TYPE, -1, [1]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [3, 4]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [2, 3]), Clazz.array(Byte.TYPE, -1, [2, 3]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [2, 3]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [4]), Clazz.array(Byte.TYPE, -1, [5]), Clazz.array(Byte.TYPE, -1, [6]), Clazz.array(Byte.TYPE, -1, [4, 6, 7]), Clazz.array(Byte.TYPE, -1, [3, 4]), Clazz.array(Byte.TYPE, -1, [3, 4]), Clazz.array(Byte.TYPE, -1, [2, 4]), Clazz.array(Byte.TYPE, -1, [1, 3]), Clazz.array(Byte.TYPE, -1, [1, 2]), Clazz.array(Byte.TYPE, -1, [1, 3]), Clazz.array(Byte.TYPE, -1, [2, 4]), Clazz.array(Byte.TYPE, -1, [3, 5]), Clazz.array(Byte.TYPE, -1, [-2, 2, 4]), Clazz.array(Byte.TYPE, -1, [-1, 1]), null, Clazz.array(Byte.TYPE, -1, [1]), Clazz.array(Byte.TYPE, -1, [2]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [4]), Clazz.array(Byte.TYPE, -1, [4, 5]), Clazz.array(Byte.TYPE, -1, [3, 4, 5, 6]), Clazz.array(Byte.TYPE, -1, [3, 4, 5, 6]), Clazz.array(Byte.TYPE, -1, [3, 4, 5, 6]), Clazz.array(Byte.TYPE, -1, [3, 4, 5, 6]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [3, 4]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [3]), Clazz.array(Byte.TYPE, -1, [2, 3]), Clazz.array(Byte.TYPE, -1, [2, 3]), Clazz.array(Byte.TYPE, -1, [3])]);
};
var $k$;
var $b$ = new Int8Array(1);
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-18 09:54:15 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
