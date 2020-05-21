package com.rfl.trn.starr_cell.Fragment.Karyawan;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rfl.trn.starr_cell.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardKaryawanFragment extends Fragment {


    @BindView(R.id.coba)
    ImageView coba;
    Unbinder unbinder;

    public DashboardKaryawanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard_karyawan, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        ReceiptBuilder struk = new ReceiptBuilder(1200);
//        struk.setMargin(50, 50)
//                .setAlign(Paint.Align.CENTER)
//                .setColor(Color.BLACK)
//                .setTextSize(60)
////                .addImage(BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_black_24dp))
//                .addText("RAFLI CELL")
//                .addText("Klahang RT 05/02")
//                .addText("085726096515")
//                .addBlankSpace(30)
//                .setAlign(Paint.Align.LEFT).
//                addText("Terminal ID: 123456", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("1234").
//                setAlign(Paint.Align.LEFT).
//                addLine().
//                addText("08/15/16", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("SERVER #4").
//                setAlign(Paint.Align.LEFT).
//                addParagraph().
//                addText("CHASE VISA - INSERT").
//                addText("AID: A000000000011111").
//                addText("ACCT #: *********1111").
//                addParagraph().
//                addText("CREDIT SALE").
//                addText("UID: 12345678", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("REF #: 1234").
//                setAlign(Paint.Align.LEFT).
//                addText("BATCH #: 091", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("AUTH #: 0701C").
//                setAlign(Paint.Align.LEFT).
//                addParagraph().
//                addText("AMOUNT", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("$ 15.00").
//                setAlign(Paint.Align.LEFT).
//                addParagraph().
//                addText("TIP", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("$        ").
//                addLine(180).
//                setAlign(Paint.Align.LEFT).
//                addParagraph().
//                addText("TOTAL", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("$        ").
//                addLine(180).
//                addParagraph().
//                setAlign(Paint.Align.CENTER).
//                addText("APPROVED").
//                addParagraph()
//                .addText("Terminal ID: 123456", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("1234").
//                setAlign(Paint.Align.LEFT).
//                addLine().
//                addText("08/15/16", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("SERVER #4").
//                setAlign(Paint.Align.LEFT).
//                addParagraph().
//                addText("CHASE VISA - INSERT").
//                addText("AID: A000000000011111").
//                addText("ACCT #: *********1111").
//                addParagraph().
//                addText("CREDIT SALE").
//                addText("UID: 12345678", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("REF #: 1234").
//                setAlign(Paint.Align.LEFT).
//                addText("BATCH #: 091", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("AUTH #: 0701C").
//                setAlign(Paint.Align.LEFT).
//                addParagraph().
//                addText("AMOUNT", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("$ 15.00").
//                setAlign(Paint.Align.LEFT).
//                addParagraph().
//                addText("TIP", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("$        ").
//                addLine(180).
//                setAlign(Paint.Align.LEFT).
//                addParagraph().
//                addText("TOTAL", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("$        ").
//                addLine(180).
//                addParagraph().
//                setAlign(Paint.Align.CENTER).
//                addText("APPROVED").
//                addParagraph()
//                .addText("Terminal ID: 123456", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("1234").
//                setAlign(Paint.Align.LEFT).
//                addLine().
//                addText("08/15/16", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("SERVER #4").
//                setAlign(Paint.Align.LEFT).
//                addParagraph().
//                addText("CHASE VISA - INSERT").
//                addText("AID: A000000000011111").
//                addText("ACCT #: *********1111").
//                addParagraph().
//                addText("CREDIT SALE").
//                addText("UID: 12345678", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("REF #: 1234").
//                setAlign(Paint.Align.LEFT).
//                addText("BATCH #: 091", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("AUTH #: 0701C").
//                setAlign(Paint.Align.LEFT).
//                addParagraph().
//                addText("AMOUNT", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("$ 15.00").
//                setAlign(Paint.Align.LEFT).
//                addParagraph().
//                addText("TIP", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("$        ").
//                addLine(180).
//                setAlign(Paint.Align.LEFT).
//                addParagraph().
//                addText("TOTAL", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("$        ").
//                addLine(180).
//                addParagraph().
//                setAlign(Paint.Align.CENTER).
//                addText("APPROVED").
//                addParagraph()
//                .addText("Terminal ID: 123456", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("1234").
//                setAlign(Paint.Align.LEFT).
//                addLine().
//                addText("08/15/16", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("SERVER #4").
//                setAlign(Paint.Align.LEFT).
//                addParagraph().
//                addText("CHASE VISA - INSERT").
//                addText("AID: A000000000011111").
//                addText("ACCT #: *********1111").
//                addParagraph().
//                addText("CREDIT SALE").
//                addText("UID: 12345678", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("REF #: 1234").
//                setAlign(Paint.Align.LEFT).
//                addText("BATCH #: 091", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("AUTH #: 0701C").
//                setAlign(Paint.Align.LEFT).
//                addParagraph().
//                addText("AMOUNT", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("$ 15.00").
//                setAlign(Paint.Align.LEFT).
//                addParagraph().
//                addText("TIP", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("$        ").
//                addLine(180).
//                setAlign(Paint.Align.LEFT).
//                addParagraph().
//                addText("TOTAL", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("$        ").
//                addLine(180).
//                addParagraph().
//                setAlign(Paint.Align.CENTER).
//                addText("APPROVED").
//                addParagraph()
//                .addText("Terminal ID: 123456", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("1234").
//                setAlign(Paint.Align.LEFT).
//                addLine().
//                addText("08/15/16", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("SERVER #4").
//                setAlign(Paint.Align.LEFT).
//                addParagraph().
//                addText("CHASE VISA - INSERT").
//                addText("AID: A000000000011111").
//                addText("ACCT #: *********1111").
//                addParagraph().
//                addText("CREDIT SALE").
//                addText("UID: 12345678", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("REF #: 1234").
//                setAlign(Paint.Align.LEFT).
//                addText("BATCH #: 091", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("AUTH #: 0701C").
//                setAlign(Paint.Align.LEFT).
//                addParagraph().
//                addText("AMOUNT", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("$ 15.00").
//                setAlign(Paint.Align.LEFT).
//                addParagraph().
//                addText("TIP", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("$        ").
//                addLine(180).
//                setAlign(Paint.Align.LEFT).
//                addParagraph().
//                addText("TOTAL", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("$        ").
//                addLine(180).
//                addParagraph().
//                setAlign(Paint.Align.CENTER).
//                addText("APPROVED").
//                addParagraph()
//                .addText("Terminal ID: 123456", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("1234").
//                setAlign(Paint.Align.LEFT).
//                addLine().
//                addText("08/15/16", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("SERVER #4").
//                setAlign(Paint.Align.LEFT).
//                addParagraph().
//                addText("CHASE VISA - INSERT").
//                addText("AID: A000000000011111").
//                addText("ACCT #: *********1111").
//                addParagraph().
//                addText("CREDIT SALE").
//                addText("UID: 12345678", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("REF #: 1234").
//                setAlign(Paint.Align.LEFT).
//                addText("BATCH #: 091", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("AUTH #: 0701C").
//                setAlign(Paint.Align.LEFT).
//                addParagraph().
//                addText("AMOUNT", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("$ 15.00").
//                setAlign(Paint.Align.LEFT).
//                addParagraph().
//                addText("TIP", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("$        ").
//                addLine(180).
//                setAlign(Paint.Align.LEFT).
//                addParagraph().
//                addText("TOTAL", false).
//                setAlign(Paint.Align.RIGHT).
//                addText("$        ").
//                addLine(180).
//                addParagraph().
//                setAlign(Paint.Align.CENTER).
//                addText("APPROVED").
//                addParagraph();
//        Bitmap aw = struk.build();
//        if (aw != null) {
//            coba.setImageBitmap(aw);
//        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
