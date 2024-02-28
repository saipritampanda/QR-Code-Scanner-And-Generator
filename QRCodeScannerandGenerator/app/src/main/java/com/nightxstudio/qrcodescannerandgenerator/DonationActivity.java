package com.nightxstudio.qrcodescannerandgenerator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

public class DonationActivity extends AppCompatActivity{


    Button chocolateDonate;

    ImageView donate30Image;
    Button coffeeDonate;

    ImageView donate50Image;
    Button burgerDonate;

    ImageView donate120Image;
    Button mealDonate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);


        chocolateDonate = findViewById(R.id.chocolateDonate);
        coffeeDonate = findViewById(R.id.coffeeDonate);
        burgerDonate = findViewById(R.id.burgerDonate);
        mealDonate = findViewById(R.id.mealDonate);

        chocolateDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonationActivity.this, Donate10Activity.class);
                startActivity(intent);

                // Load the QR code image from drawable resource
                Bitmap qrCodeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.donate_rs10_qrcode_image);
                decodeAndSearchQRCode(qrCodeBitmap);

            }
        });

        coffeeDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonationActivity.this, Donate30Activity.class);
                startActivity(intent);

                // Load the QR code image from drawable resource
                Bitmap qrCodeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.donate_rs30_qrcode_image);
                decodeAndSearchQRCode(qrCodeBitmap);
            }
        });

        burgerDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonationActivity.this, Donate50Activity.class);
                startActivity(intent);

                // Load the QR code image from drawable resource
                Bitmap qrCodeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.donate_rs50_qrcode_image);
                decodeAndSearchQRCode(qrCodeBitmap);
            }
        });

        mealDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonationActivity.this, Donate120Activity.class);
                startActivity(intent);

                // Load the QR code image from drawable resource
                Bitmap qrCodeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.donate_rs120_qrcode_image);
                decodeAndSearchQRCode(qrCodeBitmap);
            }
        });

        //Change Status Bar color:
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
    }

    private void decodeAndSearchQRCode(Bitmap qrCodeBitmap) {

        // Convert the Bitmap to a binary bitmap for QR code scanning
        RGBLuminanceSource source = new RGBLuminanceSource(qrCodeBitmap.getWidth(), qrCodeBitmap.getHeight(), getRGBIntArray(qrCodeBitmap));
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

        // Initialize the QR code reader
        Reader reader = new MultiFormatReader(); // Reader (interface) is imported from com.zxing instead of java.io

        try {
            // Decode the QR code
            Result result = reader.decode(binaryBitmap);
            String extractedText = result.getText();

            // Search the extracted text on Google
            openGoogleSearch(extractedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void openGoogleSearch(String query) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(query));
        startActivity(intent);
    }

    private int[] getRGBIntArray(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        return pixels;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

}