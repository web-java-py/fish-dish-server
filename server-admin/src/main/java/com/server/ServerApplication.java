package com.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动程序
 *
 */
@SpringBootApplication
public class ServerApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ServerApplication.class, args);
        System.out.println(
                "                                          \n" +
                "                                          \n" +
                "  ___    ___   _ __  __   __   ___   _ __ \n" +
                " / __|  / _ \\ | '__| \\ \\ / /  / _ \\ | '__|\n" +
                " \\__ \\ |  __/ | |     \\ V /  |  __/ | |   \n" +
                " |___/  \\___| |_|      \\_/    \\___| |_|   \n" +
                "                                          \n" +
                "                                          ");
    }
}