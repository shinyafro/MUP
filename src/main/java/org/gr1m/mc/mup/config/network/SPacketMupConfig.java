package org.gr1m.mc.mup.config.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.gr1m.mc.mup.Mup;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SPacketMupConfig implements IMessage
{
    private HashMap<String, Boolean> configList = new HashMap<>();

    public SPacketMupConfig()
    {

    }

    public void addConfig(String name, Boolean enabled)
    {
        configList.put(name, enabled);
    }

    public HashMap<String, Boolean> getConfigList()
    {
        return this.configList;
    }

    @Override
    public void fromBytes(final ByteBuf buf)
    {
        int configCount;

        this.configList.clear();
        configCount = buf.readInt();

        for (int configIndex = 0; configIndex < configCount; configIndex++)
        {
            int configNameLength = ((int) buf.readByte());
            byte[] fieldNameBytes = new byte[configNameLength];
            buf.readBytes(fieldNameBytes, 0, configNameLength);
            Boolean enabled = buf.readBoolean();

            try
            {
                String configName = new String(fieldNameBytes, "UTF-8");
                this.configList.put(configName, enabled);
            }
            catch (Exception e)
            {
                Mup.logger.error("SPacketMupConfig.fromBytes: String decoding failure.");
            }
        }
    }

    @Override
    public void toBytes(final ByteBuf buf)
    {
        int configCount = this.configList.size();

        buf.writeInt(configCount);

        for (Map.Entry<String, Boolean> configEntry : this.configList.entrySet())
        {
            byte[] fieldName = configEntry.getKey().getBytes(StandardCharsets.UTF_8);
            buf.writeByte(fieldName.length);
            buf.writeBytes(fieldName);
            buf.writeBoolean(configEntry.getValue());
        }
    }

    public static class Handler implements IMessageHandler<SPacketMupConfig, IMessage>
    {
        @Override
        public IMessage onMessage(final SPacketMupConfig message, final MessageContext ctx)
        {
            NetHandlerPlayClient handler = ctx.getClientHandler();

            Minecraft.getMinecraft().addScheduledTask(() ->
                                                          ConfigPacketHandler.handleServerConfigReceived(message, handler)
                                                     );

            return null;
        }
    }
}
