package JavaFX;

import Client.Client;
import Packet.*;
import com.google.gson.Gson;
import database.model.ParticipationEntity;
import database.model.PersonEntity;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CompetitorController {

    @FXML
    private CheckBox confirmCheck;
    @FXML
    private CheckBox notConfirmCheck;
    @FXML
    private TextField scoreField;
    @FXML
    private TableView tableViewComp;

    private static PersonEntity loggedPerson;

    public static PersonEntity getLoggedPerson() {
        return loggedPerson;
    }

    public static void setLoggedPerson(PersonEntity loggedPersonOther) {
        loggedPerson = loggedPersonOther;
    }

    private Client client;
    private MainController mainController;
    private Gson gson;

    public void initialize() {

        mainController = new MainController();
        client = new Client();
        gson = new Gson();
    }

    public void onConfirm() throws Exception {

        ParticipationEntity participationEntity = new ParticipationEntity();

        if (confirmCheck.isSelected()) {
            participationEntity.setParticipating(true);
        }
        else if (notConfirmCheck.isSelected()) {
            participationEntity.setParticipating(false);
        }
        participationEntity.setIdPerson(loggedPerson.getId());
        if (scoreField.getText().equals("")) {
            scoreField.setText("0");
        }
        participationEntity.setPoints(Integer.parseInt(scoreField.getText()));

        String jsonSent = gson.toJson(participationEntity);
        String jsonReceived = client.sendMessageToServer("Register stage", jsonSent);

        Packet packet = gson.fromJson(jsonReceived, Packet.class);

        if (packet.message.equals("You cannot participate again in this stage. Wait for admin to create a new stage!")) {
            mainController.createWarningAlert(packet.message);
        } else {
            mainController.createInfoAlert(packet.message);
        }

    }

    public void onNotificationButton() throws Exception {

        String jsonSent = gson.toJson(loggedPerson);
        String jsonReceived = client.sendMessageToServer("Notification", jsonSent);

        Packet packet = gson.fromJson(jsonReceived, Packet.class);
        mainController.createWarningAlert(packet.message);

    }

    public void onUpdate() throws Exception {

        ParticipationEntity participationEntity = new ParticipationEntity();

        if (confirmCheck.isSelected()) {
            participationEntity.setParticipating(true);
        }
        else if (notConfirmCheck.isSelected()) {
            participationEntity.setParticipating(false);
        }
        participationEntity.setIdPerson(loggedPerson.getId());
        if (scoreField.getText().equals("")) {
            scoreField.setText("0");
        }
        participationEntity.setPoints(Integer.parseInt(scoreField.getText()));

        String jsonSent = gson.toJson(participationEntity);
        String jsonReceived = client.sendMessageToServer("Update", jsonSent);

        Packet packet = gson.fromJson(jsonReceived, Packet.class);

        mainController.createInfoAlert(packet.message);
    }

    public void onRankingTeams() throws Exception {
        MainController.rankingTeams(tableViewComp);
    }

    public void onRankingCompetitors() throws Exception {
        MainController.rankingCompetitors(tableViewComp);
    }

    public void switchOffComp() {
        if (confirmCheck.isSelected()) {
            notConfirmCheck.setDisable(true);
        }
        else {
            notConfirmCheck.setDisable(false);
        }
        if (notConfirmCheck.isSelected()){
            confirmCheck.setDisable(true);
            scoreField.setDisable(true);
        }
        else {
            confirmCheck.setDisable(false);
            scoreField.setDisable(false);
        }
    }
}
