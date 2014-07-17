package io.github.thred.tinyconsole;

import io.github.thred.tinyconsole.util.Arguments;
import io.github.thred.tinyconsole.util.Parser;
import io.github.thred.tinyconsole.util.Scanner;
import io.github.thred.tinyconsole.util.Tokenizer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * A shell, a command line interpreter. Parses the input, creates process and executes them.
 * 
 * @author Manfred Hantschel
 */
public class Shell implements Runnable
{

    private final InputStream in;
    private final PrintStream out;
    private final PrintStream err;

    private Scanner scanner;
    private Tokenizer tokenizer;
    private Parser parser;

    public Shell(InputStream in, PrintStream out, PrintStream err)
    {
        super();

        this.in = in;
        this.out = out;
        this.err = err;
    }

    /**
     * Starts a daemon thread using this shell. Reads from the default in stream.
     */
    public void start(boolean daemon)
    {
        if (tokenizer != null)
        {
            return;
        }

        Thread thread = new Thread(this, "Shell");

        thread.setDaemon(daemon);
        thread.start();
    }

    /**
     * Stops the shell.
     */
    public void stop()
    {
        if (scanner == null)
        {
            return;
        }

        try
        {
            scanner.close();
        }
        catch (IOException e)
        {
            // ignore
        }
        finally
        {
            scanner = null;
            tokenizer = null;
            parser = null;
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
            scanner = new Scanner(new InputStreamReader(in));
            tokenizer = new Tokenizer(scanner);
            parser = new Parser(tokenizer);

            try
            {
                Arguments args;

                while ((args = parser.parse()) != null)
                {
                    new Process(null, args, in, out, err).execute();
                }
            }
            finally
            {
                tokenizer.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace(err);
        }
        finally
        {
            tokenizer = null;
        }
    }

}
