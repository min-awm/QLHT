package com.example.btl_android.thong_bao;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.DatabaseHelper;
import com.example.btl_android.R;

import java.util.List;

/**
 * @noinspection ALL
 */
public class ThongBaoActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private ImageView btnQuayLai;
    private RecyclerView rvThongBao;
    private List<ThongBao> thongBaoList;
    private ThongBaoAdapter thongBaoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thong_bao);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new DatabaseHelper(this);
        btnQuayLai = findViewById(R.id.imgQuayLai);
        rvThongBao = findViewById(R.id.rvThongBao);
        rvThongBao.setLayoutManager(new LinearLayoutManager(this));

        thongBaoList = db.getThongBao(getIntent().getStringExtra("MaSv"));
        thongBaoAdapter = new ThongBaoAdapter(thongBaoList);
        rvThongBao.setAdapter(thongBaoAdapter);

        setResult(1, null);

        btnQuayLai.setOnClickListener(v -> finish());
    }
}