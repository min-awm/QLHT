package com.example.btl_android.hoc_phan_du_kien;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
public class HocPhanDuKienActivity extends AppCompatActivity implements HocPhanAdapter.OnItemClickListener {

    private static final int REQUEST_CODE_ADD_HOCPHAN = 1;
    private static final int REQUEST_CODE_EDIT_HOCPHAN = 2;
    private RecyclerView recyclerView;
    private HocPhanAdapter hocPhanAdapter;
    private List<HocPhan> hocPhanList;
    private DatabaseHelper databaseHelper;
    private Button selectedButton = null;
    private HocPhan selectedHocPhan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hoc_phan_du_kien);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerViewHocPhan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseHelper = new DatabaseHelper(this);

        hocPhanList = databaseHelper.getAllHocPhan();
        hocPhanAdapter = new HocPhanAdapter(hocPhanList, this);
        recyclerView.setAdapter(hocPhanAdapter);

        setupSemesterButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_hoc_phan_du_kien, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(this, ThemHocPhan.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_HOCPHAN);
            return true;
        } else if (id == R.id.action_edit) {
            if (selectedHocPhan != null) {
                Intent intent = new Intent(this, ThemHocPhan.class);
                intent.putExtra("HOC_PHAN", selectedHocPhan);
                startActivityForResult(intent, REQUEST_CODE_EDIT_HOCPHAN);
            } else {
                Toast.makeText(this, "Vui lòng chọn một học phần để sửa", Toast.LENGTH_LONG).show();
            }
            return true;
        } else if (id == R.id.action_delete) {
            if (selectedHocPhan != null) {
                databaseHelper.deleteHocPhan(selectedHocPhan.getMaHp());
                hocPhanList.remove(selectedHocPhan);
                hocPhanAdapter.notifyDataSetChanged();
                selectedHocPhan = null;
                Toast.makeText(this, "Xóa học phần thành công", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Vui lòng chọn một học phần để xóa", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupSemesterButtons() {
        int[] buttonIds = {R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8};
        for (int i = 0; i < buttonIds.length; i++) {
            Button button = findViewById(buttonIds[i]);
            int finalI = i + 1;
            button.setOnClickListener(v -> filterBySemester(finalI, button));
        }
    }

    private void filterBySemester(int semester, Button button) {
        if (selectedButton != null) {
            selectedButton.setBackground(getResources().getDrawable(R.drawable.button_default1));
        }

        selectedButton = button;
        selectedButton.setBackground(getResources().getDrawable(R.drawable.button_selected));

        hocPhanList = databaseHelper.getHocPhanByHocKy(semester);
        hocPhanAdapter = new HocPhanAdapter(hocPhanList, this);
        recyclerView.setAdapter(hocPhanAdapter);
    }

    private void updateHocPhanList() {
        if (selectedButton != null) {
            int semester = Integer.parseInt(selectedButton.getText().toString());
            hocPhanList = databaseHelper.getHocPhanByHocKy(semester);
        } else {
            hocPhanList = databaseHelper.getAllHocPhan();
        }
        hocPhanAdapter = new HocPhanAdapter(hocPhanList, this);
        recyclerView.setAdapter(hocPhanAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_ADD_HOCPHAN || requestCode == REQUEST_CODE_EDIT_HOCPHAN) {
                updateHocPhanList();
            }
        }
    }

    @Override
    public void onItemClick(HocPhan hocPhan) {
        selectedHocPhan = hocPhan;
        hocPhanAdapter.setSelectedHocPhan(hocPhan);
    }
}
