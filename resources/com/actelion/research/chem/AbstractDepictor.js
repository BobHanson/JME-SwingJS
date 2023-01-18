(function(){var P$=Clazz.newPackage("com.actelion.research.chem"),p$1={},I$=[[0,'com.actelion.research.gui.generic.GenericRectangle','com.actelion.research.chem.DepictorTransformation','java.util.ArrayList','com.actelion.research.gui.generic.GenericPoint','com.actelion.research.util.ColorHelper','java.awt.Color',['com.actelion.research.chem.AbstractDepictor','.DepictorLine'],'com.actelion.research.gui.editor.AtomQueryFeatureDialogBuilder','StringBuilder','java.util.Arrays',['com.actelion.research.chem.AbstractDepictor','.DepictorDot'],'com.actelion.research.gui.generic.GenericPolygon']],I$0=I$[0],$I$=function(i,n){return((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "AbstractDepictor", function(){
Clazz.newInstance(this, arguments,0,C$);
});
C$.$classes$=[['DepictorDot',9],['DepictorLine',9]];

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
this.mBoundingRect=Clazz.new_($I$(1,1));
},1);

C$.$fields$=[['Z',['mIsValidatingView'],'D',['mpBondSpacing','mpDotDiameter','mpLineWidth','mpQFDiameter','mpBondHiliteRadius','mFactorTextSize','mpExcludeGroupRadius','mChiralTextSize'],'I',['mpLabelSize','mStandardForegroundColor','mDisplayMode','mCurrentColor','mPreviousColor','mOverruleForeground','mOverruleBackground','mBondBGHiliteColor','mBondFGHiliteColor','mExcludeGroupFGColor','mExcludeGroupBGColor','mCustomForeground','mCustomBackground','mRGBColor'],'O',['mAtomIsConnected','boolean[]','+mAtomLabelDisplayed','mpTabuZone','java.util.ArrayList','+mpDot','mMol','com.actelion.research.chem.StereoMolecule','mBoundingRect','com.actelion.research.gui.generic.GenericRectangle','mTransformation','com.actelion.research.chem.DepictorTransformation','mChiralTextLocation','com.actelion.research.gui.generic.GenericPoint','mAtomColor','int[]','+mAtomHiliteColor','mAtomHiliteRadius','float[]','mAtomText','String[]','mAlternativeCoords','com.actelion.research.gui.generic.GenericPoint[]','mContext','<T>']]
,['O',['ATOM_LABEL_COLOR','int[]']]]

Clazz.newMeth(C$, 'c$$com_actelion_research_chem_StereoMolecule',  function (mol) {
C$.c$$com_actelion_research_chem_StereoMolecule$I.apply(this, [mol, 0]);
}, 1);

Clazz.newMeth(C$, 'c$$com_actelion_research_chem_StereoMolecule$I',  function (mol, displayMode) {
;C$.$init$.apply(this);
this.mMol=mol;
this.mDisplayMode=displayMode;
this.init$();
}, 1);

Clazz.newMeth(C$, 'setDisplayMode$I',  function (displayMode) {
this.mDisplayMode=displayMode;
});

Clazz.newMeth(C$, 'setAtomText$SA',  function (atomText) {
this.mAtomText=atomText;
});

Clazz.newMeth(C$, 'setForegroundColor$java_awt_Color$java_awt_Color',  function (foreground, background) {
this.setForegroundColor$I$I(foreground == null  ? 0 : foreground.getRGB$(), background == null  ? 0 : background.getRGB$());
});

Clazz.newMeth(C$, 'setForegroundColor$I$I',  function (foreground, background) {
this.mStandardForegroundColor=-6;
this.mCustomForeground=foreground;
this.mCustomBackground=background;
p$1.updateBondHiliteColor.apply(this, []);
});

Clazz.newMeth(C$, 'setOverruleColor$java_awt_Color$java_awt_Color',  function (foreground, background) {
this.setOverruleColor$I$I(foreground == null  ? 0 : foreground.getRGB$(), background == null  ? 0 : background.getRGB$());
});

Clazz.newMeth(C$, 'setOverruleColor$I$I',  function (foreground, background) {
this.mOverruleForeground=foreground;
this.mOverruleBackground=background;
p$1.updateBondHiliteColor.apply(this, []);
});

Clazz.newMeth(C$, 'setAtomHighlightColors$IA$FA',  function (argb, radius) {
this.mAtomHiliteColor=argb;
this.mAtomHiliteRadius=radius;
});

Clazz.newMeth(C$, 'setTransformation$com_actelion_research_chem_DepictorTransformation',  function (t) {
this.mTransformation=t;
});

Clazz.newMeth(C$, 'setFactorTextSize$D',  function (factor) {
this.mFactorTextSize=factor;
});

Clazz.newMeth(C$, 'getTransformation$',  function () {
return this.mTransformation;
});

Clazz.newMeth(C$, 'applyTransformation$com_actelion_research_chem_DepictorTransformation',  function (t) {
t.applyTo$com_actelion_research_chem_DepictorTransformation(this.mTransformation);
t.applyTo$com_actelion_research_gui_generic_GenericRectangle(this.mBoundingRect);
t.applyTo$com_actelion_research_gui_generic_GenericPoint(this.mChiralTextLocation);
});

Clazz.newMeth(C$, 'updateCoords$O$com_actelion_research_gui_generic_GenericRectangle$I',  function (context, viewRect, mode) {
this.validateView$O$com_actelion_research_gui_generic_GenericRectangle$I(context, viewRect, mode);
if (this.mTransformation.isVoidTransformation$()) {
return null;
} else {
var t=this.mTransformation;
this.mTransformation.applyTo$com_actelion_research_chem_Molecule(this.mMol);
this.mTransformation=Clazz.new_($I$(2,1));
return t;
}});

Clazz.newMeth(C$, 'simpleUpdateCoords$com_actelion_research_gui_generic_GenericRectangle$I',  function (viewRect, mode) {
this.simpleValidateView$com_actelion_research_gui_generic_GenericRectangle$I(viewRect, mode);
if (this.mTransformation.isVoidTransformation$()) {
return null;
} else {
var t=this.mTransformation;
this.mTransformation.applyTo$com_actelion_research_chem_Molecule(this.mMol);
this.mTransformation=Clazz.new_($I$(2,1));
return t;
}});

Clazz.newMeth(C$, 'getMolecule$',  function () {
return this.mMol;
});

Clazz.newMeth(C$, 'validateView$O$com_actelion_research_gui_generic_GenericRectangle$I',  function (context, viewRect, mode) {
if (this.mMol.getAllAtoms$() == 0) return null;
var t1=this.simpleValidateView$com_actelion_research_gui_generic_GenericRectangle$I(viewRect, mode);
this.mMol.ensureHelperArrays$I(p$1.requiredHelperArrays.apply(this, []));
p$1.markIsolatedAtoms.apply(this, []);
this.mpDot.clear$();
this.mpTabuZone.clear$();
this.mContext=context;
p$1.calculateParameters.apply(this, []);
p$1.mpSetNormalLabelSize.apply(this, []);
this.mIsValidatingView=true;
for (var i=0; i < this.mMol.getAllAtoms$(); i++) p$1.mpDrawAtom$I$IAA.apply(this, [i, null]);

this.mIsValidatingView=false;
var avbl=this.mTransformation.getScaling$() * this.mMol.getAverageBondLength$();
p$1.expandBoundsByTabuZones$D.apply(this, [avbl]);
p$1.setChiralTextLocation$com_actelion_research_gui_generic_GenericRectangle$D$I.apply(this, [viewRect, avbl, mode]);
if (viewRect == null  || viewRect.contains$com_actelion_research_gui_generic_GenericRectangle(this.mBoundingRect) ) return t1;
var t2=Clazz.new_($I$(2,1).c$$com_actelion_research_gui_generic_GenericRectangle$com_actelion_research_gui_generic_GenericRectangle$D$I,[this.mBoundingRect, viewRect, avbl, mode]);
t2.applyTo$com_actelion_research_chem_DepictorTransformation(this.mTransformation);
t2.applyTo$com_actelion_research_gui_generic_GenericRectangle(this.mBoundingRect);
t2.applyTo$com_actelion_research_gui_generic_GenericPoint(this.mChiralTextLocation);
if (t1 == null ) return t2;
t2.applyTo$com_actelion_research_chem_DepictorTransformation(t1);
return t1;
});

Clazz.newMeth(C$, 'simpleValidateView$com_actelion_research_gui_generic_GenericRectangle$I',  function (viewRect, mode) {
if (this.mMol.getAllAtoms$() == 0) return null;
this.mBoundingRect=this.simpleCalculateBounds$();
var avbl=this.mTransformation.getScaling$() * this.mMol.getAverageBondLength$();
var t=Clazz.new_($I$(2,1).c$$com_actelion_research_gui_generic_GenericRectangle$com_actelion_research_gui_generic_GenericRectangle$D$I,[this.mBoundingRect, viewRect, avbl, mode]);
if (t.isVoidTransformation$()) {
t=null;
} else {
t.applyTo$com_actelion_research_chem_DepictorTransformation(this.mTransformation);
t.applyTo$com_actelion_research_gui_generic_GenericRectangle(this.mBoundingRect);
}p$1.setChiralTextLocation$com_actelion_research_gui_generic_GenericRectangle$D$I.apply(this, [viewRect, avbl, mode]);
return t;
});

Clazz.newMeth(C$, 'onDrawBond$I$D$D$D$D',  function (bond, x1, y1, x2, y2) {
});

Clazz.newMeth(C$, 'onDrawAtom$I$S$D$D',  function (atom, symbol, x, y) {
});

Clazz.newMeth(C$, 'simpleCalculateBounds$',  function () {
var minx=this.getAtomX$I(0);
var maxx=this.getAtomX$I(0);
var miny=this.getAtomY$I(0);
var maxy=this.getAtomY$I(0);
for (var i=0; i < this.mMol.getAllAtoms$(); i++) {
if (minx > this.getAtomX$I(i) ) minx=this.getAtomX$I(i);
if (maxx < this.getAtomX$I(i) ) maxx=this.getAtomX$I(i);
if (miny > this.getAtomY$I(i) ) miny=this.getAtomY$I(i);
if (maxy < this.getAtomY$I(i) ) maxy=this.getAtomY$I(i);
}
return Clazz.new_($I$(1,1).c$$D$D$D$D,[minx, miny, maxx - minx, maxy - miny]);
});

Clazz.newMeth(C$, 'expandBoundsByTabuZones$D',  function (avbl) {
for (var i=0; i < this.mpTabuZone.size$(); i++) this.mBoundingRect=this.mBoundingRect.union$com_actelion_research_gui_generic_GenericRectangle(this.mpTabuZone.get$I(i));

p$1.expandByHiliteBackgrounds$D.apply(this, [avbl]);
var border=0.1 * avbl;
this.mBoundingRect.x-=border;
this.mBoundingRect.y-=border;
this.mBoundingRect.width+=2.0 * border;
this.mBoundingRect.height+=2.0 * border;
}, p$1);

Clazz.newMeth(C$, 'expandByHiliteBackgrounds$D',  function (avbl) {
var isAtomHilited=Clazz.array(Boolean.TYPE, [this.mMol.getAllAtoms$()]);
for (var i=0; i < this.mMol.getAllBonds$(); i++) {
if (this.mMol.isBondBackgroundHilited$I(i)) {
isAtomHilited[this.mMol.getBondAtom$I$I(0, i)]=true;
isAtomHilited[this.mMol.getBondAtom$I$I(1, i)]=true;
}}
var rect=Clazz.new_($I$(1,1));
for (var i=0; i < this.mMol.getAllAtoms$(); i++) {
var radius=Long.$ne((Long.$and(this.mMol.getAtomQueryFeatures$I(i),536870912)),0 ) ? avbl * 0.47 : isAtomHilited[i] ? avbl * 0.38 : 0;
if (radius != 0 ) {
var x=this.mTransformation.transformX$D(this.mMol.getAtomX$I(i));
var y=this.mTransformation.transformY$D(this.mMol.getAtomY$I(i));
rect.set$D$D$D$D(x - radius, y - radius, radius * 2, radius * 2);
this.mBoundingRect=this.mBoundingRect.union$com_actelion_research_gui_generic_GenericRectangle(rect);
}}
}, p$1);

Clazz.newMeth(C$, 'setChiralTextLocation$com_actelion_research_gui_generic_GenericRectangle$D$I',  function (viewRect, avbl, mode) {
var spacing=avbl / 2.0;
switch (mode & 786432) {
case 786432:
if (viewRect != null ) {
this.mChiralTextLocation.x=viewRect.x + viewRect.width / 2.0;
this.mChiralTextLocation.y=viewRect.y + viewRect.height - spacing;
break;
}case 0:
this.mChiralTextLocation.x=this.mBoundingRect.x + this.mBoundingRect.width / 2.0;
this.mChiralTextLocation.y=this.mBoundingRect.y + this.mBoundingRect.height + spacing ;
if (viewRect != null  && this.mChiralTextLocation.y > viewRect.y + viewRect.height - spacing  ) this.mChiralTextLocation.y=viewRect.y + viewRect.height - spacing;
break;
case 524288:
if (viewRect != null ) {
this.mChiralTextLocation.x=viewRect.x + viewRect.width / 2.0;
this.mChiralTextLocation.y=viewRect.y + spacing;
break;
}case 262144:
this.mChiralTextLocation.x=this.mBoundingRect.x + this.mBoundingRect.width / 2.0;
this.mChiralTextLocation.y=this.mBoundingRect.y - spacing;
if (viewRect != null  && this.mChiralTextLocation.y < viewRect.y + spacing  ) this.mChiralTextLocation.y=viewRect.y + spacing;
break;
}
}, p$1);

Clazz.newMeth(C$, 'getAtomX$I',  function (atom) {
return this.mTransformation.transformX$D(this.mMol.getAtomX$I(atom));
});

Clazz.newMeth(C$, 'getAtomY$I',  function (atom) {
return this.mTransformation.transformY$D(this.mMol.getAtomY$I(atom));
});

Clazz.newMeth(C$, 'getBoundingRect$',  function () {
return this.mBoundingRect;
});

Clazz.newMeth(C$, 'init$',  function () {
this.mFactorTextSize=1.0;
this.mTransformation=Clazz.new_($I$(2,1));
this.mpTabuZone=Clazz.new_($I$(3,1));
this.mpDot=Clazz.new_($I$(3,1));
this.mAtomLabelDisplayed=Clazz.array(Boolean.TYPE, [this.mMol.getAllAtoms$()]);
this.mChiralTextLocation=Clazz.new_($I$(4,1));
this.mStandardForegroundColor=0;
this.mCurrentColor=-1;
p$1.updateBondHiliteColor.apply(this, []);
});

Clazz.newMeth(C$, 'updateBondHiliteColor',  function () {
var background=(this.mOverruleBackground != 0) ? this.mOverruleBackground : (this.mCustomBackground != 0) ? this.mCustomBackground : -1;
this.mBondBGHiliteColor=$I$(5).intermediateColor$I$I$F(background, -10706689, 0.3);
this.mBondFGHiliteColor=$I$(5).getContrastColor$I$I(-32768, background);
this.mExcludeGroupBGColor=-24321;
this.mExcludeGroupFGColor=-6291392;
}, p$1);

Clazz.newMeth(C$, 'calculateParameters',  function () {
var averageBondLength=this.mTransformation.getScaling$() * this.mMol.getAverageBondLength$();
this.mpLineWidth=averageBondLength * 0.06;
this.mpBondSpacing=averageBondLength * 0.15;
this.mpBondHiliteRadius=averageBondLength * 0.38;
this.mpExcludeGroupRadius=averageBondLength * 0.47;
this.mpLabelSize=((averageBondLength * this.mFactorTextSize * 0.6  + 0.5)|0);
this.mpDotDiameter=averageBondLength * 0.12;
this.mpQFDiameter=averageBondLength * 0.4;
this.mChiralTextSize=averageBondLength * 0.5 + 0.5;
}, p$1);

Clazz.newMeth(C$, 'paint$O',  function (context) {
if (this.mMol.getAllAtoms$() == 0) return;
this.mMol.ensureHelperArrays$I(p$1.requiredHelperArrays.apply(this, []));
this.mContext=context;
p$1.calculateParameters.apply(this, []);
var esrGroupMemberCount=this.mMol.getESRGroupMemberCounts$();
var explicitAtomColors=false;
this.mAtomColor=Clazz.array(Integer.TYPE, [this.mMol.getAllAtoms$()]);
for (var atom=0; atom < this.mMol.getAllAtoms$(); atom++) {
this.mAtomColor[atom]=this.mMol.getAtomColor$I(atom);
if (this.mAtomColor[atom] != 0) explicitAtomColors=true;
if (this.mMol.isSelectedAtom$I(atom)) this.mAtomColor[atom]=128;
if (this.mMol.getStereoProblem$I(atom) && (this.mDisplayMode & 2048) == 0 ) this.mAtomColor[atom]=256;
}
p$1.setColor_$I.apply(this, [-10]);
if (this.mAtomHiliteColor != null  && (this.mAtomHiliteColor.length >= this.mMol.getAtoms$()) ) this.hiliteAtomBackgrounds$IA$FA(this.mAtomHiliteColor, this.mAtomHiliteRadius);
p$1.hiliteExcludeGroups.apply(this, []);
p$1.hiliteBondBackgrounds.apply(this, []);
p$1.indicateQueryFeatures.apply(this, []);
p$1.addChiralInfo.apply(this, []);
p$1.mpSetNormalLabelSize.apply(this, []);
this.setLineWidth$D(this.mpLineWidth);
p$1.setColor_$I.apply(this, [this.mStandardForegroundColor]);
p$1.markIsolatedAtoms.apply(this, []);
this.mpDot.clear$();
this.mpTabuZone.clear$();
if ((this.mDisplayMode & 1) != 0) {
p$1.mpDrawAllBonds$IAA.apply(this, [esrGroupMemberCount]);
p$1.mpDrawAllDots.apply(this, []);
p$1.mpDrawBondQueryFeatures.apply(this, []);
}for (var i=0; i < this.mMol.getAllAtoms$(); i++) {
if (p$1.isHighlightedAtom$I.apply(this, [i])) {
p$1.setColor_$I.apply(this, [-3]);
p$1.mpDrawAtom$I$IAA.apply(this, [i, esrGroupMemberCount]);
p$1.setColor_$I.apply(this, [this.mStandardForegroundColor]);
} else if (this.mAtomColor[i] != 0) {
p$1.setColor_$I.apply(this, [this.mAtomColor[i]]);
p$1.mpDrawAtom$I$IAA.apply(this, [i, esrGroupMemberCount]);
p$1.setColor_$I.apply(this, [this.mStandardForegroundColor]);
} else if (!explicitAtomColors && this.mMol.getMoleculeColor$() != 1  && this.mMol.getAtomicNo$I(i) != 1  && this.mMol.getAtomicNo$I(i) != 6  && ((this.mDisplayMode & 1024) == 0)  && this.mMol.getAtomList$I(i) == null   && this.mMol.getAtomicNo$I(i) < C$.ATOM_LABEL_COLOR.length ) {
p$1.setRGBColor$I.apply(this, [p$1.getContrastColor$I$I.apply(this, [C$.ATOM_LABEL_COLOR[this.mMol.getAtomicNo$I(i)], i])]);
p$1.mpDrawAtom$I$IAA.apply(this, [i, esrGroupMemberCount]);
p$1.setColor_$I.apply(this, [this.mStandardForegroundColor]);
} else {
p$1.mpDrawAtom$I$IAA.apply(this, [i, esrGroupMemberCount]);
}}
if ((this.mDisplayMode & 1) == 0) {
p$1.mpDrawAllDots.apply(this, []);
p$1.mpDrawBondQueryFeatures.apply(this, []);
p$1.mpDrawAllBonds$IAA.apply(this, [esrGroupMemberCount]);
}});

Clazz.newMeth(C$, 'getBackgroundColor$',  function () {
return Clazz.new_([this.getBackgroundRGB$()],$I$(6,1).c$$I);
});

Clazz.newMeth(C$, 'getBackgroundRGB$',  function () {
return this.mOverruleBackground != 0 ? this.mOverruleBackground : this.mCustomBackground != 0 ? this.mCustomBackground : -1;
});

Clazz.newMeth(C$, 'getContrastColor$I$I',  function (rgb, atom) {
var bg=(this.mOverruleBackground != 0) ? this.mOverruleBackground : (this.mAtomHiliteColor != null  && atom < this.mAtomHiliteColor.length  && (this.mAtomHiliteColor[atom] & -16777216) != 0 ) ? this.mAtomHiliteColor[atom] : (this.mCustomBackground != 0) ? this.mCustomBackground : -1;
return $I$(5).getContrastColor$I$I(rgb, bg);
}, p$1);

Clazz.newMeth(C$, 'isHighlightedAtom$I',  function (atom) {
if (this.mMol.getAllConnAtoms$I(atom) == 0) return false;
for (var i=0; i < this.mMol.getAllConnAtoms$I(atom); i++) if (!this.mMol.isBondForegroundHilited$I(this.mMol.getConnBond$I$I(atom, i))) return false;

return true;
}, p$1);

Clazz.newMeth(C$, 'requiredHelperArrays',  function () {
return ((this.mDisplayMode & 256) != 0) ? 63 : ((this.mDisplayMode & 512) != 0) ? 95 : 31;
}, p$1);

Clazz.newMeth(C$, 'markIsolatedAtoms',  function () {
this.mAtomIsConnected=Clazz.array(Boolean.TYPE, [this.mMol.getAllAtoms$()]);
for (var bnd=0; bnd < this.mMol.getAllBonds$(); bnd++) {
this.mAtomIsConnected[this.mMol.getBondAtom$I$I(0, bnd)]=true;
this.mAtomIsConnected[this.mMol.getBondAtom$I$I(1, bnd)]=true;
}
}, p$1);

Clazz.newMeth(C$, 'addChiralInfo',  function () {
if ((this.mDisplayMode & 32) != 0) return;
var chiralText=this.mMol.getChiralText$();
if (chiralText != null ) {
if (this.mChiralTextLocation.x == 0.0  && this.mChiralTextLocation.y == 0.0  ) {
var avbl=this.mTransformation.getScaling$() * this.mMol.getAverageBondLength$();
this.mBoundingRect=this.simpleCalculateBounds$();
p$1.expandBoundsByTabuZones$D.apply(this, [avbl]);
p$1.setChiralTextLocation$com_actelion_research_gui_generic_GenericRectangle$D$I.apply(this, [null, avbl, 0]);
}this.setTextSize$I((this.mChiralTextSize|0));
if (this.mMol.getMoleculeColor$() != 1) p$1.setColor_$I.apply(this, [448]);
this.drawString$S$D$D(chiralText, this.mChiralTextLocation.x, this.mChiralTextLocation.y + 0.3 * this.mChiralTextSize);
}}, p$1);

Clazz.newMeth(C$, 'hiliteAtomBackgrounds$IA$FA',  function (argb, radius) {
var background=(this.mOverruleBackground != 0) ? this.mOverruleBackground : (this.mCustomBackground != 0) ? this.mCustomBackground : -1;
var avbl=this.mTransformation.getScaling$() * this.mMol.getAverageBondLength$();
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) {
var alpha=(argb[atom] & -16777216) >>> 24;
if (alpha != 0) {
var rgb=argb[atom];
if (alpha != 255) rgb=$I$(5).intermediateColor$I$I$F(background, argb[atom], alpha / 255.0);
var r=(radius == null ) ? 0.5 * avbl : 0.6 * radius[atom] * avbl ;
this.setRGB$I(rgb);
this.fillCircle$D$D$D(this.getAtomX$I(atom) - r, this.getAtomY$I(atom) - r, 2 * r);
}}
});

Clazz.newMeth(C$, 'hiliteBondBackgrounds',  function () {
this.setLineWidth$D(2 * this.mpBondHiliteRadius);
var line=Clazz.new_($I$(7,1));
for (var bond=0; bond < this.mMol.getAllBonds$(); bond++) {
var atom1=this.mMol.getBondAtom$I$I(0, bond);
var atom2=this.mMol.getBondAtom$I$I(1, bond);
if (this.mMol.isBondBackgroundHilited$I(bond)) {
line.x1=this.getAtomX$I(atom1);
line.y1=this.getAtomY$I(atom1);
line.x2=this.getAtomX$I(atom2);
line.y2=this.getAtomY$I(atom2);
p$1.setColor_$I.apply(this, [-2]);
this.drawBlackLine$com_actelion_research_chem_AbstractDepictor_DepictorLine(line);
}}
}, p$1);

Clazz.newMeth(C$, 'hiliteExcludeGroups',  function () {
if (this.mMol.isFragment$()) {
var radius=this.mpExcludeGroupRadius;
p$1.setColor_$I.apply(this, [-7]);
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) if (Long.$ne((Long.$and(this.mMol.getAtomQueryFeatures$I(atom),536870912)),0 )) this.fillCircle$D$D$D(this.getAtomX$I(atom) - radius, this.getAtomY$I(atom) - radius, 2 * radius);

this.setLineWidth$D(2.0 * this.mpExcludeGroupRadius);
var line=Clazz.new_($I$(7,1));
for (var bond=0; bond < this.mMol.getAllBonds$(); bond++) {
var atom1=this.mMol.getBondAtom$I$I(0, bond);
var atom2=this.mMol.getBondAtom$I$I(1, bond);
if (Long.$ne((Long.$and(this.mMol.getAtomQueryFeatures$I(atom1),this.mMol.getAtomQueryFeatures$I(atom2) , 536870912 )),0 )) {
line.x1=this.getAtomX$I(atom1);
line.y1=this.getAtomY$I(atom1);
line.x2=this.getAtomX$I(atom2);
line.y2=this.getAtomY$I(atom2);
this.drawBlackLine$com_actelion_research_chem_AbstractDepictor_DepictorLine(line);
}}
}}, p$1);

