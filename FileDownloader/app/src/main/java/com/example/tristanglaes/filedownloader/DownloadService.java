package com.example.tristanglaes.filedownloader;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class DownloadService extends Service {

    private IBinder iBinder = null;

    public class LocalBinder extends Binder {
        public DownloadService getService() {
            return DownloadService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        return iBinder;
    }

    @Override
    public void onCreate(){
        iBinder  = new LocalBinder();
    }

    private class DownloadTask extends AsyncTask<String, Integer, Integer>{

        @Override
        protected Integer doInBackground(String... urls) {
            int totalSize = 0;
            int maxSize = 0;
            try {
                InputStream fileToDownload;
                FileOutputStream downloadedFile;

                URL url = new URL(urls[0]);
                URLConnection urlConnect = url.openConnection();
                fileToDownload = urlConnect.getInputStream();
                maxSize = urlConnect.getContentLength();
                downloadedFile = openFileOutput("test.txt", Context.MODE_PRIVATE); //new FileOutputStream(f.getAbsolutePath());
                int x = 0;
                while ((x = fileToDownload.read()) != -1) {

                    downloadedFile.write(x);
                    totalSize = totalSize + x;
                    publishProgress(totalSize * 100 / maxSize );
                    if(totalSize >= maxSize){
                        break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return totalSize;
        }

        @Override
        protected void onPreExecute(){
            return;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            MainActivity.setDownloadProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(Integer result) {
            return;
        }

    }

    public void startDownload(String downloadUrl){
        DownloadTask dt = new DownloadTask();
        dt.execute(downloadUrl);
    }
}
