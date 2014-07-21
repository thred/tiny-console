package io.github.thred.tinyconsole.command;

import io.github.thred.tinyconsole.AbstractCommand;
import io.github.thred.tinyconsole.Process;

/**
 * Prints text.
 *
 * @author Manfred Hantschel
 */
public class EchoCommand extends AbstractCommand
{

    public EchoCommand()
    {
        super("echo", "e");
    }

    @Override
    public String getFormat()
    {
        return "[text ...]";
    }

    @Override
    public String getDescription()
    {
        return "Prints the text.";
    }

    @Override
    public int getOrdinal()
    {
        return 550;
    }

    @Override
    public int execute(String commandName, Process process) throws Exception
    {
        StringBuilder builder = new StringBuilder();
        String argument;

        while ((argument = process.args.consumeString()) != null)
        {
            if (builder.length() > 0)
            {
                builder.append(" ");
            }

            builder.append(argument);
        }

        process.out.println(builder.toString());

        return 0;
    }

    @Override
    protected String getHelpDescription()
    {
        return "Prints the text. May contain ${..} placeholders.";
    }

}
