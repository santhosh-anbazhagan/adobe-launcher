package com.santhosh.launcher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import androidx.activity.result.ActivityResult;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.JSObject;

import java.util.List;

@CapacitorPlugin(name = "AdobeLauncher")
public class AdobeLauncherPlugin extends Plugin {

    private PluginCall savedFilePickerCall;
    
    private static final int PICK_FILE_REQUEST = 1001; // Unique int request code

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

    @PluginMethod
    public void openApp2(com.getcapacitor.PluginCall call) {
        String packageName = call.getString("packageName");

        if (packageName == null || packageName.isEmpty()) {
            call.reject("Package name is required");
            return;
        }

        try {
            Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(packageName);
            if (intent == null) {
                call.reject("App not found: " + packageName);
                return;
            }
            getActivity().startActivity(intent);

            JSObject result = new JSObject();
            result.put("success", true);
            call.resolve(result);
        } catch (Exception ex) {
            Log.e("AdobeScanLauncher", "Error launching app", ex);
            call.reject("Error launching app: " + ex.getMessage());
        }
    }

    @PluginMethod
    public void pickFile(PluginCall call) {
        Log.d("AdobeLauncherPlugin", "pickFile called");
        savedFilePickerCall = call;
        
        // Create intent to pick files - following reference implementation
        // ⚠️ Note: ACTION_OPEN_DOCUMENT ensures compatibility from API 19+ with Scoped Storage
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        // Allow multiple file types (enhanced beyond reference)
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{
            "application/pdf",
            "image/jpeg",
            "image/png",
            "image/jpg"
        });
        
        Log.d("AdobeLauncherPlugin", "Starting file picker activity with request code: " + PICK_FILE_REQUEST);
        // Use MainActivity's startActivityForResult instead of plugin's method
        getActivity().startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    protected void handleOnResume() {
        super.handleOnResume();
        Log.d("AdobeLauncherPlugin", "App resumed - savedFilePickerCall: " + (savedFilePickerCall != null ? "exists" : "null"));
    }
    
    public void handleFilePickerResult(int resultCode, Intent data) {
        Log.d("AdobeLauncherPlugin", "handleFilePickerResult called - resultCode: " + resultCode + ", data: " + (data != null ? "not null" : "null"));
        
        if (savedFilePickerCall == null) {
            Log.e("AdobeLauncherPlugin", "savedFilePickerCall is null - plugin call was lost");
            return;
        }
        
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data != null ? data.getData() : null;
            Log.d("AdobeLauncherPlugin", "File selection result - URI: " + (uri != null ? uri.toString() : "null"));
            
            if (uri != null) {
                Log.d("AdobeLauncherPlugin", "File selected: " + uri.toString());
                
                // Request persistent permission for the URI - following reference implementation
                getContext().getContentResolver().takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                );
                
                // Get file information (enhanced beyond reference)
                String fileName = getFileName(uri);
                long fileSize = getFileSize(uri);
                String mimeType = getMimeType(uri);
                
                Log.d("AdobeLauncherPlugin", "File details - name: " + fileName + ", size: " + fileSize + ", mimeType: " + mimeType);
                
                JSObject result = new JSObject();
                result.put("uri", uri.toString());
                result.put("name", fileName);
                result.put("size", fileSize);
                result.put("mimeType", mimeType);
                
                Log.d("AdobeLauncherPlugin", "Resolving plugin call with result");
                savedFilePickerCall.resolve(result);
                savedFilePickerCall = null; // Clear the saved call
            } else {
                Log.d("AdobeLauncherPlugin", "No file selected - URI is null");
                savedFilePickerCall.reject("No file selected");
                savedFilePickerCall = null; // Clear the saved call
            }
        } else {
            Log.d("AdobeLauncherPlugin", "File selection cancelled or failed - resultCode: " + resultCode);
            savedFilePickerCall.reject("File selection cancelled");
            savedFilePickerCall = null; // Clear the saved call
        }
    }

    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("AdobeLauncherPlugin", "handleOnActivityResult called - requestCode: " + requestCode + ", resultCode: " + resultCode + ", data: " + (data != null ? "not null" : "null"));
        
        if (requestCode == PICK_FILE_REQUEST) {
            Log.d("AdobeLauncherPlugin", "Processing file picker result - savedFilePickerCall: " + (savedFilePickerCall != null ? "exists" : "null"));
            
            if (savedFilePickerCall == null) {
                Log.e("AdobeLauncherPlugin", "savedFilePickerCall is null - plugin call was lost");
                return;
            }
            
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data != null ? data.getData() : null;
                Log.d("AdobeLauncherPlugin", "File selection result - URI: " + (uri != null ? uri.toString() : "null"));
                
                if (uri != null) {
                    Log.d("AdobeLauncherPlugin", "File selected: " + uri.toString());
                    
                    // Request persistent permission for the URI - following reference implementation
                    getContext().getContentResolver().takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );
                    
                    // Get file information (enhanced beyond reference)
                    String fileName = getFileName(uri);
                    long fileSize = getFileSize(uri);
                    String mimeType = getMimeType(uri);
                    
                    Log.d("AdobeLauncherPlugin", "File details - name: " + fileName + ", size: " + fileSize + ", mimeType: " + mimeType);
                    
                    JSObject result = new JSObject();
                    result.put("uri", uri.toString());
                    result.put("name", fileName);
                    result.put("size", fileSize);
                    result.put("mimeType", mimeType);
                    
                    Log.d("AdobeLauncherPlugin", "Resolving plugin call with result");
                    savedFilePickerCall.resolve(result);
                    savedFilePickerCall = null; // Clear the saved call
                } else {
                    Log.d("AdobeLauncherPlugin", "No file selected - URI is null");
                    savedFilePickerCall.reject("No file selected");
                    savedFilePickerCall = null; // Clear the saved call
                }
            } else {
                Log.d("AdobeLauncherPlugin", "File selection cancelled or failed - resultCode: " + resultCode);
                savedFilePickerCall.reject("File selection cancelled");
                savedFilePickerCall = null; // Clear the saved call
            }
        } else {
            Log.d("AdobeLauncherPlugin", "Request code mismatch - expected: " + PICK_FILE_REQUEST + ", got: " + requestCode);
        }
        
        super.handleOnActivityResult(requestCode, resultCode, data);
    }

    private String getFileName(Uri uri) {
        String fileName = "Unknown";
        android.database.Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME);
                if (nameIndex >= 0) {
                    fileName = cursor.getString(nameIndex);
                    if (fileName == null) {
                        fileName = "Unknown";
                    }
                }
            }
            cursor.close();
        }
        return fileName;
    }

    private long getFileSize(Uri uri) {
        long fileSize = 0L;
        android.database.Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int sizeIndex = cursor.getColumnIndex(android.provider.OpenableColumns.SIZE);
                if (sizeIndex >= 0) {
                    fileSize = cursor.getLong(sizeIndex);
                }
            }
            cursor.close();
        }
        return fileSize;
    }

    private String getMimeType(Uri uri) {
        return getContext().getContentResolver().getType(uri);
    }
}
