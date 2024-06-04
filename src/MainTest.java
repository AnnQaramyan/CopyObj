import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ObjectWith5Types {
    int number;
    String text;
    int[] array;
    List<Integer> list;

    public ObjectWith5Types(int number, String text, int[] array, List<Integer> list) {
        this.number = number;
        this.text = text;
        this.array = array;
        this.list = list;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ObjectWith5Types that = (ObjectWith5Types) obj;
        return number == that.number &&
                text.equals(that.text) &&
                Arrays.equals(array, that.array) &&
                list.equals(that.list);
    }
}

class ObjectBase {
    int number;

    public ObjectBase(int number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ObjectBase that = (ObjectBase) obj;
        return number == that.number;
    }
}

class ObjectChildWithSet extends ObjectBase {
    Set<String> set;

    public ObjectChildWithSet(int number, Set<String> set) {
        super(number);
        this.set = set;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        ObjectChildWithSet that = (ObjectChildWithSet) obj;
        return set.equals(that.set);
    }
}


class A {
    List<Integer> list;

    public A(List<Integer> list) {
        this.list = list;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        A a = (A) obj;
        return list.equals(a.list);
    }
}

class B {
    A a;
    B b;

    public B(A a, B b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        B b1 = (B) obj;
        boolean a_eq = (a == null && b1.a == null) || (a != null && a.equals(b1.a));
        boolean b_eq = (b == null && b1.b == null) || (b != null && b.equals(b1.b));
        return a_eq && b_eq;
    }
}

class MainTest {

    @org.junit.jupiter.api.Test
    void copyBasicInt() {
        int number = 23;
        int copiedNumber = 0;
        try {
            copiedNumber = Main.deepCopy(number);
        } catch (Exception e) {
            fail("The method should not throw exceptions");
        }
        assertEquals(number, copiedNumber);
        number = 42;
        assertNotEquals(number, copiedNumber);
    }

    @org.junit.jupiter.api.Test
    void copyBasicString() {
        String text = "Hello, World!";
        String copiedText = null;
        try {
            copiedText = Main.deepCopy(text);
        } catch (Exception e) {
            fail("The method should not throw exceptions");
        }
        assertEquals(text, copiedText);
        text = "Java Programming";
        assertNotEquals(text, copiedText);
    }

    @org.junit.jupiter.api.Test
    void copyBasicArray() {
        int[] array = {1, 2, 3, 4, 5};
        int[] copiedArray = null;
        try {
            copiedArray = Main.deepCopy(array);
        } catch (Exception e) {
            fail("The method should not throw exceptions");
        }
        assertArrayEquals(array, copiedArray);
        array[0] = 42;
        assertNotEquals(array[0], copiedArray[0]);
    }

    @org.junit.jupiter.api.Test
    void copyBasicList() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        List<Integer> copiedList = null;
        try {
            copiedList = Main.deepCopy(list);
        } catch (Exception e) {
            fail("The method should not throw exceptions");
        }
        assertEquals(list, copiedList);
        list.add(42);
        assertNotEquals(list.size(), copiedList.size());
    }

    @org.junit.jupiter.api.Test
    void copyListOfListsOfStrings() {
        List<List<String>> list = new ArrayList<>();
        List<String> innerList1 = new ArrayList<>();
        innerList1.add("Hello");
        innerList1.add("World");
        List<String> innerList2 = new ArrayList<>();
        innerList2.add("Java");
        innerList2.add("Programming");
        list.add(innerList1);
        list.add(innerList2);
        List<List<String>> copiedList = null;
        try {
            copiedList = Main.deepCopy(list);
        } catch (Exception e) {
            fail("The method should not throw exceptions");
        }
        assertEquals(list, copiedList);
        list.get(0).add("!");
        assertNotEquals(list.get(0).size(), copiedList.get(0).size());
    }

    @org.junit.jupiter.api.Test
    void copyObjectWith5Types() {
        ObjectWith5Types object = new ObjectWith5Types(42, "Answer", new int[]{1, 2, 3}, Arrays.asList(4, 5, 6));
        ObjectWith5Types copiedObject = null;
        try {
            copiedObject = Main.deepCopy(object);
        } catch (Exception e) {
            fail("The method should not throw exceptions", e);
        }
        assertEquals(object, copiedObject);
        object.array[0] = 42;
        assertNotEquals(object, copiedObject);
    }

    @org.junit.jupiter.api.Test
    void copyObjectChildWithSet() {
        Set<String> set = new HashSet<>();
        set.add("Hello");
        set.add("World");
        ObjectChildWithSet object = new ObjectChildWithSet(42, set);
        ObjectChildWithSet copiedObject = null;
        try {
            copiedObject = Main.deepCopy(object);
        } catch (Exception e) {
            fail("The method should not throw exceptions", e);
        }
        assertEquals(object, copiedObject);
        object.number = 23;
        assertNotEquals(object, copiedObject);
    }


    @org.junit.jupiter.api.Test
    void copyRecursiveObject() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        A a = new A(list);
        B b = new B(a, null);
        a = new A(list);
        b = new B(a, b);
        B copiedB = null;
        try {
            copiedB = Main.deepCopy(b);
        } catch (Exception e) {
            fail("The method should not throw exceptions", e);
        }
        assertEquals(b, copiedB);
        b.b.a.list.add(42);
        assertNotEquals(b, copiedB);
    }
}