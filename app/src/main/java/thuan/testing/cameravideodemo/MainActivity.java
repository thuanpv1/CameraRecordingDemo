package thuan.testing.cameravideodemo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import java.io.File;
import java.io.FileWriter;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    private static int Video_Request = 101;
    private Uri videoUri = null;

    ActivityResultLauncher<Intent> myLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                videoUri = result.getData().getData();
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE}, 1);
    }

    public void writeFileOnInternalStorage(Context mcoContext, String sFileName, String sBody){
        File dir = new File("/sdcard/DCIM", "mydir");
        if(!dir.exists()){
            dir.mkdir();
        }

        try {
            File gpxfile = new File(dir, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void captureVideo(View view) {
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        File video_file = getFile();
        Uri uri = Uri.fromFile(video_file);
        String path = getFilesDir().getAbsolutePath();

        writeFileOnInternalStorage(this, "thuan.txt", "12345678974563214560");

        File dir = new File("/sdcard/DCIM", "mydir");

        File localStoragePath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, dir );
        videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        myLauncher.launch(videoIntent);
//        if (videoIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(videoIntent, Video_Request);
//        }
    }

    public File getFile() {
        String path = getFilesDir().getAbsolutePath();
        File localStoragePath = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        Log.d("===thuan==path==", path);
        Log.d("===thuan==path2==", localStoragePath.getPath());
//        File folder = new File("sdcard/myfolder");
//        if (!folder.exists()) {
//            folder.mkdir();
//        }

        File video_file = new File(path, "myvideo.mp4");
        return video_file;
    }
    public void playVideo(View view) {

        Intent playIntent = new Intent(this, videoPlayActivity.class);
        playIntent.putExtra("videoUri", videoUri.toString());
        startActivity(playIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Video_Request && resultCode == RESULT_OK) {
            videoUri = data.getData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}