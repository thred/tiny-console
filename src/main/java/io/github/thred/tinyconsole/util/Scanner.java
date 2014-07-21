package io.github.thred.tinyconsole.util;

import java.io.IOException;
import java.io.Reader;

/**
 * A scanner for the shell
 *
 * @author Manfred Hantschel
 */
public class Scanner
{
    private final Reader reader;

    private int offset = 0;
    private int line = 0;
    private int column = 0;
    private int ch;
    private int nextCh;
    private boolean skipLF = false;
    private boolean lookedAhead = false;

    public Scanner(Reader reader)
    {
        super();

        this.reader = reader;
    }

    public void reset()
    {
        offset = 0;
        line = 0;
        column = 0;
    }

    public int getOffset()
    {
        return offset;
    }

    public int getLine()
    {
        return line;
    }

    public int getColumn()
    {
        return column;
    }

    public int get()
    {
        return ch;
    }

    public int next() throws IOException
    {
        if (lookedAhead)
        {
            ch = nextCh;
            lookedAhead = false;
        }
        else
        {
            ch = read();
        }

        offset += 1;

        if (ch == '\n')
        {
            line += 1;
            column = 0;
        }

        return ch;
    }

    public int lookAhead() throws IOException
    {
        if (lookedAhead)
        {
            return nextCh;
        }

        nextCh = read();
        lookedAhead = true;

        return nextCh;
    }

    protected int read() throws IOException
    {
        int ch = reader.read();

        if ((ch == '\n') && (skipLF))
        {
            ch = reader.read();
        }

        skipLF = false;

        if (ch == '\r')
        {
            ch = '\n';
            skipLF = true;
        }

        return ch;
    }
}
