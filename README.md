# JME-SwingJS
Adaptation of JME for Swing in Java, with automatic transpilation to JavaScript using java2script/SwingJS

This project adapts JME/Java to use Swing components. 
It utilizes the java2script Eclipse plug-in transpiler 
to generate JavaScript currently with Java classes. (j2s is a "compilation follower", using the same Abstract Syntax Tree 
used by the Java compiler to create JavaScript files instead of Java class files.) 

The SwingJS JavaScript library is then used to deliver the 
Java application (now JavaScript) within a browser. 

The java2script Eclipse plugin can be found within the swingjs/ folder. The project uses the Java-8 version of this, 
and whatever Java compiler is used, it should be set to Java-8 level.
