package vlad8947.local.movement.common.view;

public interface DialogInterface {

    void error(String title, String header, String content);

    String textInput(String defaultText, String title, String header, String content) throws InterruptedException;

}
