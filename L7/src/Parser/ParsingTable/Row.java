package Parser;

public class Row {
    Integer index;
    String info;
    Integer parent;
    Integer sibling;

    @Override
    public String toString() {
        return index +" " + info + " " + parent + " " + sibling;
    }
}