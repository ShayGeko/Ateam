package enon.hfad.com.ateam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by HOME on 06.03.2017.
 */

public class settings extends AppCompatActivity {
    byte number = 0;
    byte end_number = 10;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final String[] hints = { "To start game click on the screen", "This is your money", "By clicking this you can go to the shop", "This is your lifes. Every life is one dwarf you can drop " ,"This is money. By collecting them you gain 1 coin to your balance", "Those 'bushes' are spikes. If you touch them with your dwarf, you lose one life", "When countdown ends, the game starts", "Dwarfs are falling from this plane", "This is your dwarf", "This is your current skin", "Those skins you can buy. Every skin costs 100 coins",  };

        final ImageView arrow_right = (ImageView) findViewById(R.id.arrow_right);
        final ImageView arrow_left = (ImageView) findViewById(R.id.arrow_left);
        final TextView text = (TextView) findViewById(R.id.text_hint);
        text.setText(hints[number]);
        final ImageView image = (ImageView) findViewById(R.id.image_hint);
        arrow_right.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                number += 1;
                switch ( number) {
                    case 0: image.setImageResource(R.drawable.main_0);
                        break;
                    case 1:  image.setImageResource(R.drawable.main_money_1);
                        break;
                    case 2: image.setImageResource(R.drawable.main_shop_2);
                        break;
                    case 3: image.setImageResource(R.drawable.game_lifes_3);
                        break;

                    case 4: image.setImageResource(R.drawable.game_money_4);
                        break;
                    case 5: image.setImageResource(R.drawable.game_spikes_5);
                        break;
                    case 6: image.setImageResource(R.drawable.game_countdown_6);
                        break;
                    case 7: image.setImageResource(R.drawable.game_dwarf_7);
                        break;
                    case 8: image.setImageResource(R.drawable.game_dwarf_8);
                        break;
                    case 9: image.setImageResource(R.drawable.shop_choosed_9);
                        break;
                    case 10:image.setImageResource(R.drawable.shop_can_buy_10);
                }
                text.setText(hints[number]);
                if(number == end_number){arrow_right.setVisibility(View.INVISIBLE);}
                if(number > 0){arrow_left.setVisibility(View.VISIBLE);}
            }
        });

        arrow_left.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        number -= 1;
                        switch ( number) {
                            case 0: image.setImageResource(R.drawable.main_0);
                                break;
                            case 1:  image.setImageResource(R.drawable.main_money_1);
                                break;
                            case 2: image.setImageResource(R.drawable.main_shop_2);
                                break;
                            case 3: image.setImageResource(R.drawable.game_lifes_3);
                                break;

                            case 4: image.setImageResource(R.drawable.game_money_4);
                                break;
                            case 5: image.setImageResource(R.drawable.game_spikes_5);
                                break;
                            case 6: image.setImageResource(R.drawable.game_countdown_6);
                                break;
                            case 7: image.setImageResource(R.drawable.game_dwarf_7);
                                break;
                            case 8: image.setImageResource(R.drawable.game_dwarf_8);
                                break;
                            case 9: image.setImageResource(R.drawable.shop_choosed_9);
                                break;
                            case 10:image.setImageResource(R.drawable.shop_can_buy_10);
                        }
                        text.setText(hints[number]);
                        if(number == 0){arrow_left.setVisibility(View.INVISIBLE);}
                        if(number < end_number){arrow_right.setVisibility(View.VISIBLE);}
                    }
                });
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent goToMenuIntent = new Intent(settings.this, MainActivity.class);
        startActivity(goToMenuIntent);
    }

}
