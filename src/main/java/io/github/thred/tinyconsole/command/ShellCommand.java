package io.github.thred.tinyconsole.command;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import io.github.thred.tinyconsole.AbstractCommand;
import io.github.thred.tinyconsole.ArgumentException;
import io.github.thred.tinyconsole.CLI;
import io.github.thred.tinyconsole.Process;

/**
 * Exits the JVM.
 *
 * @author Manfred Hantschel
 */
public class ShellCommand extends AbstractCommand
{

    public ShellCommand()
    {
        super("shell", "sh");
    }

    @Override
    public String getFormat()
    {
        return "script [arg ...]";
    }

    @Override
    public String getDescription()
    {
        return "Executes a script.";
    }

    @Override
    protected String getHelpDescription()
    {
        return "Executes the specified script.";
    }

    @Override
    public int getOrdinal()
    {
        return 570;
    }

    @Override
    public int execute(String commandName, Process process) throws IOException
    {
        File file = process.args.consumeFile();

        if (file == null)
        {
            throw new ArgumentException("File is missing");
        }

        FileInputStream in = new FileInputStream(file);

        try
        {
            CLI cli = new CLI(in, process.out, process.err);

            return cli.consume();
        }
        finally
        {
            in.close();
        }
    }

}
