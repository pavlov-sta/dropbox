package server;

import java.util.HashMap;

public class ClientAuth {
    private static HashMap<String, String> user = new HashMap<>();

    static  {
        user.put("admin", "123");
        user.put("admin1", "123");
        user.put("admin3", "123");
    }

    public static boolean authUser(String username, String password) {
        return password.equals(user.get(username));
    }
}
