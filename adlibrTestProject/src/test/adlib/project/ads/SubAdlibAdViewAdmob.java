/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with admob SDK 6.4.1
 */

package test.adlib.project.ads;

import com.google.ads.Ad;
import com.google.ads.AdRequest.ErrorCode;
import com.mocoplex.adlib.SubAdlibAdViewCore;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;

public class SubAdlibAdViewAdmob extends SubAdlibAdViewCore  {
	
	protected com.google.ads.AdView ad;
	protected boolean bGotAd = false;
	
	// 여기에 ADMOB ID 를 입력하세요.
	String admobID = "ADMOB_ID";
    
	public SubAdlibAdViewAdmob(Context context) {
		this(context,null);
	}
	
	public SubAdlibAdViewAdmob(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		
		initAdmobView();
	}
	
	public void initAdmobView()
	{
		ad = new com.google.ads.AdView((Activity) this.getContext(), com.google.ads.AdSize.SMART_BANNER, admobID);
		
		// 광고 뷰의 위치 속성을 제어할 수 있습니다.
		this.setGravity(Gravity.CENTER);
		
		ad.setAdListener( new com.google.ads.AdListener() {

			@Override
			public void onDismissScreen(Ad arg0) {
				
			}

			@Override
			public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
				
				bGotAd = true;
				failed();
			}

			@Override
			public void onLeaveApplication(Ad arg0) {
				
			}

			@Override
			public void onPresentScreen(Ad arg0) {
				
			}

			@Override
			public void onReceiveAd(Ad arg0) {
				
				bGotAd = true;
				queryAd();
				// 광고를 받아왔으면 이를 알려 화면에 표시합니다.
				gotAd();
			}
			
		});
	}
	
	private com.google.ads.AdRequest request = new com.google.ads.AdRequest();
    
	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.
	public void query()
	{
		bGotAd = false;
		
        this.removeAllViews();
		this.addView(ad);
		
		ad.loadAd(request);
	}
	
	public void onDestroy()
	{
		if(ad != null)
		{
			ad.destroy();
			ad = null;
		}
		
		super.onDestroy();
	}
	
	public void clearAdView()
	{
		if(ad != null)
		{
        	this.removeView(ad);
		}
		
        super.clearAdView();
	}
	
	public void onResume()
	{
        super.onResume();
	}
	
	public void onPause()
	{
        super.onPause();
	}
}