package Server;
public class Main {

    private static void init() {
        var env = System.getenv();
        var ip = env.get("IP");
        var port = env.get("PORT");
        var user = env.get("USER");
        var os = env.get("OS");
        String operatingSystem = System.getenv("OS");
        if (operatingSystem.equals("linux")) {
            System.out.println("Linux detected");
        } else if (operatingSystem.equals("windows")) {
            System.out.println("Windows detected");
        } else if (operatingSystem.equals("mac")) {
            System.out.println("Mac detected");
        } else {
            System.out.println("Unknown OS");
        }
    }

    public static void main(String[] args) {
        init();
    }

    public static void SendNotif() { }

    public static void readDb() {};
}
