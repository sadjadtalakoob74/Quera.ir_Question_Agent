import javafx.beans.binding.ObjectExpression;
import model.man.Person;
import model.primitive.Circle;

import java.lang.reflect.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Comparator.comparing;

public class Agent {

    public List<String> getMethodNames(Object object) {

        List<String> methodNamess = new ArrayList<>();
        Class test = object.getClass();
        Method[] methods = test.getDeclaredMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            methodNamess.add(methodName);
        }

        return methodNamess;
    }


    public Object getFieldContent(Object object, String name) throws NoSuchFieldException, IllegalAccessException {

        Field field = object.getClass().getDeclaredField(name);
        field.setAccessible(true);
        Object value = field.get(object);

        return value;
    }

    public void setFieldContent(Object object, String fieldName, Object co) throws NoSuchFieldException, IllegalAccessException {

        Field field = object.getClass().getDeclaredField(fieldName);
        Class<?> coType = co.getClass();
        field.setAccessible(true);

        if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {

            field.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, co);

        } else {
            if (field.getType().isAssignableFrom(coType)) {
                field.set(object, co);
            }
        }


    }

    public Object call(Object object, String methodName, Object[] parameter) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (parameter.length != 0) {
            Class test = object.getClass();
            Method method = test.getDeclaredMethod(methodName, String.class);
            method.setAccessible(true);
            object = method.invoke(object, parameter);
        } else {
            //System.out.println("salam");
            Class test = object.getClass();
            Method method = test.getDeclaredMethod(methodName, new Class[]{});
            method.setAccessible(true);
            object = method.invoke(object, parameter);
        }

        return object;
    }

    public Object createANewObject(String fullClassName, Object[] initials) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Object myObject = null;
        Constructor<?> constructor;

        Class[] intArray = new Class[initials.length];

        if (initials.length > 0) {
            for (int i = 0; i < initials.length; i++) {

                intArray[i] = initials[i].getClass();
                System.out.println(intArray[i]);
            }
            constructor = Class.forName(fullClassName).getDeclaredConstructor(intArray);
            constructor.setAccessible(true);

            myObject = constructor.newInstance(initials);

        } else {
            constructor = Class.forName(fullClassName).getDeclaredConstructor(Object[].class);
            constructor.setAccessible(true);
            myObject = constructor.newInstance();

        }


        return myObject;

    }

    public String debrief(Object object) {
        String myMethods = "";
        Method[] methods = object.getClass().getDeclaredMethods();
        ArrayList<String> arrayList1 = new ArrayList<>();
        ArrayList<List<String>> arrayList = new ArrayList<>();

        for (int i = 0; i < methods.length; i++) {
            Arrays.sort(methods, comparing(Method::getName));
            arrayList1 = new ArrayList<>();
            for (int j = 0; j < methods[i].getParameterTypes().length; j++) {
                arrayList1.add(methods[i].getParameterTypes()[j].getSimpleName());
            }
            arrayList.add(arrayList1);
        }
        for (int i = 0; i < arrayList.size(); i++) {
            myMethods = myMethods + methods[i].getReturnType().getSimpleName() + " " + methods[i].getName() + "" + arrayList.get(i).toString().replace("[", "(").replace("]", ")") + "\n";
        }

        String packagename = object.getClass().getPackage().getName();


        Class test = object.getClass();
        Constructor[] constructors = test.getDeclaredConstructors();

        for (int i = 0; i < constructors.length; i++) {
        }


        String myfields = "";
        Field[] fields = object.getClass().getDeclaredFields();
        Arrays.stream(fields).sorted();
        for (int i = 0; i < fields.length; i++) {
            Arrays.sort(fields, comparing(Field::getName));
            fields[i].setAccessible(true);
            myfields = myfields + Modifier.toString(fields[i].getModifiers()) + " " + fields[i].getType().getSimpleName() + " " + fields[i].getName() + "\n";
        }


        String output = "Name: " + object.getClass().getSimpleName() + "\n" +
                "Package: " + packagename + "\n" +
                "No. of Constructors: " + constructors.length + "\n"
                + "===\n" +
                "Fields:\n" +
                myfields +
                "(" + object.getClass().getDeclaredFields().length + " fields)\n" +
                "===\n" +
                "Methods:\n" +
                myMethods +
                "(" + arrayList.size() + " methods)";

        System.out.println(output);

        return output;
    }

    public Object clone(Object object) throws InstantiationException, IllegalAccessException {


        //return copy(object);
        return deepCopy(object);

    }

    public static <T> T deepCopy(T obj) {
        try {
            if (obj == null) {
                return null;
            }
            Class<?> clazz = obj.getClass();
            T clone = (T) clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                if (!Modifier.isFinal(field.getModifiers())) {
                    if (field.get(obj) instanceof List<?>) {
                        List<?> copiedList = deepCopyList((List<?>) field
                                .get(obj));
                        field.set(clone, copiedList);
                    } else {
                        field.set(clone, field.get(obj));
                    }
                }
            }
            while (true) {
                if (Object.class.equals(clazz)) {
                    break;
                }
                clazz = clazz.getSuperclass();
                Field[] sFields = clazz.getDeclaredFields();
                for (int i = 0; i < sFields.length; i++) {
                    Field field = sFields[i];
                    field.setAccessible(true);
                    if (!Modifier.isFinal(field.getModifiers())) {
                        if (field.get(obj) instanceof List<?>) {
                            List<?> copiedList = deepCopyList((List<?>) field
                                    .get(obj));
                            field.set(clone, copiedList);
                        } else {
                            field.set(clone, field.get(obj));
                        }
                    }
                }
            }
            return clone;
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static <T> List<T> deepCopyList(List<T> arg) {
        if (arg == null) {
            return null;
        }
        List<T> retList = new ArrayList<T>();
        for (T each : arg) {
            retList.add(deepCopy(each));
        }
        return retList;
    }

    private <T> T copy(T entity) throws IllegalAccessException, InstantiationException {
        Class<?> clazz = entity.getClass();
        Constructor<?> constructor;
        T newEntity = (T) entity.getClass().newInstance();

        while (clazz != null) {
            copyFields(entity, newEntity, clazz);
            clazz = clazz.getSuperclass();
        }

        return newEntity;
    }

    private <T> T copyFields(T entity, T newEntity, Class<?> clazz) throws IllegalAccessException {
        List<Field> fields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            fields.add(field);
        }
        for (Field field : fields) {
            field.setAccessible(true);
            field.set(newEntity, field.get(entity));
        }
        return newEntity;
    }

}



