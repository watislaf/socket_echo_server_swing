package View;

import controller.Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyListenerG implements KeyListener {
    Controller controller_;

    KeyListenerG(Controller controller) {
        controller_ = controller;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        String x = "" + e.getKeyChar();
        controller_.KeyDown(x.toLowerCase().toCharArray()[0]);
    }
}
