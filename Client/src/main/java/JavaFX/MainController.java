package JavaFX;

import Client.Client;
import Packet.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import database.model.PersonEntity;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private CheckBox adminCheckbox;
    @FXML
    private CheckBox competitorCheckbox;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField teamField;

    private static Client client;

    private static Gson gson;

    public void initialize() {

        client = new Client();
        gson = new Gson();
    }

    public void onSaveButton(javafx.event.ActionEvent actionEvent) throws Exception {

        Parent root = null;
        Boolean type = false;
        String message = null;
        List<PersonEntity> list;

        Type listObjects = new TypeToken<List<PersonEntity>>() {
        }.getType();

        if (adminCheckbox.isSelected()) {
            root = FXMLLoader.load(getClass().getResource("/fxml/admin.fxml"));
            type = true;
            message = "Admin";
        }

        if (competitorCheckbox.isSelected()) {
            root = FXMLLoader.load(getClass().getResource("/fxml/competitor.fxml"));
            message = "Competitor";
        }

        PersonEntity personEntity = new PersonEntity();
        personEntity.setIdTeam(Integer.parseInt(teamField.getText()));
        personEntity.setType(type);
        personEntity.setUsername(usernameField.getText());

        String jsonSent = gson.toJson(personEntity);
        String jsonReceived = client.sendMessageToServer(message, jsonSent);


        list = gson.fromJson(jsonReceived, listObjects);
        System.out.println(list);

        if (list.get(0).getUsername().equals("Username Exist!")) {
            createInfoAlert(list.get(0).getUsername());

        } else {

            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            CompetitorController.setLoggedPerson(list.get(1));
        }

    }

    public void onLogin(javafx.event.ActionEvent actionEvent) throws Exception {

        String username = usernameField.getText();

        String jsonSent = gson.toJson(username);
        String jsonReceived = client.sendMessageToServer("Login", jsonSent);

        List<PersonEntity> list;

        Type listObjects = new TypeToken<List<PersonEntity>>() {
        }.getType();

        list = gson.fromJson(jsonReceived, listObjects);

        if (list.get(0).getUsername().equals("Username not exist")) {
            createInfoAlert(list.get(0).getUsername());
        }
        else {
            if (list.get(1).isType()) {
                root = FXMLLoader.load(getClass().getResource("/fxml/admin.fxml"));
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            else {
                root = FXMLLoader.load(getClass().getResource("/fxml/competitor.fxml"));
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }

            CompetitorController.setLoggedPerson(list.get(1));
        }

    }

    public static void rankingTeams(TableView tableView) throws Exception {
        Packet packet = new Packet("LeaderboardTeams");


        String jsonSent = gson.toJson(packet);
        String jsonReceived = client.sendMessageToServer("LeaderboardTeams", jsonSent);

        Type leaderboardType = new TypeToken<List<Leaderboard>>() {
        }.getType();

        List<Leaderboard> leaderboards;

        leaderboards = gson.fromJson(jsonReceived, leaderboardType);

        setTableView(tableView, leaderboards, "Teams");
    }

    public static void rankingCompetitors(TableView tableView) throws Exception {

        Packet packet = new Packet("LeaderboardCompetitors");
        String jsonSent = gson.toJson(packet);
        String jsonReceived = client.sendMessageToServer("LeaderboardCompetitors", jsonSent);

        Type leaderboardType = new TypeToken<List<Leaderboard>>() {
        }.getType();

        List<Leaderboard> leaderboards;

        leaderboards = gson.fromJson(jsonReceived, leaderboardType);

        setTableView(tableView, leaderboards, "Competitors");
    }

    public static void setTableView(TableView tableView, List<Leaderboard> leaderboardList, String nameLabel) throws IOException {

        TableColumn<Leaderboard, Long> stageColumn = new TableColumn<Leaderboard, Long>("Stage");
        stageColumn.setCellValueFactory(new PropertyValueFactory<Leaderboard, Long>("id"));

        TableColumn<Leaderboard, String> nameColumn = new TableColumn<Leaderboard, String>(nameLabel);
        nameColumn.setCellValueFactory(new PropertyValueFactory<Leaderboard, String>("name"));

        TableColumn<Leaderboard, Integer> pointsColumn = new TableColumn<Leaderboard, Integer>("Points");
        pointsColumn.setCellValueFactory(new PropertyValueFactory<Leaderboard, Integer>("points"));

        ArrayList<TableColumn> columns = new ArrayList<>();

        columns.add(stageColumn);
        columns.add(nameColumn);
        columns.add(pointsColumn);

        tableView.getColumns().setAll(columns);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableView.getItems().setAll(leaderboardList);

    }

    public void switchOff() {
        if (adminCheckbox.isSelected()) {
            competitorCheckbox.setDisable(true);
        }
        else {
            competitorCheckbox.setDisable(false);
        }
        if (competitorCheckbox.isSelected()){
            adminCheckbox.setDisable(true);
        }
        else {
            adminCheckbox.setDisable(false);
        }
    }

    public void createInfoAlert(String errorMessage) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setContentText(errorMessage);
        alert.showAndWait();
        if (!alert.isShowing()) {
            alert.close();
        }
    }

    public void createWarningAlert(String errorMessage) throws IOException {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setContentText(errorMessage);
        alert.showAndWait();
        if (!alert.isShowing()) {
            alert.close();
        }
    }

}
