package HotelSimulatie.Constructor;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FilePicker {
    private static String fileFolderPath;            //holds the last folder location (static for clones)
    final private String typeOfFile;                 //type of file you're looking for
    private String fileLocationPath;                 //contains the absolute location
    private FileChooser fileChooser;                 //to select files
    private Stage stage;                             //the stage were you want to show the open dialog

    /**
     * the constructor
     *
     * @param typeOfFile name of the type of file (event or layout)
     * @param stage      the stage for the opendialog
     */
    public FilePicker(String typeOfFile, Stage stage) {
        this.fileChooser = new FileChooser();
        this.fileLocationPath = "";
        this.typeOfFile = "Select your " + typeOfFile + "-file";
        this.stage = stage;
    }

    /**
     * the file-picker method
     */
    public void filePicker() {
        fileChooser.setTitle(typeOfFile);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON-file (*.json)", "*.json"));
        if (fileFolderPath != null) {
            fileChooser.setInitialDirectory(new File(fileFolderPath));
        }
        //collect a file from the open dialog
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            this.fileLocationPath = file.getAbsolutePath();
            fileFolderPath = fileLocationPath.replace(file.getName(), "");
        }
    }

    /**
     * @return absolute path
     */
    public String chooser() {
        return this.fileLocationPath;
    }
}
