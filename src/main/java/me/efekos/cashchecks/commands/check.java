package me.efekos.cashchecks.commands;

import me.efekos.cashchecks.Main;
import me.efekos.cashchecks.config.Config;
import me.efekos.simpler.annotations.Command;
import me.efekos.simpler.commands.BaseCommand;
import me.efekos.simpler.commands.syntax.NumberArgument;
import me.efekos.simpler.commands.syntax.Syntax;
import me.efekos.simpler.commands.translation.TranslateManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@Command(name = "check",permission = "cashchecks.check",description = "Generate a check",playerOnly = true)
public class check extends BaseCommand {
    public check(@NotNull String name) {
        super(name);
    }

    public check(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    @Override
    public @NotNull Syntax getSyntax() {
        return new Syntax().withArgument(new NumberArgument("amount",0,2147483647));
    }

    @Override
    public void onPlayerUse(Player player, String[] args) {
        ItemStack stack = new ItemStack(Material.PAPER,1);
        ItemMeta meta = stack.getItemMeta();

        double amount = Double.parseDouble(args[0]);

        Config msgConfig = Main.getConfiguration();

        if(amount<0.0){
            player.sendMessage(TranslateManager.translateColors(msgConfig.getString("positive-value","&cYou need to use a positive amount.")));
            return;
        }

        Economy econ = Main.getEconomy();

        if(!econ.has(player,amount)){
            player.sendMessage(TranslateManager.translateColors(msgConfig.getString("not-enough", "&cYou need at least &a%amount% &cfor this.").replace("%amount%", econ.format(amount))));
            return;
        }

        meta.setDisplayName(TranslateManager.translateColors(msgConfig.getString("cash-check.name","&eCash Check")));
        meta.setLore(Arrays.asList(TranslateManager.translateColors(msgConfig.getString("cash-check.desc","&eMoney: &a%amount%").replace("%amount%",econ.format(amount)))));

        PersistentDataContainer container = meta.getPersistentDataContainer();

        container.set(new NamespacedKey(Main.getInstance(),"money"), PersistentDataType.DOUBLE,amount);

        stack.setItemMeta(meta);

        econ.withdrawPlayer(player,amount);

        player.getInventory().addItem(stack);
        player.sendMessage(TranslateManager.translateColors(msgConfig.getString("success","&aSuccessfully added a cash check of &b$%amount% &ato your inventory!").replace("%amount%",econ.format(amount))));
    }

    @Override
    public void onConsoleUse(ConsoleCommandSender sender, String[] args) {

    }
}