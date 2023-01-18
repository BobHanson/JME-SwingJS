(function(){var P$=Clazz.newPackage("com.actelion.research.chem"),p$1={},p$2={},p$3={},I$=[[0,'java.util.ArrayList',['com.actelion.research.chem.SmilesParser','.ParityNeighbour'],'com.actelion.research.chem.StereoMolecule','com.actelion.research.util.ArrayUtils','com.actelion.research.chem.reaction.Reaction','com.actelion.research.util.SortedList','com.actelion.research.chem.SmilesRange','com.actelion.research.chem.Molecule','java.util.TreeMap',['com.actelion.research.chem.SmilesParser','.THParity'],'java.util.Arrays','com.actelion.research.chem.coords.CoordinateInventor','StringBuilder','com.actelion.research.chem.RingCollection','com.actelion.research.chem.IsomericSmilesCreator','com.actelion.research.chem.Canonizer']],I$0=I$[0],$I$=function(i,n,m){return m?$I$(i)[n].apply(null,m):((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "SmilesParser", function(){
Clazz.newInstance(this, arguments,0,C$);
});
C$.$classes$=[['ParityNeighbour',2],['THParity',2]];

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['Z',['mCreateSmartsWarnings','mMakeHydrogenExplicit','mAllowCactvs'],'I',['mAromaticAtoms','mAromaticBonds','mSmartsMode','mCoordinateMode'],'J',['mRandomSeed'],'O',['mMol','com.actelion.research.chem.StereoMolecule','mIsAromaticBond','boolean[]','mSmartsWarningBuffer','StringBuilder']]]

Clazz.newMeth(C$, 'c$',  function () {
C$.c$$I$Z.apply(this, [0, false]);
}, 1);

Clazz.newMeth(C$, 'c$$I$Z',  function (mode, createSmartsWarnings) {
;C$.$init$.apply(this);
this.mSmartsMode=mode & 3;
this.mAllowCactvs=(mode & 16) == 0;
this.mCreateSmartsWarnings=createSmartsWarnings;
this.mMakeHydrogenExplicit=((mode & 8) != 0);
this.mCoordinateMode=2;
if ((mode & 4) != 0) this.mCoordinateMode|=1;
if (this.mMakeHydrogenExplicit) this.mCoordinateMode&=~2;
}, 1);

Clazz.newMeth(C$, 'setRandomSeed$J',  function (seed) {
this.mRandomSeed=seed;
});

Clazz.newMeth(C$, 'parseMolecule$S',  function (smiles) {
return smiles == null  ? null : this.parseMolecule$BA(smiles.getBytes$());
});

Clazz.newMeth(C$, 'parseMolecule$BA',  function (smiles) {
var mol=Clazz.new_($I$(3,1));
try {
this.parse$com_actelion_research_chem_StereoMolecule$BA(mol, smiles);
} catch (e) {
if (Clazz.exceptionOf(e,"Exception")){
return null;
} else {
throw e;
}
}
return mol;
});

Clazz.newMeth(C$, 'parseReaction$S',  function (smiles) {
return smiles == null  ? null : this.parseReaction$BA(smiles.getBytes$());
});

Clazz.newMeth(C$, 'parseReaction$BA',  function (smiles) {
var index1=$I$(4).indexOf$BA$B(smiles, 62);
var index2=(index1 == -1) ? -1 : $I$(4).indexOf$BA$B$I(smiles, 62, index1 + 1);
if (index2 == -1) throw Clazz.new_(Clazz.load('Exception').c$$S,["Missing one or both separators (\'>\')."]);
if ($I$(4).indexOf$BA$B$I(smiles, 62, index2 + 1) != -1) throw Clazz.new_(Clazz.load('Exception').c$$S,["Found more than 2 separators (\'>\')."]);
var rxn=Clazz.new_($I$(5,1));
var start=0;
for (var i=start; i < index1 - 1; i++) {
if (smiles[i] == 46  && smiles[i + 1] == 46  ) {
if (i > start) {
var reactant=Clazz.new_($I$(3,1));
this.parse$com_actelion_research_chem_StereoMolecule$BA$I$I(reactant, smiles, start, i);
rxn.addReactant$com_actelion_research_chem_StereoMolecule(reactant);
}start=i + 2;
}}
var reactants=Clazz.new_($I$(3,1));
this.parse$com_actelion_research_chem_StereoMolecule$BA$I$I(reactants, smiles, start, index1);
rxn.addReactant$com_actelion_research_chem_StereoMolecule(reactants);
if (index2 - index1 > 1) {
start=index1 + 1;
for (var i=start; i < index2 - 1; i++) {
if (smiles[i] == 46  && smiles[i + 1] == 46  ) {
if (i > start) {
var catalyst=Clazz.new_($I$(3,1));
this.parse$com_actelion_research_chem_StereoMolecule$BA$I$I(catalyst, smiles, start, i);
rxn.addCatalyst$com_actelion_research_chem_StereoMolecule(catalyst);
}start=i + 2;
}}
var catalysts=Clazz.new_($I$(3,1));
this.parse$com_actelion_research_chem_StereoMolecule$BA$I$I(catalysts, smiles, start, index2);
rxn.addCatalyst$com_actelion_research_chem_StereoMolecule(catalysts);
}start=index2 + 1;
for (var i=start; i < smiles.length - 1; i++) {
if (smiles[i] == 46  && smiles[i + 1] == 46  ) {
if (i > start) {
var product=Clazz.new_($I$(3,1));
this.parse$com_actelion_research_chem_StereoMolecule$BA$I$I(product, smiles, start, i);
rxn.addProduct$com_actelion_research_chem_StereoMolecule(product);
}start=i + 2;
}}
var products=Clazz.new_($I$(3,1));
this.parse$com_actelion_research_chem_StereoMolecule$BA$I$I(products, smiles, start, smiles.length);
rxn.addProduct$com_actelion_research_chem_StereoMolecule(products);
return rxn;
});

Clazz.newMeth(C$, 'getSmartsWarning$',  function () {
return this.mSmartsWarningBuffer == null  ? "" : "Unresolved SMARTS features:" + this.mSmartsWarningBuffer;
});

Clazz.newMeth(C$, 'parse$com_actelion_research_chem_StereoMolecule$S',  function (mol, smiles) {
this.parse$com_actelion_research_chem_StereoMolecule$BA$Z$Z(mol, smiles.getBytes$(), true, true);
});

Clazz.newMeth(C$, 'parse$com_actelion_research_chem_StereoMolecule$BA',  function (mol, smiles) {
this.parse$com_actelion_research_chem_StereoMolecule$BA$Z$Z(mol, smiles, true, true);
});

Clazz.newMeth(C$, 'parse$com_actelion_research_chem_StereoMolecule$BA$I$I',  function (mol, smiles, position, endIndex) {
this.parse$com_actelion_research_chem_StereoMolecule$BA$I$I$Z$Z(mol, smiles, position, endIndex, true, true);
});

Clazz.newMeth(C$, 'parse$com_actelion_research_chem_StereoMolecule$BA$Z$Z',  function (mol, smiles, createCoordinates, readStereoFeatures) {
this.parse$com_actelion_research_chem_StereoMolecule$BA$I$I$Z$Z(mol, smiles, 0, smiles.length, createCoordinates, readStereoFeatures);
});

Clazz.newMeth(C$, 'parse$com_actelion_research_chem_StereoMolecule$BA$I$I$Z$Z',  function (mol, smiles, position, endIndex, createCoordinates, readStereoFeatures) {
this.mMol=mol;
this.mMol.clear$();
if (this.mSmartsWarningBuffer != null ) this.mSmartsWarningBuffer.setLength$I(0);
this.mAromaticAtoms=0;
var allowSmarts=(this.mSmartsMode != 0);
var parityMap=null;
var baseAtom=Clazz.array(Integer.TYPE, [32]);
baseAtom[0]=-1;
var ringClosureAtom=Clazz.array(Integer.TYPE, [16]);
var ringClosurePosition=Clazz.array(Integer.TYPE, [16]);
var ringClosureBondType=Clazz.array(Integer.TYPE, [16]);
var ringClosureBondQueryFeatures=Clazz.array(Integer.TYPE, [16]);
for (var i=0; i < 16; i++) ringClosureAtom[i]=-1;

var atomMass=0;
var fromAtom=-1;
var squareBracketOpen=false;
var isDoubleDigit=false;
var smartsFeatureFound=false;
var bracketLevel=0;
var bondType=1;
var bondQueryFeatures=0;
var atomList=Clazz.new_($I$(6,1));
var range=Clazz.new_($I$(7,1).c$$BA,[smiles]);
while (smiles[position] <= 32)++position;

while (position < endIndex){
var theChar=String.fromCharCode(smiles[position++]);
if (Character.isLetter$C(theChar) || theChar == "*"  || theChar == "?"  || (theChar == "!" && allowSmarts  && squareBracketOpen )  || (theChar == "#" && allowSmarts  && squareBracketOpen ) ) {
var atomicNo=-1;
var charge=0;
var mapNo=0;
var abnormalValence=-1;
var explicitHydrogens=-1;
var parityFound=false;
var isClockwise=false;
var atomQueryFeatures=0;
if (squareBracketOpen) {
if (theChar == "*") {
atomicNo=6;
atomQueryFeatures=Long.$ival(Long.$or(atomQueryFeatures,(1)));
} else if (theChar == "?") {
atomicNo=0;
} else if (theChar == "#") {
var number=0;
while (position < endIndex && Character.isDigit$I(smiles[position]) ){
number=10 * number + smiles[position] - 48;
++position;
}
if (number < 1 || number >= $I$(8).cAtomLabel.length ) throw Clazz.new_(Clazz.load('Exception').c$$S,["SmilesParser: Atomic number out of range."]);
atomicNo=number;
} else {
var isNot=(theChar == "!");
if (isNot) {
smartsFeatureFound=true;
atomQueryFeatures=Long.$ival(Long.$or(atomQueryFeatures,(1)));
++position;
}if (smiles[position - 1] == 82  && allowSmarts  && (Character.isDigit$I(smiles[position]) || (this.mAllowCactvs && smiles[position] == 123  ) ) ) {
atomicNo=6;
atomQueryFeatures=Long.$ival(Long.$or(atomQueryFeatures,(1)));
--position;
if (isNot) --position;
} else {
var labelLength=Character.isLowerCase$I(smiles[position]) ? 2 : 1;
atomicNo=$I$(8,"getAtomicNoFromLabel$S",[ String.instantialize(smiles, position - 1, labelLength)]);
if (atomicNo == -1) {
atomicNo=6;
atomQueryFeatures=Long.$ival(Long.$or(atomQueryFeatures,(1)));
--position;
} else {
position+=labelLength - 1;
explicitHydrogens=9;
if (allowSmarts && (smiles[position] == 44  || isNot ) ) {
var upperCaseFound=false;
var lowerCaseFound=false;
var start=position - labelLength;
for (var p=start; p < smiles.length; p++) {
if (!Character.isLetter$I(smiles[p])) {
var no=$I$(8,"getAtomicNoFromLabel$S",[ String.instantialize(smiles, start, p - start)]);
if (no != 0) {
atomList.add$O(Integer.valueOf$I(no));
if (Character.isUpperCase$I(smiles[start])) upperCaseFound=true;
 else lowerCaseFound=true;
}start=p + 1;
if (smiles[p] != 44 ) break;
if (smiles[p + 1] == 33 ) {
if (!isNot) throw Clazz.new_(Clazz.load('Exception').c$$S,["SmilesParser: inconsistent \'!\' in atom list."]);
++p;
++start;
}}}
if (atomList.size$() > 1) {
explicitHydrogens=-1;
if (!upperCaseFound) atomQueryFeatures=Long.$ival(Long.$or(atomQueryFeatures,(2)));
 else if (!lowerCaseFound) atomQueryFeatures=Long.$ival(Long.$or(atomQueryFeatures,(4)));
}position=start - 1;
}}}}while (squareBracketOpen){
if (smiles[position] == 64 ) {
++position;
if (smiles[position] == 64 ) {
isClockwise=true;
++position;
}parityFound=true;
continue;
}if (smiles[position] == 58 ) {
++position;
while (Character.isDigit$I(smiles[position])){
mapNo=10 * mapNo + smiles[position] - 48;
++position;
}
continue;
}if (smiles[position] == 91 ) throw Clazz.new_(Clazz.load('Exception').c$$S,["SmilesParser: nested square brackets found"]);
if (smiles[position] == 93 ) {
++position;
squareBracketOpen=false;
continue;
}if (smiles[position] == 43 ) {
charge=1;
++position;
while (smiles[position] == 43 ){
++charge;
++position;
}
if (charge == 1 && Character.isDigit$I(smiles[position]) ) {
charge=smiles[position] - 48;
++position;
}if (charge == 0) atomQueryFeatures=Long.$ival(Long.$or(atomQueryFeatures,(167772160)));
continue;
}if (smiles[position] == 45 ) {
charge=-1;
++position;
while (smiles[position] == 45 ){
--charge;
++position;
}
if (charge == -1 && Character.isDigit$I(smiles[position]) ) {
charge=48 - smiles[position];
++position;
}if (charge == 0) atomQueryFeatures=Long.$ival(Long.$or(atomQueryFeatures,(167772160)));
continue;
}var isNot=(smiles[position] == 33 );
if (isNot) ++position;
if (smiles[position] == 72 ) {
++position;
position+=range.parse$BA$I$I$I(smiles, position, 1, 1);
explicitHydrogens=range.min;
var flags=0;
if (range.min <= 0 && range.max >= 0 ) flags=Long.$ival(Long.$or(flags,(128)));
if (range.min <= 1 && range.max >= 1 ) flags=Long.$ival(Long.$or(flags,(256)));
if (range.min <= 2 && range.max >= 2 ) flags=Long.$ival(Long.$or(flags,(512)));
if (range.min <= 3 && range.max >= 3 ) flags=Long.$ival(Long.$or(flags,(1024)));
if (isNot) {
atomQueryFeatures|=flags;
explicitHydrogens=-1;
} else {
if (range.isSingle$()) {
explicitHydrogens=range.min;
} else {
atomQueryFeatures=Long.$ival(Long.$or(atomQueryFeatures,((Long.$and(1920,~flags)))));
explicitHydrogens=-1;
}}continue;
}if (smiles[position] == 68 ) {
++position;
position+=range.parse$BA$I$I$I(smiles, position, 1, 1);
var flags=0;
if (range.min <= 0 && range.max >= 0 ) (flags=Long.$or(flags,(131072)));
if (range.min <= 1 && range.max >= 1 ) (flags=Long.$or(flags,(262144)));
if (range.min <= 2 && range.max >= 2 ) (flags=Long.$or(flags,(524288)));
if (range.min <= 3 && range.max >= 3 ) (flags=Long.$or(flags,(1048576)));
if (range.min <= 4 && range.max >= 4 ) (flags=Long.$or(flags,(2097152)));
if (Long.$ne(flags,0 )) {
if (!isNot) flags=Long.$xor(flags,4063232);
atomQueryFeatures=Long.$ival(Long.$or(atomQueryFeatures,(flags)));
}continue;
}if (smiles[position] == 122  && this.mAllowCactvs ) {
++position;
position+=range.parse$BA$I$I$I(smiles, position, 1, 4);
var flags=0;
if (range.min <= 0 && range.max >= 0 ) (flags=Long.$or(flags,(549755813888)));
if (range.min <= 1 && range.max >= 1 ) (flags=Long.$or(flags,(1099511627776)));
if (range.min <= 2 && range.max >= 2 ) (flags=Long.$or(flags,(2199023255552)));
if (range.min <= 3 && range.max >= 3 ) (flags=Long.$or(flags,(4398046511104)));
if (range.min <= 4 && range.max >= 4 ) (flags=Long.$or(flags,(8796093022208)));
if (Long.$ne(flags,0 )) {
if (!isNot) flags=Long.$xor(flags,17042430230528);
atomQueryFeatures=Long.$ival(Long.$or(atomQueryFeatures,(flags)));
}continue;
}if (smiles[position] == 88 ) {
++position;
position+=range.parse$BA$I$I$I(smiles, position, 1, 1);
var valences=$I$(8).cAtomValence[atomicNo];
if (valences == null ) continue;
var valence=valences[0];
var flags=0;
if (valence - range.min <= 0 && valence - range.max >= 0 ) (flags=Long.$or(flags,(16384)));
if (valence - range.min <= 1 && valence - range.max >= 1 ) (flags=Long.$or(flags,(32768)));
if (valence - range.min <= 2 && valence - range.max >= 2 ) (flags=Long.$or(flags,(65536)));
if (Long.$ne(flags,0 )) {
if (!isNot) flags=Long.$xor(flags,114688);
atomQueryFeatures=Long.$ival(Long.$or(atomQueryFeatures,(flags)));
}continue;
}if (smiles[position] == 65  || smiles[position] == 97  ) {
++position;
atomQueryFeatures=Long.$ival(Long.$or(atomQueryFeatures,((!!(isNot ^ smiles[position] == 65 )) ? 4 : 2)));
continue;
}if (smiles[position] == 82 ) {
++position;
position+=range.parse$BA$I$I$I(smiles, position, 1, 3);
var flags=0;
if (range.min <= 0 && range.max >= 0 ) (flags=Long.$or(flags,(8)));
if (range.min <= 1 && range.max >= 1 ) (flags=Long.$or(flags,(16)));
if (range.min <= 2 && range.max >= 2 ) (flags=Long.$or(flags,(32)));
if (range.min <= 3 && range.max >= 3 ) (flags=Long.$or(flags,(64)));
if (range.max > 3) p$2.smartsWarning$S.apply(this, [(isNot ? "!R" : "R") + range.max]);
if (Long.$ne(flags,0 )) {
if (!isNot) flags=Long.$xor(flags,120);
atomQueryFeatures=Long.$ival(Long.$or(atomQueryFeatures,(flags)));
}continue;
}if (smiles[position] == 114 ) {
++position;
position+=range.parse$BA$I$I$I(smiles, position, 1, 1);
if (range.isDefault) {
if (isNot) atomQueryFeatures=Long.$ival(Long.$or(atomQueryFeatures,(384)));
 else atomQueryFeatures=Long.$ival(Long.$or(atomQueryFeatures,(8)));
continue;
}var ringSize=range.min;
if (range.isRange$()) p$2.smartsWarning$S.apply(this, [(isNot ? "!r" : "r") + range.toString()]);
if (!isNot && ringSize >= 3  && ringSize <= 7 ) atomQueryFeatures|=(ringSize << 22);
 else if (!range.isRange$()) p$2.smartsWarning$S.apply(this, [(isNot ? "!r" : "r") + ringSize]);
continue;
}if (smiles[position] == 118 ) {
++position;
position+=range.parse$BA$I$I$I(smiles, position, 1, 1);
var valence=range.min;
if (range.isRange$()) p$2.smartsWarning$S.apply(this, [(isNot ? "!v" : "v") + range.toString()]);
if (!isNot && valence <= 14 ) abnormalValence=valence;
 else if (!range.isRange$()) p$2.smartsWarning$S.apply(this, [(isNot ? "!v" : "v") + valence]);
continue;
}if (allowSmarts && (smiles[position] == 59  || smiles[position] == 38  ) ) {
smartsFeatureFound=true;
++position;
continue;
}throw Clazz.new_(Clazz.load('Exception').c$$S,["SmilesParser: unexpected character inside brackets: '" + String.fromCharCode(smiles[position]) + "'" ]);
}
} else if (theChar == "*") {
atomicNo=6;
atomQueryFeatures=Long.$ival(Long.$or(atomQueryFeatures,(1)));
} else if (theChar == "?") {
atomicNo=0;
} else if ((theChar == "A" || theChar == "a" ) && allowSmarts ) {
atomicNo=6;
atomQueryFeatures=Long.$ival(Long.$or(atomQueryFeatures,(1)));
atomQueryFeatures=Long.$ival(Long.$or(atomQueryFeatures,(theChar == "A" ? 4 : 2)));
smartsFeatureFound=true;
} else {
switch ((Character.toUpperCase$C(theChar)).$c()) {
case 66:
if (position < endIndex && smiles[position] == 114  ) {
atomicNo=35;
++position;
} else atomicNo=5;
break;
case 67:
if (position < endIndex && smiles[position] == 108  ) {
atomicNo=17;
++position;
} else atomicNo=6;
break;
case 70:
atomicNo=9;
break;
case 73:
atomicNo=53;
break;
case 78:
atomicNo=7;
break;
case 79:
atomicNo=8;
break;
case 80:
atomicNo=15;
break;
case 83:
atomicNo=16;
break;
}
}if (atomicNo == -1 && theChar != "?" ) throw Clazz.new_(Clazz.load('Exception').c$$S,["SmilesParser: unknown element label found"]);
var atom=this.mMol.addAtom$I(atomicNo);
this.mMol.setAtomCharge$I$I(atom, charge);
this.mMol.setAtomMapNo$I$I$Z(atom, mapNo, false);
this.mMol.setAtomAbnormalValence$I$I(atom, abnormalValence);
if (atomQueryFeatures != 0) {
smartsFeatureFound=true;
if (Long.$ne((Long.$and(atomQueryFeatures,2)),0 )) {
atomQueryFeatures=Long.$ival(Long.$and(atomQueryFeatures,((Long.$not(2)))));
this.mMol.setAtomMarker$I$Z(atom, true);
++this.mAromaticAtoms;
} else {
this.mMol.setAtomMarker$I$Z(atom, false);
}this.mMol.setAtomQueryFeature$I$J$Z(atom, atomQueryFeatures, true);
}if (atomList.size$() != 0) {
smartsFeatureFound=true;
var list=Clazz.array(Integer.TYPE, [atomList.size$()]);
for (var i=0; i < atomList.size$(); i++) list[i]=(atomList.get$I(i)).$c();

this.mMol.setAtomList$I$IA(atom, list);
atomList.removeAll$();
} else {
if (Character.isLowerCase$C(theChar)) {
if (atomicNo != 5 && atomicNo != 6  && atomicNo != 7  && atomicNo != 8  && atomicNo != 15  && atomicNo != 16  && atomicNo != 33  && atomicNo != 34 ) throw Clazz.new_(Clazz.load('Exception').c$$S,["SmilesParser: atomicNo " + atomicNo + " must not be aromatic" ]);
this.mMol.setAtomMarker$I$Z(atom, true);
++this.mAromaticAtoms;
} else {
this.mMol.setAtomMarker$I$Z(atom, false);
}}if (explicitHydrogens != -1 && atomicNo != 1 ) {
var bytes=Clazz.array(Byte.TYPE, [1]);
bytes[0]=((explicitHydrogens == 9 ? 0 : explicitHydrogens)|0);
this.mMol.setAtomCustomLabel$I$BA(atom, bytes);
}fromAtom=baseAtom[bracketLevel];
if (baseAtom[bracketLevel] != -1 && bondType != 512 ) {
var bond=this.mMol.addBond$I$I$I(baseAtom[bracketLevel], atom, bondType);
if (bondQueryFeatures != 0) {
smartsFeatureFound=true;
this.mMol.setBondQueryFeature$I$I$Z(bond, bondQueryFeatures, true);
}}bondType=1;
bondQueryFeatures=0;
baseAtom[bracketLevel]=atom;
if (atomMass != 0) {
this.mMol.setAtomMass$I$I(atom, atomMass);
atomMass=0;
}if (readStereoFeatures) {
var parity=(parityMap == null ) ? null : parityMap.get$O(Integer.valueOf$I(fromAtom));
if (parity != null ) parity.addNeighbor$I$I$Z(atom, position, atomicNo == 1 && atomMass == 0 );
if (parityFound) {
if (parityMap == null ) parityMap=Clazz.new_($I$(9,1));
var hydrogenCount=(explicitHydrogens == 9) ? 0 : explicitHydrogens;
parityMap.put$O$O(Integer.valueOf$I(atom), Clazz.new_($I$(10,1).c$$I$I$I$I$I$Z,[this, null, atom, position - 2, fromAtom, hydrogenCount, position - 1, isClockwise]));
}}continue;
}if (theChar == ".") {
baseAtom[bracketLevel]=-1;
bondType=512;
continue;
}if (p$2.isBondSymbol$C.apply(this, [theChar])) {
if (squareBracketOpen) throw Clazz.new_(Clazz.load('Exception').c$$S,["SmilesParser: unexpected bond symbol inside square brackets: '" + theChar + "'" ]);
var excludedBonds=0;
while (p$2.isBondSymbol$C.apply(this, [theChar])){
if (theChar == "!") {
theChar=String.fromCharCode(smiles[position++]);
if (theChar == "@") bondQueryFeatures|=128;
if ((theChar == "-" && smiles[position] == 62  ) || (theChar == "<" && smiles[position] == 45  ) ) {
excludedBonds|=32;
++position;
} else if (theChar == "-") excludedBonds|=1;
 else if (theChar == "=") excludedBonds|=2;
 else if (theChar == "#") excludedBonds|=4;
 else if (theChar == "$") excludedBonds|=32;
 else if (theChar == ":") excludedBonds|=8;
 else throw Clazz.new_(Clazz.load('Exception').c$$S,["SmilesParser: bond symbol '" + theChar + "' not allowed after '!'." ]);
} else {
if (theChar == "@") bondQueryFeatures|=256;
 else if (theChar == "=") bondType=2;
 else if (theChar == "#") bondType=4;
 else if (theChar == "$") bondType=8;
 else if (theChar == ":") bondType=64;
 else if (theChar == "~") bondQueryFeatures|=31;
 else if (theChar == "/") {
if (readStereoFeatures) bondType=257;
} else if (theChar == "\\") {
if (readStereoFeatures) bondType=129;
} else if ((theChar == "-" && smiles[position] == 62  ) || (theChar == "<" && smiles[position] == 45  ) ) {
bondType=32;
++position;
}if (smiles[position] == 44 ) {
bondQueryFeatures|=p$2.bondSymbolToQueryFeature$C.apply(this, [bondType == 32 ? ">" : theChar]);
while (smiles[position] == 44 ){
if ((smiles[position + 1] == 60  && smiles[position + 2] == 45  ) || (smiles[position + 1] == 45  && smiles[position + 2] == 62  ) ) {
bondQueryFeatures|=p$2.bondSymbolToQueryFeature$C.apply(this, [">"]);
position+=3;
} else {
bondQueryFeatures|=p$2.bondSymbolToQueryFeature$C.apply(this, [String.fromCharCode(smiles[position + 1])]);
position+=2;
}}
}}if (smiles[position] == 59 ) {
++position;
theChar=String.fromCharCode(smiles[position++]);
continue;
}if (excludedBonds != 0) bondQueryFeatures|=31 & ~excludedBonds;
break;
}
continue;
}if (theChar <= " ") {
position=endIndex;
continue;
}if (Character.isDigit$C(theChar)) {
var number=theChar.$c() - 48;
if (squareBracketOpen) {
while (position < endIndex && Character.isDigit$I(smiles[position]) ){
number=10 * number + smiles[position] - 48;
++position;
}
atomMass=number;
} else {
var hasBondType=(smiles[position - 2] == 45  || smiles[position - 2] == 47   || smiles[position - 2] == 92   || smiles[position - 2] == 61   || smiles[position - 2] == 35   || smiles[position - 2] == 36   || smiles[position - 2] == 58   || smiles[position - 2] == 62   || smiles[position - 2] == 126  );
if (isDoubleDigit && position < endIndex  && Character.isDigit$I(smiles[position]) ) {
number=10 * number + smiles[position] - 48;
isDoubleDigit=false;
++position;
}if (number >= ringClosureAtom.length) {
if (number >= 100) throw Clazz.new_(Clazz.load('Exception').c$$S,["SmilesParser: ringClosureAtom number out of range"]);
var oldSize=ringClosureAtom.length;
var newSize=ringClosureAtom.length;
while (newSize <= number)newSize=Math.min(100, newSize + 16);

ringClosureAtom=$I$(11).copyOf$IA$I(ringClosureAtom, newSize);
ringClosurePosition=$I$(11).copyOf$IA$I(ringClosurePosition, newSize);
ringClosureBondType=$I$(11).copyOf$IA$I(ringClosureBondType, newSize);
ringClosureBondQueryFeatures=$I$(11).copyOf$IA$I(ringClosureBondQueryFeatures, newSize);
for (var i=oldSize; i < newSize; i++) ringClosureAtom[i]=-1;

}if (ringClosureAtom[number] == -1) {
ringClosureAtom[number]=baseAtom[bracketLevel];
ringClosurePosition[number]=position - 1;
ringClosureBondType[number]=hasBondType ? bondType : -1;
ringClosureBondQueryFeatures[number]=hasBondType ? bondQueryFeatures : 0;
} else {
if (ringClosureAtom[number] == baseAtom[bracketLevel]) throw Clazz.new_(Clazz.load('Exception').c$$S,["SmilesParser: ring closure to same atom"]);
if (readStereoFeatures && parityMap != null  ) {
var parity=parityMap.get$O(Integer.valueOf$I(ringClosureAtom[number]));
if (parity != null ) parity.addNeighbor$I$I$Z(baseAtom[bracketLevel], ringClosurePosition[number], false);
parity=parityMap.get$O(Integer.valueOf$I(baseAtom[bracketLevel]));
if (parity != null ) parity.addNeighbor$I$I$Z(ringClosureAtom[number], position - 1, false);
}if (ringClosureBondType[number] != -1) bondType=ringClosureBondType[number];
 else if (bondType == 257) bondType=129;
 else if (bondType == 129) bondType=257;
var bond=this.mMol.addBond$I$I$I(ringClosureAtom[number], baseAtom[bracketLevel], bondType);
if (ringClosureBondQueryFeatures[number] != 0) bondQueryFeatures=ringClosureBondQueryFeatures[number];
if (bondQueryFeatures != 0) {
smartsFeatureFound=true;
this.mMol.setBondQueryFeature$I$I$Z(bond, ringClosureBondQueryFeatures[number], true);
}ringClosureAtom[number]=-1;
}bondType=1;
bondQueryFeatures=0;
}continue;
}if (theChar == "+") {
throw Clazz.new_(Clazz.load('Exception').c$$S,["SmilesParser: \'+\' found outside brackets"]);
}if (theChar == "(") {
if (baseAtom[bracketLevel] == -1) throw Clazz.new_(Clazz.load('Exception').c$$S,["Smiles with leading parenthesis are not supported"]);
++bracketLevel;
if (baseAtom.length == bracketLevel) baseAtom=$I$(11).copyOf$IA$I(baseAtom, baseAtom.length + 32);
baseAtom[bracketLevel]=baseAtom[bracketLevel - 1];
continue;
}if (theChar == ")") {
--bracketLevel;
continue;
}if (theChar == "[") {
squareBracketOpen=true;
continue;
}if (theChar == "]") {
throw Clazz.new_(Clazz.load('Exception').c$$S,["SmilesParser: closing bracket at unexpected position"]);
}if (theChar == "%") {
isDoubleDigit=true;
continue;
}throw Clazz.new_(Clazz.load('Exception').c$$S,["SmilesParser: unexpected character outside brackets: '" + theChar + "'" ]);
}
if (bondType != 1) throw Clazz.new_(Clazz.load('Exception').c$$S,["SmilesParser: dangling open bond"]);
for (var rca, $rca = 0, $$rca = ringClosureAtom; $rca<$$rca.length&&((rca=($$rca[$rca])),1);$rca++) if (rca != -1) throw Clazz.new_(Clazz.load('Exception').c$$S,["SmilesParser: dangling ring closure"]);

var handleHydrogenAtomMap=this.mMol.getHandleHydrogenMap$();
this.mMol.setHydrogenProtection$Z(true);
this.mMol.ensureHelperArrays$I(1);
for (var atom=0; atom < this.mMol.getAllAtoms$(); atom++) {
if (this.mMol.getAtomCustomLabel$I(atom) != null ) {
var explicitHydrogen=this.mMol.getAtomCustomLabelBytes$I(atom)[0];
if (smartsFeatureFound || this.mSmartsMode == 2 ) {
if (this.mMakeHydrogenExplicit) {
for (var i=0; i < explicitHydrogen; i++) this.mMol.addBond$I$I$I(atom, this.mMol.addAtom$I(1), 1);

} else {
if (explicitHydrogen == 0) this.mMol.setAtomQueryFeature$I$J$Z(atom, 1792, true);
if (explicitHydrogen == 1) this.mMol.setAtomQueryFeature$I$J$Z(atom, 1664, true);
if (explicitHydrogen == 2) this.mMol.setAtomQueryFeature$I$J$Z(atom, 1408, true);
if (explicitHydrogen == 3) this.mMol.setAtomQueryFeature$I$J$Z(atom, 896, true);
}} else {
if (!this.mMol.isMetalAtom$I(atom) && (!this.mMol.isMarkedAtom$I(atom) || (this.mMol.getAtomicNo$I(atom) == 6 && this.mMol.getAtomCharge$I(atom) == 0 ) ) ) {
var valences=$I$(8,"getAllowedValences$I",[this.mMol.getAtomicNo$I(atom)]);
var compatibleValenceFound=false;
var usedValence=this.mMol.getOccupiedValence$I(atom);
usedValence-=this.mMol.getElectronValenceCorrection$I$I(atom, usedValence);
usedValence+=explicitHydrogen;
if (this.mMol.isMarkedAtom$I(atom)) ++usedValence;
for (var valence, $valence = 0, $$valence = valences; $valence<$$valence.length&&((valence=($$valence[$valence])),1);$valence++) {
if (usedValence <= valence) {
compatibleValenceFound=true;
if (valence == usedValence + 2) this.mMol.setAtomRadical$I$I(atom, 48);
 else if (valence == usedValence + 1) this.mMol.setAtomRadical$I$I(atom, 32);
 else if (valence != usedValence || valence != valences[0] ) this.mMol.setAtomAbnormalValence$I$I(atom, usedValence);
break;
}}
if (!compatibleValenceFound) this.mMol.setAtomAbnormalValence$I$I(atom, usedValence);
}if (this.mMakeHydrogenExplicit || !this.mMol.supportsImplicitHydrogen$I(atom) ) for (var i=0; i < explicitHydrogen; i++) this.mMol.addBond$I$I$I(atom, this.mMol.addAtom$I(1), 1);

}} else if (!this.mMakeHydrogenExplicit && (smartsFeatureFound || this.mSmartsMode == 2 ) ) {
var explicitHydrogen=this.mMol.getExplicitHydrogens$I(atom);
if (explicitHydrogen >= 1) this.mMol.setAtomQueryFeature$I$J$Z(atom, 128, true);
if (explicitHydrogen >= 2) this.mMol.setAtomQueryFeature$I$J$Z(atom, 256, true);
if (explicitHydrogen >= 3) this.mMol.setAtomQueryFeature$I$J$Z(atom, 512, true);
if (explicitHydrogen >= 4) this.mMol.setAtomQueryFeature$I$J$Z(atom, 1024, true);
}}
if (!this.mMakeHydrogenExplicit && (smartsFeatureFound || this.mSmartsMode == 2 ) ) this.mMol.removeExplicitHydrogens$();
this.mMol.ensureHelperArrays$I(1);
p$2.correctValenceExceededNitrogen.apply(this, []);
p$2.locateAromaticDoubleBonds$Z.apply(this, [allowSmarts]);
this.mMol.removeAtomCustomLabels$();
this.mMol.setHydrogenProtection$Z(false);
if (readStereoFeatures) {
p$2.assignKnownEZBondParities.apply(this, []);
if (parityMap != null ) {
for (var parity, $parity = parityMap.values$().iterator$(); $parity.hasNext$()&&((parity=($parity.next$())),1);) this.mMol.setAtomParity$I$I$Z(parity.mCentralAtom, parity.calculateParity$IA(handleHydrogenAtomMap), false);

this.mMol.setParitiesValid$I(0);
}}this.mMol.setParitiesValid$I(0);
if (createCoordinates) {
var inventor=Clazz.new_($I$(12,1).c$$I,[this.mCoordinateMode]);
if (Long.$ne(this.mRandomSeed,0 )) inventor.setRandomSeed$J(this.mRandomSeed);
inventor.invent$com_actelion_research_chem_StereoMolecule(this.mMol);
if (readStereoFeatures) this.mMol.setUnknownParitiesToExplicitlyUnknown$();
}if (smartsFeatureFound || this.mSmartsMode == 2 ) this.mMol.setFragment$Z(true);
});

Clazz.newMeth(C$, 'parseAtomList$BA$I$com_actelion_research_util_SortedList',  function (smiles, start, atomList) {
atomList.removeAll$();
for (var p=start; p < smiles.length; p++) {
if (!Character.isLetter$I(smiles[p])) {
var atomicNo=$I$(8,"getAtomicNoFromLabel$S",[ String.instantialize(smiles, start, p - start)]);
if (atomicNo != 0) atomList.add$O(Integer.valueOf$I(atomicNo));
start=p + 1;
if (smiles[p] != 44 ) break;
}}
return start - 1;
}, p$2);

Clazz.newMeth(C$, 'isBondSymbol$C',  function (theChar) {
return theChar == "-" || theChar == "="  || theChar == "#"  || theChar == "$"  || theChar == ":"  || theChar == "/"  || theChar == "\\"  || theChar == "<"  || theChar == "~"  || theChar == "!"  || theChar == "@" ;
}, p$2);

Clazz.newMeth(C$, 'bondSymbolToQueryFeature$C',  function (symbol) {
return symbol == "=" ? 2 : symbol == "#" ? 4 : symbol == "$" ? 32 : symbol == ":" ? 8 : symbol == ">" ? 16 : symbol == "~" ? 31 : 1;
}, p$2);

Clazz.newMeth(C$, 'smartsWarning$S',  function (feature) {
if (this.mCreateSmartsWarnings) {
if (this.mSmartsWarningBuffer == null ) this.mSmartsWarningBuffer=Clazz.new_($I$(13,1));
this.mSmartsWarningBuffer.append$S(" ");
this.mSmartsWarningBuffer.append$S(feature);
}}, p$2);

Clazz.newMeth(C$, 'locateAromaticDoubleBonds$Z',  function (allowSmartsFeatures) {
this.mMol.ensureHelperArrays$I(1);
this.mIsAromaticBond=Clazz.array(Boolean.TYPE, [this.mMol.getBonds$()]);
this.mAromaticBonds=0;
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mMol.getBondType$I(bond) == 64) {
this.mMol.setBondType$I$I(bond, 1);
this.mIsAromaticBond[bond]=true;
++this.mAromaticBonds;
}}
var isAromaticRingAtom=Clazz.array(Boolean.TYPE, [this.mMol.getAtoms$()]);
var ringSet=Clazz.new_($I$(14,1).c$$com_actelion_research_chem_ExtendedMolecule$I,[this.mMol, 3]);
var isAromaticRing=Clazz.array(Boolean.TYPE, [ringSet.getSize$()]);
for (var ring=0; ring < ringSet.getSize$(); ring++) {
var ringAtom=ringSet.getRingAtoms$I(ring);
isAromaticRing[ring]=true;
for (var i=0; i < ringAtom.length; i++) {
if (!this.mMol.isMarkedAtom$I(ringAtom[i])) {
isAromaticRing[ring]=false;
break;
}}
if (isAromaticRing[ring]) {
for (var i=0; i < ringAtom.length; i++) isAromaticRingAtom[ringAtom[i]]=true;

var ringBond=ringSet.getRingBonds$I(ring);
for (var i=0; i < ringBond.length; i++) {
if (!this.mIsAromaticBond[ringBond[i]]) {
this.mIsAromaticBond[ringBond[i]]=true;
++this.mAromaticBonds;
}}
}}
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (!this.mIsAromaticBond[bond] && ringSet.getBondRingSize$I(bond) != 0  && this.mMol.isMarkedAtom$I(this.mMol.getBondAtom$I$I(0, bond))  && this.mMol.isMarkedAtom$I(this.mMol.getBondAtom$I$I(1, bond)) ) {
p$2.addLargeAromaticRing$I.apply(this, [bond]);
}}
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (!this.mIsAromaticBond[bond]) {
var atom1=this.mMol.getBondAtom$I$I(0, bond);
var atom2=this.mMol.getBondAtom$I$I(1, bond);
if (!isAromaticRingAtom[atom1] && !isAromaticRingAtom[atom2] && this.mMol.isMarkedAtom$I(atom1) && this.mMol.isMarkedAtom$I(atom2)  ) {
this.mIsAromaticBond[bond]=true;
++this.mAromaticBonds;
}}}
this.mMol.ensureHelperArrays$I(7);
var isAromaticBond=Clazz.array(Boolean.TYPE, [this.mMol.getBonds$()]);
for (var i=0; i < this.mMol.getBonds$(); i++) isAromaticBond[i]=this.mIsAromaticBond[i];

for (var ring=0; ring < ringSet.getSize$(); ring++) {
if (isAromaticRing[ring]) {
var ringAtom=ringSet.getRingAtoms$I(ring);
for (var i=0; i < ringAtom.length; i++) {
if (!p$2.qualifiesForPi$I.apply(this, [ringAtom[i]])) {
if (this.mMol.isMarkedAtom$I(ringAtom[i])) {
this.mMol.setAtomMarker$I$Z(ringAtom[i], false);
--this.mAromaticAtoms;
}for (var j=0; j < this.mMol.getConnAtoms$I(ringAtom[i]); j++) {
var connBond=this.mMol.getConnBond$I$I(ringAtom[i], j);
if (this.mIsAromaticBond[connBond]) {
this.mIsAromaticBond[connBond]=false;
--this.mAromaticBonds;
}}
}}
}}
p$2.promoteObviousBonds.apply(this, []);
for (var ring=0; ring < ringSet.getSize$(); ring++) {
if (isAromaticRing[ring] && ringSet.getRingSize$I(ring) == 6 ) {
var ringBond=ringSet.getRingBonds$I(ring);
var isFullyDelocalized=true;
for (var bond, $bond = 0, $$bond = ringBond; $bond<$$bond.length&&((bond=($$bond[$bond])),1);$bond++) {
if (!this.mIsAromaticBond[bond]) {
isFullyDelocalized=false;
break;
}}
if (isFullyDelocalized) {
p$2.promoteBond$I.apply(this, [ringBond[0]]);
p$2.promoteBond$I.apply(this, [ringBond[2]]);
p$2.promoteBond$I.apply(this, [ringBond[4]]);
p$2.promoteObviousBonds.apply(this, []);
}}}
var qualifyingBondFound;
for (var qualifyingNo=5; qualifyingNo >= 4; qualifyingNo--) {
do {
qualifyingBondFound=false;
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mIsAromaticBond[bond]) {
var aromaticConnBonds=0;
for (var i=0; i < 2; i++) {
var bondAtom=this.mMol.getBondAtom$I$I(i, bond);
for (var j=0; j < this.mMol.getConnAtoms$I(bondAtom); j++) if (this.mIsAromaticBond[this.mMol.getConnBond$I$I(bondAtom, j)]) ++aromaticConnBonds;

}
if (aromaticConnBonds == qualifyingNo) {
p$2.promoteBond$I.apply(this, [bond]);
p$2.promoteObviousBonds.apply(this, []);
qualifyingBondFound=true;
break;
}}}
} while (qualifyingBondFound);
}
while (this.mAromaticAtoms >= 2)if (!p$2.connectConjugatedRadicalPairs$ZA.apply(this, [isAromaticBond])) break;

