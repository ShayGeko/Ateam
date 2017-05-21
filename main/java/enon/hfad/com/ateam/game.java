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
import static enon.hfad.com.ateam.R.drawable.small_coin;

/**
 * Created by HOME on 06.03.2017.
 */


public class game extends AppCompatActivity {
    int fall = 4000;
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
                    ObjectAnimator.ofFloat(coin[i], View.X, xCoin).setDuration(200),
                    ObjectAnimator.ofFloat(coin[i], View.Y, yCoin).setDuration(200),
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
        //plane.setVisibility(View.INVISIBLE);
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
        if((level + 1)* 250 < first_stage_time )
            level++;
        dropped_dwarfs = 0;
        //dwarf1.setVisibility(View.INVISIBLE);
        //dwarf2.setVisibility(View.INVISIBLE);
        //dwarf3.setVisibility(View.INVISIBLE);
        //dwarf4.setVisibility(View.INVISIBLE);
        //dwarf5.setVisibility(View.INVISIBLE);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = display.getWidth();
        final int height = display.getHeight();
        plane.setVisibility(View.VISIBLE);
        for(int i = 0; i < 5; i++)dwarf[i].setVisibility(View.INVISIBLE);
        plane.animate().xBy(width - 50).yBy(height / 4).setDuration(first_stage_time - ((level-1)*250));
        ImageButton next_level_button = (ImageButton)findViewById(R.id.level_button);
        next_level_button.setVisibility(View.INVISIBLE);
        Log.v("olo",Integer.toString(dropped_dwarfs));

    }

    //double current_x = 0;
    //double current_y = 0;
    //double speed_x = 0.91 / 5000;
    //double speed_y = 0.3 / 5000;
    //int red = 160;
    // green = 219;
    //int blue = 239;

    int[] pause_coordinates = new int[2];

    byte level = 1;
    byte nums_of_lake_tiles;
    byte nums_of_empty_tiles;
    byte nums_of_treasure_tiles;
    byte nums_of_dead_tiles;

    int money_for_one_apartment = 2;
    int money_for_one_dwarf = 1;
    //int m_seconds = 0;
    //int true_speed;
    String current_apartment;
    //boolean game_was_played = false;
    boolean game_was_started = false;
    boolean first_clicked = false;


    int wait_time = 0;
    // время после кидка одного до возможного кидка следующего (в мс)
    int current_time = 0;
    int first_time = 0;
    int pause_time = 0;
    int first_stage_time = 10000;

    // позиция Гнома (от 0 до field).
    int pos = 0;

    // Скорость УЕ/сек
    //int speed;

    // УЕ (длина экрана)
    int field;
    // если Гномик выходит за предел экрана (>field), то true_speed = -speed (УЕ отбавляется)

    int step = 500;
    //шаг времени в мс

    int high = 5;
    // высота

    double time = Math.sqrt(2 * high / 9.8)*1000;
    //время падения


    double[] walls = {0.5};
    //"стены", разделяющие квартиры (например, {250,500}-три квартиры: 1-250, 250-500 , 500-1000 )

    int[] apartment_ocupants;
    //количество гномов в квартире

    int free_dwarfs = 5;
    //количество "кидков" гномов-каскадеров-переселенцев

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
        //game_layout.setBackgroundResource(R.drawable.background_game2);
        game_layout.setBackgroundResource(R.drawable.ready_image);
        final TextView textview_money = (TextView) findViewById(R.id.textview_money);
        textview_money.setText(Integer.toString(money));
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
        for (int i = 0; i < coins; i++) {
            coin[i] = new ImageView(this);
            coin[i].setImageResource(small_coin);
            game_layout.addView(coin[i], 0, params);
            spawn_coin(field, high, i, false);
        }
        for(int i = 0; i < dwarfs_awailable; i++){
            spike[i] = new ImageView(this);
            spike[i].setImageResource(R.drawable.spikes);
            game_layout.addView(spike[i], 0, params);
            spawn_spike(field, high + (width + height) / 27, i);
        }

       /* final ImageView field1 = (ImageView)findViewById(R.id.imageView1);
        final ImageView field2 = (ImageView)findViewById(R.id.imageView2);
        final ImageView field3 = (ImageView)findViewById(R.id.imageView3);
        final ImageView field4 = (ImageView)findViewById(R.id.imageView4);
        final ImageView field5 = (ImageView)findViewById(R.id.imageView5);
        final ImageView field6 = (ImageView)findViewById(R.id.imageView6);
        final ImageView field7 = (ImageView)findViewById(R.id.imageView7);
        final ImageView field8 = (ImageView)findViewById(R.id.imageView8);
        final ImageView field9 = (ImageView)findViewById(R.id.imageView9);
        final ImageView field10 = (ImageView)findViewById(R.id.imageView10);
        final ImageView field11 = (ImageView)findViewById(R.id.imageView11);
        final ImageView field12 = (ImageView)findViewById(R.id.imageView12);
        field1.setVisibility(View.VISIBLE);
        field2.setVisibility(View.VISIBLE);
        field3.setVisibility(View.VISIBLE);
        field4.setVisibility(View.VISIBLE);
        field5.setVisibility(View.VISIBLE);
        field6.setVisibility(View.VISIBLE);
        field7.setVisibility(View.VISIBLE);
        field8.setVisibility(View.VISIBLE);
        field8.setVisibility(View.VISIBLE);
        field9.setVisibility(View.VISIBLE);
        field10.setVisibility(View.VISIBLE);
        field11.setVisibility(View.VISIBLE);
        field12.setVisibility(View.VISIBLE);
        field1.setImageResource(R.drawable.grass);
        field2.setImageResource(R.drawable.grass);
        field3.setImageResource(R.drawable.grass);
        field4.setImageResource(R.drawable.grass);
        field5.setImageResource(R.drawable.grass);
        field6.setImageResource(R.drawable.grass);
        field7.setImageResource(R.drawable.grass);
        field8.setImageResource(R.drawable.grass);
        //field1.getLayoutParams().height -= 90;
        field1.getLayoutParams().width = field/12;
        //field1.getLayoutParams().height = high;
        field2.getLayoutParams().width = field/12;
        field3.getLayoutParams().width = field/12;
        field4.getLayoutParams().width = field/12;
        field5.getLayoutParams().width = field/12;
        field6.getLayoutParams().width = field/12;
        field7.getLayoutParams().width = field/12;
        field8.getLayoutParams().width = field/12;
        field9.getLayoutParams().width = field/12;
        field10.getLayoutParams().width = field/12;
        field11.getLayoutParams().width = field/12;
        field12.getLayoutParams().width = field/12;*/



        //field1.getLayoutParams().width += 400;




        apartment_ocupants = new int[walls.length + 1];
        final TextView timer = (TextView) findViewById(R.id.timer);
        timer.setVisibility(View.INVISIBLE);
        ImageButton pause_button = (ImageButton) findViewById(R.id.pause_button);
        pause_button.setVisibility(View.INVISIBLE);
        // массив типа [4,1] - 4 гнома в первой квартире и 1 во второй

       /* for(byte i = 0; i < walls.length; i++){
            switch (i){
                case 0: {wall1.setX(walls[i]*field);
                    wall1.setY(260);
                    wall1.setVisibility(View.VISIBLE);
                    break;
                }
                case 1: {wall2.setX(walls[i]);
                    wall2.setY(260);
                    wall2.setVisibility(View.VISIBLE);
                    break;
                }
                case 2: {wall3.setX(walls[i]);
                    wall3.setY(260);
                    wall3.setVisibility(View.VISIBLE);
                    break;
                }
                case 3: {wall4.setX(walls[i]);
                    wall4.setY(260);
                    wall4.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
        */

        //TODO нарисовать стены



        handler1.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                if (game_was_started && !game_was_paused) {
                    final ImageView plane = (ImageView) findViewById(R.id.plane);
                    //game_layout.setBackgroundColor(Color.rgb(red, green, blue));
                    //red += (239 - 160) * step / 5000;
                    //green -= (219 - 0) * step / 5000;
                    //blue -= (239 - 0) * step / 5000;
                    //current_x = speed_x * m_seconds;
                    //current_y = speed_y * m_seconds;
                    //m_seconds += step;
                    current_time += step;
                    int current[] = new int[2];
                    plane.getLocationInWindow(current);
                    //TODO  !
                    //if (current[1] >= field/4 ){//|| dropped_dwarfs == 5) {
                    //    game_was_played = true;
                    //    game_layout.setBackgroundColor(Color.rgb(10, 0, 0));
                    //    plane.setVisibility(View.INVISIBLE);

                    //Toast toast = Toast.makeText(getApplicationContext(),
                    //        "Game over", Toast.LENGTH_SHORT);
                    //toast.show();
                    //}
                    ;
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

                            //plane.setImageResource(R.drawable.battery1);
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
                //System.out.println("handler " + current_x);
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

                                           //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                           @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                           @Override
                                           public void onClick(View v) {
                                               int location[] = new int[2];
                                               int money_location[] = new int[2];
                                               plane.getLocationInWindow(location);
                                               //System.out.println("plane_x " + location[0] + " " + location[1]);
                                               while(dropped_dwarfs < dwarfs_awailable && dwarf_killed[dropped_dwarfs])dropped_dwarfs++;
                                               if ((dropped_dwarfs < dwarfs_awailable)) {

                                                   if (!game_was_started) {
                                                       collecting.post(new Runnable() {
                                                           @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                                           @Override
                                                           public void run() {

                                                               for(int i = 0; i < dwarfs_awailable; i++){
                                                                   int dwarf_location[] = new int[2];
                                                                   dwarf[i].getLocationInWindow(dwarf_location);
                                                                   //System.out.println("dwarf"+ i + " " + dwarf_location[0] + " " + dwarf_location[1]);
                                                                   for(int j = 0; j< coins; j++){

                                                                       coin[j].getLocationInWindow(coin_location);
                                                                       //System.out.println("coin"+ j + " " + coin_location[0] + " " + coin_location[1]);
                                                                       if((dwarf_location[0] - coin_location[0] <= 50 && dwarf_location[0] - coin_location[0]>= -50) &&(dwarf_location[1] - coin_location[1] <= 50 && dwarf_location[1] - coin_location[1]>= -50)){
                                                                           // System.out.println("coin" + j + " caught");
                                                                           xCoin = r.nextFloat() * field;
                                                                           yCoin =r.nextFloat() * (float)0.7 * high;
                                                                           if(yCoin  + xCoin * (float)0.4 <= (float)0.7*high)yCoin+=xCoin * (float)0.4;
                                                                           number = j;
                                                                           spawn_coin(field, high, j, true);

                                                                       }
                                                                   }

                                                                   for(int j = 0; j < dwarfs_awailable; j++){
                                                                       int spike_location[] = new int[2];
                                                                       spike[j].getLocationInWindow(spike_location);
                                                                       if((dwarf_location[0] - spike_location[0] <= 50 && dwarf_location[0] - spike_location[0]>= -50) &&(dwarf_location[1] - spike_location[1] <= 50 && dwarf_location[1] - spike_location[1]>= -50)){
                                                                           kill(i);
                                                                           dwarfs_alive--;
                                                                           dwarf_killed[i] = true;
                                                                           spawn_lives(dwarfs_alive);
                                                                           break;
                                                                       }
                                                                   }
                                                                   dwarf_location = null;
                                                               }
                                                               collecting.postDelayed(this,150);
                                                           }
                                                       });
                                                       first_clicked = true;
                                                       ImageButton pause_button = (ImageButton) findViewById(R.id.pause_button);
                                                       timer.setVisibility(View.VISIBLE);
                                                       game_layout.setBackgroundResource(R.drawable.background_game2);
                                                   }
                                                   else {
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
                                                           if (dropped_dwarfs >= dwarfs_awailable - 1 && dwarfs_awailable > 0) {
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
                                                   if(alive){
                                                       money += 5;
                                                       plane.animate().x(0).y(0).setDuration(0);
                                                       ImageButton next_level_button = (ImageButton)findViewById(R.id.level_button);
                                                       next_level_button.setVisibility(View.VISIBLE);
                                                       plane.setVisibility(View.INVISIBLE);
                                                   }
                                                   else {
                                                       plane.setVisibility(View.INVISIBLE);
                                                       Toast toast = Toast.makeText(getApplicationContext(), "GAME OVER", Toast.LENGTH_SHORT);
                                                       toast.show();
                                                   }
                                               }

                                               ;
                                           }//}
                                       }
        );


    }

}