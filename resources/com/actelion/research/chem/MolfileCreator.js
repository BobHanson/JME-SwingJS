(function(){var P$=Clazz.newPackage("com.actelion.research.chem"),p$1={},I$=[[0,'StringBuilder','java.text.DecimalFormat','java.text.DecimalFormatSymbols','java.util.Locale','com.actelion.research.chem.Molecule']],I$0=I$[0],$I$=function(i,n){return((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "MolfileCreator");

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['O',['mBuilder','StringBuilder','mDoubleFormat','java.text.DecimalFormat']]]

Clazz.newMeth(C$, 'c$$com_actelion_research_chem_ExtendedMolecule',  function (mol) {
C$.c$$com_actelion_research_chem_ExtendedMolecule$Z.apply(this, [mol, true]);
}, 1);

Clazz.newMeth(C$, 'c$$com_actelion_research_chem_ExtendedMolecule$Z',  function (mol, allowScaling) {
C$.c$$com_actelion_research_chem_ExtendedMolecule$Z$StringBuilder.apply(this, [mol, allowScaling, Clazz.new_($I$(1,1).c$$I,[32768])]);
}, 1);

Clazz.newMeth(C$, 'c$$com_actelion_research_chem_ExtendedMolecule$Z$StringBuilder',  function (mol, allowScaling, builder) {
C$.c$$com_actelion_research_chem_ExtendedMolecule$Z$D$StringBuilder.apply(this, [mol, allowScaling, 0.0, builder]);
}, 1);

Clazz.newMeth(C$, 'c$$com_actelion_research_chem_ExtendedMolecule$Z$D$StringBuilder',  function (mol, allowScaling, scalingFactor, builder) {
;C$.$init$.apply(this);
this.mDoubleFormat=Clazz.new_(["0.0000", Clazz.new_([$I$(4).ENGLISH],$I$(3,1).c$$java_util_Locale)],$I$(2,1).c$$S$java_text_DecimalFormatSymbols);
var nl=System.lineSeparator$();
mol.ensureHelperArrays$I(15);
var isRacemic=true;
for (var atom=0; atom < mol.getAtoms$(); atom++) {
if (mol.getAtomParity$I(atom) != 0 && mol.getAtomParity$I(atom) != 3  && mol.getAtomESRType$I(atom) != 1 ) {
isRacemic=false;
break;
}}
var maxESRGroup=-1;
if (isRacemic) {
var esrGroupCount=Clazz.array(Integer.TYPE, [32]);
var maxGroupCount=0;
for (var atom=0; atom < mol.getAtoms$(); atom++) {
if (mol.getAtomParity$I(atom) != 0 && mol.getAtomParity$I(atom) != 3  && mol.getAtomESRType$I(atom) == 1 ) {
var group=mol.getAtomESRGroup$I(atom);
++esrGroupCount[group];
if (maxGroupCount < esrGroupCount[group]) {
maxGroupCount=esrGroupCount[group];
maxESRGroup=group;
}break;
}}
}this.mBuilder=(builder == null ) ? Clazz.new_($I$(1,1)) : builder;
var name=(mol.getName$() != null ) ? mol.getName$() : "";
this.mBuilder.append$S(name + nl);
this.mBuilder.append$S("Actelion Java MolfileCreator 1.0" + nl + nl );
p$1.appendThreeDigitInt$I.apply(this, [mol.getAllAtoms$()]);
p$1.appendThreeDigitInt$I.apply(this, [mol.getAllBonds$()]);
this.mBuilder.append$S("  0  0");
p$1.appendThreeDigitInt$I.apply(this, [(!isRacemic) ? 1 : 0]);
this.mBuilder.append$S("  0  0  0  0  0999 V2000" + nl);
var hasCoordinates=(mol.getAllAtoms$() == 1);
for (var atom=1; atom < mol.getAllAtoms$(); atom++) {
if (mol.getAtomX$I(atom) != mol.getAtomX$I(0)  || mol.getAtomY$I(atom) != mol.getAtomY$I(0)   || mol.getAtomZ$I(atom) != mol.getAtomZ$I(0)  ) {
hasCoordinates=true;
break;
}}
var grafac=1.0;
if (hasCoordinates) {
if (scalingFactor != 0 ) {
grafac=scalingFactor;
} else if (allowScaling) {
var avbl=mol.getAverageBondLength$();
if (avbl != 0.0 ) {
if (avbl < 1.0  || avbl > 3.0  ) grafac=1.5 / avbl;
} else {
var minDistance=1.7976931348623157E308;
for (var atom1=1; atom1 < mol.getAllAtoms$(); atom1++) {
for (var atom2=0; atom2 < atom1; atom2++) {
var dx=mol.getAtomX$I(atom2) - mol.getAtomX$I(atom1);
var dy=mol.getAtomY$I(atom2) - mol.getAtomY$I(atom1);
var dz=mol.getAtomZ$I(atom2) - mol.getAtomZ$I(atom1);
var distance=dx * dx + dy * dy + dz * dz;
if (minDistance > distance ) minDistance=distance;
}
}
grafac=3.0 / minDistance;
}}}for (var atom=0; atom < mol.getAllAtoms$(); atom++) {
if (hasCoordinates) {
p$1.appendTenDigitDouble$D.apply(this, [grafac * mol.getAtomX$I(atom)]);
p$1.appendTenDigitDouble$D.apply(this, [grafac * -mol.getAtomY$I(atom)]);
p$1.appendTenDigitDouble$D.apply(this, [grafac * -mol.getAtomZ$I(atom)]);
} else {
this.mBuilder.append$S("    0.0000    0.0000    0.0000");
}if (mol.getAtomList$I(atom) != null ) this.mBuilder.append$S(" L  ");
 else if (Long.$ne((Long.$and(mol.getAtomQueryFeatures$I(atom),1)),0 )) this.mBuilder.append$S(" A  ");
 else if ((mol.getAtomicNo$I(atom) >= 129 && mol.getAtomicNo$I(atom) <= 144 ) || mol.getAtomicNo$I(atom) == 154 ) this.mBuilder.append$S(" R# ");
 else {
var atomLabel=mol.getAtomLabel$I(atom);
this.mBuilder.append$S(" " + atomLabel);
if (atomLabel.length$() == 1) this.mBuilder.append$S("  ");
 else if (atomLabel.length$() == 2) this.mBuilder.append$S(" ");
}this.mBuilder.append$S(" 0  0  0");
var hydrogenFlags=Long.$and(1920,mol.getAtomQueryFeatures$I(atom));
if (Long.$eq(hydrogenFlags,0 )) this.mBuilder.append$S("  0");
 else if (Long.$eq(hydrogenFlags,(384) )) this.mBuilder.append$S("  3");
 else if (Long.$eq(hydrogenFlags,128 )) this.mBuilder.append$S("  2");
 else if (Long.$eq(hydrogenFlags,(1792) )) this.mBuilder.append$S("  1");
 else if (Long.$eq(hydrogenFlags,(1664) )) this.mBuilder.append$S("  2");
this.mBuilder.append$S((Long.$ne((Long.$and(mol.getAtomQueryFeatures$I(atom),8192)),0 )) ? "  1" : "  0");
var valence=mol.getAtomAbnormalValence$I(atom);
if (valence == -1) this.mBuilder.append$S("  0");
 else if (valence == 0) this.mBuilder.append$S(" 15");
 else p$1.appendThreeDigitInt$I.apply(this, [valence]);
this.mBuilder.append$S("  0  0  0");
p$1.appendThreeDigitInt$I.apply(this, [mol.getAtomMapNo$I(atom)]);
this.mBuilder.append$S("  0  0" + nl);
}
for (var bond=0; bond < mol.getAllBonds$(); bond++) {
var order;
var stereo;
switch (mol.getBondType$I(bond)) {
case 1:
order=1;
stereo=0;
break;
case 2:
order=2;
stereo=0;
break;
case 4:
order=3;
stereo=0;
break;
case 129:
order=1;
stereo=6;
break;
case 257:
order=1;
stereo=1;
break;
case 386:
order=2;
stereo=3;
break;
case 64:
order=4;
stereo=0;
break;
case 32:
order=8;
stereo=0;
break;
default:
order=1;
stereo=0;
break;
}
if (isRacemic && (stereo == 1 || stereo == 6 ) ) {
var atom=mol.getBondAtom$I$I(0, bond);
if (mol.getAtomESRType$I(atom) == 2) stereo=0;
 else if (mol.getAtomESRType$I(atom) == 1 && mol.getAtomESRGroup$I(atom) != maxESRGroup ) stereo=4;
}var bondType=mol.getBondQueryFeatures$I(bond) & 31;
if (bondType != 0) {
if (bondType == 8) order=4;
 else if (bondType == (3)) order=5;
 else if (bondType == (9)) order=6;
 else if (bondType == (10)) order=7;
 else order=8;
}var ringState=mol.getBondQueryFeatures$I(bond) & 384;
var topology=(ringState == 0) ? 0 : (ringState == 256) ? 1 : 2;
p$1.appendThreeDigitInt$I.apply(this, [1 + mol.getBondAtom$I$I(0, bond)]);
p$1.appendThreeDigitInt$I.apply(this, [1 + mol.getBondAtom$I$I(1, bond)]);
p$1.appendThreeDigitInt$I.apply(this, [order]);
p$1.appendThreeDigitInt$I.apply(this, [stereo]);
this.mBuilder.append$S("  0");
p$1.appendThreeDigitInt$I.apply(this, [topology]);
this.mBuilder.append$S("  0" + nl);
}
var no=0;
for (var atom=0; atom < mol.getAllAtoms$(); atom++) if (mol.getAtomCharge$I(atom) != 0) ++no;

if (no != 0) {
var count=0;
for (var atom=0; atom < mol.getAllAtoms$(); atom++) {
if (mol.getAtomCharge$I(atom) != 0) {
if (count == 0) {
this.mBuilder.append$S("M  CHG");
p$1.appendThreeDigitInt$I.apply(this, [Math.min(8, no)]);
}this.mBuilder.append$S(" ");
p$1.appendThreeDigitInt$I.apply(this, [atom + 1]);
var charge=mol.getAtomCharge$I(atom);
if (charge < 0) {
this.mBuilder.append$S("  -");
charge=-charge;
} else this.mBuilder.append$S("   ");
this.mBuilder.append$C(String.fromCharCode((48 + charge)));
--no;
if (++count == 8 || no == 0 ) {
count=0;
this.mBuilder.append$S(nl);
}}}
}no=0;
for (var atom=0; atom < mol.getAllAtoms$(); atom++) if (!mol.isNaturalAbundance$I(atom)) ++no;

if (no != 0) {
var count=0;
for (var atom=0; atom < mol.getAllAtoms$(); atom++) {
if (!mol.isNaturalAbundance$I(atom)) {
if (count == 0) {
this.mBuilder.append$S("M  ISO");
p$1.appendThreeDigitInt$I.apply(this, [Math.min(8, no)]);
}this.mBuilder.append$S(" ");
p$1.appendThreeDigitInt$I.apply(this, [atom + 1]);
this.mBuilder.append$S(" ");
p$1.appendThreeDigitInt$I.apply(this, [mol.getAtomMass$I(atom)]);
--no;
if (++count == 8 || no == 0 ) {
count=0;
this.mBuilder.append$S(nl);
}}}
}no=0;
for (var atom=0; atom < mol.getAllAtoms$(); atom++) if (mol.getAtomRadical$I(atom) != 0) ++no;

if (no != 0) {
var count=0;
for (var atom=0; atom < mol.getAllAtoms$(); atom++) {
if (mol.getAtomRadical$I(atom) != 0) {
if (count == 0) {
this.mBuilder.append$S("M  RAD");
p$1.appendThreeDigitInt$I.apply(this, [Math.min(8, no)]);
}this.mBuilder.append$S(" ");
p$1.appendThreeDigitInt$I.apply(this, [atom + 1]);
switch (mol.getAtomRadical$I(atom)) {
case 16:
this.mBuilder.append$S("   1");
break;
case 32:
this.mBuilder.append$S("   2");
break;
case 48:
this.mBuilder.append$S("   3");
break;
}
--no;
if (++count == 8 || no == 0 ) {
count=0;
this.mBuilder.append$S(nl);
}}}
}no=0;
for (var atom=0; atom < mol.getAllAtoms$(); atom++) if ((mol.getAtomicNo$I(atom) >= 129 && mol.getAtomicNo$I(atom) <= 144 ) || mol.getAtomicNo$I(atom) == 154 ) ++no;

if (no != 0) {
var count=0;
for (var atom=0; atom < mol.getAllAtoms$(); atom++) {
var atomicNo=mol.getAtomicNo$I(atom);
if ((atomicNo >= 129 && atomicNo <= 144 ) || atomicNo == 154 ) {
if (count == 0) {
this.mBuilder.append$S("M  RGP");
p$1.appendThreeDigitInt$I.apply(this, [Math.min(8, no)]);
}this.mBuilder.append$S(" ");
p$1.appendThreeDigitInt$I.apply(this, [atom + 1]);
this.mBuilder.append$S(" ");
p$1.appendThreeDigitInt$I.apply(this, [atomicNo == 154 ? 0 : atomicNo >= 142 ? atomicNo - 141 : atomicNo - 125]);
--no;
if (++count == 8 || no == 0 ) {
count=0;
this.mBuilder.append$S(nl);
}}}
}if (mol.isFragment$()) {
no=0;
for (var atom=0; atom < mol.getAllAtoms$(); atom++) if (Long.$ne((Long.$and(mol.getAtomQueryFeatures$I(atom),120)),0 )) ++no;

if (no != 0) {
var count=0;
for (var atom=0; atom < mol.getAllAtoms$(); atom++) {
var ringFeatures=Long.$and(mol.getAtomQueryFeatures$I(atom),120);
if (Long.$ne(ringFeatures,0 )) {
if (count == 0) {
this.mBuilder.append$S("M  RBC");
p$1.appendThreeDigitInt$I.apply(this, [Math.min(8, no)]);
}this.mBuilder.append$S(" ");
p$1.appendThreeDigitInt$I.apply(this, [atom + 1]);
if (Long.$eq(ringFeatures,(112) )) this.mBuilder.append$S("  -1");
 else if (Long.$eq(ringFeatures,8 )) this.mBuilder.append$S("   1");
 else if (Long.$eq(ringFeatures,(104) )) this.mBuilder.append$S("   2");
 else if (Long.$eq(ringFeatures,(88) )) this.mBuilder.append$S("   3");
 else if (Long.$eq(ringFeatures,(56) )) this.mBuilder.append$S("   4");
--no;
if (++count == 8 || no == 0 ) {
count=0;
this.mBuilder.append$S(nl);
}}}
}for (var atom=0; atom < mol.getAllAtoms$(); atom++) {
var atomList=mol.getAtomList$I(atom);
if (atomList != null ) {
this.mBuilder.append$S("M  ALS ");
p$1.appendThreeDigitInt$I.apply(this, [atom + 1]);
p$1.appendThreeDigitInt$I.apply(this, [atomList.length]);
this.mBuilder.append$S((Long.$ne((Long.$and(mol.getAtomQueryFeatures$I(atom),1)),0 )) ? " T " : " F ");
for (var i=0; i < atomList.length; i++) {
var label=$I$(5).cAtomLabel[atomList[i]];
switch (label.length$()) {
case 1:
this.mBuilder.append$S(label + "   ");
break;
case 2:
this.mBuilder.append$S(label + "  ");
break;
case 3:
this.mBuilder.append$S(label + " ");
break;
default:
this.mBuilder.append$S("   ?");
break;
}
}
this.mBuilder.append$S(nl);
}}
no=0;
for (var atom=0; atom < mol.getAllAtoms$(); atom++) if (Long.$ne((Long.$and(mol.getAtomQueryFeatures$I(atom),(6144))),0 )) ++no;

if (no != 0) {
var count=0;
for (var atom=0; atom < mol.getAllAtoms$(); atom++) {
var substitution=Long.$and(mol.getAtomQueryFeatures$I(atom),(6144));
if (Long.$ne(substitution,0 )) {
if (count == 0) {
this.mBuilder.append$S("M  SUB");
p$1.appendThreeDigitInt$I.apply(this, [Math.min(8, no)]);
}this.mBuilder.append$S(" ");
p$1.appendThreeDigitInt$I.apply(this, [atom + 1]);
if (Long.$ne((Long.$and(substitution,4096)),0 )) this.mBuilder.append$S("   " + (mol.getAllConnAtoms$I(atom) + 1));
 else this.mBuilder.append$S("  -2");
--no;
if (++count == 8 || no == 0 ) {
count=0;
this.mBuilder.append$S(nl);
}}}
}}this.mBuilder.append$S("M  END" + nl);
}, 1);

Clazz.newMeth(C$, 'getMolfile$',  function () {
return this.mBuilder.toString();
});

Clazz.newMeth(C$, 'writeMolfile$java_io_Writer',  function (theWriter) {
theWriter.write$S(this.mBuilder.toString());
});

Clazz.newMeth(C$, 'appendThreeDigitInt$I',  function (data) {
if (data < 0 || data > 999 ) {
this.mBuilder.append$S("  ?");
return;
}var digitFound=false;
for (var i=0; i < 3; i++) {
var theChar=(data/100|0);
if (theChar == 0) {
if (i == 2 || digitFound ) this.mBuilder.append$C("0");
 else this.mBuilder.append$C(" ");
} else {
this.mBuilder.append$C(String.fromCharCode((48 + theChar)));
digitFound=true;
}data=10 * (data % 100);
}
}, p$1);

Clazz.newMeth(C$, 'appendTenDigitDouble$D',  function (theDouble) {
var val=this.mDoubleFormat.format$D(theDouble);
for (var i=val.length$(); i < 10; i++) this.mBuilder.append$C(" ");

this.mBuilder.append$S(val);
}, p$1);

Clazz.newMeth(C$);
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-25 13:07:45 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
