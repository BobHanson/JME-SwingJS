(function(){var P$=Clazz.newPackage("com.actelion.research.chem"),p$1={},I$=[[0,'com.actelion.research.chem.StereoMolecule','com.actelion.research.chem.AromaticityResolver','com.actelion.research.chem.Molecule','com.actelion.research.util.DoubleFormat']],I$0=I$[0],$I$=function(i,n){return((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "IDCodeParserWithoutCoordinateInvention");

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['Z',['mNeglectSpaceDelimitedCoordinates'],'I',['mIDCodeBitsAvail','mIDCodeTempData','mIDCodeBufferIndex'],'O',['mMol','com.actelion.research.chem.StereoMolecule','mDecodingBytes','byte[]']]]

Clazz.newMeth(C$, 'ensure2DCoordinates$',  function () {
return false;
});

Clazz.newMeth(C$, 'neglectSpaceDelimitedCoordinates$',  function () {
this.mNeglectSpaceDelimitedCoordinates=true;
});

Clazz.newMeth(C$, 'getCompactMolecule$S',  function (idcode) {
return (idcode == null  || idcode.length$() == 0 ) ? null : this.getCompactMolecule$BA$BA(idcode.getBytes$(), null);
});

Clazz.newMeth(C$, 'getCompactMolecule$BA',  function (idcode) {
if (idcode == null  || idcode.length == 0 ) return null;
for (var i=2; i < idcode.length - 2; i++) if (idcode[i] == 32 ) return this.getCompactMolecule$BA$BA$I$I(idcode, idcode, 0, i + 1);

return this.getCompactMolecule$BA$BA(idcode, null);
});

Clazz.newMeth(C$, 'getCompactMolecule$S$S',  function (idcode, coordinates) {
return (idcode == null ) ? null : this.getCompactMolecule$BA$BA(idcode.getBytes$(), (coordinates == null ) ? null : coordinates.getBytes$());
});

Clazz.newMeth(C$, 'getCompactMolecule$BA$BA',  function (idcode, coordinates) {
return this.getCompactMolecule$BA$BA$I$I(idcode, coordinates, 0, 0);
});

Clazz.newMeth(C$, 'getCompactMolecule$BA$I',  function (idcode, idcodeStart) {
return this.getCompactMolecule$BA$BA$I$I(idcode, null, idcodeStart, -1);
});

Clazz.newMeth(C$, 'getCompactMolecule$BA$BA$I$I',  function (idcode, coordinates, idcodeStart, coordsStart) {
if (idcode == null ) return null;
p$1.decodeBitsStart$BA$I.apply(this, [idcode, idcodeStart]);
var abits=p$1.decodeBits$I.apply(this, [4]);
var bbits=p$1.decodeBits$I.apply(this, [4]);
if (abits > 8) abits=bbits;
var allAtoms=p$1.decodeBits$I.apply(this, [abits]);
var allBonds=p$1.decodeBits$I.apply(this, [bbits]);
var mol=Clazz.new_($I$(1,1).c$$I$I,[allAtoms, allBonds]);
this.parse$com_actelion_research_chem_StereoMolecule$BA$BA$I$I(mol, idcode, coordinates, idcodeStart, coordsStart);
return mol;
});

Clazz.newMeth(C$, 'parse$com_actelion_research_chem_StereoMolecule$S',  function (mol, idcode) {
if (idcode == null  || idcode.length$() == 0 ) {
this.parse$com_actelion_research_chem_StereoMolecule$BA$BA(mol, null, null);
return;
}var index=idcode.indexOf$I(" ");
if (index > 0 && index < idcode.length$() - 1 ) this.parse$com_actelion_research_chem_StereoMolecule$BA$BA(mol, idcode.substring$I$I(0, index).getBytes$(), idcode.substring$I(index + 1).getBytes$());
 else this.parse$com_actelion_research_chem_StereoMolecule$BA$BA(mol, idcode.getBytes$(), null);
});

Clazz.newMeth(C$, 'parse$com_actelion_research_chem_StereoMolecule$BA',  function (mol, idcode) {
this.parse$com_actelion_research_chem_StereoMolecule$BA$BA(mol, idcode, null);
});

Clazz.newMeth(C$, 'parse$com_actelion_research_chem_StereoMolecule$S$S',  function (mol, idcode, coordinates) {
var idcodeBytes=(idcode == null ) ? null : idcode.getBytes$();
var coordinateBytes=(coordinates == null ) ? null : coordinates.getBytes$();
this.parse$com_actelion_research_chem_StereoMolecule$BA$BA(mol, idcodeBytes, coordinateBytes);
});

Clazz.newMeth(C$, 'parse$com_actelion_research_chem_StereoMolecule$BA$BA',  function (mol, idcode, coordinates) {
if (idcode == null  || idcode.length == 0 ) {
mol.clear$();
return;
}this.parse$com_actelion_research_chem_StereoMolecule$BA$BA$I$I(mol, idcode, coordinates, 0, 0);
});

Clazz.newMeth(C$, 'parse$com_actelion_research_chem_StereoMolecule$BA$I',  function (mol, idcode, idcodeStart) {
this.parse$com_actelion_research_chem_StereoMolecule$BA$BA$I$I(mol, idcode, null, idcodeStart, -1);
});

Clazz.newMeth(C$, 'parse$com_actelion_research_chem_StereoMolecule$BA$BA$I$I',  function (mol, idcode, coordinates, idcodeStart, coordsStart) {
mol.clear$();
if (idcode == null  || idcodeStart < 0  || idcodeStart >= idcode.length ) return;
this.mMol=mol;
var version=8;
if (coordinates != null  && (coordsStart < 0 || coordsStart >= coordinates.length ) ) coordinates=null;
p$1.decodeBitsStart$BA$I.apply(this, [idcode, idcodeStart]);
var abits=p$1.decodeBits$I.apply(this, [4]);
var bbits=p$1.decodeBits$I.apply(this, [4]);
if (abits > 8) {
version=abits;
abits=bbits;
}if (abits == 0) {
this.mMol.setFragment$Z(p$1.decodeBits$I.apply(this, [1]) == 1);
return;
}var allAtoms=p$1.decodeBits$I.apply(this, [abits]);
var allBonds=p$1.decodeBits$I.apply(this, [bbits]);
var nitrogens=p$1.decodeBits$I.apply(this, [abits]);
var oxygens=p$1.decodeBits$I.apply(this, [abits]);
var otherAtoms=p$1.decodeBits$I.apply(this, [abits]);
var chargedAtoms=p$1.decodeBits$I.apply(this, [abits]);
for (var atom=0; atom < allAtoms; atom++) this.mMol.addAtom$I(6);

for (var i=0; i < nitrogens; i++) this.mMol.setAtomicNo$I$I(p$1.decodeBits$I.apply(this, [abits]), 7);

for (var i=0; i < oxygens; i++) this.mMol.setAtomicNo$I$I(p$1.decodeBits$I.apply(this, [abits]), 8);

for (var i=0; i < otherAtoms; i++) this.mMol.setAtomicNo$I$I(p$1.decodeBits$I.apply(this, [abits]), p$1.decodeBits$I.apply(this, [8]));

for (var i=0; i < chargedAtoms; i++) this.mMol.setAtomCharge$I$I(p$1.decodeBits$I.apply(this, [abits]), p$1.decodeBits$I.apply(this, [4]) - 8);

var closureBonds=1 + allBonds - allAtoms;
var dbits=p$1.decodeBits$I.apply(this, [4]);
var base=0;
this.mMol.setAtomX$I$D(0, 0.0);
this.mMol.setAtomY$I$D(0, 0.0);
this.mMol.setAtomZ$I$D(0, 0.0);
var decodeOldCoordinates=(coordinates != null  && coordinates[coordsStart] >= 39  );
var targetAVBL=0.0;
var xOffset=0.0;
var yOffset=0.0;
var zOffset=0.0;
var coordsAre3D=false;
var coordsAreAbsolute=false;
if (decodeOldCoordinates) {
if ((coordinates.length > 2 * allAtoms - 2 && coordinates[2 * allAtoms - 2] == 39  ) || (coordinates.length > 3 * allAtoms - 3 && coordinates[3 * allAtoms - 3] == 39  ) ) {
coordsAreAbsolute=true;
coordsAre3D=(coordinates.length == 3 * allAtoms - 3 + 9);
var index=coordsAre3D ? 3 * allAtoms - 3 : 2 * allAtoms - 2;
var avblInt=86 * ((coordinates[index + 1]|0) - 40) + (coordinates[index + 2]|0) - 40;
targetAVBL=Math.pow(10.0, avblInt / 2000.0 - 1.0);
index+=2;
var xInt=86 * ((coordinates[index + 1]|0) - 40) + (coordinates[index + 2]|0) - 40;
xOffset=Math.pow(10.0, xInt / 1500.0 - 1.0);
index+=2;
var yInt=86 * ((coordinates[index + 1]|0) - 40) + (coordinates[index + 2]|0) - 40;
yOffset=Math.pow(10.0, yInt / 1500.0 - 1.0);
if (coordsAre3D) {
index+=2;
var zInt=86 * ((coordinates[index + 1]|0) - 40) + (coordinates[index + 2]|0) - 40;
zOffset=Math.pow(10.0, zInt / 1500.0 - 1.0);
}} else {
coordsAre3D=(coordinates.length == 3 * allAtoms - 3);
}}if (this.ensure2DCoordinates$() && coordsAre3D ) {
coordinates=null;
decodeOldCoordinates=false;
}for (var i=1; i < allAtoms; i++) {
var dif=p$1.decodeBits$I.apply(this, [dbits]);
if (dif == 0) {
if (decodeOldCoordinates) {
this.mMol.setAtomX$I$D(i, this.mMol.getAtomX$I(0) + 8 * (coordinates[i * 2 - 2] - 83));
this.mMol.setAtomY$I$D(i, this.mMol.getAtomY$I(0) + 8 * (coordinates[i * 2 - 1] - 83));
if (coordsAre3D) this.mMol.setAtomZ$I$D(i, this.mMol.getAtomZ$I(0) + 8 * (coordinates[2 * allAtoms - 3 + i] - 83));
}++closureBonds;
continue;
}base+=dif - 1;
if (decodeOldCoordinates) {
this.mMol.setAtomX$I$D(i, this.mMol.getAtomX$I(base) + coordinates[i * 2 - 2] - 83);
this.mMol.setAtomY$I$D(i, this.mMol.getAtomY$I(base) + coordinates[i * 2 - 1] - 83);
if (coordsAre3D) this.mMol.setAtomZ$I$D(i, this.mMol.getAtomZ$I(base) + (coordinates[2 * allAtoms - 3 + i] - 83));
}this.mMol.addBond$I$I$I(base, i, 1);
}
for (var i=0; i < closureBonds; i++) this.mMol.addBond$I$I$I(p$1.decodeBits$I.apply(this, [abits]), p$1.decodeBits$I.apply(this, [abits]), 1);

var isDelocalizedBond=Clazz.array(Boolean.TYPE, [allBonds]);
for (var bond=0; bond < allBonds; bond++) {
var bondOrder=p$1.decodeBits$I.apply(this, [2]);
switch (bondOrder) {
case 0:
isDelocalizedBond[bond]=true;
break;
case 2:
this.mMol.setBondType$I$I(bond, 2);
break;
case 3:
this.mMol.setBondType$I$I(bond, 4);
break;
}
}
var THCount=p$1.decodeBits$I.apply(this, [abits]);
for (var i=0; i < THCount; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
if (version == 8) {
var parity=p$1.decodeBits$I.apply(this, [2]);
if (parity == 3) {
this.mMol.setAtomESR$I$I$I(atom, 1, 0);
this.mMol.setAtomParity$I$I$Z(atom, 1, false);
} else {
this.mMol.setAtomParity$I$I$Z(atom, parity, false);
}} else {
var parity=p$1.decodeBits$I.apply(this, [3]);
switch (parity) {
case 4:
this.mMol.setAtomParity$I$I$Z(atom, 1, false);
this.mMol.setAtomESR$I$I$I(atom, 1, p$1.decodeBits$I.apply(this, [3]));
break;
case 5:
this.mMol.setAtomParity$I$I$Z(atom, 2, false);
this.mMol.setAtomESR$I$I$I(atom, 1, p$1.decodeBits$I.apply(this, [3]));
break;
case 6:
this.mMol.setAtomParity$I$I$Z(atom, 1, false);
this.mMol.setAtomESR$I$I$I(atom, 2, p$1.decodeBits$I.apply(this, [3]));
break;
case 7:
this.mMol.setAtomParity$I$I$Z(atom, 2, false);
this.mMol.setAtomESR$I$I$I(atom, 2, p$1.decodeBits$I.apply(this, [3]));
break;
default:
this.mMol.setAtomParity$I$I$Z(atom, parity, false);
}
}}
if (version == 8) if ((p$1.decodeBits$I.apply(this, [1]) == 0)) this.mMol.setToRacemate$();
var EZCount=p$1.decodeBits$I.apply(this, [bbits]);
for (var i=0; i < EZCount; i++) {
var bond=p$1.decodeBits$I.apply(this, [bbits]);
if (this.mMol.getBondType$I(bond) == 1) {
var parity=p$1.decodeBits$I.apply(this, [3]);
switch (parity) {
case 4:
this.mMol.setBondParity$I$I$Z(bond, 1, false);
this.mMol.setBondESR$I$I$I(bond, 1, p$1.decodeBits$I.apply(this, [3]));
break;
case 5:
this.mMol.setBondParity$I$I$Z(bond, 2, false);
this.mMol.setBondESR$I$I$I(bond, 1, p$1.decodeBits$I.apply(this, [3]));
break;
case 6:
this.mMol.setBondParity$I$I$Z(bond, 1, false);
this.mMol.setBondESR$I$I$I(bond, 2, p$1.decodeBits$I.apply(this, [3]));
break;
case 7:
this.mMol.setBondParity$I$I$Z(bond, 2, false);
this.mMol.setBondESR$I$I$I(bond, 2, p$1.decodeBits$I.apply(this, [3]));
break;
default:
this.mMol.setBondParity$I$I$Z(bond, parity, false);
}
} else {
this.mMol.setBondParity$I$I$Z(bond, p$1.decodeBits$I.apply(this, [2]), false);
}}
this.mMol.setFragment$Z(p$1.decodeBits$I.apply(this, [1]) == 1);
var aromaticSPBond=null;
var offset=0;
while (p$1.decodeBits$I.apply(this, [1]) == 1){
var dataType=offset + p$1.decodeBits$I.apply(this, [4]);
switch (dataType) {
case 0:
var no=p$1.decodeBits$I.apply(this, [abits]);
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
this.mMol.setAtomQueryFeature$I$J$Z(atom, 2048, true);
}
break;
case 1:
no=p$1.decodeBits$I.apply(this, [abits]);
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
var mass=p$1.decodeBits$I.apply(this, [8]);
this.mMol.setAtomMass$I$I(atom, mass);
}
break;
case 2:
no=p$1.decodeBits$I.apply(this, [bbits]);
for (var i=0; i < no; i++) {
var bond=p$1.decodeBits$I.apply(this, [bbits]);
}
break;
case 3:
no=p$1.decodeBits$I.apply(this, [abits]);
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
this.mMol.setAtomQueryFeature$I$J$Z(atom, 4096, true);
}
break;
case 4:
no=p$1.decodeBits$I.apply(this, [abits]);
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
var ringState=Long.$sl(p$1.decodeBits$I.apply(this, [4]),3);
this.mMol.setAtomQueryFeature$I$J$Z(atom, ringState, true);
}
break;
case 5:
no=p$1.decodeBits$I.apply(this, [abits]);
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
var aromState=Long.$sl(p$1.decodeBits$I.apply(this, [2]),1);
this.mMol.setAtomQueryFeature$I$J$Z(atom, aromState, true);
}
break;
case 6:
no=p$1.decodeBits$I.apply(this, [abits]);
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
this.mMol.setAtomQueryFeature$I$J$Z(atom, 1, true);
}
break;
case 7:
no=p$1.decodeBits$I.apply(this, [abits]);
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
var hydrogen=Long.$sl(p$1.decodeBits$I.apply(this, [4]),7);
this.mMol.setAtomQueryFeature$I$J$Z(atom, hydrogen, true);
}
break;
case 8:
no=p$1.decodeBits$I.apply(this, [abits]);
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
var atoms=p$1.decodeBits$I.apply(this, [4]);
var atomList=Clazz.array(Integer.TYPE, [atoms]);
for (var j=0; j < atoms; j++) {
var atomicNo=p$1.decodeBits$I.apply(this, [8]);
atomList[j]=atomicNo;
}
this.mMol.setAtomList$I$IA(atom, atomList);
}
break;
case 9:
no=p$1.decodeBits$I.apply(this, [bbits]);
for (var i=0; i < no; i++) {
var bond=p$1.decodeBits$I.apply(this, [bbits]);
var ringState=p$1.decodeBits$I.apply(this, [2]) << 7;
this.mMol.setBondQueryFeature$I$I$Z(bond, ringState, true);
}
break;
case 10:
no=p$1.decodeBits$I.apply(this, [bbits]);
for (var i=0; i < no; i++) {
var bond=p$1.decodeBits$I.apply(this, [bbits]);
var bondTypes=p$1.decodeBits$I.apply(this, [5]) << 0;
this.mMol.setBondQueryFeature$I$I$Z(bond, bondTypes, true);
}
break;
case 11:
no=p$1.decodeBits$I.apply(this, [abits]);
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
this.mMol.setAtomQueryFeature$I$J$Z(atom, 8192, true);
}
break;
case 12:
no=p$1.decodeBits$I.apply(this, [bbits]);
for (var i=0; i < no; i++) {
var bond=p$1.decodeBits$I.apply(this, [bbits]);
var bridgeData=p$1.decodeBits$I.apply(this, [8]) << 9;
this.mMol.setBondQueryFeature$I$I$Z(bond, bridgeData, true);
}
break;
case 13:
no=p$1.decodeBits$I.apply(this, [abits]);
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
var piElectrons=Long.$sl(p$1.decodeBits$I.apply(this, [3]),14);
this.mMol.setAtomQueryFeature$I$J$Z(atom, piElectrons, true);
}
break;
case 14:
no=p$1.decodeBits$I.apply(this, [abits]);
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
var neighbours=Long.$sl(p$1.decodeBits$I.apply(this, [5]),17);
this.mMol.setAtomQueryFeature$I$J$Z(atom, neighbours, true);
}
break;
case 15:
case 31:
offset+=16;
break;
case 16:
no=p$1.decodeBits$I.apply(this, [abits]);
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
var ringSize=Long.$sl(p$1.decodeBits$I.apply(this, [3]),22);
this.mMol.setAtomQueryFeature$I$J$Z(atom, ringSize, true);
}
break;
case 17:
no=p$1.decodeBits$I.apply(this, [abits]);
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
this.mMol.setAtomAbnormalValence$I$I(atom, p$1.decodeBits$I.apply(this, [4]));
}
break;
case 18:
no=p$1.decodeBits$I.apply(this, [abits]);
var lbits=p$1.decodeBits$I.apply(this, [4]);
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
var count=p$1.decodeBits$I.apply(this, [lbits]);
var label=Clazz.array(Byte.TYPE, [count]);
for (var j=0; j < count; j++) label[j]=(p$1.decodeBits$I.apply(this, [7])|0);

