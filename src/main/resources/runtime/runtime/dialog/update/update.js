var update = {
	pane: null, title: null, description: null, progress: null,

	init: function() {
		update.pane = loadFXML("dialog/update/update.fxml");
		initDialog(update.pane);
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
		update.description.getStyleClass().add("error");
		update.description.setText(e.toString());
	},

	doOfflineLaunch: function(profileName) {
		update.reset("Запуск офлайн");
		var finalName = settings.shaders ? profileName + "_shaders" : profileName;
		var localLauncher = Java.type("launcher.client.LocalOfflineLauncher");
		var task = newTask(function() localLauncher.launch(updatesDir, finalName, settings.offlineNickname,
			settings.fullScreen, settings.ram));
		task.setOnSucceeded(function(event) javafx.application.Platform.exit());
		task.setOnFailed(function(event) update.setError(task.getException()));
		startTask(task);
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
		update.doLaunchClientDo(jvmDir, jvmHDir, clientHDir, profile,
			new ClientLauncherParams(auth.verifyResult.sign, assetDir, clientDir,
				servers.authResult.pp, servers.authResult.accessToken,
				settings.autoEnter, settings.fullScreen, settings.ram, 0, 0));
	},

	doLaunchClientDo: function(jvmDir, jvmHDir, clientHDir, profile, params) {
		var task = newTask(function() {
			ClientLauncher.launch(jvmDir, jvmHDir, clientHDir, profile, params, false);
			javafx.application.Platform.exit();
		});
		task.setOnFailed(function(event) update.setError(task.getException()));
		startTask(task);
	}
};
