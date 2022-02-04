package me.core.cases;

public enum CaseType {

    WEAPON_CASE("weapon_case", "case.weapon.name", WeaponCase.class);

    private final String id;
    private final String translationKey;
    private final Class<? extends Case> caseIn;

    CaseType(String id, String translationKey, Class<? extends Case> caseIn) {
        this.id = id;
        this.translationKey = translationKey;
        this.caseIn = caseIn;
    }

    public String getID() {
        return this.id;
    }

    public String getTranslationKey() {
        return this.translationKey;
    }

    public Class<? extends Case> getCase() {
        return this.caseIn;
    }
}
