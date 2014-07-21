package io.github.thred.tinyconsole;

import io.github.thred.tinyconsole.registry.Instances;
import io.github.thred.tinyconsole.registry.Registry;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Commands
{

    public static Command find(String name)
    {
        Instances<Command> instances = Registry.resolve(Command.class);

        for (Command command : instances)
        {
            String[] names = command.getNames();

            for (String current : names)
            {
                if (current.equals(name))
                {
                    return command;
                }
            }
        }

        return null;
    }

    public static Collection<Command> list()
    {
        List<Command> result = Registry.resolve(Command.class).toList();

        Collections.sort(result, new Comparator<Command>()
            {
            @Override
            public int compare(Command o1, Command o2)
            {
                return o1.getOrdinal() - o2.getOrdinal();
            }
            });

        return result;
    }

}
