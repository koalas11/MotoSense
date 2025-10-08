package org.lpss.motosense

import platform.UIKit.UIApplication

class IosContext: PlatformContext {
    override val context: UIApplication = UIApplication.sharedApplication
}
