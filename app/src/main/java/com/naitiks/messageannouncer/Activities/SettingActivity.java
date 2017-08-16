package com.naitiks.messageannouncer.Activities;
import com.naitiks.messageannouncer.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

@SuppressLint("InflateParams")
public class SettingActivity extends AppCompatActivity{
	LinearLayout layout_langSetting,layout_speed,layout_pitcs,layout_read;
	View settingView;
	RadioButton r1,r2,r3,r4,r5;
	SharedPreferences SharedPreferences;
	Editor editor;
	CheckBox playInSilent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		Toolbar toolbar = (Toolbar) (getLayoutInflater().inflate(R.layout.app_bar_main,null)).findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionBar = this.getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle("Settings");
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP|ActionBar.DISPLAY_SHOW_TITLE);
	    actionBar.show();    
	    SharedPreferences=getSharedPreferences("msgNotifier", 0);
	    layout_langSetting=(LinearLayout)findViewById(R.id.layout_langSetting);
	    layout_langSetting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				changLang();
				
			}
		});
	    layout_speed=(LinearLayout)findViewById(R.id.layout_tts_speed);
	    layout_speed.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				changSpeed();
				
			}
		});
	    layout_pitcs=(LinearLayout)findViewById(R.id.layout_pitcs);
	    layout_pitcs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				changPitcs();
				
			}
		});
	    layout_read=(LinearLayout)findViewById(R.id.layout_readMsg);
	    layout_read.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				changReadSetting();
				
			}
		});
	    playInSilent=(CheckBox)findViewById(R.id.checkBox_silentMode);
	    playInSilent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkPlaySound();
				
			}
		});
	    SharedPreferences=getSharedPreferences("msgNotifier", 0);
		if(!SharedPreferences.contains("silent")) {
			editor=SharedPreferences.edit();
			editor.putInt("silent", 1);
			editor.commit();
		}
		if(SharedPreferences.getInt("silent", 0)==1)
			playInSilent.setChecked(true);
		else if(SharedPreferences.getInt("silent", 0)==0)
			playInSilent.setChecked(false);
	}
	
	@SuppressLint("NewApi")
	public void checkPlaySound() {
		SharedPreferences=getSharedPreferences("msgNotifier", 0);
		if(!SharedPreferences.contains("silent")) {
			editor=SharedPreferences.edit();
			editor.putInt("silent", 1);
			editor.commit(); editor.apply();
		}
		 playInSilent=(CheckBox)findViewById(R.id.checkBox_silentMode);
		if(playInSilent.isChecked()) {
			editor=SharedPreferences.edit();
			editor.putInt("silent", 1);
			editor.commit(); editor.apply();
		}
		else if(!playInSilent.isChecked())
		{
			editor=SharedPreferences.edit();
			editor.putInt("silent", 0);
			editor.commit(); editor.apply();
		}
	}
	@SuppressLint("NewApi")
	private void changLang() {
		SharedPreferences=getSharedPreferences("msgNotifier", 0);
		if(!SharedPreferences.contains("language")) {
			editor=SharedPreferences.edit();
			editor.putInt("language", 0);
			editor.commit(); editor.apply();
		}
		settingView=getLayoutInflater().inflate(R.layout.popup_language,null);
		createAlert("Language Setting", "", settingView,0);
				
	}
	@SuppressLint("NewApi")
	private void changSpeed() {
		SharedPreferences=getSharedPreferences("msgNotifier", 0);
		if(!SharedPreferences.contains("speed")) {
			editor=SharedPreferences.edit();
			editor.putInt("speed", 2);
			editor.commit(); editor.apply();
		}
		settingView=getLayoutInflater().inflate(R.layout.popup_speed,null);
		createAlert("TTS Speed Setting", "", settingView,1);
	}
	@SuppressLint("NewApi")
	private void changPitcs() {
		SharedPreferences=getSharedPreferences("msgNotifier", 0);
		if(!SharedPreferences.contains("pitcs")) {
			editor=SharedPreferences.edit();
			editor.putInt("pitcs", 2);
			editor.commit(); editor.apply();
		}
		settingView=getLayoutInflater().inflate(R.layout.popup_picts,null);
		createAlert("Pitcs Setting", "", settingView,2);
				
	}
	@SuppressLint("NewApi")
	private void changReadSetting() {
		SharedPreferences=getSharedPreferences("msgNotifier", 0);
		if(!SharedPreferences.contains("read")) {
			editor=SharedPreferences.edit();
			editor.putInt("read", 0);
			editor.commit(); editor.apply();
		}
		settingView=getLayoutInflater().inflate(R.layout.popup_readmsg,null);
		createAlert("Reading Setting", "", settingView,3);
		
	}
	@SuppressLint("NewApi")
	
	private void createAlert(String title,String msg,View v,int x){
		final AlertDialog alert11;
		AlertDialog.Builder builder1 = new AlertDialog.Builder(SettingActivity.this);
		builder1.setTitle(title);
        builder1.setCancelable(true);
        builder1.setView(v);
        builder1.setPositiveButton("Cancel",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	
            }
        });
        alert11 = builder1.create();
        alert11.show();
        if(x==0) {
        	SharedPreferences=getSharedPreferences("msgNotifier", 0);
    		int res=SharedPreferences.getInt("language", 0);
    		editor=SharedPreferences.edit();
    		r1=(RadioButton)v.findViewById(R.id.radioButton_lang_us);
    		r2=(RadioButton)v.findViewById(R.id.radioButton_lang_uk);
    		r3=(RadioButton)v.findViewById(R.id.radioButton_lang_german);
    		r4=(RadioButton)v.findViewById(R.id.radioButton_lang_italian);
    		r5=(RadioButton)v.findViewById(R.id.radioButton_lang_korean);		
    		if(res==0)
    			r1.setChecked(true);
    		else if(res==1)
    			r2.setChecked(true);
    		else if(res==2)
    			r3.setChecked(true);
    		else if(res==3)
    			r4.setChecked(true);
    		else if(res==4)
    			r5.setChecked(true);
    		RadioGroup rgrp=(RadioGroup)v.findViewById(R.id.radiogroup_lang);
    		rgrp.setOnCheckedChangeListener(new OnCheckedChangeListener() {
    			
    			@Override
    			public void onCheckedChanged(RadioGroup arg0, int id) {
    				switch(id) {
    				case R.id.radioButton_lang_us :
    					editor.putInt("language", 0);
    					alert11.dismiss();
    					break;
    				case R.id.radioButton_lang_uk :
    					editor.putInt("language", 1);
    					alert11.dismiss();
    					break;
    				case R.id.radioButton_lang_german :
    					editor.putInt("language", 2);
    					alert11.dismiss();
    					break;
    				case R.id.radioButton_lang_italian :
    					editor.putInt("language", 3);
    					alert11.dismiss();
    					break;
    				case R.id.radioButton_lang_korean :
    					editor.putInt("language", 4);
    					alert11.dismiss();
    					break;
    					default : break;
    				}
    				editor.commit();  editor.apply();
    			}
    		});
        }
        if(x==1) {
        	SharedPreferences=getSharedPreferences("msgNotifier", 0);
    		int res=SharedPreferences.getInt("speed", 0);
    		editor=SharedPreferences.edit();
    		r1=(RadioButton)v.findViewById(R.id.radioButton_speed_vslow);
    		r2=(RadioButton)v.findViewById(R.id.radioButton_speed_slow);
    		r3=(RadioButton)v.findViewById(R.id.radioButton_speed_normal);
    		r4=(RadioButton)v.findViewById(R.id.radioButton_speed_fast);
    		r5=(RadioButton)v.findViewById(R.id.radioButton_speed_vfast);		
    		if(res==0)
    			r1.setChecked(true);
    		else if(res==1)
    			r2.setChecked(true);
    		else if(res==2)
    			r3.setChecked(true);
    		else if(res==3)
    			r4.setChecked(true);
    		else if(res==4)
    			r5.setChecked(true);
    		RadioGroup rgrp=(RadioGroup)v.findViewById(R.id.radiogroup_speed);
    		rgrp.setOnCheckedChangeListener(new OnCheckedChangeListener() {
    			
    			@Override
    			public void onCheckedChanged(RadioGroup arg0, int id) {
    				switch(id) {
    				case R.id.radioButton_speed_vslow :
    					editor.putInt("speed", 0);
    					alert11.dismiss();
    					break;
    				case R.id.radioButton_speed_slow :
    					editor.putInt("speed", 1);
    					alert11.dismiss();
    					break;
    				case R.id.radioButton_speed_normal :
    					editor.putInt("speed", 2);
    					alert11.dismiss();
    					break;
    				case R.id.radioButton_speed_fast :
    					editor.putInt("speed", 3);
    					alert11.dismiss();
    					break;
    				case R.id.radioButton_speed_vfast :
    					editor.putInt("speed", 4);
    					alert11.dismiss();
    					break;
    					default : break;
    				}
    				editor.commit();  editor.apply();
    			}
    		});
        }
        
        
        if(x==2) {
        	SharedPreferences=getSharedPreferences("msgNotifier", 0);
    		int res=SharedPreferences.getInt("pitcs", 0);
    		editor=SharedPreferences.edit();
    		r1=(RadioButton)v.findViewById(R.id.radioButton_picts_vslow);
    		r2=(RadioButton)v.findViewById(R.id.radioButton_picts_slow);
    		r3=(RadioButton)v.findViewById(R.id.radioButton_picts_normal);
    		r4=(RadioButton)v.findViewById(R.id.radioButton_picts_fast);
    		r5=(RadioButton)v.findViewById(R.id.radioButton_picts_vfast);		
    		if(res==0)
    			r1.setChecked(true);
    		else if(res==1)
    			r2.setChecked(true);
    		else if(res==2)
    			r3.setChecked(true);
    		else if(res==3)
    			r4.setChecked(true);
    		else if(res==4)
    			r5.setChecked(true);
    		RadioGroup rgrp=(RadioGroup)v.findViewById(R.id.radiogroup_picts);
    		rgrp.setOnCheckedChangeListener(new OnCheckedChangeListener() {
    			
    			@Override
    			public void onCheckedChanged(RadioGroup arg0, int id) {
    				switch(id) {
    				case R.id.radioButton_picts_vslow :
    					editor.putInt("pitcs", 0);
    					alert11.dismiss();
    					break;
    				case R.id.radioButton_picts_slow :
    					editor.putInt("pitcs", 1);
    					alert11.dismiss();
    					break;
    				case R.id.radioButton_picts_normal :
    					editor.putInt("pitcs", 2);
    					alert11.dismiss();
    					break;
    				case R.id.radioButton_picts_fast :
    					editor.putInt("pitcs", 3);
    					alert11.dismiss();
    					break;
    				case R.id.radioButton_picts_vfast :
    					editor.putInt("pitcs", 4);
    					alert11.dismiss();
    					break;
    					default : break;
    				}
    				editor.commit();  editor.apply();
    			}
    		});
        }
        
        if(x==3) {
        	SharedPreferences=getSharedPreferences("msgNotifier", 0);
    		int res=SharedPreferences.getInt("read", 0);
    		editor=SharedPreferences.edit();
    		r1=(RadioButton)v.findViewById(R.id.radioButton_both);
    		r2=(RadioButton)v.findViewById(R.id.radioButton_onlySender);
    		r3=(RadioButton)v.findViewById(R.id.radioButton_onlyMsg);
    		r4=(RadioButton)v.findViewById(R.id.radioButton_dntRead);		
    		if(res==0)
    			r1.setChecked(true);
    		else if(res==1)
    			r2.setChecked(true);
    		else if(res==2)
    			r3.setChecked(true);
    		else if(res==3)
    			r4.setChecked(true);
    		RadioGroup rgrp=(RadioGroup)v.findViewById(R.id.radiogroup_read);
    		rgrp.setOnCheckedChangeListener(new OnCheckedChangeListener() {
    			
    			@Override
    			public void onCheckedChanged(RadioGroup arg0, int id) {
    				switch(id) {
    				case R.id.radioButton_both :
    					editor.putInt("read", 0);
    					alert11.dismiss();
    					break;
    				case R.id.radioButton_onlySender :
    					editor.putInt("read", 1);
    					alert11.dismiss();
    					break;
    				case R.id.radioButton_onlyMsg :
    					editor.putInt("read", 2);
    					alert11.dismiss();
    					break;
    				case R.id.radioButton_dntRead :
    					editor.putInt("read", 3);
    					alert11.dismiss();
    					break;
    					default : break;
    				}
    				editor.commit();  editor.apply();
    			}
    		});
        }
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)	{       
	    onBackPressed();
	    return true;
	}
}
