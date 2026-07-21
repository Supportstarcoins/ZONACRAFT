var settings = {
	file: dir.resolve("settings.bin"), // Settings file
	login: null, rsaPassword: null, // Auth
	shaders: false, autoEnter: false, fullScreen: false, ram: 0, // Client
	offlineMode: false, offlineNickname: "Player", // Offline

	// Load and save functions
	load: function() {
		LogHelper.debug("Loading settings file");
		try {
			tryWithResources(new HInput(IOHelper.newInput(settings.file)), settings.read);
		} catch(e) {
			LogHelper.error(e);
			settings.setDefault();
		}
	},

	save: function() {
		LogHelper.debug("Saving settings file");
		try {
			tryWithResources(new HOutput(IOHelper.newOutput(settings.file)), settings.write);
		} catch(e) {
			LogHelper.error(e);
		}
	},

	read: function(input) {
		var magic = input.readInt();
		if(magic != config.settingsMagic) {
			throw new java.io.IOException("Settings magic mismatch: " + java.lang.Integer.toString(magic, 16));
		}

		settings.login = input.readBoolean() ? input.readString(255) : null;
		settings.rsaPassword = input.readBoolean() ? input.readByteArray(IOHelper.BUFFER_SIZE) : null;
		settings.shaders = input.readBoolean();
		settings.autoEnter = input.readBoolean();
		settings.fullScreen = input.readBoolean();
		settings.setRAM(input.readLength(0));
		settings.offlineMode = input.readBoolean();
		settings.offlineNickname = input.readString(16);
		cliParams.applySettings();
	},

	write: function(output) {
		output.writeInt(config.settingsMagic);
		output.writeBoolean(settings.login !== null);
		if(settings.login !== null) output.writeString(settings.login, 255);
		output.writeBoolean(settings.rsaPassword !== null);
		if(settings.rsaPassword !== null) output.writeByteArray(settings.rsaPassword, IOHelper.BUFFER_SIZE);
		output.writeBoolean(settings.shaders);
		output.writeBoolean(settings.autoEnter);
		output.writeBoolean(settings.fullScreen);
		output.writeLength(settings.ram, JVMHelper.RAM);
		output.writeBoolean(settings.offlineMode);
		output.writeString(settings.offlineNickname, 16);
	},

	setDefault: function() {
		settings.login = null;
		settings.rsaPassword = null;
		settings.shaders = config.shadersDefault;
		settings.autoEnter = config.autoEnterDefault;
		settings.fullScreen = config.fullScreenDefault;
		settings.setRAM(config.ramDefault);
		settings.offlineMode = false;
		settings.offlineNickname = "Player";
		cliParams.applySettings();
	},

	setPassword: function(password) {
		var encrypted = SecurityHelper.newRSAEncryptCipher(Launcher.getConfig().publicKey).doFinal(IOHelper.encode(password));
		settings.password = encrypted;
		return encrypted;
	},

	setRAM: function(ram) {
		settings.ram = java.lang.Math["min(int,int)"](((ram / 256) | 0) * 256, JVMHelper.RAM);
	},

	isValidOfflineNickname: function(value) {
		return value !== null && java.util.regex.Pattern.matches("[A-Za-z0-9_]{3,16}", value);
	},

	pane: null, savePasswordBox: null, ramLabel: null,

	init: function() {
		settings.pane = loadFXML("dialog/settings/settings.fxml");
		initDialog(settings.pane);

		settings.savePasswordBox = settings.pane.lookup("#savePassword");
		settings.savePasswordBox.setSelected(settings.login === null || settings.rsaPassword !== null);

		var shadersBox = settings.pane.lookup("#shaders");
		shadersBox.setSelected(settings.shaders);
		shadersBox.selectedProperty()["addListener(javafx.beans.value.ChangeListener)"](function(o, ov, nv) settings.shaders = nv);

		var autoEnterBox = settings.pane.lookup("#autoEnter");
		autoEnterBox.setSelected(settings.autoEnter);
		autoEnterBox.selectedProperty()["addListener(javafx.beans.value.ChangeListener)"](function(o, ov, nv) settings.autoEnter = nv);

		var fullScreenBox = settings.pane.lookup("#fullScreen");
		fullScreenBox.setSelected(settings.fullScreen);
		fullScreenBox.selectedProperty()["addListener(javafx.beans.value.ChangeListener)"](function(o, ov, nv) settings.fullScreen = nv);

		var offlineBox = settings.pane.lookup("#offlineMode");
		var offlineNick = settings.pane.lookup("#offlineNickname");
		offlineBox.setSelected(settings.offlineMode);
		offlineNick.setText(settings.offlineNickname);
		offlineNick.setDisable(!settings.offlineMode);
		settings.savePasswordBox.setDisable(settings.offlineMode);
		offlineBox.selectedProperty()["addListener(javafx.beans.value.ChangeListener)"](function(o, ov, nv) {
			settings.offlineMode = nv;
			offlineNick.setDisable(!nv);
			settings.savePasswordBox.setDisable(nv);
		});
		offlineNick.textProperty()["addListener(javafx.beans.value.ChangeListener)"](function(o, ov, nv) settings.offlineNickname = nv);

		settings.ramLabel = settings.pane.lookup("#ramTitle");
		settings.updateRAMLabel();
		var ramSlider = settings.pane.lookup("#ramSlider");
		ramSlider.setMin(0);
		ramSlider.setMax(JVMHelper.RAM);
		ramSlider.setSnapToTicks(true);
		ramSlider.setShowTickMarks(true);
		ramSlider.setShowTickLabels(true);
		ramSlider.setMinorTickCount(3);
		ramSlider.setMajorTickUnit(1024);
		ramSlider.setBlockIncrement(1024);
		ramSlider.setValue(settings.ram);
		ramSlider.valueProperty()["addListener(javafx.beans.value.ChangeListener)"](function(o, ov, nv) {
			settings.setRAM(nv);
			settings.updateRAMLabel();
		});

		settings.pane.lookup("#apply").setOnAction(function(event) {
			if(settings.offlineMode && !settings.isValidOfflineNickname(settings.offlineNickname)) {
				return;
			}
			settings.save();
			auth.onOfflineSettingsChanged();
			setRootParent(auth.pane);
		});
	},

	updateRAMLabel: function() {
		settings.ramLabel.setText("Выделение ОЗУ: " + (settings.ram <= 0 ? "Автоматически" : settings.ram + " MiB"));
	}
};

