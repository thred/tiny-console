package io.github.thred.tinyconsole.util.cron;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

abstract class AbstractCronAction implements CronAction
{

    public AbstractCronAction()
    {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getDelay(TimeUnit unit)
    {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Delayed o)
    {
        return (int) (getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }

}
