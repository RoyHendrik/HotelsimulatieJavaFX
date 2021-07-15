package HotelSimulatie.Constructor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * FileReader
 * gets a filepath of the json file and an builder type.
 * reads the file and sends the data into the builder.
 * this class is an singleton, so only one filereader can exist.
 */
public class FileReader {
    private static final FileReader fileReader = new FileReader();
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Constructor
     */
    public FileReader() {}

    public static FileReader getFileReader() {
        return fileReader;
    }

    /**
     * LayoutBuilder
     *
     * @param builder Reads an layout file and sends the data into the builder,
     *                checks if the units has additional data and sends it to the appropriate ConstructNew method in builder
     */
    protected void layoutReader(String filepath, UnitBuilder builder) {
        try {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JsonNode root = mapper.readTree(new File(filepath));

            for (JsonNode node : root) {
                String type = node.path("type").asText();

                int visualXPos = 0;
                int visualYPos = 0;
                int width = 0;
                int height = 0;
                int data = 0;
                boolean hasData = false;

                JsonNode positionNode = node.path("position");
                if (!positionNode.isMissingNode()) {
                    visualXPos = positionNode.path("x").asInt();
                    visualYPos = positionNode.path("y").asInt();

                }

                JsonNode dimensionNode = node.path("dimensions");
                if (!dimensionNode.isMissingNode()) {
                    width = dimensionNode.path("width").asInt();
                    height = dimensionNode.path("height").asInt();

                }

                JsonNode dataNode = node.path("data");
                if (!dataNode.isMissingNode()) {
                    if (type.equals("room")) {
                        hasData = true;
                        data = dataNode.path("stars").asInt();
                    } else if (type.equals("diner")) {
                        hasData = true;
                        data = dataNode.path("max").asInt();
                    } else {
                        hasData = false;
                    }

                }

                if (!hasData) {
                    builder.constructNew(type, width, height, visualYPos, visualXPos);
                } else {
                    builder.constructNew(type, width, height, visualYPos, visualXPos, data);
                }
            }

        } catch (IOException jme) {
            jme.printStackTrace();
        }
    }

    /**
     * EventReader
     *
     * @param builder Reads an event file and sends the data into the builder
     */
    protected void readEvents(String filepath, EventBuilder builder) {

        try {
            JsonNode root = mapper.readTree(new File(filepath));
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            for (JsonNode node : root) {
                String type = node.path("type").asText();

                int time = node.path("time").asInt();

                JsonNode dataNode = node.path("data");
                int guestID = 0;
                int stars = 0;
                int duration = 0;

                if (!dataNode.isMissingNode()) {
                    guestID = dataNode.path("guest").asInt();

                    stars = dataNode.path("stars").asInt();

                    duration = dataNode.path("duration").asInt();
                }

                builder.constructNew(type, time, guestID, stars, duration);

            }

        } catch (IOException je) {
            je.printStackTrace();
        }
    }

}
