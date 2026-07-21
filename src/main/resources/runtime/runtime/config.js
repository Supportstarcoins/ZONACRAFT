var config = {
	dir: "EvgexaCraft",
	title: "EvgexaCraft Launcher",
	icons: [ "favicon.png" ],
	registerURL: new java.net.URL("http://evgexacraft.com/index.php?do=register"),
	shopURL: new java.net.URL("http://evgexacraft.com/index.php?do=shop"),
	buyVIPURL: new java.net.URL("http://evgexacraft.com/vip.html"),
	forumURL: new java.net.URL("http://forum.evgexacraft.com/"),
	newsURL1: "http://evgexacraft.com/system/news.php?id=1",
	newsURL2: "http://evgexacraft.com/system/news.php?id=2",
	settingsMagic: 0xEFEF01,
	shadersDefault: false,
	autoEnterDefault: false,
	fullScreenDefault: false,
	ramDefault: 1024,
	jvmMustdie32Dir: "jre-8u66-win32", jvmMustdie64Dir: "jre-8u66-win64",
	jvmLinux32Dir: "jre-8u66-linux32", jvmLinux64Dir: "jre-8u66-linux64",
	jvmMacOSXDir: "jre-8u66-macosx", jvmUnknownDir: "jre-8u66-unknown"
};
var dir = IOHelper.HOME_DIR.resolve(config.dir);
if(!IOHelper.isDir(dir)) java.nio.file.Files.createDirectory(dir);
var updatesDir = dir.resolve("updates");
if(!IOHelper.isDir(updatesDir)) java.nio.file.Files.createDirectory(updatesDir);