if (allowSmartsFeatures) {
if (this.mAromaticAtoms != 0) {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mMol.isMarkedAtom$I(atom)) {
this.mMol.setAtomMarker$I$Z(atom, false);
this.mMol.setAtomQueryFeature$I$J$Z(atom, 2, true);
--this.mAromaticAtoms;
}}
}if (this.mAromaticBonds != 0) {
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mIsAromaticBond[bond]) {
this.mIsAromaticBond[bond]=false;
this.mMol.setBondType$I$I(bond, 64);
--this.mAromaticBonds;
}}
}} else {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mMol.isMarkedAtom$I(atom) && this.mMol.getImplicitHydrogens$I(atom) != 0 ) {
this.mMol.setAtomMarker$I$Z(atom, false);
this.mMol.setAtomRadical$I$I(atom, 32);
--this.mAromaticAtoms;
}}
}if (this.mAromaticAtoms != 0) throw Clazz.new_(Clazz.load('Exception').c$$S,["Assignment of aromatic double bonds failed"]);
if (this.mAromaticBonds != 0) throw Clazz.new_(Clazz.load('Exception').c$$S,["Assignment of aromatic double bonds failed"]);
}, p$2);

Clazz.newMeth(C$, 'connectConjugatedRadicalPairs$ZA',  function (isAromaticBond) {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mMol.isMarkedAtom$I(atom)) {
var graphLevel=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
var graphAtom=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
var graphParent=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
graphAtom[0]=atom;
graphLevel[atom]=1;
graphParent[atom]=-1;
var current=0;
var highest=0;
while (current <= highest){
var bondOrder=((graphLevel[graphAtom[current]] & 1) == 1) ? 1 : 2;
for (var i=0; i < this.mMol.getConnAtoms$I(graphAtom[current]); i++) {
var bond=this.mMol.getConnBond$I$I(graphAtom[current], i);
if (this.mMol.getBondOrder$I(bond) == bondOrder && isAromaticBond[bond] ) {
var candidate=this.mMol.getConnAtom$I$I(graphAtom[current], i);
if (graphLevel[candidate] == 0) {
if (bondOrder == 1 && this.mMol.isMarkedAtom$I(candidate) ) {
var parent=graphAtom[current];
while (parent != -1){
this.mMol.setBondType$I$I(this.mMol.getBond$I$I(candidate, parent), bondOrder == 1 ? 2 : 1);
bondOrder=3 - bondOrder;
candidate=parent;
parent=graphParent[parent];
}
this.mMol.setAtomMarker$I$Z(atom, false);
this.mMol.setAtomMarker$I$Z(candidate, false);
this.mAromaticAtoms-=2;
return true;
}graphAtom[++highest]=candidate;
graphParent[candidate]=graphAtom[current];
graphLevel[candidate]=graphLevel[graphAtom[current]] + 1;
}}}
++current;
}
}}
return false;
}, p$2);

