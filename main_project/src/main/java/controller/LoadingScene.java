package controller;

import static java.lang.Math.min;

class LoadingScene extends Scene {
    double start_button_move_percent_ = 0;
    Boolean is_shown = false;

    LoadingScene(Controller controller_) {
        super(controller_);
    }

    @Override
    void Start() {
        controller_.gui_.SetVisibleStartButton(true);
        is_shown = true;
    }

    @Override
    void Stop() {
        controller_.gui_.SetVisibleStartButton(false);
        is_shown = false;
    }

    @Override
    public void TickScene() {
        // button go up
        if (!is_shown) {
            Start();
        }

        start_button_move_percent_ = min(start_button_move_percent_ + 3, 100);
        if (start_button_move_percent_ < 100) {
            controller_.gui_.SetStartButtonPosition(start_button_move_percent_);
        } else {
            controller_.gui_.OpenStartButton();
        }
    }
}
