var settings = {
	file: dir.resolve("settings.bin"), // Settings file
	login: null, rsaPassword: null, // Auth
	shaders: false, autoEnter: false, fullScreen: false, ram: 0, // Client
	offlineMode: false, offlineNickname: "Player", // Local offline launch

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

	// Internal functions
	read: function(input) {
		var magic = input.readInt();
		if(magic != config.settingsMagic) {
			throw new java.io.IOException("Settings magic mismatch: " + java.lang.Integer.toString(magic, 16));
		}

		// Auth settings
		settings.login = input.readBoolean() ? input.readString(255) : null;
		settings.rsaPassword = input.readBoolean() ? input.readByteArray(IOHelper.BUFFER_SIZE) : null;

		// Client settings
		settings.shaders = input.readBoolean();
		settings.autoEnter = input.readBoolean();
		settings.fullScreen = input.readBoolean();
		settings.setRAM(input.readLength(0));

		// Offline settings
		settings.offlineMode = input.readBoolean();
		settings.offlineNickname = input.readString(16);
		if(!settings.isValidOfflineNickname(settings.offlineNickname)) {
			settings.offlineNickname = config.offlineNicknameDefault;
		}

		// Apply CLI params
		cliParams.applySettings();
	},

	write: function(output) {
		output.writeInt(config.settingsMagic);

		// Auth settings
		output.writeBoolean(settings.login !== null);
		if(settings.login !== null) {
			output.writeString(settings.login, 255);
		}
		output.writeBoolean(settings.rsaPassword !== null);
		if(settings.rsaPassword !== null) {
			output.writeByteArray(settings.rsaPassword, IOHelper.BUFFER_SIZE);
		}

		// Client settings
		output.writeBoolean(settings.shaders);
		output.writeBoolean(settings.autoEnter);
		output.writeBoolean(settings.fullScreen);
		output.writeLength(settings.ram, JVMHelper.RAM);

		// Offline settings
		output.writeBoolean(settings.offlineMode);
		output.writeString(settings.offlineNickname, 16);
	},

	setDefault: function() {
		settings.login = null;
		settings.rsaPassword = null;

		// Client settings
		settings.shaders = config.shadersDefault;
		settings.autoEnter = config.autoEnterDefault;
		settings.fullScreen = config.fullScreenDefault;
		settings.setRAM(config.ramDefault);

		// Offline settings
		settings.offlineMode = config.offlineDefault;
		settings.offlineNickname = config.offlineNicknameDefault;

		// Apply CLI params
		cliParams.applySettings();
	},

	setPassword: function(password) {
		var encrypted = SecurityHelper.newRSAEncryptCipher(Launcher.getConfig().publicKey).doFinal(IOHelper.encode(password));
		settings.rsaPassword = encrypted;
		return encrypted;
	},

	setRAM: function(ram) {
		settings.ram = java.lang.Math["min(int,int)"](((ram / 256) | 0) * 256, JVMHelper.RAM);
	},

	isValidOfflineNickname: function(nickname) {
		return nickname !== null && nickname.matches("[A-Za-z0-9_]{3,16}");
	},

	/* ===================== PANE ===================== */
	pane: null, savePasswordBox: null, ramLabel: null,
	offlineModeBox: null, offlineNicknameField: null,

	init: function() {
		settings.pane = loadFXML("dialog/settings/settings.fxml");
		initDialog(settings.pane);

		// Set savePassword checkbox
		settings.savePasswordBox = settings.pane.lookup("#savePassword");
		settings.savePasswordBox.setSelected(settings.login === null || settings.rsaPassword !== null);

		// Set shaders checkbox
		var shadersBox = settings.pane.lookup("#shaders");
		shadersBox.setSelected(settings.shaders);
		shadersBox.selectedProperty()["addListener(javafx.beans.value.ChangeListener)"](function(o, ov, nv) settings.shaders = nv);

		// Set autoEnter checkbox
		var autoEnterBox = settings.pane.lookup("#autoEnter");
		autoEnterBox.setSelected(settings.autoEnter);
		autoEnterBox.selectedProperty()["addListener(javafx.beans.value.ChangeListener)"](function(o, ov, nv) settings.autoEnter = nv);

		// Set fullScreen checkbox
		var fullScreenBox = settings.pane.lookup("#fullScreen");
		fullScreenBox.setSelected(settings.fullScreen);
		fullScreenBox.selectedProperty()["addListener(javafx.beans.value.ChangeListener)"](function(o, ov, nv) settings.fullScreen = nv);

		// Set offline controls. They exist only on the settings page.
		settings.offlineModeBox = settings.pane.lookup("#offlineMode");
		settings.offlineNicknameField = settings.pane.lookup("#offlineNickname");
		settings.offlineModeBox.setSelected(settings.offlineMode);
		settings.offlineNicknameField.setText(settings.offlineNickname);
		settings.updateOfflineControls();
		settings.offlineModeBox.selectedProperty()["addListener(javafx.beans.value.ChangeListener)"](function(o, ov, nv) {
			settings.offlineMode = nv;
			settings.updateOfflineControls();
		});

		// Set RAM label
		settings.ramLabel = settings.pane.lookup("#ramTitle");
		settings.updateRAMLabel();

		// Set RAM slider options
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

		// Apply settings
		settings.pane.lookup("#apply").setOnAction(function(event) {
			var nickname = settings.offlineNicknameField.getText().trim();
			if(settings.offlineMode && !settings.isValidOfflineNickname(nickname)) {
				settings.offlineNicknameField.setText("");
				settings.offlineNicknameField.setPromptText("Только A-Z, 0-9 и _, длина 3–16");
				settings.offlineNicknameField.requestFocus();
				return;
			}

			settings.offlineNickname = settings.isValidOfflineNickname(nickname) ? nickname : config.offlineNicknameDefault;
			settings.save();
			if(typeof auth !== "undefined" && auth.onOfflineSettingsChanged !== undefined) {
				auth.onOfflineSettingsChanged();
			}
			setRootParent(auth.pane);
		});
	},

	updateOfflineControls: function() {
		if(settings.offlineNicknameField === null) {
			return;
		}
		settings.offlineNicknameField.setDisable(!settings.offlineMode);
		settings.savePasswordBox.setDisable(settings.offlineMode);
	},

	updateRAMLabel: function() {
		settings.ramLabel.setText("Выделение ОЗУ: " + (settings.ram <= 0 ? "Автоматически" : settings.ram + " MiB"));
	}
};

