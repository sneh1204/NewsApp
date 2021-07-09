package com.example.bonus;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class NewsSourcesFragment extends Fragment {

    private static final String CATEGORY = "category";

    private String category;

    RecyclerView newssources;

    INewSource am;

    public static NewsSourcesFragment newInstance(String category) {
        NewsSourcesFragment fragment = new NewsSourcesFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    interface INewSource{

        void sendNewsSourceRequest(MainActivity.APIResponse response, String cat);

        void sendNewsFragment(String source, String name);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof INewSource){
            am = (INewSource)context;
        }else{
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString(CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_sources, container, false);
        getActivity().setTitle(category + " News Sources");

        newssources = view.findViewById(R.id.news_source_view);
        newssources.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        newssources.setLayoutManager(llm);

        am.sendNewsSourceRequest(new MainActivity.APIResponse() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Services.NewsSource source = new Services.NewsSource(jsonObject);

                newssources.setAdapter(new RecyclerView.Adapter() {
                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_source_item, parent, false);
                        return new RecyclerView.ViewHolder(view){};
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        Services.Source source1 = source.sources.get(position);
                        TextView name = holder.itemView.findViewById(R.id.textView);
                        TextView desc = holder.itemView.findViewById(R.id.textView2);
                        name.setText(source1.name);
                        desc.setText(source1.description);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                am.sendNewsFragment(source1.id, source1.name);
                            }
                        });
                    }

                    @Override
                    public int getItemCount() {
                        return source.sources.size();
                    }
                });
            }

            @Override
            public void onError(JSONObject jsonObject) {
            }
        }, category.toLowerCase());

        return view;
    }
}