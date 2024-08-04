package com.wattbroker.wattbroker.Controllers;

import javafx.scene.shape.SVGPath;

public class mapOverlayController {

    public SVGPath SVG_Icon;

    public void setContent(String state) {
        SVG_Icon.setContent(switch (state.toUpperCase()) {
            case "QLD" -> "";
            case "SA" -> "";
            default -> "0";
        });
    }
}
