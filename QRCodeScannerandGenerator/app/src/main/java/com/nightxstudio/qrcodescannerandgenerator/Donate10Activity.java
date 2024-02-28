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

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

public class Donate10Activity extends AppCompatActivity {

    private AdView mAdViewTop;
    Button pay10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate10);

        //  AD COMPONENTS STARTS HERE:
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        //1. BANNER AD AT THE TOP:
        AdView adViewTop = new AdView(this);

        adViewTop.setAdSize(AdSize.BANNER);

        adViewTop.setAdUnitId("ca-app-pub-1785962158595876/7403944463");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdViewTop = findViewById(R.id.adViewTop);
        AdRequest adRequestTop = new AdRequest.Builder().build();
        mAdViewTop.loadAd(adRequestTop);

        mAdViewTop.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        pay10 = findViewById(R.id.pay10);

        pay10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                decodeAndSearchQRCode();

            }
        });


        //Change Status Bar color:
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
    }

    private void decodeAndSearchQRCode() {
        // Load the QR code image from drawable resource
        Bitmap qrCodeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.donate_rs10_qrcode_image);

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
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext() , DonationActivity.class));
    }
}