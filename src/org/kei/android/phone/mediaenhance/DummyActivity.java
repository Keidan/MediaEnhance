package org.kei.android.phone.mediaenhance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 *******************************************************************************
 * @file DummyActivity.java
 * @author Keidan
 * @date 21/01/2016
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
public class DummyActivity extends Activity {
  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    startService(new Intent(this, MediaEnhance.class));
    this.finish();
  }
}
