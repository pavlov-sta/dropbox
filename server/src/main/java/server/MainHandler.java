package server;

import common.FileMessage;
import common.FileRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;


import java.nio.file.Files;
import java.nio.file.Paths;

public class MainHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        try {
            if (msg == null) {
                return;
            }
            if (msg instanceof FileRequest) {
                FileRequest fr = (FileRequest) msg;
                if (fr.getOperationType().equals("UPLOAD")) {
                    if (!Files.exists(Paths.get("server/src/main/server_storage/" + fr.getFilename()))) {
                        FileMessage out = new FileMessage(Paths.get("client/src/main/client_storage/" + fr.getFilename()), "UPLOAD");
                        ctx.writeAndFlush(out);
                    }
                } else if (Files.exists(Paths.get("server/src/main/client_storage/" + fr.getFilename()))) {
                    FileMessage fm = new FileMessage(Paths.get("server/src/main/server_storage/" + fr.getFilename()), "DOWNLOAD");
                    ctx.writeAndFlush(fm);
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
