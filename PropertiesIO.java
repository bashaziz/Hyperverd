//package Grey;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
 
public class PropertiesIO 
{
 
    private static String propertiesFile = "config.properties";
    private static Properties p = new Properties();
 
    public PropertiesIO()
	{
        
        this.loadProperties();
    }
 
    public static void loadProperties()
	{
        try {
            p.load(new FileInputStream(propertiesFile));
        } catch (FileNotFoundException e) 
		{
            e.printStackTrace();
        } catch (IOException e) 
		{
            e.printStackTrace();
        }
    }
 
    public ArrayList readAllValues(){
        ArrayList values = new ArrayList();
        Enumeration e = p.elements();
        while (e.hasMoreElements()){
            values.add(e.nextElement());
        }
        return values;
    }
 
    public static String readValue(String key){
        return p.getProperty(key);
    }
 
    public static void writeProperty(String key, String value){
        p.setProperty(key, value);
        try {
            p.store(new FileOutputStream(propertiesFile), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
 
}