package com.example.btl_android.hoc_phan_du_kien;

import java.io.Serializable;

/**
 * @noinspection ALL
 */
public class HocPhan implements Serializable {
    private String maHp, tenHp;
    private Float soTinChiLt, soTinChiTh;
    private Integer soTietLt, soTietTh, hocKy;
    private String hinhThucThi, heSo;

    public HocPhan(String maHp, String tenHp, float soTinChiLt, float soTinChiTh, int soTietLt, int soTietTh, int hocKy, String hinhThucThi, String heSo) {
        this.maHp = maHp;
        this.tenHp = tenHp;
        this.soTinChiLt = soTinChiLt;
        this.soTinChiTh = soTinChiTh;
        this.soTietLt = soTietLt;
        this.soTietTh = soTietTh;
        this.hocKy = hocKy;
        this.hinhThucThi = hinhThucThi;
        this.heSo = heSo;
    }


    public HocPhan() {
        this.maHp = "HP000";
        this.tenHp = "Unknown";
        this.soTinChiLt = 3.0f;
        this.soTinChiTh = 0.5f;
        this.soTietLt = 30;
        this.soTietTh = 30;
        this.hocKy = 1;
        this.hinhThucThi = "TL";
        this.heSo = "20-20-60";
    }

    public HocPhan(String maLop, String maHp, String tenHp, Float soTinChiLt,
                   Float soTinChiTh, String hinhThucThi, String heSo) {
        this.maHp = maHp;
        this.soTinChiLt = soTinChiLt;
        this.soTinChiTh = soTinChiTh;
        this.hinhThucThi = hinhThucThi;
        this.heSo = heSo;
    }

    public HocPhan(String maHp, String tenHp, Integer soTietLt, Integer hocKy) {
        this.maHp = maHp;
        this.tenHp = tenHp;
        this.soTietLt = soTietLt;
        this.hocKy = hocKy;
    }

    public HocPhan(String maHp, String tenHp, Integer soTietLt, Integer soTietTh, Integer hocKy, String hinhThucThi, String heSo) {
        this.maHp = maHp;
        this.tenHp = tenHp;
        this.soTietLt = soTietLt;
        this.soTietTh = soTietTh;
        this.hocKy = hocKy;
        this.hinhThucThi = hinhThucThi;
        this.heSo = heSo;
    }

    public HocPhan(String maHp, String tenHp, float soTinChiLyThuyet, float soTinChiThucHanh, int hocKy, String hinhThucThi, String heSo) {
        this.maHp = maHp;
        this.tenHp = tenHp;
        this.soTinChiLt = soTinChiLyThuyet;
        this.soTinChiTh = soTinChiThucHanh;
        this.hocKy = hocKy;
        this.hinhThucThi = hinhThucThi;
        this.heSo = heSo;
    }

    public String getMaHp() {
        return maHp;
    }

    public void setMaHp(String maHp) {
        this.maHp = maHp;
    }

    public String getTenHp() {
        return tenHp;
    }

    public void setTenHp(String tenHp) {
        this.tenHp = tenHp;
    }

    public Float getSoTinChiLt() {
        return soTinChiLt;
    }

    public void setSoTinChiLt(Float soTinChiLt) {
        this.soTinChiLt = soTinChiLt;
    }

    public Float getSoTinChiTh() {
        return soTinChiTh;
    }

    public void setSoTinChiTh(Float soTinChiTh) {
        this.soTinChiTh = soTinChiTh;
    }

    public Integer getSoTietLt() {
        return soTietLt;
    }

    public void setSoTietLt(Integer soTietLt) {
        this.soTietLt = soTietLt;
    }

    public Integer getSoTietTh() {
        return soTietTh;
    }

    public void setSoTietTh(Integer soTietTh) {
        this.soTietTh = soTietTh;
    }

    public Integer getHocKy() {
        return hocKy;
    }

    public void setHocKy(Integer hocKy) {
        this.hocKy = hocKy;
    }

    public String getHinhThucThi() {
        return hinhThucThi;
    }

    public void setHinhThucThi(String hinhThucThi) {
        this.hinhThucThi = hinhThucThi;
    }

    public String getHeSo() {
        return heSo;
    }

    public void setHeSo(String heSo) {
        this.heSo = heSo;
    }

}