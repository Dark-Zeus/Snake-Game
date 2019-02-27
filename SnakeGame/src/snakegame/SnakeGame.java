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
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.Vector;
import processing.core.*;
import processing.sound.*;

public class SnakeGame extends PApplet{
 /*Screen Settings*/   
    static int WIDTH=500;
    static int HEIGHT=500;
    static int SB_SIZE=40;
    
    static int Lines_V=10;
    static int Lines_H=10;
    
    
 /*Game Over Settings*/   
    int game_os_time=2000;
    

/*Color Settings*/
    /*Snake*/
    Color color_snake=new Color(0, 255, 255);
    /*Head Color*/
    Color color_snakeh=new Color(0, 190, 190);
    
    /*Food*/
    Color color_food=new Color(255, 215, 0);
    
    
/*Snake Settings*/
    /*Default Direction*/
    int snake_dir=0;
    
    /*Snake Speed*/
    float speed=3;//Cells per Secound
    
    
/*Food Settings*/
    /*Max Foods*/
    int max_fc=3;  
    
    
/*Permenet Settings(DO NOT CHANGE!!!)*/    
    /*Sound Files*/
    SoundFile crash;
    SoundFile eat;
    
    /*Speed Calculation*/
    float interval=(1/speed)*1000;
    
    /*Cell Width & Hieght Calculation*/
    static int CELL_WIDTH=WIDTH/Lines_H;
    static int CELL_HEIGHT=HEIGHT/Lines_V;
    
    /*Moves Array*/
    int moves[][]={{0,-1},{0,1},{-1,0},{1,0}};
    
    /*Cell Vectors(Arrays)*/
        /*Snake*/
        Vector <Cell> snake;
        /*Foods*/
        Vector <Cell> foods;
        
    /*Random Number Generator*/
    Random random=new Random();
        
    /*Constants*/ 
         /*Start score*/
        int score=0; 
        /*Snake Start Color*/
        int scb=255;
        /*Times*/
        int MIN=0;
        int SEC=0;
        long tc=0; 
        boolean start=false;
    
