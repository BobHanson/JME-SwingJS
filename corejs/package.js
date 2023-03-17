// BH 12/15/2012 1:56:28 PM  adds corezip.z.js and corebio.z.js
// later additions include coresym.z.js, coresurface.z.js, coremenu.z.js

// NOTE: Any changes here must also be reflected in build_03_tocore.xml

(function (ClazzLoader) {

	if (J2S.getGlobal("java.packaged"))
		return;
	J2S.setGlobal("java.packaged", true);

  if (J2S._debugCode)
    return;

	var	base = ClazzLoader.getJ2SLibBase() + "core/";

// note - we don't need to list ALL the classes -- only the ones that are entry points.
// several more classes are in each of these files

	
	ClazzLoader.jarClasspath (base + "core_jme-dnd.z.js",	[  
		"swingjs/JSDnD", "\
java/awt/datatransfer/Transferable", "\
java/io/File", "\
java/io/FileSystem", "\
java/awt/dnd/peer/DropTargetContextPeer", "\
java/awt/dnd/DropTargetDropEvent", "\
java/awt/dnd/DropTargetEvent", "\
java/awt/datatransfer/DataFlavor", "\
java/io/InputStream", "\
java/io/Closeable", "\
java/lang/AutoCloseable", "\
java/awt/datatransfer/MimeTypeParameterList", "\
java/awt/datatransfer/MimeType", "\
java/io/ByteArrayOutputStream", "\
java/io/OutputStream", "\
java/io/FileInputStream", "\
java/io/FileDescriptor", "\
java/util/concurrent/atomic/AtomicInteger", "\
javajs/util/AU", "\
java/io/ByteArrayInputStream", "\
jme/io/JMEReader", "\
java/util/regex/Matcher", "\
java/util/regex/MatchResult", "\
jme/io/ChemicalMimeType", "\
com/actelion/research/chem/MolfileParser", "\
java/io/StringReader", "\
java/io/Reader", "\
java/lang/Readable", "\
java/io/BufferedReader", "\
com/actelion/research/chem/MolfileCreator", "\
java/text/DecimalFormatSymbols", "\
sun/util/resources/LocaleData", "\
java/util/ResourceBundle", "\
sun/util/locale/provider/LocaleDataMetaInfo", "\
sun/text/resources/FormatData", "\
sun/util/resources/ParallelListResourceBundle", "\
sun/util/locale/provider/LocaleProviderAdapter", "\
sun/text/resources/en/FormatData_en", "\
java/text/DecimalFormat", "\
java/text/NumberFormat", "\
java/text/Format", "\
java/text/FieldPosition", "\
java/text/DigitList", "\
java/math/RoundingMode", "\
com/actelion/research/chem/Canonizer", "\
com/actelion/research/chem/CanonizerBaseValue", "\
java/util/ComparableTimSort", "\
java/util/DualPivotQuicksort", "\
java/text/DontCareFieldPosition", "\
java/text/AttributedCharacterIterator", "\
jme/util/Isotopes", "\
jme/canvas/Graphical2DObjectGroup", "\
jme/util/Box"

	]);
	                                                 	
	ClazzLoader.jarClasspath (base + "core_jme-svg.z.js",	[  
		"jme/io/JMESVGWriter",
		"com/actelion/research/chem/SVGDepictor",
		"com/actelion/research/chem/AbstractDepictor",
		"com/actelion/research/gui/generic/GenericRectangle",
		"com/actelion/research/gui/generic/GenericShape",
		"com/actelion/research/chem/DepictorTransformation",
		"com/actelion/research/gui/generic/GenericPoint",
		"com/actelion/research/util/ColorHelper"
	]);
		                                                 	
}) (Clazz._Loader);
