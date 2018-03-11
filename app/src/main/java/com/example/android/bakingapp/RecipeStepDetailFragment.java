package com.example.android.bakingapp;

import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsitec219.franco on 14/11/17.
 */

public class RecipeStepDetailFragment extends Fragment {

    private static String TAG = RecipeStepDetailFragment.class.getSimpleName();
    public static String KEY_STEP_LIST_DETAIL = "key-step_list_detail-bundle";
    public static String KEY_STEP_INDEX_DETAIL = "key-step_index_detail-bundle";
    public static String KEY_VIDEO_POSITION = "key-video_position-bundle";

    private List<RecipeStep> mRecipeStepList;
    private int mRecipeStepListIndex;
    private RecipeStep mRecipeStep;
    private long mVideoPosition;

    private SimpleExoPlayerView mExoPlayerView;
    private SimpleExoPlayer mExoPlayer;

    private Dialog mFullScreenDialog;
    private boolean isLandscape;

    private TextView stepDescriptionTextView;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //IF IT IS NOT A TABLET
        if(getActivity().findViewById(R.id.linear_layout_tablet) == null)
            setHasOptionsMenu(true);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack("recipe", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe_detail_step, container, false);

        stepDescriptionTextView = (TextView) rootView.findViewById(R.id.tv_step_description);
        mExoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.exoplayer_view);
        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mExoPlayerView.getLayoutParams();
        Button previousStepButton = (Button) rootView.findViewById(R.id.btn_previous_step);
        Button nextStepButton = (Button) rootView.findViewById(R.id.btn_next_step);

        mRecipeStepListIndex = getArguments().getInt(MainActivity.RECIPE_STEP_INDEX);
        mRecipeStepList = getArguments().getParcelableArrayList(MainActivity.RECIPE_STEP_LIST);

        mRecipeStep = mRecipeStepList.get(mRecipeStepListIndex);
        stepDescriptionTextView.setText(mRecipeStep.getDescription());

        mFullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar) {
            @Override
            public void onBackPressed() {
                ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
                mExoPlayerView.setLayoutParams(params);
                ((LinearLayout) rootView.findViewById(R.id.ll_step_detail_fragment)).addView(mExoPlayerView, 1);
                mFullScreenDialog.dismiss();
                isLandscape = false;
                super.onBackPressed();
            }
        };

        //HANDLE IMAGE
        String thumbnailUrl = mRecipeStep.getThumbnailURL();
        Log.e(TAG, "thumbnail: " + mRecipeStep.getThumbnailURL());
        if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
            final Uri thumbnailUri = Uri.parse(thumbnailUrl).buildUpon().build();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Bitmap thumbnail;
                        thumbnail = Picasso.with(getActivity()).load(thumbnailUri).get();
                        mExoPlayerView.setDefaultArtwork(thumbnail);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        //HANDLE VIDEO
        String videoURL = mRecipeStep.getVideoURL();
        Log.e(TAG, "videoURL: " + mRecipeStep.getVideoURL());

        //IF VIDEOURL IS INVALID
        if (videoURL == null || videoURL.isEmpty()) {
            mExoPlayerView.setVisibility(View.GONE);
            TextView invalidVideoURL = (TextView) rootView.findViewById(R.id.tv_no_video_available);
            invalidVideoURL.setVisibility(View.VISIBLE);
            invalidVideoURL.setGravity(Gravity.CENTER);
        }

        if(getActivity().findViewById(R.id.linear_layout_tablet) == null) {

            //PREVIOUS BUTTON
            previousStepButton.setOnClickListener(new View.OnClickListener() {

                 @Override
                 public void onClick(View view) {
                    if (mRecipeStepListIndex == 0) {
                        Toast.makeText(getActivity(), "You are already on the first step.", Toast.LENGTH_SHORT);
                    }
                    else {
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(MainActivity.RECIPE_STEP_LIST, (ArrayList) mRecipeStepList);
                        bundle.putInt(MainActivity.RECIPE_STEP_INDEX, mRecipeStepListIndex - 1);

                        RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
                        recipeStepDetailFragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_detail_activity, recipeStepDetailFragment,
                                RecipeDetailActivity.TAG_RECIPE_DETAIL_STEP_FRAGMENT)
                            .addToBackStack(null)
                            .commit();
                    }
                 }
             });

            //NEXT BUTTON
            nextStepButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    if (mRecipeStepListIndex >= mRecipeStepList.size() - 1) {
                        Toast.makeText(getActivity(), "You are already on the last step.", Toast.LENGTH_SHORT);
                    }
                    else{
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(MainActivity.RECIPE_STEP_LIST, (ArrayList) mRecipeStepList);
                        bundle.putInt(MainActivity.RECIPE_STEP_INDEX, mRecipeStepListIndex + 1);

                        RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
                        recipeStepDetailFragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_detail_activity, recipeStepDetailFragment,
                                RecipeDetailActivity.TAG_RECIPE_DETAIL_STEP_FRAGMENT)
                            .addToBackStack(null)
                            .commit();
                    }
                }
            });
        }

        if (savedInstanceState != null) {
            mVideoPosition = savedInstanceState.getLong(KEY_VIDEO_POSITION);
        }

        return rootView;
    }



    public void initializePlayer() {
        String videoURL = mRecipeStep.getVideoURL();
        isLandscape = getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (mExoPlayer == null && videoURL != null && !videoURL.isEmpty()) {
            Uri videoUri = Uri.parse(videoURL).buildUpon().build();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            mExoPlayerView.setPlayer(mExoPlayer);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), "BakingApp"), new DefaultBandwidthMeter());
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource videoSource = new ExtractorMediaSource(videoUri,
                    dataSourceFactory, extractorsFactory, null, null);
            if (isLandscape && getActivity().findViewById(R.id.linear_layout_tablet) == null) {
                ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
                mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mFullScreenDialog.show();
            }
            mExoPlayer.seekTo(mVideoPosition);
            mExoPlayer.prepare(videoSource);
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayerView.hideController();
        }
    }


    @Override
    public void onResume() {
        initializePlayer();
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mRecipeStepList != null) {
            outState.putParcelableArrayList(KEY_STEP_LIST_DETAIL, (ArrayList) mRecipeStepList);
            outState.putInt(KEY_STEP_INDEX_DETAIL, mRecipeStepListIndex);
        }
        if(mExoPlayer!=null)
            outState.putLong(KEY_VIDEO_POSITION,mExoPlayer.getCurrentPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

}
