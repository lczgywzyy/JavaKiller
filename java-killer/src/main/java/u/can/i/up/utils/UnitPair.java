package u.can.i.up.utils;

import soot.Unit;

public class UnitPair {
    private Unit unit;
    private int level;

    public UnitPair(Unit u, int i) {
        unit = u;
        level = i;
    }

    public Unit getUnit() {
        return unit;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "LEVEL " + Integer.valueOf(level) + ": " + unit.toString();
    }
}