Clazz.newMeth(C$, 'indicateQueryFeatures',  function () {
if (this.mMol.isFragment$()) {
p$1.setColor_$I.apply(this, [320]);
if (((this.mDisplayMode & 8) != 0)) for (var atom=0; atom < this.mMol.getAtoms$(); atom++) if (Long.$ne((Long.$and(this.mMol.getAtomQueryFeatures$I(atom),(Long.$not(536870912)))),0 )) this.fillCircle$D$D$D(this.getAtomX$I(atom) - this.mpQFDiameter / 2, this.getAtomY$I(atom) - this.mpQFDiameter / 2, this.mpQFDiameter);

for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
if (this.mMol.getBondQueryFeatures$I(bond) != 0) {
var atom1=this.mMol.getBondAtom$I$I(0, bond);
var atom2=this.mMol.getBondAtom$I$I(1, bond);
this.fillCircle$D$D$D((this.getAtomX$I(atom1) + this.getAtomX$I(atom2) - this.mpQFDiameter) / 2, (this.getAtomY$I(atom1) + this.getAtomY$I(atom2) - this.mpQFDiameter) / 2, this.mpQFDiameter);
}}
}}, p$1);

Clazz.newMeth(C$, 'mpDrawAllBonds$IAA',  function (esrGroupMemberCount) {
var origColor=this.mStandardForegroundColor;
var origForeground=this.mCustomForeground;
if ((this.mDisplayMode & 16384) != 0) {
this.mStandardForegroundColor=-6;
this.mCustomForeground=-8355712;
p$1.setColor_$I.apply(this, [1]);
}this.mAlternativeCoords=Clazz.array($I$(4), [this.mMol.getAllAtoms$()]);
for (var i=0; i < this.mMol.getAllBonds$(); i++) if (this.mMol.getBondType$I(i) == 2 || this.mMol.getBondType$I(i) == 386  || this.mMol.getBondType$I(i) == 64 ) p$1.mpDrawBond$I.apply(this, [i]);

for (var i=0; i < this.mMol.getAllBonds$(); i++) if (this.mMol.getBondType$I(i) != 2 && this.mMol.getBondType$I(i) != 386  && this.mMol.getBondType$I(i) != 64 ) p$1.mpDrawBond$I.apply(this, [i]);

if ((this.mDisplayMode & 64) == 0) {
for (var i=0; i < this.mMol.getAllBonds$(); i++) {
if (this.mMol.getBondCIPParity$I(i) != 0) {
var cipStr=null;
if (this.mMol.getBondCIPParity$I(i) == 1 || this.mMol.getBondCIPParity$I(i) == 2 ) {
if (this.mMol.getBondOrder$I(i) == 2 || this.mMol.getBondESRType$I(i) == 0  || esrGroupMemberCount == null   || esrGroupMemberCount[this.mMol.getBondESRType$I(i)][this.mMol.getBondESRGroup$I(i)] > 1 ) {
if (this.mMol.getBondCIPParity$I(i) == 1) cipStr=(this.mMol.getBondOrder$I(i) == 2) ? "E" : this.mMol.isBondParityPseudo$I(i) ? "p" : "P";
 else cipStr=(this.mMol.getBondOrder$I(i) == 2) ? "Z" : this.mMol.isBondParityPseudo$I(i) ? "m" : "M";
}} else {
cipStr="?";
}if (cipStr != null ) {
p$1.mpSetSmallLabelSize.apply(this, []);
p$1.setColor_$I.apply(this, [this.mMol.isBondForegroundHilited$I(i) ? -3 : (this.mMol.getMoleculeColor$() == 1 || (this.mDisplayMode & 4096) != 0 ) ? this.mStandardForegroundColor : 448]);
var atom1=this.mMol.getBondAtom$I$I(0, i);
var atom2=this.mMol.getBondAtom$I$I(1, i);
var x=(this.getAtomX$I(atom1) + this.getAtomX$I(atom2)) / 2;
var y=(this.getAtomY$I(atom1) + this.getAtomY$I(atom2)) / 2;
var dx=(this.getAtomX$I(atom1) - this.getAtomX$I(atom2)) / 3;
var dy=(this.getAtomY$I(atom1) - this.getAtomY$I(atom2)) / 3;
p$1.mpDrawString$D$D$S$Z.apply(this, [x + dy, y - dx, cipStr, true]);
p$1.setColor_$I.apply(this, [this.mStandardForegroundColor]);
p$1.mpSetNormalLabelSize.apply(this, []);
}}}
}if ((this.mDisplayMode & 4) != 0) {
p$1.mpSetSmallLabelSize.apply(this, []);
p$1.setColor_$I.apply(this, [384]);
for (var i=0; i < this.mMol.getAllBonds$(); i++) {
var atom1=this.mMol.getBondAtom$I$I(0, i);
var atom2=this.mMol.getBondAtom$I$I(1, i);
var type=this.mMol.isDelocalizedBond$I(i) ? "d" : this.mMol.isAromaticBond$I(i) ? "a" : "";
var x=(this.getAtomX$I(atom1) + this.getAtomX$I(atom2)) / 2;
var y=(this.getAtomY$I(atom1) + this.getAtomY$I(atom2)) / 2;
p$1.mpDrawString$D$D$S$Z.apply(this, [x, y, type + String.valueOf$I(i), true]);
}
p$1.setColor_$I.apply(this, [this.mStandardForegroundColor]);
p$1.mpSetNormalLabelSize.apply(this, []);
}if ((this.mDisplayMode & 16384) != 0) {
this.mStandardForegroundColor=origColor;
this.mCustomForeground=origForeground;
}}, p$1);