this.mMol.setAtomCustomLabel$I$S(atom,  String.instantialize(label));
}
break;
case 19:
no=p$1.decodeBits$I.apply(this, [abits]);
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
var charge=Long.$sl(p$1.decodeBits$I.apply(this, [3]),25);
this.mMol.setAtomQueryFeature$I$J$Z(atom, charge, true);
}
break;
case 20:
no=p$1.decodeBits$I.apply(this, [bbits]);
for (var i=0; i < no; i++) {
var bond=p$1.decodeBits$I.apply(this, [bbits]);
var ringSize=p$1.decodeBits$I.apply(this, [3]) << 17;
this.mMol.setBondQueryFeature$I$I$Z(bond, ringSize, true);
}
break;
case 21:
no=p$1.decodeBits$I.apply(this, [abits]);
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
this.mMol.setAtomRadical$I$I(atom, p$1.decodeBits$I.apply(this, [2]) << 4);
}
break;
case 22:
no=p$1.decodeBits$I.apply(this, [abits]);
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
this.mMol.setAtomQueryFeature$I$J$Z(atom, 268435456, true);
}
break;
case 23:
no=p$1.decodeBits$I.apply(this, [bbits]);
for (var i=0; i < no; i++) {
var bond=p$1.decodeBits$I.apply(this, [bbits]);
this.mMol.setBondQueryFeature$I$I$Z(bond, 1048576, true);
}
break;
case 24:
no=p$1.decodeBits$I.apply(this, [bbits]);
for (var i=0; i < no; i++) {
var bond=p$1.decodeBits$I.apply(this, [bbits]);
var aromState=p$1.decodeBits$I.apply(this, [2]) << 21;
this.mMol.setBondQueryFeature$I$I$Z(bond, aromState, true);
}
break;
case 25:
for (var i=0; i < allAtoms; i++) if (p$1.decodeBits$I.apply(this, [1]) == 1) this.mMol.setAtomSelection$I$Z(i, true);

