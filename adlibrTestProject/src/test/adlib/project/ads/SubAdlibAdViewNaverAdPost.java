/*
 * adlibr - Library for mobile AD mediation.
 * http://adlibr.com
 * Copyright (c) 2012-2013 Mocoplex, Inc.  All rights reserved.
 * Licensed under the BSD open source license.
 */

/*
 * confirmed compatible with NaverAdPost SDK 1.2.1
 */

package test.adlib.project.ads;

import com.mocoplex.adlib.SubAdlibAdViewCore;
import com.nbpcorp.mobilead.sdk.MobileAdListener;
import com.nbpcorp.mobilead.sdk.MobileAdView;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;


/*
 AndroidManifest.xml 에 아래 내용을 추가해주세요.
 
 <activity android:name="com.nbpcorp.mobilead.sdk.MobileAdBrowserActivity" />
 */

public class SubAdlibAdViewNaverAdPost extends SubAdlibAdViewCore  {

	protected MobileAdView ad;
	protected static boolean bGotAd = false;
    	
	public SubAdlibAdViewNaverAdPost(Context context) {
		this(context,null);
	}	

	public SubAdlibAdViewNaverAdPost(Context context, AttributeSet attrs) {
		super(context, attrs);

		initAdpostView();
	}
    
    public void initAdpostView()
    {
        // 여기에 네이버에서 발급받은 key 를 입력하세요.
		String naverAdPostKey = "NAVER_ID";

		ad = new MobileAdView(this.getContext());
		ad.setChannelID(naverAdPostKey);

		// 샘플 광고를 확인하기 위해서는 ad.setTest(true); 로 변경하여 적용해주세요.
		ad.setTest(false);
        
		this.addView(ad);

		LayoutParams l = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		ad.setLayoutParams(l);

		ad.setListener(new MobileAdListener() {
            
			@Override
			public void onReceive(int arg0) {

				if(arg0 == 0 || arg0 == 104 || arg0 == 101 || arg0 == 102 || arg0 == 106)
				{
					// 광고 수신 성공인 경우나, 검수중인 경우만 화면에 보입니다.
					bGotAd = true;
				}
				else
				{
					if(!bGotAd)
						failed();
				}

			}});
    }

	// 스케줄러에의해 자동으로 호출됩니다.
	// 실제로 광고를 보여주기 위하여 요청합니다.	
	public void query()
	{
        // NaverADPost SDK 1.2 이후로 background request 를 지원하지 않습니다.
        // 먼저 광고뷰를 화면에 보이고 수신여부를 확인합니다.
        gotAd();
        
		ad.start();
		
		if(!bGotAd)
		{
			// 3초 이상 리스너 응답이 없으면 다음 플랫폼으로 넘어갑니다.
			Handler adHandler = new Handler();
			adHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					if(bGotAd)
						return;
					else
					{
						if(ad != null)
							ad.stop();
						failed();
					}
				}
				
			}, 3000);
		}
	}

	// 광고뷰가 사라지는 경우 호출됩니다. 
	public void clearAdView()
	{
		if(ad != null)
		{
			ad.stop();
		}

		super.clearAdView();
	}

	public void onResume()
	{
		super.onResume();

		if(ad != null)
		{
			// 최초 리스너 응답을 받지 못한 상태에서 액티비티 전환이 일어나면 광고뷰가 하얗게 보이는 현상을 방지합니다.
            if(!ad.isAdAvailable())
			{
				this.removeView(ad);
				ad.stop();
				ad.destroy();
				ad = null;

				initAdpostView();
			}
            
			ad.start();
		}
	}
	public void onPause()
	{
		super.onPause();

		if(ad != null)
		{
			ad.stop();
		}		
	}	
	public void onDestroy()
	{
		super.onDestroy();

		if(ad != null)
		{			
			ad.stop();
			ad.destroy();
			ad = null;
		}				
	}
}