Clazz.newMeth(C$, 'mpDrawBond$I',  function (bnd) {
var theLine=Clazz.new_($I$(7,1));
var aLine=Clazz.new_($I$(7,1));
var bLine=Clazz.new_($I$(7,1));
var piBondOffset=Clazz.new_($I$(4,1));
var nextBondOffset=Clazz.new_($I$(4,1));
var atom1=this.mMol.getBondAtom$I$I(0, bnd);
var atom2=this.mMol.getBondAtom$I$I(1, bnd);
this.onDrawBond$I$D$D$D$D(bnd, this.getAtomX$I(atom1), this.getAtomY$I(atom1), this.getAtomX$I(atom2), this.getAtomY$I(atom2));
if (!this.mMol.isSelectedAtom$I(atom1) && !this.mMol.isSelectedAtom$I(atom2) && Long.$ne((Long.$and((Long.$or(this.mMol.getAtomQueryFeatures$I(atom1),this.mMol.getAtomQueryFeatures$I(atom2))),536870912)),0 )  ) p$1.setColor_$I.apply(this, [-8]);
if (this.mAlternativeCoords[atom1] == null ) {
theLine.x1=this.getAtomX$I(atom1);
theLine.y1=this.getAtomY$I(atom1);
} else {
theLine.x1=this.mAlternativeCoords[atom1].x;
theLine.y1=this.mAlternativeCoords[atom1].y;
}if (this.mAlternativeCoords[atom2] == null ) {
theLine.x2=this.getAtomX$I(atom2);
theLine.y2=this.getAtomY$I(atom2);
} else {
theLine.x2=this.mAlternativeCoords[atom2].x;
theLine.y2=this.mAlternativeCoords[atom2].y;
}if ((this.mMol.getBondQueryFeatures$I(bnd) & 130560) != 0) {
p$1.mpHandleDottedLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [theLine, atom1, atom2]);
p$1.setColor_$I.apply(this, [-9]);
return;
}var bondOrder=(this.mMol.getBondType$I(bnd) == 64) ? 0 : (this.mMol.getBondType$I(bnd) == 32) ? 1 : this.mMol.getBondOrder$I(bnd);
switch (bondOrder) {
case 1:
var bondType=this.mMol.getBondType$I(bnd);
if ((this.mDisplayMode & 128) != 0 && (bondType == 257 || bondType == 129 ) ) {
var stereoCenter=this.mMol.getBondAtom$I$I(0, bnd);
var esrType=this.mMol.getAtomESRType$I(stereoCenter);
if (esrType != 0) {
var esrGroup=this.mMol.getAtomESRGroup$I(stereoCenter);
var count=0;
for (var atom=0; atom < this.mMol.getAtoms$(); atom++) if (this.mMol.getAtomESRType$I(atom) == esrType && this.mMol.getAtomESRGroup$I(atom) == esrGroup ) ++count;

if (count == 1) bondType=1;
}}switch (bondType) {
case 1:
p$1.mpHandleLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [theLine, atom1, atom2]);
break;
case 257:
p$1.mpHandleWedge$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [theLine, atom1, atom2]);
break;
case 129:
var xdiff=theLine.x2 - theLine.x1;
var ydiff=theLine.y2 - theLine.y1;
var color1;
var color2;
if (this.mMol.isBondForegroundHilited$I(this.mMol.getBond$I$I(atom1, atom2))) {
color1=-3;
color2=-3;
} else {
color1=this.mAtomColor[atom1];
color2=p$1.getESRColor$I.apply(this, [atom1]);
if (color1 == this.mMol.getAtomColor$I(atom1)) color1=color2;
}for (var i=2; i < 17; i+=2) {
aLine.x1=theLine.x1 + i * xdiff / 17 - i * ydiff / 128;
aLine.y1=theLine.y1 + i * ydiff / 17 + i * xdiff / 128;
aLine.x2=theLine.x1 + i * xdiff / 17 + i * ydiff / 128;
aLine.y2=theLine.y1 + i * ydiff / 17 - i * xdiff / 128;
if (p$1.mpProperLine$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [aLine])) {
p$1.setColor_$I.apply(this, [(i < 9) ? color1 : color2]);
this.drawBlackLine$com_actelion_research_chem_AbstractDepictor_DepictorLine(aLine);
p$1.setColor_$I.apply(this, [this.mStandardForegroundColor]);
}}
break;
case 32:
p$1.mpHandleShortDashedLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [theLine, atom1, atom2]);
break;
}
break;
case 0:
case 2:
if ((this.mAtomLabelDisplayed[atom1] || this.mMol.getAtomPi$I(atom1) == 2 ) && (this.mAtomLabelDisplayed[atom2] || this.mMol.getAtomPi$I(atom2) == 2 ) && !this.mMol.isRingBond$I(bnd) && bondOrder == 2  ) {
if (!p$1.mpProperLine$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine])) break;
p$1.mpCalcPiBondOffset$D$D$com_actelion_research_gui_generic_GenericPoint.apply(this, [theLine.x2 - theLine.x1, theLine.y2 - theLine.y1, piBondOffset]);
var xdiff=piBondOffset.x / 2;
var ydiff=piBondOffset.y / 2;
aLine.x1=theLine.x1 + xdiff;
aLine.y1=theLine.y1 + ydiff;
aLine.x2=theLine.x2 + xdiff;
aLine.y2=theLine.y2 + ydiff;
bLine.x1=theLine.x1 - xdiff;
bLine.y1=theLine.y1 - ydiff;
bLine.x2=theLine.x2 - xdiff;
bLine.y2=theLine.y2 - ydiff;
if (this.mMol.getBondType$I(bnd) == 386) p$1.mpMakeCrossBond$com_actelion_research_chem_AbstractDepictor_DepictorLine$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [aLine, bLine]);
p$1.drawLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [aLine, atom1, atom2]);
if (bondOrder == 2) p$1.drawLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [bLine, atom1, atom2]);
 else p$1.drawDashedLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [bLine, atom1, atom2]);
} else if ((this.mAtomLabelDisplayed[atom2] || this.mMol.getAtomPi$I(atom2) == 2 ) && bondOrder == 2 ) {
p$1.mpDBFromNonLabelToLabel$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$Z.apply(this, [theLine, bnd, false]);
} else if ((this.mAtomLabelDisplayed[atom1] || this.mMol.getAtomPi$I(atom1) == 2 ) && bondOrder == 2 ) {
p$1.mpDBFromNonLabelToLabel$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$Z.apply(this, [theLine, bnd, true]);
} else {
var side=p$1.mpPreferredSide$I.apply(this, [bnd]);
if (side == 0) side=1;
aLine.x1=theLine.x1;
aLine.y1=theLine.y1;
aLine.x2=theLine.x2;
aLine.y2=theLine.y2;
p$1.mpCalcPiBondOffset$D$D$com_actelion_research_gui_generic_GenericPoint.apply(this, [theLine.x2 - theLine.x1, theLine.y2 - theLine.y1, piBondOffset]);
if (side > 0) {
bLine.x1=theLine.x1 + piBondOffset.x;
bLine.y1=theLine.y1 + piBondOffset.y;
bLine.x2=theLine.x2 + piBondOffset.x;
bLine.y2=theLine.y2 + piBondOffset.y;
if (p$1.mpCalcNextBondOffset$I$I$I$com_actelion_research_gui_generic_GenericPoint.apply(this, [atom1, atom2, 1, nextBondOffset]) || (this.mMol.getConnAtoms$I(atom1) > 1) ) {
bLine.x1+=nextBondOffset.x + piBondOffset.y;
bLine.y1+=nextBondOffset.y - piBondOffset.x;
}if (p$1.mpCalcNextBondOffset$I$I$I$com_actelion_research_gui_generic_GenericPoint.apply(this, [atom2, atom1, -1, nextBondOffset]) || (this.mMol.getConnAtoms$I(atom2) > 1) ) {
bLine.x2+=nextBondOffset.x - piBondOffset.y;
bLine.y2+=nextBondOffset.y + piBondOffset.x;
}} else {
bLine.x1=theLine.x1 - piBondOffset.x;
bLine.y1=theLine.y1 - piBondOffset.y;
bLine.x2=theLine.x2 - piBondOffset.x;
bLine.y2=theLine.y2 - piBondOffset.y;
if (p$1.mpCalcNextBondOffset$I$I$I$com_actelion_research_gui_generic_GenericPoint.apply(this, [atom1, atom2, -1, nextBondOffset]) || (this.mMol.getConnAtoms$I(atom1) > 1) ) {
bLine.x1+=nextBondOffset.x + piBondOffset.y;
bLine.y1+=nextBondOffset.y - piBondOffset.x;
}if (p$1.mpCalcNextBondOffset$I$I$I$com_actelion_research_gui_generic_GenericPoint.apply(this, [atom2, atom1, 1, nextBondOffset]) || (this.mMol.getConnAtoms$I(atom2) > 1) ) {
bLine.x2+=nextBondOffset.x - piBondOffset.y;
bLine.y2+=nextBondOffset.y + piBondOffset.x;
}}if (this.mMol.getBondType$I(bnd) == 386) p$1.mpMakeCrossBond$com_actelion_research_chem_AbstractDepictor_DepictorLine$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [aLine, bLine]);
p$1.mpHandleLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [aLine, atom1, atom2]);
if (bondOrder == 2) p$1.mpHandleLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [bLine, atom1, atom2]);
 else p$1.mpHandleDashedLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [bLine, atom1, atom2]);
}break;
case 3:
if (p$1.mpProperLine$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine])) {
p$1.drawLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [theLine, atom1, atom2]);
p$1.mpCalcPiBondOffset$D$D$com_actelion_research_gui_generic_GenericPoint.apply(this, [theLine.x2 - theLine.x1, theLine.y2 - theLine.y1, piBondOffset]);
p$1.drawOffsetLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I$D$D$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine, atom1, atom2, piBondOffset.x, piBondOffset.y, aLine]);
p$1.drawOffsetLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I$D$D$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine, atom1, atom2, -piBondOffset.x, -piBondOffset.y, aLine]);
}break;
case 4:
if (p$1.mpProperLine$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine])) {
p$1.mpCalcPiBondOffset$D$D$com_actelion_research_gui_generic_GenericPoint.apply(this, [theLine.x2 - theLine.x1, theLine.y2 - theLine.y1, piBondOffset]);
p$1.drawOffsetLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I$D$D$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine, atom1, atom2, 1.5 * piBondOffset.x, 1.5 * piBondOffset.y, aLine]);
p$1.drawOffsetLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I$D$D$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine, atom1, atom2, 0.5 * piBondOffset.x, 0.5 * piBondOffset.y, aLine]);
p$1.drawOffsetLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I$D$D$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine, atom1, atom2, -0.5 * piBondOffset.x, -0.5 * piBondOffset.y, aLine]);
p$1.drawOffsetLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I$D$D$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine, atom1, atom2, -1.5 * piBondOffset.x, -1.5 * piBondOffset.y, aLine]);
}break;
case 5:
if (p$1.mpProperLine$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine])) {
p$1.drawLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [theLine, atom1, atom2]);
p$1.mpCalcPiBondOffset$D$D$com_actelion_research_gui_generic_GenericPoint.apply(this, [theLine.x2 - theLine.x1, theLine.y2 - theLine.y1, piBondOffset]);
p$1.drawOffsetLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I$D$D$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine, atom1, atom2, 2 * piBondOffset.x, 2 * piBondOffset.y, aLine]);
p$1.drawOffsetLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I$D$D$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine, atom1, atom2, piBondOffset.x, piBondOffset.y, aLine]);
p$1.drawOffsetLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I$D$D$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine, atom1, atom2, -piBondOffset.x, -piBondOffset.y, aLine]);
p$1.drawOffsetLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I$D$D$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine, atom1, atom2, -2 * piBondOffset.x, -2 * piBondOffset.y, aLine]);
}break;
}
if (this.mCurrentColor == -8) p$1.setColor_$I.apply(this, [-9]);
}, p$1);

