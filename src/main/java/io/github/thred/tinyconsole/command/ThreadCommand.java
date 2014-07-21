package io.github.thred.tinyconsole.command;

import io.github.thred.tinyconsole.AbstractCommand;
import io.github.thred.tinyconsole.ArgumentException;
import io.github.thred.tinyconsole.Process;
import io.github.thred.tinyconsole.util.Utils;
import io.github.thred.tinyconsole.util.WildcardUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Prints thread information
 *
 * @author Manfred Hantschel
 */
public class ThreadCommand extends AbstractCommand
{

    public ThreadCommand()
    {
        super("thread", "t");
    }

    @Override
    public String getFormat()
    {
        return "[id ...]";
    }

    @Override
    public String getDescription()
    {
        return "Thread information.";
    }

    @Override
    protected String getHelpDescription()
    {
        return "Displays information gathered form threads. If the command is invoked without an id, "
            + "a summary of all threads will be displayed. If the command is invoked with an id (as thread id), "
            + "detail information of the thread will be displayed. You can use * as id to display detail information "
            + "of all threads.";
    }

    @Override
    public int getOrdinal()
    {
        return 520;
    }

    @Override
    public int execute(String commandName, Process process) throws Exception
    {
        if (process.args.isEmpty())
        {
            return list(process);
        }

        String id;

        while ((id = process.args.consumeString()) != null)
        {
            show(process, id);
        }

        return 0;
    }

    private int list(Process process)
    {
        List<Thread> threads = getThreads();

        int idLength = 0;
        int nameLength = 0;
        int classLength = 0;

        for (Thread thread : threads)
        {
            idLength = Math.max(idLength, String.valueOf(thread.getId()).length());
            nameLength = Math.max(nameLength, thread.getName().length());
            classLength = Math.max(classLength, thread.getClass().getName().length());
        }

        process.out.printf(" %" + idLength + "s | %-" + nameLength + "s | %-" + classLength + "s \n", "ID", "Name",
            "Class");
        process.out.println("-" + Utils.repeat("-", idLength) + "-+-" + Utils.repeat("-", nameLength) + "-+-"
            + Utils.repeat("-", classLength) + "-");

        for (Thread thread : threads)
        {
            process.out.printf(" %" + idLength + "d | %-" + nameLength + "s | %-" + classLength + "s \n",
                thread.getId(), thread.getName(), thread.getClass().getName());
        }

        process.out.println();
        process.out.printf("Thread count: %s\n", threads.size());

        return 0;
    }

    private int show(Process process, String id)
    {
        List<Thread> threads = getThreads();
        boolean hit = false;

        for (Thread thread : threads)
        {
            if (WildcardUtils.match(String.valueOf(thread.getId()), id))
            {
                show(process, thread);

                hit = true;
            }
        }

        if (!hit)
        {
            throw new ArgumentException(String.format("Invalid thread ID: %s", id));
        }

        return 0;
    }

    private void show(Process process, Thread thread)
    {
        String title = thread.getId() + ".) " + thread.getName() + " (" + thread.getClass() + ")";

        process.out.println(title);
        process.out.println(Utils.repeat("-", title.length()));

        StackTraceElement[] stackTraceElements = thread.getStackTrace();

        for (StackTraceElement element : stackTraceElements)
        {
            process.out.println(element);
        }

        process.out.println();
    }

    private List<Thread> getThreads()
    {
        List<Thread> threads = new ArrayList<Thread>(Thread.getAllStackTraces().keySet());

        Collections.sort(threads, new Comparator<Thread>()
            {

            @Override
            public int compare(Thread o1, Thread o2)
            {
                return (int) (o1.getId() - o2.getId());
            }

            });

        return threads;
    }

}
