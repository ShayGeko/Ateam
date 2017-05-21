package enon.hfad.com.ateam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView text_money;

    public static final String MAIN_ACTIVITY = "main_activity";
    public static final String GET_PLAYER_SCORE = "player_score";
    private SharedPreferences my_activity;
    // нужно для сохранения денег, забейте

    public static int money = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout layout =(RelativeLayout)findViewById(R.id.activity_main);
        my_activity = getSharedPreferences(MAIN_ACTIVITY, Context.MODE_PRIVATE);


        layout.setBackgroundResource(R.drawable.background_main);
        // установка фона (красивая картинка из инета)

        text_money = (TextView) findViewById(R.id.player_score);
    }

    // @Override
    // protected void onNewIntent(Bundle savedInstanceState) {
    // }

    @Override
    protected void onPause() {
        super.onPause();
        // запоминаем данные
        SharedPreferences.Editor editor = my_activity.edit();
        editor.putInt(GET_PLAYER_SCORE, money);
        editor.apply();
    }



    @Override
    protected void onResume() {
       super.onResume();

      if (my_activity.contains(GET_PLAYER_SCORE)) {
          // получаем число из сохранёнки
          money = my_activity.getInt(GET_PLAYER_SCORE, 0);
            // выводим
//          text_money.setText(Integer.toString(money));
      }
  }



    public void goToShop(View view) {
        Intent goToShopIntent = new Intent(MainActivity.this, shop.class);
        startActivity(goToShopIntent);
    }
    public void goToSettings(View view) {
        Intent goToSettingsIntent = new Intent(MainActivity.this, settings.class);
        startActivity(goToSettingsIntent);
    }
    public void play(View view) {
        Intent play_intent = new Intent(this, game.class);
        play_intent.putExtra("money", Integer.toString(money));
        startActivity(play_intent);
    }
}
