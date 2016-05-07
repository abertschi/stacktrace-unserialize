package ch.abertschi.unserialize;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by abertschi on 28/04/16.
 */
public class StackTraceUnserialize
{
    // match exceptions in form of Caused by: java.lang.ClassNotFoundException: message
    static final Pattern PATTERN_IN_THREAD = Pattern.compile("(Exception in thread )(\"[^\"]*\")([^:]*)(.*)");

    // match exception in form of: java.util.concurrent.CompletionException: message
    static final Pattern PATTERN_PLAIN_EXCEPTION = Pattern.compile("(^[^: ]*:)(.*)");

    // match root cause in form of //Caused by: java.lang.ClassNotFoundException: message
    static final Pattern PATTERN_CAUSED_BY = Pattern.compile("(Caused by: )(\\S*[^:\\(\\)\\t\\r])(.*)"); // TODO: combine with plain exception

    // match stacktrace elements in form of //org.jboss.modules.ModuleClassLoader.findClass(ModuleClassLoader.java:213) [jboss-modules.jar:1.3.3.Final-redhat-1]
    static final Pattern PATTERN_STACKTRACE = Pattern.compile("at ([^(: \\t\\r]*)?\\((.*?)\\)(\\[(.*?)\\])?");

    public StackTraceUnserialize()
    {
    }

    public Throwable unserialize(final String stacktrace)
    {
        Map<Integer, StackTraceElement> elements = parseStackTraceElements(stacktrace);

        TreeMap<Integer, CauseElement> causes = new TreeMap<Integer, CauseElement>(Collections.reverseOrder());
        causes.putAll(parseHeader(stacktrace));
        causes.putAll(parseRootCauses(stacktrace));

        return buildThrowable(causes, elements);
    }

    protected Throwable buildThrowable(Map<Integer, CauseElement> causes, Map<Integer, StackTraceElement> elements)
    {
        Throwable rootThrowable = null;
        Integer lastStackTraceIndex = Integer.MAX_VALUE;
        for (Map.Entry<Integer, CauseElement> cause : causes.entrySet())
        {
            Throwable throwable = lookupThrowable(cause.getValue().getType(), cause.getValue().getMessage());

            TreeMap<Integer, StackTraceElement> stacktrace = new TreeMap<Integer, StackTraceElement>();
            for (Map.Entry<Integer, StackTraceElement> subElement : elements.entrySet())
            {
                if (subElement.getKey() > cause.getKey() && subElement.getKey() < lastStackTraceIndex)
                {
                    stacktrace.put(subElement.getKey(), subElement.getValue());
                }
            }
            if (rootThrowable != null)
            {
                throwable.initCause(rootThrowable);
            }
            throwable.setStackTrace(stacktrace.values().toArray(new StackTraceElement[0]));

            lastStackTraceIndex = cause.getKey();
            rootThrowable = throwable;
        }
        return rootThrowable;
    }


    public Map<Integer, CauseElement> parseHeader(final String trace)
    {
        Map<Integer, CauseElement> returns = new TreeMap<Integer, CauseElement>();
        Matcher matcher = PATTERN_IN_THREAD.matcher(trace);
        while (matcher.find())
        {
            CauseElement element = new CauseElement();
            element.message = matcher.group(3);
            element.type = matcher.group(2);
            returns.put(matcher.start(), element);
        }
        if (returns.isEmpty())
        {
            matcher = PATTERN_PLAIN_EXCEPTION.matcher(trace);
            if (matcher.find())
            {
                CauseElement element = new CauseElement();
                String type = matcher.group(1);
                type = type.substring(0, type.length() - 1);
                element.type = type;
                element.message = matcher.group(2);
                returns.put(matcher.start(), element);
            }
        }
        return returns;
    }

    public Map<Integer, StackTraceElement> parseStackTraceElements(final String trace)
    {
        Map<Integer, StackTraceElement> stack = new TreeMap<Integer, StackTraceElement>();
        Matcher matcher = PATTERN_STACKTRACE.matcher(trace);
        while (matcher.find())
        {
            String className = matcher.group(1);
            String methodName = "";
            String[] classnameSplit = className.split("\\.");
            if (classnameSplit.length > 1)
            {
                className = className.substring(0, className.lastIndexOf("."));
                methodName = classnameSplit[classnameSplit.length - 1];
            }

            String fileName = matcher.group(2);
            int lineNumber = -2;
            if (fileName.contains(":"))
            {
                String line = fileName.substring(fileName.indexOf(":") + 1);
                fileName = fileName.substring(0, fileName.indexOf(":"));
                try
                {
                    lineNumber = Integer.valueOf(line);
                }
                catch (Exception e)
                { // No valid linenumber available
                }
            }
            StackTraceElement element = new StackTraceElement(className, methodName, fileName, lineNumber);
            stack.put(matcher.start(), element);
        }
        return stack;
    }

    public static Map<Integer, CauseElement> parseRootCauses(final String trace)
    {
        Map<Integer, CauseElement> causes = new TreeMap<Integer, CauseElement>();

        Matcher matcher = PATTERN_CAUSED_BY.matcher(trace);
        while (matcher.find())
        {
            String type = matcher.group(2);
            type = type.substring(0, type.length() - 2);
            String msg = matcher.group(3);
            causes.put(matcher.start(), new CauseElement(type, msg));
        }
        return causes;
    }

    public static Throwable lookupThrowable(String type, String msg)
    {
        Object instance = null;
        try
        {
            Class<?> clazz = Class.forName(type);
            Constructor<?> constructor = clazz.getConstructor(String.class);
            constructor.setAccessible(true);
            instance = constructor.newInstance(msg);
        }
        catch (Throwable e)
        {
            try
            {
                Class<?> clazz = Class.forName(type);
                Constructor<?> constructor = clazz.getConstructor();
                constructor.setAccessible(true);
                instance = constructor.newInstance();
            }
            catch (Throwable e1)
            {
                instance = new RuntimeException(msg);
            }
        }

        return (Throwable) instance;
    }

    public static class CauseElement
    {
        private String type;
        private String message;

        public CauseElement() {}

        public CauseElement(String type, String msg)
        {
            this.type = type;
            this.message = msg;
        }

        public String getMessage()
        {
            return message;
        }

        public CauseElement setMessage(String message)
        {
            this.message = message;
            return this;
        }

        public String getType()
        {
            return type;
        }

        public CauseElement setType(String type)
        {
            this.type = type;
            return this;
        }
    }
}
