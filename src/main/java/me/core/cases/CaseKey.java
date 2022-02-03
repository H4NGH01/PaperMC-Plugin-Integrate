package me.core.cases;

public enum CaseKey {

    KEY_WEAPON_CASE("key_weapon_case", "Weapon Case Key", CaseType.WEAPON_CASE);

    private final String id;
    private final String name;
    private final CaseType canOpen;

    CaseKey(String id, String name, CaseType canOpen) {
        this.id = id;
        this.name = name;
        this.canOpen = canOpen;
    }

    public String getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public CaseType canOpen() {
        return this.canOpen;
    }

}
