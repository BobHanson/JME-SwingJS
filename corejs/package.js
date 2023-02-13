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
		"com/actelion/research/chem/Canonizer.js", 
		"com/actelion/research/chem/CanonizerBaseValue.js", 
		"com/actelion/research/chem/MolfileCreator.js", 
		"com/actelion/research/chem/MolfileParser.js", 
		"java/awt/datatransfer/DataFlavor.js", 
		"java/awt/datatransfer/MimeType.js", 
		"java/awt/datatransfer/MimeTypeParameterList.js", 
		"java/awt/datatransfer/Transferable.js",
		"java/awt/datatransfer/Clipboard.js",
		"java/awt/dnd/DropTargetDropEvent.js", 
		"java/awt/dnd/DropTargetEvent.js", 
		"java/awt/dnd/peer/DropTargetContextPeer.js", 
		"java/io/BufferedReader.js", 
		"java/io/ByteArrayInputStream.js", 
		"java/io/ByteArrayOutputStream.js", 
		"java/io/Closeable.js", 
		"java/io/File.js", 
		"java/io/FileDescriptor.js", 
		"java/io/FileInputStream.js", 
		"java/io/FileSystem.js", 
		"java/io/InputStream.js", 
		"java/io/OutputStream.js", 
		"java/io/Reader.js", 
		"java/io/StringReader.js", 
		"java/lang/AutoCloseable.js", 
		"java/lang/Readable.js", 
		"java/math/RoundingMode.js", 
		"java/text/AttributedCharacterIterator.js", 
		"java/text/DecimalFormat.js", 
		"java/text/DecimalFormatSymbols.js", 
		"java/text/DigitList.js", 
		"java/text/DontCareFieldPosition.js", 
		"java/text/FieldPosition.js", 
		"java/text/Format.js", 
		"java/text/NumberFormat.js", 
		"java/util/ComparableTimSort.js", 
		"java/util/concurrent/atomic/AtomicInteger.js", 
		"java/util/concurrent/ConcurrentHashMap.js", 
		"java/util/concurrent/ConcurrentMap.js", 
		"java/util/DualPivotQuicksort.js", 
		"java/util/regex/Matcher.js", 
		"java/util/regex/MatchResult.js", 
		"java/util/ResourceBundle.js", 
		"java/util/spi/LocaleServiceProvider.js", 
		"javajs/util/AU.js", 
		"jme/canvas/Graphical2DObjectGroup.js", 
		"jme/io/ChemicalMimeType.js", 
		"jme/io/JMEReader.js", 
		"jme/util/Box.js", 
		"jme/util/Isotopes.js", 
		"jme/util/JMEUtil.js", 
		"sun/text/resources/en/FormatData_en.js", 
		"sun/text/resources/FormatData.js", 
		"sun/util/locale/provider/AuxLocaleProviderAdapter.js", 
		"sun/util/locale/provider/JRELocaleProviderAdapter.js", 
		"sun/util/locale/provider/LocaleDataMetaInfo.js", 
		"sun/util/locale/provider/LocaleProviderAdapter.js", 
		"sun/util/locale/provider/ResourceBundleBasedAdapter.js", 
		"sun/util/locale/provider/SPILocaleProviderAdapter.js", 
		"sun/util/resources/LocaleData.js", 
		"sun/util/resources/ParallelListResourceBundle.js", 
		"swingjs/JSDnD.js"

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
