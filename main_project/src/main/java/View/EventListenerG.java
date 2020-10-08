package View;

import controller.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class GuiActionListener implements ActionListener {
    private final Controller controller_;
    private final GUI gui_;

    public GuiActionListener(GUI gui) {
        gui_ = gui;
        controller_ = gui_.GetController();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == gui_.GetMainTimer()) {
            controller_.TickTheWorld();
        }

           if (e.getSource() == gui_.GetStartButton()) {
            controller_.StartButtonClick();
        }
        gui_.Repaint();
    }
}