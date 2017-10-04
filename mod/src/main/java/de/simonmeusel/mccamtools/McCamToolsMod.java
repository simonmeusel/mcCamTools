package de.simonmeusel.mccamtools;

import com.google.common.collect.Lists;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nullable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

@Mod(modid = McCamToolsMod.MODID, version = McCamToolsMod.VERSION)
public class McCamToolsMod implements IClientCommand {
    public static final String MODID = "examplemod";
    public static final String VERSION = "1.0";

    private ICommandSender player;
    private BufferedWriter bw;

    private int delay = 0;
    private int duration = 0;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);

        ClientCommandHandler.instance.registerCommand(this);

        // some example code
        System.out.println("DIRT BLOCK >> " + Blocks.DIRT.getUnlocalizedName());
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (player != null) {
            if (delay > 0) {
                player.sendMessage(new TextComponentString("Start in " + delay + " ticks"));
                delay--;
            } else if (duration > 0) {
                player.sendMessage(new TextComponentString("Stop in " + duration + " ticks"));
                duration--;
                Vec3d position = player.getCommandSenderEntity().getPositionVector();
                Vec2f pitchYaw = player.getCommandSenderEntity().getPitchYaw();
                try {
                    bw.write(position.x + " " + position.y + " " + position.z + " " + pitchYaw.x + " " + pitchYaw.y + "\n");
                } catch (Exception e) {
                    e.printStackTrace();
                    player.sendMessage(new TextComponentString(TextFormatting.RED + "Error while writing to file!"));
                }
            } else {
                try {
                    bw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    player.sendMessage(new TextComponentString(TextFormatting.RED + "Error while writing to file!"));
                }
                player = null;
            }
        }

    }

    @Override
    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        return false;
    }

    @Override
    public String getName() {
        return "camtools";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/camtools <delay in ticks> <duration in ticks> <path to output file>";
    }

    @Override
    public List<String> getAliases() {
        return Lists.newArrayList("mccamtools");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        try {
            delay = Integer.parseInt(args[0]);
            duration = Integer.parseInt(args[1]);
        } catch (Exception e) {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Delay and duration have to be strings!"));
            return;
        }

        File file;

        try {
            file = new File(args[2]);
            bw = new BufferedWriter(new FileWriter(file));
            bw.flush();
        } catch (Exception e) {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Could not create output file!"));
            return;
        }

        sender.sendMessage(new TextComponentString(TextFormatting.GOLD + "Starting " + duration + " tick long capture in " + delay + " ticks"));
        player = sender;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return new ArrayList<>();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
