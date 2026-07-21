var LocalOfflineLauncher = Java.type("launcher.client.LocalOfflineLauncher");

var update = {
	pane: null, title: null, description: null, progress: null,

	/* State and overlay functions */
	init: function() {
		update.pane = loadFXML("dialog/update/update.fxml");
		initDialog(update.pane);

		// Set nodes
		update.title = update.pane.lookup("#title");
		update.description = update.pane.lookup("#description");
		update.progress = update.pane.lookup("#progress");
	},

	reset: function(title) {
		update.title.setText(title);
		update.description.getStyleClass().remove("error");
		update.description.setText("...");
		update.progress.setProgress(-1.0);
	},

	setError: function(e) {
		LogHelper.error(e);

		// Set error description
		update.description.getStyleClass().add("error");
		update.description.setText(e.toString());
	},

	stateCallback: function(task, state) {
		var bps = state.getBps();
		var estimated = state.getEstimatedTime();
		var estimatedSeconds = estimated === null ? 0 : estimated.getSeconds();
		var estimatedHH = (estimatedSeconds / 3600) | 0;
		var estimatedMM = ((estimatedSeconds % 3600) / 60) | 0;
		var estimatedSS = (estimatedSeconds % 60) | 0;
		task.updateMessage(java.lang.String.format(
			"Файл: %s%n" +
			"Загружено (Файл): %.2f / %.2f MiB @ %.1f Kbps%n" +
			"Загружено (Всего): %.2f / %.2f MiB%n",

			state.filePath,
			state.getFileDownloadedMiB(), state.getFileSizeMiB(), bps <= 0.0 ? 0.0 : bps / 1024.0,
			state.getTotalDownloadedMiB(), state.getTotalSizeMiB()
		));
		task.updateProgress(state.totalDownloaded, state.totalSize);
	},

	setTaskProperties: function(task, request, callback) {
		update.description.textProperty().bind(task.messageProperty());
		update.progress.progressProperty().bind(task.progressProperty());
		request.setStateCallback(function(state) update.stateCallback(task, state));
		task.setOnFailed(function(event) {
			update.description.textProperty().unbind();
			update.progress.progressProperty().unbind();
			update.setError(task.getException());
			setRootParent(servers.pane);
		});
		task.setOnSucceeded(function(event) {
			update.description.textProperty().unbind();
			update.progress.progressProperty().unbind();
			callback(task.getValue());
		});
	},

	// Launch an already installed client without contacting LaunchServer.
	doOfflineLaunch: function(profileName) {
		update.reset("Офлайн-запуск");
		update.description.setText("Поиск локального клиента: " + profileName);
		update.progress.setProgress(-1.0);

		var task = newTask(function() {
			return LocalOfflineLauncher.launch(updatesDir, profileName, settings.offlineNickname,
				settings.fullScreen, settings.ram);
		});
		task.setOnFailed(function(event) {
			update.setError(task.getException());
		});
		task.setOnSucceeded(function(event) {
			LogHelper.info("Offline client process started");
			javafx.application.Platform.exit();
		});
		startTask(task);
	},

	doUpdate: function(profile) {
		update.reset("Обновление файлов JVM");
		var jvmDir = updatesDir.resolve(jvmDirName);
		update.makeRequest(jvmDirName, jvmDir, null, function(jvmHDir) {
			update.reset("Обновление файлов ресурсов");
			var assetDirName = profile.object.block.getEntryValue("assetDir", StringConfigEntryClass);
			var assetDir = updatesDir.resolve(assetDirName);
			update.makeRequest(assetDirName, assetDir, null, function(assetHDir) {
				update.reset("Обновление файлов клиента");
				var clientDirName = profile.object.block.getEntryValue("dir", StringConfigEntryClass);
				var clientDir = updatesDir.resolve(clientDirName);
				update.makeRequest(clientDirName, clientDir, profile.object.getUpdateMatcher(), function(clientHDir) {
					update.doLaunchClient(jvmDir, jvmHDir, clientHDir, assetDir, clientDir, profile);
				});
			});
		});
	},

	makeRequest: function(dirName, dir, matcher, callback) {
		var request = new UpdateRequest(dirName, updatesDir.resolve(dirName), matcher);
		var task = newRequestTask(request);
		update.setTaskProperties(task, request, callback);
		task.updateMessage("Состояние: Хеширование");
		task.updateProgress(-1, -1);
		startTask(task);
	},

	doLaunchClient: function(jvmDir, jvmHDir, clientHDir, assetDir, clientDir, profile) {
		update.doLaunchClientDo(jvmDir, jvmHDir, clientHDir, profile, new ClientLauncherParams(auth.verifyResult.sign, assetDir, clientDir,
			servers.authResult.pp, servers.authResult.accessToken, settings.autoEnter, settings.fullScreen, settings.ram, 0, 0));
	},

	doLaunchClientDo: function(jvmDir, jvmHDir, clientHDir, profile, params) {
		var task = newTask(function() {
			var process = ClientLauncher.launch(jvmDir, jvmHDir, clientHDir, profile, params, false);

			// Looks like we successfully started
			javafx.application.Platform.exit();
		});
		task.setOnFailed(function(event) update.setError(task.getException()));
		startTask(task);
	}
};
