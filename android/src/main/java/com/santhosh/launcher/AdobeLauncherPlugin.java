package com.santhosh.launcher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import androidx.activity.result.ActivityResult;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.Plugin;
import com.getcapacitor.JSObject;

import java.util.List;

@CapacitorPlugin(name = "AdobeLauncher")
public class AdobeLauncherPlugin extends Plugin {

    @PluginMethod
    public void canOpenUrl(com.getcapacitor.PluginCall call) {
        String url = call.getString("url");

        JSObject ret = new JSObject();

        if (url == null || url.isEmpty()) {
            ret.put("value", false);
            ret.put("error", "URL is null or empty");
            call.resolve(ret);
            return;
        }

        PackageManager pm = getContext().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);

        boolean canOpen = !activities.isEmpty();
        ret.put("value", canOpen);
        call.resolve(ret);
    }

    @PluginMethod
    public void openUrl(com.getcapacitor.PluginCall call) {
        String url = call.getString("url");
        String mimeType = call.getString("mimeType", "*/*");
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(url), mimeType);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            getContext().startActivity(intent);

            JSObject ret = new JSObject();
            ret.put("value", true);
            call.resolve(ret);
        } catch (Exception e) {
            call.reject("Could not open URL: " + e.getMessage());
        }
    }

    @PluginMethod
    public void openAdobeScan(com.getcapacitor.PluginCall call) {
        String url = call.getString("url", "adobescan://"); // Default to Adobe Scan URL scheme
        PackageManager pm = getContext().getPackageManager();
        
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            
            List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
            if (!activities.isEmpty()) {
                getContext().startActivity(intent);
                JSObject ret = new JSObject();
                ret.put("value", true);
                call.resolve(ret);
            } else {
                JSObject ret = new JSObject();
                ret.put("value", false);
                call.resolve(ret);
            }
        } catch (Exception e) {
            call.reject("Error launching Adobe Scan: " + e.getMessage());
        }
    }

    @PluginMethod
    public void openApp(com.getcapacitor.PluginCall call) {
        String packageName = call.getString("packageName");
        String mimeType = call.getString("mimeType", "*/*");
        String action = call.getString("action", Intent.ACTION_VIEW);
        String data = call.getString("data");

        PackageManager pm = getContext().getPackageManager();

        try {
            Intent intent = pm.getLaunchIntentForPackage(packageName);

            if (intent == null) {
                call.reject("App not found: " + packageName);
                return;
            }

            intent.setAction(action);

            if (data != null) {
                Uri uri = Uri.parse(data);
                intent.setDataAndType(uri, mimeType);
            }

            getActivity().startActivity(intent);
            JSObject result = new JSObject();
            result.put("success", true);
            call.resolve(result);
            // call.resolve(JSObject.fromJSONObject("{\"success\":true}"));
        } catch (Exception e) {
            Log.e("AdobeScanLauncher", "Error launching app", e);
            call.reject("Error launching app: " + e.getMessage());
        }
    }

    @PluginMethod
    public void canOpenApp(com.getcapacitor.PluginCall call) {
        String packageName = call.getString("packageName", "com.adobe.scan.android");
        PackageManager pm = getContext().getPackageManager();

        JSObject ret = new JSObject();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            ret.put("value", true);
        } catch (PackageManager.NameNotFoundException e) {
            ret.put("value", false);
        }

        call.resolve(ret);
    }
}
