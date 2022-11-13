package com.example.wh40kapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "dataLoad";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy gfgPolicy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
        }
        super.onCreate(savedInstanceState);
        try {
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);
        this.context=this;
    }

    private int loadData() throws IOException {
        Thread dataLoader = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: ");
                try  {
                    File processedData = new File(context.getFilesDir(), "Processed Data.dir");
                    if (!processedData.exists()) {
                        processedData.createNewFile();
                        Log.d(TAG, "loadData: does the processed data still exist?");
                    }
                    Log.d(TAG, "loadData: " + processedData.exists());

                    File datasheets = new File(processedData, "Datasheets.csv");
                    URL website =
                            new URL("https://github.com/EyalKotlik/40k-Android-Project/blob/main/Processed%20Data/Datasheets.csv");
                    ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                    FileOutputStream fos = new FileOutputStream(datasheets);
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

                    Log.d("TAG", "loadData: can read? " + datasheets.canRead());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "run: ");
                }
            }
        });

        dataLoader.run();
        Log.d(TAG, "loadData: ");
        return 1;
    }

}