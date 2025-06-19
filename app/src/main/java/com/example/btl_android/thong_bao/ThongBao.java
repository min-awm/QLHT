package com.example.btl_android.thong_bao;

/**
 * @noinspection ALL
 */
public class ThongBao {
    private String tieuDe, noiDung, thoiGian;

    public ThongBao(String tieuDe, String noiDung, String thoiGian) {
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.thoiGian = thoiGian;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public String getThoiGian() {
        return thoiGian;
    }
}
