(function(){var P$=Clazz.newPackage("com.actelion.research.chem.coords"),p$1={},I$=[[0,'com.actelion.research.chem.coords.InventorDefaultTemplateList','java.util.Random','java.util.ArrayList','com.actelion.research.chem.SSSearcherWithIndex','com.actelion.research.chem.SSSearcher','com.actelion.research.chem.coords.InventorFragment','com.actelion.research.chem.coords.InventorChain','com.actelion.research.chem.coords.InventorAngle','com.actelion.research.chem.Canonizer','com.actelion.research.chem.CanonizerBaseValue','java.util.Arrays','com.actelion.research.chem.coords.FragmentAssociation','com.actelion.research.chem.coords.InventorCharge','java.util.Collections']],I$0=I$[0],$I$=function(i,n,m){return m?$I$(i)[n].apply(null,m):((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "CoordinateInventor");

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['Z',['mAbsoluteOrientationTemplateFound'],'I',['mMode'],'O',['mMol','com.actelion.research.chem.StereoMolecule','mFFP','long[]','mRandom','java.util.Random','mAtomHandled','boolean[]','+mBondHandled','+mAtomIsPartOfCustomTemplate','mUnPairedCharge','int[]','mFragmentList','java.util.List','+mCustomTemplateList']]
,['O',['sDefaultTemplateList','java.util.List']]]

Clazz.newMeth(C$, 'buildDefaultTemplateList$',  function () {
if (C$.sDefaultTemplateList == null ) C$.sDefaultTemplateList=Clazz.new_($I$(1,1));
}, 1);

Clazz.newMeth(C$, 'c$',  function () {
C$.c$$I.apply(this, [2]);
}, 1);

Clazz.newMeth(C$, 'c$$I',  function (mode) {
;C$.$init$.apply(this);
this.mMode=mode;
if ((mode & 1) == 0 && C$.sDefaultTemplateList == null  ) C$.buildDefaultTemplateList$();
}, 1);

Clazz.newMeth(C$, 'setRandomSeed$J',  function (seed) {
this.mRandom=Clazz.new_($I$(2,1).c$$J,[seed]);
});

Clazz.newMeth(C$, 'setCustomTemplateList$java_util_List',  function (templateList) {
this.mCustomTemplateList=templateList;
for (var template, $template = templateList.iterator$(); $template.hasNext$()&&((template=($template.next$())),1);) template.normalizeCoordinates$();

});

Clazz.newMeth(C$, 'invent$com_actelion_research_chem_StereoMolecule',  function (mol) {
this.invent$com_actelion_research_chem_StereoMolecule$JA(mol, null);
});

Clazz.newMeth(C$, 'invent$com_actelion_research_chem_StereoMolecule$JA',  function (mol, ffp) {
var paritiesPresent=(mol.getHelperArrayStatus$() & 15) != 0;
var parityState=mol.getHelperArrayStatus$() & 248;
if (this.mRandom == null ) this.mRandom=Clazz.new_($I$(2,1));
if ((this.mMode & 2) != 0) mol.removeExplicitHydrogens$Z$Z(false, false);
this.mMol=mol;
this.mMol.ensureHelperArrays$I(7);
this.mFFP=ffp;
this.mFragmentList=((P$.CoordinateInventor$1||
(function(){/*a*/var C$=Clazz.newClass(P$, "CoordinateInventor$1", function(){Clazz.newInstance(this, arguments[0],1,C$);}, Clazz.load('java.util.ArrayList'), null, 1);

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

Clazz.newMeth(C$, ['add$com_actelion_research_chem_coords_InventorFragment','add$O'],  function (f) {
for (var ff, $ff = this.iterator$(); $ff.hasNext$()&&((ff=($ff.next$())),1);) if (ff.equals$com_actelion_research_chem_coords_InventorFragment(f)) return false;

return C$.superclazz.prototype.add$O.apply(this, [f]);
});
})()
), Clazz.new_($I$(3,1),[this, null],P$.CoordinateInventor$1));
this.mAtomHandled=Clazz.array(Boolean.TYPE, [this.mMol.getAllAtoms$()]);
this.mBondHandled=Clazz.array(Boolean.TYPE, [this.mMol.getAllBonds$()]);
this.mUnPairedCharge=Clazz.array(Integer.TYPE, [this.mMol.getAllAtoms$()]);
for (var atom=0; atom < this.mMol.getAllAtoms$(); atom++) this.mUnPairedCharge[atom]=this.mMol.getAtomCharge$I(atom);

if ((this.mMode & 12) != 0) {
p$1.locateMarkedFragments.apply(this, []);
}if (this.mCustomTemplateList != null ) this.mAtomIsPartOfCustomTemplate=p$1.locateTemplateFragments$java_util_List$I.apply(this, [this.mCustomTemplateList, 512]);
if ((this.mMode & 1) == 0 && C$.sDefaultTemplateList != null  ) p$1.locateTemplateFragments$java_util_List$I.apply(this, [C$.sDefaultTemplateList, 256]);
p$1.locateInitialFragments.apply(this, []);
p$1.joinOverlappingFragments.apply(this, []);
p$1.locateChainFragments.apply(this, []);
p$1.joinOverlappingFragments.apply(this, []);
for (var f, $f = this.mFragmentList.iterator$(); $f.hasNext$()&&((f=($f.next$())),1);) f.locateBonds$();

p$1.correctChainEZParities.apply(this, []);
p$1.optimizeFragments.apply(this, []);
p$1.locateSingleAtoms.apply(this, []);
p$1.joinMetalBondedFragments.apply(this, []);
p$1.joinChargedFragments.apply(this, []);
p$1.joinRemainingFragments.apply(this, []);
for (var i=0; i < this.mFragmentList.size$(); i++) {
var f=this.mFragmentList.get$I(i);
for (var j=0; j < f.size$(); j++) {
this.mMol.setAtomX$I$D(f.mGlobalAtom[j], f.mAtomX[j]);
this.mMol.setAtomY$I$D(f.mGlobalAtom[j], f.mAtomY[j]);
this.mMol.setAtomZ$I$D(f.mGlobalAtom[j], 0.0);
}
}
if (paritiesPresent) {
this.mMol.setParitiesValid$I(parityState);
this.mMol.setStereoBondsFromParity$();
}if (this.mAbsoluteOrientationTemplateFound) this.mMol.removeAtomMarkers$();
});

Clazz.newMeth(C$, 'getCustomTemplateAtomMask$',  function () {
return this.mAtomIsPartOfCustomTemplate;
});

Clazz.newMeth(C$, 'locateTemplateFragments$java_util_List$I',  function (templateList, priority) {
var useFFP=(this.mFFP != null  && templateList.size$() != 0  && templateList.get$I(0).getFFP$() != null  );
var searcher=null;
var searcherWithIndex=null;
if (useFFP) {
searcherWithIndex=Clazz.new_($I$(4,1));
searcherWithIndex.setMolecule$com_actelion_research_chem_StereoMolecule$JA(this.mMol, this.mFFP);
} else {
searcher=Clazz.new_($I$(5,1));
searcher.setMolecule$com_actelion_research_chem_StereoMolecule(this.mMol);
}var atomIsPartOfTemplate=Clazz.array(Boolean.TYPE, [this.mMol.getAtoms$()]);
for (var template, $template = templateList.iterator$(); $template.hasNext$()&&((template=($template.next$())),1);) {
var matchList=null;
var templateMol=template.getFragment$();
if (useFFP) {
searcherWithIndex.setFragment$com_actelion_research_chem_StereoMolecule$JA(templateMol, template.getFFP$());
if (searcherWithIndex.findFragmentInMolecule$I$I(4, 8) != 0) matchList=searcherWithIndex.getGraphMatcher$().getMatchList$();
} else {
searcher.setFragment$com_actelion_research_chem_StereoMolecule(templateMol);
if (searcher.findFragmentInMolecule$I$I(4, 8) != 0) matchList=searcher.getMatchList$();
}if (matchList != null ) {
for (var match, $match = matchList.iterator$(); $match.hasNext$()&&((match=($match.next$())),1);) {
var templateAtomCount=0;
for (var atom, $atom = 0, $$atom = match; $atom<$$atom.length&&((atom=($$atom[$atom])),1);$atom++) if (atomIsPartOfTemplate[atom]) ++templateAtomCount;

if (templateAtomCount <= 1) {
var definesAbsoluteOrientation=template.keepAbsoluteOrientation$();
if (this.mAbsoluteOrientationTemplateFound) definesAbsoluteOrientation=false;
 else this.mAbsoluteOrientationTemplateFound=true;
var fragment=Clazz.new_($I$(6,1).c$$com_actelion_research_chem_StereoMolecule$I$Z,[this.mMol, match.length, definesAbsoluteOrientation]);
for (var i=0; i < match.length; i++) {
var atom=match[i];
if (definesAbsoluteOrientation) this.mMol.setAtomMarker$I$Z(atom, true);
fragment.mPriority[i]=priority;
fragment.mGlobalAtom[i]=atom;
fragment.mAtomX[i]=template.getNormalizedAtomX$I(i);
fragment.mAtomY[i]=template.getNormalizedAtomY$I(i);
atomIsPartOfTemplate[atom]=true;
this.mAtomHandled[atom]=true;
}
for (var b=0; b < templateMol.getBonds$(); b++) this.mBondHandled[this.mMol.getBond$I$I(match[templateMol.getBondAtom$I$I(0, b)], match[templateMol.getBondAtom$I$I(1, b)])]=true;

this.mFragmentList.add$O(fragment);
}}
}}
return atomIsPartOfTemplate;
}, p$1);

Clazz.newMeth(C$, 'locateMarkedFragments',  function () {
var atomCount=0;
for (var atom=0; atom < this.mMol.getAllAtoms$(); atom++) if (this.mMol.isMarkedAtom$I(atom)) ++atomCount;

if (atomCount < 2) return;
var bondCount=0;
var avbl=0;
for (var bond=0; bond < this.mMol.getAllBonds$(); bond++) {
var atom1=this.mMol.getBondAtom$I$I(0, bond);
var atom2=this.mMol.getBondAtom$I$I(1, bond);
if (this.mMol.isMarkedAtom$I(atom1) && this.mMol.isMarkedAtom$I(atom2) ) {
this.mBondHandled[bond]=true;
this.mAtomHandled[atom1]=true;
this.mAtomHandled[atom2]=true;
avbl+=this.mMol.getBondLength$I(bond);
++bondCount;
}}
if (bondCount != 0 && avbl != 0.0  ) avbl/=bondCount;
 else {
avbl=this.mMol.getAverageBondLength$();
}for (var atom=0; atom < this.mMol.getAllAtoms$(); atom++) if (this.mMol.isMarkedAtom$I(atom) && !this.mAtomHandled[atom] ) --atomCount;

if (atomCount < 2) return;
var fragmentNo=Clazz.array(Integer.TYPE, [this.mMol.getAllAtoms$()]);
var coreFragmentCount=this.mMol.getFragmentNumbers$IA$Z$Z(fragmentNo, true, true);
var fragmentAtomCount=Clazz.array(Integer.TYPE, [coreFragmentCount]);
for (var atom=0; atom < this.mMol.getAllAtoms$(); atom++) if (fragmentNo[atom] != -1) ++fragmentAtomCount[fragmentNo[atom]];

var fragment=Clazz.array($I$(6), [coreFragmentCount]);
for (var f=0; f < coreFragmentCount; f++) fragment[f]=Clazz.new_($I$(6,1).c$$com_actelion_research_chem_StereoMolecule$I$Z,[this.mMol, fragmentAtomCount[f], true]);

var atomIndex=Clazz.array(Integer.TYPE, [coreFragmentCount]);
for (var atom=0; atom < this.mMol.getAllAtoms$(); atom++) {
var f=fragmentNo[atom];
if (f != -1) {
fragment[f].mPriority[atomIndex[f]]=1024;
fragment[f].mGlobalAtom[atomIndex[f]]=atom;
fragment[f].mAtomX[atomIndex[f]]=this.mMol.getAtomX$I(atom) / avbl;
fragment[f].mAtomY[atomIndex[f]]=this.mMol.getAtomY$I(atom) / avbl;
++atomIndex[f];
}}
var maxFragment=-1;
var maxFragmentAtoms=0;
for (var f=0; f < coreFragmentCount; f++) {
if (maxFragmentAtoms < fragmentAtomCount[f]) {
maxFragmentAtoms=fragmentAtomCount[f];
maxFragment=f;
}}
this.mFragmentList.add$O(fragment[maxFragment]);
for (var f=0; f < coreFragmentCount; f++) if (f != maxFragment) this.mFragmentList.add$O(fragment[f]);

}, p$1);

Clazz.newMeth(C$, 'locateInitialFragments',  function () {
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
if (this.mMol.getAllConnAtoms$I(atom) > 4) {
var f=Clazz.new_([this.mMol, 1 + this.mMol.getAllConnAtoms$I(atom), false],$I$(6,1).c$$com_actelion_research_chem_StereoMolecule$I$Z);
f.mAtomX[this.mMol.getAllConnAtoms$I(atom)]=0.0;
f.mAtomY[this.mMol.getAllConnAtoms$I(atom)]=0.0;
f.mPriority[this.mMol.getAllConnAtoms$I(atom)]=32;
f.mGlobalAtom[this.mMol.getAllConnAtoms$I(atom)]=atom;
this.mAtomHandled[atom]=true;
for (var i=0; i < this.mMol.getAllConnAtoms$I(atom); i++) {
var connAtom=this.mMol.getConnAtom$I$I(atom, i);
f.mAtomX[i]=Math.sin(1.0471975511965976 * i - 2.0943951023931953);
f.mAtomY[i]=Math.cos(1.0471975511965976 * i - 2.0943951023931953);
f.mPriority[i]=32;
f.mGlobalAtom[i]=connAtom;
this.mAtomHandled[connAtom]=true;
this.mBondHandled[this.mMol.getConnBond$I$I(atom, i)]=true;
}
this.mFragmentList.add$O(f);
}}
var ringSet=this.mMol.getRingSet$();
for (var ringNo=0; ringNo < ringSet.getSize$(); ringNo++) {
var ringSize=ringSet.getRingSize$I(ringNo);
var ringAtom=ringSet.getRingAtoms$I(ringNo);
var skipRing=false;
if ((this.mMode & 12) != 0) {
skipRing=true;
for (var i=0; i < ringSize; i++) {
if (!this.mMol.isMarkedAtom$I(ringAtom[i])) {
skipRing=false;
break;
}}
}if (!skipRing) {
var isElementaryRing=false;
for (var i=0; i < ringSize; i++) {
if (this.mMol.getAtomRingSize$I(ringAtom[i]) == ringSize) {
isElementaryRing=true;
break;
}}
if (isElementaryRing) {
var ringBond=ringSet.getRingBonds$I(ringNo);
p$1.addRingFragment$IA$IA.apply(this, [ringAtom, ringBond]);
for (var i=0; i < ringSize; i++) {
this.mAtomHandled[ringAtom[i]]=true;
this.mBondHandled[ringBond[i]]=true;
}
}}}
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mMol.isRingBond$I(bond) && !this.mBondHandled[bond] ) {
var theRing=p$1.getSmallestRingFromBond$I.apply(this, [bond]);
var ringAtom=theRing.getRingAtoms$();
var ringBond=theRing.getRingBonds$();
p$1.addRingFragment$IA$IA.apply(this, [ringAtom, ringBond]);
for (var i=0; i < theRing.getChainLength$(); i++) {
this.mAtomHandled[ringAtom[i]]=true;
this.mBondHandled[ringBond[i]]=true;
}
}}
for (var bond=0; bond < this.mMol.getAllBonds$(); bond++) {
if (!this.mBondHandled[bond] && this.mMol.getBondOrder$I(bond) == 3 ) {
var atom1=this.mMol.getBondAtom$I$I(0, bond);
var atom2=this.mMol.getBondAtom$I$I(1, bond);
var members=this.mMol.getAllConnAtoms$I(atom1) + this.mMol.getAllConnAtoms$I(atom2);
if (members > 2) {
var f=Clazz.new_($I$(6,1).c$$com_actelion_research_chem_StereoMolecule$I$Z,[this.mMol, members, false]);
var count=0;
for (var i=0; i < this.mMol.getAllConnAtoms$I(atom1); i++) {
var connAtom=this.mMol.getConnAtom$I$I(atom1, i);
if (connAtom != atom2) {
f.mGlobalAtom[count++]=connAtom;
this.mAtomHandled[connAtom]=true;
this.mBondHandled[this.mMol.getConnBond$I$I(atom1, i)]=true;
}}
f.mGlobalAtom[count++]=atom1;
f.mGlobalAtom[count++]=atom2;
for (var i=0; i < this.mMol.getAllConnAtoms$I(atom2); i++) {
var connAtom=this.mMol.getConnAtom$I$I(atom2, i);
if (connAtom != atom1) {
f.mGlobalAtom[count++]=connAtom;
this.mAtomHandled[connAtom]=true;
this.mBondHandled[this.mMol.getConnBond$I$I(atom2, i)]=true;
}}
for (var i=0; i < members; i++) {
f.mAtomX[i]=i;
f.mAtomY[i]=0.0;
f.mPriority[i]=1;
}
this.mAtomHandled[atom1]=true;
this.mAtomHandled[atom2]=true;
this.mBondHandled[bond]=true;
this.mFragmentList.add$O(f);
}}}
for (var bond=0; bond < this.mMol.getAllBonds$(); bond++) {
if (!this.mBondHandled[bond] && this.mMol.getBondOrder$I(bond) == 2 ) {
var alleneAtom=Clazz.array(Integer.TYPE, [this.mMol.getAllAtoms$()]);
for (var i=0; i < 2; i++) {
alleneAtom[0]=this.mMol.getBondAtom$I$I(i, bond);
alleneAtom[1]=this.mMol.getBondAtom$I$I(1 - i, bond);
if (this.mMol.getAtomPi$I(alleneAtom[0]) == 1 && this.mMol.getAtomPi$I(alleneAtom[1]) == 2  && this.mMol.getAllConnAtoms$I(alleneAtom[1]) == 2 ) {
this.mAtomHandled[alleneAtom[0]]=true;
this.mAtomHandled[alleneAtom[1]]=true;
this.mBondHandled[bond]=true;
var last=1;
do {
var nextIndex=(this.mMol.getConnAtom$I$I(alleneAtom[last], 0) == alleneAtom[last - 1]) ? 1 : 0;
alleneAtom[last + 1]=this.mMol.getConnAtom$I$I(alleneAtom[last], nextIndex);
if (this.mMol.getAtomPi$I(alleneAtom[last + 1]) == 2 && this.mMol.getAllConnAtoms$I(alleneAtom[last + 1]) > 2 ) break;
this.mAtomHandled[alleneAtom[last + 1]]=true;
this.mBondHandled[this.mMol.getConnBond$I$I(alleneAtom[last], nextIndex)]=true;
++last;
} while (this.mMol.getAtomPi$I(alleneAtom[last]) == 2 && this.mMol.getAllConnAtoms$I(alleneAtom[last]) == 2 );
var members=this.mMol.getAllConnAtoms$I(alleneAtom[0]) + this.mMol.getAllConnAtoms$I(alleneAtom[last]) + last  - 1;
var f=Clazz.new_($I$(6,1).c$$com_actelion_research_chem_StereoMolecule$I$Z,[this.mMol, members, false]);
for (var j=0; j <= last; j++) {
f.mAtomX[j]=j;
f.mAtomY[j]=0.0;
f.mPriority[j]=64;
f.mGlobalAtom[j]=alleneAtom[j];
}
var current=last + 1;
var found=false;
for (var j=0; j < this.mMol.getAllConnAtoms$I(alleneAtom[0]); j++) {
var connAtom=this.mMol.getConnAtom$I$I(alleneAtom[0], j);
if (connAtom != alleneAtom[1]) {
f.mAtomX[current]=-0.5;
f.mAtomY[current]=(found) ? Math.sin(1.0471975511965976) : -Math.sin(1.0471975511965976);
f.mPriority[current]=64;
f.mGlobalAtom[current]=connAtom;
++current;
found=true;
}}
found=false;
for (var j=0; j < this.mMol.getAllConnAtoms$I(alleneAtom[last]); j++) {
var connAtom=this.mMol.getConnAtom$I$I(alleneAtom[last], j);
if (connAtom != alleneAtom[last - 1]) {
f.mAtomX[current]=last + 0.5;
f.mAtomY[current]=(found) ? -Math.sin(1.0471975511965976) : Math.sin(1.0471975511965976);
f.mPriority[current]=64;
f.mGlobalAtom[current]=connAtom;
++current;
found=true;
}}
this.mFragmentList.add$O(f);
}}
}}
for (var atom=0; atom < this.mMol.getAllAtoms$(); atom++) {
if (this.mMol.getAllConnAtoms$I(atom) == 4) {
var primaryConnAtom=Clazz.array(Integer.TYPE, [4]);
var primaryConnBond=Clazz.array(Integer.TYPE, [4]);
var primaryConns=0;
for (var i=0; i < 4; i++) {
primaryConnAtom[primaryConns]=this.mMol.getConnAtom$I$I(atom, i);
primaryConnBond[primaryConns]=this.mMol.getConnBond$I$I(atom, i);
if (this.mMol.getAllConnAtoms$I(primaryConnAtom[primaryConns]) == 1 && !this.mBondHandled[primaryConnBond[primaryConns]] ) ++primaryConns;
}
if (primaryConns == 2) {
var f=Clazz.new_($I$(6,1).c$$com_actelion_research_chem_StereoMolecule$I$Z,[this.mMol, 3, false]);
for (var i=0; i < 2; i++) {
this.mAtomHandled[primaryConnAtom[i]]=true;
this.mBondHandled[primaryConnBond[i]]=true;
f.mGlobalAtom[i]=primaryConnAtom[i];
f.mPriority[i]=32;
}
f.mAtomX[0]=-0.5;
f.mAtomY[0]=0.866;
f.mAtomX[1]=0.5;
f.mAtomY[1]=0.866;
f.mAtomX[2]=0.0;
f.mAtomY[2]=0.0;
f.mPriority[2]=32;
f.mGlobalAtom[2]=atom;
this.mFragmentList.add$O(f);
}if (primaryConns == 3) {
for (var i=0; i < 2; i++) {
if (this.mMol.getBondOrder$I(primaryConnBond[i]) == 1) {
var temp=primaryConnAtom[i];
primaryConnAtom[i]=primaryConnAtom[2];
primaryConnAtom[2]=temp;
temp=primaryConnBond[i];
primaryConnBond[i]=primaryConnBond[2];
primaryConnBond[2]=temp;
}}
var f=Clazz.new_($I$(6,1).c$$com_actelion_research_chem_StereoMolecule$I$Z,[this.mMol, 4, false]);
for (var i=0; i < 3; i++) {
this.mAtomHandled[primaryConnAtom[i]]=true;
this.mBondHandled[primaryConnBond[i]]=true;
f.mGlobalAtom[i]=primaryConnAtom[i];
f.mPriority[i]=32;
}
f.mAtomX[0]=-1.0;
f.mAtomY[0]=0.0;
f.mAtomX[1]=1.0;
f.mAtomY[1]=0.0;
f.mAtomX[2]=0.0;
f.mAtomY[2]=1.0;
f.mAtomX[3]=0.0;
f.mAtomY[3]=0.0;
f.mPriority[3]=32;
f.mGlobalAtom[3]=atom;
this.mFragmentList.add$O(f);
}}}
}, p$1);

