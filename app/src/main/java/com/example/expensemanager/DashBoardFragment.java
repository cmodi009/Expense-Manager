package com.example.expensemanager;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensemanager.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

import java.text.DateFormat;
import java.util.Date;


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

    private TextView totalincome,totalexpense;
    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase,mExpenseDatabase;

    private RecyclerView IncomeRecycler,ExpenseRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview= inflater.inflate(R.layout.fragment_dash_board, container, false);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();

        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDatabase=FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);
        mIncomeDatabase.keepSynced(true);
        mExpenseDatabase.keepSynced(true);

        fab_main=myview.findViewById(R.id.fb_main_plus_btn);
        fab_income=myview.findViewById(R.id.income_Ft_btn);
        fab_expense=myview.findViewById(R.id.expense_Ft_btn);

        fab_income_txt=myview.findViewById(R.id.income_ft_text);
        fab_expense_txt=myview.findViewById(R.id.expense_ft_text);

        totalincome = myview.findViewById(R.id.income_set_result);
        totalexpense = myview.findViewById(R.id.expense_set_result);

        fadeopen = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        fadeclose=AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
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

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int total=0;

                for(DataSnapshot mysnapshot:dataSnapshot.getChildren())
                {
                    Data data =mysnapshot.getValue(Data.class);
                    total+=data.getAmount();
                    String result = String.valueOf(total);
                    totalincome.setText(result+".00");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int total=0;
                for(DataSnapshot mysnapshot1:dataSnapshot.getChildren())
                {
                    Data data =mysnapshot1.getValue(Data.class);
                    total+=data.getAmount();
                    String result = String.valueOf(total);
                    totalexpense.setText(result+".00");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        IncomeRecycler = myview.findViewById(R.id.recycler_income);
        ExpenseRecycler=myview.findViewById(R.id.recycler_expense);

        LinearLayoutManager layoutManagerIncome  = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerIncome.setReverseLayout(true);
        layoutManagerIncome.setStackFromEnd(true);
        IncomeRecycler.setHasFixedSize(true);
        IncomeRecycler.setLayoutManager(layoutManagerIncome);

        LinearLayoutManager layoutManagerExpense  = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerExpense.setReverseLayout(true);
        layoutManagerExpense.setStackFromEnd(true);
        ExpenseRecycler.setHasFixedSize(true);
        ExpenseRecycler.setLayoutManager(layoutManagerIncome);

        return myview;
    }

    private void ftAnimation(){
        if (isOpen){

            fab_income.startAnimation(fadeclose);
            fab_expense.startAnimation(fadeclose);
            fab_income.setClickable(false);
            fab_expense.setClickable(false);

            fab_income_txt.startAnimation(fadeclose);
            fab_expense_txt.startAnimation(fadeclose);
            fab_income_txt.setClickable(false);
            fab_expense_txt.setClickable(false);
            isOpen=false;

        }else {
            fab_income.startAnimation(fadeopen);
            fab_expense.startAnimation(fadeopen);
            fab_income.setClickable(true);
            fab_expense.setClickable(true);

            fab_income_txt.startAnimation(fadeopen);
            fab_expense_txt.startAnimation(fadeopen);
            fab_income_txt.setClickable(true);
            fab_expense_txt.setClickable(true);
            isOpen=true;

        }

    }


    private void addData()
    {
        fab_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeDataInsert();
            }
        });

        fab_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseDataInsert();
            }
        });

    }

    public void incomeDataInsert(){

        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myviewm=inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        mydialog.setView(myviewm);
        final AlertDialog dialog=mydialog.create();

        dialog.setCancelable(false);

        final EditText edtAmount=myviewm.findViewById(R.id.amount);
        final EditText edtType=myviewm.findViewById(R.id.type);
        final EditText edtNote=myviewm.findViewById(R.id.note);

        Button btnSave=myviewm.findViewById(R.id.btnSave);
        Button btnCancel=myviewm.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String type=edtType.getText().toString().trim();
                String amount=edtAmount.getText().toString().trim();
                String note=edtNote.getText().toString().trim();

                if (TextUtils.isEmpty(type)){
                    edtType.setError("Required Field..");
                    return;
                }

                if (TextUtils.isEmpty(amount)){
                    edtAmount.setError("Required Field..");
                    return;
                }

                int ouramountint=Integer.parseInt(amount);

                if (TextUtils.isEmpty(note)){
                    edtNote.setError("Required Field..");
                    return;
                }

                String id=mIncomeDatabase.push().getKey();

                String mDate=DateFormat.getDateInstance().format(new Date());

                Data data=new Data(ouramountint,type,note,id,mDate);

                mIncomeDatabase.child(id).setValue(data);

                Toast.makeText(getActivity(),"Data ADDED",Toast.LENGTH_SHORT).show();

                ftAnimation();
                dialog.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftAnimation();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void expenseDataInsert(){
        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myview = inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        mydialog.setView(myview);

        final AlertDialog dialog = mydialog.create();
        dialog.setCancelable(false);

        final EditText amount = myview.findViewById(R.id.amount);
        final EditText type = myview.findViewById(R.id.type);
        final EditText note = myview.findViewById(R.id.note);

        Button btnSave = myview.findViewById(R.id.btnSave);
        Button btnCancel  = myview.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmAmount = amount.getText().toString().trim();
                String tmType = type.getText().toString().trim();
                String tmNote = note.getText().toString().trim();

                if (TextUtils.isEmpty(tmType)){
                    type.setError("Required Field..");
                    return;
                }

                if (TextUtils.isEmpty(tmAmount)){
                    amount.setError("Required Field..");
                    return;
                }

                int ouramountint=Integer.parseInt(tmAmount);

                if (TextUtils.isEmpty(tmNote)){
                    note.setError("Required Field..");
                    return;
                }

                String id=mIncomeDatabase.push().getKey();

                String mDate=DateFormat.getDateInstance().format(new Date());

                Data data=new Data(ouramountint,tmType,tmNote,id,mDate);

                mExpenseDatabase.child(id).setValue(data);
                Toast.makeText(getActivity(),"Data added",Toast.LENGTH_SHORT).show();

                ftAnimation();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftAnimation();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data,IncomeViewHolder> incomeAdapter=new FirebaseRecyclerAdapter<Data, IncomeViewHolder>
                (
                        Data.class,
                        R.layout.dashboard_income,
                        DashBoardFragment.IncomeViewHolder.class,
                        mIncomeDatabase
                ) {
            @Override
            protected void populateViewHolder(IncomeViewHolder viewHolder, Data model, int position) {

                viewHolder.setIncomeType(model.getType());
                viewHolder.setIncomeAmount(model.getAmount());
                viewHolder.setIncomeDate(model.getDate());

            }
        };
        IncomeRecycler.setAdapter(incomeAdapter);

        FirebaseRecyclerAdapter<Data,ExpenseViewHolder>expenseAdapter=new FirebaseRecyclerAdapter<Data, ExpenseViewHolder>
                (
                        Data.class,
                        R.layout.dashboart_expense,
                        DashBoardFragment.ExpenseViewHolder.class,
                        mExpenseDatabase
                ) {
            @Override
            protected void populateViewHolder(ExpenseViewHolder viewHolder, Data model, int position) {

                viewHolder.setExpenseType(model.getType());
                viewHolder.setExpenseAmount(model.getAmount());
                viewHolder.setExpenseDate(model.getDate());

            }
        };

        ExpenseRecycler.setAdapter(expenseAdapter);

    }

    public static class IncomeViewHolder extends RecyclerView.ViewHolder{
        View IncomeView;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            IncomeView = itemView;
        }
        public void setIncomeType(String type)
        {
            TextView mtype= IncomeView.findViewById(R.id.type_Income_ds);
            mtype.setText(type);
        }
        public void setIncomeAmount(int amount){

            TextView mAmount=IncomeView.findViewById(R.id.amount_income_ds);
            String strAmount=String.valueOf(amount);
            mAmount.setText(strAmount);
        }

        public void setIncomeDate(String date){

            TextView mDate=IncomeView.findViewById(R.id.date_income_ds);
            mDate.setText(date);

        }
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder{

        View ExpenseView;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            ExpenseView=itemView;
        }

        public void setExpenseType(String type){
            TextView mtype=ExpenseView.findViewById(R.id.type_expense_ds);
            mtype.setText(type);
        }

        public void setExpenseAmount(int amount){
            TextView mAmount = ExpenseView.findViewById(R.id.amount_expense_ds);
            String strAmount=String.valueOf(amount);
            mAmount.setText(strAmount);
        }

        public void setExpenseDate(String date){
            TextView mDate=ExpenseView.findViewById(R.id.date_expense_ds);
            mDate.setText(date);
        }
    }
}
