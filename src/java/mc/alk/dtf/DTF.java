package mc.alk.dtf;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import mc.alk.arena.BattleArena;
import mc.alk.arena.controllers.APIRegistrationController;
import mc.alk.arena.objects.victoryconditions.VictoryType;
import mc.alk.arena.util.Log;

public class DTF extends JavaPlugin{
	static DTF plugin;

	@Override
	public void onEnable(){
		plugin = this;

		/// Save our default config.yml
		saveDefaultConfig();

		/// Read in our values from the config
		loadConfig();

		/// Register our competition
		VictoryType.register(FlagVictory.class, this);
		APIRegistrationController.registerCompetition(this, "DefendTheFlag", "dtf", 
		                                    BattleArena.createArenaFactory( DTFArena.class ), new DTFExecutor());

		Log.info("[" + getName()+ "] v" + getDescription().getVersion()+ " enabled!");
	}

	@Override
	public void onDisable(){
		Log.info("[" + getName() + "] v" + getDescription().getVersion() + " stopping!");
	}

	public static DTF getSelf() {
		return plugin;
	}

	@Override
	public void reloadConfig(){
		super.reloadConfig();
		loadConfig();
	}

	private void loadConfig() {
		FileConfiguration config = getConfig();
		DTFArena.capturesToWin = config.getInt("capturesToWin", 3);
	}
}
