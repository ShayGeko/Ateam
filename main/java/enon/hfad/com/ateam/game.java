package enon.hfad.com.ateam;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static enon.hfad.com.ateam.MainActivity.money;

import static enon.hfad.com.ateam.R.drawable.small_coin;

/**
 * Created by HOME on 06.03.2017.
 */


public class game extends AppCompatActivity {
    boolean game_was_paused = false;
    boolean money_was_spawned = false;
    final ImageView[] dwarf = new ImageView[5];

    public void pause_clicked(View view) {
        ImageView plane = (ImageView) findViewById(R.id.plane);
        game_was_paused = true;
        ImageButton pause_button = (ImageButton) findViewById(R.id.pause_button);
        ImageButton play_button = (ImageButton) findViewById(R.id.play_button);
        play_button.setVisibility(View.VISIBLE);
        pause_button.setVisibility(View.INVISIBLE);
        plane.getLocationInWindow(pause_coordinates);
        //plane.setVisibility(View.INVISIBLE);
        plane.animate().x(pause_coordinates[0]).y(pause_coordinates[1]).setDuration(1);
    }

    public void play_clicked(View view) {
        game_was_paused = false;
        ImageView plane = (ImageView) findViewById(R.id.plane);
        ImageButton pause_button = (ImageButton) findViewById(R.id.pause_button);
        ImageButton play_button = (ImageButton) findViewById(R.id.play_button);
        play_button.setVisibility(View.INVISIBLE);
        pause_button.setVisibility(View.VISIBLE);
        plane.setVisibility(View.VISIBLE);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = display.getWidth();
        final int height = display.getHeight();
        plane.animate().xBy(width - 50 - pause_coordinates[0]).yBy(height / 4 - pause_coordinates[1]).setDuration(first_stage_time - ((level - 1) * 250) - pause_time / 1000);
    }

