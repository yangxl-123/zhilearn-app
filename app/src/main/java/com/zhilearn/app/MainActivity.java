package com.zhilearn.app;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zhilearn.app.data.DatabaseInitializer;
import com.zhilearn.app.fragment.HomeFragment;
import com.zhilearn.app.fragment.LearnFragment;
import com.zhilearn.app.fragment.ReviewFragment;
import com.zhilearn.app.fragment.StatsFragment;
import com.zhilearn.app.fragment.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private ProgressBar initProgress;
    private View fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);
        initProgress = findViewById(R.id.init_progress);
        fragmentContainer = findViewById(R.id.fragment_container);

        // 默认先放首页，避免空容器
        if (savedInstanceState == null) {
            switchFragment(new HomeFragment());
        }

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                switchFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.nav_learn) {
                switchFragment(new LearnFragment());
                return true;
            } else if (itemId == R.id.nav_review) {
                switchFragment(new ReviewFragment());
                return true;
            } else if (itemId == R.id.nav_stats) {
                switchFragment(new StatsFragment());
                return true;
            } else if (itemId == R.id.nav_profile) {
                switchFragment(new ProfileFragment());
                return true;
            }
            return false;
        });

        // 异步初始化字库（后台线程），完成前显示加载提示
        showInitProgress(true);
        DatabaseInitializer.init(this, new DatabaseInitializer.OnInitCallback() {
            @Override
            public void onReady() {
                runOnUiThread(() -> {
                    showInitProgress(false);
                    // 通知首页刷新数据
                    Fragment current = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    if (current instanceof HomeFragment) {
                        // 首页会自行观察 ViewModel 数据，这里只需关闭加载态
                    }
                });
            }
        });
    }

    private void showInitProgress(boolean show) {
        if (initProgress != null) {
            initProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
