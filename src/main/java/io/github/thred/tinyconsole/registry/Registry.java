package io.github.thred.tinyconsole.registry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * A global implementation of a registry, that holds instances of classes (services).
 * 
 * @author Manfred Hantschel
 */
public class Registry
{

    private static final List<Class<?>> pendingTypes = new ArrayList<Class<?>>();
    private static final List<Class<?>> processingTypes = new ArrayList<Class<?>>();
    private static final List<Object> instances = new ArrayList<Object>();

    static
    {
        try
        {
            registerContributions();
        }
        catch (ClassNotFoundException | IOException e)
        {
            throw new ExceptionInInitializerError(e);
        }
    }

    protected static void registerContributions() throws IOException, ClassNotFoundException
    {
        Collection<String> contributions = new LinkedHashSet<String>();
        Enumeration<URL> resources =
            Registry.class.getClassLoader().getResources("META-INF/services/" + Registry.class.getName());

        while (resources.hasMoreElements())
        {
            URL resource = resources.nextElement();
            InputStream in = resource.openStream();

            try
            {
                read(contributions, in);
            }
            finally
            {
                in.close();
            }
        }

        for (String contribution : contributions)
        {
            register(Class.forName(contribution));
        }
    }

    /**
     * Registers an service
     * 
     * @param type the type
     */
    public static void register(Class<?> type)
    {
        pendingTypes.add(type);
    }

    /**
     * Registers an service.
     * 
     * @param instance the instance of the service
     */
    public static void register(Object instance)
    {
        instances.add(instance);
    }

    @SuppressWarnings("unchecked")
    public static <TYPE> Instances<TYPE> resolve(Class<TYPE> type)
    {
        Instances<TYPE> result = new Instances<TYPE>(type);

        processPending(type);

        for (Object instance : instances)
        {
            if (type.isInstance(instance))
            {
                result.add((TYPE) instance);
            }
        }

        return result;
    }

    protected static <TYPE> void processPending(Class<TYPE> type)
    {
        if (pendingTypes.isEmpty())
        {
            return;
        }

        synchronized (pendingTypes)
        {
            for (Class<?> currentType : new ArrayList<Class<?>>(pendingTypes))
            {
                if (type.isAssignableFrom(currentType))
                {
                    register(instantiate(currentType));
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected static <TYPE> TYPE instantiate(Class<TYPE> type)
    {
        synchronized (pendingTypes)
        {
            if (processingTypes.contains(type))
            {
                throw new IllegalArgumentException(
                    String
                        .format("Failed to instantiate %s. The type is current instantiating. There is a circular dependency."));
            }

            processingTypes.add(type);

            try
            {
                Constructor<?>[] constructors = type.getConstructors();

                if ((constructors.length < 1) || (constructors.length > 1))
                {
                    throw new IllegalArgumentException(String.format(
                        "Failed to instantiate %s. There must be exactly one constructor", type));
                }

                Constructor<?> constructor = constructors[0];
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                Object[] parameters = new Object[parameterTypes.length];

                for (int i = 0; i < parameterTypes.length; i += 1)
                {
                    Instances<?> instances = resolve(parameterTypes[i]);

                    if (instances.isEmpty())
                    {
                        parameters[i] = null;
                    }
                    else
                    {
                        parameters[i] = instances.first();
                    }
                }

                TYPE result = (TYPE) constructor.newInstance(parameters);

                pendingTypes.remove(type);

                return result;
            }
            catch (SecurityException e)
            {
                throw new IllegalArgumentException(String.format("Failed to access %s for security reasons", type), e);
            }
            catch (IllegalArgumentException e)
            {
                throw new IllegalArgumentException(String.format("Failed to instantiate %s", type), e);
            }
            catch (InstantiationException e)
            {
                throw new IllegalArgumentException(String.format("Failed to instantiate %s", type), e);
            }
            catch (IllegalAccessException e)
            {
                throw new IllegalArgumentException(String.format("Failed to access %s", type), e);
            }
            catch (InvocationTargetException e)
            {
                throw new IllegalArgumentException(String.format("Failed to instantiate %s", type), e);
            }
            finally
            {
                processingTypes.remove(type);
            }
        }
    }

    protected static void read(Collection<String> results, InputStream in) throws IOException
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            try
            {
                String line;

                while ((line = reader.readLine()) != null)
                {
                    if (line.trim().length() == 0)
                    {
                        continue;
                    }

                    if (line.startsWith("#"))
                    {
                        continue;
                    }

                    results.add(line.trim());
                }
            }
            finally
            {
                reader.close();
            }
        }
        catch (UnsupportedEncodingException e)
        {
            throw new IllegalArgumentException("Danger! Danger! Universe imploding!", e);
        }
    }

}
