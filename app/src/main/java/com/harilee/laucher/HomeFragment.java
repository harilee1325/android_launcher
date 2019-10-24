package com.harilee.laucher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends Fragment {

    @BindView(R.id.home_button)
    ImageView homeButton;
    @BindView(R.id.app_drawer)
    RecyclerView appDrawer;
    private View view;
    private ArrayList<AppInfo> appsList;
    private DockAdapter addApp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MyThread().execute();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_screen, container, false);
        ButterKnife.bind(this, view);


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    homeButton.setVisibility(View.VISIBLE);
                    appDrawer.setVisibility(View.INVISIBLE);
                    return true;

            }
            return false;
        });
        return view;
    }

    @OnClick(R.id.home_button)
    public void onViewClicked() {
        appDrawer.setVisibility(View.VISIBLE);
        homeButton.setVisibility(View.INVISIBLE);
        appDrawer.setHasFixedSize(true);
        addApp = new DockAdapter(getContext(),appsList);
        appDrawer.setAdapter(addApp);
    }

    public class MyThread extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... Params) {

            PackageManager pm = getActivity().getPackageManager();
            appsList = new ArrayList<>();

            Intent i = new Intent(Intent.ACTION_MAIN, null);
            i.addCategory(Intent.CATEGORY_LAUNCHER);

            List<ResolveInfo> allApps = pm.queryIntentActivities(i, 0);
            for(ResolveInfo ri:allApps) {
                AppInfo app = new AppInfo();
                app.label = ri.loadLabel(pm);
                app.packageName = ri.activityInfo.packageName;
                app.icon = ri.activityInfo.loadIcon(pm);
                appsList.add(app);
            }
            return "Success";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            updateStuff();
        }

    }
    public void updateStuff() {
        addApp.notifyItemInserted(addApp.getItemCount()-1);

    }

}
