package io.github.thred.tinyconsole.util.cron;

import java.util.concurrent.TimeUnit;

abstract class AbstractTimedCronAction extends AbstractCronAction
{

    public AbstractTimedCronAction()
    {
        super();
    }

    protected abstract long getTriggerTimeMillis();

    /**
     * {@inheritDoc}
     */
    @Override
    public long getDelay(TimeUnit unit)
    {
        return unit.convert(getTriggerTimeMillis() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

}
