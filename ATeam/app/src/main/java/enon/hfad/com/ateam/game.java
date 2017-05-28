package enon.hfad.com.ateam;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static enon.hfad.com.ateam.MainActivity.money;
import static enon.hfad.com.ateam.R.drawable.explosion;
import static enon.hfad.com.ateam.R.drawable.next_level_button;
import static enon.hfad.com.ateam.R.drawable.small_coin;

/**
 * Created by HOME on 06.03.2017.
 */


public class game extends AppCompatActivity {
    boolean gameOver = false;
    int coins = 3;
    int dwarfs_awailable = 5;
    boolean dwarf_killed[] = new boolean[dwarfs_awailable];
    int coin_location[];
    boolean coin_animated[] = new boolean[coins];
    float xCoin, yCoin, xSpike;
    int number = 0;
    int dwarfs_alive = dwarfs_awailable;
    Random r = new Random();
    boolean game_was_paused = false;
    final ImageView[] dwarf = new ImageView[dwarfs_awailable];
    final ImageView[] coin = new ImageView[3];
    final ImageView[] spike = new ImageView[dwarfs_awailable];
    final ImageView[] lives = new ImageView[dwarfs_awailable];
    public void spawn_coin(int field, int high, int i, boolean animation){
        xCoin = r.nextFloat() * field;
        yCoin =r.nextFloat() * (float)0.7 * high;
        yCoin+=xCoin* ((float)high / field);
        if(yCoin > (float)0.7 * (float)high)yCoin = (float)0.7 * (float)high;
        if(!animation)coin[i].animate().x(xCoin).y(yCoin).setDuration(0);
        else if(!coin_animated[i]){
            coin_animated[i] = true;
            AnimatorSet relocate = new AnimatorSet();
            relocate.playSequentially(
                    ObjectAnimator.ofFloat(coin[i], View.ALPHA,0).setDuration(50),
                    ObjectAnimator.ofFloat(coin[i], View.X, xCoin).setDuration(0),
                    ObjectAnimator.ofFloat(coin[i], View.Y, yCoin).setDuration(0),
                    ObjectAnimator.ofFloat(coin[i], View.ALPHA, 1).setDuration(500)
            );
            relocate.start();
        }
        else coin_animated[i] = false;
    }
    public void spawn_lives(int live){
        for(int i = 0; i < dwarfs_awailable; i++){
            lives[i].setVisibility(View.GONE);
        }
        for(int i = 0; i < live; i++){
            lives[i].setVisibility(View.VISIBLE);
            lives[i].animate().x(20 + i*40).y(20).setDuration(0);
        }
    }
    public void spawn_spike(int field, int high, int i){
        high+=(field  + high) / 27;
        xSpike = r.nextFloat() * field;
        spike[i].animate().x(xSpike).y((float)(0.7 * (float)high)).setDuration(0);
    }
    public void pause_clicked(View view) {
        ImageView plane = (ImageView)findViewById(R.id.plane);
        game_was_paused = true;
        ImageButton pause_button = (ImageButton) findViewById(R.id.pause_button);
        ImageButton play_button = (ImageButton) findViewById(R.id.play_button);
        play_button.setVisibility(View.VISIBLE);
        pause_button.setVisibility(View.INVISIBLE);
        plane.getLocationInWindow(pause_coordinates);
        plane.animate().x(pause_coordinates[0]).y(pause_coordinates[1]).setDuration(1);
    }

    public void kill(int i){
        AnimatorSet goToHeaven = new AnimatorSet();
        goToHeaven.playTogether(
                ObjectAnimator.ofFloat(dwarf[i], View.Y, -100).setDuration(1000),
                ObjectAnimator.ofFloat(dwarf[i], View.ALPHA, 0).setDuration(500)
        );
        goToHeaven.start();
        dwarf_killed[i] = true;
    }

