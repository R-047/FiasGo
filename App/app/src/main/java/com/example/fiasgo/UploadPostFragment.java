package com.example.fiasgo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadPostFragment extends Fragment {

    private ArrayList<String> selectedImagesArray;
    private Camera cam;
    private CameraPreview cameraPreview;
    private ImageView captureImage;
    private FrameLayout cam_holder;
    ImageView selectedImages, galleryBtn;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UploadPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadPostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadPostFragment newInstance(String param1, String param2) {
        UploadPostFragment fragment = new UploadPostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        selectedImagesArray = new ArrayList<>();
        selectedImages = view.findViewById(R.id.selected_pics);
        captureImage = view.findViewById(R.id.capture_btn);
        cam_holder = view.findViewById(R.id.cam_holder_preview);
        galleryBtn = view.findViewById(R.id.gallery_img_btn);
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onStart() {
        System.out.println("starting...........................");
        super.onStart();
        try{
            cam = Camera.open();
        }catch(Exception e){
            System.out.println("please provide permission");
        }

        System.out.println(cam);
        cameraPreview = new CameraPreview(getContext(),cam);
        cam_holder.addView(cameraPreview);


        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onCapture();

            }
        });

        selectedImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSelectedImages();
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromGallery();
            }
        });

    }

    public void selectImageFromGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                if(data.getClipData() != null) {
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    for(int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        System.out.println(imageUri);
                        String imagePath = imageUri.getEncodedPath();
                        System.out.println(imagePath);
                        selectedImagesArray.add(imagePath);
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                    }
                }
            }
    }
    }


    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
            //selectedImages.setImageBitmap(bmp);
            System.out.println("Picture clciked");
            ImageLoader task = new ImageLoader(getContext(), data);
            task.execute();
            cam.stopPreview();
            cam.startPreview();

        }
    };

    public void onCapture() {
        if(cam != null){
            cam.takePicture(null,null, mPictureCallback);
        }
    }

    public void viewSelectedImages(){
        if(selectedImagesArray != null){
            Intent intent = new Intent(getContext(), SelectedImagesActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("ARRAYLIST",(Serializable)selectedImagesArray);
            intent.putExtra("BUNDLE",args);
            startActivity(intent);
        }
    }




    public class ImageLoader extends AsyncTask<Void, Void, Bitmap> {

        private byte[] img_Arr;
        private Context cOntext;


        ImageLoader(Context context, byte[] img_arr)
        {
            this.img_Arr = img_arr;
            this.cOntext = context;
        }



        @Override
        protected Bitmap doInBackground(Void... arg0) {

            Bitmap bitmap = null;
            bitmap = BitmapFactory.decodeByteArray(img_Arr, 0, img_Arr.length);
            return bitmap;
        }

        @Override
        protected void onPostExecute( Bitmap result )  {
            selectedImages.setImageBitmap(result);
            String destFolder = cOntext.getCacheDir().getAbsolutePath();
            String file_name = UUID.randomUUID().toString()+".png";
            String ImgReference = destFolder+file_name;
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(ImgReference);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            result.compress(Bitmap.CompressFormat.PNG, 100, out);
            selectedImagesArray.add(ImgReference);
        }
    }
}