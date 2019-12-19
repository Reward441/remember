package remember;

import java.util.Arrays;

import static remember.Remember.*;
import static remember.ButtonHandler.*;

public class Storage {

    // Remember
    private int interv, dura;
    private int numOfCats, numOfSubcats, numOfMessages;
    Category[] cats;

    public int getNumOfCats() {
        return numOfCats;
    }

    public int getNumOfSubcats() {
        return numOfSubcats;
    }

    public int getNumOfMessages() {
        return numOfMessages;
    }

    public Storage() {
        dura = Integer.parseInt(fd.getText());
        interv = Integer.parseInt(fi.getText());
        numOfCats = cid;
        numOfSubcats = sid;
        numOfMessages = mid;
        cats = new Category[categories.size()];
        for (int i = 0; i < cats.length; i++) {
            cats[i] = categories.get(i);
        }
        }

    public int getInterv() {
        return interv;
    }

    public void setInterv(int interv) {
        this.interv = interv;
    }

    public int getDura() {
        return dura;
    }

    public void setDura(int dura) {
        this.dura = dura;
    }

    public Category[] getCats() {
        return cats;
    }

    public void setCats(Category[] cats) {
        this.cats = cats;
    }

    @Override
    public String toString() {
        return "Duration: " + dura + "; Interval: " + interv + "; Categories: " + Arrays.toString(cats);
    }
}