package com.nightxstudio.qrcodescannerandgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends AppCompatActivity {

    TextView resultBox;
    ImageButton share;
    ImageButton copy;
    ImageButton retake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultBox = findViewById(R.id.resultBox);
        share = findViewById(R.id.share);
        copy = findViewById(R.id.copy);
        retake = findViewById(R.id.retake);

        String passedResult = getIntent().getExtras().getString("result");

        resultBox.setText(passedResult);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain/number");
                //String shareSub = "Your subject here";
                //sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, passedResult);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", passedResult);
                clipboard.setPrimaryClip(clip);

                Toast.makeText( ResultActivity.this, "Copied To Clipboard", Toast.LENGTH_LONG).show();

            }
        });

        retake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });

        //Change Status Bar color:
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ResultActivity.this, ScanActivity.class);
        startActivity(intent);
    }
}