package com.example.cardealership;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class AboutAppFragment extends Fragment {

    interface OnAboutAppInteractionListener {
        void onAboutAppInteraction();
    }

    private OnAboutAppInteractionListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAboutAppInteractionListener) {
            mListener = (OnAboutAppInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAboutAppInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_about_app_fragment, container, false);

        // Найдем элементы, которые будем анимировать
        TextView textViewTitle = view.findViewById(R.id.textViewAboutApp);
        TextView textViewDescription = view.findViewById(R.id.textViewAuthor);

        // Загрузим анимацию
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up_animation);

        // Применим анимацию к элементам
        textViewTitle.startAnimation(animation);
        textViewDescription.startAnimation(animation);

        return view;
    }
}