/* ====================== CLI PARAMS ===================== */
var cliParams = {
	login: null, password: null, autoLogin: false, // Auth
	shaders: null, autoEnter: null, fullScreen: null, ram: -1, // Client
	offlineMode: null, offlineNickname: null, // Offline

	init: function(params) {
		var named = params.getNamed();
		var unnamed = params.getUnnamed();

		// Set auth cli params
		cliParams.login = named.get("login");
		cliParams.password = named.get("password");
		cliParams.autoLogin = unnamed.contains("--autoLogin");

		// Set client cli params
		if(named.containsKey("shaders")) {
			cliParams.shaders = java.lang.Boolean.parseBoolean(named.get("shaders"));
		}
		if(named.containsKey("autoEnter")) {
			cliParams.autoEnter = java.lang.Boolean.parseBoolean(named.get("autoEnter"));
		}
		if(named.containsKey("fullScreen")) {
			cliParams.fullScreen = java.lang.Boolean.parseBoolean(named.get("fullScreen"));
		}
		if(named.containsKey("ram")) {
			cliParams.ram = java.lang.Integer.parseUnsignedInt(named.get("ram"));
		}

		// Offline CLI params
		if(named.containsKey("offline")) {
			cliParams.offlineMode = java.lang.Boolean.parseBoolean(named.get("offline"));
		}
		if(named.containsKey("offlineNickname")) {
			cliParams.offlineNickname = named.get("offlineNickname");
		}
	},

	applySettings: function() {
		// Update auth settings
		if(cliParams.login !== null) {
			settings.login = cliParams.login;
		}
		if(cliParams.password !== null) {
			settings.setPassword(cliParams.password);
		}

		// Update client settings
		if(cliParams.shaders !== null) {
			settings.shaders = cliParams.shaders;
		}
		if(cliParams.autoEnter !== null) {
			settings.autoEnter = cliParams.autoEnter;
		}
		if(cliParams.fullScreen !== null) {
			settings.fullScreen = cliParams.fullScreen;
		}
		if(cliParams.ram >= 0) {
			settings.setRAM(cliParams.ram);
		}

		// Update offline settings
		if(cliParams.offlineMode !== null) {
			settings.offlineMode = cliParams.offlineMode;
		}
		if(settings.isValidOfflineNickname(cliParams.offlineNickname)) {
			settings.offlineNickname = cliParams.offlineNickname;
		}
	}
};