    public void play_clicked(View view) {
        game_was_paused = false;
        ImageView plane = (ImageView)findViewById(R.id.plane);
        ImageButton pause_button = (ImageButton) findViewById(R.id.pause_button);
        ImageButton play_button = (ImageButton) findViewById(R.id.play_button);
        play_button.setVisibility(View.INVISIBLE);
        pause_button.setVisibility(View.VISIBLE);
        plane.setVisibility(View.VISIBLE);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final  int width = display.getWidth();
        final int height = display.getHeight();
        plane.animate().xBy(width - 50 - pause_coordinates[0]).yBy(height / 4  - pause_coordinates[1]).setDuration(first_stage_time - ((level-1)*250) - pause_time/1000);
    }

    public void next_level_clicked(View view){

        ImageView plane = (ImageView)findViewById(R.id.plane);
        if((level + 3)* 250 < first_stage_time )
            level+=3;
        dropped_dwarfs = 0;
        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        final int width = display.getWidth();
        final int height = display.getHeight();
        plane.setVisibility(View.VISIBLE);
        for(int i = 0; i < dwarfs_awailable; i++)dwarf[i].setVisibility(View.INVISIBLE);
       // for(int i = 0; i < 5; i++)spawn_spike(field, high, i);
        plane.animate().xBy(width - 50).yBy(height / 4).setDuration(first_stage_time - ((level-1)*250));
        ImageButton next_level_button = (ImageButton)findViewById(R.id.level_button);
        next_level_button.setVisibility(View.INVISIBLE);
        Log.v("olo",Integer.toString(dropped_dwarfs));

    }

    int[] pause_coordinates = new int[2];

    byte level = 1;
    boolean game_was_started = false;
    boolean first_clicked = false;


    int wait_time = 150;
    // время после кидка одного до возможного кидка следующего (в мс)
    int current_time = 0;
    int first_time = 0;
    int pause_time = 0;
    int first_stage_time = 10000;

    int field;

    int step = 500;
    //шаг времени в мс

    int high = 5;
    // высота

    double time = Math.sqrt(2 * high / 9.8)*1000;
    //время падения

    byte dropped_dwarfs = 0;
    //сколько уже было скинуто гномов

    public int getMoney() {
        return money;
    }

