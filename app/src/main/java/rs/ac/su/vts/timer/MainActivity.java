package rs.ac.su.vts.timer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;


public class MainActivity extends ActionBarActivity {

    EditText enter_time_txt;
    TextView remaining_time_txt;
    Button set_time, start, reset;
    String shared, remaining, elapsed, start_txt;
    Long time_millisec, resume;
    CountDownTimer cdt_down;
    Integer num;
    Boolean isRunning = false;
    Boolean isReset = false;
    AlertDialog.Builder dialogBuilder;
    ProgressBar progess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enter_time_txt = (EditText)findViewById(R.id.enter_time_txt);
        remaining_time_txt = (TextView)findViewById(R.id.remaining_time_txt);
        set_time = (Button)findViewById(R.id.set_time);
        start = (Button)findViewById(R.id.start);
        reset = (Button)findViewById(R.id.reset);
        progess = (ProgressBar)findViewById(R.id.progressBar);
        progess.setVisibility(View.GONE);
        start.setEnabled(false);
        num = 0;
    }

    public void set_time(View v){
        shared = enter_time_txt.getText().toString();
        if(shared.isEmpty()) {
            Toast.makeText(MainActivity.this, "Empty", Toast.LENGTH_LONG).show();
        }
        else {
            //Toast.makeText(MainActivity.this, shared, Toast.LENGTH_LONG).show();
            int i = Integer.parseInt(shared);
            if(i < 10){
                remaining_time_txt.setText("0"+shared+":00:00");
            }
            else
                remaining_time_txt.setText(shared+":00:00");
            start.setEnabled(true);
        }
    }

    public void start(View v){
        if(enter_time_txt.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this, "Empty", Toast.LENGTH_LONG).show();
        }
        else{
            set_time.setEnabled(false);
            start_txt = start.getText().toString();
            //remaning = remaning_time_txt.getText().toString();
            //Toast.makeText(MainActivity.this, start_txt, Toast.LENGTH_SHORT).show();
            if(start_txt.equals("Start")){
                progess.setVisibility(View.VISIBLE);
                if(isRunning == false) {
                    timer_down(shared);
                }
                else {
                    cdt_down = new CountDownTimer(resume, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            isRunning = true;
                            long millis = millisUntilFinished;
                            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                            resume = millisUntilFinished;
                            remaining_time_txt.setText(hms);
                        }

                        @Override
                        public void onFinish() {
                            isRunning = false;
                            remaining_time_txt.setText("Finished");
                        }
                    }.start();
                }
                start_txt = "Stop";
                start.setText(start_txt);
            }else{
                start_txt = "Start";
                start.setText(start_txt);
                cdt_down.cancel();
                progess.setVisibility(View.GONE);
            }
        }
    }

    public void timer_down(String s){
        time_millisec  = Long.valueOf(s).longValue(); // str to long

        // 0,012 * 1000 = 12 sec * 60 * 60
        time_millisec = time_millisec * 1000 * 60 * 60;
        //Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        cdt_down = new CountDownTimer(time_millisec, 1000) {
           @Override
           public void onTick(long millisUntilFinished) {
               isRunning = true;
               long millis = millisUntilFinished;
               String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                       TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                       TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
               resume = millisUntilFinished;
               remaining_time_txt.setText(hms);
           }

           @Override
           public void onFinish() {
               isRunning = false;
               remaining_time_txt.setText("Finished");
               progess.setVisibility(View.GONE);
           }
       }.start();
    }

    public void onBackPressed() {

        dialogBuilder = new AlertDialog.Builder(this);
        String title = "Timer";
        dialogBuilder.setTitle(title);
        String msg = "Leave and stopped timer?";
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progess.setVisibility(View.GONE);
                cdt_down.cancel();
                start_txt = "Start";
                start.setText(start_txt);
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        });
        dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onPause();
            }
        });
        AlertDialog finished = dialogBuilder.create();
        finished.show();
    }

    public void reset(View v){
        set_time.setEnabled(true);
        cdt_down.cancel();
        start_txt = "Start";
        start.setText(start_txt);
        start.setEnabled(false);
        cdt_down.onFinish();
        remaining_time_txt.setText("00:00:00");
        progess.setVisibility(View.GONE);
    }










    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
