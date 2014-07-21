package io.github.thred.tinyconsole.util.cron;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.DelayQueue;

public class CronDaemon implements Runnable, Iterable<CronTask>
{

    private final List<CronTask> tasks = new ArrayList<CronTask>();
    private final DelayQueue<CronAction> queue = new DelayQueue<CronAction>();

    private Thread thread;

    public CronDaemon()
    {
        super();
    }

    public void start()
    {
        synchronized (queue)
        {
            if (isRunning())
            {
                throw new IllegalStateException("Cron daemon already running");
            }

            queue.clear();

            thread = new Thread(this, "Cron Daemon");

            thread.setDaemon(true);
            thread.start();
        }
    }

    public boolean isRunning()
    {
        return thread != null;
    }

    public void ensureRunning()
    {
        synchronized (queue)
        {
            if (!isRunning())
            {
                start();
            }
        }
    }

    public void stop()
    {
        synchronized (queue)
        {
            if (thread != null)
            {
                thread = null;

                enqueue(new CronStopAction());
            }
        }
    }

    @Override
    public void run()
    {
        try
        {
            while (thread == Thread.currentThread())
            {
                try
                {
                    CronAction action = queue.take();

                    if (action != null)
                    {
                        action.execute(this);
                    }
                }
                catch (InterruptedException e)
                {
                    // I should not ignore this...
                }
            }
        }
        finally
        {
            thread = null;
        }
    }

    protected void enqueue(CronAction action)
    {
        queue.add(action);
    }

    public void addTask(String expression, Runnable runnable) throws CronException
    {
        addTask(new CronExpressionTask(new CronExpression(expression), runnable));
    }

    public void addTask(String minute, String hour, String dayOfMonth, String month, String dayOfWeek, Runnable runnable)
        throws CronException
    {
        addTask(new CronExpressionTask(new CronExpression(minute, hour, dayOfMonth, month, dayOfWeek), runnable));
    }

    protected void addTask(CronExpressionTask task)
    {
        tasks.add(task);

        enqueue(task);
    }

    public boolean removeTask(int code)
    {
        boolean result = false;
        Iterator<CronTask> iterator = tasks.iterator();

        while (iterator.hasNext())
        {
            CronTask task = iterator.next();

            if ((task instanceof AbstractCronTask) && (code == ((AbstractCronTask) task).getCode()))
            {
                task.cancel();
                iterator.remove();

                result = true;
            }
        }

        return result;
    }

    @Override
    public Iterator<CronTask> iterator()
    {
        return Collections.unmodifiableCollection(tasks).iterator();
    }

}
