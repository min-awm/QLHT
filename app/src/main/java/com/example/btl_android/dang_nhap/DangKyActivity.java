package com.example.btl_android.dang_nhap;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.DatabaseHelper;
import com.example.btl_android.R;

import java.util.ArrayList;

/**
 * @noinspection ALL
 */
public class DangKyActivity extends AppCompatActivity {
    Button btnDangKy;
    Spinner spChuyenNganh;
    ArrayList<String> chuyenNganhList = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    EditText etTenTk, etMatKhau, etHoTen, etMaSv;
    SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        this.spChuyenNganh = this.findViewById(R.id.spChuyenNganh);
        btnDangKy = this.findViewById(R.id.btnDangKy);
        etTenTk = this.findViewById(R.id.etTenTk);
        etMatKhau = this.findViewById(R.id.etMatKhau);
        etHoTen = this.findViewById(R.id.etHoTen);
        etMaSv = this.findViewById(R.id.etMaSv);
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenTk = etTenTk.getText().toString().trim();
                String matKhau = etMatKhau.getText().toString().trim();
                String hoTen = etHoTen.getText().toString().trim();
                String maSv = etMaSv.getText().toString().trim();
                int chuyenNganh = spChuyenNganh.getSelectedItemPosition() + 1;

                if (tenTk.isEmpty() || matKhau.isEmpty() || hoTen.isEmpty() || maSv.isEmpty()) {
                    Toast.makeText(DangKyActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor cursor = db.rawQuery("SELECT maSv, tenTk FROM SinhVien WHERE maSv = ? OR tenTk = ?", new String[]{maSv, tenTk});
                if (cursor.getCount() > 0) {
                    Toast.makeText(DangKyActivity.this, "Đã tồn tại tài khoản với tên tài khoản hoặc MSV này", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues values = new ContentValues();
                values.put("maSv", maSv);
                values.put("maCn", chuyenNganh);
                values.put("tenSv", hoTen);
                values.put("tenTk", tenTk);
                values.put("matKhau", matKhau);

                long newRowId = db.insert("SinhVien", null, values);
                if (newRowId != -1) {
                    Toast.makeText(DangKyActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);
                    startActivity(intent);
                }
            }
        });
        this.SPINNER_CHUYENNGANH();
    }

    public void SPINNER_CHUYENNGANH() {
        this.chuyenNganhList.add("Công nghệ thông tin");
        this.chuyenNganhList.add("Khoa học máy tính");
        this.chuyenNganhList.add("Hệ thống thông tin");
        this.chuyenNganhList.add("Kỹ thuật phần mềm");

        this.adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                this.chuyenNganhList);
        this.spChuyenNganh.setAdapter(this.adapter);

        this.spChuyenNganh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int i, final long l) {

            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });
    }
}
