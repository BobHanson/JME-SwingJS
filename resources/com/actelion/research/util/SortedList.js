(function(){var P$=Clazz.newPackage("com.actelion.research.util"),p$1={},I$=[[0,'java.util.ArrayList']],I$0=I$[0],$I$=function(i,n){return((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "SortedList");

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
this.mList=Clazz.new_($I$(1,1));
},1);

C$.$fields$=[['O',['mList','java.util.ArrayList','mComparator','java.util.Comparator']]]

Clazz.newMeth(C$, 'c$',  function () {
C$.c$$java_util_Comparator.apply(this, [null]);
}, 1);

Clazz.newMeth(C$, 'c$$java_util_Comparator',  function (comparator) {
;C$.$init$.apply(this);
this.mComparator=comparator;
}, 1);

Clazz.newMeth(C$, 'contains$O',  function (object) {
return this.getIndex$O(object) != -1;
});

Clazz.newMeth(C$, 'equals$com_actelion_research_util_SortedList',  function (s) {
if (this.mList.size$() != s.mList.size$()) return false;
if (this.mComparator != null ) {
for (var i=0; i < this.mList.size$(); i++) if (this.mComparator.compare$O$O(this.mList.get$I(i), s.mList.get$I(i)) != 0) return false;

} else {
for (var i=0; i < this.mList.size$(); i++) if (!this.mList.get$I(i).equals$O(s.mList.get$I(i))) return false;

}return true;
});

Clazz.newMeth(C$, 'getIndex$O',  function (object) {
var index=this.getIndexOrInsertIndex$O(object);
return (index < 0) ? -1 : index;
});

Clazz.newMeth(C$, 'getIndexOrInsertIndex$O',  function (object) {
var vectorSize=this.mList.size$();
if (vectorSize == 0) {
return -1;
}var index=1;
while (2 * index <= vectorSize)index<<=1;

var increment=index;
--index;
while (increment != 0){
increment>>=1;
if (index >= vectorSize) {
index-=increment;
continue;
}var comparison=p$1.compare$O$O.apply(this, [object, this.mList.get$I(index)]);
if (comparison == 0) return index;
if (increment == 0) break;
if (comparison < 0) {
index-=increment;
} else {
index+=increment;
}}
if ((index < vectorSize) && (p$1.compare$O$O.apply(this, [object, this.mList.get$I(index)]) > 0) ) ++index;
return -(index + 1);
});

Clazz.newMeth(C$, 'compare$O$O',  function (o1, o2) {
return (this.mComparator != null ) ? this.mComparator.compare$O$O(o1, o2) : (o1).compareTo$O(o2);
}, p$1);

Clazz.newMeth(C$, 'getIndexBelowEqual$O',  function (object) {
var index=this.getIndexOrInsertIndex$O(object);
return (index < 0) ? -(index + 1) : index;
});

Clazz.newMeth(C$, 'getIndexAboveEqual$O',  function (object) {
var index=this.getIndexOrInsertIndex$O(object);
return (index < 0) ? -(index + 1) : index + 1;
});

Clazz.newMeth(C$, 'add$O',  function (object) {
var index=this.getIndexOrInsertIndex$O(object);
if (index < 0) {
index=-(index + 1);
this.mList.add$I$O(index, object);
}return index;
});

Clazz.newMeth(C$, 'addIfNew$O',  function (object) {
var index=this.getIndexOrInsertIndex$O(object);
if (index >= 0) return false;
index=-(index + 1);
this.mList.add$I$O(index, object);
return true;
});

Clazz.newMeth(C$, 'size$',  function () {
return this.mList.size$();
});

Clazz.newMeth(C$, 'get$I',  function (index) {
return (index < 0) ? null : this.mList.get$I(index);
});

Clazz.newMeth(C$, 'toArray$OA',  function (e) {
return this.mList.toArray$OA(e);
});

Clazz.newMeth(C$, 'remove$I',  function (index) {
this.mList.remove$I(index);
});

Clazz.newMeth(C$, 'removeAll$',  function () {
this.mList.clear$();
});
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-18 09:54:36 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
