package com.wattbroker.wattbroker.Controllers;

import com.wattbroker.wattbroker.UserProfile;
import javafx.scene.input.MouseEvent;

public class accountController {

    public void close(MouseEvent mouseEvent) {
        UserProfile.s.close();
    }
}
