package de.simonmeusel;

import java.io.*;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws Exception {
        Arguments arguments = new Arguments(args);

        File input =  arguments.getFile();

        if (!input.exists()) {
            // TODO: throw error
            System.exit(1);
        }

        String name = input.getName().split("\\.")[0].replaceAll(" ", "");
        File parent = input.getParentFile();

        File data = new File(parent, "data_" + name);
        data.mkdir();
        File advancements = new File(new File(data, "advancements"), "mccamtools");
        advancements.mkdirs();
        File functions = new File(new File(data, "functions"), "mccamtools");
        functions.mkdirs();

        BufferedReader br = new BufferedReader(new FileReader(input));

        double lx = 0;
        double ly = 0;
        double lz = 0;

        String line;
        int i = 0;
        while ((line = br.readLine()) != null) {
            // Create command
            StringTokenizer st = new StringTokenizer(line);
            double x = Double.parseDouble(st.nextToken());
            double y = Double.parseDouble(st.nextToken());
            double z = Double.parseDouble(st.nextToken());
            float pitch = Float.parseFloat(st.nextToken());
            float yaw = Float.parseFloat(st.nextToken());

            if (arguments.relative) {
                if (i == 0) {
                    lx = x;
                    ly = y;
                    lz = z;
                    x = 0;
                    y = 0;
                    z = 0;
                } else {
                    double nx = x - lx;
                    double ny = y - ly;
                    double nz = z - lz;
                    lx = x;
                    ly = y;
                    lz = z;
                    x = nx;
                    y = ny;
                    z = nz;
                }
            }

            String command = arguments.getCommand();
            command = command.replaceAll("%pitch", "" + pitch);
            command = command.replaceAll("%yaw", "" + yaw);
            command = command.replaceAll("%x", "" + x);
            command = command.replaceAll("%y", "" + y);
            command = command.replaceAll("%z", "" + z);
            command = command.replaceAll("%i", "" + i);
            command = command.replaceAll("\\\\n", "\n");

            // Create function
            createFunction(functions, name, command, i);
            // Create advancement
            createAdvancement(advancements, name, command, i);

            i++;
        }

        br.close();
    }

    private static void createFunction(File functions, String name, String command, int i) throws IOException {
        File function = new File(functions, name + i + ".mcfunction");
        BufferedWriter bw = new BufferedWriter(new FileWriter(function));

        bw.write(command + "\n");
        bw.write("advancement revoke @s only mccamtools:" + name + (i + 1));

        bw.close();
    }

    private static void createAdvancement(File advancements, String name, String command, int i) throws IOException {
        File advancement = new File(advancements, name + i + ".json");
        BufferedWriter bw = new BufferedWriter(new FileWriter(advancement));

        bw.write("{\"criteria\":{\"example\":{\"trigger\":\"minecraft:tick\"}}," +
                "\"rewards\":{\"function\":\"mccamtools:" + name + i + "\"}}");

        bw.close();
    }
}
