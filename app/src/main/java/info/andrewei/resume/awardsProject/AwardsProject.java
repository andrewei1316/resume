package info.andrewei.resume.awardsProject;

import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.andrewei.resume.tools.FragmentAdapter;
import info.andrewei.resume.R;
import info.andrewei.resume.tools.MyFragmentTool;
import info.andrewei.resume.tools.ShakeListener;

public class AwardsProject extends MyFragmentTool implements ViewPager.OnPageChangeListener, View.OnClickListener{
    private ViewPager myViewPager; // 要使用的ViewPager
    private TextView tv_tab0, tv_tab1, tv_tab2; // 3个选项卡
    private ImageView line_tab; // tab选项卡的下划线
    private int moveOne = 0; // 下划线移动一个选项卡
    private boolean isScrolling = false; // 手指是否在滑动
    private boolean isBackScrolling = false; // 手指离开后的回弹
    private long startTime = 0;
    private int curPosition = 0;
    private int shakeId = 0;
    private ShakeListener shakeListener;
    private View mView;
    private FragmentActivity myContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_awards_project, container, false);
        this.mView = view;
        initView();
        initLineImage();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = (FragmentActivity)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (shakeListener != null) {
            shakeListener.stop();
        }
    }

    public void initLineImage() {
        /** * 获取屏幕的宽度 */
        DisplayMetrics dm = new DisplayMetrics();
        myContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        /** * 重新设置下划线的宽度 */
        ViewGroup.LayoutParams lp = line_tab.getLayoutParams();
        lp.width = screenW / 3;
        line_tab.setLayoutParams(lp);
        moveOne = lp.width;
        // 滑动一个页面的距离
    }
    private void initView() {
        myViewPager = (ViewPager) mView.findViewById(R.id.vp_awards_project);
        Page1Awards myFragment1 = new Page1Awards();
        Page2Project myFragment2 = new Page2Project();
        Page3SelfEvaluation myFragment3 = new Page3SelfEvaluation();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(myFragment1);
        fragmentList.add(myFragment2);
        fragmentList.add(myFragment3);
        FragmentAdapter myPagerAdapter = new FragmentAdapter(this.getChildFragmentManager()
                , fragmentList);
        myViewPager.setAdapter(myPagerAdapter);
        tv_tab0 = (TextView) mView.findViewById(R.id.text_award);
        tv_tab1 = (TextView) mView.findViewById(R.id.text_project);
        tv_tab2 = (TextView) mView.findViewById(R.id.text_self_evaluation);
        line_tab = (ImageView) mView.findViewById(R.id.line_tab1);
        tv_tab0.setOnClickListener(this);
        tv_tab1.setOnClickListener(this);
        tv_tab2.setOnClickListener(this);
        myViewPager.addOnPageChangeListener(this);
        myViewPager.setCurrentItem(0);
        tv_tab0.setTextColor(Color.RED);
        tv_tab1.setTextColor(Color.BLACK);
        tv_tab2.setTextColor(Color.BLACK);

        shakeListener = new ShakeListener(myContext);//创建一个对象
        shakeListener.setOnShakeListener(new ShakeListener.OnShakeListener(){
            public void onShake() {
                if(((shakeId++) & 1) == 0)
                    return;
                onVibrator();
                if(curPosition < 2){
                    curPosition++;
                }else{
                    curPosition = 0;
                }
                onPageSelected(curPosition);
                myViewPager.setCurrentItem(curPosition);
            }
        });
    }

    private void onVibrator() {
        Vibrator vibrator = (Vibrator) myContext.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator == null) {
            vibrator = (Vibrator) myContext.getApplicationContext()
                    .getSystemService(Service.VIBRATOR_SERVICE);
        }
        vibrator.vibrate(200L);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case 1:
                isScrolling = true;
                isBackScrolling = false;
                break;
            case 2:
                isScrolling = false;
                isBackScrolling = true;
                break;
            default:
                isScrolling = false;
                isBackScrolling = false;
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        long currentTime = System.currentTimeMillis();
        if (isScrolling && (currentTime - startTime > 200)) {
            movePositionX(position, moveOne * positionOffset);
            startTime = currentTime;
        } if (isBackScrolling){
            movePositionX(position);
        }
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                tv_tab0.setTextColor(Color.RED);
                tv_tab1.setTextColor(Color.BLACK);
                tv_tab2.setTextColor(Color.BLACK);
                movePositionX(0);
                curPosition = 0;
                break;
            case 1:
                tv_tab0.setTextColor(Color.BLACK);
                tv_tab1.setTextColor(Color.RED);
                tv_tab2.setTextColor(Color.BLACK);
                curPosition = 1;
                movePositionX(1);
                break;
            case 2:
                tv_tab0.setTextColor(Color.BLACK);
                tv_tab1.setTextColor(Color.BLACK);
                tv_tab2.setTextColor(Color.RED);
                curPosition = 2;
                movePositionX(2);
                break;
            default:
                break;
        }
    }

    /** * 下划线跟随手指的滑动而移动
     * * @param toPosition
     * * @param positionOffsetPixels
     * */
    private void movePositionX(int toPosition, float positionOffsetPixels) {
        float curTranslationX = line_tab.getTranslationX();
        float toPositionX = moveOne * toPosition + positionOffsetPixels;
        ObjectAnimator animator = ObjectAnimator.ofFloat(line_tab, "translationX"
                , curTranslationX, toPositionX);
        animator.setDuration(500); animator.start();
    }

    /**
     * 下划线滑动到新的选项卡中
     * * @param toPosition
     * */
    public void movePositionX(int toPosition) {
        movePositionX(toPosition, 0);
    }

    public int getCurPosition(){
        return this.curPosition;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_award:
                myViewPager.setCurrentItem(0);
                curPosition = 0;
                break;
            case R.id.text_project:
                myViewPager.setCurrentItem(1);
                curPosition = 1;
                break;
            case R.id.text_self_evaluation:
                myViewPager.setCurrentItem(2);
                curPosition = 2;
                break;
            default:
                break;
        }
    }
}
