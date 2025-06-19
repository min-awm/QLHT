package com.example.btl_android.diem;

import com.example.btl_android.hoc_phan_du_kien.HocPhan;

/**
 * @noinspection ALL
 */
public class Diem extends HocPhan {
    private String maLop;
    private Float tx1, tx2, giuaKy, cuoiKy, diemKiVong;
    private Integer loai, vangLt, vangTh;

    public Diem() {
        super();
    }

    public Diem(String maLop, String maHp, String tenHp, Integer loai, Integer soTietLt, Integer soTietTh, Integer hocKy, String hinhThucThi,
                String heSo, Float tx1, Float tx2, Float giuaKy, Float cuoiKy, Float diemKiVong, Integer vangLt, Integer vangTh) {
        super(maHp, tenHp, soTietLt, soTietTh, hocKy, hinhThucThi, heSo);
        this.maLop = maLop;
        this.loai = loai;
        this.tx1 = tx1;
        this.tx2 = tx2;
        this.giuaKy = giuaKy;
        this.cuoiKy = cuoiKy;
        this.diemKiVong = diemKiVong;
        this.vangLt = vangLt;
        this.vangTh = vangTh;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public Float getTx1() {
        return tx1;
    }

    public void setTx1(Float tx1) {
        this.tx1 = tx1;
    }

    public Float getTx2() {
        return tx2;
    }

    public void setTx2(Float tx2) {
        this.tx2 = tx2;
    }

    public Float getGiuaKy() {
        return giuaKy;
    }

    public void setGiuaKy(Float giuaKy) {
        this.giuaKy = giuaKy;
    }

    public Float getCuoiKy() {
        return cuoiKy;
    }

    public void setCuoiKy(Float cuoiKy) {
        this.cuoiKy = cuoiKy;
    }

    public Float getDiemKiVong() {
        return diemKiVong;
    }

    public void setDiemKiVong(Float diemKiVong) {
        this.diemKiVong = diemKiVong;
    }

    public Integer getLoai() {
        return loai;
    }

    public void setLoai(Integer loai) {
        this.loai = loai;
    }

    public Integer getVangLt() {
        return vangLt;
    }

    public void setVangLt(Integer vangLt) {
        this.vangLt = vangLt;
    }

    public Integer getVangTh() {
        return vangTh;
    }

    public void setVangTh(Integer vangTh) {
        this.vangTh = vangTh;
    }

    public Float getDiem10() {
        String heSo = super.getHeSo();
        String[] heSoList = heSo.trim().split("-");
        if (heSoList.length == 3) {
            if (tx1 == null || tx2 == null || cuoiKy == null) return null;
            return tx1 * Float.parseFloat(heSoList[0]) / 100.0f +
                    tx2 * Float.parseFloat(heSoList[1]) / 100.0f + cuoiKy * Float.parseFloat(heSoList[2]) / 100.0f;
        }
        if (heSoList.length == 4) {
            if (tx1 == null || tx2 == null || giuaKy == null || cuoiKy == null) return null;
            return tx1 * Float.parseFloat(heSoList[0]) / 100.0f + tx2 * Float.parseFloat(heSoList[1]) / 100.0f +
                    giuaKy * Float.parseFloat(heSoList[2]) / 100.0f + cuoiKy * Float.parseFloat(heSoList[3]) / 100.0f;
        }
        return null;
    }

    public Float getDiem4() {
        Float diem10 = getDiem10();
        if (diem10 == null) return null;
        if (diem10 < 4.0f) return 0.0f;
        if (diem10 < 4.7f) return 1.0f;
        if (diem10 < 5.5f) return 1.5f;
        if (diem10 < 6.2f) return 2.0f;
        if (diem10 < 7.0f) return 2.5f;
        if (diem10 < 7.7f) return 3.0f;
        if (diem10 < 8.5f) return 3.5f;
        return 4.0f;
    }

    public String getDiemChu() {
        Float diem10 = getDiem10();
        if (diem10 == null) return "-";
        if (diem10 < 4.0f) return "F";
        if (diem10 < 4.7f) return "D";
        if (diem10 < 5.5f) return "D+";
        if (diem10 < 6.2f) return "C";
        if (diem10 < 7.0f) return "C+";
        if (diem10 < 7.7f) return "B";
        if (diem10 < 8.5f) return "B+";
        return "A";
    }

    public String getXepLoai() {
        Float diem10 = getDiem10();
        if (diem10 == null) return "-";
        if (diem10 < 4.0f) return "Kém";
        if (diem10 < 6.2f) return "Yếu";
        if (diem10 < 7.0f) return "Trung bình";
        if (diem10 < 8.5f) return "Khá";
        return "Giỏi";
    }

    public String getDieuKien() {
        Integer soTietLt = super.getSoTietLt(),
                soTietTh = super.getSoTietTh();
        if (soTietLt > 0 && soTietTh == 0) {
            if (vangLt / soTietLt <= 0.3f) return "Đủ điều kiện";
            else return "Cấm thi";
        }
        if (soTietLt == 0 && soTietTh > 0) {
            if (vangTh / soTietTh <= 0.3f) return "Đủ điều kiện";
            else return "Cấm thi";
        }
        if (vangLt / soTietLt <= 0.3f && vangTh / soTietTh <= 0.3f) return "Đủ điều kiện";
        else return "Cấm thi";
    }
}