    public static void main(String[] args) {
        PApplet.main(new String[]{"snakegame.SnakeGame"});
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT+SB_SIZE);
    }

   // Cell cell=new Cell(Color.yellow, 0 ,0);
    
    @Override
    public void setup() {
        crash = new SoundFile(this, "sounds\\crash.wav");
        eat = new SoundFile(this, "sounds\\eat.wav");
        Cell.HEIGHT=CELL_HEIGHT;
        Cell.WIDTH=CELL_WIDTH;
        System.out.println(interval);

        initGame();
    }

    boolean key_lock=false;
    long l_time;
    boolean game_over=false;
    long game_ot=0;
    long crash_time=0;
    @Override
    public void draw() {
        if(game_over){
            if(millis()-crash_time>game_os_time){
            crash_time=millis();
            crash.play(); 
        }
            gameOver();
            if(millis()-game_ot>game_os_time){
                game_over=false;
                initGame();
            }
            return;
        }
        
        if(keyPressed){
            if(!key_lock){
                if(keyCode==KeyEvent.VK_UP){
                    snake_dir=0;
                }else if(keyCode==KeyEvent.VK_DOWN){
                    snake_dir=1;
                }else if(keyCode==KeyEvent.VK_LEFT){
                    snake_dir=2;
                }else if(keyCode==KeyEvent.VK_RIGHT){
                    snake_dir=3;
                }
                System.out.println(snake_dir);
            }
            key_lock=true;
        }else{
            key_lock=false;
        }
        
        if(millis()-l_time>interval){
            l_time= millis();
            moveSnake();
            gameLogic();
        }
        
        background(70, 10, 30); //Bakground Color 
        
    /*Drawing Grid*/
        stroke(0, 40, 40);
        for (int w = 0; w < WIDTH; w+=CELL_WIDTH) {
            
            line(w, 0, w, HEIGHT);
        }
         for (int h = 0; h < HEIGHT; h+=CELL_HEIGHT) {
            line(0, h, WIDTH, h);
        }
    /**************/  
    
        //cell.draw(this); //Calling Draw Function To Draw Cells
        
        for (Cell s : snake) {
            s.draw(this);
        }
        
         for (Cell f : foods) {
            f.draw(this);
        }
         
    /*Score Board*/
        noStroke(); 
        fill(0, 150, 150);
        rect(0, HEIGHT, WIDTH, HEIGHT+SB_SIZE);
        
        if(millis()-tc>1000){
            tc=millis();
            SEC+=1;
        }
        if(SEC>59){
            MIN+=1;
            SEC=0;        
        }
        /*Time*/
        fill(255, 215, 0);
        textFont(new PFont(new Font("Agency FB", Font.BOLD, 35), true));
        if(SEC<10 && MIN<10){
            text("0"+MIN+":0"+SEC,WIDTH-WIDTH+30, HEIGHT+35);
        }else if(SEC>=10 && MIN<10){
            text("0"+MIN+":"+SEC,WIDTH-WIDTH+30, HEIGHT+35);
        }else if(SEC>=10 && MIN>=10){
            text(MIN+":"+SEC,WIDTH-WIDTH+30, HEIGHT+35);
        }else if(SEC<10 && MIN>=10){
            text(MIN+":0"+SEC,WIDTH-WIDTH+30, HEIGHT+35);
        }
        /*Score*/
        fill(255, 215, 0);
        textFont(new PFont(new Font("Agency FB", Font.BOLD, 35), true));
        text(score+"/"+(Lines_H*Lines_V),WIDTH-130, HEIGHT+35);
    /***************/
    }

    private void initGame() {
        snake_dir=0;
        score=0; 
        SEC=0;
        MIN=0;
        snake=new Vector<>();
        int snake_p=random.nextInt(Lines_H);
        snake.add(new Cell(color_snakeh, snake_p, Lines_V-3));
        snake.add(new Cell(color_snake, snake_p, Lines_V-2));
        snake.add(new Cell(color_snake, snake_p, Lines_V-1));
        
        foods=new Vector<>();
        createFoods();
    }

    private void moveSnake() {
        for (int s = snake.size() -1; s > 0; s--) {
            snake.get(s).setX(snake.get(s-1).getX());
            snake.get(s).setY(snake.get(s-1).getY());
        }
        int x=snake.get(0).getX()+moves[snake_dir][0];
        int y=snake.get(0).getY()+moves[snake_dir][1];
        snake.get(0).setX(x);
        snake.get(0).setY(y);
    }   

    private void createFoods() {
        while (foods.size()<max_fc){
            int fx=random.nextInt(Lines_H);
            int fy=random.nextInt(Lines_V);
            boolean onSnake=false;
            
            for (Cell s: snake){
                if(s.getX()==fx && s.getY()==fy){
                    onSnake=true;
                }
            }
            if(!onSnake){
                  foods.add(new Cell(color_food, fx, fy));
            }
            
        }
    }

    private void gameLogic() {
        int hx=snake.get(0).getX();
        int hy=snake.get(0).getY();
        
        if(hx<0 || hx> Lines_H-1 || hy<0 || hy> Lines_V-1){
            game_over=true;
            game_ot=millis();
        }
        for (int i = 1; i < snake.size(); i++) {
            if(snake.get(i).getX() == hx && snake.get(i).getY() == hy){
                game_over=true;
                game_ot=millis();
            }
        }
        
        for (int i = 0; i < foods.size(); i++) {
            if(foods.get(i).getX()==snake.get(0).getX() && foods.get(i).getY()==snake.get(0).getY()){
                foods.removeElementAt(i);
                createFoods();
                int lx=snake.get(snake.size()-1).getX();
                int ly=snake.get(snake.size()-1).getY();
                if(scb<5){
                    scb=255;
                }
                Color snake_newc=new Color(0, 255, scb);
                snake.add(new Cell(snake_newc, lx, ly));
                eat.play();
                score++;
            }
        }
    }
        
    private void gameOver() {
        fill(0, 0, 0, 10);
        rect(0, 0, WIDTH, HEIGHT);
        fill(70, 10, 30);
        textFont(new PFont(new Font("Agency FB", Font.BOLD, 55), true));
        text("GAME OVER!",WIDTH/2-120, HEIGHT/2); 
    }
    
}
