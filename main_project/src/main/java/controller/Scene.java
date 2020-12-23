package controller;

abstract class Scene {
    Controller controller_;

    public Scene(Controller controller) {
        controller_ = controller;
    }

    public abstract void TickScene();

   abstract void Start();

    abstract void Stop();
}
