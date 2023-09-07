package unipiloto.edu.stopwatchapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class TimeLapsActivity extends AppCompatActivity {

    public static final String TIME_LAPS = "";
    private HashMap<Integer, String> timeLaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_laps);

        Intent intentReceived = getIntent();
        String timeLapsJSON = intentReceived.getStringExtra(TIME_LAPS);
        Gson gson = new Gson();
        timeLaps = gson.fromJson(timeLapsJSON, new TypeToken<HashMap<Integer, String>>(){}.getType());

        LinearLayout mainContainer = (LinearLayout) findViewById(R.id.containerTimeLaps);
        for (Integer clave : timeLaps.keySet()) {
            String valor = timeLaps.get(clave);

            TextView numberLap = new TextView(this);
            numberLap.setPadding(40, 20, 40, 20);
            numberLap.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) numberLap.getLayoutParams();
            params.weight = 1;
            numberLap.setLayoutParams(params);
            numberLap.setBackgroundResource(R.drawable.borde_textview);
            numberLap.setText("Lap: " + clave);

            TextView timeLap = new TextView(this);
            timeLap.setPadding(40, 20, 40, 20);
            timeLap.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) numberLap.getLayoutParams();
            params2.weight = 2;
            timeLap.setLayoutParams(params2);
            timeLap.setBackgroundResource(R.drawable.borde_textview);
            timeLap.setText("Time: " + valor);

            LinearLayout containerRow = new LinearLayout(this);
            containerRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            containerRow.setOrientation(LinearLayout.HORIZONTAL);

            containerRow.addView(numberLap);
            containerRow.addView(timeLap);

            mainContainer.addView(containerRow);
        }
        TextView numberLap = new TextView(this);
        numberLap.setPadding(20, 20, 20, 20);
    }
}