Clazz.newMeth(C$, 'locateChainFragments',  function () {
while (true){
var longestChain=null;
for (var atom=0; atom < this.mMol.getAllAtoms$(); atom++) {
var unhandledBonds=0;
for (var i=0; i < this.mMol.getAllConnAtoms$I(atom); i++) if (!this.mBondHandled[this.mMol.getConnBond$I$I(atom, i)]) ++unhandledBonds;

if (unhandledBonds == 1) {
var theChain=p$1.getLongestUnhandledChain$I.apply(this, [atom]);
if (longestChain == null  || theChain.getChainLength$() > longestChain.getChainLength$() ) longestChain=theChain;
}}
if (longestChain == null ) break;
var f=Clazz.new_([this.mMol, longestChain.getChainLength$(), false],$I$(6,1).c$$com_actelion_research_chem_StereoMolecule$I$Z);
for (var i=0; i < longestChain.getChainLength$(); i++) {
this.mAtomHandled[longestChain.mAtom[i]]=true;
if (i < longestChain.getChainLength$() - 1) this.mBondHandled[longestChain.mBond[i]]=true;
f.mGlobalAtom[i]=longestChain.mAtom[i];
f.mAtomX[i]=Math.cos(0.5235987755982988) * i;
f.mAtomY[i]=((i & 1) == 1) ? 0.0 : 0.5;
f.mPriority[i]=128 + longestChain.getChainLength$();
}
this.mFragmentList.add$O(f);
}
}, p$1);

