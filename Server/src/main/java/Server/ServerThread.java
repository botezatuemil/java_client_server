package Server;

import Packet.*;
import database.dao.ParticipationDao;
import database.dao.PersonDao;
import database.dao.StageDao;
import database.dao.TeamDao;
import database.model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.*;

import com.google.gson.Gson;

public class ServerThread extends Thread {
    private Socket socket = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private Gson gson;

    private static Set<PersonEntity> connectedUsers = new HashSet<>();
    private static List<PersonEntity> usersNotify = new ArrayList<>();

    public ServerThread(Socket socket) {
        this.socket = socket;
        this.gson = new Gson();
        try {
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {

            String message = (String) this.in.readObject();
            String jsonReceived = (String) this.in.readObject();
            execute(message, jsonReceived);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String checkUsername(String username) {

        PersonDao personDao = new PersonDao();
        boolean exist = false;
        String serverMessage = "Username not exist!";

        List<PersonEntity> personsEntity = personDao.getAll();

        for(PersonEntity person : personsEntity) {
            if (person.getUsername().equals(username)) {
                serverMessage = "Username Exist!";
                break;
            }
        }

        return serverMessage;
    }

    public boolean isConnected(Long id) {
        for (var users : connectedUsers) {
            if (id.equals(users.getId())) {
                return true;
            }
        }
        return false;
    }

    private void execute(String message, String jsonReceived) throws IOException {

        String serverMessage = null;
        Packet packet = null;
        PersonDao personDao = new PersonDao();
        TeamDao teamDao = new TeamDao();
        ParticipationDao participationDao = new ParticipationDao();
        StageDao stageDao = new StageDao();
        List<PersonEntity> list = new ArrayList<>();
        PersonEntity personMessage = new PersonEntity();
        String jsonSent;

        switch (message) {
            case "Admin", "Competitor":

                PersonEntity personEntity = gson.fromJson(jsonReceived, PersonEntity.class);
                packet = new Packet(checkUsername(personEntity.getUsername()));

                if (!packet.message.equals("Username Exist!")) {
                    personDao.create(personEntity);
                    if (!personEntity.isType()) {
                        connectedUsers.add(personEntity);
                    }
                }

                PersonEntity personLogged = personDao.getType(personEntity.getUsername());
                personMessage.setUsername(packet.message);

                list.add(personMessage);
                list.add(personLogged);
                jsonSent = gson.toJson(list);
                this.out.writeObject(packet.message);
                this.out.writeObject(jsonSent);

                break;

            case "Login":

                String loginMessage;
                String username = gson.fromJson(jsonReceived, (Type) String.class);
                PersonEntity personLogged1 = null;

                try {
                    personLogged1 = personDao.getType(username);
                    if (!personLogged1.isType()) {
                        connectedUsers.add(personLogged1);
                    }
                    loginMessage = "Username Exist!";
                } catch (Exception e) {
                    loginMessage = "Username not exist";

                }
                personMessage.setUsername(loginMessage);


                list.add(personMessage);
                list.add(personLogged1);

                jsonSent = gson.toJson(list);
                this.out.writeObject(loginMessage);
                this.out.writeObject(jsonSent);

                break;
            case "Validate":
                List<TeamEntity> teamsEntity = teamDao.getAll();
                List<TeamEntity>teams = new ArrayList<>();

                for (TeamEntity teamEntity : teamsEntity) {
                    int counter = 0;

                    List<PersonEntity> personsEntity = personDao.getAll();

                    for(PersonEntity person : personsEntity) {
                        if (person.getIdTeam() == teamEntity.getId()) {
                            counter++;
                        }
                    }

                    System.out.println(counter);
                    if (counter < 2 || counter > 5) {
                        teams.add(teamEntity);
                    }
                }
                packet = new Packet("LIST");
                jsonSent = gson.toJson(teams);
                this.out.writeObject(packet.message);
                this.out.writeObject(jsonSent);

                break;
            case "Start competition":
                StageEntity stageEntity = new StageEntity();
                stageEntity.setName("Stage");
                stageDao.create(stageEntity);


                packet = new Packet("Competition begins");
                jsonSent = gson.toJson(packet);
                this.out.writeObject(packet.message);
                this.out.writeObject(jsonSent);
                break;

            case "Register stage":

                ParticipationEntity currentStage = gson.fromJson(jsonReceived, ParticipationEntity.class);
                ParticipationEntity maxStage;
                String stageMessage;

                var listStages = stageDao.getAll();
                long maxId = -1;
                for (var listStageMax : listStages) {
                    if (listStageMax.getId() > maxId) {
                        maxId = listStageMax.getId();
                    }
                }

                currentStage.setId(maxId);

                try {
                    currentStage.setId(maxId);
                    participationDao.create(currentStage);
                    stageMessage = "You are participating in this stage!";

                } catch (Exception e) {
                    stageMessage = "You cannot participate again in this stage. Wait for admin to create a new stage!";
                }

                packet = new Packet(stageMessage);
                jsonSent = gson.toJson(packet);
                this.out.writeObject(stageMessage);
                this.out.writeObject(jsonSent);

                break;

            case "New Stage":

                boolean userToNotify = false;
                String messageNotif;
                usersNotify = new ArrayList<>();

                var listStageMax = stageDao.getAll();
                long maxIdStage = listStageMax.get(listStageMax.size() - 1).getId();

                var listParticipationMax = participationDao.getAll();
                long maxIdParticipation = listParticipationMax.get(listParticipationMax.size() - 1).getId();
                System.out.println(maxIdStage);
                System.out.println(maxIdParticipation);

                for (var participations : participationDao.getAll()) {
                    if (participations.getParticipating() == true && isConnected(participations.getIdPerson())) {
                        if (participations.getPoints().equals(0)) {
                            usersNotify.add(personDao.get(participations.getIdPerson()));
                            userToNotify = true;
                        }
                    }
                }

                if (userToNotify == false) {
                    StageEntity stageEntity1 = new StageEntity();
                    stageEntity1.setName("Stage");
                    stageDao.create(stageEntity1);
                    messageNotif = "Stage was created!";
                }
                else {
                    messageNotif = "LIST ";
                }
                jsonSent = gson.toJson(usersNotify);
                this.out.writeObject(messageNotif);
                this.out.writeObject(jsonSent);

                break;
            case "Notification":
                PersonEntity person = gson.fromJson(jsonReceived, PersonEntity.class);
                String notification = "No notifications";

                for (var persons : usersNotify) {
                    if (person.equals(persons)) {
                        notification = "Please input your score";
                    }
                }

                packet = new Packet(notification);
                jsonSent = gson.toJson(packet);
                this.out.writeObject(notification);
                this.out.writeObject(jsonSent);
                break;

            case "Update":
                ParticipationEntity updateEntity = gson.fromJson(jsonReceived, ParticipationEntity.class);

                var listStages1 = stageDao.getAll();
                long maxId1 = listStages1.get(listStages1.size() - 1).getId();
                updateEntity.setId(maxId1);
                participationDao.update(updateEntity);

                packet = new Packet("Score updated");
                jsonSent = gson.toJson(packet);
                this.out.writeObject("Score Updated!");
                this.out.writeObject(jsonSent);
                break;

            case "LeaderboardCompetitors":

                List<Leaderboard> leaderboards = new ArrayList<>();

                for (var stage : stageDao.getAll()) {
                    try {
                        List<ParticipationEntity> listParticipation = participationDao.getParticipationsAfterStage(stage.getId());


                        for (var participation : listParticipation) {
                            if (participation.getParticipating() == true) {
                                Leaderboard leaderboard = new Leaderboard();
                                leaderboard.setId(participation.getId());
                                leaderboard.setPoints(participation.getPoints());
                                leaderboard.setName(personDao.get(participation.getIdPerson()).getUsername());
                                System.out.println(participation.getPoints());
                                leaderboards.add(leaderboard);
                            }
                        }
                    }
                    catch (Exception e) {
                        System.out.println(e);
                    }
                }

                jsonSent = gson.toJson(leaderboards);
                this.out.writeObject("LIST");
                this.out.writeObject(jsonSent);
                break;

            case "LeaderboardTeams":

                List<Leaderboard> leaderboardsTeam = new ArrayList<>();

                for (var stage : stageDao.getAll()) {

                    for (var team : teamDao.getAll()) {

                        Long score = participationDao.getScoreOfATeamAtAStage(stage.getId(), team.getId());

                        if (score != null) {
                            Leaderboard leaderboardTeam = new Leaderboard();
                            leaderboardTeam.setId(stage.getId());
                            leaderboardTeam.setPoints(score.intValue());
                            leaderboardTeam.setName(team.getName());
                            leaderboardsTeam.add(leaderboardTeam);
                            System.out.println(score);
                        }
                    }

                }

                jsonSent = gson.toJson(leaderboardsTeam);
                this.out.writeObject("LIST");
                this.out.writeObject(jsonSent);
                break;

            case "Create team":
                TeamEntity teamEntity = gson.fromJson(jsonReceived, TeamEntity.class);
                List<TeamEntity> teamEntities = teamDao.getAll();
                teamEntity.setId(teamEntities.get(teamEntities.size() - 1).getId());
                teamDao.create(teamEntity);

                jsonSent = gson.toJson("A new team was created!");
                this.out.writeObject("A new Team was created");
                this.out.writeObject(jsonSent);
                break;

            case "Delete team":
                String messageTeam = gson.fromJson(jsonReceived, String.class);
                TeamEntity teamToDelete = teamDao.getTeamAfterName(messageTeam);

                List<PersonEntity> personEntities = personDao.getAll();

                for (PersonEntity personToDelete : personEntities) {
                    if (personToDelete.getIdTeam() == teamToDelete.getId()) {
                        personDao.delete(personToDelete);
                    }
                }

                try {
                    teamDao.delete(teamToDelete);
                } catch (Exception e) {

                }

                jsonSent = gson.toJson("Team " + messageTeam + " was deleted!");
                this.out.writeObject("Team deleted");
                this.out.writeObject(jsonSent);
                break;
            default:
                break;
        }
    }
}
