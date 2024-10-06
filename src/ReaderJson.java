import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import entities.Actor;
import entities.Author;
import entities.Commit;
import entities.Event;
import entities.Payload;
import entities.Repo;

public class ReaderJson {

    public List<Event> jsonParse(String jsonString) {
        List<Event> eventos = new ArrayList<>();
        Stack<Character> stack = new Stack<>();
        StringBuilder currentObject = new StringBuilder();
        char[] chars = jsonString.toCharArray();
        for (char c : chars) {
            if (c == '{') {
                stack.push(c);
            }
            if (!stack.isEmpty()) {
                currentObject.append(c);
            }
            if (c == '}') {
                stack.pop();
                if (stack.isEmpty()) {
                    eventos.add(formatearEvento(currentObject.toString()));

                    currentObject.setLength(0);// limpiamos
                }
            }
        }
        // imprimimos
        return eventos;
    }

    public Event formatearEvento(String eventString) {
        Event e = new Event();

        Stack<Character> stack = new Stack<>();

        StringBuilder currentObject = new StringBuilder();

        // quitamos las { } del principio y final
        eventString = eventString.trim().substring(1, eventString.length() - 1);

        char[] chars = eventString.toCharArray();
        StringBuilder builderStr = new StringBuilder();
        List<String> objetos = new ArrayList<>();

        for (char c : chars) {

            if (c == '{') {
                stack.push(c);
            }
            if (!stack.isEmpty()) {
                currentObject.append(c);
            }
            if (c == '}') {
                stack.pop();
                if (stack.isEmpty()) {
                    objetos.add(currentObject.toString());
                    currentObject.setLength(0);// limpiamos
                }
            }
            if (stack.isEmpty()) {
                builderStr.append(c);
            }
        }

        String[] keyvalues = builderStr.toString().split(",");
        for (String s : keyvalues) {
            String key = s.split(":")[0].trim();
            String value;
            if (s.split(":").length > 1) {
                value = s.split(":")[1].trim();
            } else {
                value = "";
            }
            // quitamos comillas ""
            key = key.replace("\"", "");
            switch (key) {
                case "id":
                    e.setId(value);
                    break;
                case "type":
                    e.setType(value);
                    break;
                case "public":
                    e.setPublic(Boolean.parseBoolean(value));
                    break;
                case "created_at":
                    e.setCreatedAt(value.replace("\"", ""));
                    break;
                default:
                    // System.out.println("clave desconocida en envent " + value);
                    break;
            }

        }

        Actor actor;
        Repo repo;
        Payload payload;

        actor = formatearActor(objetos.get(0));
        repo = formatearRepo(objetos.get(1));
        payload = formatearPayload(objetos.get(2));

        e.setActor(actor);
        e.setRepo(repo);
        e.setPayload(payload);
        return e;
    }

    private Actor formatearActor(String actorString) {
        
        // revisar las url y escapar las // porque no las toma despues de split(":")
        Actor a = new Actor();
        actorString = actorString.substring(1, actorString.length() - 1);

        // no tiene objetos anidados
        String[] keyvalue = actorString.split(",");
        for (String s : keyvalue) {
            String key = s.split(":")[0].trim();
            String value;
            if (s.split(":").length > 1) {
                value = s.split(":")[1].trim();
            } else {
                value = "";
            }
            // quitamos comillas ""
            key = key.replace("\"", "");
            switch (key) {
                case "id":
                    a.setId(Integer.parseInt(value));
                    break;
                case "login":
                    a.setLogin(value);
                    break;
                case "display_login":
                    a.setDisplayLogin(value);
                    break;
                case "gravatar_id":
                    a.setGravatarId(value);
                    break;
                case "url":
                    a.setUrl(value);
                    break;
                case "avatar_url":
                    a.setAvatarUrl(value);
                    break;
                default:
                    System.out.println("clave desconocida en actor " + value);
                    break;
            }

        }

        return a;
    }

    private Repo formatearRepo(String repoString) {
        Repo r = new Repo();
        repoString = repoString.substring(1, repoString.length() - 1);
        String[] keyvalues = repoString.split(",");

        for (String k : keyvalues) {
            String key = k.split(":")[0].trim();
            String value;
            if (k.split(":").length > 1) {
                value = k.split(":")[1].trim();
            } else {
                value = "";
            }
            key = key.replace("\"", "");
            switch (key) {
                case "id":
                    r.setId(Integer.parseInt(value));
                    break;
                case "name":
                    r.setName(value);
                    break;
                case "url":
                    r.setUrl(value);
                    break;
                default:
                    System.out.println("clave desconocida de repo");
                    break;
            }
        }

        return r;
    }