Clazz.newMeth(C$, 'locateSingleAtoms',  function () {
for (var atom=0; atom < this.mMol.getAllAtoms$(); atom++) {
if (!this.mAtomHandled[atom] && this.mMol.getAllConnAtoms$I(atom) == 0 ) {
var f=Clazz.new_($I$(6,1).c$$com_actelion_research_chem_StereoMolecule$I$Z,[this.mMol, 1, false]);
this.mAtomHandled[atom]=true;
f.mGlobalAtom[0]=atom;
f.mAtomX[0]=0.0;
f.mAtomY[0]=0.0;
f.mPriority[0]=0;
this.mFragmentList.add$O(f);
}}
}, p$1);

Clazz.newMeth(C$, 'addRingFragment$IA$IA',  function (ringAtom, ringBond) {
var ringSize=ringAtom.length;
var f=Clazz.new_($I$(6,1).c$$com_actelion_research_chem_StereoMolecule$I$Z,[this.mMol, ringSize, false]);
f.mAtomX[0]=0.0;
f.mAtomY[0]=0.0;
for (var i=0; i < ringSize; i++) {
f.mPriority[i]=128 - ringSize;
f.mGlobalAtom[i]=ringAtom[i];
}
if (ringSize < 8) p$1.createRegularRingFragment$com_actelion_research_chem_coords_InventorFragment.apply(this, [f]);
 else p$1.createLargeRingFragment$com_actelion_research_chem_coords_InventorFragment$IA$IA.apply(this, [f, ringAtom, ringBond]);
this.mFragmentList.add$O(f);
}, p$1);

Clazz.newMeth(C$, 'createRegularRingFragment$com_actelion_research_chem_coords_InventorFragment',  function (f) {
var angleChange=3.141592653589793 - (3.141592653589793 * (f.size$() - 2)) / f.size$();
for (var i=1; i < f.size$(); i++) {
f.mAtomX[i]=f.mAtomX[i - 1] + Math.sin(angleChange * (i - 1));
f.mAtomY[i]=f.mAtomY[i - 1] + Math.cos(angleChange * (i - 1));
}
}, p$1);

Clazz.newMeth(C$, 'createRegularRingFragment$com_actelion_research_chem_coords_InventorFragment$I$I',  function (f, bondEConstraint, bondZConstraint) {
if (bondEConstraint == 0 || (bondEConstraint & bondZConstraint) != 0 ) {
p$1.createRegularRingFragment$com_actelion_research_chem_coords_InventorFragment.apply(this, [f]);
return;
}var startIndex=-1;
var startPriority=0;
var bitMinus2=1 << (f.size$() - 2);
var bitMinus1=1 << (f.size$() - 1);
var currentBit=1;
var bitPlus1=2;
for (var i=0; i < f.size$(); i++) {
if ((bondZConstraint & (bitMinus1 | currentBit)) == 0 && (bondEConstraint & (bitMinus1 | currentBit)) != 0  && (bondEConstraint & bitMinus2) == 0 ) {
var priority=0;
if ((bondZConstraint & bitMinus2) != 0) priority+=4;
if ((bondEConstraint & bitMinus1) != 0) priority+=2;
if ((bondEConstraint & currentBit) != 0) priority+=1;
if (startPriority < priority) {
startPriority=priority;
startIndex=i;
}}bitMinus2=bitMinus1;
bitMinus1=currentBit;
currentBit=bitPlus1;
bitPlus1=1 << (i + 2 < f.size$() ? i + 2 : i + 2 - f.size$());
}
if (startIndex == -1) {
p$1.createRegularRingFragment$com_actelion_research_chem_coords_InventorFragment.apply(this, [f]);
return;
}var moveToCenter=0;
moveToCenter|=(1 << startIndex);
var offset=2;
while (offset < f.size$() - 1){
var index=(startIndex + offset < f.size$()) ? startIndex + offset : startIndex + offset - f.size$();
bitMinus1=1 << (index == 0 ? f.size$() - 1 : index - 1);
if ((bondZConstraint & bitMinus1) != 0) {
++offset;
continue;
}currentBit=1 << index;
if ((bondEConstraint & bitMinus1) != 0) {
if ((bondZConstraint & currentBit) != 0) {
p$1.createRegularRingFragment$com_actelion_research_chem_coords_InventorFragment.apply(this, [f]);
return;
}moveToCenter|=currentBit;
offset+=2;
continue;
}bitPlus1=1 << (index + 1 < f.size$() ? index + 1 : index + 1 - f.size$());
if ((bondEConstraint & currentBit) != 0 && (bondZConstraint & bitPlus1) != 0 ) {
moveToCenter|=currentBit;
offset+=3;
continue;
}++offset;
}
if (moveToCenter == 0) {
p$1.createRegularRingFragment$com_actelion_research_chem_coords_InventorFragment.apply(this, [f]);
return;
}var angleChange=3.141592653589793 - (3.141592653589793 * (f.size$() - 2)) / f.size$();
for (var i=1; i < f.size$(); i++) {
f.mAtomX[i]=f.mAtomX[i - 1] + Math.sin(angleChange * (i - 1));
f.mAtomY[i]=f.mAtomY[i - 1] + Math.cos(angleChange * (i - 1));
}
currentBit=1;
var shift=2 * Math.sin(angleChange / 2);
for (var i=0; i < f.size$(); i++) {
if ((moveToCenter & currentBit) != 0) {
f.mAtomX[i]+=shift * Math.cos(angleChange * (i - 0.5));
f.mAtomY[i]-=shift * Math.sin(angleChange * (i - 0.5));
}currentBit<<=1;
}
}, p$1);

