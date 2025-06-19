package com.example.btl_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.btl_android.cong_viec.CongViec;
import com.example.btl_android.dang_nhap.MiniTimeTable;
import com.example.btl_android.diem.Diem;
import com.example.btl_android.hoc_phan_du_kien.HocPhan;
import com.example.btl_android.thong_bao.ThongBao;

import java.util.ArrayList;
import java.util.List;

/**
 * @noinspection ALL
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SINHVIEN);
        db.execSQL(CREATE_TABLE_CONGVIEC);
        db.execSQL(CREATE_TABLE_CHUYENNGANH);
        db.execSQL(CREATE_TABLE_HOCPHAN);
        db.execSQL(CREATE_TABLE_LOAIHOCPHAN);
        db.execSQL(CREATE_TABLE_KETQUAHOCPHAN);
        db.execSQL(CREATE_TABLE_LICHHOC);
        db.execSQL(CREATE_TABLE_THONGBAO);

        populateInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS SinhVien");
        db.execSQL("DROP TABLE IF EXISTS CongViec");
        db.execSQL("DROP TABLE IF EXISTS ChuyenNganh");
        db.execSQL("DROP TABLE IF EXISTS HocPhan");
        db.execSQL("DROP TABLE IF EXISTS LoaiHocPhan");
        db.execSQL("DROP TABLE IF EXISTS KetQuaHocPhan");
        db.execSQL("DROP TABLE IF EXISTS LichHoc");
        db.execSQL("DROP TABLE IF EXISTS ThongBao");
        onCreate(db);
    }

    private void populateInitialData(final SQLiteDatabase db) {
        db.execSQL(INSERT_TABLE_SINHVIEN);
        db.execSQL(INSERT_TABLE_CONGVIEC);
        db.execSQL(INSERT_TABLE_CHUYENNGANH);
        db.execSQL(INSERT_TABLE_HOCPHAN);
        db.execSQL(INSERT_TABLE_LOAIHOCPHAN);
        db.execSQL(INSERT_TABLE_KETQUAHOCPHAN);
        db.execSQL(INSERT_TABLE_LICHHOC);
    }

    // CRUD operations for HocPhan
    public boolean addHocPhan(HocPhan hocPhan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maHp", hocPhan.getMaHp());
        values.put("tenHp", hocPhan.getTenHp());
        values.put("soTinChiLyThuyet", hocPhan.getSoTinChiLt());
        values.put("soTinChiThucHanh", hocPhan.getSoTinChiTh());
        values.put("soTietLyThuyet", hocPhan.getSoTietLt());
        values.put("soTietThucHanh", hocPhan.getSoTietTh());
        values.put("hocKy", hocPhan.getHocKy());
        values.put("hinhThucThi", hocPhan.getHinhThucThi());
        values.put("heSo", hocPhan.getHeSo());

        long result = db.insert("HocPhan", null, values);
        db.close();

        return result != -1;
    }

    public boolean updateHocPhan(HocPhan hocPhan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenHp", hocPhan.getTenHp());
        values.put("soTinChiLyThuyet", hocPhan.getSoTinChiLt());
        values.put("soTinChiThucHanh", hocPhan.getSoTinChiTh());
        values.put("soTietLyThuyet", hocPhan.getSoTietLt());
        values.put("soTietThucHanh", hocPhan.getSoTietTh());
        values.put("hocKy", hocPhan.getHocKy());
        values.put("hinhThucThi", hocPhan.getHinhThucThi());
        values.put("heSo", hocPhan.getHeSo());

        int result = db.update("HocPhan", values, "maHp = ?", new String[]{hocPhan.getMaHp()});
        db.close();
        return result > 0;
    }

    public void deleteHocPhan(String maHp) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("HocPhan", "maHp=?", new String[]{maHp});
        if (result == -1) {
            Log.e("DatabaseHelper", "Xóa học phần thất bại");
        } else {
            Log.i("DatabaseHelper", "Xóa học phần thành công");
        }
        db.close();
    }

    public boolean isMaHpUnique(String maHp) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM HocPhan WHERE maHp = ?", new String[]{maHp});
        boolean isUnique = !cursor.moveToFirst();
        cursor.close();
        db.close();
        return isUnique;
    }

    public List<HocPhan> getAllHocPhan() {
        List<HocPhan> hocPhanList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM HocPhan", null);

        if (cursor.moveToFirst()) {
            do {
                HocPhan hocPhan = new HocPhan(
                        cursor.getString(cursor.getColumnIndexOrThrow("maHp")),
                        cursor.getString(cursor.getColumnIndexOrThrow("tenHp")),
                        cursor.getFloat(cursor.getColumnIndexOrThrow("soTinChiLyThuyet")),
                        cursor.getFloat(cursor.getColumnIndexOrThrow("soTinChiThucHanh")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("soTietLyThuyet")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("soTietThucHanh")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("hocKy")),
                        cursor.getString(cursor.getColumnIndexOrThrow("hinhThucThi")),
                        cursor.getString(cursor.getColumnIndexOrThrow("heSo"))
                );
                hocPhanList.add(hocPhan);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return hocPhanList;
    }

    public void insertLichHoc(String mon, String thu, String ngay, String giangVien, String phong, String tiet, String diaDiem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues tb = new ContentValues();

        tb.put("mon", mon);
        tb.put("thu", thu);
        tb.put("ngay", ngay);
        tb.put("giangVien", giangVien);
        tb.put("phong", phong);
        tb.put("tiet", tiet);
        tb.put("diaDiem", diaDiem);

        long result = db.insert("LichHoc", null, tb);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Add Thanh Cong", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor getLichHoc() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT LichHoc.*, HocPhan.tenHp " +
                "FROM LichHoc " +
                "JOIN KetQuaHocPhan ON LichHoc.maLop = KetQuaHocPhan.maLop " +
                "JOIN HocPhan ON KetQuaHocPhan.maHp = HocPhan.maHp";
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public ArrayList<MiniTimeTable> getLichHocLite(String date) {
        ArrayList<MiniTimeTable> lichHocList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM LichHoc WHERE ngay = ?", new String[]{date});
        if (cursor.moveToFirst()) {
            do {
                String tenmonhoc = cursor.getString(cursor.getColumnIndex("mon"));
                String tiethoc = cursor.getString(cursor.getColumnIndex("tiet"));
                String diadiem = cursor.getString(cursor.getColumnIndex("diaDiem"));
                MiniTimeTable miniTimeTable = new MiniTimeTable(tenmonhoc, tiethoc, diadiem);

                lichHocList.add(miniTimeTable);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return lichHocList;
    }

    public Cursor searchLichHoc(String keyword) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT LichHoc.*, HocPhan.tenHp " +
                "FROM LichHoc " +
                "JOIN KetQuaHocPhan ON LichHoc.maLop = KetQuaHocPhan.maLop " +
                "JOIN HocPhan ON KetQuaHocPhan.maHp = HocPhan.maHp " +
                "WHERE HocPhan.tenHp LIKE ? OR LichHoc.phong LIKE ?";
        String[] selectionArgs = {"%" + keyword + "%", "%" + keyword + "%"};
        Cursor cursor = db.rawQuery(query, selectionArgs);
        return cursor;
    }

    public boolean updateDataTime(Context context, int row_id, String thu, String ngay, String giangVien, String phong, String tiet, String diaDiem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //   values.put("mon", mon);
        values.put("thu", thu);
        values.put("ngay", ngay);
        values.put("giangVien", giangVien);
        values.put("phong", phong);
        values.put("tiet", tiet);
        values.put("diaDiem", diaDiem);
        values.put("loaiTietHoc", 0);
        values.put("vang", 0);

        int result = db.update("LichHoc", values, "id = ?", new String[]{String.valueOf(row_id)});
        db.close();

        if (result > 0) {
            Log.d("updateDataTime", "Update Successful for row_id: " + row_id);
            Toast.makeText(context, "Cập Nhật Thành Công !!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Log.d("updateDataTime", "Update Failed for row_id: " + row_id);
            Toast.makeText(context, "Cập Nhật Không Thành Công !!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean deleteLichHoc(int row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("LichHoc", "id=?", new String[]{String.valueOf(row_id)});
        db.close();
        return result > 0;
    }

    public boolean updateDiem(Diem diem, String maSv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tx1", diem.getTx1());
        values.put("tx2", diem.getTx2());
        values.put("giuaKy", diem.getGiuaKy());
        values.put("diemKiVong", diem.getDiemKiVong());
        values.put("cuoiKy", diem.getCuoiKy());
        long res = db.update("KetQuaHocPhan", values, "maLop = ? AND maSv = ?", new String[]{diem.getMaLop(), maSv});
        db.close();
        return res > 0;
    }

    public List<HocPhan> getHocPhanByHocKy(int hocKy) {
        List<HocPhan> hocPhanList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM HocPhan WHERE hocKy = ?", new String[]{String.valueOf(hocKy)});

        if (cursor.moveToFirst()) {
            do {
                HocPhan hocPhan = new HocPhan(
                        cursor.getString(cursor.getColumnIndex("maHp")),
                        cursor.getString(cursor.getColumnIndex("tenHp")),
                        cursor.getFloat(cursor.getColumnIndex("soTinChiLyThuyet")),
                        cursor.getFloat(cursor.getColumnIndex("soTinChiThucHanh")),
                        cursor.getInt(cursor.getColumnIndex("soTietLyThuyet")),
                        cursor.getInt(cursor.getColumnIndex("soTietThucHanh")),
                        cursor.getInt(cursor.getColumnIndex("hocKy")),
                        cursor.getString(cursor.getColumnIndex("hinhThucThi")),
                        cursor.getString(cursor.getColumnIndex("heSo"))
                );
                hocPhanList.add(hocPhan);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return hocPhanList;
    }

    // Khai báo kiểu static cho biến tatCaDiemHpList, vì là biến tĩnh, giá trị sẽ duy trì thông qua các lớp.
    // Bên cạnh đó, vì sử dụng modifier public, chia sẻ biến giữa các lớp giúp tiết kiệm bộ nhớ và số lần
    // truy vấn, tù đó cải thiện thời gian phản hồi.
    // Phương thức lấy toàn bộ điểm học phần của sinh viên hiện đang đăng nhập vào ứng dụng.
    public void getDiemHp(String maSv) {
        // Khi có sự cập nhật điểm học phần, xóa rỗng biến.
        tatCaDiemHpList.clear();
        // Tạo 1 đối tượng Database chỉ đọc.
        SQLiteDatabase db = this.getReadableDatabase();
        // Viết câu truy vấn
        String query =
                // Chọn lấy các cột cần lấy dữ liệu.
                "SELECT kq.maLop, kq.maSv, hp.maHp, hp.tenHp, lhp.loai, hp.soTinChiLyThuyet, hp.soTinChiThucHanh, " +
                "hp.soTietLyThuyet, hp.soTietThucHanh, hp.hinhThucThi, hp.heSo, kq.hocKy, " +
                "kq.tx1, kq.tx2, kq.giuaKy, kq.cuoiKy, kq.diemKiVong, " +
                // Tính số tiết vắng lý thuyết và số tiết vắng thực hành.
                "SUM(CASE WHEN lh.vang = 1 AND lh.loaiTietHoc = 0 THEN 1 ELSE 0 END) AS vangLt, " +
                "SUM(CASE WHEN lh.vang = 1 AND lh.loaiTietHoc = 1 THEN 1 ELSE 0 END) AS vangTh " +
                // Thực hiện JOIN các bảng.
                "FROM KetQuaHocPhan kq " +
                // JOIN bảng SinhVien để chỉ lấy thông tin maSv và maCn.
                "JOIN SinhVien sv ON sv.maSv = kq.maSv " +
                // JOIN bảng HocPhan để lấy các thông tin liên quan tới học phần.
                "JOIN HocPhan hp ON hp.maHp = kq.maHp " +
                // JOIN bảng LoaiHocPhan để xác định học phần bắt buộc hoặc tự chọn dựa trên maSv và maCn.
                "JOIN LoaiHocPhan lhp ON lhp.maHp = hp.maHp AND lhp.maCn = sv.maCn " +
                // LEFT JOIN bảng LichHoc để xác định số tiết nghỉ của sinh viên dựa trên maLop và maSv.
                // Chọn LEFT JOIN để lấy hết các trường hợp ở bảng KetQuaHocPhan, ngay cả khi không có dữ liệu
                // tương ứng bên bảng LichHoc.
                "LEFT JOIN LichHoc lh ON lh.maLop = kq.maLop AND lh.maSv = kq.maSv " +
                // Lấy điều kiện là maSv của sinh viên đang đăng nhập.
                "WHERE kq.maSv = ?" +
                "GROUP BY kq.maLop, kq.maSv, kq.maHp, hp.tenHp, lhp.loai, hp.soTietLyThuyet, hp.soTietThucHanh, " +
                "hp.hinhThucThi, hp.heSo, kq.hocKy, kq.tx1, kq.tx2, kq.giuaKy, kq.cuoiKy, kq.diemKiVong " +
                // Sắp xếp theo tên học phần.
                "ORDER BY hp.tenHp";

        // Thực hiện truy vấn.
        Cursor cursor = db.rawQuery(query, new String[]{maSv});
        // Nếu truy vấn có kết quả trả về.
        if (cursor.moveToFirst()) {
            // Thực hiện lấy dữ liệu đã được truy vấn.
            do {
                // Tạo 1 instance Diem.
                Diem diem = new Diem();

                // Set các thuộc tính của diem.
                diem.setMaLop(cursor.getString(cursor.getColumnIndex("maLop")));
                diem.setMaHp(cursor.getString(cursor.getColumnIndex("maHp")));
                diem.setTenHp(cursor.getString(cursor.getColumnIndex("tenHp")));
                diem.setLoai(cursor.getInt(cursor.getColumnIndex("loai")));
                diem.setSoTinChiLt(cursor.getFloat(cursor.getColumnIndex("soTinChiLyThuyet")));
                diem.setSoTinChiTh(cursor.getFloat(cursor.getColumnIndex("soTinChiThucHanh")));
                diem.setSoTietLt(cursor.getInt(cursor.getColumnIndex("soTietLyThuyet")));
                diem.setSoTietTh(cursor.getInt(cursor.getColumnIndex("soTietThucHanh")));
                diem.setHinhThucThi(cursor.getString(cursor.getColumnIndex("hinhThucThi")));
                diem.setHocKy(cursor.getInt(cursor.getColumnIndex("hocKy")));
                diem.setHeSo(cursor.getString(cursor.getColumnIndex("heSo")));
                diem.setTx1(cursor.isNull(cursor.getColumnIndex("tx1")) ? null : cursor.getFloat(cursor.getColumnIndex("tx1")));
                diem.setTx2(cursor.isNull(cursor.getColumnIndex("tx2")) ? null : cursor.getFloat(cursor.getColumnIndex("tx2")));
                diem.setGiuaKy(cursor.isNull(cursor.getColumnIndex("giuaKy")) ? null : cursor.getFloat(cursor.getColumnIndex("giuaKy")));
                diem.setCuoiKy(cursor.isNull(cursor.getColumnIndex("cuoiKy")) ? null : cursor.getFloat(cursor.getColumnIndex("cuoiKy")));
                diem.setDiemKiVong(cursor.isNull(cursor.getColumnIndex("diemKiVong")) ? null : cursor.getFloat(cursor.getColumnIndex("diemKiVong")));
                diem.setVangLt(cursor.getInt(cursor.getColumnIndex("vangLt")));
                diem.setVangTh(cursor.getInt(cursor.getColumnIndex("vangTh")));

                // Thêm diem vào biến tatCaDiemHpList.
                tatCaDiemHpList.add(diem);
            } while (cursor.moveToNext());
            // Thực hiện truy vấn cho tới khi cursor trả về null, hay đã duyệt qua hết tất cả kết quả.
        }
        // Đóng cursor và db để giải phóng bộ nhớ và tài nguyên.
        cursor.close();
        db.close();
    }

    public List<Diem> getDiemHpTheoKy(String hocKyStr) {
        int hocKy = Integer.parseInt(hocKyStr);
        List<Diem> diemList = new ArrayList<>();
        for (Diem diem : tatCaDiemHpList) {
            if (diem.getHocKy() == hocKy) diemList.add(diem);
        }
        return diemList;
    }

    public boolean insertThongBao(String maSv, String tieuDe, String noiDung, String thoiGian) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maSv", maSv);
        values.put("tieuDe", tieuDe);
        values.put("noiDung", noiDung);
        values.put("thoiGian", thoiGian);
        long res = db.insert("ThongBao", null, values);
        db.close();
        return res > 0;
    }

    public List<ThongBao> getThongBao(String maSv) {
        List<ThongBao> thongBaoList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT tieuDe, noiDung, thoiGian FROM ThongBao WHERE maSv = ? ORDER BY id DESC", new String[]{maSv});

        if (cursor.moveToFirst()) {
            do {
                String tieuDe = cursor.getString(cursor.getColumnIndex("tieuDe"));
                String noiDung = cursor.getString(cursor.getColumnIndex("noiDung"));
                String thoiGian = cursor.getString(cursor.getColumnIndex("thoiGian"));
                ThongBao thongBao = new ThongBao(tieuDe, noiDung, thoiGian);
                thongBaoList.add(thongBao);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return thongBaoList;
    }

    public ArrayList<CongViec> getAllCongViec(String msv) {
        ArrayList<CongViec> congViecList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CongViec WHERE maSv = ?", new String[]{msv});
        if (cursor.moveToFirst()) {
            do {
                int macongviec = cursor.getInt(cursor.getColumnIndex("id"));
                String maSinhVien = cursor.getString(cursor.getColumnIndex("maSv"));
                String tencongviec = cursor.getString(cursor.getColumnIndex("tenViec"));
                String chitietcongviec = cursor.getString(cursor.getColumnIndex("chiTiet"));
                String mucuutien = cursor.getString(cursor.getColumnIndex("mucUuTien"));
                String thoihanngay = cursor.getString(cursor.getColumnIndex("thoiHanNgay"));
                String thoihangio = cursor.getString(cursor.getColumnIndex("thoiHanGio"));
                int trangthai = cursor.getInt(cursor.getColumnIndex("trangThai"));
                CongViec congViec = new CongViec(macongviec, maSinhVien, tencongviec, chitietcongviec, mucuutien, thoihangio, thoihanngay, trangthai);

                congViecList.add(congViec);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return congViecList;
    }
    public Cursor searchCongViecByTen(String keyword) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM CongViec WHERE tenViec LIKE ?";
        String[] selectionArgs = {"%" + keyword + "%"};
        Cursor cursor = db.rawQuery(query, selectionArgs);
        return cursor;
    }

    public boolean addCongViec(CongViec congViec) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", congViec.getMaCongViec());
        values.put("maSv", congViec.getMaSinhVien());
        values.put("tenViec", congViec.getTenCongViec());
        values.put("chiTiet", congViec.getChiTietCongViec());
        values.put("mucUuTien", congViec.getMucUuTien());
        values.put("thoiHanNgay", congViec.getThoiHanNgay());
        values.put("thoiHanGio", congViec.getThoiHanGio());
        values.put("trangThai", congViec.getTrangThai());

        db.insert("CongViec", null, values);
        db.close();
        return true;
    }

    public boolean updateCongViec(CongViec congViec) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", congViec.getMaCongViec());
        values.put("maSv", congViec.getMaSinhVien());
        values.put("tenViec", congViec.getTenCongViec());
        values.put("chiTiet", congViec.getChiTietCongViec());
        values.put("mucUuTien", congViec.getMucUuTien());
        values.put("thoiHanNgay", congViec.getThoiHanNgay());
        values.put("thoiHanGio", congViec.getThoiHanGio());
        values.put("trangThai", congViec.getTrangThai());

        // Cập nhật công việc dựa trên ID
        db.update("CongViec", values, "id" + " = ?", new String[]{String.valueOf(congViec.getMaCongViec())});
        db.close();
        return true;
    }

    public void deleteCongViec(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("CongViec", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int getMaxId() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT MAX(id) AS max_id FROM CongViec";
        Cursor cursor = db.rawQuery(query, null);

        int maxId = -1;
        if (cursor.moveToFirst()) {
            maxId = cursor.getInt(cursor.getColumnIndex("max_id"));
        }
        cursor.close();
        db.close();
        return maxId;
    }

    // Database name and version
    private static final String DATABASE_NAME = "QuanLyHocTapCaNhan.db";
    private static final int DATABASE_VERSION = 1;
    public static List<Diem> tatCaDiemHpList = new ArrayList<>();
    private Context context;
    // SinhVien table
    private static final String CREATE_TABLE_SINHVIEN =
            "CREATE TABLE IF NOT EXISTS SinhVien (" +
                    "maSv TEXT NOT NULL," +
                    "maCn INTEGER NOT NULL," +
                    "tenSv TEXT NOT NULL," +
                    "tenTk INTEGER NOT NULL UNIQUE," +
                    "matKhau TEXT NOT NULL," +
                    "PRIMARY KEY(maSv)," +
                    "FOREIGN KEY (maCn) REFERENCES ChuyenNganh(id)" +
                    " ON UPDATE NO ACTION ON DELETE NO ACTION" +
                    ");";
    // CongViec table
    private static final String CREATE_TABLE_CONGVIEC =
            "CREATE TABLE IF NOT EXISTS CongViec (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "maSv TEXT NOT NULL," +
                    "tenViec TEXT NOT NULL," +
                    "mucUuTien INTEGER," +
                    "thoiHanGio TEXT NOT NULL," +
                    "thoiHanNgay TEXT NOT NULL," +
                    "trangThai INTEGER NOT NULL," +
                    "chiTiet TEXT," +
                    "FOREIGN KEY (maSv) REFERENCES SinhVien(maSv)" +
                    " ON UPDATE NO ACTION ON DELETE NO ACTION" +
                    ");";
    // ChuyenNganh table
    private static final String CREATE_TABLE_CHUYENNGANH =
            "CREATE TABLE IF NOT EXISTS ChuyenNganh (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "tenCn TEXT NOT NULL" +
                    ");";
    // HocPhan table
    private static final String CREATE_TABLE_HOCPHAN =
            "CREATE TABLE IF NOT EXISTS HocPhan (" +
                    "maHp TEXT PRIMARY KEY," +
                    "tenHp TEXT NOT NULL," +
                    "soTinChiLyThuyet REAL NOT NULL," +
                    "soTinChiThucHanh REAL NOT NULL," +
                    "soTietLyThuyet INTEGER NOT NULL," +
                    "soTietThucHanh INTEGER NOT NULL," +
                    "hocKy INTEGER NOT NULL," +
                    "hinhThucThi TEXT NOT NULL," +
                    "heSo TEXT NOT NULL" +
                    ");";
    // LoaiHocPhan table
    private static final String CREATE_TABLE_LOAIHOCPHAN =
            "CREATE TABLE IF NOT EXISTS LoaiHocPhan (" +
                    "maHp TEXT NOT NULL," +
                    "maCn INTEGER NOT NULL," +
                    "loai INTEGER NOT NULL," +
                    "PRIMARY KEY(maHp, maCn)," +
                    "FOREIGN KEY (maHp) REFERENCES HocPhan(maHp)" +
                    " ON UPDATE NO ACTION ON DELETE NO ACTION," +
                    "FOREIGN KEY (maCn) REFERENCES ChuyenNganh(id)" +
                    " ON UPDATE NO ACTION ON DELETE NO ACTION" +
                    ");";
    // KetQuaHocPhan table
    private static final String CREATE_TABLE_KETQUAHOCPHAN =
            "CREATE TABLE IF NOT EXISTS KetQuaHocPhan (" +
                    "maLop TEXT NOT NULL," +
                    "maSv TEXT NOT NULL," +
                    "maHp TEXT NOT NULL," +
                    "tx1 REAL," +
                    "tx2 REAL," +
                    "giuaKy REAL," +
                    "cuoiKy REAL," +
                    "diemKiVong REAL," +
                    "hocKy INTEGER NOT NULL," +
                    "PRIMARY KEY(maLop, maSv)," +
                    "FOREIGN KEY (maHp) REFERENCES HocPhan(maHp)" +
                    " ON UPDATE NO ACTION ON DELETE NO ACTION," +
                    "FOREIGN KEY (maSv) REFERENCES SinhVien(maSv)" +
                    " ON UPDATE NO ACTION ON DELETE NO ACTION " +
                    ");";
    // LichHoc table
    private static final String CREATE_TABLE_LICHHOC =
            "CREATE TABLE IF NOT EXISTS LichHoc (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "maSv TEXT NOT NULL," +
                    "maLop TEXT NOT NULL," +
                    "thu TEXT NOT NULL," +
                    "ngay TEXT NOT NULL," +
                    "phong INTEGER NOT NULL," +
                    "giangVien TEXT NOT NULL," +
                    "tiet TEXT NOT NULL," +
                    "diaDiem TEXT NOT NULL," +
                    "loaiTietHoc INTEGER NOT NULL," +
                    "vang INTEGER," +
                    "FOREIGN KEY (maSv) REFERENCES SinhVien(maSv)" +
                    " ON UPDATE NO ACTION ON DELETE NO ACTION," +
                    "FOREIGN KEY(maLop) REFERENCES KetQuaHocPhan(maLop) " +
                    " ON UPDATE NO ACTION ON DELETE NO ACTION" +
                    ");";
    // ThongBao table
    private static final String CREATE_TABLE_THONGBAO =
            "CREATE TABLE IF NOT EXISTS ThongBao (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "maSv TEXT NOT NULL," +
                    "tieuDe TEXT NOT NULL," +
                    "noiDung TEXT NOT NULL," +
                    "thoiGian TEXT NOT NULL," +
                    "FOREIGN KEY (maSv) REFERENCES SinhVien(maSv)" +
                    " ON UPDATE NO ACTION ON DELETE NO ACTION" +
                    ");";
    // ThongBao table
    private static final String INSERT_TABLE_SINHVIEN =
            "INSERT INTO SinhVien (maSv, maCn, tenSv, tenTk, matKhau) VALUES " +
                    "('2021606516', 1, 'Sinh vien haui', 'sinhvienhaui', '123456')," +
                    "('2021606856', 1, 'Sinh vien haui 1', 'sinhvienhaui1', '123456')";
    private static final String INSERT_TABLE_CONGVIEC =
            "INSERT INTO CongViec (id, maSv, tenViec, mucUuTien, thoiHanGio, thoiHanNgay, trangThai, chiTiet) VALUES " +
                    "(1,'2021606856', 'Nộp báo cáo Android', 2, '8:00', '2024-06-15', 0, 'Nộp báo cáo bài tập lớn môn Android gồm các file word và video giới thiệu'), " +
                    "(2,'2021606856', 'Bảo vệ bài tập lớn Học máy', 3, '14:00', '2024-06-18', 0, 'Đi bảo vệ bài tập lớn môn Học máy ở phòng 902-A8'), " +
                    "(3,'2021606856', 'Bảo vệ bài tập lớn Kiểm thử phần mềm', 3, '14:30', '2024-06-18', 0, 'Đi bảo vệ bài tập lớn môn Android ở phòng 701-A1'), " +
                    "(4,'2021606856', 'Ôn tập Ứng dụng thuật toán', 2, '8:35', '2024-06-20', 0, 'Ôn tập trước ngày thi cuối kỳ môn Ứng dụng thuật toán'), " +
                    "(5,'2021606856', 'Thi Quản trị mạng', 3, '11:15', '2024-06-21', 0, 'Đi thi cuối kỳ môn Quản trị mạng trên hệ điều hành Windows'), " +
                    "(6,'2021606856', 'Hoàn thành bài tập lớn Web nâng cao', 2, '8:00', '2024-06-10', 1, 'Hoàn thiện bài tập lớn sau đó đi in cho buổi vệ bài tập lớn cuối kỳ môn Thiết kế web nâng cao'), " +
                    "(7,'2021606856', 'Bảo vệ bài tập lớn Web nâng cao', 3, '8:00', '2024-06-12', 1, 'Đi bảo vệ bài tập lớn cuối kỳ môn Thiết kế web nâng cao'), " +
                    "(8,'2021606856', 'Thi cuối kỳ môn Tiếng Anh 1', 3, '13:30', '2024-05-26', 1, 'Đi thi cuối kỳ môn Tiếng Anh 2 phòng 507-A9'), " +
                    "(9,'2021606856', 'Nộp báo cáo Android', 2, '8:00', '2024-06-15', 0, 'Nộp báo cáo bài tập lớn môn Android gồm các file word và video giới thiệu'), " +
                    "(10,'2021606856', 'Bảo vệ bài tập lớn Android', 3, '14:00', '2024-06-18', 0, 'Đi bảo vệ bài tập lớn môn Android ở phòng 802-A1'), " +
                    "(11,'2021606856', 'Bảo vệ bài tập lớn Kiểm thử phần mềm', 3, '14:30', '2024-06-18', 0, 'Đi bảo vệ bài tập lớn môn Android ở phòng 701-A1'), " +
                    "(12,'2021606856', 'Ôn tập Quản trị mạng', 2, '8:35', '2024-06-20', 0, 'Ôn tập trước ngày thi cuối kỳ môn Quản trị mạng trên hệ điều hành Windows'), " +
                    "(13,'2021606856', 'Thi Quản trị mạng', 3, '11:15', '2024-06-21', 0, 'Đi thi cuối kỳ môn Quản trị mạng trên hệ điều hành Windows'), " +
                    "(14,'2021606856', 'Hoàn thành bài tập lớn Web nâng cao', 2, '8:00', '2024-06-10', 1, 'Hoàn thiện bài tập lớn sau đó đi in cho buổi vệ bài tập lớn cuối kỳ môn Thiết kế web nâng cao'), " +
                    "(15,'2021606856', 'Bảo vệ bài tập lớn Web nâng cao', 3, '8:00', '2024-06-12', 1, 'Đi bảo vệ bài tập lớn cuối kỳ môn Thiết kế web nâng cao'), " +
                    "(16,'2021606856', 'Thi cuối kỳ môn Tiếng Anh 2', 3, '13:30', '2024-05-26', 1, 'Đi thi cuối kỳ môn Tiếng Anh 2 phòng 507-A9');";
    private static final String INSERT_TABLE_CHUYENNGANH =
            "INSERT INTO ChuyenNganh (id, tenCn) VALUES " +
                    "(1, 'Công nghệ thông tin'), " +
                    "(2, 'Khoa học máy tính'), " +
                    "(3, 'Hệ thống thông tin'), " +
                    "(4, 'Kỹ thuật phần mềm');";
    private static final String INSERT_TABLE_HOCPHAN =
            "INSERT INTO HocPhan (maHp, tenHp, soTinChiLyThuyet, soTinChiThucHanh, soTietLyThuyet, soTietThucHanh, hocKy, hinhThucThi, heSo) VALUES " +
                    "('LP6010', 'Triết học Mác-Lênin', 2, 0, 20, 0, 1, 'Tự luận', '20-20-60'), " +
                    "('BS6002', 'Giải tích', 3, 0, 30, 0, 1, 'Bài tập lớn', '25-25-50'), " +
                    "('BS6018', 'Giao tiếp liên văn hoá', 2, 0, 20, 0, 1, 'Bài tập lớn', '20-20-60'), " +
                    "('BS6019', 'Nhập môn nghiên cứu khoa học', 2, 0, 20, 0, 1, 'Bài tập lớn', '20-20-60'), " +
                    "('IT6011', 'Nhập môn về kỹ thuật', 2, 0, 20, 0, 1, 'Bài tập lớn', '20-20-60'), " +
                    //kỳ2
                    "('BS6001', 'Đại số tuyến tính', 3, 0, 30, 0, 1, 'Tự luận', '20-20-60'), " +
                    "('IT6016', 'Vật lý đại cương', 2, 1, 40, 30, 1, 'Tự luận', '25-25-50'), " +
                    "('IT6015', 'Kỹ thuật lập trình', 3, 1, 30, 30, 1, 'Thi trên máy tính', '20-20-60'), " +
                    "('IT6017', 'Cấu trúc dữ liệu', 3, 1, 30, 30, 1, 'Tự luận', '25-25-50'), " +
                    "('IT6018', 'Lý thuyết thông tin', 2, 1, 20, 30, 1, 'Bài tập lớn', '20-20-60'), " +
                    "('IT6019', 'Mạng máy tính', 2, 1, 20, 30, 1, 'Thi trên máy tính', '25-25-50'), " +
                    "('IT6020', 'Hệ điều hành', 3, 1, 30, 30, 1, 'Tự luận', '20-20-60'), " +
                    "('IT6021', 'Nhập môn AI', 2, 1, 20, 30, 1, 'Bài tập lớn', '25-25-50'), " +
                    "('MH2_01', 'Toán cao cấp', 3, 0, 40, 0, 2, 'Tự luận', '20-20-60'), " +
                    "('MH2_02', 'Xác suất thống kê', 3, 0, 30, 0, 2, 'Bài tập lớn', '25-25-50'), " +
                    "('MH2_03', 'Phân tích thiết kế hệ thống', 2, 1, 20, 30, 2, 'Thi trên máy tính', '20-20-60'), " +
                    "('MH2_04', 'An ninh mạng', 2, 1, 30, 30, 2, 'Tự luận', '25-25-50'), " +
                    "('MH2_05', 'Kiến trúc máy tính', 3, 1, 30, 30, 2, 'Thi trên máy tính', '20-20-60'), " +
                    "('MH2_06', 'Phát triển phần mềm', 3, 1, 30, 30, 2, 'Tự luận', '25-25-50'), " +
                    "('MH2_07', 'Trí tuệ nhân tạo nâng cao', 2, 1, 30, 30, 2, 'Bài tập lớn', '20-20-60'), " +
                    "('MH2_08', 'Khoa học dữ liệu', 2, 1, 20, 30, 2, 'Thi trên máy tính', '25-25-50'), " +
                    "('MH2_09', 'Phát triển game', 3, 1, 30, 30, 2, 'Tự luận', '20-20-60'), " +
                    "('MH2_10', 'Blockchain', 2, 1, 20, 30, 2, 'Bài tập lớn', '25-25-50'), " +
                    "('LP6012', 'Chủ nghĩa xã hội khoa học', 2, 0, 20, 0, 3, 'Tự luận', '20-20-60'), " +
                    "('IT6035', 'Toán rời rạc', 3, 0, 30, 0, 3, 'Bài tập lớn', '25-25-50'), " +
                    "('IT6126', 'Hệ thống cơ sở dữ liệu', 2, 1, 20, 30, 3, 'Thi trên máy tính', '20-20-60'), " +
                    "('IT6067', 'Kiến trúc máy tính và hệ điều hành', 2, 1, 20, 30, 3, 'Tự luận', '25-25-50'), " +
                    "('IT6120', 'Lập trình hướng đối tượng', 3, 1, 30, 30, 3, 'Thi trên máy tính', '20-20-60'), " +
                    "('LP6013', 'Lịch sử Đảng Cộng sản Việt Nam', 2, 1, 20, 30, 4, 'Tự luận', '20-20-60'), " +
                    "('IT6001', 'An toàn và bảo mật thông tin', 3, 0, 30, 0, 4, 'Bài tập lớn', '25-25-50'), " +
                    "('IT6002', 'Cấu trúc dữ liệu và giải thuật', 2, 1, 20, 30, 4, 'Thi trên máy tính', '20-20-60'), " +
                    "('LP6004', 'Tư tưởng Hồ Chí Minh', 2, 0, 20, 0, 5, 'Tự luận', '20-20-60'), " +
                    "('IT6071', 'Phát triển dự án công nghệ thông tin', 3, 0, 30, 0, 5, 'Bài tập lớn', '25-25-50'), " +
                    "('IT6100', 'Thiết kế đồ họa 2D', 2, 1, 20, 30, 5, 'Thi trên máy tính', '20-20-60'), " +
                    "('IT6047', 'Học máy', 2, 1, 20, 30, 6, 'Tự luận', '20-20-60'), " +
                    "('IT6057', 'Phát triển ứng dụng thương mại điện tử', 2, 1, 30, 30, 6, 'Bài tập lớn', '25-25-50'), " +
                    "('IT6125', 'Thiết kế web nâng cao', 2, 1, 20, 30, 6, 'Thi trên máy tính', '20-20-60'), " +
                    "('IT6122', 'Đồ án chuyên ngành', 3, 0, 30, 0, 7, 'Tự luận', '20-20-60'), " +
                    "('IT6013', 'Kiểm thử phần mềm', 2, 1, 20, 30, 7, 'Bài tập lớn', '25-25-50'), " +
                    "('IT6029', 'Phát triển ứng dụng trên thiết bị di động', 3, 1, 30, 30, 7, 'Thi trên máy tính', '20-20-60'), " +
                    "('IT6129', 'Đồ án tốt nghiệp', 4, 5, 40, 45, 8, 'Tự luận', '30-30-40'), " +
                    "('IT6128', 'Thực tập doanh nghiệp', 3, 3, 30, 45, 8, 'Bài tập lớn', '20-30-50');";
    private static final String INSERT_TABLE_LOAIHOCPHAN =
            "INSERT INTO LoaiHocPhan (maHp, maCn, loai) VALUES " +
                    "('LP6010', 1, 0), " +
                    "('BS6002', 1, 1), " +
                    "('BS6018', 1, 1), " +
                    "('BS6019', 1, 1), " +
                    "('IT6011', 1, 1), " +
                    "('BS6001', 1, 1), " +
                    "('IT6016', 1, 1), " +
                    "('IT6015', 1, 1), " +
                    "('IT6017', 1, 1), " +
                    "('IT6019', 1, 1), " +
                    "('IT6020', 1, 1), " +
                    "('IT6021', 1, 1), " +
                    "('MH2_02', 1, 0), " +
                    "('MH2_03', 1, 1), " +
                    "('MH2_04', 1, 1), " +
                    "('MH2_05', 1, 1), " +
                    "('MH2_06', 1, 1), " +
                    "('MH2_07', 1, 1), " +
                    "('MH2_08', 1, 1), " +
                    "('MH2_09', 1, 1), " +
                    "('IT6035', 1, 1), " +
                    "('IT6126', 1, 1), " +
                    "('IT6067', 1, 1), " +
                    "('IT6120', 1, 1), " +
                    "('LP6013', 1, 1), " +
                    "('IT6002', 1, 1), " +
                    "('LP6004', 1, 0), " +
                    "('IT6100', 1, 1), " +
                    "('IT6047', 1, 1), " +
                    "('IT6057', 1, 1), " +
                    "('IT6125', 1, 1), " +
                    "('IT6122', 1, 0), " +
                    "('IT6013', 1, 1), " +
                    "('IT6129', 1, 0), " +
                    "('IT6128', 1, 1), " +
                    "('LP6010', 2, 1), " +
                    "('BS6002', 2, 0), " +
                    "('BS6001', 2, 1), " +
                    "('IT6016', 2, 0), " +
                    "('IT6015', 2, 0), " +
                    "('IT6017', 2, 0), " +
                    "('IT6019', 2, 0), " +
                    "('IT6020', 2, 0), " +
                    "('IT6021', 2, 0), " +
                    "('MH2_01', 2, 1), " +
                    "('MH2_03', 2, 0), " +
                    "('MH2_04', 2, 0), " +
                    "('MH2_05', 2, 0), " +
                    "('MH2_06', 2, 0), " +
                    "('MH2_08', 2, 0), " +
                    "('MH2_09', 2, 0), " +
                    "('LP6012', 2, 1), " +
                    "('IT6035', 2, 0), " +
                    "('IT6126', 2, 0), " +
                    "('IT6067', 2, 0), " +
                    "('LP6013', 2, 0), " +
                    "('IT6001', 2, 1), " +
                    "('IT6002', 2, 0), " +
                    "('LP6004', 2, 1), " +
                    "('IT6071', 2, 1), " +
                    "('IT6100', 2, 0), " +
                    "('IT6047', 2, 0), " +
                    "('IT6057', 2, 0), " +
                    "('IT6125', 2, 0), " +
                    "('IT6122', 2, 1), " +
                    "('IT6029', 2, 0), " +
                    "('IT6129', 2, 1), " +
                    "('IT6128', 2, 0), " +
                    "('LP6010', 3, 1), " +
                    "('BS6002', 3, 1), " +
                    "('BS6001', 3, 1), " +
                    "('IT6016', 3, 0), " +
                    "('IT6015', 3, 1), " +
                    "('IT6018', 3, 0), " +
                    "('IT6019', 3, 1), " +
                    "('IT6020', 3, 1), " +
                    "('IT6021', 3, 1), " +
                    "('MH2_01', 3, 0), " +
                    "('MH2_02', 3, 0), " +
                    "('MH2_03', 3, 1), " +
                    "('MH2_05', 3, 1), " +
                    "('MH2_06', 3, 1), " +
                    "('MH2_07', 3, 1), " +
                    "('MH2_08', 3, 1), " +
                    "('MH2_09', 3, 1), " +
                    "('LP6012', 3, 0), " +
                    "('IT6035', 3, 1), " +
                    "('IT6126', 3, 1), " +
                    "('IT6067', 3, 1), " +
                    "('IT6120', 3, 1), " +
                    "('IT6001', 3, 0), " +
                    "('IT6002', 3, 1), " +
                    "('LP6004', 3, 0), " +
                    "('IT6071', 3, 0), " +
                    "('IT6100', 3, 1), " +
                    "('IT6057', 3, 1), " +
                    "('IT6125', 3, 1), " +
                    "('IT6122', 3, 0), " +
                    "('IT6013', 3, 1), " +
                    "('IT6029', 3, 1), " +
                    "('IT6129', 3, 0), " +
                    "('IT6128', 3, 1), " +
                    "('LP6010', 4, 0), " +
                    "('BS6002', 4, 1), " +
                    "('IT6016', 4, 1), " +
                    "('IT6015', 4, 1), " +
                    "('IT6017', 4, 1), " +
                    "('IT6018', 4, 0), " +
                    "('IT6019', 4, 1), " +
                    "('IT6020', 4, 1), " +
                    "('IT6021', 4, 1), " +
                    "('MH2_01', 4, 0), " +
                    "('MH2_02', 4, 0), " +
                    "('MH2_03', 4, 1), " +
                    "('MH2_04', 4, 1), " +
                    "('MH2_05', 4, 1), " +
                    "('MH2_07', 4, 1), " +
                    "('MH2_08', 4, 1), " +
                    "('MH2_09', 4, 1), " +
                    "('MH2_10', 4, 1), " +
                    "('LP6012', 4, 0), " +
                    "('IT6035', 4, 1), " +
                    "('IT6126', 4, 1), " +
                    "('IT6067', 4, 1), " +
                    "('IT6120', 4, 1), " +
                    "('IT6001', 4, 0), " +
                    "('IT6002', 4, 1), " +
                    "('LP6004', 4, 0), " +
                    "('IT6071', 4, 0), " +
                    "('IT6100', 4, 1), " +
                    "('IT6047', 4, 1), " +
                    "('IT6057', 4, 1), " +
                    "('IT6122', 4, 0), " +
                    "('IT6013', 4, 1), " +
                    "('IT6029', 4, 1), " +
                    "('IT6129', 4, 0), " +
                    "('IT6128', 4, 1)";
    private static final String INSERT_TABLE_KETQUAHOCPHAN =
            "INSERT INTO KetQuaHocPhan (maLop, maSv, maHp, tx1, tx2, giuaKy, cuoiKy, diemKiVong, hocKy) VALUES " +
                    "('2021HP003.1', '2021606856', 'IT6016', 8.5, 9.0, null, null, null, 1), " +
                    "('2021HP002.3', '2021606856', 'MH2_06', 7.5, 8.0, 6.5, null, 7.0, 1), " +
                    "('2021HP001.3', '2021606856', 'BS6002', 5, 7, null, 8.5, null, 1), " +
                    "('2021HP008.1', '2021606856', 'IT6017', 9.0, 9.5, 8.5, 9.0, 9.0, 1), " +
                    "('2021HP005.9', '2021606856', 'MH2_05', 7.5, 8.0, 7.5, 7.5, null, 2), " +
                    "('2021HP002.2', '2021606856', 'IT6015', 9.0, 9.5, 8.5, 9.0, 9.0, 2), " +
                    "('2021HP009.4', '2021606856', 'IT6018', 8.0, 8.5, 7.0, null, null, 2), " +
                    "('2021HP001.2', '2021606856', 'LP6012', 7.5, 8.0, null, 7.5, 7.5, 2), " +
                    "('2022HP005.1', '2021606856', 'BS6001', 7.5, 6.0, 7.5, 7.5, null, 3), " +
                    "('2022HP002.2', '2021606856', 'IT6015', 9.0, 3.5, 8.5, 9.0, 9.0, 3), " +
                    "('2022HP009.4', '2021606856', 'MH2_08', 8.0, 6.5, 7.0, null, null, 3), " +
                    "('2022HP001.2', '2021606856', 'BS6002', 7.5, 7.0, null, 7.5, 7.5, 3), " +
                    "('2022HP005.9', '2021606856', 'BS6001', 7.5, 8.0, 8.5, 4.5, null, 4), " +
                    "('2022HP001.1', '2021606856', 'BS6002', 9.0, 9.5, 8.5, 9.0, 9.0, 4), " +
                    "('2022HP009.7', '2021606856', 'IT6018', 8.0, 8.5, 7.0, null, null, 4), " +
                    "('2022HP001.3', '2021606856', 'BS6002', 7.5, 8.0, null, 7.5, 7.5, 4), " +
                    "('2022HP003.3', '2021606856', 'IT6016', 8.0, 8.5, null, null, null, 4), " +
                    "('2023HP005.9', '2021606856', 'BS6001', 7.5, 8.0, 7.5, 7.5, null, 5), " +
                    "('2023HP008.3', '2021606856', 'IT6017', 7.5, 8.0, null, null, 7.0, 5), " +
                    "('2023HP002.2', '2021606856', 'IT6015', 9.0, 9.5, 8.5, 9.0, 9.0, 5), " +
                    "('2023HP009.4', '2021606856', 'IT6018', 8.0, 8.5, 7.0, null, null, 5), " +
                    "('2023HP001.2', '2021606856', 'BS6002', 7.5, 8.0, null, 7.5, 7.5, 5), " +
                    "('2023HP005.1', '2021606856', 'BS6001', 7.5, 8.0, 7.5, 7.5, null, 6), " +
                    "('2023HP008.2', '2021606856', 'IT6017', 7.5, 6.0, null, null, 7.0, 6), " +
                    "('2023HP002.7', '2021606856', 'IT6015', 4.0, 9.5, 6.5, 9.0, 9.0, 6), " +
                    "('2023HP003.3', '2021606856', 'IT6016', 8.0, 8.5, 7.0, null, null, 6), " +
                    "('2023HP001.3', '2021606856', 'BS6002', 7.5, 8.0, null, 7.5, 7.5, 6), " +
                    "('2024HP008.1', '2021606856', 'IT6017', 8.5, 7.0, null, null, 8.0, 7), " +
                    "('2024HP001.2', '2021606856', 'BS6002', 8.0, 6.5, 8.5, 9.0, 9.0, 7), " +
                    "('2024HP009.4', '2021606856', 'IT6018', 8.0, 9.5, 9.0, null, null, 7), " +
                    "('2024HP001.6', '2021606856', 'BS6002', 7.5, 8.0, null, 7.5, 7.5, 7), " +
                    "('2024HP005.5', '2021606856', 'BS6001', 8.0, 8.5, null, null, null, 7), " +
                    "('2021IT6016.1', '2021606516', 'IT6016', 8.5, 9.0, null, null, null, 1), " +
                    "('2021BS6002.3', '2021606516', 'BS6002', 7.5, 8.0, 6.5, null, 7.0, 1), " +
                    "('2021IT6018.3', '2021606516', 'IT6018', 3.5, 2.5, null, 5.0, null, 1), " +
                    "('2021LP6010.1', '2021606516', 'LP6010', 9.0, 9.5, 8.5, 9.0, 9.0, 1), " +
                    "('2021IT6015.1', '2021606516', 'IT6015', 9.0, 9.5, 8.5, 9.0, 9.0, 1), " +
                    "('2021BS6001.9', '2021606516', 'BS6001', 7.5, 8.0, 7.5, 7.5, null, 2), " +
                    "('2021IT6017.2', '2021606516', 'IT6017', 9.0, 9.5, 8.5, 9.0, 9.0, 2), " +
                    "('2021MH205.4', '2021606516', 'MH2_05', 8.0, 8.5, 7.0, null, null, 2), " +
                    "('2022MH202.1', '2021606516', 'MH2_02', 7.5, 8.0, null, 7.5, 7.5, 2), " +
                    "('2022LP6013.1', '2021606516', 'LP6013', 7.5, 8.0, null, 7.5, 7.5, 2), " +
                    "('2022IT6021.1', '2021606516', 'IT6021', 7.5, 6.0, 7.5, 7.5, null, 3), " +
                    "('2022IT6020.2', '2021606516', 'IT6020', 9.0, 3.5, 8.5, 9.0, 9.0, 3), " +
                    "('2022MH201.4', '2021606516', 'MH2_01', 8.0, 6.5, 7.0, null, null, 3), " +
                    "('2022MH203.2', '2021606516', 'MH2_03', 7.5, 7.0, null, 7.5, 7.5, 3), " +
                    "('2022IT6019.2', '2021606516', 'IT6019', 7.5, 7.0, null, 7.5, 7.5, 3), " +
                    "('2022MH204.9', '2021606516', 'MH2_04', 7.5, 8.0, 8.5, 4.5, null, 4), " +
                    "('2022MH207.1', '2021606516', 'MH2_07', 9.0, 9.5, 8.5, 9.0, 9.0, 4), " +
                    "('2022MH208.7', '2021606516', 'MH2_08', 8.0, 8.5, 7.0, null, null, 4), " +
                    "('2022LP6012.3', '2021606516', 'LP6012', 7.5, 8.0, null, 7.5, 7.5, 4), " +
                    "('2022IT6035.3', '2021606516', 'IT6035', 8.0, 8.5, null, null, null, 4), " +
                    "('2023HP005.9', '2021606516', 'MH2_10', 7.5, 8.0, 7.5, 7.5, null, 5), " +
                    "('2023IT6120.3', '2021606516', 'IT6120', 7.5, 8.0, null, null, 7.0, 5), " +
                    "('2023IT6126.2', '2021606516', 'IT6126', 9.0, 9.5, 8.5, 9.0, 9.0, 5), " +
                    "('2023LP6004.4', '2021606516', 'LP6004', 8.0, 8.5, 7.0, null, null, 5), " +
                    "('2023IT6001.2', '2021606516', 'IT6001', 7.5, 8.0, null, 7.5, 7.5, 5), " +
                    "('2023IT6047.1', '2021606516', 'IT6047', 7.5, 8.0, 7.5, 7.5, null, 6), " +
                    "('2023IT6122.2', '2021606516', 'IT6122', 7.5, 6.0, null, null, 7.0, 6), " +
                    "('2023IT6029.7', '2021606516', 'IT6029', 4.0, 9.5, 6.5, 9.0, 9.0, 6), " +
                    "('2023MH210.3', '2021606516', 'MH2_10', 8.0, 8.5, 7.0, null, null, 6), " +
                    "('2023IT6002.3', '2021606516', 'IT6002', 7.5, 8.0, null, 7.5, 7.5, 6)";
    private static final String INSERT_TABLE_LICHHOC =
            "INSERT INTO LichHoc " +
                    "(id, maSv, maLop, thu, ngay, phong, giangVien, tiet, diaDiem, loaiTietHoc, vang) " +
                    "VALUES " +
                    "(1, '2021606516', '2021HP002.3', 'Thứ Sáu', '2024-06-21', 201, 'Nguyễn Văn An', '1-2', 'A1', 0, 1), " +
                    "(2, '2021606516', '2021HP003.1', 'Thứ Ba', '2024-06-25', 202, 'Trần Thị Bích', '3-4', 'A7', 0, 0), " +
                    "(3, '2021606516', '2021HP002.3', 'Thứ Tư', '2024-06-26', 203, 'Lê Văn Hùng', '5-7', 'A8', 1, 1), " +
                    "(4, '2021606516', '2021HP003.3', 'Thứ Năm', '2024-06-27', 204, 'Phạm Thị Lan', '8-9', 'A9', 0, 0), " +
                    "(5, '2021606516', '2021HP003.1', 'Thứ Sáu', '2024-06-28', 205, 'Hoàng Minh Tuấn', '10-12', 'A10', 1, 0), " +
                    "(6, '2021606516', '2021HP002.3', 'Thứ Hai', '2024-07-01', 206, 'Đặng Thị Vân', '13-14', 'A1', 0, 1), " +
                    "(7, '2021606516', '2021HP003.3', 'Thứ Ba', '2024-07-02', 207, 'Bùi Thị Hạnh', '1-3', 'A7', 1, 0), " +
                    "(8, '2021606516', '2021HP003.1', 'Thứ Tư', '2024-07-03', 208, 'Phan Thanh Tùng', '4-5', 'A8', 0, 0), " +
                    "(9, '2021606516', '2021HP002.3', 'Thứ Năm', '2024-07-04', 601, 'Ngô Thị Ngọc', '6-7', 'A9', 1, 1), " +
                    "(10, '2021606516', '2021HP003.3', 'Thứ Sáu', '2024-07-05', 602, 'Đỗ Quang Huy', '8-9', 'A10', 0, 0), " +
                    "(11, '2021606516', '2021HP003.3', 'Thứ Hai', '2024-06-24', 603, 'Lý Thị Hoa', '10-11', 'A1', 1, 0), " +
                    "(12, '2021606516', '2021HP003.1', 'Thứ Năm', '2024-06-20', 604, 'Lương Xuân Bảo', '12-13', 'A7', 1, 0), " +
                    "(13, '2021606516', '2021HP001.3', 'Thứ Sáu', '2024-07-12', 605, 'Phạm Văn Đức', '14-15', 'A8', 0, 1), " +
                    "(14, '2021606516', '2021HP004.6', 'Thứ Ba', '2024-07-16', 606, 'Trần Văn Nam', '1-2', 'A9', 0, 0), " +
                    "(15, '2021606516', '2021HP005.9', 'Thứ Tư', '2024-07-17', 607, 'Nguyễn Thị Hồng', '3-4', 'A10', 1, 1), " +
                    "(16, '2021606516', '2021HP006.1', 'Thứ Năm', '2024-07-18', 201, 'Nguyễn Văn Bình', '5-6', 'A1', 0, 0), " +
                    "(17, '2021606516', '2021HP007.3', 'Thứ Sáu', '2024-07-19', 202, 'Lê Thị Dung', '7-8', 'A7', 1, 0), " +
                    "(18, '2021606516', '2021HP002.2', 'Thứ Hai', '2024-07-22', 203, 'Vũ Văn Cường', '9-10', 'A8', 0, 1), " +
                    "(19, '2021606516', '2021HP009.4', 'Thứ Ba', '2024-07-23', 204, 'Trịnh Thị Mai', '11-12', 'A9', 1, 0), " +
                    "(20, '2021606516', '2021HP001.2', 'Thứ Tư', '2024-07-24', 205, 'Hoàng Văn Tài', '13-14', 'A10', 0, 0), " +
                    "(21, '2021606516', '2021HP004.5', 'Thứ Năm', '2024-07-25', 206, 'Phạm Thị Lệ', '15', 'A1', 1, 1), " +
                    "(22, '2021606516', '2021HP002.3', 'Thứ Sáu', '2024-07-26', 207, 'Nguyễn Văn Khoa', '1-2', 'A7', 0, 0), " +
                    "(23, '2021606516', '2021HP005.9', 'Thứ Hai', '2024-07-29', 208, 'Phan Văn Bình', '3-4', 'A8', 1, 0), " +
                    "(24, '2021606856', '2021HP004.1', 'Thứ Hai', '2024-07-29', 201, 'Nguyễn Thị Mai', '1-2', 'A1', 0, 1), " +
                    "(25, '2021606856', '2021HP004.2', 'Thứ Ba', '2024-07-30', 202, 'Trần Văn Bình', '3-5', 'A7', 1, 0), " +
                    "(26, '2021606856', '2021HP004.3', 'Thứ Tư', '2024-07-31', 203, 'Lê Thị Hằng', '6-7', 'A8', 0, 1), " +
                    "(27, '2021606856', '2021HP004.4', 'Thứ Năm', '2024-08-01', 204, 'Phạm Văn Khánh', '8-10', 'A9', 1, 0), " +
                    "(28, '2021606856', '2021HP004.5', 'Thứ Sáu', '2024-08-02', 205, 'Hoàng Thị Thu', '11-12', 'A10', 0, 1), " +
                    "(29, '2021606856', '2021HP004.6', 'Thứ Bảy', '2024-08-03', 206, 'Vũ Văn Nam', '13-15', 'A1', 1, 0), " +
                    "(30, '2021606856', '2021HP004.7', 'Chủ Nhật', '2024-08-04', 207, 'Nguyễn Thị Ngọc', '1-3', 'A7', 0, 1), " +
                    "(31, '2021606856', '2021HP004.8', 'Thứ Hai', '2024-08-05', 208, 'Trần Văn Cường', '4-6', 'A8', 1, 0), " +
                    "(32, '2021606856', '2021HP004.9', 'Thứ Ba', '2024-08-06', 601, 'Lê Thị Tuyết', '7-9', 'A9', 0, 1), " +
                    "(33, '2021606856', '2021HP005.1', 'Thứ Tư', '2024-08-07', 602, 'Phạm Văn Hòa', '10-12', 'A10', 1, 0), " +
                    "(34, '2021606856', '2021HP005.2', 'Thứ Năm', '2024-08-08', 603, 'Hoàng Thị Lan', '13-15', 'A1', 0, 1), " +
                    "(35, '2021606856', '2021HP005.3', 'Thứ Sáu', '2024-08-09', 604, 'Vũ Văn Hải', '1-3', 'A7', 1, 0), " +
                    "(36, '2021606856', '2021HP005.4', 'Thứ Bảy', '2024-08-10', 605, 'Nguyễn Thị Thanh', '4-6', 'A8', 0, 1), " +
                    "(37, '2021606856', '2021HP005.5', 'Chủ Nhật', '2024-08-11', 606, 'Trần Văn Thịnh', '7-9', 'A9', 1, 0), " +
                    "(38, '2021606856', '2021HP005.6', 'Thứ Hai', '2024-08-12', 607, 'Lê Thị Minh', '10-12', 'A10', 0, 1)";
}
