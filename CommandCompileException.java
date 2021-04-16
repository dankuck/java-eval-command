public class CommandCompileException extends CommandException
{
    public CommandCompileException(String msg, Throwable ex)
    {
        super(msg, ex);
    }

    public CommandCompileException(String msg)
    {
        super(msg);
    }

    public CommandCompileException(Throwable ex)
    {
        super(ex);
    }
}
