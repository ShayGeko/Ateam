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

import java.util.concurrent.TimeUnit;

import static enon.hfad.com.ateam.MainActivity.money;

/**
 * Created by HOME on 06.03.2017.
 */


public class game extends AppCompatActivity {
    boolean game_was_paused = false;

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
        final int width = display.getWidth();
        final int height = display.getHeight();
        plane.animate().xBy(width - 50 - pause_coordinates[0]).yBy(height / 4  - pause_coordinates[1]).setDuration(first_stage_time - ((level-1)*250) - pause_time/1000);
    }

    public void next_level_clicked(View view){
        final ImageView dwarf1 = (ImageView) findViewById(R.id.dwarf_image1);
        final ImageView dwarf2 = (ImageView) findViewById(R.id.dwarf_image2);
        final ImageView dwarf3 = (ImageView) findViewById(R.id.dwarf_image3);
        final ImageView dwarf4 = (ImageView) findViewById(R.id.dwarf_image4);
        final ImageView dwarf5 = (ImageView) findViewById(R.id.dwarf_image5);
        ImageView plane = (ImageView)findViewById(R.id.plane);
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


        final ImageView dwarf1 = (ImageView) findViewById(R.id.dwarf_image1);
        final ImageView dwarf2 = (ImageView) findViewById(R.id.dwarf_image2);
        final ImageView dwarf3 = (ImageView) findViewById(R.id.dwarf_image3);
        final ImageView dwarf4 = (ImageView) findViewById(R.id.dwarf_image4);
        final ImageView dwarf5 = (ImageView) findViewById(R.id.dwarf_image5);

        final ImageView field1 = (ImageView)findViewById(R.id.imageView1);
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
        field12.getLayoutParams().width = field/12;



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


        game_layout.setOnClickListener(new View.OnClickListener() {
                                        TextView timer = (TextView) findViewById(R.id.timer);
                                           final ImageView plane = (ImageView) findViewById(R.id.plane);

                                           //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                           @Override
                                           public void onClick(View v) {
                                               int location[] = new int[2];
                                               plane.getLocationInWindow(location);
                                               //System.out.println("plane_x " + location[0] + " " + location[1]);
                                               if ((dropped_dwarfs < free_dwarfs)) {

                                                   if (!game_was_started) {
                                                       first_clicked = true;
                                                       ImageButton pause_button = (ImageButton) findViewById(R.id.pause_button);
                                                       timer.setVisibility(View.VISIBLE);
                                                       game_layout.setBackgroundResource(R.drawable.background_game2);
                                                   } else {
                                                       if ((current_time >= wait_time) && !game_was_paused ) {
                                                           first_clicked = false;

                                                           TranslateAnimation dwarf_vertical = new TranslateAnimation(location[0],(float) (location[0] + field/(21-level)), location[1], (int) (height * 0.7));
                                                           dwarf_vertical.setDuration((int) Math.round(time));
                                                           dwarf_vertical.setFillAfter(true);

                                                           //System.out.println(current_x);

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
                                                               //for (byte i = 0; i < apartment_ocupants.length; i++) { // деньги за уровень (выдаём за заполненные квартиры и за гномов, которые живут по одному (типа счастливые))
                                                               //    if (apartment_ocupants[i] != 0) {
                                                               //        money += money_for_one_apartment;
                                                               //    }
                                                               //    if (apartment_ocupants[i] == 1) {
                                                               //        money += money_for_one_dwarf;
                                                               //    }
                                                              // }
                                                               money += 5;

                                                               plane.animate().x(0).y(0).setDuration(0);
                                                               ImageButton next_level_button = (ImageButton)findViewById(R.id.level_button);
                                                               next_level_button.setVisibility(View.VISIBLE);
                                                               plane.setVisibility(View.INVISIBLE);

                                                               //m_seconds = 0;
                                                           }

                                                           dropped_dwarfs++;
                                                           current_time = 0;

                                                           //TODO продолжение игры (проблема с повторной анимацией самолета)



                                                           //for (byte i = 0; i < walls.length; i++) { // изменение apartment_ocupants
                                                           //    if (pos < walls[i] * field) {
                                                           //        apartment_ocupants[i]++;
                                                           //        break;
                                                           //    }
                                                           //    if (pos == walls[i] * field) {
                                                           //        current_apartment = "Wall. You missed, man!";
                                                           //        break;
                                                           //    }
                                                           //    if (pos > walls[walls.length - 1] * field) {
                                                           //        apartment_ocupants[walls.length]++;
                                                               //    break;
                                                             //  }
                                                           //}
                                                           ;

                                                       }
                                                   }
                                               }
                                               ;

                                               ;
                                           }//}
                                       }
        );


    }


}