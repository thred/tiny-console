package io.github.thred.tinyconsole.command;

import io.github.thred.tinyconsole.AbstractCommand;
import io.github.thred.tinyconsole.Process;
import io.github.thred.tinyconsole.util.Utils;

/**
 * Executes a process asynchoniously.
 *
 * @author Manfred Hantschel
 */
public class AmpersandCommand extends AbstractCommand
{

    public AmpersandCommand()
    {
        super("&");
    }

    @Override
    public String getFormat()
    {
        return Utils.EMPTY;
    }

    @Override
    public String getDescription()
    {
        return "Executes a process asynchoniously.";
    }

    @Override
    public int getOrdinal()
    {
        return -1;
    }

    @Override
    public int execute(String commandName, final Process process) throws Exception
    {
        Thread thread = new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                int result = -1;

                try
                {
                    result = process.parent.execute();
                }
                finally
                {
                    process.out.printf("Finished [%s]: %s\n", Thread.currentThread().getId(), result);
                }
            }

        }, "Shell Sub-Process");

        thread.start();

        process.out.printf("Started [%s]\n", thread.getId());

        return 0;
    }

    @Override
    protected String getHelpDescription()
    {
        return "Executes a process asynchoniously.";
    }

}
