(function(){var P$=Clazz.newPackage("com.actelion.research.util"),I$=[[0,'java.awt.Color']],I$0=I$[0],$I$=function(i,n,m){return m?$I$(i)[n].apply(null,m):((i=(I$[i]||(I$[i]=Clazz.load(I$0[i])))),!n&&i.$load$&&Clazz.load(i,2),i)};
/*c*/var C$=Clazz.newClass(P$, "ColorHelper");

C$.$clinit$=2;

Clazz.newMeth(C$, '$init$', function () {
},1);

C$.$fields$=[[]
,['O',['PERCEIVED_BRIGHTNESS','float[]']]]

Clazz.newMeth(C$, 'intermediateColor$java_awt_Color$java_awt_Color$F',  function (c1, c2, ratio) {
return Clazz.new_([((c1.getRed$() + ratio * (c2.getRed$() - c1.getRed$()))|0), ((c1.getGreen$() + ratio * (c2.getGreen$() - c1.getGreen$()))|0), ((c1.getBlue$() + ratio * (c2.getBlue$() - c1.getBlue$()))|0)],$I$(1,1).c$$I$I$I);
}, 1);

Clazz.newMeth(C$, 'intermediateColor$I$I$F',  function (rgb1, rgb2, ratio) {
var r1=(rgb1 & 16711680) >> 16;
var g1=(rgb1 & 65280) >> 8;
var b1=rgb1 & 255;
var r2=(rgb2 & 16711680) >> 16;
var g2=(rgb2 & 65280) >> 8;
var b2=rgb2 & 255;
return (r1 + (Math.round(ratio * (r2 - r1))) << 16) + (g1 + (Math.round(ratio * (g2 - g1))) << 8) + b1 + (Math.round(ratio * (b2 - b1))) ;
}, 1);

Clazz.newMeth(C$, 'brighter$java_awt_Color$F',  function (c, factor) {
var r=c.getRed$();
var g=c.getGreen$();
var b=c.getBlue$();
var alpha=c.getAlpha$();
var i=((1.0 / (1.0 - factor))|0);
if (r == 0 && g == 0  && b == 0 ) {
return Clazz.new_($I$(1,1).c$$I$I$I$I,[i, i, i, alpha]);
}if (r > 0 && r < i ) r=i;
if (g > 0 && g < i ) g=i;
if (b > 0 && b < i ) b=i;
return Clazz.new_([Math.min(((r / factor)|0), 255), Math.min(((g / factor)|0), 255), Math.min(((b / factor)|0), 255), alpha],$I$(1,1).c$$I$I$I$I);
}, 1);

Clazz.newMeth(C$, 'brighter$I$F',  function (argb, factor) {
var alpha=argb & -16777216;
var r=(argb & 16711680) >> 16;
var g=(argb & 65280) >> 8;
var b=argb & 255;
var i=((1.0 / (1.0 - factor))|0);
if (r == 0 && g == 0  && b == 0 ) return alpha | (i << 16) | (i << 8) | i ;
if (r > 0 && r < i ) r=i;
if (g > 0 && g < i ) g=i;
if (b > 0 && b < i ) b=i;
return alpha | (Math.min(((r / factor)|0), 255) << 16) | (Math.min(((g / factor)|0), 255) << 8) | Math.min(((b / factor)|0), 255) ;
}, 1);

Clazz.newMeth(C$, 'darker$java_awt_Color$F',  function (c, factor) {
return Clazz.new_([Math.max(((c.getRed$() * factor)|0), 0), Math.max(((c.getGreen$() * factor)|0), 0), Math.max(((c.getBlue$() * factor)|0), 0), c.getAlpha$()],$I$(1,1).c$$I$I$I$I);
}, 1);

Clazz.newMeth(C$, 'darker$I$F',  function (argb, factor) {
return (argb & -16777216) | (Math.round(factor * ((argb & 16711680) >> 16)) << 16) | (Math.round(factor * ((argb & 65280) >> 8)) << 8) | Math.round(factor * (argb & 255)) ;
}, 1);

Clazz.newMeth(C$, 'perceivedBrightness$java_awt_Color',  function (c) {
return (c == null ) ? 1.0 : (C$.PERCEIVED_BRIGHTNESS[0] * c.getRed$() + C$.PERCEIVED_BRIGHTNESS[1] * c.getGreen$() + C$.PERCEIVED_BRIGHTNESS[2] * c.getBlue$()) / 255.0;
}, 1);

Clazz.newMeth(C$, 'perceivedBrightness$FA',  function (cc) {
return C$.PERCEIVED_BRIGHTNESS[0] * cc[0] + C$.PERCEIVED_BRIGHTNESS[1] * cc[1] + C$.PERCEIVED_BRIGHTNESS[2] * cc[2];
}, 1);

Clazz.newMeth(C$, 'perceivedBrightness$I',  function (argb) {
return (C$.PERCEIVED_BRIGHTNESS[0] * ((argb & 16711680) >> 16) + C$.PERCEIVED_BRIGHTNESS[1] * ((argb & 65280) >> 8) + C$.PERCEIVED_BRIGHTNESS[2] * (argb & 255)) / 255.0;
}, 1);

Clazz.newMeth(C$, 'createColor$java_awt_Color$F',  function (c, perceivedBrightness) {
var cc=c.getRGBComponents$FA(null);
C$.createColor$FA$F(cc, perceivedBrightness);
return Clazz.new_($I$(1,1).c$$F$F$F$F,[cc[0], cc[1], cc[2], cc[3]]);
}, 1);

Clazz.newMeth(C$, 'createColor$I$F',  function (argb, perceivedBrightness) {
var cc=Clazz.array(Float.TYPE, [4]);
var f=0.003921569;
cc[0]=f * ((argb & 16711680) >> 16);
cc[1]=f * ((argb & 65280) >> 8);
cc[2]=f * (argb & 255);
C$.createColor$FA$F(cc, perceivedBrightness);
return (argb & -16777216) | (Math.round(cc[0] * 255) << 16) | (Math.round(cc[1] * 255) << 8) | Math.round(cc[2] * 255) ;
}, 1);

Clazz.newMeth(C$, 'createColor$FA$F',  function (cc, perceivedBrightness) {
var pb=C$.perceivedBrightness$FA(cc);
if (pb == 0.0 ) {
cc[0]=0.0;
cc[1]=0.0;
cc[2]=0.0;
return;
}var f=perceivedBrightness / pb;
var surplusBrightness=0.0;
var sum=0.0;
for (var i=0; i < 3; i++) {
cc[i]*=f;
if (cc[i] < 1.0 ) {
sum+=C$.PERCEIVED_BRIGHTNESS[i];
} else {
surplusBrightness+=(cc[i] - 1.0) * C$.PERCEIVED_BRIGHTNESS[i];
cc[i]=1.0;
}}
if (surplusBrightness != 0 ) {
var remainingBrightness=0.0;
for (var i=0; i < 3; i++) {
if (cc[i] < 1.0 ) {
cc[i]+=surplusBrightness / sum;
if (cc[i] > 1.0 ) {
remainingBrightness+=(cc[i] - 1.0) * C$.PERCEIVED_BRIGHTNESS[i];
cc[i]=1.0;
}}}
if (remainingBrightness != 0.0 ) {
for (var i=0; i < 3; i++) {
if (cc[i] < 1.0 ) {
cc[i]+=remainingBrightness / C$.PERCEIVED_BRIGHTNESS[i];
if (cc[i] > 1.0 ) {
cc[i]=1.0;
}}}
}}}, 1);

Clazz.newMeth(C$, 'getContrastColor$java_awt_Color$java_awt_Color',  function (fg, bg) {
var bgb=C$.perceivedBrightness$java_awt_Color(bg);
var fgb=C$.perceivedBrightness$java_awt_Color(fg);
var contrast=Math.abs(bgb - fgb);
if (contrast > 0.3 ) return fg;
var hsbBG=$I$(1,"RGBtoHSB$I$I$I$FA",[bg.getRed$(), bg.getGreen$(), bg.getBlue$(), null]);
var hsbFG=$I$(1,"RGBtoHSB$I$I$I$FA",[fg.getRed$(), fg.getGreen$(), fg.getBlue$(), null]);
var hueDif=Math.abs(hsbFG[0] - hsbBG[0]);
if (hueDif > 0.5 ) hueDif=1.0 - hueDif;
var saturationFactor=1 - Math.max(hsbFG[1], hsbBG[1]);
var brightnessFactor=Math.abs(fgb + bgb - 1);
var hueDifferenceFactor=Math.cos(3.141592653589793 * hueDif * 3 );
var neededContrast=0.3 * Math.max(saturationFactor, Math.max(brightnessFactor, hueDifferenceFactor));
if (contrast > neededContrast ) return fg;
var darken=(fgb > bgb ) ? (fgb + neededContrast > 1.0 ) : (fgb - neededContrast > 0.0 );
return C$.createColor$java_awt_Color$F(fg, darken ? bgb - neededContrast : bgb + neededContrast);
}, 1);

Clazz.newMeth(C$, 'getContrastColor$I$I',  function (fgRGB, bgRGB) {
var bgb=C$.perceivedBrightness$I(bgRGB);
var fgb=C$.perceivedBrightness$I(fgRGB);
var contrast=Math.abs(bgb - fgb);
if (contrast > 0.3 ) return fgRGB;
var hsbBG=$I$(1,"RGBtoHSB$I$I$I$FA",[(bgRGB & 16711680) >> 16, (bgRGB & 65280) >> 8, bgRGB & 255, null]);
var hsbFG=$I$(1,"RGBtoHSB$I$I$I$FA",[(fgRGB & 16711680) >> 16, (fgRGB & 65280) >> 8, fgRGB & 255, null]);
var hueDif=Math.abs(hsbFG[0] - hsbBG[0]);
if (hueDif > 0.5 ) hueDif=1.0 - hueDif;
var saturationFactor=1 - Math.max(hsbFG[1], hsbBG[1]);
var brightnessFactor=Math.abs(fgb + bgb - 1);
var hueDifferenceFactor=Math.cos(3.141592653589793 * hueDif * 3 );
var neededContrast=0.3 * Math.max(saturationFactor, Math.max(brightnessFactor, hueDifferenceFactor));
if (contrast > neededContrast ) return fgRGB;
var darken=(fgb > bgb ) ? (fgb + neededContrast > 1.0 ) : (fgb - neededContrast > 0.0 );
return C$.createColor$I$F(fgRGB, darken ? bgb - neededContrast : bgb + neededContrast);
}, 1);

C$.$static$=function(){C$.$static$=0;
C$.PERCEIVED_BRIGHTNESS=Clazz.array(Float.TYPE, -1, [0.299, 0.587, 0.114]);
};

Clazz.newMeth(C$);
})();
;Clazz.setTVer('3.3.1-v5');//Created 2023-01-25 13:08:06 Java2ScriptVisitor version 3.3.1-v5 net.sf.j2s.core.jar version 3.3.1-v5
