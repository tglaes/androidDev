package com.example.tristanglaes.filedownloader;

import android.app.Activity;
import android.content.ComponentName;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


public class MainActivity extends Activity {

    private Button downloadBtn;
    private EditText downloadUrlEt;
    private static ProgressBar progressBar;
    public final static Object MAINACTIVITY_KEY = "MAINACTIVITY";
    private DownloadService downloadService = null; // Service Referenz
    private boolean bound = false; // ob Service verbunden ist
    private String downloadUrl = "";
    int progress = 0;

    public ServiceConnection mConn = new ServiceConnection() {
        /* Wird aufgerufen, wenn der Service verbunden ist */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DownloadService.LocalBinder binder = (DownloadService.LocalBinder) service;
            downloadService = binder.getService();
            bound = true;
        }
        /* Wird aufgerufen, wenn der Service nicht mehr verbunden ist */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadBtn = findViewById(R.id.downloadBtn);
        downloadUrlEt = findViewById(R.id.downloadUrlEt);
        downloadUrlEt.setText("http://www.speedtestx.de/testfiles/data_1mb.test");
        progressBar = findViewById(R.id.downloadPb);
        startDownloadService();

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                downloadUrl = downloadUrlEt.getText().toString();
                progressBar.setProgress(0);

                if(downloadUrl.equals("")){
                    Toast.makeText(MainActivity.this, "Что ты делаешь?", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Start Download", Toast.LENGTH_LONG).show();
                    startDownload(downloadUrl);
                    //TODO: Toast wenn download fertig ist.
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        unbindService(mConn);
        super.onDestroy();
    }

    private void startDownloadService() {
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent, mConn, Context.BIND_AUTO_CREATE);
    }

    private void startDownload(String url){
        if(bound) {
            downloadService.startDownload(url);
        }
    }

    private void destroyDownloadService() {
        unbindService(mConn);
    }

    public static void setDownloadProgress(int progress){
        progressBar.setProgress(progress);
    }
}