Clazz.newMeth(C$, 'addLargeAromaticRing$I',  function (bond) {
var graphLevel=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
var graphAtom=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
var graphBond=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
var graphParent=Clazz.array(Integer.TYPE, [this.mMol.getAtoms$()]);
var atom1=this.mMol.getBondAtom$I$I(0, bond);
var atom2=this.mMol.getBondAtom$I$I(1, bond);
graphAtom[0]=atom1;
graphAtom[1]=atom2;
graphBond[0]=-1;
graphBond[1]=bond;
graphLevel[atom1]=1;
graphLevel[atom2]=2;
graphParent[atom1]=-1;
graphParent[atom2]=atom1;
var current=1;
var highest=1;
while (current <= highest && graphLevel[graphAtom[current]] < 15 ){
var parent=graphAtom[current];
for (var i=0; i < this.mMol.getConnAtoms$I(parent); i++) {
var candidate=this.mMol.getConnAtom$I$I(parent, i);
if (candidate != graphParent[parent]) {
var candidateBond=this.mMol.getConnBond$I$I(parent, i);
if (candidate == atom1) {
graphBond[0]=candidateBond;
for (var j=0; j <= highest; j++) {
if (!this.mIsAromaticBond[graphBond[i]]) {
this.mIsAromaticBond[graphBond[i]]=true;
++this.mAromaticBonds;
}}
return;
}if (this.mMol.isMarkedAtom$I(candidate) && graphLevel[candidate] == 0 ) {
++highest;
graphAtom[highest]=candidate;
graphBond[highest]=candidateBond;
graphLevel[candidate]=graphLevel[parent] + 1;
graphParent[candidate]=parent;
}}}
++current;
}
return;
}, p$2);

