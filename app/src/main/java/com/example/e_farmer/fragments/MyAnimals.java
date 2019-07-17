package com.example.e_farmer.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.e_farmer.adapters.MyAnimalAdapter;
import com.example.e_farmer.models.Animals;
import com.example.e_farmer.viewmodels.MyAnimalViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.e_farmer.AddAnimal;
import com.example.e_farmer.IMainActivity;
import com.example.e_farmer.R;

import java.util.List;

public class MyAnimals extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private FloatingActionButton fab;
    private MyAnimalViewModel myAnimalViewModel;
    private MyAnimalAdapter myAnimalAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView searchView;

    IMainActivity iMainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        iMainActivity = (IMainActivity) this.getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iMainActivity.setToolbarTitle(getTag());

        //        instantiate the viewmodel class here
        myAnimalViewModel = ViewModelProviders.of(this).get(MyAnimalViewModel.class);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_animals, container, false);

        mRecyclerView = view.findViewById(R.id.animal_recyclerview);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddAnimal.class);
                startActivity(intent);
            }
        });
        searchView = view.findViewById(R.id.searchview);
        swipeRefreshLayout = view.findViewById(R.id.animal_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        initSearchView();
        initRecyclerView();
        myAnimalViewModel.getAllAnimals().observe(this, new Observer<List<Animals>>() {
            @Override
            public void onChanged(List<Animals> animals) {
                myAnimalAdapter.setUpdatedData(animals);
                myAnimalAdapter.notifyDataSetChanged();

            }
        });

        return view;
    }
    private void initSearchView() {
        //Associate searchable configuration with the searchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        //listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                myAnimalAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myAnimalAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }


    private void initRecyclerView() {
        myAnimalAdapter = new MyAnimalAdapter(getContext());
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(myAnimalAdapter);
    }

    @Override
    public void onRefresh() {

        myAnimalAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}
