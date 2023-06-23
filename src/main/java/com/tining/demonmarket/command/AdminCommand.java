package com.tining.demonmarket.command;

import com.tining.demonmarket.command.dispatcher.AdminShopCommand;
import com.tining.demonmarket.command.dispatcher.AdminShopNbtSetCommand;
import com.tining.demonmarket.command.dispatcher.AdminShopSetCommand;
import com.tining.demonmarket.common.util.LangUtil;
import com.tining.demonmarket.common.util.PluginUtil;
import com.tining.demonmarket.common.util.WorthUtil;
import com.tining.demonmarket.common.ref.JsonItemStack;
import com.tining.demonmarket.storage.ConfigReader;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static com.tining.demonmarket.common.util.PluginUtil.itemStackSerialize;

public class AdminCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 1) {
            return false;
        }
        // 构造参数包
        CommandPack commandPack = new CommandPack();
        commandPack.sender = sender;
        commandPack.command = command;
        commandPack.label = label;
        commandPack.args = args;

        switch (args[0]) {
            case "nbtset": {
                Player player = (Player) sender;
                //校验参数合法
                if (args.length < 2) {
                    //应该有一个set和一个价格 两个参数
                    return false;
                }
                //获取物品名称
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                Material itemToSell = player.getInventory().getItemInMainHand().getType();
                //校验物品是否合法
                if (Objects.isNull(itemToSell) || itemToSell.name().equals("AIR")) {
                    sender.sendMessage(ChatColor.YELLOW + LangUtil.get("[DemonMarket]你手里的物品无法交易"));
                    return true;
                }
                double price = 0.0;
                //校验价值是否合法
                try {
                    price = Double.parseDouble(args[1]);
                    if (price < 0) {
                        sender.sendMessage(ChatColor.YELLOW + LangUtil.get("[DemonMarket]你输入的价格不合法"));
                        return true;
                    }
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.YELLOW + LangUtil.get("[DemonMarket]你输入的价格不合法"));
                    return true;
                }
                //修改数值
                WorthUtil.addToNBTWorth(PluginUtil.getKeyName(itemStack), price);
                //修改配置文件
                //保存
                ConfigReader.reloadConfig();
                sender.sendMessage(ChatColor.YELLOW + LangUtil.get("[DemonMarket]设置成功"));
                return true;
            }
            case "set": {
                Player player = (Player) sender;
                //校验参数合法
                if (args.length < 2) {
                    //应该有一个set和一个价格 两个参数
                    return false;
                }
                //获取物品名称
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                Material itemToSell = player.getInventory().getItemInMainHand().getType();
                //校验物品是否合法
                if (Objects.isNull(itemToSell) || itemToSell.name().equals("AIR")) {
                    sender.sendMessage(ChatColor.YELLOW + LangUtil.get("[DemonMarket]你手里的物品无法交易"));
                    return true;
                }
                double price = 0.0;
                //校验价值是否合法
                try {
                    price = Double.parseDouble(args[1]);
                    if (price < 0) {
                        sender.sendMessage(ChatColor.YELLOW + LangUtil.get("[DemonMarket]你输入的价格不合法"));
                        return true;
                    }
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.YELLOW + LangUtil.get("[DemonMarket]你输入的价格不合法"));
                    return true;
                }
                //修改数值
                WorthUtil.addToWorth(itemStack.getType().name(), price);
                //修改配置文件
                //保存
                ConfigReader.reloadConfig();
                sender.sendMessage(ChatColor.YELLOW + LangUtil.get("[DemonMarket]设置成功"));
                return true;
            }
            case "name": {
                Player player = (Player) sender;
                Material itemToSell = player.getInventory().getItemInMainHand().getType();
                sender.sendMessage(ChatColor.YELLOW + LangUtil.get("[DemonMarket]你手中拿的是：") + itemToSell.name());
                return true;
            }
            case "nbt": {
                Player player = (Player) sender;
                Material itemToSell = player.getInventory().getItemInMainHand().getType();
                sender.sendMessage(ChatColor.YELLOW + LangUtil.get("[DemonMarket]你手中拿的是：") + itemToSell.name());
                sender.sendMessage(ChatColor.YELLOW + LangUtil.get("[DemonMarket]NBT为：") + itemStackSerialize(player.getInventory().getItemInMainHand()));
                sender.sendMessage(ChatColor.YELLOW + LangUtil.get("[DemonMarket]在商店配置中的NBT对应码为：") + PluginUtil.getKeyName(player.getInventory().getItemInMainHand()));
                return true;
            }
            case "reload": {
                ConfigReader.reloadConfig();
                sender.sendMessage(ChatColor.YELLOW + LangUtil.get("[DemonMarket]重载成功"));
                return true;
            }
            case "shopset": {
                return new AdminShopSetCommand().deal(commandPack);
            }
            case "shopnbtset": {
                return new AdminShopNbtSetCommand().deal(commandPack);
            }
            case "shop":{
                return new AdminShopCommand().deal(commandPack);
            }
            default: {
                sender.sendMessage(getHelp());
                return true;
            }
        }
    }

    /**
     * 获取帮助提示
     * @return
     */
    private String getHelp() {
        String help = LangUtil.get("/mtadmin set [价格] 为手里物品新增或修改价格\n") +
                LangUtil.get("/mtadmin nbtset [价格] 为手里的nbt物品新增或修改价格") + "\n" +
                LangUtil.get("/mtadmin nbt 查看手中物品nbt信息\n") +
                LangUtil.get("/mtadmin name 查看手中物品名称\n") +
                LangUtil.get("/mtadmin reload 重载插件配置");
        return help;
    }
}
