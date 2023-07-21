package me.efekos.cashchecks.events;

import me.efekos.cashchecks.Main;
import me.efekos.cashchecks.config.Config;
import me.efekos.simpler.commands.translation.TranslateManager;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class OnPlayer implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_AIR&&e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player p = e.getPlayer();
        ItemStack stack = e.getItem();
        if(stack==null) return;

        ItemMeta meta = stack.getItemMeta();
        if(meta==null)return;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        Config con = Main.getConfiguration();


        if(container.has(new NamespacedKey(Main.getInstance(),"money"),PersistentDataType.DOUBLE)){
            double amountToGet = container.get(new NamespacedKey(Main.getInstance(),"money"),PersistentDataType.DOUBLE);

            EconomyResponse res = Main.getEconomy().depositPlayer(p,amountToGet);
            if(res.transactionSuccess()){
                p.sendMessage(TranslateManager.translateColors(con.getString("used","&aSuccessfully used the cash check! You got &b%amount%&a.").replace("%amount%",Main.getEconomy().format(amountToGet))));

                e.getItem().setAmount(e.getItem().getAmount()-1);
            }

        }
    }
}
