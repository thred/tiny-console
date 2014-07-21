package io.github.thred.tinyconsole.util.cron;

import java.util.Calendar;

abstract class AbstractCronTask extends AbstractTimedCronAction implements CronTask
{

    private final Runnable runnable;

    private long triggerTimeMillis;
    private boolean canceled = false;

    public AbstractCronTask(Runnable runnable)
    {
        super();

        this.runnable = runnable;
    }

    public Runnable getRunnable()
    {
        return runnable;
    }

    public int getCode() {
        return hashCode();
    }
    
    @Override
    public void cancel()
    {
        canceled = true;
    }

    @Override
    protected long getTriggerTimeMillis()
    {
        return triggerTimeMillis;
    }

    protected void setTriggerTimeMillis(long triggerTimeMillis)
    {
        this.triggerTimeMillis = triggerTimeMillis;
    }

    protected void setTriggerTimeMillis(Calendar calendar)
    {
        triggerTimeMillis = calendar.getTimeInMillis();
    }

    @Override
    public void execute(CronDaemon daemon)
    {
        if (canceled)
        {
            return;
        }

        try
        {
            runnable.run();
        }
        finally
        {
            onAfterExecute(daemon);
        }
    }

    protected void onAfterExecute(CronDaemon daemon)
    {
        // intentionally left blank
    }

}
