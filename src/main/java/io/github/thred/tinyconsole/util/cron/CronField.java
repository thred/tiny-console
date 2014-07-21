package io.github.thred.tinyconsole.util.cron;

interface CronField
{

    CronFieldType getType();

    boolean isRestricted();

    boolean isAllowed(CronTime time);

    String toString();
}
