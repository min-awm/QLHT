package com.example.btl_android.lich_hoc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_android.DatabaseHelper;
import com.example.btl_android.R;

/**
 * @noinspection ALL
 */
public class Add_Timetable extends AppCompatActivity {


    Toolbar toolbar;
    private EditText mon, thu, ngay, giangvien, phong, tiet, diadiem;
    private Button btnAdd, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_timetable);

        mon = findViewById(R.id.mon);
        thu = findViewById(R.id.thu);
        ngay = findViewById(R.id.ngay);
        giangvien = findViewById(R.id.giangvien);
        phong = findViewById(R.id.phong);
        tiet = findViewById(R.id.tiet);
        diadiem = findViewById(R.id.diadiem);
        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        String tab = "";
        for (int i = 0; i < 15; i++) {
            tab += "\t";
        }
        actionBar.setTitle(tab + "Thêm Lịch Học");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areFieldsEmpty()) {
                    Toast.makeText(Add_Timetable.this, "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseHelper myDB = new DatabaseHelper(Add_Timetable.this);
                    myDB.insertLichHoc(
                            mon.getText().toString().trim(),
                            thu.getText().toString().trim(),
                            ngay.getText().toString().trim(),
                            giangvien.getText().toString().trim(),
                            phong.getText().toString().trim(),
                            tiet.getText().toString().trim(),
                            diadiem.getText().toString().trim()
                    );
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private boolean areFieldsEmpty() {
        return mon.getText().toString().trim().isEmpty() ||
                thu.getText().toString().trim().isEmpty() ||
                ngay.getText().toString().trim().isEmpty() ||
                giangvien.getText().toString().trim().isEmpty() ||
                phong.getText().toString().trim().isEmpty() ||
                tiet.getText().toString().trim().isEmpty() ||
                diadiem.getText().toString().trim().isEmpty();
    }


}
