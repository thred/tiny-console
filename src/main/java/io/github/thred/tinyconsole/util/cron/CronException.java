package io.github.thred.tinyconsole.util.cron;

public class CronException extends Exception
{

    private static final long serialVersionUID = -6791988052194987902L;

    public CronException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public CronException(String message)
    {
        super(message);
    }

}
