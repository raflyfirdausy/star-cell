package com.rfl.trn.starr_cell.Custom;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rfl.trn.starr_cell.Custom.MyTextView;
import com.rfl.trn.starr_cell.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BottomSheetDialogFotoKonter extends BottomSheetDialogFragment {


    @BindView(R.id.tv_btn_add_photo_camera)
    MyTextView tvBtnAddPhotoCamera;
    @BindView(R.id.tv_btn_add_photo_gallery)
    MyTextView tvBtnAddPhotoGallery;
    @BindView(R.id.tv_btn_hapus_foto)
    MyTextView tvBtnHapusFoto;
    Unbinder unbinder;
    private BottomSheetListener mBottomSheetListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_bottom_sheet_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        tvBtnAddPhotoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetListener.onOptionClick("kamera");
                dismiss();
            }
        });
        tvBtnAddPhotoGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetListener.onOptionClick("gallery");
                dismiss();
            }
        });
        tvBtnHapusFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetListener.onOptionClick("hapus");
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mBottomSheetListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    public interface BottomSheetListener {
        void onOptionClick(String text);
    }
}
