package remember;

import java.util.*;

public class Category extends Layer implements Comparable<Category> {

    List<Integer> subcatIDs = new ArrayList<>();
    List<Subcategory> subcategories;

    public Category(String name, int id, List<Subcategory> subcats) {
        this.name = name;
        this.id = id;
        subcategories=subcats;
    }

    public List<Integer> getSubcatIDs() {
        return subcatIDs;
    }

    public void setSubcatIDs(List<Integer> subcatIDs) {
        this.subcatIDs = subcatIDs;
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }




    @Override
    public int compareTo(Category o) {
        return Integer.compare(id, o.id);
    }
}
