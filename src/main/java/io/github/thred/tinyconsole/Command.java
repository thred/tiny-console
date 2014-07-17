package io.github.thred.tinyconsole;

/**
 * A command can be executed by the {@link Shell}. If has a list of name, and some help associated with it.
 * 
 * @author Manfred Hantschel
 */
public interface Command
{

    /**
     * A list of names. The first one is the default one, like 'help'. Other name are usually abbreviations like 'h' and
     * '?'.
     * 
     * @return a list of names, never null, at least one entry.
     */
    String[] getNames();

    /**
     * Returns the (informational) format, used by the help command.
     * 
     * @return the format
     */
    String getFormat();

    /**
     * Returns a description, used by the help command.
     * 
     * @return
     */
    String getDescription();

    /**
     * Called by the help command.
     * 
     * @param process the process
     * @return the result of the execution, usually 0
     * @throws Exception on occasion
     */
    int help(Process process) throws Exception;

    /**
     * Returns the ordinal for sorting commands. Return < 0 to remove the command from the help overview.
     * 
     * @return the ordinal
     */
    int getOrdinal();

    /**
     * Returns true if the command should be enabled by default. Commands can, disable themself if, e.g. prerequisits
     * are not met, like a class or library is missing.
     * 
     * @return true if enabled.
     */
    boolean isEnabled();

    /**
     * Executes the command using the specified process. As the command may be called using different names, the command
     * name is the exact name, that was used to execute the command.
     * 
     * @param commandName the name of the command
     * @param process the process
     * @return the result of the command
     * @throws Exception on occasion
     */
    int execute(String commandName, Process process) throws Exception;
}
