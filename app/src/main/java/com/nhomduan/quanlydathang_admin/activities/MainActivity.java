package com.nhomduan.quanlydathang_admin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.fragment.DanhSachLoaiSanPhamFragment;
import com.nhomduan.quanlydathang_admin.fragment.DanhSachSanPhamFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        replaceFragment(new DanhSachSanPhamFragment());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_tool) {
            mDrawerLayout.openDrawer(GravityCompat.END);
        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_sanPham:
                replaceFragment(new DanhSachSanPhamFragment());
                break;
            case R.id.nav_loaiSP:
                replaceFragment(new DanhSachLoaiSanPhamFragment());
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.END);

        return true;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contentFrame, fragment, fragment.getClass().getSimpleName())
                .commit();
    }
}