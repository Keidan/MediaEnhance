package fr.ralala.mediaenhance;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import fr.ralala.mediaenhance.utils.Helper;
import fr.ralala.mediaenhance.mediakey.MediaKeyMethod;
import fr.ralala.mediaenhance.utils.HelperUI;

/**
 *******************************************************************************
 * <p><b>Project MediaEnhance</b><br/>
 * Main activity
 * </p>
 * @author Keidan
 *
 *******************************************************************************
 */
public class MediaEnhanceActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
  private EditText mEtDelayUp = null;
  private EditText mEtDelayDown = null;
  private EditText mEtMinUp = null;
  private EditText mEtMinDown = null;
  private EditText mEtNbUp = null;
  private EditText mEtNbDown = null;
  private TextView mLblWarningMethod = null;
  private TextView mLblWarning = null;
  private TextView mTvNbUp = null;
  private TextView mTvNbDown = null;
  private Spinner mSpMethod = null;
  private CheckBox mChkDisplayToast = null;
  private Button mBtApply = null;
  private ToggleButton mToggleOnOff = null;
  private String mWarningMethodSettings = null;
  private String mWarningMethodHidden = null;

  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    MediaEnhanceApp app = ((MediaEnhanceApp)getApplication());
    app.loadConfig();

    Resources r = getResources();
    mWarningMethodSettings = r.getString(R.string.warningMethodSettings);
    mWarningMethodHidden = r.getString(R.string.warningMethodHidden);

    /* Initialization */
    mEtDelayUp = findViewById(R.id.etDelayUp);
    mEtDelayDown = findViewById(R.id.etDelayDown);
    mEtMinUp = findViewById(R.id.etMinUp);
    mEtMinDown = findViewById(R.id.etMinDown);
    mEtNbUp = findViewById(R.id.etNbUp);
    mEtNbDown = findViewById(R.id.etNbDown);
    mLblWarningMethod = findViewById(R.id.lblWarningMethod);
    mLblWarning = findViewById(R.id.lblWarning);
    mTvNbUp = findViewById(R.id.tvNbUp);
    mTvNbDown = findViewById(R.id.tvNbDown);
    mSpMethod = findViewById(R.id.spMethod);
    mChkDisplayToast = findViewById(R.id.chkDisplayToast);
    mBtApply = findViewById(R.id.btApply);
    mToggleOnOff = findViewById(R.id.toggleOnOff);

    /* Texts initializations */
    mEtDelayUp.setText(String.valueOf(app.getTimeDelayUp()));
    mEtDelayDown.setText(String.valueOf(app.getTimeDelayDown()));
    mEtNbUp.setText(String.valueOf(app.getDeltaUp()));
    mEtNbDown.setText(String.valueOf(app.getDeltaDown()));
    mToggleOnOff.setChecked(Helper.isServiceRunning(this, MediaEnhanceService.class));
    mChkDisplayToast.setChecked(app.isDisplayToast());
    mEtMinUp.setText(String.valueOf(app.getTimeMinUp()));
    mEtMinDown.setText(String.valueOf(app.getTimeMinDown()));

    final List<MediaKeyMethod> list = new ArrayList<>();
    list.add(MediaKeyMethod.HIDDEN);
    list.add(MediaKeyMethod.SETTINGS);
    final ArrayAdapter<MediaKeyMethod> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
    //adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mSpMethod.setAdapter(adapter);
    mSpMethod.setSelection(app.getMediaKeyMethod().id());
    updateUI(app.getMediaKeyMethod());
    /* Adds listeners */
    mSpMethod.setOnItemSelectedListener(this);
    mBtApply.setOnClickListener(this);
    mToggleOnOff.setOnClickListener(this);
    mLblWarning.setOnClickListener(this);
  }

  /**
   * Called when the activity is resumed.
   */
  @Override
  public void onResume() {
    super.onResume();
    if(Helper.hasAudioControlPermissions(this)) {
      mLblWarning.setVisibility(View.GONE);
    } else {
      mLblWarning.setVisibility(View.VISIBLE);
      if (mToggleOnOff.isChecked())
        mToggleOnOff.setChecked(false);
      if (Helper.isServiceRunning(this, MediaEnhanceService.class)) {
        ((MediaEnhanceApp)getApplication()).setSenpuku(true);
        stopService(new Intent(this, MediaEnhanceService.class));
      }
    }
  }

  /**
   * Updates the UI when the method is changed.
   * @param method The new method.
   */
  private void updateUI(final MediaKeyMethod method) {
    String text;
    int visibility;
    if (method == MediaKeyMethod.HIDDEN) {
      text = mWarningMethodHidden;
      visibility = View.GONE;
    }
    else {
      text = mWarningMethodSettings;
      visibility = View.VISIBLE;
    }
    mLblWarningMethod.setText(text);
    mTvNbDown.setVisibility(visibility);
    mEtNbDown.setVisibility(visibility);
    mTvNbUp.setVisibility(visibility);
    mEtNbUp.setVisibility(visibility);
  }

  @Override
  public void onItemSelected(final AdapterView<?> parent, final View view,
                             final int pos, final long id) {
    updateUI(MediaKeyMethod.fromString(parent.getItemAtPosition(pos).toString()));
  }

  /**
   * Saves the new configuration.
   */
  private void save() {
    MediaEnhanceApp app = ((MediaEnhanceApp)getApplication());
    app.setDeltaDown(HelperUI.getInt(mEtNbDown, MediaEnhanceApp.DEFAULT_DELTA));
    app.setDeltaUp(HelperUI.getInt(mEtNbUp, MediaEnhanceApp.DEFAULT_DELTA));
    app.setTimeDelayDown(HelperUI.getLong(mEtDelayDown, MediaEnhanceApp.DEFAULT_TIME_DELAY));
    app.setTimeDelayUp(HelperUI.getLong(mEtDelayUp, MediaEnhanceApp.DEFAULT_TIME_DELAY));
    app.setMediaKeyMethod((MediaKeyMethod)mSpMethod.getSelectedItem());
    app.setDisplayToast(mChkDisplayToast.isChecked());
    app.setTimeMinDown(HelperUI.getLong(mEtMinDown, MediaEnhanceApp.DEFAULT_TIME_MIN));
    app.setTimeMinUp(HelperUI.getLong(mEtMinUp, MediaEnhanceApp.DEFAULT_TIME_MIN));
    app.saveConfig();
  }

  @Override
  public void onClick(View v) {
    if(v.equals(mBtApply)) {
      save();
      if(!mToggleOnOff.isChecked() && Helper.isServiceRunning(this, MediaEnhanceService.class))
        mToggleOnOff.setChecked(true);
    } else if(v.equals(mToggleOnOff)) {
      if (Helper.hasAudioControlPermissions(this)) {
        if(!mToggleOnOff.isChecked()) {
          ((MediaEnhanceApp)getApplication()).setSenpuku(true);
          stopService(new Intent(this, MediaEnhanceService.class));
        } else {
          save();
          ((MediaEnhanceApp)getApplication()).setSenpuku(false);
          startService(new Intent(this, MediaEnhanceService.class));
        }
      } else {
        HelperUI.toast(this, getString(R.string.error_control_media));
        mToggleOnOff.setChecked(false);
        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(intent);
      }
    }
  }

  @Override
  public void onNothingSelected(final AdapterView<?> parent) {
  }

}