Clazz.newMeth(C$, 'createLargeRingFragment$com_actelion_research_chem_coords_InventorFragment$IA$IA',  function (f, ringAtom, ringBond) {
var FIRST_RING_SIZE=9;
var LAST_RING_SIZE=25;
var cAngleCorrection=Clazz.array(Double.TYPE, -2, [Clazz.array(Double.TYPE, -1, [20]), null, null, Clazz.array(Double.TYPE, -1, [0, 10]), null, null, Clazz.array(Double.TYPE, -1, [-4, 12]), Clazz.array(Double.TYPE, -1, [0, 0, -7.5]), null, null, null, null, Clazz.array(Double.TYPE, -1, [8, -8]), null, null, null, Clazz.array(Double.TYPE, -1, [-2.4])]);
var cBondZList=Clazz.array(Integer.TYPE, -2, [Clazz.array(Integer.TYPE, -1, [146]), Clazz.array(Integer.TYPE, -1, [627]), null, Clazz.array(Integer.TYPE, -1, [2457, 1170]), null, Clazz.array(Integer.TYPE, -1, [2451, 8643, 2519]), Clazz.array(Integer.TYPE, -1, [9362, 14798]), Clazz.array(Integer.TYPE, -1, [34377, -2147448999, 26214]), null, Clazz.array(Integer.TYPE, -1, [37449, 137313, 95703, 34371, 37815, 54891, 132867, -2147309741, 54857, 55129, -2147449005, -2147449065]), null, Clazz.array(Integer.TYPE, -1, [530697, 531819, 899169, 137289, 694617, -2146951863, -2146952797, -2146939175, -2146929547, -2146929564, -2146625111, -2146931799, -2146940503, -2146931935]), Clazz.array(Integer.TYPE, -1, [1007293, 610915]), Clazz.array(Integer.TYPE, -1, [542985, 137283, 2122017, 530691, 2206773, -2144711351, 219209, 2840841, 137555, -2146871031, -2147264167, 613705, -2145360543, -2146625271, 694611, 2454837, -2145356703, -2147345133, -2146928951, -2146931805, -2144641719, -2146951869, -2146625237, -2146624183, 2841963, 1074905, -2146625117, 2799955, -2144723645, 138583, 859225, -2145264843, -2145216253, -2146624149, -2144700727, -2146928917, -2143905527, -2144045771, -2146789097, 2288547, 544407, 2104323, -2146911977, -2144479405, 3633737, -2146870089, -2146952169]), null, Clazz.array(Integer.TYPE, -1, [8487297, 2172633, 2116611, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8829813]), Clazz.array(Integer.TYPE, -1, [14071213])]);
var maxBit=(1 << f.size$());
var bondEConstraint=0;
var bondZConstraint=0;
if (f.size$() > 7) {
for (var i=0; i < f.size$(); i++) {
var bondParity=p$1.getLargeRingBondParity$IA$IA$I.apply(this, [ringAtom, ringBond, i]);
if (bondParity == 1) bondEConstraint+=maxBit;
 else if (bondParity == 2) bondZConstraint+=maxBit;
bondEConstraint>>>=1;
bondZConstraint>>>=1;
}
}var ringIndex=f.size$() - 9;
if (f.size$() >= 9 && f.size$() <= 25  && cBondZList[ringIndex] != null  ) {
for (var zList=0; zList < cBondZList[ringIndex].length; zList++) {
var isSymmetrical=((-2147483648 & cBondZList[ringIndex][zList]) == 0);
var bondZList=(2147483647 & cBondZList[ringIndex][zList]);
for (var inverted=false; !inverted; inverted=!inverted) {
if (inverted) {
if (isSymmetrical) break;
var newBondZList=0;
for (var bit=1; bit != maxBit; bit<<=1) {
newBondZList<<=1;
if ((bondZList & bit) != 0) newBondZList|=1;
}
bondZList=newBondZList;
}for (var rotation=0; rotation < f.size$(); rotation++) {
if ((bondZList & bondEConstraint) == 0 && (~bondZList & bondZConstraint) == 0 ) {
var bondAngle=0.0;
var correction=0.017453292519943295 * (cAngleCorrection[ringIndex] == null  ? 0 : cAngleCorrection[ringIndex][zList]);
var rightTurns=0;
var tempZList=bondZList;
var isRightTurn=true;
for (var i=0; i < f.size$(); i++) {
if (isRightTurn) ++rightTurns;
if ((tempZList & 1) == 0) isRightTurn=!isRightTurn;
tempZList>>>=1;
}
var wasRightTurn=(rightTurns > (f.size$()/2|0));
for (var i=1; i < f.size$(); i++) {
f.mAtomX[i]=f.mAtomX[i - 1] + Math.sin(bondAngle);
f.mAtomY[i]=f.mAtomY[i - 1] + Math.cos(bondAngle);
if ((bondZList & 1) == 0) wasRightTurn=!wasRightTurn;
bondAngle+=correction + (wasRightTurn ? 1.0471975511965976 : -1.0471975511965976);
bondZList>>>=1;
}
return;
}if ((bondZList & 1) != 0) bondZList|=maxBit;
bondZList>>>=1;
}
}
}
}p$1.createRegularRingFragment$com_actelion_research_chem_coords_InventorFragment$I$I.apply(this, [f, bondEConstraint, bondZConstraint]);
}, p$1);

Clazz.newMeth(C$, 'getLargeRingBondParity$IA$IA$I',  function (ringAtom, ringBond, index) {
var higherIndex=(index == ringAtom.length - 1) ? 0 : index + 1;
var lowerIndex=(index == 0) ? ringAtom.length - 1 : index - 1;
var highestIndex=(higherIndex == ringAtom.length - 1) ? 0 : higherIndex + 1;
if (this.mMol.getBondOrder$I(ringBond[index]) == 2) {
var bondParity=this.mMol.getBondParity$I(ringBond[index]);
if (bondParity == 1 || bondParity == 2 ) {
if (!!(p$1.isLowestIndexNeighbour$I$I$I.apply(this, [ringAtom[lowerIndex], ringAtom[index], ringAtom[higherIndex]]) ^ p$1.isLowestIndexNeighbour$I$I$I.apply(this, [ringAtom[highestIndex], ringAtom[higherIndex], ringAtom[index]]))) bondParity=(bondParity == 1) ? 2 : 1;
return bondParity;
}}if (this.mMol.isSmallRingBond$I(ringBond[index])) {
var sharedRing1=this.mMol.getRingSet$().getSharedRing$I$I(ringBond[lowerIndex], ringBond[index]);
var sharedRing2=this.mMol.getRingSet$().getSharedRing$I$I(ringBond[higherIndex], ringBond[index]);
if (sharedRing1 != -1 || sharedRing2 != -1 ) return sharedRing1 == sharedRing2 ? 2 : 1;
return 2;
}return 0;
}, p$1);

Clazz.newMeth(C$, 'isLowestIndexNeighbour$I$I$I',  function (atom, rootAtom, excludeAtom) {
for (var i=0; i < this.mMol.getConnAtoms$I(rootAtom); i++) {
var connAtom=this.mMol.getConnAtom$I$I(rootAtom, i);
if (connAtom != excludeAtom && connAtom < atom ) return false;
}
return true;
}, p$1);

Clazz.newMeth(C$, 'getSmallestRingFromBond$I',  function (bond) {
var atom1=this.mMol.getBondAtom$I$I(0, bond);
var atom2=this.mMol.getBondAtom$I$I(1, bond);
var graphAtom=Clazz.array(Integer.TYPE, [this.mMol.getAllAtoms$()]);
var graphBond=Clazz.array(Integer.TYPE, [this.mMol.getAllAtoms$()]);
var graphLevel=Clazz.array(Integer.TYPE, [this.mMol.getAllAtoms$()]);
var graphParent=Clazz.array(Integer.TYPE, [this.mMol.getAllAtoms$()]);
graphAtom[0]=atom1;
graphAtom[1]=atom2;
graphBond[1]=bond;
graphLevel[atom1]=1;
graphLevel[atom2]=2;
graphParent[0]=-1;
graphParent[1]=0;
var current=1;
var highest=1;
while (current <= highest){
for (var i=0; i < this.mMol.getConnAtoms$I(graphAtom[current]); i++) {
var candidate=this.mMol.getConnAtom$I$I(graphAtom[current], i);
if ((current > 1) && candidate == atom1 ) {
var theRing=Clazz.new_($I$(7,1).c$$I,[graphLevel[graphAtom[current]]]);
graphBond[0]=this.mMol.getConnBond$I$I(graphAtom[current], i);
var index=current;
for (var j=0; j < theRing.getChainLength$(); j++) {
theRing.mAtom[j]=graphAtom[index];
theRing.mBond[j]=graphBond[index];
index=graphParent[index];
}
return theRing;
}if (graphLevel[candidate] == 0 && this.mMol.isRingAtom$I(candidate) ) {
graphAtom[++highest]=candidate;
graphBond[highest]=this.mMol.getConnBond$I$I(graphAtom[current], i);
graphLevel[candidate]=graphLevel[graphAtom[current]] + 1;
graphParent[highest]=current;
}}
++current;
}
return null;
}, p$1);

Clazz.newMeth(C$, 'getSmallestRingSize$I$I$I',  function (atom1, atom2, atom3) {
var graphAtom=Clazz.array(Integer.TYPE, [this.mMol.getAllAtoms$()]);
var graphLevel=Clazz.array(Integer.TYPE, [this.mMol.getAllAtoms$()]);
graphAtom[0]=atom2;
graphAtom[1]=atom1;
graphLevel[atom2]=1;
graphLevel[atom1]=2;
var current=1;
var highest=1;
while (current <= highest){
for (var i=0; i < this.mMol.getConnAtoms$I(graphAtom[current]); i++) {
var candidate=this.mMol.getConnAtom$I$I(graphAtom[current], i);
if (candidate == atom3) return 1 + graphLevel[graphAtom[current]];
if (graphLevel[candidate] == 0 && this.mMol.isRingAtom$I(candidate) ) {
graphAtom[++highest]=candidate;
graphLevel[candidate]=graphLevel[graphAtom[current]] + 1;
}}
++current;
}
return 0;
}, p$1);

Clazz.newMeth(C$, 'joinOverlappingFragments',  function () {
while (true){
var maxJoinPriority=0;
var maxCommonAtoms=0;
var maxFragment1=null;
var maxFragment2=null;
for (var i=1; i < this.mFragmentList.size$(); i++) {
var f1=this.mFragmentList.get$I(i);
for (var j=0; j < i; j++) {
var f2=this.mFragmentList.get$I(j);
var commonAtom=0;
var commonAtoms=0;
var maxF1Priority=0;
var maxF2Priority=0;
for (var k=0; k < f1.size$(); k++) {
for (var l=0; l < f2.size$(); l++) {
if (f1.mGlobalAtom[k] == f2.mGlobalAtom[l]) {
++commonAtoms;
commonAtom=f1.mGlobalAtom[k];
if (maxF1Priority < f1.mPriority[k]) maxF1Priority=f1.mPriority[k];
if (maxF2Priority < f2.mPriority[l]) maxF2Priority=f2.mPriority[l];
}}
}
if (commonAtoms > 0) {
var handlePreferred=(commonAtoms == 1 && p$1.getConnAtoms$com_actelion_research_chem_coords_InventorFragment$I.apply(this, [f1, commonAtom]) == 1  && p$1.getConnAtoms$com_actelion_research_chem_coords_InventorFragment$I.apply(this, [f2, commonAtom]) == 1 ) ? 0 : 1;
var joinPriority;
if (maxF1Priority > maxF2Priority) joinPriority=(handlePreferred << 24) + (maxF1Priority << 16) + (maxF2Priority << 8) + commonAtoms ;
 else joinPriority=(handlePreferred << 24) + (maxF2Priority << 16) + (maxF1Priority << 8) + commonAtoms ;
if (maxJoinPriority < joinPriority) {
maxJoinPriority=joinPriority;
maxCommonAtoms=commonAtoms;
maxF1Priority=0;
maxF2Priority=0;
for (var k=0; k < f1.size$(); k++) if (maxF1Priority < f1.mPriority[k]) maxF1Priority=f1.mPriority[k];

for (var k=0; k < f2.size$(); k++) if (maxF2Priority < f2.mPriority[k]) maxF2Priority=f2.mPriority[k];

if (maxF1Priority > maxF2Priority) {
maxFragment1=f1;
maxFragment2=f2;
} else {
maxFragment1=f2;
maxFragment2=f1;
}}}}
}
if (maxJoinPriority == 0) break;
if (maxCommonAtoms == maxFragment1.size$()) this.mFragmentList.remove$O(maxFragment1);
 else if (maxCommonAtoms == maxFragment2.size$()) this.mFragmentList.remove$O(maxFragment2);
 else p$1.joinFragments$com_actelion_research_chem_coords_InventorFragment$com_actelion_research_chem_coords_InventorFragment$I.apply(this, [maxFragment1, maxFragment2, maxCommonAtoms]);
}
}, p$1);

