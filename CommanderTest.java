import java.util.HashMap;
import java.util.Map;

public class CommanderTest
{
    public static void main(String[] args)
        throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> x = new HashMap<String, Object>();
        map.put("x", x);

        new Commander("Test", true).run(map, "x.put(\"y\", \"Hello\")");
        System.out.println("Hello".equals(x.get("y")) ? "Pass" : "Fail");
    }
}
