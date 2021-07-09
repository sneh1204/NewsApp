package com.example.bonus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class NewsFragment extends Fragment {

    private static final String SOURCE = "source";
    private static final String NAME = "name";

    private String source, name;

    RecyclerView everything_list;

    INFrag am;

    public static NewsFragment newInstance(String source, String name) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(SOURCE, source);
        args.putString(NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    interface INFrag{

        void sendEverythingRequest(MainActivity.APIResponse response, String source);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof INFrag){
            am = (INFrag)context;
        }else{
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            source = getArguments().getString(SOURCE);
            name = getArguments().getString(NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        getActivity().setTitle(name);

        everything_list = view.findViewById(R.id.everything_list);
        everything_list.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        everything_list.setLayoutManager(llm);


        am.sendEverythingRequest(new MainActivity.APIResponse() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Services.Articles articles = new Services.Articles(jsonObject);

                everything_list.setAdapter(new RecyclerView.Adapter() {
                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.source_item, parent, false);
                        return new RecyclerView.ViewHolder(view){};
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        Services.News news = articles.news.get(position);
                        TextView title = holder.itemView.findViewById(R.id.textView3);
                        TextView desc = holder.itemView.findViewById(R.id.textView4);
                        TextView author = holder.itemView.findViewById(R.id.textView5);
                        TextView pub_date = holder.itemView.findViewById(R.id.textView6);
                        title.setText(news.title.substring(0, Math.min(news.title.length(), 25)));
                        desc.setText(news.description.substring(0, Math.min(news.description.length(), 100)) + "... ");
                        author.setText(news.author.substring(0, Math.min(news.author.length(), 25)));
                        pub_date.setText(news.pub_date);

                        if(news.urlToImage != null)
                            Picasso.get().load(news.urlToImage).into((ImageView) holder.itemView.findViewById(R.id.imageView));

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.url));
                                startActivity(browserIntent);
                            }
                        });
                    }

                    @Override
                    public int getItemCount() {
                        return articles.news.size();
                    }
                });
            }

            @Override
            public void onError(JSONObject jsonObject) {
            }
        }, source);
        return view;
    }
}