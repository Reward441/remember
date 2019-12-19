package remember;

import java.awt.*;

public class Layer {

    protected int id;
    protected String name;
    protected Status status = Status.Unchecked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status toggleStatus() {
        if (status==Status.Unchecked)setStatus(Status.FullyChecked);
        else if (status==Status.FullyChecked) setStatus(Status.Unchecked);
        return status;
    }

    public boolean checked() {
        if (status==Status.Unchecked) return false;
        else return true;
    }

    @Override
    public String toString() {
        return "  "+this.name;
    }
}
