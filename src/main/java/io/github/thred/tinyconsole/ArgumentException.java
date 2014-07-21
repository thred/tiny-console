package io.github.thred.tinyconsole;

/**
 * Common exceptions with arguments.
 *
 * @author Manfred Hantschel
 */
public class ArgumentException extends ProcessException
{

    private static final long serialVersionUID = 4671208137366239322L;

    public ArgumentException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ArgumentException(String message)
    {
        super(message);
    }

}
