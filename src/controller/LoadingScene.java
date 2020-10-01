package controller;

import static java.lang.Math.min;

public class LoadingScene    extends Scene {
    double button_up_percent_ = 0;
    Boolean is_shown = false;

    LoadingScene(Controller controller_) {
        super(controller_);
    }

    @Override
    public void TickScene() {
        if (!is_shown) {
            is_shown = true;
            Show();
        }
        // button go up
        button_up_percent_ += 3;
        button_up_percent_ = min(button_up_percent_, 100);
        if(button_up_percent_<100) {
            controller_.gui_.SetStartButtonPosition(button_up_percent_);
        }else{
            controller_.gui_.OpenStartButton();
        }
    }

    @Override
    public void Show() {
        controller_.gui_.SetVisibleStartButton(true);
    }

    @Override
    public void Hide() {
        controller_.gui_.SetVisibleStartButton(false);
    }
}