break;
case 26:
no=p$1.decodeBits$I.apply(this, [bbits]);
aromaticSPBond=Clazz.array(Integer.TYPE, [no]);
for (var i=0; i < no; i++) aromaticSPBond[i]=p$1.decodeBits$I.apply(this, [bbits]);

break;
case 27:
no=p$1.decodeBits$I.apply(this, [abits]);
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
this.mMol.setAtomQueryFeature$I$J$Z(atom, 536870912, true);
}
break;
case 28:
no=p$1.decodeBits$I.apply(this, [bbits]);
for (var i=0; i < no; i++) this.mMol.setBondType$I$I(p$1.decodeBits$I.apply(this, [bbits]), 32);

break;
case 29:
no=p$1.decodeBits$I.apply(this, [abits]);
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
var hint=Long.$sl(p$1.decodeBits$I.apply(this, [2]),30);
this.mMol.setAtomQueryFeature$I$J$Z(atom, hint, true);
}
break;
case 30:
no=p$1.decodeBits$I.apply(this, [abits]);
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
var ringSize=Long.$sl(p$1.decodeBits$I.apply(this, [7]),32);
this.mMol.setAtomQueryFeature$I$J$Z(atom, ringSize, true);
}
break;
case 32:
no=p$1.decodeBits$I.apply(this, [abits]);
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
var stereoState=Long.$sl(p$1.decodeBits$I.apply(this, [2]),44);
this.mMol.setAtomQueryFeature$I$J$Z(atom, stereoState, true);
}
break;
case 33:
no=p$1.decodeBits$I.apply(this, [abits]);
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
var eNeighbours=Long.$sl(p$1.decodeBits$I.apply(this, [5]),39);
this.mMol.setAtomQueryFeature$I$J$Z(atom, eNeighbours, true);
}
break;
case 34:
no=p$1.decodeBits$I.apply(this, [abits]);
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
this.mMol.setAtomQueryFeature$I$J$Z(atom, 70368744177664, true);
}
break;
case 35:
no=p$1.decodeBits$I.apply(this, [bbits]);
for (var i=0; i < no; i++) {
var bond=p$1.decodeBits$I.apply(this, [bbits]);
this.mMol.setBondQueryFeature$I$I$Z(bond, 8388608, true);
}
break;
case 36:
no=p$1.decodeBits$I.apply(this, [bbits]);
for (var i=0; i < no; i++) {
var bond=p$1.decodeBits$I.apply(this, [bbits]);
var bondType=p$1.decodeBits$I.apply(this, [2]) << 5;
this.mMol.setBondQueryFeature$I$I$Z(bond, bondType, true);
}
break;
case 37:
no=p$1.decodeBits$I.apply(this, [bbits]);
for (var i=0; i < no; i++) {
var bond=p$1.decodeBits$I.apply(this, [bbits]);
var bondType=p$1.decodeBits$I.apply(this, [1]) == 0 ? 8 : 16;
this.mMol.setBondType$I$I(bond, bondType);
}
break;
}
}
Clazz.new_($I$(2,1).c$$com_actelion_research_chem_ExtendedMolecule,[this.mMol]).locateDelocalizedDoubleBonds$ZA(isDelocalizedBond);
if (aromaticSPBond != null ) for (var bond, $bond = 0, $$bond = aromaticSPBond; $bond<$$bond.length&&((bond=($$bond[$bond])),1);$bond++) this.mMol.setBondType$I$I(bond, this.mMol.getBondType$I(bond) == 2 ? 4 : 2);

