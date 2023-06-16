package performance;

public enum TireParts {

    FIVE_STAR_GREEN_IMPROVED(24, -4, 12),
    FIVE_STAR_GREEN_SPORT(25, -5, 13),
    FIVE_STAR_GREEN_TUNED(27, -5, 14),
    FIVE_STAR_GREEN_CUSTOM(28, -5, 15),
    FIVE_STAR_GREEN_ELITE(30, -5, 15),

    FIVE_STAR_RED_IMPROVED(8, -4, 28),
    FIVE_STAR_RED_SPORT(8, -5, 30),
    FIVE_STAR_RED_TUNED(9, -5, 32),
    FIVE_STAR_RED_CUSTOM(9, -5, 34),
    FIVE_STAR_RED_ELITE(10, -5, 35),

    FIVE_STAR_BLUE_IMPROVED(8, 10, 14),
    FIVE_STAR_BLUE_SPORT(8, 10, 15),
    FIVE_STAR_BLUE_TUNED(9, 11, 16),
    FIVE_STAR_BLUE_CUSTOM(9, 12, 17),
    FIVE_STAR_BLUE_ELITE(10, 12, 18),

    FIVE_STAR_WHITE_IMPROVED(12, 4, 16),
    FIVE_STAR_WHITE_SPORT(12, 5, 16),
    FIVE_STAR_WHITE_TUNED(13, 5, 18),
    FIVE_STAR_WHITE_CUSTOM(14, 5, 19),
    FIVE_STAR_WHITE_ELITE(15, 5, 20),

    FOUR_STAR_GREEN_IMPROVED(18, -3, 9),
    FOUR_STAR_GREEN_SPORT(19, -4, 10),
    FOUR_STAR_GREEN_TUNED(21, -4, 11),
    FOUR_STAR_GREEN_CUSTOM(22, -4, 12),
    //FOUR_STAR_GREEN_ELITE(24, -4, 12),

    FOUR_STAR_RED_IMPROVED(6, -3, 21),
    FOUR_STAR_RED_SPORT(6, -4, 23),
    FOUR_STAR_RED_TUNED(7, -4, 25),
    FOUR_STAR_RED_CUSTOM(7, -4, 27),
    //FOUR_STAR_RED_ELITE(8, -4, 28),

    FOUR_STAR_BLUE_IMPROVED(6, 8, 10),
    FOUR_STAR_BLUE_SPORT(6, 8, 11),
    FOUR_STAR_BLUE_TUNED(7, 9, 12),
    FOUR_STAR_BLUE_CUSTOM(7, 10, 13),
    //FOUR_STAR_BLUE_ELITE(8, 10, 14),

    FOUR_STAR_WHITE_IMPROVED(9, 3, 12),
    FOUR_STAR_WHITE_SPORT(9, 4, 12),
    FOUR_STAR_WHITE_TUNED(10, 4, 14),
    FOUR_STAR_WHITE_CUSTOM(11, 4, 15),
    //FOUR_STAR_WHITE_ELITE(12, 4, 16),

    THREE_STAR_WHITE_IMPROVED(6, 2, 8),
    THREE_STAR_WHITE_SPORT(6, 3, 8),
    THREE_STAR_WHITE_TUNED(7, 3, 10),
    THREE_STAR_WHITE_CUSTOM(8, 3, 11),
    //THREE_STAR_WHITE_ELITE(9, 3, 12),

    THREE_STAR_BLUE_IMPROVED(4, 6, 6),
    THREE_STAR_BLUE_SPORT(4, 6, 7),
    THREE_STAR_BLUE_TUNED(5, 7, 8),
    THREE_STAR_BLUE_CUSTOM(5, 8, 9),
    //THREE_STAR_BLUE_ELITE(6, 8, 10),

    THREE_STAR_GREEN_IMPROVED(12, -2, 6),
    THREE_STAR_GREEN_SPORT(13, -3, 7),
    THREE_STAR_GREEN_TUNED(15, -3, 8),
    THREE_STAR_GREEN_CUSTOM(16, -3, 9),
    //THREE_STAR_GREEN_ELITE(18, -3, 9),

