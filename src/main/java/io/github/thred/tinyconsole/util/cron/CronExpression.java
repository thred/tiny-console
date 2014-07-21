package io.github.thred.tinyconsole.util.cron;

import java.util.Calendar;

class CronExpression
{

    private static final CronFieldType[] TYPES = CronFieldType.values();

    private static final int MINUTE = CronFieldType.MINUTE.ordinal();
    private static final int HOUR = CronFieldType.HOUR.ordinal();
    private static final int DAY_OF_MONTH = CronFieldType.DAY_OF_MONTH.ordinal();
    private static final int MONTH = CronFieldType.MONTH.ordinal();
    private static final int DAY_OF_WEEK = CronFieldType.DAY_OF_WEEK.ordinal();

    private final CronField[] fields = new CronField[TYPES.length];

    public CronExpression(String expression) throws CronException
    {
        this(expression.split("\\s"));
    }

    public CronExpression(String... fieldExpressions) throws CronException
    {
        super();

        if (fieldExpressions.length != TYPES.length)
        {
            throw new CronException("Invalid number of fields: " + fieldExpressions.length);
        }

        for (int i = 0; i < fieldExpressions.length; ++i)
        {
            fields[i] = parse(TYPES[i], fieldExpressions[i]);
        }
    }

    protected CronField parse(CronFieldType type, String fieldExpression) throws CronException
    {
        if ("*".equals(fieldExpression))
        {
            return new CronAsterixField(type);
        }

        String[] fieldExpressions = fieldExpression.split(",");

        if (fieldExpressions.length > 0)
        {
            CronListField result = new CronListField(type);

            for (String currentField : fieldExpressions)
            {
                result.add(parse(type, currentField));
            }

            return result;
        }

        fieldExpressions = fieldExpression.split("/");

        if (fieldExpressions.length == 2)
        {
            CronField rangeField = parse(type, fieldExpressions[0]);

            if (!(rangeField instanceof CronRangeField))
            {
                throw new CronException("Invalid cron range of increment expression: " + fieldExpression);
            }

            try
            {
                return new CronIncrementField(type, rangeField, Integer.decode(fieldExpressions[1]));
            }
            catch (NumberFormatException e)
            {
                throw new CronException("Invalid cron range increment value: " + fieldExpression, e);
            }
        }
        else if (fieldExpressions.length > 2)
        {
            throw new CronException("Invalid cron expression: " + fieldExpression);
        }

        fieldExpressions = fieldExpression.split("-");

        if (fieldExpressions.length == 2)
        {
            try
            {
                int minimum = Integer.decode(fieldExpressions[0]);
                int maximum = Integer.decode(fieldExpressions[1]);

                return new CronExplicitRangeField(type, minimum, maximum);
            }
            catch (NumberFormatException e)
            {
                throw new CronException("Invalid cron range values: " + fieldExpression, e);
            }
        }

        try
        {
            int value = Integer.decode(fieldExpression);

            return new CronValueField(type, value);
        }
        catch (NumberFormatException e)
        {
            throw new CronException("Invalid cron value: " + fieldExpression, e);
        }
    }

    public Calendar next(Calendar earliest)
    {
        CronTime time = new CronTime(earliest);

        while (!fields[MONTH].isAllowed(time))
        {
            time.incMonth(1);
            time.resetDayOfMonth();
            time.resetHour();
            time.resetMinute();
        }

        boolean special = fields[DAY_OF_MONTH].isRestricted() && fields[DAY_OF_WEEK].isRestricted();

        if (special)
        {
            // either day of month or day of week may match
            while ((!fields[DAY_OF_MONTH].isAllowed(time)) && (!fields[DAY_OF_WEEK].isAllowed(time)))
            {
                time.incDayOfMonth(1);
                time.resetHour();
                time.resetMinute();
            }
        }
        else
        {
            // day of month and day of week must match
            while ((!fields[DAY_OF_MONTH].isAllowed(time)) || (!fields[DAY_OF_WEEK].isAllowed(time)))
            {
                time.incDayOfMonth(1);
                time.resetHour();
                time.resetMinute();
            }
        }

        while (!fields[HOUR].isAllowed(time))
        {
            time.incHour(1);
            time.resetMinute();
        }

        while (!fields[MINUTE].isAllowed(time))
        {
            time.incMinute(1);
        }

        return time.getCalendar();
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        for (CronField field : fields)
        {
            if (builder.length() > 0)
            {
                builder.append(" ");
            }

            builder.append(field);
        }
        
        return builder.toString();
    }

}
