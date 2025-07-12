package com.example.prm391_project_apprestaurants.controllers.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.adapters.RestaurantManagementAdapter;
import com.example.prm391_project_apprestaurants.controllers.fragments.SideBarFragment;
import com.example.prm391_project_apprestaurants.entities.Restaurant;
import com.example.prm391_project_apprestaurants.requests.SearchRestaurantRequest;
import com.example.prm391_project_apprestaurants.services.RestaurantService;

import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RestaurantManagementActivity extends AppCompatActivity {

    private RestaurantService restaurantService;
    private RecyclerView recyclerView;
    private RestaurantManagementAdapter adapter;
    private ImageButton imageMenuButton;
    private SideBarFragment sideBarFragment;
    private Spinner spinner;
    private Button searchButton;
    private Button createButton;
    private EditText editTextKeyword;
    private SearchRestaurantRequest searchRestaurantRequest;
    private TextView textViewPage;
    private ImageButton btnFirst;
    private ImageButton btnLast;
    private ImageButton btnPrevious;
    private ImageButton btnNext;
    private final Map<String , Integer> pageSizeMap = new LinkedHashMap<>() {{
        put("5", 5);
        put("10", 10);
        put("15", 15);
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_management);
        Initialize();
        RegisterEvents();
    }

    private void Initialize() {
        searchRestaurantRequest = new SearchRestaurantRequest();
        searchRestaurantRequest.setPage(1);
        searchRestaurantRequest.setPageSize(5);
        recyclerView = findViewById(R.id.recyclerViewRestaurants);
        restaurantService = new RestaurantService(this);
        adapter = new RestaurantManagementAdapter(restaurantService.getAllRestaurants(searchRestaurantRequest));
        recyclerView.setAdapter(adapter);
        searchButton = findViewById(R.id.btnSearch);
        editTextKeyword = findViewById(R.id.editTextSearch);
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        imageMenuButton = findViewById(R.id.imageMenuButton);
        textViewPage = findViewById(R.id.txtPage);
        btnFirst = findViewById(R.id.btnStart);
        btnLast = findViewById(R.id.btnEnd);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        createButton = findViewById(R.id.btnCreate);
        bindingPaginationData(searchRestaurantRequest);
        imageMenuButton.setOnClickListener(view -> {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
            sideBarFragment = SideBarFragment.newInstance("param1", "param2");
            fragmentTransaction.replace(R.id.fragment_container, sideBarFragment);
            fragmentTransaction.addToBackStack(this.getClass().getName());
            fragmentTransaction.commit();
        });
        spinner = findViewById(R.id.spinnerPageSize);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pageSizeMap.keySet().toArray(new String[0]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void RegisterEvents() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setPageSizeMap(parent, view, pageSizeMap.get(parent.getItemAtPosition(position)));
                loadRestaurantData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        searchButton.setOnClickListener(this::handleClickSearch);
        btnFirst.setOnClickListener(view -> {
            searchRestaurantRequest.setPage(1);
            loadRestaurantData();
        });
        btnLast.setOnClickListener(view ->{
            searchRestaurantRequest.setPage(searchRestaurantRequest.getTotalPage());
            loadRestaurantData();
        });
        btnPrevious.setOnClickListener(view -> {
            if (searchRestaurantRequest.getPage() > 1) searchRestaurantRequest.setPage(searchRestaurantRequest.getPage() - 1);
            loadRestaurantData();
        });
        btnNext.setOnClickListener(view -> {
            if (searchRestaurantRequest.getPage() < searchRestaurantRequest.getTotalPage()) searchRestaurantRequest.setPage(searchRestaurantRequest.getPage() + 1);
            loadRestaurantData();
        });
        createButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, CreateRestaurantDashboardActivity.class);
            startActivity(intent);
        });
    }

    private void setPageSizeMap(AdapterView<?> parent, View view, int pageSize) {
        searchRestaurantRequest.setPageSize(pageSize);
    }

    private void handleClickSearch(View view) {
        String keyword = editTextKeyword.getText().toString();
        searchRestaurantRequest.setKeyword(keyword);
        loadRestaurantData();
    }

    public void loadRestaurantData(){
        List<Restaurant> restaurants = restaurantService.getAllRestaurants(searchRestaurantRequest);
        adapter.setRestaurants(restaurants);
        bindingPaginationData(searchRestaurantRequest);
    }

    private void bindingPaginationData(SearchRestaurantRequest searchRestaurantRequest) {
        textViewPage.setText("Page " + searchRestaurantRequest.getPage() + " of " + searchRestaurantRequest.getTotalPage());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
           loadRestaurantData();
        }
    }


}