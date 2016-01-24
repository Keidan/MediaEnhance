package org.kei.android.phone.mediaenhance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
    
    MediaEnhanceApp app = ((MediaEnhanceApp)getApplication());
    app.loadConfig();
    etDelayUp.setText(String.valueOf(app.getTimeDelayUp()));
    etDelayDown.setText(String.valueOf(app.getTimeDelayDown()));
    etNbUp.setText(String.valueOf(app.getDeltaUp()));
    etNbDown.setText(String.valueOf(app.getDeltaDown()));
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
      MediaEnhanceApp app = ((MediaEnhanceApp)getApplication());
      app.setDeltaDown(getInt(etNbDown.getText().toString(), MediaEnhanceApp.DEFAULT_DELTA));
      app.setDeltaUp(getInt(etNbUp.getText().toString(), MediaEnhanceApp.DEFAULT_DELTA));
      app.setTimeDelayDown(getLong(etDelayDown.getText().toString(), MediaEnhanceApp.DEFAULT_TIME_DELAY));
      app.setTimeDelayUp(getLong(etDelayUp.getText().toString(), MediaEnhanceApp.DEFAULT_TIME_DELAY));
      app.saveConfig();
      if(!toggleOnOff.isChecked() && Tools.isServiceRunning(this, MediaEnhanceService.class)) toggleOnOff.setChecked(true);
    } else if(v.equals(toggleOnOff)) {
      if(!toggleOnOff.isChecked()) {
        ((MediaEnhanceApp)getApplication()).setSenpuku(true);
        stopService(new Intent(this, MediaEnhanceService.class));
      } else {
        ((MediaEnhanceApp)getApplication()).setSenpuku(false);
        startService(new Intent(this, MediaEnhanceService.class));
      }
    }
  }
  
  private long getLong(final String val, final long def) {
    long l = def;
    try {
      l = Long.parseLong(val);
    } catch(Exception ex) {
      l = def;
    }
    return l;
  }
  
  private int getInt(final String val, final int def) {
    int l = def;
    try {
      l = Integer.parseInt(val);
    } catch(Exception ex) {
      l = def;
    }
    return l;
  }
}