Clazz.newMeth(C$, 'joinFragments$com_actelion_research_chem_coords_InventorFragment$com_actelion_research_chem_coords_InventorFragment$I',  function (f1, f2, commonAtoms) {
var commonAtom=Clazz.array(Integer.TYPE, [commonAtoms]);
var count=0;
for (var i=0; i < f1.mGlobalAtom.length; i++) for (var j=0; j < f2.mGlobalAtom.length; j++) if (f1.mGlobalAtom[i] == f2.mGlobalAtom[j]) commonAtom[count++]=f1.mGlobalAtom[i];


var joinedFragment=(commonAtoms == 1) ? p$1.getFusedFragment$com_actelion_research_chem_coords_InventorFragment$com_actelion_research_chem_coords_InventorFragment$I.apply(this, [f1, f2, commonAtom[0]]) : p$1.getFusedFragment$com_actelion_research_chem_coords_InventorFragment$com_actelion_research_chem_coords_InventorFragment$IA$I.apply(this, [f1, f2, commonAtom, commonAtoms]);
p$1.updateFragmentList$com_actelion_research_chem_coords_InventorFragment$com_actelion_research_chem_coords_InventorFragment$com_actelion_research_chem_coords_InventorFragment.apply(this, [f1, f2, joinedFragment]);
}, p$1);

Clazz.newMeth(C$, 'getFusedFragment$com_actelion_research_chem_coords_InventorFragment$com_actelion_research_chem_coords_InventorFragment$I',  function (f1, f2, commonAtom) {
var index1=f1.getLocalAtom$I(commonAtom);
var index2=f2.getLocalAtom$I(commonAtom);
f2.translate$D$D(f1.mAtomX[index1] - f2.mAtomX[index2], f1.mAtomY[index1] - f2.mAtomY[index2]);
var angle1=p$1.suggestNewBondAngle$com_actelion_research_chem_coords_InventorFragment$I.apply(this, [f1, commonAtom]);
var angle2=p$1.suggestNewBondAngle$com_actelion_research_chem_coords_InventorFragment$I.apply(this, [f2, commonAtom]);
var angleInc=0.0;
if (p$1.getConnAtoms$com_actelion_research_chem_coords_InventorFragment$I.apply(this, [f1, commonAtom]) == 1 && p$1.getConnAtoms$com_actelion_research_chem_coords_InventorFragment$I.apply(this, [f2, commonAtom]) == 1 ) angleInc=1.0471975511965976;
f2.rotate$D$D$D(f2.mAtomX[index2], f2.mAtomY[index2], angle1 - angle2 + angleInc + 3.141592653589793);
return p$1.getMergedFragment$com_actelion_research_chem_coords_InventorFragment$com_actelion_research_chem_coords_InventorFragment$I.apply(this, [f1, f2, 1]);
}, p$1);

Clazz.newMeth(C$, 'getFusedFragment$com_actelion_research_chem_coords_InventorFragment$com_actelion_research_chem_coords_InventorFragment$IA$I',  function (f1, f2, commonAtom, commonAtoms) {
var index1=Clazz.array(Integer.TYPE, [commonAtoms]);
var index2=Clazz.array(Integer.TYPE, [commonAtoms]);
for (var i=0; i < commonAtoms; i++) {
index1[i]=f1.getLocalAtom$I(commonAtom[i]);
index2[i]=f2.getLocalAtom$I(commonAtom[i]);
}
var meanX1=0.0;
var meanY1=0.0;
var meanX2=0.0;
var meanY2=0.0;
for (var i=0; i < commonAtoms; i++) {
meanX1+=f1.mAtomX[index1[i]];
meanY1+=f1.mAtomY[index1[i]];
meanX2+=f2.mAtomX[index2[i]];
meanY2+=f2.mAtomY[index2[i]];
}
meanX1/=commonAtoms;
meanY1/=commonAtoms;
meanX2/=commonAtoms;
meanY2/=commonAtoms;
f2.translate$D$D(meanX1 - meanX2, meanY1 - meanY2);
var f1Angle=Clazz.array($I$(8), [commonAtoms]);
var f2Angle=Clazz.array($I$(8), [commonAtoms]);
var angleDif=Clazz.array($I$(8), [commonAtoms]);
var angleDifFlip=Clazz.array($I$(8), [commonAtoms]);
for (var i=0; i < commonAtoms; i++) {
f1Angle[i]=Clazz.new_($I$(8,1).c$$D$D$D$D,[meanX1, meanY1, f1.mAtomX[index1[i]], f1.mAtomY[index1[i]]]);
f2Angle[i]=Clazz.new_($I$(8,1).c$$D$D$D$D,[meanX1, meanY1, f2.mAtomX[index2[i]], f2.mAtomY[index2[i]]]);
angleDif[i]=Clazz.new_($I$(8,1).c$$D$D,[f1Angle[i].mAngle - f2Angle[i].mAngle, f1Angle[i].mLength * f2Angle[i].mLength]);
angleDifFlip[i]=Clazz.new_($I$(8,1).c$$D$D,[f1Angle[i].mAngle + f2Angle[i].mAngle, f1Angle[i].mLength * f2Angle[i].mLength]);
}
var meanAngleDif=C$.getMeanAngle$com_actelion_research_chem_coords_InventorAngleA$I(angleDif, commonAtoms);
var meanAngleDifFlip=C$.getMeanAngle$com_actelion_research_chem_coords_InventorAngleA$I(angleDifFlip, commonAtoms);
var neighbourCountF1=0;
var neighbourCountF2=0;
for (var i=0; i < commonAtoms; i++) {
for (var j=0; j < this.mMol.getAllConnAtoms$I(commonAtom[i]); j++) {
var connAtom=this.mMol.getConnAtom$I$I(commonAtom[i], j);
if (f1.isMember$I(connAtom) && !f2.isMember$I(connAtom) ) ++neighbourCountF1;
if (!f1.isMember$I(connAtom) && f2.isMember$I(connAtom) ) ++neighbourCountF2;
}
}
var f1NeighbourAngle=Clazz.array($I$(8), [neighbourCountF1]);
var f2NeighbourAngle=Clazz.array($I$(8), [neighbourCountF2]);
var f2NeighbourAngleFlip=Clazz.array($I$(8), [neighbourCountF2]);
neighbourCountF1=0;
neighbourCountF2=0;
for (var i=0; i < commonAtoms; i++) {
for (var j=0; j < this.mMol.getAllConnAtoms$I(commonAtom[i]); j++) {
var connAtom=this.mMol.getConnAtom$I$I(commonAtom[i], j);
if (f1.isMember$I(connAtom) && !f2.isMember$I(connAtom) ) {
var connIndex=f1.getLocalAtom$I(connAtom);
f1NeighbourAngle[neighbourCountF1]=Clazz.new_($I$(8,1).c$$D$D$D$D,[f1.mAtomX[index1[i]], f1.mAtomY[index1[i]], f1.mAtomX[connIndex], f1.mAtomY[connIndex]]);
++neighbourCountF1;
}if (!f1.isMember$I(connAtom) && f2.isMember$I(connAtom) ) {
var connIndex=f2.getLocalAtom$I(connAtom);
var neighbourAngle=Clazz.new_($I$(8,1).c$$D$D$D$D,[f2.mAtomX[index2[i]], f2.mAtomY[index2[i]], f2.mAtomX[connIndex], f2.mAtomY[connIndex]]);
f2NeighbourAngle[neighbourCountF2]=Clazz.new_($I$(8,1).c$$D$D,[meanAngleDif.mAngle + neighbourAngle.mAngle, neighbourAngle.mLength]);
f2NeighbourAngleFlip[neighbourCountF2]=Clazz.new_($I$(8,1).c$$D$D,[meanAngleDifFlip.mAngle - neighbourAngle.mAngle, neighbourAngle.mLength]);
++neighbourCountF2;
}}
}
var meanNeighbourAngleF1=C$.getMeanAngle$com_actelion_research_chem_coords_InventorAngleA$I(f1NeighbourAngle, neighbourCountF1);
var meanNeighbourAngleF2=C$.getMeanAngle$com_actelion_research_chem_coords_InventorAngleA$I(f2NeighbourAngle, neighbourCountF2);
var meanNeighbourAngleF2Flip=C$.getMeanAngle$com_actelion_research_chem_coords_InventorAngleA$I(f2NeighbourAngleFlip, neighbourCountF2);
if (Math.abs(p$1.getAngleDif$D$D.apply(this, [meanNeighbourAngleF1.mAngle, meanNeighbourAngleF2.mAngle])) > Math.abs(p$1.getAngleDif$D$D.apply(this, [meanNeighbourAngleF1.mAngle, meanNeighbourAngleF2Flip.mAngle])) ) {
f2.rotate$D$D$D(meanX1, meanY1, meanAngleDif.mAngle);
} else {
f2.flip$D$D$D(meanX1, meanY1, 0.0);
f2.rotate$D$D$D(meanX1, meanY1, meanAngleDifFlip.mAngle);
}return p$1.getMergedFragment$com_actelion_research_chem_coords_InventorFragment$com_actelion_research_chem_coords_InventorFragment$I.apply(this, [f1, f2, commonAtoms]);
}, p$1);

