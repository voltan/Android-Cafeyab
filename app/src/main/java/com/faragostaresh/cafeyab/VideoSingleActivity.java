package com.faragostaresh.cafeyab;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.faragostaresh.adaptor.MyTagHandler;
import com.faragostaresh.adaptor.VideoListAdapter;
import com.faragostaresh.app.Config;
import com.faragostaresh.model.ItemList;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 * https://geoffledak.com/blog/2017/09/11/how-to-add-a-fullscreen-toggle-button-to-exoplayer-in-android/
 */
public class VideoSingleActivity extends AppCompatActivity {

    private static final String TAG = VideoSingleActivity.class.getSimpleName();

    private FirebaseAnalytics mFirebaseAnalytics;

    Context context = this;

    public static String videoUrl = "";
    public static String videoSourceUrl = "";
    public static String itemId = "";
    public static String itemTitle = "";
    public static String itemUrl = Config.URL_WEBSITE;
    public static String largeUrl = "";

    public TextView txtSummary;
    public TextView txtDescription;

    public ListView listView;
    public VideoListAdapter adapter;
    public List<ItemList> myVideoList = new ArrayList<ItemList>();

    public ProgressDialog progressDialog;

    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";

    private SimpleExoPlayerView mExoPlayerView;
    private MediaSource mVideoSource;
    private boolean mExoPlayerFullscreen = false;
    private FrameLayout mFullScreenButton;
    private ImageView mFullScreenIcon;
    private Dialog mFullScreenDialog;

