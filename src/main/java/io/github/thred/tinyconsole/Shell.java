package io.github.thred.tinyconsole;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * A shell - an autonomous command line interpreter. Parses the input, creates process and executes them.
 *
 * @author Manfred Hantschel
 */
public class Shell extends CLI implements Runnable
{

    private volatile Thread thread;

    public Shell(InputStream in, PrintStream out, PrintStream err)
    {
        super(in, out, err);
    }

    /**
     * Starts a daemon thread using this shell. Reads from the default in stream.
     */
    public void start(boolean daemon)
    {
        if (thread != null)
        {
            throw new IllegalStateException("Already running");
        }

        thread = new Thread(this, "Shell");

        thread.setDaemon(daemon);
        thread.start();
    }

    /**
     * Stops the shell.
     */
    public void stop()
    {
        thread = null;
    }

    public void close()
    {
        try
        {
            in.close();
        }
        catch (IOException e)
        {
            // ignore
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run()
    {
        try
        {
            consume();
        }
        catch (IOException e)
        {
            e.printStackTrace(err);
        }
    }

    @Override
    protected boolean isRunning()
    {
        return thread != null;
    }

    @Override
    protected void onFinished()
    {
        super.onFinished();

        thread = null;
    }

}
