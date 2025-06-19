package com.example.btl_android.diem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.DatabaseHelper;
import com.example.btl_android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @noinspection ALL
 */
public class DiemActivity extends AppCompatActivity implements OnItemClickListener {
    private DatabaseHelper db;
    private ImageButton btnQuayLai, btnThongKe;
    private LinearLayout btnHocKySet;
    private Button lastSelectHocKy;
    private RecyclerView rvDiemHp;
    private List<Diem> diemList;
    private DiemAdapter diemHpAdapter;
    private String hocKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_diem);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWidget();
        setupButtons();
    }

    private void getWidget() {
        btnQuayLai = findViewById(R.id.imgQuayLai);
        btnThongKe = findViewById(R.id.imgMenu);
        rvDiemHp = findViewById(R.id.rvDiemHp);
        diemList = new ArrayList<>();
        hocKy = "1";

        db = new DatabaseHelper(this);
        db.getDiemHp(getIntent().getStringExtra("MaSv"));
    }

    private void setupButtons() {
        btnQuayLai.setOnClickListener(v -> finish());

        btnThongKe.setOnClickListener(v -> {
            final Intent intent = new Intent(DiemActivity.this, ThongKeActivity.class);
            DiemActivity.this.startActivity(intent);
        });

        btnHocKySet = findViewById(R.id.btnHocKySet);
        btnHocKySet.getChildAt(0).setBackgroundResource(R.drawable.button_selected);
        lastSelectHocKy = (Button) btnHocKySet.getChildAt(0);
        setButtonHocKySet(btnHocKySet);

        rvDiemHp.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setButtonHocKySet(LinearLayout btnHocKySet) {
        for (int i = 0; i < btnHocKySet.getChildCount(); i++) {
            View view = btnHocKySet.getChildAt(i);
            if (view instanceof Button) {
                Button button = (Button) view;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        button.setBackgroundResource(R.drawable.button_selected);
                        if (lastSelectHocKy != null && lastSelectHocKy != button) {
                            lastSelectHocKy.setBackgroundResource(R.drawable.button_default1);
                        }
                        lastSelectHocKy = button;

                        hocKy = button.getText().toString();
                        hocKy = String.valueOf(hocKy.charAt(hocKy.length() - 1));
                        diemList = db.getDiemHpTheoKy(hocKy);
                        if (diemList.isEmpty()) {
                            Toast.makeText(DiemActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                        }
                        diemHpAdapter = new DiemAdapter(diemList, DiemActivity.this, getIntent());
                        rvDiemHp.setAdapter(diemHpAdapter);
                    }
                });
            }
        }
    }

    @Override
    protected void onResume() {
        diemList = db.getDiemHpTheoKy(hocKy);
        diemHpAdapter = new DiemAdapter(diemList, DiemActivity.this, getIntent());
        rvDiemHp.setAdapter(diemHpAdapter);
        super.onResume();
    }

    @Override
    public void onItemClick(int pos) {
        Diem diem = diemList.get(pos);
        Intent intent = new Intent(DiemActivity.this, DiemChiTietActivity.class);
        intent.putExtra("DiemChiTiet", diem);
        DiemActivity.this.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            setResult(1, null);
        }
    }
}