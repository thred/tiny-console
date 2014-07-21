package io.github.thred.tinyconsole.util.cron;

enum CronFieldType
{

    MINUTE(0, 60, true),

    HOUR(0, 24, true),

    DAY_OF_MONTH(1, 31, false),

    MONTH(1, 12, false, "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"),

    DAY_OF_WEEK(0, 7, true, "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT");

    private final int minimum;
    private final int maximum;
    private final boolean overflow;
    private String[] names;

    private CronFieldType(int minimum, int maximum, boolean overflow, String... names)
    {
        this.minimum = minimum;
        this.maximum = maximum;
        this.overflow = overflow;
        this.names = names;
    }

    public String[] getNames()
    {
        return names;
    }

    public int getMinimum()
    {
        return minimum;
    }

    public int getMaximum()
    {
        return maximum;
    }

    public boolean isOverflow()
    {
        return overflow;
    }

    public void setNames(String[] names)
    {
        this.names = names;
    }

    public int increment(int value)
    {
        value += 1;

        if ((overflow) && (value == maximum))
        {
            value = minimum;
        }
        else if ((!overflow) && (value > maximum))
        {
            value = minimum;
        }

        return value;
    }

    public int parse(String s) throws CronException
    {
        if (names != null)
        {
            for (int i = 0; i < names.length; ++i)
            {
                if (names[i].equalsIgnoreCase(s))
                {
                    return minimum + i;
                }
            }
        }

        int value;

        try
        {
            value = Integer.decode(s);
        }
        catch (NumberFormatException e)
        {
            throw new CronException(String.format("Invalid cron field value \"%s\" for field type \"%s\"", s, this), e);
        }

        if (value < minimum)
        {
            throw new CronException(String.format(
                "Cron field value \"%d\" less than minimum \"%d\" for field type \"%s\"", value, minimum, this));
        }

        if (value > maximum)
        {
            throw new CronException(String.format(
                "Cron field value \"%d\" greater than maximum \"%d\" for field type \"%s\"", value, maximum, this));
        }

        if ((overflow) && (value == maximum))
        {
            value = minimum;
        }

        return value;
    }
}
