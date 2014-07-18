package io.github.thred.tinyconsole.command;

import io.github.thred.tinyconsole.AbstractCommand;
import io.github.thred.tinyconsole.Process;
import io.github.thred.tinyconsole.util.Utils;

/**
 * Exits the JVM.
 *  
 * @author Manfred Hantschel
 */
public class ExitCommand extends AbstractCommand
{

    public ExitCommand()
    {
        super("exit", "x");
    }

    @Override
    public String getFormat()
    {
        return Utils.EMPTY;
    }

    @Override
    public String getDescription()
    {
        return "Exits the VM.";
    }

    @Override
    protected String getHelpDescription()
    {
        return "Exists the VM. The server may not have enougth time to shutdown gracefully.";
    }

    @Override
    public int getOrdinal()
    {
        return 590;
    }

    @Override
    public int execute(String commandName, Process process)
    {
        process.out.println("Bye.");

        System.exit(0);

        return 0;
    }

}
