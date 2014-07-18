package io.github.thred.tinyconsole.command;

import io.github.thred.tinyconsole.AbstractCommand;
import io.github.thred.tinyconsole.Command;
import io.github.thred.tinyconsole.Commands;
import io.github.thred.tinyconsole.TinyConsole;
import io.github.thred.tinyconsole.Process;
import io.github.thred.tinyconsole.util.CommandUtils;
import io.github.thred.tinyconsole.util.Utils;
import io.github.thred.tinyconsole.util.WordWrap;

import java.util.Collection;

/**
 * Prints help.
 * 
 * @author Manfred Hantschel
 */
public class HelpCommand extends AbstractCommand
{

    public HelpCommand()
    {
        super("help", "h", "?");
    }

    @Override
    public String getFormat()
    {
        return "[command [arg ...]]";
    }

    @Override
    public String getDescription()
    {
        return "Shows a list of commands and provides help for each command.";
    }

    @Override
    protected String getHelpDescription()
    {
        return "You can call this help, with or without a command.\n"
            + "If called without a command, it shows a list of all possible commands.\n"
            + "If called with a command, it shows a description of the specified command.";

    }

    @Override
    public int getOrdinal()
    {
        return 0;
    }

    @Override
    public int execute(String commandName, Process process) throws Exception
    {
        String command = process.args.consumeString();

        if (command != null)
        {
            showDetailHelp(process, command);
        }
        else
        {
            showHelp(process);
        }

        return 0;
    }

    private void showHelp(Process process)
    {
        Collection<Command> commands = Commands.list();

        int maxNameLength = 0;

        for (Command command : commands)
        {
            maxNameLength = Math.max(maxNameLength, CommandUtils.getNameDescriptor(command, true).length());
        }

        String prefix = Utils.repeat(" ", maxNameLength + 3);

        for (Command command : commands)
        {
            if (command.getOrdinal() < 0)
            {
                continue;
            }

            showHelp(process, command, prefix);
        }

        process.out.println();
        process.out.println(new WordWrap().perform("Using > will pipe the output of any command to a file. "
            + "Arguments may contain ${..} placeholds, to access environment and system properties.",
            TinyConsole.getLineLength()));
    }

    private void showHelp(Process process, Command command, String prefix)
    {
        process.out.printf("%-" + prefix.length() + "s", CommandUtils.getNameDescriptor(command, true));

        process.out
            .println(Utils.prefixLine(
                new WordWrap().perform(command.getDescription(), TinyConsole.getLineLength() - prefix.length()), prefix,
                false));
    }

    private int showDetailHelp(Process process, String name) throws Exception
    {
        Command command = Commands.find(name);

        if (command == null)
        {
            process.err.printf("Unknown command: %s\n", name);

            return -1;
        }

        return command.help(process);
    }

}
