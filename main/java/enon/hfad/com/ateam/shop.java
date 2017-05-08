package enon.hfad.com.ateam;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static enon.hfad.com.ateam.MainActivity.money;

//import static enon.hfad.com.ateam.MainActivity.money;

/**
 * Created by HOME on 06.03.2017.
 */

public class shop extends AppCompatActivity {
    public static boolean purchased[]= new boolean[5];
    public int chosen = 1;
    ImageButton[] cell = new ImageButton[5];
    ImageView temporary;
    TextView money_text;
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("money", money);
        intent.putExtra("chosen", chosen);
        setResult(RESULT_OK, intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = display.getWidth();

        cell[0] = (ImageButton) findViewById(R.id.imageButton1);
        cell[1] = (ImageButton) findViewById(R.id.imageButton2);
        cell[2] = (ImageButton) findViewById(R.id.imageButton3);
        cell[3] = (ImageButton) findViewById(R.id.imageButton4);
        cell[4] = (ImageButton) findViewById(R.id.imageButton5);


        //отступы
        ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(cell[0].getLayoutParams());
        marginParams.setMargins((width) / 16, 0, (width) / 16, 0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(marginParams);
        for(int i = 0; i < 5; i++){
            cell[i].setLayoutParams(layoutParams);
            cell[i].setPadding(10,5,10,5);
            if(i == chosen)cell[i].setBackgroundColor(Color.GREEN);
            else if(purchased[i])cell[i].setBackgroundColor(Color.YELLOW);
        }

        money_text = (TextView) findViewById(R.id.coins);
        money_text.setText(Integer.toString(money));
    }
    void onCellClick(View v){
        for(int i =0; i < 5; i++) {
            if (v == cell[i]) {
                if (chosen!= i + 1 && purchased[i]) {
                    cell[chosen - 1].setBackgroundColor(Color.YELLOW);
                    chosen = i;
                    cell[i].setBackgroundColor(Color.GREEN);
                    Toast toast = Toast.makeText(getApplicationContext(), "Skin was chosen", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (money >= 5) {
                    money -= 5;
                    purchased[i] = true;
                    money_text.setText(Integer.toString(money));
                    cell[i].setBackgroundColor(Color.YELLOW);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Not enough coins!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
    }

}