Clazz.newMeth(C$, 'drawOffsetLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I$D$D$com_actelion_research_chem_AbstractDepictor_DepictorLine',  function (theLine, atom1, atom2, dx, dy, aLine) {
aLine.x1=theLine.x1 + dx;
aLine.y1=theLine.y1 + dy;
aLine.x2=theLine.x2 + dx;
aLine.y2=theLine.y2 + dy;
p$1.drawLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [aLine, atom1, atom2]);
}, p$1);

Clazz.newMeth(C$, 'mpDBFromNonLabelToLabel$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$Z',  function (theLine, bnd, inverted) {
var aLine=Clazz.new_($I$(7,1));
var bLine=Clazz.new_($I$(7,1));
var piBondOffset=Clazz.new_($I$(4,1));
var nextBondOffset=Clazz.new_($I$(4,1));
var atm1=this.mMol.getBondAtom$I$I(0, bnd);
var atm2=this.mMol.getBondAtom$I$I(1, bnd);
if (inverted) {
var td=theLine.x1;
theLine.x1=theLine.x2;
theLine.x2=td;
td=theLine.y1;
theLine.y1=theLine.y2;
theLine.y2=td;
var ti=atm1;
atm1=atm2;
atm2=ti;
}if (!p$1.mpProperLine$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine])) return;
if (this.mMol.isRingBond$I(bnd)) {
aLine.x1=theLine.x1;
aLine.y1=theLine.y1;
aLine.x2=theLine.x2;
aLine.y2=theLine.y2;
var side=(inverted) ? -p$1.mpPreferredSide$I.apply(this, [bnd]) : p$1.mpPreferredSide$I.apply(this, [bnd]);
if (side == 0) side=1;
p$1.mpCalcPiBondOffset$D$D$com_actelion_research_gui_generic_GenericPoint.apply(this, [theLine.x2 - theLine.x1, theLine.y2 - theLine.y1, piBondOffset]);
if (side > 0) {
bLine.x1=theLine.x1 + piBondOffset.x;
bLine.y1=theLine.y1 + piBondOffset.y;
bLine.x2=theLine.x2 + piBondOffset.x;
bLine.y2=theLine.y2 + piBondOffset.y;
if (p$1.mpCalcNextBondOffset$I$I$I$com_actelion_research_gui_generic_GenericPoint.apply(this, [atm1, atm2, 1, nextBondOffset]) || (this.mMol.getConnAtoms$I(atm1) > 1) ) {
bLine.x1+=nextBondOffset.x + piBondOffset.y;
bLine.y1+=nextBondOffset.y - piBondOffset.x;
}} else {
bLine.x1=theLine.x1 - piBondOffset.x;
bLine.y1=theLine.y1 - piBondOffset.y;
bLine.x2=theLine.x2 - piBondOffset.x;
bLine.y2=theLine.y2 - piBondOffset.y;
if (p$1.mpCalcNextBondOffset$I$I$I$com_actelion_research_gui_generic_GenericPoint.apply(this, [atm1, atm2, -1, nextBondOffset]) || (this.mMol.getConnAtoms$I(atm1) > 1) ) {
bLine.x1+=nextBondOffset.x + piBondOffset.y;
bLine.y1+=nextBondOffset.y - piBondOffset.x;
}}if (this.mMol.getBondType$I(bnd) == 386) p$1.mpMakeCrossBond$com_actelion_research_chem_AbstractDepictor_DepictorLine$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [aLine, bLine]);
p$1.mpHandleLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [aLine, atm1, atm2]);
if (this.mMol.getBondType$I(bnd) == 64) p$1.mpHandleDashedLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [bLine, atm1, atm2]);
 else p$1.mpHandleLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [bLine, atm1, atm2]);
} else {
p$1.mpCalcPiBondOffset$D$D$com_actelion_research_gui_generic_GenericPoint.apply(this, [theLine.x2 - theLine.x1, theLine.y2 - theLine.y1, piBondOffset]);
var xdiff=piBondOffset.x / 2;
var ydiff=piBondOffset.y / 2;
var aLineIsInnerLine=false;
aLine.x1=theLine.x1 + xdiff;
aLine.y1=theLine.y1 + ydiff;
aLine.x2=theLine.x2 + xdiff;
aLine.y2=theLine.y2 + ydiff;
if (this.mMol.getConnAtoms$I(atm1) > 1) {
if (!p$1.mpCalcNextBondOffset$I$I$I$com_actelion_research_gui_generic_GenericPoint.apply(this, [atm1, atm2, 1, nextBondOffset])) {
this.mAlternativeCoords[atm1]=Clazz.new_($I$(4,1).c$$D$D,[aLine.x1, aLine.y1]);
} else {
aLine.x1+=nextBondOffset.x;
aLine.y1+=nextBondOffset.y;
if (this.mMol.getConnAtoms$I(atm1) == 2) {
if (nextBondOffset.x != 0  || nextBondOffset.y != 0  ) {
aLine.x1+=piBondOffset.y;
aLine.y1-=piBondOffset.x;
}}}}bLine.x1=theLine.x1 - xdiff;
bLine.y1=theLine.y1 - ydiff;
bLine.x2=theLine.x2 - xdiff;
bLine.y2=theLine.y2 - ydiff;
if (this.mMol.getConnAtoms$I(atm1) > 1) {
if (!p$1.mpCalcNextBondOffset$I$I$I$com_actelion_research_gui_generic_GenericPoint.apply(this, [atm1, atm2, 0, nextBondOffset])) {
this.mAlternativeCoords[atm1]=Clazz.new_($I$(4,1).c$$D$D,[bLine.x1, bLine.y1]);
aLineIsInnerLine=true;
} else {
bLine.x1+=nextBondOffset.x;
bLine.y1+=nextBondOffset.y;
if (this.mMol.getConnAtoms$I(atm1) == 2) {
if (nextBondOffset.x != 0  || nextBondOffset.y != 0  ) {
bLine.x1+=piBondOffset.y;
bLine.y1-=piBondOffset.x;
}}}}if (this.mMol.getBondType$I(bnd) == 386) p$1.mpMakeCrossBond$com_actelion_research_chem_AbstractDepictor_DepictorLine$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [aLine, bLine]);
if (this.mMol.getBondType$I(bnd) == 64) {
if (aLineIsInnerLine) {
p$1.drawDashedLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [aLine, atm1, atm2]);
p$1.drawLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [bLine, atm1, atm2]);
} else {
p$1.drawLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [aLine, atm1, atm2]);
p$1.drawDashedLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [bLine, atm1, atm2]);
}} else {
p$1.drawLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [aLine, atm1, atm2]);
p$1.drawLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [bLine, atm1, atm2]);
}}}, p$1);

Clazz.newMeth(C$, 'mpMakeCrossBond$com_actelion_research_chem_AbstractDepictor_DepictorLine$com_actelion_research_chem_AbstractDepictor_DepictorLine',  function (aLine, bLine) {
var temp;
temp=aLine.x2;
aLine.x2=bLine.x2;
bLine.x2=temp;
temp=aLine.y2;
aLine.y2=bLine.y2;
bLine.y2=temp;
}, p$1);

Clazz.newMeth(C$, 'mpCalcPiBondOffset$D$D$com_actelion_research_gui_generic_GenericPoint',  function (dx, dy, piBondOffset) {
if (dx == 0 ) {
if (dy < 0 ) piBondOffset.x=this.mpBondSpacing;
 else piBondOffset.x=-this.mpBondSpacing;
piBondOffset.y=0;
return;
}var alpha=Math.atan(dy / dx);
if (dx < 0 ) alpha+=3.141592653589793;
piBondOffset.x=-(this.mpBondSpacing * Math.sin(alpha));
piBondOffset.y=(this.mpBondSpacing * Math.cos(alpha));
}, p$1);

Clazz.newMeth(C$, 'mpProperLine$com_actelion_research_chem_AbstractDepictor_DepictorLine',  function (theLine) {
var endsExchanged;
var retval;
if (theLine.x1 == theLine.x2  && theLine.y1 == theLine.y2  ) {
for (var tabuZone, $tabuZone = this.mpTabuZone.iterator$(); $tabuZone.hasNext$()&&((tabuZone=($tabuZone.next$())),1);) if (tabuZone.contains$D$D(theLine.x1, theLine.y1)) return false;

return true;
}var theFrame=p$1.mpGetFrame$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine]);
endsExchanged=false;
if (theLine.x1 > theLine.x2 ) {
p$1.mpExchangeLineEnds$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine]);
endsExchanged=true;
}for (var i=0; i < this.mpTabuZone.size$(); i++) {
var tabuZone=this.mpTabuZone.get$I(i);
if (tabuZone.x > theFrame.x + theFrame.width  || tabuZone.y > theFrame.y + theFrame.height   || theFrame.x > tabuZone.x + tabuZone.width   || theFrame.y > tabuZone.y + tabuZone.height  ) continue;
if (p$1.mpInTabuZone$D$D$I.apply(this, [theLine.x1, theLine.y1, i])) {
if (p$1.mpInTabuZone$D$D$I.apply(this, [theLine.x2, theLine.y2, i])) {
if (endsExchanged) p$1.mpExchangeLineEnds$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine]);
return false;
}p$1.mpShortenLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [theLine, 0, i]);
retval=p$1.mpProperLine$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine]);
if (endsExchanged) p$1.mpExchangeLineEnds$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine]);
return retval;
}if (p$1.mpInTabuZone$D$D$I.apply(this, [theLine.x2, theLine.y2, i])) {
p$1.mpShortenLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [theLine, 1, i]);
retval=p$1.mpProperLine$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine]);
if (endsExchanged) p$1.mpExchangeLineEnds$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine]);
return retval;
}}
if (endsExchanged) p$1.mpExchangeLineEnds$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine]);
return true;
}, p$1);