Clazz.newMeth(C$, 'qualifiesForPi$I',  function (atom) {
if (!$I$(14,"qualifiesAsAromaticAtomicNo$I",[this.mMol.getAtomicNo$I(atom)])) return false;
if (this.mMol.getAtomicNo$I(atom) == 6) {
if (!this.mMol.isMarkedAtom$I(atom)) return false;
if (this.mMol.getAtomCharge$I(atom) > 0) return false;
}var explicitHydrogens=(this.mMol.getAtomCustomLabel$I(atom) == null ) ? ($b$[0] = 0, $b$[0]) : this.mMol.getAtomCustomLabelBytes$I(atom)[0];
var freeValence=this.mMol.getFreeValence$I(atom) - explicitHydrogens;
if (freeValence < 1) return false;
if (this.mMol.getAtomicNo$I(atom) == 16 || this.mMol.getAtomicNo$I(atom) == 34  || this.mMol.getAtomicNo$I(atom) == 52 ) {
if (this.mMol.getConnAtoms$I(atom) == 2 && this.mMol.getAtomCharge$I(atom) <= 0 ) return false;
if (freeValence == 2) return false;
}return true;
}, p$2);

Clazz.newMeth(C$, 'promoteBond$I',  function (bond) {
if (this.mMol.getBondType$I(bond) == 1) this.mMol.setBondType$I$I(bond, 2);
for (var i=0; i < 2; i++) {
var bondAtom=this.mMol.getBondAtom$I$I(i, bond);
if (this.mMol.isMarkedAtom$I(bondAtom)) {
this.mMol.setAtomMarker$I$Z(bondAtom, false);
--this.mAromaticAtoms;
}for (var j=0; j < this.mMol.getConnAtoms$I(bondAtom); j++) {
var connBond=this.mMol.getConnBond$I$I(bondAtom, j);
if (this.mIsAromaticBond[connBond]) {
this.mIsAromaticBond[connBond]=false;
--this.mAromaticBonds;
}}
}
}, p$2);

