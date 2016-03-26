/**
 * Scientific publishing
 * Copyright (C) 2015-2016  Bozhidar Bozhanov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.scipub.model;

import java.util.Locale;

public enum Language {
    EN("English", "en"), 
    DE("Deutsch", "de"),
    ES("Español", "es"),
    FR("Français", "fr"),
    RU("Русский", "ru"),
    JA("Japanese", "ja"),
    ZH("Chinese", "zh"),
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