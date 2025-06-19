package com.example.btl_android.lich_hoc;
 
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
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
public class Update_Timetable extends AppCompatActivity {
    EditText thu_input, ngay_input, giangvien_input, phong_input, tiet_input, diadiem_input;
    TextView mon_input;
    Button btnUpdate, btnXoa;
    String mon, thu, ngay, giangvien, phong, tiet, diadiem;
    int id;
    Toolbar toolbar;
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_timetable); // Chuyển setContentView lên đầu
        id = getIntent().getIntExtra("id", 0);
        // Initialize views
        mon_input = findViewById(R.id.mon2);
        thu_input = findViewById(R.id.thu2);
        ngay_input = findViewById(R.id.ngay2);
        giangvien_input = findViewById(R.id.giangvien2);
        phong_input = findViewById(R.id.phong2);
        tiet_input = findViewById(R.id.tiet2);
        diadiem_input = findViewById(R.id.diadiem2);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnXoa = findViewById(R.id.btnXoa);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        String tab = "";
        for (int i = 0; i < 8; i++) {
            tab += "\t";
        }
        actionBar.setTitle(tab + "Cập Nhật Lịch Học");
        myDB = new DatabaseHelper(Update_Timetable.this);
        getAndSetIntentData();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cập nhật các giá trị từ các trường đầu vào

                // mon = mon_input.getText().toString().trim();
                thu = thu_input.getText().toString().trim();
                ngay = ngay_input.getText().toString().trim();
                giangvien = giangvien_input.getText().toString().trim();
                phong = phong_input.getText().toString().trim();
                tiet = tiet_input.getText().toString().trim();
                diadiem = diadiem_input.getText().toString().trim();

                boolean isUpdated = myDB.updateDataTime(Update_Timetable.this, id, thu, ngay, giangvien, phong, tiet, diadiem);
                if (isUpdated) {
                    Intent intent = new Intent(Update_Timetable.this, TimeTable.class);
                    startActivity(intent);
                } else {
                    Log.d("MainActivity", "Update Failed for row_id: " + id);
                }
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    void getAndSetIntentData() {
        if (getIntent().hasExtra("mon") &&
            getIntent().hasExtra("id") &&
            getIntent().hasExtra("thu") &&
            getIntent().hasExtra("ngay") &&
            getIntent().hasExtra("giangvien") &&
            getIntent().hasExtra("phong") &&
            getIntent().hasExtra("tiet") &&
            getIntent().hasExtra("diadiem")) {

            id = getIntent().getIntExtra("id", 0);
            mon = getIntent().getStringExtra("mon");
            thu = getIntent().getStringExtra("thu");
            ngay = getIntent().getStringExtra("ngay");
            giangvien = getIntent().getStringExtra("giangvien");
            phong = getIntent().getStringExtra("phong");
            tiet = getIntent().getStringExtra("tiet");
            diadiem = getIntent().getStringExtra("diadiem");

            mon_input.setText(mon);
            thu_input.setText(thu);
            ngay_input.setText(ngay);
            giangvien_input.setText(giangvien);
            phong_input.setText(phong);
            tiet_input.setText(tiet);
            diadiem_input.setText(diadiem);
        } else {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(" Xóa Môn " + mon + " ? ");
        builder.setMessage(" Bạn có muốn xóa môn " + mon + " ? ");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean isDeleted = myDB.deleteLichHoc(id);
                if (isDeleted) {
                    // Xóa thành công, thực hiện các hành động phù hợp ở đây
                    Toast.makeText(Update_Timetable.this, "Dữ Liệu Đã Được Xóa Thành Công !", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Update_Timetable.this, TimeTable.class);
                    startActivity(intent);
                } else {
                    // Không thể xóa hoặc không tìm thấy hàng để xóa
                    Toast.makeText(Update_Timetable.this, "Không Tìm Thấy Dữ Liệu Để Xóa !", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

}