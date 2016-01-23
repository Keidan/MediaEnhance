package org.kei.android.phone.mediaenhance;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

/**
 *******************************************************************************
 * @file MediaEnhanceActivity.java
 * @author Keidan
 * @date 23/01/2016
 * @par Project MediaEnhance
 *
 * @par Copyright 2016 Keidan, all right reserved
 *
 *      This software is distributed in the hope that it will be useful, but
 *      WITHOUT ANY WARRANTY.
 *
 *      License summary : You can modify and redistribute the sources code and
 *      binaries. You can send me the bug-fix
 *
 *      Term of the license in in the file license.txt.
 *
 *******************************************************************************
 */
public class MediaEnhanceActivity extends Activity implements OnClickListener {
  private EditText     etDelayUp   = null;
  private EditText     etDelayDown = null;
  private EditText     etNbUp      = null;
  private EditText     etNbDown    = null;
  private Button       btApply     = null;
  private ToggleButton toggleOnOff = null;
  
  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    etDelayUp = (EditText)findViewById(R.id.etDelayUp);
    etDelayDown = (EditText)findViewById(R.id.etDelayDown);
    etNbUp = (EditText)findViewById(R.id.etNbUp);
    etNbDown = (EditText)findViewById(R.id.etNbDown);
    btApply = (Button)findViewById(R.id.btApply);
    toggleOnOff = (ToggleButton)findViewById(R.id.toggleOnOff);
    
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    etDelayUp.setText(String.valueOf(
        prefs.getLong(MediaEnhanceService.KEY_TIME_DELAY_UP, MediaEnhanceService.DEFAULT_TIME_DELAY)));
    etDelayDown.setText(String.valueOf(
        prefs.getLong(MediaEnhanceService.KEY_TIME_DELAY_DOWN, MediaEnhanceService.DEFAULT_TIME_DELAY)));
    etNbUp.setText(String.valueOf(
        prefs.getInt(MediaEnhanceService.KEY_DELTA_UP, MediaEnhanceService.DEFAULT_DELTA)));
    etNbDown.setText(String.valueOf(
        prefs.getInt(MediaEnhanceService.KEY_DELTA_DOWN, MediaEnhanceService.DEFAULT_DELTA)));
    if(Tools.isServiceRunning(this, MediaEnhanceService.class))
      toggleOnOff.setChecked(true);
    //startService(new Intent(this, MediaEnhanceService.class));
    //this.finish();

    btApply.setOnClickListener(this);
    toggleOnOff.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    if(v.equals(btApply)) {
      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
      Editor e = prefs.edit();
      addLong(e, MediaEnhanceService.KEY_TIME_DELAY_UP, 
          etDelayUp.getText().toString(), MediaEnhanceService.DEFAULT_TIME_DELAY);
      addLong(e, MediaEnhanceService.KEY_TIME_DELAY_DOWN, 
          etDelayDown.getText().toString(), MediaEnhanceService.DEFAULT_TIME_DELAY);
      addInt(e, MediaEnhanceService.KEY_DELTA_UP, 
          etNbUp.getText().toString(), MediaEnhanceService.DEFAULT_DELTA);
      addInt(e, MediaEnhanceService.KEY_DELTA_DOWN, 
          etNbDown.getText().toString(), MediaEnhanceService.DEFAULT_DELTA);
      e.commit();
      Intent i = new Intent(this, MediaEnhanceService.class);
      i.setAction(MediaEnhanceService.ACTION_APPLY);
      startService(i);
      if(!toggleOnOff.isChecked() && Tools.isServiceRunning(this, MediaEnhanceService.class)) toggleOnOff.setChecked(true);
    } else if(v.equals(toggleOnOff)) {
      if(!toggleOnOff.isChecked()) {
        Intent i = new Intent(this, MediaEnhanceService.class);
        i.setAction(MediaEnhanceService.ACTION_SENPUKU);
        stopService(i);
      } else {
        startService(new Intent(this, MediaEnhanceService.class));
      }
    }
  }
  
  private void addLong(final Editor e, final String key, final String val, final long def) {
    long l = def;
    try {
      l = Long.parseLong(val);
    } catch(Exception ex) {
      l = def;
    } finally {
      e.putLong(key, l);
    }
  }
  
  private void addInt(final Editor e, final String key, final String val, final int def) {
    int l = def;
    try {
      l = Integer.parseInt(val);
    } catch(Exception ex) {
      l = def;
    } finally {
      e.putInt(key, l);
    }
  }
}
