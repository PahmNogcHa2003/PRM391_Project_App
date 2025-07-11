package com.example.prm391_project_apprestaurants.controllers.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.adapters.UserManagementAdapter;
import com.example.prm391_project_apprestaurants.controllers.fragments.SideBarFragment;
import com.example.prm391_project_apprestaurants.databinding.ActivityUserManagementBinding;
import com.example.prm391_project_apprestaurants.entities.Restaurant;
import com.example.prm391_project_apprestaurants.entities.User;
import com.example.prm391_project_apprestaurants.requests.SearchUserRequest;
import com.example.prm391_project_apprestaurants.services.UserService;

import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserManagementActivity extends AppCompatActivity {
    private ActivityUserManagementBinding binding;
    private UserService userService;
    private SearchUserRequest request;
    private UserManagementAdapter adapter;
    private final Map<String , Integer> pageSizeMap = new LinkedHashMap<>() {{
        put("5", 5);
        put("10", 10);
        put("15", 15);
    }};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_management);
        Initialize();
        BindingData();
        registerEvents();
    }

    public void Initialize() {
        userService = new UserService(this);
        request = new SearchUserRequest();
        request.setPage(1);
        request.setPageSize(5);
    }

    public void BindingData() {
        binding.recyclerViewUsers.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        adapter = new UserManagementAdapter(userService.getAllUsers(request));
        binding.recyclerViewUsers.setAdapter(adapter);
        binding.setViewModel(request);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pageSizeMap.keySet().toArray(new String[0]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPageSize.setAdapter(adapter);
    }

    public void registerEvents(){
        binding.btnNext.setOnClickListener(v -> {
            request.setPage(Math.min(request.getPage() + 1, request.getTotalPage()));
            loadUserData();
        });
        binding.btnPrevious.setOnClickListener(v -> {
            request.setPage(Math.max(request.getPage() - 1, 1));
            loadUserData();
        });
        binding.btnStart.setOnClickListener(v -> {
            request.setPage(1);
            loadUserData();
        });
        binding.btnEnd.setOnClickListener(v -> {
            request.setPage(request.getTotalPage());
            loadUserData();
        });
        binding.imageMenuButton.setOnClickListener(view -> {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
            SideBarFragment sideBarFragment = SideBarFragment.newInstance("param1", "param2");
            fragmentTransaction.replace(R.id.fragment_container, sideBarFragment);
            fragmentTransaction.addToBackStack(this.getClass().getName());
            fragmentTransaction.commit();
        });
        binding.spinnerPageSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer pageSize = pageSizeMap.get(parent.getItemAtPosition(position).toString());
                if (pageSize != null) {
                    request.setPageSize(pageSize);
                    loadUserData();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        binding.btnSearch.setOnClickListener(v -> {
            request.setKeyword(binding.editTextSearch.getText().toString());
            loadUserData();
        });
    }

    public void loadUserData(){
        List<User> users = userService.getAllUsers(request);
        adapter.setUsers(users);
        binding.setViewModel(request);
    }
}