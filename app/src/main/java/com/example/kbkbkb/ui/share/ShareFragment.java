package com.example.kbkbkb.ui.share;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.broooapps.graphview.CurveGraphConfig;
import com.broooapps.graphview.CurveGraphView;
import com.broooapps.graphview.models.GraphData;
import com.broooapps.graphview.models.PointMap;
import com.example.kbkbkb.R;

public class ShareFragment extends Fragment {

    private ShareViewModel shareViewModel;
    private CurveGraphView cgv1, cgv2, cgv3, cgv4;
    private Context cont;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        cont = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareViewModel =
                ViewModelProviders.of(this).get(ShareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_predict, container, false);

        cgv1 = (CurveGraphView) root.findViewById(R.id.cgv1);
        cgv2 = (CurveGraphView) root.findViewById(R.id.cgv2);
        cgv3 = (CurveGraphView) root.findViewById(R.id.cgv3);
        cgv4 = (CurveGraphView) root.findViewById(R.id.cgv4);

        cgv1.configure(
                new CurveGraphConfig.Builder(cont)
                    .setAxisColor(R.color.Black)
                    .setVerticalGuideline(4)
                    .setHorizontalGuideline(2)
                    .setGuidelineColor(R.color.Black)
                    .setNoDataMsg(" No Data ")
                    .setxAxisScaleTextColor(R.color.Black)
                    .setyAxisScaleTextColor(R.color.Black)
                    .setAnimationDuration(2000)
                    .build()
        );

        PointMap pointmap1 = new PointMap();
        pointmap1.addPoint(0,9);
        pointmap1.addPoint(1,26);
        pointmap1.addPoint(2,28);
        pointmap1.addPoint(3,101);
        pointmap1.addPoint(4,120);
        pointmap1.addPoint(5,123);
        pointmap1.addPoint(6,135);
        pointmap1.addPoint(7,137);
        pointmap1.addPoint(8,140);
        pointmap1.addPoint(9,146);
        pointmap1.addPoint(10,151);
        pointmap1.addPoint(11,158);
        pointmap1.addPoint(12,159);
        pointmap1.addPoint(13,166);
        pointmap1.addPoint(14,170);
        pointmap1.addPoint(15,202);
        pointmap1.addPoint(16,204);
        pointmap1.addPoint(17,205);
        pointmap1.addPoint(18,217);
        pointmap1.addPoint(19,234);

        final GraphData gd1 = GraphData.builder(cont)
                            .setPointMap(pointmap1)
                            .setGraphStroke(R.color.Black)
                            .setGraphGradient(R.color.gradientStartColor, R.color.gradientEndColor)
                            .animateLine(true)
                            .setPointColor(R.color.Black)
                            .setPointRadius(10)
                            .build();

        cgv2.configure(
                new CurveGraphConfig.Builder(cont)
                        .setAxisColor(R.color.Black)
                        .setVerticalGuideline(4)
                        .setHorizontalGuideline(2)
                        .setGuidelineColor(R.color.Black)
                        .setNoDataMsg(" No Data ")
                        .setxAxisScaleTextColor(R.color.Black)
                        .setyAxisScaleTextColor(R.color.Black)
                        .setAnimationDuration(2000)
                        .build()
        );

        PointMap pointmap2 = new PointMap();
        pointmap2.addPoint(0,288);
        pointmap2.addPoint(1,297);
        pointmap2.addPoint(2,137);
        pointmap2.addPoint(3,139);
        pointmap2.addPoint(4,213);
        pointmap2.addPoint(5,232);
        pointmap2.addPoint(6,234);
        pointmap2.addPoint(7,246);
        pointmap2.addPoint(8,248);
        pointmap2.addPoint(9,250);
        pointmap2.addPoint(10,256);
        pointmap2.addPoint(11,256);
        pointmap2.addPoint(12,263);
        pointmap2.addPoint(13,264);
        pointmap2.addPoint(14,2);
        pointmap2.addPoint(15,1);
        pointmap2.addPoint(16,30);
        pointmap2.addPoint(17,2);
        pointmap2.addPoint(18,3);
        pointmap2.addPoint(19,15);

