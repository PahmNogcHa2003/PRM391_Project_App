package com.example.prm391_project_apprestaurants.controllers.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.prm391_project_apprestaurants.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;

import com.example.prm391_project_apprestaurants.controllers.adapters.MenuManagementAdapter;
import com.example.prm391_project_apprestaurants.controllers.fragments.SideBarFragment;
import com.example.prm391_project_apprestaurants.databinding.ActivityMenuManagementBinding;
import com.example.prm391_project_apprestaurants.entities.Menu;
import com.example.prm391_project_apprestaurants.entities.Restaurant;
import com.example.prm391_project_apprestaurants.requests.SearchMenuRequest;
import com.example.prm391_project_apprestaurants.services.MenuService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MenuManagementActivity extends AppCompatActivity {

    private ActivityMenuManagementBinding binding;
    private MenuService menuService;
    private SearchMenuRequest request;

    private MenuManagementAdapter adapter;

    private final Map<String , Integer> pageSizeMap = new LinkedHashMap<>() {{
        put("5", 5);
        put("10", 10);
        put("15", 15);
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            binding = DataBindingUtil.setContentView(this, R.layout.activity_menu_management);
            Initialize();
            BindingData();
            RegisterEvents();
        }catch (Exception e){
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
        }
    }
    private void Initialize() {
        menuService = new MenuService(this);
        request = new SearchMenuRequest();
        request.setPage(1);
        request.setPageSize(5);
    }
    private void BindingData() {
        adapter = new MenuManagementAdapter(menuService.getAllMenu(request));
        binding.recyclerViewMenus.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        binding.recyclerViewMenus.setAdapter(adapter);
        binding.setViewModel(request);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pageSizeMap.keySet().toArray(new String[0]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPageSize.setAdapter(adapter);
    }
    private void RegisterEvents() {
        binding.spinnerPageSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setPageSizeMap(parent, view, pageSizeMap.get(parent.getItemAtPosition(position)));
                loadMenuData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        binding.imageMenuButton.setOnClickListener(view -> {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
            SideBarFragment sideBarFragment = SideBarFragment.newInstance("param1", "param2");
            fragmentTransaction.replace(R.id.fragment_container, sideBarFragment);
            fragmentTransaction.addToBackStack(this.getClass().getName());
            fragmentTransaction.commit();
        });
        binding.btnNext.setOnClickListener(v -> {
            request.setPage(Math.min(request.getPage() + 1, request.getTotalPage()));
            loadMenuData();
        });
        binding.btnPrevious.setOnClickListener(v -> {
            request.setPage(Math.max(request.getPage() - 1, 1));
            loadMenuData();
        });
        binding.btnStart.setOnClickListener(v -> {
            request.setPage(1);
            loadMenuData();
        });
        binding.btnEnd.setOnClickListener(v -> {
            request.setPage(request.getTotalPage());
            loadMenuData();
        });
        binding.btnSearch.setOnClickListener(v -> {
            request.setKeyword(binding.editTextSearch.getText().toString());
            loadMenuData();
        });
        binding.btnCreate.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateMenuDashboardActivity.class);
            startActivity(intent);
        });
    }

    private void setPageSizeMap(AdapterView<?> parent, View view, int pageSize) {
        request.setPageSize(pageSize);
    }
    public void loadMenuData(){
        List<Menu> menus = menuService.getAllMenu(request);
        adapter.setMenus(menus);
        binding.setViewModel(request);
    }


}