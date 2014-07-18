package io.github.thred.tinyconsole.command;

import io.github.thred.tinyconsole.AbstractCommand;
import io.github.thred.tinyconsole.ArgumentException;
import io.github.thred.tinyconsole.Process;
import io.github.thred.tinyconsole.util.MemoryUtils;
import io.github.thred.tinyconsole.util.Utils;

/**
 * Prints memory usage.
 * 
 * @author Manfred Hantschel
 */
public class MemoryCommand extends AbstractCommand
{

    public MemoryCommand()
    {
        super("memory", "m");
    }

    @Override
    public String getFormat()
    {
        return "[-gc]";
    } 

    @Override
    public String getDescription()
    {
        return "Memory utilities.";
    }

    @Override
    protected String getHelpDescription()
    {
        return "Prints memory information to the console. If invoked with the -gc command, it "
            + "performs a garbage collection.";
    }

    @Override
    public int getOrdinal()
    {
        return 500;
    }

    @Override
    public int execute(String commandName, Process process)
    {
        String command = process.args.consumeString();

        long freeMemory = MemoryUtils.printMemoryUsage(process.out);

        if (command == null)
        {
            return 0;
        }

        if ("-gc".equalsIgnoreCase(command))
        {
            return gc(process, freeMemory);
        }

        throw new ArgumentException(String.format("Invalid command: %s", command));
    }

    private int gc(Process process, long freeMemory)
    {
        process.out.println();
        process.out.print("Performing GC...");

        long millis = System.nanoTime();

        System.gc();

        process.out.printf(" [%s]\n", Utils.formatSeconds((System.nanoTime() - millis) / 1000000000d));
        process.out.println();

        long newFreeMemory = MemoryUtils.printMemoryUsage(process.out);

        process.out.println();
        process.out.printf("Saved Memory:      %13s\n", Utils.formatBytes(newFreeMemory - freeMemory));

        return 0;
    }

}
