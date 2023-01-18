(function(){var P$=Clazz.newPackage("com.actelion.research.chem"),p$1={},I$=[[0,'com.actelion.research.chem.StereoMolecule','com.actelion.research.chem.Molecule','java.io.BufferedReader','java.io.InputStreamReader','java.io.FileInputStream','com.actelion.research.io.BOMSkipper','java.io.StringReader','java.io.FileReader','java.util.TreeMap','com.actelion.research.chem.AromaticityResolver']],I$0=I$[0],$I$=function(i,n){return((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "MolfileParser");

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['Z',['mTreatAnyAsMetalBond','mDeduceMissingCharges','mChiralFlag','mIsV3000','mAssumeChiralTrue'],'I',['mMode'],'O',['mMol','com.actelion.research.chem.StereoMolecule','mAtomIndexMap','java.util.TreeMap','+mBondIndexMap','mHydrogenMap','int[]']]
,['Z',['debug']]]

Clazz.newMeth(C$, 'c$',  function () {
;C$.$init$.apply(this);
this.mMode=0;
}, 1);

Clazz.newMeth(C$, 'c$$I',  function (mode) {
;C$.$init$.apply(this);
this.mMode=mode;
}, 1);

Clazz.newMeth(C$, 'getHandleHydrogenMap$',  function () {
return this.mHydrogenMap == null  ? this.mMol.getHandleHydrogenMap$() : this.mHydrogenMap;
});

Clazz.newMeth(C$, 'readMoleculeFromBuffer$java_io_BufferedReader',  function (reader) {
var valence=null;
try {
var line;
var natoms;
var nbonds;
var nlists;
this.mHydrogenMap=null;
if (this.mMol != null ) {
this.mMol.clear$();
this.mMol.setFragment$Z(false);
}var name=(line=reader.readLine$());
if (null == name ) {
this.TRACE$S("readMoleculeFromBuffer: No Header Line\n");
return false;
}if (null == (line=reader.readLine$()) ) {
this.TRACE$S("Error [readMoleculeFromBuffer]: No Program Line\n");
return false;
}if (null == (line=reader.readLine$()) ) {
this.TRACE$S("Error [readMoleculeFromBuffer]: No Comment Line\n");
return false;
}this.mTreatAnyAsMetalBond=line.contains$CharSequence("From CSD data. Using bond type \'Any\'");
this.mDeduceMissingCharges=line.contains$CharSequence("From CSD data.");
if (null == (line=reader.readLine$()) ) {
this.TRACE$S("Error [readMoleculeFromBuffer]: No Counts Line\n");
return false;
}this.mIsV3000=false;
this.mChiralFlag=this.mAssumeChiralTrue;
try {
natoms=Integer.parseInt$S(line.substring$I$I(0, 3).trim$());
nbonds=Integer.parseInt$S(line.substring$I$I(3, 6).trim$());
nlists=p$1.parseIntOrSpaces$S.apply(this, [line.substring$I$I(6, 9).trim$()]);
this.mChiralFlag=!!(this.mChiralFlag|((1 == p$1.parseIntOrSpaces$S.apply(this, [line.substring$I$I(12, 15).trim$()]))));
this.mIsV3000=(line.length$() >= 39 && line.startsWith$S$I("V3000", 34) );
} catch (e) {
if (Clazz.exceptionOf(e,"Exception")){
this.TRACE$S("Warning [readMoleculeFromBuffer]: Unable to interpret counts line\n");
return false;
} else {
throw e;
}
}
if (this.mIsV3000) {
var res=p$1.readMoleculeV3FromBuffer$java_io_BufferedReader.apply(this, [reader]);
this.mMol.setName$S(name);
return res;
}if (this.mMol == null ) {
this.mMol=Clazz.new_($I$(1,1).c$$I$I,[natoms, nbonds]);
}this.mMol.setName$S(name);
if (!this.mChiralFlag) {
this.mMol.setToRacemate$();
}if (0 == natoms) {
while (line != null  && (!(line.equals$O("M  END") || line.equals$O("$$$$") || line.substring$I(1).equals$O("$")  )) ){
line=reader.readLine$();
}
return true;
}for (var i=0; i < natoms; i++) {
if (null == (line=reader.readLine$()) ) {
this.TRACE$S("Error [readMoleculeFromBuffer]: No Atom Line\n");
return false;
}var x=Float.parseFloat$S(line.substring$I$I(0, 10).trim$());
var y=Float.parseFloat$S(line.substring$I$I(10, 20).trim$());
var z=Float.parseFloat$S(line.substring$I$I(20, 30).trim$());
var atom=this.mMol.addAtom$D$D$D(x, -y, -z);
var label=line.substring$I$I(31, 34).trim$();
if (label.equals$O("A") || label.equals$O("*") ) {
this.mMol.setAtomQueryFeature$I$J$Z(atom, 1, true);
} else if (label.equals$O("Q")) {
var list=Clazz.array(Integer.TYPE, [1]);
list[0]=6;
this.mMol.setAtomList$I$IA$Z(atom, list, true);
} else {
var atomicNo=$I$(2).getAtomicNoFromLabel$S$I(label, 67);
this.mMol.setAtomicNo$I$I(atom, atomicNo);
}var massDif=p$1.parseIntOrSpaces$S.apply(this, [line.substring$I$I(34, 36).trim$()]);
if (massDif != 0) {
this.mMol.setAtomMass$I$I(atom, $I$(2).cRoundedMass[this.mMol.getAtomicNo$I(atom)] + massDif);
}var chargeDif=p$1.parseIntOrSpaces$S.apply(this, [line.substring$I$I(36, 39).trim$()]);
if (chargeDif != 0) {
if (chargeDif == 4) this.mMol.setAtomRadical$I$I(atom, 32);
 else this.mMol.setAtomCharge$I$I(atom, 4 - chargeDif);
}var mapNo=(line.length$() < 63) ? 0 : p$1.parseIntOrSpaces$S.apply(this, [line.substring$I$I(60, 63).trim$()]);
this.mMol.setAtomMapNo$I$I$Z(atom, mapNo, false);
var hCount=(line.length$() < 45) ? 0 : p$1.parseIntOrSpaces$S.apply(this, [line.substring$I$I(42, 45).trim$()]);
switch (hCount) {
case 0:
break;
case 1:
this.mMol.setAtomQueryFeature$I$J$Z(atom, 768, true);
break;
case 2:
this.mMol.setAtomQueryFeature$I$J$Z(atom, 128, true);
break;
case 3:
this.mMol.setAtomQueryFeature$I$J$Z(atom, 384, true);
break;
default:
this.mMol.setAtomQueryFeature$I$J$Z(atom, 896, true);
break;
}
if (line.length$() >= 48 && line.charAt$I(47) == "1" ) {
this.mMol.setAtomQueryFeature$I$J$Z(atom, 8192, true);
}var v=(line.length$() < 51) ? 0 : p$1.parseIntOrSpaces$S.apply(this, [line.substring$I$I(48, 51).trim$()]);
if (v != 0) {
if (valence == null ) valence=Clazz.array(Integer.TYPE, [natoms]);
valence[atom]=v;
}}
for (var i=0; i < nbonds; i++) {
if (null == (line=reader.readLine$()) ) {
this.TRACE$S("Error [readMoleculeFromBuffer]:No Bond Line\n");
return false;
}var atom1=Integer.parseInt$S(line.substring$I$I(0, 3).trim$()) - 1;
var atom2=Integer.parseInt$S(line.substring$I$I(3, 6).trim$()) - 1;
var bondType=Integer.parseInt$S(line.substring$I$I(6, 9).trim$());
var stereo=(line.length$() < 12) ? 0 : p$1.parseIntOrSpaces$S.apply(this, [line.substring$I$I(9, 12).trim$()]);
var topology=(line.length$() < 18) ? 0 : p$1.parseIntOrSpaces$S.apply(this, [line.substring$I$I(15, 18).trim$()]);
if (bondType == 8 && (this.mTreatAnyAsMetalBond || this.mMol.isMetalAtom$I(atom1) || this.mMol.isMetalAtom$I(atom2)  ) ) bondType=9;
p$1.buildBond$I$I$I$I$I.apply(this, [atom1, atom2, bondType, stereo, topology]);
}
for (var i=0; i < nlists; i++) {
if (null == (line=reader.readLine$()) ) {
this.TRACE$S("Error [readMoleculeFromBuffer]: No List Line\n");
return false;
}}
if (null == (line=reader.readLine$()) ) {
this.TRACE$S("Error ReadMoleculeFromBuffer Missing M END or $$$$\n");
if ((this.mMode & 1) != 0) this.mHydrogenMap=this.mMol.getHandleHydrogenMap$();
p$1.handleValences$IA.apply(this, [valence]);
if (!this.mChiralFlag) this.mMol.ensureHelperArrays$I(15);
return true;
}while (line != null  && (!(line.equals$O("M  END") || line.equals$O("$$$$") )) ){
if (line.startsWith$S("M  CHG")) {
var aaa;
var vvv;
var j=Integer.parseInt$S(line.substring$I$I(6, 9).trim$());
if (j > 0) {
aaa=10;
vvv=14;
for (var k=1; k <= j; k++, aaa+=8, vvv+=8) {
var atom=Integer.parseInt$S(line.substring$I$I(aaa, aaa + 3).trim$()) - 1;
var charge=Integer.parseInt$S(line.substring$I$I(vvv, vvv + 3).trim$());
this.mMol.setAtomCharge$I$I(atom, charge);
}
}}if (line.startsWith$S("M  ISO")) {
var aaa;
var vvv;
var j=Integer.parseInt$S(line.substring$I$I(6, 9).trim$());
if (j > 0) {
aaa=10;
vvv=14;
for (var k=1; k <= j; k++, aaa+=8, vvv+=8) {
var atom=Integer.parseInt$S(line.substring$I$I(aaa, aaa + 3).trim$()) - 1;
var mass=Integer.parseInt$S(line.substring$I$I(vvv, vvv + 3).trim$());
this.mMol.setAtomMass$I$I(atom, mass);
}
}}if (line.startsWith$S("M  RAD")) {
var aaa;
var vvv;
var j=Integer.parseInt$S(line.substring$I$I(6, 9).trim$());
if (j > 0) {
aaa=10;
vvv=14;
for (var k=1; k <= j; k++, aaa+=8, vvv+=8) {
var atom=Integer.parseInt$S(line.substring$I$I(aaa, aaa + 3).trim$()) - 1;
var radical=Integer.parseInt$S(line.substring$I$I(vvv, vvv + 3).trim$());
switch (radical) {
case 1:
this.mMol.setAtomRadical$I$I(atom, 16);
break;
case 2:
this.mMol.setAtomRadical$I$I(atom, 32);
break;
case 3:
this.mMol.setAtomRadical$I$I(atom, 48);
break;
}
}
}}if (line.startsWith$S("M  RBC") || line.startsWith$S("M  RBD") ) {
var j=Integer.parseInt$S(line.substring$I$I(6, 9).trim$());
if (j > 0) {
var aaa=10;
var vvv=14;
for (var k=1; k <= j; k++, aaa+=8, vvv+=8) {
var atom=Integer.parseInt$S(line.substring$I$I(aaa, aaa + 3).trim$()) - 1;
var ringState=Integer.parseInt$S(line.substring$I$I(vvv, vvv + 3).trim$());
switch (ringState) {
case -1:
this.mMol.setAtomQueryFeature$I$J$Z(atom, 112, true);
break;
case 1:
this.mMol.setAtomQueryFeature$I$J$Z(atom, 8, true);
break;
case 2:
this.mMol.setAtomQueryFeature$I$J$Z(atom, 104, true);
break;
case 3:
this.mMol.setAtomQueryFeature$I$J$Z(atom, 112, true);
break;
case 4:
this.mMol.setAtomQueryFeature$I$J$Z(atom, 56, true);
break;
}
}
}}if (line.startsWith$S("M  ALS")) {
var atom=Integer.parseInt$S(line.substring$I$I(7, 10).trim$()) - 1;
if (atom >= 0) {
var no=Integer.parseInt$S(line.substring$I$I(10, 13).trim$());
var bNotList=(line.charAt$I(14) == "T");
var v=Clazz.array(Integer.TYPE, [no]);
var aaa=16;
for (var k=0; k < no; k++, aaa+=4) {
var sym=line.substring$I$I(aaa, aaa + 4).trim$();
v[k]=$I$(2).getAtomicNoFromLabel$S$I(sym, 1);
}
this.mMol.setAtomicNo$I$I(atom, 6);
this.mMol.setAtomList$I$IA$Z(atom, v, bNotList);
}}if (line.startsWith$S("M  SUB")) {
var aaa;
var vvv;
var j=Integer.parseInt$S(line.substring$I$I(6, 9).trim$());
if (j > 0) {
aaa=10;
vvv=14;
for (var k=1; k <= j; k++, aaa+=8, vvv+=8) {
var atom=Integer.parseInt$S(line.substring$I$I(aaa, aaa + 3).trim$()) - 1;
var substitution=Integer.parseInt$S(line.substring$I$I(vvv, vvv + 3).trim$());
if (substitution == -2) {
this.mMol.setAtomQueryFeature$I$J$Z(atom, 2048, true);
} else if (substitution > 0) {
var substitutionCount=0;
for (var bond=0; bond < this.mMol.getAllBonds$(); bond++) {
if (this.mMol.getBondAtom$I$I(0, bond) == atom || this.mMol.getBondAtom$I$I(1, bond) == atom ) {
++substitutionCount;
}}
if (substitution > substitutionCount) {
this.mMol.setAtomQueryFeature$I$J$Z(atom, 4096, true);
}}}
}}if (line.startsWith$S("M  RGP")) {
var aaa;
var vvv;
var j=Integer.parseInt$S(line.substring$I$I(6, 9).trim$());
if (j > 0) {
aaa=10;
vvv=14;
for (var k=1; k <= j; k++, aaa+=8, vvv+=8) {
var atom=Integer.parseInt$S(line.substring$I$I(aaa, aaa + 3).trim$()) - 1;
var rno=Integer.parseInt$S(line.substring$I$I(vvv, vvv + 3).trim$());
if (rno >= 1 && rno <= 20 ) {
this.mMol.setAtomicNo$I$I(atom, $I$(2).getAtomicNoFromLabel$S$I("R" + rno, 2));
}}
}}line=reader.readLine$();
}
} catch (e) {
if (Clazz.exceptionOf(e,"Exception")){
e.printStackTrace$();
System.err.println$S("error reading molfile " + e);
return false;
} else {
throw e;
}
}
if (this.mDeduceMissingCharges) {
p$1.introduceObviousMetalBonds.apply(this, []);
p$1.deduceMissingCharges.apply(this, []);
}if ((this.mMode & 1) != 0) this.mHydrogenMap=this.mMol.getHandleHydrogenMap$();
p$1.handleValences$IA.apply(this, [valence]);
this.mMol.ensureHelperArrays$I(15);
return true;
}, p$1);

Clazz.newMeth(C$, 'setAssumeChiralTrue$Z',  function (b) {
this.mAssumeChiralTrue=b;
});

Clazz.newMeth(C$, 'isChiralFlagSet$',  function () {
return this.mChiralFlag;
});

Clazz.newMeth(C$, 'isV3000$',  function () {
return this.mIsV3000;
});

Clazz.newMeth(C$, 'handleValences$IA',  function (valence) {
if (valence != null ) {
this.mMol.ensureHelperArrays$I(1);
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (valence[atom] != 0) {
var chargeCorrection=this.mMol.getElectronValenceCorrection$I$I(atom, this.mMol.getOccupiedValence$I(atom));
if (valence[atom] == 15) {
if (chargeCorrection >= 0) this.mMol.setAtomAbnormalValence$I$I(atom, 0);
} else {
if (valence[atom] != this.mMol.getMaxValence$I(atom)) this.mMol.setAtomAbnormalValence$I$I(atom, valence[atom] - chargeCorrection);
}}}
}}, p$1);

Clazz.newMeth(C$, 'readMoleculeV3FromBuffer$java_io_BufferedReader',  function (reader) {
var MODE_CTAB=1;
var MODE_CTAB_ATOM=2;
var MODE_CTAB_BOND=3;
var MODE_CTAB_COLLECTION=4;
if (this.mAtomIndexMap != null ) this.mAtomIndexMap.clear$();
if (this.mBondIndexMap != null ) this.mBondIndexMap.clear$();
var mode=0;
var line=reader.readLine$();
while (line != null  && line.startsWith$S("M  V30 ") ){
line=line.substring$I(7).trim$();
while (line.endsWith$S("-")){
var cont=reader.readLine$();
if (!cont.startsWith$S("M  V30 ")) {
return false;
}line=line.substring$I$I(0, line.length$() - 1).concat$S(cont.substring$I(7)).trim$();
}
if (line.startsWith$S("BEGIN")) {
var modeString=line.substring$I(6).trim$();
if (modeString.startsWith$S("CTAB")) {
mode=1;
} else if (modeString.startsWith$S("ATOM")) {
mode=2;
} else if (modeString.startsWith$S("BOND")) {
mode=3;
} else if (modeString.startsWith$S("COLLECTION")) {
mode=4;
} else {
this.TRACE$S("Error MolfileParser: Unsupported version 3 block\n");
return false;
}} else if (line.startsWith$S("END")) {
mode=0;
} else if (mode == 1) {
p$1.interpretV3CountLine$S.apply(this, [line]);
} else if (mode == 2) {
p$1.interpretV3AtomLine$S.apply(this, [line]);
} else if (mode == 3) {
p$1.interpretV3BondLine$S.apply(this, [line]);
} else if (mode == 4) {
p$1.interpretV3CollectionLine$S.apply(this, [line]);
} else {
this.TRACE$S("Error MolfileParser: Unexpected version 3 line\n");
return false;
}line=reader.readLine$();
}
while (line != null  && (!(line.startsWith$S("M  END") || line.equals$O("$$$$") )) ){
line=reader.readLine$();
}
return true;
}, p$1);

Clazz.newMeth(C$, 'interpretV3CountLine$S',  function (line) {
if (this.mMol == null ) {
if (line.startsWith$S("COUNTS")) {
var index1=7;
var index2=p$1.indexOfNextItem$S$I.apply(this, [line, p$1.indexOfWhiteSpace$S$I.apply(this, [line, 7])]);
var natoms=Integer.parseInt$S(line.substring$I$I(index1, p$1.indexOfWhiteSpace$S$I.apply(this, [line, index1])));
var nbonds=Integer.parseInt$S(line.substring$I$I(index2, p$1.indexOfWhiteSpace$S$I.apply(this, [line, index2])));
this.mMol=Clazz.new_($I$(1,1).c$$I$I,[natoms, nbonds]);
}}}, p$1);

Clazz.newMeth(C$, 'interpretV3AtomLine$S',  function (line) {
var index1=0;
var index2=p$1.endOfItem$S$I.apply(this, [line, index1]);
var atomIndex=Integer.parseInt$S(line.substring$I$I(index1, index2));
index1=p$1.indexOfNextItem$S$I.apply(this, [line, index2]);
index2=p$1.endOfItem$S$I.apply(this, [line, index1]);
var label=line.substring$I$I(index1, index2);
var v=null;
var bNotList=false;
var l=p$1.isV3AtomList$S.apply(this, [line]);
if (l != 0) {
v=p$1.interpretV3AtomList$S.apply(this, [line]);
if (l < 0) bNotList=true;
index2=Math.abs(l);
}index1=p$1.indexOfNextItem$S$I.apply(this, [line, index2]);
index2=p$1.endOfItem$S$I.apply(this, [line, index1]);
var x=Float.parseFloat$S(line.substring$I$I(index1, index2));
index1=p$1.indexOfNextItem$S$I.apply(this, [line, index2]);
index2=p$1.endOfItem$S$I.apply(this, [line, index1]);
var y=Float.parseFloat$S(line.substring$I$I(index1, index2));
index1=p$1.indexOfNextItem$S$I.apply(this, [line, index2]);
index2=p$1.endOfItem$S$I.apply(this, [line, index1]);
var z=Float.parseFloat$S(line.substring$I$I(index1, index2));
index1=p$1.indexOfNextItem$S$I.apply(this, [line, index2]);
index2=p$1.endOfItem$S$I.apply(this, [line, index1]);
var mapNo=Integer.parseInt$S(line.substring$I$I(index1, index2));
var atom=this.mMol.addAtom$D$D$D(x, -y, -z);
if (atom + 1 != atomIndex) p$1.mapAtomIndex$I$I.apply(this, [atomIndex, atom]);
if (v != null ) {
this.mMol.setAtomicNo$I$I(atom, 6);
this.mMol.setAtomList$I$IA$Z(atom, v, bNotList);
}if (mapNo != 0) {
this.mMol.setAtomMapNo$I$I$Z(atom, mapNo, false);
}if (label.equals$O("A") || label.equals$O("*") ) {
this.mMol.setAtomQueryFeature$I$J$Z(atom, 1, true);
} else if (label.equals$O("Q")) {
var list=Clazz.array(Integer.TYPE, [1]);
list[0]=6;
this.mMol.setAtomList$I$IA$Z(atom, list, true);
} else {
this.mMol.setAtomicNo$I$I(atom, $I$(2).getAtomicNoFromLabel$S$I(label, 67));
}while ((index1=p$1.indexOfNextItem$S$I.apply(this, [line, index2])) != -1){
index2=p$1.endOfItem$S$I.apply(this, [line, index1]);
var specifier=line.substring$I$I(index1, index2);
var index=specifier.indexOf$I("=");
var field=specifier.substring$I$I(0, index);
var value=Integer.parseInt$S(specifier.substring$I(index + 1));
if (field.equals$O("CHG")) {
this.mMol.setAtomCharge$I$I(atom, value);
} else if (field.equals$O("RAD")) {
switch (value) {
case 1:
this.mMol.setAtomRadical$I$I(atom, 16);
break;
case 2:
this.mMol.setAtomRadical$I$I(atom, 32);
break;
case 3:
this.mMol.setAtomRadical$I$I(atom, 48);
break;
}
} else if (field.equals$O("CFG")) {
} else if (field.equals$O("MASS")) {
this.mMol.setAtomMass$I$I(atom, value);
} else if (field.equals$O("VAL")) {
this.mMol.setAtomAbnormalValence$I$I(atom, (value == -1) ? 0 : (value == 0) ? -1 : value);
} else if (field.equals$O("HCOUNT")) {
switch (value) {
case 0:
break;
case -1:
this.mMol.setAtomQueryFeature$I$J$Z(atom, 1792, true);
break;
case 1:
this.mMol.setAtomQueryFeature$I$J$Z(atom, 128, true);
break;
case 2:
this.mMol.setAtomQueryFeature$I$J$Z(atom, 384, true);
break;
default:
this.mMol.setAtomQueryFeature$I$J$Z(atom, 896, true);
break;
}
} else if (field.equals$O("SUBST")) {
if (value == -1) {
this.mMol.setAtomQueryFeature$I$J$Z(atom, 2048, true);
} else if (value > 0) {
var substitutionCount=0;
for (var bond=0; bond < this.mMol.getAllBonds$(); bond++) {
if (this.mMol.getBondAtom$I$I(0, bond) == atom || this.mMol.getBondAtom$I$I(1, bond) == atom ) {
++substitutionCount;
}}
if (value > substitutionCount) {
this.mMol.setAtomQueryFeature$I$J$Z(atom, 4096, true);
}}} else if (field.equals$O("RBCNT")) {
switch (value) {
case -1:
this.mMol.setAtomQueryFeature$I$J$Z(atom, 112, true);
break;
case 1:
this.mMol.setAtomQueryFeature$I$J$Z(atom, 8, true);
break;
case 2:
this.mMol.setAtomQueryFeature$I$J$Z(atom, 104, true);
break;
case 3:
this.mMol.setAtomQueryFeature$I$J$Z(atom, 112, true);
break;
case 4:
this.mMol.setAtomQueryFeature$I$J$Z(atom, 56, true);
break;
}
} else {
this.TRACE$S("Warning MolfileParser: Unused version 3 atom specifier:" + field + "\n" );
}}
}, p$1);

Clazz.newMeth(C$, 'interpretV3BondLine$S',  function (line) {
var index1=0;
var index2=p$1.endOfItem$S$I.apply(this, [line, index1]);
var bondIndex=Integer.parseInt$S(line.substring$I$I(index1, index2));
index1=p$1.indexOfNextItem$S$I.apply(this, [line, index2]);
index2=p$1.endOfItem$S$I.apply(this, [line, index1]);
var bondType=Integer.parseInt$S(line.substring$I$I(index1, index2));
index1=p$1.indexOfNextItem$S$I.apply(this, [line, index2]);
index2=p$1.endOfItem$S$I.apply(this, [line, index1]);
var atom1=p$1.getUsedAtomIndex$I.apply(this, [Integer.parseInt$S(line.substring$I$I(index1, index2))]);
index1=p$1.indexOfNextItem$S$I.apply(this, [line, index2]);
index2=p$1.endOfItem$S$I.apply(this, [line, index1]);
var atom2=p$1.getUsedAtomIndex$I.apply(this, [Integer.parseInt$S(line.substring$I$I(index1, index2))]);
var stereo=0;
var topology=0;
while ((index1=p$1.indexOfNextItem$S$I.apply(this, [line, index2])) != -1){
index2=p$1.endOfItem$S$I.apply(this, [line, index1]);
var specifier=line.substring$I$I(index1, index2);
var index=specifier.indexOf$I("=");
var field=specifier.substring$I$I(0, index);
var value=Integer.parseInt$S(specifier.substring$I(index + 1));
if (field.equals$O("CFG")) {
switch (value) {
case 1:
stereo=1;
break;
case 2:
stereo=(bondType == 2) ? 3 : 4;
break;
case 3:
stereo=6;
break;
}
} else if (field.equals$O("TOPO")) {
topology=value;
} else {
this.TRACE$S("Warning MolfileParser: Unused version 3 bond specifier:" + field + "\n" );
}}
var bond=p$1.buildBond$I$I$I$I$I.apply(this, [atom1, atom2, bondType, stereo, topology]);
if (bond + 1 != bondIndex) p$1.mapBondIndex$I$I.apply(this, [bondIndex, bond]);
}, p$1);

Clazz.newMeth(C$, 'interpretV3CollectionLine$S',  function (line) {
var objectType=p$1.interpretObjectType$S.apply(this, [line]);
if (objectType != null ) {
var list=p$1.interpretV3List$S$S.apply(this, [line, objectType]);
if (line.startsWith$S("MDLV30/STEABS")) {
if (objectType.equals$O("ATOMS")) for (var i=0; i < list.length; i++) this.mMol.setAtomESR$I$I$I(p$1.getUsedAtomIndex$I.apply(this, [list[i]]), 0, -1);

 else for (var i=0; i < list.length; i++) this.mMol.setBondESR$I$I$I(p$1.getUsedBondIndex$I.apply(this, [list[i]]), 0, -1);

} else if (line.startsWith$S("MDLV30/STERAC")) {
var group=Integer.parseInt$S(line.substring$I$I(13, p$1.indexOfWhiteSpace$S$I.apply(this, [line, 13])));
if (objectType.equals$O("ATOMS")) for (var i=0; i < list.length; i++) this.mMol.setAtomESR$I$I$I(p$1.getUsedAtomIndex$I.apply(this, [list[i]]), 1, group - 1);

 else for (var i=0; i < list.length; i++) this.mMol.setBondESR$I$I$I(p$1.getUsedBondIndex$I.apply(this, [list[i]]), 1, group - 1);

} else if (line.startsWith$S("MDLV30/STEREL")) {
var group=Integer.parseInt$S(line.substring$I$I(13, p$1.indexOfWhiteSpace$S$I.apply(this, [line, 13])));
if (objectType.equals$O("ATOMS")) for (var i=0; i < list.length; i++) this.mMol.setAtomESR$I$I$I(p$1.getUsedAtomIndex$I.apply(this, [list[i]]), 2, group - 1);

 else for (var i=0; i < list.length; i++) this.mMol.setBondESR$I$I$I(p$1.getUsedBondIndex$I.apply(this, [list[i]]), 2, group - 1);

} else if (line.startsWith$S("MDLV30/HILITE")) {
if (objectType.equals$O("ATOMS")) {
for (var i=0; i < list.length; i++) this.mMol.setAtomColor$I$I(p$1.getUsedAtomIndex$I.apply(this, [list[i]]), 448);

} else {
for (var i=0; i < list.length; i++) {
var bond=p$1.getUsedBondIndex$I.apply(this, [list[i]]);
this.mMol.setAtomColor$I$I(this.mMol.getBondAtom$I$I(0, bond), 448);
this.mMol.setAtomColor$I$I(this.mMol.getBondAtom$I$I(1, bond), 448);
}
}} else {
this.TRACE$S("Error [readMoleculeFromBuffer]: Unknown version 3 collection type\n");
}}}, p$1);

Clazz.newMeth(C$, 'interpretObjectType$S',  function (line) {
if (line.contains$CharSequence("ATOMS=(")) return "ATOMS";
if (line.contains$CharSequence("BONDS=(")) return "BONDS";
this.TRACE$S("Error [readMoleculeFromBuffer]: Unknown or missing collection object type\n");
return null;
}, p$1);

Clazz.newMeth(C$, 'interpretV3AtomList$S',  function (line) {
var res=null;
var i1=line.indexOf$S("[");
var i2=line.indexOf$S$I("]", i1);
if (i1 >= 0 && i2 > 0 ) {
var atoms=Clazz.array(Integer.TYPE, [16]);
var s=line.substring$I$I(i1 + 1, i2);
var index=0;
var ok=true;
while (ok && index < 16 ){
i1=s.indexOf$S(",");
var l=null;
if (i1 == -1) {
l=s;
ok=false;
} else {
l=s.substring$I$I(0, i1);
s=s.substring$I(i1 + 1);
}atoms[index++]=$I$(2).getAtomicNoFromLabel$S$I(l, 1);
}
res=Clazz.array(Integer.TYPE, [index]);
System.arraycopy$O$I$O$I$I(atoms, 0, res, 0, index);
}return res;
}, p$1);

Clazz.newMeth(C$, 'isV3AtomList$S',  function (line) {
if (line.indexOf$S("[") >= 0) {
var i1=line.indexOf$S(" NOT[");
var i2=line.indexOf$S$I("]", i1);
if (i1 >= 0 && i2 > 0 ) {
return -(i2 + 1);
} else {
i1=line.indexOf$S(" [");
i2=line.indexOf$S$I("]", i1);
if (i1 >= 0 && i2 > 0 ) {
return i2 + 1;
}}i1=line.indexOf$S(" \'NOT[");
i2=line.indexOf$S$I("]\'", i1);
if (i1 >= 0 && i2 > 0 ) {
return -(i2 + 2);
} else {
i1=line.indexOf$S(" \'[");
i2=line.indexOf$S$I("]\'", i1);
if (i1 >= 0 && i2 > 0 ) {
return i2 + 2;
}}System.err.println$S("Warning invalid atom list in line: " + line);
}return 0;
}, p$1);

Clazz.newMeth(C$, 'interpretV3List$S$S',  function (line, type) {
var index1=line.indexOf$S(type + "=(") + type.length$() + 2 ;
var index2=line.indexOf$I$I(")", index1);
var index=p$1.indexOfWhiteSpace$S$I.apply(this, [line, index1]);
var count=Integer.parseInt$S(line.substring$I$I(index1, index));
var list=Clazz.array(Integer.TYPE, [count]);
for (var i=0; i < count; i++) {
index1=p$1.indexOfNextItem$S$I.apply(this, [line, index]);
index=p$1.indexOfWhiteSpace$S$I.apply(this, [line, index1]);
if (index == -1 || index > index2 ) {
index=index2;
}list[i]=Integer.parseInt$S(line.substring$I$I(index1, index));
}
return list;
}, p$1);

Clazz.newMeth(C$, 'parse$com_actelion_research_chem_StereoMolecule$java_io_File',  function (mol, file) {
this.mMol=mol;
try {
var reader=Clazz.new_([Clazz.new_([Clazz.new_($I$(5,1).c$$java_io_File,[file]), "UTF-8"],$I$(4,1).c$$java_io_InputStream$S)],$I$(3,1).c$$java_io_Reader);
$I$(6).skip$java_io_Reader(reader);
return p$1.readMoleculeFromBuffer$java_io_BufferedReader.apply(this, [reader]);
} catch (e) {
if (Clazz.exceptionOf(e,"java.io.IOException")){
System.err.println$S("Error reading file " + e);
} else {
throw e;
}
}
return false;
});

Clazz.newMeth(C$, 'parse$com_actelion_research_chem_StereoMolecule$S',  function (mol, molFile) {
return this.parse$com_actelion_research_chem_StereoMolecule$java_io_BufferedReader(mol, Clazz.new_([Clazz.new_($I$(7,1).c$$S,[molFile])],$I$(3,1).c$$java_io_Reader));
});

Clazz.newMeth(C$, 'parse$com_actelion_research_chem_StereoMolecule$StringBuffer',  function (mol, molFile) {
return this.parse$com_actelion_research_chem_StereoMolecule$S(mol, molFile.toString());
});

Clazz.newMeth(C$, 'parse$com_actelion_research_chem_StereoMolecule$java_io_BufferedReader',  function (m, rd) {
this.mMol=m;
return p$1.readMoleculeFromBuffer$java_io_BufferedReader.apply(this, [rd]);
});

Clazz.newMeth(C$, 'getCompactMolecule$S',  function (molFile) {
this.mMol=null;
return p$1.readMoleculeFromBuffer$java_io_BufferedReader.apply(this, [Clazz.new_([Clazz.new_($I$(7,1).c$$S,[molFile])],$I$(3,1).c$$java_io_Reader)]) ? this.mMol : null;
});

Clazz.newMeth(C$, 'getCompactMolecule$java_io_BufferedReader',  function (reader) {
this.mMol=null;
return (p$1.readMoleculeFromBuffer$java_io_BufferedReader.apply(this, [reader])) ? this.mMol : null;
});

Clazz.newMeth(C$, 'getCompactMolecule$java_io_File',  function (file) {
this.mMol=null;
try {
var reader=Clazz.new_([Clazz.new_($I$(8,1).c$$java_io_File,[file])],$I$(3,1).c$$java_io_Reader);
var success=p$1.readMoleculeFromBuffer$java_io_BufferedReader.apply(this, [reader]);
try {
reader.close$();
} catch (ioe) {
if (Clazz.exceptionOf(ioe,"java.io.IOException")){
} else {
throw ioe;
}
}
return success ? this.mMol : null;
} catch (fnfe) {
if (Clazz.exceptionOf(fnfe,"java.io.FileNotFoundException")){
return null;
} else {
throw fnfe;
}
}
});

Clazz.newMeth(C$, 'buildBond$I$I$I$I$I',  function (atom1, atom2, bondType, stereo, topology) {
var realBondType=1;
var isAtomESRAnd=false;
switch (stereo) {
case 1:
realBondType=257;
break;
case 3:
realBondType=386;
break;
case 4:
realBondType=257;
isAtomESRAnd=true;
break;
case 6:
realBondType=129;
break;
default:
switch (bondType) {
case 1:
realBondType=1;
break;
case 2:
realBondType=2;
break;
case 3:
realBondType=4;
break;
case 4:
realBondType=64;
break;
case 9:
realBondType=32;
break;
}
break;
}
var bond=this.mMol.addBond$I$I$I(atom1, atom2, realBondType);
var queryFeatures=0;
if (isAtomESRAnd) {
this.mMol.setAtomESR$I$I$I(atom1, 1, -1);
}if (bondType > 4) {
switch (bondType) {
case 5:
queryFeatures|=3;
break;
case 6:
queryFeatures|=9;
break;
case 7:
queryFeatures|=10;
break;
case 8:
if (realBondType != 32) queryFeatures|=31;
break;
}
}if (topology == 1) {
queryFeatures|=256;
}if (topology == 2) {
queryFeatures|=128;
}if (queryFeatures != 0) {
this.mMol.setBondQueryFeature$I$I$Z(bond, queryFeatures, true);
}return bond;
}, p$1);

Clazz.newMeth(C$, 'mapAtomIndex$I$I',  function (sourceAtomIndex, usedAtomIndex) {
if (this.mAtomIndexMap == null ) this.mAtomIndexMap=Clazz.new_($I$(9,1));
this.mAtomIndexMap.put$O$O( new Integer(sourceAtomIndex),  new Integer(usedAtomIndex));
}, p$1);

Clazz.newMeth(C$, 'mapBondIndex$I$I',  function (sourceBondIndex, usedBondIndex) {
if (this.mBondIndexMap == null ) this.mBondIndexMap=Clazz.new_($I$(9,1));
this.mBondIndexMap.put$O$O( new Integer(sourceBondIndex),  new Integer(usedBondIndex));
}, p$1);

Clazz.newMeth(C$, 'getUsedAtomIndex$I',  function (sourceAtomIndex) {
var ui=(this.mAtomIndexMap == null ) ? null : this.mAtomIndexMap.get$O( new Integer(sourceAtomIndex));
return (ui == null ) ? sourceAtomIndex - 1 : ui.intValue$();
}, p$1);

Clazz.newMeth(C$, 'getUsedBondIndex$I',  function (sourceBondIndex) {
var ui=(this.mBondIndexMap == null ) ? null : this.mBondIndexMap.get$O( new Integer(sourceBondIndex));
return (ui == null ) ? sourceBondIndex - 1 : ui.intValue$();
}, p$1);

Clazz.newMeth(C$, 'parseIntOrSpaces$S',  function (s) {
return (s.length$() == 0) ? 0 : Integer.parseInt$S(s);
}, p$1);

Clazz.newMeth(C$, 'endOfItem$S$I',  function (line, start) {
var end=p$1.indexOfWhiteSpace$S$I.apply(this, [line, start + 1]);
return (end == -1) ? line.length$() : end;
}, p$1);

Clazz.newMeth(C$, 'indexOfWhiteSpace$S$I',  function (line, fromIndex) {
for (var i=fromIndex; i < line.length$(); i++) {
if (line.charAt$I(i) == " " || line.charAt$I(i) == "\t" ) {
return i;
}}
return -1;
}, p$1);

Clazz.newMeth(C$, 'indexOfNextItem$S$I',  function (line, afterPreviousItem) {
if (afterPreviousItem == -1) {
return -1;
}for (var i=afterPreviousItem + 1; i < line.length$(); i++) {
if (line.charAt$I(i) != " " && line.charAt$I(i) != "\t" ) {
return i;
}}
return -1;
}, p$1);

Clazz.newMeth(C$, 'TRACE$S',  function (s) {
if (C$.debug) {
System.out.println$S(s);
}});

Clazz.newMeth(C$, 'introduceObviousMetalBonds',  function () {
var occupiedValence=Clazz.array(Integer.TYPE, [this.mMol.getAllAtoms$()]);
for (var bond=0; bond < this.mMol.getAllBonds$(); bond++) if (this.mMol.getBondType$I(bond) == 64) for (var i=0; i < 2; i++) occupiedValence[this.mMol.getBondAtom$I$I(i, bond)]=1;


for (var bond=0; bond < this.mMol.getAllBonds$(); bond++) {
var order=this.mMol.getBondOrder$I(bond);
for (var i=0; i < 2; i++) occupiedValence[this.mMol.getBondAtom$I$I(i, bond)]+=order;

}
for (var bond=0; bond < this.mMol.getAllBonds$(); bond++) {
if (this.mMol.getBondOrder$I(bond) == 1) {
for (var i=0; i < 2; i++) {
var metalAtom=this.mMol.getBondAtom$I$I(1 - i, bond);
if (this.mMol.isMetalAtom$I(metalAtom)) {
var atom=this.mMol.getBondAtom$I$I(i, bond);
if (this.mMol.isElectronegative$I(atom) && occupiedValence[atom] > this.mMol.getMaxValence$I(atom) ) {
this.mMol.setBondType$I$I(bond, 32);
continue;
}}}
}}
}, p$1);

Clazz.newMeth(C$, 'deduceMissingCharges',  function () {
var chargeChange=Clazz.array(Integer.TYPE, [this.mMol.getAllAtoms$()]);
for (var atom=0; atom < this.mMol.getAllAtoms$(); atom++) chargeChange[atom]=-this.mMol.getAtomCharge$I(atom);

Clazz.new_($I$(10,1).c$$com_actelion_research_chem_ExtendedMolecule,[this.mMol]).locateDelocalizedDoubleBonds$ZA$Z$Z(null, true, false);
for (var atom=0; atom < this.mMol.getAllAtoms$(); atom++) chargeChange[atom]+=this.mMol.getAtomCharge$I(atom);

for (var atom=0; atom < this.mMol.getAllAtoms$(); atom++) {
if (chargeChange[atom] != 0) {
var chargeToDistribute=-chargeChange[atom];
for (var bond=0; bond < this.mMol.getAllBonds$(); bond++) {
for (var i=0; i < 2; i++) {
if (chargeToDistribute > 0 && this.mMol.getBondType$I(bond) == 32  && this.mMol.getBondAtom$I$I(1 - i, bond) == atom ) {
var metal=this.mMol.getBondAtom$I$I(i, bond);
if (this.mMol.isMetalAtom$I(metal)) {
var maxCharge=p$1.getMaxOxidationState$I.apply(this, [metal]);
var charge=this.mMol.getAtomCharge$I(metal);
if (charge < maxCharge) {
var dif=Math.min(chargeToDistribute, maxCharge - charge);
this.mMol.setAtomCharge$I$I(metal, charge + dif);
chargeToDistribute-=dif;
}}}}
}
}}
}, p$1);

Clazz.newMeth(C$, 'getMaxOxidationState$I',  function (metal) {
var atomicNo=this.mMol.getAtomicNo$I(metal);
var os=(atomicNo < $I$(2).cCommonOxidationState.length) ? $I$(2).cCommonOxidationState[atomicNo] : null;
return (os == null ) ? ($b$[0] = 0, $b$[0]) : os[os.length - 1];
}, p$1);

C$.$static$=function(){C$.$static$=0;
C$.debug=false;
};
var $b$ = new Int8Array(1);
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-18 09:54:15 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
