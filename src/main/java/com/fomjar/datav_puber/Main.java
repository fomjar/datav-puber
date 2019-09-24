package com.fomjar.datav_puber;

public class Main {

    public static void main(String[] args) {
        String driver   = null;
        String url      = null;
        boolean papi    = false;
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "driver":
                case "-driver":
                case "--driver":
                    driver = args[++i];
                    break;
                case "url":
                case "-url":
                case "--url":
                    url = args[++i];
                    break;
                case "papi":
                case "-papi":
                case "--papi":
                    papi = true;
                    break;
                default:
                    break;
            }
        }

        if (null == driver || null == url) {
            printUsage();
            return;
        }

        Puber puber = null;
        try {
            puber = new Puber(driver);
            puber.url(url);
            if (papi) {
                System.out.println("============= PAPI start =============");
                puber.papi();
                System.out.println("============= PAPI done  =============");
            }
        } finally { puber.quit(); }
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("\t\tjava -jar datav_puber.jar -driver \"drivers/chromedriver\" -url \"http://datav-screen/\" -papi");
    }

}