Clazz.newMeth(C$, 'promoteObviousBonds',  function () {
var terminalAromaticBondFound;
do {
terminalAromaticBondFound=false;
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mIsAromaticBond[bond]) {
var isTerminalAromaticBond=false;
for (var i=0; i < 2; i++) {
var aromaticNeighbourFound=false;
var bondAtom=this.mMol.getBondAtom$I$I(i, bond);
for (var j=0; j < this.mMol.getConnAtoms$I(bondAtom); j++) {
if (bond != this.mMol.getConnBond$I$I(bondAtom, j) && this.mIsAromaticBond[this.mMol.getConnBond$I$I(bondAtom, j)] ) {
aromaticNeighbourFound=true;
break;
}}
if (!aromaticNeighbourFound) {
isTerminalAromaticBond=true;
break;
}}
if (isTerminalAromaticBond) {
terminalAromaticBondFound=true;
p$2.promoteBond$I.apply(this, [bond]);
}}}
} while (terminalAromaticBondFound);
}, p$2);

Clazz.newMeth(C$, 'correctValenceExceededNitrogen',  function () {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mMol.getAtomicNo$I(atom) == 7 && this.mMol.getAtomCharge$I(atom) == 0  && this.mMol.getOccupiedValence$I(atom) > 3  && this.mMol.getAtomPi$I(atom) > 0 ) {
for (var i=0; i < this.mMol.getConnAtoms$I(atom); i++) {
var connAtom=this.mMol.getConnAtom$I$I(atom, i);
var connBond=this.mMol.getConnBond$I$I(atom, i);
if ((this.mMol.getBondOrder$I(connBond) > 1) && this.mMol.isElectronegative$I(connAtom) ) {
if (this.mMol.getBondType$I(connBond) == 4) this.mMol.setBondType$I$I(connBond, 2);
 else this.mMol.setBondType$I$I(connBond, 1);
this.mMol.setAtomCharge$I$I(atom, this.mMol.getAtomCharge$I(atom) + 1);
this.mMol.setAtomCharge$I$I(connAtom, this.mMol.getAtomCharge$I(connAtom) - 1);
this.mMol.setAtomAbnormalValence$I$I(atom, -1);
break;
}}
}}
}, p$2);