        final GraphData gd2 = GraphData.builder(cont)
                .setPointMap(pointmap2)
                .setGraphStroke(R.color.Black)
                .setGraphGradient(R.color.gradientStartColor, R.color.gradientEndColor)
                .animateLine(true)
                .setPointColor(R.color.Black)
                .setPointRadius(10)
                .build();

        cgv3.configure(
                new CurveGraphConfig.Builder(cont)
                        .setAxisColor(R.color.Black)
                        .setVerticalGuideline(4)
                        .setHorizontalGuideline(2)
                        .setGuidelineColor(R.color.Black)
                        .setNoDataMsg(" No Data ")
                        .setxAxisScaleTextColor(R.color.Black)
                        .setyAxisScaleTextColor(R.color.Black)
                        .setAnimationDuration(2000)
                        .build()
        );

        PointMap pointmap3 = new PointMap();
        pointmap3.addPoint(0,276);
        pointmap3.addPoint(1,302);
        pointmap3.addPoint(2,138);
        pointmap3.addPoint(3,132);
        pointmap3.addPoint(4,199);
        pointmap3.addPoint(5,232);
        pointmap3.addPoint(6,225);
        pointmap3.addPoint(7,230);
        pointmap3.addPoint(8,239);
        pointmap3.addPoint(9,226);
        pointmap3.addPoint(10,227);
        pointmap3.addPoint(11,252);
        pointmap3.addPoint(12,248);
        pointmap3.addPoint(13,247);
        pointmap3.addPoint(14,2);
        pointmap3.addPoint(15,1);
        pointmap3.addPoint(16,27);
        pointmap3.addPoint(17,3);
        pointmap3.addPoint(18,5);
        pointmap3.addPoint(19,16);

        final GraphData gd3 = GraphData.builder(cont)
                .setPointMap(pointmap3)
                .setGraphStroke(R.color.Black)
                .setGraphGradient(R.color.gradientStartColor, R.color.gradientEndColor)
                .animateLine(true)
                .setPointColor(R.color.Black)
                .setPointRadius(10)
                .build();

        cgv4.configure(
                new CurveGraphConfig.Builder(cont)
                        .setAxisColor(R.color.Black)
                        .setVerticalGuideline(4)
                        .setHorizontalGuideline(2)
                        .setGuidelineColor(R.color.Black)
                        .setNoDataMsg(" No Data ")
                        .setxAxisScaleTextColor(R.color.Black)
                        .setyAxisScaleTextColor(R.color.Black)
                        .setAnimationDuration(2000)
                        .build()
        );

        PointMap pointmap4 = new PointMap();
        pointmap4.addPoint(0,256);
        pointmap4.addPoint(1,242);
        pointmap4.addPoint(2,148);
        pointmap4.addPoint(3,239);
        pointmap4.addPoint(4,271);
        pointmap4.addPoint(5,189);
        pointmap4.addPoint(6,300);
        pointmap4.addPoint(7,210);
        pointmap4.addPoint(8,246);
        pointmap4.addPoint(9,236);
        pointmap4.addPoint(10,139);
        pointmap4.addPoint(11,253);
        pointmap4.addPoint(12,255);
        pointmap4.addPoint(13,263);
        pointmap4.addPoint(14,7);
        pointmap4.addPoint(15,24);
        pointmap4.addPoint(16,33);
        pointmap4.addPoint(17,2);
        pointmap4.addPoint(18,4);
        pointmap4.addPoint(19,6);

        final GraphData gd4 = GraphData.builder(cont)
                .setPointMap(pointmap4)
                .setGraphStroke(R.color.Black)
                .setGraphGradient(R.color.gradientStartColor, R.color.gradientEndColor)
                .animateLine(true)
                .setPointColor(R.color.Black)
                .setPointRadius(10)
                .build();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cgv1.setData(20, 250, gd1);
                cgv2.setData(20, 300, gd2);
                cgv3.setData(20, 320, gd3);
                cgv4.setData(20, 320, gd4);
            }
        }, 250);

        return root;
    }
}