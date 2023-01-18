(function(){var P$=Clazz.newPackage("com.actelion.research.chem.coords"),p$1={},I$=[[0,'java.util.Arrays','com.actelion.research.chem.coords.InventorAngle','java.util.ArrayList','com.actelion.research.chem.coords.CoordinateInventor']],I$0=I$[0],$I$=function(i,n){return((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "InventorFragment");

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
this.CIRCULAR_BINS=36;
},1);

C$.$fields$=[['Z',['mKeepMarkedAtoms','mMinMaxAvail'],'D',['mMinX','mMinY','mMaxX','mMaxY','mCollisionPanalty'],'I',['CIRCULAR_BINS'],'O',['mGlobalAtom','int[]','+mGlobalBond','+mGlobalToLocalAtom','+mPriority','mAtomX','double[]','+mAtomY','mMol','com.actelion.research.chem.StereoMolecule','mFlipList','int[][]','mSortedAtom','int[]']]]

Clazz.newMeth(C$, 'c$$com_actelion_research_chem_StereoMolecule$I$Z',  function (mol, atoms, keepMarkedAtoms) {
;C$.$init$.apply(this);
this.mMol=mol;
this.mKeepMarkedAtoms=keepMarkedAtoms;
this.mGlobalAtom=Clazz.array(Integer.TYPE, [atoms]);
this.mPriority=Clazz.array(Integer.TYPE, [atoms]);
this.mAtomX=Clazz.array(Double.TYPE, [atoms]);
this.mAtomY=Clazz.array(Double.TYPE, [atoms]);
}, 1);

Clazz.newMeth(C$, 'c$$com_actelion_research_chem_coords_InventorFragment',  function (f) {
;C$.$init$.apply(this);
this.mMol=f.mMol;
this.mKeepMarkedAtoms=f.mKeepMarkedAtoms;
this.mGlobalAtom=Clazz.array(Integer.TYPE, [f.size$()]);
this.mPriority=Clazz.array(Integer.TYPE, [f.size$()]);
this.mAtomX=Clazz.array(Double.TYPE, [f.size$()]);
this.mAtomY=Clazz.array(Double.TYPE, [f.size$()]);
for (var i=0; i < f.size$(); i++) {
this.mGlobalAtom[i]=f.mGlobalAtom[i];
this.mPriority[i]=f.mPriority[i];
this.mAtomX[i]=f.mAtomX[i];
this.mAtomY[i]=f.mAtomY[i];
}
if (f.mGlobalBond != null ) {
this.mGlobalBond=Clazz.array(Integer.TYPE, [f.mGlobalBond.length]);
for (var i=0; i < f.mGlobalBond.length; i++) this.mGlobalBond[i]=f.mGlobalBond[i];

}if (f.mGlobalToLocalAtom != null ) {
this.mGlobalToLocalAtom=Clazz.array(Integer.TYPE, [f.mGlobalToLocalAtom.length]);
for (var i=0; i < f.mGlobalToLocalAtom.length; i++) this.mGlobalToLocalAtom[i]=f.mGlobalToLocalAtom[i];

}}, 1);

Clazz.newMeth(C$, 'size$',  function () {
return this.mGlobalAtom.length;
});

Clazz.newMeth(C$, 'equals$com_actelion_research_chem_coords_InventorFragment',  function (f) {
if (f.size$() != this.size$()) return false;
var sorted=p$1.getSortedAtoms.apply(this, []);
var sortedF=p$1.getSortedAtoms.apply(f, []);
for (var i=0; i < sorted.length; i++) if (sorted[i] != sortedF[i]) return false;

return true;
});

Clazz.newMeth(C$, 'getSortedAtoms',  function () {
if (this.mSortedAtom == null ) {
this.mSortedAtom=this.mGlobalAtom.clone$();
$I$(1).sort$IA(this.mSortedAtom);
}return this.mSortedAtom;
}, p$1);

Clazz.newMeth(C$, 'getAtomX$I',  function (index) {
return this.mAtomX[index];
});

Clazz.newMeth(C$, 'getAtomY$I',  function (index) {
return this.mAtomY[index];
});

Clazz.newMeth(C$, 'getWidth$',  function () {
p$1.calculateMinMax.apply(this, []);
return this.mMaxX - this.mMinX + 1.0;
});

Clazz.newMeth(C$, 'getHeight$',  function () {
p$1.calculateMinMax.apply(this, []);
return this.mMaxY - this.mMinY + 1.0;
});

Clazz.newMeth(C$, 'isMember$I',  function (globalAtom) {
for (var i=0; i < this.mGlobalAtom.length; i++) if (globalAtom == this.mGlobalAtom[i]) return true;

return false;
});

Clazz.newMeth(C$, 'getGlobalAtom$I',  function (localAtom) {
return this.mGlobalAtom[localAtom];
});

Clazz.newMeth(C$, 'getLocalAtom$I',  function (globalAtom) {
for (var i=0; i < this.mGlobalAtom.length; i++) if (globalAtom == this.mGlobalAtom[i]) return i;

return -1;
});

Clazz.newMeth(C$, 'translate$D$D',  function (dx, dy) {
for (var i=0; i < this.mGlobalAtom.length; i++) {
this.mAtomX[i]+=dx;
this.mAtomY[i]+=dy;
}
});

Clazz.newMeth(C$, 'rotate$D$D$D',  function (x, y, angleDif) {
for (var i=0; i < this.mGlobalAtom.length; i++) {
var distance=Math.sqrt((this.mAtomX[i] - x) * (this.mAtomX[i] - x) + (this.mAtomY[i] - y) * (this.mAtomY[i] - y));
var angle=$I$(2).getAngle$D$D$D$D(x, y, this.mAtomX[i], this.mAtomY[i]) + angleDif;
this.mAtomX[i]=x + distance * Math.sin(angle);
this.mAtomY[i]=y + distance * Math.cos(angle);
}
});

Clazz.newMeth(C$, 'flip$D$D$D',  function (x, y, mirrorAngle) {
for (var i=0; i < this.mGlobalAtom.length; i++) {
var distance=Math.sqrt((this.mAtomX[i] - x) * (this.mAtomX[i] - x) + (this.mAtomY[i] - y) * (this.mAtomY[i] - y));
var angle=2 * mirrorAngle - $I$(2).getAngle$D$D$D$D(x, y, this.mAtomX[i], this.mAtomY[i]);
this.mAtomX[i]=x + distance * Math.sin(angle);
this.mAtomY[i]=y + distance * Math.cos(angle);
}
});

Clazz.newMeth(C$, 'flipOneSide$I',  function (bond) {
if (this.mFlipList == null ) this.mFlipList=Clazz.array(Integer.TYPE, [this.mMol.getAllBonds$(), null]);
if (this.mFlipList[bond] == null ) {
var graphAtom=Clazz.array(Integer.TYPE, [this.mGlobalAtom.length]);
var isOnSide=Clazz.array(Boolean.TYPE, [this.mMol.getAllAtoms$()]);
var atom1=this.mMol.getBondAtom$I$I(0, bond);
var atom2=this.mMol.getBondAtom$I$I(1, bond);
graphAtom[0]=atom1;
isOnSide[atom1]=true;
var current=0;
var highest=0;
while (current <= highest){
for (var i=0; i < this.mMol.getAllConnAtoms$I(graphAtom[current]); i++) {
var candidate=this.mMol.getConnAtom$I$I(graphAtom[current], i);
if (!isOnSide[candidate] && candidate != atom2 ) {
graphAtom[++highest]=candidate;
isOnSide[candidate]=true;
}}
if (current == highest) break;
++current;
}
var flipOtherSide=(highest + 1 > (this.mGlobalAtom.length/2|0));
if (this.mKeepMarkedAtoms) {
var coreOnSide=false;
var coreOffSide=false;
for (var i=0; i < this.mGlobalAtom.length; i++) {
var atom=this.mGlobalAtom[i];
if (this.mMol.isMarkedAtom$I(atom) && atom != atom1  && atom != atom2 ) {
if (isOnSide[this.mGlobalAtom[i]]) coreOnSide=true;
 else coreOffSide=true;
}}
if (coreOnSide != coreOffSide ) flipOtherSide=coreOnSide;
}var count=2;
this.mFlipList[bond]=Clazz.array(Integer.TYPE, [flipOtherSide ? this.mGlobalAtom.length - highest : highest + 2]);
for (var i=0; i < this.mGlobalAtom.length; i++) {
if (this.mGlobalAtom[i] == atom1) this.mFlipList[bond][flipOtherSide ? 0 : 1]=i;
 else if (this.mGlobalAtom[i] == atom2) this.mFlipList[bond][flipOtherSide ? 1 : 0]=i;
 else if (!!(flipOtherSide ^ isOnSide[this.mGlobalAtom[i]])) this.mFlipList[bond][count++]=i;
}
}var x=this.mAtomX[this.mFlipList[bond][0]];
var y=this.mAtomY[this.mFlipList[bond][0]];
var mirrorAngle=$I$(2).getAngle$D$D$D$D(x, y, this.mAtomX[this.mFlipList[bond][1]], this.mAtomY[this.mFlipList[bond][1]]);
for (var i=2; i < this.mFlipList[bond].length; i++) {
var index=this.mFlipList[bond][i];
var distance=Math.sqrt((this.mAtomX[index] - x) * (this.mAtomX[index] - x) + (this.mAtomY[index] - y) * (this.mAtomY[index] - y));
var angle=2 * mirrorAngle - $I$(2).getAngle$D$D$D$D(x, y, this.mAtomX[index], this.mAtomY[index]);
this.mAtomX[index]=x + distance * Math.sin(angle);
this.mAtomY[index]=y + distance * Math.cos(angle);
}
});

Clazz.newMeth(C$, 'arrangeWith$com_actelion_research_chem_coords_InventorFragment',  function (f) {
var maxGain=0.0;
var maxCorner=0;
for (var corner=0; corner < 4; corner++) {
var gain=p$1.getCornerDistance$I.apply(this, [corner]) + p$1.getCornerDistance$I.apply(f, [(corner >= 2) ? corner - 2 : corner + 2]);
if (maxGain < gain ) {
maxGain=gain;
maxCorner=corner;
}}
var sumHeight=this.getHeight$() + f.getHeight$();
var sumWidth=0.75 * (this.getWidth$() + f.getWidth$());
var maxHeight=Math.max(this.getHeight$(), f.getHeight$());
var maxWidth=0.75 * Math.max(this.getWidth$(), f.getWidth$());
var bestCornerSize=Math.sqrt((sumHeight - maxGain) * (sumHeight - maxGain) + (sumWidth - 0.75 * maxGain) * (sumWidth - 0.75 * maxGain));
var toppedSize=Math.max(maxWidth, sumHeight);
var besideSize=Math.max(maxHeight, sumWidth);
if (bestCornerSize < toppedSize  && bestCornerSize < besideSize  ) {
switch (maxCorner) {
case 0:
f.translate$D$D(this.mMaxX - f.mMinX - maxGain  + 1.0, this.mMinY - f.mMaxY + maxGain - 1.0);
break;
case 1:
f.translate$D$D(this.mMaxX - f.mMinX - maxGain  + 1.0, this.mMaxY - f.mMinY - maxGain  + 1.0);
break;
case 2:
f.translate$D$D(this.mMinX - f.mMaxX + maxGain - 1.0, this.mMaxY - f.mMinY - maxGain  + 1.0);
break;
case 3:
f.translate$D$D(this.mMinX - f.mMaxX + maxGain - 1.0, this.mMinY - f.mMaxY + maxGain - 1.0);
break;
}
} else if (besideSize < toppedSize ) {
f.translate$D$D(this.mMaxX - f.mMinX + 1.0, (this.mMaxY + this.mMinY - f.mMaxY - f.mMinY) / 2);
} else {
f.translate$D$D((this.mMaxX + this.mMinX - f.mMaxX - f.mMinX) / 2, this.mMaxY - f.mMinY + 1.0);
}});

Clazz.newMeth(C$, 'calculateMinMax',  function () {
if (this.mMinMaxAvail) return;
this.mMinX=this.mAtomX[0];
this.mMaxX=this.mAtomX[0];
this.mMinY=this.mAtomY[0];
this.mMaxY=this.mAtomY[0];
for (var i=0; i < this.mGlobalAtom.length; i++) {
var surplus=p$1.getAtomSurplus$I.apply(this, [i]);
if (this.mMinX > this.mAtomX[i] - surplus ) this.mMinX=this.mAtomX[i] - surplus;
if (this.mMaxX < this.mAtomX[i] + surplus ) this.mMaxX=this.mAtomX[i] + surplus;
if (this.mMinY > this.mAtomY[i] - surplus ) this.mMinY=this.mAtomY[i] - surplus;
if (this.mMaxY < this.mAtomY[i] + surplus ) this.mMaxY=this.mAtomY[i] + surplus;
}
this.mMinMaxAvail=true;
}, p$1);

Clazz.newMeth(C$, 'getCornerDistance$I',  function (corner) {
var minDistance=9999.0;
for (var atom=0; atom < this.mGlobalAtom.length; atom++) {
var surplus=p$1.getAtomSurplus$I.apply(this, [atom]);
var d=0.0;
switch (corner) {
case 0:
d=this.mMaxX - 0.5 * (this.mMaxX + this.mMinY + this.mAtomX[atom]  - this.mAtomY[atom]);
break;
case 1:
d=this.mMaxX - 0.5 * (this.mMaxX - this.mMaxY + this.mAtomX[atom] + this.mAtomY[atom]);
break;
case 2:
d=0.5 * (this.mMinX + this.mMaxY + this.mAtomX[atom]  - this.mAtomY[atom]) - this.mMinX;
break;
case 3:
d=0.5 * (this.mMinX - this.mMinY + this.mAtomX[atom] + this.mAtomY[atom]) - this.mMinX;
break;
}
if (minDistance > d - surplus ) minDistance=d - surplus;
}
return minDistance;
}, p$1);

Clazz.newMeth(C$, 'getAtomSurplus$I',  function (atom) {
return (Long.$ne(this.mMol.getAtomQueryFeatures$I(this.mGlobalAtom[atom]),0 )) ? 0.6 : (this.mMol.getAtomicNo$I(this.mGlobalAtom[atom]) != 6) ? 0.25 : 0.0;
}, p$1);

Clazz.newMeth(C$, 'getCollisionList$',  function () {
this.mCollisionPanalty=0.0;
var collisionList=Clazz.new_($I$(3,1));
for (var i=1; i < this.mGlobalAtom.length; i++) {
for (var j=0; j < i; j++) {
var xdif=Math.abs(this.mAtomX[i] - this.mAtomX[j]);
var ydif=Math.abs(this.mAtomY[i] - this.mAtomY[j]);
var dist=Math.sqrt(xdif * xdif + ydif * ydif);
if (dist < 0.8 ) {
var collidingAtom=Clazz.array(Integer.TYPE, [2]);
collidingAtom[0]=this.mGlobalAtom[i];
collidingAtom[1]=this.mGlobalAtom[j];
collisionList.add$O(collidingAtom);
}var panalty=1.0 - Math.min(dist, 1.0);
this.mCollisionPanalty+=panalty * panalty;
}
}
return collisionList;
});

Clazz.newMeth(C$, 'getCollisionPanalty$',  function () {
return this.mCollisionPanalty;
});

Clazz.newMeth(C$, 'locateBonds$',  function () {
var fragmentBonds=0;
for (var i=0; i < this.mGlobalAtom.length; i++) {
var atom=this.mGlobalAtom[i];
var connAtoms=this.mMol.getAllConnAtoms$I(atom);
for (var j=0; j < connAtoms; j++) if (this.mMol.getConnAtom$I$I(atom, j) > atom) ++fragmentBonds;

}
this.mGlobalBond=Clazz.array(Integer.TYPE, [fragmentBonds]);
this.mGlobalToLocalAtom=Clazz.array(Integer.TYPE, [this.mMol.getAllAtoms$()]);
fragmentBonds=0;
for (var i=0; i < this.mGlobalAtom.length; i++) {
var atom=this.mGlobalAtom[i];
var connAtoms=this.mMol.getAllConnAtoms$I(atom);
this.mGlobalToLocalAtom[atom]=i;
for (var j=0; j < connAtoms; j++) if (this.mMol.getConnAtom$I$I(atom, j) > atom) this.mGlobalBond[fragmentBonds++]=this.mMol.getConnBond$I$I(atom, j);

}
});

Clazz.newMeth(C$, 'optimizeAtomCoordinates$I',  function (atom) {
var x=this.mAtomX[atom];
var y=this.mAtomY[atom];
var collisionForce=Clazz.array($I$(2), [4]);
var forces=0;
for (var i=0; i < this.mGlobalBond.length; i++) {
if (forces >= 4) break;
if (atom == this.mGlobalToLocalAtom[this.mMol.getBondAtom$I$I(0, this.mGlobalBond[i])] || atom == this.mGlobalToLocalAtom[this.mMol.getBondAtom$I$I(1, this.mGlobalBond[i])] ) continue;
var x1=this.mAtomX[this.mGlobalToLocalAtom[this.mMol.getBondAtom$I$I(0, this.mGlobalBond[i])]];
var y1=this.mAtomY[this.mGlobalToLocalAtom[this.mMol.getBondAtom$I$I(0, this.mGlobalBond[i])]];
var x2=this.mAtomX[this.mGlobalToLocalAtom[this.mMol.getBondAtom$I$I(1, this.mGlobalBond[i])]];
var y2=this.mAtomY[this.mGlobalToLocalAtom[this.mMol.getBondAtom$I$I(1, this.mGlobalBond[i])]];
var d1=Math.sqrt((x1 - x) * (x1 - x) + (y1 - y) * (y1 - y));
var d2=Math.sqrt((x2 - x) * (x2 - x) + (y2 - y) * (y2 - y));
var bondLength=Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
if (d1 < bondLength  && d2 < bondLength  ) {
if (x1 == x2 ) {
var d=Math.abs(x - x1);
if (d < 0.5 ) collisionForce[forces++]=Clazz.new_([$I$(2).getAngle$D$D$D$D(x1, y, x, y), (0.5 - d) / 2],$I$(2,1).c$$D$D);
} else if (y1 == y2 ) {
var d=Math.abs(y - y1);
if (d < 0.5 ) collisionForce[forces++]=Clazz.new_([$I$(2).getAngle$D$D$D$D(x, y1, x, y), (0.5 - d) / 2],$I$(2,1).c$$D$D);
} else {
var m1=(y2 - y1) / (x2 - x1);
var m2=-1 / m1;
var a1=y1 - m1 * x1;
var a2=y - m2 * x;
var xs=(a2 - a1) / (m1 - m2);
var ys=m1 * xs + a1;
var d=Math.sqrt((xs - x) * (xs - x) + (ys - y) * (ys - y));
if (d < 0.5 ) collisionForce[forces++]=Clazz.new_([$I$(2).getAngle$D$D$D$D(xs, ys, x, y), (0.5 - d) / 2],$I$(2,1).c$$D$D);
}continue;
}if (d1 < 0.5 ) {
collisionForce[forces++]=Clazz.new_([$I$(2).getAngle$D$D$D$D(x1, y1, x, y), (0.5 - d1) / 2],$I$(2,1).c$$D$D);
continue;
}if (d2 < 0.5 ) {
collisionForce[forces++]=Clazz.new_([$I$(2).getAngle$D$D$D$D(x2, y2, x, y), (0.5 - d2) / 2],$I$(2,1).c$$D$D);
continue;
}}
if (forces > 0) {
var force=$I$(4).getMeanAngle$com_actelion_research_chem_coords_InventorAngleA$I(collisionForce, forces);
this.mAtomX[atom]+=force.mLength * Math.sin(force.mAngle);
this.mAtomY[atom]+=force.mLength * Math.cos(force.mAngle);
}});

Clazz.newMeth(C$, 'calculatePreferredAttachmentAngle$D$D$I$D',  function (x, y, neighbourAtomCount, padding) {
if (this.size$() == 1) return 0;
var BIN_ANGLE=0.17453292519943295;
var neighbourRadius=padding + Math.sqrt(neighbourAtomCount);
var distance=Clazz.array(Double.TYPE, [36]);
for (var i=0; i < this.mGlobalAtom.length; i++) {
var angle=$I$(2).getAngle$D$D$D$D(x, y, this.mAtomX[i], this.mAtomY[i]);
var bin=p$1.correctBin$I.apply(this, [Long.$ival(Math.round$D(angle * 36 / (6.283185307179586)))]);
var dx=x - this.mAtomX[i];
var dy=y - this.mAtomY[i];
var sd=dx * dx + dy * dy;
if (distance[bin] < sd ) distance[bin]=sd;
}
var maxDistance=-1;
var maxBin=-1;
for (var i=0; i < 36; i++) {
distance[i]=Math.sqrt(distance[i]);
if (maxDistance < distance[i] ) {
maxDistance=distance[i];
maxBin=i;
}}
var preferredBin=p$1.correctBin$I.apply(this, [maxBin - 18]);
for (var i=0; i <= 18; i++) {
distance[p$1.correctBin$I.apply(this, [preferredBin + i])]+=0.01 * i;
distance[p$1.correctBin$I.apply(this, [preferredBin - i])]+=0.01 * i;
}
var neighbourCount=9;
var sin=Clazz.array(Double.TYPE, [neighbourCount]);
var cos=Clazz.array(Double.TYPE, [neighbourCount]);
for (var i=1; i < neighbourCount; i++) {
sin[i]=Math.sin(i * 0.17453292519943295);
cos[i]=Math.cos(i * 0.17453292519943295);
}
var squareRadius=neighbourRadius * neighbourRadius;
var minDistance=1.7976931348623157E308;
var minBin=-1;
for (var bin=0; bin < 36; bin++) {
if (distance[bin] >= minDistance ) continue;
var localMinDistance=distance[bin];
for (var i=1; i < neighbourCount; i++) {
for (var j=-1; j <= 1; j+=2) {
var neighbourBin=p$1.correctBin$I.apply(this, [bin + j * i]);
if (distance[neighbourBin] * cos[i] <= localMinDistance ) continue;
var d=cos[i] * Math.min(distance[neighbourBin], neighbourRadius / sin[i]);
if (localMinDistance < d ) {
localMinDistance=d;
if (minDistance <= localMinDistance ) break;
}}
if (minDistance <= localMinDistance ) break;
}
if (minDistance > localMinDistance ) {
minDistance=localMinDistance;
minBin=bin;
}}
return 3.141592653589793 * 2 * minBin  / 36;
});

Clazz.newMeth(C$, 'correctBin$I',  function (bin) {
return bin < 0 ? bin + 36 : bin >= 36 ? bin - 36 : bin;
}, p$1);

Clazz.newMeth(C$);
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-18 09:54:18 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
