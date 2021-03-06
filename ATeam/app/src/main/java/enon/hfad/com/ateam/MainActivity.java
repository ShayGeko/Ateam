package enon.hfad.com.ateam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static enon.hfad.com.ateam.R.id.activity_main;
import static enon.hfad.com.ateam.R.id.player_score;

public class MainActivity extends AppCompatActivity {
    private TextView text_money;

    public static final String MAIN_ACTIVITY = "main_activity";
    public static final String GET_PLAYER_SCORE = "player_score";
    private SharedPreferences my_activity;
    // нужно для сохранения денег, забейте

    public static int money = 0;
    public static int chosen = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout layout =(RelativeLayout)findViewById(R.id.activity_main);
        my_activity = getSharedPreferences(MAIN_ACTIVITY, Context.MODE_PRIVATE);


        layout.setBackgroundResource(R.drawable.background_main);
        // установка фона (красивая картинка из инета)

        text_money = (TextView) findViewById(R.id.player_score);
        if (my_activity.contains(GET_PLAYER_SCORE)) {
            // получаем число из сохранёнки

            money = my_activity.getInt(GET_PLAYER_SCORE, 0);
            // выводим
            if(text_money == null){
                text_money = (TextView) findViewById(R.id.player_score);
            }
            try {
                text_money.setText(Integer.toString(money));
            } catch (Exception e){
                Log.v("olo", "Money == null" + e);
            }
        }
        ImageView shop_image = (ImageView) findViewById(R.id.shop);
        shop_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToShop(v);
            }
        });

        RelativeLayout main =(RelativeLayout)findViewById(R.id.activity_main);
        main.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                play(v);
            }
        });

        ImageView tutorial_image = (ImageView) findViewById(R.id.tutorial);
        tutorial_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSettings(v);
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        // запоминаем данные
        SharedPreferences.Editor editor = my_activity.edit();
        editor.putInt(GET_PLAYER_SCORE, money);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences.Editor editor = my_activity.edit();
        editor.putInt(GET_PLAYER_SCORE, money);
        editor.apply();
        money = data.getIntExtra("money", 0);
        if (my_activity.contains(GET_PLAYER_SCORE)) {
            // получаем число из сохранёнки

            money = my_activity.getInt(GET_PLAYER_SCORE, 0);
            // выводим
            if(text_money == null){
                text_money = (TextView) findViewById(R.id.player_score);
            }
            try {
                text_money.setText(Integer.toString(money));
            } catch (Exception e){
                Log.v("olo", "Money == null" + e);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }




    public void goToShop(View view) {
        Intent goToShopIntent = new Intent(MainActivity.this, shop.class);
        goToShopIntent.putExtra("chosen", chosen);
        startActivityForResult(goToShopIntent, 2);
    }
    public void goToSettings(View view) {
        Intent goToSettingsIntent = new Intent(MainActivity.this, settings.class);
        startActivity(goToSettingsIntent);
    }
    public void play(View view) {
        Intent play_intent = new Intent(this, game.class);
        play_intent.putExtra("money", Integer.toString(money));
        play_intent.putExtra("chosen", chosen);
        startActivityForResult(play_intent, 1);
    }
    public void money_plus(View view) {
        text_money.setText(Integer.toString(money));
        //money++;
    }
}