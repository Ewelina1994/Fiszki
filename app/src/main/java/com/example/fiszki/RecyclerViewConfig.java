package com.example.fiszki;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiszki.activityPanel.UpdateQuestionActivity;
import com.example.fiszki.entity.QuestionDTO;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewConfig {
    private Context mContext;
    private QuestionAdapter mQuestionAdapter;
    public void setConfig(RecyclerView recyclerView, Context context, List<QuestionDTO>questions, List<String>keys){
        mContext=context;
        mQuestionAdapter=new QuestionAdapter(questions, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mQuestionAdapter);
    }

    class QuestionItemView extends RecyclerView.ViewHolder {
        private TextView question;
        private TextView optionPLtxt;
        private TextView optionENtxt;
        private ImageView imageQuestion;

        private String key;

        public QuestionItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.question_list_item, parent, false));

            question= itemView.findViewById(R.id.questionTitle);
            optionPLtxt=itemView.findViewById(R.id.optionPL);
            optionENtxt=itemView.findViewById(R.id.optionEN);
            imageQuestion=itemView.findViewById(R.id.imageQuestion);
        }

        public void bind(QuestionDTO questionDTO, String key) {
            question.setText(questionDTO.getQuestion().getName());
            Uri uriImage=questionDTO.getQuestion().getUploadImageUri();
            if(uriImage!=null){
                Picasso.get().load(uriImage).into(imageQuestion);
            }

            questionDTO.getOptions().forEach(option->{
                if(option.getIs_right()==1 && option.getLanguage().equalsIgnoreCase("PL")){
                    optionPLtxt.setText(option.getName());
                }

                if(option.getIs_right()==1 && option.getLanguage().equalsIgnoreCase("EN")){
                    optionENtxt.setText(option.getName());
                }
            });
            this.key=key;

        }
    }

    class QuestionAdapter extends RecyclerView.Adapter<QuestionItemView>{
        private List<QuestionDTO> mQuestionList;
        private List<String> mKeys;

        public QuestionAdapter(List<QuestionDTO> mQuestionList, List<String> keys) {
            this.mQuestionList = mQuestionList;
            this.mKeys = keys;
        }

        @NonNull
        @Override
        public QuestionItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new QuestionItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull QuestionItemView holder, int position) {
            holder.bind(mQuestionList.get(position), mKeys.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog(mQuestionList.get(position), Integer.parseInt(mKeys.get(position)));
                }
            });
        }

        @Override
        public int getItemCount() {
            return mQuestionList.size();
        }
    }

    private void showDialog(QuestionDTO questionDTO, int key) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View promptView = layoutInflater.inflate(R.layout.alert_dialog, null);

        final AlertDialog alertD = new AlertDialog.Builder(mContext).create();
        Button btnUpdate = (Button) promptView.findViewById(R.id.btnUpdate);
        Button btnDelete = (Button) promptView.findViewById(R.id.btnDelete);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentUpdate=new Intent(mContext, UpdateQuestionActivity.class);
                intentUpdate.putExtra("question", (Parcelable) questionDTO);
                intentUpdate.putExtra("key", key);
                mContext.startActivity(intentUpdate);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteSHowDialog(questionDTO, key, alertD);

            }
        });
        alertD.setView(promptView);
        alertD.show();
    }

    private void deleteSHowDialog(QuestionDTO questionDTO,int key, AlertDialog dialogBig) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle(R.string.are_you_sure_you_want_to_delete);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                FirebaseConfiguration.deleteIdiom(key, mContext);
                StorageFirebase.deleteImage(questionDTO.getQuestion().getUploadImageUri());
                dialogBig.cancel();
            }
        });

        alertDialogBuilder.show().getButton(DialogInterface.BUTTON_POSITIVE).requestFocus();

    }
}
