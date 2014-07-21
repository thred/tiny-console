package io.github.thred.tinyconsole.command;

import java.util.Iterator;

import io.github.thred.tinyconsole.AbstractCommand;
import io.github.thred.tinyconsole.ArgumentException;
import io.github.thred.tinyconsole.Process;
import io.github.thred.tinyconsole.util.Arguments;
import io.github.thred.tinyconsole.util.cron.CronDaemon;
import io.github.thred.tinyconsole.util.cron.CronException;
import io.github.thred.tinyconsole.util.cron.CronExpressionTask;
import io.github.thred.tinyconsole.util.cron.CronTask;

/**
 * Prints text.
 *
 * @author Manfred Hantschel
 */
public class CronCommand extends AbstractCommand
{

    private final CronDaemon cronDaemon;

    public CronCommand(CronDaemon cronDaemon)
    {
        super("cron", "crontab");

        this.cronDaemon = cronDaemon;
    }

    @Override
    public String getFormat()
    {
        return "[add * * * * * command [arg ...]] [cancel id] [list]";
    }

    @Override
    public String getDescription()
    {
        return "A simple cron implementation.";
    }

    @Override
    public int getOrdinal()
    {
        return 610;
    }

    @Override
    public int execute(String commandName, final Process process) throws Exception
    {
        String command = process.args.consumeString();

        if (("add".equalsIgnoreCase(command)) || ("a".equalsIgnoreCase(command)))
        {
            return addTask(process);
        }

        if (("cancel".equalsIgnoreCase(command)) || ("c".equalsIgnoreCase(command)))
        {
            return cancelTask(process);
        }

        if ((command == null) || ("list".equalsIgnoreCase(command)) || ("l".equalsIgnoreCase(command)))
        {
            return listTasks(process);
        }

        throw new ArgumentException("Invalid command: " + command);
    }

    protected int addTask(final Process process) throws CronException
    {
        String minute = process.args.consumeString();
        String hour = process.args.consumeString();
        String dayOfMonth = process.args.consumeString();
        String month = process.args.consumeString();
        String dayOfWeek = process.args.consumeString();

        if ((minute == null) || (hour == null) || (dayOfMonth == null) || (month == null) || (dayOfWeek == null))
        {
            throw new ArgumentException("Cron expression missing");
        }

        final Arguments args = process.args.consume();

        cronDaemon.ensureRunning();
        cronDaemon.addTask(minute, hour, dayOfMonth, month, dayOfWeek, new Runnable()
        {
            @Override
            public void run()
            {
                Thread thread = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        new Process(process, args.clone(), System.in, System.out, System.err).execute();
                    }

                }, "Cron Daemon Sub-Process");

                thread.setDaemon(true);
                thread.start();
            }

            @Override
            public String toString()
            {
                return args.toString();
            }
        });

        process.out.println("Task added.");

        return 0;
    }

    protected int cancelTask(Process process)
    {
        String id = process.args.consumeString();

        if (id == null)
        {
            throw new ArgumentException("Id is missing");
        }

        try
        {
            int code = Integer.parseInt(id, 16);

            if (cronDaemon.removeTask(code))
            {
                process.out.println("Task removed.");

                return 0;
            }
            else
            {
                process.err.println("Task not found.");

                return -1;
            }
        }
        catch (NumberFormatException e)
        {
            throw new ArgumentException("Invalid id.");
        }
    }

    protected int listTasks(Process process)
    {
        process.out.println("List of Tasks:");
        process.out.println("==============");
        process.out.println();

        Iterator<CronTask> iterator = cronDaemon.iterator();

        if (!iterator.hasNext())
        {
            process.out.println("No tasks found.");
        }

        while (iterator.hasNext())
        {
            CronTask task = iterator.next();

            if (task instanceof CronExpressionTask)
            {
                process.out.printf("%08x: %s %s\n", ((CronExpressionTask) task).getCode(),
                    ((CronExpressionTask) task).getExpression(), ((CronExpressionTask) task).getRunnable());
            }
        }

        return 0;
    }

    @Override
    protected String getHelpDescription()
    {
        return "A simple cron implementation.\n\n" //
            + "add       Add a task" //
            + "cancel    Cancel a task" //
            + "list      List all tasks";
    }

}
