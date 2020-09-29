package com.example.myapplication;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Clock;

public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);

                Snackbar.make(view, "Creating folder", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Context context = getContext();
                File mydir = context.getDir("mydir", Context.MODE_PRIVATE); //Creating an internal dir;
                File[] files = mydir.listFiles();
                for(File file:files)
                {
                    System.out.println("Myfilenameis >>>>>>>>>>>>>>>>>>>"+file.getAbsolutePath());
                    file.delete();
                }

                File myVideoDir = context.getExternalFilesDir("myvideos"); //Creating an internal dir;
                System.out.println("Myvideodir >>>>>>>>>>>>>>>>>>>"+myVideoDir.isDirectory());

                System.out.println("Myvideodir path >>>>>>>>>>>>>>>>>>>"+myVideoDir.getAbsolutePath());
                if(myVideoDir.isDirectory())
                {

                   String fileNames[] =  myVideoDir.list();
                    System.out.println("fileNames.length ============="+fileNames.length);
                   for(String name:fileNames)
                   {
                       System.out.println("============="+name);
                       String key = "1234567890123456";
                       File inputFile = new File(myVideoDir,name);
                       File encryptedFile = new File(myVideoDir,"encrypted.mp4");
                       File decryptedFile = new File(mydir,"decrypted.mp4");

                       try {
                           System.out.println("Starting Encryption");
                           CryptoUtil.encrypt(key, inputFile, encryptedFile);

                           System.out.println("Starting Decryption");
                           CryptoUtil.decrypt(key, encryptedFile, decryptedFile);
                       }
                       catch(CryptoException ce)
                       {
                            ce.printStackTrace();
                       }
                   }
                }


                Uri uri = Uri.fromFile(new File(mydir,"decrypted.mp4"));
                MediaPlayer mediaplayer = MediaPlayer.create(context, uri);
                if(mediaplayer == null) {
                    //Log.v(TAG, "Create() on MediaPlayer failed.");
                } else {
                    mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mediaplayer) {
                            mediaplayer.stop();
                            mediaplayer.release();
                        }
                    });
                    mediaplayer.start();

                }

                //Testfile in private location
                /*try {


                    FileInputStream fis = new FileInputStream(new File(mydir,"decrypted.mp4"));
                    FileOutputStream fos = new FileOutputStream(new File(myVideoDir,"final.mp4"));
                    byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                        fis.close();
                        fos.close();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }*/



            }
        });
    }
}