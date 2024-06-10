package com.example.cardealership;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import android.content.Context;

public class UserManualFragment extends Fragment {

    interface OnUserManualInteractionListener {
        void onUserManualInteraction();
    }

    private OnUserManualInteractionListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserManualInteractionListener) {
            mListener = (OnUserManualInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserManualInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_user_manual_fragment, container, false);
    }
}
