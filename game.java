package enon.hfad.com.ateam;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;
import static enon.hfad.com.ateam.MainActivity.money;

/**
 * Created by HOME on 06.03.2017.
 */



public class game extends AppCompatActivity {
    double current_x = 0;
    double current_y = 0;
    double speed_x = 0.91/5000;
    double speed_y = 0.1/5000;
    int red = 160;
    int green = 219;
    int blue = 239;

    int money_for_one_apartment = 2;
    int money_for_one_dwarf = 1;
    int m_seconds = 0;
    boolean dwarf_was_dropped = false;
    int true_speed;
    String current_apartment;
    boolean game_was_played = false;
    boolean game_was_started = false;

    int wait_time = 100;
    // время после кидка одного до возможного кидка следующего (в мс)
    int current_time = 0;
    int first_time = 0;


    // позиция Гнома (от 0 до field).
    int pos = 0;

    // Скорость УЕ/сек
    int speed;

    // УЕ (длина экрана)
    int field;
    // если Гномик выходит за предел экрана (>field), то true_speed = -speed (УЕ отбавляется)

    int step = 500;
    //шаг времени в мс

    int high = 5;
    // высота

    double time = Math.sqrt(2*high/9.8);
    //время падения


    double[] walls = {0.5};
    //"стены", разделяющие квартиры (например, {250,500}-три квартиры: 1-250, 250-500 , 500-1000 )

    int[] apartment_ocupants;
    //количество гномов в квартире

    byte free_dwarfs = 5;
    //количество "кидков" гномов-каскадеров-переселенцев

    byte dropped_dwarfs = 0;
    //сколько уже было скинуто гномов

    public int getMoney() {
        return money;
    }