Clazz.newMeth(C$, 'getMergedFragment$com_actelion_research_chem_coords_InventorFragment$com_actelion_research_chem_coords_InventorFragment$I',  function (f1, f2, commonAtoms) {
var f=Clazz.new_([this.mMol, f1.mGlobalAtom.length + f2.mGlobalAtom.length - commonAtoms, !!(f1.mKeepMarkedAtoms | f2.mKeepMarkedAtoms)],$I$(6,1).c$$com_actelion_research_chem_StereoMolecule$I$Z);
var count=0;
for (var i=0; i < f1.mGlobalAtom.length; i++) {
f.mGlobalAtom[count]=f1.mGlobalAtom[i];
f.mPriority[count]=f1.mPriority[i];
f.mAtomX[count]=f1.mAtomX[i];
f.mAtomY[count++]=f1.mAtomY[i];
}
for (var i=0; i < f2.mGlobalAtom.length; i++) {
var index=f1.getLocalAtom$I(f2.mGlobalAtom[i]);
if (index == -1) {
f.mGlobalAtom[count]=f2.mGlobalAtom[i];
f.mPriority[count]=f2.mPriority[i];
f.mAtomX[count]=f2.mAtomX[i];
f.mAtomY[count++]=f2.mAtomY[i];
} else {
if (f.mPriority[index] < f2.mPriority[i]) {
f.mPriority[index]=f2.mPriority[i];
f.mAtomX[index]=f2.mAtomX[i];
f.mAtomY[index]=f2.mAtomY[i];
}}}
return f;
}, p$1);

Clazz.newMeth(C$, 'getLongestUnhandledChain$I',  function (atom) {
var graphAtom=Clazz.array(Integer.TYPE, [this.mMol.getAllAtoms$()]);
var graphBond=Clazz.array(Integer.TYPE, [this.mMol.getAllAtoms$()]);
var graphLevel=Clazz.array(Integer.TYPE, [this.mMol.getAllAtoms$()]);
var graphParent=Clazz.array(Integer.TYPE, [this.mMol.getAllAtoms$()]);
graphAtom[0]=atom;
graphLevel[atom]=1;
graphParent[0]=-1;
var current=0;
var highest=0;
while (current <= highest){
if (current == 0 || !this.mAtomHandled[graphAtom[current]] ) {
for (var i=0; i < this.mMol.getAllConnAtoms$I(graphAtom[current]); i++) {
var candidate=this.mMol.getConnAtom$I$I(graphAtom[current], i);
var theBond=this.mMol.getConnBond$I$I(graphAtom[current], i);
if (graphLevel[candidate] == 0 && !this.mBondHandled[theBond] ) {
graphAtom[++highest]=candidate;
graphBond[highest]=theBond;
graphLevel[candidate]=graphLevel[graphAtom[current]] + 1;
graphParent[highest]=current;
}}
}if (current == highest) {
var theChain=Clazz.new_($I$(7,1).c$$I,[graphLevel[graphAtom[current]]]);
var index=current;
for (var j=0; j < theChain.getChainLength$(); j++) {
theChain.mAtom[j]=graphAtom[index];
theChain.mBond[j]=graphBond[index];
index=graphParent[index];
}
return theChain;
}++current;
}
return null;
}, p$1);

Clazz.newMeth(C$, 'suggestNewBondAngle$com_actelion_research_chem_coords_InventorFragment$I',  function (f, atom) {
var connAngle=Clazz.array(Double.TYPE, [this.mMol.getAllConnAtoms$I(atom) + 1]);
var connAtom=Clazz.array(Integer.TYPE, [this.mMol.getAllConnAtoms$I(atom) + 1]);
var connBond=Clazz.array(Integer.TYPE, [this.mMol.getAllConnAtoms$I(atom) + 1]);
var rootIndex=f.getLocalAtom$I(atom);
var connAngles=0;
for (var i=0; i < this.mMol.getAllConnAtoms$I(atom); i++) {
connAtom[connAngles]=this.mMol.getConnAtom$I$I(atom, i);
connBond[connAngles]=this.mMol.getConnBond$I$I(atom, i);
var index=f.getLocalAtom$I(connAtom[connAngles]);
if (index != -1) connAngle[connAngles++]=$I$(8).getAngle$D$D$D$D(f.mAtomX[rootIndex], f.mAtomY[rootIndex], f.mAtomX[index], f.mAtomY[index]);
}
if (connAngles == 1) return connAngle[0] + 3.141592653589793;
for (var i=connAngles - 1; i > 0; i--) {
for (var j=0; j < i; j++) {
if (connAngle[j] > connAngle[j + 1] ) {
var tempAngle=connAngle[j];
connAngle[j]=connAngle[j + 1];
connAngle[j + 1]=tempAngle;
var tempAtom=connAtom[j];
connAtom[j]=connAtom[j + 1];
connAtom[j + 1]=tempAtom;
var tempBond=connBond[j];
connBond[j]=connBond[j + 1];
connBond[j + 1]=tempBond;
}}
}
connAngle[connAngles]=connAngle[0] + 6.283185307179586;
connAtom[connAngles]=connAtom[0];
connBond[connAngles]=connBond[0];
var maxAngleDif=-100.0;
var maxIndex=0;
for (var i=0; i < connAngles; i++) {
var angleDif=connAngle[i + 1] - connAngle[i];
if (connAngles > 2 && this.mMol.isRingBond$I(connBond[i])  && this.mMol.isRingBond$I(connBond[i + 1]) ) {
var ringSize=p$1.getSmallestRingSize$I$I$I.apply(this, [connAtom[i], atom, connAtom[i + 1]]);
if (ringSize != 0) angleDif-=100.0 - ringSize;
}if (maxAngleDif < angleDif ) {
maxAngleDif=angleDif;
maxIndex=i;
}}
return (connAngle[maxIndex] + connAngle[maxIndex + 1]) / 2;
}, p$1);

Clazz.newMeth(C$, 'getAngleDif$D$D',  function (angle1, angle2) {
var angleDif=angle1 - angle2;
while (angleDif < -3.141592653589793 )angleDif+=6.283185307179586;

while (angleDif > 3.141592653589793 )angleDif-=6.283185307179586;

return angleDif;
}, p$1);

Clazz.newMeth(C$, 'getMeanAngle$com_actelion_research_chem_coords_InventorAngleA$I',  function (angle, noOfAngles) {
var sinSum=0;
var cosSum=0;
for (var i=0; i < noOfAngles; i++) {
sinSum+=angle[i].mLength * Math.sin(angle[i].mAngle);
cosSum+=angle[i].mLength * Math.cos(angle[i].mAngle);
}
var meanAngle;
if (cosSum == 0 ) meanAngle=(sinSum > 0 ) ? 1.5707963267948966 : -1.5707963267948966;
 else {
meanAngle=Math.atan(sinSum / cosSum);
if (cosSum < 0 ) meanAngle+=3.141592653589793;
}var length=Math.sqrt(sinSum * sinSum + cosSum * cosSum) / noOfAngles;
return Clazz.new_($I$(8,1).c$$D$D,[meanAngle, length]);
}, 1);

Clazz.newMeth(C$, 'getConnAtoms$com_actelion_research_chem_coords_InventorFragment$I',  function (f, atom) {
var connAtoms=0;
for (var i=0; i < this.mMol.getAllConnAtoms$I(atom); i++) {
if (f.isMember$I(this.mMol.getConnAtom$I$I(atom, i))) ++connAtoms;
}
return connAtoms;
}, p$1);

Clazz.newMeth(C$, 'correctChainEZParities',  function () {
for (var fragmentNo=0; fragmentNo < this.mFragmentList.size$(); fragmentNo++) {
var f=this.mFragmentList.get$I(fragmentNo);
for (var i=0; i < f.mGlobalBond.length; i++) {
var bond=f.mGlobalBond[i];
if (this.mMol.getBondOrder$I(bond) == 2) {
if (!this.mMol.isSmallRingBond$I(bond) && (this.mMol.getBondParity$I(bond) == 3 || this.mMol.getBondParity$I(bond) == 0 ) ) this.mMol.setBondParityUnknownOrNone$I(bond);
if (!this.mMol.isRingBond$I(bond) && (this.mMol.getConnAtoms$I(this.mMol.getBondAtom$I$I(0, bond)) > 1) && (this.mMol.getConnAtoms$I(this.mMol.getBondAtom$I$I(1, bond)) > 1) && (this.mMol.getBondParity$I(bond) == 1 || this.mMol.getBondParity$I(bond) == 2 )  ) {
var minConnAtom=Clazz.array(Integer.TYPE, [2]);
var bondAtom=Clazz.array(Integer.TYPE, [2]);
for (var j=0; j < 2; j++) {
minConnAtom[j]=this.mMol.getMaxAtoms$();
bondAtom[j]=this.mMol.getBondAtom$I$I(j, bond);
for (var k=0; k < this.mMol.getAllConnAtoms$I(bondAtom[j]); k++) {
var connAtom=this.mMol.getConnAtom$I$I(bondAtom[j], k);
if (connAtom != this.mMol.getBondAtom$I$I(1 - j, bond) && minConnAtom[j] > connAtom ) minConnAtom[j]=connAtom;
}
}
var dbAngle=$I$(8).getAngle$D$D$D$D(f.mAtomX[f.mGlobalToLocalAtom[bondAtom[0]]], f.mAtomY[f.mGlobalToLocalAtom[bondAtom[0]]], f.mAtomX[f.mGlobalToLocalAtom[bondAtom[1]]], f.mAtomY[f.mGlobalToLocalAtom[bondAtom[1]]]);
var angle1=$I$(8).getAngle$D$D$D$D(f.mAtomX[f.mGlobalToLocalAtom[minConnAtom[0]]], f.mAtomY[f.mGlobalToLocalAtom[minConnAtom[0]]], f.mAtomX[f.mGlobalToLocalAtom[bondAtom[0]]], f.mAtomY[f.mGlobalToLocalAtom[bondAtom[0]]]);
var angle2=$I$(8).getAngle$D$D$D$D(f.mAtomX[f.mGlobalToLocalAtom[bondAtom[1]]], f.mAtomY[f.mGlobalToLocalAtom[bondAtom[1]]], f.mAtomX[f.mGlobalToLocalAtom[minConnAtom[1]]], f.mAtomY[f.mGlobalToLocalAtom[minConnAtom[1]]]);
if (!!((!!((p$1.getAngleDif$D$D.apply(this, [dbAngle, angle1]) < 0 ) ^ (p$1.getAngleDif$D$D.apply(this, [dbAngle, angle2]) < 0 ))) ^ (this.mMol.getBondParity$I(bond) == 2))) {
f.flipOneSide$I(bond);
}}}}
}
}, p$1);

