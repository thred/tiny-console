package io.github.thred.tinyconsole.command;

import io.github.thred.tinyconsole.AbstractCommand;
import io.github.thred.tinyconsole.Process;
import io.github.thred.tinyconsole.util.WildcardUtils;

import java.util.Map.Entry;
import java.util.Properties;

/**
 * Prints and modifies properties.
 * 
 * @author Manfred Hantschel
 */
public class PropCommand extends AbstractCommand
{

    public PropCommand()
    {
        super("prop", "p");
    }

    @Override
    public String getFormat()
    {
        return "[<KEY> [<VALUE>]]";
    }

    @Override
    public String getDescription()
    {
        return "Manage system properties.";
    }

    @Override
    public int getOrdinal()
    {
        return 530;
    }

    @Override
    public int execute(String commandName, Process process) throws Exception
    {
        String key = process.args.consumeString();

        if (key == null)
        {
            key = "*";
        }

        String value = process.args.consumeString();

        if (value == null)
        {
            Properties properties = System.getProperties();

            for (Entry<Object, Object> entry : properties.entrySet())
            {
                if (WildcardUtils.match(String.valueOf(entry.getKey()).toLowerCase(), key.toLowerCase()))
                {
                    process.out.printf("%s = %s\n", entry.getKey(), entry.getValue());
                }
            }
        }
        else
        {
            System.setProperty(key, value);

            process.out.println("Property set.");
        }

        return 0;
    }

    @Override
    protected String getHelpDescription()
    {
        return "Called without arguments, it will display all system properties. "
            + "If one argument is specified, it will display the specified property (the "
            + "key may then contain wildcards). If two arguments are specified, it will "
            + "try to set the specified property to the specified value.";
    }

}
