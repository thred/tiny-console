package io.github.thred.tinyconsole.util.cron;

class CronExplicitRangeField extends AbstractCronField implements CronRangeField
{

    private final int minimum;
    private final int maximum;

    public CronExplicitRangeField(CronFieldType type, int minimum, int maximum)
    {
        super(type);

        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public int getMinimum()
    {
        return minimum;
    }

    public int getMaximum()
    {
        return maximum;
    }

    @Override
    public boolean isAllowed(CronTime time)
    {
        int value = time.get(getType());

        return ((value >= minimum) && (value <= maximum));
    }

    @Override
    public String toString()
    {
        return String.format("%d-%d", minimum, maximum);
    }

}
