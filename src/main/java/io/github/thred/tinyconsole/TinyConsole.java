package io.github.thred.tinyconsole;

public class TinyConsole
{

    private static Shell shell;

    public static void main(String[] args)
    {
        start(false);
    }

    private static void start(boolean daemon)
    {
        shell = new Shell(System.in, System.out, System.err);

        shell.start(daemon);
    }

    public static int getLineLength()
    {
        return 80;
    }
}
