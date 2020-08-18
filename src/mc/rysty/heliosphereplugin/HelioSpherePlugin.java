package mc.rysty.heliosphereplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import mc.rysty.heliosphereplugin.chat.AFK;
import mc.rysty.heliosphereplugin.chat.AdminChat;
import mc.rysty.heliosphereplugin.chat.Broadcast;
import mc.rysty.heliosphereplugin.chat.ChatCommands;
import mc.rysty.heliosphereplugin.chat.CommandMute;
import mc.rysty.heliosphereplugin.chat.CommandSpy;
import mc.rysty.heliosphereplugin.chat.CommandSpyCommands;
import mc.rysty.heliosphereplugin.chat.CustomChatFormat;
import mc.rysty.heliosphereplugin.chat.JoinLeaveMessages;
import mc.rysty.heliosphereplugin.chat.SayCommand;
import mc.rysty.heliosphereplugin.chat.SignColors;
import mc.rysty.heliosphereplugin.chat.StaffChat;
import mc.rysty.heliosphereplugin.chat.UpdateDataYaml;
import mc.rysty.heliosphereplugin.commands.CommandFeed;
import mc.rysty.heliosphereplugin.commands.CommandHat;
import mc.rysty.heliosphereplugin.commands.CommandHead;
import mc.rysty.heliosphereplugin.commands.CommandRename;
import mc.rysty.heliosphereplugin.commands.CommandVersionlist;
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
import mc.rysty.heliosphereplugin.commands.Sudo;
import mc.rysty.heliosphereplugin.commands.ToggleBlockModify;
import mc.rysty.heliosphereplugin.commands.Vanish;
import mc.rysty.heliosphereplugin.commands.gamemode.GMA;
import mc.rysty.heliosphereplugin.commands.gamemode.GMC;
import mc.rysty.heliosphereplugin.commands.gamemode.GMS;
import mc.rysty.heliosphereplugin.commands.gamemode.GMSP;
import mc.rysty.heliosphereplugin.commands.inventory.ClearInventory;
import mc.rysty.heliosphereplugin.commands.inventory.EnderChestCommand;
import mc.rysty.heliosphereplugin.commands.inventory.InvseeCommand;
import mc.rysty.heliosphereplugin.utils.SettingsManager;

public class HelioSpherePlugin extends JavaPlugin {

	public static HelioSpherePlugin plugin;

	public static HelioSpherePlugin getInstance() {
		return plugin;
	}

	private PluginManager pluginManager = Bukkit.getPluginManager();

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
		new Sudo(this);
		new CommandFeed(this);
		new CommandVersionlist(this);
		new CommandRename(this);
		new CommandHat(this);
		new CommandHead(this);

		new JoinLeaveMessages(this);
		pluginManager.registerEvents(new UpdateDataYaml(), this);
		pluginManager.registerEvents(new CommandSpy(), this);
		pluginManager.registerEvents(new CustomChatFormat(), this);
		new CommandMute(this);

		new ChatCommands(this);
		new AFK(this);
		new StaffChat(this);
		new AdminChat(this);
		new ToggleBlockModify(this);
		new Vanish(this);
		pluginManager.registerEvents(new ChatCommands(this), this);
		pluginManager.registerEvents(new ToggleBlockModify(this), this);
		pluginManager.registerEvents(new Vanish(this), this);
		new SignColors(this);

		System.out.println("HS-Core enabled");
	}

	public void onDisable() {
		System.out.println("HS-Core disabled");
	}
}
