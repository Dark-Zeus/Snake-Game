/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * @author : M.A.Sunera Madanperuma
 * @Date   : 2019.02.15
 * @Modifications:
 *      *No Modifications
 */

package snakegame;

import java.awt.Color;
import processing.core.PApplet;

public class Cell {
    private Color color;
    private int x;
    private int y;
    
    public static int WIDTH;
    public static int HEIGHT;
    
    public Cell(Color color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }
    
    public void draw(PApplet app){
        app.fill(color.getRGB());
        app.rect(x*HEIGHT, y*WIDTH, WIDTH, HEIGHT);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
}
