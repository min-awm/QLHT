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

    // Database name and version
    private static final String DATABASE_NAME = "QuanLyHocTapCaNhan.db";
    private static final int DATABASE_VERSION = 1;
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
                    "mon TEXT NOT NULL," +
                    "thu TEXT NOT NULL," +
                    "ngay TEXT NOT NULL," +
                    "phong TEXT NOT NULL," +
                    "giangVien TEXT NOT NULL," +
                    "tiet TEXT NOT NULL," +
                    "diaDiem TEXT NOT NULL" +
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
                    "('2021600847', 1, 'Sinh vien haui', 'sinhvienhaui', '123456');";
    private static final String INSERT_TABLE_CONGVIEC =
            "INSERT INTO CongViec (id, maSv, tenViec, mucUuTien, thoiHanGio, thoiHanNgay, trangThai, chiTiet) VALUES " +
                    "(1,'2021600847', 'Nộp báo cáo phát triển ứng dụng trên thiết bị di động', 2, '8:00', '2025-06-23', 0, 'Nộp báo cáo bài tập lớn môn phát triển ứng dụng trên thiết bị di động gồm các file word'), " +
                    "(2,'2021600847', 'Bảo vệ bài tập phát triển ứng dụng trên thiết bị di động', 3, '12:30', '2025-06-23', 0, 'Đi bảo vệ bài tập lớn môn phát triển ứng dụng trên thiết bị di động ở phòng 1305-A1');";
    private static final String INSERT_TABLE_CHUYENNGANH =
            "INSERT INTO ChuyenNganh (id, tenCn) VALUES " +
                    "(1, 'Mạng máy tính và truyền thông dữ liệu');";
    private static final String INSERT_TABLE_HOCPHAN =
            "INSERT INTO HocPhan (maHp, tenHp, soTinChiLyThuyet, soTinChiThucHanh, soTietLyThuyet, soTietThucHanh, hocKy, hinhThucThi, heSo) VALUES " +
                    // Học kỳ 1 (Bắt buộc)
                    "('LP6010', 'Triết học Mác-Lênin', 3, 0, 30, 0, 1, 'Tự luận', '20-20-60'), " +
                    "('BS6018', 'Giao tiếp liên văn hóa', 2, 0, 20, 0, 1, 'Bài tập lớn', '20-20-60'), " +
                    "('BM6091', 'Quản lý dự án', 2, 0, 20, 0, 1, 'Bài tập lớn', '20-20-60'), " +
                    "('BS6001', 'Đại số tuyến tính', 3, 0, 30, 0, 1, 'Tự luận', '20-20-60'), " +
                    "('BS6002', 'Giải tích', 3, 0, 30, 0, 1, 'Bài tập lớn', '25-25-50'), " +
                    "('DC6004', 'Đường lối QP&AN của ĐCS Việt Nam', 3, 0, 37, 8, 1, 'Tự luận', '20-20-60'), " +
                    "('DC6005', 'Công tác quốc phòng và an ninh', 2, 0, 22, 8, 1, 'Tự luận', '20-20-60'), " +
                    "('DC6006', 'Quân sự chung', 1, 0.5, 14, 16, 1, 'Thực hành', '30-30-40'), " +
                    "('DC6007', 'Kỹ thuật chiến đấu bộ binh và chiến thuật', 0, 2, 4, 56, 1, 'Thực hành', '30-30-40'), " +
                    "('FE6077', 'Nhập môn mạng và truyền thông', 2, 0, 20, 0, 1, 'Thi trên máy tính', '25-25-50'), " +

                    // Học kỳ 1 (Tự chọn)
                    "('FL6130', 'Tiếng Anh Điện-Điện tử cơ bản 1', 2.67, 0, 0, 0, 1, 'Tự luận', '20-20-60'), " +
                    "('FL6130OT', 'Ôn tập Tiếng Anh Điện-Điện tử cơ bản 1', 1, 0, 0, 0, 1, 'Tự luận', '20-20-60'), " +
                    "('FL6282', 'Tiếng Trung cơ bản 1', 2.67, 0, 0, 0, 1, 'Tự luận', '20-20-60'), " +
                    "('FL6287', 'Tiếng Hàn cơ bản 1', 2.67, 0, 0, 0, 1, 'Tự luận', '20-20-60'), " +
                    "('FL6292', 'Tiếng Nhật cơ bản 1', 2.67, 0, 0, 0, 1, 'Tự luận', '20-20-60'), " +
                    "('PE6001', 'Aerobic 1', 0, 1, 0, 0, 1, 'Thực hành', '30-30-40'), " +
                    "('PE6003', 'Bóng chuyền 1', 0, 1, 0, 0, 1, 'Thực hành', '30-30-40'), " +
                    "('PE6011', 'Karate 1', 0, 1, 0, 0, 1, 'Thực hành', '30-30-40'), " +
                    "('PE6013', 'Khiêu vũ 1', 0, 1, 0, 0, 1, 'Thực hành', '30-30-40'), " +
                    "('PE6015', 'Pencak Silat 1', 0, 1, 0, 0, 1, 'Thực hành', '30-30-40'), " +
                    "('PE6017', 'Bóng bàn 1', 0, 1, 0, 0, 1, 'Thực hành', '30-30-40'), " +
                    "('PE6021', 'Bóng rổ 1', 0, 1, 0, 0, 1, 'Thực hành', '30-30-40'), " +
                    "('PE6025', 'Cầu lông 1', 0, 1, 0, 0, 1, 'Thực hành', '30-30-40'), " +
                    "('PE6019', 'Tennis 1', 0, 1, 0, 0, 1, 'Thực hành', '30-30-40'), " +
                    "('PE6027', 'Bóng đá 1', 0, 1, 0, 0, 1, 'Thực hành', '30-30-40'), " +
                    "('PE6029', 'Đá cầu 1', 0, 1, 0, 0, 1, 'Thực hành', '30-30-40'), " +
                    "('PE6031', 'Cầu mây 1', 0, 1, 0, 0, 1, 'Thực hành', '30-30-40'), " +
                    "('PE6033', 'Bóng ném 1', 0, 1, 0, 0, 1, 'Thực hành', '30-30-40'), " +

                    // Học kỳ 2 (Bắt buộc)
                    "('LP6011', 'Kinh tế chính trị Mác-Lênin', 2, 0, 20, 0, 2, 'Thi trên máy tính', '20-20-60'), " +
                    "('BS6006', 'Vật lý 1', 3, 1, 30, 25, 2, 'Tự luận', '25-25-50'), " +
                    "('BS6008', 'Xác suất thống kê', 3, 0, 30, 0, 2, 'Bài tập lớn', '25-25-50'), " +
                    "('IT6035', 'Toán rời rạc', 3, 0, 30, 0, 2, 'Tự luận', '20-20-60'), " +
                    "('FE6047', 'Kỹ thuật lập trình nhúng', 2, 0, 20, 0, 2, 'Bài tập lớn', '25-25-50'), " +

                    // Học kỳ 2 (Tự chọn)
                    "('FL6131', 'Tiếng Anh Điện-Điện tử cơ bản 2', 2.67, 0, 0, 0, 2, 'Tự luận', '20-20-60'), " +
                    "('FL6283', 'Tiếng Trung cơ bản 2', 2.67, 0, 0, 0, 2, 'Tự luận', '20-20-60'), " +
                    "('FL6288', 'Tiếng Hàn cơ bản 2', 2.67, 0, 0, 0, 2, 'Tự luận', '20-20-60'), " +
                    "('FL6293', 'Tiếng Nhật cơ bản 2', 2.67, 0, 0, 0, 2, 'Tự luận', '20-20-60'), " +
                    "('BS6023', 'Nghệ thuật học đại cương', 2, 0, 0, 0, 2, 'Tự luận', '20-20-60'), " +
                    "('BS6024', 'Mỹ thuật đại cương', 2, 0, 0, 0, 2, 'Tự luận', '20-20-60'), " +
                    "('PE6018', 'Bóng bàn 2', 0, 1, 0, 0, 2, 'Thực hành', '30-30-40'), " +
                    "('PE6022', 'Bóng rổ 2', 0, 1, 0, 0, 2, 'Thực hành', '30-30-40'), " +
                    "('PE6020', 'Tennis 2', 0, 1, 0, 0, 2, 'Thực hành', '30-30-40'), " +
                    "('PE6034', 'Bóng ném 2', 0, 1, 0, 0, 2, 'Thực hành', '30-30-40'), " +

                    // Học kỳ 3 (Bắt buộc)
                    "('FE6014', 'Kỹ thuật điện tử', 2, 1, 30, 30, 3, 'Tự luận', '25-25-50'), " +
                    "('FE6032', 'Tín hiệu và hệ thống', 3, 0, 30, 0, 3, 'Tự luận', '25-25-50'), " +
                    "('FE6049', 'Xử lý tín hiệu số', 2, 1, 20, 30, 3, 'Thi trên máy tính', '25-25-50'), " +
                    "('FE6078', 'Thiết kế mạch số', 2, 0, 20, 0, 3, 'Bài tập lớn', '20-20-60'), " +
                    "('IT6067', 'Kiến trúc máy tính và hệ điều hành', 3, 0, 30, 0, 3, 'Thi trên máy tính', '20-20-60'), " +

                    // Học kỳ 3 (Tự chọn)
                    "('FL6132', 'Tiếng Anh Điện-Điện tử cơ bản 3', 2.67, 0, 0, 0, 3, 'Tự luận', '20-20-60'), " +
                    "('FL6284', 'Tiếng Trung cơ bản 3', 2.67, 0, 0, 0, 3, 'Tự luận', '20-20-60'), " +
                    "('FL6289', 'Tiếng Hàn cơ bản 3', 2.67, 0, 0, 0, 3, 'Tự luận', '20-20-60'), " +
                    "('FL6294', 'Tiếng Nhật cơ bản 3', 2.67, 0, 0, 0, 3, 'Tự luận', '20-20-60'), " +
                    "('BS6003', 'Phương pháp tính', 3, 0, 30, 0, 3, 'Tự luận', '20-20-60'), " +
                    "('BS6004', 'Toán kỹ thuật', 2, 1, 20, 30, 3, 'Bài tập lớn', '25-25-50'), " +

                    // Học kỳ 4 (Bắt buộc)
                    "('LP6012', 'Chủ nghĩa xã hội khoa học', 2, 0, 20, 0, 4, 'Tự luận', '20-20-60'), " +
                    "('LP6013', 'Lịch sử Đảng Cộng sản Việt Nam', 2, 0, 20, 0, 4, 'Bài tập lớn', '25-25-50'), " +
                    "('FE6044', 'Vi xử lý và cấu trúc máy tính', 2, 1, 20, 30, 4, 'Thi trên máy tính', '20-20-60'), " +
                    "('FE6048', 'Truyền thông số', 3, 1, 30, 30, 4, 'Tự luận', '25-25-50'), " +
                    "('FE6079', 'Mạng máy tính và truyền thông', 2, 1, 20, 30, 4, 'Bài tập lớn', '20-20-60'), " +
                    "('FE6082', 'Phân tích và thiết kế hệ thống truyền thông', 2, 1, 20, 30, 4, 'Thi trên máy tính', '25-25-50'), " +

                    // Học kỳ 4 (Tự chọn)
                    "('FL6133', 'Tiếng Anh Điện-Điện tử cơ bản 4', 2.67, 0, 0, 0, 4, 'Tự luận', '20-20-60'), " +
                    "('FL6285', 'Tiếng Trung cơ bản 4', 2.67, 0, 0, 0, 4, 'Tự luận', '20-20-60'), " +
                    "('FL6290', 'Tiếng Hàn cơ bản 4', 2.67, 0, 0, 0, 4, 'Tự luận', '20-20-60'), " +
                    "('FL6295', 'Tiếng Nhật cơ bản 4', 2.67, 0, 0, 0, 4, 'Tự luận', '20-20-60'), " +

                    // Học kỳ 5 (Bắt buộc)
                    "('IT6018', 'Lập trình hướng đối tượng', 2, 1, 20, 30, 5, 'Thi trên máy tính', '20-20-60'), " +
                    "('FE6081', 'Nhập môn IoT', 2, 0, 20, 0, 5, 'Tự luận', '20-20-60'), " +
                    "('FE6083', 'Đồ án cơ sở ngành', 0, 0, 0, 0, 5, 'Bài tập lớn', '100'), " +
                    "('FE6089', 'Chuyển mạch và định tuyến', 2, 1, 20, 30, 5, 'Thi trên máy tính', '25-25-50'), " +

                    // Học kỳ 5 (Tự chọn)
                    "('FL6335', 'Tiếng Hàn 1', 5, 0, 0, 0, 5, 'Tự luận', '20-20-60'), " +
                    "('FL6337', 'Tiếng Nhật 1', 5, 0, 0, 0, 5, 'Tự luận', '20-20-60'), " +
                    "('FL6339', 'Tiếng Trung 1', 5, 0, 0, 0, 5, 'Tự luận', '20-20-60'), " +
                    "('FL6357', 'Tiếng Anh Điện-Điện tử 1', 5, 0, 0, 0, 5, 'Tự luận', '20-20-60'), " +
                    "('FE6007', 'Đo lường điều khiển bằng máy tính', 2, 1, 20, 30, 5, 'Bài tập lớn', '25-25-50'), " +
                    "('FE6051', 'Lập trình Python', 2, 1, 20, 30, 5, 'Thi trên máy tính', '20-20-60'), " +
                    "('FE6074', 'Học máy và nhận dạng', 2, 1, 20, 30, 5, 'Bài tập lớn', '25-25-50'), " +
                    "('FE6085', 'Lý thuyết anten và truyền sóng', 2, 1, 20, 30, 5, 'Tự luận', '20-20-60'), " +

                    // Học kỳ 6 (Bắt buộc)
                    "('LP6004', 'Tư tưởng Hồ Chí Minh', 2, 0, 20, 0, 6, 'Tự luận', '20-20-60'), " +
                    "('FE6080', 'Quản trị hệ thống trên nền tảng windows', 2, 1, 20, 30, 6, 'Bài tập lớn', '25-25-50'), " +
                    "('FE6072', 'Lập trình mạng và truyền thông', 2, 1, 20, 30, 6, 'Thi trên máy tính', '20-20-60'), " +
                    "('FE6093', 'Thiết kế ứng dụng IoT', 2, 1, 20, 30, 6, 'Bài tập lớn', '25-25-50'), " +

                    // Học kỳ 6 (Tự chọn)
                    "('FL6336', 'Tiếng Hàn 2', 5, 0, 0, 0, 6, 'Tự luận', '20-20-60'), " +
                    "('FL6338', 'Tiếng Nhật 2', 5, 0, 0, 0, 6, 'Tự luận', '20-20-60'), " +
                    "('FL6340', 'Tiếng Trung 2', 5, 0, 0, 0, 6, 'Tự luận', '20-20-60'), " +
                    "('FL6358', 'Tiếng Anh Điện-Điện tử 2', 5, 0, 0, 0, 6, 'Tự luận', '20-20-60'), " +
                    "('FE6070', 'Thiết kế ứng dụng trên thiết bị di động', 2, 0, 20, 0, 6, 'Bài tập lớn', '25-25-50'), " +
                    "('FE6095', 'Nguyên tắc cơ bản của điện toán đám mây', 2, 1, 20, 30, 6, 'Thi trên máy tính', '20-20-60'), " +
                    "('FE6097', 'Lập trình Web', 2, 1, 20, 30, 6, 'Bài tập lớn', '25-25-50'), " +

                    // Học kỳ 7 (Bắt buộc)
                    "('LP6003', 'Pháp luật đại cương', 2, 0, 20, 0, 7, 'Tự luận', '20-20-60'), " +
                    "('FE6088', 'An toàn mạng máy tính và truyền thông', 2, 1, 20, 30, 7, 'Thi trên máy tính', '25-25-50'), " +
                    "('FE6090', 'Hệ thống nhúng cho truyền thông dữ liệu', 2, 1, 20, 30, 7, 'Bài tập lớn', '20-20-60'), " +
                    "('FE6091', 'Kiểm thử hệ thống mạng và truyền thông', 2, 2, 20, 60, 7, 'Thực hành', '30-30-40'), " +
                    "('FE6092', 'Quản trị hệ thống trên nền tảng Linux', 2, 1, 20, 30, 7, 'Bài tập lớn', '25-25-50'), " +
                    "('FE6094', 'Đồ án chuyên ngành', 0, 0, 0, 0, 7, 'Bài tập lớn', '100'), " +

                    // Học kỳ 8 (Bắt buộc)
                    "('FE6101', 'Thực tập doanh nghiệp', 0, 0, 0, 0, 8, 'Thực hành', '0-0-100'), " +
                    "('FE6102', 'Đồ án tốt nghiệp', 0, 0, 0, 0, 8, 'Bài tập lớn', '0-0-100');";
    private static final String INSERT_TABLE_LOAIHOCPHAN =
            "INSERT INTO LoaiHocPhan (maHp, maCn, loai) VALUES " +
                    "('LP6010', 1, 1), " +
                    "('BS6018', 1, 1), " +
                    "('BM6091', 1, 1), " +
                    "('BS6001', 1, 1), " +
                    "('BS6002', 1, 1), " +
                    "('DC6004', 1, 1), " +
                    "('DC6005', 1, 1), " +
                    "('DC6006', 1, 1), " +
                    "('DC6007', 1, 1), " +
                    "('FE6077', 1, 1), " +
                    "('FL6130', 1, 0), " +
                    "('FL6130OT', 1, 0), " +
                    "('FL6282', 1, 0), " +
                    "('FL6287', 1, 0), " +
                    "('FL6292', 1, 0), " +
                    "('PE6001', 1, 0), " +
                    "('PE6003', 1, 0), " +
                    "('PE6011', 1, 0), " +
                    "('PE6013', 1, 0), " +
                    "('PE6015', 1, 0), " +
                    "('PE6017', 1, 0), " +
                    "('PE6021', 1, 0), " +
                    "('PE6025', 1, 0), " +
                    "('PE6019', 1, 0), " +
                    "('PE6027', 1, 0), " +
                    "('PE6029', 1, 0), " +
                    "('PE6031', 1, 0), " +
                    "('PE6033', 1, 0), " +
                    "('LP6011', 1, 1), " +
                    "('BS6006', 1, 1), " +
                    "('BS6008', 1, 1), " +
                    "('IT6035', 1, 1), " +
                    "('FE6047', 1, 1), " +
                    "('FL6131', 1, 0), " +
                    "('FL6283', 1, 0), " +
                    "('FL6288', 1, 0), " +
                    "('FL6293', 1, 0), " +
                    "('BS6023', 1, 0), " +
                    "('BS6024', 1, 0), " +
                    "('PE6018', 1, 0), " +
                    "('PE6022', 1, 0), " +
                    "('PE6020', 1, 0), " +
                    "('PE6034', 1, 0), " +
                    "('FE6014', 1, 1), " +
                    "('FE6032', 1, 1), " +
                    "('FE6049', 1, 1), " +
                    "('FE6078', 1, 1), " +
                    "('IT6067', 1, 1), " +
                    "('FL6132', 1, 0), " +
                    "('FL6284', 1, 0), " +
                    "('FL6289', 1, 0), " +
                    "('FL6294', 1, 0), " +
                    "('BS6003', 1, 0), " +
                    "('BS6004', 1, 0), " +
                    "('LP6012', 1, 1), " +
                    "('LP6013', 1, 1), " +
                    "('FE6044', 1, 1), " +
                    "('FE6048', 1, 1), " +
                    "('FE6079', 1, 1), " +
                    "('FE6082', 1, 1), " +
                    "('FL6133', 1, 0), " +
                    "('FL6285', 1, 0), " +
                    "('FL6290', 1, 0), " +
                    "('FL6295', 1, 0), " +
                    "('IT6018', 1, 1), " +
                    "('FE6081', 1, 1), " +
                    "('FE6083', 1, 1), " +
                    "('FE6089', 1, 1), " +
                    "('FL6335', 1, 0), " +
                    "('FL6337', 1, 0), " +
                    "('FL6339', 1, 0), " +
                    "('FL6357', 1, 0), " +
                    "('FE6007', 1, 0), " +
                    "('FE6051', 1, 0), " +
                    "('FE6074', 1, 0), " +
                    "('FE6085', 1, 0), " +
                    "('LP6004', 1, 1), " +
                    "('FE6080', 1, 1), " +
                    "('FE6072', 1, 1), " +
                    "('FE6093', 1, 1), " +
                    "('FL6336', 1, 0), " +
                    "('FL6338', 1, 0), " +
                    "('FL6340', 1, 0), " +
                    "('FL6358', 1, 0), " +
                    "('FE6070', 1, 0), " +
                    "('FE6095', 1, 0), " +
                    "('FE6097', 1, 0), " +
                    "('LP6003', 1, 1), " +
                    "('FE6088', 1, 1), " +
                    "('FE6090', 1, 1), " +
                    "('FE6091', 1, 1), " +
                    "('FE6092', 1, 1), " +
                    "('FE6094', 1, 1), " +
                    "('FE6101', 1, 1), " +
                    "('FE6102', 1, 1);";
    private static final String INSERT_TABLE_KETQUAHOCPHAN =
            "INSERT INTO KetQuaHocPhan (maLop, maSv, maHp, tx1, tx2, giuaKy, cuoiKy, diemKiVong, hocKy) VALUES " +
                    // Học kỳ 1 (Bắt buộc)
                    "('LP6010.1', '2021600847', 'LP6010', 7.3, 7.1, 9.0, 9.5, NULL, 1), " +
                    "('BS6018.1', '2021600847', 'BS6018', 8.5, 8.9, 8.4, 8.9, NULL, 1), " +
                    "('BM6091.1', '2021600847', 'BM6091', 7.8, 8.3, 6.6, 9.8, NULL, 1), " +
                    "('BS6001.1', '2021600847', 'BS6001', 6.9, 9.5, 8.9, 8.6, NULL, 1), " +
                    "('BS6002.1', '2021600847', 'BS6002', 9.7, 8.1, 9.0, 10.0, NULL, 1), " +
                    "('DC6004.1', '2021600847', 'DC6004', 8.7, 9.0, 6.2, 10.0, NULL, 1), " +
                    "('DC6005.1', '2021600847', 'DC6005', 7.4, 9.6, 7.7, 7.9, NULL, 1), " +
                    "('DC6006.1', '2021600847', 'DC6006', 7.3, 7.6, 9.2, 8.3, NULL, 1), " +
                    "('DC6007.1', '2021600847', 'DC6007', 10.0, 9.2, 10.0, 7.0, NULL, 1), " +
                    "('FE6077.1', '2021600847', 'FE6077', 6.1, 8.3, 9.0, 6.5, NULL, 1), " +

                    // Học kỳ 1 (Tự chọn)
                    "('FL6130.1', '2021600847', 'FL6130', 7.7, 8.4, 8.4, 9.5, NULL, 1), " +
                    "('PE6001.1', '2021600847', 'PE6001', 8.2, 8.0, 9.2, 8.8, NULL, 1), " +

                    // Học kỳ 2 (Bắt buộc)
                    "('LP6011.1', '2021600847', 'LP6011', 6.3, 8.3, 9.4, 10.0, NULL, 2), " +
                    "('BS6006.1', '2021600847', 'BS6006', 9.0, 7.8, 10.0, 6.2, NULL, 2), " +
                    "('BS6008.1', '2021600847', 'BS6008', 9.4, 6.2, 9.1, 6.8, NULL, 2), " +
                    "('IT6035.1', '2021600847', 'IT6035', 6.2, 7.7, 9.1, 7.5, NULL, 2), " +
                    "('FE6047.1', '2021600847', 'FE6047', 9.1, 7.3, 8.6, 7.0, NULL, 2), " +

                    // Học kỳ 2 (Tự chọn)
                    "('FL6131.1', '2021600847', 'FL6131', 7.8, 6.5, 9.2, 6.8, NULL, 2), " +
                    "('PE6018.1', '2021600847', 'PE6018', 6.8, 8.6, 7.4, 6.3, NULL, 2), " +

                    // Học kỳ 3 (Bắt buộc)
                    "('FE6014.1', '2021600847', 'FE6014', 8.5, 8.1, 10.0, 7.3, NULL, 3), " +
                    "('FE6032.1', '2021600847', 'FE6032', 6.8, 8.3, 7.2, 6.8, NULL, 3), " +
                    "('FE6049.1', '2021600847', 'FE6049', 7.1, 6.7, 10.0, 10.0, NULL, 3), " +
                    "('FE6078.1', '2021600847', 'FE6078', 6.8, 9.5, 6.7, 8.0, NULL, 3), " +
                    "('IT6067.1', '2021600847', 'IT6067', 6.5, 7.4, 10.0, 10.0, NULL, 3), " +

                    // Học kỳ 3 (Tự chọn)
                    "('FL6132.1', '2021600847', 'FL6132', 8.2, 7.1, 9.7, 6.6, NULL, 3), " +
                    "('BS6003.1', '2021600847', 'BS6003', 8.0, 7.5, 8.6, 7.8, NULL, 3), " +

                    // Học kỳ 4 (Bắt buộc)
                    "('LP6012.1', '2021600847', 'LP6012', 7.4, 6.6, 8.1, 8.0, NULL, 4), " +
                    "('LP6013.1', '2021600847', 'LP6013', 9.0, 9.7, 10.0, 8.5, NULL, 4), " +
                    "('FE6044.1', '2021600847', 'FE6044', 8.5, 9.8, 7.9, 10.0, NULL, 4), " +
                    "('FE6048.1', '2021600847', 'FE6048', 7.9, 6.0, 10.0, 7.4, NULL, 4), " +
                    "('FE6079.1', '2021600847', 'FE6079', 6.8, 9.6, 8.6, 9.7, NULL, 4), " +
                    "('FE6082.1', '2021600847', 'FE6082', 8.9, 9.5, 10.0, 7.6, NULL, 4), " +

                    // Học kỳ 4 (Tự chọn)
                    "('FL6133.1', '2021600847', 'FL6133', 9.9, 8.0, 6.5, 6.3, NULL, 4), " +

                    // Học kỳ 5 (Bắt buộc)
                    "('IT6018.1', '2021600847', 'IT6018', 6.8, 6.5, 9.6, 10.0, NULL, 5), " +
                    "('FE6081.1', '2021600847', 'FE6081', 8.1, 7.3, 6.5, 8.2, NULL, 5), " +
                    "('FE6083.1', '2021600847', 'FE6083', 6.6, 7.8, 9.8, 6.1, NULL, 5), " +
                    "('FE6089.1', '2021600847', 'FE6089', 6.3, 6.3, 8.8, 7.2, NULL, 5), " +

                    // Học kỳ 5 (Tự chọn)
                    "('FL6357.1', '2021600847', 'FL6357', 8.3, 7.6, 6.6, 9.5, NULL, 5), " +
                    "('FE6074.1', '2021600847', 'FE6074', 7.0, 7.0, 10.0, 7.3, NULL, 5), " +

                    // Học kỳ 6 (Bắt buộc)
                    "('LP6004.1', '2021600847', 'LP6004', 9.1, 7.5, 7.8, 8.4, NULL, 6), " +
                    "('FE6080.1', '2021600847', 'FE6080', 6.9, 9.3, 10.0, 6.9, NULL, 6), " +
                    "('FE6072.1', '2021600847', 'FE6072', 7.2, 8.0, 6.3, 9.0, NULL, 6), " +
                    "('FE6093.1', '2021600847', 'FE6093', 8.7, 8.9, 7.7, 7.2, NULL, 6), " +

                    // Học kỳ 6 (Tự chọn)
                    "('FE6070.1', '2021600847', 'FE6070', 10.0, 10.0, 10.0, 10.0, NULL, 6), " +
                    "('FE6097.1', '2021600847', 'FE6097', 7.4, 8.1, 10.0, 8.3, NULL, 6), " +

                    // Học kỳ 7 (Bắt buộc)
                    "('LP6003.1', '2021600847', 'LP6003', 8.6, 8.8, 8.5, 9.2, NULL, 7), " +
                    "('FE6088.1', '2021600847', 'FE6088', 9.0, 8.9, 7.9, 8.6, NULL, 7), " +
                    "('FE6090.1', '2021600847', 'FE6090', 6.8, 7.2, 9.0, 8.1, NULL, 7), " +
                    "('FE6091.1', '2021600847', 'FE6091', 8.3, 8.4, 9.1, 7.9, NULL, 7), " +
                    "('FE6092.1', '2021600847', 'FE6092', 7.8, 6.7, 8.2, 6.4, NULL, 7), " +

                    // Học kỳ 8 (Bắt buộc)
                    "('FE6101.1', '2021600847', 'FE6101', 9.0, 9.2, 9.4, 9.5, NULL, 8), " +
                    "('FE6102.1', '2021600847', 'FE6102', 8.6, 9.0, 8.7, 9.3, NULL, 8);";
    private static final String INSERT_TABLE_LICHHOC =
            "INSERT INTO LichHoc " +
                    "(id, mon, thu, ngay, phong, giangVien, tiet, diaDiem) " +
                    "VALUES " +
                    "(1, 'Thiết kế ứng dụng IoT', 'Thứ Năm', '2025-06-19', '406', 'Phan Thị Thu Hằng', '1-2', 'A9'), " +
                    "(2, 'Thiết kế ứng dụng trên thiết bị di động', 'Thứ Hai', '2025-06-23', '202', 'Phạm Thị Quỳnh Trang', '1-2-3', 'A9');";
    public static List<Diem> tatCaDiemHpList = new ArrayList<>();
    private Context context;

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
        String query = "SELECT * FROM LichHoc";
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public ArrayList<MiniTimeTable> getLichHocLite(String date) {
        ArrayList<MiniTimeTable> lichHocList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM LichHoc WHERE ngay = ? ", new String[]{date});
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
        String query = "SELECT * FROM LichHoc WHERE mon LIKE ?";
        String[] selectionArgs = {"%" + keyword + "%"};
        Cursor cursor = db.rawQuery(query, selectionArgs);
        return cursor;
    }

    public boolean updateDataTime(Context context, int row_id, String mon, String thu, String ngay, String giangVien, String phong, String tiet, String diaDiem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mon", mon);
        values.put("thu", thu);
        values.put("ngay", ngay);
        values.put("giangVien", giangVien);
        values.put("phong", phong);
        values.put("tiet", tiet);
        values.put("diaDiem", diaDiem);

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
                        "kq.tx1, kq.tx2, kq.giuaKy, kq.cuoiKy, kq.diemKiVong " +
                        // Thực hiện JOIN các bảng.
                        "FROM KetQuaHocPhan kq " +
                        // JOIN bảng SinhVien để chỉ lấy thông tin maSv và maCn.
                        "JOIN SinhVien sv ON sv.maSv = kq.maSv " +
                        // JOIN bảng HocPhan để lấy các thông tin liên quan tới học phần.
                        "JOIN HocPhan hp ON hp.maHp = kq.maHp " +
                        // JOIN bảng LoaiHocPhan để xác định học phần bắt buộc hoặc tự chọn dựa trên maSv và maCn.
                        "JOIN LoaiHocPhan lhp ON lhp.maHp = hp.maHp AND lhp.maCn = sv.maCn " +
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
}