Clazz.newMeth(C$, 'assignKnownEZBondParities',  function () {
this.mMol.ensureHelperArrays$I(7);
var paritiesFound=false;
var refAtom=Clazz.array(Integer.TYPE, [2]);
var refBond=Clazz.array(Integer.TYPE, [2]);
var otherAtom=Clazz.array(Integer.TYPE, [2]);
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (!this.mMol.isSmallRingBond$I(bond) && this.mMol.getBondType$I(bond) == 2 ) {
for (var i=0; i < 2; i++) {
refAtom[i]=-1;
otherAtom[i]=-1;
var atom=this.mMol.getBondAtom$I$I(i, bond);
for (var j=0; j < this.mMol.getConnAtoms$I(atom); j++) {
var connBond=this.mMol.getConnBond$I$I(atom, j);
if (connBond != bond) {
if (refAtom[i] == -1 && (this.mMol.getBondType$I(connBond) == 257 || this.mMol.getBondType$I(connBond) == 129 ) ) {
refAtom[i]=this.mMol.getConnAtom$I$I(atom, j);
refBond[i]=connBond;
} else {
otherAtom[i]=this.mMol.getConnAtom$I$I(atom, j);
}}}
if (refAtom[i] == -1) break;
}
if (refAtom[0] != -1 && refAtom[1] != -1 ) {
var isZ=this.mMol.getBondType$I(refBond[0]) == this.mMol.getBondType$I(refBond[1]);
for (var i=0; i < 2; i++) if (refAtom[i] == this.mMol.getBondAtom$I$I(0, refBond[i])) isZ=!isZ;

for (var i=0; i < 2; i++) if (otherAtom[i] != -1 && otherAtom[i] < refAtom[i] ) isZ=!isZ;

this.mMol.setBondParity$I$I$Z(bond, isZ ? 2 : 1, false);
paritiesFound=true;
}}}
for (var bond=0; bond < this.mMol.getBonds$(); bond++) if (this.mMol.getBondType$I(bond) == 257 || this.mMol.getBondType$I(bond) == 129 ) this.mMol.setBondType$I$I(bond, 1);

return paritiesFound;
}, p$2);

