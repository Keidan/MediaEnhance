package org.kei.android.phone.mediaenhance;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemSelectedListener;

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
public class MediaEnhanceActivity extends Activity implements OnClickListener, OnItemSelectedListener {
  private EditText     etDelayUp             = null;
  private EditText     etDelayDown           = null;
  private EditText     etNbUp                = null;
  private EditText     etNbDown              = null;
  private TextView     lblWarningMethod      = null;
  private TextView     tvNbUp                = null;
  private TextView     tvNbDown              = null;
  private Spinner      spMethod              = null;
  private Button       btApply               = null;
  private ToggleButton toggleOnOff           = null;
  private String       warningMethodSettings = null;
  private String       warningMethodHidden   = null;
  
  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    
    Resources r = getResources();
    warningMethodSettings = r.getString(R.string.warningMethodSettings);
    warningMethodHidden = r.getString(R.string.warningMethodHidden);    
    
    
    etDelayUp = (EditText)findViewById(R.id.etDelayUp);
    etDelayDown = (EditText)findViewById(R.id.etDelayDown);
    etNbUp = (EditText)findViewById(R.id.etNbUp);
    etNbDown = (EditText)findViewById(R.id.etNbDown);
    lblWarningMethod = (TextView)findViewById(R.id.lblWarningMethod);
    tvNbUp = (TextView)findViewById(R.id.tvNbUp);
    tvNbDown = (TextView)findViewById(R.id.tvNbDown);
    spMethod = (Spinner)findViewById(R.id.spMethod);
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
 
    final List<String> list = new ArrayList<String>();
    list.add(MediaEnhanceApp.VOLUME_METHOD_HIDDEN);
    list.add(MediaEnhanceApp.VOLUME_METHOD_SETTINGS);
    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        this, android.R.layout.simple_spinner_item, list);
    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
    spMethod.setAdapter(adapter);
    if (app.getVolumeMethod().equals(MediaEnhanceApp.VOLUME_METHOD_HIDDEN))
      spMethod.setSelection(0);
    else
      spMethod.setSelection(1);
    updateUI(app.getVolumeMethod());
    spMethod.setOnItemSelectedListener(this);
    btApply.setOnClickListener(this);
    toggleOnOff.setOnClickListener(this);
  }
  
  private void updateUI(final String method) {
    String text = null;
    int visibility = 0;
    if (method.equals(MediaEnhanceApp.VOLUME_METHOD_HIDDEN)) {
      text = warningMethodHidden;
      visibility = View.GONE;
    }
    else {
      text = warningMethodSettings;
      visibility = View.VISIBLE;
    }
    lblWarningMethod.setText(text);
    tvNbDown.setVisibility(visibility);
    etNbDown.setVisibility(visibility);
    tvNbUp.setVisibility(visibility);
    etNbUp.setVisibility(visibility);
  }
  
  @Override
  public void onItemSelected(final AdapterView<?> parent, final View view,
      final int pos, final long id) {
    updateUI(parent.getItemAtPosition(pos).toString());
  }
  
  private void save() {
    MediaEnhanceApp app = ((MediaEnhanceApp)getApplication());
    app.setDeltaDown(getInt(etNbDown.getText().toString(), MediaEnhanceApp.DEFAULT_DELTA));
    app.setDeltaUp(getInt(etNbUp.getText().toString(), MediaEnhanceApp.DEFAULT_DELTA));
    app.setTimeDelayDown(getLong(etDelayDown.getText().toString(), MediaEnhanceApp.DEFAULT_TIME_DELAY));
    app.setTimeDelayUp(getLong(etDelayUp.getText().toString(), MediaEnhanceApp.DEFAULT_TIME_DELAY));
    app.setVolumeMethod(""+spMethod.getSelectedItem());
    app.saveConfig();
  }

  @Override
  public void onClick(View v) {
    if(v.equals(btApply)) {
      save();
      if(!toggleOnOff.isChecked() && Tools.isServiceRunning(this, MediaEnhanceService.class)) toggleOnOff.setChecked(true);
    } else if(v.equals(toggleOnOff)) {
      if(!toggleOnOff.isChecked()) {
        ((MediaEnhanceApp)getApplication()).setSenpuku(true);
        stopService(new Intent(this, MediaEnhanceService.class));
      } else {
        save();
        ((MediaEnhanceApp)getApplication()).setSenpuku(false);
        startService(new Intent(this, MediaEnhanceService.class));
      }
    }
  }

  @Override
  public void onNothingSelected(final AdapterView<?> parent) {
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
