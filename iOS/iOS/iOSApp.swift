import UIKit
import common

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        window = UIWindow(frame: UIScreen.main.bounds)
        let mainViewController = MainKt.mainViewController(window: window!)
        window?.rootViewController = mainViewController
        window?.makeKeyAndVisible()
        return true
    }
}
