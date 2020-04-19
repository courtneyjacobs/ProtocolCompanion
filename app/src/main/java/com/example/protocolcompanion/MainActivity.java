package com.example.protocolcompanion;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DownloadCallback {

    private AppBarConfiguration mAppBarConfiguration;

    private StudyViewModel studyViewModel;

    // All network-related info in this file is from Android docs at https://developer.android.com/training/basics/network-ops/connecting
    // Keep a reference to the NetworkFragment, which owns the AsyncTask object
    // that is used to execute network ops.
    private NetworkFragment networkFragment;

    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean downloading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_createstudy, R.id.nav_loadstudy)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Import studies from JSON file
        studyViewModel = new ViewModelProvider(this).get(StudyViewModel.class);
        File JSONFile = new File(Objects.requireNonNull(getApplicationContext()).getFilesDir(), "protocols.json");
        StringBuilder stringBuilder = new StringBuilder();
        try {
            if(!JSONFile.createNewFile()) {
                BufferedReader br = new BufferedReader(new FileReader(JSONFile.getPath()));
                String currentLine;
                while ((currentLine = br.readLine()) != null) {
                    stringBuilder.append(currentLine).append("\n");
                }
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        studyViewModel.setFullJSONObject(stringBuilder.toString());

        // set up network frag
        networkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "https://www.dropbox.com/s/k7apsscrnoavgk3/test2studies.json?dl=1");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void updateFromDownload(Object result) {
        Context context = getApplicationContext();
        CharSequence text;
        int duration = Toast.LENGTH_SHORT;
        // if null, contains error messages, or doesn't contain any braces ("{"), the import was unsuccessful
        if(result == null || String.valueOf(result).contains("no protocol") || String.valueOf(result).contains("timeout") || !String.valueOf(result).contains("{")) {
            text = "Import unsuccessful.";
        }
        else {
            text = "Study successfully imported!";
            System.out.println("RESULT VALUE: " + result);
            studyViewModel.appendToFullJSONObject(String.valueOf(result));
        }
        Toast.makeText(context, text, duration).show();
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        /*switch(progressCode) {
            // You can add UI behavior for progress updates here.
            case Progress.ERROR:
                break;
            case Progress.CONNECT_SUCCESS:

                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:

                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:

                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:

                break;
        }*/
    }

    public void startDownload() {
        if (!downloading && networkFragment != null) {
            // Execute the async download.
            networkFragment.startDownload();
            downloading = true;
        }
    }

    @Override
    public void finishDownloading() {
        downloading = false;
        if (networkFragment != null) {
            networkFragment.cancelDownload();
        }
    }


}