Clazz.newMeth(C$, 'mpCalcNextBondOffset$I$I$I$com_actelion_research_gui_generic_GenericPoint',  function (atm1, atm2, side, nextBondOffset) {
var RO_LIMIT=2.617993878;
var LO_LIMIT=3.665191429;
var RI_LIMIT=0.523598776;
var LI_LIMIT=5.759586531;
var retval;
var i;
var remoteAtm;
var bnd;
var bondAngle;
var theBondAngle;
var testAngle;
var angleDiff;
var currentAngleDiff;
var distance;
retval=false;
nextBondOffset.x=0;
nextBondOffset.y=0;
if (side > 0) angleDiff=2.617993878;
 else angleDiff=3.665191429;
theBondAngle=this.mMol.getBondAngle$I$I(atm1, atm2);
for (i=0; i < this.mMol.getConnAtoms$I(atm1); i++) {
bnd=this.mMol.getConnBond$I$I(atm1, i);
bondAngle=theBondAngle;
if (this.mMol.getBondAtom$I$I(0, bnd) == atm1) remoteAtm=this.mMol.getBondAtom$I$I(1, bnd);
 else remoteAtm=this.mMol.getBondAtom$I$I(0, bnd);
if (remoteAtm == atm2) continue;
testAngle=this.mMol.getBondAngle$I$I(atm1, remoteAtm);
if (bondAngle < testAngle ) bondAngle+=6.283185307179586;
currentAngleDiff=bondAngle - testAngle;
if (side > 0) {
if (currentAngleDiff < 3.141592653589793 ) retval=true;
if (currentAngleDiff > 2.617993878 ) currentAngleDiff=2.617993878;
if (currentAngleDiff < 0.523598776 ) currentAngleDiff=0.523598776;
if (currentAngleDiff <= angleDiff ) {
angleDiff=currentAngleDiff;
distance=this.mpBondSpacing * Math.tan(angleDiff - 1.5707963267948966) / 2;
nextBondOffset.x=-(distance * Math.sin(bondAngle));
nextBondOffset.y=-(distance * Math.cos(bondAngle));
}} else {
if (currentAngleDiff >= 3.141592653589793 ) retval=true;
if (currentAngleDiff < 3.665191429 ) currentAngleDiff=3.665191429;
if (currentAngleDiff > 5.759586531 ) currentAngleDiff=5.759586531;
if (currentAngleDiff >= angleDiff ) {
angleDiff=currentAngleDiff;
distance=this.mpBondSpacing * Math.tan(4.712388981 - angleDiff) / 2;
nextBondOffset.x=-(distance * Math.sin(bondAngle));
nextBondOffset.y=-(distance * Math.cos(bondAngle));
}}}
return retval;
}, p$1);

Clazz.newMeth(C$, 'mpExchangeLineEnds$com_actelion_research_chem_AbstractDepictor_DepictorLine',  function (theLine) {
var temp;
temp=theLine.x1;
theLine.x1=theLine.x2;
theLine.x2=temp;
temp=theLine.y1;
theLine.y1=theLine.y2;
theLine.y2=temp;
}, p$1);

Clazz.newMeth(C$, 'mpHandleLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I',  function (theLine, atm1, atm2) {
if (p$1.mpProperLine$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine])) p$1.drawLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [theLine, atm1, atm2]);
}, p$1);

Clazz.newMeth(C$, 'mpHandleDashedLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I',  function (theLine, atm1, atm2) {
if (p$1.mpProperLine$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine])) p$1.drawDashedLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [theLine, atm1, atm2]);
}, p$1);

Clazz.newMeth(C$, 'mpHandleShortDashedLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I',  function (theLine, atm1, atm2) {
if (p$1.mpProperLine$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine])) p$1.drawShortDashedLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [theLine, atm1, atm2]);
}, p$1);

Clazz.newMeth(C$, 'mpHandleDottedLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I',  function (theLine, atm1, atm2) {
if (p$1.mpProperLine$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theLine])) this.drawDottedLine$com_actelion_research_chem_AbstractDepictor_DepictorLine(theLine);
}, p$1);

Clazz.newMeth(C$, 'mpHandleWedge$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I',  function (origWedge, atm1, atm2) {
var theWedge=Clazz.new_($I$(7,1));
if (origWedge.x1 == origWedge.x2  && origWedge.y1 == origWedge.y2  ) return;
theWedge.x1=origWedge.x1;
theWedge.y1=origWedge.y1;
theWedge.x2=origWedge.x2;
theWedge.y2=origWedge.y2;
var theFrame=p$1.mpGetFrame$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [theWedge]);
for (var i=0; i < this.mpTabuZone.size$(); i++) {
var tabuZone=this.mpTabuZone.get$I(i);
if (tabuZone.x > theFrame.x + theFrame.width  || tabuZone.y > theFrame.y + theFrame.height   || theFrame.x > tabuZone.x + tabuZone.width   || theFrame.y > tabuZone.y + tabuZone.height  ) continue;
if (p$1.mpInTabuZone$D$D$I.apply(this, [theWedge.x1, theWedge.y1, i])) {
if (p$1.mpInTabuZone$D$D$I.apply(this, [theWedge.x2, theWedge.y2, i])) return;
p$1.mpShortenLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [theWedge, 0, i]);
p$1.mpHandleWedge$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [theWedge, atm1, atm2]);
return;
}if (p$1.mpInTabuZone$D$D$I.apply(this, [theWedge.x2, theWedge.y2, i])) {
p$1.mpShortenLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [theWedge, 1, i]);
p$1.mpHandleWedge$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [theWedge, atm1, atm2]);
return;
}}
p$1.drawWedge$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [theWedge, atm1, atm2]);
}, p$1);

Clazz.newMeth(C$, 'mpGetFrame$com_actelion_research_chem_AbstractDepictor_DepictorLine',  function (theLine) {
var theFrame=Clazz.new_($I$(1,1));
if (theLine.x1 <= theLine.x2 ) {
theFrame.x=theLine.x1;
theFrame.width=theLine.x2 - theLine.x1;
} else {
theFrame.x=theLine.x2;
theFrame.width=theLine.x1 - theLine.x2;
}if (theLine.y1 <= theLine.y2 ) {
theFrame.y=theLine.y1;
theFrame.height=theLine.y2 - theLine.y1;
} else {
theFrame.y=theLine.y2;
theFrame.height=theLine.y1 - theLine.y2;
}return theFrame;
}, p$1);

Clazz.newMeth(C$, 'mpInTabuZone$D$D$I',  function (x, y, tabuZoneNo) {
if ((this.mDisplayMode & 1) != 0) return false;
var tabuZone=this.mpTabuZone.get$I(tabuZoneNo);
return (x > tabuZone.x  && x < tabuZone.x + tabuZone.width   && y > tabuZone.y   && y < tabuZone.y + tabuZone.height  );
}, p$1);

Clazz.newMeth(C$, 'mpShortenLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I',  function (theLine, pointNo, tabuZoneNo) {
var x1;
var y1;
var x2;
var y2;
var dx;
var dy;
var tabuX;
var tabuY;
var sx;
var sy;
if (pointNo == 0) {
x1=theLine.x1;
y1=theLine.y1;
x2=theLine.x2;
y2=theLine.y2;
} else {
x1=theLine.x2;
y1=theLine.y2;
x2=theLine.x1;
y2=theLine.y1;
}var tabuZone=this.mpTabuZone.get$I(tabuZoneNo);
tabuX=(x2 > x1 ) ? tabuZone.x + tabuZone.width : tabuZone.x;
tabuY=(y2 > y1 ) ? tabuZone.y + tabuZone.height : tabuZone.y;
dx=x2 - x1;
dy=y2 - y1;
if (Math.abs(dx) > Math.abs(dy) ) {
if (y1 == y2 ) {
sx=tabuX;
sy=y1;
} else {
sx=x1 + dx * (tabuY - y1) / dy;
if ((x2 > x1 ) == (tabuX > sx ) ) {
sy=tabuY;
} else {
sx=tabuX;
sy=y1 + dy * (tabuX - x1) / dx;
}}} else {
if (x1 == x2 ) {
sx=x1;
sy=tabuY;
} else {
sy=y1 + dy * (tabuX - x1) / dx;
if ((y2 > y1 ) == (tabuY > sy ) ) {
sx=tabuX;
} else {
sx=x1 + dx * (tabuY - y1) / dy;
sy=tabuY;
}}}if (pointNo == 0) {
theLine.x1=sx;
theLine.y1=sy;
} else {
theLine.x2=sx;
theLine.y2=sy;
}}, p$1);

Clazz.newMeth(C$, 'mpPreferredSide$I',  function (bnd) {
var isAromatic=Clazz.array(Boolean.TYPE, [16]);
var isInRing=Clazz.array(Boolean.TYPE, [16]);
var angle=Clazz.array(Double.TYPE, [16]);
var bondAngle=Clazz.array(Double.TYPE, [2]);
var angles=0;
for (var i=0; i < 2; i++) {
var atm=this.mMol.getBondAtom$I$I(i, bnd);
for (var j=0; j < this.mMol.getConnAtoms$I(atm); j++) {
var connBond=this.mMol.getConnBond$I$I(atm, j);
if (connBond == bnd) continue;
if (angles == 4) return 0;
isAromatic[angles]=this.mMol.isAromaticBond$I(connBond);
isInRing[angles]=this.mMol.isRingBond$I(connBond);
angle[angles++]=this.mMol.getBondAngle$I$I(atm, this.mMol.getConnAtom$I$I(atm, j));
}
}
var changed;
bondAngle[0]=this.mMol.getBondAngle$I$I(this.mMol.getBondAtom$I$I(0, bnd), this.mMol.getBondAtom$I$I(1, bnd));
if (bondAngle[0] < 0 ) {
bondAngle[1]=bondAngle[0] + 3.141592653589793;
changed=false;
} else {
bondAngle[1]=bondAngle[0];
bondAngle[0]=bondAngle[1] - 3.141592653589793;
changed=true;
}var side=0;
for (var i=0; i < angles; i++) {
var value;
if (isAromatic[i]) value=20;
 else if (isInRing[i]) value=17;
 else value=16;
if ((angle[i] > bondAngle[0] ) && (angle[i] < bondAngle[1] ) ) side-=value;
 else side+=value;
}
return (changed) ? -side : side;
}, p$1);

