package io.github.thred.tinyconsole;

import java.io.File;

import io.github.thred.tinyconsole.util.Arguments;

public class TinyConsole 
{

    private static Shell shell;

    public static void main(String[] args)
    {
        start(false);
    }

    public static void start(boolean daemon)
    {
        shell = new Shell(System.in, System.out, System.err);

        File file = new File("autostart.script");

        if (file.exists())
        {
            shell.execute(new Arguments("shell", file.getPath()));
        }

        shell.start(daemon);
    }

    public static int getLineLength()
    {
        return 80;
    }
}
