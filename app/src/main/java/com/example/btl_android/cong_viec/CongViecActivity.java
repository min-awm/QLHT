package com.example.btl_android.cong_viec;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_android.DatabaseHelper;
import com.example.btl_android.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * @noinspection ALL
 */
public class CongViecActivity extends AppCompatActivity {

    ListView lvCongViec;
    ImageButton btnMenu;
    ArrayList<CongViec> congViecList = new ArrayList<>();
    CongViecAdapter cvAdapter;
    int selectedItemPosition;
    Spinner spMucUuTien;
    ArrayList<String> mucUuTienList = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ImageView back;
    String maSv;
    EditText etThoiHanGio, etThoiHanNgay;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cong_viec);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnMenu = findViewById(R.id.btn_menu);
        back = findViewById(R.id.btn_backtrangchu);
        dbHelper = new DatabaseHelper(this);
        maSv = getIntent().getStringExtra("MaSv");
        congViecList = dbHelper.getAllCongViec(maSv);
        softCongViecList(congViecList);
        showlvCongViec();
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddEditDialog(null, 0);
            }
        });
        back.setOnClickListener(v -> {
            setResult(1, null);
            finish();
        });
        mucUuTienList.add("Không quan trọng");
        mucUuTienList.add("Quan trọng");
        mucUuTienList.add("Rất quan trọng");

        SearchView searchCv = findViewById(R.id.searchCv);
        searchCv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Khi người dùng nhấn Enter hoặc tìm kiếm
                searchCongViec(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Khi văn bản thay đổi trong ô tìm kiếm
                searchCongViec(newText);
                return false;
            }
        });
    }

    void showlvCongViec() {
        Context x = this;
        lvCongViec = findViewById(R.id.lvcongviec);

        cvAdapter = new CongViecAdapter(x, R.layout.customlv_cong_viec, congViecList);
        lvCongViec.setAdapter(cvAdapter);
        registerForContextMenu(lvCongViec);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_cong_viec, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.congviec_edit:
                showAddEditDialog(congViecList.get(selectedItemPosition), selectedItemPosition);
                return true;
            case R.id.congviec_delete:
                // Xử lý sự kiện xóa
                new AlertDialog.Builder(this).setTitle("Xác nhận").setMessage("Bạn có chắc chắn muốn xóa công việc này không?").setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(CongViecActivity.this, "Đã xóa công việc", Toast.LENGTH_SHORT).show();
                        dbHelper.deleteCongViec(congViecList.get(selectedItemPosition).maCongViec);
                        congViecList.remove(congViecList.get(selectedItemPosition));
                        cvAdapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("Hủy", null).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void setSelectedItemPosition(int position) {
        selectedItemPosition = position;
    }

    public void softCongViecList(ArrayList<CongViec> congViecList) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        ArrayList<CongViec> trangThai1List = new ArrayList<>();
        ArrayList<CongViec> trangThai0List = new ArrayList<>();

        for (CongViec cv : congViecList) {
            if (cv.trangThai == 1) {
                trangThai1List.add(cv);
            } else {
                trangThai0List.add(cv);
            }
        }

        // Sắp xếp danh sách công việc có trạng thái 0 theo thời gian và mức ưu tiên
        for (int i = 0; i < trangThai0List.size() - 1; i++) {
            for (int j = i + 1; j < trangThai0List.size(); j++) {
                CongViec cv1 = trangThai0List.get(i);
                CongViec cv2 = trangThai0List.get(j);
                Date date1 = null;
                Date date2 = null;
                try {
                    date1 = dateFormat.parse(cv1.thoiHanNgay + " " + cv1.thoiHanGio);
                    date2 = dateFormat.parse(cv2.thoiHanNgay + " " + cv2.thoiHanGio);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date1 != null && date2 != null) {
                    if (date1.after(date2) || (date1.equals(date2) && Integer.parseInt(cv1.mucUuTien) < Integer.parseInt(cv2.mucUuTien))) {
                        trangThai0List.set(i, cv2);
                        trangThai0List.set(j, cv1);
                    }
                }
            }
        }

        // Không xóa hết danh sách congViecList, chỉ thêm các danh sách đã phân loại
        congViecList.clear();
        congViecList.addAll(trangThai0List);
        congViecList.addAll(trangThai1List);
    }


    private void showAddEditDialog(final CongViec congViec, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.diaglog_congviec, null);
        builder.setView(dialogView);
        spMucUuTien = dialogView.findViewById(R.id.spn_mucuutien);
        spinner();
        final TextView texttitle = dialogView.findViewById(R.id.dialog_title);
        final EditText etTenCongViec = dialogView.findViewById(R.id.edt_tenviec);
        final EditText etChiTietCongViec = dialogView.findViewById(R.id.edt_chitiet);
        etThoiHanGio = dialogView.findViewById(R.id.edt_hangio);
        etThoiHanNgay = dialogView.findViewById(R.id.edt_hanngay);
        final Button daypick = dialogView.findViewById(R.id.btn_daypick);
        final Button timepick = dialogView.findViewById(R.id.btn_timepick);
        daypick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        timepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        if (congViec != null) {
            texttitle.setText("Sửa công việc");
            etTenCongViec.setText(congViec.getTenCongViec());
            etChiTietCongViec.setText(congViec.getChiTietCongViec());
            spMucUuTien.setSelection(Integer.parseInt(congViec.mucUuTien) - 1);
            etThoiHanGio.setText(congViec.getThoiHanGio());
            etThoiHanNgay.setText(congViec.getThoiHanNgay());
        } else {
            texttitle.setText("Thêm công việc");
        }

        builder.setPositiveButton(congViec == null ? "Thêm" : "Lưu", null); // Không làm gì khi bấm nút "Thêm"/"Lưu"
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        // Khi bấm nút "Thêm" hoặc "Lưu", kiểm tra và xử lý dữ liệu
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenCongViec = etTenCongViec.getText().toString();
                String chiTietCongViec = etChiTietCongViec.getText().toString();
                int mucUuTien = spMucUuTien.getSelectedItemPosition() + 1;
                String thoiHanGio = etThoiHanGio.getText().toString();
                String thoiHanNgay = etThoiHanNgay.getText().toString();

                // Kiểm tra tên công việc
                if (tenCongViec.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập tên công việc!", Toast.LENGTH_SHORT).show();
                    return; // Dừng lại và không đóng dialog
                }

                // Kiểm tra thời gian (Giờ)
                if (thoiHanGio.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập thời gian (giờ)!", Toast.LENGTH_SHORT).show();
                    return; // Dừng lại và không đóng dialog
                }

                // Kiểm tra thời gian (Ngày)
                if (thoiHanNgay.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập thời gian (ngày)!", Toast.LENGTH_SHORT).show();
                    return; // Dừng lại và không đóng dialog
                }

                // Kiểm tra các trường khác nếu cần thiết
                if (chiTietCongViec.isEmpty() || mucUuTien == 0) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin chi tiết công việc!", Toast.LENGTH_SHORT).show();
                    return; // Dừng lại và không đóng dialog
                }

                // Nếu mọi thứ hợp lệ, tiếp tục thêm hoặc sửa công việc
                if (congViec == null) {
                    CongViec x = new CongViec(dbHelper.getMaxId() + 1, maSv, tenCongViec, chiTietCongViec, mucUuTien + "", thoiHanGio, thoiHanNgay, 0);
                    boolean isAdded = dbHelper.addCongViec(x); // Thêm công việc
                    if (isAdded) {
                        congViecList.add(0, x);
                        softCongViecList(congViecList);
                        Toast.makeText(getApplicationContext(), "Thêm công việc thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Thêm công việc thất bại!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    congViec.setTenCongViec(tenCongViec);
                    congViec.setChiTietCongViec(chiTietCongViec);
                    congViec.setMucUuTien(mucUuTien + "");
                    congViec.setThoiHanGio(thoiHanGio);
                    congViec.setThoiHanNgay(thoiHanNgay);
                    dbHelper.updateCongViec(congViec);
                    congViecList.set(position, congViec);
                    boolean isUpdated = dbHelper.updateCongViec(congViec); // Sửa công việc
                    if (isUpdated) {
                        congViecList.set(position, congViec);
                        softCongViecList(congViecList);
                        Toast.makeText(getApplicationContext(), "Cập nhật công việc thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Cập nhật công việc thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }

                // Khi thông tin hợp lệ, đóng dialog
                dialog.dismiss();
                cvAdapter.notifyDataSetChanged();
            }
        });

    }

    void savetrangthai(CongViec congViec, boolean b) {
        int stt = congViecList.indexOf(congViec);
        if (b) {
            congViec.trangThai = 1;
            Toast.makeText(this, "Hoàn thành " + congViec.tenCongViec, Toast.LENGTH_SHORT).show();
        } else {
            congViec.trangThai = 0;
        }
        dbHelper.updateCongViec(congViec);
        congViecList.set(stt, congViec);
        softCongViecList(congViecList);
        cvAdapter.notifyDataSetChanged();
    }

    private void spinner() {
        this.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, this.mucUuTienList);
        this.spMucUuTien.setAdapter(this.adapter);

        this.spMucUuTien.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int i, final long l) {

            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });
    }

    private void showDatePickerDialog() {
        // Lấy ngày hiện tại
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
                // Cập nhật TextView với ngày được chọn
                String selectedDate = year + "-" + (month + 1) + "-" + day;
                etThoiHanNgay.setText(selectedDate);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        // Lấy giờ hiện tại
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Tạo TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(android.widget.TimePicker timePicker, int hourOfDay, int minute) {
                // Cập nhật TextView với giờ được chọn
                String selectedTime = hourOfDay + ":" + String.format("%02d", minute);
                etThoiHanGio.setText(selectedTime);
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private void searchCongViec(String keyword) {
        Cursor cursor = dbHelper.searchCongViecByTen(keyword); // Gọi phương thức tìm kiếm
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            // Làm sạch dữ liệu cũ trong danh sách
            congViecList.clear();
            while (cursor.moveToNext()) {
                int macongviec = cursor.getInt(cursor.getColumnIndex("id"));
                String maSinhVien = cursor.getString(cursor.getColumnIndex("maSv"));
                String tv_tenviec = cursor.getString(cursor.getColumnIndex("tenViec"));
                String chitietcongviec = cursor.getString(cursor.getColumnIndex("chiTiet"));
                String mucuutien = cursor.getString(cursor.getColumnIndex("mucUuTien"));
                String thoihanngay = cursor.getString(cursor.getColumnIndex("thoiHanNgay"));
                String thoihangio = cursor.getString(cursor.getColumnIndex("thoiHanGio"));
                int trangthai = cursor.getInt(cursor.getColumnIndex("trangThai"));

                // Thêm công việc vào danh sách
                CongViec congViec = new CongViec(macongviec, maSinhVien, tv_tenviec, chitietcongviec, mucuutien, thoihangio, thoihanngay, trangthai);
                congViecList.add(congViec);
            }
            cursor.close(); // Đừng quên đóng cursor sau khi sử dụng

            // Cập nhật RecyclerView hoặc ListView
            cvAdapter.notifyDataSetChanged();
        }
    }
}