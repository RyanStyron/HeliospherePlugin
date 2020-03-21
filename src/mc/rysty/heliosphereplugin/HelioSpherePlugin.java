package mc.rysty.heliosphereplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import mc.rysty.heliosphereplugin.chat.AFK;
import mc.rysty.heliosphereplugin.chat.Broadcast;
import mc.rysty.heliosphereplugin.chat.ChatCommands;
import mc.rysty.heliosphereplugin.chat.CommandSpy;
import mc.rysty.heliosphereplugin.chat.CommandSpyCommands;
import mc.rysty.heliosphereplugin.chat.CustomChatFormat;
import mc.rysty.heliosphereplugin.chat.JoinLeaveMessages;
import mc.rysty.heliosphereplugin.chat.SayCommand;
import mc.rysty.heliosphereplugin.chat.StaffChat;
import mc.rysty.heliosphereplugin.chat.UpdateDataYaml;
import mc.rysty.heliosphereplugin.commands.Back;
import mc.rysty.heliosphereplugin.commands.Console;
import mc.rysty.heliosphereplugin.commands.Fly;
import mc.rysty.heliosphereplugin.commands.GiveAliasCommand;
import mc.rysty.heliosphereplugin.commands.Heal;
import mc.rysty.heliosphereplugin.commands.Invincible;
import mc.rysty.heliosphereplugin.commands.Ping;
import mc.rysty.heliosphereplugin.commands.PlayerInfo;
import mc.rysty.heliosphereplugin.commands.Reload;
import mc.rysty.heliosphereplugin.commands.See;
import mc.rysty.heliosphereplugin.commands.ServerInfo;
import mc.rysty.heliosphereplugin.commands.ToggleBlockModify;
import mc.rysty.heliosphereplugin.commands.Vanish;
import mc.rysty.heliosphereplugin.commands.gamemode.GMA;
import mc.rysty.heliosphereplugin.commands.gamemode.GMC;
import mc.rysty.heliosphereplugin.commands.gamemode.GMS;
import mc.rysty.heliosphereplugin.commands.gamemode.GMSP;
import mc.rysty.heliosphereplugin.inventory.ClearInventory;
import mc.rysty.heliosphereplugin.inventory.EnderChestCommand;
import mc.rysty.heliosphereplugin.inventory.InvseeCommand;
import mc.rysty.heliosphereplugin.utils.SettingsManager;

public class HelioSpherePlugin extends JavaPlugin {

	public static HelioSpherePlugin plugin;

	public static HelioSpherePlugin getInstance() {
		return plugin;
	}

	PluginManager pm = Bukkit.getPluginManager();

	public void onEnable() {
		plugin = this;
		saveDefaultConfig();
		SettingsManager.getInstance().setup(this);

		new GMA(this);
		new GMC(this);
		new GMS(this);
		new GMSP(this);
		new CommandSpyCommands(this);
		new Broadcast(this);
		new Fly(this);
		new ClearInventory(this);
		new InvseeCommand(this);
		new EnderChestCommand(this);
		new PlayerInfo(this);
		new Invincible(this);
		new ServerInfo(this);
		new Reload(this);
		new Heal(this);
		new See(this);
		new SayCommand(this);
		new Console(this);
		new Ping(this);
		new GiveAliasCommand(this);

		pm.registerEvents(new UpdateDataYaml(), this);
		pm.registerEvents(new JoinLeaveMessages(), this);
		pm.registerEvents(new CommandSpy(), this);
		pm.registerEvents(new CustomChatFormat(), this);

		new ChatCommands(this);
		new AFK(this);
		new StaffChat(this);
		new ToggleBlockModify(this);
		new Vanish(this);
		new Back(this);
		pm.registerEvents(new ChatCommands(this), this);
		pm.registerEvents(new AFK(this), this);
		pm.registerEvents(new StaffChat(this), this);
		pm.registerEvents(new ToggleBlockModify(this), this);
		pm.registerEvents(new Vanish(this), this);
		pm.registerEvents(new Back(this), this);

		System.out.println("HS-Core enabled");
	}

	public void onDisable() {
		System.out.println("HS-Core disabled");
	}

}
