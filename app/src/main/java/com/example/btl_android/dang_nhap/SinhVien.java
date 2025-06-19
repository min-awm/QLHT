package com.example.btl_android.dang_nhap;

/**
 * @noinspection ALL
 */
public class SinhVien {
    private String maSV;
    private int maChuyenNganh;
    private String tenSV;
    private String tenTK;
    private String matKhau;

    public SinhVien(String maSV, int maChuyenNganh, String tenSV, String tenTK, String matKhau) {
        this.maSV = maSV;
        this.maChuyenNganh = maChuyenNganh;
        this.tenSV = tenSV;
        this.tenTK = tenTK;
        this.matKhau = matKhau;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public int getMaChuyenNganh() {
        return maChuyenNganh;
    }

    public void setMaChuyenNganh(int maChuyenNganh) {
        this.maChuyenNganh = maChuyenNganh;
    }

    public String getTenSV() {
        return tenSV;
    }

    public void setTenSV(String tenSV) {
        this.tenSV = tenSV;
    }

    public String getTenTK() {
        return tenTK;
    }

    public void setTenTK(String tenTK) {
        this.tenTK = tenTK;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
}
