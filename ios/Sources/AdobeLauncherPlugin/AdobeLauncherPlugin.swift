import Foundation
import Capacitor
import UIKit

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(AdobeLauncherPlugin)
public class AdobeLauncherPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "AdobeLauncherPlugin"
    public let jsName = "AdobeLauncher"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "canOpenUrl", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "canOpenApp", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "openUrl", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "openAdobeScan", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "openApp", returnType: CAPPluginReturnPromise)
    ]

    @objc func canOpenUrl(_ call: CAPPluginCall) {
        guard let urlString = call.getString("url") else {
            call.resolve([
                "value": false,
                "error": "URL parameter is required"
            ])
            return
        }
        
        guard let url = URL(string: urlString) else {
            call.resolve([
                "value": false,
                "error": "Invalid URL format"
            ])
            return
        }
        
        let canOpen = UIApplication.shared.canOpenURL(url)
        call.resolve([
            "value": canOpen
        ])
    }
    
    @objc func openUrl(_ call: CAPPluginCall) {
        guard let urlString = call.getString("url") else {
            call.reject("URL parameter is required")
            return
        }
        
        guard let url = URL(string: urlString) else {
            call.reject("Invalid URL format")
            return
        }
        
        DispatchQueue.main.async {
            if UIApplication.shared.canOpenURL(url) {
                UIApplication.shared.open(url) { success in
                    call.resolve([
                        "value": success
                    ])
                }
            } else {
                call.reject("Cannot open URL")
            }
        }
    }
    
    @objc func openAdobeScan(_ call: CAPPluginCall) {
        let urlString = call.getString("url") ?? "adobescan://"
        guard let adobeScanURL = URL(string: urlString) else {
            call.reject("Invalid URL format")
            return
        }
        
        DispatchQueue.main.async {
            if UIApplication.shared.canOpenURL(adobeScanURL) {
                UIApplication.shared.open(adobeScanURL) { success in
                    call.resolve([
                        "value": success
                    ])
                }
            } else {
                // Try to open App Store if Adobe Scan is not installed
                let appStoreURL = URL(string: "https://apps.apple.com/app/adobe-scan/id1199564834")!
                UIApplication.shared.open(appStoreURL) { success in
                    call.resolve([
                        "value": false
                    ])
                }
            }
        }
    }
    
    @objc func canOpenApp(_ call: CAPPluginCall) {
        let packageName = call.getString("packageName") ?? "com.adobe.scan.android"
        
        // For iOS, we'll try to check if the app can be opened using URL schemes
        // This is a simplified approach since iOS doesn't have direct package checking like Android
        let appURL = URL(string: "\(packageName)://")!
        
        let canOpen = UIApplication.shared.canOpenURL(appURL)
        call.resolve([
            "value": canOpen
        ])
    }
    
    @objc func openApp(_ call: CAPPluginCall) {
        guard let packageName = call.getString("packageName") else {
            call.reject("Package name is required")
            return
        }
        
        let action = call.getString("action") ?? "view"
        let data = call.getString("data")
        let mimeType = call.getString("mimeType") ?? "*/*"
        
        // For iOS, we'll try to open the app using URL schemes
        // This is a simplified approach since iOS doesn't have direct package launching like Android
        var appURL: URL?
        
        if let data = data, let url = URL(string: data) {
            appURL = url
        } else {
            // Try to construct a URL scheme from package name
            appURL = URL(string: "\(packageName)://")
        }
        
        guard let finalURL = appURL else {
            call.reject("Invalid app URL")
            return
        }
        
        DispatchQueue.main.async {
            if UIApplication.shared.canOpenURL(finalURL) {
                UIApplication.shared.open(finalURL) { success in
                    call.resolve([
                        "success": success
                    ])
                }
            } else {
                call.resolve([
                    "success": false
                ])
            }
        }
    }
}
