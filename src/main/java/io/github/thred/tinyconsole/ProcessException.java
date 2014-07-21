package io.github.thred.tinyconsole;

/**
 * Root {@link RuntimeException} for processes and commands.
 *
 * @author Manfred Hantschel
 */
public class ProcessException extends RuntimeException
{

    private static final long serialVersionUID = 4671208137366239322L;

    public ProcessException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ProcessException(String message)
    {
        super(message);
    }

}
