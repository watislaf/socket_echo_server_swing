package main;

import controller.Controller;
import View.GUI;

public class Main {
    public static void main(String[] args) {
        GUI gui = new GUI();
        Controller controller_ = new Controller(gui);
        gui.SetController(controller_);
        gui.StartWindow();
    }
}
