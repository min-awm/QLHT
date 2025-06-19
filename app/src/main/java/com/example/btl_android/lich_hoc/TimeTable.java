package com.example.btl_android.lich_hoc;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.DatabaseHelper;
import com.example.btl_android.R;

import java.util.ArrayList;

/**
 * @noinspection ALL
 */
public class TimeTable extends AppCompatActivity {

    DatabaseHelper myDB;
    TimeTableAdapter timeTableAdapter;
    ArrayList<String> tb_mon, tb_thu, tb_ngay, tb_giangvien, tb_phong, tb_tiet, tb_diadiem;
    ArrayList<Integer> tb_id;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view before accessing any views
        setContentView(R.layout.activity_lich_hoc);
        // Enable Edge to Edge
        EdgeToEdge.enable(this);
        // Apply window insets to the main view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());


        // Initialize the DatabaseHelper
        myDB = new DatabaseHelper(TimeTable.this);
        // Initialize the ArrayLists
        tb_id = new ArrayList<>();
        tb_mon = new ArrayList<>();
        tb_thu = new ArrayList<>();
        tb_ngay = new ArrayList<>();
        tb_giangvien = new ArrayList<>();
        tb_phong = new ArrayList<>();
        tb_tiet = new ArrayList<>();
        tb_diadiem = new ArrayList<>();

        // Load data from the database
        LuuTruData();

        // Initialize the TimeTableAdapter
        timeTableAdapter = new TimeTableAdapter(TimeTable.this, tb_id, tb_mon, tb_thu, tb_ngay, tb_giangvien, tb_phong, tb_tiet, tb_diadiem);
        Log.d("TimeTableAdapter", "tb_id: " + tb_id);

        // Initialize the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(timeTableAdapter);

        // Initialize the SearchView
        searchView = findViewById(R.id.searchPerson);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search when the user presses the search button or Enter key
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search as the user types
                search(newText);
                return true;
            }
        });
    }

    private void search(String keyword) {
        Cursor cursor = myDB.searchLichHoc(keyword);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            tb_id.clear();
            tb_mon.clear();
            tb_thu.clear();
            tb_ngay.clear();
            tb_giangvien.clear();
            tb_phong.clear();
            tb_tiet.clear();
            tb_diadiem.clear();

            while (cursor.moveToNext()) {
                tb_id.add(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                tb_mon.add(cursor.getString(cursor.getColumnIndexOrThrow("tenHp"))); // Sử dụng tenHp thay vì maLop
                tb_thu.add(cursor.getString(cursor.getColumnIndexOrThrow("thu")));
                tb_ngay.add(cursor.getString(cursor.getColumnIndexOrThrow("ngay")));
                tb_giangvien.add(cursor.getString(cursor.getColumnIndexOrThrow("giangVien")));
                tb_phong.add(cursor.getString(cursor.getColumnIndexOrThrow("phong")));
                tb_tiet.add(cursor.getString(cursor.getColumnIndexOrThrow("tiet")));
                tb_diadiem.add(cursor.getString(cursor.getColumnIndexOrThrow("diaDiem")));
            }
            // After adding new data, update the RecyclerView
            timeTableAdapter.notifyDataSetChanged();
        }
        cursor.close();
    }


    void LuuTruData() {
        Cursor cursor = myDB.getLichHoc();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                tb_id.add(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                tb_mon.add(cursor.getString(cursor.getColumnIndexOrThrow("tenHp"))); // Sử dụng tenHp thay vì maLop
                tb_thu.add(cursor.getString(cursor.getColumnIndexOrThrow("thu")));
                tb_ngay.add(cursor.getString(cursor.getColumnIndexOrThrow("ngay")));
                tb_giangvien.add(cursor.getString(cursor.getColumnIndexOrThrow("giangVien")));
                tb_phong.add(cursor.getString(cursor.getColumnIndexOrThrow("phong")));
                tb_tiet.add(cursor.getString(cursor.getColumnIndexOrThrow("tiet")));
                tb_diadiem.add(cursor.getString(cursor.getColumnIndexOrThrow("diaDiem")));
            }
        }
        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timetable, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_timetable) {
            Intent intent = new Intent(this, Add_Timetable.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}