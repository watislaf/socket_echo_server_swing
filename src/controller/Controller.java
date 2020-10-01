package controller;

import Server.Message;
import Server.MyClient;
import Server.MyServer;
import View.GUI;
import Objects.Drawable;

import java.awt.*;
import java.util.ArrayList;

public class Controller {
    GUI gui_;
    Scenario scenario_;
    ArrayList<Message> unreadMessages = new ArrayList<>();
    LoadingScene loadingScene;
    GameScene gameScene;
    MyServer server;
    MyClient my_client_;
    int server_status_ = 404;

    public void StartButtonClick() {
        scenario_ = Scenario.GameProcess;
        loadingScene.Hide();
        gameScene.Show();
    }

    public ArrayList<Drawable> GetDrawable() {
        switch (scenario_) {
            case GameProcess:
                return gameScene.GetDrawable();
        }
        return new ArrayList<>();
    }

    public void KeyDown(char keyChar) {
        switch (scenario_) {
            case GameProcess:
                gameScene.KeyDown(keyChar);
        }
    }

    public void DrawServerStatus(Graphics2D g2d) {
        if (server_status_ == 200) {
            g2d.setColor(Color.ORANGE);
        } else {
            g2d.setColor(Color.RED);
        }
        g2d.fill(new Rectangle(0, 0, 10, 10));
    }

    public Scenario GetScenario() {
        return scenario_;
    }

    public void AddNewMessage(Message message) {
        unreadMessages.add(message);
    }

    public Integer Random() {
        while (true) {
            double d = Math.random() * 10000;
            int x = ((int) d) % 10000;
            boolean valid = true;
            for (Drawable obj : gameScene.shapes_around_) {
                if (obj.GetUniqId() == x) {
                    valid = false;
                    System.out.println("random loose");
                }
            }
            if (valid)
                return x;
        }
    }

    public Integer GameGetShapesCount() {
        return gameScene.GetShapesCount();
    }

    public Point GetMainShapePosition() {
        if (gameScene.main_shape_setted_ == null) {
            return new Point(-1000, -1000);
        }
        Point true_position = (Point) gameScene.main_shape_setted_.GetPosition().clone();
        Point offset = gui_.GetGlobalOffset();
        true_position.x -= offset.x;
        true_position.y -= offset.y;
        return true_position;

    }

    public int GameHelpMenu() {
        return gameScene.GetHelpMenu();
    }

    public enum Scenario {
        LoadingScreen,
        GameProcess
    }

    public Controller(GUI gui) {
        scenario_ = Scenario.LoadingScreen;
        gui_ = gui;
        loadingScene = new LoadingScene(this);
        gameScene = new GameScene(this);
    }

    public int InitializeConnection() {
        try {
            server = new MyServer(this);
            server.StartListen();
        } catch (java.nio.channels.AcceptPendingException io) {
            server_status_ = 200;
            return 200;

        } catch (Exception e) {
            return 406;

        } finally {
            try {
                my_client_ = new MyClient();
                my_client_.CreateUserConnection(this);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return 444;
    }

    public void TickTheWorld() {
        switch (scenario_) {
            case LoadingScreen:
                loadingScene.TickScene();
                break;
            case GameProcess:
                gameScene.TickScene();
                break;
        }
    }
}
