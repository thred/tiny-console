package io.github.thred.tinyconsole.util.cron;

import java.util.Calendar;

class CronTime
{

    private final Calendar calendar;

    public CronTime(Calendar calendar)
    {
        super();

        this.calendar = calendar;
    }

    public Calendar getCalendar()
    {
        return calendar;
    }

    public int get(CronFieldType type)
    {
        switch (type)
        {
            case DAY_OF_MONTH:
                return getDayOfMonth();

            case DAY_OF_WEEK:
                return getDayOfWeek();

            case HOUR:
                return getHour();

            case MINUTE:
                return getMinute();

            case MONTH:
                return getMonth();
        }

        throw new UnsupportedOperationException(String.format("Unsupported field type: %s", type));
    }

    public CronTime resetSeconds()
    {
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return this;
    }

    public int getMinute()
    {
        return calendar.get(Calendar.MINUTE);
    }

    public CronTime incMinute(int minutes)
    {
        calendar.add(Calendar.MINUTE, minutes);

        return this;
    }

    public CronTime resetMinute()
    {
        calendar.set(Calendar.MINUTE, 0);

        return this;
    }

    public int getHour()
    {
        return calendar.get(Calendar.HOUR);
    }

    public CronTime incHour(int hours)
    {
        calendar.add(Calendar.HOUR, hours);

        return this;
    }

    public CronTime resetHour()
    {
        calendar.set(Calendar.HOUR, 0);

        return this;
    }

    public int getDayOfMonth()
    {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public CronTime incDayOfMonth(int days)
    {
        calendar.add(Calendar.DAY_OF_MONTH, days);

        return this;
    }

    public CronTime resetDayOfMonth()
    {
        calendar.set(Calendar.DAY_OF_MONTH, 0);

        return this;
    }

    public int getMonth()
    {
        return calendar.get(Calendar.MONTH) + 1;
    }

    public CronTime incMonth(int months)
    {
        calendar.add(Calendar.MONTH, months);

        return this;
    }

    public CronTime resetMonth()
    {
        calendar.set(Calendar.MONTH, 0);

        return this;
    }

    public int getDayOfWeek()
    {
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

}
