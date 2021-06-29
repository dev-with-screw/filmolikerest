package home.work.filmolikerest.model;

public enum Estimate {
    NOT_ESTIMATE("Без оценки"),
    TERRIBLE("Ужасно"),
    POOR("Плохо"),
    AVERAGE("Средне"),
    GOOD("Хорошо"),
    EXCELLENT("Превосходно");

    private final String russianDescription;

    private Estimate(String russianDescription) {
        this.russianDescription = russianDescription;
    }

    public String getDisplayName() {
        return russianDescription;
    }
}
