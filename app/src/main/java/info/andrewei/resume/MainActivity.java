package info.andrewei.resume;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.ArrayList;

import info.andrewei.resume.awardsProject.AwardsProject;
import info.andrewei.resume.onlineInfo.OnlineInfo;
import info.andrewei.resume.otherItem.ContactMe;
import info.andrewei.resume.otherItem.ContentMain;
import info.andrewei.resume.otherItem.PictureIntroduction;
import info.andrewei.resume.otherItem.VideoIntroduction;
import info.andrewei.resume.primaryInfo.PrimaryInfo;
import info.andrewei.resume.tools.MusicService;
import info.andrewei.resume.tools.MyFragmentTool;
import info.andrewei.resume.tools.ToolsClass;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private long exitTime;
    private Intent musicIntent;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private int curSelectedItemId;
    private boolean isSelectedItem;
    private FragmentManager sfm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.exitTime = System.currentTimeMillis();
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null){
            fab.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view) {
                    ToolsClass.sendEmail(view.getContext());
                }
            });
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if(!isSelectedItem) return;
                Fragment newFragment = null;
                if(curSelectedItemId == R.id.nav_home){
                    setTitle("欢迎页");
                    newFragment = new ContentMain();
                } else if (curSelectedItemId == R.id.nav_primary_info) {
                    setTitle("基本信息");
                    newFragment = new PrimaryInfo();
                } else if (curSelectedItemId == R.id.nav_awards_project) {
                    setTitle("获奖与项目");
                    newFragment = new AwardsProject();
                } else if (curSelectedItemId == R.id.nav_online_media) {
                    setTitle("深入了解我");
                    newFragment = new OnlineInfo();
                } else if (curSelectedItemId == R.id.nav_picture_introduction) {
                    setTitle("图片介绍");
                    newFragment = new PictureIntroduction();
                } else if (curSelectedItemId == R.id.nav_video_introduction) {
                    setTitle("视频介绍");
                    newFragment = new VideoIntroduction();
                    if(isServiceRunning(MainActivity.this)){
                        musicServiceStop(musicIntent, toolbar.getMenu().findItem(R.id.action_music_controller));
                    }
                }else if (curSelectedItemId == R.id.nav_send) {
                    startActivity(new Intent(MainActivity.this, ContactMe.class));
                }
                if(newFragment != null){
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_main, newFragment);
                    transaction.commitAllowingStateLoss();
                }
                isSelectedItem = false;
            }

        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(navigationView != null){
            navigationView.setNavigationItemSelectedListener(this);
        }

        sfm = getSupportFragmentManager();
        Fragment newFragment = sfm.findFragmentById(R.id.content_main);
        if(newFragment == null){
            setTitle("欢迎页");
            if(navigationView != null){
                MenuItem item = navigationView.getMenu().findItem(R.id.nav_home);
                isSelectedItem = false;
                curSelectedItemId = item.getItemId();
                item.setChecked(true);
            }
            newFragment = new ContentMain();
            sfm.beginTransaction().add(R.id.content_main, newFragment).commit();
        }

        musicIntent = new Intent(MainActivity.this, MusicService.class);
        musicServiceStart(musicIntent, toolbar.getMenu().findItem(R.id.action_music_controller));
    }

    private void musicServiceStart(Intent intent, MenuItem item){
        if(item != null) item.setIcon(R.drawable.music_on);
        startService(intent);
    }

    private void musicServiceStop(Intent intent, MenuItem item){
        if(item != null) item.setIcon(R.drawable.music_off);
        stopService(intent);
    }

    @Override
    protected void onStop() {
        stopService(musicIntent);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.action_music_controller){
            if(isServiceRunning(MainActivity.this)){
                musicServiceStop(musicIntent, item);
                return true;
            }else {
                musicServiceStart(musicIntent, item);
                return true;
            }
        }
        else return super.onOptionsItemSelected(item);
    }

    private boolean isServiceRunning(Context context) {
        if (!TextUtils.isEmpty("info.andrewei.resume.tools.MusicService") && context != null) {
            ActivityManager activityManager
                    = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ArrayList<ActivityManager.RunningServiceInfo> runningServiceInfoList
                    = (ArrayList<ActivityManager.RunningServiceInfo>) activityManager.getRunningServices(100);
            for(ActivityManager.RunningServiceInfo runningServiceInfo : runningServiceInfoList){
                if ("info.andrewei.resume.tools.MusicService".equals(runningServiceInfo.service.getClassName())) {
                    return true;
                }
            }
        } else {
            return false;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        isSelectedItem = true;
        curSelectedItemId = item.getItemId();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                if(drawer.isDrawerOpen(GravityCompat.START)){
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
                WebView webView = (WebView)findViewById(R.id.web_blog);
                if(webView == null){
                    webView = (WebView)findViewById(R.id.web_weibo);
                }
                if(webView == null){
                    webView = (WebView)findViewById(R.id.web_github);
                }
                if (webView != null && webView.canGoBack()) {
                    webView.goBack();//退回到上一个页面
                    return true;
                }
                else{
                    if(event.getAction() == KeyEvent.ACTION_DOWN)
                    {
                        if((System.currentTimeMillis()-exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
                        {
                            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                            exitTime = System.currentTimeMillis();
                        }
                        else
                        {
                            musicServiceStop(musicIntent, toolbar.getMenu().findItem(R.id.action_music_controller));
                            finish();
                            System.exit(0);
                        }
                        return true;
                    }
                }
                return super.onKeyDown(keyCode, event);
            case KeyEvent.KEYCODE_MENU:
                if(drawer.isDrawerOpen(GravityCompat.START)){
                    drawer.closeDrawer(GravityCompat.START);
                }else{
                    drawer.openDrawer(GravityCompat.START);
                }
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onConfigurationChanged( Configuration newConfig )
    {
        super.onConfigurationChanged( newConfig );
        if(sfm.findFragmentById(R.id.content_main) instanceof MyFragmentTool){
            MyFragmentTool curFragment = (MyFragmentTool) sfm.findFragmentById(R.id.content_main);
            if(curFragment instanceof PictureIntroduction){
                ((PictureIntroduction) curFragment).setCurImageCenter();
            }else{
                try{
                    curFragment.initLineImage();
                    curFragment.movePositionX(curFragment.getCurPosition());
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }
//
//        if ( this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE )
//        {
//            // 当前为横屏， 在此处添加额外的处理代码
//        }
//        else if ( this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT )
//        {
//            //当前为竖屏， 在此处添加额外的处理代码
//        }
    }
}