if (coordinates == null  && !this.mNeglectSpaceDelimitedCoordinates  && idcode.length > this.mIDCodeBufferIndex + 1  && (idcode[this.mIDCodeBufferIndex + 1] == 32  || idcode[this.mIDCodeBufferIndex + 1] == 9  ) ) {
coordinates=idcode;
coordsStart=this.mIDCodeBufferIndex + 2;
}if (coordinates != null ) {
try {
if (coordinates[coordsStart] == 33  || coordinates[coordsStart] == 35  ) {
p$1.decodeBitsStart$BA$I.apply(this, [coordinates, coordsStart + 1]);
coordsAre3D=(p$1.decodeBits$I.apply(this, [1]) == 1);
coordsAreAbsolute=(p$1.decodeBits$I.apply(this, [1]) == 1);
var resolutionBits=2 * p$1.decodeBits$I.apply(this, [4]);
var binCount=(1 << resolutionBits);
var factor;
var from=0;
var bond=0;
for (var atom=1; atom < allAtoms; atom++) {
if (bond < allBonds && this.mMol.getBondAtom$I$I(1, bond) == atom ) {
from=this.mMol.getBondAtom$I$I(0, bond++);
factor=1.0;
} else {
from=0;
factor=8.0;
}this.mMol.setAtomX$I$D(atom, this.mMol.getAtomX$I(from) + factor * (p$1.decodeBits$I.apply(this, [resolutionBits]) - (binCount/2|0)));
this.mMol.setAtomY$I$D(atom, this.mMol.getAtomY$I(from) + factor * (p$1.decodeBits$I.apply(this, [resolutionBits]) - (binCount/2|0)));
if (coordsAre3D) this.mMol.setAtomZ$I$D(atom, this.mMol.getAtomZ$I(from) + factor * (p$1.decodeBits$I.apply(this, [resolutionBits]) - (binCount/2|0)));
}
if (coordinates[coordsStart] == 35 ) {
var hydrogenCount=0;
var hCount=Clazz.array(Integer.TYPE, [allAtoms]);
for (var atom=0; atom < allAtoms; atom++) hydrogenCount+=(hCount[atom]=this.mMol.getImplicitHydrogens$I(atom));

for (var atom=0; atom < allAtoms; atom++) {
for (var i=0; i < hCount[atom]; i++) {
var hydrogen=this.mMol.addAtom$I(1);
this.mMol.addBond$I$I$I(atom, hydrogen, 1);
this.mMol.setAtomX$I$D(hydrogen, this.mMol.getAtomX$I(atom) + (p$1.decodeBits$I.apply(this, [resolutionBits]) - (binCount/2|0)));
this.mMol.setAtomY$I$D(hydrogen, this.mMol.getAtomY$I(atom) + (p$1.decodeBits$I.apply(this, [resolutionBits]) - (binCount/2|0)));
if (coordsAre3D) this.mMol.setAtomZ$I$D(hydrogen, this.mMol.getAtomZ$I(atom) + (p$1.decodeBits$I.apply(this, [resolutionBits]) - (binCount/2|0)));
}
}
allAtoms+=hydrogenCount;
allBonds+=hydrogenCount;
}var avblDefault=coordsAre3D ? 1.5 : $I$(3).getDefaultAverageBondLength$();
var avbl=this.mMol.getAverageBondLength$I$I$D(allAtoms, allBonds, avblDefault);
if (coordsAreAbsolute) {
targetAVBL=p$1.decodeAVBL$I$I.apply(this, [p$1.decodeBits$I.apply(this, [resolutionBits]), binCount]);
xOffset=targetAVBL * p$1.decodeShift$I$I.apply(this, [p$1.decodeBits$I.apply(this, [resolutionBits]), binCount]);
yOffset=targetAVBL * p$1.decodeShift$I$I.apply(this, [p$1.decodeBits$I.apply(this, [resolutionBits]), binCount]);
if (coordsAre3D) zOffset=targetAVBL * p$1.decodeShift$I$I.apply(this, [p$1.decodeBits$I.apply(this, [resolutionBits]), binCount]);
factor=targetAVBL / avbl;
for (var atom=0; atom < allAtoms; atom++) {
this.mMol.setAtomX$I$D(atom, xOffset + factor * this.mMol.getAtomX$I(atom));
this.mMol.setAtomY$I$D(atom, yOffset + factor * this.mMol.getAtomY$I(atom));
if (coordsAre3D) this.mMol.setAtomZ$I$D(atom, zOffset + factor * this.mMol.getAtomZ$I(atom));
}
} else {
targetAVBL=1.5;
factor=targetAVBL / avbl;
for (var atom=0; atom < allAtoms; atom++) {
this.mMol.setAtomX$I$D(atom, factor * this.mMol.getAtomX$I(atom));
this.mMol.setAtomY$I$D(atom, factor * this.mMol.getAtomY$I(atom));
if (coordsAre3D) this.mMol.setAtomZ$I$D(atom, factor * this.mMol.getAtomZ$I(atom));
}
}} else {
if (coordsAre3D && !coordsAreAbsolute && targetAVBL == 0.0   ) targetAVBL=1.5;
if (targetAVBL != 0.0  && this.mMol.getAllBonds$() != 0 ) {
var avbl=0.0;
for (var bond=0; bond < this.mMol.getAllBonds$(); bond++) {
var dx=this.mMol.getAtomX$I(this.mMol.getBondAtom$I$I(0, bond)) - this.mMol.getAtomX$I(this.mMol.getBondAtom$I$I(1, bond));
var dy=this.mMol.getAtomY$I(this.mMol.getBondAtom$I$I(0, bond)) - this.mMol.getAtomY$I(this.mMol.getBondAtom$I$I(1, bond));
var dz=coordsAre3D ? this.mMol.getAtomZ$I(this.mMol.getBondAtom$I$I(0, bond)) - this.mMol.getAtomZ$I(this.mMol.getBondAtom$I$I(1, bond)) : 0.0;
avbl+=Math.sqrt(dx * dx + dy * dy + dz * dz);
}
avbl/=this.mMol.getAllBonds$();
var f=targetAVBL / avbl;
for (var atom=0; atom < this.mMol.getAllAtoms$(); atom++) {
this.mMol.setAtomX$I$D(atom, this.mMol.getAtomX$I(atom) * f + xOffset);
this.mMol.setAtomY$I$D(atom, this.mMol.getAtomY$I(atom) * f + yOffset);
if (coordsAre3D) this.mMol.setAtomZ$I$D(atom, this.mMol.getAtomZ$I(atom) * f + zOffset);
}
}}} catch (e) {
if (Clazz.exceptionOf(e,"Exception")){
e.printStackTrace$();
System.err.println$S("Faulty id-coordinates:" + e.toString() + " " +  String.instantialize(idcode) + " " +  String.instantialize(coordinates) );
coordinates=null;
coordsAre3D=false;
} else {
throw e;
}
}
}var coords2DAvailable=(coordinates != null  && !coordsAre3D );
if (coords2DAvailable || this.ensure2DCoordinates$() ) {
this.mMol.ensureHelperArrays$I(7);
for (var bond=0; bond < this.mMol.getBonds$(); bond++) if (this.mMol.getBondOrder$I(bond) == 2 && !this.mMol.isSmallRingBond$I(bond)  && this.mMol.getBondParity$I(bond) == 0 ) this.mMol.setBondParityUnknownOrNone$I(bond);

}this.mMol.setParitiesValid$I(0);
if (!coords2DAvailable && this.ensure2DCoordinates$() ) {
try {
this.inventCoordinates$com_actelion_research_chem_StereoMolecule(this.mMol);
coords2DAvailable=true;
} catch (e) {
if (Clazz.exceptionOf(e,"Exception")){
e.printStackTrace$();
System.err.println$S("2D-coordinate creation failed:" + e.toString() + " " +  String.instantialize(idcode) );
} else {
throw e;
}
}
}if (coords2DAvailable) {
this.mMol.setStereoBondsFromParity$();
this.mMol.setUnknownParitiesToExplicitlyUnknown$();
} else if (!coordsAre3D) {
this.mMol.setParitiesValid$I(0);
}});

