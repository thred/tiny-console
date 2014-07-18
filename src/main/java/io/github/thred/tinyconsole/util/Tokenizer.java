package io.github.thred.tinyconsole.util;

import java.io.IOException;

/**
 * A tokenizer for the shell
 * 
 * @author Manfred Hantschel
 */
public class Tokenizer
{
    private final Scanner scanner;

    public Tokenizer(Scanner scanner)
    {
        super();

        this.scanner = scanner;
    }

    public Token read() throws IOException
    {
        int ch;
        int line;
        int column;

        do
        {
            ch = scanner.next();
            line = scanner.getLine();
            column = scanner.getColumn();
        } while (ch == ' ');

        if (ch < 0)
        {
            return new Token(Token.Type.END_OF_FILE, line, column, null);
        }

        if ((ch == '\n') || (ch == ';'))
        {
            return new Token(Token.Type.END_OF_LINE, line, column, String.valueOf((char) ch));
        }

        if (ch == '\'')
        {
            StringBuilder builder = new StringBuilder();

            while (true)
            {
                ch = scanner.next();

                if (ch == '\'')
                {
                    return new Token(Token.Type.STRING, line, column, builder.toString());
                }

                builder.append((char) ch);
            }
        }

        if (ch == '\"')
        {
            StringBuilder builder = new StringBuilder();

            while (true)
            {
                ch = scanner.next();

                if (ch == '\"')
                {
                    return new Token(Token.Type.STRING, line, column, builder.toString());
                }

                builder.append((char) ch);
            }
        }

        StringBuilder builder = new StringBuilder();

        while (true)
        {
            if (ch == '\\')
            {
                ch = scanner.next();
            }

            builder.append((char) ch);

            ch = scanner.lookAhead();
 
            if ((ch == ' ') || (ch == '\n') || (ch == ';'))
            {
                return new Token(Token.Type.COMMAND, line, column, builder.toString());
            }

            ch = scanner.next();
        }
    }

    protected static boolean isWhitespace(char ch)
    {
        return (ch == ' ') || (ch == '\t') || (ch == '\r') || (ch == '\n');
    }
}
