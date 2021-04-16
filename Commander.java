import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.FileWriter;
import java.time.Instant;
import java.util.Map;

public class Commander
{
    private static long base = 0;
    private static long index = 0;

    private String prefix;
    private boolean autoCleanup;

    public Commander(String prefix, boolean autoCleanup)
    {
        if (base == 0) {
            base = Instant.now().getEpochSecond();
        }
        this.prefix = prefix;
        this.autoCleanup = autoCleanup;
    }

    public void run(Map<String, Object> map, String userCommand)
        throws Throwable
    {
        String className = generateClassName();
        String code = buildCode(className, map, userCommand);
        try {
            Class<Command> c = loadClass(className, code);
            Command com = c.newInstance();
            com.run(map);
        } finally {
            if (autoCleanup) {
                cleanup(className);
            }
        }
    }

    public synchronized String generateClassName()
    {
        String className = prefix + "Command" + base + index;
        index++;
        return className;
    }

    public String buildCode(String className, Map<String, Object> map, String userCommand)
    {
        StringBuilder code = new StringBuilder();
        code.append("class " + className + " extends Commander.Command\n");
        code.append("{\n");
        code.append("public void run(java.util.Map<String, Object> map" + className + ")\n");
        code.append("throws Throwable\n");
        code.append("{\n");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String cn = entry.getValue().getClass().getName();
            code.append(cn + " " + entry.getKey() + " = (" + cn + ") map" + className + ".get(\"" + entry.getKey() + "\");\n");
        }
        code.append(userCommand + ";\n");
        code.append("}\n");
        code.append("}\n");
        return code.toString();
    }

    public Class loadClass(String className, String code)
        throws CommandCompileException,
            CommandException
    {
        try {
            String fileName = className + ".java";
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            out.write(code);
            out.close();
            Process p = Runtime.getRuntime().exec("javac " + fileName);
            p.waitFor();
            if (p.exitValue() != 0) {
                throw new CommandCompileException(
                    streamToString(p.getErrorStream()) + "\n"
                    + streamToString(p.getInputStream())
                );
            }
            return Class.forName(className);
        } catch (InterruptedException e) {
            throw new CommandCompileException(e);
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }

    private String streamToString(InputStream is)
        throws java.io.IOException
    {
        StringBuilder b = new StringBuilder();
        byte[] bs = new byte[256];
        while (is.read(bs) != -1) {
            b.append(new String(bs));
        }
        return b.toString();
    }

    public void cleanup(String className)
    {
        new File(className + ".java").delete();
        new File(className + ".class").delete();
    }

    public static class Command {
        public void run(Map<String, Object> map)
            throws Throwable
        {

        }

        public void println(Object object)
        {
            System.out.println(object);
        }
    }
}