Clazz.newMeth(C$, 'inventCoordinates$com_actelion_research_chem_StereoMolecule',  function (mol) {
throw Clazz.new_(Clazz.load('Exception').c$$S,["Unexpected request to invent coordinates. Check source code logic!"]);
});

Clazz.newMeth(C$, 'parseCoordinates$BA$I$com_actelion_research_chem_StereoMolecule$com_actelion_research_chem_CoordinatesA',  function (encodedCoords, coordsStart, mol, coords) {
mol.ensureHelperArrays$I(1);
var atomCount=mol.getAtoms$();
var bondCount=mol.getBonds$();
p$1.decodeBitsStart$BA$I.apply(this, [encodedCoords, coordsStart + 1]);
var coordsAre3D=(p$1.decodeBits$I.apply(this, [1]) == 1);
var coordsAreAbsolute=(p$1.decodeBits$I.apply(this, [1]) == 1);
var resolutionBits=2 * p$1.decodeBits$I.apply(this, [4]);
var binCount=(1 << resolutionBits);
var factor;
var from=0;
var bond=0;
for (var atom=1; atom < atomCount; atom++) {
if (bond < bondCount && mol.getBondAtom$I$I(1, bond) == atom ) {
from=mol.getBondAtom$I$I(0, bond++);
factor=1.0;
} else {
from=0;
factor=8.0;
}coords[atom].x=coords[from].x + factor * (p$1.decodeBits$I.apply(this, [resolutionBits]) - (binCount/2|0));
coords[atom].y=coords[from].y + factor * (p$1.decodeBits$I.apply(this, [resolutionBits]) - (binCount/2|0));
if (coordsAre3D) coords[atom].z=coords[from].z + factor * (p$1.decodeBits$I.apply(this, [resolutionBits]) - (binCount/2|0));
}
var avbl=coordsAre3D ? 1.5 : $I$(3).getDefaultAverageBondLength$();
if (bondCount != 0) for (var b=0; b < bondCount; b++) avbl+=coords[mol.getBondAtom$I$I(0, b)].distance$com_actelion_research_chem_Coordinates(coords[mol.getBondAtom$I$I(1, b)]);

avbl/=bondCount;
if (encodedCoords[coordsStart] == 35 ) {
var hydrogenCount=0;
var hydrogen=atomCount;
for (var atom=0; atom < atomCount; atom++) {
var hCount=mol.getAllConnAtoms$I(atom) - mol.getConnAtoms$I(atom);
for (var i=0; i < hCount; i++) {
coords[hydrogen].x=coords[atom].x + (p$1.decodeBits$I.apply(this, [resolutionBits]) - (binCount/2|0));
coords[hydrogen].y=coords[atom].y + (p$1.decodeBits$I.apply(this, [resolutionBits]) - (binCount/2|0));
if (coordsAre3D) coords[hydrogen].z=coords[atom].z + (p$1.decodeBits$I.apply(this, [resolutionBits]) - (binCount/2|0));
++hydrogen;
}
hydrogenCount+=hCount;
}
atomCount+=hydrogenCount;
bondCount+=hydrogenCount;
}if (coordsAreAbsolute) {
var targetAVBL=p$1.decodeAVBL$I$I.apply(this, [p$1.decodeBits$I.apply(this, [resolutionBits]), binCount]);
var xOffset=targetAVBL * p$1.decodeShift$I$I.apply(this, [p$1.decodeBits$I.apply(this, [resolutionBits]), binCount]);
var yOffset=targetAVBL * p$1.decodeShift$I$I.apply(this, [p$1.decodeBits$I.apply(this, [resolutionBits]), binCount]);
var zOffset=0;
if (coordsAre3D) zOffset=targetAVBL * p$1.decodeShift$I$I.apply(this, [p$1.decodeBits$I.apply(this, [resolutionBits]), binCount]);
factor=targetAVBL / avbl;
for (var atom=0; atom < atomCount; atom++) {
coords[atom].x=xOffset + factor * coords[atom].x;
coords[atom].y=yOffset + factor * coords[atom].y;
if (coordsAre3D) coords[atom].z=zOffset + factor * coords[atom].z;
}
} else {
var targetAVBL=1.5;
factor=targetAVBL / avbl;
for (var atom=0; atom < atomCount; atom++) {
coords[atom].x=factor * coords[atom].x;
coords[atom].y=factor * coords[atom].y;
if (coordsAre3D) coords[atom].z=factor * coords[atom].z;
}
}});

Clazz.newMeth(C$, 'parseMapping$BA',  function (mapping) {
this.parseMapping$BA$I(mapping, 0);
});

Clazz.newMeth(C$, 'parseMapping$BA$I',  function (mapping, mappingStart) {
if (mapping == null  || mapping.length <= mappingStart  || mapping[mappingStart] < 64 ) return;
p$1.decodeBitsStart$BA$I.apply(this, [mapping, mappingStart]);
var nbits=p$1.decodeBits$I.apply(this, [4]);
var autoMappingFound=(p$1.decodeBits$I.apply(this, [1]) == 1);
var manualMappingFound=(p$1.decodeBits$I.apply(this, [1]) == 1);
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
var mapNo=p$1.decodeBits$I.apply(this, [nbits]);
var autoMapped=autoMappingFound;
if (autoMappingFound && manualMappingFound ) autoMapped=(p$1.decodeBits$I.apply(this, [1]) == 1);
this.mMol.setAtomMapNo$I$I$Z(atom, mapNo, autoMapped);
}
});

Clazz.newMeth(C$, 'coordinatesAre3D$S$S',  function (idcode, coordinates) {
return coordinates != null  && this.coordinatesAre3D$BA$BA(idcode.getBytes$(), coordinates.getBytes$()) ;
});

Clazz.newMeth(C$, 'coordinatesAre3D$BA$BA',  function (idcode, coordinates) {
return this.coordinatesAre3D$BA$BA$I$I(idcode, coordinates, 0, 0);
});

Clazz.newMeth(C$, 'coordinatesAre3D$BA$BA$I$I',  function (idcode, coordinates, idcodeStart, coordsStart) {
if (coordinates == null  || coordinates.length <= coordsStart ) return false;
if (coordinates[coordsStart] == 33  || coordinates[coordsStart] == 35  ) {
p$1.decodeBitsStart$BA$I.apply(this, [coordinates, coordsStart + 1]);
return (p$1.decodeBits$I.apply(this, [1]) == 1);
} else {
var allAtoms=this.getAtomCount$BA$I(idcode, idcodeStart);
return (allAtoms != 0 && coordinates.length >= coordsStart + 3 * allAtoms - 3  && coordinates[coordsStart + 2 * allAtoms - 2] != 39  );
}});

Clazz.newMeth(C$, 'coordinatesAreAbsolute$S',  function (coordinates) {
return coordinates != null  && this.coordinatesAreAbsolute$BA(coordinates.getBytes$()) ;
});

Clazz.newMeth(C$, 'coordinatesAreAbsolute$BA',  function (coordinates) {
return this.coordinatesAreAbsolute$BA$I(coordinates, 0);
});

Clazz.newMeth(C$, 'coordinatesAreAbsolute$BA$I',  function (coordinates, coordStart) {
if (coordinates == null  || coordinates.length <= coordStart ) return false;
if (coordinates[coordStart] >= 39 ) {
for (var i=coordStart; i < coordinates.length; i++) if (coordinates[i] == 39  || coordinates[i] == 38  ) return true;

} else if (coordinates[coordStart] == 33  || coordinates[coordStart] == 35  ) {
p$1.decodeBitsStart$BA$I.apply(this, [coordinates, coordStart + 1]);
p$1.decodeBits$I.apply(this, [1]);
return (p$1.decodeBits$I.apply(this, [1]) == 1);
}return false;
});

Clazz.newMeth(C$, 'getIDCodeVersion$S',  function (idcode) {
if (idcode == null  || idcode.length$() == 0 ) return -1;
return this.getIDCodeVersion$BA(idcode.getBytes$());
});

Clazz.newMeth(C$, 'getIDCodeVersion$BA',  function (idcode) {
var version=8;
p$1.decodeBitsStart$BA$I.apply(this, [idcode, 0]);
var abits=p$1.decodeBits$I.apply(this, [4]);
if (abits > 8) version=abits;
return version;
});

