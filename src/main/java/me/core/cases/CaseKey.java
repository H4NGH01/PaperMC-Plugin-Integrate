package me.core.cases;

public enum CaseKey {

    KEY_WEAPON_CASE("key_weapon_case", "case.key.weapon.name", CaseType.WEAPON_CASE);

    private final String id;
    private final String translationKey;
    private final CaseType canOpen;

    CaseKey(String id, String translationKey, CaseType canOpen) {
        this.id = id;
        this.translationKey = translationKey;
        this.canOpen = canOpen;
    }

    public String getID() {
        return this.id;
    }

    public String getTranslationKey() {
        return this.translationKey;
    }

    public CaseType canOpen() {
        return this.canOpen;
    }

}