var cliParams = {
	login: null, password: null, autoLogin: false,
	shaders: null, autoEnter: null, fullScreen: null, ram: -1,

	init: function(params) {
		var named = params.getNamed();
		var unnamed = params.getUnnamed();
		cliParams.login = named.get("login");
		cliParams.password = named.get("password");
		cliParams.autoLogin = unnamed.contains("--autoLogin");
		if(named.containsKey("shaders")) cliParams.shaders = java.lang.Boolean.parseBoolean(named.get("shaders"));
		if(named.containsKey("autoEnter")) cliParams.autoEnter = java.lang.Boolean.parseBoolean(named.get("autoEnter"));
		if(named.containsKey("fullScreen")) cliParams.fullScreen = java.lang.Boolean.parseBoolean(named.get("fullScreen"));
		if(named.containsKey("ram")) cliParams.ram = java.lang.Integer.parseUnsignedInt(named.get("ram"));
	},

	applySettings: function() {
		if(cliParams.login !== null) settings.login = cliParams.login;
		if(cliParams.password !== null) settings.setPassword(cliParams.password);
		if(cliParams.shaders !== null) settings.shaders = cliParams.shaders;
		if(cliParams.autoEnter !== null) settings.autoEnter = cliParams.autoEnter;
		if(cliParams.fullScreen !== null) settings.fullScreen = cliParams.fullScreen;
		if(cliParams.ram >= 0) settings.setRAM(cliParams.ram);
	}
};