    private AnimationDrawable mAnimationDrawable = null;
    private final static int DURATION = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final BitmapDrawable frame1 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.plane1);
        final BitmapDrawable frame2 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.plane2);
        final BitmapDrawable frame3 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.plane3);
        final BitmapDrawable frame4 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.plane4);

        //final ImageView ready_image = (ImageView) findViewById(R.id.ready_image);
        final ImageView wall1 = (ImageView)findViewById(R.id.wall1);
        final ImageView wall2 = (ImageView)findViewById(R.id.wall2);
        final ImageView wall3 = (ImageView)findViewById(R.id.wall3);
        final ImageView wall4 = (ImageView)findViewById(R.id.wall4);



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        final RelativeLayout game_layout =(RelativeLayout)findViewById(R.id.activity_game);
        //game_layout.setBackgroundResource(R.drawable.background_game2);
        game_layout.setBackgroundResource(R.drawable.ready_image);
        final TextView textview_money = (TextView)findViewById(R.id.textview_money);
        textview_money.setText(Integer.toString(money));
        final Handler handler1 = new Handler();
        true_speed=speed;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;
        final int height = size.y;

        field = width;
        high = height/10*9;
        speed=Math.round(field/5);


        apartment_ocupants = new int[walls.length+1];
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
        //TODO решить ошибку с строке 108 (java.lang.nullpointerexception)
        //TODO реализовать сохранение денег, нарисовать стены

        handler1.post(new Runnable() {
            @Override
            public void run() {
                if(dropped_dwarfs < free_dwarfs) {
                    if (game_was_started && !game_was_played) {
                        game_layout.setBackgroundColor(Color.rgb(red, green, blue));
                        red+=(239-160)*step/5000;
                        green-=(219-0)*step/5000;
                        blue-=(239-0)*step/5000;
                        current_x += speed_x*step/5000;
                        current_y -= speed_y *step/5000;
                        m_seconds += step;
                        current_time += step;
                        if(pos == field || dropped_dwarfs == free_dwarfs){
                        game_was_played = true;
                        game_layout.setBackgroundColor(Color.rgb(10, 0, 0));
                            final ImageView plane = (ImageView) findViewById(R.id.plane);
                            plane.setVisibility(View.INVISIBLE);

                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Game over", Toast.LENGTH_SHORT);
                            toast.show();
                        };
                    }
                    handler1.postDelayed(this, step);
                }
            }
        });


        game_layout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if((dropped_dwarfs<free_dwarfs)) {

                    if (!game_was_started) {
                        game_layout.setBackgroundResource(R.drawable.background_game2);
                        //TODO создать обратный отсчет 3...2...1.... Игра началась!
                        //TODO почему не работают Toast?
                        final ImageView plane = (ImageView) findViewById(R.id.plane);
                        //plane.setImageResource(R.drawable.battery1);
                        //TranslateAnimation plane_animation = new TranslateAnimation(0, field, high, high);
                        TranslateAnimation plane_animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -0.13f, Animation.RELATIVE_TO_PARENT,0.78f, Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0.1f);
                        plane_animation.setDuration(5000);
                        plane_animation.setFillAfter(true);
                        plane.startAnimation(plane_animation);

                        mAnimationDrawable = new AnimationDrawable();

                        mAnimationDrawable.setOneShot(false);
                        mAnimationDrawable.addFrame(frame1, DURATION);
                        mAnimationDrawable.addFrame(frame2, DURATION);
                        mAnimationDrawable.addFrame(frame3, DURATION);
                        mAnimationDrawable.addFrame(frame4, DURATION);

                        plane.setBackground(mAnimationDrawable);

                            mAnimationDrawable.setVisible(true, true);
                            mAnimationDrawable.start();




                        game_was_started = true;
                    } else {
                        if (current_time > wait_time || dropped_dwarfs == 0) {
                            final ImageView plane = (ImageView) findViewById(R.id.plane);
                            final ImageView dwarf1 = (ImageView) findViewById(R.id.dwarf_image1);
                            final ImageView dwarf2 = (ImageView) findViewById(R.id.dwarf_image2);
                            final ImageView dwarf3 = (ImageView) findViewById(R.id.dwarf_image3);
                            final ImageView dwarf4 = (ImageView) findViewById(R.id.dwarf_image4);
                            final ImageView dwarf5 = (ImageView) findViewById(R.id.dwarf_image5);
                            TranslateAnimation dwarf_vertical = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,(float)current_x,Animation.RELATIVE_TO_PARENT, (float)current_x,Animation.RELATIVE_TO_PARENT, (float)current_y,Animation.RELATIVE_TO_PARENT, 0.65f);
                            dwarf_vertical.setDuration((int) Math.round(time * 1000));
                            dwarf_vertical.setFillAfter(true);
                            if (dropped_dwarfs == 0) {
                                dwarf1.setVisibility(View.VISIBLE);
                                dwarf1.startAnimation(dwarf_vertical);
                                //plane.setImageResource(R.drawable.battery2);
                            }
                            if (dropped_dwarfs == 1) {
                                dwarf2.setVisibility(View.VISIBLE);
                                dwarf2.startAnimation(dwarf_vertical);
                                //plane.setImageResource(R.drawable.battery3);
                            }
                            if (dropped_dwarfs == 2) {
                                dwarf3.setVisibility(View.VISIBLE);
                                dwarf3.startAnimation(dwarf_vertical);
                                //plane.setImageResource(R.drawable.battery4);
                            }
                            if (dropped_dwarfs == 3) {
                                dwarf4.setVisibility(View.VISIBLE);
                                dwarf4.startAnimation(dwarf_vertical);
                                //plane.setImageResource(R.drawable.battery5);
                            }
                            if (dropped_dwarfs == 4) {
                                dwarf5.setVisibility(View.VISIBLE);
                                dwarf5.startAnimation(dwarf_vertical);
                                //plane.setImageResource(R.drawable.battery6);
                            }


                            dropped_dwarfs++;
                            current_time = 0;


                            for (byte i = 0; i < walls.length; i++) { // изменение apartment_ocupants
                                if (pos < walls[i] * field) {
                                    apartment_ocupants[i]++;
                                    break;
                                }
                                if (pos == walls[i] * field) {
                                    current_apartment = "Wall. You missed, man!";
                                    break;
                                }
                                if (pos > walls[walls.length - 1] * field) {
                                    apartment_ocupants[walls.length]++;
                                    break;
                                }
                            }
                            ;

                        }
                    }
                };
                    if(dropped_dwarfs == free_dwarfs && !game_was_played){
                        game_was_played = true;
                        for(byte i = 0; i < apartment_ocupants.length; i++){ // деньги за уровень (выдаём за заполненные квартиры и за гномов, которые живут по одному (типа счастливые))
                            if(apartment_ocupants[i] != 0){money += money_for_one_apartment;}
                            if(apartment_ocupants[i] == 1){money += money_for_one_dwarf;}
                        }
                        textview_money.setText(Integer.toString(money));
                    };
            }//}
        }
        );





    }


}



