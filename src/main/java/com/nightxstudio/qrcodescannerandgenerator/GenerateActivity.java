package com.nightxstudio.qrcodescannerandgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;


public class GenerateActivity extends AppCompatActivity {

    private ImageView holder;
    private EditText dataEditText;
    private Button generate;
    private ImageButton save;
    private TextView sampleText;

    private String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    private Bitmap bitmap;
    private int QRCodeWidth = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        holder = findViewById(R.id.holder);
        dataEditText = findViewById(R.id.dataEditText);
        generate = findViewById(R.id.generate);
        save = findViewById(R.id.save);
        sampleText = findViewById(R.id.sampleText);


        generate.setOnClickListener(new View.OnClickListener() {
            private static final String TAG = "Error";

            @Override
            public void onClick(View v) {

                if (dataEditText.getText().toString().trim().length() == 0) {
                    sampleText.setVisibility(View.VISIBLE);
                } else {
                    sampleText.setVisibility(View.GONE);
                }

                String data = dataEditText.getText().toString();


                if (dataEditText.getText().toString().trim().length() == 0) {
                    Toast.makeText(GenerateActivity.this, "Enter some text", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        bitmap = textToImageEncode(data);
                        holder.setImageBitmap(bitmap);
                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (dataEditText.getText().toString().trim().length() == 0) {
                                    Toast.makeText(GenerateActivity.this, "Generate QR Code to save it", Toast.LENGTH_SHORT).show();
                                } else {
                                    MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "QRCode_scanner_nxs"
                                            , null);
                                    Toast.makeText(GenerateActivity.this, "Saved to gallery", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        });


                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }


            }
        });

        //Change Status Bar color:
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
    }


    private Bitmap textToImageEncode(String value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE, QRCodeWidth, QRCodeWidth, null);
        } catch (IllegalArgumentException e) {
            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offSet = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offSet + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

}