Clazz.newMeth(C$, 'optimizeFragments',  function () {
var atomSymRank=p$1.calculateAtomSymmetries.apply(this, []);
var bondFlipPriority=Clazz.array(Byte.TYPE, [this.mMol.getAllBonds$()]);
p$1.locateFlipBonds$BA$IA.apply(this, [bondFlipPriority, atomSymRank]);
for (var bond=0; bond < this.mMol.getAllBonds$(); bond++) if (bondFlipPriority[bond] == 2 && (this.mMol.isRingAtom$I(this.mMol.getBondAtom$I$I(0, bond)) || this.mMol.isRingAtom$I(this.mMol.getBondAtom$I$I(1, bond)) ) ) bondFlipPriority[bond]=3;

for (var fragmentNo=0; fragmentNo < this.mFragmentList.size$(); fragmentNo++) {
var f=this.mFragmentList.get$I(fragmentNo);
var collisionList=f.getCollisionList$();
var minCollisionPanalty=f.getCollisionPanalty$();
var minCollisionFragment=Clazz.new_($I$(6,1).c$$com_actelion_research_chem_coords_InventorFragment,[f]);
var lastBond=-1;
for (var flip=0; flip < 224 && collisionList.size$() != 0 ; flip++) {
var collisionNo=this.mRandom.nextInt$I(collisionList.size$());
var collidingAtom=collisionList.get$I(collisionNo);
var bondSequence=p$1.getShortestConnection$I$I.apply(this, [collidingAtom[0], collidingAtom[1]]);
var availableBond=Clazz.array(Integer.TYPE, [bondSequence.length]);
var availableBonds=0;
if (flip < 32) {
for (var i=1; i < bondSequence.length - 1; i++) if (bondFlipPriority[bondSequence[i]] == 3) availableBond[availableBonds++]=bondSequence[i];

} else if (flip < 96) {
for (var i=1; i < bondSequence.length - 1; i++) if (bondFlipPriority[bondSequence[i]] >= 2) availableBond[availableBonds++]=bondSequence[i];

} else {
for (var i=1; i < bondSequence.length - 1; i++) if (bondFlipPriority[bondSequence[i]] >= 1) availableBond[availableBonds++]=bondSequence[i];

}if (availableBonds != 0) {
var theBond=availableBond[0];
if (availableBonds > 1) {
do {
theBond=availableBond[this.mRandom.nextInt$I(availableBonds)];
} while (theBond == lastBond);
}if (theBond != lastBond) {
lastBond=theBond;
f.flipOneSide$I(theBond);
collisionList=f.getCollisionList$();
if (minCollisionPanalty > f.getCollisionPanalty$() ) {
minCollisionPanalty=f.getCollisionPanalty$();
minCollisionFragment=Clazz.new_($I$(6,1).c$$com_actelion_research_chem_coords_InventorFragment,[f]);
}}}}
this.mFragmentList.set$I$O(fragmentNo, minCollisionFragment);
f=minCollisionFragment;
var currentRank=1;
var nextAvailableRank;
do {
nextAvailableRank=9999;
for (var i=0; i < f.size$(); i++) {
var theRank=atomSymRank[f.mGlobalAtom[i]];
if (theRank == currentRank) f.optimizeAtomCoordinates$I(i);
 else if (theRank > currentRank && theRank < nextAvailableRank ) nextAvailableRank=theRank;
}
currentRank=nextAvailableRank;
} while (nextAvailableRank != 9999);
}
}, p$1);

Clazz.newMeth(C$, 'getShortestConnection$I$I',  function (atom1, atom2) {
var graphAtom=Clazz.array(Integer.TYPE, [this.mMol.getAllAtoms$()]);
var graphBond=Clazz.array(Integer.TYPE, [this.mMol.getAllAtoms$()]);
var graphLevel=Clazz.array(Integer.TYPE, [this.mMol.getAllAtoms$()]);
var graphParent=Clazz.array(Integer.TYPE, [this.mMol.getAllAtoms$()]);
graphAtom[0]=atom2;
graphLevel[atom2]=1;
graphParent[0]=-1;
var current=0;
var highest=0;
while (current <= highest){
for (var i=0; i < this.mMol.getAllConnAtomsPlusMetalBonds$I(graphAtom[current]); i++) {
var candidate=this.mMol.getConnAtom$I$I(graphAtom[current], i);
var theBond=this.mMol.getConnBond$I$I(graphAtom[current], i);
if (candidate == atom1) {
var chainLength=graphLevel[graphAtom[current]];
var bondSequence=Clazz.array(Integer.TYPE, [chainLength]);
bondSequence[0]=theBond;
for (var j=1; j < chainLength; j++) {
bondSequence[j]=graphBond[current];
current=graphParent[current];
}
return bondSequence;
}if (graphLevel[candidate] == 0) {
graphAtom[++highest]=candidate;
graphBond[highest]=theBond;
graphLevel[candidate]=graphLevel[graphAtom[current]] + 1;
graphParent[highest]=current;
}}
if (current == highest) return null;
++current;
}
return null;
}, p$1);

Clazz.newMeth(C$, 'locateFlipBonds$BA$IA',  function (bondFlipPriority, atomSymRank) {
for (var bond=0; bond < this.mMol.getAllBonds$(); bond++) {
var atom1=this.mMol.getBondAtom$I$I(0, bond);
var atom2=this.mMol.getBondAtom$I$I(1, bond);
if (this.mMol.isRingBond$I(bond) || this.mMol.getBondOrder$I(bond) != 1  || this.mMol.getAllConnAtoms$I(atom1) == 1  || this.mMol.getAllConnAtoms$I(atom2) == 1 ) continue;
if ((this.mMode & 4) != 0 && this.mMol.isMarkedAtom$I(atom1)  && this.mMol.isMarkedAtom$I(atom2) ) continue;
var oneBondEndIsSymmetric=false;
for (var i=0; i < 2; i++) {
var bondAtom=this.mMol.getBondAtom$I$I(i, bond);
if (this.mMol.getAllConnAtoms$I(bondAtom) > 2) {
var symmetricEndFound=true;
var connSymRank=-1;
for (var j=0; j < this.mMol.getAllConnAtoms$I(bondAtom); j++) {
var connAtom=this.mMol.getConnAtom$I$I(bondAtom, j);
if (connAtom != this.mMol.getBondAtom$I$I(1 - i, bond)) {
if (connSymRank == -1) connSymRank=atomSymRank[connAtom];
 else if (connSymRank != atomSymRank[connAtom]) symmetricEndFound=false;
}}
if (symmetricEndFound) {
oneBondEndIsSymmetric=true;
break;
}}}
if (!oneBondEndIsSymmetric) {
if ((this.mMode & 8) != 0 && this.mMol.isMarkedAtom$I(atom1)  && this.mMol.isMarkedAtom$I(atom2) ) bondFlipPriority[bond]=1;
 else bondFlipPriority[bond]=2;
}}
}, p$1);

Clazz.newMeth(C$, 'calculateAtomSymmetries',  function () {
var atomBits=$I$(9,"getNeededBits$I",[this.mMol.getAtoms$()]);
var maxConnAtoms=2;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) maxConnAtoms=Math.max(maxConnAtoms, this.mMol.getAllConnAtoms$I(atom));

var baseValueSize=((62 + 2 * atomBits + maxConnAtoms * (atomBits + 1))/63|0);
var baseValue=Clazz.array($I$(10), [this.mMol.getAllAtoms$()]);
for (var atom=0; atom < this.mMol.getAllAtoms$(); atom++) {
baseValue[atom]=Clazz.new_($I$(10,1).c$$I,[baseValueSize]);
baseValue[atom].init$I(atom);
}
var symRank=Clazz.array(Integer.TYPE, [this.mMol.getAllAtoms$()]);
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
var bondParity=this.mMol.getBondParity$I(bond);
if (bondParity == 1 || bondParity == 2 ) {
baseValue[this.mMol.getBondAtom$I$I(0, bond)].add$J(bondParity);
baseValue[this.mMol.getBondAtom$I$I(1, bond)].add$J(bondParity);
}}
var oldNoOfRanks;
var newNoOfRanks=p$1.consolidateRanks$com_actelion_research_chem_CanonizerBaseValueA$IA.apply(this, [baseValue, symRank]);
do {
oldNoOfRanks=newNoOfRanks;
p$1.calcNextBaseValues$com_actelion_research_chem_CanonizerBaseValueA$IA$I$I.apply(this, [baseValue, symRank, atomBits, maxConnAtoms]);
newNoOfRanks=p$1.consolidateRanks$com_actelion_research_chem_CanonizerBaseValueA$IA.apply(this, [baseValue, symRank]);
} while (oldNoOfRanks != newNoOfRanks);
return symRank;
}, p$1);

Clazz.newMeth(C$, 'calcNextBaseValues$com_actelion_research_chem_CanonizerBaseValueA$IA$I$I',  function (baseValue, symRank, atomBits, maxConnAtoms) {
var connRank=Clazz.array(Integer.TYPE, [maxConnAtoms]);
for (var atom=0; atom < this.mMol.getAllAtoms$(); atom++) {
for (var i=0; i < this.mMol.getAllConnAtoms$I(atom); i++) {
var rank=symRank[this.mMol.getConnAtom$I$I(atom, i)];
var j;
for (j=0; j < i; j++) if (rank < connRank[j]) break;

for (var k=i; k > j; k--) connRank[k]=connRank[k - 1];

connRank[j]=rank;
}
var neighbours=this.mMol.getAllConnAtoms$I(atom);
baseValue[atom].init$I(atom);
baseValue[atom].add$I$J(atomBits, symRank[atom]);
baseValue[atom].add$I$J((maxConnAtoms - neighbours) * (atomBits + 1), 0);
for (var i=0; i < neighbours; i++) baseValue[atom].add$I$J(atomBits + 1, connRank[i]);

}
}, p$1);

Clazz.newMeth(C$, 'consolidateRanks$com_actelion_research_chem_CanonizerBaseValueA$IA',  function (baseValue, symRank) {
var rank=0;
$I$(11).sort$OA(baseValue);
for (var i=0; i < baseValue.length; i++) {
if (i == 0 || baseValue[i].compareTo$com_actelion_research_chem_CanonizerBaseValue(baseValue[i - 1]) != 0 ) ++rank;
symRank[baseValue[i].getAtom$()]=rank;
}
return rank;
}, p$1);

Clazz.newMeth(C$, 'joinMetalBondedFragments',  function () {
var associationList=p$1.createMetalBondAssociations.apply(this, []);
while (associationList != null ){
var association=p$1.getMaxPriorityAssociation$java_util_ArrayList.apply(this, [associationList]);
p$1.joinAssociatedFragments$com_actelion_research_chem_coords_FragmentAssociation$D.apply(this, [association, 1.2]);
associationList=p$1.createMetalBondAssociations.apply(this, []);
}
}, p$1);

