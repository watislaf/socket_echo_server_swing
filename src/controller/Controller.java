package controller;

import Server.Message;
import Server.MyClient;
import Server.MyServer;
import View.GUI;
import Objects.Drawable;

import java.awt.*;
import java.util.ArrayList;

public class Controller {
    public enum Scenario {LoadingScreen, GameProcess;}

    ArrayList<Message> unread_messages_from_server_ = new ArrayList<>();

    Scenario current_scenario_ = Scenario.LoadingScreen;
    LoadingScene loading_scene_;
    GameScene game_scene_;
    MyServer server_;
    MyClient client_;
    GUI gui_;

    public Controller(GUI gui) {
        loading_scene_ = new LoadingScene(this);
        game_scene_ = new GameScene(this);
        gui_ = gui;
    }

    public void InitializeConnection() {
        server_ = new MyServer(this);
        client_ = new MyClient(this);
    }

    public void StartButtonClick() {
        loading_scene_.Stop();
        game_scene_.Start();
    }

    public void TickTheWorld() {
        switch (current_scenario_) {
            case LoadingScreen -> loading_scene_.TickScene();
            case GameProcess -> game_scene_.TickScene();
        }
    }

    public void KeyDown(char key_char) {
        if (current_scenario_ == Scenario.GameProcess) {
            game_scene_.KeyDown(key_char);
        }
    }

    public ArrayList<Drawable> GetDrawable() {
        if (current_scenario_ == Scenario.GameProcess) {
            return game_scene_.GetDrawable();
        }
        return new ArrayList<>();
    }

    public int GetServerStatus() {
        if (server_ == null) return 404;
        return server_.GetStatus();
    }

    public Scenario GetScenario() {
        return current_scenario_;
    }

    public Integer GameGetShapesCount() {
        return game_scene_.GetShapesCount();
    }

    public Point GetMainShapePosition() {
        if (game_scene_.main_shape_setted_ == null) {
            // out of plane position
            return new Point(-1000, -1000);
        }
        Point main_shape_position = new Point(game_scene_.main_shape_setted_.GetPosition());
        Point main_frame_offset = gui_.GetGlobalOffset();
        main_shape_position.x -= main_frame_offset.x;
        main_shape_position.y -= main_frame_offset.y;
        return main_shape_position;
    }

    public void AddNewUnreadMessage(Message message) {
        unread_messages_from_server_.add(message);
    }

    public int GetGameHelpMenuId() {
        return game_scene_.GetHelpMenuId();
    }

    public Integer GameGetMaxShapes() {
        return game_scene_.max_shapes_;
    }

    Integer GenerateNewId() {
        while (true) {
            double d = Math.random() * 10000;
            int x = ((int) d) % 10000;
            boolean valid = true;
            for (Drawable obj : game_scene_.shapes_around_) {
                if (obj.GetUniqId() == x) {
                    valid = false;
                    System.out.println("random loose");
                }
            }
            if (valid)
                return x;
        }
    }
}
