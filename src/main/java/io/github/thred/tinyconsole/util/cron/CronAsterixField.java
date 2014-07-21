package io.github.thred.tinyconsole.util.cron;

class CronAsterixField extends AbstractCronField implements CronRangeField
{

    public CronAsterixField(CronFieldType type)
    {
        super(type);
    }

    @Override
    public int getMinimum()
    {
        return getType().getMinimum();
    }

    @Override
    public boolean isRestricted()
    {
        return false;
    }

    @Override
    public boolean isAllowed(CronTime time)
    {
        return true;
    }

    @Override
    public String toString()
    {
        return "*";
    }

}