    private int mResumeWindow;
    private long mResumePosition;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_single);

        // Set for support RTL
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Get search information
        Bundle extras = getIntent().getExtras();
        itemId = extras.getString("itemId");
        videoSourceUrl = extras.getString("videoSourceUrl");
        videoUrl = Config.URL_VIDEO_SINGLE + itemId;

        Log.d(TAG, "Single item url : " + videoUrl);
        Log.d(TAG, "Single video url : " + videoSourceUrl);

        // Set title
        setTitle(itemTitle);

        new getJson().execute();

        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }

        // Set shear bottom
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.share_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Intent> targetShareIntents = new ArrayList<Intent>();

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("*/*");

                List<ResolveInfo> resInfos = context.getPackageManager().queryIntentActivities(shareIntent, 0);
                for (ResolveInfo ri : resInfos) {

                    String packageName = ri.activityInfo.packageName;
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                    intent.setPackage(packageName);

                    if (packageName.contains("com.twitter.android")) {

                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, itemTitle);
                        intent.putExtra(Intent.EXTRA_TEXT, itemUrl);
                        intent.putExtra(Intent.EXTRA_STREAM, largeUrl);
                        targetShareIntents.add(intent);

                    } else if (packageName.contains("com.facebook.katana")) {

                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, itemTitle);
                        intent.putExtra(Intent.EXTRA_TEXT, itemUrl);
                        intent.putExtra(Intent.EXTRA_STREAM, largeUrl);
                        targetShareIntents.add(intent);

                    } else if (packageName.contains("com.instagram")) {

                        intent.setType("image/jpeg");
                        intent.putExtra(Intent.EXTRA_STREAM, largeUrl);
                        intent.putExtra(Intent.EXTRA_TEXT, itemTitle);
                        targetShareIntents.add(intent);

                    } else if (packageName.contains("com.pinterest")) {

                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, itemTitle);
                        intent.putExtra(Intent.EXTRA_TEXT, itemUrl);
                        intent.putExtra(Intent.EXTRA_STREAM, largeUrl);
                        targetShareIntents.add(intent);

                    } else if (packageName.contains("com.google.android.gm")) {

                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_TITLE, itemTitle);
                        intent.putExtra(Intent.EXTRA_SUBJECT, itemTitle);
                        intent.putExtra(Intent.EXTRA_TEXT, itemUrl);
                        intent.putExtra(Intent.EXTRA_STREAM, largeUrl);
                        targetShareIntents.add(intent);

                    } else if (packageName.contains("com.hootsuite.droid.full")) {

                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, itemTitle);
                        intent.putExtra(Intent.EXTRA_TEXT, itemUrl);
                        targetShareIntents.add(intent);

                    } else if (packageName.contains("com.facebook.pages.app")) {

                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, itemTitle);
                        intent.putExtra(Intent.EXTRA_TEXT, itemUrl);
                        intent.putExtra(Intent.EXTRA_STREAM, largeUrl);
                        targetShareIntents.add(intent);

                    } else if (packageName.contains("org.telegram.messenger")) {

                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, itemTitle);
                        intent.putExtra(Intent.EXTRA_TEXT, itemUrl);
                        targetShareIntents.add(intent);

                    } /* else {

                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, itemTitle);
                        intent.putExtra(Intent.EXTRA_TEXT, itemUrl);
                        intent.putExtra(Intent.EXTRA_STREAM, largeUrl);
                        targetShareIntents.add(intent);

                    } */
                }

                Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0), "Choose app to share");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
                startActivity(chooserIntent);

            }
        });
    }


    public class getJson extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            //progressDialog = new ProgressDialog(VideoSingleActivity.this, R.style.AppTheme_Dark_Dialog);
            //progressDialog.setIndeterminate(true);
            //progressDialog.setMessage("در حال دریافت اطلاعات ...");
            //progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            adapter = new VideoListAdapter(VideoSingleActivity.this, myVideoList, "related");

            //Creating a json array request
            JsonArrayRequest volleyRequest = new JsonArrayRequest(videoUrl,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //VideoList videoSingle = new VideoList();
                            Log.d(TAG, response.toString());
                            JSONObject json = null;
                            try {
                                json = response.getJSONObject(0);

                                // Set title
                                setTitle(json.getString("title"));

                                // Set info for layout
                                TextView textViewTitle = (TextView) findViewById(R.id.viewTitle);
                                textViewTitle.setText(json.getString("title"));

                                txtSummary = (TextView) findViewById(R.id.summary);
                                if (!json.getString("text_summary").isEmpty()) {
                                    txtSummary.setText(json.getString("text_summary"));
                                } else {
                                    txtSummary.setVisibility(View.GONE);
                                }

                                txtDescription = (TextView) findViewById(R.id.description);
                                if (!json.getString("text_description").isEmpty()) {
                                    txtDescription.setText(Html.fromHtml(json.getString("text_description"), null, new MyTagHandler()));
                                } else {
                                    txtDescription.setVisibility(View.GONE);
                                }

                                // Set item url
                                itemUrl = json.getString("videoUrl");

                                // Set item largeUrl
                                largeUrl = json.getString("largeUrl");

                                // Set video url
                                //videoSourceUrl = json.getString("video_qmery_hls");


                                /*
                                 * https://github.com/ayalus/ExoPlayer-2-Example
                                 */

                                // 1. Create a default TrackSelector
                                /* Handler mainHandler = new Handler();
                                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                                TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
                                TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

                                // 2. Create a default LoadControl
                                LoadControl loadControl = new DefaultLoadControl();

                                // 3. Create the player
                                player = ExoPlayerFactory.newSimpleInstance(VideoSingleActivity.this, trackSelector, loadControl);
                                simpleExoPlayerView = new SimpleExoPlayerView(VideoSingleActivity.this);
                                simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);

                                //Set media controller
                                simpleExoPlayerView.setUseController(true);
                                simpleExoPlayerView.requestFocus();

                                // Bind the player to the view.
                                simpleExoPlayerView.setPlayer(player);


                                Uri mp4VideoUri = Uri.parse(videoSourceUrl);

                                // Measures bandwidth during playback. Can be null if not required.
                                DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();
                                // Produces DataSource instances through which media data is loaded.
                                // DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "exoplayer2example"), bandwidthMeterA);
                                DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(VideoSingleActivity.this, Util.getUserAgent(VideoSingleActivity.this, "exoplayer2example"), bandwidthMeterA);

                                // Produces Extractor instances for parsing the media data.
                                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

                                // This is the MediaSource representing the media to be played:
                                // FOR SD CARD SOURCE:
                                // MediaSource videoSource = new ExtractorMediaSource(mp4VideoUri, dataSourceFactory, extractorsFactory, null, null);

                                // FOR LIVESTREAM LINK:
                                MediaSource videoSource = new HlsMediaSource(mp4VideoUri, dataSourceFactory, 1, null, null);
                                final LoopingMediaSource loopingSource = new LoopingMediaSource(videoSource);


                                // Prepare the player with the source.
                                player.prepare(loopingSource);


                                player.addListener(new ExoPlayer.EventListener() {
                                    @Override
                                    public void onLoadingChanged(boolean isLoading) {
                                        Log.v(TAG, "Listener-onLoadingChanged...");

                                    }

                                    @Override
                                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                                        Log.v(TAG, "Listener-onPlayerStateChanged...");

                                    }

                                    @Override
                                    public void onTimelineChanged(Timeline timeline, Object manifest) {
                                        Log.v(TAG, "Listener-onTimelineChanged...");

                                    }

                                    @Override
                                    public void onPlayerError(ExoPlaybackException error) {
                                        Log.v(TAG, "Listener-onPlayerError...");
                                        player.stop();
                                        player.prepare(loopingSource);
                                        player.setPlayWhenReady(true);
                                    }

                                    @Override
                                    public void onPositionDiscontinuity() {
                                        Log.v(TAG, "Listener-onPositionDiscontinuity...");

                                    }
                                });
                                player.setPlayWhenReady(true); */

                                Log.d(TAG, "videoRelated array: " + json.getJSONArray("videoRelated"));

                                JSONArray jsonArrayRelated = json.getJSONArray("videoRelated");
                                if (jsonArrayRelated.length() > 0) {
                                    for (int i = 0; i < jsonArrayRelated.length(); i++) {
                                        try {
                                            JSONObject obj = jsonArrayRelated.getJSONObject(i);
                                            ItemList video = new ItemList();
                                            video.setTitle(obj.getString("title"));
                                            video.setItemID(obj.getString("id"));
                                            video.setThumbnailUrl(obj.getString("mediumUrl"));
                                            video.setItemType(obj.getString("video_qmery_hls"));
                                            myVideoList.add(video);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    // Set for list of items
                                    listView = (ListView) findViewById(R.id.videoRelatedlist);
                                    listView.setAdapter(adapter);
                                    listView.setOnItemClickListener(new VideoSingleActivity.ListVewiClickListener());
                                    setListViewHeightBasedOnChildren(listView);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                        }
                    });
            //Creating request queue
            RequestQueue requestQueue = Volley.newRequestQueue(VideoSingleActivity.this);

            // Add retry policy
            volleyRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            //Adding request to the queue
            requestQueue.add(volleyRequest);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            /* new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            }, 1000); */
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            /* case R.id.menu_home:
                Intent intentHome = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentHome);
                break; */

            case R.id.menu_user:
                Intent intentUser = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intentUser);
                break;

            case R.id.menu_about:
                Intent intentAbout = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intentAbout);
                break;

            /* case R.id.menu_search:
                Intent intentSearch = new Intent(getApplicationContext(), CafeListActivity.class);
                startActivity(intentSearch);
                break; */
        }
        return super.onOptionsItemSelected(item);
    }


    private class ListVewiClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            ItemList getselected = (ItemList) (listView.getItemAtPosition(position));
            itemId = getselected.getItemId();
            videoSourceUrl = getselected.getItemType();
            displayView(itemId, videoSourceUrl);
        }

        private void displayView(String itemId, String videoSourceUrl) {
            // Start New activity with a request to Show Selected News
            Intent intent = new Intent(getApplicationContext(), VideoSingleActivity.class);
            intent.putExtra("itemId", itemId);
            intent.putExtra("videoSourceUrl", videoSourceUrl);
            startActivity(intent);

        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }







    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);

        super.onSaveInstanceState(outState);
    }


    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }


    private void openFullscreenDialog() {

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(VideoSingleActivity.this, R.drawable.ic_fullscreen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }


    private void closeFullscreenDialog() {

        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(mExoPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(VideoSingleActivity.this, R.drawable.ic_fullscreen_expand));

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }


    private void initFullscreenButton() {

        PlaybackControlView controlView = mExoPlayerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen)
                    openFullscreenDialog();
                else
                    closeFullscreenDialog();
            }
        });
    }


    private void initExoPlayer() {

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this), trackSelector, loadControl);
        mExoPlayerView.setPlayer(player);

        boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;

        if (haveResumePosition) {
            mExoPlayerView.getPlayer().seekTo(mResumeWindow, mResumePosition);
        }

        mExoPlayerView.getPlayer().prepare(mVideoSource);
        mExoPlayerView.getPlayer().setPlayWhenReady(true);
    }


    @Override
    protected void onResume() {

        super.onResume();

        if (mExoPlayerView == null) {

            mExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exoplayer);
            initFullscreenDialog();
            initFullscreenButton();

            String userAgent = Util.getUserAgent(VideoSingleActivity.this, getApplicationContext().getApplicationInfo().packageName);
            DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent, null, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS, DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true);
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(VideoSingleActivity.this, null, httpDataSourceFactory);
            Uri daUri = Uri.parse(videoSourceUrl);

            mVideoSource = new HlsMediaSource(daUri, dataSourceFactory, 1, null, null);
        }

        initExoPlayer();

        if (mExoPlayerFullscreen) {
            ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
            mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(VideoSingleActivity.this, R.drawable.ic_fullscreen_skrink));
            mFullScreenDialog.show();
        }
    }


    @Override
    protected void onPause() {

        super.onPause();

        mResumeWindow = mExoPlayerView.getPlayer().getCurrentWindowIndex();
        mResumePosition = Math.max(0, mExoPlayerView.getPlayer().getContentPosition());

        if (mExoPlayerView != null && mExoPlayerView.getPlayer() != null) {
            mExoPlayerView.getPlayer().release();
        }

        if (mFullScreenDialog != null)
            mFullScreenDialog.dismiss();
    }
}