package io.github.thred.tinyconsole.util;

import io.github.thred.tinyconsole.Command;

/**
 * Common utils for the {@link Command}s
 * 
 * @author Manfred Hantschel
 */
public class CommandUtils
{

    /**
     * Creates a nicly formatted description of the command.
     * 
     * @param command the command
     * @param withFormat true to add the format descriptor
     * @return the description of the command
     */
    public static String getNameDescriptor(Command command, boolean withFormat)
    {
        StringBuilder result = new StringBuilder();

        for (String name : command.getNames())
        {
            if (result.length() > 0)
            {
                result.append(", ");
            }

            result.append(name);
        }

        if ((withFormat) && (command.getFormat().length() > 0))
        {
            result.append(" <arg>");
        }

        return result.toString();
    }

    /**
     * Returns the format description with the first name.
     * 
     * @param command the command
     * @return the format description
     */
    public static String getFormatDescriptor(Command command)
    {
        return command.getNames()[0] + " " + command.getFormat();
    }

}
