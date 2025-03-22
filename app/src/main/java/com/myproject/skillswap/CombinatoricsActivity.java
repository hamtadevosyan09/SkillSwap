package com.myproject.skillswap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CombinatoricsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combinatorics);

        TextView book1 = findViewById(R.id.book1_c);
        TextView book2 = findViewById(R.id.book2_c);
        TextView book3 = findViewById(R.id.book3_c);

        book1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://postypashki.ru/wp-content/uploads/2019/02/Комбинаторика.pdf";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        book2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.maths.ed.ac.uk/~v1ranick/papers/wilsongraph.pdf";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
        https://ia600800.us.archive.org/32/items/olympiad-combinatorics/Olympiad_Combinatorics.pdf
        book3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://ia600800.us.archive.org/32/items/olympiad-combinatorics/Olympiad_Combinatorics.pdf";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        ImageView backButton = findViewById(R.id.backlearn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}