import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import entities.Commit;
import entities.Event;

public class GitHubActivityCLI {
    private static final String API_URL = "https://api.github.com/users/%s/events";

    private static String fecthGitHubActivity(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("error al obtener respuesta de la api de github: " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();

    }

    private static void imprimir(List<Event> eventos) {
        for (Event e : eventos) {
            System.out.println("--------evento-----------");
            System.out.println(e.getId());
            System.out.println(e.getType());
            System.out.println(e.isPublic());
            System.out.println(e.getCreatedAt());
            System.out.println("---------actor----------");
            System.out.println(e.getActor().getId());
            System.out.println(e.getActor().getLogin());
            System.out.println(e.getActor().getDisplayLogin());
            System.out.println(e.getActor().getGravatarId());
            System.out.println(e.getActor().getUrl());
            System.out.println(e.getActor().getAvatarUrl());
            System.out.println("---------repo----------");
            System.out.println(e.getRepo().getId());
            System.out.println(e.getRepo().getName());
            System.out.println(e.getRepo().getUrl());
            System.out.println("--------------payload----------------");
            System.out.println(e.getPayload().getRef());
            System.out.println(e.getPayload().getRefType());
            System.out.println(e.getPayload().getMasterBranch());
            System.out.println(e.getPayload().getDescription());
            System.out.println(e.getPayload().getPusherType());
            List<Commit> commits = e.getPayload().getCommits();
            System.out.println("numero de commits-> " + commits.size());
            for (Commit c : commits) {
                System.out.println("------commit----------");
                System.out.println(c.getSha());
                System.out.println(c.getMessage());
                System.out.println(c.isDistinct());
                System.out.println(c.getUrl());
                System.out.println("-------author---------");
                System.out.println(c.getAuthor().getEmail());
                System.out.println(c.getAuthor().getName());
            }
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("ingresa numero de usuario que interese buscar");
        String username = sc.nextLine();

        if (username.trim().isEmpty()) {
            System.out.println("el nombre de usuario no puede estar vacio.");
            sc.close();
            return;
        }

        String url = String.format(API_URL, username);

        try {
            System.out.println("url--> " + url);

            String jsonResponse = fecthGitHubActivity(url);

            List<Event> eventos = new ReaderJson().jsonParse(jsonResponse);
            imprimir(eventos);

        } catch (Exception e) {
            System.err.println("error al obtener actividad: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}
