package com.nightxstudio.qrcodescannerandgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.FormatException;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.IOException;

public class ImportActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    Button import_image;

    private AdView mAdViewTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        import_image = findViewById(R.id.import_image);

        import_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        });


        //  AD COMPONENTS STARTS HERE:
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        //1. BANNER AD AT THE TOP:
        AdView adViewTop = new AdView(this);
        adViewTop.setAdSize(AdSize.BANNER);
        adViewTop.setAdUnitId("ca-app-pub-1785962158595876/2886114951");

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

        //2. NATIVE AD AT THE BOTTOM:
        //MobileAds.initialize(this);
        AdLoader adLoader = new AdLoader.Builder(this, "ca-app-pub-1785962158595876/4646886749")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        NativeTemplateStyle styles = new NativeTemplateStyle.Builder().build();
                        TemplateView template = findViewById(R.id.my_template);
                        template.setStyles(styles);
                        template.setNativeAd(nativeAd);
                    }
                })
                .build();
        adLoader.loadAds(new AdRequest.Builder().build(), 3);


        //Change Status Bar color:
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                decodeQRCode(bitmap);
            } catch (IOException e) {
                Toast.makeText(this, "Error loading QR Code image", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void decodeQRCode(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            Result result = reader.decode(binaryBitmap);
            //String text = result.getText();

            Intent intent = new Intent(ImportActivity.this, ResultActivity.class);
            intent.putExtra("result", result.getText()); // getText() SHOULD NOT be static!!!
            startActivity(intent);

            // Get instance of Vibrator from current Context
            Vibrator vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 200 milliseconds
            vibrate.vibrate(200);
        }
        catch (NotFoundException | ChecksumException | com.google.zxing.FormatException e) {
            Toast.makeText(this, "Error decoding QR Code", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ImportActivity.this , ScanMenuActivity.class));
    }
}