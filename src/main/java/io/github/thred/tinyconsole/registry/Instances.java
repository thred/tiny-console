package io.github.thred.tinyconsole.registry;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Instances<TYPE> implements Iterable<TYPE>
{

    private final List<TYPE> instances = new ArrayList<>();

    private final Class<TYPE> type;

    public Instances(Class<TYPE> type)
    {
        super();

        this.type = type;
    }

    public Class<TYPE> getType()
    {
        return type;
    }

    protected void add(TYPE instance)
    {
        instances.add(instance);
    }

    public boolean isEmpty()
    {
        return instances.isEmpty();
    }

    public int size()
    {
        return instances.size();
    }

    public TYPE first()
    {
        if (isEmpty())
        {
            throw new IllegalArgumentException("No instance available");
        }

        return instances.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<TYPE> iterator()
    {
        return instances.iterator();
    }

    @SuppressWarnings("unchecked")
    public TYPE[] toArray()
    {
        return instances.toArray((TYPE[]) Array.newInstance(type, instances.size()));
    }

    public List<TYPE> toList()
    {
        return new ArrayList<TYPE>(instances);
    }

    @Override
    public String toString()
    {
        return instances.toString();
    }

}
