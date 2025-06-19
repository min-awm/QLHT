package com.example.btl_android.diem;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Pair;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_android.DatabaseHelper;
import com.example.btl_android.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @noinspection ALL
 */
public class ThongKeActivity extends AppCompatActivity {
    private TextView tvXepLoai, tvTbc;
    private ImageButton btnQuayLai;
    private LineChart lineChart;
    private BarChart barChart;
    private CombinedChart combinedChart;
    private DatabaseHelper db;
    private int[][] diemChuByHocKy, diemSoByHocKy;
    private float[] diemTkByHocKy;
    private int maxCnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        this.setContentView(R.layout.activity_thong_ke);
        ViewCompat.setOnApplyWindowInsetsListener(this.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWidget();
        setupLineChart();
        setupBarChart();
        setupStackedAreaChart();
        setXepLoaiVaTbc();
        btnQuayLai.setOnClickListener(v -> finish());
    }

    private void getWidget() {
        tvXepLoai = findViewById(R.id.tvXepLoai);
        tvTbc = findViewById(R.id.tvTbc);
        btnQuayLai = findViewById(R.id.imgQuayLai);
        lineChart = findViewById(R.id.lineChart);
        barChart = findViewById(R.id.barChart);
        combinedChart = findViewById(R.id.combinedChart);
        maxCnt = 0;
        db = new DatabaseHelper(this);
    }

    private void setXepLoaiVaTbc() {
        String xepLoai = "";
        float tbc = 0.0f, sumOfDiemTk = 0.0f;
        float cnt = 0.0f;
        for (int i = 0; i < 8; i++) {
            if (diemTkByHocKy[i] > 0.0f) {
                cnt += 1.0f;
                sumOfDiemTk += diemTkByHocKy[i];
            }
        }
        if (cnt != 0.0f) tbc = sumOfDiemTk / cnt;

        if (tbc == 0.0f) xepLoai = "-";
        else if (tbc < 1.0f) xepLoai = "Kém";
        else if (tbc < 2.0f) xepLoai = "Yếu";
        else if (tbc < 2.5f) xepLoai = "Trung bình";
        else if (tbc < 3.2f) xepLoai = "Khá";
        else if (tbc < 3.6f) xepLoai = "Giỏi";
        else xepLoai = "Xuất sắc";

        tvXepLoai.setText(xepLoai);
        tvTbc.setText(tbc == 0.0f ? "-" : String.format("%.2f", tbc));
    }

    private void setupLineChart() {
        diemChuByHocKy = new int[8][8];
        for (Diem diem : db.tatCaDiemHpList) {
            int hocKy = diem.getHocKy() - 1;
            String diemChu = diem.getDiemChu();
            if (diemChu.equals("-")) continue;
            switch (diemChu) {
                case "F":
                    diemChuByHocKy[0][hocKy]++;
                    maxCnt = Math.max(maxCnt, diemChuByHocKy[0][hocKy]);
                    break;
                case "D":
                    diemChuByHocKy[1][hocKy]++;
                    maxCnt = Math.max(maxCnt, diemChuByHocKy[1][hocKy]);
                    break;
                case "D+":
                    diemChuByHocKy[2][hocKy]++;
                    maxCnt = Math.max(maxCnt, diemChuByHocKy[2][hocKy]);
                    break;
                case "C":
                    diemChuByHocKy[3][hocKy]++;
                    maxCnt = Math.max(maxCnt, diemChuByHocKy[3][hocKy]);
                    break;
                case "C+":
                    diemChuByHocKy[4][hocKy]++;
                    maxCnt = Math.max(maxCnt, diemChuByHocKy[4][hocKy]);
                    break;
                case "B":
                    diemChuByHocKy[5][hocKy]++;
                    maxCnt = Math.max(maxCnt, diemChuByHocKy[5][hocKy]);
                    break;
                case "B+":
                    diemChuByHocKy[6][hocKy]++;
                    maxCnt = Math.max(maxCnt, diemChuByHocKy[6][hocKy]);
                    break;
                case "A":
                    diemChuByHocKy[7][hocKy]++;
                    maxCnt = Math.max(maxCnt, diemChuByHocKy[7][hocKy]);
                    break;
            }
        }

        int[] diemChu = new int[8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                diemChu[i] += diemChuByHocKy[i][j];
            }
        }