    private Payload formatearPayload(String payloadString) {
        Payload p = new Payload();
        List<Commit> commits = new ArrayList<>();
        Stack<Character> stack = new Stack<>();

        StringBuilder currentObject = new StringBuilder();
        // quitamos las { } del principio y final
        payloadString = payloadString.trim().substring(1, payloadString.length() - 1);
        char[] chars = payloadString.toCharArray();
        StringBuilder builderStr = new StringBuilder();
        for (char c : chars) {
            if (c == '{') {
                stack.push(c);
            }
            if (!stack.isEmpty()) {
                currentObject.append(c);
            }
            if (c == '}') {
                stack.pop();
                if (stack.isEmpty()) {
                    commits.add(formatearCommit(currentObject.toString()));
                    currentObject.setLength(0);// limpiamos
                }
            }
            if (stack.isEmpty()) {
                builderStr.append(c);
            }
        }

        String[] keyvalues = builderStr.toString().split(",");
        for (String s : keyvalues) {
            String key = s.split(":")[0].trim();
            String value;
            if (s.split(":").length > 1) {
                value = s.split(":")[1].trim();
            } else {
                value = "";
            }
            // quitamos comillas ""
            key = key.replace("\"", "");
            switch (key) {
                case "ref":
                    p.setRef(value);
                    break;
                case "ref_type":
                    p.setRef(value);
                    break;
                case "master_branch":
                    p.setMasterBranch(value);
                    break;
                case "description":
                    p.setDescription(value);
                    break;
                case "pusher_type":
                    p.setPusherType(value);
                    break;
                default:
                    // System.out.println("clave desconocida en envent " + value);
                    break;
            }

        }

        // agregar los commit a la cosa
        p.setCommits(commits);
        return p;
    }

    private Commit formatearCommit(String commitString) {
        Commit commit = new Commit();

        Stack<Character> stack = new Stack<>();
        StringBuilder currentObject = new StringBuilder();
        // quitamos las { } del principio y final
        commitString = commitString.trim().substring(1, commitString.length() - 1);
        char[] chars = commitString.toCharArray();
        StringBuilder builderStr = new StringBuilder();
        for (char c : chars) {
            if (c == '{') {
                stack.push(c);
            }
            if (!stack.isEmpty()) {
                currentObject.append(c);
            }
            if (c == '}') {
                stack.pop();
                if (stack.isEmpty()) {
                    Author author = formatearAuthor(currentObject.toString());
                    commit.setAuthor(author);
                    currentObject.setLength(0);// limpiamos

                }
            }
            if (stack.isEmpty()) {
                builderStr.append(c);
            }
        }
        String[] keyvalues = builderStr.toString().split(",");
        for (String s : keyvalues) {
            String key = s.split(":")[0].trim();
            String value;
            if (s.split(":").length > 1) {
                value = s.split(":")[1].trim();
            } else {
                value = "";
            }

            // quitamos comillas ""
            key = key.replace("\"", "");

            switch (key) {
                case "sha":
                    commit.setSha(value);
                    break;
                case "message":
                    commit.setMessage(value);
                    break;
                case "distinc":
                    commit.setDistinct(Boolean.parseBoolean(value));
                    break;
                case "url":
                    commit.setUrl(value);
                default:
                    // System.out.println("clave desconocida en commit " + value);
                    break;
            }

        }

        return commit;
    }

    private Author formatearAuthor(String autorString) {
        Author a = new Author();

        autorString = autorString.substring(1, autorString.length() - 1);
        String[] keyvalues = autorString.split(",");

        for (String k : keyvalues) {
            String key = k.split(":")[0].trim();
            String value;
            if (k.split(":").length > 1) {
                value = k.split(":")[1].trim();
            } else {
                value = "";
            }
            key = key.replace("\"", "");
            switch (key) {
                case "email":
                    a.setEmail(value);
                    break;
                case "name":
                    a.setName(value);
                    break;
                default:
                    System.out.println("clave desconocida de author");
                    break;
            }
        }
        return a;
    }
}
