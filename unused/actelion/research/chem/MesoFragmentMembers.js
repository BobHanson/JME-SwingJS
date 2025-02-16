(function(){var P$=Clazz.newPackage("com.actelion.research.chem"),p$1={},p$2={},p$3={},I$=[[0,'com.actelion.research.chem.CanonizerMesoHelper','java.util.TreeSet','com.actelion.research.chem.MesoFragmentBranch','com.actelion.research.chem.MesoFragmentMembers',['com.actelion.research.chem.CanonizerMesoHelper','.ESRGroupFragmentMatrix'],'java.util.ArrayList','com.actelion.research.chem.ESRGroupNormalizationInfo','com.actelion.research.chem.CanonizerRankListComparator','java.util.Arrays']],I$0=I$[0],$I$=function(i,n,m){return m?$I$(i)[n].apply(null,m):((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "MesoFragmentMembers", null, null, 'Comparable');

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['O',['isMember','boolean[]','memberAtom','int[]']]]

Clazz.newMeth(C$, 'c$$I',  function (atoms) {
;C$.$init$.apply(this);
this.isMember=Clazz.array(Boolean.TYPE, [atoms]);
}, 1);

Clazz.newMeth(C$, 'add$I',  function (atom) {
this.isMember[atom]=true;
});

Clazz.newMeth(C$, 'consolidate',  function () {
var count=0;
for (var is, $is = 0, $$is = this.isMember; $is<$$is.length&&((is=($$is[$is])),1);$is++) if (is) ++count;

this.memberAtom=Clazz.array(Integer.TYPE, [count]);
count=0;
for (var atom=0; atom < this.isMember.length; atom++) if (this.isMember[atom]) this.memberAtom[count++]=atom;

}, p$3);

Clazz.newMeth(C$, 'hasStereoCenters$ZA',  function (isStereoCenter) {
p$3.consolidate.apply(this, []);
for (var j=0; j < this.memberAtom.length; j++) if (isStereoCenter[this.memberAtom[j]]) return true;

return false;
});

Clazz.newMeth(C$, ['compareTo$com_actelion_research_chem_MesoFragmentMembers','compareTo$O'],  function (members) {
if (this.memberAtom.length != members.memberAtom.length) return (this.memberAtom.length < members.memberAtom.length) ? -1 : 1;
for (var i=0; i < this.memberAtom.length; i++) if (this.memberAtom[i] != members.memberAtom[i]) return (this.memberAtom[i] < members.memberAtom[i]) ? -1 : 1;

return 0;
});

Clazz.newMeth(C$);
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-18 09:54:14 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
