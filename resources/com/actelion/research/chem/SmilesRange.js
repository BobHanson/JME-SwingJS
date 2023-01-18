(function(){var P$=Clazz.newPackage("com.actelion.research.chem"),p$1={},p$2={},p$3={},I$=[[0,'java.util.ArrayList',['com.actelion.research.chem.SmilesParser','.ParityNeighbour'],'com.actelion.research.chem.StereoMolecule','com.actelion.research.util.ArrayUtils','com.actelion.research.chem.reaction.Reaction','com.actelion.research.util.SortedList','com.actelion.research.chem.SmilesRange','com.actelion.research.chem.Molecule','java.util.TreeMap',['com.actelion.research.chem.SmilesParser','.THParity'],'java.util.Arrays','com.actelion.research.chem.coords.CoordinateInventor','StringBuilder','com.actelion.research.chem.RingCollection','com.actelion.research.chem.IsomericSmilesCreator','com.actelion.research.chem.Canonizer']],I$0=I$[0],$I$=function(i,n,m){return m?$I$(i)[n].apply(null,m):((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "SmilesRange");

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[['Z',['isDefault'],'I',['pos','min','max'],'O',['smi','byte[]']]]

Clazz.newMeth(C$, 'c$$BA',  function (smiles) {
;C$.$init$.apply(this);
this.smi=smiles;
}, 1);

Clazz.newMeth(C$, 'parse$BA$I$I$I',  function (smiles, position, defaultMin, defaultMax) {
this.isDefault=false;
this.pos=position;
if (Character.isDigit$I(smiles[position])) {
this.min=this.max=p$3.parseInt.apply(this, []);
return this.pos - position;
}if (smiles[position] == 123  && Character.isDigit$I(smiles[position + 1]) ) {
++this.pos;
this.min=p$3.parseInt.apply(this, []);
if (smiles[this.pos++] != 45 ) return 0;
if (!Character.isDigit$I(smiles[this.pos])) return 0;
this.max=p$3.parseInt.apply(this, []);
if (smiles[this.pos++] != 125 ) return 0;
return this.pos - position;
}this.min=defaultMin;
this.max=defaultMax;
this.isDefault=true;
return 0;
});

Clazz.newMeth(C$, 'isSingle$',  function () {
return this.max == this.min;
});

Clazz.newMeth(C$, 'isRange$',  function () {
return this.max > this.min;
});

Clazz.newMeth(C$, 'toString',  function () {
return "{" + this.min + "-" + this.max + "}" ;
});

Clazz.newMeth(C$, 'parseInt',  function () {
var num=this.smi[this.pos++] - 48;
if (Character.isDigit$I(this.smi[this.pos])) num=10 * num + (this.smi[this.pos++] - 48);
return num;
}, p$3);

Clazz.newMeth(C$);
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-18 09:54:16 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
