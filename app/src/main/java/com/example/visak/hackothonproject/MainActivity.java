package com.example.visak.hackothonproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.utils.URIBuilder;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.util.EntityUtils;

public class MainActivity extends AppCompatActivity {

    Button cameraIntent;
    Button userInputIntent;
    Uri fileUri;
    String result;
    String parsedResult;
    HashMap<String,Double> emotionsResult = new HashMap<>();
    HashMap<String,Integer> emotionsValue = new HashMap<>();
    ArrayList<Integer> sortedEmotionsList = new ArrayList<>();


    public void sortHashMap(){
        ArrayList<Double> sortedEmotionsListValues = new ArrayList<>();
        sortedEmotionsListValues.add(emotionsResult.get("anger"));
        sortedEmotionsListValues.add(emotionsResult.get("happiness"));
        sortedEmotionsListValues.add(emotionsResult.get("sadness"));
        emotionsValue.put("anger",0);
        emotionsValue.put("sadness",1);
        emotionsValue.put("happiness",2);

        Collections.sort(sortedEmotionsListValues,Collections.<Double>reverseOrder());
        Log.d("Values",""+sortedEmotionsListValues);

        for (Double value:sortedEmotionsListValues){
            for (String key:emotionsResult.keySet()){
                if (Double.compare(value,emotionsResult.get(key))==0){
                    sortedEmotionsList.add(emotionsValue.get(key));
                }
            }
        }
        Log.d("Sorted List",""+sortedEmotionsList);
    }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    private void getEmotion(){
        final AsyncTask<Void,Void,Void> newTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                HttpClient httpClient = HttpClients.createDefault();
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                try {
                    URIBuilder builder = new URIBuilder("https://westus.api.cognitive.microsoft.com/emotion/v1.0/recognize");
                    URI uri = builder.build();
                    HttpPost request = new HttpPost(uri);
                    request.setHeader("Content-Type", "application/octet-stream");
                    request.setHeader("Ocp-Apim-Subscription-Key", "1ab4ed76e2c8422983d06c005de774be");
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inSampleSize = 8;
//                    final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),options);
                    final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    byte[] b = baos.toByteArray();
                    request.setEntity(new ByteArrayEntity(b));
                    HttpResponse response = httpClient.execute(request);
                    HttpEntity entity = response.getEntity();
                    result = EntityUtils.toString(entity);
                    if (result!=null){
                        Log.d("Parsed Result",result);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(result);
                                String emotions = "";
                                for (int i = 0; i<jsonArray.length();i++){
                                    JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                                    JSONObject scores = jsonObject.getJSONObject("scores");
                                    double max = 0;
                                    String emotion = "";
                                    for (int j =0; j < scores.names().length(); j++){
                                        emotionsResult.put(scores.names().getString(j),scores.getDouble(scores.names().getString(j)));
                                        if (scores.getDouble(scores.names().getString(j))>max){
                                            max = scores.getDouble(scores.names().getString(j));
                                            emotion = scores.names().getString(j);
                                        }
                                    }
                                    emotions = emotion+"\n";
                                }
                                parsedResult = ""+emotions;
                                if (result!=null){
                                    Log.d("Parsed Result",parsedResult);
                                    sortHashMap();
                                    Intent intent = new Intent(MainActivity.this,ConfirmationActivity.class);
                                    intent.putExtra("emotion",sortedEmotionsList.get(0));
                                    intent.putExtra("flowValue",1);
                                    startActivity(intent);
                                }
                            }catch (Exception e){
                                parsedResult = "No emotion Detected!!";
                            }
                        }
                    });
                }catch (Exception e){
                    return null;
                }
                return null;
            }
        };
        runAsyncTask(newTask);
    }

    public void captureImage(){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(1);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
        startActivityForResult(cameraIntent,100);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 100){
            if (resultCode == RESULT_OK){
                getEmotion();
                Toast.makeText(getApplicationContext(),"Image Captured",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(),"Image not Captured",Toast.LENGTH_LONG).show();
            }
        }
    }

    public Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type){
        File mediaStorage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"image");
        File mediafile;
        if (!mediaStorage.exists()){
            if (!mediaStorage.mkdirs()){
                Log.d("image","Failed to create Directory");
            }
        }
        mediafile = new File(mediaStorage.getPath() + File.separator + "EMOTION_IMG"+".jpg");
        return mediafile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraIntent = (Button)findViewById(R.id.cameraIntent);
        userInputIntent = (Button)findViewById(R.id.userIntent);

        if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},100);
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0x3);
        }


        cameraIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });

        userInputIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,UserInput.class);
                intent.putExtra("flowValue",-1);
                startActivity(intent);
            }
        });

    }
}