Clazz.newMeth(C$, 'mpDrawAtom$I$IAA',  function (atom, esrGroupMemberCount) {
var chax;
var chay;
var xdiff;
var ydiff;
var x;
var y;
if (!this.mIsValidatingView) this.onDrawAtom$I$S$D$D(atom, this.mMol.getAtomLabel$I(atom), this.getAtomX$I(atom), this.getAtomY$I(atom));
var propStr=null;
if (this.mMol.getAtomCharge$I(atom) != 0) {
var valStr=(Math.abs(this.mMol.getAtomCharge$I(atom)) == 1) ? "" : String.valueOf$I(Math.abs(this.mMol.getAtomCharge$I(atom)));
propStr=(this.mMol.getAtomCharge$I(atom) < 0) ? valStr + "-" : valStr + "+";
}if (this.mAtomText != null  && (atom < this.mAtomText.length)  && this.mAtomText[atom] != null   && this.mAtomText[atom].length$() > 0 ) propStr=p$1.append$S$S.apply(this, [propStr, this.mAtomText[atom]]);
var isoStr=null;
var queryFeatures=this.mMol.getAtomQueryFeatures$I(atom);
if (Long.$ne(queryFeatures,0 )) {
if (Long.$ne((Long.$and(queryFeatures,17592186044416)),0 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "*"]);
if (Long.$ne((Long.$and(queryFeatures,35184372088832)),0 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "!*"]);
if (Long.$ne((Long.$and(queryFeatures,70368744177664)),0 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "ha"]);
 else if (Long.$ne((Long.$and(queryFeatures,2)),0 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "a"]);
 else if (Long.$ne((Long.$and(queryFeatures,4)),0 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "!a"]);
if (Long.$ne((Long.$and(queryFeatures,4096)),0 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "s"]);
if (Long.$ne((Long.$and(queryFeatures,1920)),0 )) {
var hydrogens=(Long.$and(queryFeatures,1920));
if (Long.$eq(hydrogens,1792 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "h0"]);
 else if (Long.$eq(hydrogens,1664 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "h1"]);
 else if (Long.$eq(hydrogens,1408 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "h2"]);
 else if (Long.$eq(hydrogens,128 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "h>0"]);
 else if (Long.$eq(hydrogens,384 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "h>1"]);
 else if (Long.$eq(hydrogens,896 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "h>2"]);
 else if (Long.$eq(hydrogens,1024 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "h<3"]);
 else if (Long.$eq(hydrogens,1536 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "h<2"]);
}if (Long.$ne((Long.$and(queryFeatures,234881024)),0 )) {
var charge=(Long.$and(queryFeatures,234881024));
if (Long.$eq(charge,167772160 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "c0"]);
 else if (Long.$eq(charge,100663296 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "c+"]);
 else if (Long.$eq(charge,201326592 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "c-"]);
}if (Long.$ne((Long.$and(queryFeatures,114688)),0 )) {
var piElectrons=(Long.$and(queryFeatures,114688));
if (Long.$eq(piElectrons,98304 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "pi0"]);
 else if (Long.$eq(piElectrons,81920 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "pi1"]);
 else if (Long.$eq(piElectrons,49152 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "pi2"]);
 else if (Long.$eq(piElectrons,16384 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "pi>0"]);
}if (Long.$ne((Long.$and(queryFeatures,4063232)),0 )) {
var neighbours=(Long.$and(queryFeatures,4063232));
if (Long.$eq(neighbours,(3801088) )) isoStr=p$1.append$S$S.apply(this, [isoStr, "n1"]);
 else if (Long.$eq(neighbours,(3538944) )) isoStr=p$1.append$S$S.apply(this, [isoStr, "n2"]);
 else if (Long.$eq(neighbours,(3014656) )) isoStr=p$1.append$S$S.apply(this, [isoStr, "n3"]);
 else if (Long.$eq(neighbours,3145728 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "n<3"]);
 else if (Long.$eq(neighbours,2097152 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "n<4"]);
 else if (Long.$eq(neighbours,393216 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "n>1"]);
 else if (Long.$eq(neighbours,917504 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "n>2"]);
 else if (Long.$eq(neighbours,(1966080) )) isoStr=p$1.append$S$S.apply(this, [isoStr, "n>3"]);
}if (Long.$ne((Long.$and(queryFeatures,17042430230528)),0 )) {
var eNegNeighbours=(Long.$and(queryFeatures,17042430230528));
if (Long.$eq(eNegNeighbours,(16492674416640) )) isoStr=p$1.append$S$S.apply(this, [isoStr, "e0"]);
 else if (Long.$eq(eNegNeighbours,(15942918602752) )) isoStr=p$1.append$S$S.apply(this, [isoStr, "e1"]);
 else if (Long.$eq(eNegNeighbours,(14843406974976) )) isoStr=p$1.append$S$S.apply(this, [isoStr, "e2"]);
 else if (Long.$eq(eNegNeighbours,(12644383719424) )) isoStr=p$1.append$S$S.apply(this, [isoStr, "e3"]);
 else if (Long.$eq(eNegNeighbours,(15393162788864) )) isoStr=p$1.append$S$S.apply(this, [isoStr, "e<2"]);
 else if (Long.$eq(eNegNeighbours,(13194139533312) )) isoStr=p$1.append$S$S.apply(this, [isoStr, "e<3"]);
 else if (Long.$eq(eNegNeighbours,8796093022208 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "e<4"]);
 else if (Long.$eq(eNegNeighbours,549755813888 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "e>0"]);
 else if (Long.$eq(eNegNeighbours,(1649267441664) )) isoStr=p$1.append$S$S.apply(this, [isoStr, "e>1"]);
 else if (Long.$eq(eNegNeighbours,(3848290697216) )) isoStr=p$1.append$S$S.apply(this, [isoStr, "e>2"]);
 else if (Long.$eq(eNegNeighbours,(8246337208320) )) isoStr=p$1.append$S$S.apply(this, [isoStr, "e>3"]);
 else if (Long.$eq(eNegNeighbours,(4947802324992) )) isoStr=p$1.append$S$S.apply(this, [isoStr, "e1-2"]);
 else if (Long.$eq(eNegNeighbours,(9345848836096) )) isoStr=p$1.append$S$S.apply(this, [isoStr, "e1-3"]);
 else if (Long.$eq(eNegNeighbours,(10445360463872) )) isoStr=p$1.append$S$S.apply(this, [isoStr, "e2-3"]);
}if (Long.$ne((Long.$and(queryFeatures,120)),0 )) {
var ringBonds=(Long.$and(queryFeatures,120));
if (Long.$eq(ringBonds,112 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "!r"]);
 else if (Long.$eq(ringBonds,8 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "r"]);
 else if (Long.$eq(ringBonds,96 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "rb<3"]);
 else if (Long.$eq(ringBonds,104 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "rb2"]);
 else if (Long.$eq(ringBonds,88 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "rb3"]);
 else if (Long.$eq(ringBonds,56 )) isoStr=p$1.append$S$S.apply(this, [isoStr, "rb4"]);
}if (Long.$ne((Long.$and(queryFeatures,29360128)),0 )) {
isoStr=p$1.append$S$S.apply(this, [isoStr, "r" + (Long.$s(Long.$sr((Long.$and(queryFeatures,29360128)),22)))]);
}if (Long.$ne((Long.$and(queryFeatures,545460846592)),0 )) {
isoStr=p$1.append$S$S.apply(this, [isoStr, p$1.createRingSizeText$J.apply(this, [queryFeatures])]);
}if (Long.$ne((Long.$and(queryFeatures,268435456)),0 )) {
isoStr=p$1.append$S$S.apply(this, [isoStr, "f"]);
}}if (this.mMol.getAtomMass$I(atom) != 0) {
isoStr=p$1.append$S$S.apply(this, [isoStr, String.valueOf$I(this.mMol.getAtomMass$I(atom))]);
}var unpairedElectrons=0;
if (this.mMol.getAtomRadical$I(atom) != 0) {
switch (this.mMol.getAtomRadical$I(atom)) {
case 16:
propStr=p$1.append$S$S.apply(this, [propStr, "|"]);
break;
case 32:
unpairedElectrons=1;
break;
case 48:
unpairedElectrons=2;
break;
}
}var cipStr=null;
if ((this.mDisplayMode & 64) == 0) {
if (this.mMol.isAtomConfigurationUnknown$I(atom)) cipStr="?";
 else if (this.mMol.getAtomCIPParity$I(atom) != 0) {
if (this.mMol.getAtomESRType$I(atom) == 0 || esrGroupMemberCount == null   || esrGroupMemberCount[this.mMol.getAtomESRType$I(atom)][this.mMol.getAtomESRGroup$I(atom)] > 1 ) {
if (this.mMol.getConnAtoms$I(atom) == 2) {
switch (this.mMol.getAtomCIPParity$I(atom)) {
case 2:
cipStr=this.mMol.isAtomParityPseudo$I(atom) ? "p" : "P";
break;
case 1:
cipStr=this.mMol.isAtomParityPseudo$I(atom) ? "m" : "M";
break;
default:
cipStr="*";
break;
}
} else {
switch (this.mMol.getAtomCIPParity$I(atom)) {
case 1:
cipStr=this.mMol.isAtomParityPseudo$I(atom) ? "r" : "R";
break;
case 2:
cipStr=this.mMol.isAtomParityPseudo$I(atom) ? "s" : "S";
break;
default:
cipStr="*";
break;
}
}}}}if ((this.mDisplayMode & 768) != 0) cipStr=p$1.append$S$S.apply(this, [cipStr, String.valueOf$I(this.mMol.getSymmetryRank$I(atom))]);
var mapStr=null;
if ((this.mDisplayMode & 16) != 0 && this.mMol.getAtomMapNo$I(atom) != 0 ) mapStr="" + this.mMol.getAtomMapNo$I(atom);
var esrStr=null;
if (this.mMol.getStereoBond$I(atom) != -1) {
var esrInfo=p$1.getESRTypeToDisplayAt$I.apply(this, [atom]);
if (esrInfo != -1) esrStr=(esrInfo == 0) ? "abs" : (((esrInfo & 255) == 1) ? "&" : "or") + (1 + (esrInfo >> 8));
}var hydrogensToAdd=0;
if ((this.mDisplayMode & 8192) == 0) {
if (this.mMol.isFragment$()) {
if (Long.$ne((Long.$and(this.mMol.getAtomQueryFeatures$I(atom),2048)),0 )) hydrogensToAdd=this.mMol.getImplicitHydrogens$I(atom);
} else {
if (this.mMol.getAtomicNo$I(atom) != 6 || this.mMol.getAtomMass$I(atom) != 0  || !this.mAtomIsConnected[atom]  || this.mMol.getAtomRadical$I(atom) != 0 ) hydrogensToAdd=this.mMol.getImplicitHydrogens$I(atom);
}}var largeIsoString=false;
var atomStr=this.mMol.getAtomCustomLabel$I(atom);
if (atomStr != null  && atomStr.startsWith$S("]") ) {
isoStr=p$1.append$S$S.apply(this, [atomStr.substring$I(1), isoStr]);
atomStr=null;
largeIsoString=true;
}if (atomStr != null ) {
hydrogensToAdd=0;
} else if (this.mMol.getAtomList$I(atom) != null ) {
var atmStart=(Long.$ne((Long.$and(this.mMol.getAtomQueryFeatures$I(atom),1)),0 )) ? "[!" : "[";
atomStr=atmStart + this.mMol.getAtomListString$I(atom) + "]" ;
if (atomStr.length$() > 5) atomStr=atmStart + this.mMol.getAtomList$I(atom).length + "]" ;
if (Long.$ne((Long.$and(this.mMol.getAtomQueryFeatures$I(atom),2048)),0 )) hydrogensToAdd=-1;
} else if (Long.$ne((Long.$and(this.mMol.getAtomQueryFeatures$I(atom),1)),0 )) {
atomStr="?";
if (Long.$ne((Long.$and(this.mMol.getAtomQueryFeatures$I(atom),2048)),0 )) hydrogensToAdd=-1;
} else if (this.mMol.getAtomicNo$I(atom) != 6 || propStr != null   || isoStr != null   || (hydrogensToAdd > 0)  || !this.mAtomIsConnected[atom] ) atomStr=this.mMol.getAtomLabel$I(atom);
var labelWidth=0.0;
if (!!(!this.mMol.isSelectedAtom$I(atom) & Long.$ne((Long.$and(this.mMol.getAtomQueryFeatures$I(atom),536870912)),0 ))) p$1.setColor_$I.apply(this, [-8]);
if (atomStr != null ) {
labelWidth=this.getStringWidth$S(atomStr);
p$1.mpDrawString$D$D$S$Z.apply(this, [this.getAtomX$I(atom), this.getAtomY$I(atom), atomStr, true]);
this.mAtomLabelDisplayed[atom]=true;
} else if (p$1.mpAlleneCenter$I.apply(this, [atom])) p$1.mpDrawDot$D$D$I.apply(this, [this.getAtomX$I(atom), this.getAtomY$I(atom), atom]);
if (propStr != null ) {
p$1.mpSetSmallLabelSize.apply(this, []);
x=this.getAtomX$I(atom) + ((labelWidth + this.getStringWidth$S(propStr)) / 2.0 + 1);
y=this.getAtomY$I(atom) - (((this.getTextSize$() * 4 - 4)/8|0));
p$1.mpDrawString$D$D$S$Z.apply(this, [x, y, propStr, true]);
p$1.mpSetNormalLabelSize.apply(this, []);
}if ((this.mDisplayMode & 2) != 0) isoStr=String.valueOf$I(atom);
if (isoStr != null ) {
if (largeIsoString) p$1.mpSetReducedLabelSize.apply(this, []);
 else p$1.mpSetSmallLabelSize.apply(this, []);
x=this.getAtomX$I(atom) - ((labelWidth + this.getStringWidth$S(isoStr)) / 2.0);
y=this.getAtomY$I(atom) - (((this.getTextSize$() * 4 - 4)/8|0));
p$1.mpDrawString$D$D$S$Z.apply(this, [x, y, isoStr, true]);
p$1.mpSetNormalLabelSize.apply(this, []);
}if (cipStr != null ) {
p$1.mpSetSmallLabelSize.apply(this, []);
x=this.getAtomX$I(atom) - ((labelWidth + this.getStringWidth$S(cipStr)) / 2.0);
y=this.getAtomY$I(atom) + (((this.getTextSize$() * 4 + 4)/8|0));
var theColor=this.mCurrentColor;
if (this.mMol.getMoleculeColor$() != 1 && (this.mDisplayMode & 4096) == 0 ) p$1.setColor_$I.apply(this, [448]);
p$1.mpDrawString$D$D$S$Z.apply(this, [x, y, cipStr, false]);
p$1.setColor_$I.apply(this, [theColor]);
p$1.mpSetNormalLabelSize.apply(this, []);
}if (mapStr != null ) {
p$1.mpSetSmallLabelSize.apply(this, []);
x=this.getAtomX$I(atom) + ((labelWidth + this.getStringWidth$S(mapStr)) / 2.0 + 1);
y=this.getAtomY$I(atom) + (((this.getTextSize$() * 4 + 4)/8|0));
var theColor=this.mCurrentColor;
p$1.setColor_$I.apply(this, [this.mMol.isAutoMappedAtom$I(atom) ? 384 : 448]);
p$1.mpDrawString$D$D$S$Z.apply(this, [x, y, mapStr, true]);
p$1.setColor_$I.apply(this, [theColor]);
p$1.mpSetNormalLabelSize.apply(this, []);
}if (esrStr != null ) {
var angle=p$1.mpGetFreeSpaceAngle$I.apply(this, [atom]);
p$1.mpSetSmallLabelSize.apply(this, []);
x=this.getAtomX$I(atom) + 0.7 * this.getTextSize$() * Math.sin(angle) ;
y=this.getAtomY$I(atom) + 0.7 * this.getTextSize$() * Math.cos(angle) ;
var theColor=this.mCurrentColor;
if (!this.mIsValidatingView && this.mMol.getMoleculeColor$() != 1 ) p$1.setColor_$I.apply(this, [p$1.getESRColor$I.apply(this, [atom])]);
p$1.mpDrawString$D$D$S$Z.apply(this, [x, y, esrStr, false]);
p$1.setColor_$I.apply(this, [theColor]);
p$1.mpSetNormalLabelSize.apply(this, []);
}if (hydrogensToAdd == 0 && unpairedElectrons == 0 ) {
if (this.mCurrentColor == -8) p$1.setColor_$I.apply(this, [-9]);
return;
}var hindrance=Clazz.array(Double.TYPE, [4]);
for (var i=0; i < this.mMol.getAllConnAtomsPlusMetalBonds$I(atom); i++) {
var bnd=this.mMol.getConnBond$I$I(atom, i);
for (var j=0; j < 2; j++) {
if (this.mMol.getBondAtom$I$I(j, bnd) == atom) {
var theAngle=this.mMol.getBondAngle$I$I(this.mMol.getBondAtom$I$I(j, bnd), this.mMol.getBondAtom$I$I(1 - j, bnd));
if (theAngle < -1.5707963267948966 ) {
hindrance[0]-=(theAngle + 1.5707963267948966);
hindrance[3]+=(theAngle + 3.141592653589793);
} else if (theAngle < 0 ) {
hindrance[2]+=(theAngle + 1.5707963267948966);
hindrance[3]-=theAngle;
} else if (theAngle < 1.5707963267948966 ) {
hindrance[1]+=theAngle;
hindrance[2]+=(1.5707963267948966 - theAngle);
} else {
hindrance[0]+=(theAngle - 1.5707963267948966);
hindrance[1]+=(3.141592653589793 - theAngle);
}}}
}
if (this.mMol.getConnAtoms$I(atom) == 0) {
if (this.mMol.isElectronegative$I(atom)) hindrance[3]-=0.2;
 else hindrance[1]-=0.2;
} else hindrance[1]-=0.1;
if (propStr != null  || mapStr != null  ) hindrance[1]+=10;
if (isoStr != null  || cipStr != null  ) hindrance[3]+=10;
var hNoStr="";
if (hydrogensToAdd != 0) {
var hydrogenWidth=this.getStringWidth$S("H");
var hNoWidth=0.0;
var hHeight=this.getTextSize$();
if (hydrogensToAdd == -1) {
hNoStr="n";
p$1.mpSetSmallLabelSize.apply(this, []);
hNoWidth=this.getStringWidth$S(hNoStr);
} else if (hydrogensToAdd > 1) {
hNoStr=String.valueOf$I(hydrogensToAdd);
p$1.mpSetSmallLabelSize.apply(this, []);
hNoWidth=this.getStringWidth$S(hNoStr);
}if (hindrance[1] < 0.6  || hindrance[3] < 0.6  ) {
chay=this.getAtomY$I(atom);
if (hindrance[1] <= hindrance[3] ) {
hindrance[1]+=10;
chax=this.getAtomX$I(atom) + ((labelWidth + hydrogenWidth) / 2.0);
} else {
hindrance[3]+=10;
chax=this.getAtomX$I(atom) - ((labelWidth + hydrogenWidth) / 2.0) - hNoWidth ;
}} else {
chax=this.getAtomX$I(atom);
if (hindrance[0] < hindrance[2] ) {
hindrance[0]+=10;
chay=this.getAtomY$I(atom) - hHeight;
} else {
hindrance[2]+=10;
chay=this.getAtomY$I(atom) + hHeight;
}}if (hNoWidth > 0 ) {
x=chax + ((hydrogenWidth + hNoWidth) / 2.0);
y=chay + (((this.getTextSize$() * 4 + 4)/8|0));
p$1.mpDrawString$D$D$S$Z.apply(this, [x, y, hNoStr, true]);
p$1.mpSetNormalLabelSize.apply(this, []);
}p$1.mpDrawString$D$D$S$Z.apply(this, [chax, chay, "H", true]);
}var bestSide=0;
if (unpairedElectrons != 0) {
var minHindrance=50.0;
var counterHindrance=0.0;
for (var i=0; i < 4; i++) {
var counterSide=(i > 1) ? i - 2 : i + 2;
if (hindrance[i] < minHindrance ) {
bestSide=i;
minHindrance=hindrance[i];
counterHindrance=hindrance[counterSide];
} else if (hindrance[i] == minHindrance ) {
if (hindrance[counterSide] > counterHindrance ) {
bestSide=i;
counterHindrance=hindrance[counterSide];
}}}
switch (bestSide) {
case 0:
chax=this.getAtomX$I(atom);
chay=this.getAtomY$I(atom) - this.mpDotDiameter - labelWidth / 2 ;
break;
case 1:
chax=this.getAtomX$I(atom) + this.mpDotDiameter + labelWidth / 2 ;
chay=this.getAtomY$I(atom);
break;
case 2:
chax=this.getAtomX$I(atom);
chay=this.getAtomY$I(atom) + this.mpDotDiameter + labelWidth / 2 ;
break;
default:
chax=this.getAtomX$I(atom) - this.mpDotDiameter - labelWidth / 2 ;
chay=this.getAtomY$I(atom);
break;
}
if (unpairedElectrons == 1) {
p$1.mpDrawDot$D$D$I.apply(this, [chax, chay, atom]);
} else {
switch (bestSide) {
case 0:
xdiff=2 * this.mpDotDiameter;
ydiff=0;
chax-=this.mpDotDiameter;
break;
case 1:
xdiff=0;
ydiff=2 * this.mpDotDiameter;
chay-=this.mpDotDiameter;
break;
case 2:
xdiff=2 * this.mpDotDiameter;
ydiff=0;
chax-=this.mpDotDiameter;
break;
default:
xdiff=0;
ydiff=2 * this.mpDotDiameter;
chay-=this.mpDotDiameter;
break;
}
p$1.mpDrawDot$D$D$I.apply(this, [chax, chay, atom]);
p$1.mpDrawDot$D$D$I.apply(this, [chax + xdiff, chay + ydiff, atom]);
}}if (this.mCurrentColor == -8) p$1.setColor_$I.apply(this, [-9]);
}, p$1);

