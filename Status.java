package remember;

public enum Status {
    // How should we implement PartiallyChecked?
    Unchecked("U"), PartiallyChecked("PC"), FullyChecked("FC");

    public final String symbol;

    Status(String symbol) {
        this.symbol = symbol;
    }
}