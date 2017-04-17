package enon.hfad.com.ateam;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;
import static enon.hfad.com.ateam.MainActivity.money;

/**
 * Created by HOME on 06.03.2017.
 */



public class game extends AppCompatActivity {
    int money_for_one_apartment = 2;
    int money_for_one_dwarf = 1;
    int m_seconds = 0;
    boolean dwarf_was_dropped = false;
    int true_speed;
    String current_apartment;
    boolean game_was_played = false;


    // позиция Гнома (от 0 до field).
    int pos = 0;

    // Скорость УЕ/сек
    int speed;

    // УЕ (длина экрана)
    int field;
    // если Гномик выходит за предел экрана (>field), то true_speed = -speed (УЕ отбавляется)

    int step = 100;
    //шаг времени в мс

    int high = 5;
    // высота

    double time = Math.sqrt(2*high/9.8);
    //время падения

    double now_time;
    //время до возможного след. кидка гнома

    int[] walls = {500};
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;
        final int height = size.y;

        field = width*9/10;
        high = height*6/10;
        speed=Math.round(field/4);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        RelativeLayout game_layout =(RelativeLayout)findViewById(R.id.activity_game);
        game_layout.setBackgroundResource(R.drawable.background_game);
        final TextView textview_money = (TextView)findViewById(R.id.textview_money);
        final TextView pos_view = (TextView)findViewById(R.id.position_text);
        final TextView sec_view = (TextView)findViewById(R.id.seconds_text);
        textview_money.setText(Integer.toString(money));
        final Handler handler1 = new Handler();
        final Handler handler2 = new Handler();
        true_speed=speed;
        now_time = 0;

        apartment_ocupants = new int[walls.length+1];
        // массив типа [4,1] - 4 гнома в первой квартире и 1 во второй



        //TODO Сделать анимацию ПЯТИ гномов, реализовать сохранение денег, сменить картинки гномов на клешни, обнулять pos и m_seconds после броска

        handler1.post(new Runnable() {
            @Override
            public void run() {
                if(dropped_dwarfs < free_dwarfs) {
                    pos=speed*m_seconds/1000;//true_speed*m_seconds/1000;
                    pos_view.setText(Integer.toString(pos));
                    sec_view.setText(Integer.toString(m_seconds));
                    // if(pos >= field){true_speed= -speed;}//ограничение выхода за поле(вправо)
                    // if(pos <= 0){true_speed= speed;}//ограничение выхода за поле(влево)
                    m_seconds += step; // step - точность в мс хода времени
                    now_time+=step;

                    handler1.postDelayed(this, step);

                }
            }
        });

        final ImageView dwarf = (ImageView) findViewById(R.id.dwarf_image);
        TranslateAnimation dwarf_horizontal = new TranslateAnimation(0,field,40,40);
        dwarf_horizontal.setDuration(4000);//field/speed*1000);
        dwarf_horizontal.setFillAfter(true);
        dwarf.startAnimation(dwarf_horizontal);



        game_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(dropped_dwarfs == 0 ){now_time = time*1000+1;}
                if((dropped_dwarfs<free_dwarfs)){ //&& (now_time/1000>time)){

                    dropped_dwarfs++;

                    dwarf.setImageResource(R.drawable.dwarf_dead);
                    TranslateAnimation dwarf_vertical = new TranslateAnimation(pos+50,pos+50,40,height/2);
                    dwarf_vertical.setDuration((int)Math.round(time*1000));
                    dwarf_vertical.setFillAfter(true);
                    ImageView dwarf2 = (ImageView) findViewById(R.id.dwarf_image2);
                    dwarf.startAnimation(dwarf_vertical);

                    for(byte i = 0; i < walls.length; i++){ // изменение apartment_ocupants
                        if(pos < walls[i]){
                            apartment_ocupants[i]++;
                            break;
                        }
                        if(pos == walls[i]){
                            current_apartment="Wall. You missed, man!";
                            break;
                        }
                        if(pos > walls[walls.length-1]){
                            apartment_ocupants[walls.length]++;
                            break;
                        }
                    };
                    pos=0;
                    m_seconds=0;
                } else {
                    if(!game_was_played){
                        game_was_played = true;
                        for(byte i = 0; i < apartment_ocupants.length; i++){ // деньги за уровень (выдаём за заполненные квартиры и за гномов, которые живут по одному (типа счастливые))
                            if(apartment_ocupants[i] != 0){money += money_for_one_apartment;}
                            if(apartment_ocupants[i] == 1){money += money_for_one_dwarf;}
                        }
                        textview_money.setText(Integer.toString(money));
                    }};
            }
        }
        );





    }
    @Override
    public void onBackPressed() { // щелчок кнопки "назад". возвращаемся в главное меню
        Intent go_to_main_intent = new Intent(this, MainActivity.class);
        startActivity(go_to_main_intent);

    }


}



