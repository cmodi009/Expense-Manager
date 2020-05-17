package com.example.expensemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.expensemanager.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mExpenseDatabase;

    private RecyclerView recyclerView;
    private TextView expenseSumResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview=  inflater.inflate(R.layout.fragment_expense, container, false);
        mAuth=FirebaseAuth.getInstance();

        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();

        mExpenseDatabase= FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);

        expenseSumResult=myview.findViewById(R.id.expense_txt_result);

        recyclerView=myview.findViewById(R.id.recycler_id_expense);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int expenseSum=0;

                for (DataSnapshot mysanapshot:dataSnapshot.getChildren()){

                    Data data=mysanapshot.getValue(Data.class);
                    expenseSum+=data.getAmount();
                    String strExpensesum=String.valueOf(expenseSum);

                    expenseSumResult.setText(strExpensesum+".00");

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return myview;
    }
    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data,MyViewHolder> adapter=new FirebaseRecyclerAdapter<Data, MyViewHolder>
                (
                        Data.class,
                        R.layout.expense_recycler_data,
                        MyViewHolder.class,
                        mExpenseDatabase
                ) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, final Data model, final int position) {

                viewHolder.setDate(model.getDate());
                viewHolder.setType(model.getType());
                viewHolder.setNote(model.getNote());
                viewHolder.setAmount(model.getAmount());

            }
        };
        recyclerView.setAdapter(adapter);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        private void setDate(String date){
            TextView mDate=mView.findViewById(R.id.date_txt_expense);
            mDate.setText(date);
        }

        private void setType(String type){
            TextView mType=mView.findViewById(R.id.type_txt_expense);
            mType.setText(type);
        }

        private void setNote(String note){
            TextView mNote=mView.findViewById(R.id.note_txt_expense);
            mNote.setText(note);
        }

        private void setAmount(int amount){
            TextView Amount=mView.findViewById(R.id.amount_txt_expense);
            String stramount=String.valueOf(amount);
            Amount.setText(stramount);

        }

    }
}
