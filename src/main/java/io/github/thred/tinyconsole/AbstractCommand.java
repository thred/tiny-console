package io.github.thred.tinyconsole;

import io.github.thred.tinyconsole.util.CommandUtils;
import io.github.thred.tinyconsole.util.WordWrap;

/**
 * Abstract base implementation of a {@link Command}.
 * 
 * @author Manfred Hantschel
 */
public abstract class AbstractCommand implements Command
{

    private final String[] names;

    /**
     * Creates the command using the specified names.
     * 
     * @param names the names
     */
    public AbstractCommand(String... names)
    {
        super();

        this.names = names;
    }

    @Override
    public String[] getNames()
    {
        return names;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

    /**
     * Common implementation of the help method. Build the description by printing the usage and synonyms and calls the
     * {@link #getHelpDescription()} method for the detailed description.
     */
    @Override
    public int help(Process process) throws Exception
    {
        process.out.println(new WordWrap().perform(
            String.format("Usage:    %s", CommandUtils.getFormatDescriptor(this)), TinyConsole.getLineLength()));

        if (getNames().length > 1)
        {
            process.out.printf("Synonyms: %s\n", CommandUtils.getNameDescriptor(this, false));
        }

        process.out.println();

        process.out.println(new WordWrap().perform(getHelpDescription(), TinyConsole.getLineLength()));
        process.out.println();

        return 0;
    }

    /**
     * Return the detailed description of the command. The result will get word wrapped.
     * 
     * @return the detailed description
     */
    protected abstract String getHelpDescription();

}