    THREE_STAR_RED_IMPROVED(4, -2, 14),
    THREE_STAR_RED_SPORT(4, -3, 16),
    THREE_STAR_RED_TUNED(5, -3, 18),
    THREE_STAR_RED_CUSTOM(5, -3, 20),
    //THREE_STAR_RED_ELITE(6, -3, 21),

    TWO_STAR_WHITE_IMPROVED(3, 1, 4),
    TWO_STAR_WHITE_SPORT(3, 2, 4),
    TWO_STAR_WHITE_TUNED(4, 2, 6),
    TWO_STAR_WHITE_CUSTOM(5, 2, 7),
    //TWO_STAR_WHITE_ELITE(6, 2, 8),

    TWO_STAR_BLUE_IMPROVED(2, 4, 2),
    TWO_STAR_BLUE_SPORT(2, 4, 3),
    TWO_STAR_BLUE_TUNED(3, 5, 4),
    TWO_STAR_BLUE_CUSTOM(3, 6, 5),
    //TWO_STAR_BLUE_ELITE(4, 6, 6),

    TWO_STAR_RED_IMPROVED(2, -1, 7),
    TWO_STAR_RED_SPORT(2, -2, 9),
    TWO_STAR_RED_TUNED(3, -2, 11),
    TWO_STAR_RED_CUSTOM(3, -2, 13),
    //TWO_STAR_RED_ELITE(4, -2, 14),

    TWO_STAR_GREEN_IMPROVED(6, -1, 3),
    TWO_STAR_GREEN_SPORT(7, -2, 4),
    TWO_STAR_GREEN_TUNED(9, -2, 5),
    TWO_STAR_GREEN_CUSTOM(10, -2, 6),
    //TWO_STAR_GREEN_ELITE(12, -2, 6),

    ONE_STAR_WHITE_IMPROVED(1, 0, 0),
    ONE_STAR_WHITE_SPORT(1, 1, 1),
    ONE_STAR_WHITE_TUNED(2, 1, 2),
    ONE_STAR_WHITE_CUSTOM(3, 1, 3),
    //ONE_STAR_WHITE_ELITE(3, 1, 4),

    ONE_STAR_BLUE_IMPROVED(0, 1, 0),
    ONE_STAR_BLUE_SPORT(0, 2, 1),
    ONE_STAR_BLUE_TUNED(1, 3, 1),
    ONE_STAR_BLUE_CUSTOM(2, 3, 2),
    //ONE_STAR_BLUE_ELITE(2, 4, 2),

    ONE_STAR_RED_IMPROVED(0, -1, 2),
    ONE_STAR_RED_SPORT(1, -1, 3),
    ONE_STAR_RED_TUNED(1, -1, 5),
    ONE_STAR_RED_CUSTOM(2, -1, 6),
    //ONE_STAR_RED_ELITE(2, -1, 7),

    ONE_STAR_GREEN_IMPROVED(2, -1, 0),
    ONE_STAR_GREEN_SPORT(3, -1, 1),
    ONE_STAR_GREEN_TUNED(4, -1, 2),
    ONE_STAR_GREEN_CUSTOM(5, -1, 3),
    //ONE_STAR_GREEN_ELITE(6, -1, 3),

    EMPTY(0,0,0)
    ;


