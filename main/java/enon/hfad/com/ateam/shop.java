package enon.hfad.com.ateam;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static enon.hfad.com.ateam.MainActivity.money;

/**
 * Created by HOME on 06.03.2017.
 */

public class shop extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        RelativeLayout shop_layout =(RelativeLayout)findViewById(R.id.activity_shop);
        shop_layout.setBackgroundResource(R.drawable.background_shop);
        TextView money_text = (TextView)findViewById(R.id.money);
        money_text.setText(Integer.toString(money));


    }
}