Clazz.newMeth(C$, 'getAtomCount$S',  function (idcode) {
if (idcode == null  || idcode.length$() == 0 ) return 0;
return this.getAtomCount$BA$I(idcode.getBytes$(), 0);
});

Clazz.newMeth(C$, 'getAtomCount$BA$I',  function (idcode, offset) {
if (idcode == null  || idcode.length <= offset ) return 0;
p$1.decodeBitsStart$BA$I.apply(this, [idcode, offset]);
var abits=p$1.decodeBits$I.apply(this, [4]);
var bbits=p$1.decodeBits$I.apply(this, [4]);
if (abits > 8) abits=bbits;
if (abits == 0) return 0;
return p$1.decodeBits$I.apply(this, [abits]);
});

Clazz.newMeth(C$, 'getAtomAndBondCounts$S$IA',  function (idcode, count) {
if (idcode == null  || idcode.length$() == 0 ) return null;
return this.getAtomAndBondCounts$BA$I$IA(idcode.getBytes$(), 0, count);
});

Clazz.newMeth(C$, 'getAtomAndBondCounts$BA$I$IA',  function (idcode, offset, count) {
if (idcode == null  || idcode.length == 0 ) return null;
p$1.decodeBitsStart$BA$I.apply(this, [idcode, 0]);
var abits=p$1.decodeBits$I.apply(this, [4]);
var bbits=p$1.decodeBits$I.apply(this, [4]);
if (abits > 8) abits=bbits;
if (count == null ) count=Clazz.array(Integer.TYPE, [2]);
if (abits == 0) {
count[0]=0;
count[1]=0;
} else {
count[0]=p$1.decodeBits$I.apply(this, [abits]);
count[1]=p$1.decodeBits$I.apply(this, [bbits]);
}return count;
});

Clazz.newMeth(C$, 'decodeBitsStart$BA$I',  function (bytes, offset) {
this.mIDCodeBitsAvail=6;
this.mIDCodeBufferIndex=offset;
this.mDecodingBytes=bytes;
this.mIDCodeTempData=(bytes[this.mIDCodeBufferIndex] & 63) << 11;
}, p$1);

Clazz.newMeth(C$, 'decodeBits$I',  function (bits) {
var allBits=bits;
var data=0;
while (bits != 0){
if (this.mIDCodeBitsAvail == 0) {
this.mIDCodeTempData=(this.mDecodingBytes[++this.mIDCodeBufferIndex] & 63) << 11;
this.mIDCodeBitsAvail=6;
}data|=((65536 & this.mIDCodeTempData) >> (16 - allBits + bits));
this.mIDCodeTempData<<=1;
--bits;
--this.mIDCodeBitsAvail;
}
return data;
}, p$1);

Clazz.newMeth(C$, 'decodeAVBL$I$I',  function (value, binCount) {
return Math.pow(10, Math.log10(2000.0) * value / (binCount - 1) - 1);
}, p$1);

Clazz.newMeth(C$, 'decodeShift$I$I',  function (value, binCount) {
var halfBinCount=(binCount/2|0);
var isNegative=(value >= halfBinCount);
if (isNegative) value-=halfBinCount;
var steepness=(binCount/32|0);
var doubleValue=steepness * value / (halfBinCount - value);
return isNegative ? -doubleValue : doubleValue;
}, p$1);

Clazz.newMeth(C$, 'printContent$BA$BA',  function (idcode, coordinates) {
try {
var version=8;
if (idcode == null  || idcode.length == 0 ) return;
if (coordinates != null  && coordinates.length == 0 ) coordinates=null;
System.out.println$S("idcode: " +  String.instantialize(idcode));
if (coordinates != null ) System.out.println$S("coords: " +  String.instantialize(coordinates));
p$1.decodeBitsStart$BA$I.apply(this, [idcode, 0]);
var abits=p$1.decodeBits$I.apply(this, [4]);
var bbits=p$1.decodeBits$I.apply(this, [4]);
if (abits > 8) {
version=abits;
abits=bbits;
}System.out.println$S("version:" + version);
var allAtoms=p$1.decodeBits$I.apply(this, [abits]);
if (allAtoms == 0) return;
var allBonds=p$1.decodeBits$I.apply(this, [bbits]);
var nitrogens=p$1.decodeBits$I.apply(this, [abits]);
var oxygens=p$1.decodeBits$I.apply(this, [abits]);
var otherAtoms=p$1.decodeBits$I.apply(this, [abits]);
var chargedAtoms=p$1.decodeBits$I.apply(this, [abits]);
System.out.println$S("allAtoms:" + allAtoms + " allBonds:" + allBonds );
if (nitrogens != 0) {
System.out.print$S("nitrogens:");
for (var i=0; i < nitrogens; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]));

System.out.println$();
}if (oxygens != 0) {
System.out.print$S("oxygens:");
for (var i=0; i < oxygens; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]));

System.out.println$();
}if (otherAtoms != 0) {
System.out.print$S("otherAtoms:");
for (var i=0; i < otherAtoms; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]) + ":" + p$1.decodeBits$I.apply(this, [8]) );

System.out.println$();
}if (chargedAtoms != 0) {
System.out.print$S("chargedAtoms:");
for (var i=0; i < chargedAtoms; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]) + ":" + (p$1.decodeBits$I.apply(this, [4]) - 8) );

