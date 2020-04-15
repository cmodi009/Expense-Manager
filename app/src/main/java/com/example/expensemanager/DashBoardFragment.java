package com.example.expensemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashBoardFragment extends Fragment {

    private FloatingActionButton fab_main;
    private FloatingActionButton fab_income;
    private FloatingActionButton fab_expense;

    private TextView fab_income_txt,fab_expense_txt;

    private boolean isOpen=false;

    private Animation fadeopen,fadeclose;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview= inflater.inflate(R.layout.fragment_dash_board, container, false);


        fab_main=myview.findViewById(R.id.fb_main_plus_btn);
        fab_income=myview.findViewById(R.id.income_Ft_btn);
        fab_expense=myview.findViewById(R.id.expense_Ft_btn);

        fab_income_txt=myview.findViewById(R.id.income_ft_text);
        fab_expense_txt=myview.findViewById(R.id.expense_ft_text);

        fadeopen = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        fadeclose=AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen)
                {
                    fab_income.startAnimation(fadeclose);
                    fab_expense.startAnimation(fadeclose);
                    fab_expense.setClickable(false);
                    fab_income.setClickable(false);

                    fab_income_txt.startAnimation(fadeclose);
                    fab_expense_txt.startAnimation(fadeclose);
                    fab_expense_txt.setClickable(false);
                    fab_income_txt.setClickable(false);

                    isOpen=false;
                }
                else {
                    fab_income.startAnimation(fadeopen);
                    fab_expense.startAnimation(fadeopen);
                    fab_expense.setClickable(true);
                    fab_income.setClickable(true);

                    fab_income_txt.startAnimation(fadeopen);
                    fab_expense_txt.startAnimation(fadeopen);
                    fab_expense_txt.setClickable(true);
                    fab_income_txt.setClickable(true);

                    isOpen=true;

                }


            }
        });



        return myview;
    }
}
