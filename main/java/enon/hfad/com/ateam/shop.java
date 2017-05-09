package enon.hfad.com.ateam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

//import static enon.hfad.com.ateam.MainActivity.money;

/**
 * Created by HOME on 06.03.2017.
 */

public class shop extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        HorizontalScrollView shop_layout =(HorizontalScrollView)findViewById(R.id.activity_shop);
        shop_layout.setBackgroundResource(R.drawable.background_shop);
        TextView heads_text = (TextView)findViewById(R.id.heads);
        //money_text.setText(Integer.toString(money));
        heads_text.setText("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBccccccccccccccccccc");
        TextView t_shirts_text = (TextView)findViewById(R.id.t_shirts);
        t_shirts_text.setText("");
    }
    public void scroll_right(View view) {
        HorizontalScrollView scroll_view = (HorizontalScrollView) findViewById(R.id.activity_shop);
        scroll_view.scrollBy(200, 0);
    }



}