package enon.hfad.com.ateam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ToggleButton;

/**
 * Created by HOME on 06.03.2017.
 */

public class settings extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
    boolean apply_blood  = true;
    public void onToggleClicked(View view) {
        // button YES/NO blood
        apply_blood = ((ToggleButton) view).isChecked();
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent goToMenuIntent = new Intent(settings.this, MainActivity.class);
    }

}
