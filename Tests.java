package remember;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

//import static remember.Remember.*;
public class Tests {

    private Remember remember;
    Subcategory sub1 = new Subcategory("Subcategory1", 1, 1, new ArrayList<>());
    Subcategory sub2 = new Subcategory("Subcategory2", 2, 1, new ArrayList<>());
    Subcategory sub3 = new Subcategory("Subcategory3", 3, 2, new ArrayList<>());
    Subcategory sub4 = new Subcategory("Subcategory4", 4, 2, new ArrayList<>());
    Subcategory sub5 = new Subcategory("Subcategory5", 5, 3, new ArrayList<>());
    Subcategory sub6 = new Subcategory("Subcategory6", 6, 3, new ArrayList<>());
    Subcategory sub7 = new Subcategory("Subcategory7", 7, 4, new ArrayList<>());
    Subcategory sub8 = new Subcategory("Subcategory8", 8, 4, new ArrayList<>());

    // sample data
    private static List<Category> sampleCategories() {
        return new ArrayList<>(Arrays.asList(
                new Category("Category1", 1, new ArrayList<>()),
                new Category("Category2", 2, new ArrayList<>()),
                new Category("Category3", 3, new ArrayList<>()),
                new Category("Category4", 4, new ArrayList<>())
        ));
    }

    private List<Subcategory> sampleSubcategories() {
        return new ArrayList<>(Arrays.asList(
                sub1, sub2, sub3, sub4, sub5, sub6, sub7, sub8
        ));
    }

    private List<Message> sampleMessages() {
        return new ArrayList<>(Arrays.asList(
//                new Message("Message1", 1, sub1),
//                new Message("Message2", 2, sub1),
//                new Message("Message3", 3, sub2),
//                new Message("Message4", 4, sub2),
//                new Message("Message5", 5, sub3),
//                new Message("Message6", 6, sub3),
//                new Message("Message7", 7, sub4),
//                new Message("Message8", 8, sub4),
//                new Message("Message9", 9, sub5),
//                new Message("Message10", 10, sub5),
//                new Message("Message11", 11, sub6),
//                new Message("Message12", 12, sub6),
//                new Message("Message13", 13, sub7),
//                new Message("Message14", 14, sub7),
//                new Message("Message15", 15, sub8),
//                new Message("Message16", 16, sub8)
        ));
    }

    private static Map<String, String> sampleAbbreviations() {
        Map<String, String> abbs = new TreeMap<String, String>();
        abbs.put("BEI", "behaviour, ego and intentional");
        abbs.put("OP", "object and perceptual");
        abbs.put("SW", "spatial world");
        return abbs;
    }

    @Before
    public void setup() {
        remember = new Remember();
        remember.setCategories(sampleCategories());
        for (Category c : remember.getCategories()) {
            for (Subcategory s : sampleSubcategories()) {
                if (c.getId() == s.getCatID()) c.getSubcategories().add(s);
//                for (Message m : sampleMessages()) {
//                    if (s.getId() == m.getSub().id) s.getMessages().add(m);
//                }
            }
        }
    }

    @Test
    public void modifyCategory() {
        List<Category> cats = remember.getCategories();
        List<String> catNames = new ArrayList<>();
        for (Category c : cats) {
            for (int i = 0; i < cats.size(); i++) {
                cats.get(i).setName("" + i);
                catNames.add("" + i);
            }
        }
        Assert.assertEquals(catNames, new ArrayList<String>(Arrays.asList("0", "1", "2", "3", "4")));
    }

//    public void modifySubcategory() {
//        Assert.assertEquals(game.getPrices(), new ArrayList<Integer>(Arrays.asList( 80, 100, 120, 100, 100 )));
//    }

    @Test
    public void modifyMessage() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void deleteMessage() {
    }

    @Test
    public void flash() {
    }

    @Test
    public void listen() {
    }

    @Test
    public void flashAndListen() {
    }

    @Test
    public void save() {
    }

    @Test
    public void colour() {
    }

    @Test
    public void shape() {
    }

    @Test
    public void flashPeriod() {
    }

    @Test
    public void flashInterval() {
    }
}
