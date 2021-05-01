package local.movement.common.transfer;

import local.movement.common.AppProperties;
import local.movement.common.view.DialogInterface;
import local.movement.common.model.MovementProperties;
import local.movement.common.model.MovementStatus;
import local.movement.common.view.MovementPropListAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.StandardOpenOption;

import static local.movement.common.transfer.ChannelTransfer.*;

public class FileReceiver implements Runnable, Closeable {

    private Logger logger = LogManager.getLogger(FileReceiver.class);

    private MovementProperties movementProperties;
    private MovementPropListAdapter receiverListAdapter;
    private MovementPropListAdapter movementListAdapter;
    private SocketChannel socketChannel;
    private ByteBuffer dataBuffer = ByteBuffer.allocate(AppProperties.getBufferLength());
    private ByteBuffer messageBuffer = ByteBuffer.allocate(Message.LENGTH);
    private FileChannel fileChannel;
    private String directory;
    private DialogInterface dialog;

    public FileReceiver(MovementProperties movementProperties, String directory, DialogInterface dialog,
                        MovementPropListAdapter receiverListAdapter, MovementPropListAdapter movementListAdapter) {
        this.movementProperties = movementProperties;
        this.directory = directory;
        this.dialog = dialog;
        this.receiverListAdapter = receiverListAdapter;
        this.movementListAdapter = movementListAdapter;
        socketChannel = movementProperties.getSocketChannel();
    }

    @Override
    public void run() {
        logger.info("Start");
        try {
            File file = new File(directory, movementProperties.getFileProperties().getFileName());
            logger.info("Create receive file");
            String title = AppProperties.Localisation.messages.getString("dialog.get_file_name.title");
            String headerBeforeFileName = AppProperties.Localisation.messages.getString("dialog.get_file_name.header.before_file_name");
            String headerAfterFileName = AppProperties.Localisation.messages.getString("dialog.get_file_name.header.after_file_name");
            String content = AppProperties.Localisation.messages.getString("dialog.get_file_name.content");
            while (!file.createNewFile()) {
                String fileName = dialog.textInput(file.getName(),
                        title, headerAfterFileName + file.getName() + headerBeforeFileName, content);
                if (fileName == null) {
                    logger.info("File name equals null, send CANCEL");
                    clearPutFlipWriteFB(Message.CANCEL, socketChannel, messageBuffer);
                    return;
                }
                file = new File(directory, fileName);
            }
            movementProperties.setFile(file);
            movementProperties.setStatus(MovementStatus.MOVE);
            movementProperties.setCloseable(this);
            receiverListAdapter.remove(movementProperties);
            movementListAdapter.add(movementProperties);
            receiveFile();

            if (!socketChannel.isOpen()) return;
            //Finish message
            logger.info("Write finish message");
            clearPutFlipWriteFB(Message.FINISH, socketChannel, messageBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            logger.info("Finish");
            movementProperties.setStatus(MovementStatus.FINISH);
            try {
                close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void receiveFile() {
        logger.info("Receive file");
        try (FileChannel fileChannel =
                     FileChannel.open(movementProperties.getFile().toPath(), StandardOpenOption.APPEND)) {
            this.fileChannel = fileChannel;
            clearPutFlipWriteFB(Message.CONFIRM, socketChannel, messageBuffer);

            long dataLength = movementProperties.getFileProperties().getFileLength();
            long position = 0;

            while (position < dataLength-1) {
                clearRead(socketChannel, dataBuffer);
                flipWriteFB(fileChannel, dataBuffer);
                position += dataBuffer.limit();
                movementProperties.addDoneBytes(dataBuffer.limit());
            }

        } catch (IOException e) {
            if (socketChannel.isOpen()) {
                logger.warn(e);
                e.printStackTrace();
            }
        }
        logger.info("End receive file");
    }

    @Override
    public void close() throws IOException {
        if (socketChannel.isOpen()) {
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
