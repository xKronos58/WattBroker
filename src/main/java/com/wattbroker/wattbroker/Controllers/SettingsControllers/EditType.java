package com.wattbroker.wattbroker.Controllers.SettingsControllers;

public enum EditType {
    EMAIL("email"), USERNAME("username"), ALGORITHM("Algorithm File Location");

    final String s;
    EditType(String s) { this.s = s; }
    public String getName() { return s; }
}
