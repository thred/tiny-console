package io.github.thred.tinyconsole.util;

import java.io.PrintStream;

/**
 * Common utilities for memory consumption
 * 
 * @author Manfred Hantschel
 */
public class MemoryUtils
{

    /**
     * Prints the memory usage as used by various commands.
     * 
     * @param out the stream
     * @return the free memory
     */
    public static long printMemoryUsage(PrintStream out)
    {
        Runtime runtime = Runtime.getRuntime();

        long freeMemory = runtime.freeMemory();
        long totalMemory = runtime.totalMemory();
        long maxMemory = runtime.maxMemory();
        long usedMemory = totalMemory - freeMemory;

        out.printf("Free Memory:       %13s\n", Utils.formatBytes(freeMemory));
        out.printf("Used Memory:       %13s\n", Utils.formatBytes(usedMemory));
        out.printf("Total Memory:      %13s\n", Utils.formatBytes(totalMemory));
        out.printf("Maximum Memory:    %13s\n", Utils.formatBytes(maxMemory));
        out.printf("Number of Threads: %,8d     \n", Thread.activeCount());

        return freeMemory;
    }

}
