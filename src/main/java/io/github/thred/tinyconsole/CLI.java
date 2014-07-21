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
 * A command line interpreter - parses the input, creates process and executes them.
 *
 * @author Manfred Hantschel
 */
public class CLI
{

    protected final InputStream in;
    protected final PrintStream out;
    protected final PrintStream err;

    private InputStreamReader reader;
    private Scanner scanner;
    private Tokenizer tokenizer;
    private Parser parser;

    private int result;

    public CLI(InputStream in, PrintStream out, PrintStream err)
    {
        super();

        this.in = in;
        this.out = out;
        this.err = err;
    }

    public int consume() throws IOException
    {
        try
        {
            reader = new InputStreamReader(in);
            scanner = new Scanner(reader);
            tokenizer = new Tokenizer(scanner);
            parser = new Parser(tokenizer);

            Arguments args;

            while ((args = parser.parse()) != null)
            {
                if (!isRunning())
                {
                    return result;
                }

                result = execute(args);
            }
        }
        finally
        {
            onFinished();

            scanner = null;
            tokenizer = null;
            parser = null;
        }

        return result;
    }

    protected int execute(Arguments args)
    {
        return new Process(null, args, in, out, err).execute();
    }

    protected boolean isRunning()
    {
        return true;
    }

    protected void onFinished()
    {
        // intentionally left blank
    }

}
