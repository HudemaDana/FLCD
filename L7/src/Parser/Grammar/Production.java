package Grammar;

import java.util.List;

public class Production {
    private String lhs;
    private String rhs;

    public Production(String lhs, String rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public String getLHS() {
        return lhs;
    }

    public String getRHS() {
        return rhs;
    }

    public List<String> getRHSList() {
        List<String> rhs = List.of(this.rhs.split(" "));
        return rhs;
    }

    @Override
    public String toString() {
        return lhs + " -> " + rhs;
    }
}