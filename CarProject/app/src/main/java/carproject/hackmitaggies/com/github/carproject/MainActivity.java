package carproject.hackmitaggies.com.github.carproject;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.microsoft.projectoxford.speechrecognition.ISpeechRecognitionServerEvents;
import com.microsoft.projectoxford.speechrecognition.MicrophoneRecognitionClient;
import com.microsoft.projectoxford.speechrecognition.RecognitionResult;
import com.microsoft.projectoxford.speechrecognition.RecognitionStatus;
import com.microsoft.projectoxford.speechrecognition.SpeechRecognitionServiceFactory;
import com.microsoft.projectoxford.speechrecognition.SpeechRecognitionMode;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ISpeechRecognitionServerEvents {

    int m_waitSeconds = 0;
    MicrophoneRecognitionClient micClient = null;
    FinalResponseStatus isReceivedResponse = FinalResponseStatus.NotReceived;
    boolean run = true;
    DatabaseReference ref = null;

    public enum FinalResponseStatus { NotReceived, OK, Timeout }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://hackmit-e9161.firebaseio.com/status");

        final MainActivity This = this;
        final TextView status = (TextView) findViewById(R.id.textView);

        Button start = (Button) findViewById(R.id.button2);
        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                run = true;
                status.setText("STARTING");
                This.startListener(view);
            }
        });

        Button stop = (Button) findViewById(R.id.button);
        stop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                run = false;
                This.resetClient();
                status.setText("STOPPED");
            }
        });

        Button forward = (Button) findViewById(R.id.FORWARD);
        forward.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ref.child("message").setValue("f");
                ref.child("message").setValue("");
            }
        });

        Button left = (Button) findViewById(R.id.LEFT);
        left.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ref.child("message").setValue("l");
                ref.child("message").setValue("");
            }
        });

        Button right = (Button) findViewById(R.id.RIGHT);
        right.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ref.child("message").setValue("r");
                ref.child("message").setValue("");
            }
        });

        Button backward = (Button) findViewById(R.id.BACKWARD);
        backward  .setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ref.child("message").setValue("R");
                ref.child("message").setValue("");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showToast(CharSequence text)
    {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void setStatus(CharSequence text)
    {
        final TextView status = (TextView) findViewById(R.id.textView);
        status.setText(text);
    }

    //Voice api area

    public void resetClient()
    {
        if (this.micClient != null) {
            this.micClient.endMicAndRecognition();
            try {
                this.micClient.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            this.micClient = null;
        }
    }


    private void startListener(View view) {
        m_waitSeconds = 20;

        if (this.micClient == null) {
            setStatus("Trying to make client");
            this.micClient = SpeechRecognitionServiceFactory.createMicrophoneClient(
                    this,
                    SpeechRecognitionMode.ShortPhrase,
                    "en-us",
                    this,
                    this.getPrimaryKey(),
                    this.getSecondaryKey());
        }

        setStatus("Made client");
        this.micClient.startMicAndRecognition();
    }

    /**
     * Gets the primary subscription key
     */
    public String getPrimaryKey() {
        return this.getString(R.string.primaryKey);
    }

    /**
     * Gets the secondary subscription key
     */
    public String getSecondaryKey() {
        return this.getString(R.string.secondaryKey);
    }

    public void onPartialResponseReceived(final String response) {
        //showToast(response);
        final TextView status = (TextView) findViewById(R.id.textView);
        status.setText("R:"+response);
    }

    public void onIntentReceived(final String payload) {
    }

    public void onError(final int errorCode, final String response) {
    }

    public void onFinalResponseReceived(final RecognitionResult response) {
        boolean isFinalDicationMessage = false &&
                (response.RecognitionStatus == RecognitionStatus.EndOfDictation ||
                        response.RecognitionStatus == RecognitionStatus.DictationEndSilenceTimeout);
        if (null != this.micClient && true) {
            // we got the final result, so it we can end the mic reco.  No need to do this
            // for dataReco, since we already called endAudio() on it as soon as we were done
            // sending all the data.
            this.micClient.endMicAndRecognition();
        }

        if (isFinalDicationMessage) {
            //this._startButton.setEnabled(true);
            this.isReceivedResponse = FinalResponseStatus.OK;
        }

        if (!isFinalDicationMessage) {
            for (int i = 0; i < response.Results.length; i++) {
                    //showToast(response.Results[i].DisplayText);
                final TextView status = (TextView) findViewById(R.id.textView);
                status.setText("F:"+response.Results[i].DisplayText);

            }

            //if(run)
             //   startListener(findViewById(android.R.id.content));
        }
    }

    public void onAudioEvent(boolean recording) {
        if (recording) {
            final TextView status = (TextView) findViewById(R.id.textView);
            //showToast("Please start speaking.");
            status.setText("SPEAK");
        }

        if (!recording) {
            this.micClient.endMicAndRecognition();
            //this._startButton.setEnabled(true);
        }
    }

    //End voice api area
}
