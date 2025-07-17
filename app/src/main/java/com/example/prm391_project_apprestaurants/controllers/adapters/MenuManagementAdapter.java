package com.example.prm391_project_apprestaurants.controllers.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391_project_apprestaurants.controllers.admin.MenuDetailDashboardActivity;
import com.example.prm391_project_apprestaurants.controllers.admin.MenuManagementActivity;
import com.example.prm391_project_apprestaurants.controllers.admin.UpdateMenuDashboardActivity;
import com.example.prm391_project_apprestaurants.controllers.viewholders.BindingViewHolder;
import com.example.prm391_project_apprestaurants.databinding.ViewholderMenuItemBinding;
import com.example.prm391_project_apprestaurants.entities.Menu;
import com.example.prm391_project_apprestaurants.services.MenuService;
import com.example.prm391_project_apprestaurants.utils.UtilHelper;

import java.util.List;

public class MenuManagementAdapter extends RecyclerView.Adapter<BindingViewHolder<ViewholderMenuItemBinding>> {
    private List<Menu> menuList;

    private MenuService menuService;

    public MenuManagementAdapter(List<Menu> menuList) {
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public BindingViewHolder<ViewholderMenuItemBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        menuService = new MenuService(parent.getContext());
        ViewholderMenuItemBinding binding = ViewholderMenuItemBinding.inflate(inflater, parent, false);
        return new BindingViewHolder<>(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder<ViewholderMenuItemBinding> holder, int position) {
        Menu menu = menuList.get(position);
        holder.getBinding().setViewHolder(menu);
        holder.getBinding().cardViewMenu.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MenuDetailDashboardActivity.class);
            intent.putExtra("menuId", menu.getId());
            v.getContext().startActivity(intent);
            ((Activity) v.getContext()).finish();
        });
        holder.getBinding().buttonEdit.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), UpdateMenuDashboardActivity.class);
            intent.putExtra("menuId", menu.getId());
            v.getContext().startActivity(intent);
            ((Activity) v.getContext()).finish();
        });
        holder.getBinding().buttonDelete.setOnClickListener(v -> {
            Context context = v.getContext();
            AlertDialog alertDialog = UtilHelper.createDialog(context, "Xóa món ăn", "Bạn có chắc muốn xóa món ăn này?", "Không", "Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean isDeleted = menuService.updateStatusMenu(menu.getId(), 1);
                            if (isDeleted) {
                                MenuManagementActivity activity = (MenuManagementActivity) context;
                                activity.loadMenuData();
                                Toast.makeText(context, "Xóa món ăn thành công", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Xóa món ăn thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        });
        holder.getBinding().buttonActivate.setOnClickListener(v -> {
            Context context = v.getContext();
            AlertDialog alertDialog = UtilHelper.createDialog(context, "Kích hoạt món ăn", "Bạn có chắc muốn kích hoạt món ăn này?", "Không", "Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean isActivated = menuService.updateStatusMenu(menu.getId(), 0);
                            if (isActivated) {
                                MenuManagementActivity activity = (MenuManagementActivity) context;
                                activity.loadMenuData();
                                Toast.makeText(context, "Kích hoạt món ăn thành công", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Kích hoạt món ăn thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        });
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMenus(List<Menu> menus) {
        this.menuList.clear();
        this.menuList.addAll(menus);
        notifyDataSetChanged();
    }
}
