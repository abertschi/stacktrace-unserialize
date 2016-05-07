package ch.abertschi.unserialize;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by abertschi on 07/05/16.
 */
public class StackTraceUnserializeTest
{
    static final String CAUSE_IN_HEADER = "java.lang.Error: My Exception msg\n" +
            "       at org.jboss.modules.ModuleClassLoader.defineClass(ModuleClassLoader.java:487) [jboss-modules.jar:1.3.3.Final-redhat-1]\n" +
            "       at org.jboss.modules.ModuleClassLoader.loadClassLocal(ModuleClassLoader.java:277) [jboss-modules.jar:1.3.3.Final-redhat-1]\n" +
            "Caused by: java.lang.NoClassDefFoundError: org/eclipse/osgi/util/NLS()\n" +
            "       at java.lang.ClassLoader.defineClass1(Native Method) [rt.jar:1.8.0_51]\n" +
            "       at java.lang.ClassLoader.defineClass(ClassLoader.java:760) [rt.jar:1.8.0_51]\n" +
            "       at org.jboss.modules.ModuleClassLoader.doDefineOrLoadClass(ModuleClassLoader.java:361) [jboss-modules.jar:1.3.3.Final-redhat-1]\n" +
            "       at org.jboss.modules.ModuleClassLoader.defineClass(ModuleClassLoader.java:482) [jboss-modules.jar:1.3.3.Final-redhat-1]\n" +
            "       ... 64 more\n" +
            "Caused by: java.lang.ClassNotFoundException: org.eclipse.osgi.util.NLS from [Module \"deployment.partneraccess-inttest.ear:main\" from Service Module Loader]\n" +
            "       at org.jboss.modules.ModuleClassLoader.findClass(ModuleClassLoader.java:213) [jboss-modules.jar:1.3.3.Final-redhat-1]\n" +
            "       at org.jboss.modules.ConcurrentClassLoader.performLoadClassUnchecked(ConcurrentClassLoader.java:459) [jboss-modules.jar:1.3.3.Final-redhat-1]\n" +
            "       at org.jboss.modules.ConcurrentClassLoader.performLoadClassChecked(ConcurrentClassLoader.java:408) [jboss-modules.jar:1.3.3.Final-redhat-1]\n" +
            "       at org.jboss.modules.ConcurrentClassLoader.performLoadClass(ConcurrentClassLoader.java:389) [jboss-modules.jar:1.3.3.Final-redhat-1]\n" +
            "       at org.jboss.modules.ConcurrentClassLoader.loadClass(ConcurrentClassLoader.java:134) [jboss-modules.jar:1.3.3.Final-redhat-1]\n" +
            "       ... 68 more";


    static final String EXCEPTION_IN_THREAD = "Exception in thread \"main\" java.lang.RuntimeException: alkdsfj\n" +
            "\tat Main.main(StackTraceUnserialize.java:107)\n" +
            "\tat sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\n" +
            "\tat sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\n" +
            "\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n" +
            "\tat java.lang.reflect.Method.invoke(Method.java:497)\n" +
            "\tat com.intellij.rt.execution.application.AppMain.main(AppMain.java:140)\n" +
            "Caused by: java.lang.UnsupportedClassVersionError: bla\n" +
            "\t... 6 more";


    @Test
    public void test_exception_in_header() throws Throwable{
        StackTraceUnserialize serialize = new StackTraceUnserialize();
        Throwable th = serialize.unserialize(CAUSE_IN_HEADER);

        Assert.assertTrue(th instanceof Error);
        Assert.assertEquals(" My Exception msg", th.getMessage()); // TODO remove leading blank
        Assert.assertNotNull(th.getCause());
        Assert.assertTrue(th.getCause() instanceof NoClassDefFoundError);
        Assert.assertEquals("org/eclipse/osgi/util/NLS()", th.getCause().getMessage());
        Assert.assertNotNull(th.getCause().getCause());
        Assert.assertTrue(th.getCause().getCause() instanceof ClassNotFoundException);
        Assert.assertEquals("org.eclipse.osgi.util.NLS from [Module \"deployment.partneraccess-inttest.ear:main\" from Service Module Loader]", th.getCause().getCause().getMessage());
    }

}
