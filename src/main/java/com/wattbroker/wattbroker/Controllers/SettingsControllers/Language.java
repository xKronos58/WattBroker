package com.wattbroker.wattbroker.Controllers.SettingsControllers;

/**
 * Languages supported by wattbroker for translations * translations will not be 100% accurate aside from english */
public enum Language {
    BELARUSIAN, CATALAN, CZECH, DANISH, DUTCH, ENGLISH, ESTONIAN, FINNISH, FRENCH, GERMAN, GREEK, HUNGARIAN, ICELANDIC, ITALIAN, LATVIAN, LITHUANIAN, NORWEGIAN, POLISH, PORTUGUESE, ROMANIAN, RUSSIAN, SERBIAN, SLOVAK, SLOVENIAN, SPANISH, SWEDISH, UKRAINIAN;

    @Override
    public String toString() {
        return name().substring(0,1).toUpperCase() + name().substring(1).toLowerCase();
    }
}
