package com.scipub.model;

import java.util.Locale;

public enum Language {
    EN("English", "en"), 
    DE("Deutsch", "de"),
    ES("Español", "es"),
    FR("Français", "fr"),
    RU("Русский", "ru"),
    BG("Български", "bg");
 
    private final String name;
    private final String code;

    private Language(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public Locale toLocale() {
        return new Locale(code);
    }
}