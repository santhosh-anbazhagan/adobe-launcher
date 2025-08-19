// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "AdobeLauncherPlugin",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "AdobeLauncherPlugin",
            targets: ["AdobeLauncherPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "AdobeLauncherPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/AdobeLauncherPlugin"),
        .testTarget(
            name: "AdobeLauncherPluginTests",
            dependencies: ["AdobeLauncherPlugin"],
            path: "ios/Tests/AdobeLauncherPluginTests")
    ]
)