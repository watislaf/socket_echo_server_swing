package controller;

public abstract class Scene {
    Controller controller_;
    public Scene(Controller controller) {
        controller_ = controller;
    }

    public abstract void TickScene();
    public abstract void Show();
    public abstract void Hide();
}
