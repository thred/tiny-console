package io.github.thred.tinyconsole.command;

import io.github.thred.tinyconsole.AbstractCommand;
import io.github.thred.tinyconsole.ArgumentException;
import io.github.thred.tinyconsole.Process;

/**
 * Prints text.
 *
 * @author Manfred Hantschel
 */
public class SleepCommand extends AbstractCommand
{

    public SleepCommand()
    {
        super("sleep");
    }

    @Override
    public String getFormat()
    {
        return "seconds";
    }

    @Override
    public String getDescription()
    {
        return "Sleeps some seconds.";
    }

    @Override
    public int getOrdinal()
    {
        return 580;
    }

    @Override
    public int execute(String commandName, Process process) throws Exception
    {
        Double duration = process.args.consumeDouble();

        if (duration == null)
        {
            throw new ArgumentException("Duration missing");
        }

        Thread.sleep((long) (duration.doubleValue() * 1000));

        return 0;
    }

    @Override
    protected String getHelpDescription()
    {
        return "Sleeps some seconds\n\n" //
            + "<SECONDS>   The number of seconds (float value)";
    }

}
