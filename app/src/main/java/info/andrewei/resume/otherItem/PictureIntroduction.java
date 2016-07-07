package info.andrewei.resume.otherItem;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import info.andrewei.resume.R;
import info.andrewei.resume.tools.MyFragmentTool;
import info.andrewei.resume.tools.ZoomImageView;

public class PictureIntroduction extends MyFragmentTool {
    private final int[] mImages = new int[] { R.drawable.quanyecha1, R.drawable.quanyecha2,R.drawable.quanyecha3 };
    private final ImageView[] mImageViews = new ImageView[mImages.length];

    private View mView;
    private ZoomImageView curView;
    private FragmentActivity myContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_picture_introduction, container, false);
        init();
        return mView;
    }

    @Override
    public void onAttach(Context context) {
        this.myContext=(FragmentActivity) context;
        super.onAttach(context);
    }

    public void init(){
        ViewPager mViewPager = (ViewPager) mView.findViewById(R.id.id_viewpager);
        mViewPager.setAdapter(new PagerAdapter()
        {
            @Override
            public Object instantiateItem(ViewGroup container, int position)
            {
                ZoomImageView imageView = new ZoomImageView(myContext.getApplicationContext());
                imageView.setImageResource(mImages[position]);
                container.addView(imageView);
                mImageViews[position] = imageView;
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object)
            {
                container.removeView(mImageViews[position]);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1)
            {
                return arg0 == arg1;
            }

            @Override
            public int getCount()
            {
                return mImages.length;
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                curView = (ZoomImageView) object;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int pPosition) {
                curView.setImageCenter(mView.getHeight(), mView.getWidth(), curView);
            }

            @Override
            public void onPageScrolled(int pPosition, float pPositionOffset, int pPositionOffsetPixels) {
                curView.setImageCenter(mView.getHeight(), mView.getWidth(), curView);
            }

            @Override
            public void onPageScrollStateChanged(int pState) {
                curView.setImageCenter(mView.getHeight(), mView.getWidth(), curView);
            }
        });
    }

    public void setCurImageCenter(){
        this.curView.setImageCenter(mView.getWidth(), mView.getHeight(), curView);
    }
}
