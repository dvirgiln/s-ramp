// Download the appropriate driver from selenium page, extract it and return the path
def String downloadExplorerDriver() {
	def url = 'http://selenium-release.storage.googleapis.com/2.39/IEDriverServer_x64_2.39.0.zip'
	def file = new File('iedriver.zip')
	def fileOS = file.newOutputStream()
	fileOS << new URL(url).openStream()
	fileOS.close()

	File dest = new File(System.getProperty("WORKSPACE") + 'webdrivers/')
	File driver = new File(dest,"IEDriverServer.exe")

	if (driver.isFile() && driver.delete()) {
		System.out.println('*** IEDriver already exists - removed ***')
	}

	ant.unzip(
		src:file.absolutePath,
		dest:dest.absolutePath,
		overwrite:"true")

	if (driver.isFile()) {
		System.out.println('*** IEDriver downloaded successfully ***')
		return driver.absolutePath
	} else {
		return "";
	}
}

// Download the appropriate driver from selenium page, extract it and return the path
def String downloadChromeDriver() {
	def latestVersion = new URL('http://chromedriver.storage.googleapis.com/LATEST_RELEASE').getText()
	def url = 'http://chromedriver.storage.googleapis.com/'+latestVersion+'/chromedriver_win32.zip'
	def file = new File('chromedriver_win32.zip')
	def fileOS = file.newOutputStream()
	fileOS << new URL(url).openStream()
	fileOS.close()

	File dest = new File(System.getProperty("WORKSPACE") + 'webdrivers/')
	File driver = new File(dest,"chromedriver.exe")

	if (driver.isFile() && driver.delete()) {
		System.out.println('*** ChromeDriver already exists - removed ***')
	}

	ant.unzip(
		src:file.absolutePath,
		dest:dest.absolutePath,
		overwrite:"true")

	if (driver.isFile()) {
		System.out.println('*** ChromeDriver downloaded ***')
		return driver.absolutePath
	} else {
		return "";
	}
}

// Set browser-related properties
def void setProperties() {
	def browser = null
	def browserLabel = null

	// If this job is a matrixjob it does not have browser property
	if (System.getenv('test_browser') == null) {
		browserLabel = System.getenv('browserLabel')
		System.out.println("$browser")
		switch (browserLabel[0..3]) {
			case "brFF":
				browser = "firefox"
				break
			case "brCr":
				browser = "chrome"
				break
			case "brIE":
				browser = "internetExplorer"
				break
			default:
				browser = "safari"
				break
		}
	}
	else {
		browser = System.getenv('test_browser')
	}
	if (browser == "chrome") {
		System.setProperty('arq.extension.webdriver.chromeDriverBinary', downloadChromeDriver())
	}
	if (browser == "internetExplorer") {
		System.setProperty('arq.extension.webdriver.ieDriverBinary', downloadExplorerDriver())
	}
	System.setProperty('arq.extension.webdriver.browser', browser)
	System.out.println('*** Browser: ' + browser + ' ***')
}

// Set appropriate System properties for arquillian
setProperties();