    private int tGain, aGain, hGain;
    public static final TireParts[] VALUES = values();
    public static final TireParts[] GREEN_VALUES = {FIVE_STAR_GREEN_ELITE, FIVE_STAR_GREEN_CUSTOM, FIVE_STAR_GREEN_TUNED, FIVE_STAR_GREEN_SPORT, FIVE_STAR_GREEN_IMPROVED, FOUR_STAR_GREEN_CUSTOM, FOUR_STAR_GREEN_TUNED, FOUR_STAR_GREEN_SPORT, FOUR_STAR_GREEN_IMPROVED, THREE_STAR_GREEN_CUSTOM, THREE_STAR_GREEN_TUNED, THREE_STAR_GREEN_SPORT, THREE_STAR_GREEN_IMPROVED, TWO_STAR_GREEN_CUSTOM, TWO_STAR_GREEN_TUNED, TWO_STAR_GREEN_SPORT, TWO_STAR_GREEN_IMPROVED, ONE_STAR_GREEN_CUSTOM, ONE_STAR_GREEN_TUNED, ONE_STAR_GREEN_SPORT, ONE_STAR_GREEN_IMPROVED,EMPTY};
    public static final TireParts[] BLUE_VALUES = {FIVE_STAR_BLUE_ELITE, FIVE_STAR_BLUE_CUSTOM, FIVE_STAR_BLUE_TUNED, FIVE_STAR_BLUE_SPORT, FIVE_STAR_BLUE_IMPROVED, FOUR_STAR_BLUE_CUSTOM, FOUR_STAR_BLUE_TUNED, FOUR_STAR_BLUE_SPORT, FOUR_STAR_BLUE_IMPROVED, THREE_STAR_BLUE_CUSTOM, THREE_STAR_BLUE_TUNED, THREE_STAR_BLUE_SPORT, THREE_STAR_BLUE_IMPROVED, TWO_STAR_BLUE_CUSTOM, TWO_STAR_BLUE_TUNED, TWO_STAR_BLUE_SPORT, TWO_STAR_BLUE_IMPROVED, ONE_STAR_BLUE_CUSTOM, ONE_STAR_BLUE_TUNED, ONE_STAR_BLUE_SPORT, ONE_STAR_BLUE_IMPROVED,EMPTY};
    public static final TireParts[] RED_VALUES = {FIVE_STAR_RED_ELITE, FIVE_STAR_RED_CUSTOM, FIVE_STAR_RED_TUNED, FIVE_STAR_RED_SPORT, FIVE_STAR_RED_IMPROVED, FOUR_STAR_RED_CUSTOM, FOUR_STAR_RED_TUNED, FOUR_STAR_RED_SPORT, FOUR_STAR_RED_IMPROVED, THREE_STAR_RED_CUSTOM, THREE_STAR_RED_TUNED, THREE_STAR_RED_SPORT, THREE_STAR_RED_IMPROVED, TWO_STAR_RED_CUSTOM, TWO_STAR_RED_TUNED, TWO_STAR_RED_SPORT, TWO_STAR_RED_IMPROVED, ONE_STAR_RED_CUSTOM, ONE_STAR_RED_TUNED, ONE_STAR_RED_SPORT, ONE_STAR_RED_IMPROVED,EMPTY};
    public static final TireParts[] WHITE_VALUES = {FIVE_STAR_WHITE_ELITE, FIVE_STAR_WHITE_CUSTOM, FIVE_STAR_WHITE_TUNED, FIVE_STAR_WHITE_SPORT, FIVE_STAR_WHITE_IMPROVED, FOUR_STAR_WHITE_CUSTOM, FOUR_STAR_WHITE_TUNED, FOUR_STAR_WHITE_SPORT, FOUR_STAR_WHITE_IMPROVED, THREE_STAR_WHITE_CUSTOM, THREE_STAR_WHITE_TUNED, THREE_STAR_WHITE_SPORT, THREE_STAR_WHITE_IMPROVED, TWO_STAR_WHITE_CUSTOM, TWO_STAR_WHITE_TUNED, TWO_STAR_WHITE_SPORT, TWO_STAR_WHITE_IMPROVED, ONE_STAR_WHITE_CUSTOM, ONE_STAR_WHITE_TUNED, ONE_STAR_WHITE_SPORT, ONE_STAR_WHITE_IMPROVED,EMPTY};

    TireParts(int t, int a, int h) {
        this.tGain = t;
        this.hGain = h;
        this.aGain = a;
    }

    public int tGain() {
        return tGain;
    }

    public int aGain() {
        return aGain;
    }

    public int hGain() {
        return hGain;
    }

    @Override
    public String toString() {
        return ValueFilter.formattedName(this);
    }
}