        List<Entry> diemChuEntries = new ArrayList<>();
        List<String> diemChuLabels = new ArrayList<>(Arrays.asList("F", "D", "D+", "C", "C+", "B", "B+", "A"));

        for (int i = 0; i < 8; i++) {
            diemChuEntries.add(new Entry(i, diemChu[i]));
        }

        LineDataSet lineDataSet = new LineDataSet(diemChuEntries, "Điểm chữ");
        lineDataSet.setColor(Color.parseColor("#4CAF50"));
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(Color.parseColor("#4CAF50"));
        lineDataSet.setLineWidth(4f);
        lineDataSet.setValueTextColor(Color.DKGRAY);
        lineDataSet.setValueTextSize(12f);
        lineDataSet.setValueTypeface(Typeface.DEFAULT_BOLD);
        lineDataSet.setValueFormatter(new DefaultValueFormatter(0));

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextSize(12f);
        xAxis.setTypeface(Typeface.DEFAULT_BOLD);
        xAxis.setYOffset(3f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(diemChuLabels));

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setDrawAxisLine(false);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(lineChart.getYMax() + 1.0f);
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });
        yAxis.setLabelCount((int) lineChart.getData().getYMax());
        yAxis.setTextSize(12f);
        yAxis.setXOffset(12f);
        lineChart.getAxisRight().setEnabled(false);

        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.animateY(1000, Easing.EaseInOutQuad);

        ChuThichMarkerView markerView = new ChuThichMarkerView(this, R.layout.custommv_chu_thich, 0);
        markerView.setChartView(lineChart);
        lineChart.setMarker(markerView);
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                lineChart.highlightValue(highlight);
            }

            @Override
            public void onNothingSelected() {
            }
        });

        lineChart.invalidate();
    }

    private void setupBarChart() {
        diemSoByHocKy = new int[8][8];
        for (Diem diem : db.tatCaDiemHpList) {
            int hocKy = diem.getHocKy() - 1;
            Float diemSo = diem.getDiem10();
            if (diemSo == null) continue;
            if (diemSo < 4.0f) {
                diemSoByHocKy[0][hocKy]++;
            } else if (diemSo < 5.0f) {
                diemSoByHocKy[1][hocKy]++;
            } else if (diemSo < 6.0f) {
                diemSoByHocKy[2][hocKy]++;
            } else if (diemSo < 7.0f) {
                diemSoByHocKy[3][hocKy]++;
            } else if (diemSo < 8.0f) {
                diemSoByHocKy[4][hocKy]++;
            } else if (diemSo < 9.0f) {
                diemSoByHocKy[5][hocKy]++;
            } else if (diemSo < 10.0f) {
                diemSoByHocKy[6][hocKy]++;
            } else if (diemSo == 10.0f) {
                diemSoByHocKy[7][hocKy]++;
            }
        }

        int[] diemSo = new int[8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                diemSo[i] += diemSoByHocKy[i][j];
            }
        }

        List<BarEntry> diemSoEntries = new ArrayList<>();
        List<String> diemSoLabels = new ArrayList<>(Arrays.asList("<4", ">=4", ">=5", ">=6", ">=7", ">=8", ">=9", "10"));

        for (int i = 0; i < 8; i++) {
            diemSoEntries.add(new BarEntry(i, diemSo[i]));
        }

        BarDataSet barDataSet = new BarDataSet(diemSoEntries, "Điểm số");
        barDataSet.setColor(Color.parseColor("#68F1AF"));
        barDataSet.setValueTextColor(Color.DKGRAY);
        barDataSet.setValueTextSize(12f);
        barDataSet.setValueFormatter(new DefaultValueFormatter(0));

        barChart.setData(new BarData(barDataSet));

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setYOffset(-8f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(diemSoLabels));
        xAxis.setTextSize(12f);
        xAxis.setTypeface(Typeface.DEFAULT_BOLD);
        xAxis.setYOffset(3f);

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(barChart.getYMax() + 1.0f);
        yAxis.setXOffset(12f);
        yAxis.setTextSize(12f);
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });
        yAxis.setLabelCount((int) barChart.getData().getYMax());
        barChart.getAxisRight().setEnabled(false);

        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000, Easing.EaseInOutQuad);

        ChuThichMarkerView markerView = new ChuThichMarkerView(this, R.layout.custommv_chu_thich, 1);
        markerView.setChartView(barChart);
        barChart.setMarker(markerView);
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                barChart.highlightValue(highlight);
            }

            @Override
            public void onNothingSelected() {
            }
        });

        barChart.invalidate();
    }

    private void setupStackedAreaChart() {
        List<List<Pair<Float, Float>>> diemHpByHocKy = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            diemHpByHocKy.add(new ArrayList<>(8));
        }
        for (Diem diem : db.tatCaDiemHpList) {
            int hocKy = diem.getHocKy() - 1;
            if (diem.getDiem4() == null) continue;
            Float diem4 = diem.getDiem4(), soTc = diem.getSoTinChiLt() + diem.getSoTinChiTh();
            if (diem4 == 0.0f) {
                diemHpByHocKy.get(hocKy).add(new Pair<>(diem4, soTc));
            } else if (diem4 == 1.0f) {
                diemHpByHocKy.get(hocKy).add(new Pair<>(diem4, soTc));
            } else if (diem4 == 1.5f) {
                diemHpByHocKy.get(hocKy).add(new Pair<>(diem4, soTc));
            } else if (diem4 == 2.0f) {
                diemHpByHocKy.get(hocKy).add(new Pair<>(diem4, soTc));
            } else if (diem4 == 2.5f) {
                diemHpByHocKy.get(hocKy).add(new Pair<>(diem4, soTc));
            } else if (diem4 == 3.0f) {
                diemHpByHocKy.get(hocKy).add(new Pair<>(diem4, soTc));
            } else if (diem4 == 3.5f) {
                diemHpByHocKy.get(hocKy).add(new Pair<>(diem4, soTc));
            } else if (diem4 == 4.0f) {
                diemHpByHocKy.get(hocKy).add(new Pair<>(diem4, soTc));
            }
        }

        diemTkByHocKy = new float[8];
        for (int i = 0; i < 8; i++) {
            Float sumOfHpxTc = 0.0f, sumOfTc = 0.0f;
            for (Pair<Float, Float> hp : diemHpByHocKy.get(i)) {
                sumOfHpxTc += (hp.first * hp.second);
                sumOfTc += hp.second;
            }
            diemTkByHocKy[i] = (sumOfHpxTc / sumOfTc);
        }

        List<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            barEntries.add(new BarEntry(i, diemTkByHocKy[i]));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, null);
        barDataSet.setColor(Color.parseColor("#68F1AF"));
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(12f);
        barDataSet.setValueTypeface(Typeface.DEFAULT_BOLD);
        barDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return Float.isNaN(value) ? "" : String.format("%.2f", value);
            }
        });
        BarData barData = new BarData(barDataSet);

        String[] letterScores = {"F", "D", "D+", "C", "C+", "B", "B+", "A"};
        List<List<Entry>> lineEntriesList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            lineEntriesList.add(new ArrayList<>());
            for (int j = 0; j < 8; j++) {
                Entry entry = new Entry(j, diemChuByHocKy[i][j], letterScores[i]);
                lineEntriesList.get(i).add(entry);
            }
        }

        String[] colors = {"#F50057", "#FF5733", "#FFC300", "#4CAF50", "#673AB7", "#2196F3", "#F45eEf", "#00BCD4"};
        List<LineDataSet> lineDataSets = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            LineDataSet lineDataSet = new LineDataSet(lineEntriesList.get(i), letterScores[i]);
            lineDataSet.setLineWidth(3f);
            lineDataSet.setColor(Color.parseColor(colors[i]));
            lineDataSet.setCircleColor(Color.parseColor(colors[i]));
            lineDataSet.setHighLightColor(Color.TRANSPARENT);
            lineDataSet.setValueTextSize(0f);
            lineDataSet.setValueFormatter(new DefaultValueFormatter(0));
            lineDataSets.add(lineDataSet);
        }

        LineData lineData = new LineData();
        for (int i = 0; i < lineDataSets.size(); i++) {
            lineData.addDataSet(lineDataSets.get(i));
        }

        CombinedData combinedData = new CombinedData();
        combinedData.setData(barData);
        combinedData.setData(lineData);
        combinedChart.setData(combinedData);

        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        List<String> hocKyLables = new ArrayList<>(Arrays.asList("HK1", "HK2", "HK3", "HK4", "HK5", "HK6", "HK7", "HK8"));
        xAxis.setValueFormatter(new IndexAxisValueFormatter(hocKyLables));
        xAxis.setTextSize(12f);
        xAxis.setTypeface(Typeface.DEFAULT_BOLD);
        xAxis.setYOffset(3f);
        xAxis.setAxisMinimum(combinedData.getXMin() - .5f);
        xAxis.setAxisMaximum(combinedData.getXMax() + .5f);

        YAxis yAxis = combinedChart.getAxisLeft();
        yAxis.setDrawAxisLine(false);
        yAxis.setAxisMinimum(0f);
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });
        yAxis.setLabelCount((int) combinedChart.getData().getYMax());
        yAxis.setTextSize(12f);
        yAxis.setXOffset(12f);
        yAxis.setAxisMinimum(combinedData.getYMin() - .1f);
        yAxis.setAxisMaximum(combinedData.getYMax() + .3f);

        combinedChart.getAxisRight().setEnabled(false);

        Legend legend = combinedChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setWordWrapEnabled(true);

        List<LegendEntry> legendEntries = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            LegendEntry entry = new LegendEntry();
            entry.label = letterScores[i];
            entry.formColor = Color.parseColor(colors[i]);
            legendEntries.add(entry);
        }
        legend.setCustom(legendEntries);
        legend.setTextColor(Color.BLACK);
        legend.setTextSize(12f);
        combinedChart.getDescription().setEnabled(false);

        ChuThichMarkerView markerView = new ChuThichMarkerView(this, R.layout.custommv_chu_thich, 2);
        markerView.setChartView(combinedChart);
        combinedChart.setMarker(markerView);
        combinedChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                combinedChart.invalidate();
            }

            @Override
            public void onNothingSelected() {
            }
        });
        combinedChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {
            }

            @Override
            public void onChartGestureEnd(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {
            }

            @Override
            public void onChartLongPressed(MotionEvent motionEvent) {
            }

            @Override
            public void onChartDoubleTapped(MotionEvent motionEvent) {
            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
                Highlight highlight = combinedChart.getHighlightByTouchPoint(me.getX(), me.getY());
                if (highlight == null) return;
                int selectedIndex = highlight.getDataIndex();
                if (selectedIndex == 0) {
                    float x = highlight.getX();
                    float y = highlight.getY();

                    List<LineDataSet> setsToHighlight = new ArrayList<>();
                    List<LineDataSet> setsToUpdate = new ArrayList<>();
                    for (ILineDataSet lineDataSet : lineData.getDataSets()) {
                        for (Entry entry : lineDataSet.getEntriesForXValue(x)) {
                            if (entry.getY() == y) {
                                setsToHighlight.add((LineDataSet) lineDataSet);
                            } else {
                                setsToUpdate.add((LineDataSet) lineDataSet);
                            }
                        }
                    }
                    for (LineDataSet lineDataSet : setsToHighlight) {
                        lineDataSet.setLineWidth(6f);
                        lineData.removeDataSet(lineDataSet);
                        lineData.addDataSet(lineDataSet);
                    }
                    for (LineDataSet lineDataSet : setsToUpdate) {
                        lineDataSet.setLineWidth(3f);
                    }
                    combinedChart.invalidate();
                    return;
                }
                for (ILineDataSet set : lineData.getDataSets()) {
                    ((LineDataSet) set).setLineWidth(3f);
                }
                combinedChart.invalidate();
            }

            @Override
            public void onChartFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

            }

            @Override
            public void onChartScale(MotionEvent motionEvent, float v, float v1) {

            }

            @Override
            public void onChartTranslate(MotionEvent motionEvent, float v, float v1) {

            }
        });

        combinedChart.invalidate();
    }

    public int[][] getDiemChuByHocKy() {
        return diemChuByHocKy;
    }

    public int[][] getDiemSoByHocKy() {
        return diemSoByHocKy;
    }
}