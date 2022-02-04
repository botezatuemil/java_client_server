package JavaFX;

import Client.Client;
import Packet.Packet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import database.model.PersonEntity;
import database.model.TeamEntity;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class AdminController {

    @FXML
    private TableView tableViewComp;
    @FXML
    private TextField teamField;

    private Client client;
    private Gson gson;
    private MainController mainController;

    public void initialize() {

        mainController = new MainController();
        client = new Client();
        gson = new Gson();
    }

    public void onValidate() throws IOException {

        Packet packet = new Packet("Validate");

        String jsonSent = gson.toJson(packet);
        String jsonReceived = client.sendMessageToServer("Validate", jsonSent);

        Gson gson = new Gson();

        Type teamsListType = new TypeToken<List<TeamEntity>>() {
        }.getType();

        List<TeamEntity> teams;

        try {
            teams = gson.fromJson(jsonReceived, teamsListType);

            String message = "The following teams are not valid:\n ";
            for (TeamEntity teamEntity : teams) {
                message += teamEntity.getName() + "\n";
            }
            mainController.createInfoAlert(message);

        } catch (Exception e) {
            mainController.createInfoAlert("All teams are valid");
        }
    }

    public void startCompetition() throws Exception {

        Packet packet = new Packet("Start competition");
        String jsonSent = gson.toJson(packet);
        String jsonReceived = client.sendMessageToServer("Start competition", jsonSent);

        packet = gson.fromJson(jsonReceived, Packet.class);

        mainController.createInfoAlert(packet.message);
    }

    public void onNewStage() throws Exception {
        Type usersType = new TypeToken<List<PersonEntity>>() {
        }.getType();

        List<PersonEntity> users;

        Packet packet = new Packet("New Stage");
        String jsonSent = gson.toJson(packet);
        String jsonReceived = client.sendMessageToServer("New Stage", jsonSent);

        users = gson.fromJson(jsonReceived, usersType);

        if (users.size() > 0) {
            mainController.createWarningAlert("Not all users have introduced their score!");
        }
        else {
            mainController.createInfoAlert("A new stage was created!");
        }
    }

    public void onRankingTeams() throws Exception {
        MainController.rankingTeams(tableViewComp);
    }

    public void onRankingCompetitors() throws Exception {
        MainController.rankingCompetitors(tableViewComp);
    }

    public void addTeam() throws IOException {
        TeamEntity teamEntity = new TeamEntity();
        teamEntity.setName(teamField.getText());

        String jsonSent = gson.toJson(teamEntity);
        String jsonReceived = client.sendMessageToServer("Create team", jsonSent);

        String message = gson.fromJson(jsonReceived, String.class);
        mainController.createInfoAlert(message);
    }

    public void onDeleteTeam() throws IOException {
        String jsonSent = gson.toJson(teamField.getText());
        String jsonReceived = client.sendMessageToServer("Delete team", jsonSent);

        String message = gson.fromJson(jsonReceived, String.class);
        mainController.createInfoAlert(message);
    }
}
