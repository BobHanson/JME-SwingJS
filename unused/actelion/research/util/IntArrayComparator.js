(function(){var P$=Clazz.newPackage("com.actelion.research.util"),I$=[];
/*c*/var C$=Clazz.newClass(P$, "IntArrayComparator", null, null, ['java.util.Comparator', 'java.io.Serializable']);

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

Clazz.newMeth(C$, ['compare$IA$IA','compare$O$O'],  function (ia1, ia2) {
if (ia1 == null ) return (ia2 == null ) ? 0 : 1;
if (ia2 == null ) return -1;
for (var i=0; i < ia1.length; i++) {
if (ia2.length == i) return 1;
if (ia1[i] != ia2[i]) return (ia1[i] < ia2[i]) ? -1 : 1;
}
return (ia2.length > ia1.length) ? -1 : 0;
});

Clazz.newMeth(C$);
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-25 13:08:06 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
