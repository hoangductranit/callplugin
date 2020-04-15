package cordova.plugin.callplugin;

// The native android API
import android.content.pm.PackageManager;
import android.content.Context;
import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import java.util.List;

// Cordova-required packages
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.security.auth.callback.Callback;

/**
 * This class echoes a string called from JavaScript.
 */
public class callplugin extends CordovaPlugin {

    private static final String CALL_NUMBER = "callNumber";
    private CallbackContext callback;
    String [] permissions = { Manifest.permission.CALL_PHONE };
    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
         callback = callbackContext;

        if (CALL_NUMBER.equals(action)) {
            callNumber(args.getString(0));
            return true;
        } 
        else {
            return false;
        }
    }

    private void callNumber(String phoneNumber) throws JSONException {
    if (hasPermission()) {
      Uri call = Uri.parse("tel:" + phoneNumber);             
      Intent intent = new Intent(Intent.ACTION_CALL, call); 
      cordova.getActivity().startActivity(intent);
      callback.sendPluginResult(new PluginResult(Status.OK));
    } else {
      PermissionHelper.requestPermissions(this, 0, permissions);
    }

  }

  public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
      PluginResult result;
      if(callback != null) {
          for (int r : grantResults) {
              if (r == PackageManager.PERMISSION_DENIED) {
                  result = new PluginResult(PluginResult.Status.ILLEGAL_ACCESS_EXCEPTION);
                  callback.sendPluginResult(result);
                  return;
              }

          }
          result = new PluginResult(PluginResult.Status.OK);
          callback.sendPluginResult(result);
      }
  }

  public boolean hasPermission() {
    for(String p : permissions) {
        if(!PermissionHelper.hasPermission(this, p)) {
            return false;
        }
    }
    return true;
  }

  public void requestPermissions(int requestCode) {
      PermissionHelper.requestPermissions(this, requestCode, permissions);
  }
}
