package io.github.thred.tinyconsole.util.cron;

import java.util.concurrent.Delayed;

interface CronAction extends Delayed
{

    void execute(CronDaemon daemon);

}
