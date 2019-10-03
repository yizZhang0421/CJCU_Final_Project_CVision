package com.cjcu.ima105.cvision;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProgressTerminateBottomSheet extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.progress_terminate_bottom_sheet, container, false);
        v.findViewById(R.id.button_delete_progress_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getArguments().getInt("position");
                String model_id = getArguments().getString("model");
                try {
                    ProgressListRecycleViewAdapter.progressList.remove(position);
                    ProgressListRecycleViewAdapter.threads.remove(model_id);
                    ProgressListRecycleViewAdapter.progressListRecycleViewAdapterContext.notifyItemRemoved(position);
                    ProgressListRecycleViewAdapter.progressListRecycleViewAdapterContext.notifyItemRangeChanged(position, ProgressListRecycleViewAdapter.progressListRecycleViewAdapterContext.progressList.size());
                }catch (Exception e){}

                ServerOperate.terminate_train(CurrentPosition.key, model_id);
                ProgressTerminateBottomSheet.this.dismiss();
            }
        });
        return v;
    }

}