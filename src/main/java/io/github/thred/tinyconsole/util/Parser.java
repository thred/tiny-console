package io.github.thred.tinyconsole.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser
{

    private final Tokenizer tokenizer;

    public Parser(Tokenizer tokenizer)
    {
        super();

        this.tokenizer = tokenizer;
    };

    public Arguments parse() throws IOException
    {
        List<String> tokens = new ArrayList<>();

        while (true)
        {
            Token token = tokenizer.read();

            switch (token.getType())
            {
                case END_OF_FILE:
                    return null;
                    
                case END_OF_LINE:
                case SEPARATOR:
                    return new Arguments(tokens);

                case STRING:
                case COMMAND:
                    tokens.add(token.getValue());
                    break;
            }
        }
    }

}
