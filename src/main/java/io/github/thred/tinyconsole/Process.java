package io.github.thred.tinyconsole;

import io.github.thred.tinyconsole.util.Arguments;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * Each {@link Command} or list of {@link Command}s will be executed within a process. The process holds the in/out
 * streams, the arguments and so on.
 *
 * @author Manfred Hantschel
 */
public class Process
{

    public final Process parent;
    public final Arguments args;
    public final InputStream in;
    public final PrintStream out;
    public final PrintStream err;

    private final int result = 0;

    public Process(Process parent, Arguments args, InputStream in, PrintStream out, PrintStream err)
    {
        super();

        this.parent = parent;
        this.args = args;
        this.in = in;
        this.out = out;
        this.err = err;
    }

    /**
     * Creates a new process with the same properties, but different streams.
     *
     * @param in the in stream
     * @param out the out stream
     * @param err the error stream
     * @return the new process
     */
    public Process redirect(InputStream in, PrintStream out, PrintStream err)
    {
        return new Process(parent, args, in, out, err);
    }

    /**
     * Execute the process. Parses the arguments and launches the command in the same or a dedicated child process.
     *
     * @return the result of the command
     */
    public int execute()
    {
        int index = args.lastIndexOf(">", ">>", "&");

        if (index > 0)
        {
            String arg = args.consumeString(index);

            return new Process(this, args.consume(index).add(0, arg), in, out, err).execute();
        }

        return execute(this);
    }

    /**
     * The result of the command.
     *
     * @return the result
     */
    public int getResult()
    {
        return result;
    }

    public void close()
    {
        if (err != System.err)
        {
            err.close();
        }

        if (out != System.out)
        {
            out.close();
        }

        if (in != System.in)
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
    }

    /**
     * Executes the process, handles errors.
     *
     * @param process the process to be executed
     * @return the result of the command
     */
    private static int execute(Process process)
    {
        if (process.args.isEmpty())
        {
            return 0;
        }

        int result = 0;

        String commandName = process.args.consumeString();

        try
        {
            result = execute(process, commandName);
        }
        catch (ArgumentException e)
        {
            process.err.println(e);
        }
        catch (Exception e)
        {
            process.err.println("An exception occured:");

            e.printStackTrace(process.err);

            result = -1;
        }

        return result;

    }

    /**
     * Executes the specified command using the specified process.
     *
     * @param process the process
     * @param commandName the name of the command
     * @return the result of the command
     * @throws Exception on occasion
     */
    private static int execute(Process process, String commandName) throws Exception
    {
        Command command = Commands.find(commandName.toLowerCase());

        if (command == null)
        {
            process.err.printf("Unknown command: %s\n", commandName);
            process.err.println("Type \"help\" to get help.");

            return -1;
        }

        return command.execute(commandName, process);
    }

}
