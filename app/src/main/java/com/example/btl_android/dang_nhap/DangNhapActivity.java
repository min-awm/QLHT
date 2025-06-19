package com.example.btl_android.dang_nhap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.DatabaseHelper;
import com.example.btl_android.R;
import com.example.btl_android.diem.Diem;

import java.util.ArrayList;
import java.util.List;

/**
 * @noinspection ALL
 */
public class DangNhapActivity extends AppCompatActivity {

    List<Diem> diemList;
    SQLiteDatabase db;
    private Button btnDangNhap, btnDangKy;
    private EditText etTenTk, etMatKhau;
    private CheckBox cbLuuThongTin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        this.setContentView(R.layout.activity_dang_nhap);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        btnDangNhap = this.findViewById(R.id.btnDangNhap);
        btnDangKy = this.findViewById(R.id.btnDangKy);
        etTenTk = this.findViewById(R.id.editTenTk);
        etMatKhau = this.findViewById(R.id.editPassword);
        cbLuuThongTin = findViewById(R.id.chkLuuThongTin);
        diemList = new ArrayList<>();

        loadCredentials();

        Intent intentDangKy = new Intent(this, DangKyActivity.class);
        btnDangKy.setOnClickListener(v -> DangNhapActivity.this.startActivity(intentDangKy));
        DANG_NHAP();
    }

    private void DANG_NHAP() {
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etTenTk.length() == 0 || etMatKhau.length() == 0) {
                    Toast.makeText(DangNhapActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    String username = etTenTk.getText().toString().trim();
                    String password = etMatKhau.getText().toString().trim();
                    SinhVien sinhVien = CheckLogin(username, password);
                    if (sinhVien != null) {
                        if (cbLuuThongTin.isChecked()) {
                            saveCredentials(username, password);
                        } else {
                            clearCredentials();
                        }
                        Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                        Intent trangChuIntent = new Intent(DangNhapActivity.this, TrangChuActivity.class);
                        trangChuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        trangChuIntent.putExtra("MaSv", sinhVien.getMaSV());
                        trangChuIntent.putExtra("MaCn", sinhVien.getMaChuyenNganh());
                        trangChuIntent.putExtra("TenSv", sinhVien.getTenSV());
                        trangChuIntent.putExtra("TenTk", sinhVien.getTenTK());
                        trangChuIntent.putExtra("MatKhau", sinhVien.getMatKhau());
                        startActivity(trangChuIntent);
                        finish();
                    } else {
                        Toast.makeText(DangNhapActivity.this, "Tài khoản hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private SinhVien CheckLogin(String username, String password) {
        String query = "SELECT * FROM SinhVien WHERE tenTk = ? AND matKhau = ?";
        String[] selectionArgs = {username, password};

        // Thực hiện truy vấn
        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()) {
            // Truy cập dữ liệu từ Cursor
            String maSv = cursor.getString(cursor.getColumnIndex("maSv"));
            int maCn = cursor.getInt(cursor.getColumnIndex("maCn"));
            String tenSv = cursor.getString(cursor.getColumnIndex("tenSv"));
            String tenTk = cursor.getString(cursor.getColumnIndex("tenTk"));
            String matKhau = cursor.getString(cursor.getColumnIndex("matKhau"));

            SinhVien user = new SinhVien(maSv, maCn, tenSv, tenTk, matKhau);
            cursor.close();
            return user;
        }
        // Đóng Cursor
        cursor.close();
        return null;
    }

    private void loadCredentials() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        boolean rememberMe = sharedPreferences.getBoolean("remember_me", false);

        if (rememberMe) {
            etTenTk.setText(username);
            etMatKhau.setText(password);
            cbLuuThongTin.setChecked(true);
        }
    }

    private void saveCredentials(String username, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putBoolean("remember_me", true);
        editor.apply();
    }

    private void clearCredentials() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.remove("password");
        editor.remove("remember_me");
        editor.apply();
    }
}