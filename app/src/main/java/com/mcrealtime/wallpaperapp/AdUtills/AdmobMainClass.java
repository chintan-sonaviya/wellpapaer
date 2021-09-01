package com.mcrealtime.wallpaperapp.AdUtills;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.mcrealtime.wallpaperapp.R;


public class AdmobMainClass {
    private static AdmobMainClass mInstance;
    boolean isshow = true;
    public InterstitialAd interstitial;
    AdRequest adRequest;
    AdmobCallback myCallback;
    private UnifiedNativeAd nativeAd;
    private LinearLayout linearLayoutLoader;

    public static AdmobMainClass getInstance() {
        if (mInstance == null) {
            mInstance = new AdmobMainClass();
        }
        return mInstance;
    }

    public void loadAdmobIntertialads(final Activity context) {
        String checkAdmobInterstitial = context.getResources().getString(R.string.Admob_int_id);
        if (!checkAdmobInterstitial.isEmpty()) {
            interstitial = new InterstitialAd(context);
            interstitial.setAdUnitId(context.getResources().getString(R.string.Admob_int_id));
            adRequest = new AdRequest.Builder().build();
            interstitial.loadAd(adRequest);
            interstitial.setAdListener(new AdListener() {
                public void onAdLoaded() {

                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    super.onAdFailedToLoad(errorCode);
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                    interstitial.loadAd(adRequest);
//                    setdelay();
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                }
            });
        }
    }

    public void displayAdmobInterstitial(final Activity activity, AdmobCallback _myCallback) {
        this.myCallback = _myCallback;
        if (!isshow) {
            if (myCallback != null) {
                myCallback.callbackCall();
                myCallback = null;
            }
        } else {
            if (interstitial != null) {
                if (interstitial.isLoaded()) {
                    Adloder.getInstance().showLoader(activity);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Adloder.getInstance().dismissLoader();
                            interstitial.show();
                        }
                    }, 1000);
                } else if (!interstitial.isLoading()) {
                    interstitial.loadAd(adRequest);
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                } else {
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                }
            } else {
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
            }
        }
    }

    void setdelay() {
        isshow = false;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isshow = true;
            }
        }, 1000 * 5);
    }

    public interface AdmobCallback {
        void callbackCall();
    }

    public void loadAdmobNativeWithLoader(final Activity activity, final FrameLayout frameLayout) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        linearLayoutLoader = (LinearLayout) inflater.inflate(R.layout.loadernative_dialog, frameLayout, false);
        loadAdmobNativeLarge(activity, frameLayout);
        frameLayout.addView(linearLayoutLoader);
    }

    public void loadAdmobNativeLarge(final Activity activity, final FrameLayout frameLayout) {
        String checkAdmobNative = activity.getResources().getString(R.string.Admob_native_id);
        frameLayout.setBackgroundResource(R.drawable.customadbg);
        if (!checkAdmobNative.isEmpty()) {
            AdLoader.Builder builder = new AdLoader.Builder(activity, activity.getString(R.string.Admob_native_id));
            builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                @Override
                public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                    if (nativeAd != null) {
                        nativeAd.destroy();
                    }
                    nativeAd = unifiedNativeAd;
                    UnifiedNativeAdView adView = (UnifiedNativeAdView) activity.getLayoutInflater().inflate(R.layout.admobnativecustom, null);
                    populateUnifiedNativeAdView(unifiedNativeAd, adView);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                    linearLayoutLoader.setVisibility(View.GONE);
                }
            });

            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {

                }
            }).build();

            adLoader.loadAd(new AdRequest.Builder().build());
        } else {
            linearLayoutLoader.setVisibility(View.GONE);
            frameLayout.setVisibility(View.GONE);
        }
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(nativeAd);
    }

    public void loadAdmobbannerWithLoader(final Activity activity, final LinearLayout linearLayout) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        linearLayoutLoader = (LinearLayout) inflater.inflate(R.layout.loaderbanner_dialog, linearLayout, false);
        loadAdmobBanner(activity, linearLayout);
        linearLayout.addView(linearLayoutLoader);
    }

    public void loadAdmobBanner(Activity activity, LinearLayout linearLayout) {
        String checkAdmobBanner = activity.getResources().getString(R.string.Admob_banner_id);
        linearLayout.setBackgroundResource(R.drawable.customadbg);
        if (!checkAdmobBanner.isEmpty()) {
            AdView adView = new AdView(activity);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(activity.getString(R.string.Admob_banner_id));
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    linearLayoutLoader.setVisibility(View.GONE);
                }
            });
            linearLayout.addView(adView);
        } else {
            linearLayoutLoader.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
        }
    }
}
