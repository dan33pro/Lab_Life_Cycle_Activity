package unipiloto.edu.stopwatchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Locale;

public class StopwatchActivity extends AppCompatActivity {

    private int seconds = 0;
    private int lapSize = 0;
    private int numberDefinitiveLaps = 0;
    private boolean running;
    private Switch switchControllerLaps;
    private HashMap<Integer, String> timeLaps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        runTimer();
        EditText hoursText = findViewById(R.id.hours_text);
        EditText minutesText = findViewById(R.id.minutes_text);
        EditText secondsText = findViewById(R.id.seconds_text);
        timeLaps = new HashMap<>();
        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                // Verificar si la entrada es un número decimal válido
                try {
                    String newText = dest.toString().substring(0, dstart) + source.toString() + dest.toString().substring(dend);
                    double inputNumber = Double.parseDouble(newText);

                    // Limitar a dos cifras después del punto decimal
                    if (inputNumber >= 0 && inputNumber < 60) {
                        // Aceptar la entrada
                        return null;
                    }
                } catch (NumberFormatException e) {
                    // La entrada no es un número válido
                }

                // Rechazar la entrada
                return "";
            }
        };
        hoursText.setFilters(new InputFilter[]{inputFilter});
        minutesText.setFilters(new InputFilter[]{inputFilter});
        secondsText.setFilters(new InputFilter[]{inputFilter});
        switchControllerLaps  = findViewById(R.id.controller_laps);
    }

    public void onClickStart(View view) {
        final EditText hoursET = (EditText) findViewById(R.id.hours_text);
        final EditText minutesET = (EditText) findViewById(R.id.minutes_text);
        final EditText secondsET = (EditText) findViewById(R.id.seconds_text);
        final EditText numberLaps = (EditText) findViewById(R.id.number_laps);
        final Switch swLaps = (Switch) findViewById(R.id.controller_laps);

        if (switchControllerLaps.isChecked() && lapSize == 0) {
            String hoursText = hoursET.getText().toString();
            String minutesText = minutesET.getText().toString();
            String secondsText = secondsET.getText().toString();
            if (!hoursText.isEmpty()) {
                int hoursSize = Integer.parseInt(hoursText) * 3600;
                lapSize += hoursSize;
            }
            if (!minutesText.isEmpty()) {
                int minutesSize = Integer.parseInt(minutesText) * 60;
                lapSize += minutesSize;
            }
            if (!secondsText.isEmpty()) {
                int secondsSize = Integer.parseInt(secondsText);
                lapSize += secondsSize;
            }
            numberDefinitiveLaps = Integer.parseInt(numberLaps.getText().toString());
        }
        hoursET.setEnabled(false);
        minutesET.setEnabled(false);
        secondsET.setEnabled(false);
        numberLaps.setEnabled(false);
        swLaps.setEnabled(false);
        running = true;
    }

    public void onClickStop(View view) {
        running = false;
    }

    public void onClickReset(View view) {
        final EditText hoursET = (EditText) findViewById(R.id.hours_text);
        final EditText minutesET = (EditText) findViewById(R.id.minutes_text);
        final EditText secondsET = (EditText) findViewById(R.id.seconds_text);
        final EditText numberLaps = (EditText) findViewById(R.id.number_laps);
        final TextView numberLapsTEXT = (TextView) findViewById(R.id.currently_number_laps);
        final Switch swLaps = (Switch) findViewById(R.id.controller_laps);
        final Button startBTN = (Button) findViewById(R.id.start_button);

        numberLapsTEXT.setText(R.string.time);
        timeLaps.clear();
        hoursET.setText("");
        minutesET.setText("");
        secondsET.setText("");
        numberLaps.setText("");


        hoursET.setEnabled(true);
        minutesET.setEnabled(true);
        secondsET.setEnabled(true);
        numberLaps.setEnabled(true);
        swLaps.setEnabled(true);
        startBTN.setEnabled(true);

        running = false;
        seconds = 0;
        lapSize = 0;
        numberDefinitiveLaps = 0;
    }

    private void runTimer() {
        final TextView timeView = (TextView) findViewById(R.id.time_view);
        final TextView numberLaps = (TextView) findViewById(R.id.currently_number_laps);
        final Button startBTN = (Button) findViewById(R.id.start_button);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds/3600;
                int minutes = (seconds%3600)/60;
                int secs = seconds%60;
                String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs);
                timeView.setText(time);
                if (lapSize != 0) {
                    int laps = seconds / lapSize;
                    numberLaps.setText(laps+"");
                    String time2 = String.format(Locale.getDefault(), "%02d:%02d:%02d", (seconds + 1)/3600, (((seconds + 1)%3600)/60), (seconds + 1)%60);
                    if (laps == numberDefinitiveLaps) {
                        running = false;
                        startBTN.setEnabled(false);
                    } else {
                        timeLaps.put(laps + 1, time2);
                    }
                }

                if (running) {
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    public void onClickViewLaps(View view) {
        Gson gson = new Gson();
        String timeLapsJSON = gson.toJson(timeLaps);

        Intent intent = new Intent(this, TimeLapsActivity.class);
        intent.putExtra(TimeLapsActivity.TIME_LAPS, timeLapsJSON);
        startActivity(intent);
    }
}