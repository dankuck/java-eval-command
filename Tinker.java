import java.util.HashMap;
import java.util.Map;

public class Tinker
{
    public static void main(String[] args)
        throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("map", map);
        Commander commander = new Commander("Console", true);

        for (;;) {
            System.out.print("> ");
            String line = System.console().readLine();
            try {
                commander.run(map, line);
            } catch (Throwable e) {
                System.out.println("Exception caught: " + e);
                e.printStackTrace();
            }
            System.out.println("\n");
        }
    }
}
