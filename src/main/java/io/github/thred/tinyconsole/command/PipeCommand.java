package io.github.thred.tinyconsole.command;

import io.github.thred.tinyconsole.AbstractCommand;
import io.github.thred.tinyconsole.ArgumentException;
import io.github.thred.tinyconsole.Process;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Redirects output.
 *
 * @author Manfred Hantschel
 */
public class PipeCommand extends AbstractCommand
{

    public PipeCommand()
    {
        super(">", ">>");
    }

    @Override
    public String getFormat()
    {
        return "[file]";
    }

    @Override
    public String getDescription()
    {
        return "Writes the input stream to the file.";
    }

    @Override
    public int getOrdinal()
    {
        return -1;
    }

    @Override
    public int execute(String commandName, Process process) throws Exception
    {
        boolean append = ">>".equals(commandName);
        File file = process.args.consumeFile();

        PrintStream out;

        if (file != null)
        {
            process.out.printf("Redirecting output to %s\n", file.getAbsolutePath());

            out = new PrintStream(new FileOutputStream(file, append));
        }
        else
        {
            out = System.out;
        }

        try
        {
            if (process.args.size() > 0)
            {
                throw new ArgumentException("Too many arguments");
            }

            if (process.parent != null)
            {
                process.parent.redirect(process.in, out, out).execute();
            }
        }
        finally
        {
            if (out != System.out)
            {
                out.close();
            }
        }
        return 0;
    }

    @Override
    protected String getHelpDescription()
    {
        return "Writes the input stream to the specified file. " //
            + "This command is used for > and >> pipe commands. " //
            + "If -a is specified, it will append the output to the specified file.";
    }

}
