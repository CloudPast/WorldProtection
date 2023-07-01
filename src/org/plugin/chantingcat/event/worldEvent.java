package org.plugin.chantingcat.event;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.plugin.chantingcat.color.Message;
import org.plugin.chantingcat.pluginmain.Main;

import static org.bukkit.entity.EntityType.PLAYER;
import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class worldEvent implements Listener {
    private final Plugin plugin = getPlugin(Main.class);
    private long lastMessageTime = 0;
    private Message message = new Message();

    //画的破坏
    @EventHandler
    public void onHangingBreakEvent (HangingBreakByEntityEvent event) {
        if (!event.getEventName().equals("HangingBreakByEntityEvent")) {
            return;
        }
        if (this.plugin.getConfig().getBoolean("worldList." + event.getEntity().getWorld().getName() + ".type")
                && this.plugin.getConfig().getBoolean("worldList." + event.getEntity().getWorld().getName() + ".draw")) {
            if (event.getRemover().isOp() || event.getRemover()
                .hasPermission(this.message.Message(this.plugin.getConfig().getString("worldList." + event.getEntity().getWorld().getName() + ".permissions")))) {
            }else{
                event.setCancelled(true);
            }
        }
    }
    //画的放置
    @EventHandler
    public void onHangingPlaceEvent (HangingPlaceEvent event) {
        if (this.plugin.getConfig().getBoolean("worldList." + event.getEntity().getWorld().getName() + ".type")
                && this.plugin.getConfig().getBoolean("worldList." + event.getEntity().getWorld().getName() + ".draw")) {
            if (event.getPlayer().isOp() || event.getPlayer()
                    .hasPermission(this.message.Message(this.plugin.getConfig().getString("worldList." + event.getPlayer().getWorld().getName() + ".permissions")))) {
            }else {
                event.setCancelled(true);
            }
        }
    }
    //禁止破坏
    @EventHandler
    public void worldBreak(BlockBreakEvent event){
        //判断是否是配置中的世界
        if (this.plugin.getConfig().getBoolean("worldList." + event.getPlayer().getWorld().getName() + ".type")) {

            if (event.getPlayer()
                    .hasPermission(this.plugin.getConfig().getString("worldList." + event.getPlayer().getWorld().getName() + ".permissions"))
                    || event.getPlayer().isOp()){
                return;
            }

            if (this.plugin.getConfig().getBoolean("worldList." + event.getPlayer().getWorld().getName() + ".breakBlockWhite.type")){
                if (this.plugin.getConfig()
                        .getStringList("worldList." + event.getPlayer().getWorld().getName() + ".breakBlockWhite.block")
                        .contains(event.getBlock().getType().toString())){
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(this.message.Message(getPlugin(Main.class).getMessage().getString("pluginPrefix") + getPlugin(Main.class).getMessage().getString("pluginMessage.notBreak")));
                    return;
                }
            } else {
                if (!this.plugin.getConfig()
                        .getStringList("worldList." + event.getPlayer().getWorld().getName() + ".breakBlockWhite.block")
                        .contains(event.getBlock().getType().toString())){
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(this.message.Message(getPlugin(Main.class).getMessage().getString("pluginPrefix") + getPlugin(Main.class).getMessage().getString("pluginMessage.notBreak")));
                }
            }
        }
    }
    private boolean istool(Material material) {
        switch (material) {
            case WOODEN_AXE:
            case STONE_AXE:
            case IRON_AXE:
            case GOLDEN_AXE:
            case DIAMOND_AXE:
            case WOODEN_SHOVEL:
            case STONE_SHOVEL:
            case IRON_SHOVEL:
            case GOLDEN_SHOVEL:
            case DIAMOND_SHOVEL:
            case WOODEN_HOE:
            case STONE_HOE:
            case IRON_HOE:
            case GOLDEN_HOE:
            case DIAMOND_HOE:
                return true;
            default:
                return false;
        }
    }

    @EventHandler
    public void worldInteraction(PlayerInteractEvent event) {
        //判断是否是配置中的世界
        if (this.plugin.getConfig().getBoolean("worldList." + event.getPlayer().getWorld().getName() + ".type")) {

            if (event.getPlayer()
                    .hasPermission(this.plugin.getConfig().getString("worldList." + event.getPlayer().getWorld().getName() + ".permissions"))
                    || event.getPlayer().isOp()) {
                return;
            }

            //判断是否是黑名单的物品
            if (this.plugin.getConfig().getBoolean("worldList." + event.getPlayer().getWorld().getName() + ".itemAllInteract.type") && event.hasItem()) {
                if (event.getHand().equals(EquipmentSlot.HAND) || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
                    if (this.plugin.getConfig().getStringList("worldList." + event.getPlayer().getWorld().getName() + ".itemAllInteract.item").contains(event.getItem().getType().toString())) {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(this.message.Message(getPlugin(Main.class).getMessage().getString("pluginPrefix") + getPlugin(Main.class).getMessage().getString("pluginMessage.notItem")));
                        return;
                    }
                }
            }
            if (!this.plugin.getConfig().getBoolean("worldList." + event.getPlayer().getWorld().getName() + ".itemAllInteract.type") && event.hasItem()) {
                if (event.getHand().equals(EquipmentSlot.HAND) || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
                    if (!this.plugin.getConfig().getStringList("worldList." + event.getPlayer().getWorld().getName() + ".itemAllInteract.item").contains(event.getItem().getType().toString())) {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(this.message.Message(getPlugin(Main.class).getMessage().getString("pluginPrefix") + getPlugin(Main.class).getMessage().getString("pluginMessage.notItem")));
                        return;
                    }
                }
            }

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK
                    && this.plugin.getConfig().getBoolean("worldList." + event.getPlayer().getWorld().getName() + ".notAxeToWood")
                    && istool(event.getPlayer().getInventory().getItemInMainHand().getType())) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(this.message.Message(getPlugin(Main.class).getMessage().getString("pluginPrefix") + getPlugin(Main.class).getMessage().getString("pluginMessage.notToolRight")));
                return;
            }

            //放置方块
            Action action = event.getAction();
            if (action == Action.RIGHT_CLICK_BLOCK && event.isBlockInHand() && event.hasItem() && !this.plugin.getConfig().getStringList("placeBlock").contains(event.getItem().getType().toString())) {
                if (this.plugin.getConfig().getBoolean("worldList." + event.getPlayer().getWorld().getName() + ".placeBlockWhite.type")) {
                    if (this.plugin.getConfig().getStringList("worldList." + event.getPlayer().getWorld().getName() + ".placeBlockWhite.block").contains(event.getItem().getType().toString())) {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(this.message.Message(getPlugin(Main.class).getMessage().getString("pluginPrefix")
                                + getPlugin(Main.class).getMessage().getString("pluginMessage.notBuilder")));
                        return;
                    }
                    return;
                } else {
                    if (!this.plugin.getConfig().getStringList("worldList." + event.getPlayer().getWorld().getName() + ".placeBlockWhite.block").contains(event.getItem().getType().toString())) {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(this.message.Message(getPlugin(Main.class).getMessage().getString("pluginPrefix")
                                + getPlugin(Main.class).getMessage().getString("pluginMessage.notBuilder")));
                        return;
                    }
                    return;
                }
            }
            //判断右键交互
            if (action == Action.RIGHT_CLICK_BLOCK && event.hasBlock()) {
                if (event.getHand().equals(EquipmentSlot.HAND)) {
                    if (this.plugin.getConfig().getBoolean("worldList." + event.getPlayer().getWorld().getName() + ".blockAllInteract.type")) {
                        if (this.plugin.getConfig().getStringList("worldList." + event.getPlayer().getWorld().getName() + ".blockAllInteract.block").contains(event.getClickedBlock().getType().toString())) {
                            long currentTime = System.currentTimeMillis();
                            event.setCancelled(true);
                            if (currentTime - lastMessageTime > this.plugin.getConfig().getInt("worldList." + event.getPlayer().getWorld().getName() + ".blockAllInteract.time") * 1000) { //设置5秒钟限制
                                String message = getPlugin(Main.class).getMessage().getString("pluginMessage.notUse");
                                event.getPlayer().sendMessage(this.message.Message(getPlugin(Main.class).getMessage().getString("pluginPrefix") + message));
                                lastMessageTime = currentTime;
                            }
                            return;
                        } else {
                            return;
                        }
                    } else {
                        if (!this.plugin.getConfig().getStringList("worldList." + event.getPlayer().getWorld().getName() + ".blockAllInteract.block").contains(event.getClickedBlock().getType().toString())) {
                            long currentTime = System.currentTimeMillis();
                            event.setCancelled(true);
                            if (currentTime - lastMessageTime > this.plugin.getConfig().getInt("worldList." + event.getPlayer().getWorld().getName() + ".blockAllInteract.time") * 1000) { //设置5秒钟限制
                                String message = getPlugin(Main.class).getMessage().getString("pluginMessage.notUse");
                                event.getPlayer().sendMessage(this.message.Message(getPlugin(Main.class).getMessage().getString("pluginPrefix") + message));
                                lastMessageTime = currentTime;
                            }
                            return;
                        } else {
                            return;
                        }
                    }
                }
            }
            //判断踩踏
            if (action == Action.PHYSICAL && event.hasBlock()) {
                if (this.plugin.getConfig().getBoolean("worldList." + event.getPlayer().getWorld().getName() + ".blockAllInteract.type")) {
                    if (this.plugin.getConfig().getStringList("worldList." + event.getPlayer().getWorld().getName() + ".blockAllInteract.item").contains(event.getClickedBlock().getType().toString())) {
                        long currentTime = System.currentTimeMillis();
                        event.setCancelled(true);
                        if (currentTime - lastMessageTime > this.plugin.getConfig().getInt("worldList." + event.getPlayer().getWorld().getName() + ".blockAllInteract.time") * 1000) { //设置5秒钟限制
                            String message = getPlugin(Main.class).getMessage().getString("pluginMessage.notUse");
                            event.getPlayer().sendMessage(this.message.Message(getPlugin(Main.class).getMessage().getString("pluginPrefix") + message));
                            lastMessageTime = currentTime;
                        }
                    }
                }else {
                    if (!this.plugin.getConfig().getStringList("worldList." + event.getPlayer().getWorld().getName() + ".blockAllInteract.block").contains(event.getClickedBlock().getType().toString())) {
                        long currentTime = System.currentTimeMillis();
                        event.setCancelled(true);
                        if (currentTime - lastMessageTime > this.plugin.getConfig().getInt("worldList." + event.getPlayer().getWorld().getName() + ".blockAllInteract.time") * 1000) { //设置5秒钟限制
                            String message = getPlugin(Main.class).getMessage().getString("pluginMessage.notUse");
                            event.getPlayer().sendMessage(this.message.Message(getPlugin(Main.class).getMessage().getString("pluginPrefix") + message));
                            lastMessageTime = currentTime;
                        }
                    }
                }
            }
        }
    }
    //保护实体
    @EventHandler
    public void entityAction(EntityDamageByEntityEvent event){
        if (event.getDamager().getType() == PLAYER){
            //判断是否是配置中的世界
            if (this.plugin.getConfig().getBoolean("worldList." + event.getDamager().getWorld().getName() + ".type")) {

                if (event.getDamager().isOp() || event.getDamager().hasPermission(this.plugin.getConfig().getString("worldList." + event.getDamager().getWorld().getName() + ".permissions"))) {
                    return;
                }
                //展示框
                if (event.getEntityType() == EntityType.ITEM_FRAME){
                    return;
                }
                //盔甲架
                if (event.getEntityType() == EntityType.ARMOR_STAND
                        && !this.plugin.getConfig().getBoolean("worldList." + event.getDamager().getWorld().getName() + ".amrorBreak")
                ) {
                    return;
                }

                if (this.plugin.getConfig().getBoolean("worldList." + event.getDamager().getWorld().getName() + ".entityInteract.type")) {
                    if (this.plugin.getConfig().getStringList("worldList." + event.getDamager().getWorld().getName() + ".entityInteract.entity").contains(event.getEntity().getType().toString())) {
                        event.getDamager().sendMessage(this.message.Message(getPlugin(Main.class).getMessage().getString("pluginPrefix") + getPlugin(Main.class).getMessage().getString("pluginMessage.notDamageEntity")));
                        event.setCancelled(true);
                    }
                }
                else if(!this.plugin.getConfig().getStringList("worldList." + event.getDamager().getWorld().getName() + ".entityInteract.entity").contains(event.getEntity().getType().toString())){
                        event.getDamager().sendMessage(this.message.Message(getPlugin(Main.class).getMessage().getString("pluginPrefix") + getPlugin(Main.class).getMessage().getString("pluginMessage.notDamageEntity")));
                        event.setCancelled(true);
                }
            }
        }
    }
    //禁止展示框放置物品
    @EventHandler
    public void onPlayerInteractEntityEvent (PlayerInteractEntityEvent event) {
        if (this.plugin.getConfig().getBoolean("worldList." + event.getPlayer().getWorld().getName() + ".type")
                && this.plugin.getConfig().getBoolean("worldList." + event.getPlayer().getWorld().getName() + ".frameProtection")) {
        if (event.getPlayer().isOp() || event.getPlayer()
                .hasPermission(this.plugin.getConfig().getString("worldList." + event.getPlayer().getWorld().getName() + ".permissions"))) {
            return;
        }
            if (event.getRightClicked().getType() == EntityType.ITEM_FRAME){
                event.setCancelled(true);
                event.getPlayer().sendMessage(this.message.Message(getPlugin(Main.class).getMessage().getString("pluginPrefix") + getPlugin(Main.class).getMessage().getString("pluginMessage.notFrame")));
            }
        }
    }
    //禁止取出和装入装备盔甲架
    @EventHandler
    public void onPlayerArmorStandManipulateEvent (PlayerArmorStandManipulateEvent event) {
        if (this.plugin.getConfig().getBoolean("worldList." + event.getPlayer().getWorld().getName() + ".type")
                && this.plugin.getConfig().getBoolean("worldList." + event.getPlayer().getWorld().getName() + ".amrorClick")) {
        if (event.getPlayer().isOp() || event.getPlayer()
                .hasPermission(this.plugin.getConfig().getString("worldList." + event.getPlayer().getWorld().getName() + ".permissions"))) {
            return;
        }
            event.setCancelled(true);
            event.getPlayer().sendMessage(this.message.Message(getPlugin(Main.class).getMessage().getString("pluginPrefix") + getPlugin(Main.class).getMessage().getString("pluginMessage.notArmorClick")));
        }
    }
    //禁止盔甲架、水晶、船、矿车
    @EventHandler
    public void amrorEvent(EntityPlaceEvent event){
        if (this.plugin.getConfig().getBoolean("worldList." + event.getPlayer().getWorld().getName() + ".type")) {
            if (event.getPlayer().isOp() || event.getPlayer().hasPermission(this.plugin.getConfig().getString("worldList." + event.getPlayer().getWorld().getName() + ".permissions"))) {
                return;
            }
            if (this.plugin.getConfig().getBoolean("worldList." + event.getPlayer().getWorld().getName() + ".amrorProtection")) {
                event.getPlayer().sendMessage(this.message.Message(getPlugin(Main.class).getMessage().getString("pluginPrefix") + getPlugin(Main.class).getMessage().getString("pluginMessage.notArmor")));
                event.setCancelled(true);
            }
        }
    }

    //树叶禁止消失
    @EventHandler
    public void LeavesEvent (LeavesDecayEvent event) {
        if (this.plugin.getConfig().getBoolean("worldList." + event.getBlock().getWorld().getName() + ".type")) {
            if (this.plugin.getConfig().getBoolean("worldList." + event.getBlock().getWorld().getName() + ".LeavesProtection")) {
                event.setCancelled(true);
            }
        }
    }

    //检测水桶倒水
    @EventHandler
    public void bucketPour(PlayerBucketEmptyEvent event) {
        if (this.plugin.getConfig().getBoolean("worldList." + event.getPlayer().getWorld().getName() + ".type")) {
            if (event.getPlayer().isOp() || event.getPlayer().hasPermission(this.plugin.getConfig().getString("worldList." + event.getPlayer().getWorld().getName() + ".permissions"))) {
                return;
            }
            if (this.plugin.getConfig().getBoolean("worldList." + event.getPlayer().getWorld().getName() + ".bucketPour")) {
                event.getPlayer().sendMessage(this.message.Message(getPlugin(Main.class).getMessage().getString("pluginPrefix") + getPlugin(Main.class).getMessage().getString("pluginMessage.notBucketPour")));
                event.setCancelled(true);
            }
        }
    }
    //检测水桶装水
    @EventHandler
    public void bucketFill(PlayerBucketFillEvent event) {
        if (this.plugin.getConfig().getBoolean("worldList." + event.getPlayer().getWorld().getName() + ".type")) {
            if (event.getPlayer().isOp() || event.getPlayer().hasPermission(this.plugin.getConfig().getString("worldList." + event.getPlayer().getWorld().getName() + ".permissions"))) {
                return;
            }
            if (this.plugin.getConfig().getBoolean("worldList." + event.getPlayer().getWorld().getName() + ".bucketFill")) {
                event.getPlayer().sendMessage(this.message.Message(getPlugin(Main.class).getMessage().getString("pluginPrefix") + getPlugin(Main.class).getMessage().getString("pluginMessage.notBucketFill")));
                event.setCancelled(true);
            }
        }
    }

    //禁止自然消失
    @EventHandler
    public void BlockFade(BlockFadeEvent event) {
        if (this.plugin.getConfig().getBoolean("worldList." + event.getBlock().getWorld().getName() + ".type")) {
            if (this.plugin.getConfig().getBoolean("worldList." + event.getBlock().getWorld().getName() + ".notBlockTick")){
                event.setCancelled(true);
            }
        }
    }

    //检测爆炸实体
    @EventHandler
    public void onTNTPrimeEvent(EntityExplodeEvent event){
        if (this.plugin.getConfig().getBoolean("worldList." + event.getEntity().getWorld().getName() + ".type")) {
            if (this.plugin.getConfig().getBoolean("worldList." + event.getEntity().getWorld().getName() + ".notEntityExplode")){
                event.getEntity().remove();
            }
        }
    }

    //检测生物出生
    @EventHandler
    public void onCreatureSpawnEvent(CreatureSpawnEvent event){
        if (this.plugin.getConfig().getBoolean("worldList." + event.getEntity().getWorld().getName() + ".type")
                && this.plugin.getConfig().getBoolean("worldList." + event.getEntity().getName() + ".notEntitySpawn")
                && event.getEntity().getType() != PLAYER) {

            event.setCancelled(true);
        }
    }
    //检测区块强加载导致的生物生成
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        //检查生物生成的世界
        if (this.plugin.getConfig().getBoolean("worldList." + event.getWorld().getName() + ".type")
                && this.plugin.getConfig().getBoolean("worldList." + event.getWorld().getName() + ".notEntitySpawn")) {
            //清除已生成的生物实体
            for (Entity entity : event.getChunk().getEntities()) {
                if (entity instanceof LivingEntity) {
                    entity.remove(); //移除生物实体
                }
            }
        }
    }

    //检测告示牌
    @EventHandler
    public void onSignChangeEvent(SignChangeEvent event){
        if (this.plugin.getConfig().getBoolean("worldList." + event.getPlayer().getWorld().getName() + ".type")) {
            if (event.getPlayer().isOp() || event.getPlayer().hasPermission(this.plugin.getConfig().getString("worldList." + event.getPlayer().getWorld().getName() + ".permissions"))) {
                return;
            }
            if (this.plugin.getConfig().getBoolean("worldList." + event.getPlayer().getWorld().getName() + ".notSignEdit")){
                event.getPlayer().sendMessage(this.message.Message(getPlugin(Main.class).getMessage().getString("pluginPrefix") + getPlugin(Main.class).getMessage().getString("pluginMessage.notSignEdit")));
                event.setCancelled(true);
            }
        }
    }
}
