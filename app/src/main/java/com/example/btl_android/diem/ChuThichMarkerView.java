package com.example.btl_android.diem;

import android.content.Context;
import android.widget.TextView;

import com.example.btl_android.R;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @noinspection ALL
 */
public class ChuThichMarkerView extends MarkerView {

    private final TextView tvTieuDe;
    private final TextView tvNoiDung;
    private int chartId;

    public ChuThichMarkerView(Context context, int layoutResource, int chartId) {
        super(context, layoutResource);
        tvTieuDe = findViewById(R.id.tvTieuDe);
        tvNoiDung = findViewById(R.id.tvNoiDung);
        this.chartId = chartId;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        int xIndex = (int) e.getX();
        int yIndex = (int) e.getY();
        if (chartId <= 1) {
            int[][] data = new int[8][8];
            if (chartId == 0) data = ((ThongKeActivity) getContext()).getDiemChuByHocKy();
            else data = ((ThongKeActivity) getContext()).getDiemSoByHocKy();

            boolean isEmpty = true;
            for (int count : data[xIndex]) {
                if (count > 0) {
                    isEmpty = false;
                    break;
                }
            }

            if (!isEmpty) {
                StringBuilder info = new StringBuilder();
                for (int i = 0; i < 8; i++) {
                    if (data[xIndex][i] > 0)
                        info.append("HK").append(i + 1).append(": ").append(data[xIndex][i]).append("\n");
                }
                if (info.charAt(info.length() - 1) == '\n')
                    info.deleteCharAt(info.length() - 1);
                tvNoiDung.setText(info.toString());
            } else {
                tvNoiDung.setText("Không có\ndữ liệu");
            }
        } else {
            if (e instanceof BarEntry) {
                tvTieuDe.setText("Điểm tổng kết");
                if (yIndex > 0)
                    tvNoiDung.setText("Học kỳ " + (xIndex + 1) + ": " + String.format("%.2f", e.getY()));
                else tvNoiDung.setText("Không có\ndữ liệu");
            } else {
                tvTieuDe.setText("Số lượng\nđiểm HK" + (xIndex + 1));
                Chart chart = getChartView();
                CombinedData combinedData = ((CombinedChart) chart).getCombinedData();
                StringBuilder info = new StringBuilder();
                List<ILineDataSet> dataSetList = new ArrayList<>(combinedData.getLineData().getDataSets());
                Collections.sort(dataSetList, new Comparator<ILineDataSet>() {
                    @Override
                    public int compare(ILineDataSet dataSet1, ILineDataSet dataSet2) {
                        String label1 = dataSet1.getLabel();
                        String label2 = dataSet2.getLabel();
                        List<String> desiredOrder = Arrays.asList("A", "B+", "B", "C+", "C", "D+", "D", "F");
                        int index1 = desiredOrder.indexOf(label1);
                        int index2 = desiredOrder.indexOf(label2);
                        return Integer.compare(index1, index2);
                    }
                });
                for (ILineDataSet lineDataSet : dataSetList) {
                    for (Entry entry : lineDataSet.getEntriesForXValue(e.getX())) {
                        if (entry.getY() != yIndex) continue;
                        info.append("Điểm ").append(lineDataSet.getLabel())
                                .append(": ").append(yIndex).append("\n");
                    }
                }
                if (info.length() > 0 && info.charAt(info.length() - 1) == '\n') {
                    info.deleteCharAt(info.length() - 1);
                }
                tvNoiDung.setText(info);
            }
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}

