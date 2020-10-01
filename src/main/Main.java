package main;

import controller.Controller;
import View.GUI;

public class Main {
    public static void main(String[] args) {
        GUI gui = new GUI();
        Controller controller_ = new Controller(gui);
        gui.SetController(controller_);
             gui.StartWindow();
/*
        Ball ball = new Ball(42.0);
        Cube cube = new Cube(4.0);
        System.out.println(ball.getVolume());
        System.out.println(cube.getVolume());
        NameGiver nameGiver = new NameGiver();
        ball.acceptVisitor(nameGiver);
        cube.acceptVisitor(nameGiver);

        Backpack<Ball> backpack_balls = new Backpack<>(3);
        Backpack<Cube> backpack_cubes = new Backpack<>(3);
        backpack_balls.addShape(ball);
        backpack_cubes.addShape(cube);*/
    }
}
