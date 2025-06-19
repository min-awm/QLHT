package com.example.btl_android.hoc_phan_du_kien;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.DatabaseHelper;
import com.example.btl_android.R;

/**
 * @noinspection ALL
 */
public class ThemHocPhan extends AppCompatActivity {

    private EditText maHpEditText, tenHpEditText, soTinChiLyThuyetEditText, soTinChiThucHanhEditText, hocKyEditText, hinhThucThiEditText, heSoEditText;
    private Button buttonCancel, buttonSubmit;
    private EditText soTietLyThuyetEditText, soTietThucHanhEditText;
    private DatabaseHelper databaseHelper;
    private HocPhan hocPhan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_hoc_phan);

        databaseHelper = new DatabaseHelper(this);

        maHpEditText = findViewById(R.id.maHpEditText);
        tenHpEditText = findViewById(R.id.tenHpEditText);
        soTinChiLyThuyetEditText = findViewById(R.id.soTinChiLyThuyetEditText);
        soTinChiThucHanhEditText = findViewById(R.id.soTinChiThucHanhEditText);
        hocKyEditText = findViewById(R.id.hocKyEditText);
        hinhThucThiEditText = findViewById(R.id.hinhThucThiEditText);
        soTietLyThuyetEditText = findViewById(R.id.soTietLyThuyetEditText);
        soTietThucHanhEditText = findViewById(R.id.soTietThucHanhEditText);
        heSoEditText = findViewById(R.id.heSoEditText);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonCancel.setOnClickListener(v -> finish());

        buttonSubmit.setOnClickListener(v -> submitForm());

        hocPhan = (HocPhan) getIntent().getSerializableExtra("HOC_PHAN");
        if (hocPhan != null) {
            loadHocPhanData();
            maHpEditText.setEnabled(false); // Disable editing Ma Hoc Phan
        }
    }

    private void loadHocPhanData() {
        maHpEditText.setText(hocPhan.getMaHp());
        tenHpEditText.setText(hocPhan.getTenHp());
        soTinChiLyThuyetEditText.setText(String.valueOf(hocPhan.getSoTinChiLt()));
        soTinChiThucHanhEditText.setText(String.valueOf(hocPhan.getSoTinChiTh()));
        soTietLyThuyetEditText.setText(String.valueOf(hocPhan.getSoTietLt()));
        soTietThucHanhEditText.setText(String.valueOf(hocPhan.getSoTietTh()));
        hocKyEditText.setText(String.valueOf(hocPhan.getHocKy()));
        hinhThucThiEditText.setText(hocPhan.getHinhThucThi());
        heSoEditText.setText(hocPhan.getHeSo());
    }

    private void submitForm() {
        String maHp = maHpEditText.getText().toString();
        String tenHp = tenHpEditText.getText().toString();
        String soTinChiLt = soTinChiLyThuyetEditText.getText().toString();
        String soTinChiTh = soTinChiThucHanhEditText.getText().toString();
        String soTietLt = soTietLyThuyetEditText.getText().toString();
        String soTietTh = soTietThucHanhEditText.getText().toString();
        String hocKy = hocKyEditText.getText().toString();
        String hinhThucThi = hinhThucThiEditText.getText().toString();
        String heSo = heSoEditText.getText().toString();

        if (maHp.isEmpty() || tenHp.isEmpty() || soTinChiLt.isEmpty() || soTinChiTh.isEmpty() ||
                soTietLt.isEmpty() || soTietTh.isEmpty() || hocKy.isEmpty() || hinhThucThi.isEmpty() || heSo.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_LONG).show();
            return;
        }

        int hocKyInt = Integer.parseInt(hocKy);
        if (hocKyInt < 1 || hocKyInt > 8) {
            Toast.makeText(this, "Học kỳ phải nằm trong khoảng từ 1 đến 8", Toast.LENGTH_LONG).show();
            return;
        }

        if (!heSo.matches("\\d+-\\d+-\\d+")) {
            Toast.makeText(this, "Hệ số phải theo dạng x-y-z (x, y, z là các số nguyên dương)", Toast.LENGTH_LONG).show();
            return;
        }

        boolean result;
        if (hocPhan == null) {
            hocPhan = new HocPhan(maHp, tenHp, Float.parseFloat(soTinChiLt), Float.parseFloat(soTinChiTh),
                    Integer.parseInt(soTietLt), Integer.parseInt(soTietTh), hocKyInt,
                    hinhThucThi, heSo);
            result = databaseHelper.addHocPhan(hocPhan);
        } else {
            hocPhan.setTenHp(tenHp);
            hocPhan.setSoTinChiLt(Float.parseFloat(soTinChiLt));
            hocPhan.setSoTinChiTh(Float.parseFloat(soTinChiTh));
            hocPhan.setSoTietLt(Integer.parseInt(soTietLt));
            hocPhan.setSoTietTh(Integer.parseInt(soTietTh));
            hocPhan.setHocKy(hocKyInt);
            hocPhan.setHinhThucThi(hinhThucThi);
            hocPhan.setHeSo(heSo);
            result = databaseHelper.updateHocPhan(hocPhan);
        }

        if (result) {
            Toast.makeText(this, hocPhan == null ? "Thực hiện thành công" : "Thực hiện thành công", Toast.LENGTH_LONG).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, hocPhan == null ? "Có lỗi xảy ra" : "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
        }
    }
}
