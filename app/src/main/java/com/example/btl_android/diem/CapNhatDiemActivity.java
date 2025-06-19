package com.example.btl_android.diem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_android.DatabaseHelper;
import com.example.btl_android.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @noinspection ALL
 */
public class CapNhatDiemActivity extends AppCompatActivity {

    private ImageButton btnQuayLai;
    private Button btnCapNhat;
    private EditText etTx1, etTx2, etGiuaKy, etKiVong, etCuoiKy;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cap_nhat_diem);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.capNhatDiemActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWidget();
        settupButtons();
    }

    private void getWidget() {
        btnQuayLai = findViewById(R.id.imgQuayLai);
        btnCapNhat = findViewById(R.id.btnCapNhat);
        etTx1 = findViewById(R.id.etTx1);
        etTx2 = findViewById(R.id.etTx2);
        etGiuaKy = findViewById(R.id.etGiuaKy);
        etKiVong = findViewById(R.id.etKiVong);
        etCuoiKy = findViewById(R.id.etCuoiKy);
        db = new DatabaseHelper(this);
    }

    private void settupButtons() {
        btnQuayLai.setOnClickListener(v -> finish());
        Intent intent = getIntent();
        Diem diem = (Diem) intent.getSerializableExtra("Diem");
        if (diem.getHeSo().length() == 8) etGiuaKy.setEnabled(false);

        etTx1.setText(diem.getTx1() == null ? "." : diem.getTx1().toString());
        etTx2.setText(diem.getTx2() == null ? "." : diem.getTx2().toString());
        etGiuaKy.setText(diem.getGiuaKy() == null ? "." : diem.getGiuaKy().toString());
        etKiVong.setText(diem.getDiemKiVong() == null ? "." : diem.getDiemKiVong().toString());
        etCuoiKy.setText(diem.getCuoiKy() == null ? "." : diem.getCuoiKy().toString());

        btnCapNhat.setOnClickListener(v -> {
            String tx1 = etTx1.getText().toString();
            String tx2 = etTx2.getText().toString();
            String giuaKy = etGiuaKy.getText().toString();
            String kiVong = etKiVong.getText().toString();
            String cuoiKy = etCuoiKy.getText().toString();

            if (!isValid(tx1)) {
                Toast.makeText(this, "Điểm thường xuyên 1 không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isValid(tx2)) {
                Toast.makeText(this, "Điểm thường xuyên 2 không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isValid(giuaKy)) {
                Toast.makeText(this, "Điểm giữa kỳ không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isValid(kiVong)) {
                Toast.makeText(this, "Điểm kỳ vọng không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isValid(cuoiKy)) {
                Toast.makeText(this, "Điểm cuối kỳ không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            diem.setTx1(tx1.equals(".") ? null : Float.parseFloat(tx1));
            diem.setTx2(tx2.equals(".") ? null : Float.parseFloat(tx2));
            diem.setGiuaKy(giuaKy.equals(".") ? null : Float.parseFloat(giuaKy));
            diem.setDiemKiVong(kiVong.equals(".") ? null : Float.parseFloat(kiVong));
            diem.setCuoiKy(cuoiKy.equals(".") ? null : Float.parseFloat(cuoiKy));

            boolean res = db.updateDiem(diem, intent.getStringExtra("MaSv"));
            if (res) {
                db.getDiemHp(intent.getStringExtra("MaSv"));
                String tieuDe = "Bạn đã cập nhật điểm học phần " + diem.getTenHp() + " thành công!";

                StringBuilder builder = new StringBuilder();
                builder.append(diem.getTx1() == null ? "Bạn đã xóa điểm TX1\n" : "Bạn đã cập nhật điểm TX1 thành " + diem.getTx1() + "\n");
                builder.append(diem.getTx2() == null ? "Bạn đã xóa điểm TX2\n" : "Bạn đã cập nhật điểm TX2 thành " + diem.getTx2() + "\n");
                builder.append(diem.getGiuaKy() == null ? "Bạn đã xóa điểm Giữa kỳ\n" : "Bạn đã cập nhật điểm Giữa kỳ thành " + diem.getGiuaKy() + "\n");
                builder.append(diem.getDiemKiVong() == null ? "Bạn đã xóa Điểm kì vọng\n" : "Bạn đã cập nhật điểm Điểm kì vọng thành " + diem.getDiemKiVong() + "\n");
                builder.append(diem.getCuoiKy() == null ? "Bạn đã xóa điểm Cuối kỳ\n" : "Bạn đã cập nhật điểm Cuối kỳ thành " + diem.getCuoiKy());
                String noiDung = builder.toString();

                String thoiGian = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy"));
                db.insertThongBao(intent.getStringExtra("MaSv"), tieuDe, noiDung, thoiGian);

                setResult(1, null);

                Toast.makeText(this, "Cập nhật điểm thành công", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private boolean isValid(String diemStr) {
        if (diemStr.equals(".")) return true;
        if (diemStr.isEmpty()) return false;
        try {
            Float diem = Float.parseFloat(diemStr);
            if (0.0f <= diem && diem <= 10.0f) return true;
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}