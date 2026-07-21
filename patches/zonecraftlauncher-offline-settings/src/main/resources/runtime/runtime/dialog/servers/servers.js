var servers = {
	pane: null, authResult: null,

	init: function() {
		servers.pane = loadFXML("dialog/servers/servers.fxml");
		initDialog(servers.pane);

		// Server buttons (first row)
		servers.pane.lookup("#classic").setOnAction(function(e) servers.goChooseVersion("classic"));
		servers.pane.lookup("#hitechLight").setOnAction(function(e) servers.goChooseVersion("hitechLight"));
		servers.pane.lookup("#hitechHard").setOnAction(function(e) servers.goUpdate("hitechHard"));
		servers.pane.lookup("#magic").setOnAction(function(e) servers.goChooseVersion("magic"));

		// Server buttons (second row)
		servers.pane.lookup("#pixelmon").setOnAction(function(e) servers.goChooseVersion("pixelmon"));
		servers.pane.lookup("#clanwar").setOnAction(function(e) servers.goUpdate("clanwar"));
		servers.pane.lookup("#rpg").setOnAction(function(e) servers.goUpdate("rpg"));
		servers.pane.lookup("#minigames").setOnAction(function(e) servers.goUpdate("minigames"));

		// Private server button
		servers.pane.lookup("#private").setOnAction(function(e) servers.goUpdate("private"));
	},

	lookupProfile: function(profileName) {
		if(auth.verifyResult === null || auth.verifyResult.profiles === null) {
			return null;
		}
		for each(var profile in auth.verifyResult.profiles) {
			var dir = profile.object.getTitle();
			if(!dir.equals(profileName)) {
				continue;
			}
			return profile;
		}
		return null;
	},

	goChooseVersion: function(profilePrefix) {
		serversVersion.profilePrefix = profilePrefix;
		setRootParent(serversVersion.pane);
	},

	goUpdate: function(profileName) {
		// Offline mode never uses LaunchServer profiles or AuthRequest results.
		if(settings.offlineMode) {
			var localProfileName = profileName;
			if(settings.shaders) {
				localProfileName += "_shaders";
			}
			setRootParent(update.pane);
			update.doOfflineLaunch(localProfileName);
			return;
		}

		var profile = servers.lookupProfile(profileName);
		var shadersProfile = servers.lookupProfile(profileName + "_shaders");
		if(settings.shaders && shadersProfile !== null) {
			profile = shadersProfile;
		}

		// Verify is not null
		if(profile === null) {
			LogHelper.error("PROFILE NOT FOUND!!! " + profileName);
			return;
		}

		// Switch to update
		setRootParent(update.pane);
		update.doUpdate(profile);
	}
};
