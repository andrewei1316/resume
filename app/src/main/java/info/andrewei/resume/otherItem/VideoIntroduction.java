package info.andrewei.resume.otherItem;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import info.andrewei.resume.R;


public class VideoIntroduction extends Fragment implements UniversalVideoView.VideoViewCallback{
    private View mView;
    private Context context;

    private UniversalVideoView mVideoView;
    private UniversalMediaController mMediaController;

    private View mBottomLayout;
    private View mVideoLayout;
    private LinearLayout mVideoOutLayout;

    private int mSeekPosition;
    private int cachedHeight;

    private String SEEK_POSITION_KEY;
    private String VIDEO_URL;
    private TextView mStart;

    private void init(){
        SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
        VIDEO_URL = Uri.parse("android.resource://" + context.getPackageName()
                + "/" + R.raw.test_video).toString();

        mVideoLayout = mView.findViewById(R.id.video_layout);
        mBottomLayout = mView.findViewById(R.id.bottom_layout);
        mVideoOutLayout =(LinearLayout) mView.findViewById(R.id.video_out_layout);
        mVideoView = (UniversalVideoView) mView.findViewById(R.id.videoView);
        mMediaController = (UniversalMediaController) mView.findViewById(R.id.media_controller);
        mVideoView.setMediaController(mMediaController);
        setVideoAreaSize();
        mVideoView.setVideoViewCallback(this);
        mStart = (TextView) mView.findViewById(R.id.start);

        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mVideoView.isPlaying()){
                    mVideoView.pause();
                    mStart.setText("开始");
                }
                else{
                    if (mSeekPosition > 0) {
                        mVideoView.seekTo(mSeekPosition);
                    }
                    mVideoView.start();
                    mStart.setText("暂停");
                    mMediaController.setTitle("像树一样成长，有水的精神");
                }
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mStart.setText("开始");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_video_introduction, container, false);
        init();
        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onPause() {
        super.onPause();
        mStart.setText("开始");
        if (mVideoView != null && mVideoView.isPlaying()) {
            mSeekPosition = mVideoView.getCurrentPosition();
            mVideoView.pause();
        }
    }

    /**
     * 置视频区域大小
     */
    private void setVideoAreaSize() {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = mVideoLayout.getWidth();
                cachedHeight = (int) (width * 405f / 720f);
                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                mVideoLayout.setLayoutParams(videoLayoutParams);
                mVideoView.setVideoPath(VIDEO_URL);
                mVideoView.requestFocus();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);
    }

    @Override
    public void onScaleChange(boolean isFullscreen) {
        if (isFullscreen) {
            try{
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 0, 0, 0);
                mVideoOutLayout.setLayoutParams(lp);
                ((FloatingActionButton)getActivity().findViewById(R.id.fab)).hide();
            }catch (java.lang.NullPointerException ex){
                ex.printStackTrace();
            }
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoLayout.setLayoutParams(layoutParams);
            mBottomLayout.setVisibility(View.GONE);
        } else {
            try{
                int dpValue = 56; // margin in dips
                float d = context.getResources().getDisplayMetrics().density;
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, (int)(dpValue * d), 0, 0);
                mVideoOutLayout.setLayoutParams(lp);
                ((FloatingActionButton)getActivity().findViewById(R.id.fab)).show();
            }catch (java.lang.NullPointerException ex){
                ex.printStackTrace();
            }
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            mVideoLayout.setLayoutParams(layoutParams);
            mBottomLayout.setVisibility(View.VISIBLE);
        }

        switchTitleBar(!isFullscreen);
    }

    @Override
    public void onPause(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onStart(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {

    }

    private void switchTitleBar(boolean show) {
        android.support.v7.app.ActionBar supportActionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            if (show) {
                supportActionBar.show();
            } else {
                supportActionBar.hide();
            }
        }
    }
}