Clazz.newMeth(C$, 'createRingSizeText$J',  function (queryFeatures) {
(queryFeatures=Long.$and(queryFeatures,(545460846592)));
for (var i=0; i < $I$(8).RING_SIZE_VALUES.length; i++) if (Long.$eq(queryFeatures,$I$(8).RING_SIZE_VALUES[i] )) return $I$(8).RING_SIZE_SHORT_TEXT[i];

var customOption=Clazz.new_($I$(9,1).c$$S,["R"]);
if (Long.$ne((Long.$and(queryFeatures,4294967296)),0 )) customOption.append$S("0");
if (Long.$ne((Long.$and(queryFeatures,8589934592)),0 )) customOption.append$S("3");
if (Long.$ne((Long.$and(queryFeatures,17179869184)),0 )) customOption.append$S("4");
if (Long.$ne((Long.$and(queryFeatures,34359738368)),0 )) customOption.append$S("5");
if (Long.$ne((Long.$and(queryFeatures,68719476736)),0 )) customOption.append$S("6");
if (Long.$ne((Long.$and(queryFeatures,137438953472)),0 )) customOption.append$S("7");
if (Long.$ne((Long.$and(queryFeatures,274877906944)),0 )) customOption.append$S("8");
return customOption.toString();
}, p$1);

Clazz.newMeth(C$, 'mpSetNormalLabelSize',  function () {
this.setTextSize$I(this.mpLabelSize);
}, p$1);

Clazz.newMeth(C$, 'mpSetReducedLabelSize',  function () {
this.setTextSize$I(((this.mpLabelSize * 5 + 1)/6|0));
}, p$1);

Clazz.newMeth(C$, 'mpSetSmallLabelSize',  function () {
this.setTextSize$I(((this.mpLabelSize * 2 + 1)/3|0));
}, p$1);

Clazz.newMeth(C$, 'mpGetFreeSpaceAngle$I',  function (atom) {
var angle=Clazz.array(Double.TYPE, [this.mMol.getAllConnAtoms$I(atom)]);
for (var i=0; i < this.mMol.getAllConnAtoms$I(atom); i++) angle[i]=this.mMol.getBondAngle$I$I(atom, this.mMol.getConnAtom$I$I(atom, i));

$I$(10).sort$DA(angle);
var maxMean=p$1.mpGetMeanAngle$DA$I.apply(this, [angle, 0]);
var maxVal=p$1.mpGetAngleESRScore$DA$I$D.apply(this, [angle, 0, maxMean]);
for (var i=1; i < angle.length; i++) {
var mean=p$1.mpGetMeanAngle$DA$I.apply(this, [angle, i]);
var val=p$1.mpGetAngleESRScore$DA$I$D.apply(this, [angle, i, mean]);
if (maxVal < val ) {
maxVal=val;
maxMean=mean;
}}
return maxMean;
}, p$1);

Clazz.newMeth(C$, 'mpGetAngleESRScore$DA$I$D',  function (angleList, index, meanAngle) {
var score=(index == 0) ? 6.283185307179586 + angleList[0] - angleList[angleList.length - 1] : angleList[index] - angleList[index - 1];
if (meanAngle > -2.0943951023931953  && meanAngle < 1.0471975511965976  ) score-=2 * Math.cos(meanAngle + 0.5235987755982988);
 else score-=0.5 * Math.cos(meanAngle + 0.5235987755982988);
return score;
}, p$1);

Clazz.newMeth(C$, 'mpGetMeanAngle$DA$I',  function (angle, index) {
if (index > 0) return (angle[index] + angle[index - 1]) / 2.0;
var mean=3.141592653589793 + (angle[0] + angle[angle.length - 1]) / 2.0;
return (mean > 3.141592653589793 ) ? mean - 6.283185307179586 : mean;
}, p$1);

Clazz.newMeth(C$, 'append$S$S',  function (a, b) {
return (a == null ) ? b : (b == null ) ? a : a + "," + b ;
}, p$1);

Clazz.newMeth(C$, 'mpDrawString$D$D$S$Z',  function (x, y, str, withTabu) {
if (withTabu) {
var strWidth;
var xdiff;
var ydiff;
strWidth=this.getStringWidth$S(str);
xdiff=strWidth / 2 + (this.getTextSize$()/8|0);
ydiff=(this.getTextSize$()/2|0);
if (str === "+"  || str === "-"  ) ydiff=ydiff * 2 / 3;
this.mpTabuZone.add$O(Clazz.new_($I$(1,1).c$$D$D$D$D,[x - xdiff, y - ydiff, 2 * xdiff, 2 * ydiff]));
}if (!this.mIsValidatingView) this.drawString$S$D$D(str, x, y);
}, p$1);

Clazz.newMeth(C$, 'mpDrawDot$D$D$I',  function (x, y, atm) {
this.mpTabuZone.add$O(Clazz.new_($I$(1,1).c$$D$D$D$D,[x - this.mpDotDiameter, y - this.mpDotDiameter, 2 * this.mpDotDiameter, 2 * this.mpDotDiameter]));
if (!this.mIsValidatingView) {
this.mpDot.add$O(Clazz.new_([x, y, p$1.isHighlightedAtom$I.apply(this, [atm]) ? -3 : this.mAtomColor[atm]],$I$(11,1).c$$D$D$I));
}}, p$1);

Clazz.newMeth(C$, 'mpDrawAllDots',  function () {
for (var dot, $dot = this.mpDot.iterator$(); $dot.hasNext$()&&((dot=($dot.next$())),1);) {
p$1.setColor_$I.apply(this, [dot.color]);
this.drawDot$D$D(dot.x, dot.y);
}
p$1.setColor_$I.apply(this, [this.mStandardForegroundColor]);
}, p$1);

Clazz.newMeth(C$, 'mpAlleneCenter$I',  function (atm) {
if (this.mMol.getConnAtoms$I(atm) != 2) return false;
for (var i=0; i < 2; i++) if (this.mMol.getConnBondOrder$I$I(atm, i) != 2) return false;

return true;
}, p$1);

Clazz.newMeth(C$, 'mpDrawBondQueryFeatures',  function () {
var textSizeChanged=false;
for (var bond=0; bond < this.mMol.getBonds$(); bond++) {
var label=null;
if (this.mMol.isBondBridge$I(bond)) {
var minAtoms=this.mMol.getBondBridgeMinSize$I(bond);
var maxAtoms=this.mMol.getBondBridgeMaxSize$I(bond);
label=(minAtoms == maxAtoms) ? "[" + minAtoms + "]"  : "[" + minAtoms + ":" + maxAtoms + "]" ;
} else if ((this.mMol.getBondQueryFeatures$I(bond) & 6291456) != 0) {
label=((this.mMol.getBondQueryFeatures$I(bond) & 6291456) == 2097152) ? "a" : ((this.mMol.getBondQueryFeatures$I(bond) & 384) == 256) ? "r!a" : "!a";
} else if ((this.mMol.getBondQueryFeatures$I(bond) & 384) != 0) {
label=((this.mMol.getBondQueryFeatures$I(bond) & 384) == 256) ? "r" : "!r";
}var ringSize=(this.mMol.getBondQueryFeatures$I(bond) & 917504) >> 17;
if (ringSize != 0) label=((label == null ) ? "" : label) + ringSize;
if (label != null ) {
var atom1=this.mMol.getBondAtom$I$I(0, bond);
var atom2=this.mMol.getBondAtom$I$I(1, bond);
if (!textSizeChanged) {
p$1.mpSetSmallLabelSize.apply(this, []);
textSizeChanged=true;
}var x=(this.getAtomX$I(atom1) + this.getAtomX$I(atom2)) / 2;
var y=(this.getAtomY$I(atom1) + this.getAtomY$I(atom2)) / 2;
var dx=this.getAtomX$I(atom2) - this.getAtomX$I(atom1);
var dy=this.getAtomY$I(atom2) - this.getAtomY$I(atom1);
var d=Math.sqrt(dx * dx + dy * dy);
var hw=0.6 * this.getStringWidth$S(label);
var hh=0.55 * this.getTextSize$();
if (d != 0 ) {
if (dx > 0 ) p$1.mpDrawString$D$D$S$Z.apply(this, [x + hw * dy / d, y - hh * dx / d, label, true]);
 else p$1.mpDrawString$D$D$S$Z.apply(this, [x - hw * dy / d, y + hh * dx / d, label, true]);
}}}
if (textSizeChanged) p$1.mpSetNormalLabelSize.apply(this, []);
}, p$1);

Clazz.newMeth(C$, 'drawLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I',  function (theLine, atom1, atom2) {
if (this.mMol.isBondForegroundHilited$I(this.mMol.getBond$I$I(atom1, atom2))) {
p$1.setColor_$I.apply(this, [-3]);
this.drawBlackLine$com_actelion_research_chem_AbstractDepictor_DepictorLine(theLine);
p$1.setColor_$I.apply(this, [this.mStandardForegroundColor]);
} else if (this.mAtomColor[atom1] != this.mAtomColor[atom2]) {
p$1.drawColorLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I.apply(this, [theLine, atom1, atom2]);
} else if (this.mAtomColor[atom1] != 0) {
p$1.setColor_$I.apply(this, [this.mAtomColor[atom1]]);
this.drawBlackLine$com_actelion_research_chem_AbstractDepictor_DepictorLine(theLine);
p$1.setColor_$I.apply(this, [this.mStandardForegroundColor]);
} else {
this.drawBlackLine$com_actelion_research_chem_AbstractDepictor_DepictorLine(theLine);
}}, p$1);

