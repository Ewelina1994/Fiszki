package com.example.fiszki;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    class QuestionItemView extends RecyclerView.ViewHolder{
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
        }

        @Override
        public int getItemCount() {
            return mQuestionList.size();
        }
    }
}
