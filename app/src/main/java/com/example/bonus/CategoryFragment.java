package com.example.bonus;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;


public class CategoryFragment extends Fragment {

    ICat am;

    ListView cat_view;

    interface ICat{

        void sendNewsSourceFragment(String category);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ICat){
            am = (ICat)context;
        }else{
            throw new RuntimeException(context.toString());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        getActivity().setTitle("News Source Categories");
        cat_view = view.findViewById(R.id.cat_View);
        ArrayList<String> cats = new ArrayList<>();
        for (NewsData.Category category : NewsData.categories){
            cats.add(category.name);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, cats);
        cat_view.setAdapter(adapter);
        cat_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                am.sendNewsSourceFragment(NewsData.categories.get(i).name);
            }
        });
        return view;
    }
}