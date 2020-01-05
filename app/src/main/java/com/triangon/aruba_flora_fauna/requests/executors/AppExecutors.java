package com.triangon.aruba_flora_fauna.requests.executors;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutors {

    private static AppExecutors instance;

    public static AppExecutors getInstance() {
        if(instance == null) {
            instance = new AppExecutors();
        }

        return instance;
    }

    private final ScheduledExecutorService mNetowrokIO = Executors.newScheduledThreadPool(3);

    public ScheduledExecutorService networkIO() {
        return mNetowrokIO;
    }

}
