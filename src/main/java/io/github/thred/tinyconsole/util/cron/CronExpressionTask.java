package io.github.thred.tinyconsole.util.cron;

import java.util.Calendar;

public class CronExpressionTask extends AbstractCronTask
{

    private final CronExpression expression;

    public CronExpressionTask(CronExpression expression, Runnable runnable)
    {
        super(runnable);

        this.expression = expression;

        computeTriggerTimeMillis(0);
    }

    public CronExpression getExpression()
    {
        return expression;
    }

    private void computeTriggerTimeMillis(int delayInMinutes)
    {
        Calendar calendar = Calendar.getInstance();

        if (delayInMinutes > 0)
        {
            calendar.add(Calendar.MINUTE, delayInMinutes);
        }

        setTriggerTimeMillis(expression.next(calendar));
    }

    @Override
    protected void onAfterExecute(CronDaemon daemon)
    {
        computeTriggerTimeMillis(1);

        daemon.enqueue(this);
    }

}