Clazz.newMeth(C$, 'drawColorLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I',  function (theLine, atm1, atm2) {
var line1=Clazz.new_($I$(7,1));
var line2=Clazz.new_($I$(7,1));
line1.x1=theLine.x1;
line1.y1=theLine.y1;
line1.x2=(theLine.x1 + theLine.x2) / 2;
line1.y2=(theLine.y1 + theLine.y2) / 2;
line2.x1=line1.x2;
line2.y1=line1.y2;
line2.x2=theLine.x2;
line2.y2=theLine.y2;
if (p$1.mpProperLine$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [line1])) {
p$1.setColor_$I.apply(this, [this.mAtomColor[atm1]]);
this.drawBlackLine$com_actelion_research_chem_AbstractDepictor_DepictorLine(line1);
}if (p$1.mpProperLine$com_actelion_research_chem_AbstractDepictor_DepictorLine.apply(this, [line2])) {
p$1.setColor_$I.apply(this, [this.mAtomColor[atm2]]);
this.drawBlackLine$com_actelion_research_chem_AbstractDepictor_DepictorLine(line2);
}p$1.setColor_$I.apply(this, [this.mStandardForegroundColor]);
}, p$1);

Clazz.newMeth(C$, 'drawDashedLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I',  function (theLine, atom1, atom2) {
var xinc=(theLine.x2 - theLine.x1) / 10;
var yinc=(theLine.y2 - theLine.y1) / 10;
var aLine=Clazz.new_($I$(7,1));
var color1;
var color2;
if (this.mMol.isBondForegroundHilited$I(this.mMol.getBond$I$I(atom1, atom2))) {
color1=-3;
color2=-3;
} else {
color1=this.mAtomColor[atom1];
color2=this.mAtomColor[atom2];
}p$1.setColor_$I.apply(this, [color1]);
aLine.x1=theLine.x1;
aLine.y1=theLine.y1;
aLine.x2=theLine.x1 + xinc * 2;
aLine.y2=theLine.y1 + yinc * 2;
this.drawBlackLine$com_actelion_research_chem_AbstractDepictor_DepictorLine(aLine);
aLine.x1=theLine.x1 + xinc * 4;
aLine.y1=theLine.y1 + yinc * 4;
aLine.x2=theLine.x1 + xinc * 5;
aLine.y2=theLine.y1 + yinc * 5;
this.drawBlackLine$com_actelion_research_chem_AbstractDepictor_DepictorLine(aLine);
p$1.setColor_$I.apply(this, [color2]);
aLine.x1=theLine.x1 + xinc * 5;
aLine.y1=theLine.y1 + yinc * 5;
aLine.x2=theLine.x1 + xinc * 6;
aLine.y2=theLine.y1 + yinc * 6;
this.drawBlackLine$com_actelion_research_chem_AbstractDepictor_DepictorLine(aLine);
aLine.x1=theLine.x1 + xinc * 8;
aLine.y1=theLine.y1 + yinc * 8;
aLine.x2=theLine.x2;
aLine.y2=theLine.y2;
this.drawBlackLine$com_actelion_research_chem_AbstractDepictor_DepictorLine(aLine);
p$1.setColor_$I.apply(this, [this.mStandardForegroundColor]);
}, p$1);

Clazz.newMeth(C$, 'drawShortDashedLine$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I',  function (theLine, atom1, atom2) {
var xdif=theLine.x2 - theLine.x1;
var ydif=theLine.y2 - theLine.y1;
var length=Math.sqrt(xdif * xdif + ydif * ydif);
var points=2 * Long.$ival(Math.round$D(length / (4 * this.mpLineWidth)));
var xinc=xdif / (points - 1);
var yinc=ydif / (points - 1);
var color1;
var color2;
if (this.mMol.isBondForegroundHilited$I(this.mMol.getBond$I$I(atom1, atom2))) {
color1=-3;
color2=-3;
} else {
color1=this.mAtomColor[atom1];
color2=this.mAtomColor[atom2];
}var x=theLine.x1 - this.mpLineWidth / 2;
var y=theLine.y1 - this.mpLineWidth / 2;
p$1.setColor_$I.apply(this, [color1]);
for (var i=0; i < (points/2|0); i++) {
this.fillCircle$D$D$D(x, y, this.mpLineWidth);
x+=xinc;
y+=yinc;
}
p$1.setColor_$I.apply(this, [color2]);
for (var i=0; i < (points/2|0); i++) {
this.fillCircle$D$D$D(x, y, this.mpLineWidth);
x+=xinc;
y+=yinc;
}
p$1.setColor_$I.apply(this, [this.mStandardForegroundColor]);
}, p$1);

Clazz.newMeth(C$, 'drawWedge$com_actelion_research_chem_AbstractDepictor_DepictorLine$I$I',  function (theWedge, atom1, atom2) {
var xdiff=(theWedge.y1 - theWedge.y2) / 9;
var ydiff=(theWedge.x2 - theWedge.x1) / 9;
var xe1=(theWedge.x2 + xdiff);
var ye1=(theWedge.y2 + ydiff);
var xe2=(theWedge.x2 - xdiff);
var ye2=(theWedge.y2 - ydiff);
var xm1=(theWedge.x1 + xe1) / 2;
var ym1=(theWedge.y1 + ye1) / 2;
var xm2=(theWedge.x1 + xe2) / 2;
var ym2=(theWedge.y1 + ye2) / 2;
var p1=Clazz.new_($I$(12,1).c$$I,[3]);
var p2=Clazz.new_($I$(12,1).c$$I,[4]);
p1.addPoint$D$D(theWedge.x1, theWedge.y1);
p1.addPoint$D$D(xm1, ym1);
p1.addPoint$D$D(xm2, ym2);
p2.addPoint$D$D(xm2, ym2);
p2.addPoint$D$D(xm1, ym1);
p2.addPoint$D$D(xe1, ye1);
p2.addPoint$D$D(xe2, ye2);
var color1;
var color2;
if (this.mMol.isBondForegroundHilited$I(this.mMol.getBond$I$I(atom1, atom2))) {
color1=-3;
color2=-3;
} else {
color1=this.mAtomColor[atom1];
color2=this.mAtomColor[atom2];
if (this.mMol.getMoleculeColor$() != 1) {
color2=p$1.getESRColor$I.apply(this, [atom1]);
if (color1 == this.mMol.getAtomColor$I(atom1)) color1=color2;
}}p$1.setColor_$I.apply(this, [color1]);
this.drawPolygon$com_actelion_research_gui_generic_GenericPolygon(p1);
p$1.setColor_$I.apply(this, [color2]);
this.drawPolygon$com_actelion_research_gui_generic_GenericPolygon(p2);
p$1.setColor_$I.apply(this, [this.mStandardForegroundColor]);
}, p$1);

Clazz.newMeth(C$, 'drawDot$D$D',  function (x, y) {
this.fillCircle$D$D$D(x - this.mpDotDiameter / 2, y - this.mpDotDiameter / 2, this.mpDotDiameter);
});

Clazz.newMeth(C$, 'setRGBColor$I',  function (rgb) {
if (this.mOverruleForeground != 0) {
if (this.mCurrentColor != -4) {
this.mCurrentColor=-4;
this.setRGB$I(this.mOverruleForeground);
}return;
}this.mCurrentColor=-5;
this.mRGBColor=rgb;
this.setRGB$I(rgb);
}, p$1);

Clazz.newMeth(C$, 'setColor_$I',  function (theColor) {
if (this.mIsValidatingView) return;
if (theColor == -10) {
this.mCurrentColor=-999;
theColor=this.mStandardForegroundColor;
}if (theColor != -2 && theColor != -7  && this.mOverruleForeground != 0 ) theColor=-4;
if (theColor == this.mCurrentColor) return;
if (this.mCurrentColor == -8 && theColor != -9 ) return;
if (theColor == -8) this.mPreviousColor=this.mCurrentColor;
if (theColor == -9) theColor=this.mPreviousColor;
this.mCurrentColor=theColor;
switch (theColor) {
case 0:
this.setRGB$I(this.mCustomForeground == 0 ? -16777216 : this.mCustomForeground);
break;
case -6:
this.setRGB$I(this.mCustomForeground);
break;
case -4:
this.setRGB$I(this.mOverruleForeground);
break;
case -2:
this.setRGB$I(this.mBondBGHiliteColor);
break;
case -3:
this.setRGB$I(this.mBondFGHiliteColor);
break;
case -7:
this.setRGB$I(this.mExcludeGroupBGColor);
break;
case -8:
this.setRGB$I(this.mExcludeGroupFGColor);
break;
case -5:
this.setRGB$I(this.mRGBColor);
break;
case 64:
this.setRGB$I(-14655233);
break;
case 128:
this.setRGB$I(-65536);
break;
case 256:
this.setRGB$I(-4194049);
break;
case 192:
this.setRGB$I(-16711936);
break;
case 320:
this.setRGB$I(-24576);
break;
case 384:
this.setRGB$I(-16744448);
break;
case 448:
this.setRGB$I(-6291456);
break;
case 1:
this.setRGB$I(-8355712);
break;
default:
this.setRGB$I(-16777216);
break;
}
}, p$1);

Clazz.newMeth(C$, 'getESRTypeToDisplayAt$I',  function (atom) {
var type=-1;
var group=-1;
if ((this.mDisplayMode & 128) != 0) return type;
if (this.mMol.isAtomStereoCenter$I(atom)) {
type=this.mMol.getAtomESRType$I(atom);
group=this.mMol.getAtomESRGroup$I(atom);
}var bond=this.mMol.findBINAPChiralityBond$I(atom);
if (bond != -1) {
type=this.mMol.getBondESRType$I(bond);
group=this.mMol.getBondESRGroup$I(bond);
}if (type != -1 && type != 0 ) type|=(group << 8);
return type;
}, p$1);

Clazz.newMeth(C$, 'getESRColor$I',  function (atom) {
if ((this.mDisplayMode & (4224)) != 0) return this.mAtomColor[atom];
var esrInfo=p$1.getESRTypeToDisplayAt$I.apply(this, [atom]);
if (esrInfo == -1) {
var alleneCenter=this.mMol.findAlleneCenterAtom$I(atom);
if (alleneCenter != -1) {
atom=alleneCenter;
esrInfo=p$1.getESRTypeToDisplayAt$I.apply(this, [atom]);
}}if (esrInfo == -1) return this.mAtomColor[atom];
switch (esrInfo & 255) {
case 1:
return 384;
case 2:
return 64;
default:
return 448;
}
}, p$1);

C$.$static$=function(){C$.$static$=0;
C$.ATOM_LABEL_COLOR=Clazz.array(Integer.TYPE, -1, [0, 16777215, 14286847, 13402367, 12779264, 16758197, 9474192, 3166456, 16715021, 9494608, 11789301, 11230450, 9109248, 12560038, 15780000, 16744448, 16777008, 2093087, 8442339, 9388244, 4062976, 15132390, 12567239, 10921643, 9083335, 10255047, 14706227, 15765664, 5296208, 13140019, 8224944, 12750735, 6721423, 12419299, 16752896, 10889513, 6076625, 7351984, 65280, 9764863, 9756896, 7586505, 5551541, 3907230, 2396047, 687500, 27013, 12632256, 16767375, 10909043, 6717568, 10380213, 13924864, 9699476, 4366000, 5707663, 51456, 7394559, 16777159, 14286791, 13107143, 10747847, 9437127, 6422471, 4587463, 3211207, 2097095, 65436, 58997, 54354, 48952, 43812, 5096191, 5089023, 2200790, 2522539, 2516630, 1528967, 13684960, 16765219, 12105936, 10900557, 5724513, 10375093, 11230208, 7688005, 4358806, 4325478, 32000, 7384058, 47871, 41471, 36863, 33023, 27647, 5528818, 7888099, 9064419, 10565332, 11739092, 11739066, 11734438, 12389767, 13041766, 13369433, 13697103, 14221381, 14680120, 15073326, 15400998, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13158600, 1334015, 56540, 15075850, 15132160, 56540, 15075850, 15461355, 8553170, 1016335, 1016335, 1334015, 15132160, 3289770, 14456450, 16422400, 16422400, 11819700, 3289770, 1016335]);
};
;
(function(){/*c*/var C$=Clazz.newClass(P$.AbstractDepictor, "DepictorDot", function(){
Clazz.newInstance(this, arguments[0],false,C$);
});

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['D',['x','y'],'I',['color']]]

Clazz.newMeth(C$, 'c$$D$D$I',  function (x, y, color) {
;C$.$init$.apply(this);
this.x=x;
this.y=y;
this.color=color;
}, 1);

Clazz.newMeth(C$);
})()
;
(function(){/*c*/var C$=Clazz.newClass(P$.AbstractDepictor, "DepictorLine", function(){
Clazz.newInstance(this, arguments[0],false,C$);
});

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['D',['x1','y1','x2','y2']]]

Clazz.newMeth(C$, 'c$$D$D$D$D',  function (x1, y1, x2, y2) {
;C$.$init$.apply(this);
this.x1=x1;
this.y1=y1;
this.x2=x2;
this.y2=y2;
}, 1);

Clazz.newMeth(C$, 'c$',  function () {
;C$.$init$.apply(this);
}, 1);
})()

Clazz.newMeth(C$);
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-18 09:54:13 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