Clazz.newMeth(C$, 'testStereo$',  function () {
var data=Clazz.array(String, -2, [Clazz.array(String, -1, ["F/C=C/I", "F/C=C/I"]), Clazz.array(String, -1, ["F/C=C\\I", "F/C=C\\I"]), Clazz.array(String, -1, ["C(=C/I)/F", "F/C=C\\I"]), Clazz.array(String, -1, ["[H]C(/F)=C/I", "F/C=C\\I"]), Clazz.array(String, -1, ["C(=C\\1)/I.F1", "F/C=C/I"]), Clazz.array(String, -1, ["C(=C1)/I.F/1", "F/C=C/I"]), Clazz.array(String, -1, ["C(=C\\F)/1.I1", "F/C=C/I"]), Clazz.array(String, -1, ["C(=C\\F)1.I\\1", "F/C=C/I"]), Clazz.array(String, -1, ["C\\1=C/I.F1", "F/C=C/I"]), Clazz.array(String, -1, ["C1=C/I.F/1", "F/C=C/I"]), Clazz.array(String, -1, ["C(=C\\1)/2.F1.I2", "F/C=C/I"]), Clazz.array(String, -1, ["C/2=C\\1.F1.I2", "F/C=C/I"]), Clazz.array(String, -1, ["C/1=C/C=C/F.I1", "F/C=C/C=C\\I"]), Clazz.array(String, -1, ["C1=C/C=C/F.I\\1", "F/C=C/C=C\\I"]), Clazz.array(String, -1, ["C(/I)=C/C=C/1.F1", "F/C=C/C=C\\I"]), Clazz.array(String, -1, ["C(/I)=C/C=C1.F\\1", "F/C=C/C=C\\I"]), Clazz.array(String, -1, ["[C@](Cl)(F)(I)1.Br1", "F[C@](Cl)(Br)I"]), Clazz.array(String, -1, ["Br[C@](Cl)(I)1.F1", "F[C@](Cl)(Br)I"]), Clazz.array(String, -1, ["[C@H](F)(I)1.Br1", "F[C@H](Br)I"]), Clazz.array(String, -1, ["Br[C@@H](F)1.I1", "F[C@H](Br)I"]), Clazz.array(String, -1, ["C[S@@](CC)=O", "CC[S@](C)=O"]), Clazz.array(String, -1, ["[S@](=O)(C)CC", "CC[S](C)=O"])]);
var mol=Clazz.new_($I$(3,1));
for (var test, $test = 0, $$test = data; $test<$$test.length&&((test=($$test[$test])),1);$test++) {
try {
Clazz.new_(C$).parse$com_actelion_research_chem_StereoMolecule$S(mol, test[0]);
var smiles=Clazz.new_($I$(15,1).c$$com_actelion_research_chem_StereoMolecule,[mol]).getSmiles$();
System.out.print$S("IN:" + test[0] + " OUT:" + smiles );
if (!test[1].equals$O(smiles)) System.out.println$S(" EXPECTED: " + test[1] + " ERROR!" );
 else System.out.println$S(" OK");
} catch (e) {
if (Clazz.exceptionOf(e,"Exception")){
e.printStackTrace$();
} else {
throw e;
}
}
}
}, 1);

