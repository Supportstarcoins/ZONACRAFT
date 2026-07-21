var auth = {
	pane: null,
	loginField: null, passwordField: null,
	newsEngine1: null, newsEngine2: null,
	overlayDim: null, overlayDescription: null,
	sign: null, profiles: null, verifyResult: null,

	init: function() {
		auth.pane = loadFXML("dialog/auth/auth.fxml");
		initDialog(auth.pane);

		// News view data dir
		var webviewDir = dir.resolve("webview").toFile();

		// News view 1
		var newsView1 = auth.pane.lookup("#news1");
		newsView1.setBlendMode(javafx.scene.effect.BlendMode.DARKEN);
		auth.newsEngine1 = newsView1.getEngine();
		auth.newsEngine1.setUserDataDirectory(webviewDir);

		// News view 2
		var newsView2 = auth.pane.lookup("#news2");
		newsView2.setBlendMode(javafx.scene.effect.BlendMode.DARKEN);
		auth.newsEngine2 = newsView2.getEngine();
		auth.newsEngine2.setUserDataDirectory(webviewDir);
		auth.updateNewsMode();

		// Set action buttons
		auth.pane.lookup("#auth").setOnAction(auth.goAuth);
		auth.pane.lookup("#settings").setOnAction(auth.goSettings);

		// Set login field
		auth.loginField = auth.pane.lookup("#login");
		auth.loginField.setOnAction(auth.goAuth);
		if(settings.login !== null) {
			auth.loginField.setText(settings.login);
		}

		// Set password field
		auth.passwordField = auth.pane.lookup("#password");
		auth.passwordField.setOnAction(auth.goAuth);
		if(settings.rsaPassword !== null) {
			auth.passwordField.setPromptText("********");
		}

		// Init overlay
		auth.overlayDim = auth.pane.lookup("#overlayDim");
		auth.overlayDescription = auth.overlayDim.lookup("#description");

		// Verify launcher only in online mode
		auth.verifyLauncher();
	},

	updateNewsMode: function() {
		if(auth.newsEngine1 === null || auth.newsEngine2 === null) {
			return;
		}

		if(settings.offlineMode) {
			var html = "<html><body style='background:#171717;color:#ddd;font-family:sans-serif;text-align:center;padding-top:80px'>"
				+ "<h2>ZONACRAFT</h2><p>Офлайн-режим включён</p>"
				+ "<p>Сетевые новости и старая авторизация отключены.</p></body></html>";
			auth.newsEngine1.loadContent(html);
			auth.newsEngine2.loadContent(html);
			return;
		}

		auth.newsEngine1.load(config.newsURL1);
		auth.newsEngine2.load(config.newsURL2);
	},

	verifyLauncher: function() {
		if(settings.offlineMode) {
			LogHelper.info("Offline mode is enabled: LauncherRequest is skipped");
			auth.verifyResult = null;
			auth.overlayDim.setVisible(false);
			return;
		}

		auth.prepareOverlay("Получение информации с сервера");
		auth.makeLauncherRequest(function(result) auth.fade(auth.overlayDim, 0, 1.0, 0.0, function(event) {
			auth.overlayDim.setVisible(false);
			auth.verifyResult = result;

			// Go auth?
			if(cliParams.autoLogin) {
				auth.goAuth(null);
			}
		}));
	},

	goAuth: function(event) {
		// Verify there's no overlays
		if(auth.overlayDim.isVisible()) {
			return;
		}

		// Offline mode uses the nickname stored only on the settings page.
		if(settings.offlineMode) {
			var offlineLogin = settings.offlineNickname;
			if(!settings.isValidOfflineNickname(offlineLogin)) {
				auth.prepareOverlay("Некорректный офлайн-ник. Откройте настройки и укажите 3–16 символов: A-Z, 0-9 или _");
				auth.fade(auth.overlayDim, 2500, 1.0, 0.0, function(event) auth.overlayDim.setVisible(false));
				return;
			}

			settings.login = offlineLogin;
			settings.save();
			servers.authResult = null;
			setRootParent(servers.pane);
			return;
		}

		// Get login
		var login = auth.loginField.getText();
		if(login.isEmpty()) {
			return;
		}

		// Get password
		var rsaPassword;
		var password = auth.passwordField.getText();
		if(!password.isEmpty()) {
			rsaPassword = settings.setPassword(password);
		} else if(settings.rsaPassword !== null) {
			rsaPassword = settings.rsaPassword;
		} else {
			return;
		}

		// Store login and password
		settings.login = login;
		settings.rsaPassword = settings.savePasswordBox.isSelected() ? rsaPassword : null;

		// Show auth overlay
		auth.doAuth(login, rsaPassword);
	},

	doAuth: function(login, rsaPassword) {
		auth.prepareOverlay("Авторизация на сервере");
		auth.makeAuthRequest(login, rsaPassword, function(result) auth.fade(auth.overlayDim, 0, 1.0, 0.0, (function(event) {
			auth.overlayDim.setVisible(false);
			servers.authResult = result;
			setRootParent(servers.pane);
		})));
	},

	goSettings: function(event) {
		// The initial server error overlay is automatically hidden, so settings remain reachable.
		if(auth.overlayDim.isVisible()) {
			return;
		}

		// Show settings overlay
		setRootParent(settings.pane);
	},

	onOfflineSettingsChanged: function() {
		auth.updateNewsMode();
		if(settings.offlineMode) {
			LogHelper.info("Offline mode enabled from settings");
			auth.verifyResult = null;
			auth.overlayDim.setVisible(false);
			return;
		}

		// Switching back to online mode requests fresh server data.
		if(auth.verifyResult === null) {
			auth.verifyLauncher();
		}
	},

	/* Overlay */
	makeLauncherRequest: function(callback) {
		var task = newRequestTask(new LauncherRequest());
		auth.setTaskProperties(task, callback, false);
		startTask(task);
	},

	makeAuthRequest: function(username, rsaPassword, callback) {
		var task = newRequestTask(new AuthRequest(username, rsaPassword));
		auth.setTaskProperties(task, callback, true);
		startTask(task);
	},

	prepareOverlay: function(description) {
		auth.overlayDim.setVisible(true);
		auth.fade(auth.overlayDim, 0, 0.0, 1.0, null);
		auth.overlayDescription.getStyleClass().remove("error");
		auth.overlayDescription.setText(description);
	},

	setTaskProperties: function(task, callback, hide) {
		task.setOnFailed(function(event) {
			auth.overlayDescription.getStyleClass().add("error");
			var error = task.getException().toString();
			if(error.equals("Incorrect username or password")) {
				error = "Неправильное имя пользователя или пароль";
			} else if(error.equals("Password decryption error")) {
				error = "Пожалуйста, укажите пароль заново";
			}

			// Show the error briefly, then unlock the interface so settings can be opened.
			auth.overlayDescription.setText(error);
			auth.fade(auth.overlayDim, 2500, 1.0, 0.0, function(event) auth.overlayDim.setVisible(false));
		});
		if(callback !== null) {
			task.setOnSucceeded(function(event) callback(task.getValue()));
		}
	},

	fade: function(region, delay, from, to, onFinished) {
		var transition = new javafx.animation.FadeTransition(javafx.util.Duration.millis(100), region);
		if(onFinished !== null) {
			transition.setOnFinished(onFinished);
		}

		// Launch transition
		transition.setDelay(javafx.util.Duration.millis(delay));
		transition.setFromValue(from);
		transition.setToValue(to);
		transition.play();
	}
};