System.out.println$();
}var closureBonds=1 + allBonds - allAtoms;
var dbits=p$1.decodeBits$I.apply(this, [4]);
var base=0;
var bondAtom=Clazz.array(Integer.TYPE, [2, allBonds]);
var bondCount=0;
for (var i=1; i < allAtoms; i++) {
var dif=p$1.decodeBits$I.apply(this, [dbits]);
if (dif == 0) {
++closureBonds;
continue;
}base+=dif - 1;
bondAtom[0][bondCount]=base;
bondAtom[1][bondCount++]=i;
}
for (var i=0; i < closureBonds; i++) {
bondAtom[0][bondCount]=p$1.decodeBits$I.apply(this, [abits]);
bondAtom[1][bondCount++]=p$1.decodeBits$I.apply(this, [abits]);
}
var bondOrder=Clazz.array(Integer.TYPE, [allBonds]);
System.out.print$S("bonds:");
for (var bond=0; bond < allBonds; bond++) {
System.out.print$S(" " + bondAtom[0][bond]);
bondOrder[bond]=p$1.decodeBits$I.apply(this, [2]);
System.out.print$S(bondOrder[bond] == 0 ? "." : bondOrder[bond] == 1 ? "-" : bondOrder[bond] == 2 ? "=" : "#");
System.out.print$S("" + bondAtom[1][bond]);
}
System.out.println$();
var THCount=p$1.decodeBits$I.apply(this, [abits]);
if (THCount != 0) {
System.out.print$S("parities:");
for (var i=0; i < THCount; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
if (version == 8) {
var parity=p$1.decodeBits$I.apply(this, [2]);
if (parity == 3) {
System.out.print$S(" " + atom + ":1&0" );
} else {
System.out.print$S(" " + atom + ":" + parity );
}} else {
var parity=p$1.decodeBits$I.apply(this, [3]);
switch (parity) {
case 4:
System.out.print$S(" " + atom + ":1&" + p$1.decodeBits$I.apply(this, [3]) );
break;
case 5:
System.out.print$S(" " + atom + ":2&" + p$1.decodeBits$I.apply(this, [3]) );
break;
case 6:
System.out.print$S(" " + atom + ":1|" + p$1.decodeBits$I.apply(this, [3]) );
break;
case 7:
System.out.print$S(" " + atom + ":2|" + p$1.decodeBits$I.apply(this, [3]) );
break;
default:
System.out.print$S(" " + atom + ":" + parity );
}
}}
System.out.println$();
}if (version == 8) if ((p$1.decodeBits$I.apply(this, [1]) == 0)) System.out.println$S("isRacemate");
var EZCount=p$1.decodeBits$I.apply(this, [bbits]);
if (EZCount != 0) {
System.out.print$S("EZ:");
for (var i=0; i < EZCount; i++) {
var bond=p$1.decodeBits$I.apply(this, [bbits]);
if (bondOrder[bond] == 1) {
var parity=p$1.decodeBits$I.apply(this, [3]);
switch (parity) {
case 4:
System.out.print$S(" " + bond + ":1&" + p$1.decodeBits$I.apply(this, [3]) );
break;
case 5:
System.out.print$S(" " + bond + ":2&" + p$1.decodeBits$I.apply(this, [3]) );
break;
case 6:
System.out.print$S(" " + bond + ":1|" + p$1.decodeBits$I.apply(this, [3]) );
break;
case 7:
System.out.print$S(" " + bond + ":2|" + p$1.decodeBits$I.apply(this, [3]) );
break;
default:
System.out.print$S(" " + bond + ":" + parity );
}
} else System.out.print$S(" " + bond + ":" + p$1.decodeBits$I.apply(this, [2]) );
}
System.out.println$();
}if (p$1.decodeBits$I.apply(this, [1]) == 1) System.out.println$S("isFragment = true");
var offset=0;
while (p$1.decodeBits$I.apply(this, [1]) == 1){
var dataType=offset + p$1.decodeBits$I.apply(this, [4]);
switch (dataType) {
case 0:
var no=p$1.decodeBits$I.apply(this, [abits]);
System.out.print$S("noMoreNeighbours:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]));

System.out.println$();
break;
case 1:
no=p$1.decodeBits$I.apply(this, [abits]);
System.out.print$S("mass:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]) + ":" + p$1.decodeBits$I.apply(this, [8]) );

System.out.println$();
break;
case 2:
no=p$1.decodeBits$I.apply(this, [bbits]);
System.out.print$S("delocalizedBonds (outdated, redundant and wrong):");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [bbits]));

System.out.println$();
break;
case 3:
no=p$1.decodeBits$I.apply(this, [abits]);
System.out.print$S("moreNeighbours:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]));

System.out.println$();
break;
case 4:
no=p$1.decodeBits$I.apply(this, [abits]);
System.out.print$S("atomRingState:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]) + ":" + p$1.decodeBits$I.apply(this, [4]) );

System.out.println$();
break;
case 5:
no=p$1.decodeBits$I.apply(this, [abits]);
System.out.print$S("atomAromState:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]) + ":" + p$1.decodeBits$I.apply(this, [2]) );

System.out.println$();
break;
case 6:
no=p$1.decodeBits$I.apply(this, [abits]);
System.out.print$S("atomAny:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]));

System.out.println$();
break;
case 7:
no=p$1.decodeBits$I.apply(this, [abits]);
System.out.print$S("atomHydrogen:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]) + ":" + p$1.decodeBits$I.apply(this, [4]) );

System.out.println$();
break;
case 8:
no=p$1.decodeBits$I.apply(this, [abits]);
System.out.print$S("atomList:");
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
var atoms=p$1.decodeBits$I.apply(this, [4]);
System.out.print$S(" " + atom);
for (var j=0; j < atoms; j++) {
System.out.print$S(j == 0 ? ":" : ",");
System.out.print$S("" + p$1.decodeBits$I.apply(this, [8]));
}
}
System.out.println$();
break;
case 9:
no=p$1.decodeBits$I.apply(this, [bbits]);
System.out.print$S("bondRingState:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [bbits]) + ":" + p$1.decodeBits$I.apply(this, [2]) );

System.out.println$();
break;
case 10:
no=p$1.decodeBits$I.apply(this, [bbits]);
System.out.print$S("bondTypes:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [bbits]) + ":" + p$1.decodeBits$I.apply(this, [5]) );

System.out.println$();
break;
case 11:
no=p$1.decodeBits$I.apply(this, [abits]);
System.out.print$S("atomMatchStereo:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]));

System.out.println$();
break;
case 12:
no=p$1.decodeBits$I.apply(this, [bbits]);
for (var i=0; i < no; i++) {
System.out.print$S("bridgeBond:" + p$1.decodeBits$I.apply(this, [bbits]));
var min=p$1.decodeBits$I.apply(this, [4]);
var max=min + p$1.decodeBits$I.apply(this, [4]);
System.out.println$S("(" + min + "-" + max + ")" );
}
break;
case 13:
no=p$1.decodeBits$I.apply(this, [abits]);
System.out.print$S("atomPiElectrons:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]) + ":" + p$1.decodeBits$I.apply(this, [3]) );

System.out.println$();
break;
case 14:
no=p$1.decodeBits$I.apply(this, [abits]);
System.out.print$S("AtomQFNeighbours:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]) + ":" + p$1.decodeBits$I.apply(this, [5]) );

System.out.println$();
break;
case 15:
case 31:
offset+=16;
System.out.println$S("<start next feature set>");
break;
case 16:
no=p$1.decodeBits$I.apply(this, [abits]);
System.out.print$S("AtomQFSmallRingSize:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]) + ":" + p$1.decodeBits$I.apply(this, [3]) );

System.out.println$();
break;
case 17:
no=p$1.decodeBits$I.apply(this, [abits]);
System.out.print$S("AtomAbnormalValence:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]) + ":" + p$1.decodeBits$I.apply(this, [4]) );

System.out.println$();
break;
case 18:
no=p$1.decodeBits$I.apply(this, [abits]);
System.out.print$S("AtomCustomLabel:");
var lbits=p$1.decodeBits$I.apply(this, [4]);
for (var i=0; i < no; i++) {
var atom=p$1.decodeBits$I.apply(this, [abits]);
var count=p$1.decodeBits$I.apply(this, [lbits]);
var label=Clazz.array(Byte.TYPE, [count]);
for (var j=0; j < count; j++) label[j]=(p$1.decodeBits$I.apply(this, [7])|0);

System.out.print$S(" " + atom + ":" +  String.instantialize(label) );
}
System.out.println$();
break;
case 19:
no=p$1.decodeBits$I.apply(this, [abits]);
System.out.print$S("AtomQFCharge:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]) + ":" + p$1.decodeBits$I.apply(this, [3]) );

System.out.println$();
break;
case 20:
no=p$1.decodeBits$I.apply(this, [bbits]);
System.out.print$S("BondQFRingSize:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [bbits]) + ":" + p$1.decodeBits$I.apply(this, [3]) );

System.out.println$();
break;
case 21:
no=p$1.decodeBits$I.apply(this, [abits]);
System.out.print$S("AtomRadicalState:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]) + ":" + p$1.decodeBits$I.apply(this, [2]) );

System.out.println$();
break;
case 22:
no=p$1.decodeBits$I.apply(this, [abits]);
System.out.print$S("AtomQFFlatNitrogen:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]));

System.out.println$();
break;
case 23:
no=p$1.decodeBits$I.apply(this, [bbits]);
System.out.print$S("cBondQFMatchStereo:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]));

System.out.println$();
break;
case 24:
no=p$1.decodeBits$I.apply(this, [bbits]);
System.out.print$S("BondQFAromState:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [bbits]) + ":" + p$1.decodeBits$I.apply(this, [2]) );

System.out.println$();
break;
case 25:
System.out.print$S("AtomSelection:");
for (var i=0; i < allAtoms; i++) if (p$1.decodeBits$I.apply(this, [1]) == 1) System.out.print$S(" " + i);

System.out.println$();
break;
case 26:
System.out.print$S("DelocalizedHigherOrderBonds:");
no=p$1.decodeBits$I.apply(this, [bbits]);
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [bbits]));

break;
case 27:
no=p$1.decodeBits$I.apply(this, [abits]);
System.out.print$S("AtomQFExcludeGroup:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]));

System.out.println$();
break;
case 28:
no=p$1.decodeBits$I.apply(this, [bbits]);
System.out.print$S("Coordinate Bonds:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [bbits]));

System.out.println$();
break;
case 29:
no=p$1.decodeBits$I.apply(this, [abits]);
System.out.print$S("ReactionParityHint:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]) + ":" + p$1.decodeBits$I.apply(this, [2]) );

System.out.println$();
break;
case 30:
no=p$1.decodeBits$I.apply(this, [abits]);
System.out.print$S("AtomQFNewRingSize:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]) + ":" + p$1.decodeBits$I.apply(this, [7]) );

System.out.println$();
break;
case 32:
no=p$1.decodeBits$I.apply(this, [abits]);
System.out.print$S("AtomQFStereoState:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]) + ":" + p$1.decodeBits$I.apply(this, [2]) );

System.out.println$();
break;
case 33:
no=p$1.decodeBits$I.apply(this, [abits]);
System.out.print$S("AtomQFENeighbours:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]) + ":" + p$1.decodeBits$I.apply(this, [5]) );

System.out.println$();
break;
case 34:
no=p$1.decodeBits$I.apply(this, [abits]);
System.out.print$S("AtomQFHeteroAromatic:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]));

System.out.println$();
break;
case 35:
no=p$1.decodeBits$I.apply(this, [bbits]);
System.out.print$S("BondQFMatchFormalOrder:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [abits]));

System.out.println$();
break;
case 36:
no=p$1.decodeBits$I.apply(this, [bbits]);
System.out.print$S("BondQFRareBondType:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [bbits]) + ":" + p$1.decodeBits$I.apply(this, [2]) );

System.out.println$();
break;
case 37:
no=p$1.decodeBits$I.apply(this, [bbits]);
System.out.print$S("Rare Bond Type:");
for (var i=0; i < no; i++) System.out.print$S(" " + p$1.decodeBits$I.apply(this, [bbits]) + ":" + (p$1.decodeBits$I.apply(this, [1]) == 0 ? "quadruple" : "quintuple") );

break;
}
}
if (coordinates != null ) {
if (coordinates[0] == 33  || coordinates[0] == 35  ) {
p$1.decodeBitsStart$BA$I.apply(this, [coordinates, 1]);
var coordsAre3D=(p$1.decodeBits$I.apply(this, [1]) == 1);
var coordsAreAbsolute=(p$1.decodeBits$I.apply(this, [1]) == 1);
var resolutionBits=2 * p$1.decodeBits$I.apply(this, [4]);
var binCount=(1 << resolutionBits);
var factor;
var hydrogenCount=0;
var hCount=null;
if (coordinates[0] == 35 ) {
var mol=Clazz.new_(C$).getCompactMolecule$BA(idcode);
hCount=Clazz.array(Integer.TYPE, [allAtoms]);
for (var atom=0; atom < allAtoms; atom++) hydrogenCount+=(hCount[atom]=mol.getImplicitHydrogens$I(atom));

}var coords=Clazz.array(Double.TYPE, [coordsAre3D ? 3 : 2, allAtoms + hydrogenCount]);
var from=0;
var bond=0;
System.out.print$S("Raw coords:");
for (var atom=1; atom < allAtoms; atom++) {
if (bond < allBonds && bondAtom[1][bond] == atom ) {
from=bondAtom[0][bond++];
factor=1.0;
} else {
from=0;
factor=8.0;
}System.out.print$S(atom + " (");
coords[0][atom]=coords[0][from] + factor * (p$1.decodeBits$I.apply(this, [resolutionBits]) - (binCount/2|0));
System.out.print$S((coords[0][atom]|0) + ",");
coords[1][atom]=coords[1][from] + factor * (p$1.decodeBits$I.apply(this, [resolutionBits]) - (binCount/2|0));
System.out.print$I((coords[1][atom]|0));
if (coordsAre3D) {
coords[2][atom]=coords[2][from] + factor * (p$1.decodeBits$I.apply(this, [resolutionBits]) - (binCount/2|0));
System.out.print$S("," + (coords[0][atom]|0));
}System.out.print$S("), ");
if ((atom & 3) == 3 || atom == allAtoms - 1 ) System.out.println$();
}
var avbl=0;
if (allBonds != 0) {
for (bond=0; bond < allBonds; bond++) avbl+=p$1.getDistance$DAA$I$I$Z.apply(this, [coords, bondAtom[0][bond], bondAtom[1][bond], coordsAre3D]);

avbl/=allBonds;
} else {
var defaultAVBL=coordsAre3D ? 1.5 : $I$(3).getDefaultAverageBondLength$();
if (allAtoms < 2) {
avbl=defaultAVBL;
} else {
var lowDistance=1.7976931348623157E308;
for (var atom1=1; atom1 < allAtoms; atom1++) {
for (var atom2=0; atom2 < atom1; atom2++) {
var distance=p$1.getDistance$DAA$I$I$Z.apply(this, [coords, atom1, atom2, coordsAre3D]);
if (distance > 0  && distance < lowDistance  ) lowDistance=distance;
}
}
avbl=(lowDistance == 1.7976931348623157E308 ) ? defaultAVBL : lowDistance;
}}if (coordinates[0] == 35 ) {
System.out.print$S("hydrogen coords (" + hydrogenCount + " expected): " );
var hydrogen=allAtoms;
for (var atom=0; atom < allAtoms; atom++) {
if (hCount[atom] != 0) System.out.print$I(atom);
for (var i=0; i < hCount[atom]; i++) {
System.out.print$S(" (");
coords[0][hydrogen]=coords[0][atom] + (p$1.decodeBits$I.apply(this, [resolutionBits]) - (binCount/2|0));
System.out.print$S((coords[0][hydrogen]|0) + ",");
coords[1][hydrogen]=coords[1][atom] + (p$1.decodeBits$I.apply(this, [resolutionBits]) - (binCount/2|0));
System.out.print$I((coords[1][hydrogen]|0));
if (coordsAre3D) {
coords[2][hydrogen]=coords[2][atom] + (p$1.decodeBits$I.apply(this, [resolutionBits]) - (binCount/2|0));
System.out.print$S("," + (coords[2][hydrogen]|0));
}System.out.print$S("), ");
++hydrogen;
}
}
System.out.println$();
}System.out.print$S(coordsAreAbsolute ? "absolute coords:" : "relative coords:");
if (hydrogenCount != 0) System.out.println$S("Coordinates contain " + hydrogenCount + " hydrogen atoms!" );
if (coordsAreAbsolute) {
var targetAVBL=p$1.decodeAVBL$I$I.apply(this, [p$1.decodeBits$I.apply(this, [resolutionBits]), binCount]);
var xOffset=targetAVBL * p$1.decodeShift$I$I.apply(this, [p$1.decodeBits$I.apply(this, [resolutionBits]), binCount]);
var yOffset=targetAVBL * p$1.decodeShift$I$I.apply(this, [p$1.decodeBits$I.apply(this, [resolutionBits]), binCount]);
var zOffset=0;
if (coordsAre3D) zOffset=targetAVBL * p$1.decodeShift$I$I.apply(this, [p$1.decodeBits$I.apply(this, [resolutionBits]), binCount]);
System.out.println$S("Abs-coord transformation: targetAVBL:" + new Double(targetAVBL).toString() + " xOffset:" + new Double(xOffset).toString() + " yOffset:" + new Double(yOffset).toString() + " zOffset:" + new Double(zOffset).toString() );
factor=targetAVBL / avbl;
for (var atom=0; atom < allAtoms; atom++) {
coords[0][atom]=xOffset + factor * coords[0][atom];
coords[1][atom]=xOffset + factor * coords[1][atom];
if (coordsAre3D) coords[2][atom]=xOffset + factor * coords[2][atom];
}
} else {
var targetAVBL=1.5;
factor=targetAVBL / avbl;
for (var atom=0; atom < allAtoms; atom++) {
System.out.print$S(atom + " (");
coords[0][atom]=coords[0][atom] * factor;
System.out.print$S($I$(4).toString$D(coords[0][atom]) + ",");
coords[1][atom]=coords[1][atom] * factor;
System.out.print$S($I$(4).toString$D(coords[1][atom]));
if (coordsAre3D) {
coords[2][atom]=coords[2][atom] * factor;
System.out.print$S("," + $I$(4).toString$D(coords[2][atom]));
}System.out.print$S("), ");
if ((atom & 3) == 3 || atom == allAtoms - 1 ) System.out.println$();
}
}}}System.out.println$();
} catch (e) {
if (Clazz.exceptionOf(e,"Exception")){
e.printStackTrace$();
} else {
throw e;
}
}
});

Clazz.newMeth(C$, 'getDistance$DAA$I$I$Z',  function (coords, atom1, atom2, coordsAre3D) {
var dx=coords[0][atom1] - coords[0][atom2];
var dy=coords[1][atom1] - coords[1][atom2];
var dz=coordsAre3D ? coords[2][atom1] - coords[2][atom2] : 0;
return Math.sqrt(dx * dx + dy * dy + dz * dz);
}, p$1);

Clazz.newMeth(C$);
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-25 13:07:44 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