Clazz.newMeth(C$, 'main$SA',  function (args) {
C$.testStereo$();
System.out.println$S("ID-code equivalence test:");
var data=Clazz.array(String, -2, [Clazz.array(String, -1, ["N[C@@]([H])(C)C(=O)O", "S-alanine", "gGX`BDdwMUM@@"]), Clazz.array(String, -1, ["N[C@@H](C)C(=O)O", "S-alanine", "gGX`BDdwMUM@@"]), Clazz.array(String, -1, ["N[C@H](C(=O)O)C", "S-alanine", "gGX`BDdwMUM@@"]), Clazz.array(String, -1, ["[H][C@](N)(C)C(=O)O", "S-alanine", "gGX`BDdwMUM@@"]), Clazz.array(String, -1, ["[C@H](N)(C)C(=O)O", "S-alanine", "gGX`BDdwMUM@@"]), Clazz.array(String, -1, ["N[C@]([H])(C)C(=O)O", "R-alanine", "gGX`BDdwMUL`@"]), Clazz.array(String, -1, ["N[C@H](C)C(=O)O", "R-alanine", "gGX`BDdwMUL`@"]), Clazz.array(String, -1, ["N[C@@H](C(=O)O)C", "R-alanine", "gGX`BDdwMUL`@"]), Clazz.array(String, -1, ["[H][C@@](N)(C)C(=O)O", "R-alanine", "gGX`BDdwMUL`@"]), Clazz.array(String, -1, ["[C@@H](N)(C)C(=O)O", "R-alanine", "gGX`BDdwMUL`@"]), Clazz.array(String, -1, ["C[C@H]1CCCCO1", "S-Methyl-pyran", "gOq@@eLm]UUH`@"]), Clazz.array(String, -1, ["O1CCCC[C@@H]1C", "S-Methyl-pyran", "gOq@@eLm]UUH`@"]), Clazz.array(String, -1, ["[C@H](F)(B)O", "S-Methyl-oxetan", "gCaDDICTBSURH@"]), Clazz.array(String, -1, ["C1CO[C@H]1C", "S-Methyl-oxetan", "gKQ@@eLmUTb@"]), Clazz.array(String, -1, ["C1CO[C@@H](C)1", "S-Methyl-oxetan", "gKQ@@eLmUTb@"]), Clazz.array(String, -1, ["[C@H]1(C)CCO1", "S-Methyl-oxetan", "gKQ@@eLmUTb@"]), Clazz.array(String, -1, ["[H][C@]1(C)CCO1", "S-Methyl-oxetan", "gKQ@@eLmUTb@"]), Clazz.array(String, -1, ["[H][C@@]1(CCO1)C", "S-Methyl-oxetan", "gKQ@@eLmUTb@"]), Clazz.array(String, -1, ["[C@@]1([H])(C)CCO1", "S-Methyl-oxetan", "gKQ@@eLmUTb@"]), Clazz.array(String, -1, ["[C@]1(C)([H])CCO1", "S-Methyl-oxetan", "gKQ@@eLmUTb@"]), Clazz.array(String, -1, ["C1[C@@H]2COC2=N1", "oxetan-azetin", "gGy@LDimDvfja`@"]), Clazz.array(String, -1, ["CC(C)[C@@]12C[C@@H]1[C@@H](C)C(=O)C2", "alpha-thujone", "dmLH@@RYe~IfyjjjkDaIh@"]), Clazz.array(String, -1, ["CN1CCC[C@H]1c2cccnc2", "Nicotine", "dcm@@@{IDeCEDUSh@UUECP@"]), Clazz.array(String, -1, ["CC[C@H](O1)CC[C@@]12CCCO2", "2S,5R-Chalcogran", "dmLD@@qJZY|fFZjjjdbH`@"]), Clazz.array(String, -1, ["CCCC", "butane", "gC`@Dij@@"]), Clazz.array(String, -1, ["C1C.CC1", "butane", "gC`@Dij@@"]), Clazz.array(String, -1, ["[CH3][CH2][CH2][CH3]", "butane", "gC`@Dij@@"]), Clazz.array(String, -1, ["C-C-C-C", "butane", "gC`@Dij@@"]), Clazz.array(String, -1, ["C12.C1.CC2", "butane", "gC`@Dij@@"]), Clazz.array(String, -1, ["[Na+].[Cl-]", "NaCl", "eDARHm@zd@@"]), Clazz.array(String, -1, ["[Na+]-[Cl-]", "NaCl", "error"]), Clazz.array(String, -1, ["[Na+]1.[Cl-]1", "NaCl", "error"]), Clazz.array(String, -1, ["c1ccccc1", "benzene", "gFp@DiTt@@@"]), Clazz.array(String, -1, ["C1=C-C=C-C=C1", "benzene", "gFp@DiTt@@@"]), Clazz.array(String, -1, ["C1:C:C:C:C:C:1", "benzene", "gFp@DiTt@@@"]), Clazz.array(String, -1, ["c1ccncc1", "pyridine", "gFx@@eJf`@@@"]), Clazz.array(String, -1, ["[nH]1cccc1", "pyrrole", "gKX@@eKcRp@"]), Clazz.array(String, -1, ["N1C=C-C=C1", "pyrrole", "gKX@@eKcRp@"]), Clazz.array(String, -1, ["[H]n1cccc1", "pyrrole", "gKX@@eKcRp@"]), Clazz.array(String, -1, ["[H]n1cccc1", "pyrrole", "gKX@@eKcRp@"]), Clazz.array(String, -1, ["c1cncc1", "pyrrole no [nH]", "error"]), Clazz.array(String, -1, ["[13CH4]", "C13-methane", "fH@FJp@"]), Clazz.array(String, -1, ["[35ClH]", "35-chlorane", "fHdP@qX`"]), Clazz.array(String, -1, ["[35Cl-]", "35-chloride", "fHtPxAbq@"]), Clazz.array(String, -1, ["[Na+].[O-]c1ccccc1", "Na-phenolate", "daxHaHCPBXyAYUn`@@@"]), Clazz.array(String, -1, ["c1cc([O-].[Na+])ccc1", "Na-phenolate", "daxHaHCPBXyAYUn`@@@"]), Clazz.array(String, -1, ["C[C@@](C)(O1)C[C@@H](O)[C@@]1(O2)[C@@H](C)[C@@H]3CC=C4[C@]3(C2)C(=O)C[C@H]5[C@H]4CC[C@@H](C6)[C@]5(C)Cc(n7)c6nc(C[C@@]89(C))c7C[C@@H]8CC[C@@H]%10[C@@H]9C[C@@H](O)[C@@]%11(C)C%10=C[C@H](O%12)[C@]%11(O)[C@H](C)[C@]%12(O%13)[C@H](O)C[C@@]%13(C)CO", "Cephalostatin-1", "gdKe@h@@K`H@XjKHuYlnoP\\bbdRbbVTLbTrJbRaQRRRbTJTRTrfrfTTOBPHtFODPhLNSMdIERYJmShLfs]aqy|uUMUUUUUUE@UUUUMUUUUUUTQUUTPR`nDdQQKB|RIFbiQeARuQt`rSSMNtGS\\ct@@"])]);
var mol=Clazz.new_($I$(3,1));
for (var test, $test = 0, $$test = data; $test<$$test.length&&((test=($$test[$test])),1);$test++) {
try {
Clazz.new_(C$).parse$com_actelion_research_chem_StereoMolecule$S(mol, test[0]);
var idcode=Clazz.new_($I$(16,1).c$$com_actelion_research_chem_StereoMolecule,[mol]).getIDCode$();
if (test[2].equals$O("error")) System.out.println$S("Should create error! " + test[1] + " smiles:" + test[0] + " idcode:" + idcode );
 else if (!test[2].equals$O(idcode)) System.out.println$S("ERROR! " + test[1] + " smiles:" + test[0] + " is:" + idcode + " must:" + test[2] );
} catch (e) {
if (Clazz.exceptionOf(e,"Exception")){
if (!test[2].equals$O("error")) System.out.println$S("ERROR! " + test[1] + " smiles:" + test[0] + " exception:" + e.getMessage$() );
} else {
throw e;
}
}
}
}, 1);
var $b$ = new Int8Array(1);
;
(function(){/*c*/var C$=Clazz.newClass(P$.SmilesParser, "ParityNeighbour", function(){
Clazz.newInstance(this, arguments[0],true,C$);
});

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['I',['mAtom','mPosition']]]

Clazz.newMeth(C$, 'c$$I$I',  function (atom, position) {
;C$.$init$.apply(this);
this.mAtom=atom;
this.mPosition=position;
}, 1);

Clazz.newMeth(C$);
})()
;
(function(){/*c*/var C$=Clazz.newClass(P$.SmilesParser, "THParity", function(){
Clazz.newInstance(this, arguments[0],true,C$);
});

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['Z',['mIsClockwise','mError'],'I',['mCentralAtom','mCentralAtomPosition'],'O',['mNeighbourList','java.util.ArrayList']]]

Clazz.newMeth(C$, 'c$$I$I$I$I$I$Z',  function (centralAtom, centralAtomPosition, fromAtom, explicitHydrogen, hydrogenPosition, isClockwise) {
;C$.$init$.apply(this);
if (explicitHydrogen != 0 && explicitHydrogen != 1 ) {
this.mError=true;
} else {
this.mCentralAtom=centralAtom;
this.mCentralAtomPosition=centralAtomPosition;
this.mIsClockwise=isClockwise;
this.mNeighbourList=Clazz.new_($I$(1,1));
if (fromAtom != -1) this.addNeighbor$I$I$Z(fromAtom, centralAtomPosition - 1, false);
if (fromAtom != -1 && explicitHydrogen == 1 ) this.addNeighbor$I$I$Z(2147483646, centralAtomPosition + 1, false);
}}, 1);

Clazz.newMeth(C$, 'addNeighbor$I$I$Z',  function (atom, position, unused) {
if (!this.mError) {
if (this.mNeighbourList.size$() == 4) {
this.mError=true;
return;
}this.mNeighbourList.add$O(Clazz.new_($I$(2,1).c$$I$I,[this, null, atom, position]));
}});

Clazz.newMeth(C$, 'calculateParity$IA',  function (handleHydrogenAtomMap) {
if (this.mError) return 3;
for (var neighbour, $neighbour = this.mNeighbourList.iterator$(); $neighbour.hasNext$()&&((neighbour=($neighbour.next$())),1);) if (neighbour.mAtom != 2147483646 && neighbour.mAtom != 2147483647 ) neighbour.mAtom=handleHydrogenAtomMap[neighbour.mAtom];

if (this.mNeighbourList.size$() == 3) this.mNeighbourList.add$O(Clazz.new_($I$(2,1).c$$I$I,[this, null, 2147483647, this.mCentralAtomPosition]));
 else if (this.mNeighbourList.size$() != 4) return 3;
var parity=(!!(this.mIsClockwise ^ p$1.isInverseOrder.apply(this, []))) ? 1 : 2;
return parity;
});

Clazz.newMeth(C$, 'isInverseOrder',  function () {
var inversion=false;
for (var i=1; i < this.mNeighbourList.size$(); i++) {
for (var j=0; j < i; j++) {
if (this.mNeighbourList.get$I(j).mAtom > this.mNeighbourList.get$I(i).mAtom) inversion=!inversion;
if (this.mNeighbourList.get$I(j).mPosition > this.mNeighbourList.get$I(i).mPosition) inversion=!inversion;
}
}
return inversion;
}, p$1);

Clazz.newMeth(C$);
})()
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-18 09:54:16 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
