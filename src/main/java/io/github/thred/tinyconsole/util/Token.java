package io.github.thred.tinyconsole.util;

public class Token
{

    public enum Type
    {
        END_OF_FILE,

        END_OF_LINE,

        SEPARATOR,

        STRING,

        COMMAND,
    }

    private final Type type;
    private final int line;
    private final int column;
    private final String value;

    public Token(Type type, int line, int column, String value)
    {
        super();

        this.type = type;
        this.line = line;
        this.column = column;
        this.value = value;
    }

    public Type getType()
    {
        return type;
    }

    public int getLine()
    {
        return line;
    }

    public int getColumn()
    {
        return column;
    }

    public String getValue()
    {
        return value;
    }

}
