package View;

import controller.Controller;
import Objects.Drawable;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GUI {
    private GuiActionListener listener;
    private Controller controller_;

    private JButton start_button_;
    private MyPanel start_panel_;

    final Point WINDOW_SIZE_ = new Point(600, 400);
    Integer start_start_degree_ = 10;

    private JFrame frame_;
    private Timer main_timer_;


    class MyPanel extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (controller_.GetScenario() == Controller.Scenario.GameProcess) {
                DrawScore(g2d);

                int help_menu = controller_.GetGameHelpMenuId();
                if (help_menu == 1) {
                    DrawHelpCercle(g2d);
                }
                if (help_menu == 2) {
                    DrawHelpW(g2d);
                }
            }

            ArrayList<Drawable> drawable = controller_.GetDrawable();
            for (Drawable elem : drawable) {
                elem.Draw(g2d, GetGlobalOffset());
            }
            DrawServerStatus(g2d);
            DrawPayLoad(g2d);

        }

        public void DrawServerStatus(Graphics2D g2d) {
            if (controller_.GetServerStatus() == 200) {
                g2d.setColor(Color.ORANGE);
            } else {
                g2d.setColor(Color.RED);
            }
            g2d.fill(new Rectangle(0, 0, 10, 10));
        }

        private void DrawPayLoad(Graphics2D g2d) {
            g2d.setColor(Color.black);
            start_start_degree_ += 4;
            start_start_degree_ %= 360;
            int f = 10;
            Point star_pos = GetCenter();
            star_pos.x -= 40;
            Integer start_size = 200;
            star_pos.y -= start_size / 20;
            for (int i = 0; i < f; i++) {
                Double deg = (start_start_degree_ + i * 360 / f) * Math.PI / 180;
                Integer c = (int) (Math.cos(deg) * start_size);
                Integer s = (int) (Math.sin(deg) * start_size);

                deg = (start_start_degree_ + (i + 5) * 360 / f) * Math.PI / 180;
                Integer c2 = (int) (Math.cos(deg) * (start_size - 60 * i));
                Integer s2 = (int) (Math.sin(deg) * (start_size));
                g2d.drawLine(star_pos.x + c, star_pos.y + s2, star_pos.x + c2, star_pos.y + s);
            }
        }

    }

    private void DrawHelpW(Graphics2D g2d) {
        Point help_center = controller_.GetMainShapePosition();
        help_center.x -= 30;
        help_center.y += 25;
        g2d.setColor(Color.gray.darker().darker());
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, 60));
        g2d.drawString("W", help_center.x, help_center.y);
    }

    private void DrawHelpCercle(Graphics2D g2d) {
        g2d.setColor(Color.gray.darker().darker());
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, 40));
        String characters_to_use = "FCXSER";
        Point help_center = controller_.GetMainShapePosition();
        help_center.x -= 12;
        help_center.y += 12;
        for (int x = 0; x < characters_to_use.length(); x++) {
            Double c = Math.cos(x * 60 * Math.PI / 180.) * (120);
            Double s = Math.sin(x * 60 * Math.PI / 180.) * (120);
            g2d.drawString("" + characters_to_use.charAt(x), help_center.x + c.intValue(), help_center.y + s.intValue());
        }

        g2d.drawString("D", help_center.x, help_center.y);
    }

    public Point GetCenter() {
        return new Point(frame_.getSize().width / 2, frame_.getSize().height / 2 - 20);
    }

    public Point GetGlobalOffset() {
        return frame_.getLocation();
    }

    private void DrawScore(Graphics2D g2d) {
        Integer points = controller_.GameGetShapesCount();
        g2d.setColor(Color.ORANGE);
        if (points == 0 || points.equals(controller_.GameGetMaxShapes())) {
            g2d.setColor(Color.RED);
        }
        g2d.drawString(points.toString(), 30, 10);
    }


    public void StartWindow() {
        InitializeListener();
        InitializeWindow();
        InitializeButtons();
        InitializeTimer(60);
    }

    private void InitializeButtons() {
        start_button_ = new JButton("Start");
        int buttonWidth = 150;
        int buttonHeight = 100;
        start_button_.setSize(new Dimension(buttonWidth, buttonHeight));
        start_button_.setFocusable(false);
        start_button_.addActionListener(listener);
        start_button_.setBackground(Color.black);
        start_button_.setFocusPainted(false);
        start_button_.setForeground(Color.white);
        start_button_.setVisible(false);
        start_panel_.add(start_button_);
    }

    private void InitializeTimer(int interval) {
        main_timer_ = new Timer(interval, listener);
        main_timer_.start();
    }

    private void InitializeWindow() {
        start_panel_ = new MyPanel();
        start_panel_.setBackground(Color.BLACK);
        start_panel_.setLayout(null);

        KeyListenerG key_listener = new KeyListenerG(controller_);
        frame_ = new JFrame("Oh yes this is laba2");
        frame_.addKeyListener(key_listener);
        frame_.setSize(WINDOW_SIZE_.x, WINDOW_SIZE_.y);
        frame_.setVisible(true);
        frame_.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame_.add(start_panel_);
        DialogWindow();
    }

    private void DialogWindow() {
        int n = 2;
        while (n == 2) {
            Object[] options = {"Yes, please",
                    "No, thanks",
                    "what?"};
            n = JOptionPane.showOptionDialog(frame_,
                    "This app need some friends... Can it make more ?",
                    "A Silly Question",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[2]);
        }
        if (n == 0) {
            String OS = System.getProperty("os.name").toLowerCase();
            if (OS.contains("win")) {
                try {
                    // Execute command
                    String command = "cmd /c java -jar socket_echo_server_swing.jar ";
                    Process child = Runtime.getRuntime().exec(command);

                }catch (Exception ignore){}
                return;
            }
            System.out.println(OS);
            if (OS.contains("nix") || OS.contains("nux")) {
                try {
                    String[] commands = {"bash", "-c", "java -jar socket_echo_server_swing.jar &"};
                    Runtime r = Runtime.getRuntime();
                    BufferedReader b = new BufferedReader(new InputStreamReader(
                            r.exec(commands).getInputStream()
                    ));
                    String line = "";
                    while ((line = b.readLine()) != null) {
                        System.out.println(line);
                    }
                    b.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void InitializeListener() {
        listener = new GuiActionListener(this);
    }

    public JButton GetStartButton() {
        return start_button_;
    }

    public Timer GetMainTimer() {
        return main_timer_;
    }

    public void SetController(Controller controller) {
        controller_ = controller;
    }

    final public Controller GetController() {
        return controller_;
    }

    public void SetVisibleStartButton(boolean b) {
        start_button_.setVisible(b);
    }

    public void SetStartButtonPosition(Double button_up_percent_) {
        int buttonWidth = 20;
        int buttonHeight = 20;
        double y_position = (WINDOW_SIZE_.y - buttonHeight) * button_up_percent_ / 200.;
        start_button_.setBounds(
                new Rectangle((WINDOW_SIZE_.x - buttonWidth) / 2, (int) y_position
                        , 20, 20));


    }

    public void OpenStartButton() {
        int buttonWidth = 150;
        int buttonHeight = 100;
        double y_position = (WINDOW_SIZE_.y - buttonHeight * 1.5) * 100 / 200.;
        start_button_.setBounds(
                new Rectangle((WINDOW_SIZE_.x - buttonWidth) / 2, (int) y_position
                        , buttonWidth, buttonHeight));
    }

    public void SetBackGroundColorPercent(int color_percent) {
        int channel = 255 * color_percent / 100;
        start_panel_.setBackground(new Color(channel, channel, channel));
    }

    public void Repaint() {
        frame_.repaint();
    }
}
