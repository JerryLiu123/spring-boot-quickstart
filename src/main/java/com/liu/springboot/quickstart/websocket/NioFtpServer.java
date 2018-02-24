package com.liu.springboot.quickstart.websocket;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioFtpServer {
    //新建缓冲区,用户给客户端发发送消息
    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    //保存key 和 文件通道
    Map<SelectionKey, FileChannel> fileMap = new ConcurrentHashMap<SelectionKey, FileChannel>();
    //开启线程池
    private ExecutorService exec = Executors.newFixedThreadPool(10);
    private static final String ROOT_PATH="E:\\FtpDir\\";
    private boolean running = true;
    /*
     * 这样的话一个文件就是建立一个socket链接，怎么多个文件建立一个socket链接~~
     */
    public void startServer() throws IOException, InterruptedException{
        Selector selector = Selector.open();
        //建立socket通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        //设置为不阻塞
        serverChannel.configureBlocking(false);
        //打开端口
        serverChannel.bind(new InetSocketAddress(5000));
        //将 socket通道注册到选择器selector上，另外通道一旦被注册，将不能再回到阻塞状态
        //一个通道可以被注册到多个选择器上，但对每个选择器而言只能被注册一次。
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器已开启...");
        try {
            while (running && !Thread.interrupted()) {
                int num = selector.select();
                if (num == 0) continue;
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    //在这里面是线性的
                    SelectionKey key = it.next();
                    if (key.isAcceptable()) {//连接成功状态，给客户端发送信息
                        ServerSocketChannel serverChannel1 = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = serverChannel1.accept();
                        if (socketChannel == null) continue;
                        //读取客户端传过来的文件信息
                        String fileName = readToClient(socketChannel);
                        //设置通道为非阻塞,例如:read操作就不会堵住了,但是会成为线性的？
                        socketChannel.configureBlocking(false);
                        //标记为对Read事件感兴趣
                        //将SocketChannel通道注册到选择器selector上,
                        //也就是说选择器selector监听了通道SocketChannel的read操作和通道ServerSocketChannel的acceptable操作
                        SelectionKey key1 = socketChannel.register(selector, SelectionKey.OP_READ);
                        InetSocketAddress remoteAddress = (InetSocketAddress)socketChannel.getRemoteAddress();
                        String path = ROOT_PATH + remoteAddress.getHostName() + "_" + remoteAddress.getPort() + "_" + fileName;
                        //使用多线程的时候注意，同一个文件要用一个FileChannel
                        FileChannel flChannel = new RandomAccessFile(path, "rw").getChannel();
                        fileMap.put(key1, flChannel);
                        System.out.println(socketChannel.getRemoteAddress() + "连接成功...  FileName:"+fileName);
                        writeToClient(socketChannel);
                    }else if (key.isReadable()){//接收数据
                        //System.out.println("key:"+key.toString());
                        //会多次执行，也就是说nio是面向缓存区的概念，当一个缓存的数据写入通道，选择器就会监听到通道已经是可读取的状态，所以就会运行该方法，开始读取数据
                        //io socket 和nio socket的区别我认为在这里的时候io是一直在阻塞的发送，数据也就是同一个文件只会有一个线程在写入文件，
                        //但是nio可以一个文件可以多个线程写入
                        readData(key);
                    }
                    // NIO的特点只会累加，已选择的键的集合不会删除，ready集合会被清空
                    // 只是临时删除已选择键集合，当该键代表的通道上再次有感兴趣的集合准备好之后，又会被select函数选中
                    it.remove();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally {
            if(selector != null) {
                //关闭选择器
                selector.close();
            }
            if(serverChannel != null) {
                //关闭socket端口
                serverChannel.close();
            }
        }
    }
    private void writeToClient(SocketChannel socketChannel) throws IOException {
        buffer.clear();
        buffer.put((socketChannel.getRemoteAddress() + "连接成功").getBytes("utf-8"));
        buffer.flip();
        socketChannel.write(buffer);
        buffer.clear();
    }
    
    private String readToClient(SocketChannel socketChannel) throws IOException {
        buffer.clear();
        int ret = socketChannel.read(buffer);
        //buffer.flip();
        String value = null;
        if (ret > 0) {
            value = byteBufferToString(buffer);
        }
        return value;
    }
    
    private static String byteBufferToString(ByteBuffer dst) {  
        String ret = null;  
        if (dst != null) {  
            dst.flip();  
            byte[] tempb = new byte[dst.limit()];  
            dst.get(tempb);  
            ret = new String(tempb);  
        }  
        return ret;  
    }  
    protected void readData(final SelectionKey key) throws IOException {
        // 移除掉这个key的可读事件，已经在线程池里面处理
        key.interestOps(key.interestOps() & (~SelectionKey.OP_READ));
        exec.execute(new Runnable() {
            @Override
            public void run() {
                //定义缓冲区
                ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
                FileChannel fileChannel = fileMap.get(key);
                buffer.clear();
                SocketChannel socketChannel = (SocketChannel) key.channel();
                //InetSocketAddress remoteAddress = null;
                try {
                    //Thread.sleep(3000);
                    System.out.println("---------------"+key.toString());
                    int num = 0;
                    //从socketChannel中获得信息，通过fileChannel写入文件
                    while ((num = socketChannel.read(buffer)) > 0) {
                        buffer.flip();
                        // 写入文件
                        fileChannel.write(buffer);
                        //清除这个缓冲区
                        buffer.clear();
                    }
                    // 调用close为-1 到达末尾
                    if (num == -1) {
                        //关闭文件传输通道
                        fileChannel.close();
                        System.out.println("上传完毕"+key.toString());
                        //向客户端发送信息
                        buffer.clear();
                        buffer.put((socketChannel.getRemoteAddress() + "上传成功").getBytes("utf-8"));
                        buffer.flip();
                        socketChannel.write(buffer);
                        socketChannel.close();
                        // 只有调用cancel才会真正从已选择的键的集合里面移除，否则下次select的时候又能得到
                        // 一端close掉了，其对端仍然是可读的，读取得到EOF，返回-1
                        key.cancel(); 
                        return;
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    key.cancel();
                }
                // Channel的read方法可能返回0，返回0并不一定代表读取完了。
                // 工作线程结束对通道的读取，需要再次更新键的ready集合，将感兴趣的集合重新放在里面
                key.interestOps(key.interestOps() | SelectionKey.OP_READ);
                // 调用wakeup，使得选择器上的第一个还没有返回的选择操作立即返回即重新select
                key.selector().wakeup();
            }
        });
    }
    
    public static void main(String[] args) {
        try {
            new NioFtpServer().startServer();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
