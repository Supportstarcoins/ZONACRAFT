var auth = {
	pane: null, loginField: null, passwordField: null,
	overlayDim: null, overlayDescription: null,
	verifyResult: null,
	init: function() {
		auth.pane = loadFXML("dialog/auth/auth.fxml"); initDialog(auth.pane);
		var webviewDir = dir.resolve("webview").toFile();
		var newsView1 = auth.pane.lookup("#news1"); var engine1 = newsView1.getEngine(); engine1.setUserDataDirectory(webviewDir);
		var newsView2 = auth.pane.lookup("#news2"); var engine2 = newsView2.getEngine(); engine2.setUserDataDirectory(webviewDir);
		if(!settings.offlineMode) { engine1.load(config.newsURL1); engine2.load(config.newsURL2); }
		auth.pane.lookup("#auth").setOnAction(auth.goAuth); auth.pane.lookup("#settings").setOnAction(auth.goSettings);
		auth.loginField = auth.pane.lookup("#login"); auth.loginField.setOnAction(auth.goAuth); if(settings.login !== null) auth.loginField.setText(settings.login);
		auth.passwordField = auth.pane.lookup("#password"); auth.passwordField.setOnAction(auth.goAuth); if(settings.rsaPassword !== null) auth.passwordField.setPromptText("********");
		auth.overlayDim = auth.pane.lookup("#overlayDim"); auth.overlayDescription = auth.overlayDim.lookup("#description");
		auth.verifyLauncher();
	},
	verifyLauncher: function() {
		if(settings.offlineMode) { auth.verifyResult = null; auth.overlayDim.setVisible(false); return; }
		auth.prepareOverlay("Получение информации с сервера");
		auth.makeLauncherRequest(function(result) auth.fade(auth.overlayDim, 0, 1.0, 0.0, function(event) { auth.overlayDim.setVisible(false); auth.verifyResult = result; if(cliParams.autoLogin) auth.goAuth(null); }));
	},
	goAuth: function(event) {
		if(auth.overlayDim.isVisible()) return;
		if(settings.offlineMode) {
			if(!settings.isValidOfflineNickname(settings.offlineNickname)) { auth.prepareOverlay("Некорректный офлайн-ник: 3–16 символов A-Z, 0-9 или _"); auth.fade(auth.overlayDim, 2500, 1.0, 0.0, function(event) auth.overlayDim.setVisible(false)); return; }
			settings.login = settings.offlineNickname; settings.save(); servers.authResult = null; setRootParent(servers.pane); return;
		}
		var login = auth.loginField.getText(); if(login.isEmpty()) return;
		var rsaPassword; var password = auth.passwordField.getText();
		if(!password.isEmpty()) rsaPassword = settings.setPassword(password); else if(settings.rsaPassword !== null) rsaPassword = settings.rsaPassword; else return;
		settings.login = login; settings.rsaPassword = settings.savePasswordBox.isSelected() ? rsaPassword : null; auth.doAuth(login, rsaPassword);
	},
	doAuth: function(login, rsaPassword) { auth.prepareOverlay("Авторизация на сервере"); auth.makeAuthRequest(login, rsaPassword, function(result) auth.fade(auth.overlayDim, 0, 1.0, 0.0, function(event) { auth.overlayDim.setVisible(false); servers.authResult = result; setRootParent(servers.pane); })); },
	goSettings: function(event) { if(auth.overlayDim.isVisible()) return; setRootParent(settings.pane); },
	onOfflineSettingsChanged: function() { if(settings.offlineMode) { auth.verifyResult = null; auth.overlayDim.setVisible(false); } else if(auth.verifyResult === null) auth.verifyLauncher(); },
	makeLauncherRequest: function(callback) { var task = newRequestTask(new LauncherRequest()); auth.setTaskProperties(task, callback); startTask(task); },
	makeAuthRequest: function(username, rsaPassword, callback) { var task = newRequestTask(new AuthRequest(username, rsaPassword)); auth.setTaskProperties(task, callback); startTask(task); },
	prepareOverlay: function(description) { auth.overlayDim.setVisible(true); auth.fade(auth.overlayDim, 0, 0.0, 1.0, null); auth.overlayDescription.getStyleClass().remove("error"); auth.overlayDescription.setText(description); },
	setTaskProperties: function(task, callback) { task.setOnFailed(function(event) { auth.overlayDescription.getStyleClass().add("error"); auth.overlayDescription.setText(task.getException().toString()); auth.fade(auth.overlayDim, 2500, 1.0, 0.0, function(event) auth.overlayDim.setVisible(false)); }); if(callback !== null) task.setOnSucceeded(function(event) callback(task.getValue())); },
	fade: function(region, delay, from, to, onFinished) { var transition = new javafx.animation.FadeTransition(javafx.util.Duration.millis(100), region); if(onFinished !== null) transition.setOnFinished(onFinished); transition.setDelay(javafx.util.Duration.millis(delay)); transition.setFromValue(from); transition.setToValue(to); transition.play(); }
};