    public void next_level_clicked(View view) {
        ImageView plane = (ImageView) findViewById(R.id.plane);
        level++;
        dropped_dwarfs = 0;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = display.getWidth();
        final int height = display.getHeight();
        plane.setVisibility(View.VISIBLE);
        plane.animate().xBy(width - 50).yBy(height / 4).setDuration(first_stage_time - ((level - 1) * 250));
        ImageButton next_level_button = (ImageButton) findViewById(R.id.level_button);
        next_level_button.setVisibility(View.INVISIBLE);
        Log.v("olo", Integer.toString(dropped_dwarfs));

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
    boolean game_was_started = false;
    boolean first_clicked = false;


    int wait_time = 250;
    // время после кидка одного до возможного кидка следующего (в мс)
    int current_time = 0;
    int first_time = 0;
    int pause_time = 0;
    int first_stage_time = 10000;
    byte money_for_one_chest = 5;

    int field;
    // если Гномик выходит за предел экрана (>field), то true_speed = -speed (УЕ отбавляется)

    int step = 500;
    //шаг времени в мс

    int high = 5;
    // высота

    double time = Math.sqrt(2 * high / 9.8) * 1000;
    //время падения


    double[] walls = {0.5};
    //"стены", разделяющие квартиры (например, {250,500}-три квартиры: 1-250, 250-500 , 500-1000 )

    int[] apartment_ocupants;
    //количество гномов в квартире

    byte free_dwarfs = 5;
    //количество "кидков" гномов-каскадеров-переселенцев

    byte dropped_dwarfs = 0;
    //сколько уже было скинуто гномов
    ImageView coin[];

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
        final BitmapDrawable frame3 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.plane3);
        final BitmapDrawable frame4 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.plane4);

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_game);

        final RelativeLayout game_layout = (RelativeLayout) findViewById(R.id.activity_game);
        game_layout.setBackgroundResource(R.drawable.ready_image);
        final TextView textview_money = (TextView) findViewById(R.id.textview_money);
        textview_money.setText(Integer.toString(money));
        final Handler handler1 = new Handler();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = display.getWidth();
        final int height = display.getHeight();

        field = width;
        high = height / 10 * 9;


        Intent getData = getIntent();
        chosen = getData.getIntExtra("chosen", 1);
        View v = new ImageView(getBaseContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(40, 40);


        switch (chosen) {
            case 1:
                for (int i = 0; i < 5; i++) {
                    dwarf[i] = new ImageView(this);
                    dwarf[i].setImageResource(R.drawable.dwarf1);
                    game_layout.addView(dwarf[i], 0, params);
                    dwarf[i].setVisibility(View.GONE);
                }
                break;
            case 2:
                for (int i = 0; i < 5; i++) {
                    dwarf[i] = new ImageView(this);
                    dwarf[i].setImageResource(R.drawable.dwarf2);
                    game_layout.addView(dwarf[i], 0, params);
                    dwarf[i].setVisibility(View.GONE);
                }
                break;
            case 3:
                for (int i = 0; i < 5; i++) {
                    dwarf[i] = new ImageView(this);
                    dwarf[i].setImageResource(R.drawable.dwarf3);
                    game_layout.addView(dwarf[i], 0, params);
                    dwarf[i].setVisibility(View.GONE);
                }
                break;
            case 4:
                for (int i = 0; i < 5; i++) {
                    dwarf[i] = new ImageView(this);
                    dwarf[i].setImageResource(R.drawable.dwarf4);
                    game_layout.addView(dwarf[i], 0, params);
                    dwarf[i].setVisibility(View.GONE);
                }
                break;
            case 5:
                for (int i = 0; i < 5; i++) {
                    dwarf[i] = new ImageView(this);
                    dwarf[i].setImageResource(R.drawable.dwarf5);
                    game_layout.addView(dwarf[i], 0, params);
                    dwarf[i].setVisibility(View.GONE);
                }
                break;
        }
        coin = new ImageView[3];
        for (int i = 0; i < 3; i++) {
            coin[i] = new ImageView(this);
            coin[i].setImageResource(small_coin);
            game_layout.addView(coin[i], 0, params);
        }


        apartment_ocupants = new int[walls.length + 1];
        final TextView timer = (TextView) findViewById(R.id.timer);
        timer.setVisibility(View.INVISIBLE);
        ImageButton pause_button = (ImageButton) findViewById(R.id.pause_button);
        pause_button.setVisibility(View.INVISIBLE);
        // массив типа [4,1] - 4 гнома в первой квартире и 1 во второй

        //TODO нарисовать стены


        handler1.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                if (game_was_started && !game_was_paused) {
                    final ImageView plane = (ImageView) findViewById(R.id.plane);
                    current_time += step;
                    int location[] = new int[2];
                    plane.getLocationInWindow(location);
                } else {
                    if (first_clicked) {
                        final TextView timer = (TextView) findViewById(R.id.timer);
                        timer.setVisibility(View.VISIBLE);
                        if (first_time == 1000) {
                            timer.setText("2");
                        }
                        if (first_time == 2000) {
                            timer.setText("1");
                        }
                        if (first_time == 3000) {
                            timer.setVisibility(View.INVISIBLE);
                            ImageButton pause_button = (ImageButton) findViewById(R.id.pause_button);
                            pause_button.setVisibility(View.VISIBLE);
                            final ImageView plane = (ImageView) findViewById(R.id.plane);
                            game_was_started = true;
                            plane.setVisibility(View.VISIBLE);
                            plane.animate().xBy(width - 50).yBy(height / 4).setDuration(first_stage_time - ((level - 1) * 100));
                            mAnimationDrawable = new AnimationDrawable();

                            mAnimationDrawable.setOneShot(false);
                            mAnimationDrawable.addFrame(frame1, DURATION);
                            mAnimationDrawable.addFrame(frame2, DURATION);
                            mAnimationDrawable.addFrame(frame3, DURATION);
                            mAnimationDrawable.addFrame(frame4, DURATION);

                            plane.setBackground(mAnimationDrawable);

                            mAnimationDrawable.setVisible(true, true);
                            mAnimationDrawable.start();
                            if (game_was_paused) {
                                pause_time += step;

                            }
                        }
                        first_time += step;
                    }

                }


                List<int[]> locationMoney = new ArrayList<int[]>(5);
                for (int i = 0; i < 3; i++) {
                    int arr[] = new int[2];
                    coin[i].getLocationInWindow(arr);
                    locationMoney.add(arr);
                }

                List<int[]> locationDwarf = new ArrayList<int[]>(5);
                for (int i = 0; i < 5; i++) {
                    int arr[] = new int[2];
                    dwarf[i].getLocationInWindow(arr);
                    locationDwarf.add(arr);
                }

               /* int location1dwarf[] = new int[2];
                dwarf1.getLocationInWindow(location1dwarf);
                int location2dwarf[] = new int[2];
                dwarf2.getLocationInWindow(location2dwarf);
                int location3dwarf[] = new int[2];
                dwarf3.getLocationInWindow(location3dwarf);
                int location4dwarf[] = new int[2];
                dwarf4.getLocationInWindow(location4dwarf);
                int location5dwarf[] = new int[2];
                dwarf5.getLocationInWindow(location5dwarf);*/


                if (money_was_spawned) {
                    for (int i = 0; i < 5; i++)
                        for (int j = 0; j < 3; j++)
                            if ((locationDwarf.get(i)[0] > locationMoney.get(j)[0] - 30 && locationDwarf.get(i)[0] < locationMoney.get(j)[0] + 30) && locationDwarf.get(i)[1] > locationMoney.get(j)[1] - 20 && locationDwarf.get(i)[1] < locationMoney.get(j)[1] + 20) {
                                coin[j].setVisibility(View.INVISIBLE);
                                money += 5;

                            }
                }


                handler1.postDelayed(this, step);
            }
        });


        game_layout.setOnClickListener(new View.OnClickListener() {
                                           TextView timer = (TextView) findViewById(R.id.timer);
                                           final ImageView plane = (ImageView) findViewById(R.id.plane);

                                           @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                           @Override
                                           public void onClick(View v) {
                                               if ((dropped_dwarfs < free_dwarfs)) {

                                                   if (!game_was_started) {
                                                       first_clicked = true;
                                                       timer.setVisibility(View.VISIBLE);
                                                       game_layout.setBackgroundResource(R.drawable.background_game2);
                                                       for (int i = 0; i < 3; i++)
                                                           coin[i].animate().x((float) Math.random() * field).y((float) Math.random() * high).setDuration(0);
                                                       List<int[]> locationMoney = new ArrayList<int[]>(5);
                                                       for (int i = 0; i < 3; i++) {
                                                           int arr[] = new int[2];
                                                           coin[i].getLocationInWindow(arr);
                                                           locationMoney.add(arr);
                                                       }


                                                       for (int i = 0; i < 3; i++)
                                                           Log.v("location", (i + 1) + Arrays.toString(locationMoney.get(i)));
                                                       money_was_spawned = true;
                                                   } else {
                                                       if ((current_time >= wait_time) && !game_was_paused) {
                                                           first_clicked = false;

                                                           int location[] = new int[2];
                                                           plane.getLocationInWindow(location);

                                                           Runnable endAction = new Runnable() {
                                                               public void run() {
                                                                   int location[] = new int[2];
                                                                   plane.getLocationInWindow(location);
                                                                   //dwarf[dropped_dwarfs].setVisibility(View.VISIBLE);
                                                                   dwarf[dropped_dwarfs].animate().x(location[0]).yBy((int) (height * 0.7) - location[1]).setDuration((int) Math.round(time));
                                                               }
                                                           };
                                                           dwarf[dropped_dwarfs].setVisibility(View.VISIBLE);
                                                           dwarf[dropped_dwarfs].animate().x(location[0]).y(location[1]).setDuration(0).withEndAction(endAction);
                                                           dwarf[dropped_dwarfs].setVisibility(View.VISIBLE);
                                                           if (dropped_dwarfs == 4) {
                                                               plane.animate().x(0).y(0).setDuration(0);
                                                               ImageButton next_level_button = (ImageButton) findViewById(R.id.level_button);
                                                               next_level_button.setVisibility(View.VISIBLE);
                                                               plane.setVisibility(View.INVISIBLE);

                                                           }

                                                           dropped_dwarfs++;
                                                           if (dropped_dwarfs == 5) dropped_dwarfs = 0;
                                                           current_time = 0;


                                                       }
                                                   }
                                               }


                                           }
                                       }
        );


    }


}

