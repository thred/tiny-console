package io.github.thred.tinyconsole.util.cron;

abstract class AbstractCronField implements CronField
{

    private final CronFieldType type;

    public AbstractCronField(CronFieldType type)
    {
        super();

        this.type = type;
    }

    @Override
    public CronFieldType getType()
    {
        return type;
    }

    @Override
    public boolean isRestricted()
    {
        return true;
    }

}
