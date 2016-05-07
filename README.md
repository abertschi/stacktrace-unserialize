# stacktrace-unserialize

> Java StackTrace as String to Throwable

The aim of this project is to provide a utility to unserialize StackTraces from Strings.

## Install
```xml
<dependency>
    <groupId>ch.abertschi.unserialize</groupId>
    <artifactId>stacktrace-unserialize</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>    
```

## Usage
```java
String stacktrace = "java.lang.Error: My Stacktrace as String ...";
StackTraceUnserialize unserialize = new StackTraceUnserialize();

throw unserialize.unserialize(stacktrace);
```

## API
### StackTraceUnserialize\#unserialize(stacktrace: String)

**Parameter**:  

- stacktrace: Stacktrace as String

**Return Type**:  

- Throwable

If an exception type can not be found on classpath, a `java.lang.RuntimeException` will be used instead.

## Examples of serialized stacktraces
```
java.lang.Error: My Exception msg
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

## License