Clazz.newMeth(C$, 'joinChargedFragments',  function () {
var association=p$1.createChargeAssociation.apply(this, []);
while (association != null ){
p$1.joinAssociatedFragments$com_actelion_research_chem_coords_FragmentAssociation$D.apply(this, [association, 1.5]);
association=p$1.createChargeAssociation.apply(this, []);
}
}, p$1);

Clazz.newMeth(C$, 'joinRemainingFragments',  function () {
var association=p$1.createDisconnectedAssociation.apply(this, []);
while (association != null ){
p$1.joinAssociatedFragments$com_actelion_research_chem_coords_FragmentAssociation$D.apply(this, [association, 1.8]);
association=p$1.createDisconnectedAssociation.apply(this, []);
}
}, p$1);

Clazz.newMeth(C$, 'joinAssociatedFragments$com_actelion_research_chem_coords_FragmentAssociation$D',  function (association, minDistance) {
association.arrange$D$Z(minDistance, (this.mMode & 12) != 0);
var mergedFragment=p$1.getMergedFragment$com_actelion_research_chem_coords_InventorFragment$com_actelion_research_chem_coords_InventorFragment$I.apply(this, [association.getFragment$I(0), association.getFragment$I(1), 0]);
p$1.updateFragmentList$com_actelion_research_chem_coords_InventorFragment$com_actelion_research_chem_coords_InventorFragment$com_actelion_research_chem_coords_InventorFragment.apply(this, [association.getFragment$I(0), association.getFragment$I(1), mergedFragment]);
}, p$1);

Clazz.newMeth(C$, 'getMaxPriorityAssociation$java_util_ArrayList',  function (associationList) {
var maxPriority=0;
var maxFA=null;
for (var association, $association = associationList.iterator$(); $association.hasNext$()&&((association=($association.next$())),1);) {
if (maxPriority < association.getPriority$()) {
maxPriority=association.getPriority$();
maxFA=association;
}}
return maxFA;
}, p$1);

Clazz.newMeth(C$, 'createMetalBondAssociations',  function () {
var associationList=null;
var fa=null;
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mMol.getBondType$I(bond) == 32) {
var atom1=this.mMol.getBondAtom$I$I(0, bond);
var atomIndex1=-1;
var f1=0;
for (; f1 < this.mFragmentList.size$(); f1++) {
atomIndex1=this.mFragmentList.get$I(f1).getLocalAtom$I(atom1);
if (atomIndex1 != -1) break;
}
var atom2=this.mMol.getBondAtom$I$I(1, bond);
var atomIndex2=-1;
var f2=0;
for (; f2 < this.mFragmentList.size$(); f2++) {
atomIndex2=this.mFragmentList.get$I(f2).getLocalAtom$I(atom2);
if (atomIndex2 != -1) break;
}
if (f1 != f2) {
if (f1 > f2) {
var temp=f1;
f1=f2;
f2=temp;
temp=atomIndex1;
atomIndex1=atomIndex2;
atomIndex2=temp;
}if (fa == null ) fa=Clazz.array($I$(12), [this.mFragmentList.size$(), null]);
if (fa[f2] == null ) fa[f2]=Clazz.array($I$(12), [f2]);
if (fa[f2][f1] != null ) fa[f2][f1].add$I$I(atomIndex1, atomIndex2);
 else {
fa[f2][f1]=Clazz.new_([this.mFragmentList.get$I(f1), this.mFragmentList.get$I(f2), atomIndex1, atomIndex2],$I$(12,1).c$$com_actelion_research_chem_coords_InventorFragment$com_actelion_research_chem_coords_InventorFragment$I$I);
if (associationList == null ) associationList=Clazz.new_($I$(3,1));
associationList.add$O(fa[f2][f1]);
}}}}
return associationList;
}, p$1);

Clazz.newMeth(C$, 'createChargeAssociation',  function () {
var negChargeList=Clazz.new_($I$(3,1));
var posChargeList=Clazz.new_($I$(3,1));
var chargeList=Clazz.new_($I$(3,1));
for (var f, $f = this.mFragmentList.iterator$(); $f.hasNext$()&&((f=($f.next$())),1);) {
var fragmentCharge=0;
chargeList.clear$();
for (var i=0; i < f.size$(); i++) {
var atom=f.getGlobalAtom$I(i);
var charge=this.mUnPairedCharge[atom];
if (charge != 0) {
chargeList.add$O(Clazz.new_($I$(13,1).c$$com_actelion_research_chem_coords_InventorFragment$I$I,[f, i, charge]));
fragmentCharge+=charge;
}}
if (fragmentCharge != 0) {
$I$(14,"sort$java_util_List$java_util_Comparator",[chargeList, ((P$.CoordinateInventor$2||
(function(){/*a*/var C$=Clazz.newClass(P$, "CoordinateInventor$2", function(){Clazz.newInstance(this, arguments[0],1,C$);}, null, 'java.util.Comparator', 1);

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

Clazz.newMeth(C$, ['compare$com_actelion_research_chem_coords_InventorCharge$com_actelion_research_chem_coords_InventorCharge','compare$O$O'],  function (o1, o2) {
var c1=Math.abs(o1.charge);
var c2=Math.abs(o2.charge);
return c1 < c2 ? -1 : c1 == c2 ? 0 : 1;
});
})()
), Clazz.new_(P$.CoordinateInventor$2.$init$,[this, null]))]);
for (var ic, $ic = chargeList.iterator$(); $ic.hasNext$()&&((ic=($ic.next$())),1);) {
if (fragmentCharge * ic.charge > 0) {
var charge=(Math.abs(fragmentCharge) >= Math.abs(ic.charge)) ? ic.charge : fragmentCharge;
fragmentCharge-=charge;
(charge < 0 ? negChargeList : posChargeList).add$O(Clazz.new_($I$(13,1).c$$com_actelion_research_chem_coords_InventorFragment$I$I,[f, ic.atom, charge]));
if (fragmentCharge == 0) break;
}}
}}
if (negChargeList.size$() == 0 || posChargeList.size$() == 0 ) return null;
$I$(14,"sort$java_util_List$java_util_Comparator",[posChargeList, ((P$.CoordinateInventor$3||
(function(){/*a*/var C$=Clazz.newClass(P$, "CoordinateInventor$3", function(){Clazz.newInstance(this, arguments[0],1,C$);}, null, 'java.util.Comparator', 1);

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

Clazz.newMeth(C$, ['compare$com_actelion_research_chem_coords_InventorCharge$com_actelion_research_chem_coords_InventorCharge','compare$O$O'],  function (o1, o2) {
var c1=o1.fragment.size$();
var c2=o1.fragment.size$();
return c1 < c2 ? 1 : c1 == c2 ? 0 : -1;
});
})()
), Clazz.new_(P$.CoordinateInventor$3.$init$,[this, null]))]);
$I$(14,"sort$java_util_List$java_util_Comparator",[negChargeList, ((P$.CoordinateInventor$4||
(function(){/*a*/var C$=Clazz.newClass(P$, "CoordinateInventor$4", function(){Clazz.newInstance(this, arguments[0],1,C$);}, null, 'java.util.Comparator', 1);

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

Clazz.newMeth(C$, ['compare$com_actelion_research_chem_coords_InventorCharge$com_actelion_research_chem_coords_InventorCharge','compare$O$O'],  function (o1, o2) {
var c1=o1.fragment.size$();
var c2=o1.fragment.size$();
return c1 < c2 ? -1 : c1 == c2 ? 0 : 1;
});
})()
), Clazz.new_(P$.CoordinateInventor$4.$init$,[this, null]))]);
for (var pc, $pc = posChargeList.iterator$(); $pc.hasNext$()&&((pc=($pc.next$())),1);) {
for (var nc, $nc = negChargeList.iterator$(); $nc.hasNext$()&&((nc=($nc.next$())),1);) {
if (pc.charge == -nc.charge) {
this.mUnPairedCharge[pc.fragment.getGlobalAtom$I(pc.atom)]-=pc.charge;
this.mUnPairedCharge[nc.fragment.getGlobalAtom$I(nc.atom)]-=nc.charge;
return Clazz.new_($I$(12,1).c$$com_actelion_research_chem_coords_InventorFragment$com_actelion_research_chem_coords_InventorFragment$I$I,[pc.fragment, nc.fragment, pc.atom, nc.atom]);
}}
}
for (var pc, $pc = posChargeList.iterator$(); $pc.hasNext$()&&((pc=($pc.next$())),1);) {
for (var nc, $nc = negChargeList.iterator$(); $nc.hasNext$()&&((nc=($nc.next$())),1);) {
if (pc.charge > -nc.charge) {
this.mUnPairedCharge[pc.fragment.getGlobalAtom$I(pc.atom)]+=nc.charge;
this.mUnPairedCharge[nc.fragment.getGlobalAtom$I(nc.atom)]-=nc.charge;
return Clazz.new_($I$(12,1).c$$com_actelion_research_chem_coords_InventorFragment$com_actelion_research_chem_coords_InventorFragment$I$I,[pc.fragment, nc.fragment, pc.atom, nc.atom]);
}}
}
for (var pc, $pc = posChargeList.iterator$(); $pc.hasNext$()&&((pc=($pc.next$())),1);) {
for (var nc, $nc = negChargeList.iterator$(); $nc.hasNext$()&&((nc=($nc.next$())),1);) {
if (pc.charge < -nc.charge) {
this.mUnPairedCharge[pc.fragment.getGlobalAtom$I(pc.atom)]-=pc.charge;
this.mUnPairedCharge[nc.fragment.getGlobalAtom$I(nc.atom)]+=pc.charge;
return Clazz.new_($I$(12,1).c$$com_actelion_research_chem_coords_InventorFragment$com_actelion_research_chem_coords_InventorFragment$I$I,[pc.fragment, nc.fragment, pc.atom, nc.atom]);
}}
}
return null;
}, p$1);

Clazz.newMeth(C$, 'createDisconnectedAssociation',  function () {
if (this.mFragmentList.size$() < 2) return null;
return Clazz.new_([this.mFragmentList.get$I(0), this.mFragmentList.get$I(1)],$I$(12,1).c$$com_actelion_research_chem_coords_InventorFragment$com_actelion_research_chem_coords_InventorFragment);
}, p$1);

Clazz.newMeth(C$, 'updateFragmentList$com_actelion_research_chem_coords_InventorFragment$com_actelion_research_chem_coords_InventorFragment$com_actelion_research_chem_coords_InventorFragment',  function (fOld1, fOld2, fJoined) {
var index=Math.min(this.mFragmentList.indexOf$O(fOld1), this.mFragmentList.indexOf$O(fOld2));
this.mFragmentList.add$I$O(index, fJoined);
this.mFragmentList.remove$O(fOld1);
this.mFragmentList.remove$O(fOld2);
}, p$1);
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-25 13:07:47 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
