package controller;

import Objects.*;
import Server.ChangesEvent;
import Server.Message;
import containers.Backpack;
import containers.MyException;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class GameScene extends Scene {
    int white_screen_percent;
    // personal data
    int max_shapes = 10;
    Backpack<SmallCube> cubes_ = new Backpack<>(max_shapes);
    Backpack<SmallBall> balls_ = new Backpack<>(max_shapes);
    Double catch_distance = 100.;
    Drawable main_shape_setted_ = null;
    // data to draw
    ArrayList<Drawable> shapes_around_ = new ArrayList<>();
    boolean IsCube = false;
    boolean ControlHelpToBeShown = true;
    boolean CatchItHelpToBeShown = false;

    public ArrayList<Drawable> GetDrawable() {
        return shapes_around_;
    }

    public void KeyDown(char keyChar) {
        if (main_shape_setted_ == null) {
            return;
        }
        Point center_position = main_shape_setted_.GetPosition();
        int angular = 0;
        switch (keyChar) {
            case 'f':
                angular = 0;
                break;
            case 'c':
                angular = 60;
                break;
            case 'x':
                angular = 120;
                break;
            case 's':
                angular = 180;
                break;
            case 'e':
                angular = 240;
                break;
            case 'r':
                angular = 300;
                break;

            case 'd':
                Message main_shape_destroy;
                if (ControlHelpToBeShown) {
                    ControlHelpToBeShown = false;
                }
                if (!IsCube) {
                    main_shape_destroy = new Message(ChangesEvent.bigBallRemoved, 0, 0, 0, main_shape_setted_.GetUniqId());
                } else {
                    main_shape_destroy = new Message(ChangesEvent.bigCubeRemoved, 0, 0, 0, main_shape_setted_.GetUniqId());
                }
                controller_.my_client_.SendMessage(main_shape_destroy);
                break;
            case 'w':
                CatchTheSmall();
                break;

        }
        if (CanCreate() && "serfcx".contains("" + keyChar)) {
            RemoveOne();
            ChangesEvent create;
            if (IsCube) {
                create = ChangesEvent.smallCubeCreated;
            } else {
                create = ChangesEvent.smallBallCreated;
            }
            Message ball_creation = new Message(create, center_position.x, center_position.y,
                    angular, controller_.Random());
            controller_.my_client_.SendMessage(ball_creation);
        }
    }

    private void RemoveOne() {
        if (IsCube) {
            cubes_.removeShape();
        } else {
            balls_.removeShape();
        }
    }

    private boolean CanCreate() {
        if (IsCube) {
            System.out.println(cubes_.Size());
            return cubes_.Size() > 0;
        } else {
            return balls_.Size() > 0;
        }
    }

    private void CatchTheSmall() {
        Drawable to_catch = CanCatch();
        if (to_catch == null) {
            return;
        }
        AddOne();
        ChangesEvent remove_object = ChangesEvent.smallCubeRemoved;
        if (main_shape_setted_.getClass() == BigBall.class) {
            remove_object = ChangesEvent.smallBallRemoved;
        }
        controller_.my_client_.SendMessage(new Message(remove_object, 0, 0, 0, to_catch.GetUniqId()));
    }

    private Drawable CanCatch() {
        boolean isBall = false;
        if (main_shape_setted_ == null) {
            return null;
        }
        if (main_shape_setted_.getClass() == BigBall.class) {
            isBall = true;
        }

        Drawable nearest = null;

        for (Drawable object : shapes_around_) {
            if (isBall && object.getClass() == SmallBall.class && balls_.Size() < 10) { // ball and ball
                nearest = GetNearestShape(nearest, object);
            }
            if (!isBall && object.getClass() == SmallCube.class && cubes_.Size() < 10) { // not ball and not ball
                nearest = GetNearestShape(nearest, object);
            }
        }
        if (nearest == null) {
            return null;
        }
        if (main_shape_setted_.Distance(nearest) < catch_distance) {
            return nearest;
        }
        return null;
    }

    private void AddOne() {
        try {
            if (IsCube) {
                cubes_.addShape(new SmallCube());
            } else {
                balls_.addShape(new SmallBall());
            }
        } catch (MyException e) {

        }
    }

    private Drawable GetNearestShape(Drawable nearest, Drawable possible_nearest) {
        if (nearest == null) {
            return possible_nearest;
        }
        if (abs(main_shape_setted_.Distance(possible_nearest)) < abs(main_shape_setted_.Distance(nearest))) {
            return possible_nearest;
        }
        return nearest;
    }

    Integer my_uniq_window_id_;

    public GameScene(Controller controller) {
        super(controller);
    }

    @Override
    public void TickScene() {
        controller_.my_client_.Tick();
        ProcessNewMessage();
        if (white_screen_percent > 0) {
            white_screen_percent -= 10;
            white_screen_percent = max(0, white_screen_percent);
            controller_.gui_.SetBackGroundColorPercent(white_screen_percent);
        }

        ArrayList<Drawable> to_remove = new ArrayList<Drawable>();
        for (Drawable shape : shapes_around_) {
            shape.Tick();
            if (shape.IsDead()) {
                to_remove.add(shape);
            }
        }
        for (Drawable obj : to_remove) {
            shapes_around_.remove(obj);
        }
        CatchItHelpToBeShown = CanCatch() != null;

    }

    private void ProcessNewMessage() {
        while (!controller_.unreadMessages.isEmpty()) {
            Message message = controller_.unreadMessages.get(0);
            controller_.unreadMessages.remove(0);
            if (message.getEvent() != ChangesEvent.newUserHello &&
                    message.getPrivilage() != 0 && !my_uniq_window_id_.equals(message.getPrivilage())) {
                continue;
            }
            switch (message.getEvent()) {
                case newUserHello:
                    CreateFlash();
                    if (controller_.server_status_ == 200) {
                        SceneTransfer(message);
                    }
                    break;

                case bigBallRemoved:
                case bigCubeRemoved:
                case smallBallRemoved:
                case smallCubeRemoved:
                    RemoveObject(message);
                    break;

                case bigCubeCreated:
                case bigBallCreated:
                    CreateBig(message);
                    break;

                case smallCubeCreated:
                case smallBallCreated:
                    CreateSmall(message);
                    break;

                case BoomCreated:
                    shapes_around_.add(new Boom(message));
                    break;
            }
        }
    }

    private void CreateSmall(Message message) {
        if (message.getEvent() == ChangesEvent.smallBallCreated) {
            shapes_around_.add(new SmallBall(message));
        } else {
            shapes_around_.add(new SmallCube(message));
        }
    }


    private void ChangeShape() {
        CreateFlash();
        IsCube = !IsCube;
    }

    private void RemoveObject(Message message) {
        for (Drawable obj : shapes_around_) {
            if (obj.GetUniqId().equals(message.getUniqId())) {
                if (obj.GetUniqId().equals(my_uniq_window_id_)) {
                    ChangesEvent reverse;
                    if (message.getEvent() == ChangesEvent.bigCubeRemoved) {
                        reverse = ChangesEvent.bigBallCreated;
                    } else {
                        reverse = ChangesEvent.bigCubeCreated;
                    }
                    ChangeShape();
                    controller_.my_client_.SendMessage(new Message(
                            reverse, main_shape_setted_.GetPosition().x,
                            main_shape_setted_.GetPosition().y, 0, my_uniq_window_id_));
                    main_shape_setted_ = null;
                }

                if (message.getEvent() == ChangesEvent.smallBallRemoved ||
                        message.getEvent() == ChangesEvent.smallCubeRemoved) {
                    CreateBoom(obj.GetPosition());
                }
                shapes_around_.remove(obj);
                return;
            }
        }
    }

    private void CreateBoom(Point getPosition) {
        controller_.my_client_.SendMessage(new Message(ChangesEvent.BoomCreated, getPosition.x, getPosition.y,
                0, controller_.Random()));
    }

    private void CreateFlash() {
        controller_.gui_.SetBackGroundColorPercent(100);
        white_screen_percent = 100;
        for (Drawable obj : shapes_around_) {
            obj.ReloadAnimation();
        }
    }

    private void CreateBig(Message message) {
        if (message.getEvent() == ChangesEvent.bigBallCreated) {
            shapes_around_.add(new BigBall(message));
        } else {
            shapes_around_.add(new BigCube(message));
        }
        if (main_shape_setted_ == null) {
            main_shape_setted_ = shapes_around_.get(shapes_around_.size() - 1);
            main_shape_setted_.SetMainColor(Color.ORANGE);
        }
    }

    private void SceneTransfer(Message message) {
        for (Drawable object : shapes_around_) {
            Message object_in_message = object.PackToMessage(message.getAdditionalVar());
            object_in_message.setPrivilage(message.getPrivilage());
            controller_.my_client_.SendMessage(object_in_message);
        }
    }

    @Override
    public void Show() {
        for (int i = 0; i < max_shapes; i++) {
            try {
                cubes_.addShape(new SmallCube());
                balls_.addShape(new SmallBall());
            } catch (Exception ignored) {
            }
        }
        controller_.InitializeConnection();
        my_uniq_window_id_ = controller_.Random();
        Message hello_message = new Message(ChangesEvent.newUserHello, 0, 0, 0, 0);
        hello_message.setPrivilage(my_uniq_window_id_);
        controller_.my_client_.SendMessage(hello_message);

        Point center = new Point(
                controller_.gui_.GetCenter().x + controller_.gui_.GetGlobalOffset().x,
                controller_.gui_.GetCenter().y + controller_.gui_.GetGlobalOffset().y);

        Message start_shape_creation;
        start_shape_creation = new Message(ChangesEvent.bigBallCreated, center.x, center.y, 0, my_uniq_window_id_);

        controller_.my_client_.SendMessage(start_shape_creation);
    }


    @Override
    public void Hide() {
        controller_.gui_.SetBackGroundColorPercent(0);
    }

    public Integer GetShapesCount() {
        if (IsCube) {
            return cubes_.Size();
        } else {
            return balls_.Size();
        }
    }

    public int GetHelpMenu() {
        if (ControlHelpToBeShown) {
            return 1;
        }
        if (CatchItHelpToBeShown) {
            return 2;
        }
        return 0;
    }
}
