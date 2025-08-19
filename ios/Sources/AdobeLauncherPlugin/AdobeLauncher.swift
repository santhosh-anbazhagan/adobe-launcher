import Foundation

@objc public class AdobeLauncher: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
