package com.bakuard.simpleCrud;

import com.bakuard.simpleCrud.conf.DIContainer;

public class Main {

    public static void main(String[] args) {
        DIContainer diContainer = new DIContainer("application.properties");
        diContainer.webServer().start();
    }

}