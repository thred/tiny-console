package io.github.thred.tinyconsole.util.cron;

class CronIncrementField extends AbstractCronField
{

    private final CronField rangeField;
    private final int increment;

    public CronIncrementField(CronFieldType type, CronField rangeField, int increment)
    {
        super(type);

        this.rangeField = rangeField;
        this.increment = increment;
    }

    @Override
    public boolean isAllowed(CronTime time)
    {
        if (!rangeField.isAllowed(time))
        {
            return false;
        }

        int value = time.get(getType());

        return (value % increment) == 0;
    }

    @Override
    public String toString()
    {
        return String.format("%s/%d", rangeField, increment);
    }

}
