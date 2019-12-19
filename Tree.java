package remember;

import java.util.*;

public class Tree<T> {
    private T info;
    private List<Tree<? extends T>> children = new ArrayList<Tree<? extends T>>();

    public Tree<? extends T> getChildren(int n) {
        return children.get(n);
    }

    public void addChildren(Tree<? extends T> children) {
        this.children.add(children);
    }

    public static void main(String[] args) {
        Thread t=new Thread();
        t=Thread.currentThread();
        test(new Tree<Number>());
        String[] l={"chevin"};
//        int i = []l.length;
    }

    private static void test(Tree<?> b2) {
        // "<?>" means we don't know what the generic type of b2 is, so
        // the compiler can't possibly know if it's safe to add any type
        // of children...

//        b2.addChildren(new Tree<Number>()); // 1) why it doesn't work ?
//        b2.addChildren(new Tree<Integer>()); // neither does this one!
        b2.addChildren(new Tree<>()); // 2) but with diamond <> it works ?
//        List<Number> lk=new ArrayList<Integer>(); // Error.

    }
}
