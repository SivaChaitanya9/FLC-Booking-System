package flc;

public enum ExerciseType {
    YOGA(12.00),
    ZUMBA(10.00),
    AQUACISE(8.00),
    BOX_FIT(15.00),
    BODY_BLITZ(11.00);

    private final double price;

    ExerciseType(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name().replace("_", " ");
    }

    public static ExerciseType fromString(String s) {
        for (ExerciseType e : values()) {
            if (e.name().equalsIgnoreCase(s.replace(" ", "_"))) return e;
        }
        return null;
    }
}