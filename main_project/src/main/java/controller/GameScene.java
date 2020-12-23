package controller;

import Objects.*;
import Server.ChangesEvent;
import Server.Message;
import containers.Backpack;
import containers.BackPackException;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.abs;
import static java.lang.Math.max;

class GameScene extends Scene {
    ArrayList<Drawable> shapes_around_ = new ArrayList<>();
    boolean CatchItHelpToBeShown = false;
    boolean ControlHelpToBeShown = true;
    Drawable main_shape_setted_ = null;
    int flash_screen_percent_ = 0;
    Double catch_distance = 100.;
    boolean is_cube = false;
    int max_shapes_ = 20;
    Backpack<SmallCube> cubes_ = new Backpack<>(max_shapes_);
    Backpack<SmallBall> balls_ = new Backpack<>(max_shapes_);

    @Override
    void Start() {
        controller_.InitializeConnection();

        my_uniq_window_id_ = controller_.GenerateNewId();
        Message hello_message = new Message(ChangesEvent.newUserHello, 0, 0, 0, 0);
        hello_message.SetPrivilege(my_uniq_window_id_);
        controller_.client_.SendMessage(hello_message);

        Point center = new Point(
                controller_.gui_.GetCenter().x + controller_.gui_.GetGlobalOffset().x,
                controller_.gui_.GetCenter().y + controller_.gui_.GetGlobalOffset().y);
        Message main_shape_creation;
        main_shape_creation = new Message(ChangesEvent.bigBallCreated, center.x, center.y, 0, my_uniq_window_id_);

        controller_.client_.SendMessage(main_shape_creation);
        controller_.current_scenario_ = Controller.Scenario.GameProcess;
        for (int i = 0; i < max_shapes_ / 2; i++) {
            try {
                cubes_.AddShape(new SmallCube());
                balls_.AddShape(new SmallBall());
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void Stop() {
        controller_.gui_.SetBackGroundColorPercent(0);
    }

    public void KeyDown(char keyChar) {
        if (main_shape_setted_ == null) {
            return;
        }
        Point center_position = main_shape_setted_.GetPosition();
        int fly_angle = 0;
        switch (keyChar) {
            case 'w': {
                CatchTheSmall();
                break;
            }
            case 'd': {
                Message main_shape_destroy;
                if (is_cube) {
                    main_shape_destroy = new Message(ChangesEvent.bigCubeRemoved, 0, 0, 0, main_shape_setted_.GetUniqId());
                } else {
                    main_shape_destroy = new Message(ChangesEvent.bigBallRemoved, 0, 0, 0, main_shape_setted_.GetUniqId());
                }
                controller_.client_.SendMessage(main_shape_destroy);
                if (ControlHelpToBeShown) {
                    ControlHelpToBeShown = false;
                }
            }
        }

        if (CanCreateTheSmall() && "serfcx".contains("" + keyChar)) {
            switch (keyChar) {
                case 'c': {
                    fly_angle = 60;
                    break;
                }
                case 'x': {
                    fly_angle = 120;
                    break;
                }
                case 's': {
                    fly_angle = 180;
                    break;
                }
                case 'e': {
                    fly_angle = 240;
                    break;
                }
                case 'r': {
                    fly_angle = 300;
                    break;
                }
            }
            RemoveOneFromBackPack();
            ChangesEvent create;
            create = is_cube ? ChangesEvent.smallCubeCreated : ChangesEvent.smallBallCreated;

            Message ball_creation = new Message(create, center_position.x, center_position.y, fly_angle,
                    controller_.GenerateNewId());
            controller_.client_.SendMessage(ball_creation);
        }
    }

    private void AddOneToBackPack() {
        try {
            if (is_cube) {
                cubes_.AddShape(new SmallCube());
            } else {
                balls_.AddShape(new SmallBall());
            }
        } catch (BackPackException e) {

        }
    }

    private void RemoveOneFromBackPack() {
        if (is_cube) {
            cubes_.RemoveShape();
        } else {
            balls_.RemoveShape();
        }
    }

    private boolean CanCreateTheSmall() {
        if (is_cube) {
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
        AddOneToBackPack();

        ChangesEvent remove_object = ChangesEvent.smallCubeRemoved;
        if (main_shape_setted_.getClass() == BigBall.class) {
            remove_object = ChangesEvent.smallBallRemoved;
        }
        controller_.client_.SendMessage(new Message(remove_object, 0, 0, 0, to_catch.GetUniqId()));
    }

    private Drawable CanCatch() {
        boolean is_ball = false;
        if (main_shape_setted_ == null) {
            return null;
        }
        if (main_shape_setted_.getClass() == BigBall.class) {
            is_ball = true;
        }

        Drawable nearest = null;
        for (Drawable object : shapes_around_) {
            if (is_ball && object.getClass() == SmallBall.class && balls_.Size() < 10) { // ball and ball
                nearest = GetNearestShape(nearest, object);
            }
            if (!is_ball && object.getClass() == SmallCube.class && cubes_.Size() < 10) { // not ball and not ball
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

    public ArrayList<Drawable> GetDrawable() {
        return shapes_around_;
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
        controller_.client_.Tick();
        ProcessNewMessage();
        if (flash_screen_percent_ > 0) {
            flash_screen_percent_ -= 10;
            flash_screen_percent_ = max(0, flash_screen_percent_);
            controller_.gui_.SetBackGroundColorPercent(flash_screen_percent_);
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

    private void SceneTransferToOtherClient(Message message) {
        for (Drawable object : shapes_around_) {
            Message object_in_message = object.PackToMessage(message.GetAdditionalVar());
            object_in_message.SetPrivilege(message.GetPrivilege());
            controller_.client_.SendMessage(object_in_message);
        }
    }

    private void ProcessNewMessage() {
        while (!controller_.unread_messages_from_server_.isEmpty()) {
            Message message = controller_.unread_messages_from_server_.get(0);
            controller_.unread_messages_from_server_.remove(0);
            if (message.GetEvent() != ChangesEvent.newUserHello && message.GetPrivilege() != 0
                    && !my_uniq_window_id_.equals(message.GetPrivilege())) {
                continue;
            }
            switch (message.GetEvent()) {
                case bigBallRemoved:
                case bigCubeRemoved:
                case smallBallRemoved:
                case smallCubeRemoved: {
                    RemoveObject(message);
                    break;
                }
                case smallCubeCreated:
                case smallBallCreated: {
                    CreateSmall(message);
                    break;
                }
                case bigCubeCreated:
                case bigBallCreated: {
                    CreateBig(message);
                    break;
                }
                case boomCreated: {
                    shapes_around_.add(new Boom(message));
                    break;
                }
                case newUserHello: {
                    CreateFlash();
                    if (controller_.server_.GetStatus() == 200) {
                        SceneTransferToOtherClient(message);
                    }
                    for (Drawable obj : shapes_around_) {
                        obj.ReloadAnimation();
                    }
                }
            }
        }
    }

    private void RemoveObject(Message message) {
        for (Drawable obj : shapes_around_) {
            if (!obj.GetUniqId().equals(message.GetUniqId())) {
                continue;
            }

            if (obj.GetUniqId().equals(my_uniq_window_id_)) {
                ChangesEvent reverse;
                ChangeShape();
                if (message.GetEvent() == ChangesEvent.bigCubeRemoved) {
                    reverse = ChangesEvent.bigBallCreated;
                } else {
                    reverse = ChangesEvent.bigCubeCreated;
                }
                controller_.client_.SendMessage(
                        new Message(
                                reverse, main_shape_setted_.GetPosition().x,
                                main_shape_setted_.GetPosition().y, 0, my_uniq_window_id_
                        )
                );
                main_shape_setted_ = null;
            }

            if (message.GetEvent() == ChangesEvent.smallBallRemoved ||
                    message.GetEvent() == ChangesEvent.smallCubeRemoved) {
                CreateBoom(obj.GetPosition());
            }
            shapes_around_.remove(obj);
            return;
        }
    }

    private void ChangeShape() {
        CreateFlash();
        is_cube = !is_cube;
    }

    private void CreateBoom(Point getPosition) {
        controller_.client_.SendMessage(new Message(ChangesEvent.boomCreated, getPosition.x, getPosition.y,
                0, controller_.GenerateNewId()));
    }

    private void CreateSmall(Message message) {
        if (message.GetEvent() == ChangesEvent.smallBallCreated) {
            shapes_around_.add(new SmallBall(message));
        } else {
            shapes_around_.add(new SmallCube(message));
        }
    }

    private void CreateFlash() {
        controller_.gui_.SetBackGroundColorPercent(100);
        flash_screen_percent_ = 100;

    }

    private void CreateBig(Message message) {
        if (message.GetEvent() == ChangesEvent.bigBallCreated) {
            shapes_around_.add(new BigBall(message));
        } else {
            shapes_around_.add(new BigCube(message));
        }
        if (main_shape_setted_ == null) {
            main_shape_setted_ = shapes_around_.get(shapes_around_.size() - 1);
            main_shape_setted_.SetMainColor(Color.ORANGE);
        }
    }

    public Integer GetShapesCount() {
        if (is_cube) {
            return cubes_.Size();
        } else {
            return balls_.Size();
        }
    }

    public int GetHelpMenuId() {
        if (ControlHelpToBeShown) {
            return 1;
        }
        if (CatchItHelpToBeShown) {
            return 2;
        }
        return 0;
    }
}
