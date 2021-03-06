[![Maven Central](https://maven-badges.herokuapp.com/maven-central/ch.abertschi.unserialize/stacktrace-unserialize/badge.svg?style=flat)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22ch.abertschi.unserialize%22)
[![Build Status](https://travis-ci.org/abertschi/stacktrace-unserialize.svg?branch=master)](https://travis-ci.org/abertschi/stacktrace-unserialize)
[![codecov](https://codecov.io/gh/abertschi/stacktrace-unserialize/branch/master/graph/badge.svg)](https://codecov.io/gh/abertschi/stacktrace-unserialize)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/6f82ce480b504210ba6c92f909ddb8f7)](https://www.codacy.com/app/abertschi/stacktrace-unserialize?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=abertschi/stacktrace-unserialize&amp;utm_campaign=Badge_Grade)
[![Apache 2](http://img.shields.io/badge/license-APACHE2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

# stacktrace-unserialize

> Java StackTrace as String to Throwable

Unserialize your StackTraces from `String` to `java.lang.Throwable`.

## Install
```xml
<dependency>
    <groupId>ch.abertschi.unserialize</groupId>
    <artifactId>stacktrace-unserialize</artifactId>
    <version>0.0.1</version>
</dependency>    
```

## Usage
```java
String stacktrace = "java.lang.Error: My Stacktrace as String ...";

throw StackTraceUnserialize.unserialize(stacktrace);
```

## API
### StackTraceUnserialize\#unserialize(stacktrace: String)

**Parameter**:  

- stacktrace: Stacktrace as String

**Return Type**:

- Throwable, throws IllegalArgumentException if no stacktrace was found.

## Examples of serialized stacktraces

StackTraces in the format of `Throwable#printStackTrace(PrintWriter)` are supported.

```
java.lang.OutOfMemoryError:
```
- Note the colon after the exception type

```
java.lang.RuntimeException: You pressed the red button.
```

```
java.lang.RuntimeException: org.eclipse.osgi.util.NLS from [Module \deployment.partneraccess-inttest.ear:main\ from Service Module Loader]
       at org.jboss.modules.ModuleClassLoader.defineClass(ModuleClassLoader.java:487) [jboss-modules.jar:1.3.3.Final-redhat-1] 
       at org.jboss.modules.ModuleClassLoader.loadClassLocal(ModuleClassLoader.java:277) [jboss-modules.jar:1.3.3.Final-redhat-1] 
Caused by: java.lang.NoClassDefFoundError: org/eclipse/osgi/util/NLS() 
       at java.lang.ClassLoader.defineClass1(Native Method) [rt.jar:1.8.0_51] 
       at java.lang.ClassLoader.defineClass(ClassLoader.java:760) [rt.jar:1.8.0_51] 
       at org.jboss.modules.ModuleClassLoader.doDefineOrLoadClass(ModuleClassLoader.java:361) [jboss-modules.jar:1.3.3.Final-redhat-1] 
       at org.jboss.modules.ModuleClassLoader.defineClass(ModuleClassLoader.java:482) [jboss-modules.jar:1.3.3.Final-redhat-1] 
       ... 64 more
Caused by: java.lang.ClassNotFoundException: org.eclipse.osgi.util.NLS from [Module \deployment.partneraccess-inttest.ear:main\ from Service Module Loader] 
       at org.jboss.modules.ModuleClassLoader.findClass(ModuleClassLoader.java:213) [jboss-modules.jar:1.3.3.Final-redhat-1] 
       at org.jboss.modules.ConcurrentClassLoader.performLoadClassUnchecked(ConcurrentClassLoader.java:459) [jboss-modules.jar:1.3.3.Final-redhat-1] 
       at org.jboss.modules.ConcurrentClassLoader.performLoadClassChecked(ConcurrentClassLoader.java:408) [jboss-modules.jar:1.3.3.Final-redhat-1] 
       at org.jboss.modules.ConcurrentClassLoader.performLoadClass(ConcurrentClassLoader.java:389) [jboss-modules.jar:1.3.3.Final-redhat-1] 
       at org.jboss.modules.ConcurrentClassLoader.loadClass(ConcurrentClassLoader.java:134) [jboss-modules.jar:1.3.3.Final-redhat-1] 
       ... 68 more;
```

```
Exception in thread "main" java.lang.RuntimeException: My Message 
     at Main.main(StackTraceUnserialize.java:107) 
     at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) 
     at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) 
     at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) 
     at java.lang.reflect.Method.invoke(Method.java:497) 
     at com.intellij.rt.execution.application.AppMain.main(AppMain.java:140) 
     Caused by: java.lang.UnsupportedClassVersionError: My Message
     ... 6 more;
```

You may want to take a look at the [unit tests](https://github.com/abertschi/stacktrace-unserialize/blob/master/src/test/java/ch/abertschi/unserialize/StackTraceUnserializeTest.java) for more examples.

## License
Apache 2.0
