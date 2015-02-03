package tv.savageboy74.savagecore.common.core.command;

/*
 * CommandHandler.java
 * Copyright (C) 2014 - 2015 Savage - github.com/savageboy74
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import tv.savageboy74.savagecore.common.core.helper.StringHelper;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;

import java.util.List;
import java.util.Set;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

public class CommandHandler extends CommandBase {

    public static final String COMMAND_DISALLOWED = StringHelper.LIGHT_RED + "You are not allowed to use this command.";

    public static CommandHandler instance = new CommandHandler();

    private static TMap<String, ISubCommand> commands = new THashMap<String, ISubCommand>();

    static {
        registerSubCommand(CommandHelp.instance);
        registerSubCommand(CommandVersion.instance);
        registerSubCommand(CommandUpdate.instance);
    }

    public static void initCommands(FMLServerStartingEvent event) {

        event.registerServerCommand(instance);
    }

    public static boolean registerSubCommand(ISubCommand subCommand) {

        if (!commands.containsKey(subCommand.getCommandName())) {
            commands.put(subCommand.getCommandName(), subCommand);
            return true;
        }
        return false;
    }

    public static Set<String> getCommandList() {

        return commands.keySet();
    }

    @Override
    public String getCommandName() {

        return "savagecore";
    }

    @Override
    public List getCommandAliases() {

        return null;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {

        return "/" + getCommandName() + " help";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {

        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] arguments) {

        if (arguments.length <= 0) {
            throw new WrongUsageException("Type '" + getCommandUsage(sender) + "' for help.");
        }

        if (commands.containsKey(arguments[0])) {
            commands.get(arguments[0]).handleCommand(sender, arguments);
            return;
        }
        throw new WrongUsageException("Type '" + getCommandUsage(sender) + "' for help.");
    }

    @Override
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {

        if (par2ArrayOfStr.length == 1) {
            return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, commands.keySet());
        } else if (commands.containsKey(par2ArrayOfStr[0])) {
            return commands.get(par2ArrayOfStr[0]).addTabCompletionOptions(par1ICommandSender, par2ArrayOfStr);
        }
        return null;
    }

}