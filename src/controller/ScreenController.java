package controller;

import common.ControlledScreen;

/**
 * Controller of screen
 */
public class ScreenController implements ControlledScreen {

    public ScreensController screensController;

    @Override
    public void setScreenParent(ScreensController screenParent){
        screensController = screenParent;
    }
}
