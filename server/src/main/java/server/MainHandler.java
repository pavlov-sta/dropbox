package server;

import common.AuthMessage;
import common.AuthRequest;
import common.FileMessage;
import common.FileRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;


import java.nio.file.Files;
import java.nio.file.Paths;

public class MainHandler extends ChannelInboundHandlerAdapter {
    private final String CLIENT_DIRECTORY = "client/src/main/client_storage/";
    private final String SERVER_DIRECTORY = "server/src/main/server_storage/";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        try {
            if (msg == null) {
                return;
            }
            if(msg instanceof AuthRequest){
                AuthRequest user = (AuthRequest) msg;
                if (ClientAuth.authUser(user.getUsername(),user.getPassword())) {
                    System.out.println("Start");
                    ctx.writeAndFlush(new AuthMessage(true));
                } else {
                    System.out.println("No");
                    ctx.writeAndFlush(new AuthMessage(false));
                }
            }else if (msg instanceof FileRequest) {
                FileRequest fr = (FileRequest) msg;
                if (fr.getOperationType().equals("UPLOAD")) {
                    if (!Files.exists(Paths.get(SERVER_DIRECTORY + fr.getFilename()))) {
                        FileMessage out = new FileMessage(Paths.get(CLIENT_DIRECTORY + fr.getFilename()), "UPLOAD", null);
                        ctx.writeAndFlush(out);
                    }
                } else if (fr.getOperationType().equals("DOWNLOAD")) {
                    if (!Files.exists(Paths.get(CLIENT_DIRECTORY + fr.getFilename()))) {
                        FileMessage fm = new FileMessage(Paths.get(SERVER_DIRECTORY + fr.getFilename()), "DOWNLOAD", null);
                        ctx.writeAndFlush(fm);
                    }
                } else if (fr.getOperationType().equals("DELETE")) {
                    if (Files.exists(Paths.get(CLIENT_DIRECTORY + fr.getFilename())) || Files.exists(Paths.get(SERVER_DIRECTORY + fr.getFilename()))) {
                        if (fr.getListViewName().equals("filesServerList")) {
                            FileMessage fm = new FileMessage(Paths.get(SERVER_DIRECTORY + fr.getFilename()), "DELETE", "filesServerList");
                            ctx.writeAndFlush(fm);
                        } else {
                            FileMessage fm = new FileMessage(Paths.get(CLIENT_DIRECTORY + fr.getFilename()), "DELETE", "filesList");
                            ctx.writeAndFlush(fm);
                        }

                    }
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
