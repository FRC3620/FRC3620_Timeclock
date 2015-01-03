import java.util.*;

public class PropertiesTest
{
    public static void main(String[] args)
    {
        Properties props = new Properties();
        props.setProperty("foo", "foo/${os.name}/baz/${os.version}");
        props.setProperty("bar", "bar/${user.country}/baz/${user.country}");

        System.out.println("BEFORE:");
        printProperties(props);

        resolveSystemProperties(props);

        System.out.println("\n\nAFTER:");
        printProperties(props);
    }

    static void resolveSystemProperties(Properties props)
    {
        Map<String, String> sysProps = readSystemProperties();
        Set<String> sysPropRefs = sysProps.keySet();

        Enumeration names = props.propertyNames();
        while (names.hasMoreElements())
        {
            String name = (String) names.nextElement();
            String value = props.getProperty(name);
            for (String ref : sysPropRefs)
            {
                if (value.contains(ref))
                {
                    value = value.replace(ref, sysProps.get(ref));
                }
            }
            props.setProperty(name, value);
        }
    }

    static Map<String, String> readSystemProperties()
    {
        Properties props = System.getProperties();
        Map<String, String> propsMap = 
            new HashMap<String, String>(props.size());

        Enumeration names = props.propertyNames();
        while (names.hasMoreElements())
        {
            String name = (String) names.nextElement();
            propsMap.put("${" + name + "}", props.getProperty(name));
        }
        return propsMap;
    }

    static void printProperties(Properties props)
    {
        Enumeration names = props.propertyNames();
        while (names.hasMoreElements())
        {
            String name = (String) names.nextElement();
            String value = props.getProperty(name);
            System.out.println(name + " => " + value);
        }
    }
}