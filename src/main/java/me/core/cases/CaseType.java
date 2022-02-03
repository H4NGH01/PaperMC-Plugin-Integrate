package me.core.cases;

public enum CaseType {

    WEAPON_CASE("weapon_case", "Weapon Case", WeaponCase.class);

    private final String id;
    private final String name;
    private final Class<? extends Case> caseIn;

    CaseType(String id, String name, Class<? extends Case> caseIn) {
        this.id = id;
        this.name = name;
        this.caseIn = caseIn;
    }

    public String getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Class<? extends Case> getCase() {
        return this.caseIn;
    }
}
