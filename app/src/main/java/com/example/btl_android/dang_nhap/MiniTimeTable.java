package com.example.btl_android.dang_nhap;

public class MiniTimeTable {
    String tenMonHoc;
    String tietHoc;
    String diaDiem;

    public MiniTimeTable(String tenMonHoc, String tietHoc, String diaDiem) {
        this.tenMonHoc = tenMonHoc;
        this.tietHoc = tietHoc;
        this.diaDiem = diaDiem;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public void setTenMonHoc(String tenMonHoc) {
        this.tenMonHoc = tenMonHoc;
    }

    public String getTietHoc() {
        return tietHoc;
    }

    public void setTietHoc(String tietHoc) {
        this.tietHoc = tietHoc;
    }

    public String getDiaDiem() {
        return diaDiem;
    }

    public void setDiaDiem(String diaDiem) {
        this.diaDiem = diaDiem;
    }
}
