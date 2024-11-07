package io.github.jmgarridopaz.bluezone.driver.forcheckingcars.cli;

import io.github.jmgarridopaz.bluezone.hexagon.ports.driving.forcheckingcars.ForCheckingCars;
import io.github.jmgarridopaz.bluezone.hexagon.ports.driving.forconfiguringapp.ForConfiguringApp;

public enum SystemUnderTest {

    INSTANCE;

    private ForCheckingCars carChecker;
    private ForConfiguringApp appConfigurator;

    public static SystemUnderTest instance() {
        return SystemUnderTest.INSTANCE;
    }

    public void setCarChecker(ForCheckingCars carChecker) {
        this.carChecker = carChecker;
    }

    public void setAppConfigurator(ForConfiguringApp appConfigurator) {
        this.appConfigurator = appConfigurator;
    }

    public ForConfiguringApp appConfigurator() {
        return this.appConfigurator;
    }

    public ForCheckingCars carChecker() {
        return this.carChecker;
    }
}
