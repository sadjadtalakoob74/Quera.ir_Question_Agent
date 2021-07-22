import model.man.Person;
import sun.misc.SoftCache;

import java.lang.reflect.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Comparator.comparing;

public class Main {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        /*Person object = new Person();
        //String className = object.getClass().getPackage().getName();
        //System.out.println(className);

        //Class test = object.getClass();

        //Object someObject = getItSomehow();
        Field[] fields = object.getClass().getDeclaredFields();
        Arrays.stream(fields).sorted();
        for (int i = 0; i < fields.length; i++) {
            Arrays.sort(fields, comparing(Field::getName));
            fields[i].setAccessible(true);
            System.out.println(Modifier.toString(fields[i].getModifiers()) + " " + fields[i].getType().getSimpleName() + " " + fields[i].getName());
        }
        System.out.println(object.getClass().getDeclaredFields().length + " fields");*/

        Person object = new Person();
        String className = object.getClass().getSimpleName();
        //System.out.println(className);

        Method[] methods = object.getClass().getDeclaredMethods();
        //System.out.println("Methods: ");
        ArrayList<String> arrayList1 = new ArrayList<>();
        ArrayList<List<String>> arrayList = new ArrayList<>();

        for (int i = 0; i < methods.length; i++) {
            Arrays.sort(methods, comparing(Method::getName));
            arrayList1 = new ArrayList<>();
            for (int j = 0; j < methods[i].getParameterTypes().length; j++) {
                arrayList1.add(methods[i].getParameterTypes()[j].getSimpleName());
                //System.out.println("1"+methods[i].getParameterTypes()[j].getSimpleName());
            }
            arrayList.add(arrayList1);
            //System.out.println("2"+arrayList1);
        }
        for (int i = 0; i < arrayList.size(); i++) {
            System.out.println();
        }
        System.out.println(arrayList.size() + " methods");

    }
}
