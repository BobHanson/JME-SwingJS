(function(){var P$=Clazz.newPackage("com.actelion.research.chem.coords"),I$=[[0,'com.actelion.research.chem.SSSearcherWithIndex','com.actelion.research.chem.IDCodeParserWithoutCoordinateInvention','com.actelion.research.chem.coords.InventorTemplate']],I$0=I$[0],$I$=function(i,n){return((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "InventorDefaultTemplateList", null, 'java.util.ArrayList');

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[[]
,['O',['DEFAULT_TEMPLATE','String[]']]]

Clazz.newMeth(C$, 'c$',  function () {
Clazz.super_(C$, this);
var searcher=Clazz.new_($I$(1,1));
for (var idcode, $idcode = 0, $$idcode = C$.DEFAULT_TEMPLATE; $idcode<$$idcode.length&&((idcode=($$idcode[$idcode])),1);$idcode++) {
var fragment=Clazz.new_($I$(2,1)).getCompactMolecule$S(idcode);
var ffp=searcher.createLongIndex$com_actelion_research_chem_StereoMolecule(fragment);
var template=Clazz.new_($I$(3,1).c$$com_actelion_research_chem_StereoMolecule$JA$Z,[fragment, ffp, false]);
template.normalizeCoordinates$();
this.add$O(template);
}
}, 1);

C$.$static$=function(){C$.$static$=0;
C$.DEFAULT_TEMPLATE=Clazz.array(String, -1, ["gkvt@@@@LddTTTrbTRTRTRRRRRRRRRRRRRrVRrIh\\IAaQxlY@gRHdJCJcRXlv_CfJx|A\\hRHejiLaQjTje^kSjtFcIhvXmVKMjt{lN{Kavy\\^wGjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjh@@vo@HBC@PhLN@bPhtFKCcpDbILaRhtzCIbsX\\nOO`JDbqDjSKdJeJmQjtz}Ahr[LVkMnpz\\nwGj{PBhBdBlBBBjBfBnBaBiBeBmBcBkBgBoB`bhbdblbbbjbfbnbabibebmbcbkbgbob`RhRdRlRbRjRfRnRaRiReRmRcRkRgRoR`rhrdrlrbrjrfrnrarirermrcrkrgror`JhJdJlJbJjJfJnJaJiJeJmJcJkJgJoJ`jhjdjljbjjjfjnjajijej` !BnkjyVwsVr|iQn|Q|goTZWPIJwbudnRkVYBez]siZymNJZUqNFBqZWxS~iCXVU]SeRjwrtSPAjkvXLpBAZauDPzq]PfMlecrMnkv|@\\SFD`m|mWiEoCXp`SIe_J[l|[XCbloTV`[Gc@FJGopyyoOlFQfUy^w\\Bgz|", "gcrt@@@@LdbbbbTRbRbRbRRRRRRRRRRRRVRrVQIA`HtRGAaIxZAHfShTjCIbqylQGKgqdBaXeQJeruBiPitZmFoPZLFSYbvZlVGMnsZ]vWSmr{]UUUUUUUUUUUUUUUUUUUUUUUUUUUUUT@@[G`DAA`HTFG@QHTZCEaqxBQDfPiTZ]AdqYlNWGgpEBQXbUIerEReVhuZ]^`tYMfKUfwX]NW[jkPBhBdBlBbBjBfBnBaBiBeBmBcBkBgBoB`bhbdblbbbjbfbnbabibebmbcbkbgbob`RhRdRlRbRjRfRnRaRiReRmRcRkRgRoR`rhrdrlrbrjrfrnrarirermrcrkrgror`JhJdJlJbJjJfJnJaJiJeJmJcJkJgJoJ`jhjdjljbjjjfjnjajij` !B^cR]`]Fm]QkfljE\\pUVfgOmFXsQe_gXPyXis_wF|vUUX_XbxpzU]HUFgYViwFo~@uemc@}~TIEPioYVwr]JnM~[ZEC\\g}~o_pUfdo~irsklTLiyVJshnw^iVAsZ`_~}PYkckURH{FYMImFaRaccUlCZSHMfP", "dml@@Dje^VGiyZjjjh@vtHSBinFU@ !BPTCTy[skMzUPF`AJbBixEZHS[Il", "dml@@DjYVvGiyZjjjh@vtHSBinFU@ !BwLo~BJ~UquhXBinZ\\ykA@F_eMrT", "dml@@LdfbbQX^fUZjjj`C[PaLJfxYT !BzxIHVc{OiJVRpprePho~]}ywLl", "deL@@DjUYkfEijjjj@MeBDpj[ad !BaAMVr[AvkKzm_jKvVbD{sk", "dil@@LddTQRl[NX^Fjjjj@MiBDpj[a@ !BPfL@ox@M~T@ox@`C~@@", "daL@@DjYtKJqjynjjjj@MaBDpj[` !B`bL@_gx@@Gy~@Gx@_`@"]);
};
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-18 09:54:18 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
