package me.efekos.cashchecks;

import me.efekos.cashchecks.commands.check;
import me.efekos.cashchecks.config.Config;
import me.efekos.cashchecks.events.OnPlayer;
import me.efekos.simpler.commands.CommandManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    private static Economy economy = null;

    private static Config configuration;

    public static Config getConfiguration() {
        return configuration;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        configuration = new Config("config.yml");


        if (!setupEconomy() ) {
            System.out.println("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new OnPlayer(),this);

        try {
            CommandManager.registerBaseCommand(this, check.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        configuration.setup();
    }

    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }


    public static Economy getEconomy() {
        return economy;
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
