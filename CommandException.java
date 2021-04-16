public class CommandException extends Exception
{
    public CommandException(String msg, Throwable ex)
    {
        super(msg, ex);
    }

    public CommandException(String msg)
    {
        super(msg);
    }

    public CommandException(Throwable ex)
    {
        super(ex);
    }
}
