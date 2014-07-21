package io.github.thred.tinyconsole.util.cron;

class CronValueField extends AbstractCronField
{

    private final int value;

    public CronValueField(CronFieldType type, int value)
    {
        super(type);

        this.value = value;
    }

    @Override
    public boolean isAllowed(CronTime time)
    {
        int value = time.get(getType());

        return this.value == value;
    }

    @Override
    public String toString()
    {
        return String.format("%d", value);
    }

}