    private AnimationDrawable mAnimationDrawable = null;
    private final static int DURATION = 250;
    int chosen;
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("money", money);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final BitmapDrawable frame1 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.plane1);
        final BitmapDrawable frame2 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.plane2);
        final BitmapDrawable frame3 = (BitmapDrawable) getResources().
                getDrawable(
                        R.drawable.plane3);
        final BitmapDrawable frame4 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.plane4);

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_game);

        final RelativeLayout game_layout = (RelativeLayout) findViewById(R.id.activity_game);

        final Handler handler1 = new Handler();
        //true_speed = speed;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = display.getWidth();
        final int height = display.getHeight();

        field = width;
        high = height / 10 * 9;
        //speed = Math.round(field / 5);

        Intent getData = getIntent();
        chosen = getData.getIntExtra("chosen", 1);
        View v = new ImageView(getBaseContext());



        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((width + height) / 27, (width + height) / 27);
        RelativeLayout.LayoutParams small_params = new RelativeLayout.LayoutParams((width + height) / 50, (width + height) / 50);
        switch (chosen) {
            case 1:
                for(int i = 0; i < dwarfs_awailable; i++) {
                    dwarf[i] = new ImageView(this);
                    dwarf[i].setImageResource(R.drawable.dwarf1);
                    game_layout.addView(dwarf[i],0, params);
                    dwarf[i].setVisibility(View.GONE);

                    lives[i] = new ImageView(this);
                    lives[i].setImageResource(R.drawable.dwarf1);
                    game_layout.addView(lives[i],0, small_params);
                }
                break;
            case 2: for(int i = 0; i < dwarfs_awailable; i++) {
                dwarf[i] = new ImageView(this);
                dwarf[i].setImageResource(R.drawable.dwarf2);
                game_layout.addView(dwarf[i],0, params);
                dwarf[i].setVisibility(View.GONE);

                lives[i] = new ImageView(this);
                lives[i].setImageResource(R.drawable.dwarf2);
                game_layout.addView(lives[i],0, small_params);
            }
                break;
            case 3: for(int i = 0; i < dwarfs_awailable; i++) {
                dwarf[i] = new ImageView(this);
                dwarf[i].setImageResource(R.drawable.dwarf3);
                game_layout.addView(dwarf[i],0, params);
                dwarf[i].setVisibility(View.GONE);

                lives[i] = new ImageView(this);
                lives[i].setImageResource(R.drawable.dwarf3);
                game_layout.addView(lives[i],0, small_params);

            }
                break;
            case 4: for(int i = 0; i < dwarfs_awailable; i++) {
                dwarf[i] = new ImageView(this);
                dwarf[i].setImageResource(R.drawable.dwarf4);
                game_layout.addView(dwarf[i],0, params);
                dwarf[i].setVisibility(View.GONE);

                lives[i] = new ImageView(this);
                lives[i].setImageResource(R.drawable.dwarf4);
                game_layout.addView(lives[i],0, small_params);
            }
                break;
            case 5: for(int i = 0; i < dwarfs_awailable; i++) {
                dwarf[i] = new ImageView(this);
                dwarf[i].setImageResource(R.drawable.dwarf5);
                game_layout.addView(dwarf[i],0, params);
                dwarf[i].setVisibility(View.GONE);

                lives[i] = new ImageView(this);
                lives[i].setImageResource(R.drawable.dwarf5);
                game_layout.addView(lives[i],0, small_params);
            }
                break;
        }
        spawn_lives(dwarfs_awailable);
        for(int i = 0; i < dwarfs_awailable; i++)lives[i].setVisibility(View.INVISIBLE);
        for (int i = 0; i < coins; i++) {
            coin[i] = new ImageView(this);
            coin[i].setImageResource(small_coin);
            game_layout.addView(coin[i], 0, params);
            spawn_coin(field, high, i, false);
            coin[i].setVisibility(View.INVISIBLE);
        }
        for(int i = 0; i < dwarfs_awailable; i++){
            spike[i] = new ImageView(this);
            spike[i].setImageResource(R.drawable.spikes);
            game_layout.addView(spike[i], 0, params);
            spawn_spike(field, high, i);
            spike[i].setVisibility(View.INVISIBLE);
        }
        game_layout.setBackgroundResource(R.drawable.ready_image);
        final TextView timer = (TextView) findViewById(R.id.timer);
        timer.setVisibility(View.INVISIBLE);
        ImageButton pause_button = (ImageButton) findViewById(R.id.pause_button);
        pause_button.setVisibility(View.INVISIBLE);


        handler1.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                if (game_was_started && !game_was_paused) {
                    final ImageView plane = (ImageView) findViewById(R.id.plane);
                    current_time += step;
                    int current[] = new int[2];
                    plane.getLocationInWindow(current);
                    if(!gameOver && current[0] >=field * 0.9){
                        gameOver = true;
                        plane.setVisibility(View.GONE);
                        ImageView explosion = new ImageView(game.this);
                        explosion.setImageResource(R.drawable.explosion);
                        RelativeLayout.LayoutParams exp = new RelativeLayout.LayoutParams((int)(field * 0.8), (int)(high * 0.9 ));
                        game_layout.addView(explosion, exp);
                        explosion.animate().x(field / 8).y(high / 5).setDuration(0);
                        Toast toast = Toast.makeText(getApplicationContext(), "Boom!", Toast.LENGTH_SHORT);
                        toast.show();
                        return;

                    }
                } else {
                    if(first_clicked){
                        final TextView timer = (TextView) findViewById(R.id.timer);
                        timer.setVisibility(View.VISIBLE);
                        if(first_time == 1000) {
                            timer.setText("2");
                        }
                        if(first_time == 2000) {
                            timer.setText("1");
                        }
                        if(first_time == 3000) {
                            timer.setVisibility(View.INVISIBLE);
                            ImageButton pause_button = (ImageButton) findViewById(R.id.pause_button);
                            pause_button.setVisibility(View.VISIBLE);
                            final ImageView plane = (ImageView) findViewById(R.id.plane);
                            game_was_started = true;

                            plane.setVisibility(View.VISIBLE);
                            plane.animate().xBy(width - 50).yBy(height / 4).setDuration(first_stage_time-((level-1)*100));
                            mAnimationDrawable = new AnimationDrawable();

                            mAnimationDrawable.setOneShot(false);
                            mAnimationDrawable.addFrame(frame1, DURATION);
                            mAnimationDrawable.addFrame(frame2, DURATION);
                            mAnimationDrawable.addFrame(frame3, DURATION);
                            mAnimationDrawable.addFrame(frame4, DURATION);

                            plane.setBackground(mAnimationDrawable);

                            mAnimationDrawable.setVisible(true, true);
                            mAnimationDrawable.start();
                            if(game_was_paused){
                                pause_time += step;

                            }}
                        first_time += step;}
                }

                handler1.postDelayed(this, step);
            }
        });
        coin_location = new int[2];

        final AnimatorSet coinCaught = new AnimatorSet();

        final Handler collecting = new Handler();
        final Handler spiking = new Handler();
        game_layout.setOnClickListener(new View.OnClickListener() {
                                           TextView timer = (TextView) findViewById(R.id.timer);
                                           final ImageView plane = (ImageView) findViewById(R.id.plane);

                                           @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                           @Override
                                           public void onClick(View v) {

                                               int location[] = new int[2];
                                               int money_location[] = new int[2];
                                               plane.getLocationInWindow(location);
                                               while(dropped_dwarfs < dwarfs_awailable && dwarf_killed[dropped_dwarfs])dropped_dwarfs++;
                                               if ((dropped_dwarfs < dwarfs_awailable)) {

                                                   if (!game_was_started ) {
                                                       first_clicked = true;
                                                       timer.setVisibility(View.VISIBLE);
                                                       game_layout.setBackgroundResource(R.drawable.background_game);
                                                       for(int i = 0; i < coins; i++)coin[i].setVisibility(View.VISIBLE);
                                                       for(int i = 0; i < 5; i++)spike[i].setVisibility(View.VISIBLE);
                                                       for(int i = 0; i < 5; i++)lives[i].setVisibility(View.VISIBLE);
                                                   }
                                                   else if(!gameOver){

                                                       if ((current_time >= wait_time) && !game_was_paused && dropped_dwarfs < dwarfs_awailable ) {
                                                           first_clicked = false;
                                                           final TranslateAnimation dwarf_vertical = new TranslateAnimation(location[0],(float) (location[0]), location[1], (int) (height * 0.7));

                                                           dwarf_vertical.setDuration((int) Math.round(time));
                                                           dwarf_vertical.setFillAfter(true);

                                                           dwarf[dropped_dwarfs].setVisibility(View.VISIBLE);
                                                           AnimatorSet dropDwarf = new AnimatorSet();
                                                           dropDwarf.playSequentially(
                                                                   ObjectAnimator.ofFloat(dwarf[dropped_dwarfs],View.X, (float)(location[0])).setDuration(0),
                                                                   ObjectAnimator.ofFloat(dwarf[dropped_dwarfs],View.Y, (float)(location[1])).setDuration(0),
                                                                   ObjectAnimator.ofFloat(dwarf[dropped_dwarfs],View.Y, (float)(high * 0.7)).setDuration(Math.round(time))

                                                           );
                                                           dropDwarf.start();
                                                           collecting.post(new Runnable() {

                                                               {
                                                                   System.out.println("new handler");
                                                               }
                                                               final int current_dwarf = dropped_dwarfs;
                                                               @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                                               @Override
                                                               public void run() {
                                                                   int dwarf_location[] = new int[2];
                                                                   dwarf[current_dwarf].getLocationInWindow(dwarf_location);
                                                                   //System.out.println("dwarf"+ i + " " + dwarf_location[0] + " " + dwarf_location[1]);
                                                                   for(int j = 0; j< coins; j++){

                                                                       coin[j].getLocationInWindow(coin_location);
                                                                       if((dwarf_location[0] - coin_location[0] <= 50 && dwarf_location[0] - coin_location[0]>= -50) &&(dwarf_location[1] - coin_location[1] <= 50 && dwarf_location[1] - coin_location[1]>= -50)){
                                                                           xCoin = r.nextFloat() * field;
                                                                           yCoin =r.nextFloat() * (float)0.7 * high;
                                                                           if(yCoin  + xCoin * (float)0.4 <= (float)0.7*high)yCoin+=xCoin * (float)0.4;
                                                                           number = j;
                                                                           spawn_coin(field, high, j, true);
                                                                           money++;

                                                                       }
                                                                   }
                                                                   for(int j = 0; j < dwarfs_awailable; j++){
                                                                       int spike_location[] = new int[2];
                                                                       spike[j].getLocationInWindow(spike_location);
                                                                       if(!dwarf_killed[current_dwarf] && (dwarf_location[0] - spike_location[0] <= 50 && dwarf_location[0] - spike_location[0]>= -50) &&(dwarf_location[1] - spike_location[1] <= 50 && dwarf_location[1] - spike_location[1]>= -50)){
                                                                           kill(current_dwarf);
                                                                           dwarf_killed[current_dwarf] = true;
                                                                           System.out.println("dwarf " + (current_dwarf + 1) + " (" + dwarf_location[0] + " " +  dwarf_location[1] + ") " + " killed by spike " + spike_location[0] + " " + spike_location[1]);
                                                                           dwarfs_alive = 0;
                                                                           for(int a = 0; a < 5; a++){
                                                                               if(!dwarf_killed[a])dwarfs_alive++;
                                                                           }
                                                                           spawn_lives(dwarfs_alive);
                                                                           if(dwarfs_alive == 0){
                                                                               gameOver = true;
                                                                               Toast toast = Toast.makeText(getApplicationContext(), "GAME OVER", Toast.LENGTH_SHORT);
                                                                               toast.show();
                                                                               ImageButton next_level_button = (ImageButton) findViewById(R.id.level_button);
                                                                               next_level_button.setVisibility(View.GONE);
                                                                               plane.setVisibility(View.GONE);
                                                                           }
                                                                           break;
                                                                       }
                                                                   }
                                                                   if(dwarf_location[1] >= (float)(high * 0.65) || dwarf_killed[current_dwarf]){System.out.println("Handler terminated");return;}
                                                                   dwarf_location = null;

                                                                   collecting.postDelayed(this,100);
                                                               }
                                                           });
                                                           if (dropped_dwarfs >= dwarfs_awailable - 1 && !gameOver) {
                                                               money += 5;
                                                               plane.animate().x(0).y(0).setDuration(0);
                                                               ImageButton next_level_button = (ImageButton)findViewById(R.id.level_button);
                                                               next_level_button.setVisibility(View.VISIBLE);
                                                               plane.setVisibility(View.INVISIBLE);
                                                           }
                                                           dropped_dwarfs++;
                                                           current_time = 0;

                                                       }
                                                   }
                                               }
                                               else{
                                                   boolean alive = false;
                                                   for(int i = 0; i < dwarfs_awailable; i++)if(!dwarf_killed[i])alive = true;
                                                   if(alive && !gameOver) {
                                                       money += 5;
                                                       plane.animate().x(0).y(0).setDuration(0);
                                                       ImageButton next_level_button = (ImageButton) findViewById(R.id.level_button);
                                                       next_level_button.setVisibility(View.VISIBLE);
                                                       plane.setVisibility(View.INVISIBLE);
                                                   }
                                                   else {

                                                   }
                                               }


                                           }
                                       }
        );


    }

}